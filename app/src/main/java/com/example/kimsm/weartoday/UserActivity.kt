package com.example.kimsm.weartoday

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user.*
import org.jsoup.Jsoup

class UserActivity : AppCompatActivity() {
    val mysharedpreference by lazy { MySharedPreferences(this)}
    var image1 = ""
    var image2 = ""
    var image3 = ""
    var pressedTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val styleasync = StyleAsync()
        styleasync.execute()
        imageView.setOnClickListener {
            var intent = Intent(this,SpecifyActivity::class.java)
            intent.putExtra("overall",image1);
            startActivity(intent)
        }
        imageView2.setOnClickListener {
            var intent = Intent(this,SpecifyActivity::class.java)
            intent.putExtra("overall",image2);
            startActivity(intent)
        }
        imageView3.setOnClickListener {
            var intent = Intent(this,SpecifyActivity::class.java)
            intent.putExtra("overall",image3);
            startActivity(intent)
        }
        imageView6.setOnClickListener {
            val intent = Intent(this,WeatherActivity::class.java)
            startActivity(intent)
        }
        imageView18.setOnClickListener{
            val intent =Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (pressedTime == 0.toLong()) {
            Toast.makeText(this@UserActivity, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
            pressedTime = System.currentTimeMillis()
            return
        } else {
            val seconds = (System.currentTimeMillis() - pressedTime).toInt()
            if (seconds > 2000) {
                Toast.makeText(this@UserActivity, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
                pressedTime = 0
            } else {
                super.onBackPressed()
                ActivityCompat.finishAffinity(this)
            }
        }
    }
    inner class StyleAsync : AsyncTask<Void, String, Void>() {
        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            imageView.setImageResource(resources.getIdentifier("@drawable/"+image1,"drawable",packageName))
            imageView2.setImageResource(resources.getIdentifier("@drawable/"+image2,"drawable",packageName))
            imageView3.setImageResource(resources.getIdentifier("@drawable/"+image3,"drawable",packageName))
        }

        override fun doInBackground(vararg voids: Void): Void? {//user thread
            val url = "http://192.168.1.121:7070/WearToday/getOverall.jsp"
            var temp =0;
            if(mysharedpreference.getTemp()<=3)
                temp=0
            else if(mysharedpreference.getTemp()>3&&mysharedpreference.getTemp()<25)
                temp=1
            else
                temp=2
            val param = "?id=${mysharedpreference.getID()}&temperature=$temp&weather=${mysharedpreference.getWea()}"

            val u = url + param
            Log.i("zzzzzzzzzzzzzzz",u)

            Jsoup.connect(u).get().run {
                select("data").forEachIndexed { index, element ->
                    Log.i("zzzzzzzzzzzzzzzzzzzz",element.select("overall1").text().toString())
                    image1 = element.select("overall1").text().toString()
                    image2 = element.select("overall2").text().toString()
                    image3 = element.select("overall3").text().toString()
                    Log.i("zzzzzzzzzzzzzzzzzz",image1.toString())
                    Log.i("zzzzzzzzzzzzzzzzzz",image2.toString())
                    Log.i("zzzzzzzzzzzzzzzzzz",image3.toString())
                }
            }
            publishProgress()
            return null
        }
    }
}
