package com.example.todo.ui.settings

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        AppCompatDelegate.getApplicationLocales()[0]?.displayName?.let {
            binding.languageAutoComplete.setText(it, false)
        }

        binding.languageAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val languageCode = if (position == 0)
                SettingsSharedPreferences.ENGLISH_LANGUAGE_CODE
            else SettingsSharedPreferences.ARABIC_LANGUAGE_CODE

            val localeListCompat = LocaleListCompat.forLanguageTags(languageCode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val localeManager = requireActivity().getSystemService(LocaleManager::class.java)
                localeManager?.applicationLocales = LocaleList.forLanguageTags(languageCode)
            } else {
                AppCompatDelegate.setApplicationLocales(localeListCompat)
            }
        }
    }

    private fun setupThemeDropdown() {
        val themes = resources.getStringArray(R.array.theme_array)
//        val adapter =
//            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, themes)
//        binding.themeAutoComplete.setAdapter(adapter)

        val currentThemeInitialText =
            if (settingsSharedPreferences.getTheme() == SettingsSharedPreferences.LIGHT_THEME)
                themes.getOrNull(0)
            else themes.getOrNull(1)
        currentThemeInitialText?.let { binding.themeAutoComplete.setText(it, false) }

        binding.themeAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedThemeName = parent.getItemAtPosition(position).toString()
            settingsSharedPreferences.setTheme(selectedThemeName)

            val nightMode =
                if (selectedThemeName == themes.getOrNull(0)) {
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
