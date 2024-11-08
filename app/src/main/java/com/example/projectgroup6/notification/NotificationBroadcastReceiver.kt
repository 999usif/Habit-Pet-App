package com.example.projectgroup6.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.projectgroup6.MainActivity
import com.example.projectgroup6.R


const val notificationID = 1
const val  channelID = "taskChannel"
const val TITLE = ""
const val MESSAGE = ""

class NotificationBroadcastReceiver : BroadcastReceiver()
{
    /**
     * sends notification with pending intent to MainActivity
     *
     * @param context
     * @param intent
     */
    override fun onReceive(context: Context,intent: Intent) {
        // Create an Intent for the activity you want to start
        val resultIntent = Intent(context, MainActivity::class.java)
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }


        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.logodkyp_icon)
            .setContentTitle(intent.getStringExtra("Time to do task: $TITLE"))
            .setContentText(intent.getStringExtra(MESSAGE))
            .setContentIntent(resultPendingIntent)
            .build()
        notification.flags =  NotificationCompat.FLAG_AUTO_CANCEL

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID,notification)
    }

}