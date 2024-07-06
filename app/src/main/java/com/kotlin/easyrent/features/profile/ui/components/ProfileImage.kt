package com.kotlin.easyrent.features.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kotlin.easyrent.core.theme.EasyRentTheme
import com.kotlin.easyrent.core.theme.springGreen
import com.kotlin.easyrent.utils.getImageRequest

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {

    val context = LocalContext.current
    val imageRequest = getImageRequest(imageUrl, context)

    Box{
        AsyncImage(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            model = imageRequest,
            contentDescription = null,
            contentScale = ContentScale.Crop

        )
        Box(modifier = Modifier
            .align(Alignment.TopEnd).padding(top = 7.dp, end = 7.dp)
            .size(28.dp)
            .clip(CircleShape)
            .background(springGreen)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePreview() {
    EasyRentTheme {
        ProfileImage(imageUrl = "")
    }
}