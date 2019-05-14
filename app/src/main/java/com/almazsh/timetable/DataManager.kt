package com.almazsh.timetable

import com.almazsh.timetable.db.model.CourseSelection
import com.almazsh.timetable.model.GroupTimetableResponse
import com.almazsh.timetable.model.states.GroupsListState
import com.almazsh.timetable.model.states.TimetableState
import com.almazsh.timetable.prefs_model.SavedResponse
import com.google.gson.Gson
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

class DataManager {
    // TODO singleton Observable, LiveData, ... ???

    public fun getTimetable(): Observable<TimetableState> {
        val res =
            Observable.create<TimetableState> { emitter: ObservableEmitter<TimetableState> ->
                emitter.onNext(TimetableState.NotDownloadedYet(true))

                val prefsReader = readSavedResponseAsync()
                    .subscribeBy(
                        onSuccess = {
                            emitter.onNext(
                                TimetableState.WithResponse.FromDb(true, it.response, it.date)
                            )
                        },
                        onComplete = {

                        }
                    )

                App.retrofitService.getGroupTimetable(AppPrefs.groupName!!).subscribeBy(
                    onSuccess = { response: GroupTimetableResponse ->
                        prefsReader.dispose()
                        writeSavedResponseAsync(SavedResponse(response, Date())).subscribe()
                        emitter.onNext(
                            TimetableState.WithResponse.FromNet(false, response)
                        )
                    },
                    onError = {
                        emitter.onNext(
                            TimetableState.DownloadingError(false)
                        )
//                        throw it
                    }
                )
            }
                .subscribeOn(Schedulers.io())

        return res
    }

    fun getGroups(): Observable<GroupsListState> {
        val res =
            Observable.create<GroupsListState> { emitter: ObservableEmitter<GroupsListState> ->
                emitter.onNext(GroupsListState.NotDownloadedYet(true))

                App.retrofitService.getGroupsList().subscribeBy(
                    onSuccess = { response: List<String> ->
                        emitter.onNext(
                            GroupsListState.Dowloaded(response, false)
                        )
                    },
                    onError = {
                        emitter.onNext(
                            GroupsListState.DownloadingError(false)
                        )
                    }
                )
            }
                .subscribeOn(Schedulers.io())

        return res
    }


    //TODO save null?
    fun saveCoursesSelection(list: List<Pair<String, Boolean>>): Completable {
        return Completable
            .fromAction {
                App.db.courseSelectionDao().insertAll(
                    list.map { CourseSelection(it.first, it.second) }
                )
            }
            .subscribeOn(Schedulers.io())
    }

    fun getCoursesSelection(): Single<List<CourseSelection>> {
        return App.db.courseSelectionDao()
            .getAll()
            .subscribeOn(Schedulers.io())
    }

    fun writeSavedResponseAsync(response: SavedResponse): Completable {
        // TODO убрать необходимость в .subscribe() ???
        return Completable
            .fromAction {
                val file = File(App.app.filesDir, SAVED_RESPONSE_FILENAME)
                val serializedResponse = Gson().toJson(response)
                file.writeText(serializedResponse)
//                file.createNewFile()
            }
            .subscribeOn(Schedulers.io())
    }

    public fun readSavedResponseAsync(): Maybe<SavedResponse> {
        return Maybe.create { emitter ->
            val file = File(App.app.filesDir, SAVED_RESPONSE_FILENAME)
            if (file.exists()) {
                val serializedResponse = file.readText()
                val response = Gson().fromJson(serializedResponse, SavedResponse::class.java)
                emitter.onSuccess(response)
            } else {
                emitter.onComplete()
            }
        }
    }

    companion object {
        const val SAVED_RESPONSE_FILENAME = "saved_response.json"
    }
}
