package com.example.todo.ui.todo_list

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.todo.R
import com.example.todo.ToDoApplication
import com.example.todo.database.TaskRepository
import com.example.todo.databinding.FragmentTodoListBinding
import com.example.todo.ui.todo_list.adapter.DayViewContainer
import com.example.todo.ui.todo_list.adapter.TasksAdapter
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class ToDoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private var selectedDay: LocalDate = LocalDate.now()
    private var displayedYearMonth = YearMonth.from(selectedDay)

    private lateinit var taskRepository: TaskRepository
    private lateinit var tasksAdapter: TasksAdapter

    private var tasksJob: Job? = null

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

        taskRepository = (requireActivity().application as ToDoApplication).taskRepository

        tasksAdapter = TasksAdapter(
            onTaskDoneClicked = { task ->
                viewLifecycleOwner.lifecycleScope.launch {
                    taskRepository.updateTaskIsDone(task.id, !task.isDone)
                }
            },
            onTaskDeleteSwipe = { task ->
                viewLifecycleOwner.lifecycleScope.launch {
                    taskRepository.deleteTask(task)
                }
            },
            onTaskClicked = { task ->
                val updateTaskFragment = UpdateTaskFragment(task) { updateTask ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        taskRepository.updateTask(updateTask)
                    }
                }
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(R.id.todo_frame_layout, updateTaskFragment)
                    .addToBackStack(null)
                    .commit()
            }
        )
        binding.tasksRecyclerView.adapter = tasksAdapter

        observeTasks()
        setupCalendar()
        setupDayBinder()
    }

    private fun observeTasks() {
        tasksJob?.cancel()
        tasksJob = viewLifecycleOwner.lifecycleScope.launch {
            taskRepository.getTasksByDay(selectedDay).collectLatest { tasksList ->
                tasksAdapter.submitList(tasksList)
            }
        }
    }

    private fun setupDayBinder() {
        binding.weekCalendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) {
                container.apply {
                    binding.weekDay.text =
                        data.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    binding.monthDay.text = data.date.dayOfMonth.toString()
                    this.view.setOnClickListener { updateSelectedDay(data.date) }
                    container.binding.root.cardElevation =
                        if (selectedDay == data.date) 24f else 0f
                    updateTextColor(this, data.date)
                }
            }
        }
    }

    private fun updateSelectedDay(newDate: LocalDate) {
        if (selectedDay == newDate) return

        val oldSelectedDay = selectedDay
        selectedDay = newDate
        binding.weekCalendarView.notifyDateChanged(oldSelectedDay)
        binding.weekCalendarView.notifyDateChanged(selectedDay)
        observeTasks()
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
        val currentYearMonth = YearMonth.now()
        binding.weekCalendarView.apply {
            setup(
                currentYearMonth.minusYears(1).withMonth(1).atStartOfMonth(),
                currentYearMonth.plusYears(1).withMonth(12).atEndOfMonth(),
                firstDayOfWeekFromLocale()
            )
            scrollToWeek(selectedDay)
            weekScrollListener = { weekDays ->
                val referenceDayInWeek = weekDays.days[3].date
                if (YearMonth.from(referenceDayInWeek) != displayedYearMonth)
                    displayedYearMonth = YearMonth.from(referenceDayInWeek)
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
