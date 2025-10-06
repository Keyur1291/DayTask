package com.example.daytask.presentation.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.RemeasureToBounds
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.ProjectState
import com.example.daytask.presentation.home.components.IncompleteTaskCard
import com.example.daytask.presentation.home.components.TopBarSearchRow
import com.example.daytask.presentation.ui.theme.splashBgColor

@OptIn(
    ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun IncompleteProjects(
    modifier: Modifier = Modifier,
    projectState: ProjectState,
    projectEvent: (ProjectEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToProjectDetail: (projectWithTasks: ProjectWithTasks) -> Unit,
    navigateToHome: () -> Unit

) {

    var searchBarQuery by remember { mutableStateOf("") }
    val incompleteProjectsList = projectState.projects.filter { it.project.progress != 1f }

    Scaffold { paddings ->
        Surface(
            color = splashBgColor
        ) {
            with(sharedTransitionScope) {
                with(animatedVisibilityScope) {
                    LazyColumn(
                        contentPadding = paddings,
                        modifier = modifier
                            .fillMaxSize(),
                    ) {

                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .animateEnterExit(
                                        enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                        exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                                    )
                                    .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                            ) {
                                IconButton(
                                    onClick = navigateToHome
                                ) {
                                    Icon(
                                        tint = Color.White,
                                        contentDescription = null,
                                        painter = painterResource(R.drawable.arrowleft)
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                                Text(color = Color.White, text = "Incomplete Projects")
                                Spacer(Modifier.weight(1f))
                            }
                        }

                        stickyHeader {
                            TopBarSearchRow(
                                value = searchBarQuery,
                                onValueChange = { searchBarQuery = it },
                                onBackspaceClick = { searchBarQuery = "" },
                                modifier = Modifier
                                    .background(splashBgColor)
                                    .statusBarsPadding()
                                    .padding(horizontal = 20.dp)
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState("SharedTopBar"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                            )
                        }

                        itemsIndexed(
                            key = { index, item -> "${index}${item.project.title}${item.project.id}" },
                            items = incompleteProjectsList.filter {
                                it.project.title.contains(
                                    searchBarQuery,
                                    ignoreCase = true
                                )
                            },
                        ) { index, projectWithTasks ->

                            IncompleteTaskCard(
                                projectWithTasks = projectWithTasks,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        projectEvent(
                                            ProjectEvent.GetProjectById(
                                                projectWithTasks.project.id
                                            )
                                        )
                                        navigateToProjectDetail(projectWithTasks)
                                    }
                                    .animateItem()
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
    }
}