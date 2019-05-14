package com.almazsh.timetable.ui.lessons_selecting

import com.almazsh.timetable.ui.lessons_selecting.list.LessonsSelectingListItemData
import com.arellomobile.mvp.MvpView

interface LessonsSelectingView : MvpView {
    fun showUpdating(show: Boolean)
    fun showList(groupedLessons: List<LessonsSelectingListItemData>)
    fun closeFragment()
}
