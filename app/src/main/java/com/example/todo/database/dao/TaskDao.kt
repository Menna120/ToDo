package com.example.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.database.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun addTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("UPDATE task SET isDone = :isDone WHERE id = :id")
    suspend fun updateIsDone(id: Int, isDone: Boolean)

    @Delete
    suspend fun deleteTask(task: Task)
}
