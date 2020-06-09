package com.example.kimsm.weartoday.Helper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Helper {
    fun getHTTPData(urlString: String): String {
        try {
            val url = URL(urlString)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            if (httpURLConnection.responseCode == 200) {
                val r = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val sb = StringBuilder()
                var line: String?
                while (true){
                    line = r.readLine()
                    if(line==null){break}
                    sb.append(line)
                }
                stream = sb.toString()
                httpURLConnection.disconnect()
            }


        }  catch (e: IOException) {
            e.printStackTrace()
        }

        return stream!! as String
    }

    companion object {
        internal var stream: String? = null
    }
}
