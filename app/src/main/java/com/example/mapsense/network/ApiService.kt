package com.example.mapsense.network

import com.example.mapsense.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
   suspend fun getWeatherInfo(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("appid") appid : String,
        @Query("units") units : String
    ) : WeatherResponse

    @GET("data/2.5/weather")
    suspend fun getWeatherInfoForCity(
        @Query("q") cityName : String,
        @Query("appid") appid : String,
        @Query("units") units : String
    ) : WeatherResponse
}