package com.example.todo.ui.todo_list

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.data.tasks
import com.example.todo.databinding.FragmentTodoListBinding
import com.example.todo.ui.todo_list.adapter.DayViewContainer
import com.example.todo.ui.todo_list.adapter.TasksAdapter
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class ToDoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private var selectedDay: LocalDate = LocalDate.now()
    private var displayedYearMonth = YearMonth.from(selectedDay)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tasksRecyclerView.adapter = TasksAdapter(tasks) { i ->
            tasks[i].isDone = true
            binding.tasksRecyclerView.adapter?.notifyItemChanged(i) // Consider using DiffUtil for better performance
        }
        setupCalendar()
        setupDayBinder()
    }

    private fun setupDayBinder() {
        binding.weekCalendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) = container.run {
                binding.weekDay.text =
                    data.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                binding.monthDay.text = data.date.dayOfMonth.toString()
                this.view.setOnClickListener { updateSelectedDay(data.date) }
                binding.root.cardElevation =
                    if (selectedDay == data.date) 16f else 0f // Use resources for dimensions
                updateTextColor(this, data.date)
            }
        }
    }

    private fun updateSelectedDay(newDate: LocalDate) {
        if (selectedDay == newDate) return

        binding.weekCalendarView.notifyDateChanged(selectedDay)
        selectedDay = newDate
        binding.weekCalendarView.notifyDateChanged(selectedDay)
    }

    private fun updateTextColor(container: DayViewContainer, date: LocalDate) {
        val colorRes =
            if (selectedDay == date) com.google.android.material.R.attr.colorPrimary
            else com.google.android.material.R.attr.colorOnSurface
        val typedValue =
            TypedValue().also { requireContext().theme.resolveAttribute(colorRes, it, true) }
        container.binding.weekDay.setTextColor(typedValue.data)
        container.binding.monthDay.setTextColor(typedValue.data)
    }

    private fun setupCalendar() {
        val currentMonth = YearMonth.now()
        binding.weekCalendarView.apply {
            setup(
                currentMonth.minusMonths(100).atStartOfMonth(),
                currentMonth.plusMonths(100).atEndOfMonth(),
                firstDayOfWeekFromLocale()
            )
            scrollToWeek(selectedDay)
            weekScrollListener = { weekDays ->
                val firstDateInWeek = weekDays.days[3].date
                displayedYearMonth = YearMonth.from(firstDateInWeek)
                binding.monthYearText.text = getString(
                    R.string.month_year_format,
                    displayedYearMonth.month.getDisplayName(
                        TextStyle.SHORT_STANDALONE,
                        Locale.getDefault()
                    ),
                    displayedYearMonth.year
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
