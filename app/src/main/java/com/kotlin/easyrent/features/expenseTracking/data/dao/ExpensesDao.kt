package com.kotlin.easyrent.features.expenseTracking.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kotlin.easyrent.features.expenseTracking.data.modal.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE rentalId = :rentalId AND isDeleted = 0")
    suspend fun getExpensesByRental(rentalId: String): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE isDeleted = 0")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    suspend fun getExpenseById( expenseId: String ): ExpenseEntity?
    @Query("SELECT * FROM expenses WHERE isSynced = 0")
    suspend fun getUnsyncedExpenses(): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE isDeleted = 1")
    suspend fun getDeletedExpenses(): List<ExpenseEntity>
    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpense(expenseId: String)

    @Query("SELECT SUM(amount) FROM expenses WHERE isDeleted = 0")
    fun getTotalExpenses(): Flow<Double>

    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()
}