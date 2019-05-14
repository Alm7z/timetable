package com.almazsh.timetable.ui.lessons_selecting.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.lessons_selecting_list_item.view.*

class LessonsSelectingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val checkBox = view.check_box_lessons_selecting
    val list = view.ll_lessons_selecting
    val textViews = list.getChilds().toList()

    init {
    }

    fun bind(data: Pair<LessonsSelectingListItemData, Boolean>) {
        if (data.first.lessonsList.size != textViews.size) throw Exception("lessonsList.size != textViews.size")

        for (i in 0 until data.first.lessonsList.size) {
            textViews[i].text = data.first.lessonsList[i]
        }

        checkBox.isChecked = data.second
    }
}

fun LinearLayout.getChilds(): Sequence<TextView> =
    sequence {
        for (i in 0 until this@getChilds.childCount) {
            yield(this@getChilds.getChildAt(i) as TextView)
        }
    }
