package com.konge.dolbogram.notifications

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.messaging.FirebaseMessagingService
import com.konge.dolbogram.R
import com.konge.dolbogram.activities.MainActivity
import com.konge.dolbogram.utilits.*
import java.util.*


class PushService : FirebaseMessagingService() {

 //    private lateinit var mChildEventListener: ChildEventListener
//    private lateinit var mNotificationChannel: NotificationChannel
//    private val CHANNEL_ID = "PUSH_SERVICE"
//    private val NOTIFICATION_ID = 606
    override fun onNewToken(token: String) {
        super.onNewToken(token)



    }
/*
    override fun onCreate() {
        super.onCreate()

        initFirebase()

        mNotificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            mNotificationChannel
        )

        mChildEventListener = object :ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                snapshot.ref.limitToLast(1).addListenerForSingleValueEvent(AppValueEventListener{

                    it.children.forEach {message ->
                        var msgModel = message.getCommonModel()
                        println("From -> ${msgModel.from}")
                        println("Msg -> ${msgModel.text}")

                        REF_DATABASE_ROOT.child(NODE_USERS).child(msgModel.from).addListenerForSingleValueEvent(AppValueEventListener{
                            var user = it.getUserModel()

                            sendNotification(user.fullname, msgModel.text)

                        })

                    }
                })

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UUID).addChildEventListener(mChildEventListener)

        println("Service started")

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UUID).removeEventListener(mChildEventListener)
        println("Service stopped")
    }

    fun sendNotification(from: String, msg: String){

        val messagingStyle = NotificationCompat.MessagingStyle(from)
            .addMessage(msg, Date().time, from)

        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_push_notifacation)
            .setStyle(messagingStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pIntent)
            .setAutoCancel(true)


        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())
        }
    }
*/
}