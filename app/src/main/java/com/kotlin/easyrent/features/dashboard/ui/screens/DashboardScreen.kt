package com.kotlin.easyrent.features.dashboard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.dashboard.ui.components.KeyMetricCard
import com.kotlin.easyrent.features.dashboard.ui.viewModel.DashboardViewModel
import com.kotlin.easyrent.utils.formatCurrencyWithNoUGX

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {

    val uiState = dashboardViewModel.uiState.collectAsState().value

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    KeyMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Revenue(UGX)",
                        value = formatCurrencyWithNoUGX(uiState.expectedRevenue)
                    )
                    KeyMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Payments(UGX)",
                        value = formatCurrencyWithNoUGX(uiState.totalPayments)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val monthlyProfit = uiState.expectedRevenue-uiState.totalExpenses
                    val dailyProfits = uiState.totalPayments-uiState.totalExpenses
                    KeyMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Expenses(UGX)",
                        value = formatCurrencyWithNoUGX(uiState.totalExpenses)
                    )
                    KeyMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Profits(UGX)",
                        value = formatCurrencyWithNoUGX(monthlyProfit)
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 10.dp)
        ) {
            val tenants = if(uiState.totalTenants in 0..9) "0${uiState.totalTenants}" else uiState.totalTenants.toString()
            val rentals = if(uiState.totalRentals in 0..9) "0${uiState.totalRentals}" else uiState.totalRentals.toString()
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("Rentals: ")
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = poppinsBold,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(rentals)
                }
            })
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("Tenants: ")
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = poppinsBold,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(tenants)
                }
            })
        }
    }
}