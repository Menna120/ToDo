package com.example.todo.ui.update_task

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.data.database.model.Task
import com.example.todo.databinding.FragmentUpdateTaskBinding
import com.example.todo.utils.dateFormatter
import com.example.todo.utils.timeFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class UpdateTaskFragment(
    private val task: Task,
    private val onSaveChangesClicked: (task: Task) -> Unit
) : Fragment() {

    private var _binding: FragmentUpdateTaskBinding? = null
    private val binding get() = _binding!!

    private var updatedDate = task.date.toLocalDate()
    private var updatedTime = task.date.toLocalTime()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.taskTitleEditText.editText?.setText(task.title)
        binding.taskDescriptionEditText.editText?.setText(task.description)
        binding.dateText.text = task.date.toLocalDate().format(dateFormatter(requireContext()))
        binding.timeText.text = task.date.toLocalTime().format(timeFormatter(requireContext()))

        binding.dateText.setOnClickListener { showDatePickerDialog() }
        binding.timeText.setOnClickListener { showTimePickerDialog() }
        binding.toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        binding.saveButton.setOnClickListener {
            val title = binding.taskTitleEditText.editText?.text?.trim().toString()
            val description = binding.taskDescriptionEditText.editText?.text?.trim().toString()
            val date = LocalDateTime.of(updatedDate, updatedTime)
            val isDone =
                if (title == task.title && description == task.description && date == task.date) task.isDone else false
            val updatedTask = task.copy(
                title = title,
                description = description,
                date = date,
                isDone = isDone
            )
            onSaveChangesClicked(updatedTask)
            parentFragmentManager.popBackStack()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker =
            MaterialDatePicker.Builder
                .datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(parentFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            updatedDate = LocalDate.ofEpochDay(it / 86400000)
            binding.dateText.text = updatedDate.format(dateFormatter(requireContext()))
        }
    }

    private fun showTimePickerDialog() {
        val clockFormat =
            if (DateFormat.is24HourFormat(requireActivity())) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .build()
        picker.show(parentFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            updatedTime = LocalTime.of(picker.hour, picker.minute)
            binding.timeText.text = updatedTime.format(timeFormatter(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
