package com.example.cargoconnect

// MainActivity.kt
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.net.toUri


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        // OneSignal Initialization
        OneSignal.initWithContext(this, "ce077d49-4a63-4cf4-b2d7-fa20fbb415fb")
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(false)
        }
        setContent {
            Navigation()
        }

        val openEventHandler = object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                val payload = event.notification.additionalData


                val message = payload?.optString("message")
                val latitude = payload?.optString("latitude")
                val longitude = payload?.optString("longitude")

                Log.d("OneSignal", "CargoApp Msg: $message")
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "http://maps.google.com/maps?saddr=32.9857,96.7502&daddr=$latitude,$longitude".toUri()
                )
                startActivity(intent)
                TODO("Not yet implemented")
            }


            // Handle push notification message
        }

        // Handle push notification data when the app is in the foreground
        OneSignal.Notifications.addClickListener(openEventHandler)
    }
}

