package com.darekbx.legopartscount.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.darekbx.legopartscount.ui.Destinations.PartsList
import com.darekbx.legopartscount.ui.Destinations.PartsListArgs.SetId
import com.darekbx.legopartscount.ui.Destinations.SetSearch
import com.darekbx.legopartscount.ui.Destinations.DefinedParts
import com.darekbx.legopartscount.ui.Destinations.ViewDefinedParts
import com.darekbx.legopartscount.ui.definedparts.DefinedPartsScreen
import com.darekbx.legopartscount.ui.viewdefinedparts.ViewDefinedPartsScreen
import com.darekbx.legopartscount.viewmodel.DefinedPartsViewModel
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel

@Composable
fun NavigationApp(rebrickableViewModel: RebrickableViewModel, definedPartsViewModel: DefinedPartsViewModel) {
    val navigationController = rememberNavController()
    val actions = remember(navigationController) { Actions(navigationController) }
    NavHost(
        navController = navigationController,
        startDestination = SetSearch
    ) {
        composable(SetSearch) {
            SetSearchScreen(
                rebrickableViewModel,
                actions.openPartsList,
                actions.openDefinedParts,
                actions.openViewDefinedParts
            )
        }
        composable(
            "$PartsList/{$SetId}",
            arguments = listOf(
                navArgument(SetId) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            PartsListScreen(
                navBackStackEntry.arguments?.getString(SetId)!!,
                rebrickableViewModel,
                actions.navigateUp
            )
        }
        composable(DefinedParts) {
            DefinedPartsScreen(rebrickableViewModel, definedPartsViewModel, actions.navigateUp)
        }
        composable(ViewDefinedParts) {
            ViewDefinedPartsScreen(definedPartsViewModel)
        }
    }
}
