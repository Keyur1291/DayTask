package com.example.daytask.presentation.project_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.RemeasureToBounds
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytask.R
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.data.local.model.Task
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.TaskEvent
import com.example.daytask.presentation.ProjectViewModel
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.home.components.CardImageRowItem
import com.example.daytask.presentation.home.components.IncompleteTaskCardProgressBox
import com.example.daytask.presentation.home.components.IncompleteTaskCardTitle
import com.example.daytask.presentation.home.components.OverlappingImagesRow
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.contentDarkGrayBg
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProjectDetails(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    projectViewModel: ProjectViewModel,
    onProjectEvent: (ProjectEvent) -> Unit,
    onTaskEvent: (TaskEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateToAddTask: (task: Task) -> Unit,
    navigateToEditTask: (task: Task) -> Unit,
    editProject: (projectWithTasks: ProjectWithTasks) -> Unit
) {

    val projectWithTasks by projectViewModel.projectWithTasksState.collectAsState()

    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        animationPlayed = true
    }

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Scaffold(
                modifier = modifier,
                topBar = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(splashBgColor)
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp)
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                            )
                            .renderInSharedTransitionScopeOverlay(
                                zIndexInOverlay = 1f
                            )
                    ) {
                        IconButton(
                            onClick = navigateUp
                        ) {
                            Icon(
                                tint = Color.White,
                                contentDescription = null,
                                painter = painterResource(R.drawable.arrowleft)
                            )
                        }
                        Text(
                            color = Color.White,
                            text = "Project Details"
                        )
                        IconButton(
                            onClick = {
                                editProject(projectWithTasks)
                                onProjectEvent(
                                    ProjectEvent.SetProjectTitle(
                                        projectWithTasks.project.title
                                    )
                                )
                                onProjectEvent(
                                    ProjectEvent.SetProjectDescription(
                                        projectWithTasks.project.description
                                    )
                                )
                                onProjectEvent(
                                    ProjectEvent.SetProjectDueDate(
                                        projectWithTasks.project.dueDate
                                    )
                                )
                                onProjectEvent(
                                    ProjectEvent.SetProjectProgress(
                                        projectWithTasks.project.progress
                                    )
                                )
                            }
                        ) {
                            Icon(
                                tint = Color.White,
                                contentDescription = null,
                                painter = painterResource(R.drawable.edit)
                            )
                        }
                    }
                },
                bottomBar = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay()
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it })
                            )
                            .fillMaxWidth()
                            .background(bottomNavBarContainerColor)
                    ) {
                        ReusableLargeButton(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
                                .fillMaxWidth(0.8f),
                            onClick = {
                                navigateToAddTask(
                                    Task(
                                        id = 0,
                                        projectId = projectWithTasks.project.id,
                                        title = "",
                                        description = "",
                                        time = "",
                                        dueDate = "",
                                        isComplete = false
                                    )
                                )
                                onTaskEvent(TaskEvent.OnTitleChange(""))
                                onTaskEvent(TaskEvent.OnDescriptionChange(""))
                                onTaskEvent(TaskEvent.OnTaskTimeChange(""))
                                onTaskEvent(TaskEvent.OnTaskDueDateChange(""))
                                onTaskEvent(TaskEvent.OnTaskStatusChange(false))
                            },
                            buttonText = "Add Task"
                        )
                    }
                }
            ) { paddings ->

                Surface(
                    color = splashBgColor
                ) {

                    LazyColumn(
                        contentPadding = paddings,
                        modifier = Modifier
                            .sharedBounds(
                                resizeMode = RemeasureToBounds,
                                sharedContentState = rememberSharedContentState("${projectWithTasks.project.id}DetailBounds"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .skipToLookaheadSize()
                            .fillMaxSize()
                            .padding(bottom = 16.dp),
                    ) {

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Spacer(Modifier.height(32.dp))
                                IncompleteTaskCardTitle(
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("Shared${projectWithTasks.project.title}"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        ),
                                    text = projectWithTasks.project.title
                                )
                                Spacer(Modifier.height(24.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        )
                                        .fillMaxWidth()
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .animateEnterExit(
                                                enter = slideInHorizontally { it },
                                                exit = slideOutHorizontally { -it }
                                            )
                                    ) {
                                        Icon(
                                            tint = splashBgColor,
                                            contentDescription = null,
                                            painter = painterResource(R.drawable.calendarremove),
                                            modifier = Modifier
                                                .background(yellowColor)
                                                .padding(16.dp)
                                                .size(24.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                fontSize = 12.sp,
                                                color = Color.White.copy(0.5f),
                                                text = "Due Date"
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .sharedElement(
                                                        sharedContentState = rememberSharedContentState("shared${projectWithTasks.project.title}DueDate"),
                                                        animatedVisibilityScope = animatedVisibilityScope
                                                    ),
                                                fontSize = 18.sp,
                                                color = Color.White,
                                                text = projectWithTasks.project.dueDate
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .animateEnterExit(
                                                enter = slideInHorizontally { it },
                                                exit = slideOutHorizontally { -it }
                                            )
                                    ) {
                                        Icon(
                                            tint = splashBgColor,
                                            contentDescription = null,
                                            painter = painterResource(R.drawable.profile2user),
                                            modifier = Modifier
                                                .background(yellowColor)
                                                .padding(16.dp)
                                                .size(24.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                fontSize = 12.sp,
                                                color = Color.White.copy(0.5f),
                                                text = "Project Team"
                                            )
                                            OverlappingImagesRow(
                                                modifier = Modifier,
                                                overlappingPercentage = 0.3f,
                                                content = {
                                                    repeat(3) {
                                                        CardImageRowItem()
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                                Spacer(Modifier.height(24.dp))
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        ),
                                    color = Color.White,
                                    text = "Project Details"
                                )
                                Text(
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        ),
                                    fontSize = 12.sp,
                                    color = Color.White.copy(0.5f),
                                    text = projectWithTasks.project.description
                                )
                                Spacer(Modifier.height(24.dp))
                            }
                        }

                        item {

                            val currentPercentage by animateFloatAsState(
                                targetValue = if (animationPlayed) projectWithTasks.project.progress else 0f,
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    )
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        ),
                                    color = Color.White,
                                    text = "Project Progress"
                                )
                                IncompleteTaskCardProgressBox(
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("shared${projectWithTasks.project.title}Progress"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        ),
                                    progress = currentPercentage,
                                    unit = "%"
                                )
                            }
                        }

                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    )
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(vertical = 16.dp, horizontal = 20.dp),
                                    text = "All Tasks"
                                )
                            }
                        }

                        if (projectWithTasks.tasks.isEmpty()) {
                            item {
                                Text(
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        )
                                        .fillMaxWidth(),
                                    text = "No task found "
                                )
                            }
                        } else {
                            itemsIndexed(
                                key = { index, item -> "${index}${item.title}${item.id}" },
                                items = projectWithTasks.tasks
                            ) { index, task ->
                                Card(
                                    shape = RectangleShape,
                                    colors = CardDefaults.cardColors(
                                        containerColor = contentDarkGrayBg,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { (index + 1) * it },
                                            exit = slideOutHorizontally { -((index + 1) * it) }
                                        )
                                        .padding(vertical = 4.dp, horizontal = 20.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            navigateToEditTask(task)
                                            onTaskEvent(TaskEvent.OnTitleChange(task.title))
                                            onTaskEvent(TaskEvent.OnDescriptionChange(task.description))
                                            onTaskEvent(TaskEvent.OnTaskTimeChange(task.time))
                                            onTaskEvent(TaskEvent.OnTaskDueDateChange(task.dueDate))
                                            onTaskEvent(TaskEvent.OnTaskStatusChange(task.isComplete))
                                        }
                                        .sharedBounds(
                                            resizeMode = RemeasureToBounds,
                                            sharedContentState = rememberSharedContentState("${task.title}SharedBounds"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            color = Color.White,
                                            modifier = Modifier.padding(start = 8.dp),
                                            fontSize = 18.sp,
                                            text = task.title
                                        )
                                        Icon(
                                            tint = splashBgColor,
                                            contentDescription = null,
                                            painter = painterResource(
                                                if (task.isComplete) R.drawable.tickcircle
                                                else R.drawable.untickcircle
                                            ),
                                            modifier = Modifier
                                                .background(yellowColor)
                                                .clickable { }
                                                .padding(6.dp)
                                                .size(28.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}