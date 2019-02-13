package edu.rosehulman.cuiy1.rosechecker

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

open class Event(
    var name: String = " ",
    var location: String = " ",
    var timestamp: String= " ",
    @ServerTimestamp var startTime: Timestamp? = null,
    @ServerTimestamp var endTime: Timestamp? = null,
    var isFinished: Boolean = false,
    var importance: Int = 0,
    var eventType: EventType = EventType.NormalEvent,
    var courseInfo: HashMap<String, String> = hashMapOf(),
    var meetingInfo: HashMap<String, String> = hashMapOf()


) {
    enum class EventType {
        NormalEvent,
        CourseEvent,
        MeetingEvent;
    }

    @get:Exclude
    var id = ""

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