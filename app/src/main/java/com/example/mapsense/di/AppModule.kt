package com.example.mapsense.di

import com.example.mapsense.data.WeatherDataSource
import com.example.mapsense.data.WeatherRepo
import com.example.mapsense.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun getMovieDataSource(apiService: ApiService) : WeatherDataSource{
        return WeatherDataSource(apiService)
    }

    @Provides
    fun getMovieRepo(weatherDataSource: WeatherDataSource) : WeatherRepo {
        return WeatherRepo(weatherDataSource)
    }


}


