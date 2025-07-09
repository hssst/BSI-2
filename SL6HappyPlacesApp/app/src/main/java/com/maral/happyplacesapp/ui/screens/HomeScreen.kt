package com.maral.happyplacesapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Map
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

/**
 * Hauptansicht der App.
 * Zeigt Liste der gespeicherten Orte mit Suchfeld und Kartenzugang.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val placeViewModel: PlaceViewModel = viewModel()
    val places by placeViewModel.places.collectAsState()
    val searchQuery by placeViewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Happy Places") },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("map_all_places")
                        }) {
                            Icon(Icons.Filled.Map, contentDescription = "Karte")
                        }
                    }
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { placeViewModel.updateSearchQuery(it) },
                    label = { Text("Suche nach Name oder Kategorie") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_place")
            }) {
                Text("+")
            }
        }
    ) { padding ->
        if (places.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Keine Orte gefunden")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(places) { place ->
                    PlaceCard(
                        place = place,
                        onClick = {
                            navController.navigate("place/${place.id}")
                        },
                        onDeleteClick = {
                            placeViewModel.deletePlace(place)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Darstellung eines einzelnen Orts mit Bild, Name, Kategorie und Lösch-Icon.
 */
@Composable
fun PlaceCard(
    place: Place,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            place.photoPath?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(place.name, style = MaterialTheme.typography.titleMedium)
                Text(place.category ?: "Keine Kategorie", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDeleteClick() }) {
                Icon(Icons.Default.Delete, contentDescription = "Löschen")
            }
        }
    }
}
