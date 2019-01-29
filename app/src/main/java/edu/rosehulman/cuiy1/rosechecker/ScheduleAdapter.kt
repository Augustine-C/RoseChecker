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
import java.text.DateFormat
import java.util.*

class ScheduleAdapter(var context: Context?) : RecyclerView.Adapter<ScheduleViewHolder>() {

    var events = ArrayList<Event>()
    lateinit var registration: ListenerRegistration
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS_COLLECTION)

    fun addSnapshotListener() {
        Log.d("!!!", "add snapshotlistener ${events}")
//        PicListWrapper.picList = ArrayList()
        registration = eventsRef
            .addSnapshotListener{ snapshot : QuerySnapshot?, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    Log.w("!!!", "Firebase Error: $firebaseFirestoreException")
                    return@addSnapshotListener
                }
                processSnapshotDiff(snapshot!!)
                Log.d("!!!", "get new snapshot ${events} ")
            }
//        Log.d("!!!", "add snapshotlistener ${PicListWrapper.picList} ${PicListWrapper.picList[0].id}")
    }

    private fun processSnapshotDiff(snapshot: QuerySnapshot) {
        for(documentChange in snapshot.documentChanges){
            val event = Event.fromSnapshot(documentChange.document)
            when (documentChange.type){
                DocumentChange.Type.ADDED -> {
                    Log.d("!!!", "ADDED")
                    events.add(0,event)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d("!!!", "REMOVE ${event.id}")
                    val pos = events.indexOfFirst { it.id == event.id }
                    events.removeAt(pos)
                    notifyItemRemoved(pos)
                }
                DocumentChange.Type.MODIFIED -> {
                    val pos = events.indexOfFirst { it.id == event.id }
                    Log.d("!!!","MODIFY"+pos.toString())
                    events[pos] = event
                    notifyItemChanged(pos)
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
        val builder = AlertDialog.Builder(this.context!!)
        //configure build: title icon message or custom view or list. Buttons (pos, neg, neutral)
        builder.setTitle("Edit a dialog")
        val view = LayoutInflater.from(context).inflate(R.layout.add_course_event, null, false)
        builder.setView(view)
        val event = events[position]
        when (event.javaClass) {

            else -> {
                true
            }
        }

        builder.setPositiveButton(android.R.string.ok, { _, _ ->
            // edit(position,caption,url)
        })
        builder.setNeutralButton("Remove") { _, _ ->
            //            remove(position)
        }
        builder.create().show()
    }

    fun add(event: Event) {
        eventsRef.add(event)
    }

//    fun edit(position: Int, name: String, location: String, startTime: String, endTime: String) {
//        val event = events[position]
//        event.name = name
//        event.location = location
//        event.startTime = startTime
//        event.endTime = endTime
//        eventsRef.document(events[position].id).set(event)
//    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(events[position])
    }

}