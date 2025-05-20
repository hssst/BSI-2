package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import androidx.navigation.compose.rememberNavController


// Main activity class - the entry point of your Android application
class MainActivity : ComponentActivity() {
    // onCreate is called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge display (uses the full screen)
        enableEdgeToEdge()
        // Sets the content of the activity using Jetpack Compose
        setContent {
            // Applies your custom theme to the entire app
            ShoppingListTheme {
                // Surface is a basic container that applies background color
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Calls your main composable function that contains the app UI
                    ShoppingListApp()
                }
            }
        }
    }
}

// Data class to represent a shopping item
// Each item has:
// - id: A unique identifier for the item
// - name: The text description of the item
data class ShoppingItem(val id: Int, var name: String)

// Main composable function for the shopping list app
// @OptIn is used to acknowledge we're using experimental Material3 API features
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp() {
    // State variables to keep track of UI and data
    // When these values change, the UI will automatically update

    // Stores the current text in the input field
    var newItem by remember { mutableStateOf("") }

    // List of all shopping items
    var items by remember { mutableStateOf(listOf<ShoppingItem>()) }

    // Counter for generating unique IDs for new items
    var nextId by remember { mutableStateOf(1) }

    // Currently edited item (null if not editing)
    var editingItem by remember { mutableStateOf<ShoppingItem?>(null) }

    // Flag to track if we're in edit mode
    var isEditing by remember { mutableStateOf(false) }

    // Scaffold provides the basic material design layout structure
    // It has slots for topBar, content, bottomBar, etc.
    Scaffold(
        // Top app bar - appears at the top of the screen
        topBar = {
            TopAppBar(
                // Title text for the app bar
                title = { Text("Einkaufsliste", fontWeight = FontWeight.Bold) },
                // Colors for the app bar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        // Main content column - arranges children vertically
        Column(
            // Apply padding from the scaffold and add additional padding
            modifier = Modifier
                .padding(paddingValues)  // This respects the top bar space
                .padding(16.dp)          // Additional padding around all content
        ) {
            // Card for the input section - gives elevated appearance
            Card(
                modifier = Modifier
                    .fillMaxWidth()       // Take full width available
                    .shadow(4.dp, RoundedCornerShape(8.dp)),  // Add shadow effect
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                // Column inside the card for input controls
                Column(modifier = Modifier.padding(16.dp)) {
                    // Title text changes based on whether we're editing or adding
                    Text(
                        text = if (isEditing) "Artikel bearbeiten" else "Artikel hinzufügen",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Text input field for new items or editing existing ones
                    OutlinedTextField(
                        value = newItem,  // Current text value
                        onValueChange = { newItem = it },  // Update when text changes
                        label = { Text(if (isEditing) "Artikel ändern" else "Neuer Artikel") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Space between text field and buttons
                    Spacer(modifier = Modifier.height(16.dp))

                    // Row to contain action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Add/Save button - behavior changes based on edit mode
                        Button(
                            onClick = {
                                if (newItem.isNotBlank()) {  // Only proceed if text isn't empty
                                    if (isEditing && editingItem != null) {
                                        // If editing, update the existing item
                                        items = items.map {
                                            if (it.id == editingItem!!.id) it.copy(name = newItem) else it
                                        }
                                        isEditing = false
                                        editingItem = null
                                    } else {
                                        // If adding, create a new item
                                        items = items + ShoppingItem(nextId++, newItem)
                                    }
                                    newItem = ""  // Clear the input field
                                }
                            },
                            modifier = Modifier.weight(1f),  // Take equal space in row
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (isEditing) "Speichern" else "Hinzufügen")
                        }

                        // Cancel button - only shown in edit mode
                        if (isEditing) {
                            OutlinedButton(
                                onClick = {
                                    isEditing = false
                                    editingItem = null
                                    newItem = ""  // Clear the input field
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Abbrechen")
                            }
                        }
                    }
                }
            }

            // Space between input card and items list
            Spacer(modifier = Modifier.height(24.dp))

            // Title for the items list section with item count
            Text(
                text = "Meine Artikel (${items.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Show a message if list is empty, otherwise show the items
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center  // Center the content
                ) {
                    Text(
                        "Noch keine Artikel in der Liste",
                        color = Color.Gray
                    )
                }
            } else {
                // LazyColumn is like RecyclerView - efficient for long lists
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)  // Space between items
                ) {
                    // Generate items in the list from our data
                    items(items) { item ->
                        // Card for each item - gives elevated appearance
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            // Row to arrange item text and action buttons horizontally
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Item text
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)  // Take available space
                                )

                                // Action buttons row
                                Row {
                                    // Edit button
                                    IconButton(
                                        onClick = {
                                            // Set up edit mode with the current item
                                            editingItem = item
                                            newItem = item.name
                                            isEditing = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Bearbeiten",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    // Delete button
                                    IconButton(
                                        onClick = {
                                            // Remove the item from the list
                                            items = items.filter { it.id != item.id }
                                            // If we were editing this item, exit edit mode
                                            if (isEditing && editingItem?.id == item.id) {
                                                isEditing = false
                                                newItem = ""
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Löschen",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}