package com.kotlin.easyrent.features.dashboard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold

@Composable
fun KeyMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    valueSize: TextUnit = 25.sp
) {
    ElevatedCard(modifier = modifier.padding(8.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontFamily = poppins, fontWeight = FontWeight.ExtraBold, maxLines = 1)
            Text(value, fontFamily = poppinsBold, fontSize = valueSize, maxLines = 1)
        }
    }
}