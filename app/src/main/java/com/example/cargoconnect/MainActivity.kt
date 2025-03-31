package com.example.cargoconnect

import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.Alignment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cargoconnect.ui.theme.CargoConnectTheme
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ONESIGNAL_APP_ID = "ce7d49-4a63-4cf4-b2d7-fa20fbb415fb"

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

                // Get cargo details from notification (if available)
                val cargoId = additionalData?.optString("cargoId") ?: "Unknown"
                val cargoType = additionalData?.optString("cargoType") ?: "Standard"
                val isReadyForLoading = additionalData?.optBoolean("isReadyForLoading") ?: false

                // Get dock details from notification (if available)
                val dockId = additionalData?.optString("dockId") ?: "Unknown"
                val dockLocation = additionalData?.optString("dockLocation") ?: "Main Terminal"
                val isDockAvailable = additionalData?.optBoolean("isDockAvailable") ?: true

                Log.d("OneSignal", "Notification tapped with data: $msg at ($lat, $lon)")

                // Store in intent so it's available after activity recreate
                val restartIntent = Intent(this@MainActivity, MainActivity::class.java).apply {
                    putExtra("message", msg)
                    putExtra("latitude", lat)
                    putExtra("longitude", lon)

                    // Add cargo details to intent
                    putExtra("cargoId", cargoId)
                    putExtra("cargoType", cargoType)
                    putExtra("isReadyForLoading", isReadyForLoading)

                    // Add dock details to intent
                    putExtra("dockId", dockId)
                    putExtra("dockLocation", dockLocation)
                    putExtra("isDockAvailable", isDockAvailable)

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

                if (message != null) {
                    // Extract cargo details from intent
                    val cargoDetails = CargoDetails(
                        cargoId = intent.getStringExtra("cargoId") ?: "Unknown",
                        cargoType = intent.getStringExtra("cargoType") ?: "Standard",
                        isReadyForLoading = intent.getBooleanExtra("isReadyForLoading", false)
                    )

                    // Extract dock details from intent
                    val dockDetails = DockDetails(
                        dockId = intent.getStringExtra("dockId") ?: "Unknown",
                        dockLocation = intent.getStringExtra("dockLocation") ?: "Main Terminal",
                        isDockAvailable = intent.getBooleanExtra("isDockAvailable", true)
                    )

                    CargoAndDockDetailsScreen(
                        message = message,
                        latitude = lat,
                        longitude = lon,
                        cargoDetails = cargoDetails,
                        dockDetails = dockDetails
                    )
                } else {
                    DefaultHomeScreen()
                }
            }
        }
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
