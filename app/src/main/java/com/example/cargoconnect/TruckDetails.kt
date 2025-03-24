package com.example.cargoconnect

data class TruckDetails(
    val truckId: String,
    val sourceLatitude: Double,
    val sourceLongitude: Double,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
    val eta: String,
    val routeSteps: List<String>,
    val trafficStatus: String,
    val dockId: String,
    val dockLocation: String
)

data class RouteGuidance(
    val truckId: String,
    val recommendedSpeed: Int,
    val message: String,
    val signalPrediction: SignalPrediction,
    val alerts: List<Alert>
)

data class SignalPrediction(
    val trafficLightState: String,
    val remainingTime: Int
)

data class Alert(
    val type: String,
    val message: String,
    val timestamp: String
)

data class TruckLocation(
    val truckId: String,
    val priority: Int,
    val latitude: Double,
    val longitude: Double,
    val speed: Int,
    val heading: Int,
    val timestamp: Long
)
