package com.almazsh.timetable.ui.group_selecting

import com.almazsh.timetable.App
import com.almazsh.timetable.AppPrefs
import com.almazsh.timetable.logAlm
import com.almazsh.timetable.model.states.GroupsListState
import com.almazsh.timetable.topicOfGroup
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

@InjectViewState
class GroupSelectingPresenter : MvpPresenter<GroupSelectingView>() {

    var groupsListObserver: Disposable? = null

    fun loadGroups() {
        groupsListObserver = App.dataManager.getGroups()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { state: GroupsListState ->
                    viewState.showUpdating(state.updating)
                    when (state) {
                        is GroupsListState.NotDownloadedYet -> {
                            logAlm("GroupsListState.NotDownloadedYet")
                        }
                        is GroupsListState.DownloadingError -> {
                            logAlm("GroupsListState.DownloadingError")
                        }
                        is GroupsListState.Dowloaded -> {
                            logAlm("GroupsListState.Dowloaded")
                            viewState.showGroupsList(state.groupsList)
                        }
                    }
                }
//                onError = {
//                    throw it
//                }
            )
    }

    fun selectGroup(groupName: String) {
        logAlm("selectGroup $groupName")

        AppPrefs.groupName = groupName

        FirebaseMessaging.getInstance().subscribeToTopic(topicOfGroup(groupName))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewState.goToCoursesSelection()
                } else {
                    viewState.showFirebaseError()
                }
            }

    }
}
