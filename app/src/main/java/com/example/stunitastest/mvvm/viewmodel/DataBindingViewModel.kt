package com.example.stunitastest.mvvm.viewmodel

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

open class DataBindingViewModel : ViewModel() {
    private val disposableContainer = CompositeDisposable()

    val progressPublisher = PublishSubject.create<Boolean>().apply { bind() }
    val toastPublisher = PublishSubject.create<@StringRes Int>().apply { bind() }

    open fun initData(bundle: Bundle) {
    }

    fun <T> Single<T>.bind() {
        disposableContainer.add(this.subscribe())
    }

    fun <T> PublishSubject<T>.bind() {
        disposableContainer.add(this.subscribe())
    }

    fun addDisposable(disposable: Disposable) {
        disposableContainer.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposableContainer.dispose()
    }

    fun showToast(@StringRes stringResId: Int) {
        toastPublisher.onNext(stringResId)
    }

    fun showProgress() {
        progressPublisher.onNext(true)
    }

    fun dismissProgress() {
        progressPublisher.onNext(false)
    }
}