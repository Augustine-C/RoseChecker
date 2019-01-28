package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable
import java.text.DateFormat

class CourseEvent(
    name: String = " ",
    location: String = " ",
    startTime: String = " ",
    endTime: String = " ",
    isFinished: Boolean = false,
    importance: Int = 0,
    var keyContent: String = "",
    var homework: String = ""
//    var preparation: String
) : Event(name, location, startTime, endTime, isFinished, importance) {


}