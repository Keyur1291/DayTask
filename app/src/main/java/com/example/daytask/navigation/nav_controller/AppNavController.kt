package com.example.daytask.navigation.nav_controller

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.daytask.data.local.model.UserState
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.ProjectState
import com.example.daytask.navigation.destinations.Destinations
import com.example.daytask.presentation.home.Home
import com.example.daytask.presentation.chat.ChatList
import com.example.daytask.presentation.notifications.Notifications
import com.example.daytask.presentation.shedule.Schedule
import okhttp3.Route

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavController(
    navController: NavHostController,
    parentNavController: NavController,
    userState: UserState,
    projectState: ProjectState,
    onProjectEvent: (ProjectEvent) -> Unit,
    paddings: PaddingValues,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    NavHost(
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        navController = navController,
        startDestination = Destinations.App.HomeScaffold.Home
    ) {

        composable<Destinations.App.HomeScaffold.Home> {
            Home(
                modifier = Modifier,
                paddings = paddings,
                userState = userState,
                projectState = projectState,
                projectEvent = onProjectEvent,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                navigateToProjectDetail = { projectWithTasks ->
                    parentNavController.navigate(Destinations.App.ProjectDetails(projectWithTasks))
                },
                navigateToProfile = {
                    parentNavController.navigate(Destinations.App.Profile)
                },
                navigateToCompletedProjects = {
                    parentNavController.navigate(Destinations.App.CompletedProjects) {
                        launchSingleTop = true
                    }
                },
                navigateToIncompleteProjects = {
                    parentNavController.navigate(Destinations.App.InCompleteProjects) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Destinations.App.HomeScaffold.Chat> {
            ChatList(
                modifier = Modifier,
                paddings = paddings,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                navigateToPersonalChat = {
                    parentNavController.navigate(Destinations.App.PersonalChat)
                },
                navigateUp = { navController.popBackStack() }
            )
        }

        composable<Destinations.App.HomeScaffold.Calendar> {
            Schedule(
                modifier = Modifier,
                paddings = paddings,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                projectState = projectState,
                navigateUp = { navController.popBackStack() }
            )
        }

        composable<Destinations.App.HomeScaffold.Notification> {
            Notifications(
                paddings = paddings,
                modifier = Modifier,
                navigateUp = { navController.popBackStack() }
            )
        }
    }
}