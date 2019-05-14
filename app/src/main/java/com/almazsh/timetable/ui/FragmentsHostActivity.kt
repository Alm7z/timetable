package com.almazsh.timetable.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.almazsh.timetable.AppPrefs
import com.almazsh.timetable.R
import com.hunter.library.debug.HunterDebug

class FragmentsHostActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragments_host)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.main_graph)

        graph.startDestination = when {
//            AppPrefs.groupName == null -> R.id.groupSelectingFragment
//            !AppPrefs.filterModeAnswered -> R.id.lessonsSelectingFragment
            !AppPrefs.filterModeAnswered -> R.id.groupSelectingFragment
            else -> R.id.timetableFragment
        }

        navController.graph = graph

//        if (AppPrefs.groupName != null) {
////            navController.navigate(R.id.action_groupSelectingFragment_to_timetableFragment)
//        }
    }

    @HunterDebug
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
