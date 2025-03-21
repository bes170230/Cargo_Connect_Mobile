package com.example.cargoconnect

// ScheduleConfirmationScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ScheduleConfirmationScreen(navController: NavController) {
    val schedule = remember { mutableStateOf<TruckSchedule?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        schedule.value?.let {
            Text("Truck ID: ${it.truckId}")
            Text("Estimated Arrival Time: ${it.estimatedArrivalTime}")
            Text("Dock ID: ${it.dockId}")
            Text("Dock Location: ${it.dockLocation}")
            Text("Source Location: (${it.sourceLatitude}, ${it.sourceLongitude})")
            Text("Destination Location: (${it.destinationLatitude}, ${it.destinationLongitude})")

            it.routeSteps.forEach { step ->
                Text("Step: ${step.step}, Distance: ${step.distance}")
            }
        }
    }
}
