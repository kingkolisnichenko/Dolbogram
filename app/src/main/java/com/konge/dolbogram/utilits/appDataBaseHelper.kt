package com.konge.dolbogram.utilits

import android.net.Uri
import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.models.User

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User
lateinit var UUID: String

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()

    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference

    USER = User()
    UUID = AUTH.currentUser?.uid.toString()

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

                if (phone.length == 9){
                    phone = APP_ACTIVITY.getString(R.string.default_country_phone_code) + phone.substringAfter("0")
                }else if(phone.substring(0,3) == "022") continue

                arrayContacts.add(
                    CommonModel(
                        fullname = fullName,
                        phone = phone
                    )
                )

            }
        }
        cursor?.close()

        updatePhonesToDatabase(arrayContacts)

    }

}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
        it.children.forEach { snapshot ->

            arrayContacts.forEach { contact ->
                if (contact.phone == snapshot.key.toString()) {
                    REF_DATABASE_ROOT
                        .child(NODE_PHONES_CONTACTS)
                        .child(UUID)
                        .child(snapshot.value.toString())
                        .child(CHILD_ID)
                        .setValue(snapshot.value.toString())
                        .addOnFailureListener { showToast(it.message.toString()) }
                }
            }

        }
    })
}

// Заполнить ссылку на изображение в базе данных
inline fun putUrlToDataBase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UUID).child(CHILD_PHOTO_URL)
        .setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

// Получить ссылку на изображение из хранилища
inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

// Отправить изображение в хранилище
inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {

    REF_DATABASE_ROOT.child(NODE_USERS).child(UUID)
        .addListenerForSingleValueEvent(AppValueEventListener {

            USER = it.getValue(User::class.java) ?: User()

            if (USER.username.isEmpty()) {
                USER.username = USER.id
            }

            function()
        })

}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()
