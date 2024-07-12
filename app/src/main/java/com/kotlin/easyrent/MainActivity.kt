package com.kotlin.easyrent

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.kotlin.easyrent.core.graphs.RootGraph
import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.Graphs
import com.kotlin.easyrent.core.theme.EasyRentTheme
import com.kotlin.easyrent.core.theme.SetSystemBarColor
import com.kotlin.easyrent.core.theme.splashBg
import com.kotlin.easyrent.features.paymentTracking.data.worker.PaymentsSyncWorker
import com.kotlin.easyrent.features.rentalManagement.data.worker.RentalsSyncWorker
import com.kotlin.easyrent.features.tenantManagement.data.worker.DaysCalculationWorker
import com.kotlin.easyrent.features.tenantManagement.data.worker.TenantsSyncWorker
import com.kotlin.easyrent.utils.getCurrentMonthDays
import com.kotlin.easyrent.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyRentTheme {

                val (month, days) = getCurrentMonthDays()
                Log.i("%", "Current month: $month, Number of days: $days")

                val navController = rememberNavController()
                val sharedViewModel: SharedViewModel = hiltViewModel()
                val context = LocalContext.current
                val currentLanguage = sharedViewModel.currentLanguage.collectAsState().value

                val isLoggedIn = firebaseAuth.currentUser != null

                if ( isLoggedIn ) {
                    scheduleWorkers()
                } else {
                    Log.e("MyWorker", "MyWorker can't run! User is null")
                }

                val startDestination = if ( isLoggedIn ) Graphs.HOME else Graphs.AUTH

                if (currentLanguage == null){
                    SetSystemBarColor(
                        statusBarColor = splashBg,
                        navigationBarColor = splashBg,
                        isAppearanceLightStatusBars = false,
                        isAppearanceLightNavigationBars = false
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(splashBg),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    SetSystemBarColor(
                        statusBarColor = MaterialTheme.colorScheme.surface,
                        navigationBarColor = MaterialTheme.colorScheme.surface
                    )
                    LaunchedEffect(currentLanguage) {
                        Log.e("$", "The current app language is $currentLanguage & the startDestination is $startDestination")
                    }
                    val language = Language.entries.find { it.name.lowercase() == currentLanguage }  ?: Language.English
                    setLocale(language = language, context = context)
                    RootGraph(
                        sharedViewModel = sharedViewModel,
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }

    private fun scheduleWorkers() {
        Log.i("MyWorker", "MyWorker is running")

        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val workerConstraints2 = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED).build()

        //payments
        val paymentsSyncRequest = PeriodicWorkRequestBuilder<PaymentsSyncWorker>(
            repeatInterval = Duration.ofMinutes(15),
            flexTimeInterval = Duration.ofMinutes(7)
        ).setConstraints(workerConstraints).build()


        //rentals
        val rentalsSyncRequest = PeriodicWorkRequestBuilder<RentalsSyncWorker>(
            repeatInterval = Duration.ofMinutes(15),
            flexTimeInterval = Duration.ofMinutes(7)
        ).setConstraints(workerConstraints).build()


        //tenants
        val tenantsSyncRequest = PeriodicWorkRequestBuilder<TenantsSyncWorker>(
            repeatInterval = Duration.ofMinutes(15),
            flexTimeInterval = Duration.ofMinutes(7)
        ).setConstraints(workerConstraints).build()

        //days in rental
        val daysInRentalSyncRequest = PeriodicWorkRequestBuilder<DaysCalculationWorker>(
            repeatInterval = Duration.ofHours(4),
            flexTimeInterval = Duration.ofHours(2)
        ).setConstraints(workerConstraints2).build()

        WorkManager.getInstance(applicationContext)
            .apply {

                enqueueUniquePeriodicWork(
                    "payments_sync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    paymentsSyncRequest
                )

                enqueueUniquePeriodicWork(
                    "rentals_sync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    rentalsSyncRequest
                )

                enqueueUniquePeriodicWork(
                    "tenants_sync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    tenantsSyncRequest
                )

                enqueueUniquePeriodicWork(
                    "days_in_rental_sync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    daysInRentalSyncRequest
                )
            }
    }
}

