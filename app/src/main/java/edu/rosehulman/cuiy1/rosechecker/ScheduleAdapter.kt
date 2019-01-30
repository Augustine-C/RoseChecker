package edu.rosehulman.cuiy1.rosechecker

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.add_course_event.view.*
import kotlinx.android.synthetic.main.add_meeting_event.view.*
import java.util.*

class ScheduleAdapter(var context: Context?, var date: Date) : RecyclerView.Adapter<ScheduleViewHolder>() {

    var events = ArrayList<Event>()
    lateinit var registration: ListenerRegistration
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS_COLLECTION)

    fun addSnapshotListener() {
        Log.d("!!!", "add snapshotlistener ${events}")
//        PicListWrapper.picList = ArrayList()
        registration = eventsRef
            .addSnapshotListener { snapshot: QuerySnapshot?, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w("!!!", "Firebase Error: $firebaseFirestoreException")
                    return@addSnapshotListener
                }
                processSnapshotDiff(snapshot!!)
                Log.d("!!!", "get new snapshot ${events} ")
            }
//        Log.d("!!!", "add snapshotlistener ${PicListWrapper.picList} ${PicListWrapper.picList[0].id}")
    }
    fun removeSnapshotListener(){
        Log.d("!!!", "Remove snapshotlistener")
        registration.remove()
    }



    private fun processSnapshotDiff(snapshot: QuerySnapshot) {
        for (documentChange in snapshot.documentChanges) {
            val event = Event.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d("!!!", "ADDED")
                    if (event.timestamp.equals("${date.year}${date.month}${date.date}")) {
                        events.add(0, event)
                        notifyItemInserted(0)
                    }
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d("!!!", "REMOVE ${event.id}")
                    val pos = events.indexOfFirst { it.id == event.id }
                    if(pos != -1){
                        events.removeAt(pos)
                        notifyItemRemoved(pos)
                    }

                }
                DocumentChange.Type.MODIFIED -> {
                    val pos = events.indexOfFirst { it.id == event.id }
                    Log.d("!!!", "MODIFY" + pos.toString())
                    if(event.timestamp.equals("${date.year}${date.month}${date.date}")){
                        events[pos] = event
                        notifyItemChanged(pos)
                    } else {
                        events.removeAt(pos)
                        notifyItemRemoved(pos)
                    }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.event_holder, parent, false)
        return ScheduleViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun showEditDialog(position: Int) {

        val event = events[position]
        when (event.eventType) {
            Event.EventType.NormalEvent -> {

            }
            Event.EventType.MeetingEvent -> {
                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle("Edit a Meeting Event")
                val view = LayoutInflater.from(context).inflate(R.layout.add_meeting_event, null, false)
                view.title.setText(event.name)
                view.location.setText(event.location)
                view.meeting_agenda.setText(event.meetingInfo.get("meetingAgenda"))
                view.meeting_member.setText(event.meetingInfo.get("meetingMember"))
                builder.setView(view)
                builder.setPositiveButton(android.R.string.ok, { _, _ ->
                    val name = view.title.text.toString()
                    val location = view.location.text.toString()
                    val meeting_agenda = view.meeting_agenda.text.toString()
                    val meeting_member = view.meeting_member.text.toString()
                    editMeeting(position, name, location, meeting_agenda, meeting_member)
                })
                builder.setNeutralButton("Remove") { _, _ ->
                           remove(position)
                }
                builder.create().show()
            }
            Event.EventType.CourseEvent -> {
                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle("Edit a Coure Event")
                val view = LayoutInflater.from(context).inflate(R.layout.add_course_event, null, false)
                view.title.setText(event.name)
                view.location.setText(event.location)
                view.keycontent.setText(event.courseInfo.get("keyContent"))
                view.homework.setText(event.courseInfo.get("homeWork"))
                builder.setView(view)
                builder.setPositiveButton(android.R.string.ok, { _, _ ->
                    val name = view.title.text.toString()
                    val location = view.location.text.toString()
                    val keyContent = view.keycontent.text.toString()
                    val homework = view.homework.text.toString()
                    editCourse(position, name, location, keyContent, homework)
                })
                builder.setNeutralButton("Remove") { _, _ ->
                    remove(position)
                }
                builder.create().show()
            }
            else -> {
                true
            }
        }


    }

    fun editMeeting(position: Int, name: String, location: String, meeting_agenda: String, meeting_member: String) {
        val temp=events[position]
        temp.name=name
        temp.location=location
        temp.meetingInfo.replace("meetingAgenda",meeting_agenda)
        temp.meetingInfo.replace("meetingMember",meeting_member)
        eventsRef.document(events[position].id).set(temp)
    }

    fun editCourse(position: Int, name: String, location: String, keyContent: String, homework: String) {
        val temp=events[position]
        temp.name=name
        temp.location=location
        temp.courseInfo.replace("keyContent",keyContent)
        temp.courseInfo.replace("homeWork",homework)
        eventsRef.document(events[position].id).set(temp)
    }

    fun remove(position: Int) {
        eventsRef.document(events[position].id).delete()
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(events[position])
    }

}