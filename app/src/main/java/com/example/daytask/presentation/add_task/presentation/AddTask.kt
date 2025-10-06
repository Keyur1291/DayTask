package com.example.daytask.presentation.add_task.presentation

import android.widget.Toast
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.daytask.R
import com.example.daytask.data.local.model.Task
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.TaskEvent
import com.example.daytask.domain.TaskState
import com.example.daytask.presentation.ProjectViewModel
import com.example.daytask.presentation.add_task.domain.TaskNotificationSchedulerImpl
import com.example.daytask.presentation.authentication.presentation.events.RegisterEvent
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.common.components.ReusableTextField
import com.example.daytask.presentation.common.components.SquareCheckBox
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.contentDarkGrayBg
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(value = 31)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AddTask(
    modifier: Modifier = Modifier,
    taskValidationEvent: Flow<ProjectViewModel.ValidationEvent>,
    onProjectEvent: (ProjectEvent) -> Unit,
    task: Task,
    state: TaskState,
    onTaskEvent: (TaskEvent) -> Unit,
    navigateUp: () -> Unit,
    popBackToHomeAfterSavingTask: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var switchToTimeInput by remember { mutableStateOf(false) }
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    val timePickerState = rememberTimePickerState(
        initialHour = LocalDateTime.now().hour,
        initialMinute = LocalDateTime.now().minute,
        is24Hour = false
    )
    val selectedTime =
        LocalTime.of(timePickerState.hour, timePickerState.minute)
    val currentTime = timeFormatter.format(selectedTime)
    LaunchedEffect(timePickerState) {

        launch {
            snapshotFlow {
                Pair(timePickerState.hour, timePickerState.minute)
            }
                .distinctUntilChanged()
                .collect {
                    haptics.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                }
        }
    }

    val dateFormatter = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = java.util.Calendar.getInstance().timeInMillis
    )
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val currentDate =
        if (datePickerState.selectedDateMillis == null) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))
        } else {
            dateFormatter.format(datePickerState.selectedDateMillis)
        }

    val notificationScheduler =
        TaskNotificationSchedulerImpl(LocalContext.current, datePickerState, timePickerState)

    LaunchedEffect(context) {
        taskValidationEvent.collect { event ->
            when (event) {
                is ProjectViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        if (task.id == 0) "Task added" else "Task updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (!state.isCompleted) {
                        notificationScheduler.schedule(
                            task = Task(
                                title = state.title,
                                description = state.description,
                                isComplete = false,
                                id = task.id,
                                projectId = task.projectId,
                                time = if (state.time != "") state.time else currentTime,
                                dueDate = if (state.dueDate != "") state.dueDate else currentDate
                            )
                        )
                    }
                    popBackToHomeAfterSavingTask()
                }
            }
        }
    }

    var backProgress: Float? by remember { mutableStateOf(null) }

    val onBackPressed = remember {
        object : OnBackPressedCallback(true) {

            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
                backProgress = backEvent.progress
            }

            override fun handleOnBackPressed() {
                if (showTimePickerDialog) showTimePickerDialog = false
                if (showDatePickerDialog) showDatePickerDialog = false
                backProgress = null
            }

            override fun handleOnBackCancelled() {
                backProgress = null
            }
        }
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    DisposableEffect(backPressedDispatcher, showTimePickerDialog, showDatePickerDialog) {
        if (showDatePickerDialog || showTimePickerDialog) {
            backPressedDispatcher.addCallback(onBackPressed)
        }

        onDispose {
            onBackPressed.remove()
        }
    }

    val backdropShadow by animateColorAsState(
        targetValue =
            if (showDatePickerDialog || showTimePickerDialog) Color.Black.copy(0.5f)
            else Color.Transparent
    )
    val interactionSource = remember { MutableInteractionSource() }

    var enableOtherElements by rememberSaveable { mutableStateOf(false) }
    enableOtherElements = !(showDatePickerDialog || showTimePickerDialog)

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(backdropShadow)
                    .clickable(
                        enabled = !enableOtherElements,
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (showTimePickerDialog) showTimePickerDialog = false
                        if (showDatePickerDialog) showDatePickerDialog = false
                    }
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(bottom = 12.dp)
                        .imePadding()
                        .sharedBounds(
                            resizeMode = RemeasureToBounds,
                            sharedContentState = rememberSharedContentState("${task.projectId}TaskToHomeSharedBounds"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .sharedBounds(
                            resizeMode = RemeasureToBounds,
                            sharedContentState = rememberSharedContentState("${state.title}SharedBounds"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
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
                                enabled = enableOtherElements,
                                onClick = navigateUp
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
                                text = if (task.projectId == 0) "Create New Task" else "Edit task"
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
                                        enter = slideInHorizontally { it * 2 },
                                        exit = slideOutHorizontally { -(it * 2) }
                                    ),
                                color = Color.White,
                                fontSize = 20.sp,
                                text = "Task Title"
                            )
                            Spacer(Modifier.height(16.dp))
                            ReusableTextField(
                                enabled = enableOtherElements,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                value = state.title,
                                onValueChange = { onTaskEvent(TaskEvent.OnTitleChange(it)) },
                                placeHolder = "Title",
                                leadingIcon = 0,
                                isError = state.titleError != null,
                                supportingText = state.titleError,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it * 2 },
                                        exit = slideOutHorizontally { -(it * 2) }
                                    ),
                                color = Color.White,
                                fontSize = 20.sp,
                                text = "Task Detail"
                            )
                            Spacer(Modifier.height(16.dp))
                            ReusableTextField(
                                enabled = enableOtherElements,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                value = state.description,
                                onValueChange = { onTaskEvent(TaskEvent.OnDescriptionChange(it)) },
                                placeHolder = "Details",
                                minLines = 3,
                                singleLine = false,
                                leadingIcon = 0,
                                isError = state.descriptionError != null,
                                supportingText = state.descriptionError,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Done
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it * 2 },
                                        exit = slideOutHorizontally { -(it * 2) }
                                    ),
                                color = Color.White,
                                fontSize = 20.sp,
                                text = "Time & Date"
                            )
                            Spacer(Modifier.height(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    )
                                    .fillMaxWidth()
                            ) {
                                AnimatedContent(
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        ),
                                    targetState = showTimePickerDialog
                                ) {
                                    when (it) {
                                        true -> {}

                                        else -> {
                                            Card(
                                                modifier = Modifier
                                                    .sharedBounds(
                                                        resizeMode = RemeasureToBounds,
                                                        sharedContentState = rememberSharedContentState(
                                                            "TimeDialog"
                                                        ),
                                                        animatedVisibilityScope = this
                                                    )
                                                    .weight(1f)
                                                    .clickable { showTimePickerDialog = true },
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
                                                        painter = painterResource(R.drawable.clock),
                                                        modifier = Modifier
                                                            .background(yellowColor)
                                                            .padding(8.dp)
                                                    )
                                                    Text(
                                                        color = Color.White,
                                                        modifier = Modifier
                                                            .padding(horizontal = 12.dp),
                                                        fontSize = 18.sp,
                                                        textAlign = TextAlign.Center,
                                                        text = if (state.time != "") state.time
                                                        else "$currentTime"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(Modifier.width(4.dp))
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
                                                    .weight(1f)
                                                    .clickable { showDatePickerDialog = true },
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
                                                            .padding(8.dp)
                                                    )
                                                    Text(
                                                        color = Color.White,
                                                        modifier = Modifier.padding(horizontal = 12.dp),
                                                        fontSize = 18.sp,
                                                        textAlign = TextAlign.Center,
                                                        text = if (state.dueDate != "") state.dueDate
                                                        else "$currentDate"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(32.dp))
                            Row(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it * 2 },
                                        exit = slideOutHorizontally { -(it * 2) }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SquareCheckBox(
                                    enabled = enableOtherElements,
                                    value = state.isCompleted,
                                    onTickMarkChange = {
                                        if (state.isCompleted) {
                                            onTaskEvent(TaskEvent.OnTaskStatusChange(false))
                                        } else {
                                            onTaskEvent(TaskEvent.OnTaskStatusChange(true))
                                        }
                                    }
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    color = Color.White,
                                    text = "Mark as finished"
                                )
                            }
                            Spacer(Modifier.height(32.dp))
                            ReusableLargeButton(
                                enabled = enableOtherElements,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                buttonText = if (task.id == 0) "Create" else "Update",
                                onClick = {
                                    onProjectEvent(
                                        ProjectEvent.UpsertTask(
                                            projectId = task.projectId,
                                            task = Task(
                                                title = state.title,
                                                description = state.description,
                                                isComplete = state.isCompleted,
                                                id = task.id,
                                                projectId = task.projectId,
                                                time = if (state.time != "") state.time else currentTime,
                                                dueDate = if (state.dueDate.isNotEmpty()) state.dueDate else currentDate
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
                AnimatedContent(
                    targetState = showTimePickerDialog
                ) { state ->
                    when (state) {
                        true -> {
                            Card(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = 1f - ((backProgress ?: 0f) * 0.2f)
                                        scaleY = 1f - ((backProgress ?: 0f) * 0.2f)
                                    }
                                    .padding(horizontal = 16.dp)
                                    .sharedBounds(
                                        resizeMode = RemeasureToBounds,
                                        sharedContentState = rememberSharedContentState(
                                            "TimeDialog"
                                        ),
                                        animatedVisibilityScope = this
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = bottomNavBarContainerColor
                                ),
                                shape = RectangleShape
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    AnimatedContent(
                                        targetState = switchToTimeInput
                                    ) {
                                        when (it) {
                                            true -> {
                                                TimeInput(
                                                    modifier = Modifier
                                                        .skipToLookaheadSize(),
                                                    state = timePickerState,
                                                    colors = timePickerColors()
                                                )
                                            }

                                            else -> {
                                                TimePicker(
                                                    modifier = Modifier
                                                        .skipToLookaheadSize(),
                                                    state = timePickerState,
                                                    colors = timePickerColors(),
                                                    layoutType = TimePickerLayoutType.Vertical
                                                )
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .skipToLookaheadSize(),
                                    ) {
                                        IconButton(
                                            modifier = Modifier
                                                .skipToLookaheadSize(),
                                            onClick = { switchToTimeInput = !switchToTimeInput }
                                        ) {
                                            Icon(
                                                tint = Color.White,
                                                contentDescription = null,
                                                painter = painterResource(R.drawable.clock)
                                            )
                                        }
                                        OutlinedButton(
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.White
                                            ),
                                            shape = RectangleShape,
                                            modifier = Modifier
                                                .weight(1f)
                                                .skipToLookaheadSize(),
                                            onClick = { showTimePickerDialog = false }
                                        ) {
                                            Text(text = "Cancel")
                                        }
                                        Spacer(Modifier.width(16.dp))
                                        Button(
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = yellowColor,
                                                contentColor = splashBgColor
                                            ),
                                            shape = RectangleShape,
                                            modifier = Modifier
                                                .weight(1f)
                                                .skipToLookaheadSize(),
                                            onClick = {
                                                onTaskEvent(TaskEvent.OnTaskTimeChange(currentTime))
                                                showTimePickerDialog = false
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

                AnimatedContent(
                    targetState = showDatePickerDialog
                ) {
                    when (it) {

                        true -> {
                            Card(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = 1f - ((backProgress ?: 0f) * 0.2f)
                                        scaleY = 1f - ((backProgress ?: 0f) * 0.2f)
                                    }
                                    .padding(horizontal = 16.dp)
                                    .sharedBounds(
                                        resizeMode = RemeasureToBounds,
                                        sharedContentState = rememberSharedContentState(
                                            "DateDialog"
                                        ),
                                        animatedVisibilityScope = this
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = bottomNavBarContainerColor
                                ),
                                shape = RectangleShape
                            ) {
                                Column(
                                    modifier = Modifier
                                        .skipToLookaheadSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    DatePicker(
                                        modifier = Modifier
                                            .skipToLookaheadSize(),
                                        colors = datePickerColors(),
                                        state = datePickerState,
                                        headline = {
                                            Text(
                                                color = Color.White,
                                                modifier = Modifier.padding(16.dp),
                                                text = currentDate
                                            )
                                        }
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp, end = 12.dp)
                                            .skipToLookaheadSize()
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
                                                onTaskEvent(
                                                    TaskEvent.OnTaskDueDateChange(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerColors(): DatePickerColors {
    return DatePickerDefaults.colors(
        containerColor = bottomNavBarContainerColor,
        titleContentColor = Color.White,
        headlineContentColor = Color.White,
        weekdayContentColor = Color.White,
        subheadContentColor = Color.White,
        navigationContentColor = yellowColor,
        yearContentColor = Color.White,
        disabledYearContentColor = Color.Unspecified,
        currentYearContentColor = yellowColor,
        selectedYearContentColor = splashBgColor,
        disabledSelectedYearContentColor = Color.Unspecified,
        selectedYearContainerColor = yellowColor,
        disabledSelectedYearContainerColor = Color.Unspecified,
        dayContentColor = Color.White,
        disabledDayContentColor = Color.Unspecified,
        selectedDayContentColor = splashBgColor,
        disabledSelectedDayContentColor = Color.Unspecified,
        selectedDayContainerColor = yellowColor,
        disabledSelectedDayContainerColor = Color.Unspecified,
        todayContentColor = yellowColor,
        todayDateBorderColor = yellowColor,
        dayInSelectionRangeContentColor = splashBgColor,
        dayInSelectionRangeContainerColor = yellowColor,
        dividerColor = yellowColor,
        dateTextFieldColors = TextFieldDefaults.colors(
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White,
            focusedContainerColor = contentDarkGrayBg,
            unfocusedContainerColor = contentDarkGrayBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedPlaceholderColor = Color.White.copy(0.5f),
            unfocusedPlaceholderColor = Color.White.copy(0.5f),
            errorContainerColor = Color.Red.copy(0.2f),
            errorTextColor = Color.White,
            errorCursorColor = Color.Red,
            errorLeadingIconColor = Color.White,
            errorIndicatorColor = Color.Transparent,
            errorPlaceholderColor = Color.White.copy(0.5f),
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun timePickerColors(): TimePickerColors {
    return TimePickerDefaults.colors(

        timeSelectorUnselectedContentColor = Color.White,
        timeSelectorUnselectedContainerColor = contentDarkGrayBg,
        timeSelectorSelectedContentColor = splashBgColor,
        timeSelectorSelectedContainerColor = yellowColor,
        periodSelectorBorderColor = yellowColor,
        periodSelectorSelectedContentColor = splashBgColor,
        periodSelectorUnselectedContainerColor = Color.Unspecified,
        periodSelectorSelectedContainerColor = yellowColor,
        periodSelectorUnselectedContentColor = Color.White,
        selectorColor = yellowColor,
        clockDialColor = contentDarkGrayBg,
        clockDialSelectedContentColor = splashBgColor,
        clockDialUnselectedContentColor = Color.White,
        containerColor = bottomNavBarContainerColor
    )
}