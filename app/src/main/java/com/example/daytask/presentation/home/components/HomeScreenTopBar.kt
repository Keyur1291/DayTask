package com.example.daytask.presentation.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.daytask.R
import com.example.daytask.presentation.ui.theme.splashFontFamily
import com.example.daytask.presentation.ui.theme.yellowColor
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier,
    user: FirebaseUser?,
    navigateToProfile: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TopBarGreetingText(
                    modifier = Modifier,
                    text = "Welcome back!"
                )
                TopBarUserNameText(
                    modifier = Modifier,
                    userName = user?.displayName ?: "Anonymous"
                )
            }
            AsyncImage(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("userProfile"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ).clip(CircleShape).size(48.dp)
                    .clickable { navigateToProfile() },
                clipToBounds = true,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                error = painterResource(R.drawable.profile_image),
                model = user?.photoUrl ?: ""
            )

        }
    }
}

@Composable
fun TopBarGreetingText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        fontSize = 12.sp,
        color = yellowColor,
        text = text
    )
}

@Composable
fun TopBarUserNameText(
    modifier: Modifier = Modifier,
    userName: String
) {
    Text(
        modifier = modifier,
        fontFamily = splashFontFamily,
        fontSize = 22.sp,
        color = Color.White,
        text = userName
    )
}