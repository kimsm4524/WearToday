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
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.widget.TextView
import android.widget.Toast

import com.example.kimsm.weartoday.Common.Common
import com.example.kimsm.weartoday.Helper.Helper
import com.example.kimsm.weartoday.Model.OpenWeatherMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity(){

    internal var txtDescription_kr: TextView? = null

    internal var txtDescription: TextView? = null
    internal var txtHumidity: TextView? = null
    internal var txtCelsius: TextView? = null
    val mysharedpreference by lazy {MySharedPreferences(this)}
    var pressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        //Control
        txtDescription_kr = findViewById<TextView>(R.id.txtDescription_kr)
        txtDescription = findViewById<TextView>(R.id.txtDescription)
        txtHumidity = findViewById<TextView>(R.id.txtHumidity)
        txtCelsius = findViewById<TextView>(R.id.txtCelsius)
        var desc = StringBuffer()
        val temp = mysharedpreference.getTemp()
        val humidity = mysharedpreference.gethumidity()
        val weather = mysharedpreference.getWea()
        if(weather == "sunny"){
            imageView12.setImageResource(R.drawable.za)
            txtDescription?.setText("맑음")
            if(temp <-5){
                desc.append(" 패딩을 추천합니다.")
            }else if(temp<10){
                desc.append("코트를 추천합니다")
            }else{
                desc.append("외투를 벗으시는걸 추천드립니다")
            }
        }else if(weather == "cloudy"){
            imageView12.setImageResource(R.drawable.zb)
            if(temp <-5){
                desc.append("패딩을 추천합니다")
            }else{
                desc.append(" 코트류를 추천합니다.")
            }
        }else if(weather == "rainy"){
            imageView12.setImageResource(R.drawable.zd)
            txtDescription?.setText("소나기성 비")
            if(temp <5){
                desc.append("우산을 챙기세요! 패딩을 추천합니다.")
            }else{
                desc.append("우산을 챙기세요!코트류를 추천합니다.")
            }
        }else if(weather == "snow"){
            imageView12.setImageResource(R.drawable.zg)
            txtDescription?.setText("눈")
            desc.append("눈이옵니다. 우산과 패딩을 챙겨입으세요 ")
        }

        txtDescription?.setText("날씨 정보")
        txtDescription_kr?.setText(desc)
        txtHumidity?.setText(java.lang.String.format("습도 : %d%%", humidity))
        txtCelsius?.setText(java.lang.String.format("온도 :%.2f °C", temp))
        imageView13.setOnClickListener {
            val intent = Intent(this,UserActivity::class.java)
            startActivity(intent)
        }
        imageView15.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        if (pressedTime == 0.toLong()) {
            Toast.makeText(this@WeatherActivity, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
            pressedTime = System.currentTimeMillis()
            return
        } else {
            val seconds = (System.currentTimeMillis() - pressedTime).toInt()
            if (seconds > 2000) {
                Toast.makeText(this@WeatherActivity, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
                pressedTime = 0
            } else {
                super.onBackPressed()
                ActivityCompat.finishAffinity(this)
            }
        }
    }

}
