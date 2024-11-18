package com.bibek.uxcamsdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.bibek.coresdk.core.UxCam
import com.bibek.uxcamsdk.ui.theme.UxCamSdkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        UxCam.setIdentifier("com.bibek.uxcam")
            .setName("Bibek Maharjan")
            .setEmail("abnormal.bbk@gmail.com")
            .build(this)
        CoroutineScope(Dispatchers.IO).launch {
            UxCam.startSession()
        }
        setContent {
            UxCamSdkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()
                        AppNavHost(navController)
                    }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.IO).launch {
            UxCam.endSession()
        }
    }
}