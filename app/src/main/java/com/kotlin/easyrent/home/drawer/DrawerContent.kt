package com.kotlin.easyrent.home.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.routes.HomeRoutes
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun DrawerContent(
    route: String,
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToTenants: () -> Unit,
    navigateToPayments: () -> Unit,
    navigateToExpenses: () -> Unit,
    navigateToRentals: () -> Unit,
    closeDrawer: () -> Unit,
    userProfilePhotoUrl: String?,
    userName: String?
) {

    ModalDrawerSheet(modifier = Modifier) {
        DrawerHeader(modifier, userProfilePhotoUrl, userName)
        Spacer(modifier = Modifier.padding(5.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 13.dp)
        ) {
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(id = R.string.home),
                        fontFamily = poppins, fontWeight = FontWeight.Bold
                    )
                },
                selected = route == HomeRoutes.Dashboard.destination || route == HomeRoutes.Profile.destination,
                onClick = {
                    navigateToHome()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )

            NavigationDrawerItem(
                label = { Text(
                    text = stringResource(id = R.string.tenants),
                    fontFamily = poppins, fontWeight = FontWeight.Bold
                ) },
                selected = route == HomeRoutes.Tenants.destination || route.contains(HomeRoutes.TenantUpsert.destination),
                onClick = {
                    navigateToTenants()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.People, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )

            NavigationDrawerItem(
                label = { Text(
                    text = stringResource(id = R.string.payments),
                    fontFamily = poppins, fontWeight = FontWeight.Bold
                ) },
                selected = route == HomeRoutes.Payments.destination || route.contains(HomeRoutes.PaymentUpsert.destination),
                onClick = {
                    navigateToPayments()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.Money, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )

            NavigationDrawerItem(
                label = { Text(
                    text = stringResource(id = R.string.expenses),
                    fontFamily = poppins, fontWeight = FontWeight.Bold
                ) },
                selected = route == HomeRoutes.Expenses.destination || route.contains(HomeRoutes.ExpenseUpsert.destination),
                onClick = {
                    navigateToExpenses()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.Receipt, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )

            NavigationDrawerItem(
                label = { Text(
                    text = stringResource(id = R.string.rentals),
                    fontFamily = poppins, fontWeight = FontWeight.Bold
                ) },
                selected = route == HomeRoutes.Rentals.destination || route.contains(HomeRoutes.RentalUpsert.destination),
                onClick = {
                    navigateToRentals()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.HomeWork, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )
        }
    }
}