package com.example.mapsense.uiLayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsense.data.Resource
import com.example.mapsense.data.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepo: WeatherRepo) : ViewModel() {

    private val _weatherLiveData = MutableLiveData<WeatherStateHolder>()
    val weatherLiveData: LiveData<WeatherStateHolder> = _weatherLiveData

    fun getWeatherData(
        lat: Double,
        long: Double
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = weatherRepo.getWeatherData(lat, long)) {
            is Resource.Success -> {
                _weatherLiveData.postValue(WeatherStateHolder(data = result.data))
            }

            is Resource.Failure -> {
                _weatherLiveData.postValue(WeatherStateHolder(error = result.message.toString()))
            }

            else -> {

            }
        }
    }

    fun getWeatherDataForCityName(
        cityName: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        when (val result = weatherRepo.getWeatherDataForCity(cityName)) {
            is Resource.Success -> {
                _weatherLiveData.postValue(WeatherStateHolder(data = result.data))
            }

            is Resource.Failure -> {
                if(result.message.toString().contains("404")){
                    _weatherLiveData.postValue(WeatherStateHolder(error = "City not found"))
                }else{
                    _weatherLiveData.postValue(WeatherStateHolder(error = result.message.toString()))
                }
            }
            else -> {

            }
        }
    }

}