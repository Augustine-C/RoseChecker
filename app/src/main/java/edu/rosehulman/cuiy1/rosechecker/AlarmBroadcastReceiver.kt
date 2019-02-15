package edu.rosehulman.cuiy1.rosechecker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import edu.rosehulman.cuiy1.rosechecker.Utils.notificationManager
import edu.rosehulman.cuiy1.rosechecker.Utils.mChannel
import java.util.*


class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Create the notification to be shown

        val id = Utils.upcomingEvent!!.startTime!!.toDate().time / 1000


        val mBuilder = NotificationCompat.Builder(context!!, id.toString())
            .setSmallIcon(R.mipmap.rose_icon_round)
            .setContentTitle("Upcoming Event")
            .setContentText("${Utils.upcomingEvent!!.name} will start in ${(Utils.upcomingEvent!!.startTime!!.toDate().time - Calendar.getInstance().time.time).toInt() / 60000} minutes")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setChannelId(Constants.CHANNEL)
        notificationManager.notify(id.toInt(), mBuilder.build())
        Log.d(Constants.TAG, "${id.toInt()} ALARM")
    }
}