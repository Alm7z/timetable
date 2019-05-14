package com.almazsh.timetable.ui.timetable

import com.almazsh.timetable.App
import com.almazsh.timetable.AppPrefs
import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.logAlm
import com.almazsh.timetable.model.LessonInfo
import com.almazsh.timetable.model.states.TimetableState
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

@InjectViewState
class TimetablePresenter : MvpPresenter<TimetableView>() {

    init {

    }

    var timetableObserver: Disposable? = null

    fun loadTimetable() {
        val selectedCourses: Maybe<List<CourseSelection>> = if (AppPrefs.filterElectiveCourses) {
            App.dataManager.getCoursesSelection().cache().toMaybe()
        } else {
            Maybe.empty()
        }


        timetableObserver = App.dataManager.getTimetable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { state: TimetableState ->
                    viewState.showUpdating(state.updating)
                    when (state) {
                        is TimetableState.NotDownloadedYet -> {
                            logAlm("TimetableState.NotDownloadedYet")
                        }
                        is TimetableState.DownloadingError -> {
                            logAlm("TimetableState.DownloadingError")
                        }
                        is TimetableState.WithResponse -> {
                            when (state) {
                                is TimetableState.WithResponse.FromDb -> {
                                    logAlm("TimetableState.WithResponse.FromDb")
                                }
                                is TimetableState.WithResponse.FromNet -> {
                                    logAlm("TimetableState.WithResponse.FromNet")
                                }
                            }
                            selectedCourses
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeBy(
                                    onSuccess = { list ->
                                        // elective courses filter enabled
                                        if (!allElectiveCoursesSelected(state.response.lessons, list)) {
                                            viewState.showLessonsSelecting()
                                        } else {
                                            //TODO переделать?
                                            viewState.showTimetable(state.response.lessons, list)
                                        }
                                    },
                                    onComplete = {
                                        // elective courses filter disabled
                                        viewState.showTimetable(state.response.lessons, null)
                                    }
                                )
                        }
                    }
                },
                // TODO remove onError
                onError = {

                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

fun allElectiveCoursesSelected(lessons: List<LessonInfo>, selection: List<CourseSelection>): Boolean {
    for (lesson in lessons)
        if (lesson.lessonsGroup != null)
            for (lgi in lesson.lessonsGroup.items)
                if (!selection.any { it.id == lgi.id })
                    return false

    return true
}
