package com.example.todo.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters // Added
import com.example.todo.data.database.typeConverter.DateTimeConverters
import java.time.LocalDateTime // Added

@Entity
@TypeConverters(DateTimeConverters::class)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val date: LocalDateTime,
    var isDone: Boolean = false,
)
