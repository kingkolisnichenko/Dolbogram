package com.konge.dolbogram.ui.fragments

import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_user_name.*

class ChangeUserNameFragment : BaseChangeFragment(R.layout.fragment_change_user_name) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
    }

    override fun change() {
        super.change()

        mNewUsername = settings_input_username.text.toString().lowercase()

        if (mNewUsername.isEmpty()) {
            showToast(getString(R.string.settings_toast_username_is_empty))
        } else {

            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername)) {
                        showToast(getString(R.string.settings_toast_username_is_not_unical))
                    } else {
                        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername)
                            .setValue(UUID)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    updateCurrentUser()
                                }
                            }
                    }
                })

        }
    }

    private fun updateCurrentUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UUID).child(CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                showToast(getString(R.string.toast_data_update))
                USER.username = mNewUsername
                fragmentManager?.popBackStack()
            }else{
                showToast(it.exception?.message.toString())
            }
        }



    }
}