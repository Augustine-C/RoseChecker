package edu.rosehulman.cuiy1.rosechecker

import android.content.IntentSender
import android.os.Parcel
import android.os.Parcelable

class MeetingEvent ( name : String,
                     location : String,
                     time:Int,
                     duration:Int,
                     isFinished: Boolean,
                     importance: Int,
                     var numPeople : Int,
                     var members : String = "",
                     var agenda: String = "") : Event(name, location, time, duration, isFinished,importance){


}