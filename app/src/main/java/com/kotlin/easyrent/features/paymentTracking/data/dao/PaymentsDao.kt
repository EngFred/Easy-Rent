package com.kotlin.easyrent.features.paymentTracking.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kotlin.easyrent.features.paymentTracking.data.modal.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentsDao {
    @Upsert
    suspend fun savePayment(paymentEntity: PaymentEntity)

    @Query("SELECT * FROM payments WHERE isDeleted = 0")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE rentalId = :rentalId")
    fun getRentalPayments(rentalId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE tenantId = :tenantId")
    fun getTenantPayments(tenantId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE id = :id")
    suspend fun getPaymentById(id: String): PaymentEntity?

    @Query("DELETE FROM payments WHERE id = :id")
    suspend fun deletePaymentById(id: String)

    @Query("DELETE FROM payments WHERE isDeleted = 0")
    suspend fun deleteAllPayments()

    @Query("SELECT COUNT(*) FROM payments")
    suspend fun getPaymentsCount(): Int

    @Query("SELECT * FROM payments WHERE isSynced = 0")
    suspend fun getUnsyncedPayments(): List<PaymentEntity>

    @Query("SELECT * FROM payments WHERE isDeleted = 1")
    suspend fun getDeletedPayments(): List<PaymentEntity>

    @Query("SELECT SUM(amount) FROM payments WHERE isDeleted = 0")
    fun getTotalPayments(): Flow<Double>
}