package com.maral.happyplacesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class HappyPlacesDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: HappyPlacesDatabase? = null

        fun getDatabase(context: Context): HappyPlacesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HappyPlacesDatabase::class.java,
                    "happy_places_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
