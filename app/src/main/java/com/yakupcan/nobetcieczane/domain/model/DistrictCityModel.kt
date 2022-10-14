package com.yakupcan.nobetcieczane

import com.google.gson.annotations.SerializedName

data class DistrictCityModel
    (
    @SerializedName("data")
    var data: ArrayList<CityDistrictsItem> = arrayListOf()
)

data class CityDistrictsItem(
    @SerializedName("value")
    var value: Int,
    @SerializedName("text")
    var text: String,
    @SerializedName("districts")
    var districts: List<Districts>,
)

data class Districts(
    @SerializedName("text")
    var text: String,
    @SerializedName("value")
    var value: Int
)
