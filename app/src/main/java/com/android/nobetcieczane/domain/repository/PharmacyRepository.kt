package com.android.nobetcieczane.domain.repository

import com.android.nobetcieczane.data.model.DataDto
import com.android.nobetcieczane.data.model.PharmacyResponse

interface PharmacyRepository {
    suspend fun getPharmacy(city: String?, town: String?, apikey: String?): ArrayList<DataDto>
}