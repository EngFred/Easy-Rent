package com.kotlin.easyrent.home

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun RadioButtonTile(
    selected: () -> Boolean,
    onClick: () -> Unit,
    @StringRes
    label: Int,
    textColor: Color
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(0.dp, Color.Transparent),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected(), onClick = onClick, colors = RadioButtonDefaults.colors(
                selectedColor = textColor,
            ))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = label), color = textColor, fontFamily = poppins, fontWeight = FontWeight.Bold)
        }
    }
    HorizontalDivider()
}