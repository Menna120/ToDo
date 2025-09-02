package com.example.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo.database.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TaskDao {
    @Insert
    suspend fun addTask(task: Task)

    @Query("SELECT * FROM task WHERE date >= :startOfDay AND date <= :endOfDay ORDER BY date ASC")
    fun getTasksByDay(startOfDay: LocalDateTime, endOfDay: LocalDateTime): Flow<List<Task>>

    @Query("UPDATE task SET isDone = :isDone WHERE id = :id")
    suspend fun updateIsDone(id: Int, isDone: Boolean)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}
