package com.almazsh.timetable.ui.prefs

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import com.almazsh.timetable.AppPrefs
import com.almazsh.timetable.MainGraphDirections
import com.almazsh.timetable.R
import com.almazsh.timetable.topicOfGroup
import com.almazsh.timetable.ui.FragmentsHostActivity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_prefs.*

class MyPreferencesFragment : PreferenceFragmentCompat() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        preferenceManager.sharedPreferencesName="main_prefs_1"
//    }

    // TODO presenter

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }

    override fun onStart() {
        super.onStart()

        fragmentsHostActivity.setSupportActionBar(toolbar_prefs)
        fragmentsHostActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        fragmentsHostActivity.supportActionBar!!.setHomeButtonEnabled(true)

        findPref(R.string.key_reselect_courses).setOnPreferenceClickListener { preference ->
            fragmentsHostActivity.navController.navigate(
                MainGraphDirections.actionGlobalLessonsSelectingFragment(
                    false
                )
            )
            return@setOnPreferenceClickListener true
        }

        val reselectGroupPref = findPref(R.string.key_reselect_group)

        // .orEmpty() ?
        reselectGroupPref.title = getString(R.string.group_, AppPrefs.groupName.orEmpty())
        reselectGroupPref.setOnPreferenceClickListener { preference ->
            //TODO show loading

            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicOfGroup(AppPrefs.groupName.orEmpty()))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AppPrefs.groupName = null
                        AppPrefs.filterModeAnswered = false

                        fragmentsHostActivity.navController.navigate(R.id.action_myPreferencesFragment_to_groupSelectingFragment)
                    } else {
                        Toast.makeText(context, "Firebase Error", Toast.LENGTH_SHORT).show()
                    }
                }

            true
        }

        findPref(R.string.key_crash).setOnPreferenceClickListener { preference ->
            throw Exception("Test Exception")
        }

        findPref(R.string.key_subscribe_test1).setOnPreferenceClickListener { preference ->
            FirebaseMessaging.getInstance().subscribeToTopic("test1")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "subscribe ok", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "subscribe error", Toast.LENGTH_SHORT).show()
                    }
                }
            true
        }

        findPref(R.string.key_unsubscribe_test1).setOnPreferenceClickListener { preference ->
            FirebaseMessaging.getInstance().unsubscribeFromTopic("test1")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "unsubscribe ok", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "unsubscribe error", Toast.LENGTH_SHORT).show()
                    }
                }

            true
        }

    }

    val fragmentsHostActivity by lazy {
        (activity!! as FragmentsHostActivity)
    }

    fun findPref(@StringRes key: Int) = findPreference(getString(key))
}
