package com.android.nobetcieczane.ui.filterfragment

import androidx.lifecycle.ViewModel
import com.android.nobetcieczane.util.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterFragmentViewModel @Inject constructor(private val preferences: MyPreferences) : ViewModel() {
    fun setFilterCity(filterCity: String) {
        preferences.setCityFilter(filterCity)
    }

    fun getFilterCity(): String? {
        return preferences.getCityFilter()
    }

    fun setFilterDistrict(filterDistrict: String) {
        preferences.setDistrictFilter(filterDistrict)
    }

    fun getFilterDistrict(): String? {
        return preferences.getDistrictFilter()
    }
}
