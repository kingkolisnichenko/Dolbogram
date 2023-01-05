package com.konge.dolbogram.utilits

import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.konge.dolbogram.models.User

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User
lateinit var UUID:String

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()

    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference

    USER = User()
    UUID = AUTH.currentUser?.uid.toString()


}