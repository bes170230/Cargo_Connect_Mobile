package com.example.cargoconnect

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
//import com.google.maps.android.compose.MapView
import com.google.maps.android.compose.rememberCameraPositionState
// import com.google.maps.android.compose.CameraPosition

@Composable
fun TruckRouteMapView(latitude: Double, longitude: Double) {
    // Initialize the camera position to center on the truck's location
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 14f)
    }

    // Render the map with a marker at the truck's location
    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            // position = LatLng(latitude, longitude),
            title = "Truck Location"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTruckRouteMapView() {
    TruckRouteMapView(latitude = 37.7749, longitude = -122.4194) // Example: San Francisco coordinates
}
