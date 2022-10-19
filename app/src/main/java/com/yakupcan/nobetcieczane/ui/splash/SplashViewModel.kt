package com.yakupcan.nobetcieczane.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakupcan.nobetcieczane.domain.use_case.UseCases
import com.yakupcan.nobetcieczane.util.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferences : MyPreferences,
    private val firestoreUseCases : UseCases
) : ViewModel() {
    fun saveCityTown(city : String, town : String) {
        preferences.setCity(city)
        preferences.setTown(town)
    }

    fun getCityLocation() : String? {
        return preferences.getCity()
    }

    fun getTownLocation() : String? {
        return preferences.getTown()
    }

    fun saveLocationLatLng(lat : Double, lng : Double) {
        preferences.setLatitude(lat)
        preferences.setLongitude(lng)
    }

    fun getLat() : Float {
        return preferences.getLatitude()
    }

    fun getLng() : Float {
        return preferences.getLongitude()
    }

    fun savePushToken(androidID : String) {
        val pushToken = preferences.getPushToken()
        if (pushToken?.isEmpty() == true)
            return
        viewModelScope.launch {
            firestoreUseCases.tokenSave(androidID, pushToken!!).collect {
                Log.d("Firebase Token", "Firebase Push Token is successfully saved in Firestore.")
            }
        }
    }
}
