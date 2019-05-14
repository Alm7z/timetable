package com.almazsh.timetable.prefs_model

import com.almazsh.timetable.model.GroupTimetableResponse
import java.util.*

data class SavedResponse(
    val response: GroupTimetableResponse,
    val date: Date
)
