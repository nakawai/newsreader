package com.github.nakawai.newsreader.presentation.search.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.github.nakawai.newsreader.databinding.ListItemSearchResultBinding
import com.github.nakawai.newsreader.domain.entities.StoryUrl

// ListView adapter class
class SearchResultAdapter(private val onItemClick: (StoryUrl) -> Unit) :
    ListAdapter<SearchResultUiModel, SearchResultAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(var binding: ListItemSearchResultBinding, val progressDrawable: CircularProgressDrawable) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val progressDrawable = CircularProgressDrawable(binding.root.context)
        progressDrawable.setStyle(CircularProgressDrawable.DEFAULT)
        progressDrawable.start()
        val viewHolder = ViewHolder(binding, progressDrawable)
        binding.root.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition)
            onItemClick(item.url)
        }


        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val uiModel = getItem(position)
        holder.binding.text.text = uiModel.title
        holder.binding.subTitle.text = uiModel.storyAbstract

    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResultUiModel>() {
    override fun areItemsTheSame(oldItem: SearchResultUiModel, newItem: SearchResultUiModel): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: SearchResultUiModel, newItem: SearchResultUiModel): Boolean {
        return oldItem == newItem
    }

}
