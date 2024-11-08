package com.example.projectgroup6

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.projectgroup6.databinding.ActivityTaskSchedulerBinding
import com.example.projectgroup6.firebase.Firebase
import com.example.projectgroup6.notification.MESSAGE
import com.example.projectgroup6.notification.NotificationBroadcastReceiver
import com.example.projectgroup6.notification.TITLE
import com.example.projectgroup6.notification.channelID
import com.example.projectgroup6.pet.Pet
import com.example.projectgroup6.task.DailyTask
import java.util.*

class TaskScheduler : AppCompatActivity() {
    private lateinit var binding: ActivityTaskSchedulerBinding
    private lateinit var pet: Pet
    private lateinit var firebase: Firebase
    private lateinit var dailyTask: DailyTask


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityTaskSchedulerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        firebase = Firebase()

        //gets pet from intent
        pet = (intent.getSerializableExtra("pet") as? Pet)!!



        binding.okButton.setOnClickListener {
            //makes sure title and description is not empty
            if (binding.taskTitle.text.toString().isEmpty()) {
                Helper.Helper.showAlert(this, "Error", getString(R.string.noTaskTitle))
            } else if (binding.taskDesc.text.toString().isEmpty()) {
                Helper.Helper.showAlert(this, "Error", getString(R.string.noTaskDescription))
            } else {
                dailyTask = DailyTask(
                    binding.taskTitle.text.toString(),
                    getTime(),
                    binding.taskDesc.text.toString(),
                    (0..99999).random(),
                    0L
                )

                pet.updateDailyTask(dailyTask)

                firebase.saveDailyTask(pet.getUuid(), dailyTask)

                scheduleNotification(dailyTask)

                //finish the activity, go back to home screen
                finish()
            }
        }
    }

    /**
     *  schedules a repeating notification from dailyTask
     *
     * @param dailyTask
     */
    private fun scheduleNotification(dailyTask: DailyTask?) {
        if (dailyTask != null) {
            Log.d(TAG, "Pet DocumentSnapshot added with ID: ${dailyTask.getId()}")


            val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java)

            val title = dailyTask.getDescription()
            val desc = "Time to do task: " + binding.taskTitle.text.toString()

            intent.putExtra(TITLE, title)
            intent.putExtra(MESSAGE, desc)

            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                dailyTask.getId(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            // dailyTask.set(time)
            val time = getTime()

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    /**
     * gets time in ms from timepicker
     *
     * @return
     */
    private fun getTime(): Long {
        val minute = binding.selectedTime.minute
        val hour = binding.selectedTime.hour

        val cal: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return cal.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "name"
        val desc = "Task Scheduler Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
