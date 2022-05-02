package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabaseDao
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val asteroidsDatabaseDao: AsteroidsDatabaseDao) {
//    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
//        asteroidsDatabaseDao.){
//        it.asDatabaseModel()
//    }


    suspend fun refreshAsteroidsList() {
        withContext(Dispatchers.IO){
            var asteroidsList = AsteroidApi.retrofitService.getAsteroids()
            val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsList))
            if(!parsedAsteroids.isNullOrEmpty()){
                asteroidsDatabaseDao.insertAll(parsedAsteroids.asDatabaseModel() as ArrayList<DatabaseAsteroid>)
//                asteroidsDatabaseDao.insertAll(parsedAsteroids.asDatabaseModel())
            }
        }
    }
}