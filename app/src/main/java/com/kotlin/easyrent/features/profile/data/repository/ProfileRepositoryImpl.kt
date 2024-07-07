package com.kotlin.easyrent.features.profile.data.repository

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kotlin.easyrent.R
import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.features.profile.domain.repository.ProfileRepository
import com.kotlin.easyrent.utils.Collections
import com.kotlin.easyrent.utils.Constants
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import okio.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : ProfileRepository {

    companion object {
        private val TAG = ProfileRepositoryImpl::class.java.simpleName
    }

    override suspend fun updateProfilePhoto(photoUrl: String): ServiceResponse<Any> {
        return try {
            val storageRef = firebaseStorage.reference.child("${Constants.PROFILE_PHOTOS}/${firebaseAuth.uid!!}/${System.currentTimeMillis()}")
            val uploadTask = storageRef.putFile(photoUrl.toUri()).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            val task = firestore.collection(Collections.LANDLORDS).document(firebaseAuth.uid!!).update("profileImage", downloadUrl.toString()).await()
            ServiceResponse.Success(task)
        } catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            if ( e is IOException ) {
                ServiceResponse.Error(R.string.network_error)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override suspend fun updateBio(bio: String): ServiceResponse<Any> {
        return try {
            val task = firestore.collection(Collections.LANDLORDS).document(firebaseAuth.uid!!).update("about", bio).await()
            ServiceResponse.Success(task)
        } catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            if ( e is IOException ) {
                ServiceResponse.Error(R.string.network_error)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override suspend fun updateUsername(lastName: String, firstName: String): ServiceResponse<Any> {
        return try {
            val myMap = mapOf(
                "firstName" to firstName,
                "lastName" to lastName
            )
            val task = firestore.collection(Collections.LANDLORDS).document(firebaseAuth.uid!!).update(myMap).await()
            ServiceResponse.Success(task)
        } catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            if ( e is IOException ) {
                ServiceResponse.Error(R.string.network_error)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override suspend fun updateTel(tel: String): ServiceResponse<Any> {
        return try {
            val task = firestore.collection(Collections.LANDLORDS).document(firebaseAuth.uid!!).update("contactNumber", tel).await()
            ServiceResponse.Success(task)
        } catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            if ( e is IOException ) {
                ServiceResponse.Error(R.string.network_error)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override suspend fun updateAddress(address: String): ServiceResponse<Any> {
        return try {
            val task = firestore.collection(Collections.LANDLORDS).document(firebaseAuth.uid!!).update("address", address).await()
            ServiceResponse.Success(task)
        } catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            if ( e is IOException ) {
                ServiceResponse.Error(R.string.network_error)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override suspend fun updateDOB(dob: Long): ServiceResponse<Any> {
        return try {
            val task = firestore.collection(Collections.LANDLORDS).document(firebaseAuth.uid!!).update("dateOfBirth", dob).await()
            ServiceResponse.Success(task)
        } catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            ServiceResponse.Error(0)
        }
    }

    override suspend fun logout(): ServiceResponse<Unit> {
        return try {
            val task = firebaseAuth.signOut()
            ServiceResponse.Success(task)
        } catch (e:Exception) {
            Log.e(TAG, "${e.message}")
            if ( e is IOException ) {
                ServiceResponse.Error(R.string.network_error)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override fun getLandlord(): Flow<ServiceResponse<User?>> {
        return callbackFlow {
            trySend(ServiceResponse.Idle)
            if ( firebaseAuth.uid != null ) {
                val listenerRegistration = firestore.collection(Collections.LANDLORDS).document(firebaseAuth.uid!!).addSnapshotListener { value, error ->
                    if ( error != null ) {
                        Log.e(TAG, "Snapshot error: $error")
                        trySend(ServiceResponse.Error(0))
                        return@addSnapshotListener
                    }

                    if ( value != null ) {
                        val user = value.toObject(User::class.java)
                        trySend(ServiceResponse.Success(user))
                    }
                }
                awaitClose {
                    listenerRegistration.remove()
                }
            } else {
                trySend(ServiceResponse.Success(null))
            }
        }.flowOn(Dispatchers.IO)
            .catch{
                Log.e(TAG, "${it.message}")
                if ( it is IOException ) {
                    emit(ServiceResponse.Error(R.string.network_error))
                } else {
                    emit(ServiceResponse.Error(R.string.unknown_error))
                }
            }
    }

}