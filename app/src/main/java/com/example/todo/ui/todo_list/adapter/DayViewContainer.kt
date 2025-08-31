package com.example.todo.ui.todo_list.adapter

import android.view.View
import com.example.todo.databinding.CalendarDayBinding
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val binding = CalendarDayBinding.bind(view)
}
