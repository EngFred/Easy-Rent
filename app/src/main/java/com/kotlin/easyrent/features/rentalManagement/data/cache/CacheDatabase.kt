package com.kotlin.easyrent.features.rentalManagement.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlin.easyrent.features.rentalManagement.data.modal.RentalEntity

@Database(
    entities = [RentalEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun rentalsDao(): RentalsDao
}