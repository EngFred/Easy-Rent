package com.kotlin.easyrent.features.tenantManagement.data.repository

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.rentalManagement.data.mapper.toEntity
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.data.mapper.toDomain
import com.kotlin.easyrent.features.tenantManagement.data.mapper.toEntity
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.modal.TenantStatus
import com.kotlin.easyrent.features.tenantManagement.domain.repository.TenantsRepository
import com.kotlin.easyrent.utils.Collections
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TenantsRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    cacheDatabase: CacheDatabase
) : TenantsRepository {

    companion object {
        private val TAG = TenantsRepositoryImpl::class.java.simpleName
    }

    private val tenantsDao = cacheDatabase.tenantsDao()
    private val rentalsDao = cacheDatabase.rentalsDao()

    override suspend fun upsertTenant(tenant: Tenant, rental: Rental, tenantStatus: TenantStatus): ServiceResponse<Unit> {
        return try {
            var rentalUpdatedInFirestore = false
            tenantsDao.upsertTenant(tenant.toEntity())
            val updatedRental = if(tenantStatus == TenantStatus.New) {
                Log.v(TAG, "Tenant just moved in...")
                rental.copy(noOfRooms = (rental.noOfRooms-1), isSynced = false)
            } else {
                Log.v(TAG, "Tenant already moved in")
                rental
            }
            rentalsDao.upsertRental(updatedRental.toEntity())
            firestore.runTransaction {
                val tenantDocument = it.get(firestore.collection(Collections.LANDLORDS)
                    .document(firebaseAuth.uid!!)
                    .collection(Collections.TENANTS)
                    .document(tenant.id))
                val rentalsDocument = it.get(firestore.collection(Collections.LANDLORDS)
                    .document(firebaseAuth.uid!!)
                    .collection(Collections.RENTALS)
                    .document(rental.id))

                it.set(
                    tenantDocument.reference,
                    tenant.copy(isSynced = true)
                )

                if ( rentalsDocument.exists() ) {
                    if ( tenantStatus == TenantStatus.New ) {
                        it.update(
                            rentalsDocument.reference,
                            "noOfRooms", updatedRental.noOfRooms
                        )
                        rentalUpdatedInFirestore = true
                    }
                }
            }.await()
            tenantsDao.upsertTenant(tenant.copy(isSynced = true).toEntity())
            if ( rentalUpdatedInFirestore ) {
                rentalsDao.upsertRental(updatedRental.copy(isSynced = true).toEntity())
            }
            ServiceResponse.Success(Unit)
        }catch (e: Exception) {
            Log.e(TAG, "$e.message")
            if ( e is FirebaseFirestoreException || e is FirebaseNetworkException) {
                Log.v(TAG, "Not synced!!")
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override fun getAllTenants(): Flow<ServiceResponse<List<Tenant>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            tenantsDao.getAllTenants().collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteTenant(tenant: Tenant, rental: Rental): ServiceResponse<Unit> {
        return try {
            var deletedFromFirestore = false
            tenantsDao.upsertTenant(tenant.copy(isDeleted = true).toEntity())
            //we need to increase the number of rooms available by one
            val updatedRental = rental.copy(noOfRooms = rental.noOfRooms+1, isSynced = false)
            rentalsDao.upsertRental(updatedRental.toEntity())
            firestore.runTransaction {
                val document = it.get(firestore.collection(Collections.LANDLORDS)
                    .document(firebaseAuth.uid!!)
                    .collection(Collections.TENANTS)
                    .document(tenant.id))

                if ( document.exists() ) {
                    it.delete(document.reference)
                    deletedFromFirestore = true
                }
            }.await()
            if ( deletedFromFirestore ){
                tenantsDao.deleteTenantById(tenant.id)
                updateRentalInFirestore(updatedRental) //increase the number of available rooms for the rental by one
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Success(Unit)
            }
        } catch(e: Exception) {
            Log.e(TAG, "$e.message")
            if ( e is FirebaseFirestoreException || e is FirebaseNetworkException) {
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    private suspend fun updateRentalInFirestore(updatedRental: Rental) {
        firestore.runTransaction {
            val document = it.get(firestore.collection(Collections.LANDLORDS)
                .document(firebaseAuth.uid!!)
                .collection(Collections.RENTALS)
                .document(updatedRental.id))

            if ( document.exists() ) {
                it.update(
                    document.reference,
                    "noOfRooms", updatedRental.noOfRooms
                )
            }
        }.await()
        rentalsDao.upsertRental(updatedRental.copy(isSynced = true).toEntity())
    }

    override suspend fun getTenantById(id: String): ServiceResponse<Tenant?> {
        return try {
            val rental = tenantsDao.getTenantById(id)
            ServiceResponse.Success(rental?.toDomain())
        }catch(e: Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override fun getAllUnsyncedTenants(): Flow<ServiceResponse<List<Tenant>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            tenantsDao.getAllUnsyncedTenants().collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override fun getAllDeletedTenants(): Flow<ServiceResponse<List<Tenant>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            tenantsDao.getAllDeletedTenants().collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }
}