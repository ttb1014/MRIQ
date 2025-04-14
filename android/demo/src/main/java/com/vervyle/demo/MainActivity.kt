package com.vervyle.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vervyle.data.repository.StructuredMriDataRepository
import com.vervyle.demo.ui.App
import com.vervyle.demo.ui.rememberAppState
import com.vervyle.design_system.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: StructuredMriDataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()

            Theme {
                App(appState = appState)
            }
        }
    }
}