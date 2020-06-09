package com.example.kimsm.weartoday.Common
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object Common {
    var API_KEY = "64ebf5a1310d5aa93e3f03a9eb3a574a"

    var API_Link = "http://api.openweathermap.org/data/2.5/weather"

    val dateNow: String
        get() {
            val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm")
            val date = Date()
            return dateFormat.format(date)
        }

    fun apiRequest(lat: String, lng: String): String {
        val sb = StringBuilder(API_Link)
        sb.append(java.lang.String.format("?lat=%s&lon=%s&APPID=%s&units=metric", lat, lng, API_KEY))
        return sb.toString()
    }

    fun unixTimeStampToDataType(unixTimeStamp: Double): String {
        val dataFormat = SimpleDateFormat("HH:mm")
        val date = Date()
        date.time = unixTimeStamp.toLong() * 1000
        return dataFormat.format(date)
    }

    fun getImage(icon: String): String {
        return java.lang.String.format("http://openweathermap.org/img/w/%s.png", icon)
    }
}