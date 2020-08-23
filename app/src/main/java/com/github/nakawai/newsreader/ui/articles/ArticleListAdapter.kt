package com.github.nakawai.newsreader.ui.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ListItemBinding
import com.github.nakawai.newsreader.model.entity.Article

// ListView adapter class
class ArticleListAdapter(private val listener: OnItemClickListener) : ListAdapter<Article, ArticleListAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(story: Article)
    }

    class ViewHolder(var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

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
        holder.binding.text.text = story.title
        holder.binding.subTitle.text = story.storyAbstract

        val context = holder.binding.root.context
        val readColor = ContextCompat.getColor(context, android.R.color.darker_gray)
        val unreadColor = ContextCompat.getColor(context, android.R.color.primary_text_light)
        holder.binding.text.setTextColor(if (story!!.isRead) readColor else unreadColor)

        if (story.multimedia.isNotEmpty()) {
            Glide.with(holder.binding.root)
                .load(story.multimedia[0].url)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(holder.binding.imageView)
        } else {
            Glide.with(holder.binding.root)
                .load(R.drawable.placeholder)
                .into(holder.binding.imageView)

        }

    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

}
