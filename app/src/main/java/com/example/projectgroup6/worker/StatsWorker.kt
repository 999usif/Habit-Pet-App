package com.example.projectgroup6.worker


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.projectgroup6.firebase.Firebase
import com.example.projectgroup6.notification.Notification

//Background worker to update firebase when app is closed
class StatsWorker (context : Context, params : WorkerParameters) : Worker(context, params)  {
    private lateinit var firebase: Firebase

    private val mContext = context
    private var uuid: String = ""


    override fun doWork(): Result {
        firebase = Firebase()
        uuid = inputData.getString("uuid").toString()
        Log.d(ContentValues.TAG, "Pet Worker run: $uuid")
        Handler(Looper.getMainLooper()).post {
          //  val n = Notification()
         //   n.fyl(mContext)
            firebase.reduceStats(uuid, ::test)
        }
        return Result.success()
    }
    fun test(string: String){
        Log.d(TAG, "Pet worker sucess")
    }
}