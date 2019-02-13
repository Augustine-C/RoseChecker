package edu.rosehulman.cuiy1.rosechecker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.helper.ItemTouchHelper


private const val ARG_YEAR = "year"
private const val ARG_DATE = "date"
private const val ARG_MONTH = "month"
private const val ARG_UID = "UID"

class ScheduleFragemnt : Fragment() {
    private var adapter: ScheduleAdapter? = null
    private var year: Int? = null
    private var month: Int? = null
    private var date: Int? = null
    private var uid: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            year = it.getInt(ARG_YEAR)
            month = it.getInt(ARG_MONTH)
            date = it.getInt(ARG_DATE)
            uid = it.getString(ARG_UID)
            adapter = ScheduleAdapter(activity, Date(year!!, month!!, date!!, 0, 0, 0), uid!!)
            adapter!!.addSnapshotListener()
        }

//        val alarmMgr = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val broadcastIntent = Intent(activity, AlarmBroadcastReceiver::class.java)
//        val pIntent = PendingIntent.getBroadcast(activity,0,broadcastIntent,0)
//        alarmMgr.set(
//            AlarmManager.RTC_WAKEUP,
//            Utils.upcomingEvent.startTime!!.toDate().time-1800000,
//            pIntent
//        )
//        Log.d(Constants.TAG,"alarm set")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recyclerView = inflater.inflate(R.layout.calender_content, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT
            ) { override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                return true
            }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter!!.remove(viewHolder.adapterPosition)
                }
            })
        mIth.attachToRecyclerView(recyclerView)
        return recyclerView
    }

    override fun onStop() {
        super.onStop()
        adapter?.removeSnapshotListener()
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
    interface OnDateChangeListener {
        fun onDateChange(time: Date, uid: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(time: Date, uid: String) =
            ScheduleFragemnt().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DATE, time.date)
                    putInt(ARG_YEAR, time.year)
                    putInt(ARG_MONTH, time.month)
                    putString(ARG_UID, uid)
                }
            }
    }
}

