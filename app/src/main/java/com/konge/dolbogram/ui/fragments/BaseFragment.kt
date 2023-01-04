package com.konge.dolbogram.ui.fragments

import androidx.fragment.app.Fragment
import com.konge.dolbogram.MainActivity
import com.konge.dolbogram.ui.fragments.activities.RegisterActivity

open class BaseFragment(private val layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        try {
            (activity as MainActivity).mAppDrawer.disableDrawer()
        }catch (e:Exception){

        }


    }

    override fun onStop() {
        super.onStop()
        try {
            (activity as MainActivity).mAppDrawer.enableDrawer()
        }catch (e:Exception){

        }


    }
}