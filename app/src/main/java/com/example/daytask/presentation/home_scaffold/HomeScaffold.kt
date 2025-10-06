package com.example.daytask.presentation.home_scaffold

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.daytask.presentation.home_scaffold.components.BottomBarView
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.splashBgColor
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavController
import com.example.daytask.data.local.model.UserState
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.ProjectState
import com.example.daytask.navigation.nav_controller.AppNavController
import com.example.daytask.presentation.ui.theme.yellowColor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScaffold(
    modifier: Modifier = Modifier,
    parentNavController: NavController,
    userState: UserState,
    projectState: ProjectState,
    onProjectEvent: (ProjectEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Scaffold(
                modifier = modifier,
                floatingActionButton = {
                    AnimatedVisibility(
                        enter = slideInHorizontally(initialOffsetX = { (it  * 2) }),
                        exit = slideOutHorizontally(targetOffsetX = { (it * 2) }),
                        visible = currentRoute?.route?.endsWith("Chat") == true) {
                        FloatingActionButton(
                            modifier = Modifier
                                .fillMaxWidth(0.5f),
                            containerColor = yellowColor,
                            shape = RectangleShape,
                            onClick = {}
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Start Chat"
                            )
                        }
                    }
                },
                bottomBar = {
                    BottomBarView(
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onProjectEvent = onProjectEvent,
                        parentNavController = parentNavController,
                        navController = navController,
                        currentRoute = currentRoute,
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay()
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it })
                            )
                            .fillMaxWidth()
                            .background(bottomNavBarContainerColor)
                            .navigationBarsPadding()
                            .padding(top = 8.dp)

                    )
                }
            ) { paddings ->

                Surface(
                    color = splashBgColor
                ) {
                    AppNavController(
                        navController = navController,
                        parentNavController = parentNavController,
                        userState = userState,
                        projectState = projectState,
                        onProjectEvent = onProjectEvent,
                        paddings = paddings,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
        }
    }
}