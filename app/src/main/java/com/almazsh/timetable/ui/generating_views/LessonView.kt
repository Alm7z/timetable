package com.almazsh.timetable.ui.generating_views

import android.content.Context
import android.view.View
import com.almazsh.timetable.R
import com.almazsh.timetable.model.LessonInfo
import kotlinx.android.synthetic.main.item_timetable_raw_lesson.view.*

object LessonView {
    fun create(context: Context, lessonInfo: LessonInfo): View {
        val view = View.inflate(context, R.layout.item_timetable_raw_lesson, null)
        view.tv_end_time.text = lessonInfo.parsedEndTime
        view.tv_start_time.text = lessonInfo.parsedStartTime
        view.tv_raw_lesson.text = lessonInfo.rawLesson
        return view
    }
}
