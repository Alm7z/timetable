package com.almazsh.timetable.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.almazsh.timetable.db.model.CourseSelection

@Database(entities = [CourseSelection::class], version = 1)
abstract class MainDB : RoomDatabase() {
    abstract fun courseSelectionDao(): CourseSelectionDao
}
