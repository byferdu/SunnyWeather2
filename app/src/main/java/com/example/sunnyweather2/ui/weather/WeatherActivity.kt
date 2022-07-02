package com.example.sunnyweather2.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather2.R
import com.example.sunnyweather2.databinding.ActivityWeatherBinding
import com.example.sunnyweather2.databinding.ForcastItemBinding
import com.example.sunnyweather2.databinding.LifeIndexBinding
import com.example.sunnyweather2.databinding.NowBinding
import com.example.sunnyweather2.logic.model.Weather
import com.example.sunnyweather2.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
   lateinit var binding: ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
         binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nowLayout.navBtn.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
               // TODO("Not yet implemented")
            }

            override fun onDrawerOpened(drawerView: View) {
              //  TODO("Not yet implemented")
            }

            override fun onDrawerClosed(drawerView: View) {
               // TODO("Not yet implemented")
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                manager.hideSoftInputFromWindow(decorView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {
               // TODO("Not yet implemented")

            }

        })
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, androidx.lifecycle.Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法获取天气信息", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
            binding.refresh.isRefreshing=false
        })
        binding.refresh.setOnRefreshListener {
            refreshWeather()
        }
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }


   fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.refresh.isRefreshing=true
    }

    @SuppressLint("SetTextI18n")
    private fun showWeatherInfo(weather: Weather) {
        val realtime = weather.realtime
        val daily = weather.daily
        val sky = getSky(realtime.skycon)
        binding.nowLayout.run {
            titleTextView.text = viewModel.placeName
            realTemp.text = realtime.temperature.toInt().toString() + "℃"
            infoText.text = sky.info
            airText.text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
            nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        }
        val findViewById = findViewById<ViewGroup>(R.id.forceLayout)
            findViewById.removeAllViews()
        for (i in daily.skycon.indices) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val forecastItemBinding =
                ForcastItemBinding.inflate(LayoutInflater.from(this), null, false)
            forecastItemBinding.apply {
                val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateInfo.text = simpleFormat.format(skycon.date)
                val sky = getSky(skycon.value)
                skyIcon.setImageResource(sky.icon)
                skyInfo.text = sky.info
                val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                temperatureInfo.text = tempText
                findViewById.addView(forecastItemBinding.root)
            }
        }
        val lifeIndex = daily.life_index
        binding.lifeIndexLayout.run {
            data = lifeIndex
        }
        binding.weatherLayout.visibility = View.VISIBLE

    }

}