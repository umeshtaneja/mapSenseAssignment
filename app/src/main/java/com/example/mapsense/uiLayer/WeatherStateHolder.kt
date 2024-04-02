package com.example.mapsense.uiLayer

import com.example.mapsense.data.model.WeatherResponse

data class WeatherStateHolder(
    val isLoading: Boolean = false,
    val data : WeatherResponse? = null,
    val error : String = ""
)