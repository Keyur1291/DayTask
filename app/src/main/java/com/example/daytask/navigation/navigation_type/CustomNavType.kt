package com.example.daytask.navigation.navigation_type

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.daytask.data.local.model.Project
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.data.local.model.Task
import kotlinx.serialization.json.Json

object CustomNavType {

    val projectType = object: NavType<ProjectWithTasks>(
        isNullableAllowed = false
    ){
        override fun get(bundle: Bundle, key: String): ProjectWithTasks? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ProjectWithTasks {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ProjectWithTasks): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: ProjectWithTasks) {
            return bundle.putString(key, Json.encodeToString(value))
        }
    }

    val taskType = object: NavType<Task>(
        isNullableAllowed = false
    ){
        override fun get(bundle: Bundle, key: String): Task? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Task {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Task): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Task) {
            return bundle.putString(key, Json.encodeToString(value))
        }
    }
}