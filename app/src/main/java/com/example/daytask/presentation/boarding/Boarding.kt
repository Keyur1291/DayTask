package com.example.daytask.presentation.boarding

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.boarding.components.SplashLargeText
import com.example.daytask.presentation.boarding.components.SplashSmallText

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Boarding(
    modifier: Modifier = Modifier,
    navigateToLoginScreen: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
            ) {

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("splashLogo"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("loginLogo"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    ) {
                        Image(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .width(61.dp)
                                .height(47.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.ic_logo)
                        )
                        SplashSmallText(
                            modifier = Modifier.animateEnterExit(
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { -(2*it) }
                            )
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .background(Color.White)
                        ) {
                            Image(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                contentDescription = null,
                                painter = painterResource(R.drawable.ic_splash_bg)
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        SplashLargeText(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                        )
                        Spacer(Modifier.height(24.dp))
                        ReusableLargeButton(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .navigationBarsPadding()
                                .fillMaxWidth()
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("loginButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ),
                            onClick = navigateToLoginScreen,
                            buttonText = "Let's Start"
                        )
                    }
                }
            }
        }
    }
}