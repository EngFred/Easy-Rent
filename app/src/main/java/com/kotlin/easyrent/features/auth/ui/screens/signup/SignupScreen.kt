package com.kotlin.easyrent.features.auth.ui.screens.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.theme.EasyRentTheme
import com.kotlin.easyrent.core.theme.SetSystemBarColor
import com.kotlin.easyrent.core.theme.myBackground
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.auth.ui.components.AuthTextField
import com.kotlin.easyrent.utils.showDatePickerDialog

@Composable
fun Signup(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    SetSystemBarColor(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent
    )


    val context = LocalContext.current
    var selectedDOB by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        //the background image
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(myBackground.copy(alpha = .6f))){}

        //the content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 50.dp, bottom = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 13.dp, vertical = 13.dp),
                text = stringResource(id = R.string.sign_up),
                fontFamily = poppinsBold,
                fontSize = 28.sp,
                color = Color.White
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(myBackground.copy(alpha = .7f))
                    .padding(22.dp),
            ) {

                AuthTextField(
                    placeHolder = stringResource(id = R.string.last_name),
                    onTextChange = {},
                    value = { "" }
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.first_name),
                    onTextChange = {},
                    value = { "" }
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.email),
                    onTextChange = {},
                    value = { "" }
                )
                Spacer(modifier = Modifier.size(8.dp))
                DobComponent(
                    onSelectDOB = {
                        Toast.makeText(context, "Pick date", Toast.LENGTH_SHORT).show()
                        showDatePickerDialog(
                            context,
                            onSelectDOB = {
                                selectedDOB = it
                            }
                        )
                    },
                    selectedDDOB = { selectedDOB }
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.phone_number),
                    onTextChange = {},
                    value = { "" }
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = "${stringResource(id = R.string.address)} (${stringResource(id = R.string.optional)})",
                    onTextChange = {},
                    value = { "" }
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.password),
                    onTextChange = {},
                    value = { "" },
                    isPassword = true
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.confirm_password),
                    onTextChange = {},
                    value = { "" },
                    isPassword = true
                )
                Spacer(modifier = Modifier.size(8.dp))
                MyButton(
                    text = stringResource(id = R.string.continue_text),
                    onClick = { onHomeClick() },
                    isLoading = { false }
                )
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.already_have_an_account_message),
                        color = Color.White,
                        fontFamily = poppins,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold )
                    Spacer(modifier = Modifier.width(5.dp))
                    ClickableText(text = buildAnnotatedString {
                        append(stringResource(id = R.string.login))
                    }, onClick = {
                        onLoginClick()
                    }, style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold,
                        color = myPrimary
                    )
                    )
                }

            }
        }

    }
}

@Preview( showBackground = true )
@Composable
private fun LoginScreenPreview() {
    EasyRentTheme {
        Signup(onHomeClick = { /*TODO*/ }) {
            
        }
    }

}


@Composable
private fun DobComponent(
    onSelectDOB: () -> Unit,
    selectedDDOB: () -> String
) {

    OutlinedTextField(
        value = selectedDDOB(),
        readOnly = true,
        enabled = false,
        shape = RoundedCornerShape(8.dp),
        onValueChange = {},
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            errorContainerColor = Color.White,
            disabledContainerColor = Color.White,
            disabledTextColor = Color.DarkGray,
            disabledLabelColor = Color.DarkGray,
            disabledPlaceholderColor = Color.DarkGray,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = myPrimary
        ),
        trailingIcon = {
            IconButton(onClick = { onSelectDOB() } ) {
                Icon(imageVector = Icons.Rounded.DateRange, contentDescription = null, tint = myPrimary)
            }
        },
        textStyle = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Bold
        ),
        maxLines = 1,
        label = {
            Text(text = "${stringResource(id = R.string.date_of_birth)} (${stringResource(id = R.string.optional)})",
                fontFamily = poppins, fontWeight = FontWeight.Bold)
                },
        modifier = Modifier.fillMaxWidth(),
    )
}