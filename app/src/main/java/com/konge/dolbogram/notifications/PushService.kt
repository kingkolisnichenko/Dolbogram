package com.konge.dolbogram.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.konge.dolbogram.utilits.*

class PushService : Service() {

    private lateinit var mChildEventListener: ChildEventListener

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        initFirebase()

        mChildEventListener = object :ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                snapshot.ref.limitToLast(1).addListenerForSingleValueEvent(AppValueEventListener{

                    it.children.forEach {message ->
                        var msgModel = message.getCommonModel()
                        println("From -> ${msgModel.from}")
                        println("Msg -> ${msgModel.text}")
                    }
                })

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

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

 }