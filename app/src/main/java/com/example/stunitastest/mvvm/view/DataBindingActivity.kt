package com.example.stunitastest.mvvm.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.example.stunitastest.dialog.ProgressDialogFragment
import com.example.stunitastest.mvvm.viewmodel.DataBindingViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

abstract class DataBindingActivity<VDB : ViewDataBinding, VM : DataBindingViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: VDB
        private set

    lateinit var vm: VM

    abstract val layoutResId: Int

    private val toastThrottleTime = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView<VDB>(this, layoutResId).apply {
            lifecycleOwner = this@DataBindingActivity
        }
        vm = createViewModel()
        initViewModel(viewDataBinding, vm)
        vm.run {
            toastPublisher.throttleFirst(toastThrottleTime, TimeUnit.MILLISECONDS).subscribeOn(
                AndroidSchedulers.mainThread()).subscribe { messageRedId ->
                if (messageRedId > 0 && !isFinishing)
                    Toast.makeText(this@DataBindingActivity, messageRedId, Toast.LENGTH_SHORT).show()
            }

            progressPublisher.subscribeOn(AndroidSchedulers.mainThread()).subscribe { switch ->
                if (!isFinishing) {
                    when (switch) {
                        true -> ProgressDialogFragment.show(supportFragmentManager)
                        false -> ProgressDialogFragment.dismiss(supportFragmentManager)
                    }
                }
            }

            intent.extras?.run {
                initData(this)
            }
        }
        initView()
    }

    override fun finish() {
        ProgressDialogFragment.dismiss(supportFragmentManager)
        super.finish()
    }

    open fun createViewModel(): VM = ViewModelProviders.of(this).get(getViewModelClass())

    fun Disposable.bind() {
        vm.addDisposable(this)
    }

    abstract fun getViewModelClass(): Class<VM>

    abstract fun initViewModel(viewDataBinding: VDB, vm: VM)
    abstract fun initView()
}