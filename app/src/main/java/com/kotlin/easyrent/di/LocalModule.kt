package com.kotlin.easyrent.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.core.cache.MIGRATION_1_2
import com.kotlin.easyrent.core.cache.MIGRATION_2_1
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun providesDataStorePrefs( @ApplicationContext context: Context): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("settings")
    }

    @Provides
    @Singleton
    fun providesCacheDatabase(
        @ApplicationContext context: Context
    ) : CacheDatabase {
        return Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            "easyRateCache.db"
        ).addMigrations(MIGRATION_2_1)
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }

}