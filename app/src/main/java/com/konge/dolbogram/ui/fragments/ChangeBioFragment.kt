package com.konge.dolbogram.ui.fragments

import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_bio.*
import kotlinx.android.synthetic.main.fragment_change_user_name.*
import kotlin.concurrent.fixedRateTimer

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()

        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()

        REF_DATABASE_ROOT.child(NODE_USERS).child(UUID).child(CHILD_BIO)
            .setValue(settings_input_bio.text.toString())
            .addOnCompleteListener {
            if (it.isSuccessful){
                showToast(getString(R.string.toast_data_update))

                USER.bio = settings_input_bio.text.toString()
                fragmentManager?.popBackStack()
            }
        }

    }
}