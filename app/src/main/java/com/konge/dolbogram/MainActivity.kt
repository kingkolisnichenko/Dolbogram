package com.konge.dolbogram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.konge.dolbogram.databinding.ActivityMainBinding
import com.konge.dolbogram.ui.fragments.ChatsFragment
import com.konge.dolbogram.ui.fragments.activities.RegisterActivity
import com.konge.dolbogram.ui.objects.AppDrawer
import com.konge.dolbogram.utilits.AUTH
import com.konge.dolbogram.utilits.initFirebase
import com.konge.dolbogram.utilits.replaceActivity
import com.konge.dolbogram.utilits.replaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    }

    override fun onStart() {
        super.onStart()
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

    }
}
