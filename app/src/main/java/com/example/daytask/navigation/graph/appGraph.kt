package com.example.daytask.navigation.graph

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.data.local.model.Task
import com.example.daytask.data.local.model.UserState
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.ProjectState
import com.example.daytask.domain.UserEvent
import com.example.daytask.navigation.destinations.Destinations
import com.example.daytask.navigation.navigation_type.CustomNavType
import com.example.daytask.presentation.ProjectViewModel
import com.example.daytask.presentation.add_project.AddProject
import com.example.daytask.presentation.add_task.presentation.AddTask
import com.example.daytask.domain.TaskEvent
import com.example.daytask.domain.TaskState
import com.example.daytask.presentation.home.CompletedProjects
import com.example.daytask.presentation.home.IncompleteProjects
import com.example.daytask.presentation.home_scaffold.HomeScaffold
import com.example.daytask.presentation.chat.PersonalChat
import com.example.daytask.presentation.profile.Profile
import com.example.daytask.presentation.project_details.ProjectDetails
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.typeOf

@RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.appGraph(
    navController: NavController,
    taskValidationEvent: Flow<ProjectViewModel.ValidationEvent>,
    projectValidationEvent: Flow<ProjectViewModel.ValidationEvent>,
    projectViewModel: ProjectViewModel,
    projectState: ProjectState,
    onUserEvent: (UserEvent) -> Unit,
    userState: UserState,
    onProjectEvent: (ProjectEvent) -> Unit,
    taskState: TaskState,
    onTaskEvent: (TaskEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {

    navigation<Destinations.App>(
        startDestination = Destinations.App.HomeScaffold
    ) {

        composable<Destinations.App.HomeScaffold> {
            HomeScaffold(
                userState = userState,
                projectState = projectState,
                onProjectEvent = onProjectEvent,
                parentNavController = navController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this
            )
        }

        composable<Destinations.App.PersonalChat> {
            PersonalChat(
                modifier = Modifier,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Destinations.App.CompletedProjects> {
            CompletedProjects(
                modifier = Modifier,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                projectState = projectState,
                projectEvent = onProjectEvent,
                navigateToProjectDetail = { projectWithTasks ->
                    navController.navigate(Destinations.App.ProjectDetails(projectWithTasks))
                },
                navigateToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable<Destinations.App.InCompleteProjects> {
            IncompleteProjects(
                modifier = Modifier,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                projectState = projectState,
                projectEvent = onProjectEvent,
                navigateToProjectDetail = { projectWithTasks ->
                    navController.navigate(Destinations.App.ProjectDetails(projectWithTasks))
                },
                navigateToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable<Destinations.App.ProjectDetails>(
            typeMap = mapOf(
                typeOf<ProjectWithTasks>() to CustomNavType.projectType,
                typeOf<Task>() to NavType.StringListType
            ),
        ) {
            Log.d("Destination", "appGraph: ${navController.currentDestination}")

            val arguments = it.toRoute<Destinations.App.ProjectDetails>()

            ProjectDetails(
                modifier = Modifier,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                projectViewModel = projectViewModel,
                onProjectEvent = onProjectEvent,
                editProject = { projectWithTask ->
                    navController.navigate(Destinations.App.AddProject(projectWithTask))
                },
                navigateUp = {
                    navController.popBackStack()
                },
                navigateToAddTask = { task ->
                    navController.navigate(Destinations.App.AddTask(task))
                },
                navigateToEditTask = { task ->
                    navController.navigate(Destinations.App.AddTask(task))
                },
                onTaskEvent = onTaskEvent
            )
        }

        composable<Destinations.App.AddProject>(
            typeMap = mapOf(
                typeOf<ProjectWithTasks>() to CustomNavType.projectType,
                typeOf<Task>() to NavType.StringListType
            )
        ) {

            val arguments = it.toRoute<Destinations.App.AddProject>()

            AddProject(
                modifier = Modifier,
                projectValidationEvent = projectValidationEvent,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                projectState = projectState,
                projectWithTasks = arguments.projectWithTasks,
                onProjectEvent = onProjectEvent,
                navigateBackToHome = {
                    navController.popBackStack()
                },
                projectCreationSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable<Destinations.App.Profile> {
            Profile(
                userState = userState,
                onUserEvent = onUserEvent,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                navigateUp = {
                    navController.popBackStack()
                },
                navigateToLogin = {
                    navController.navigate(Destinations.Auth) {
                        popUpTo<Destinations.App> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Destinations.App.AddTask>(
            typeMap = mapOf(
                typeOf<Task>() to CustomNavType.taskType
            ),
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = "day-task://notification/{task}"
                }
            )
        ) {
            val arguments = it.toRoute<Destinations.App.AddTask>()
            AddTask(
                modifier = Modifier,
                taskValidationEvent = taskValidationEvent,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                onProjectEvent = onProjectEvent,
                task = arguments.task,
                state = taskState,
                onTaskEvent = onTaskEvent,
                navigateUp = {
                    navController.popBackStack()
                },
                popBackToHomeAfterSavingTask = {
                    navController.popBackStack()
                }
            )
        }
    }
}