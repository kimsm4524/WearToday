package com.example.kimsm.weartoday

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.kimsm.weartoday.R.id.chick_sign
import kotlinx.android.synthetic.main.activity_send_signup.*
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.lang.Exception
import java.util.ArrayList

class SendSignup : AppCompatActivity() {
    internal val id by lazy { intent.getStringExtra("id") }
    internal val pass by lazy { intent.getStringExtra("pass") }
    internal val nickname by lazy { intent.getStringExtra("nickname") }
    internal val age by lazy { intent.getStringExtra("age") }
    internal val gender by lazy { intent.getStringExtra("gender") }
    internal val style by lazy { intent.getStringExtra("style") }
    internal val figure by lazy { intent.getStringExtra("figure") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_signup)
        val anim = AnimationUtils.loadAnimation(this,R.anim.animation)
        chick_sign.animation = anim
        val info = INFOAsync()
        info.execute()
        val idpw = IDPWAsync()
        idpw.execute()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    internal inner class INFOAsync : AsyncTask<Void, String, Void>() {
        override fun doInBackground(vararg voids: Void): Void? {//user thread

            onProgressUpdate(makePostRequest())
            return null
        }

        override fun onProgressUpdate(vararg values: String) {//main thread
            super.onProgressUpdate(*values)

        }
    }

    private fun makePostRequest(): String {
        val msg = ""
        try {
            val httpClient = DefaultHttpClient()
            // replace with your url
            val httpPost = HttpPost("http://192.168.1.121:7070/WearToday/userinfo.jsp")


            //Post Data
            val nameValuePair = ArrayList<NameValuePair>(2)
            nameValuePair.add(BasicNameValuePair("USER_ID", id))
            nameValuePair.add(BasicNameValuePair("NICKNAME", nickname))
            nameValuePair.add(BasicNameValuePair("AGE", age))
            nameValuePair.add(BasicNameValuePair("GENDER", gender))
            nameValuePair.add(BasicNameValuePair("STYLE", style))
            nameValuePair.add(BasicNameValuePair("FIGURE", figure))


            //Encoding POST data
            val entity = UrlEncodedFormEntity(nameValuePair, "utf-8")
            httpPost.entity = entity


            //making POST request.

            val response = httpClient.execute(httpPost)
            //Log.d("Http Post Response:", response.toString());
            return response.toString()
            //EntityUtils.getContentCharSet(entity);
            // write response to log
            /*HttpEntity et = response.getEntity();
            InputStream in  = et.getContent();
                    //response.toString();*/

        } catch (e: Exception) {
            // Log exception
            e.printStackTrace()
        }

        return msg
    }

     inner class IDPWAsync : AsyncTask<Void, String, Void>() {

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            if(values[0]=="true")
                Toast.makeText(this@SendSignup, "회원가입 완료", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg voids: Void): Void? {//user thread
            val url = "http://192.168.1.121:7070/WearToday/signUp.jsp"
            val param = "?id=$id&pass=$pass"
            var xml: Document? = null
            val u = url + param
            try {
                xml = Jsoup.connect(u).get()//url에 접속해서 xml파일을 받아옴
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val result = xml!!.select("data")
            for (e in result)
                publishProgress(e.select("result").text())

            return null
        }
    }
}