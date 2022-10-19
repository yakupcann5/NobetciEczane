package com.yakupcan.nobetcieczane.util

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GPFirebaseMessagingService :
    FirebaseMessagingService() {
    @Inject
    lateinit var preferences : MyPreferences

    override fun onNewToken(token : String) {
        subscribeToAll()
        savePushToken(token)
    }

    private fun savePushToken(token : String) {
        preferences.setPushToken(token)
    }

    private fun subscribeToAll() {
        Firebase.messaging.subscribeToTopic("all")
            .addOnCompleteListener {}
    }

    override fun onMessageReceived(message : RemoteMessage) {
        super.onMessageReceived(message)
    }
}
