package com.example.daytask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.local.database.ProjectDao
import com.example.daytask.data.local.model.Project
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.data.local.model.Task
import com.example.daytask.domain.ProjectEvent
import com.example.daytask.domain.ProjectState
import com.example.daytask.domain.use_cases.ProjectUseCases
import com.example.daytask.domain.use_cases.TaskUseCases
import com.example.daytask.domain.TaskEvent
import com.example.daytask.domain.TaskState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val dao: ProjectDao,
    private val projectUseCases: ProjectUseCases = ProjectUseCases(),
    private val taskUseCases: TaskUseCases = TaskUseCases()
) : ViewModel() {

    private val projectList = dao.getProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _projectState = MutableStateFlow<ProjectState>(ProjectState())

    val projectState = combine(_projectState, projectList) { state, list ->
        state.copy(
            projects = list
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectState())

    private val projectValidationEventChannel = Channel<ValidationEvent>()
    val projectValidationEvent = projectValidationEventChannel.receiveAsFlow()

    private val _projectWithTasksState = MutableStateFlow(ProjectWithTasks())
    val projectWithTasksState = _projectWithTasksState.asStateFlow()

    private val _taskState = MutableStateFlow(TaskState())
    val taskState = _taskState.asStateFlow()

    private val taskValidationEventChannel = Channel<ValidationEvent>()
    val taskValidationEvent = taskValidationEventChannel.receiveAsFlow()

    fun onProjectEvent(projectEvent: ProjectEvent) {

        when (projectEvent) {

            is ProjectEvent.UpsertTask -> {
                validateTaskData(projectEvent.task, projectEvent.projectId)
            }

            is ProjectEvent.DeleteTask -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.deleteTask(
                        task = projectEvent.task
                    )
                }
            }

            is ProjectEvent.UpsertProject -> {
                validateProjectData(projectEvent.project)
            }

            is ProjectEvent.DeleteProject -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.deleteProject(
                        project = projectEvent.project
                    )
                }
            }

            is ProjectEvent.SetProjectTitle -> {
                _projectState.update {
                    it.copy(
                        title = projectEvent.newTitle
                    )
                }
            }

            is ProjectEvent.SetProjectDescription -> {
                _projectState.update {
                    it.copy(
                        description = projectEvent.newDescription
                    )
                }
            }

            is ProjectEvent.SetProjectDueDate -> {
                _projectState.update {
                    it.copy(
                        dueDate = projectEvent.newDueDate
                    )
                }
            }

            is ProjectEvent.SetProjectProgress -> {
                _projectState.update {
                    val totalTask = if(it.tasks.isEmpty()) 0f else it.tasks.count().toFloat()
                    val finishedTasks = if(it.tasks.isEmpty()) 0f else  it.tasks.count { it.isComplete }.toFloat()
                    it.copy(
                        progress = if (it.tasks.isEmpty()) 0f else finishedTasks / totalTask
                    )
                }
            }

            is ProjectEvent.GetProjectById -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val projectWithTasks = dao.getProjectById(projectEvent.projectId)
                    _projectWithTasksState.update {
                        projectWithTasks
                    }
                }
            }
        }
    }

    fun validateProjectData(project: Project) {

        val projectTitleResult = projectUseCases.validateProjectTitle.execute(_projectState.value.title)
        val projectDetailResult = projectUseCases.validateProjectDetail.execute(_projectState.value.description)
        val hasError = listOf(
            projectTitleResult, projectDetailResult
        ).any { it.error != null }

        if (hasError) {
            _projectState.update {
                it.copy(
                    titleError = projectTitleResult.error,
                    descriptionError = projectDetailResult.error,
                )
            }
            return
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                dao.upsertProject(
                    project = project
                )
                projectValidationEventChannel.send(ValidationEvent.Success)
            }
            _projectWithTasksState.update {
                it.copy(
                    project = project
                )
            }
            _projectState.update {
                it.copy(
                    titleError = null,
                    descriptionError = null,
                )
            }
        }
    }

    fun onTaskEvent(taskEvent: TaskEvent) {

        when(taskEvent) {

            is TaskEvent.OnTitleChange -> {
                _taskState.update {
                    it.copy(
                        title = taskEvent.newTitle
                    )
                }
            }

            is TaskEvent.OnDescriptionChange -> {
                _taskState.update {
                    it.copy(
                        description = taskEvent.newDescription
                    )
                }
            }

            is TaskEvent.OnTaskStatusChange -> {
                _taskState.update {
                    it.copy (
                        isCompleted = taskEvent.newStatus
                    )
                }
            }

            is TaskEvent.OnTaskTimeChange -> {
                _taskState.update {
                    it.copy (
                        time = taskEvent.newTime
                    )
                }
            }

            is TaskEvent.OnTaskDueDateChange -> {
                _taskState.update {
                    it.copy (
                        dueDate = taskEvent.newDueDate
                    )
                }
            }
        }
    }

    fun validateTaskData(task: Task, projectId: Int) {

        val taskTitleResult = taskUseCases.validateTaskTitle.execute(_taskState.value.title)
        val taskDetailResult = taskUseCases.validateTaskDetail.execute(_taskState.value.description)

        val hasError = listOf(
            taskTitleResult, taskDetailResult
        ).any { it.error != null }

        if (hasError) {
            _taskState.update {
                it.copy(
                    titleError = taskTitleResult.error,
                    descriptionError = taskDetailResult.error,
                )
            }
            return
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                dao.upsertTask(
                    task = task
                )
                val project = dao.getProjectById(projectId)
                val totalTask = project.tasks.count().toFloat()
                val finishedTask = project.tasks.count { it.isComplete }.toFloat()
                val newProgress = finishedTask / totalTask
                project.project.progress = newProgress
                dao.upsertProject(project.project)
                _projectWithTasksState.update { project }
                taskValidationEventChannel.send(ValidationEvent.Success)
            }
            _taskState.update {
                it.copy(
                    titleError = null,
                    descriptionError = null,
                )
            }
        }
    }


    sealed class ValidationEvent {
        data object Success : ValidationEvent()
    }
}