package com.kotlin.easyrent.features.tenantManagement.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.rentalManagement.data.cache.RentalsDao
import com.kotlin.easyrent.features.tenantManagement.data.dao.TenantsDao
import com.kotlin.easyrent.features.tenantManagement.data.mapper.toDomain
import com.kotlin.easyrent.features.tenantManagement.data.modal.TenantEntity
import com.kotlin.easyrent.utils.Collections
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

@HiltWorker
class ResetBalanceWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cacheDatabase: CacheDatabase,
    @Assisted private val firestore: FirebaseFirestore,
    @Assisted private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.i("MyWorker", "ResetBalanceWorker started!")
            val tenantsDao = cacheDatabase.tenantsDao()
            val rentalsDao = cacheDatabase.rentalsDao()
            Log.i("MyWorker", "Checking tenants with due rent...")
            val allTenants = tenantsDao.getAllTenants().first()
            val currentDate = Date().time
            if ( allTenants.isNotEmpty() ) {
                allTenants.forEach { tenant ->
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = tenant.lastResetDate
                    }

                    val lastMonth = calendar.get(Calendar.MONTH)
                    calendar.timeInMillis = currentDate
                    val currentMonth = calendar.get(Calendar.MONTH)

                    if (currentMonth != lastMonth) {
                        resetTenantBalance(tenant, currentDate, tenantsDao, rentalsDao)
                    }
                }
            }
            Log.i("MyWorker", "ResetBalanceWorker:::All checks done!")
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }

    private suspend fun resetTenantBalance(
        tenant: TenantEntity,
        currentDate: Long,
        tenantsDao: TenantsDao,
        rentalsDao: RentalsDao
    ) {
        val tenantRental = rentalsDao.getRentalById(tenant.rentalId).first()
        val rentalMonthlyPayment = tenantRental?.monthlyPayment ?: 0.0
        val newBalance = tenant.balance + rentalMonthlyPayment
        val updatedTenant = tenant.copy(
            balance = newBalance,
            unpaidMonths = if ( tenant.balance != 0.0  ) tenant.unpaidMonths+1 else tenant.unpaidMonths,
            lastResetDate = currentDate,
            isSynced = false
        )
        tenantsDao.upsertTenant(updatedTenant)
        if ( firebaseAuth.uid != null ) {
            val collectionRef = firestore
                .collection(Collections.LANDLORDS)
                .document(firebaseAuth.uid!!)
                .collection(Collections.TENANTS)

            collectionRef.document(tenant.id).set(updatedTenant.copy(isSynced = true).toDomain()).await()
            tenantsDao.upsertTenant(updatedTenant.copy(isSynced = true))
        }
    }
}