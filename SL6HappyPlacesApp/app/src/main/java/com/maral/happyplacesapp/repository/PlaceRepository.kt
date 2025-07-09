package com.maral.happyplacesapp.repository

import com.maral.happyplacesapp.data.Place
import com.maral.happyplacesapp.data.PlaceDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository for place operations.
 * Forwards database queries from the DAO.
 */
class PlaceRepository(private val placeDao: PlaceDao) {

    fun searchPlaces(query: String): Flow<List<Place>> = placeDao.searchPlaces(query)

    suspend fun insertPlace(place: Place) = placeDao.insertPlace(place)
    suspend fun updatePlace(place: Place) = placeDao.updatePlace(place)
    suspend fun deletePlace(place: Place) = placeDao.deletePlace(place)
    suspend fun getPlaceById(id: Int) = placeDao.getPlaceById(id)
}

