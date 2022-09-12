package com.yakupcan.nobetcieczane.data

import com.yakupcan.nobetcieczane.data.model.DataDto
import com.yakupcan.nobetcieczane.data.model.PharmacyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("pharmacyLink")
    suspend fun getPharmacyOnDuty(
        @Query("city") city: String?,
        @Query("county") county: String?,
        @Query("apikey") apikey: String?
    ): PharmacyResponse
}