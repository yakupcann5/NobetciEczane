package com.yakupcan.nobetcieczane.domain.repository

import com.yakupcan.nobetcieczane.common.RequestState
import com.yakupcan.nobetcieczane.data.model.PushResponseDTO
import com.yakupcan.nobetcieczane.domain.model.PushModel
import kotlinx.coroutines.flow.Flow

interface FirebaseFirestoreRepository {
/*
    fun getTokenFromFirestore(id : String) : Flow<RequestState<MutableList<DocumentSnapshot>>?>
*/

    suspend fun saveTokenToFirestore(id : String, token : String) : Flow<RequestState<Any>>
    suspend fun pushToDevices(pushModel : PushModel, serverKey : String) : PushResponseDTO
}
