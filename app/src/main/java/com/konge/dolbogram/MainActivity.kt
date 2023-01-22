package com.konge.dolbogram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.konge.dolbogram.databinding.ActivityMainBinding
import com.konge.dolbogram.ui.fragments.ChatsFragment
import com.konge.dolbogram.activities.RegisterActivity
import com.konge.dolbogram.ui.objects.AppDrawer
import com.konge.dolbogram.utilits.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        APP_ACTIVITY = this

        initFirebase()
        initUser {

            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }

            initFields()
            initFunc()
        }

    }

    override fun onStart() {
        super.onStart()
        AppStates.updadeState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updadeState(AppStates.OFFLINE)
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
        mAppDrawer = AppDrawer()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
    }
}
