package edu.rosehulman.cuiy1.rosechecker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*


private const val ARG_YEAR = "year"
private const val ARG_DATE = "date"
private const val ARG_MONTH = "month"

class ScheduleFragemnt : Fragment() {
    private var adapater: ScheduleAdapter? = null
    private var year: Int? = null
    private var month: Int? = null
    private var date: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            year = it.getInt(ARG_YEAR)
            month = it.getInt(ARG_MONTH)
            date = it.getInt(ARG_DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recyclerView = inflater.inflate(R.layout.calender_content, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapater = ScheduleAdapter(activity, Date(year!!, month!!, date!!,0,0,0))
        adapater!!.addSnapshotListener()
        recyclerView.adapter = adapater
        return recyclerView
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
        fun onDateChange(time: Date)
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
        fun newInstance(time: Date) =
            ScheduleFragemnt().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DATE, time.date)
                    putInt(ARG_YEAR, time.year)
                    putInt(ARG_MONTH, time.month)
                }
            }
    }
}

