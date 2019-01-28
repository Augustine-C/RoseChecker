package edu.rosehulman.cuiy1.rosechecker

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

open class Event(
    var name: String = " ",
    var location: String = " ",
    var startTime: String =" ",
    var endTime: String = " ",
    var isFinished: Boolean = false,
    var importance: Int = 0,
    var eventType : EventType = EventType.NomalEvent,
    var courseInfo: HashMap<String, String> = hashMapOf(),
    var meetingInfo : HashMap<String,String> = hashMapOf()


) {
    enum class EventType {
        NomalEvent,
        CourseEvent,
        MeetingEvent;
    }
    @get:Exclude var id = ""

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): Event {
            Log.d("!!!", snapshot.data.toString())
            val event = snapshot.toObject(Event::class.java)!!
            event.id = snapshot.id
            return event
        }
    }
    //use a map to store the infomation of the course
}