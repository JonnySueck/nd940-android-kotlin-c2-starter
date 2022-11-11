package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidsDatabaseDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(databaseAsteroid: ArrayList<DatabaseAsteroid>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(asteroid: DatabaseAsteroid)

        @Query("SELECT * FROM asteroids_database WHERE close_approach_date >= :todaysDate")
        suspend fun getAll(todaysDate: String): List<DatabaseAsteroid>

        @Query("SELECT * FROM asteroids_database WHERE close_approach_date >= :todaysDate AND close_approach_date <= :oneWeekAwayDate")
        suspend fun getWeek(todaysDate: String, oneWeekAwayDate: String): List<DatabaseAsteroid>

        @Query("SELECT * FROM asteroids_database WHERE close_approach_date is :todaysDate")
        suspend fun getTonight(todaysDate: String): List<DatabaseAsteroid>

        @Query("SELECT * FROM asteroids_database WHERE id = :id")
        fun getAsteroidById(id: Long): LiveData<DatabaseAsteroid>

        @Query("DELETE FROM asteroids_database WHERE close_approach_date < :todaysDate")
        fun deleteOldAsteroids(todaysDate: String): Int
}