package com.almazsh.timetable.ui.generating_views

import android.content.Context
import android.view.View
import com.almazsh.timetable.R
import com.almazsh.timetable.model.LessonsGroupItem
import kotlinx.android.synthetic.main.item_timetable_lessons_group_item.view.*

object LessonsGroupItemView {
    fun create(context: Context, lessonsGroupItem: LessonsGroupItem): View {
        val view = View.inflate(context, R.layout.item_timetable_lessons_group_item, null)
        view.tv_lessons_group_raw_lesson.text = lessonsGroupItem.raw
        return view
    }
}
