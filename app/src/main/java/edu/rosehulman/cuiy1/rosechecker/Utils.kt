package edu.rosehulman.cuiy1.rosechecker

import android.util.Log
import com.google.firebase.Timestamp
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val MONTH = arrayListOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
    val WEEKDAY = arrayListOf<String>("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")
    var isAll = false
    var upcomingEvent : Event? = null
    var timer = Timer()

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
            startYear, endYear,
            startMonth, startDay,
            endMonth, endDay,
            startTime, endTime,
            startDate, endDate,
            showStartTime, showEndTime
        )
    }

    fun pad(minute: Int): String {
        if (minute < 10) {
            return "0$minute"
        }

        return "$minute"
    }

    fun convertFromImputStream(icsInput: InputStream): ArrayList<Event> {
        val events = arrayListOf<Event>()
        val dateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss")
        val reader = icsInput.bufferedReader()
        val iterator = reader.lines().iterator()
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.equals("BEGIN:VEVENT")) {
                val name = iterator.next().substring(8)
                val profname = iterator.next().substring(37)
//                        professor name
                val location = iterator.next().substring(9)
                var startTime = iterator.next().substring(8)
                if (startTime.length < 14) {
                    startTime = startTime + iterator.next()
                }
                Log.d(Constants.TAG, startTime)
                val startDate = dateFormat.parse(startTime)
                val timeStamp = "${startDate.year}${startDate.month}${startDate.date}"
                Log.d(Constants.TAG, timeStamp)
                var endTime = iterator.next().substring(6)
                if (endTime.length < 14) {
                    endTime = endTime + iterator.next()
                }
                val endDate = dateFormat.parse(endTime)
                val event = Event(
                    name,
                    location,
                    timeStamp,
                    Timestamp(startDate),
                    Timestamp(endDate),
                    false,
                    0,
                    Event.EventType.CourseEvent
                )
                event.courseInfo.put("keyContent", profname)
                event.courseInfo.put("source","online")
                events.add(
                    event
                )
                Log.d(Constants.TAG, "course added")
            }
        }
        Log.d("!!!!", "test:")
        return events
    }
}