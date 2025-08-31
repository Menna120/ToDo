package com.example.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todo.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupBottomSheet()
        setupFab()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setSupportActionBar(binding.toolbar)
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_todo_list, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun setupBottomSheet() {
        val titleEditText: EditText = binding.addTaskBottomSheetContent.taskTitleEditText.editText!!
        val descriptionEditText: EditText =
            binding.addTaskBottomSheetContent.taskDescriptionEditText.editText!!

        val bottomSheetView = binding.addTaskBottomSheet
        (bottomSheetView.layoutParams as CoordinatorLayout.LayoutParams).behavior =
            BottomSheetBehavior<View>()
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> handleBottomSheetCollapsed(
                        titleEditText,
                        descriptionEditText
                    )

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

        binding.root.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun handleBottomSheetCollapsed(titleEditText: EditText, descriptionEditText: EditText) {
        binding.addNewTaskFab.setImageResource(R.drawable.ic_add)
        titleEditText.text.clear()
        descriptionEditText.text.clear()
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
            bottomSheetBehavior.state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
                    BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun updateFabIcon(titleEditText: EditText, descriptionEditText: EditText) {
        val titleHasText = titleEditText.text.isNotEmpty()
        val descriptionHasText = descriptionEditText.text.isNotEmpty()
        val iconRes =
            if (titleHasText && descriptionHasText) R.drawable.ic_check else R.drawable.ic_close
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            binding.addNewTaskFab.setImageResource(iconRes)
        }
    }
}
