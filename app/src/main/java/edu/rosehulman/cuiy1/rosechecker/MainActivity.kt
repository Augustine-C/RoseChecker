package edu.rosehulman.cuiy1.rosechecker

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_course_event.view.*
import kotlinx.android.synthetic.main.add_meeting_event.view.*
import kotlinx.android.synthetic.main.choose_event_type.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import edu.rosehulman.rosefire.Rosefire
import kotlinx.android.synthetic.main.add_course_event.*
import kotlin.math.min


//Augustine and tiger
class MainActivity : AppCompatActivity()
    , LoginFragment.OnLoginListener, NavigationView.OnNavigationItemSelectedListener
    , ScheduleFragemnt.OnDateChangeListener {


    private lateinit var eventsRef:CollectionReference
    private lateinit var currentTime: Date
    lateinit var calendarDate: Date
    var starting = Calendar.getInstance().time
    var ending = Calendar.getInstance().time
    val auth = FirebaseAuth.getInstance()
    lateinit var authListener: FirebaseAuth.AuthStateListener
//    private val RC_SIGN_IN = 1
    private val RC_ROSEFIRE_LOGIN = 1001
    private var uid:String=""

//    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentTime = Calendar.getInstance().time
        calendarDate = currentTime.clone() as Date
        initializeListeners()
        val toggle = ActionBarDrawerToggle(
            this, main_content, toolbar, R.string.open, R.string.close
        )
        left_button.setOnClickListener {
            calendarDate.date = calendarDate.date - 1
            onDateChange(calendarDate,uid!!)
        }
        right_button.setOnClickListener {
            calendarDate.date = calendarDate.date + 1
            onDateChange(calendarDate,uid!!)
        }
        calendar_button.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                calendarDate = Date(year - 1900, month, dayOfMonth)
                onDateChange(calendarDate,uid)
            }
            datePickerDialog.show()
        }
        main_content.addDrawerListener(toggle)
        toggle.syncState()
        nav_bar.setNavigationItemSelectedListener(this)
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
    fun switchToLoginFragment(){
        fab.hide()
        buttons.visibility = View.GONE
        toolbar.visibility = View.GONE
        date_id.visibility = View.GONE
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_contianer, LoginFragment(), "login")
        ft.commit()
    }
    override fun OnLoginListener() {
        onRosefireLogin()
    }
    fun swtichToSchduleFragment(uid:String){
        fab.show()
        buttons.visibility = View.VISIBLE
        date_id.visibility = View.VISIBLE
        date_id.text = String.format("%s/%s/%s ", currentTime.year, currentTime.month, currentTime.date)
        eventsRef=FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION).document(uid).collection(Constants.EVENTS_COLLECTION)
        onDateChange(currentTime,uid)
    }
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }
    fun initializeListeners(){
        authListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            Log.d(Constants.TAG, "In auth listener, User: $user")
            if (user != null) {
                Log.d(Constants.TAG, "UID: ${user.uid}")
                uid=user.uid
                swtichToSchduleFragment(uid)
            } else {
                switchToLoginFragment()
            }
        }
    }
    fun onRosefireLogin() {
        val signInIntent = Rosefire.getSignInIntent(this, getString(R.string.token))
        startActivityForResult(signInIntent,RC_ROSEFIRE_LOGIN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RC_ROSEFIRE_LOGIN) {
            val result = Rosefire.getSignInResultFromIntent(data)
            if (result.isSuccessful) {
                auth.signInWithCustomToken(result.token)
            } else {
                Log.d(Constants.TAG, "Rosefire failed")
            }
        }
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

    override fun onDateChange(time: Date,uid:String) {
        date_id.text = String.format("%s/%s/%s ", calendarDate.year + 1900, calendarDate.month + 1, calendarDate.date)
        fab.show()
        buttons.visibility = View.VISIBLE
        date_id.visibility = View.VISIBLE
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_contianer, ScheduleFragemnt.newInstance(time,uid!!), "schedule")
        ft.commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("!!!!!",item.itemId.toString())
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
                start = String.format("%s/%s/%s ", year, Utils.MONTH[month], day)
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
                start = String.format("%s/%s/%s ", year, Utils.MONTH[month], day)
                ending.year = year - 1900
                ending.month = month
                ending.date = day
                view.endDate.text = start
            }
            datePiker.show()
        }

        view.startTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.startTime.text = "$hourOfDay : ${Utils.pad(minute)}"
                starting.hours = hourOfDay
                starting.minutes = minute
                starting.seconds = 0
            }, currentTime.hours, currentTime.minutes, true)
            timePicker.show()
        }
        view.endTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.endTime.text = "$hourOfDay : ${Utils.pad(minute)}"
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
                start = String.format("%s/%s/%s ", year,Utils.MONTH[month], day)
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
                start = String.format("%s/%s/%s ", year, Utils.MONTH[month], day)
                ending.year = year - 1900
                ending.month = month
                ending.date = day
                view.meetingEndDate.text = start
            }
            datePiker.show()
        }

        view.meetingStartTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.meetingStartTime.text = "$hourOfDay : ${Utils.pad(minute)}"
                starting.hours = hourOfDay
                starting.minutes = minute
                starting.seconds = 0
            }, currentTime.hours, currentTime.minutes, true)
            timePicker.show()
        }
        view.meetingEndTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                view.meetingEndTime.text = "$hourOfDay : ${Utils.pad(minute)}"
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
