package com.example.daytask.presentation.home_scaffold.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.navigation.destinations.BottomBarItem
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor

val bottomBarItems = listOf(
    BottomBarItem.Home,
    BottomBarItem.Chat,
    BottomBarItem.Add,
    BottomBarItem.Calender,
    BottomBarItem.Notification,
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BottomBarView(
    modifier: Modifier = Modifier,
    onProjectEvent: (ProjectEvent) -> Unit,
    parentNavController: NavController,
    navController: NavController,
    currentRoute: NavDestination?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        bottomBarItems.forEachIndexed { index, item ->

            val isSelected = currentRoute?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true

            BottomBarItemView(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier
                    .weight(1f),
                isSelected = isSelected,
                bottomBarItem = item,
                onClick = {
                    if (!isSelected) {
                        if (item.title == "Add") {
                            parentNavController.navigate(item.route) {
                                restoreState = false
                                launchSingleTop = true

                                popUpTo(navController.graph.findStartDestination().route!!) {
                                    saveState = false
                                }

                                onProjectEvent(ProjectEvent.SetProjectTitle(""))
                                onProjectEvent(ProjectEvent.SetProjectDescription(""))
                                onProjectEvent(ProjectEvent.SetProjectDueDate(""))
                            }
                        } else {
                            navController.navigate(item.route) {
                                restoreState = false
                                popUpTo(navController.graph.findStartDestination().route!!) {
                                    saveState = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BottomBarItemView(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    bottomBarItem: BottomBarItem<out Any>,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            if (bottomBarItem.title == "Add") {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1.5f)
                        .padding(horizontal = 8.dp)
                        .clickable { onClick() }
                        .background(yellowColor)
                ) {
                    Icon(
                        tint = splashBgColor,
                        contentDescription = null,
                        painter = painterResource(bottomBarItem.icon),
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .clickable { onClick() }
                ) {
                    Icon(
                        tint = if (isSelected) yellowColor
                               else Color.White.copy(0.5f),
                        contentDescription = null,
                        painter = painterResource(bottomBarItem.icon),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        color = if (isSelected) yellowColor else Color.White.copy(
                            0.5f
                        ),
                        fontSize = 10.sp,
                        text = bottomBarItem.title
                    )
                }
            }
        }
    }
}