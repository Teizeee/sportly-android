package com.simple.sportly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.simple.sportly.core.di.DefaultAppContainer
import com.simple.sportly.ui.navigation.SportlyApp
import com.simple.sportly.ui.theme.SportlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = DefaultAppContainer(applicationContext)

        enableEdgeToEdge()
        setContent {
            SportlyTheme {
                SportlyApp(appContainer = appContainer)
            }
        }
    }
}
