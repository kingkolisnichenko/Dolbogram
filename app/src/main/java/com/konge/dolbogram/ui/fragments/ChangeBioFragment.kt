package com.konge.dolbogram.ui.fragments

import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.USER
import com.konge.dolbogram.utilits.setBioToDatabase
import kotlinx.android.synthetic.main.fragment_change_bio.*

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()

        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()

        setBioToDatabase(settings_input_bio.text.toString())

    }
}