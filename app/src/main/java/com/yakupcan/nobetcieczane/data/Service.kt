package com.yakupcan.nobetcieczane.data

import com.yakupcan.nobetcieczane.data.model.PharmacyResponse
import com.yakupcan.nobetcieczane.data.model.PushResponseDTO
import com.yakupcan.nobetcieczane.domain.model.PushModel
import retrofit2.http.*

interface Service {
    @GET("pharmacyLink")
    suspend fun getPharmacyOnDuty(
        @Query("city") city : String?,
        @Query("county") county : String?,
        @Query("apikey") apikey : String?
    ) : PharmacyResponse
}

interface PushService {
    @POST("fcm/send")
    suspend fun sendAllDevices(
        @Body pushModel : PushModel,
        @Header("Authorization") serverKey : String,
    ) : PushResponseDTO
}