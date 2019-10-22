package com.example.stunitastest.mvvm.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.stunitastest.R
import com.example.stunitastest.adapter.ImageListAdapter
import com.example.stunitastest.data.ImageItem

class BindingAdapter {
    companion object {
        @BindingAdapter("bindList")
        @JvmStatic
        fun RecyclerView.bindList(list: ObservableArrayList<ImageItem>) {
            (adapter as? ImageListAdapter)?.run {
                update(ArrayList(list))
            }
        }

        @BindingAdapter("updateImage")
        @JvmStatic
        fun ImageView.updateImage(url: String) {
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_placeholder)
                .error(R.drawable.default_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    }
}
