package com.yakupcan.nobetcieczane.domain.use_case

import com.yakupcan.nobetcieczane.domain.use_case.firestore.FirebaseTokenGet
import com.yakupcan.nobetcieczane.domain.use_case.firestore.FirebaseTokenSave
import com.yakupcan.nobetcieczane.domain.use_case.firestore.SendPushAllDevicesUseCase

data class UseCases(
    val tokenSave : FirebaseTokenSave,
    val getToken : FirebaseTokenGet,
    val sendPushToAllDevicesUseCase : SendPushAllDevicesUseCase
)
