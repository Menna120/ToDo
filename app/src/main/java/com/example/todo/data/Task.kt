package com.example.todo.data

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

data class Task(
    val title: String,
    val description: String,
    val date: Date,
    var isDone: Boolean = false
)

val tasks = mutableListOf(
    Task(
        "Task 1",
        "Description 1",
        Date.from(LocalDate.now().atTime(9, 0).atZone(ZoneId.systemDefault()).toInstant())
    ),
    Task(
        "Task 2",
        "Description 2",
        Date.from(
            LocalDate.now().plusDays(1).atTime(10, 30).atZone(ZoneId.systemDefault()).toInstant()
        ),
        true
    ),
    Task(
        "Task 3",
        "Description 3",
        Date.from(
            LocalDate.now().plusDays(2).atTime(14, 0).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 4",
        "Description 4",
        Date.from(
            LocalDate.now().plusDays(3).atTime(8, 15).atZone(ZoneId.systemDefault()).toInstant()
        ),
        true
    ),
    Task(
        "Task 5",
        "Description 5",
        Date.from(
            LocalDate.now().plusDays(4).atTime(16, 45).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 6",
        "Description 6",
        Date.from(
            LocalDate.now().plusDays(5).atTime(11, 0).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 7",
        "Description 7",
        Date.from(
            LocalDate.now().plusDays(6).atTime(13, 20).atZone(ZoneId.systemDefault()).toInstant()
        ),
        true
    ),
    Task(
        "Task 8",
        "Description 8",
        Date.from(
            LocalDate.now().plusDays(7).atTime(9, 50).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 9",
        "Description 9",
        Date.from(
            LocalDate.now().plusDays(8).atTime(17, 0).atZone(ZoneId.systemDefault()).toInstant()
        ),
        true
    ),
    Task(
        "Task 10",
        "Description 10",
        Date.from(
            LocalDate.now().plusDays(9).atTime(7, 30).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 11",
        "Description 11",
        Date.from(
            LocalDate.now().plusDays(10).atTime(12, 10).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 12",
        "Description 12",
        Date.from(
            LocalDate.now().plusDays(11).atTime(15, 55).atZone(ZoneId.systemDefault()).toInstant()
        ),
        true
    ),
    Task(
        "Task 13",
        "Description 13",
        Date.from(
            LocalDate.now().plusDays(12).atTime(8, 40).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 14",
        "Description 14",
        Date.from(
            LocalDate.now().plusDays(12).atTime(18, 0).atZone(ZoneId.systemDefault()).toInstant()
        )
    ),
    Task(
        "Task 15",
        "Description 15",
        Date.from(
            LocalDate.now().plusDays(13).atTime(10, 5).atZone(ZoneId.systemDefault()).toInstant()
        ),
        true
    ),
)
