package com.konge.dolbogram.utilits

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.konge.dolbogram.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_settings.*

fun showToast(msg: String) {
    Toast.makeText(APP_ACTIVITY, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {

    when(addStack){
        true -> supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.dataContainer, fragment)
            .commit()
        false -> supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer, fragment)
            .commit()
    }


}

fun Fragment.replaceFragment(fragment: Fragment) {

    this.fragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(R.id.dataContainer, fragment)
        ?.commit()
}

fun CircleImageView.downloadAndSetImage(url: String){
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.default_photo)
        .into(this)
}
