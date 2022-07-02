package com.example.sunnyweather2.logic.network

import com.example.sunnyweather2.SunnyApplication
import com.example.sunnyweather2.logic.model.DailyResponse
import com.example.sunnyweather2.logic.model.RealTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.6/${SunnyApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng:String , @Path("lat") lat:String):Call<RealTimeResponse>

    @GET("v2.6/${SunnyApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>

}