package com.android.nobetcieczane.domain.use_case.getPharmcy

import com.android.nobetcieczane.common.Constants
import com.android.nobetcieczane.domain.repository.PharmacyRepository
import com.android.nobetcieczane.common.RequestState
import com.android.nobetcieczane.util.MyPreferences
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