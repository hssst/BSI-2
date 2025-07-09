package com.maral.happyplacesapp.ui.screens

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Composable wrapper for OSMDroid MapView.
 * Shows an interactive OpenStreetMap where the user can select a location.
 */
@Composable
fun OSMMapView(
    modifier: Modifier = Modifier,
    selectedLocation: GeoPoint?,
    onLocationSelected: (GeoPoint) -> Unit
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        factory = {
            // Lade OSM-Konfiguration
            Configuration.getInstance().load(
                context, context.getSharedPreferences("osm", Context.MODE_PRIVATE)
            )

            val mapView = MapView(context).apply {
                setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)

                // Zentrum setzen
                val startPoint = selectedLocation ?: GeoPoint(52.52, 13.405) // Berlin als Standard
                controller.setCenter(startPoint)

                // Marker setzen
                val marker = Marker(this).apply {
                    position = startPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                overlays.add(marker)

                // Tipp auf Karte = neue Position setzen
                setOnTouchListener { v, event ->
                    v.onTouchEvent(event)
                    val projection = this.projection
                    val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint // ✅ Cast!
                    marker.position = geoPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    invalidate()
                    onLocationSelected(geoPoint)
                    true
                }
            }

            mapView
        }
    )
}

