package com.konge.dolbogram.ui.fragments

import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()

        initFullnameList()

    }


    private fun initFullnameList() {
        var fullnamelist = USER.fullname.split(" ")
        if (fullnamelist.size > 1) {
            settings_input_name.setText(fullnamelist[0])
            settings_input_surname.setText(fullnamelist[1])
        } else settings_input_name.setText(fullnamelist[0])
    }

    override fun change() {
        super.change()

        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()

        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"

            REF_DATABASE_ROOT
                .child(NODE_USERS)
                .child(UUID)
                .child(CHILD_FULLNAME)
                .setValue(fullname)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.toast_data_update))
                        USER.fullname = fullname
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        fragmentManager?.popBackStack()
                    }
                }

        }

    }


}