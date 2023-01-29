package com.konge.dolbogram.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.konge.dolbogram.utilits.updateMessageToken

class PushService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        updateMessageToken()

    }

}