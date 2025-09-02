package com.example.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todo.database.TaskRepository
import com.example.todo.database.model.Task
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.utils.dateFormatter
import com.example.todo.utils.timeFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var taskRepository: TaskRepository

    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedTime: LocalTime = LocalTime.MIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskRepository = (application as ToDoApplication).taskRepository

        setupNavigation()
        setupBottomSheet()
        setupFab()

        onBackPressedDispatcher.addCallback {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                isEnabled = false
                handleOnBackPressed()
                isEnabled = true
            }
        }
    }

    private fun setupNavigation() {
        val navController = (
                supportFragmentManager.findFragmentById(
                    R.id.nav_host_fragment_activity_main
                ) as NavHostFragment).navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_todo_list, R.id.navigation_settings))

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun setupBottomSheet() {
        val titleEditText: EditText = binding.addTaskBottomSheetContent.taskTitleEditText.editText!!
        val descriptionEditText: EditText =
            binding.addTaskBottomSheetContent.taskDescriptionEditText.editText!!
        val timeText = binding.addTaskBottomSheetContent.timeText
        val dayText = binding.addTaskBottomSheetContent.dateText

        dayText.text = selectedDate.format(dateFormatter)
        timeText.text = selectedTime.format(timeFormatter)

        dayText.setOnClickListener { showDatePickerDialog() }
        timeText.setOnClickListener { showTimePickerDialog() }

        val bottomSheetView = binding.addTaskBottomSheet
        (bottomSheetView.layoutParams as CoordinatorLayout.LayoutParams).behavior =
            BottomSheetBehavior<View>()
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED,
                    BottomSheetBehavior.STATE_EXPANDED -> updateFabIcon(
                        titleEditText,
                        descriptionEditText
                    )

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        val textWatcher = createTextWatcher(titleEditText, descriptionEditText)
        titleEditText.addTextChangedListener(textWatcher)
        descriptionEditText.addTextChangedListener(textWatcher)
    }

    private fun showDatePickerDialog() {
        val datePicker =
            MaterialDatePicker.Builder
                .datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(supportFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            selectedDate = LocalDate.ofEpochDay(it / 86400000)
            binding.addTaskBottomSheetContent.dateText.text = selectedDate.format(dateFormatter)
        }
    }

    private fun showTimePickerDialog() {
        val clockFormat = if (is24HourFormat(this)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .build()
        picker.show(supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            selectedTime = LocalTime.of(picker.hour, picker.minute)
            binding.addTaskBottomSheetContent.timeText.text = selectedTime.format(timeFormatter)
        }
    }


    private fun createTextWatcher(
        titleEditText: EditText,
        descriptionEditText: EditText
    ): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                updateFabIcon(titleEditText, descriptionEditText)
            }
        }
    }

    private fun setupFab() {
        binding.addNewTaskFab.setOnClickListener {
            val titleEditText = binding.addTaskBottomSheetContent.taskTitleEditText.editText!!
            val descriptionEditText =
                binding.addTaskBottomSheetContent.taskDescriptionEditText.editText!!
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                if (title.isNotEmpty()) {
                    val taskDateTime = LocalDateTime.of(selectedDate, selectedTime)
                    val newTask = Task(
                        title = title,
                        description = description,
                        date = taskDateTime
                    )
                    lifecycleScope.launch { taskRepository.addTask(newTask) }
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun updateFabIcon(titleEditText: EditText, descriptionEditText: EditText) {
        val timeText = binding.addTaskBottomSheetContent.timeText
        val dayText = binding.addTaskBottomSheetContent.dateText

        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            val titleHasText = titleEditText.text.isNotEmpty()
            val iconRes = if (titleHasText) R.drawable.ic_check else R.drawable.ic_close
            binding.addNewTaskFab.setImageResource(iconRes)
        } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            binding.addNewTaskFab.setImageResource(R.drawable.ic_add)
            titleEditText.text.clear()
            descriptionEditText.text.clear()

            selectedDate = LocalDate.now()
            selectedTime = LocalTime.now()
            dayText.text = selectedDate.format(dateFormatter)
            timeText.text = selectedTime.format(timeFormatter)

            hideKeyboard(titleEditText)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
