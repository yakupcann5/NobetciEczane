package com.yakupcan.nobetcieczane.domain.use_case.firestore

import android.util.Log
import com.yakupcan.nobetcieczane.common.RequestState
import com.yakupcan.nobetcieczane.domain.model.PushModel
import com.yakupcan.nobetcieczane.domain.repository.FirebaseFirestoreRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendPushAllDevicesUseCase @Inject constructor(private var repository : FirebaseFirestoreRepository) {
    operator fun invoke(pushModel : PushModel, serverKey : String) = flow {
        try {
            emit(RequestState.Loading())
            val pushResponse = repository.pushToAllDevices(pushModel, serverKey)
            emit(RequestState.Success(pushResponse))
        } catch (e : Exception) {
            Log.e("Push Error", e.message.toString())
            emit(RequestState.Error(e))
        }
    }
}
