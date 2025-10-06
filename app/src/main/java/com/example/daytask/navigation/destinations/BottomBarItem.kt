package com.example.daytask.navigation.destinations

import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomBarItem<T>(
    val title: String,
    val icon: Int,
    val route: T
) {
    @Serializable
    data object Home: BottomBarItem<Destinations.App.HomeScaffold.Home>(
        "Home",
        R.drawable.home2,
        Destinations.App.HomeScaffold.Home
    )

    @Serializable
    data object Chat: BottomBarItem<Destinations.App.HomeScaffold.Chat>(
        "Chat",
        R.drawable.messages1,
        Destinations.App.HomeScaffold.Chat
    )

    @Serializable
    data object Add: BottomBarItem<Destinations.App.AddProject>(
        "Add",
        R.drawable.addsquare,
        Destinations.App.AddProject(ProjectWithTasks())
    )

    @Serializable
    data object Calender: BottomBarItem<Destinations.App.HomeScaffold.Calendar>(
        "Calender",
        R.drawable.calendar,
        Destinations.App.HomeScaffold.Calendar
    )

    @Serializable
    data object Notification: BottomBarItem<Destinations.App.HomeScaffold.Notification>(
        "Notification",
        R.drawable.notification1,
        Destinations.App.HomeScaffold.Notification
    )
}