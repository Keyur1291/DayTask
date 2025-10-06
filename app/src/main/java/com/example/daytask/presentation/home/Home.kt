package com.example.daytask.presentation.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.RemeasureToBounds
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.data.local.model.UserState
import com.example.daytask.domain.ProjectState
import com.example.daytask.presentation.home.components.CompletedTaskCard
import com.example.daytask.presentation.home.components.HomeScreenTopBar
import com.example.daytask.presentation.home.components.IncompleteTaskCard
import com.example.daytask.presentation.home.components.TopBarSearchRow
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.animatedSize
import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.TransformOrigin
import com.example.daytask.domain.ProjectEvent

@OptIn(
    ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    paddings: PaddingValues,
    userState: UserState,
    projectState: ProjectState,
    projectEvent: (ProjectEvent) -> Unit,
    navigateToProjectDetail: (projectWithTasks: ProjectWithTasks) -> Unit,
    navigateToIncompleteProjects: () -> Unit,
    navigateToCompletedProjects: () -> Unit,
    navigateToProfile: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    var searchBarQuery by remember { mutableStateOf("") }
    val completedProjectsList = projectState.projects.filter { it.project.progress == 1f }
    val incompleteProjectsList = projectState.projects.filter { it.project.progress != 1f }

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 12.dp)
                    .imePadding(),
                contentPadding = paddings
            ) {

                item {
                    HomeScreenTopBar(
                        modifier = Modifier
                            .background(splashBgColor)
                            .padding(horizontal = 20.dp)
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                            )
                            .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f),
                        user = userState.firebaseUser,
                        navigateToProfile = navigateToProfile,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                }

                stickyHeader {
                    TopBarSearchRow(
                        value = searchBarQuery,
                        onValueChange = { searchBarQuery = it },
                        onBackspaceClick = { searchBarQuery = "" },
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { -it }
                            )
                            .background(splashBgColor)
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp)
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("SharedTopBar"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                ),
                            color = Color.White,
                            text = "Completed Projects"
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { it }
                                )
                                .clickable { navigateToCompletedProjects() }
                                .padding(2.dp),
                            color = yellowColor,
                            text = "See all"
                        )
                    }
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        itemsIndexed(
                            key = { index, item -> item.project.id },
                            items = completedProjectsList.filter {
                                it.project.title.contains(
                                    searchBarQuery,
                                    ignoreCase = true
                                )
                            }
                        ) { index, projectWithTasks ->
                            CompletedTaskCard(
                                projectWithTasks = projectWithTasks,
                                index = index,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { (index + 1) * (2*it) },
                                        exit = slideOutHorizontally { (index + 1) * (2*it) }
                                    )
                                    .animateItem()
                                    .padding(horizontal = 4.dp)
                                    .clickable {
                                        projectEvent(
                                            ProjectEvent.GetProjectById(
                                                projectWithTasks.project.id
                                            )
                                        )
                                        navigateToProjectDetail(projectWithTasks)
                                    }
                                    .height(180.dp)
                                    .width(190.dp)
                                    .sharedBounds(
                                        resizeMode = RemeasureToBounds,
                                        sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}DetailBounds"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                                    .sharedBounds(
                                        resizeMode = RemeasureToBounds,
                                        sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}Bounds"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}SharedElement"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier

                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            modifier = Modifier.animateEnterExit(
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { -it }
                            ),
                            color = Color.White,
                            text = "Ongoing Tasks"
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { it }
                                )
                                .clickable { navigateToIncompleteProjects() }
                                .padding(2.dp),
                            color = yellowColor,
                            text = "See all"
                        )
                    }
                }

                itemsIndexed(
                    key = { index, item -> "${index}${item.project.title}${item.project.id}" },
                    items = incompleteProjectsList
                        .filter { it.project.title.contains(searchBarQuery, ignoreCase = true) },
                ) { index, projectWithTasks ->

                    IncompleteTaskCard(
                        projectWithTasks = projectWithTasks,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInHorizontally { (index + 1) * it },
                                exit = slideOutHorizontally { -((index + 1) * it) }
                            )
                            .animateItem()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .clickable {
                                projectEvent(
                                    ProjectEvent.GetProjectById(
                                        projectWithTasks.project.id
                                    )
                                )
                                navigateToProjectDetail(projectWithTasks)
                            }
                            .fillMaxWidth()
                            .sharedBounds(
                                resizeMode = RemeasureToBounds,
                                sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}TaskToHomeSharedBounds"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .sharedBounds(
                                resizeMode = RemeasureToBounds,
                                sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}DetailBounds"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .sharedBounds(
                                resizeMode = RemeasureToBounds,
                                sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}Bounds"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}SharedElement"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }
        }
    }
}