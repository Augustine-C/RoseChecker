package edu.rosehulman.cuiy1.rosechecker

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.event_holder.view.*
import org.apache.log4j.lf5.util.Resource

class ScheduleViewHolder(itemView: View, val adapter: ScheduleAdapter) : RecyclerView.ViewHolder(itemView) {
    init {

        itemView.setOnClickListener {
            adapter.showEditDialog(adapterPosition)
        }
    }

    fun bind(event: Event) {
        val eventTime=Utils.timeStampToString(event.startTime!!, event.endTime!!)
        itemView.eventName.text = event.name
        itemView.event_time.text = "${eventTime.showStartTime} - ${eventTime.showEndTime}"
        itemView.event_datemonth.text=eventTime.startMonth
        itemView.event_dateday.text=eventTime.startDay
        itemView.event_location.text = event.location
        if(event.id == Utils.upcomingEvent.id){
            itemView.background = adapter.context!!.getDrawable(R.drawable.card_view_back)
        }
    }

    fun processTime(time: Int): String {
        //TODO to be implemented
        return ""
    }
}