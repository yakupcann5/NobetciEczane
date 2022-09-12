package com.yakupcan.nobetcieczane.ui.filterfragment

import androidx.lifecycle.ViewModel
import com.yakupcan.nobetcieczane.util.MyPreferences
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

    fun getDisc(): String? {
        return preferences.getFilterDisc()
    }

    fun setDisc(disc: String) {
        preferences.setFilterDisc(disc)
    }

    fun getCity(): String? {
        return preferences.getFilterCity()
    }

    fun setCity(disc: String) {
        preferences.setFilterCity(disc)
    }
}
