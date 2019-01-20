package edu.rosehulman.cuiy1.rosechecker

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ScheduleFragemnt: Fragment() {
    private var listener: OnClassSelectedListener? = null
    private var adapater: ScheduleAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recyclerView= inflater.inflate(R.layout.fragment_main, container, false) as RecyclerView
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter =ScheduleAdapter(activity)
        return recyclerView
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClassSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnAnimalSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnClassSelectedListener {
        fun OnClassSelectedListener(meetingEvent: MeetingEvent)
    }

}

