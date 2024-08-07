package com.nmk.myapplication.work.manager

import com.nmk.myapplication.work.utils.common.LogUtil
import com.nmk.myapplication.work.utils.http.ExceptionHandle
import com.nmk.myapplication.work.utils.http.MyAppException
import kotlinx.coroutines.*
import com.nmk.myapplication.work.utils.http.data.BaseResponse
import com.nmk.myapplication.work.utils.http.executeResponse

/**
 * @desc： 弹窗请求Http
 * @author： Created by H-ray on 2023/3/27.
 */
class HttpManager {

    private val listJob = mutableListOf<Job>()

    /**
     * 发起HTTP请求
     */
    fun <T> sendHttp(block: suspend () -> BaseResponse<T>,
                     success: (T) -> Unit,
                     error: (MyAppException) -> Unit = {},
                     isShowDialog: Boolean = false,
                     loadingMessage: String = ""): Job{
        //如果需要弹窗 通知Activity/fragment弹窗
        val job = CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                if (isShowDialog) LoadingDialogManager.getInstance().showLoadingDialog(loadingMessage, false, null)
                //请求体
                block()
            }.onSuccess {
                //网络请求成功 关闭弹窗
                if (isShowDialog) LoadingDialogManager.getInstance().hideLoadingDialog()
                runCatching {
                    //校验请求结果码是否正确，不正确会抛出异常走下面的onFailure
                    executeResponse(it) { t ->
                        withContext(Dispatchers.Main){
                            success(t)
                        }
                    }
                }.onFailure { e ->
                    //打印错误消息
                    LogUtil.http(String.format(LogUtil.VALUE_FORMAT, LogUtil.TAG_HTTP, "HttpApiError：${e.message}"))
                    //打印错误栈信息
                    e.printStackTrace()
                    //失败回调
                    withContext(Dispatchers.Main){
                        error(ExceptionHandle.handleException(e))
                    }
                }
            }.onFailure {
                //记录错误日志
                LogUtil.http(String.format(LogUtil.VALUE_FORMAT, LogUtil.TAG_HTTP, "HttpApiError：${it.message}"))
                //网络请求异常 关闭弹窗
                if (isShowDialog) LoadingDialogManager.getInstance().hideLoadingDialog()
                //打印错误栈信息
                it.printStackTrace()
                //失败回调
                withContext(Dispatchers.Main){
                    error(ExceptionHandle.handleException(it))
                }
            }
        }
        listJob.add(job)
        return job
    }

    /**
     * 销毁单个请求
     */
    @Synchronized
    fun cancelHttp(job: Job?){
        if(job == null || !job.isActive) return
        for(i in listJob.size - 1 downTo 0){
            if(job == listJob[i]){
                job.cancel()
                listJob.removeAt(i)
                return
            }
        }
        job.cancel()
    }

    /**
     * 销毁所有请求
     */
    @Synchronized
    fun cancelAllHttp(){
        for(i in listJob.size - 1 downTo 0){
            listJob[i].cancel()
            listJob.removeAt(i)
        }
    }

    /**
     * 当前是否存在正在请求的任务
     */
    fun isExitJob(): Boolean{
        val activeJob = listJob.find { it.isActive }
        return activeJob != null
    }

}