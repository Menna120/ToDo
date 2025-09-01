package com.example.todo

import android.app.Application
import com.example.todo.database.TaskDatabase
import com.example.todo.database.TaskRepository

class ToDoApplication : Application() {

    private val database by lazy { TaskDatabase.Companion.getDatabase(this) }

    val taskRepository by lazy { TaskRepository(database.taskDao()) }
}
