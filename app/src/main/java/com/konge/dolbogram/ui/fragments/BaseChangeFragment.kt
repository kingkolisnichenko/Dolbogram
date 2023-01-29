package com.konge.dolbogram.ui.fragments

import androidx.fragment.app.Fragment
import com.konge.dolbogram.utilits.APP_ACTIVITY
import kotlinx.android.synthetic.main.fragment_change_bio.*

open class BaseChangeFragment(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY.mAppDrawer.disableDrawer()

        settings_confirm_change_data.setOnClickListener { change() }

    }

    override fun onStop() {
        super.onStop()
    }

    open fun change() {

    }

}