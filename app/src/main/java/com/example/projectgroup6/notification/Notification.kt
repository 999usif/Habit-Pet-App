package com.example.projectgroup6.notification


import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projectgroup6.MainActivity
import com.example.projectgroup6.R


open class Notification : AppCompatActivity() {
    private var mContext: Context? = null
    open fun fyl(mContext: Context?) {
        this.mContext = mContext
    }

    private val CHANNEL_ID = "channelID"
    private val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0

    private lateinit var text :String
    private lateinit var title :String


    /**
     * sends a notification
     *
     */
    fun sendNotification(type: String) {
        createNotifChannel()

        when (type) {
            "statsLow" -> {
                text = "Help"
                title = "Help"
            }
        }
        val intent = Intent(mContext, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(mContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = mContext?.let {
            NotificationCompat.Builder(it, channelID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.logodkyp_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }
        val notificationM = mContext?.let { NotificationManagerCompat.from(it) }

        if (notification != null) {
            notificationM?.notify(NOTIF_ID, notification)
        }
    }
    private fun createNotifChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            lightColor = Color.BLUE
            enableLights(true)
        }
    }

    }
