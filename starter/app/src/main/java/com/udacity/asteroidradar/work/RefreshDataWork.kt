package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getInstance(applicationContext)
        val repository = AsteroidsRepository(database.asteroidsDao)
        withContext(Dispatchers.IO) {
            var asteroidsList = AsteroidApi.retrofitService.getAsteroids(
                startDate = getNextSevenDaysFormattedDates().first(),
                endDate = getNextSevenDaysFormattedDates().last()
            )
            val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsList))
            if (!parsedAsteroids.isNullOrEmpty()) {
                for (asteroid in parsedAsteroids)
                    repository.insertAsteroid(asteroid)
            }
        }
        return Result.success()
    }
}