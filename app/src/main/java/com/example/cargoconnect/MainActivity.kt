package com.example.cargoconnect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cargoconnect.ui.theme.CargoConnectTheme
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

const val ONESIGNAL_APP_ID = "ce7d49-4a63-4cf4-b2d7-fa20fbb415fb"
//const val SAMPLE_TRUCK_ID = "SAMPLE_TRUCK_ID_4242"

// Data class for cargo information
data class CargoInfo(
    val airportName: String,
    val parkingSpot: String,
    val parkingId: String,
    val priority: String
)

// Object to track maps status
object MapStatusTracker {
    private val _isMapRunning = MutableStateFlow(false)
    val isMapRunning: StateFlow<Boolean> = _isMapRunning

    fun setMapRunning(isRunning: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            _isMapRunning.value = isRunning
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(false)
        }

        val clickListener = object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                val additionalData = event.notification.additionalData
                val msg = additionalData?.optString("message") ?: "No message"
                val lat = additionalData?.optDouble("latitude") ?: 0.0
                val lon = additionalData?.optDouble("longitude") ?: 0.0

                // Extract cargo information from notification
                val airportName = additionalData?.optString("airportName") ?: "DFW Airport"
                val parkingSpot = additionalData?.optString("parkingSpot") ?: "Parking Spot #124"
                val parkingId = additionalData?.optString("parkingId") ?: "Parking ID #32584"
                val priority = additionalData?.optString("priority") ?: "Normal"

                Log.d("OneSignal", "Notification content: $msg at ($lat, $lon)")

                // Store in intent so it's available after activity recreate
                val restartIntent = Intent(this@MainActivity, MainActivity::class.java).apply {
                    putExtra("message", msg)
                    putExtra("latitude", lat)
                    putExtra("longitude", lon)

                    // Add cargo information to intent
                    putExtra("airportName", airportName)
                    putExtra("parkingSpot", parkingSpot)
                    putExtra("parkingId", parkingId)
                    putExtra("priority", priority)

                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(restartIntent)
                finish()
            }
        }

        OneSignal.Notifications.addClickListener(clickListener)

        setContent {
            CargoConnectTheme {
                val message = intent.getStringExtra("message")
                val lat = intent.getDoubleExtra("latitude", 0.0)
                val lon = intent.getDoubleExtra("longitude", 0.0)

                // Extract cargo information from intent
                val cargoInfo = if (message != null) {
                    CargoInfo(
                        airportName = intent.getStringExtra("airportName") ?: "DFW Airport",
                        parkingSpot = intent.getStringExtra("parkingSpot") ?: "Parking Spot #124",
                        parkingId = intent.getStringExtra("parkingId") ?: "Parking ID #32584",
                        priority = intent.getStringExtra("priority") ?: "Normal"
                    )
                } else null

                if (message != null && cargoInfo != null) {
                    NotificationScreen(
                        message = message,
                        latitude = lat,
                        longitude = lon,
                        cargoInfo = cargoInfo
                    )
                } else {
                    DefaultHomeScreen()
                }
            }
        }
    }
}

@Composable
fun NotificationScreen(
    message: String,
    latitude: Double,
    longitude: Double,
    cargoInfo: CargoInfo
) {
    val context = LocalContext.current
    var showMapNotification by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Cargo Information Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Cargo Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CargoInfoRow("Airport", cargoInfo.airportName)
                CargoInfoRow("Parking Spot", cargoInfo.parkingSpot)
                CargoInfoRow("Parking ID", cargoInfo.parkingId)

                // Priority with color coding
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Priority")
                    Text(
                        text = cargoInfo.priority,
                        color = when(cargoInfo.priority.lowercase()) {
                            "high" -> Color.Red
                            "medium" -> Color.Blue
                            else -> Color.Green
                        },
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Text(
            text = "Destination: ($latitude, $longitude)",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = {
            val uri = "google.navigation:q=$latitude,$longitude".toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }

            try {
                context.startActivity(intent)
                // Set map running state to true
                MapStatusTracker.setMapRunning(true)
                showMapNotification = true
            } catch (e: Exception) {
                // Fallback if Google Maps not installed
                val browserUri = "https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude".toUri()
                val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
                context.startActivity(browserIntent)
                MapStatusTracker.setMapRunning(true)
                showMapNotification = true
            }
        }) {
            Text("Navigate to Destination")
        }

        // Notification handler for when Google Maps is running
        if (showMapNotification) {
            LaunchedEffect(key1 = true) {
                // Simulate a delay - in a real app, you'd use a service to detect when Maps is actually running
                delay(5000)
                // After some time, set map state to false (simulating app returning to foreground)
                MapStatusTracker.setMapRunning(false)
                showMapNotification = false
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Navigation Active",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Delivering to: ${cargoInfo.airportName} (${cargoInfo.parkingSpot})",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Cargo: ${cargoInfo.parkingId} - ${cargoInfo.priority} Priority",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CargoInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(
            text = value,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DefaultHomeScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Welcome to Cargo App")
    }
}