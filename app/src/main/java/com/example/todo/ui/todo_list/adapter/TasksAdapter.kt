package com.example.todo.ui.todo_list.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.Task
import com.example.todo.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TasksAdapter(
    private val tasks: List<Task>,
    private val onDoneClick: (position: Int) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TaskViewHolder =
        TaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(
        holder: TaskViewHolder, position: Int
    ) = holder.bind(tasks[position], position, onDoneClick)

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context: Context = binding.root.context
        private val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        fun bind(task: Task, position: Int, onDoneClick: (position: Int) -> Unit) {
            binding.taskTitleText.text = task.title
            binding.taskTimeText.text = dateFormat.format(task.date)
            binding.markDoneButton.setOnClickListener { onDoneClick(position) }

            binding.doneText.visibility = if (task.isDone) View.VISIBLE else View.GONE
            binding.markDoneButton.visibility = if (task.isDone) View.GONE else View.VISIBLE

            val color = if (task.isDone) context.getColor(R.color.green) else TypedValue().apply {
                context.theme.resolveAttribute(
                    com.google.android.material.R.attr.colorPrimary,
                    this,
                    true
                )
            }.data
            binding.taskTitleText.setTextColor(color)
            binding.divider.backgroundTintList =
                if (task.isDone) ColorStateList.valueOf(color) else null
        }
    }
}
