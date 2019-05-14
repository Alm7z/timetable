package com.almazsh.timetable.ui.timetable

import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.model.LessonInfo
import com.arellomobile.mvp.MvpView

interface TimetableView : MvpView {
    fun showUpdating(show: Boolean)
    // TODO strategy
    fun showTimetable(lessons: List<LessonInfo>, selectedCourses: List<CourseSelection>?)
    fun showLessonsSelecting()
}
