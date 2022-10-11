package com.yakupcan.nobetcieczane.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.databinding.ActivityMainBinding
import com.yakupcan.nobetcieczane.ui.main.MainActivityViewModel
import com.yakupcan.nobetcieczane.ui.settingsfragment.SettingsFragmentViewModel
import com.yakupcan.nobetcieczane.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applicationPreparation()
        checkedTheme()
    }

    fun selectedLanguage() {
        when (val selectedLang = viewModel.getSelectedLanguage()) {
            selectedLang.toString() -> {

            }
        }
    }

    private fun applicationPreparation() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val config = resources.configuration

        when (viewModel.getLanguage()) {
            "en" -> {
                viewModel.setSelectedLanguage("en")
            }
            "ar" -> {
                viewModel.setSelectedLanguage("ar")
            }
            "de" -> {
                viewModel.setSelectedLanguage("de")
            }
            "fr" -> {
                viewModel.setSelectedLanguage("fr")
            }
        }
        val lang = if (viewModel.getLanguage() == null) {
            "tr" // your language code
        } else {
            viewModel.getLanguage()!!
        }
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun checkedTheme() {
        if (viewModel.getSelectedTheme() == null) {
            if (viewModel.getSelectedTheme() == "night") {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    viewModel.setSelectedTheme("dark")
                } else {
                    viewModel.setSelectedTheme("night")
                }
            }
        } else {
            if (viewModel.getSelectedTheme() == "night") {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    viewModel.setSelectedTheme("dark")
                } else {
                    viewModel.setSelectedTheme("night")
                }
            }
        }
    }
}