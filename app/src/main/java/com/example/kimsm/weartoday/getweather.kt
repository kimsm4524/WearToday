package com.example.kimsm.weartoday

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.TextView

import com.example.kimsm.weartoday.Common.Common
import com.example.kimsm.weartoday.Helper.Helper
import com.example.kimsm.weartoday.Model.OpenWeatherMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_getweather.*

//05338424
class getweather : AppCompatActivity(), LocationListener {
    internal var locationManager: LocationManager?=null
    internal var provider: String? = null
    internal var openWeatherMap = OpenWeatherMap()
    internal var MY_PERMISSION = 0
    val mysharedpreference by lazy { MySharedPreferences(this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getweather)
        val anim = AnimationUtils.loadAnimation(this,R.anim.animation)
        chick_weather.animation = anim
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        provider = locationManager?.getBestProvider(Criteria(), false)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@getweather, arrayOf(Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSION)
            return
        }
        locationManager?.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,this,null)
        Log.i("zzzzzzzzzzzzzz","getweather")
        val location = locationManager?.getLastKnownLocation(provider)
        if (location == null)
        {
            Log.i("zzzzzzzzzzzzzzzz","error!!!!!")
        }
        else
            onLocationChanged(location)
    }


    override fun onPause() {
        super.onPause()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@getweather, arrayOf(Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSION)

        }

        locationManager?.removeUpdates(this)
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@getweather, arrayOf(Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSION)

        }
        if(provider != null)
            locationManager?.requestLocationUpdates(provider, 0, 0f, this)
    }


    override fun onLocationChanged(location: Location) {
        getweather.lat = location.latitude
        getweather.lng = location.longitude


        GetWeather().execute(Common.apiRequest(getweather.lat.toString(), getweather.lng.toString()))
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }
    private inner class GetWeather : AsyncTask<String, Void, String>() {
        internal var pd = ProgressDialog(this@getweather)

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: String): String? {
            var stream: java.lang.String?
            val urlString  = params[0]
            val http = Helper()
            stream = http.getHTTPData(urlString) as java.lang.String
            return stream as String
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            val a : java.lang.String = s as java.lang.String
            if (a.contains("Error: Not found city")) {
                pd.dismiss()
                return
            }
            val gson = Gson()
            val mType = object : TypeToken<OpenWeatherMap>() {

            }.type
            openWeatherMap = gson.fromJson(s as String, mType)
            pd.dismiss()
            mysharedpreference.setTemp(openWeatherMap.main!!.temp.toFloat())
            mysharedpreference.sethumidity(openWeatherMap.main!!.humidity)
            if(openWeatherMap.weather!![0].description == "clear sky"){
                mysharedpreference.setWea("sunny")
            }else if(openWeatherMap.weather!![0].description == "scattered clouds"){
                mysharedpreference.setWea("cloudy")
            }else if(openWeatherMap.weather!![0].description == "broken clouds"){
                mysharedpreference.setWea("cloudy")
            }else if(openWeatherMap.weather!![0].description == "shower rain"){
                mysharedpreference.setWea("rainy")
            }else if(openWeatherMap.weather!![0].description == "rain"){
                mysharedpreference.setWea("rainy")
            }else if(openWeatherMap.weather!![0].description == "thunderstorm"){
                mysharedpreference.setWea("rainy")
            }else if(openWeatherMap.weather!![0].description == "snow"){
                mysharedpreference.setWea("snow")
            }else if(openWeatherMap.weather!![0].description == "mist"){
                mysharedpreference.setWea("cloudy")
            }
            Log.i("zzzzzzzzzzzzz","here")
            val intent = Intent(this@getweather,UserActivity::class.java)
            startActivity(intent)
        }

    }
    companion object {
        internal var lat: Double = 0.toDouble()
        internal var lng: Double = 0.toDouble()
    }
}
