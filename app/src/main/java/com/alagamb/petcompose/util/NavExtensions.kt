package com.alagamb.petcompose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

/**
 * Returns a Hilt ViewModel scoped to a parent navigation graph.
 * All destinations within [graphRoute] share the exact same ViewModel instance.
 */
@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
    graphRoute: String
): VM {
    val parentEntry = remember(this) {
        navController.getBackStackEntry(graphRoute)
    }
    return hiltViewModel(parentEntry)
}
