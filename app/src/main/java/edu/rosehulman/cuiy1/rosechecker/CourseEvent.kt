package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable
import java.text.DateFormat

class CourseEvent(name : String,
                       location : String,
                       startTime: DateFormat,
                       endTime: DateFormat,
                       duration:Int,
                       isFinished: Boolean,
                       importance: Int,
                       var chapterCovered : String = "",
                       var keyContent : String = "",
                       var homework : String = "",
                       var preparation : String) : Event(name, location, startTime, endTime, duration, isFinished, importance){

}