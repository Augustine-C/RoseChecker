package edu.rosehulman.cuiy1.rosechecker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_course_event.view.*
import kotlinx.android.synthetic.main.add_meeting_event.view.*
import kotlinx.android.synthetic.main.choose_event_type.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.add_course_event.*
import kotlin.math.min


//Augustine and tiger
class MainActivity : AppCompatActivity()
    , LoginFragment.OnLoginListener, NavigationView.OnNavigationItemSelectedListener
    , ScheduleFragemnt.OnDateChangeListener {


    private val eventsRef = FirebaseFirestore.getInstance().collection(Constants.EVENTS_COLLECTION)
    private lateinit var currentTime: Date
    lateinit var calendarDate: Date
    var starting = Calendar.getInstance().time
    var ending = Calendar.getInstance().time

//    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentTime = Calendar.getInstance().time
        calendarDate = currentTime.clone() as Date
        val toggle = ActionBarDrawerToggle(
            this, main_content, toolbar, R.string.open, R.string.close
        )
        left_button.setOnClickListener {
            calendarDate.date = calendarDate.date - 1
            onDateChange(calendarDate)
        }
        right_button.setOnClickListener {
            calendarDate.date = calendarDate.date + 1
            onDateChange(calendarDate)
        }
        calendar_button.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                calendarDate = Date(year - 1900, month, dayOfMonth)
                onDateChange(calendarDate)
            }
            datePickerDialog.show()
        }
        main_content.addDrawerListener(toggle)
        toggle.syncState()
        nav_bar.setNavigationItemSelectedListener(this)
        fab.hide()
        buttons.visibility = View.GONE
        toolbar.visibility = View.GONE
        date_id.visibility = View.GONE
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_contianer, LoginFragment(), "login")
        ft.commit()
        fab.setOnClickListener {
            showChooseDialog()
        }

        val date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
        Log.d("!!!", df.format(date) + date.time)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun OnLoginListener() {
        fab.show()
        buttons.visibility = View.VISIBLE
        date_id.visibility = View.VISIBLE
        date_id.text = String.format("%s/%s/%s ", currentTime.year, currentTime.month, currentTime.date)
        onDateChange(currentTime)
    }

    fun showChooseDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a type of event")
        val view = LayoutInflater.from(this).inflate(R.layout.choose_event_type, null, false)
        builder.setView(view)
        val bu = builder.create()
        view.choose_normalEvent.setOnClickListener { showAddCourseDialog(bu) }
        view.choose_courseEvent.setOnClickListener { showAddCourseDialog(bu) }
        view.choose_meetingEvent.setOnClickListener { showAddMeetingDialog(bu) }
        bu.show()
    }

    override fun onDateChange(time: Date) {
        date_id.text = String.format("%s/%s/%s ", calendarDate.year + 1900, calendarDate.month + 1, calendarDate.date)
        fab.show()
        buttons.visibility = View.VISIBLE
        date_id.visibility = View.VISIBLE
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_contianer, ScheduleFragemnt.newInstance(time), "schedule")
        ft.addToBackStack("list")
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.color -> {
                Log.d("!!!", "color selected")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("click_test")
                builder.create().show()
            }
        }
        main_content.closeDrawer(GravityCompat.START)
        return true
    }

    fun showAddCourseDialog(chooseBuilder: AlertDialog) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add an event")
        val view = LayoutInflater.from(this).inflate(R.layout.add_course_event, null, false)
        var start = ""
        val calendar = Calendar.getInstance()
        val startingDate = calendar.time.clone() as Date


        view.startDate.setOnClickListener {

            val datePiker = DatePickerDialog(this)
            datePiker.setOnDateSetListener { _, year, month, day ->
                (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                start = String.format("%s/%s/%s ", year, month + 1, day)
                starting.year = year - 1900
                starting.month = month
                starting.date = day

                view.startDate.text = start
            }
            datePiker.show()

        }

        view.endDate.setOnClickListener {

            val datePiker = DatePickerDialog(this)
            datePiker.setOnDateSetListener { _, year, month, day ->
                (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                start = String.format("%s/%s/%s ", year, month + 1, day)
                ending.year = year - 1900
                ending.month = month
                ending.date = day
                view.endDate.text = start
            }
            datePiker.show()
        }

        view.startTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.startTime.text = "$hourOfDay : $minute"
                starting.hours = hourOfDay
                starting.minutes = minute
                starting.seconds = 0
            }, currentTime.hours, currentTime.minutes, true)
            timePicker.show()
        }
        view.endTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.endTime.text = "$hourOfDay : $minute"
                ending.hours = hourOfDay
                ending.minutes = minute
                ending.seconds = 0
            }, currentTime.hours, currentTime.minutes, true)
            timePicker.show()
        }

        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val name = view.title.text.toString()
            val location = view.location.text.toString()
            val keyContent = view.keycontent.text.toString()
            val homeWork = view.homework.text.toString()
            Log.d("DATE","start time: ${starting.year} / ${starting.month} / ${starting.day} ")
            val timeStamp = "${starting.year}${starting.month}${starting.date}"
            val event =
                Event(name, location,
                    timeStamp, Timestamp(starting), Timestamp(ending), false, 0, Event.EventType.CourseEvent)
            event.courseInfo.put("keyContent", keyContent)
            event.courseInfo.put("homeWork", homeWork)
            eventsRef.add(event)
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
        chooseBuilder.hide()

    }

    fun showAddMeetingDialog(chooseBuilder: AlertDialog) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add an event")
        val view = LayoutInflater.from(this).inflate(R.layout.add_meeting_event, null, false)
        var start = ""
        val calendar = Calendar.getInstance()
        val startingDate = calendar.time.clone() as Date



        view.meetingStartDate.setOnClickListener {

            val datePiker = DatePickerDialog(this)
            datePiker.setOnDateSetListener { _, year, month, day ->
                (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                start = String.format("%s/%s/%s ", year, month + 1, day)
                starting.year = year - 1900
                starting.month = month
                starting.date = day
                view.meetingStartDate.text = start
            }
            datePiker.show()

        }

        view.meetingEndDate.setOnClickListener {

            val datePiker = DatePickerDialog(this)
            datePiker.setOnDateSetListener { _, year, month, day ->
                (Log.d("DATE", year.toString() + month.toString() + day.toString()))
                start = String.format("%s/%s/%s ", year, month + 1, day)
                ending.year = year - 1900
                ending.month = month
                ending.date = day
                view.meetingEndDate.text = start
            }
            datePiker.show()
        }

        view.meetingStartTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.meetingStartTime.text = "$hourOfDay : $minute"
                starting.hours = hourOfDay
                starting.minutes = minute
                starting.seconds = 0
            }, currentTime.hours, currentTime.minutes, true)
            timePicker.show()
        }
        view.meetingEndTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.meetingEndTime.text = "$hourOfDay : $minute"
                ending.hours = hourOfDay
                ending.minutes = minute
                ending.seconds = 0
            }, currentTime.hours, currentTime.minutes, true)
            timePicker.show()
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val name = view.meeting_title.text.toString()
            val location = view.meeting_location.text.toString()
            val meetingAgenda = view.meeting_agenda.text.toString()
            val meetingMember = view.meeting_member.text.toString()
            val timeStamp = "${starting.year}${starting.month}${starting.date}"
            val event =
                Event(name, location,timeStamp, Timestamp(starting), Timestamp(ending), false, 0, Event.EventType.MeetingEvent)
            event.meetingInfo.put("meetingAgenda", meetingAgenda)
            event.meetingInfo.put("meetingMember", meetingMember)
            eventsRef.add(event)
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
        chooseBuilder.hide()

    }


}
