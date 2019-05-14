package com.almazsh.timetable

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.model.LessonInfo
import com.almazsh.timetable.model.states.TimetableState
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.Maybe
import io.reactivex.rxkotlin.subscribeBy

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val selectedCourses: Maybe<List<CourseSelection>> = if (AppPrefs.filterElectiveCourses) {
            App.dataManager.getCoursesSelection().cache().toMaybe()
        } else {
            Maybe.empty()
        }

        val r = App.dataManager.readSavedResponseAsync().subscribeBy(
            onSuccess = { savedResponse ->
                App.dataManager.getTimetable().subscribeBy(
                    onNext = { state ->
                        when (state) {
                            is TimetableState.WithResponse.FromNet -> {
                                selectedCourses.subscribeBy(
                                    onSuccess = { list ->
                                        compareTimetables(savedResponse.response.lessons, state.response.lessons, list)
                                    },
                                    onComplete = {
                                        compareTimetables(savedResponse.response.lessons, state.response.lessons, null)

                                    }
                                )
                            }
                        }
                    }
                )
            }
        )

        super.onMessageReceived(message)
    }

    private fun compareTimetables(
        old: List<LessonInfo>,
        new: List<LessonInfo>,
        selected: List<CourseSelection>?
    ) {
        if (old.size != new.size) {
            showDefNotif()
            return
        }

        for (i in 0 until old.size) {
            if (old[i] != new[i]) {
                if (old[i].lessonsGroup == null || new[i].lessonsGroup == null || selected == null) {
                    // raw || courses filter disabled
                    showTNotif(old[i], new[i])
                } else {
                    // added new course id
                    if (!new[i].lessonsGroup!!.items.all { lgi ->
                            selected.any { it.id == lgi.id }
                        }) {
                        showTNotif(old[i], new[i])
                    }


                    if (new[i].lessonsGroup!!.items.any { lgi ->
                            val sel = selected.find { it.id == lgi.id } ?: return@any true
                            if (sel.selected) {
                                val oldLgi = old[i].lessonsGroup!!.items.find { it.id == lgi.id } ?: return@any true
                                if (oldLgi.raw != lgi.raw)
                                    return@any true
                            }
                            false
                        }) {
                        showTNotif(old[i], new[i])
                    }
                }
            }
        }
    }

    private fun showTNotif(old: LessonInfo, new: LessonInfo) {
        showNotif(new.rawDayName + " " + (new.parsedStartTime ?: new.rawTime))
    }

    private fun showDefNotif() {
        showNotif("")
    }

    fun showNotif(text: String) {
        val CHANNEL_ID = "c1"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Изменения в расписании"
            val descriptionText = "Все изменения в расписании"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Расписание изменено")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // TODO replacing
        val notificationId = 1

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    override fun onDeletedMessages() {
        // TODO
        super.onDeletedMessages()
    }
}
