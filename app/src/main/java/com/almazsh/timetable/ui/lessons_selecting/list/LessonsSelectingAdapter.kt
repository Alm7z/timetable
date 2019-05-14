package com.almazsh.timetable.ui.lessons_selecting.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.almazsh.timetable.ui.generating_views.lessons_selecting.LessonsSelectingGroup

class LessonsSelectingAdapter(
    val context: Context,
    val list: Array<Pair<LessonsSelectingListItemData, Boolean>>
) : RecyclerView.Adapter<LessonsSelectingViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int) = list[position].first.lessonsList.size

//    private fun onCheck(view:View)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LessonsSelectingViewHolder {
        val view = LessonsSelectingGroup.create(context, viewType)
        val holder = LessonsSelectingViewHolder(view)
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            list[holder.adapterPosition] = list[holder.adapterPosition].copy(second = isChecked)
        }
        return holder
    }

    override fun onBindViewHolder(holder: LessonsSelectingViewHolder, position: Int) {
        holder.bind(list[position])
    }

}
