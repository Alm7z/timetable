package com.almazsh.timetable.network

import com.almazsh.timetable.model.GroupTimetableResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("groups/{groupName}")
    fun getGroupTimetable(@Path("groupName") groupName: String): Single<GroupTimetableResponse>

    @GET("groups")
    fun getGroupsList(): Single<List<String>>
}
