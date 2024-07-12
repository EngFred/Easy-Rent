package com.kotlin.easyrent.core.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlin.easyrent.features.paymentTracking.data.dao.PaymentsDao
import com.kotlin.easyrent.features.paymentTracking.data.modal.PaymentEntity
import com.kotlin.easyrent.features.rentalManagement.data.cache.RentalsDao
import com.kotlin.easyrent.features.rentalManagement.data.modal.RentalEntity
import com.kotlin.easyrent.features.tenantManagement.data.dao.TenantsDao
import com.kotlin.easyrent.features.tenantManagement.data.modal.TenantEntity

@Database(
    entities = [RentalEntity::class, TenantEntity::class, PaymentEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun rentalsDao(): RentalsDao
    abstract fun tenantsDao(): TenantsDao
    abstract fun paymentsDao(): PaymentsDao
}