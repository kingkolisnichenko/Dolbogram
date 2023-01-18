@file:Suppress("DEPRECATION")

package com.konge.dolbogram.ui.fragments

import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.konge.dolbogram.MainActivity
import com.konge.dolbogram.R
import com.konge.dolbogram.ui.fragments.activities.RegisterActivity
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                /*AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uuid = AUTH.currentUser?.uid.toString()

                        val dateMap = mutableMapOf<String, Any>()
                        dateMap[CHILD_ID] = uuid
                        dateMap[CHILD_PHONE] = mPhoneNumber
                        dateMap[CHILD_USERNAME] = uuid

                        REF_DATABASE_ROOT.child(NODE_USERS).child(uuid).updateChildren(dateMap)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    //showToast("Wellcome!!")
                                    //(activity as RegisterActivity).replaceActivity(MainActivity())
                                } else {
                                    showToast(it.exception?.message.toString())
                                }

                            }
                    } else {
                        showToast(task.exception?.message.toString())
                    }
                }*/
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))

            }

        }

        register_btn_next.setOnClickListener { sendCode() }

    }

    private fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
        }

    }

    private fun authUser() {
        mPhoneNumber = register_input_phone_number.text.toString()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            activity as RegisterActivity,
            mCallback
            )

    }
}