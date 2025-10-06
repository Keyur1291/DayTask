package com.example.daytask.navigation.destinations

import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.data.local.model.Task
import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations {

    @Serializable
    data object Splash: Destinations()

    @Serializable
    data object Boarding: Destinations()

    @Serializable
    data object Auth {

        @Serializable
        data object Login: Destinations()

        @Serializable
        data object Register: Destinations()
    }

    @Serializable
    data object App {

        @Serializable
        data class ProjectDetails(val projectWithTasks: ProjectWithTasks): Destinations()

        @Serializable
        data object CompletedProjects : Destinations()

        @Serializable
        data object InCompleteProjects : Destinations()

        @Serializable
        data object Profile: Destinations()

        @Serializable
        data class AddTask(val task: Task): Destinations()

        @Serializable
        data class AddProject(val projectWithTasks: ProjectWithTasks): Destinations()

        @Serializable
        data object PersonalChat: Destinations()

        @Serializable
        data object HomeScaffold {

            @Serializable
            data object Home : Destinations()

            @Serializable
            data object Chat : Destinations()

            @Serializable
            data object Calendar : Destinations()

            @Serializable
            data object Notification : Destinations()
        }
    }
}