package com.yakupcan.nobetcieczane.domain.model

import com.google.gson.Gson
import java.io.Serializable

data class Pharmacy(
    val eczaneAdi: String? = "",
    val adresi: String? = "",
    val semt: String? = "",
    val yolTarifi: String? = "",
    val telefon: String? = "",
    val telefon2: String? = "",
    val sehir: String? = "",
    val ilce: String? = "",
    val latitude: Double? = .0,
    val longitude: Double? = .0
) : Serializable

fun Pharmacy.toJSON(): String {
    val gson = Gson()
    val json = gson.toJson(this)
    return json
}