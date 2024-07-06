package com.kotlin.easyrent.features.auth.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.core.theme.EasyRentTheme
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    placeHolder: String,
    onTextChange: (String) -> Unit,
    value: () -> String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    isError: () -> Boolean? = { null },
    errorMessage: () -> Int? = { null },
    isLogin: Boolean = true,
) {

    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None

    if (!isLogin) {
        OutlinedTextField(
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
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = if (passwordVisibility) {
                            Icons.Rounded.Visibility
                        } else {
                            Icons.Rounded.VisibilityOff
                        }, contentDescription = null, tint = myPrimary)
                    }
                }
            },
            textStyle = TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            label = { Text(text = placeHolder,fontFamily = poppins, fontWeight = FontWeight.Bold) },
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            visualTransformation = visualTransformation
        )
    } else {
        OutlinedTextField(
            value = value(),
            isError = isError() ?: false,
            shape = RoundedCornerShape(8.dp),
            onValueChange = onTextChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = if (passwordVisibility) {
                            Icons.Rounded.Visibility
                        } else {
                            Icons.Rounded.VisibilityOff
                        }, contentDescription = null, tint = myPrimary)
                    }
                }
            },
            textStyle = TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            label = { Text(text = placeHolder,fontFamily = poppins, fontWeight = FontWeight.Bold) },
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            visualTransformation = visualTransformation
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthTextFieldPreview() {
    EasyRentTheme(
        darkTheme = false
    ) {
        AuthTextField(
            placeHolder = "Email",
            onTextChange = {},
            value = { "" },
            isError = {false}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthTextFieldPreview2() {
    EasyRentTheme(
        darkTheme = false
    ) {
        AuthTextField(
            placeHolder = "Password",
            onTextChange = {},
            value = { "" },
            isPassword = true,
            isError = {false}
        )
    }
}