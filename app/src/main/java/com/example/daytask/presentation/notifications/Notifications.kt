package com.example.daytask.presentation.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytask.R
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notifications(
    modifier: Modifier = Modifier,
    paddings: PaddingValues,
    navigateUp: () -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = paddings
    ) {

        item {
            CenterAlignedTopAppBar(
                title = { Text(color = Color.White, text = "Notifications") },
                modifier = Modifier,
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp
                    ) {
                        Icon(
                            tint = Color.White,
                            contentDescription = null,
                            painter = painterResource(R.drawable.arrowleft)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = splashBgColor
                )
            )
        }

        item {
            Text(
                color =Color.White,
                modifier = Modifier.padding(20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                text = "New"
            )
        }

        items(4){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier.size(45.dp).weight(1f),
                    contentDescription = null,
                    painter = painterResource(R.drawable.profile_image)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    maxLines = 4,
                    modifier = Modifier,
                    fontSize = 14.sp,
                    text = buildAnnotatedString {
                        val text = "Olivia Anna left a comment in task\nDayTask App"
                        append(text)
                        addStyle(
                            SpanStyle(color = Color.White, fontWeight = FontWeight.SemiBold),
                            text.indexOf("Olivia"), text.length
                        )
                        addStyle(
                            SpanStyle(color = Color.White.copy(0.5f), fontWeight = FontWeight.Normal),
                            text.indexOf("left"), text.length
                        )
                        addStyle(
                            SpanStyle(color = yellowColor),
                            text.indexOf("DayTask"), text.length
                        )
                    }
                )
                Text(
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 8.sp,
                    text = "31 min"
                )
            }
        }

        item {
            Text(
                color =Color.White,
                modifier = Modifier.padding(20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                text = "Earlier"
            )
        }

        items(4) {

        }
    }
}