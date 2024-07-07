package com.kotlin.easyrent.features.auth.ui.screens.signup

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.theme.SetSystemBarColor
import com.kotlin.easyrent.core.theme.myBackground
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.auth.ui.components.AuthTextField
import com.kotlin.easyrent.features.auth.ui.viewModel.SignupViewModel
import com.kotlin.easyrent.utils.Constants
import com.kotlin.easyrent.utils.formatDateToString
import com.kotlin.easyrent.utils.getImageRequest
import com.kotlin.easyrent.utils.showDatePickerDialog

@Composable
fun Signup(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: SignupViewModel = hiltViewModel()
) {

    SetSystemBarColor(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent
    )

    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect( uiState.signupError ) {
        if ( uiState.signupError != null ) {
            Toast.makeText(context, uiState.signupError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect( uiState.signupSuccess ) {
        if ( uiState.signupSuccess ) {
            navigateToHome()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        val imageRequest = getImageRequest(Constants.BG_IMAGE, context)
        //the background image
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .background(myBackground),
            model = imageRequest,
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
                .padding(start = 10.dp, end = 10.dp, top = 40.dp, bottom = 40.dp)
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
                    onTextChange = {
                        viewModel.onEvent(SignupUiEvents.LastNameChanged(it))
                    },
                    value = { uiState.lastName },
                    isLogin = false,
                    errorMessage = { uiState.lastNameError },
                    isError = { uiState.lastNameError != null }
                )
                Spacer(modifier = Modifier.size(4.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.first_name),
                    onTextChange = {
                        viewModel.onEvent(SignupUiEvents.FirstNameChanged(it))
                    },
                    value = { uiState.firstName },
                    isLogin = false,
                    errorMessage = { uiState.firstNameError },
                    isError = { uiState.firstNameError != null }
                )
                Spacer(modifier = Modifier.size(4.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.email),
                    onTextChange = {
                        viewModel.onEvent(SignupUiEvents.EmailChanged(it))
                    },
                    value = { uiState.email },
                    isLogin = false,
                    errorMessage = { uiState.emailError },
                    isError = { uiState.emailError != null },
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.size(4.dp))
                DobComponent(
                    onSelectDOB = {
                        showDatePickerDialog(
                            context,
                            onSelectDOB = {
                                viewModel.onEvent(SignupUiEvents.DOBChanged(it))
                            }
                        )
                    },
                    selectedDDOB = {
                        if ( uiState.dob != null ) {
                            formatDateToString(uiState.dob)
                        } else ""
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.phone_number),
                    onTextChange = {
                        viewModel.onEvent(SignupUiEvents.ContactNumberChanged(it))
                    },
                    value = { uiState.contactNumber },
                    isLogin = false,
                    errorMessage = { uiState.contactNumberError },
                    isError = { uiState.contactNumberError != null },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.size(4.dp))
                AuthTextField(
                    placeHolder = "${stringResource(id = R.string.address)} (${stringResource(id = R.string.optional)})",
                    onTextChange = {
                          viewModel.onEvent(SignupUiEvents.AddressChanged(it))
                    },
                    value = { uiState.address ?: "" }
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.password),
                    onTextChange = {
                        viewModel.onEvent(SignupUiEvents.PasswordChanged(it))
                    },
                    value = { uiState.password },
                    isPassword = true,
                    isLogin = false,
                    errorMessage = { uiState.passwordError },
                    isError = { uiState.passwordError != null },
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.size(4.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.confirm_password),
                    onTextChange = {
                        viewModel.onEvent(SignupUiEvents.ConfirmPasswordChanged(it))
                    },
                    value = { uiState.confirmPassword },
                    isPassword = true,
                    isLogin = false,
                    errorMessage = { uiState.confirmPasswordError },
                    isError = { uiState.confirmPasswordError != null },
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.size(8.dp))
                MyButton(
                    text = stringResource(id = R.string.continue_text),
                    onClick = {
                        if ( !uiState.signupInProgress ) {
                            viewModel.onEvent(SignupUiEvents.ConfirmButtonClicked)
                        }
                    },
                    isLoading = { uiState.signupInProgress },
                    enabled = uiState.isValidForm
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