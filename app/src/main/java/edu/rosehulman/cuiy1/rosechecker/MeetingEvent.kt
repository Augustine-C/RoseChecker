package edu.rosehulman.cuiy1.rosechecker

import android.os.Parcel
import android.os.Parcelable

class MeetingEvent ( var event:Event,
                     var numPeople : Int,
                     var members : String = "",
                     var agenda: String = "") : Event(event.name, event.location, event.time, event.duration,event.isFinished,event.importance){
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Event::class.java.classLoader),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeParcelable(event, flags)
        parcel.writeInt(numPeople)
        parcel.writeString(members)
        parcel.writeString(agenda)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MeetingEvent> {
        override fun createFromParcel(parcel: Parcel): MeetingEvent {
            return MeetingEvent(parcel)
        }

        override fun newArray(size: Int): Array<MeetingEvent?> {
            return arrayOfNulls(size)
        }
    }


}