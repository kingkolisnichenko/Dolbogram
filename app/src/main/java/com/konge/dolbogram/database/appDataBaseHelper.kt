package com.konge.dolbogram.utilits

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.models.UserModel

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference

lateinit var USER: UserModel
lateinit var UUID: String

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val TYPE_TEXT = "text"

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"
const val NODE_MESSAGES = "messages"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timeStamp"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()

    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference

    USER = UserModel()
    UUID = AUTH.currentUser?.uid.toString()

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

                    REF_DATABASE_ROOT
                        .child(NODE_PHONES_CONTACTS)
                        .child(UUID)
                        .child(snapshot.value.toString())
                        .child(CHILD_FULLNAME)
                        .setValue(contact.fullname)
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

            USER = it.getValue(UserModel::class.java) ?: UserModel()

            if (USER.username.isEmpty()) {
                USER.username = USER.id
            }

            function()
        })

}

fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UUID).child(CHILD_BIO)
        .setValue(newBio)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))

            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun setFullNameToDatabase(newFullName: String) {
    REF_DATABASE_ROOT
        .child(NODE_USERS)
        .child(UUID)
        .child(CHILD_FULLNAME)
        .setValue(newFullName)
        .addOnSuccessListener {

            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.fullname = newFullName
            APP_ACTIVITY.mAppDrawer.updateHeader()
            APP_ACTIVITY.supportFragmentManager.popBackStack()

        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun setUserNameToDatabase(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(newUsername)
        .setValue(UUID)
        .addOnSuccessListener {
            updateCurrentUser(newUsername)
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

private fun updateCurrentUser(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UUID).child(CHILD_USERNAME)
        .setValue(newUsername)
        .addOnSuccessListener {
            deleteOldUsername(newUsername)
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

private fun deleteOldUsername(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.username = newUsername
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun sendMessage(message: String, receivingUserID: String, typeText: String, function: () -> Unit) {

    val refDialogUser = "$NODE_MESSAGES/$UUID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$UUID"
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = UUID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialogs = hashMapOf<String, Any>()
    mapDialogs["$refDialogUser/$messageKey"] = mapMessage
    mapDialogs["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT.updateChildren(mapDialogs)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()


