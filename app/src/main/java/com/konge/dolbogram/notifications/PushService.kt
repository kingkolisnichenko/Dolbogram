package com.konge.dolbogram.notifications

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.konge.dolbogram.R
import com.konge.dolbogram.activities.MainActivity
import com.konge.dolbogram.utilits.*
import java.util.*


class PushService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        println("handleIntent")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        println("From: ${message.from}")

    }
}