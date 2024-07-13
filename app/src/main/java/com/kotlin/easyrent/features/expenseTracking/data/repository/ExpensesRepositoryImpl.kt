package com.kotlin.easyrent.features.expenseTracking.data.repository

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.expenseTracking.data.mapper.toDomain
import com.kotlin.easyrent.features.expenseTracking.data.mapper.toEntity
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.domain.repository.ExpensesRepository
import com.kotlin.easyrent.utils.Collections
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(
    cacheDatabase: CacheDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ExpensesRepository {

    companion object {
        private val TAG = ExpensesRepositoryImpl::class.java.simpleName
    }

    private val expenseDao = cacheDatabase.expensesDao()

    override suspend fun addExpense(expense: Expense): ServiceResponse<Unit> {
       return try {
           withContext(NonCancellable) {
               expenseDao.insertExpense(expense.toEntity())
               firestore.runTransaction {  transaction ->
                   val documentRef = firestore
                       .collection(Collections.LANDLORDS)
                       .document(firebaseAuth.uid!!)
                       .collection(Collections.EXPENSES)
                       .document(expense.id)

                   transaction.set(
                       documentRef,
                       expense
                   )
               }.await()
               expenseDao.insertExpense(expense.copy(isSynced = true).toEntity())
           }
            ServiceResponse.Success(Unit)
        }catch (e: Exception) {
            Log.e(TAG, "Error adding expense: $e")
            if ( e is FirebaseFirestoreException || e is FirebaseNetworkException ) {
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override fun getAllExpenses(): Flow<ServiceResponse<List<Expense>>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            expenseDao.getAllExpenses().collectLatest {
                send(ServiceResponse.Success(it.map { it.toDomain() }))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteExpense(expense: Expense): ServiceResponse<Unit> {
        return try {
            withContext(NonCancellable) {
                expenseDao.insertExpense(expense.copy(isDeleted = true).toEntity())
                firestore.runTransaction {
                    val document = it.get(firestore.collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.EXPENSES)
                        .document(expense.id))

                    if ( document.exists() ) {
                        it.delete(document.reference)
                    }
                }.await()
                expenseDao.deleteExpense(expense.id)
            }
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

    override suspend fun getRentalExpenses(rentalId: String): ServiceResponse<List<Expense>> {
        return  try {
            val expenses = expenseDao.getExpensesByRental(rentalId)
            ServiceResponse.Success(expenses.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e(TAG, "$e.message")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun getAllUnsyncedExpenses(): ServiceResponse<List<Expense>> {
        return  try {
            val expenses = expenseDao.getUnsyncedExpenses()
            ServiceResponse.Success(expenses.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e(TAG, "$e.message")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun getAllDeletedExpenses(): ServiceResponse<List<Expense>> {
        return  try {
            val expenses = expenseDao.getDeletedExpenses()
            ServiceResponse.Success(expenses.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e(TAG, "$e.message")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun getExpenseById(expenseId: String): ServiceResponse<Expense?> {
        return  try {
            val expense = expenseDao.getExpenseById(expenseId)
            ServiceResponse.Success(expense?.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "$e.message")
            ServiceResponse.Error(R.string.unknown_error)
        }
    }

    override suspend fun deleteAllExpenses(): ServiceResponse<Unit> {
        return  try {
            withContext(NonCancellable) {
                val expenses = expenseDao.getAllExpenses().first()
                if ( expenses.isNotEmpty() ) {
                    expenses.forEach { expense ->
                        expenseDao.insertExpense(expense.copy(isDeleted = true))
                    }
                }
                val deletedExpenses = expenseDao.getDeletedExpenses()
                if ( deletedExpenses.isNotEmpty() ) {
                    val collectionRef = firestore
                        .collection(Collections.LANDLORDS
                        ).document(firebaseAuth.uid!!)
                        .collection(Collections.EXPENSES)

                    deletedExpenses.forEach { expense ->
                        firestore.runTransaction { transaction ->
                            val docRef = collectionRef.document(expense.id)
                            transaction.delete(docRef)
                        }.await()
                        expenseDao.deleteExpense(expense.id)
                    }
                }
            }
            ServiceResponse.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "$e.message")
            if ( e is FirebaseFirestoreException || e is FirebaseNetworkException) {
                ServiceResponse.Success(Unit)
            } else {
                ServiceResponse.Error(R.string.unknown_error)
            }
        }
    }

    override fun getTotalExpenses(): Flow<ServiceResponse<Double>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            expenseDao.getTotalExpenses().collectLatest {
                send(ServiceResponse.Success(it))
            }
        }.catch{
            Log.e(TAG, "${it.message}")
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }
}