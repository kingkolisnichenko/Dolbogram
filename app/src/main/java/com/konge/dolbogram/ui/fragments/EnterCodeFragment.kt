package com.konge.dolbogram.ui.fragments

import com.google.firebase.auth.PhoneAuthProvider
import com.konge.dolbogram.MainActivity
import com.konge.dolbogram.R
import com.konge.dolbogram.ui.fragments.activities.RegisterActivity
import com.konge.dolbogram.utilits.AUTH
import com.konge.dolbogram.utilits.AppTextWatcher
import com.konge.dolbogram.utilits.replaceActivity
import com.konge.dolbogram.utilits.showToast
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(val phoneNumber: String, val id: String) :
    BaseFragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()

        (activity as RegisterActivity).title = phoneNumber

        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                enterCode()
            }

        })

    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)

        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("Wellcome!!")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            } else {
                showToast(task.exception?.message.toString())
            }
        }
    }

}