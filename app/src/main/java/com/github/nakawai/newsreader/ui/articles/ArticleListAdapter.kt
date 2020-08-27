package com.github.nakawai.newsreader.ui.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ListItemArticleBinding
import com.github.nakawai.newsreader.domain.story.Story

// ListView adapter class
class ArticleListAdapter(private val listener: OnItemClickListener) : ListAdapter<Story, ArticleListAdapter.ViewHolder>(DIFF_CALLBACK) {


    interface OnItemClickListener {
        fun onItemClick(story: Story)
    }

    class ViewHolder(var binding: ListItemArticleBinding, val progressDrawable: CircularProgressDrawable) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val progressDrawable = CircularProgressDrawable(binding.root.context)
        progressDrawable.setStyle(CircularProgressDrawable.DEFAULT)
        progressDrawable.start()
        val viewHolder = ViewHolder(binding, progressDrawable)
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
                .placeholder(holder.progressDrawable)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(holder.binding.imageView)
        } else {
            Glide.with(holder.binding.root)
                .load(R.drawable.placeholder)
                .into(holder.binding.imageView)

        }

    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem == newItem
    }

}
