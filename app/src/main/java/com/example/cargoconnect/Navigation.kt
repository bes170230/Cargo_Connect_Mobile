package com.example.cargoconnect

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cargoconnect.MainScreen
import com.example.cargoconnect.TruckDetailsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") { MainScreen(navController) }
        composable("truckDetails") { TruckDetailsScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Navigation()
}
