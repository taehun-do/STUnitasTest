package com.example.stunitastest.adapter

import androidx.recyclerview.widget.ListUpdateCallback

interface RecyclerAdapterDiffCallback<DATA> : ListUpdateCallback {
    fun areItemsTheSame(oldItem: DATA, newItem: DATA): Boolean

    fun areContentsTheSame(oldItem: DATA, newItem: DATA): Boolean
}