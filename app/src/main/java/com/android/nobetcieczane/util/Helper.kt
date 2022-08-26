package com.android.nobetcieczane.util

import android.content.Context
import com.android.nobetcieczane.DistrictCityModel
import com.google.gson.Gson
import java.io.IOException

class Helper {
    fun getJsonDataFromAssets(context: Context, fileName: String): DistrictCityModel? {
        val jsonString: String
        val gson = Gson()
        val response: DistrictCityModel
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            response = gson.fromJson(jsonString, DistrictCityModel::class.java)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return response
    }
}