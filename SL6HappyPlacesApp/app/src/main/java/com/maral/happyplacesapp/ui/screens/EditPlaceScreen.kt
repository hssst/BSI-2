package com.maral.happyplacesapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.maral.happyplacesapp.data.Place
import com.maral.happyplacesapp.viewmodel.PlaceViewModel
import org.osmdroid.util.GeoPoint

/**
 * Bildschirm zum Bearbeiten eines gespeicherten Ortes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaceScreen(navController: NavHostController, placeId: Int) {
    val context = LocalContext.current
    val viewModel: PlaceViewModel = viewModel()
    var place by remember { mutableStateOf<Place?>(null) }

    // Lade Ort aus Datenbank
    LaunchedEffect(placeId) {
        place = viewModel.getPlaceById(placeId)
    }

    if (place == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Lokale Zustände
    var name by remember { mutableStateOf(place!!.name) }
    var description by remember { mutableStateOf(place!!.description) }
    var category by remember { mutableStateOf(place!!.category ?: "") }
    var notes by remember { mutableStateOf(place!!.notes ?: "") }
    var photoUri by remember { mutableStateOf<Uri?>(Uri.parse(place!!.photoPath ?: "")) }
    var location by remember { mutableStateOf(GeoPoint(place!!.latitude, place!!.longitude)) }

    // Scrollbarer Bearbeitungsbildschirm
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name des Ortes") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences)
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Beschreibung") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Kategorie (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notiz (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            photoUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

        item {
            Text("Standort bearbeiten:")
        }

        item {
            OSMMapView(
                selectedLocation = location,
                onLocationSelected = { location = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Speicher-Button zentriert
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    viewModel.updatePlace(
                        place!!.copy(
                            name = name,
                            description = description,
                            category = category,
                            notes = notes,
                            photoPath = photoUri?.toString(),
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                    navController.popBackStack()
                }) {
                    Text("Änderungen speichern")
                }
            }
        }

        // Löschen-Button zentriert
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        viewModel.deletePlace(place!!)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Ort löschen")
                }
            }
        }
    }
}
