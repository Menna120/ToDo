package com.example.todo.database

import com.example.todo.database.dao.TaskDao
import com.example.todo.database.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun addTask(task: Task) = taskDao.addTask(task)

    suspend fun updateTaskIsDone(taskId: Int, isDone: Boolean) =
        taskDao.updateIsDone(taskId, isDone)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}
