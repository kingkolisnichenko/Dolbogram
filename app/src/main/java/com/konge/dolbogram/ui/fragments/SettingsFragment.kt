package com.konge.dolbogram.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.konge.dolbogram.MainActivity
import com.konge.dolbogram.R
import com.konge.dolbogram.ui.fragments.activities.RegisterActivity
import com.konge.dolbogram.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()

        setHasOptionsMenu(true)

        initFields()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }

            R.id.settings_menu_change_name -> {
                replaceFragment(ChangeNameFragment())
            }
        }

        return true
    }

    private fun initFields() {

        settings_bio.text = USER.bio
        settings_full_name.text = USER.fullname
        settings_phone.text = USER.phone
        settings_status.text = USER.status
        settings_user_name.text = USER.username

        settings_change_user_name.setOnClickListener { replaceFragment(ChangeUserNameFragment()) }
        settings_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settings_change_photo.setOnClickListener { changeUserPhoto() }
    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            .start(APP_ACTIVITY)
    }

}