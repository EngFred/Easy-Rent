package com.kotlin.easyrent

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kotlin.easyrent.core.graphs.RootGraph
import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.theme.EasyRentTheme
import com.kotlin.easyrent.utils.setLocale
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
                val context = LocalContext.current
                val currentLanguage = sharedViewModel.currentLanguage.collectAsState().value

                if (currentLanguage == null){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LaunchedEffect(currentLanguage) {
                        Log.e("$", "The current app language is $currentLanguage")
                    }
                    val language = Language.entries.find { it.name.lowercase() == currentLanguage }  ?: Language.English
                    setLocale(language = language, context = context)
                }
                RootGraph(sharedViewModel = sharedViewModel, navController = navController)
            }
        }
    }
}

