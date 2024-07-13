package com.kotlin.easyrent.core.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlin.easyrent.features.expenseTracking.data.dao.ExpensesDao
import com.kotlin.easyrent.features.expenseTracking.data.modal.ExpenseEntity
import com.kotlin.easyrent.features.paymentTracking.data.dao.PaymentsDao
import com.kotlin.easyrent.features.paymentTracking.data.modal.PaymentEntity
import com.kotlin.easyrent.features.rentalManagement.data.cache.RentalsDao
import com.kotlin.easyrent.features.rentalManagement.data.modal.RentalEntity
import com.kotlin.easyrent.features.tenantManagement.data.dao.TenantsDao
import com.kotlin.easyrent.features.tenantManagement.data.modal.TenantEntity

@Database(
    entities = [RentalEntity::class, TenantEntity::class, PaymentEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun rentalsDao(): RentalsDao
    abstract fun tenantsDao(): TenantsDao
    abstract fun paymentsDao(): PaymentsDao
    abstract fun expensesDao() : ExpensesDao
}