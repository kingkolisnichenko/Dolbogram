package com.konge.dolbogram.ui.fragments

import androidx.fragment.app.Fragment
import com.konge.dolbogram.MainActivity

open class BaseFragment(private val layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()

        (activity as MainActivity).mAppDrawer.disableDrawer()

    }


    override fun onStop() {
        super.onStop()

        (activity as MainActivity).mAppDrawer.enableDrawer()

    }
}