package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable

open class Event(var name:String,
                 var location : String,
                 var time: Int,
                 var duration: Int,
                 var isFinished: Boolean,
                 var importance: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeInt(time)
        parcel.writeInt(duration)
        parcel.writeByte(if (isFinished) 1 else 0)
        parcel.writeInt(importance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}