package com.yakupcan.nobetcieczane.domain.repository

import com.yakupcan.nobetcieczane.data.model.DataDto
import com.yakupcan.nobetcieczane.data.model.PharmacyResponse

interface PharmacyRepository {
    suspend fun getPharmacy(city: String?, town: String?, apikey: String?): ArrayList<DataDto>
}