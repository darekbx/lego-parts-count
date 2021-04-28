package com.darekbx.legopartscount.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.darekbx.legopartscount.ui.Destinations.PartsList
import com.darekbx.legopartscount.ui.Destinations.PartsListArgs.SetId
import com.darekbx.legopartscount.ui.Destinations.SetSearch
import com.darekbx.legopartscount.ui.Destinations.DefinedParts
import com.darekbx.legopartscount.ui.definedparts.DefinedPartsScreen
import com.darekbx.legopartscount.viewmodel.MainViewModel

@Composable
fun NavigationApp(mainViewModel: MainViewModel) {
    val navigationController = rememberNavController()
    val actions = remember(navigationController) { Actions(navigationController) }
    NavHost(navController = navigationController, startDestination = SetSearch) {
        composable(SetSearch) {
            SetSearchScreen(mainViewModel, actions.openPartsList, actions.openDefinedParts)
        }
        composable(
            "$PartsList/{$SetId}",
            arguments = listOf(
                navArgument(SetId) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            PartsListScreen(
                navBackStackEntry.arguments?.getString(SetId)!!,
                mainViewModel,
                actions.navigateUp
            )
        }
        composable(DefinedParts) {
            DefinedPartsScreen(mainViewModel, actions.navigateUp)
        }
    }
}
