package com.almazsh.timetable.ui.lessons_selecting

import android.annotation.SuppressLint
import com.almazsh.timetable.App
import com.almazsh.timetable.AppPrefs
import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.logAlm
import com.almazsh.timetable.model.states.TimetableState
import com.almazsh.timetable.ui.lessons_selecting.list.LessonsSelectingListItemData
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

@InjectViewState
class LessonsSelectingPresenter : MvpPresenter<LessonsSelectingView>() {

    @SuppressLint("CheckResult")
    fun loadData(showOnlyNew: Boolean) {

        App.dataManager.getTimetable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { state ->
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
                            // TODO обрабатывать те, которых уже нет в расписании

                            val allNewGroupedLessons = state.response
                                .lessons
                                .filter { it.lessonsGroup != null }
                                .map { it.lessonsGroup!! }
                                .flatMap { lessonsGroup ->
                                    lessonsGroup.items.map {
                                        Pair(lessonsGroup.groupName, it)
                                    }
                                }
                                .groupBy { it.second.id }
                                .map { entry ->
                                    LessonsSelectingListItemData(
                                        entry.key,
                                        entry.value
                                            .map { it.first + " " + it.second.raw }
                                            .distinct()
                                            .toList()
                                    )
                                }

                            if (showOnlyNew) {
                                App.dataManager.getCoursesSelection()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe { savedSelection: List<CourseSelection> ->

                                        val onlyNewGroupedLessons = allNewGroupedLessons.filter { new ->
                                            !savedSelection.any { saved ->
                                                new.id == saved.id
                                            }
                                        }
                                        viewState.showList(onlyNewGroupedLessons)
                                    }

                            } else {
                                viewState.showList(allNewGroupedLessons)
                            }
                        }
                    }
                },
                onError = {
                    throw it
                }
            )

    }

    @SuppressLint("CheckResult")
    fun saveAnswer(list: List<Pair<String, Boolean>>?) {
        AppPrefs.filterModeAnswered = true

        if (list == null) {
            AppPrefs.filterElectiveCourses = false
            viewState.closeFragment()
        } else {
            AppPrefs.filterElectiveCourses = true

            App.dataManager.saveCoursesSelection(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onComplete = {
                        viewState.closeFragment()
                    }
                )
        }
    }
}
