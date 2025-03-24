package com.example.cargoconnect

import android.app.Application
import com.onesignal.OneSignal

class OneSignalHandler(application: Application) {
    init {
        OneSignal.initWithContext(application)
        OneSignal.setAppId("YOUR_ONESIGNAL_APP_ID")
    }

    fun handleReceivedNotification() {
        OneSignal.setNotificationReceivedHandler { receivedEvent ->
            val message = receivedEvent.notification.body
            // Handle push notification message
        }
    }
}
