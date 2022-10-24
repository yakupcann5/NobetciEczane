package com.yakupcan.nobetcieczane.data.repository

import com.google.firebase.firestore.CollectionReference
import com.yakupcan.nobetcieczane.common.RequestState
import com.yakupcan.nobetcieczane.data.PushService
import com.yakupcan.nobetcieczane.data.model.PushResponseDTO
import com.yakupcan.nobetcieczane.domain.model.PushModel
import com.yakupcan.nobetcieczane.domain.repository.FirebaseFirestoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class FirestoreRepositoryImpl @Inject constructor(
    private val tokensRef : CollectionReference,
    private val api : PushService
/*
    private val tokensQuery : Query
*/
) : FirebaseFirestoreRepository {
/*    override fun getTokenFromFirestore(id : String) = callbackFlow {
        val snapshotListener = tokensQuery.addSnapshotListener { snapshot, error ->
            val response = if (snapshot != null) {
                val token = snapshot.documents
                RequestState.Success(token)
            } else {
                error?.cause?.let { RequestState.Error(it) }
            }
            trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }*/

    override suspend fun saveTokenToFirestore(id : String, token : String) = flow {
        try {
            emit(RequestState.Loading())
            val tokenMap = hashMapOf("token" to token)
            val addition = tokensRef.document(id).set(tokenMap).await()
            emit(RequestState.Success(addition))
        } catch (e : Exception) {
            emit(RequestState.Error(e))
        }
    }

    override suspend fun pushToDevices(pushModel : PushModel, serverKey : String) : PushResponseDTO {
        return api.sendAllDevices(pushModel, serverKey = "key=$serverKey")
    }
}