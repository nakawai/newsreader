package com.github.nakawai.newsreader.ui.main

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.nakawai.newsreader.databinding.ListItemBinding
import com.github.nakawai.newsreader.model.entity.NYTimesStory

// ListView adapter class
class NewsListAdapter(private val listener: OnItemClickListener) : ListAdapter<NYTimesStory, NewsListAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(story: NYTimesStory)
    }

    class ViewHolder(var binding: ListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context))

        val viewHolder = ViewHolder(binding)
        binding.root.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition)
            listener.onItemClick(item)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val story = getItem(position)
        holder.binding.text.text = story?.title

        val context = holder.binding.root.context
        val readColor = ContextCompat.getColor(context, R.color.darker_gray)
        val unreadColor = ContextCompat.getColor(context, R.color.primary_text_light)
        holder.binding.text.setTextColor(if (story!!.isRead) readColor else unreadColor)
    }
}

private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<NYTimesStory>() {
    override fun areItemsTheSame(oldItem: NYTimesStory, newItem: NYTimesStory): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: NYTimesStory, newItem: NYTimesStory): Boolean {
        return oldItem.equals(newItem)
    }

}
