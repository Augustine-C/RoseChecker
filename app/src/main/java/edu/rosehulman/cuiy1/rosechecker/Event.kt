package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable

open class Event(var name:String,
                 var location : String,
                 var time: Int,
                 var duration: Int,
                 var isFinished: Boolean,
                 var importance: Int) {

}