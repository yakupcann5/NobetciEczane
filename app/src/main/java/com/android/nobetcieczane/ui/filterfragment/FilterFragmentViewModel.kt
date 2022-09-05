package com.android.nobetcieczane.ui.filterfragment

import androidx.lifecycle.ViewModel
import com.android.nobetcieczane.util.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterFragmentViewModel @Inject constructor(private val preferences: MyPreferences) : ViewModel() {
    fun setFilterCity(filterCity: String) {
        preferences.setCity(filterCity)
    }

    fun getFilterCity(): String? {
        return preferences.getCity()
    }

    fun setFilterDistrict(filterDistrict: String) {
        preferences.setTown(filterDistrict)
    }

    fun getFilterDistrict(): String? {
        return preferences.getTown()
    }
}
