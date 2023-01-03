package com.konge.dolbogram.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseFragment(R.layout.fragment_change_name) {

    override fun onStart() {
        super.onStart()

        settings_confirm_change.setOnClickListener { changeName() }

        settings_input_name.setText(USER.fullname.split(" ")[0])
        settings_input_surname.setText(USER.fullname.split(" ")[1])

    }

    private fun changeName() {

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
                        fragmentManager?.popBackStack()
                    }
                }

        }

    }


}