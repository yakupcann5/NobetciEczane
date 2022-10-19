package com.yakupcan.nobetcieczane.domain.use_case.firestore

import com.yakupcan.nobetcieczane.domain.repository.FirebaseFirestoreRepository
import javax.inject.Inject

class FirebaseTokenSave @Inject constructor(private val repo : FirebaseFirestoreRepository) {
    suspend operator fun invoke(id : String, token : String) = repo.saveTokenToFirestore(id, token)
}