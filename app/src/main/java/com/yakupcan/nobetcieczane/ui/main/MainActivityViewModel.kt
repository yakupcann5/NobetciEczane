package com.yakupcan.nobetcieczane.ui.main

import androidx.lifecycle.ViewModel
import com.yakupcan.nobetcieczane.util.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val preferences: MyPreferences) :
    ViewModel() {
    fun getLanguage(): String? {
        return preferences.getLanguage()
    }

    fun setLanguage(language: String?) {
        preferences.setLanguage(language)
    }

    fun getSelectedLanguage(): String? {
        return preferences.getSelectedLanguage()
    }

    fun setSelectedLanguage(selectedLanguage: String?) {
        preferences.setSelectedLanguage(selectedLanguage)
    }


    fun setSelectedTheme(theme: String?) {
        preferences.setSelectedTheme(theme)
    }

    fun getSelectedTheme(): String? {
        return preferences.getSelectedTheme()
    }
}