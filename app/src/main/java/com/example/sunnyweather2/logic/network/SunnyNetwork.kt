package com.example.sunnyweather2.logic.network


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlace(query:String) = placeService.searchPlace(query).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine {
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    //TODO("Not yet implemented")
                    val body = response.body()
                    if (body != null) it.resume(body)
                    else it.resumeWithException(RuntimeException("responce body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    //TODO("Not yet implemented")
                    it.resumeWithException(t)
                }

            })
        }
    }
}