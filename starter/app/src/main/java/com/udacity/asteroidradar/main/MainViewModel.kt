package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
// add to mainViewModel as a dependency
class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Private value that tells the status of loading the asteroids
    private val _status = MutableLiveData<AsteroidApiStatus>()

    private lateinit var asteroids : ArrayList<DatabaseAsteroid>
//            get() = _asteroids

    private var _asteroids = MutableLiveData<ArrayList<DatabaseAsteroid>>()

    // Private value that keeps track of the PictureOfTheDay
    val _asteroidImage = MutableLiveData<PictureOfDay>()

    // Public value that keeps track of the PicureOfTheDay
    val asteroidImage: LiveData<PictureOfDay>
        get() = _asteroidImage

    lateinit var repository: AsteroidsRepository

    init {
        val asteroidDB = AsteroidsDatabase.getInstance(application).asteroidsDao()
        repository = AsteroidsRepository(asteroidDB)
    }

    fun getImageOfTheDay() {
        _status.value = AsteroidApiStatus.LOADING
        viewModelScope.launch {
            try {
                val imageResult = AsteroidApi.retrofitService.getImageOfTheDay()
                // update the PictureOfDay to the newly defined imageResult
                _asteroidImage.value = imageResult
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

    fun getAsteroidsList() {
        _status.value = AsteroidApiStatus.LOADING
        viewModelScope.launch {
            try {
                repository.refreshAsteroidsList()
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

}