package com.example.kimsm.weartoday

import android.content.SharedPreferences
import android.content.Context
import java.util.Calendar

public class MySharedPreferences(context: Context) {

    val PREFS_FILENAME = "prefs"
    val Login_count = "LoginCount"
    val USER_ID = "user_id"
    val weather = "Weather"
    val temperature = "temperature"
    val humidity = "humidity"
    val prefs = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    /* 파일 이름과 EditText를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    fun getLoginCount() : Int{
        return prefs.getInt(Login_count,0)
    }
    fun setLoginCount(count:Int){
        val editor = prefs.edit()
        editor.putInt(Login_count,count)
        editor.apply()
    }
    fun setID(userID:String)
    {
        val editor = prefs.edit()
        editor.putString(USER_ID, userID)
        editor.apply()
    }
    fun getID() : String{
        return prefs.getString(USER_ID,"")
    }
    fun setWea(wea :String){
        val editor = prefs.edit()
        editor.putString(weather,wea)
        editor.apply();
    }
    fun setTemp(temp : Float){
        val editor = prefs.edit()
        editor.putFloat(temperature,temp)
        editor.apply()
    }
    fun getWea() : String{
        return prefs.getString(weather,"")
    }
    fun getTemp() : Float{
        return prefs.getFloat(temperature,Float.NaN);
    }
    fun sethumidity(temp : Int){
        val editor = prefs.edit()
        editor.putInt(humidity,temp)
        editor.apply()
    }
    fun gethumidity():Int{
        return prefs.getInt(humidity,0)
    }
    fun clear(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}