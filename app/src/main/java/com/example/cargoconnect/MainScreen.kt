package com.example.cargoconnect

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {
    Button(onClick = {
        navController.navigate("truckDetails")
    }) {
        Text("Go to Truck Details")
    }
}
