package com.example.soundcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.soundcraft.navigation.AuthNavigationGraph
import com.example.soundcraft.ui.theme.SoundCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoundCraftTheme {
                val navController = rememberNavController()
                AuthNavigationGraph(navController)
            }
        }
    }
}
