package com.almazsh.timetable.ui.group_selecting

import com.arellomobile.mvp.MvpView

interface GroupSelectingView : MvpView {
    fun showUpdating(updating: Boolean)
    fun showGroupsList(list: List<String>)
    fun goToCoursesSelection()
    fun showFirebaseError()
}
