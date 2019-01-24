package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable
import java.text.DateFormat

open class Event(var name:String,
                 var location : String,
                 var startTime: DateFormat,
                 var endTime: DateFormat,
                 var duration: Int,
                 var isFinished: Boolean,
                 var importance: Int) {

}