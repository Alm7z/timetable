package com.almazsh.timetable.model

data class LessonsGroup(
    val groupName: String,
    val items: List<LessonsGroupItem>
) {
}
