package com.almazsh.timetable.ui.generating_views.lessons_selecting

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.almazsh.timetable.R
import kotlinx.android.synthetic.main.lessons_selecting_list_item.view.*

object LessonsSelectingGroup {
    fun create(context: Context, lessonsCount:Int): View {
        val view = View.inflate(context, R.layout.lessons_selecting_list_item, null)

        view.layoutParams=ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        for (i in 1..lessonsCount) {
//            val tView = View.inflate(context, R.layout.lessons_selecting_list_item, null)
            val tView = TextView(context)

            tView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            view.ll_lessons_selecting.addView(tView)
        }

        return view
    }
}
