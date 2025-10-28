package com.example.unitrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.unitrack.navigation.AppNavigation
import com.example.unitrack.ui.theme.UniTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniTrackTheme {
                // AppNavigation will handle all screen logic
                AppNavigation()
            }
        }
    }
}
