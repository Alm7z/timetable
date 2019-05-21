package com.almazsh.timetable.ui.generating_views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.almazsh.timetable.AppPrefs
import com.almazsh.timetable.R
import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.model.LessonInfo
import kotlinx.android.synthetic.main.item_timetable_day.view.*

object DayView {
    fun create(
        context: Context,
        lessons: List<LessonInfo>,
        selectedCourses: List<CourseSelection>?
    ): View {
        val view = View.inflate(context, R.layout.item_timetable_day, null)
        view.tv_day_name.text = lessons[0].rawDayName

        val last = lessons.indexOfLast {
            mustShow(it, selectedCourses)
        }

        val first = lessons.indexOfFirst {
            mustShow(it, selectedCourses)
        }
        if (last != -1 && first != -1) {
            for ((i, lesson) in lessons.withIndex()) {

                if (AppPrefs.hideDayStart && i < first) {
                    continue
                }

                if (AppPrefs.hideAllEmptyLessons && !mustShow(lesson, selectedCourses)) {
                    continue
                }

                if (lesson.lessonsGroup == null) {
                    val lessonView = LessonView.create(context, lesson)
                    lessonView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    view.ll_lessons_list.addView(lessonView)
                } else {
                    val lessonView = LessonsGroupView.create(context, lesson, selectedCourses)
//                lessonView.layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
                    view.ll_lessons_list.addView(lessonView)
                }

                if (AppPrefs.hideDayEnd && i == last) {
                    break
                }

            }
        }
        return view
    }

    private fun mustShow(
        lessonInfo: LessonInfo,
        selectedCourses: List<CourseSelection>?
    ): Boolean {
        return !lessonInfo.rawLesson.isBlank() &&
                (lessonInfo.lessonsGroup == null ||
                        selectedCourses == null ||
                        lessonInfo.lessonsGroup.items.any { lessonGroupItem ->
                            selectedCourses.any { it.selected && it.id == lessonGroupItem.id }
                        })
    }

}
