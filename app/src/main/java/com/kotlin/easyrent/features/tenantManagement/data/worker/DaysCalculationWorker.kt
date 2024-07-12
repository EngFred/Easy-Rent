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
import com.kotlin.easyrent.utils.getCurrentMonthAndYear
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class DaysCalculationWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cacheDatabase: CacheDatabase,
    @Assisted private val firestore: FirebaseFirestore,
    @Assisted private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.i("MyWorker", "DaysCalculationWorker started!")
            val tenantsDao = cacheDatabase.tenantsDao()
            val rentalsDao = cacheDatabase.rentalsDao()
            if ( firebaseAuth.uid != null ) {
                Log.i("MyWorker", "Checking tenants with due rent...")
                val (month, year) = getCurrentMonthAndYear()
                val allTenants = tenantsDao.getAllTenants().first()
                if ( allTenants.isNotEmpty() ) {
                    allTenants.forEach { tenant ->
                        if ( (tenant.month.lowercase() != month.lowercase()) && (tenant.year == year)) {
                            updateTenantInfo(tenant, tenantsDao, rentalsDao)
                        }
                    }
                }
            }
            Log.i("MyWorker", "DaysCalculationWorker:::All checks done!")
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }

    private suspend fun updateTenantInfo(
        tenant: TenantEntity,
        tenantsDao: TenantsDao,
        rentalsDao: RentalsDao
    ) {
        val hasNotYetPaidRentForMonth = (tenant.balance != 0.0)
        val tenantRental = rentalsDao.getRentalById(tenant.rentalId).first()
        tenantRental?.let {
            val updatedTenant = if (hasNotYetPaidRentForMonth) {
                tenant.copy(
                    unpaidMonths = tenant.unpaidMonths + 1,
                    isSynced = false,
                    month = getCurrentMonthAndYear().first,
                    balance = tenant.balance + tenantRental.monthlyPayment
                )
            } else {
                tenant.copy(
                    month = getCurrentMonthAndYear().first,
                    balance = tenantRental.monthlyPayment.toDouble(),
                    isSynced = false
                )
            }
            tenantsDao.upsertTenant(updatedTenant)
            val collectionRef = firestore
                .collection(Collections.LANDLORDS)
                .document(firebaseAuth.uid!!)
                .collection(Collections.TENANTS)

            collectionRef.document(tenant.id).set(updatedTenant.copy(isSynced = true).toDomain())
            tenantsDao.upsertTenant(updatedTenant.copy(isSynced = true))
        }
    }
}