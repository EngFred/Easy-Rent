package com.kotlin.easyrent.features.rentalManagement.data.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.kotlin.easyrent.R
import com.kotlin.easyrent.features.rentalManagement.data.cache.CacheDatabase
import com.kotlin.easyrent.features.rentalManagement.data.mapper.toDomain
import com.kotlin.easyrent.features.rentalManagement.data.mapper.toEntity
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.rentalManagement.domain.repository.RentalsRepository
import com.kotlin.easyrent.utils.Collections
import com.kotlin.easyrent.utils.Constants
import com.kotlin.easyrent.utils.ServiceResponse
import com.kotlin.easyrent.utils.compressImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RentalsRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    cache: CacheDatabase,
    private val context: Context
) : RentalsRepository {

    companion object {
        private const val TAG = "RentalsRepositoryImpl"
    }
    
    private val dao = cache.rentalsDao()

    override suspend fun upsertRental(rental: Rental): ServiceResponse<Unit> {
        return try {
            Log.v(TAG, "Upserting...")
            if ( rental.image != null && !rental.image.contains("firebasestorage") ) {
                Log.v(TAG, "Compressing image...")
                val storageRef = firebaseStorage.reference.child("${Constants.RENTAL_PHOTOS}/${firebaseAuth.uid!!}/${rental.id}/${System.currentTimeMillis()}")
                val compressedImageUri = compressImage(context, rental.image.toUri(), maxWidth = 800, maxHeight = 800, quality = 50)
                if ( compressedImageUri != null ) {
                    Log.d(TAG, "compressedImageUri: $compressedImageUri")
                    Log.v(TAG, "Uploading compressed image to storage...")
                    val uploadTask = storageRef.putFile(compressedImageUri).await()
                    val downloadUrl = uploadTask.storage.downloadUrl.await()
                    val updatedRental = rental.copy(image = downloadUrl.toString())
                    Log.v(TAG, "Image uploaded successfully to storage! updating rental data in cache...")
                    dao.upsertRental(updatedRental.toEntity())
                    Log.v(TAG, "Rental data updated successfully in cache! uploading rental data to firestore...")
                    firestore.runTransaction {
                        val document = it.get(firestore.collection(Collections.LANDLORDS)
                            .document(firebaseAuth.uid!!)
                            .collection(Collections.RENTALS)
                            .document(rental.id))
                        it.set(
                            document.reference,
                            updatedRental.copy(isSynced = true)
                        )
                    }.await()
                    Log.v(TAG, "Rental data uploaded successfully to cloud! updating rental data in cache (making synced  be true...")
                    dao.upsertRental(updatedRental.copy(isSynced = true).toEntity())
                    Log.i(TAG, "Rental upserted successfully!")
                    ServiceResponse.Success(Unit)
                } else {
                    Log.e(TAG, "compressedImageUri is null")
                    ServiceResponse.Error(R.string.unknown_error)
                }
            } else {
                dao.upsertRental(rental.toEntity())
                Log.v(TAG, "rental data upserted successfully in cache! uploading rental data to firestore...")
                firestore.runTransaction {
                    val document = it.get(firestore.collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.RENTALS)
                        .document(rental.id))
                    it.set(
                        document.reference,
                        rental.copy(isSynced = true)
                    )
                }.await()
                Log.v(TAG, "rental data uploaded successfully to cloud! updating rental data in cache (making synced  be true...")
                dao.upsertRental(rental.copy(isSynced = true).toEntity())
                Log.i(TAG, "Rental upserted successfully!")
                ServiceResponse.Success(Unit)
            }
        } catch(e: Exception) {
            Log.e(TAG, "$e.message")
            if ( e is FirebaseFirestoreException || e is FirebaseNetworkException) {
                Log.v(TAG, "Not synced!!")
                dao.upsertRental(rental.toEntity())
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override suspend fun deleteRental(rental: Rental): ServiceResponse<Unit> {
        return try {
            var deletedFromFirestore = false
            firestore.runTransaction {
                val document = it.get(firestore.collection(Collections.LANDLORDS)
                    .document(firebaseAuth.uid!!)
                    .collection(Collections.RENTALS)
                    .document(rental.id))

                if ( document.exists() ) {
                    it.delete(document.reference)
                    deletedFromFirestore = true
                }
            }.await()
            if ( deletedFromFirestore ){
                dao.deleteRental(rental.id)
                ServiceResponse.Success(Unit)
            } else {
                dao.upsertRental(rental.copy(isDeleted = true).toEntity())
                ServiceResponse.Success(Unit)
            }
        } catch(e: Exception) {
            Log.e(TAG, "$e.message")
            if ( e is FirebaseFirestoreException || e is FirebaseNetworkException) {
                Log.v(TAG, "Not deleted from remote!!")
                dao.upsertRental(rental.toEntity().copy(isDeleted = true))
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override suspend fun getRentalById(id: String): ServiceResponse<Rental?> {
        return try {
            val rental = dao.getRentalById(id).firstOrNull()
            ServiceResponse.Success(rental?.toDomain())
        }catch(e: Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override fun getAllRentals(): Flow<ServiceResponse<List<Rental>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            dao.getAllRentals().collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllDeletedRentals(): ServiceResponse<List<Rental>> {
        return try {
            val rentals = dao.getAllDeletedRentals().first()
            ServiceResponse.Success(rentals.map { it.toDomain() })
        }catch(e: Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun getAllUnsyncedRentals(): ServiceResponse<List<Rental>> {
        return try {
            val rentals = dao.getAllUnsyncedRentals().first()
            ServiceResponse.Success(rentals.map { it.toDomain() })
        }catch(e: Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }
}