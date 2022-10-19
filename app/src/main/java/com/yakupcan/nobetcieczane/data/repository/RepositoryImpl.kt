package com.yakupcan.nobetcieczane.data.repository

import android.content.SharedPreferences
import com.yakupcan.nobetcieczane.common.Constants
import com.yakupcan.nobetcieczane.data.Service
import com.yakupcan.nobetcieczane.data.model.DataDto
import com.yakupcan.nobetcieczane.data.model.PharmacyResponse
import com.yakupcan.nobetcieczane.data.model.PushResponseDTO
import com.yakupcan.nobetcieczane.domain.repository.PharmacyRepository
import com.yakupcan.nobetcieczane.util.MyPreferences
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: Service,
) : PharmacyRepository {

    override suspend fun getPharmacy(
        city: String?,
        town: String?,
        apikey: String?
    ): ArrayList<DataDto> {
        return api.getPharmacyOnDuty(city, town, apikey).data
    }
}