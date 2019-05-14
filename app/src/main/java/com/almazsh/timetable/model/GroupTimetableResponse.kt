package com.almazsh.timetable.model

data class GroupTimetableResponse(
    val groupName:String,
    val lessons:List<LessonInfo>,
    val appInfo:ClientAppInfo
) {
}
