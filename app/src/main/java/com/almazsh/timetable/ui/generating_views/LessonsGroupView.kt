package com.almazsh.timetable.ui.generating_views

import android.content.Context
import android.view.View
import com.almazsh.timetable.R
import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.model.LessonInfo
import kotlinx.android.synthetic.main.item_timetable_lessons_group.view.*
import java.lang.Exception

object LessonsGroupView {
    fun create(
        context: Context,
        lessonInfo: LessonInfo,
        selectedCourses: List<CourseSelection>?
    ): View {
        val view = View.inflate(context, R.layout.item_timetable_lessons_group, null)
        view.tv_end_time.text = lessonInfo.parsedEndTime
        view.tv_start_time.text = lessonInfo.parsedStartTime

        var visibleExists = false

        if (lessonInfo.lessonsGroup != null) {
            view.tv_lessons_group_name_start.text = lessonInfo.lessonsGroup.groupName
            for (lesson_item in lessonInfo.lessonsGroup.items) {
                val item_view = LessonsGroupItemView.create(context, lesson_item)

                if (selectedCourses != null) {
                    val selection = selectedCourses.find { it.id == lesson_item.id }
                    if (selection == null) throw Exception("selection==null")

                    item_view.visibility = if (selection.selected) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                    visibleExists = selection.selected || visibleExists
                }

                view.ll_lessons_group_lessons.addView(item_view)
            }
        }

        if(!visibleExists){
            //TODO hide all
        }

        return view
    }
}
