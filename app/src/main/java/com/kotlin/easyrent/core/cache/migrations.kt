package com.kotlin.easyrent.core.cache

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kotlin.easyrent.utils.getCurrentMonthAndYear

val MIGRATION_2_1 = object : Migration(2, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the new payments table
        db.execSQL(
            """
            CREATE TABLE payments (
                id TEXT NOT NULL PRIMARY KEY,
                tenantId TEXT NOT NULL,
                rentalId TEXT NOT NULL,
                by TEXT NOT NULL,
                rentalName TEXT NOT NULL,
                amount REAL NOT NULL,
                date INTEGER NOT NULL,
                completed INTEGER NOT NULL DEFAULT 0,
                isSynced INTEGER NOT NULL DEFAULT 0,
                isDeleted INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add the new column with a default value of 0
        db.execSQL("ALTER TABLE tenants ADD COLUMN unpaidMonths INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE tenants ADD COLUMN month TEXT NOT NULL DEFAULT ${getCurrentMonthAndYear().first}")
        db.execSQL("ALTER TABLE tenants ADD COLUMN year INTEGER NOT NULL DEFAULT ${getCurrentMonthAndYear().second}")
    }
}