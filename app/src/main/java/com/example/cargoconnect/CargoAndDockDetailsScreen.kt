package com.example.cargoconnect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

data class CargoDetails(
    val cargoId: String,
    val cargoType: String,
    val isReadyForLoading: Boolean
)

data class DockDetails(
    val dockId: String,
    val dockLocation: String,
    val isDockAvailable: Boolean
)

@Composable
fun CargoAndDockDetailsScreen(
    message: String,
    latitude: Double,
    longitude: Double,
    cargoDetails: CargoDetails,
    dockDetails: DockDetails
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Assignment Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Alert notification
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // Cargo Details Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Cargo Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                DetailRow("Cargo ID", cargoDetails.cargoId)
                DetailRow("Cargo Type", cargoDetails.cargoType)
                DetailRow("Ready for Loading", if (cargoDetails.isReadyForLoading) "Yes" else "No",
                    valueColor = if (cargoDetails.isReadyForLoading) Color.Green else Color.Red)
            }
        }

        // Dock Assignment Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Dock Assignment",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                DetailRow("Dock ID", dockDetails.dockId)
                DetailRow("Dock Location", dockDetails.dockLocation)
                DetailRow("Dock Available", if (dockDetails.isDockAvailable) "Yes" else "No",
                    valueColor = if (dockDetails.isDockAvailable) Color.Green else Color.Red)
            }
        }

        Text(
            text = "Destination Coordinates",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "($latitude, $longitude)",
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                val uri = "google.navigation:q=$latitude,$longitude".toUri()
                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage("com.google.android.apps.maps")
                }
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback if Google Maps not installed
                    val browserUri = "https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude".toUri()
                    val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
                    context.startActivity(browserIntent)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Navigate to Destination")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, color = valueColor, fontWeight = FontWeight.Medium)
    }
}
