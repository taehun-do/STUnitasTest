package com.example.stunitastest.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stunitastest.adapter.viewholder.ImageViewHolder
import com.example.stunitastest.data.ImageItem

class ImageListAdapter : BaseRecyclerViewAdapter<ImageItem, ImageViewHolder>() {

    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean = oldItem.image_url == newItem.image_url

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(recyclerView.context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}