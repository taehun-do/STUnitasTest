package com.example.stunitastest.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.example.stunitastest.R

class ProgressDialogFragment : AppCompatDialogFragment() {

    companion object {
        private const val TAG_PROGRESS = "ProgressDialog"

        fun show(fragmentManager: FragmentManager,
                 cancelable: Boolean = false) {
            fragmentManager.apply {
                if (findFragmentByTag(TAG_PROGRESS) == null) {
                    ProgressDialogFragment().apply {
                        isCancelable = cancelable
                        show(fragmentManager, TAG_PROGRESS)
                    }
                }
            }
        }

        fun dismiss(fragmentManager: FragmentManager) {
            fragmentManager.apply {
                (findFragmentByTag(TAG_PROGRESS) as? ProgressDialogFragment)?.dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return ProgressDialog(activity!!)
    }


    inner class ProgressDialog(context: Context) : Dialog(context) {

        init {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.progress_dialog)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        override fun show() {
            super.show()
            findViewById<ProgressBar>(R.id.progress_bar)?.isIndeterminate = true
        }

        override fun dismiss() {
            super.dismiss()
            findViewById<ProgressBar>(R.id.progress_bar)?.isIndeterminate = false
        }

    }
}