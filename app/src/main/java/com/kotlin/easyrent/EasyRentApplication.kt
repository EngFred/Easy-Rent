package com.kotlin.easyrent

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.expenseTracking.data.worker.ExpensesSyncWorker
import com.kotlin.easyrent.features.paymentTracking.data.worker.PaymentsSyncWorker
import com.kotlin.easyrent.features.rentalManagement.data.worker.RentalsSyncWorker
import com.kotlin.easyrent.features.tenantManagement.data.worker.ResetBalanceWorker
import com.kotlin.easyrent.features.tenantManagement.data.worker.TenantsSyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class EasyRentApplication : Application(),
    ImageLoaderFactory,
    Configuration.Provider {

    @Inject
    lateinit var workerFactory: WorkersFactory

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()


    class WorkersFactory @Inject constructor(
        private val cacheDatabase: CacheDatabase,
        private val firestore: FirebaseFirestore,
        private  val firebaseAuth: FirebaseAuth
    ) : WorkerFactory() {

        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            when (workerClassName) {
                PaymentsSyncWorker::class.java.name -> return PaymentsSyncWorker(
                    appContext,
                    workerParameters,
                    cacheDatabase,
                    firestore,
                    firebaseAuth
                )
                RentalsSyncWorker::class.java.name -> return RentalsSyncWorker(
                    appContext,
                    workerParameters,
                    cacheDatabase,
                    firestore,
                    firebaseAuth
                )
                TenantsSyncWorker::class.java.name -> return TenantsSyncWorker(
                    appContext,
                    workerParameters,
                    cacheDatabase,
                    firestore,
                    firebaseAuth
                )
                ResetBalanceWorker::class.java.name -> return ResetBalanceWorker(
                    appContext,
                    workerParameters,
                    cacheDatabase,
                    firestore,
                    firebaseAuth
                )
                ExpensesSyncWorker::class.java.name -> return ExpensesSyncWorker(
                    appContext,
                    workerParameters,
                    cacheDatabase,
                    firestore,
                    firebaseAuth
                )
                else -> throw IllegalArgumentException("Unknown worker class: $workerClassName")
            }
        }
    }
}