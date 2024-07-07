package com.kotlin.easyrent.features.auth.data.repository

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.R
import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.features.auth.domain.repository.AuthRepository
import com.kotlin.easyrent.utils.Collections
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    companion object {
        private val TAG = AuthRepositoryImpl::class.java.simpleName
    }

    override suspend fun login(username: String, password: String): ServiceResponse<Boolean> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(username, password).await()
            if (authResult != null) {
                ServiceResponse.Success(true)
            } else {
                ServiceResponse.Error(R.string.user_is_null)
            }
        }catch (e:Exception){
            Log.e(TAG, "${e.message}")
            handleExceptions(e)
        }
    }

    override suspend fun signup(user: User, password: String): ServiceResponse<Boolean> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()

            if (authResult == null) {
                ServiceResponse.Error(R.string.unknown_error)
            } else {
                val firebaseUserId = authResult.user?.uid!!
                val landlord = user.copy(id = firebaseUserId)
                firestore.collection(Collections.LANDLORDS).document(landlord.id).set(landlord).await()
                ServiceResponse.Success(true)
            }
        }catch (e:Exception){
            Log.e(TAG, "${e.message}")
            handleExceptions(e)
        }
    }


    private fun handleExceptions(e: Exception) = when (e) {
        is FirebaseAuthInvalidCredentialsException -> {
            ServiceResponse.Error(R.string.invalid_email_or_password)
        }

        is FirebaseAuthInvalidUserException -> {
            when (e.errorCode) {
                "ERROR_USER_DISABLED" -> {
                    ServiceResponse.Error(R.string.account_disabled)
                }

                "ERROR_USER_NOT_FOUND" -> {
                    ServiceResponse.Error(R.string.user_not_found)
                }

                else -> {
                    ServiceResponse.Error(R.string.unknown_error)
                }

            }
        }

        is FirebaseNetworkException -> {
            ServiceResponse.Error(R.string.network_error)
        }

        else -> {
            ServiceResponse.Error(R.string.unknown_error)
        }
    }
}