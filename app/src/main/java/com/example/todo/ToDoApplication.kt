package com.example.todo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.todo.data.database.TaskDatabase
import com.example.todo.data.database.TaskRepository
import com.example.todo.data.shared_prefs.SettingsSharedPreferences
import com.example.todo.data.shared_prefs.SettingsSharedPreferences.Companion.LIGHT_THEME

class ToDoApplication : Application() {

    private val database by lazy { TaskDatabase.Companion.getDatabase(this) }

    val taskRepository by lazy { TaskRepository(database.taskDao()) }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
            if (SettingsSharedPreferences(applicationContext).getTheme() == LIGHT_THEME)
                AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }
}
