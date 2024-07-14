package com.kotlin.easyrent.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun SyncIndicator(
    modifier: Modifier = Modifier,
    isSynced: Boolean,
    unpaidMonths: Int = 0
) {
    Box(modifier = modifier
        .size(30.dp)
        .clip(CircleShape)
        .background(
            if (isSynced) myPrimary else MaterialTheme.colorScheme.error
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$unpaidMonths",
            fontFamily = poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}