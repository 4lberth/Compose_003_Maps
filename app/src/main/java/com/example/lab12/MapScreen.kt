package com.example.lab12

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    val ArequipaLocation = LatLng(-16.4040102, -71.559611)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ArequipaLocation, 12f)
    }

    val tiposMapas = listOf("Normal", "Satélite", "Híbrido", "Terreno", "Ninguno")
    var selectedMapType by remember { mutableStateOf(0) }
    var showDropdown by remember { mutableStateOf(false) }
    var showBsAiresMarker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { showDropdown = true },
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Text(
                text = tiposMapas[selectedMapType],
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false }
        ) {
            tiposMapas.forEachIndexed { index, tipo ->
                DropdownMenuItem(
                    onClick = {
                        selectedMapType = index
                        showDropdown = false
                    },
                    text = { Text(text = tipo) }
                )
            }
        }

        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = when (selectedMapType) {
                    0 -> MapType.NORMAL
                    1 -> MapType.SATELLITE
                    2 -> MapType.HYBRID
                    3 -> MapType.TERRAIN
                    else -> MapType.NONE
                }
            )
        ) {

            Marker(
                state = rememberMarkerState(position = ArequipaLocation),
                title = "Arequipa, Perú"
            )

            if (showBsAiresMarker) {
                val bsAiresLocation = LatLng(-34.603722, -58.381592)
                Marker(
                    state = rememberMarkerState(position = bsAiresLocation),
                    title = "Buenos Aires, Argentina"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { showBsAiresMarker = !showBsAiresMarker },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = if (showBsAiresMarker) "Eliminar Marcador en Buenos Aires" else "Agregar Marcador en Buenos Aires")
        }
    }
}
