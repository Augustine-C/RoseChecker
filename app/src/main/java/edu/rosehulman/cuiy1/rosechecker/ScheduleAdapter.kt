package edu.rosehulman.cuiy1.rosechecker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.add_course_event.view.*
import kotlinx.android.synthetic.main.add_meeting_event.view.*
import java.util.*

class ScheduleAdapter(var context: Context?, var date: Date, var uid: String) :
    RecyclerView.Adapter<ScheduleViewHolder>() {

    var events = ArrayList<Event>()
    lateinit var registration: ListenerRegistration
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USERS_COLLECTION)
        .document(uid)
        .collection(Constants.EVENTS_COLLECTION)

    fun addSnapshotListener() {
        Log.d("!!!", "add snapshotlistener ${events} $uid")
        if (Utils.isAll) {
            registration = eventsRef
                .orderBy("startTime", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot: QuerySnapshot?, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.w("!!!", "Firebase Error: $firebaseFirestoreException")
                        return@addSnapshotListener
                    }
                    processSnapshotDiff(snapshot!!)
                    Log.d("!!!", "get new snapshot ${events} ")
                }
        } else {
            registration = eventsRef
                .orderBy("startTime", Query.Direction.DESCENDING)
                .whereEqualTo("timestamp", "${date.year}${date.month}${date.date}")
                .addSnapshotListener { snapshot: QuerySnapshot?, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.w("!!!", "Firebase Error: $firebaseFirestoreException")
                        return@addSnapshotListener
                    }
                    processSnapshotDiff(snapshot!!)
                    Log.d("!!!", "get new snapshot ${events} ")
                }
        }
    }

    fun removeSnapshotListener() {
        Log.d("!!!", "Remove snapshotlistener")
        registration.remove()
    }

    private fun processSnapshotDiff(snapshot: QuerySnapshot) {
        for (documentChange in snapshot.documentChanges) {
            val event = Event.fromSnapshot(documentChange.document)
//            if (event.id == Utils.upcomingEvent.id) {
//                event.importance = 10
//            }
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d("!!!", "ADDED")
//                    if (event.timestamp.equals("${date.year}${date.month}${date.date}")) {
                    events.add(event)
                    events.sortWith(compareBy { it.startTime })
                    val dest = events.indexOfFirst { it.id == event.id }
                    notifyItemInserted(dest)
//                    }
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d("!!!", "REMOVE ${event.id}")
                    val pos = events.indexOfFirst { it.id == event.id }
                    if (pos != -1) {
                        events.removeAt(pos)
                        notifyItemRemoved(pos)
                    }

                }
                DocumentChange.Type.MODIFIED -> {
                    val pos = events.indexOfFirst { it.id == event.id }
                    Log.d("!!!", "MODIFY" + pos.toString())
                    if (Utils.isAll) {
                        val temp = pos
                        events[pos] = event
                        notifyItemChanged(pos)
                        events.sortWith(compareBy { it.startTime })
                        val dest = events.indexOfFirst { it.id == event.id }
                        notifyItemMoved(temp, dest)
                    } else if (event.timestamp.equals("${date.year}${date.month}${date.date}")) {
                        val temp = pos
                        events[pos] = event
                        notifyItemChanged(pos)
                        events.sortWith(compareBy { it.startTime })
                        val dest = events.indexOfFirst { it.id == event.id }
                        notifyItemMoved(temp, dest)
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
        val eventTime = Utils.timeStampToString(event.startTime!!, event.endTime!!)
        var starting = event.startTime!!.toDate()
        var ending = event.endTime!!.toDate()
        var start = ""
        when (event.eventType) {
            Event.EventType.NormalEvent -> {

            }
            Event.EventType.MeetingEvent -> {
                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle("Edit a Meeting Event")
                val view = LayoutInflater.from(context).inflate(R.layout.add_meeting_event, null, false)
                view.meeting_title.setText(event.name)
                view.meeting_location.setText(event.location)
                view.meetingStartDate.text = eventTime.startDate
                view.meetingEndDate.text = eventTime.endDate
                view.meetingStartTime.text = eventTime.showStartTime
                view.meetingEndTime.text = eventTime.showEndTime
                view.meeting_agenda.setText(event.meetingInfo.get("meetingAgenda"))
                view.meeting_member.setText(event.meetingInfo.get("meetingMember"))
                view.meetingStartDate.setOnClickListener {
                    val datePiker = DatePickerDialog(context)
                    datePiker.updateDate(starting.year + 1900, starting.month, starting.date)
                    datePiker.setOnDateSetListener { _, year, month, day ->
                        (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                        start = String.format("%s/%s/%s ", year, Utils.MONTH[month], day)
                        starting.year = year - 1900
                        starting.month = month
                        starting.date = day
                        view.meetingStartDate.text = start
                    }
                    datePiker.show()

                }

                view.meetingEndDate.setOnClickListener {
                    val datePiker = DatePickerDialog(context)
                    datePiker.updateDate(ending.year + 1900, ending.month, ending.date)
                    datePiker.setOnDateSetListener { _, year, month, day ->
                        (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                        start = String.format("%s/%s/%s ", year, Utils.MONTH[month], day)
                        ending.year = year - 1900
                        ending.month = month
                        ending.date = day
                        view.meetingEndDate.text = start
                    }
                    datePiker.show()
                }

                view.meetingStartTime.setOnClickListener {
                    val timePicker =
                        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            view.meetingStartTime.text = "$hourOfDay : ${Utils.pad(minute)}"
                            starting.hours = hourOfDay
                            starting.minutes = minute
                            starting.seconds = 0
                        }, event.startTime!!.toDate().hours, event.startTime!!.toDate().hours, true)
                    timePicker.show()
                }
                view.meetingEndTime.setOnClickListener {
                    val timePicker =
                        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            view.meetingEndTime.text = "$hourOfDay : ${Utils.pad(minute)}"
                            ending.hours = hourOfDay
                            ending.minutes = minute
                            ending.seconds = 0
                        }, event.endTime!!.toDate().hours, event.endTime!!.toDate().minutes, true)
                    timePicker.show()
                }
                builder.setView(view)
                builder.setPositiveButton(android.R.string.ok) { _, _ ->
                    val name = view.meeting_title.text.toString()
                    val location = view.meeting_location.text.toString()
                    val meeting_agenda = view.meeting_agenda.text.toString()
                    val meeting_member = view.meeting_member.text.toString()
                    editMeeting(position, name, location, meeting_agenda, meeting_member, starting, ending)
                }
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
                view.startTime.text = eventTime.showStartTime
                view.startDate.text = eventTime.startDate
                view.endTime.text = eventTime.showEndTime
                view.endDate.text = eventTime.endDate
                view.homework.setText(event.courseInfo.get("homeWork"))
                view.startDate.setOnClickListener {

                    val datePiker = DatePickerDialog(context)
                    datePiker.updateDate(starting.year + 1900, starting.month, starting.date)
                    datePiker.setOnDateSetListener { _, year, month, day ->
                        (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                        start = String.format("%s/%s/%s ", year, Utils.MONTH[month], day)
                        starting.year = year - 1900
                        starting.month = month
                        starting.date = day
                        view.startDate.text = start
                    }
                    datePiker.show()

                }

                view.endDate.setOnClickListener {

                    val datePiker = DatePickerDialog(context)
                    datePiker.updateDate(ending.year + 1900, ending.month, ending.date)
                    datePiker.setOnDateSetListener { _, year, month, day ->
                        (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                        start = String.format("%s/%s/%s ", year, Utils.MONTH[month], day)
                        ending.year = year - 1900
                        ending.month = month
                        ending.date = day
                        view.endDate.text = start
                    }
                    datePiker.show()
                }

                view.startTime.setOnClickListener {
                    val timePicker =
                        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            view.startTime.text = "$hourOfDay : ${Utils.pad(minute)}"
                            starting.hours = hourOfDay
                            starting.minutes = minute
                            starting.seconds = 0
                        }, event.endTime!!.toDate().hours, event.endTime!!.toDate().minutes, true)
                    timePicker.show()
                }
                view.endTime.setOnClickListener {
                    val timePicker =
                        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            view.endTime.text = "$hourOfDay : ${Utils.pad(minute)}"
                            ending.hours = hourOfDay
                            ending.minutes = minute
                            ending.seconds = 0
                        }, event.endTime!!.toDate().hours, event.endTime!!.toDate().minutes, true)
                    timePicker.show()
                }
                builder.setView(view)
                builder.setPositiveButton(android.R.string.ok, { _, _ ->
                    val name = view.title.text.toString()
                    val location = view.location.text.toString()
                    val keyContent = view.keycontent.text.toString()
                    val homework = view.homework.text.toString()
                    editCourse(position, name, location, keyContent, homework, starting, ending)
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

    fun editMeeting(
        position: Int,
        name: String,
        location: String,
        meeting_agenda: String,
        meeting_member: String,
        starting: Date,
        ending: Date
    ) {
        val temp = events[position]
        temp.name = name
        temp.location = location
        temp.meetingInfo.replace("meetingAgenda", meeting_agenda)
        temp.meetingInfo.replace("meetingMember", meeting_member)
        temp.startTime = Timestamp(starting)
        temp.timestamp = "${starting.year}${starting.month}${starting.date}"
        temp.endTime = Timestamp(ending)
        eventsRef.document(events[position].id).set(temp)
    }

    fun editCourse(
        position: Int,
        name: String,
        location: String,
        keyContent: String,
        homework: String,
        starting: Date,
        ending: Date
    ) {
        val temp = events[position]
        temp.name = name
        temp.location = location
        temp.courseInfo.replace("keyContent", keyContent)
        temp.courseInfo.replace("homeWork", homework)
        temp.startTime = Timestamp(starting)
        temp.endTime = Timestamp(ending)
        temp.timestamp = "${starting.year}${starting.month}${starting.date}"
        eventsRef.document(events[position].id).set(temp)
    }

    fun remove(position: Int) {
        eventsRef.document(events[position].id).delete()
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(events[position])
    }

}