package edu.rosehulman.cuiy1.rosechecker

import com.google.firebase.Timestamp

object Utils {
    val MONTH = arrayListOf<String>("JAN","FEB", "MAR", "APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC")

    fun timeStampToString(startTimeStamp: Timestamp, endTimestamp: Timestamp): EventTime{
        val date = startTimeStamp.toDate()
        val endDate = endTimestamp.toDate()
        val startMonth = MONTH[date.month]
        val startDay = date.day.toString()
        val endDay = endDate.day.toString()
        val endMonth = MONTH[date.month]
        val startTime = "${date.hours} : ${date.minutes}"
        val endTime = "${endDate.hours} : ${endDate.minutes}"
        return EventTime(startMonth,startDay,endMonth,endDay,startTime,endTime)
    }
}