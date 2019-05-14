package com.almazsh.timetable.ui.timetable

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout
import com.almazsh.timetable.MainGraphDirections
import com.almazsh.timetable.R
import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.logAlm
import com.almazsh.timetable.model.LessonInfo
import com.almazsh.timetable.ui.FragmentsHostActivity
import com.almazsh.timetable.ui.generating_views.DayView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.hunter.library.debug.HunterDebug
import kotlinx.android.synthetic.main.fragment_timetable.*

class TimetableFragment : MvpAppCompatFragment(), TimetableView {
    @InjectPresenter
    lateinit var presenter: TimetablePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity!! as AppCompatActivity).setSupportActionBar(toolbar_timetable)
        setHasOptionsMenu(true)

        presenter.loadTimetable()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.timetable_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_settings -> {
                logAlm("open Settings")

//                val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
                fragmentsHostActivity.navController.navigate(R.id.action_timetableFragment_to_myPreferencesFragment)
            }
        }
        return true
    }

    override fun showUpdating(show: Boolean) {
        logAlm("TimetableFragment: updating: $show")
    }


    @HunterDebug
    override fun showTimetable(lessons: List<LessonInfo>, selectedCourses: List<CourseSelection>?) {
//        logAlm("showTimetable: start")
        logAlm("TimetableFragment: lessons: ${lessons.toString()}")

        if (context == null) {
            logAlm("Context in null!!!")
            return
        }

        scroll_view_container.removeAllViews()

        if (lessons != null) {
            val lgs = lessons
                .groupBy { l -> l.rawDayName }

            lgs.forEach { lg ->
                val dayView = DayView.create(context!!, lg.value, selectedCourses)
//            val dayView = TextView(context)
//            dayView.text = "123"
                //TODO fix !!! Errors not showing in Logcat!!! (RxJava onError?)
//            dayView.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
//            dayView.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                dayView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                scroll_view_container.addView(dayView)
            }
        }

//        logAlm("showTimetable: end")
    }

    override fun showLessonsSelecting() {
        //TODO null?
        (activity as FragmentsHostActivity).navController.navigate(
            MainGraphDirections.actionGlobalLessonsSelectingFragment(
                true
            )
        )
    }

    val fragmentsHostActivity by lazy {
        (activity!! as FragmentsHostActivity)
    }
}
