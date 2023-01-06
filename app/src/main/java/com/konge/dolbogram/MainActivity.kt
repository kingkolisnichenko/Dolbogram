package com.konge.dolbogram

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.konge.dolbogram.databinding.ActivityMainBinding
import com.konge.dolbogram.models.User
import com.konge.dolbogram.ui.fragments.ChatsFragment
import com.konge.dolbogram.ui.fragments.activities.RegisterActivity
import com.konge.dolbogram.ui.objects.AppDrawer
import com.konge.dolbogram.utilits.*
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        APP_ACTIVITY = this

        initFields()
        initFunc()

    }


    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()

            replaceFragment(ChatsFragment(), false)

        } else {
            replaceActivity(RegisterActivity())
        }

    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)

        initFirebase()
        initUser()

    }

    private fun initUser() {

        REF_DATABASE_ROOT.child(NODE_USERS).child(UUID)
            .addListenerForSingleValueEvent(AppValueEventListener {

                USER = it.getValue(User::class.java) ?: User()
            })

    }

}
