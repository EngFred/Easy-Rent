package com.kotlin.easyrent.home.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.theme.myBackground
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.utils.getImageRequest

@Composable
fun DrawerHeader(
    modifier: Modifier,
    userProfilePhotoUrl: String?,
    userName: String?
) {

    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(15.dp)
            .fillMaxWidth()
    ) {

        if ( userProfilePhotoUrl != null ) {
            val imageRequest = getImageRequest(userProfilePhotoUrl, context)
            AsyncImage(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(myBackground),
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painterResource(id = R.drawable.profile_picture),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = userName ?: stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            fontFamily = poppinsBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}