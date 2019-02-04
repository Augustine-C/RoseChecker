package edu.rosehulman.cuiy1.rosechecker

import com.google.firebase.Timestamp

object Utils {
    val MONTH = arrayListOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
    var isAll = false

    fun timeStampToString(startTimeStamp: Timestamp, endTimestamp: Timestamp): EventTime {
        val start = startTimeStamp.toDate()
        val end = endTimestamp.toDate()
        val startYear = (start.year + 1900).toString()
        val endYear = (end.year + 1900).toString()
        val startMonth = MONTH[start.month]
        val startDay = start.date.toString()
        val endDay = end.date.toString()
        val endMonth = MONTH[start.month]
        val startTime = "${start.hours} : ${start.minutes}"
        val endTime = "${end.hours} : ${end.minutes}"
        val showStartTime = "${start.hours} : ${pad(start.minutes)}"


        val showEndTime = "${end.hours} : ${pad(end.minutes)}"

        val startDate = "$startYear/$startMonth/$startDay"
        val endDate = "$endYear/$endMonth/$endDay"
        return EventTime(
            startYear,
            endYear,
            startMonth,
            startDay,
            endMonth,
            endDay,
            startTime,
            endTime,
            startDate,
            endDate,
            showStartTime,
            showEndTime
        )
    }

    fun pad(minute: Int): String {
        if (minute < 10) {
            return "0$minute"
        }

        return "$minute"
    }
}