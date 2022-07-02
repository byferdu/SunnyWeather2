package com.example.sunnyweather2.logic.model

import java.util.*


data class DailyResponse(
    val result: Result,
    val status: String
) {

    data class Result(
        val daily: Daily
    )

    data class Daily(
        val life_index: LifeIndex,
        val skycon: List<Skycon>,
        val temperature: List<Temperature>
    )
    data class Temperature(val max:Float,val min:Float)
    data class Skycon(val value:String,val date:Date)

    data class LifeIndex(
        val carWashing: List<LifeDescription>,
        val coldRisk: List<LifeDescription>,
        val dressing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>
    )
    data class LifeDescription(val desc:String)
}