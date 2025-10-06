package com.example.daytask.navigation.graph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.daytask.navigation.destinations.Destinations
import com.example.daytask.presentation.authentication.presentation.Login
import com.example.daytask.presentation.authentication.presentation.events.LoginEvent
import com.example.daytask.presentation.authentication.domain.model.LoginState
import com.example.daytask.presentation.authentication.presentation.AuthenticationViewModel
import com.example.daytask.presentation.authentication.presentation.Register
import com.example.daytask.presentation.authentication.presentation.events.RegisterEvent
import com.example.daytask.presentation.authentication.domain.model.RegisterState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.authenticationGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope
) {

    navigation<Destinations.Auth>(
        startDestination = Destinations.Auth.Login
    ) {

        composable<Destinations.Auth.Login>{
            Login(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                navigateToRegister = {
                    navController.navigate(Destinations.Auth.Register) {
                        launchSingleTop = true
                    }
                },
                loginSuccess = {
                    navController.navigate(Destinations.App.HomeScaffold) {
                        popUpTo<Destinations.Splash>() {
                            inclusive = true
                        }
                        popUpTo<Destinations.Auth> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Destinations.Auth.Register>{
            Register(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                navigateToLogin = {
                    navController.popBackStack()
                },
                registerSuccess = {
                    navController.navigate(Destinations.App.HomeScaffold) {
                        popUpTo<Destinations.Splash>() {
                            inclusive = true
                        }
                        popUpTo<Destinations.Auth> {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}