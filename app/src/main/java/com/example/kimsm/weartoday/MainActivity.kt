package com.example.kimsm.weartoday

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast



class MainActivity : AppCompatActivity() {
    var id : String = ""
    var pass : String = ""
    var pressedTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("zzzzzzzzzzz",R.drawable.a_1_1.toString())
        val mysharedpreference = MySharedPreferences(this)
        var loginCount = mysharedpreference.getLoginCount()
        if(loginCount==1)
        {
            val intent = Intent(this, getweather::class.java)
            intent.putExtra("nickname", mysharedpreference.getID())
            startActivity(intent)
        }
        LOGINb.setOnClickListener {
                id = IDtext.text.toString()
                pass = PASStext.text.toString()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("pass", pass)
                IDtext.setText("")
                PASStext.setText("")
                startActivity(intent)
        }
        SIGNUPb.setOnClickListener {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
    }

    override fun onBackPressed() {
        if (pressedTime == 0.toLong()) {
            Toast.makeText(this@MainActivity, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
            pressedTime = System.currentTimeMillis()
            return
        } else {
            val seconds = (System.currentTimeMillis() - pressedTime).toInt()
            if (seconds > 2000) {
                Toast.makeText(this@MainActivity, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
                pressedTime = 0
            } else {
                super.onBackPressed()
                ActivityCompat.finishAffinity(this)
            }
        }
    }
}
