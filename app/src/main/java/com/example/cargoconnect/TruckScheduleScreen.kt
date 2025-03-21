package com.example.cargoconnect

// TruckScheduleScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun TruckScheduleScreen(navController: NavController) {
    var truckId by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf("") }
    var heading by remember { mutableStateOf("") }

    val authManager = AuthManager(LocalContext.current)
    val schedule = remember { mutableStateOf<TruckSchedule?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = truckId, onValueChange = { truckId = it }, label = { Text("Truck ID") })
        TextField(value = latitude, onValueChange = { latitude = it }, label = { Text("Latitude") })
        TextField(value = longitude, onValueChange = { longitude = it }, label = { Text("Longitude") })
        TextField(value = timestamp, onValueChange = { timestamp = it }, label = { Text("Timestamp") })
        TextField(value = speed, onValueChange = { speed = it }, label = { Text("Speed (km/h)") })
        TextField(value = heading, onValueChange = { heading = it }, label = { Text("Heading") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val token = authManager.getAuthToken()
            if (token != null) {
                val response = RetrofitInstance.api.getTruckSchedule("Bearer $token", truckId)
                if (response.isSuccessful) {
                    schedule.value = response.body()
                    navController.navigate("scheduleConfirmation")
                }
            }
        }) {
            Text("Submit")
        }
    }

    schedule.value?.let {
        Text("Source: (${it.sourceLatitude}, ${it.sourceLongitude})")
        Text("Destination: (${it.destinationLatitude}, ${it.destinationLongitude})")
    }
}
