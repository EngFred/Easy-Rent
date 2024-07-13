package com.kotlin.easyrent.features.expenseTracking.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.expenseTracking.data.dao.ExpensesDao
import com.kotlin.easyrent.features.expenseTracking.data.mapper.toDomain
import com.kotlin.easyrent.features.expenseTracking.data.modal.ExpenseEntity
import com.kotlin.easyrent.utils.Collections
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class ExpensesSyncWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cacheDatabase: CacheDatabase,
    @Assisted private val firestore: FirebaseFirestore,
    @Assisted private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.v("MyWorker", "ExpensesSyncWorker started!")
            val expensesDao = cacheDatabase.expensesDao()

            if ( firebaseAuth.uid != null ) {
                Log.v("MyWorker", "Checking for deleted expenses...")
                val deletedExpenses = expensesDao.getDeletedExpenses()
                if ( deletedExpenses.isNotEmpty() ) {
                    firebaseAuth.uid?.let{
                        val collectionRef = firestore
                            .collection(Collections.LANDLORDS)
                            .document(firebaseAuth.uid!!)
                            .collection(Collections.EXPENSES)
                        deletedExpenses.forEach { expense ->
                            deleteExpense(collectionRef, expense, expensesDao)
                        }
                    }
                } else {
                    Log.v("MyWorker", "No deleted expenses found! checking for unsynced expenses....")
                }

                val unsyncedExpenses = expensesDao.getUnsyncedExpenses()
                if ( unsyncedExpenses.isNotEmpty() ) {
                    val collectionRef =  firestore
                        .collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.EXPENSES)
                    unsyncedExpenses.forEach { expense ->
                        syncExpense(collectionRef, expense, expensesDao)
                    }
                } else {
                    Log.v("MyWorker", "No unsynced expenses found!")
                }
            }
            Log.v("MyWorker", "ExpensesSyncWorker:::All checks done!")
            return Result.success()
        }catch (e: Exception){
            Log.e("MyWorker", "$e")
            return Result.failure()
        }
    }

    private suspend fun deleteExpense(
        collectionRef: CollectionReference,
        expense: ExpenseEntity,
        expensesDao: ExpensesDao
    ) {
        firestore.runTransaction {
            it.delete(
                collectionRef.document(expense.id)
            )
        }.await()
        expensesDao.deleteExpense(expense.id)
    }

    private suspend fun syncExpense(
        collection: CollectionReference,
        expense: ExpenseEntity,
        expensesDao: ExpensesDao
    ) {
        firestore.runTransaction {
            it.set(
                collection.document(expense.id),
                expense.copy(isSynced = true).toDomain()
            )
        }.await()
        expensesDao.insertExpense(expense.copy(isSynced = true))
    }
}