package com.kotlin.easyrent.core.cache

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kotlin.easyrent.utils.getCurrentMonthAndYear

val MIGRATION_22_1 = object : Migration(22, 1) {
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

val MIGRATION_11_2 = object : Migration(11, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add the new column with a default value of 0
        db.execSQL("ALTER TABLE tenants ADD COLUMN unpaidMonths INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE tenants ADD COLUMN month TEXT NOT NULL DEFAULT ${getCurrentMonthAndYear().first}")
        db.execSQL("ALTER TABLE tenants ADD COLUMN year INTEGER NOT NULL DEFAULT ${getCurrentMonthAndYear().second}")
    }
}



val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the new payments table
        db.execSQL(
            """
            CREATE TABLE expenses (
                id TEXT NOT NULL PRIMARY KEY,
                rentalId TEXT NOT NULL,
                rentalName TEXT NOT NULL,
                description TEXT NOT NULL,
                amount REAL NOT NULL,
                date INTEGER NOT NULL,
                isSynced INTEGER NOT NULL DEFAULT 0,
                isDeleted INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }
}


val MIGRATION_2_1 = object : Migration(2, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the new table
        db.execSQL("""
            CREATE TABLE tenants_new (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                phone TEXT NOT NULL,
                email TEXT,
                balance REAL NOT NULL,
                rentalId TEXT NOT NULL,
                rentalName TEXT NOT NULL,
                moveInDate INTEGER NOT NULL,
                unpaidMonths INTEGER NOT NULL,
                address TEXT,
                profilePhotoUrl TEXT,
                description TEXT,
                isSynced INTEGER NOT NULL,
                isDeleted INTEGER NOT NULL,
                lastResetDate INTEGER NOT NULL
            )
        """)

        // Copy the data
        db.execSQL("""
            INSERT INTO tenants_new (id, name, phone, email, balance, rentalId, rentalName, moveInDate, unpaidMonths, address, profilePhotoUrl, description, isSynced, isDeleted, lastResetDate)
            SELECT id, name, phone, email, balance, rentalId, rentalName, moveInDate, unpaidMonths, address, profilePhotoUrl, description, 0, isDeleted, lastResetDate
            FROM tenants
        """)

        // Remove the old table
        db.execSQL("DROP TABLE tenants")

        // Rename the new table to the old table name
        db.execSQL("ALTER TABLE tenants_new RENAME TO tenants")
    }
}