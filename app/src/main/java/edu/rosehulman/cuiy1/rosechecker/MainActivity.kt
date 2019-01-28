package edu.rosehulman.cuiy1.rosechecker

import android.support.v7.app.AppCompatActivity

import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_course_event.view.*
import kotlinx.android.synthetic.main.login.*

//Augustine and tiger
class MainActivity : AppCompatActivity()
    , LoginFragment.OnLoginListener {
    private val eventsRef = FirebaseFirestore.getInstance().collection(Constants.EVENTS_COLLECTION)


    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

//    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab.hide()
        buttons.visibility = View.GONE
        toolbar.visibility = View.GONE
        date_id.visibility = View.GONE
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_contianer, LoginFragment(), "login")

        ft.commit()
        fab.setOnClickListener {
            showAddDialog()
        }
        var event : Event
        event = CourseEvent()
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
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_contianer, ScheduleFragemnt(), "schedule")
        ft.addToBackStack("list")
        ft.commit()
    }

    fun showAddDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add an event")
        val view = LayoutInflater.from(this).inflate(R.layout.add_course_event, null, false)
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val name = view.title.text.toString()
            val location = view.location.text.toString()
            val startTime = view.startTime.text.toString()
            val endTime = view.endTime.text.toString()
            val keyContent = view.keycontent.text.toString()
            val homeWork = view.homework.text.toString()
            eventsRef.add(CourseEvent(name,location,startTime,endTime,false,0,keyContent,homeWork))
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()

    }


}
