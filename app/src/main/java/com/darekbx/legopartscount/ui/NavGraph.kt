package com.darekbx.legopartscount.ui

import androidx.navigation.compose.navigate
import androidx.navigation.NavHostController
import com.darekbx.legopartscount.ui.Destinations.PartsList
import com.darekbx.legopartscount.ui.Destinations.DefinedParts

object Destinations {
    const val SetSearch = "SetSearch"
    const val PartsList = "PartsList"
    const val DefinedParts = "DefinedParts"

    object PartsListArgs {
        const val SetId = "setId"
    }
}

class Actions(navController: NavHostController) {
    val openPartsList: (String) -> Unit = { setId ->
        navController.navigate("$PartsList/$setId") { launchSingleTop= true }
    }
    val openDefinedParts: () -> Unit = {
        navController.navigate(DefinedParts)
    }
    val navigateUp: () -> Unit = {
        navController.popBackStack()
    }
}
