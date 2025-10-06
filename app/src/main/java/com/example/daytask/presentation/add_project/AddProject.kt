package com.example.daytask.presentation.add_project

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.RemeasureToBounds
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.request.Disposable
import com.example.daytask.R
import com.example.daytask.data.local.model.Project
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.ProjectState
import com.example.daytask.presentation.ProjectViewModel
import com.example.daytask.presentation.add_task.presentation.datePickerColors
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.common.components.ReusableTextField
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.contentDarkGrayBg
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AddProject(
    modifier: Modifier = Modifier,
    projectValidationEvent: Flow<ProjectViewModel.ValidationEvent>,
    projectState: ProjectState,
    projectWithTasks: ProjectWithTasks,
    onProjectEvent: (ProjectEvent) -> Unit,
    navigateBackToHome: () -> Unit,
    projectCreationSuccess: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val context = LocalContext.current
    LaunchedEffect(context) {
        projectValidationEvent.collect { event ->
            when (event) {
                is ProjectViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        if (projectWithTasks.project.id == 0) "Project created" else "Project updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    projectCreationSuccess()
                }
            }
        }
    }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val datePickerState = rememberDatePickerState()
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val currentDate =
        if (datePickerState.selectedDateMillis == null) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } else {
            dateFormatter.format(datePickerState.selectedDateMillis)
        }

    var backProgress: Float? by remember { mutableStateOf(null) }

    val onBackPressed = remember {
        object : OnBackPressedCallback(true) {

            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
                backProgress = backEvent.progress
            }

            override fun handleOnBackPressed() {
                showDatePickerDialog = false
                backProgress = null
            }

            override fun handleOnBackCancelled() {
                backProgress = null
            }
        }
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    DisposableEffect(backPressedDispatcher, showDatePickerDialog) {
        if (showDatePickerDialog) {
            backPressedDispatcher.addCallback(onBackPressed)
        }

        onDispose {
            onBackPressed.remove()
        }
    }

    val backdropShadow by animateColorAsState(
        targetValue = if (showDatePickerDialog) Color.Black.copy(0.5f) else Color.Transparent
    )
    val interactionSource = remember { MutableInteractionSource() }

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(backdropShadow)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = showDatePickerDialog
                    ) {
                        showDatePickerDialog = false
                    }
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(bottom = 12.dp)
                        .imePadding()
                ) {

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateEnterExit(
                                    enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                    exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                                )
                                .statusBarsPadding()
                                .padding(horizontal = 20.dp)
                        ) {
                            IconButton(
                                enabled = !showDatePickerDialog,
                                onClick = navigateBackToHome
                            ) {
                                Icon(
                                    tint = Color.White,
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.arrowleft)
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            Text(
                                color = Color.White,
                                text = if (projectWithTasks.project.id == 0) "Create New Project" else "Update Project"
                            )
                            Spacer(Modifier.weight(1f))
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                color = Color.White,
                                fontSize = 20.sp,
                                text = "Project Title"
                            )
                            Spacer(Modifier.height(16.dp))
                            ReusableTextField(
                                enabled = !showDatePickerDialog,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    )
                                    .fillMaxWidth(),
                                value = projectState.title,
                                onValueChange = { onProjectEvent(ProjectEvent.SetProjectTitle(it)) },
                                placeHolder = "Title",
                                leadingIcon = 0,
                                isError = projectState.titleError != null,
                                supportingText = projectState.titleError,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                color = Color.White,
                                fontSize = 20.sp,
                                text = "Project Detail"
                            )
                            Spacer(Modifier.height(16.dp))
                            ReusableTextField(
                                enabled = !showDatePickerDialog,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    )
                                    .fillMaxWidth(),
                                value = projectState.description,
                                onValueChange = {
                                    onProjectEvent(
                                        ProjectEvent.SetProjectDescription(
                                            it
                                        )
                                    )
                                },
                                placeHolder = "Details",
                                minLines = 3,
                                singleLine = false,
                                leadingIcon = 0,
                                isError = projectState.descriptionError != null,
                                supportingText = projectState.descriptionError,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                color = Color.White,
                                fontSize = 20.sp,
                                text = "Due Date"
                            )
                            Spacer(Modifier.height(16.dp))
                            AnimatedContent(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                targetState = showDatePickerDialog
                            ) {
                                when (it) {

                                    true -> {}

                                    else -> {
                                        Card(
                                            modifier = Modifier
                                                .sharedBounds(
                                                    resizeMode = RemeasureToBounds,
                                                    sharedContentState = rememberSharedContentState(
                                                        "DateDialog"
                                                    ),
                                                    animatedVisibilityScope = this
                                                )
                                                .clickable {
                                                    showDatePickerDialog = true
                                                },
                                            shape = RectangleShape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = contentDarkGrayBg
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                            ) {
                                                Icon(
                                                    tint = splashBgColor,
                                                    contentDescription = null,
                                                    painter = painterResource(R.drawable.calendar),
                                                    modifier = Modifier
                                                        .background(yellowColor)
                                                        .padding(12.dp)
                                                )
                                                Text(
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 8.dp),
                                                    text = if (projectState.dueDate != "") projectState.dueDate
                                                    else currentDate,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(32.dp))
                            ReusableLargeButton(
                                enabled = !showDatePickerDialog,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    )
                                    .fillMaxWidth(),
                                buttonText = if (projectWithTasks.project.id == 0) "Create" else "Update",
                                onClick = {
                                    if (projectWithTasks.project.id == 0) {
                                        onProjectEvent(
                                            ProjectEvent.UpsertProject(
                                                project = Project(
                                                    id = 0,
                                                    title = projectState.title,
                                                    description = projectState.description,
                                                    dueDate = projectState.dueDate.ifEmpty { currentDate },
                                                    progress = projectState.progress
                                                )
                                            )
                                        )
                                    } else {
                                        onProjectEvent(
                                            ProjectEvent.UpsertProject(
                                                project = Project(
                                                    id = projectWithTasks.project.id,
                                                    title = projectState.title,
                                                    description = projectState.description,
                                                    dueDate = projectState.dueDate.ifEmpty { currentDate },
                                                    progress = projectWithTasks.project.progress
                                                )
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
                AnimatedContent(
                    targetState = showDatePickerDialog
                ) {
                    when (it) {

                        true -> {
                            Card(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .graphicsLayer {
                                        scaleX = 1f - ((backProgress ?: 0f) * 0.2f)
                                        scaleY = 1f - ((backProgress ?: 0f) * 0.2f)
                                    }
                                    .padding(horizontal = 16.dp)
                                    .sharedBounds(
                                        resizeMode = RemeasureToBounds,
                                        sharedContentState = rememberSharedContentState("DateDialog"),
                                        animatedVisibilityScope = this
                                    ),
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = bottomNavBarContainerColor
                                ),
                            ) {
                                Column(
                                    modifier = Modifier
                                ) {

                                    DatePicker(
                                        colors = datePickerColors(),
                                        modifier = Modifier.skipToLookaheadSize(),
                                        state = datePickerState,
                                        headline = {
                                            Text(
                                                modifier = Modifier.padding(16.dp),
                                                text = currentDate
                                            )
                                        }
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .skipToLookaheadSize()
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp, end = 12.dp)
                                    ) {
                                        OutlinedButton(
                                            modifier = Modifier
                                                .skipToLookaheadSize(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.White
                                            ),
                                            shape = RectangleShape,
                                            onClick = { showDatePickerDialog = false }
                                        ) {
                                            Text(text = "Cancel")
                                        }
                                        Spacer(Modifier.width(12.dp))
                                        Button(
                                            modifier = Modifier
                                                .skipToLookaheadSize(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = yellowColor,
                                                contentColor = splashBgColor
                                            ),
                                            shape = RectangleShape,
                                            onClick = {
                                                onProjectEvent(
                                                    ProjectEvent.SetProjectDueDate(
                                                        currentDate
                                                    )
                                                )
                                                showDatePickerDialog = false
                                            }
                                        ) {
                                            Text(text = "Confirm")
                                        }
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}