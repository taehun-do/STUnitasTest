package com.example.stunitastest.mvvm.view

import com.example.stunitastest.R
import com.example.stunitastest.adapter.ImageListAdapter
import com.example.stunitastest.databinding.ActivityMainBinding
import com.example.stunitastest.mvvm.viewmodel.MainViewModel
import io.reactivex.plugins.RxJavaPlugins

class MainActivity : DataBindingActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun initViewModel(viewDataBinding: ActivityMainBinding, vm: MainViewModel) {
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
        viewDataBinding.viewModel = vm
    }

    override fun initView() {
        viewDataBinding.recyclerView.adapter = ImageListAdapter()
    }
}
