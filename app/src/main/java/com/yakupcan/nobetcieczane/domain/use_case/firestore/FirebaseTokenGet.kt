package com.yakupcan.nobetcieczane.domain.use_case.firestore

import com.yakupcan.nobetcieczane.domain.repository.FirebaseFirestoreRepository
import javax.inject.Inject

class FirebaseTokenGet @Inject constructor(private val repo : FirebaseFirestoreRepository) {
/*
    operator fun invoke(id : String) = repo.getTokenFromFirestore(id)
*/
}