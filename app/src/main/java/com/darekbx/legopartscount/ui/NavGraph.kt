package com.darekbx.legopartscount.ui

import androidx.navigation.compose.navigate
import androidx.navigation.NavHostController
import com.darekbx.legopartscount.ui.Destinations.AllTechnicSetsSearch
import com.darekbx.legopartscount.ui.Destinations.PartsList
import com.darekbx.legopartscount.ui.Destinations.DefinedParts
import com.darekbx.legopartscount.ui.Destinations.ViewDefinedParts

object Destinations {
    const val SetSearch = "SetSearch"
    const val PartsList = "PartsList"
    const val DefinedParts = "DefinedParts"
    const val ViewDefinedParts = "ViewDefinedParts"
    const val AllTechnicSetsSearch = "AllTechnicSetsSearch"

    object PartsListArgs {
        const val SetId = "setId"
    }
}

class Actions(navController: NavHostController) {
    val openPartsList: (String) -> Unit = { setId ->
        navController.navigate("$PartsList/$setId") { launchSingleTop = false }
    }
    val openDefinedParts: () -> Unit = {
        navController.navigate(DefinedParts)
    }
    val openViewDefinedParts: () -> Unit = {
        navController.navigate(ViewDefinedParts)
    }
    val openAllTechnicSetsSearch: () -> Unit = {
        navController.navigate(AllTechnicSetsSearch)
    }
    val navigateUp: () -> Unit = {
        navController.popBackStack()
    }
}
