package com.kotlin.easyrent.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.core.theme.myPrimary

@Composable
fun SyncIndicator(
    modifier: Modifier = Modifier,
    isSynced: Boolean
) {
    Box(modifier = modifier
        .size(15.dp)
        .clip(CircleShape)
        .background(
            if (isSynced) myPrimary else MaterialTheme.colorScheme.error
        )
    )
}