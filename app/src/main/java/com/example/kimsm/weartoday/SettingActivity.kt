package com.example.kimsm.weartoday

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.kimsm.weartoday.R.id.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.jsoup.Jsoup

import java.text.DateFormat
import java.util.Calendar


class SettingActivity  : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    val mysharedpreference by lazy { MySharedPreferences(this)}
    private var textView: TextView? = null
    var hour = 8
    var minute = 0
    var enable = 0
    var c = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        textView = findViewById<View>(R.id.AlarmText) as TextView
        getTimeAsync().execute()
        button.setOnClickListener {
            val timePicker = TimePickerFragment()
            timePicker.show(supportFragmentManager, "time picker ")
        }


        buttoncancelalarm.setOnClickListener { cancelAlarm() }
        buttonlogout.setOnClickListener{
            val mysharedpreference = MySharedPreferences(this)
            mysharedpreference.clear()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute1: Int) {
        textView!!.text = "Hour : " + hourOfDay + "Minute : " + minute

        val c = Calendar.getInstance()
        hour= hourOfDay
        minute = minute1
        enable = 1
        c.set(Calendar.HOUR_OF_DAY,hour)
        c.set(Calendar.MINUTE,minute)
        c.set(Calendar.SECOND,0)
        Toast.makeText(this, "$hourOfDay/$minute1",
                Toast.LENGTH_LONG).show()
        setTimeAsync().execute()
        startAlarm(c)
        updateTimeText(c)
    }

    private fun updateTimeText(c: Calendar) {
        var timeText = "알람 : "
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)

        textView!!.text = timeText

    }

    private fun startAlarm(c: Calendar) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)


        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
        textView!!.text = "Alarm canceled"
        enable = 0
        setTimeAsync().execute()
    }
    inner class getTimeAsync : AsyncTask<Void, String, Void>() {
        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            if(enable==1) {
                val c = Calendar.getInstance()
                c.set(Calendar.HOUR_OF_DAY,hour)
                c.set(Calendar.MINUTE,minute)
                c.set(Calendar.SECOND,0)
                startAlarm(c)
                updateTimeText(c)
            }
            else
                cancelAlarm()
        }

        override fun doInBackground(vararg voids: Void): Void? {//user thread
            val url = "http://192.168.1.121:7070/WearToday/Time.jsp"
            val param = "?id=${mysharedpreference.getID()}"

            val u = url + param
            Log.i("zzzzzzzzzzzzzz",u)
            Jsoup.connect(u).get().run {
                select("data").forEachIndexed { index, element ->
                    Log.i("zzzzzzzzzzzzzzzzzzzz",element.select("hour").text().toString())
                    hour = Integer.parseInt(element.select("hour").text().toString())
                    minute = Integer.parseInt(element.select("minute").text().toString())
                    enable = Integer.parseInt(element.select("enable").text().toString())
                }
            }
            publishProgress()
            return null
        }
    }
    inner class setTimeAsync : AsyncTask<Void, String, Void>() {
        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
        }

        override fun doInBackground(vararg voids: Void): Void? {//user thread
            val url = "http://192.168.1.121:7070/WearToday/setTime.jsp"
            val param = "?hour=$hour&minute=$minute&enable=$enable&id=${mysharedpreference.getID()}"
            val u = url + param
            Log.i("zzzzzzzzzzzzzz",u)
            Jsoup.connect(u).get().run {
                if(select("result").toString()=="true")
                   publishProgress("알람설정완료")
            }
            publishProgress("알람설정실패")
            return null
        }
    }

}