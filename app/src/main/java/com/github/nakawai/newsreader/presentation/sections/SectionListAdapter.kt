package com.github.nakawai.newsreader.presentation.sections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.nakawai.newsreader.databinding.ListItemSectionBinding
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.presentation.translate

class SectionListAdapter(private val onItemClick: (section: Section) -> Unit) : ListAdapter<Section, SectionListAdapter.ViewHolder>(DIFF_CALLBACK) {


    class ViewHolder(var binding: ListItemSectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = ViewHolder(binding)
        binding.root.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition)
            onItemClick(item)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val section = getItem(position).translate()
        holder.binding.imageView.setImageResource(section.iconResId)
        holder.binding.text.text = section.label

    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem == newItem
    }

}
