package com.example.sunnyweather2.logic.network

import com.example.sunnyweather2.SunnyApplication
import com.example.sunnyweather2.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
              //v2/place?token=${SunnyApplication.TOKEN}&lang=zh_CN
    @GET("v2/place?token=${SunnyApplication.TOKEN}&lang=zh_CN")
    fun searchPlace(@Query("query")query: String) :Call<PlaceResponse>
}