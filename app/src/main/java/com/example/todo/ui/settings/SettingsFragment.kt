package com.example.todo.ui.settings

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.data.shared_prefs.SettingsSharedPreferences
import com.example.todo.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsSharedPreferences: SettingsSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsSharedPreferences = SettingsSharedPreferences(requireContext())
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLanguageDropdown()
        setupThemeDropdown()
    }

    private fun setupLanguageDropdown() {
        val languages = resources.getStringArray(R.array.language_array)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSpinner.adapter = adapter

        val currentLanguageCode = AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()
        val currentLanguagePosition =
            if (currentLanguageCode == SettingsSharedPreferences.ARABIC_LANGUAGE_CODE) 1 else 0
        binding.languageSpinner.setSelection(currentLanguagePosition)

        binding.languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val languageCode = if (position == 0)
                        SettingsSharedPreferences.ENGLISH_LANGUAGE_CODE
                    else SettingsSharedPreferences.ARABIC_LANGUAGE_CODE

                    if (AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag() == languageCode) return

                    val localeListCompat = LocaleListCompat.forLanguageTags(languageCode)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val localeManager =
                            requireActivity().getSystemService(LocaleManager::class.java)
                        localeManager?.applicationLocales = LocaleList.forLanguageTags(languageCode)
                    } else {
                        AppCompatDelegate.setApplicationLocales(localeListCompat)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setupThemeDropdown() {
        val themes = resources.getStringArray(R.array.theme_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.themeSpinner.adapter = adapter

        val currentThemePosition =
            if (settingsSharedPreferences.getTheme() == SettingsSharedPreferences.LIGHT_THEME)
                0
            else 1
        binding.themeSpinner.setSelection(currentThemePosition)

        binding.themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedThemeName = parent?.getItemAtPosition(position).toString()

                if (settingsSharedPreferences.getTheme() == selectedThemeName) return

                settingsSharedPreferences.setTheme(selectedThemeName)

                val nightMode =
                    if (selectedThemeName == themes.getOrNull(0)) AppCompatDelegate.MODE_NIGHT_NO
                    else AppCompatDelegate.MODE_NIGHT_YES
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
