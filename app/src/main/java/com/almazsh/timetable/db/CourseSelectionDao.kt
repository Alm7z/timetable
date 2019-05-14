package com.almazsh.timetable.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.almazsh.timetable.db.model.CourseSelection
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CourseSelectionDao {

    @Query("SELECT * FROM CourseSelection")
    fun getAll(): Single<List<CourseSelection>>

    @Insert
    fun insert(courseSelection: CourseSelection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<CourseSelection>)

    @Query("DELETE FROM CourseSelection")
    fun deleteAll()
}
