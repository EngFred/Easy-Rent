package com.kotlin.easyrent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kotlin.easyrent.core.graphs.RootGraph
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.theme.EasyRentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyRentTheme {
                val navController = rememberNavController()
                val sharedViewModel: SharedViewModel = hiltViewModel()

                RootGraph(sharedViewModel = sharedViewModel, navController = navController)
            }
        }
    }
}