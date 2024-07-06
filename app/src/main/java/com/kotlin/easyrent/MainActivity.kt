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
import com.google.firebase.auth.FirebaseAuth
import com.kotlin.easyrent.core.graphs.RootGraph
import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.Graphs
import com.kotlin.easyrent.core.theme.EasyRentTheme
import com.kotlin.easyrent.core.theme.SetSystemBarColor
import com.kotlin.easyrent.core.theme.splashBg
import com.kotlin.easyrent.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint
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
                val navController = rememberNavController()
                val sharedViewModel: SharedViewModel = hiltViewModel()
                val context = LocalContext.current
                val currentLanguage = sharedViewModel.currentLanguage.collectAsState().value

                val isLoggedIn = firebaseAuth.currentUser != null

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
}

