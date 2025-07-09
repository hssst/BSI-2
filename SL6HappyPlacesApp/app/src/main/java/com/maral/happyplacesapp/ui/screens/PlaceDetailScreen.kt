package com.maral.happyplacesapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.maral.happyplacesapp.data.Place
import com.maral.happyplacesapp.viewmodel.PlaceViewModel
import org.osmdroid.util.GeoPoint

/**
 * Displays detailed information of a saved place.
 * Includes image, category, description, location and optional notes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(navController: NavHostController, placeId: Int) {
    val viewModel: PlaceViewModel = viewModel()
    var place by remember { mutableStateOf<Place?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // Load place from database using the provided ID
    LaunchedEffect(placeId) {
        place = viewModel.getPlaceById(placeId)
    }

    place?.let {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(it.name) })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Image
                it.photoPath?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                // Category and description
                Text(text = "Kategorie: ${it.category ?: "Keine"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Beschreibung: ${it.description}", style = MaterialTheme.typography.bodyMedium)

                // 🆕 Show notes if available
                if (!it.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Notiz: ${it.notes}", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Standort auf der Karte:")

                // Show map with fixed location
                OSMMapView(
                    selectedLocation = GeoPoint(it.latitude, it.longitude),
                    onLocationSelected = {}
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons: Edit and Delete
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        navController.navigate("edit_place/${it.id}")
                    }) {
                        Text("Bearbeiten")
                    }

                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Löschen")
                    }
                }

                // Delete confirmation dialog
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Ort löschen") },
                        text = { Text("Möchtest du diesen Ort wirklich löschen?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deletePlace(it)
                                navController.popBackStack()
                            }) {
                                Text("Ja")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Abbrechen")
                            }
                        }
                    )
                }
            }
        }
    } ?: run {
        // Show loading spinner while place is being fetched
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}



