package com.example.mapsense.data

import com.example.mapsense.data.model.WeatherResponse

class WeatherRepo(private val weatherDataSource: WeatherDataSource) {

    suspend fun getWeatherData(lat : Double,long : Double) : Resource<WeatherResponse> {
        return try {
            Resource.Success(data = weatherDataSource.getWeatherInfo(lat, long))
        }catch (e : Exception){
            Resource.Failure(message = e.message.toString())
        }
    }

    suspend fun getWeatherDataForCity(cityName : String) : Resource<WeatherResponse> {
        return try {
            Resource.Success(data = weatherDataSource.getWeatherInfoForCityName(cityName))
        }catch (e : Exception){
            Resource.Failure(message = e.message.toString())
        }
    }

}