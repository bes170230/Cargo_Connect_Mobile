package com.example.cargoconnect

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

@Composable
fun TruckRouteMapView(latitude: Double, longitude: Double) {
    val mapView = rememberMapViewWithLifecycle()

    mapView.getMapAsync(object : OnMapReadyCallback {
        override fun onMapReady(googleMap: GoogleMap) {
            val truckLocation = LatLng(latitude, longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(truckLocation, 14f))
        }
    })

    AndroidView({ mapView }) { mapView ->
        mapView.getMapAsync { googleMap ->
            // Handle map updates if necessary
        }
    }
}
