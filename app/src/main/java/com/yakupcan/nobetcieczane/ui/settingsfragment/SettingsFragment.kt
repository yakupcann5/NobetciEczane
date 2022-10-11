package com.yakupcan.nobetcieczane.ui.settingsfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deneme()
        selectedLanguage(viewModel.getSelectedLanguage())
        checkedTheme()
        initViews()
    }

    private fun initViews() {
        binding.settingsBackButton.setOnClickListener(this)
        binding.radioButtonEng.setOnClickListener(this)
        binding.radioButtonGerman.setOnClickListener(this)
        binding.radioButtonFrench.setOnClickListener(this)
        binding.radioButtonArabic.setOnClickListener(this)
        binding.lightModeLinear.setOnClickListener(this)
        binding.darkModeLinear.setOnClickListener(this)
        binding.darkThemeSwitch.setOnClickListener(null)
        binding.lightThemeSwitch.setOnClickListener(null)
        if (viewModel.getSelectedTheme() == "night") {
            binding.lightThemeSwitch.isChecked = true
            binding.darkThemeSwitch.isChecked = false
        } else {
            binding.lightThemeSwitch.isChecked = false
            binding.darkThemeSwitch.isChecked = true
        }
        //binding.systemDefaultLinear.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.settings_back_button -> {
                findNavController().navigate(R.id.action_settingsFragment_to_mapsFragment2)
            }
            R.id.radioButtonEng -> {
                setLanguage("en")
                checkedLanguage(v)
            }
            R.id.radioButtonFrench -> {
                setLanguage("fr")
                checkedLanguage(v)
            }
            R.id.radioButtonGerman -> {
                setLanguage("de")
                checkedLanguage(v)
            }
            R.id.radioButtonArabic -> {
                setLanguage("ar")
                checkedLanguage(v)
            }
            R.id.light_mode_linear -> {
                if (viewModel.getSelectedTheme() == "dark") {
                    viewModel.setSelectedTheme("night")
                    binding.lightThemeSwitch.isChecked = true
                    binding.darkThemeSwitch.isChecked = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.setSelectedTheme("night")
                    //setTheme()
                } else {
                    viewModel.setSelectedTheme("night")
                    binding.lightThemeSwitch.isChecked = true
                    binding.darkThemeSwitch.isChecked = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            R.id.dark_mode_linear -> {
                if (viewModel.getSelectedTheme() == "night") {
                    viewModel.setSelectedTheme("dark")
                    binding.lightThemeSwitch.isChecked = false
                    binding.darkThemeSwitch.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    //setTheme()
                } else {
                    viewModel.setSelectedTheme("dark")
                    binding.lightThemeSwitch.isChecked = false
                    binding.darkThemeSwitch.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }

    private fun setLanguage(language: String) {
        viewModel.setLanguage(language)
        val intent = requireActivity().intent
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION
        )
        requireActivity().overridePendingTransition(0, 0)
        requireActivity().finish()
        requireActivity().overridePendingTransition(0, 0)
        startActivity(intent)
    }

    private fun checkedLanguage(selectedLanguage: View) {
        // hangi dil seçildiyse onun checked yapılması için yazılacak switch case kullanılacak
        // view model üzerinden preferencese kayıt yapılacak
        if (selectedLanguage is RadioButton) {
            val checked = selectedLanguage.isChecked

            // Check which radio button was clicked
            when (selectedLanguage.getId()) {
                R.id.radioButtonEng ->
                    if (checked) {
                        viewModel.selectedLanguage("en")
                        binding.radioButtonEng.isChecked = true
                        binding.radioButtonFrench.isChecked = false
                        binding.radioButtonGerman.isChecked = false
                        binding.radioButtonArabic.isChecked = false
                    }
            }
        }
    }

    private fun checkedTheme() {
        val selectedTheme = viewModel.getSelectedTheme()
        if (selectedTheme != null) {
            if (selectedTheme == "dark") {
                binding.darkThemeSwitch.isChecked = true
                binding.lightThemeSwitch.isChecked = false
                //binding.systemDefaultSwitch.isChecked = false

            }
            if (selectedTheme == "night") {
                binding.darkThemeSwitch.isChecked = false
                binding.lightThemeSwitch.isChecked = true
                //binding.systemDefaultSwitch.isChecked = false
            }
        }
    }

    private fun setTheme() {
        val intent = requireActivity().intent
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION
        )
        requireActivity().overridePendingTransition(0, 0)
        requireActivity().finish()
        requireActivity().overridePendingTransition(0, 0)
        startActivity(intent)
    }

    private fun selectedLanguage(language: String?) {
        when (language.toString()) {
            "en" -> {
                binding.radioButtonEng.isChecked = true
                binding.radioButtonFrench.isChecked = false
                binding.radioButtonArabic.isChecked = false
                binding.radioButtonGerman.isChecked = false
            }
            "ar" -> {
                binding.radioButtonEng.isChecked = false
                binding.radioButtonFrench.isChecked = false
                binding.radioButtonArabic.isChecked = true
                binding.radioButtonGerman.isChecked = false
            }
            "de" -> {
                binding.radioButtonEng.isChecked = false
                binding.radioButtonFrench.isChecked = false
                binding.radioButtonArabic.isChecked = false
                binding.radioButtonGerman.isChecked = true
            }
            "fr" -> {
                binding.radioButtonEng.isChecked = false
                binding.radioButtonFrench.isChecked = true
                binding.radioButtonArabic.isChecked = false
                binding.radioButtonGerman.isChecked = false
            }
        }
    }

    fun deneme() {
        binding.darkThemeSwitch.isClickable = false
        binding.lightThemeSwitch.isClickable = false
    }
}