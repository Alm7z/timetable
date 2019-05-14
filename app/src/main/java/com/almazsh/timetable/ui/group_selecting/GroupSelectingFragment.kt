package com.almazsh.timetable.ui.group_selecting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.almazsh.timetable.MainGraphDirections
import com.almazsh.timetable.R
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_group_selecting.*

class GroupSelectingFragment : MvpAppCompatFragment(), GroupSelectingView {

    @InjectPresenter
    lateinit var presenter: GroupSelectingPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_selecting, container, false)
    }

    override fun onStart() {
        super.onStart()

        presenter.loadGroups()

        btn_select_group.setOnClickListener {
            presenter.selectGroup(spinner_groups.selectedItem as String)

        }

    }


    override fun showUpdating(updating: Boolean) {
        if (updating) {
            progress_group_selecting_loading.visibility = View.VISIBLE
            group_select_group.visibility = View.INVISIBLE
        } else {
            progress_group_selecting_loading.visibility = View.INVISIBLE
            group_select_group.visibility = View.VISIBLE
        }
    }

    override fun showGroupsList(list: List<String>) {

        spinner_groups.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list)

    }

    override fun goToCoursesSelection() {

        val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

//            navController.navigate(R.id.action_groupSelectingFragment_to_timetableFragment)

//            navController.navigate(R.id.action_global_lessonsSelectingFragment)

        navController.navigate(MainGraphDirections.actionGlobalLessonsSelectingFragment(false))
    }

    override fun showFirebaseError() {
        Toast.makeText(context, "Firebase Error", Toast.LENGTH_SHORT).show()
    }
}
