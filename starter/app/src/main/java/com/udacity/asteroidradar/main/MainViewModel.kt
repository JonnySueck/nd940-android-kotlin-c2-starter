package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
// private val repository: AsteroidsRepository
// add to mainViewModel as a dependency
class MainViewModel() : ViewModel() {

    // Private value that tells the status of loading the asteroids
    private val _status = MutableLiveData<AsteroidApiStatus>()

    val _asteroids = MutableLiveData<ArrayList<Asteroid>>()

    // Private value that keeps track of the PictureOfTheDay
    val _asteroidImage = MutableLiveData<PictureOfDay>()

//    private val database: AsteroidsDatabase(application)
//
//    val repository: AsteroidsRepository(database)

    // Public value that keeps track of the PicureOfTheDay
    val asteroidImage: LiveData<PictureOfDay>
        get() = _asteroidImage

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
                //                repository.refreshAsteroidsList()
                val asteroidsList = AsteroidApi.retrofitService.getAsteroids()
                val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsList))
                _asteroids.value = parsedAsteroids
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

}
//class AsteroidViewModelFactory(private val repository: AsteroidsRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AsteroidsRepository::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return MainViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}