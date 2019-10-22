package com.example.stunitastest.adapter.viewholder

import android.view.ViewGroup
import com.example.stunitastest.BR
import com.example.stunitastest.R
import com.example.stunitastest.data.ImageItem
import com.example.stunitastest.databinding.ListLayoutImageBinding

class ImageViewHolder(parent: ViewGroup
) : DataBindingViewHolder<ListLayoutImageBinding>(R.layout.list_layout_image, parent) {

    fun bind(data: ImageItem) {
        bind(BR.data, data)
    }
}