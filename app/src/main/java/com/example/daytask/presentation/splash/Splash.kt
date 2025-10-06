package com.example.daytask.presentation.splash

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.domain.UserEvent
import com.example.daytask.presentation.boarding.components.SplashSmallText
import com.example.daytask.presentation.ui.theme.yellowColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Splash(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToBoarding: () -> Unit,
    onUserEvent: (UserEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val firebaseAuth = FirebaseAuth.getInstance()

    LaunchedEffect(true) {
        delay(2000)
        if(firebaseAuth.currentUser != null) {
            onUserEvent(UserEvent.UpdateUser(firebaseAuth.currentUser))
            navigateToHome()
        } else {
            navigateToBoarding()
        }
    }

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .animateEnterExit(
                            enter = slideInHorizontally { it },
                            exit = slideOutHorizontally { -it }
                        )
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("splashLogo"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                ) {
                    Image(
                        contentDescription = null,
                        painter = painterResource(R.drawable.ic_logo),
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { -(2*it) }
                            )
                            .width(300.dp)
                            .height(200.dp)
                    )
                    SplashSmallText(
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { -(2*it) }
                            )
                            .offset(y = -(20).dp),
                        textSize = 32
                    )
                    Spacer(Modifier.height(16.dp))
                }
                CircularProgressIndicator(
                    trackColor = yellowColor.copy(0.2f),
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .animateEnterExit(
                            enter = slideInHorizontally { it },
                            exit = slideOutHorizontally { -(4*it) }
                        )
                        .size(50.dp),
                    color = yellowColor
                )
            }
        }
    }
}