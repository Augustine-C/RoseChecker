package edu.rosehulman.cuiy1.rosechecker

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_course_event.view.*
import java.text.DateFormat
import java.util.*

class ScheduleAdapter(var context:Context?
                      ,val listener: ScheduleFragemnt.OnEventSelectedListener
                       ):RecyclerView.Adapter<ScheduleViewHolder>() {
    var events=ArrayList<Event>()
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS_COLLECTION)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.event_holder, parent, false)
        return ScheduleViewHolder(view,this)
    }

    override fun getItemCount(): Int {
        return events.size
    }
    fun selectEvent(position: Int){
        listener.onEventSelected(events[position])
    }
    fun showEditDialog(position: Int ){
        val builder= AlertDialog.Builder(this.context!!)
        //configure build: title icon message or custom view or list. Buttons (pos, neg, neutral)
        builder.setTitle("Edit a dialog")
        val view =LayoutInflater.from(context).inflate(R.layout.add_course_event,null,false)
        builder.setView(view)
        val event=events[position]
        when (event.javaClass){
            CourseEvent::class.java ->{
                view.view_event_type.text="CourseEvent"
                view.title.setText(event.name)
                view.location.setText(event.location)
//                view.startTime.setText(event.startTime.toString())
//                view.endTime.setText(event.endTime.toString())
                true
            }
            MeetingEvent::class.java ->{
                view.view_event_type.text="MeetingEvent"
                view.title.setText(event.name)
                view.location.setText(event.location)
//                view.startTime.setText(event.startTime.toString())
//                view.endTime.setText(event.endTime.toString())
                true
            }
            else ->{
                true
            }
        }

        builder.setPositiveButton(android.R.string.ok,{ _,_->

           // edit(position,caption,url)
        })
        builder.setNeutralButton("Remove"){ _, _ ->
//            remove(position)

        }
        builder.create().show()
    }
    fun add(event: Event){
        eventsRef.add(event)
    }
    fun edit(position: Int,name:String,location:String,startTime:DateFormat,endTime:DateFormat){
        val event=events[position]
        event.name=name
        event.location=location
        event.startTime=startTime
        event.endTime=endTime
        eventsRef.document(events[position].id).set(event)
    }
    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(events[position])
    }

}