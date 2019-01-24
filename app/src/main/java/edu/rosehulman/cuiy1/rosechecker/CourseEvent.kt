package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable

data class CourseEvent(var event:Event,
                       var chapterCovered : String = "",
                       var keyContent : String = "",
                       var homework : String = "",
                       var preparation : String) : Event(event.name, event.location, event.time, event.duration,event.isFinished,event.importance){

}