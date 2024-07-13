package com.kotlin.easyrent.features.tenantManagement.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.tenantManagement.data.dao.TenantsDao
import com.kotlin.easyrent.features.tenantManagement.data.mapper.toDomain
import com.kotlin.easyrent.features.tenantManagement.data.modal.TenantEntity
import com.kotlin.easyrent.utils.Collections
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class TenantsSyncWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cacheDatabase: CacheDatabase,
    @Assisted private val firestore: FirebaseFirestore,
    @Assisted private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.wtf("MyWorker", "TenantsSyncWorker started!")
            val tenantsDao = cacheDatabase.tenantsDao()
            if ( firebaseAuth.uid != null ) {
                Log.wtf("MyWorker", "Checking for deleted Tenants...")
                val deletedTenants = tenantsDao.getAllDeletedTenants()
                if ( deletedTenants.isNotEmpty() ) {
                    val collectionRef = firestore
                        .collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.TENANTS)
                    deletedTenants.forEach { tenant ->
                        deletePayment(collectionRef, tenant, tenantsDao)
                    }
                } else {
                    Log.wtf("MyWorker", "No tenants were deleted in cache, checking for unsynced tenants...")
                }
                val unsyncedTenants = tenantsDao.getAllUnsyncedTenants()
                if ( unsyncedTenants.isNotEmpty() ) {
                    val collection =  firestore
                        .collection(Collections.LANDLORDS)
                        .document(firebaseAuth.uid!!)
                        .collection(Collections.TENANTS)
                    unsyncedTenants.forEach { tenant ->
                        syncTenant(collection, tenant, tenantsDao )
                    }
                } else {
                    Log.wtf("MyWorker", "No tenants were unsynced in cache!")
                }
            }
            Log.wtf("MyWorker", "TenantsSyncWorker:::All checks done!")
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }

    private suspend fun deletePayment(
        collectionRef: CollectionReference,
        tenant: TenantEntity,
        tenantsDao: TenantsDao
    ) {
        firestore.runTransaction {
            it.delete(
                collectionRef.document(tenant.id)
            )
        }.await()
        tenantsDao.deleteTenantById(tenant.id)
    }

    private suspend fun syncTenant(
        collectionRef: CollectionReference,
        tenant: TenantEntity,
        tenantsDao: TenantsDao
    ) {
        firestore.runTransaction {
            it.set(
                collectionRef.document(tenant.id),
                tenant.copy(isSynced = true).toDomain()
            )
        }.await()
        tenantsDao.upsertTenant(tenant.copy(isSynced = true))
    }

}