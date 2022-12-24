package com.konge.dolbogram.ui.fragments.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.konge.dolbogram.R
import com.konge.dolbogram.databinding.ActivityRegisterBinding
import com.konge.dolbogram.ui.fragments.EnterPhoneNumberFragment
import com.konge.dolbogram.utilits.replaceFragment


class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()

        mToolbar = mBinding.registerToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phote)

        replaceFragment(EnterPhoneNumberFragment())

    }
}