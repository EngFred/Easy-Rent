package com.kotlin.easyrent.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    label: String,
    onTextChange: (String) -> Unit,
    value: () -> String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isError: () -> Boolean? = { null },
    errorMessage: () -> Int? = { null }
) {
    TextField(
        value = value(),
        isError = isError() ?: false,
        shape = RoundedCornerShape(8.dp),
        supportingText = {
            if (isError() == true && errorMessage() != null) {
                Text(text = stringResource(id = errorMessage()!!),
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        onValueChange = onTextChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            focusedIndicatorColor = myPrimary,
            cursorColor = myPrimary
        ),
        textStyle = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Bold
        ),
        maxLines = 1,
        label = { Text(text = label,fontFamily = poppins, fontWeight = FontWeight.Bold) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
    )


}