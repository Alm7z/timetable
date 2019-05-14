package com.almazsh.timetable.model.states

import com.almazsh.timetable.model.GroupTimetableResponse
import java.util.*

interface TimetableState {
    val updating: Boolean

    data class NotDownloadedYet(
        override val updating: Boolean
    ) : TimetableState

    data class DownloadingError(
        override val updating: Boolean
    ) : TimetableState

    interface WithResponse : TimetableState {
        val response: GroupTimetableResponse

        data class FromDb(
            override val updating: Boolean,
            override val response: GroupTimetableResponse,
            val updTime: Date
        ) : TimetableState.WithResponse

        data class FromNet(
            override val updating: Boolean,
            override val response: GroupTimetableResponse
        ) : TimetableState.WithResponse

    }

}

