package com.bibek.coresdk

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MyApplication : UxCamApplication(), FirebaseProvider {
    override lateinit var firestore: FirebaseFirestore

    override fun onCreate() {
        Log.d("XXXXX", "onCreate: MyApplication 1")
        FirebaseApp.initializeApp(this)
        firestore = FirebaseFirestore.getInstance()

        super.onCreate()

        Log.d("XXXXX", "onCreate: MyApplication 2")
    }
}