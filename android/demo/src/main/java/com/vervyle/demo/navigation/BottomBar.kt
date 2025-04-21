package com.vervyle.demo.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun BottomBar(
    destinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
) {
    NavigationBar(
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                modifier = modifier,
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    if (selected) {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = null
                        )
                    }
                },
                enabled = destination.enabled,
                label = { Text(stringResource(destination.iconTextId)) },
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false