package com.example.cargoconnect

// MainActivity.kt
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onesignal.OneSignal
import com.example.cargoconnect.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }

        // Handle push notification data when the app is in the foreground
        OneSignal.setNotificationWillShowInForegroundHandler() { receivedEvent ->
            val message = receivedEvent.notification.body
            // Handle push notification message
        }
    }
}

