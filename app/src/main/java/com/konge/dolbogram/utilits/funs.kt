package com.konge.dolbogram.utilits

import android.content.Intent
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.squareup.picasso.Picasso

fun showToast(msg: String) {
    Toast.makeText(APP_ACTIVITY, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {

    when (addStack) {
        true -> supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.data_container, fragment)
            .commit()
        false -> supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment)
            .commit()
    }

}

fun Fragment.replaceFragment(fragment: Fragment) {

    this.fragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(R.id.data_container, fragment)
        ?.commit()
}

fun ImageView.downloadAndSetImage(url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
}

fun initContacts() {
    if (checkPermissions(READ_CONTACTS)) {
        var arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val fullName = it.getString(
                    it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) ?: 0
                )
                var phone = it.getString(
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) ?: 0
                ).replace(Regex("[\\s,-]"), "")

                if (phone.length == 9) {
                    phone =
                        APP_ACTIVITY.getString(R.string.default_country_phone_code) + phone.substringAfter(
                            "0"
                        )
                } else if (phone.substring(0, 3) == "022") continue

                arrayContacts.add(
                    CommonModel(
                        fullname = fullName,
                        phone = phone
                    )
                )

            }
        }
        cursor?.close()

        if (AUTH.currentUser != null) {
            updatePhonesToDatabase(arrayContacts)
        }

    }

}
