package com.example.sunnyweather2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyApplication : Application(){

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "cw4t3EF7HfccxUa6"
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}