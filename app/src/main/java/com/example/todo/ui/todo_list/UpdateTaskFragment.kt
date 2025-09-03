package com.example.todo.ui.todo_list

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.database.model.Task
import com.example.todo.databinding.FragmentUpdateTaskDialogBinding
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

    private var _binding: FragmentUpdateTaskDialogBinding? = null
    private val binding get() = _binding!!

    private var updatedDate = task.date.toLocalDate()
    private var updatedTime = task.date.toLocalTime()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateTaskDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.taskTitleEditText.editText?.setText(task.title)
        binding.taskDescriptionEditText.editText?.setText(task.description)
        binding.dateText.text = task.date.format(dateFormatter)
        binding.timeText.text = task.date.format(timeFormatter)

        binding.dateText.setOnClickListener { showDatePickerDialog() }
        binding.timeText.setOnClickListener { showTimePickerDialog() }
        binding.toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        binding.saveButton.setOnClickListener {
            val title = binding.taskTitleEditText.editText?.text.toString()
            val description = binding.taskDescriptionEditText.editText?.text.toString()
            val updatedTask = task.copy(
                title = title,
                description = description,
                date = LocalDateTime.of(updatedDate, updatedTime)
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
            binding.dateText.text = updatedDate.format(dateFormatter)
        }
    }

    private fun showTimePickerDialog() {
        val clockFormat =
            if (is24HourFormat(requireActivity())) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .build()
        picker.show(parentFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            updatedTime = LocalTime.of(picker.hour, picker.minute)
            binding.timeText.text = updatedTime.format(timeFormatter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
