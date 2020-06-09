package com.example.kimsm.weartoday

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat


class NotificationHelper(base: Context) : ContextWrapper(base) {

    var intent = Intent(this, MainActivity::class.java)


    private var mManager: NotificationManager? = null
    val manager: NotificationManager
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager as NotificationManager
        }
    var activityIntent = Intent(this, MainActivity::class.java)
    var contentIntent = PendingIntent.getActivity(this,
            0, activityIntent, 0)

    //받은 사진
    var picture = BitmapFactory.decodeResource(resources, R.drawable.icon)

    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(applicationContext, channel1Id)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("오늘 뭐입지?")
                .setContentText("오늘 스타일을 확인하세요!")
                .setLargeIcon(picture)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannels() {
        val channel1 = NotificationChannel(channel1Id, channel1Name, NotificationManager.IMPORTANCE_DEFAULT)
        channel1.enableLights(true)
        channel1.enableVibration(true)
        channel1.lightColor = R.color.colorPrimary
        channel1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        manager.createNotificationChannel(channel1)


    }

    companion object {
        val channel1Id = "channel1ID"
        val channel1Name = "channel 1"
    }
}
