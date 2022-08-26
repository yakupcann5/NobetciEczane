package com.android.nobetcieczane.data.repository

import android.content.SharedPreferences
import com.android.nobetcieczane.common.Constants
import com.android.nobetcieczane.data.Service
import com.android.nobetcieczane.data.model.DataDto
import com.android.nobetcieczane.data.model.PharmacyResponse
import com.android.nobetcieczane.domain.repository.PharmacyRepository
import com.android.nobetcieczane.util.MyPreferences
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: Service,
) : PharmacyRepository {

    override suspend fun getPharmacy(
        city: String?,
        town: String?,
        apikey: String?
    ): ArrayList<DataDto> {
        return api.getPharmacyOnDuty(city,town,apikey).data
    }
}