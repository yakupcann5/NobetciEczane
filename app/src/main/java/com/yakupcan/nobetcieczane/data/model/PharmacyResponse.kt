package com.yakupcan.nobetcieczane.data.model

import com.google.gson.annotations.SerializedName

data class PharmacyResponse(
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("rowCount")
    var rowCount: Int? = null,
    @SerializedName("data")
    var data: ArrayList<DataDto> = arrayListOf(),
)
