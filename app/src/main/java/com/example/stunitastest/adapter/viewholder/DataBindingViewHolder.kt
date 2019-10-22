package com.example.stunitastest.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class DataBindingViewHolder<DB : ViewDataBinding>(
    @LayoutRes layoutResId: Int,
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) {

    val viewDataBinding: DB = DataBindingUtil.bind(itemView)!!

    protected fun bind(variableId: Int, data: Any) {
        viewDataBinding.setVariable(variableId, data)
        viewDataBinding.executePendingBindings()
    }

}