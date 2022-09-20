package com.yakupcan.nobetcieczane.data.model

import com.yakupcan.nobetcieczane.domain.model.Pharmacy
import com.google.gson.annotations.SerializedName

class DataDto(
    @SerializedName("EczaneAdi")
    val eczaneAdi: String,
    @SerializedName("Adresi")
    val adresi: String,
    @SerializedName("Semt")
    val semt: String,
    @SerializedName("YolTarifi")
    val yolTarifi: String,
    @SerializedName("Telefon")
    val telefon: String,
    @SerializedName("Telefon2")
    val telefon2: String,
    @SerializedName("Sehir")
    val sehir: String,
    @SerializedName("ilce")
    val ilce: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

fun DataDto.toPharmacy(): Pharmacy {
    return Pharmacy(
        eczaneAdi = eczaneAdi,
        adresi = adresi,
        semt = semt,
        yolTarifi = yolTarifi,
        telefon = telefon,
        telefon2 = telefon2,
        sehir = sehir,
        ilce = ilce,
        latitude = latitude,
        longitude = longitude
        )
}
