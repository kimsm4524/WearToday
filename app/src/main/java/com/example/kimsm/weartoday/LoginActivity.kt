package com.example.kimsm.weartoday

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    val id by lazy { intent.getStringExtra("id") }
    val pass by lazy { intent.getStringExtra("pass") }
    val mysharedpreference by lazy{ MySharedPreferences(this) }
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val anim = AnimationUtils.loadAnimation(this,R.anim.animation)
        chick_login.animation = anim
        val data = DataAsync()
        data.execute()

    }

    inner class DataAsync : AsyncTask<Void, String, Void>() {
        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            Toast.makeText(this@LoginActivity, values[0], Toast.LENGTH_SHORT).show()
            if (values[0] == "로그인 성공") {
                mysharedpreference.setLoginCount(1)
                mysharedpreference.setID(values[1])
                val i = Intent(this@LoginActivity, getweather::class.java)
                startActivity(i)
            }else{
                Toast.makeText(this@LoginActivity,"실패",Toast.LENGTH_SHORT).show()
                val i =Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(i)
            }
        }

        override fun doInBackground(vararg voids: Void): Void? {//user thread
            val url = "http://192.168.1.121:7070/WearToday/login.jsp"
            val param = "?id=$id&pass=$pass"

            val u = url + param
            Log.i("zzzzzzzzzzzzzz",u)
            Jsoup.connect(u).get().run {
                select("data").forEachIndexed { index, element ->
                    val result = element.select("result").text().toString()
                    if(result.equals("true"))
                        publishProgress("로그인 성공", id)
                    else
                        publishProgress("로그인 실패")
                }
            }
            return null
        }
    }
}
