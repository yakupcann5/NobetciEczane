package com.android.nobetcieczane.ui.splash

import androidx.lifecycle.ViewModel
import com.android.nobetcieczane.domain.use_case.getPharmcy.GetPharmacyUseCase
import com.android.nobetcieczane.util.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val preferences: MyPreferences) : ViewModel() {
    fun saveCityTown(city: String, town: String) {
        preferences.setCity(city)
        preferences.setTown(town)
    }

    fun getCityLocation(): String? {
        return preferences.getCity()
    }

    fun getTownLocation(): String? {
        return preferences.getTown()
    }

    fun saveLocationLatLng(lat: Double, lng: Double) {
        preferences.setLatitude(lat)
        preferences.setLongitude(lng)
    }

    fun getLat(): Float {
        return preferences.getLatitude()
    }

    fun getLng(): Float {
        return preferences.getLongitude()
    }
}