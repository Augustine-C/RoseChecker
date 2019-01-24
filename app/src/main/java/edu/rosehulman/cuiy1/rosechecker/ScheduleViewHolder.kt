package edu.rosehulman.cuiy1.rosechecker

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.event_holder.view.*

class ScheduleViewHolder(itemView:View, val adapter: ScheduleAdapter):RecyclerView.ViewHolder(itemView) {
    init {
        //TODO Set On Edit Listener
        itemView.setOnClickListener{
            adapter.selectEvent(adapterPosition)
        }
    }

    fun bind(event: Event){
        itemView.eventName.text = event.name
        itemView.event_time.text = event.time.toString()
        itemView.event_location.text = event.location
    }

    fun processTime(time : Int) : String{
        //TODO to be implemented
        return ""
    }
}