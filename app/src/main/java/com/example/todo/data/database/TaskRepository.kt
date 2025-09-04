package com.example.todo.data.database

import com.example.todo.data.database.dao.TaskDao
import com.example.todo.data.database.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun addTask(task: Task) = taskDao.addTask(task)

    fun getTasksByDay(day: LocalDate): Flow<List<Task>> =
        taskDao.getTasksByDay(day.atStartOfDay(), day.atTime(23, 59))

    suspend fun updateTaskIsDone(taskId: Int, isDone: Boolean) =
        taskDao.updateIsDone(taskId, isDone)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}
