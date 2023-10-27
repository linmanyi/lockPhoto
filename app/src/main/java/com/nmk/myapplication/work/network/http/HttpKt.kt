package com.hray.library.util.http

import android.net.ParseException
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.nmk.myapplication.work.network.http.Error
import com.nmk.myapplication.work.network.http.MyAppException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.executeResponse
import me.hgj.jetpackmvvm.network.AppException
import me.hgj.jetpackmvvm.network.BaseResponse
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import javax.net.ssl.SSLException

fun <T> BaseViewModel.sendHttp(
    block: suspend () -> BaseResponse<T>,
    success: (T) -> Unit,
    error: (MyAppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = ""
): Job {
    //如果需要弹窗 通知Activity/fragment弹窗
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            //网络请求成功 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            runCatching {
                //校验请求结果码是否正确，不正确会抛出异常走下面的onFailure
                executeResponse(it) { t ->
                    success(t)
                }
            }.onFailure { e ->
                //打印错误消息
//                LogUtil.http(String.format(VALUE_FORMAT, TAG_HTTP, "HttpApiError：${e.message}"))
                //打印错误栈信息
                e.printStackTrace()
                //失败回调
                error(ExceptionHandle.handleException(e))
            }
        }.onFailure {
            //记录错误日志
//            LogUtil.http(String.format(VALUE_FORMAT, TAG_HTTP, "HttpApiError：${it.message}, block=${block}"))
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            //打印错误栈信息
            it.printStackTrace()
            //失败回调
            error(ExceptionHandle.handleException(it))
        }
    }
}

object ExceptionHandle {

    fun handleException(e: Throwable?): MyAppException {
        val ex: MyAppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = MyAppException(Error.NETWORK_ERROR, e)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = MyAppException(Error.PARSE_ERROR, e)
                    return ex
                }
                is ConnectException -> {
                    ex = MyAppException(Error.NETWORK_ERROR, e)
                    return ex
                }
                is SSLException -> {
                    ex = MyAppException(Error.SSL_ERROR, e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = MyAppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = MyAppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }
                is AppException -> {
                    return MyAppException(errCode = it.errCode, error = it.errorMsg, errorLog = it.errorLog, throwable = it.throwable)
                }

                else -> {
                    ex = MyAppException(Error.UNKNOWN, e)
                    return ex
                }
            }
        }
        ex = MyAppException(Error.UNKNOWN, e)
        return ex
    }
}

