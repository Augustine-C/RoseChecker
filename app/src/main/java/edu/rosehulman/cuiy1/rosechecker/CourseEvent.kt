package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable

data class CourseEvent(var event:Event,
                       var chapterCovered : String = "",
                       var keyContent : String = "",
                       var homework : String = "",
                       var preparation : String) : Event(event.name, event.location, event.time, event.duration,event.isFinished,event.importance){
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Event::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeParcelable(event, flags)
        parcel.writeString(chapterCovered)
        parcel.writeString(keyContent)
        parcel.writeString(homework)
        parcel.writeString(preparation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseEvent> {
        override fun createFromParcel(parcel: Parcel): CourseEvent {
            return CourseEvent(parcel)
        }

        override fun newArray(size: Int): Array<CourseEvent?> {
            return arrayOfNulls(size)
        }
    }

}