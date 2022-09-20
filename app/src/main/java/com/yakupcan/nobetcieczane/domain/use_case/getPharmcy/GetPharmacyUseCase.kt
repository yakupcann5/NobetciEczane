package com.yakupcan.nobetcieczane.domain.use_case.getPharmcy

import com.yakupcan.nobetcieczane.common.Constants
import com.yakupcan.nobetcieczane.domain.repository.PharmacyRepository
import com.yakupcan.nobetcieczane.common.RequestState
import com.yakupcan.nobetcieczane.util.MyPreferences
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPharmacyUseCase @Inject constructor(private var repository: PharmacyRepository, private val preferences: MyPreferences) {
    operator fun invoke() = flow {
        try {
            emit(RequestState.Loading())
            val pharmacy = repository.getPharmacy(preferences.getCity(),preferences.getTown(),Constants.API_KEY)
            emit(RequestState.Success(pharmacy))
        } catch (e: HttpException) {
            emit(RequestState.Error(e))
        } catch (e: IOException) {
            emit(RequestState.Error(e))
        }
    }
}