package com.kotlin.easyrent.features.paymentTracking.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.paymentTracking.data.dao.PaymentsDao
import com.kotlin.easyrent.features.paymentTracking.data.mapper.toDomain
import com.kotlin.easyrent.features.paymentTracking.data.modal.PaymentEntity
import com.kotlin.easyrent.utils.Collections
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class PaymentsSyncWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cacheDatabase: CacheDatabase,
    @Assisted private val firestore: FirebaseFirestore,
    @Assisted private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.v("MyWorker", "PaymentsSyncWorker started!")
            val paymentsDao = cacheDatabase.paymentsDao()

            if ( firebaseAuth.uid != null ) {
                Log.v("MyWorker", "Checking for deleted payments...")
                val deletedPayments = paymentsDao.getDeletedPayments()
                if ( deletedPayments.isNotEmpty() ) {
                    firebaseAuth.uid?.let{
                        val collectionRef = firestore
                            .collection(Collections.LANDLORDS)
                            .document(firebaseAuth.uid!!)
                            .collection(Collections.PAYMENTS)
                        deletedPayments.forEach { payment ->
                            deletePayment(collectionRef, payment, paymentsDao)
                        }
                    }
                } else {
                    Log.v("MyWorker", "No deleted payments found! checking for unsynced payments....")
                }

                val unsyncedPayments = paymentsDao.getUnsyncedPayments()
                if ( unsyncedPayments.isNotEmpty() ) {
                    val collectionRef =  firestore
                        .collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.PAYMENTS)
                    unsyncedPayments.forEach { payment ->
                        syncPayment(collectionRef, payment, paymentsDao)
                    }
                } else {
                    Log.v("MyWorker", "No unsynced payments found!")
                }
            }
            Log.v("MyWorker", "PaymentsSyncWorker:::All checks done!")
            return Result.success()
        }catch (e: Exception){
            Log.e("MyWorker", "$e")
            return Result.failure()
        }
    }

    private suspend fun deletePayment(
        collectionRef: CollectionReference,
        payment: PaymentEntity,
        paymentsDao: PaymentsDao
    ) {
        collectionRef.document(payment.id).delete().await()
        paymentsDao.deletePaymentById(payment.id)
    }

    private suspend fun syncPayment(
        collection: CollectionReference,
        payment: PaymentEntity,
        paymentsDao: PaymentsDao
    ) {
        collection.document(payment.id).set(payment.copy(isSynced = true).toDomain()).await()
        paymentsDao.savePayment(payment.copy(isSynced = true))
    }
}