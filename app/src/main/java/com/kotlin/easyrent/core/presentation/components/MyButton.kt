package com.kotlin.easyrent.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.core.theme.EasyRentTheme
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    cornerSize: Dp = 8.dp,
    height: Dp = 75.dp,
    backgroundColor: Color = myPrimary,
    isLoading: () -> Boolean,
    enabled: Boolean = true
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(cornerSize)
    ) {

        if (!isLoading()) {
            Text(
                text,
                color = Color.White,
                fontFamily = poppinsBold,
                fontWeight = FontWeight.ExtraBold
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color.LightGray)
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun MyButtonPreview() {
    EasyRentTheme(
        darkTheme = false
    ) {
        MyButton(text = "SignIn", onClick = {  }, isLoading = {false})
    }
}