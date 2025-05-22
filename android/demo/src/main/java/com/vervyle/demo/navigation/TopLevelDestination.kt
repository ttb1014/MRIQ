package com.vervyle.demo.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.vervyle.demo.R
import com.vervyle.design_system.components.Icons

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val enabled: Boolean,
    val iconTextId: Int
) {
    CATALOG(
        selectedIcon = Icons.Catalog,
        unselectedIcon = Icons.Catalog,
        enabled = true,
        iconTextId = R.string.catalog
    ),
    QUIZ(
        selectedIcon = Icons.Quiz,
        unselectedIcon = Icons.Quiz,
        enabled = true,
        iconTextId = R.string.quiz
    ),
}