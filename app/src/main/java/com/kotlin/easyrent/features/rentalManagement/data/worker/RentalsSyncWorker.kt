package com.kotlin.easyrent.features.rentalManagement.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.rentalManagement.data.cache.RentalsDao
import com.kotlin.easyrent.features.rentalManagement.data.mapper.toDomain
import com.kotlin.easyrent.features.rentalManagement.data.modal.RentalEntity
import com.kotlin.easyrent.utils.Collections
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class RentalsSyncWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cacheDatabase: CacheDatabase,
    @Assisted private val firestore: FirebaseFirestore,
    @Assisted private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.d("MyWorker", "RentalsSyncWorker started!...")
            val rentalsDao = cacheDatabase.rentalsDao()
            if ( firebaseAuth.uid != null ) {
                val deletedRentals = rentalsDao.getAllDeletedRentals()
                if ( deletedRentals.isNotEmpty() ) {
                    val collectionRef =  firestore
                        .collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.RENTALS)
                    deletedRentals.forEach { rental ->
                        deleteRental(collectionRef, rental, rentalsDao)
                    }
                } else {
                    Log.d("MyWorker", "No deleted rentals!")
                }

                Log.d("MyWorker", "Checking for unsynced rentals....")
                val unsyncedRentals = rentalsDao.getAllUnsyncedRentals()
                if ( unsyncedRentals.isNotEmpty() ) {
                    val collection =  firestore
                        .collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.RENTALS)
                   unsyncedRentals.forEach { rental ->
                        syncRental(collection, rental, rentalsDao)
                    }
                } else {
                    Log.d("MyWorker", "No unsynced rentals!")
                }
            }
            Log.d("MyWorker", "RentalsSyncWorker:::All checks done!")
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }


    private suspend fun deleteRental(
        collectionRef: CollectionReference,
        rental: RentalEntity,
        rentalsDao: RentalsDao
    ) {
        collectionRef.document(rental.id).delete().await()
        rentalsDao.deleteRental(rental.id)
    }

    private suspend fun syncRental(
        collection: CollectionReference,
        rental: RentalEntity,
        rentalsDao: RentalsDao
    ) {
        collection.document(rental.id).set(rental.copy(isSynced = true).toDomain()).await()
        rentalsDao.upsertRental(rental.copy(isSynced = true))
    }
}