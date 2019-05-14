package com.almazsh.timetable.model.states

interface GroupsListState {
    val updating: Boolean

    data class NotDownloadedYet(
        override val updating: Boolean
    ) : GroupsListState

    data class DownloadingError(
        override val updating: Boolean
    ) : GroupsListState

    data class Dowloaded(
        val groupsList: List<String>,
        override val updating: Boolean = false
    ) : GroupsListState

}
