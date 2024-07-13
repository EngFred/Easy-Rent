package com.kotlin.easyrent.features.paymentTracking.data.repository

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.paymentTracking.data.mapper.toDomain
import com.kotlin.easyrent.features.paymentTracking.data.mapper.toEntity
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.paymentTracking.domain.repository.PaymentRepository
import com.kotlin.easyrent.features.tenantManagement.data.mapper.toEntity
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
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

class PaymentsRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val cache: CacheDatabase
) : PaymentRepository {

    companion object {
        private val TAG = PaymentsRepositoryImpl::class.java.simpleName
    }

    private val tenantsDao = cache.tenantsDao()
    private val paymentsDao = cache.paymentsDao()

    override suspend fun savePayment(
        payment: Payment,
        tenant: Tenant
    ): ServiceResponse<Unit> {
        return try {
            var paymentUpdatedInFirestore = false
            val newBalance = tenant.balance - payment.amount
            if ( newBalance <= 0.toDouble() ) {
                ServiceResponse.Error(R.string.invalid_payment)
            }
            paymentsDao.savePayment(payment.toEntity())
            val updatedTenant = tenant.copy(balance = newBalance, isSynced = false)
            tenantsDao.upsertTenant(updatedTenant.toEntity())
            firestore.runTransaction {
                val paymentDocument = it.get(firestore.collection(Collections.LANDLORDS)
                    .document(firebaseAuth.uid!!)
                    .collection(Collections.PAYMENTS)
                    .document(payment.id))
                val tenantsDocument = it.get(firestore.collection(Collections.LANDLORDS)
                    .document(firebaseAuth.uid!!)
                    .collection(Collections.TENANTS)
                    .document(tenant.id))
                it.set(
                    paymentDocument.reference,
                    payment.copy(isSynced = true)
                )
                if ( tenantsDocument.exists() ) {
                    it.update(
                        tenantsDocument.reference,
                        "balance", updatedTenant.balance
                    )
                    paymentUpdatedInFirestore = true
                }
            }.await()
            tenantsDao.upsertTenant(updatedTenant.copy(isSynced = true).toEntity())
            if ( paymentUpdatedInFirestore ) {
                paymentsDao.savePayment(payment.copy(isSynced = true).toEntity())
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

    override fun getAllPayments(): Flow<ServiceResponse<List<Payment>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            paymentsDao.getAllPayments().collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override fun getPaymentsForRental(rentalId: String): Flow<ServiceResponse<List<Payment>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            paymentsDao.getRentalPayments(rentalId).collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override fun getPaymentsForTenant(tenantId: String): Flow<ServiceResponse<List<Payment>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            paymentsDao.getTenantPayments(tenantId).collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUnsyncedPayments(): ServiceResponse<List<Payment>> {
        return try {
            val unsyncedPayments = paymentsDao.getUnsyncedPayments()
            ServiceResponse.Success(unsyncedPayments.map { it.toDomain() })
        }catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun getDeletedPayments(): ServiceResponse<List<Payment>> {
        return try {
            val deletedPayments = paymentsDao.getDeletedPayments()
            ServiceResponse.Success(deletedPayments.map { it.toDomain() })
        }catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun getPaymentById(paymentId: String): ServiceResponse<Payment?> {
        return try {
            val payment = paymentsDao.getPaymentById(paymentId)
            ServiceResponse.Success(payment?.toDomain())
        }catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun deletePayment(
        payment: Payment
    ): ServiceResponse<Unit> {
        return try {
            paymentsDao.savePayment(payment.copy(isDeleted = true).toEntity())
            firestore.runTransaction {
                val document = it.get(firestore.collection(Collections.LANDLORDS)
                    .document(firebaseAuth.uid!!)
                    .collection(Collections.PAYMENTS)
                    .document(payment.id))

                if ( document.exists() ) {
                    it.delete(document.reference)
                }
            }.await()
            paymentsDao.deletePaymentById(payment.id)
            ServiceResponse.Success(Unit)
        } catch(e: Exception) {
            Log.e(TAG, "$e.message")
            if ( e is FirebaseFirestoreException || e is FirebaseNetworkException) {
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }
}