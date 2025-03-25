/*package com.example.cargoconnect

import android.app.Application
import com.onesignal.OneSignal

class OneSignalHandler(application: Application) {
    init {
        OneSignal.initWithContext(application)
        OneSignal.setAppId("ce077d49-4a63-4cf4-b2d7-fa20fbb415fb")
    }

    fun handleReceivedNotification() {
        OneSignal.setNotificationWillShowInForegroundHandler() { receivedEvent ->
            val message = receivedEvent.notification.body
            // Handle push notification message
        }
    }
}
 */
