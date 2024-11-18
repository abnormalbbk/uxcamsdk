package com.bibek.uxcamsdk

import com.bibek.coresdk.FirebaseProvider
import com.bibek.coresdk.UxCamApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MyApplication : UxCamApplication(), FirebaseProvider {
    override lateinit var firestore: FirebaseFirestore

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        firestore = FirebaseFirestore.getInstance()

        super.onCreate()
    }
}