package edu.rosehulman.cuiy1.rosechecker

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.app.NotificationChannel



class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Create the notification to be shown
        val am = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = Utils.upcomingEvent.startTime!!.toDate().time/1000
        val mChannel = NotificationChannel(id.toString(),"Channel Name",NotificationManager.IMPORTANCE_HIGH)
        am.createNotificationChannel(mChannel)
        val mBuilder = NotificationCompat.Builder(context,id.toString())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm Manager")
            .setContentText("${Utils.upcomingEvent.name} will start in 30 minutes")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        am.notify(id.toInt(), mBuilder.build())
        Log.d(Constants.TAG,"${id.toInt()} ALARM")
    }
}