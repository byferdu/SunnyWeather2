package com.example.sunnyweather2.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather2.logic.dao.PlaceDao
import com.example.sunnyweather2.logic.model.Place
import com.example.sunnyweather2.logic.model.Weather
import com.example.sunnyweather2.logic.network.SunnyNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyNetwork.searchPlace(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is${placeResponse.status}"))
        }
    }


    fun refreshWeather(lng: String, lat: String,placeName:String) = fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime = async {
                    SunnyNetwork.getRealTimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    SunnyNetwork.getDailyWeather(lng, lat)
                }
                val realTimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realTimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(realTimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realTimeResponse.status}\n" +
                                    "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

}