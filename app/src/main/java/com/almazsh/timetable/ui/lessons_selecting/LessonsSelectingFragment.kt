package com.almazsh.timetable.ui.lessons_selecting

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.almazsh.timetable.R
import com.almazsh.timetable.ui.lessons_selecting.list.LessonsSelectingAdapter
import com.almazsh.timetable.ui.lessons_selecting.list.LessonsSelectingListItemData
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_lessons_selecting.*

class LessonsSelectingFragment : MvpAppCompatFragment(), LessonsSelectingView {

    @InjectPresenter
    lateinit var presenter: LessonsSelectingPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lessons_selecting, container, false)
    }

    val safeArgs: LessonsSelectingFragmentArgs by navArgs()

    override fun onStart() {
        super.onStart()

        //TODO удалять те пары, которых больше нет в расписании ?
        // (или оставлять?)
        // (или отображать в отдельном списке?)

        val showOnlyNew = safeArgs.onlyNew
//        MainGraphDirections.actionGlobalLessonsSelectingFragment(true)

        if (showOnlyNew) {
            btn_skip_lessons_selecting.visibility = View.GONE
        }

        presenter.loadData(showOnlyNew)

        btn_confirn_lessons_selecting.setOnClickListener {
            presenter.saveAnswer(
                (rv_courses_selecting.adapter as LessonsSelectingAdapter)
                    .list
                    .map { Pair(it.first.id, it.second) }
            )
        }

        btn_skip_lessons_selecting.setOnClickListener {
            presenter.saveAnswer(null)
        }
    }

    override fun showUpdating(show: Boolean) {

    }

    override fun showList(groupedLessons: List<LessonsSelectingListItemData>) {
        rv_courses_selecting.apply {
            //            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = LessonsSelectingAdapter(
                context,
                groupedLessons.map { Pair(it, false) }.toTypedArray()
            )
        }
    }

    //TODO error if return to this fragment ?

    override fun closeFragment() {
        val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        navController.navigate(R.id.action_lessonsSelectingFragment_to_timetableFragment)
    }
}
