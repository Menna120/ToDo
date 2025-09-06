package com.example.todo.ui.todo_list.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.database.model.Task
import com.example.todo.data.shared_prefs.SettingsSharedPreferences
import com.example.todo.databinding.ItemTaskBinding
import com.example.todo.utils.timeFormatter
import com.zerobranch.layout.SwipeLayout

class TasksAdapter(
    private val onTaskDoneClicked: (task: Task) -> Unit,
    private val onTaskDeleteSwipe: (task: Task) -> Unit,
    private val onTaskClicked: (task: Task) -> Unit,
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TaskViewHolder =
        TaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)

        holder.binding.markDoneButton.setOnClickListener { onTaskDoneClicked(task) }

        holder.binding.swipeLayout.currentDirection =
            if (AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag() == SettingsSharedPreferences.ENGLISH_LANGUAGE_CODE)
                SwipeLayout.RIGHT
            else SwipeLayout.LEFT
        holder.binding.swipeLayout.setOnActionsListener(object :
            SwipeLayout.SwipeActionsListener {
            override fun onOpen(direction: Int, isContinuous: Boolean) = onTaskDeleteSwipe(task)

            override fun onClose() {}
        })

        holder.binding.taskCard.setOnClickListener {
            onTaskClicked(task)
        }
    }

    class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context: Context = binding.root.context

        fun bind(task: Task) {
            binding.taskTitleText.text = task.title
            binding.taskTimeText.text =
                task.date.toLocalTime().format(timeFormatter(context))

            if (task.isDone) {
                binding.doneText.visibility = View.VISIBLE
                binding.markDoneButton.visibility = View.GONE
                binding.taskTitleText.paintFlags =
                    binding.taskTitleText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.doneText.visibility = View.GONE
                binding.markDoneButton.visibility = View.VISIBLE
                binding.taskTitleText.paintFlags =
                    binding.taskTitleText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            val primaryColor = TypedValue().apply {
                context.theme.resolveAttribute(
                    com.google.android.material.R.attr.colorPrimary,
                    this,
                    true
                )
            }.data
            val greenColor = context.getColor(R.color.green)

            val color = if (task.isDone) greenColor else primaryColor
            binding.taskTitleText.setTextColor(color)
            binding.divider.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
