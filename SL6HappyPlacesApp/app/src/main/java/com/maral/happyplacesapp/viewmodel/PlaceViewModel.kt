package com.maral.happyplacesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maral.happyplacesapp.data.HappyPlacesDatabase
import com.maral.happyplacesapp.data.Place
import com.maral.happyplacesapp.repository.PlaceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel holding the search logic and live list of filtered places.
 */
class PlaceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlaceRepository

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Live filtered places depending on search query
    val places: StateFlow<List<Place>>

    init {
        val dao = HappyPlacesDatabase.getDatabase(application).placeDao()
        repository = PlaceRepository(dao)

        places = searchQuery
            .debounce(300) // wait 300ms before updating
            .flatMapLatest { query -> repository.searchPlaces(query) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }

    fun insertPlace(place: Place) = viewModelScope.launch { repository.insertPlace(place) }
    fun updatePlace(place: Place) = viewModelScope.launch { repository.updatePlace(place) }
    fun deletePlace(place: Place) = viewModelScope.launch { repository.deletePlace(place) }
    suspend fun getPlaceById(id: Int) = repository.getPlaceById(id)
}


