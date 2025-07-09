package com.maral.happyplacesapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for Place.
 * Includes a search query using LIKE on name and category.
 */
@Dao
interface PlaceDao {

    @Query("""
        SELECT * FROM Place
        WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'
        ORDER BY name ASC
    """)
    fun searchPlaces(query: String): Flow<List<Place>>

    @Insert
    suspend fun insertPlace(place: Place)

    @Update
    suspend fun updatePlace(place: Place)

    @Delete
    suspend fun deletePlace(place: Place)

    @Query("SELECT * FROM Place WHERE id = :id")
    suspend fun getPlaceById(id: Int): Place
}

