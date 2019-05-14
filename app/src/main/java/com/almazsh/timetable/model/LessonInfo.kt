package com.almazsh.timetable.model

data class LessonInfo(
    val lessonNum: Int,
    val rawDayName: String,
    val parsedDayNum: Int?,
    val rawTime: String,
    val parsedStartTime: String?,
    val parsedEndTime: String?,
    val rawLesson: String,
    val lessonMergeWidth: Int?,
    val lessonsGroup:LessonsGroup?
) {
}
