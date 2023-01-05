package com.konge.dolbogram.ui.fragments

import androidx.fragment.app.Fragment
import com.konge.dolbogram.MainActivity
import com.konge.dolbogram.R
import kotlinx.android.synthetic.main.fragment_change_user_name.*

open class BaseChangeFragment(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()

        (activity as MainActivity).mAppDrawer.disableDrawer()

        settings_confirm_change_data.setOnClickListener { change() }

    }

    override fun onStop() {
        super.onStop()
    }

    open fun change() {

    }

}