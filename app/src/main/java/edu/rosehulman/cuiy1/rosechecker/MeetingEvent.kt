package edu.rosehulman.cuiy1.rosechecker

import android.content.IntentSender
import android.os.Parcel
import android.os.Parcelable
import java.text.DateFormat

class MeetingEvent(
    name: String,
    location: String,
    startTime: DateFormat,
    endTime: DateFormat,
    duration: Int,
    isFinished: Boolean,
    importance: Int,
    var numPeople: Int,
    var members: String = "",
    var agenda: String = ""
) : Event(name, location, startTime, endTime, duration, isFinished, importance) {


}