package com.example.cargoconnect

// AppNavigation.kt
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("truckSchedule") { TruckScheduleScreen(navController) }
        composable("scheduleConfirmation") { ScheduleConfirmationScreen(navController) }
    }
}
