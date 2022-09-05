package com.android.nobetcieczane.util

import android.content.SharedPreferences
import android.util.Log
import com.android.nobetcieczane.data.model.DataDto
import com.google.gson.Gson
import javax.inject.Inject


class MyPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {
    val PHARMACY_DATA_DTO = "pharmacy_data_dto"
    val LATITUDE = "now_latitude"
    val LONGITUDE = "now_longitude"
    val CITY = "city"
    val TOWN = "town"
    val CENTER = "merkez"
    val FILTER_CITY = "filter_city"
    val FILTER_DISTRICT = "filter_district"
    val PHONE_NUMBER = "phone_number"
    val BOTTOM_SHEET_LNG = "bottom_sheet_lng"
    val BOTTOM_SHEET_LAT = "bottom_sheet_lat"


    fun setData(pharmacy: ArrayList<DataDto>) {
        val gson = Gson()
        val json = gson.toJson(pharmacy)
        sharedPreferences.edit().putString(PHARMACY_DATA_DTO, json).apply()
    }

    fun getLatitude(): Float {
        return sharedPreferences.getFloat(LATITUDE, 0F)
    }

    fun setLatitude(locLAT: Double) {
        sharedPreferences.edit().putFloat(LATITUDE, locLAT.toFloat()).apply()
    }

    fun getLongitude(): Float {
        return sharedPreferences.getFloat(LONGITUDE, 0F)
    }

    fun setLongitude(locLNG: Double) {
        sharedPreferences.edit().putFloat(LONGITUDE, locLNG.toFloat()).apply()
    }

    fun setCity(city: String) {
        sharedPreferences.edit().putString(CITY, Tools.trEngCevir(city)).apply()
    }

    fun getCity(): String? {
        return sharedPreferences.getString(CITY, "")
    }

    fun getTown(): String? {
        return sharedPreferences.getString(TOWN, "")
    }

    fun setTown(town: String) {
        if (town.contains("Merkez")) {
            sharedPreferences.edit().putString(TOWN, CENTER).apply()
        } else {
            sharedPreferences.edit().putString(TOWN, Tools.trEngCevir(town)).apply()
        }
    }

    fun getPhoneNumber(): String? {
        return sharedPreferences.getString(PHONE_NUMBER, "")
    }

    fun setPhoneNumber(phoneNumber: String) {
        sharedPreferences.edit().putString(PHONE_NUMBER, phoneNumber).apply()
    }

    fun getMarkerLocationLng(): Float {
        return sharedPreferences.getFloat(BOTTOM_SHEET_LNG, 0F)
    }

    fun setMarkerLocationLng(markerLng: Double) {
        sharedPreferences.edit().putFloat(BOTTOM_SHEET_LNG,markerLng.toFloat()).apply()
    }

    fun getMarkerLocationLat(): Float {
        return sharedPreferences.getFloat(BOTTOM_SHEET_LAT, 0F)
    }

    fun setMarkerLocationLat(markerLat: Double) {
        sharedPreferences.edit().putFloat(BOTTOM_SHEET_LAT, markerLat.toFloat()).apply()
    }
}