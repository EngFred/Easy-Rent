package com.kotlin.easyrent.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.HomeRoutes
import com.kotlin.easyrent.core.theme.myBackground
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.home.bottomBar.BottomBar
import com.kotlin.easyrent.home.drawer.DrawerContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    onLogout: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var showChooseLanguageDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showConfirmationDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedLanguage by rememberSaveable {
        mutableStateOf("")
    }

    val currentLanguage = sharedViewModel.currentLanguage.collectAsState().value
    val landlord = sharedViewModel.loggedInUser.collectAsState().value
    val context = LocalContext.current

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: HomeRoutes.Dashboard.destination


    if ( showChooseLanguageDialog ) {
        ShowChangeLanguageDialog(
            onDismissRequest = { showChooseLanguageDialog = false },
            currentLanguage = currentLanguage ?: Language.English.name,
            setLanguageAsEnglish = {
                selectedLanguage = Language.English.name
                showChooseLanguageDialog = false
                showConfirmationDialog = true
            },
            setLanguageAsSwahili = {
                selectedLanguage = Language.Swahili.name
                showChooseLanguageDialog = false
                showConfirmationDialog = true
            },
            setLanguageAsLuganda = {
                selectedLanguage = Language.Luganda.name
                showChooseLanguageDialog = false
                showConfirmationDialog = true
            }
        )
    }

    if ( showConfirmationDialog ) {
        ShowConfirmationDialog(
            onDismiss = {
                showConfirmationDialog = false
            },
            onConfirm = {
                showConfirmationDialog = false
                sharedViewModel.setLanguage(selectedLanguage, context)
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                route = currentRoute,
                navigateToHome = {
                    navController.navigate(HomeRoutes.Dashboard.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToTenants = {
                    navController.navigate(HomeRoutes.Tenants.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToPayments = {
                    navController.navigate(HomeRoutes.Payments.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToExpenses = {
                    navController.navigate(HomeRoutes.Expenses.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToRentals = {
                    navController.navigate(HomeRoutes.Rentals.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, closeDrawer = {
                    coroutineScope.launch { drawerState.close() }
                }, userProfilePhotoUrl = landlord?.profileImage, userName = if (landlord != null) "${landlord.lastName} ${landlord.firstName}" else null)
        }
    ) {
        val title = when (currentRoute) {
            HomeRoutes.Tenants.destination -> stringResource(id = R.string.tenants)
            HomeRoutes.Payments.destination -> stringResource(id = R.string.payments)
            HomeRoutes.Expenses.destination -> stringResource(id = R.string.expenses)
            HomeRoutes.Rentals.destination -> stringResource(id = R.string.rentals)
            else -> "EasyRent"
        }
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.padding(end = 10.dp),
                    title = { Text(text = title, fontFamily = poppinsBold) },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Rounded.Menu, contentDescription = null )
                        }
                    },
                    actions = {
                        if ( currentRoute == HomeRoutes.Profile.destination ) {
                            IconButton(onClick = {
                                showChooseLanguageDialog = !showChooseLanguageDialog
                            }) {
                                Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                            }
                        }
                    }
                )
            },
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            HomeNavGraph(
                navController = navController,
                sharedViewModel = sharedViewModel,
                modifier = Modifier.padding(innerPadding),
                onLogout = onLogout
            )
        }
    }
}

@Composable
fun ShowChangeLanguageDialog(
    onDismissRequest: () -> Unit,
    textColor: Color = myBackground,
    currentLanguage: String,
    setLanguageAsEnglish: () -> Unit,
    setLanguageAsSwahili: () -> Unit,
    setLanguageAsLuganda: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.choose_language), fontFamily = poppinsBold)
        },
        text = {
            Column {
                RadioButtonTile(
                    textColor = textColor,
                    selected = { currentLanguage.lowercase() == Language.English.name.lowercase()  },
                    onClick = {
                        if (currentLanguage.lowercase() != Language.English.name.lowercase() ) {
                            setLanguageAsEnglish()
                        } else{
                            onDismissRequest()
                        }
                    },
                    label = R.string.english,
                )
                RadioButtonTile(
                    textColor = textColor,
                    selected = { currentLanguage.lowercase() == Language.Swahili.name.lowercase()  },
                    onClick = {
                        if (currentLanguage.lowercase() != Language.Swahili.name.lowercase() ) {
                            setLanguageAsSwahili()
                        } else{
                            onDismissRequest()
                        }
                    },
                    label = R.string.swahili
                )
                RadioButtonTile(
                    textColor = textColor,
                    selected = { currentLanguage.lowercase() == Language.Luganda.name.lowercase()  },
                    onClick = {
                        if (currentLanguage.lowercase() != Language.Luganda.name.lowercase() ) {
                            setLanguageAsLuganda()

                        } else{
                            onDismissRequest()
                        }
                    },
                    label = R.string.luganda
                )
            }
        },
        onDismissRequest = onDismissRequest, confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}


@Composable
fun ShowConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.restart_app))
        },
        text = {
            Text(stringResource(R.string.restart_app_message))
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}




