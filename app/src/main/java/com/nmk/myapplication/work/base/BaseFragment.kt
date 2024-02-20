package com.nmk.myapplication.work.base

import android.content.Context
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import me.hgj.jetpackmvvm.base.fragment.BaseVmVbFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *
 * @author Created by H-ray on 2020/7/1
 *
 * @desc : 请完成类说明注释
 *
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewBinding> : BaseVmVbFragment<VM, DB>() {

    protected lateinit var mContext: Context
    var isShow = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    /**
     * Fragment被显示后调用(Tab切换也会回调)
     */
    open fun tagShowFragment() {

    }

    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    override fun showLoading(message: String) {

    }

    protected fun isFinish(): Boolean{
        return !isAdded || isDetached || isRemoving
    }

    private var delayJob: Job? = null
    protected fun delayBlock(delayTime: Long, block: () -> Unit = {}){
        delayJob = CoroutineScope(Dispatchers.IO).launch {
            delay(delayTime)
            withContext(Dispatchers.Main) {
                block()
            }
        }
    }

    override fun onDestroy() {
        delayJob?.run {
            cancel()
            delayJob = null
        }
        super.onDestroy()
    }

}