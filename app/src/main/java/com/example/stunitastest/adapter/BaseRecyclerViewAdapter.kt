package com.example.stunitastest.adapter

import android.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.CompletableSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

abstract class BaseRecyclerViewAdapter<DATA, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(), RecyclerAdapterDiffCallback<DATA> {

    private val diffListData = DiffListData(this)

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        diffListData.onDetached()
    }

    override fun getItemCount(): Int = diffListData.getCount()

    fun getItem(position: Int): DATA? {
        return if (position < diffListData.getCount()) {
            diffListData.displayList[position]
        } else null
    }

    fun getItemList(): ArrayList<DATA> {
        return ArrayList(diffListData.displayList)
    }

    fun addItem(data: DATA) {
        getItemList().apply {
            add(data)
            update(this)
        }
    }

    fun removeItem(position: Int): DATA = getItemList().run {
        val removeTarget = removeAt(position)
        update(this)
        removeTarget
    }

    fun removeItem(data: DATA) {
        getItemList().run {
            remove(data)
            update(this)
        }
    }


    fun addAll(list: ArrayList<DATA>) {
        getItemList().run {
            addAll(list)
            update(this)
        }
    }

    fun update() {
        diffListData.update()
    }

    fun update(newList: ArrayList<DATA>) {
        diffListData.update(newList)
    }

    fun clear() {
        diffListData.update(ArrayList())
    }

    override fun onInserted(position: Int, count: Int) {
        notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        notifyItemRangeChanged(position, count, payload)
    }

    private inner class DiffListData constructor(private var diffCallback: RecyclerAdapterDiffCallback<DATA>?) {

        val disposableContainer = CompositeDisposable()
        val publishSubject = PublishSubject.create<ArrayList<DATA>>()

        val displayList= ArrayList<DATA>()

        init {
            val disposable = publishSubject
                .subscribeOn(Schedulers.computation())
                .doOnError { it.printStackTrace() }
                .flatMap { newList ->
                    val diffResult = calculateDiff(newList)

                    Observable.just<Pair<DiffUtil.DiffResult, ArrayList<DATA>>>(Pair.create(diffResult, newList))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable { pair ->

                    diffCallback?.let {
                        displayList.clear()
                        displayList.addAll(pair.second)
                        pair.second.clear()
                        pair.first.dispatchUpdatesTo(it)
                    }

                    CompletableSource { it.onComplete() }
                }.subscribe()
            disposableContainer.add(disposable)
        }

        @Synchronized
        private fun calculateDiff(newList: List<DATA>): DiffUtil.DiffResult {
            val oldList = ArrayList(displayList)

            return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return oldList.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = diffCallback?.let { diffCallback ->
                    takeIf { oldList[oldItemPosition] != null && newList[newItemPosition] != null }?.let {
                        diffCallback.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
                    } ?: false
                } ?: false

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = diffCallback?.let { diffCallback ->
                    takeIf { oldList[oldItemPosition] != null && newList[newItemPosition] != null }?.let {
                        diffCallback.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
                    } ?: false
                } ?: false
            })
        }

        fun getCount() = displayList.size

        fun update(list: ArrayList<DATA> = displayList) {
            publishSubject.onNext(ArrayList(list))
        }

        fun onDetached() {
            publishSubject.onComplete()
            disposableContainer.clear()
            diffCallback = null
        }
    }
}