package edu.rosehulman.cuiy1.rosechecker

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class ScheduleAdapter(var context:Context?
                      ,val listener: ScheduleFragemnt.OnEventSelectedListener
                       ):RecyclerView.Adapter<ScheduleViewHolder>() {
    var events=ArrayList<Event>()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ScheduleViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return events.size
    }
    fun selectEvent(position: Int){
        listener.onEventSelected(events[position])
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(events[position])
    }

}