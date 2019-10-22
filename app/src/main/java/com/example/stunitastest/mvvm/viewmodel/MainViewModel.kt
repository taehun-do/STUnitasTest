package com.example.stunitastest.mvvm.viewmodel

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stunitastest.R
import com.example.stunitastest.data.ImageItem
import com.example.stunitastest.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainViewModel : DataBindingViewModel() {

    private val requestPublisher = PublishSubject.create<String>().apply {
        bind()
        throttleLast(1, TimeUnit.SECONDS).subscribe { searchText ->
            isEnd = false
            requestSearchImage(searchText, 1)
        }
    }

    private var isLoading = false
    private var isEnd = false
    private var nextPage = 1
    private var searchText = ""

    val dataList = ObservableArrayList<ImageItem>()

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.run {
                val visibleItemCount = getChildCount()
                val totalItemCount = getItemCount()
                val firstVisibleItemPosition = findFirstVisibleItemPosition()

                if (!isLoading
                    && !isEnd
                    && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                ) {
                    requestSearchImage(searchText, nextPage)
                }
            }
        }
    }

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable) {
        }

        override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            requestPublisher.onNext(p0.toString())
        }
    }

    fun requestSearchImage(query: String, page: Int = 1) {
        if (isLoading || TextUtils.isEmpty(query.trim()))
            return

        ApiClient.getKakaoApi().getImages(query = query, page = page)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isLoading = true
                showProgress()
            }
            .doOnError {
                showToast(R.string.default_error)
            }
            .doOnSuccess {
                isEnd = it.meta.is_end
                if (page == 1) {
                    searchText = query
                    dataList.clear()
                }
                if (it.documents.isEmpty()) {
                    showToast(R.string.empty_search_result)
                } else {
                    dataList.addAll(it.documents)
                    nextPage++
                }
            }
            .doFinally {
                isLoading = false
                dismissProgress()
            }.bind()
    }

    override fun onCleared() {
        requestPublisher.onComplete()
        super.onCleared()
    }
}