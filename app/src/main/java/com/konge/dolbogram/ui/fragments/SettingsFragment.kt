package com.konge.dolbogram.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.konge.dolbogram.R
import com.konge.dolbogram.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()

        setHasOptionsMenu(true)

        APP_ACTIVITY.title = getString(R.string.app_title_settings)

        initFields()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AppStates.updadeState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }

        }

        return true
    }

    private fun initFields() {

        settings_bio.text = USER.bio
        settings_full_name.text = USER.fullname
        settings_phone.text = USER.phone
        settings_status.text = USER.state
        settings_user_name.text = USER.username

        settings_user_photo.downloadAndSetImage(USER.photoUrl)

        settings_change_user_name.setOnClickListener { replaceFragment(ChangeUserNameFragment()) }
        settings_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settings_full_name.setOnClickListener { replaceFragment(ChangeNameFragment()) }
        settings_user_photo.setOnClickListener { changeUserPhoto() }

    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK
            && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri

            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE).child(UUID)

            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDataBase(it) {
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }

        }
    }


}