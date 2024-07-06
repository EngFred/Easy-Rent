package com.kotlin.easyrent.features.auth.ui.screens.login

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
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
import coil.ImageLoader
import coil.compose.AsyncImage
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.theme.SetSystemBarColor
import com.kotlin.easyrent.core.theme.myBackground
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.auth.ui.components.AuthTextField
import com.kotlin.easyrent.features.auth.ui.viewModel.LoginViewModel
import com.kotlin.easyrent.utils.Constants
import com.kotlin.easyrent.utils.getImageRequest

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    onSignupClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsState().value

    SetSystemBarColor(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent
    )

    LaunchedEffect( uiState.loginError ) {
        if( uiState.loginError != null ) {
            Toast.makeText(context, uiState.loginError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect( uiState.loginSuccess ) {
        if( uiState.loginSuccess ) {
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
                .padding(10.dp)
                .align(Alignment.Center)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 13.dp, vertical = 13.dp),
                text = stringResource(id = R.string.login),
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
                    placeHolder = stringResource(id = R.string.email),
                    onTextChange = {
                        viewModel.onEvent(LoginUiEvents.EmailChanged(it))
                    },
                    value = { uiState.email },
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthTextField(
                    placeHolder = stringResource(id = R.string.password),
                    onTextChange = {
                         viewModel.onEvent(LoginUiEvents.PasswordChanged(it))
                    },
                    value = { uiState.password },
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.size(8.dp))
                MyButton(
                    text = stringResource(id = R.string.continue_text),
                    onClick = {
                         if( !uiState.isLoading ) {
                             viewModel.onEvent(LoginUiEvents.ConfirmButtonClicked)
                         }
                    },
                    isLoading = { uiState.isLoading },
                    enabled = uiState.isFormValid
                )
                Spacer(modifier = Modifier.size(16.dp))
                ClickableText(text = buildAnnotatedString {
                    append(stringResource(id = R.string.forgot_password))
                }, onClick = {

                }, style = TextStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold,
                    color = myPrimary
                ))
                Spacer(modifier = Modifier.size(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.dont_have_an_account_yet_message),
                        color = Color.White,
                        fontFamily = poppins,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold )
                    Spacer(modifier = Modifier.width(5.dp))
                    ClickableText(text = buildAnnotatedString {
                        append(stringResource(id = R.string.sign_up))
                    }, onClick = {
                        onSignupClick()
                    }, style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold,
                        color = myPrimary
                    ))
                }
                
            }
        }

    }
}