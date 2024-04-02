package com.example.mapsense.data

import com.example.mapsense.network.ApiService

class WeatherDataSource(private val apiService: ApiService){

    // we can store the AppID in secrets for production builds

    /**
     * method is used to get weather info based on coordinates
     */
    suspend fun getWeatherInfo(
        lat : Double,
        long : Double,
    ) = apiService.getWeatherInfo(lat,long,appid = "de73680ed1fcbafe972e567786c48047","metric")


    /**
     * method is used to get weather info based on city name
     */
    suspend fun getWeatherInfoForCityName(
        cityName : String
    ) = apiService.getWeatherInfoForCity(cityName,appid = "de73680ed1fcbafe972e567786c48047","metric")
}