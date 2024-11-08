package com.example.projectgroup6

import android.content.Context
import androidx.appcompat.app.AlertDialog

class Helper {
    /**
     * Helper object
     */
    object Helper {
        private lateinit var builder: AlertDialog.Builder

        /**
         * shows an alert in the sent context
         *
         * @param context
         * @param title title of the alert
         * @param message message of the alert
         */
        fun showAlert(context : Context, title: String, message: String) {
            val builder = AlertDialog.Builder(context)
            with(builder)
            {
                setTitle(title)
                setMessage(message)
                show()
            }
        }

        /**
         * return an alertDialog builder
         *
         * @param message
         * @param context
         * @return
         */
        fun getAlertDialog(message: String, context: Context): AlertDialog.Builder {
            builder = AlertDialog.Builder(context)
            builder.setMessage(message)
                .setCancelable(true)
            return builder

        }

        /**
         * @return returns current time in
         */
        fun getTime(): Long {
            return System.currentTimeMillis()/1000
        }
    }
}