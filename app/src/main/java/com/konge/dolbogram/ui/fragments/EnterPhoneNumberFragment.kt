package com.konge.dolbogram.ui.fragments

import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.replaceFragment
import com.konge.dolbogram.utilits.showToast
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*

class EnterPhoneNumberFragment : BaseFragment(R.layout.fragment_enter_phone_number) {
    override fun onStart() {
        super.onStart()

        register_btn_next.setOnClickListener {
            sendCode()

        }

    }

    @Suppress("DEPRECATION")
    private fun sendCode() {
        if(register_input_phone_number.text.toString().isEmpty()){

             showToast(getString(R.string.register_toast_enter_phone))
            
        }else{

            replaceFragment(EnterCodeFragment())

        }


    }
}