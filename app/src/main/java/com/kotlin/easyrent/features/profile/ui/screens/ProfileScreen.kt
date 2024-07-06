package com.kotlin.easyrent.features.profile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.profile.ui.components.ProfileImage
import com.kotlin.easyrent.utils.Constants

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel
) {

    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileImage(imageUrl = Constants.DEFAULT_PROFILE_PICTURE)
        Text(text = "Engineer Fred", fontFamily = poppinsBold, fontSize = 24.sp, maxLines = 2)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "say something about yourself...", fontFamily = poppins, fontWeight = FontWeight.ExtraBold)
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }
        }
        
        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "ACCOUNT INFORMATION",
            fontFamily = poppinsBold,
            color = Color.Gray,
            maxLines = 1
        )

        Spacer(modifier = Modifier.size(7.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                AccountDetail(
                    detail = "Email.",
                    value = "omongolealfred4@gmail.com"
                )
                HorizontalDivider()
                AccountDetail(
                    detail = "Tel no.",
                    value = "0777604972"
                )
                HorizontalDivider()
                AccountDetail(
                    detail = "Address.",
                    value = "Kisaasi"
                )
                HorizontalDivider()
                AccountDetail(
                    detail = "DOB.",
                    value = "16/09/2000"
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.weight(1f))
        MyButton(
            text = "Log out",
            onClick = { /*TODO*/ },
            isLoading = { false },
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            textColor = MaterialTheme.colorScheme.error
        )

    }
}

@Composable
private fun AccountDetail(
    detail: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = detail, fontFamily = poppins, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = value,
            fontFamily = poppins,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

