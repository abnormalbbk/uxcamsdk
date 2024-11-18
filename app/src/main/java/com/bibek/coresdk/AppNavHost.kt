package com.bibek.coresdk

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bibek.coresdk.feature.dashboard.DashboardPage
import com.bibek.coresdk.feature.login.LoginPage

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(
                navigateToDashboard = {
                    navController.navigate(
                        route = "dashboard"
                    )
                }
            )
        }
        composable("dashboard") { DashboardPage() }
    }
}