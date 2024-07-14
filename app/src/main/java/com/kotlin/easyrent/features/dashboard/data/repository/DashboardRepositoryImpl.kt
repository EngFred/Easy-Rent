package com.kotlin.easyrent.features.dashboard.data.repository

import android.util.Log
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.cache.CacheDatabase
import com.kotlin.easyrent.features.dashboard.domain.repository.DashboardRepository
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val cacheDatabase: CacheDatabase
) : DashboardRepository {

    companion object {
        private val TAG = DashboardRepositoryImpl::class.java.simpleName
    }

    private val paymentsDao = cacheDatabase.paymentsDao()
    private val expensesDao = cacheDatabase.expensesDao()
    private val rentalsDao = cacheDatabase.rentalsDao()
    private val tenantsDao = cacheDatabase.tenantsDao()

    override fun getTotalPayments(): Flow<ServiceResponse<Double>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            paymentsDao.getTotalPayments().collect{
                send(ServiceResponse.Success(it))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override fun getTotalExpenses(): Flow<ServiceResponse<Double>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            expensesDao.getTotalExpenses().collect{
                send(ServiceResponse.Success(it))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override fun getTotalRentals(): Flow<ServiceResponse<Int>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            rentalsDao.getRentalsCount().collect{
                send(ServiceResponse.Success(it))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override fun getTotalTenants(): Flow<ServiceResponse<Int>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            tenantsDao.getTenantsCount().collect{
                send(ServiceResponse.Success(it))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(ServiceResponse.Error(R.string.unknown_error))
        }.flowOn(Dispatchers.IO)
    }

    override fun getExpectedRevenue(): Flow<ServiceResponse<Double>> {
        return channelFlow {
            send(ServiceResponse.Idle)
            var expectedRevenue = 0.0 //160000
            val rentals = rentalsDao.getAllRentals().first()
            rentals.forEach { rental ->
                expectedRevenue += (rental.monthlyPayment*rental.occupiedRooms)
            }
            send(ServiceResponse.Success(expectedRevenue))
        }.catch{
            Log.e(TAG, it.message.toString())
            emit(ServiceResponse.Error(R.string.unknown_error))
        }
    }
}