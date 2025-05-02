package com.vervyle.demo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.vervyle.demo.navigation.BottomBar
import com.vervyle.demo.navigation.NavigationHost
import com.vervyle.demo.navigation.TopLevelDestination
import com.vervyle.ui.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            BottomBar(
                destinations = TopLevelDestination.entries,
                currentDestination = appState.currentDestination,
                modifier = Modifier,
                onNavigateToDestination = appState::navigateToTopLevelDestination
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    ),
                ),
        ) {
            Column(Modifier.fillMaxSize()) {
                val destination = appState.currentTopLevelDestination
                destination?.let {
                    TopAppBar(
                        destination.name,
                        { },
                        { }
                    )
                }
                NavigationHost(
                    appState = appState,
                    navHostController = appState.navHostController,
                    onShowSnackBar = { _, _ -> },
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App(
        AppState(
            rememberCoroutineScope(),
            rememberNavController()
        )
    )
}