package com.almazsh.timetable.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class CourseSelection(
    @PrimaryKey var id: String,
    var selected: Boolean
) {
    constructor() : this("", false)
}
