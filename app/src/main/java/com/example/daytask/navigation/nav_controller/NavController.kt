package com.example.daytask.navigation.nav_controller

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daytask.data.local.model.UserState
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.ProjectState
import com.example.daytask.domain.UserEvent
import com.example.daytask.navigation.destinations.Destinations
import com.example.daytask.navigation.graph.appGraph
import com.example.daytask.navigation.graph.authenticationGraph
import com.example.daytask.presentation.ProjectViewModel
import com.example.daytask.domain.TaskEvent
import com.example.daytask.domain.TaskState
import com.example.daytask.presentation.boarding.Boarding
import com.example.daytask.presentation.splash.Splash
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavController(
    modifier: Modifier = Modifier,
    projectViewModel: ProjectViewModel,
    projectState: ProjectState,
    onProjectEvent: (ProjectEvent) -> Unit,
    projectValidationEvent: Flow<ProjectViewModel.ValidationEvent>,
    taskState: TaskState,
    onTaskEvent: (TaskEvent) -> Unit,
    taskValidationEvent: Flow<ProjectViewModel.ValidationEvent>,
    onUserEvent: (UserEvent) -> Unit,
    userState: UserState,
) {

    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Destinations.Splash
        ) {

            composable<Destinations.Splash> {
                Splash(
                    onUserEvent = onUserEvent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    navigateToHome = {
                        navController.navigate(Destinations.App.HomeScaffold) {
                            restoreState = false
                            popUpTo<Destinations.Splash> {
                                saveState = false
                                inclusive = true
                            }
                        }
                    },
                    navigateToBoarding = {
                        navController.navigate(Destinations.Boarding) {
                            popUpTo<Destinations.Splash> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Destinations.Boarding> {
                Boarding(
                    modifier = Modifier,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    navigateToLoginScreen = {
                        navController.navigate(Destinations.Auth.Login) {
                            launchSingleTop = true
                        }
                    },
                )
            }

            authenticationGraph(
                sharedTransitionScope = this@SharedTransitionLayout,
                navController = navController,
            )

            appGraph(
                navController = navController,
                sharedTransitionScope = this@SharedTransitionLayout,
                projectValidationEvent = projectValidationEvent,
                taskValidationEvent = taskValidationEvent,
                userState = userState,
                onUserEvent = onUserEvent,
                projectViewModel = projectViewModel,
                projectState = projectState,
                onProjectEvent = onProjectEvent,
                taskState = taskState,
                onTaskEvent = onTaskEvent,
            )
        }
    }
}