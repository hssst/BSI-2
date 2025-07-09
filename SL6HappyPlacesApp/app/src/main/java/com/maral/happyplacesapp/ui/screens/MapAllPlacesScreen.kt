package com.maral.happyplacesapp.ui.screens

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maral.happyplacesapp.viewmodel.PlaceViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Zeigt alle gespeicherten Orte als Marker auf einer großen interaktiven OpenStreetMap.
 */
@Composable
fun MapAllPlacesScreen() {
    val context = LocalContext.current
    val viewModel: PlaceViewModel = viewModel()
    val places by viewModel.places.collectAsState()

    // Initialisiere OSM-Konfiguration
    DisposableEffect(Unit) {
        Configuration.getInstance().load(
            context, context.getSharedPreferences("osm_config", Context.MODE_PRIVATE)
        )
        onDispose { }
    }

    // AndroidView zur Einbettung der klassischen OSM MapView
    AndroidView(
        factory = {
            MapView(it).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                // Zentriere Karte auf ersten Ort oder Default-Position
                val centerPoint = places.firstOrNull()?.let {
                    GeoPoint(it.latitude, it.longitude)
                } ?: GeoPoint(51.0, 10.0) // Deutschlandmittelpunkt

                controller.setZoom(5.5)
                controller.setCenter(centerPoint)

                // Füge Marker für alle gespeicherten Orte hinzu
                places.forEach { place ->
                    val marker = Marker(this)
                    marker.position = GeoPoint(place.latitude, place.longitude) // ✅ explizit GeoPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = place.name
                    marker.subDescription = place.description
                    overlays.add(marker)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

