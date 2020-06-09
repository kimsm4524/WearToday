package com.example.kimsm.weartoday

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_specify.*
import org.jsoup.Jsoup

class SpecifyActivity : AppCompatActivity() {
    val overall by lazy { intent.getStringExtra("overall") }
    var outerimage = ""
    var topimage = ""
    var bottomimage = ""
    var shoesimage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specify)
        val clothesAsync = ClothesAsync()
        clothesAsync.execute()
        imageView16.setOnClickListener{
            val intent = Intent(this,UserActivity::class.java)
            startActivity(intent)
        }
        imageView17.setOnClickListener{
            val intent = Intent(this,WeatherActivity::class.java)
            startActivity(intent)
        }
        imageView18.setOnClickListener{
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }
    }

    inner class ClothesAsync : AsyncTask<Void, String, Void>() {
        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            if (outerimage != "null") {
                imageView8.visibility = View.VISIBLE
                imageView8.setImageResource(resources.getIdentifier("@drawable/"+outerimage,"drawable",packageName))
            }
            imageView9.setImageResource(resources.getIdentifier(topimage,"drawable",packageName))
            imageView10.setImageResource(resources.getIdentifier(bottomimage,"drawable",packageName))
            imageView11.setImageResource(resources.getIdentifier(shoesimage,"drawable",packageName))
        }

        override fun doInBackground(vararg voids: Void): Void? {//user thread
            val url = "http://192.168.1.121:7070/WearToday/getClothes.jsp"

            val param = "?overall=$overall"

            val u = url + param
            Log.i("zzzzzzzzzzzzzzz", u)

            Jsoup.connect(u).get().run {
                select("data").forEachIndexed { index, element ->
                    outerimage = element.select("outer").text().toString()
                    topimage = "@drawable/"+element.select("top").text().toString()
                    bottomimage = "@drawable/"+element.select("bottoms").text().toString()
                    shoesimage = "@drawable/"+element.select("shoes").text().toString()
                }
            }
            publishProgress()
            return null
        }
    }
}
