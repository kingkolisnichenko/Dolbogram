package com.konge.dolbogram.ui.fragments

import androidx.fragment.app.Fragment
import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.APP_ACTIVITY
import com.konge.dolbogram.utilits.hideKeyboard

class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()

        APP_ACTIVITY.title = getString(R.string.app_title_chats)

        APP_ACTIVITY.mAppDrawer.enableDrawer()

        hideKeyboard()

    }
}