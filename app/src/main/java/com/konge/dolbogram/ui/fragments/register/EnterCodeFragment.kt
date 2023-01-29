package com.konge.dolbogram.ui.fragments.register

import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY.title = phoneNumber

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

                val uuid = AUTH.currentUser?.uid.toString()

                REF_DATABASE_ROOT.child(NODE_USERS)
                    .addListenerForSingleValueEvent(AppValueEventListener {
                        if (it.hasChild(uuid)) {
                            REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uuid)
                                .addOnFailureListener { showToast(it.message.toString()) }
                                .addOnSuccessListener {
                                    restartActivity()
                                }

                        } else {
                            val dateMap = mutableMapOf<String, Any>()
                            dateMap[CHILD_ID] = uuid
                            dateMap[CHILD_PHONE] = phoneNumber
                            dateMap[CHILD_USERNAME] = uuid

                            REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uuid)
                                .addOnFailureListener { showToast(it.message.toString()) }
                                .addOnSuccessListener {
                                    REF_DATABASE_ROOT.child(NODE_USERS).child(uuid)
                                        .updateChildren(dateMap)
                                        .addOnSuccessListener {
                                            restartActivity()
                                        }
                                        .addOnFailureListener { showToast(it.message.toString()) }
                                }
                        }
                    })

            } else {
                showToast(task.exception?.message.toString())
            }
        }
    }

}