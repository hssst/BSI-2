package com.maral.happyplacesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class for storing user-defined happy places.
 * Includes optional image, category and personal notes.
 */
@Entity
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,     // Unique ID
    val name: String,                                     // Name of the place
    val description: String,                              // Short description
    val latitude: Double,                                 // Latitude coordinate
    val longitude: Double,                                // Longitude coordinate
    val photoPath: String? = null,                        // Optional path to an image
    val category: String? = null,                         // Optional category label
    val notes: String? = null                             // 🆕 Optional personal notes
)
