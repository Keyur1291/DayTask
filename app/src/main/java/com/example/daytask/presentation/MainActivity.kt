package com.example.daytask.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.daytask.data.local.database.ProjectDatabase
import com.example.daytask.navigation.nav_controller.NavController
import com.example.daytask.presentation.ui.theme.DayTaskTheme
import com.example.daytask.presentation.ui.theme.splashBgColor
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
                this,
                ProjectDatabase::class.java,
                "projects.db"
            ).fallbackToDestructiveMigration(false)
            .build()
    }
    private val projectViewModel by viewModels<ProjectViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProjectViewModel(database.projectDao) as T
                }
            }
        }
    )
    private val userViewModel by viewModels<UserViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.Companion.dark(
                scrim = Color.Companion.Transparent.value.toInt()
            ),
            navigationBarStyle = SystemBarStyle.Companion.dark(
                scrim = Color.Companion.Transparent.value.toInt(),
            )
        )

        val permissions = if(Build.VERSION.SDK_INT >= 33) {
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
            )
        } else arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY)

        ActivityCompat.requestPermissions(
            this, permissions, 0
        )

        setContent {

            val userState by userViewModel.userState.collectAsState()
            val projectState by projectViewModel.projectState.collectAsState()
            val taskState by projectViewModel.taskState.collectAsState()

            DayTaskTheme {
                Surface(
                    color = splashBgColor
                ) {
                    NavController(
                        modifier = Modifier.fillMaxSize(),
                        onUserEvent = userViewModel::onUserEvent,
                        userState = userState,
                        taskValidationEvent = projectViewModel.taskValidationEvent,
                        taskState = taskState,
                        onTaskEvent = projectViewModel::onTaskEvent,
                        projectViewModel = projectViewModel,
                        projectValidationEvent = projectViewModel.projectValidationEvent,
                        projectState = projectState,
                        onProjectEvent = projectViewModel::onProjectEvent,
                    )
                }
            }
        }
    }
}