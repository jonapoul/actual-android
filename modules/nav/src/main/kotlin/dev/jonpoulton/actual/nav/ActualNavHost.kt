package dev.jonpoulton.actual.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun ActualNavHost() {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = NavDestination.ServerUrl.route,
  ) {
    composable(navController, NavDestination.ServerUrl)
    composable(navController, NavDestination.Login)
    composable(navController, NavDestination.Bootstrap)
    composable(navController, NavDestination.SyncBudget)
  }
}
