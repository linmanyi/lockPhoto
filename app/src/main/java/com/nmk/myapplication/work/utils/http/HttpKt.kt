package com.nmk.myapplication.work.utils.http

import android.net.ParseException
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.nmk.myapplication.work.base.BaseViewModel
import com.nmk.myapplication.work.utils.common.LogUtil
import com.nmk.myapplication.work.utils.common.LogUtil.TAG_HTTP
import com.nmk.myapplication.work.utils.common.LogUtil.VALUE_FORMAT
import com.nmk.myapplication.work.utils.http.data.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
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
                LogUtil.http(String.format(VALUE_FORMAT, TAG_HTTP, "HttpApiError：${e.message}"))
                //打印错误栈信息
                e.printStackTrace()
                //失败回调
                error(ExceptionHandle.handleException(e))
            }
        }.onFailure {
            //记录错误日志
            LogUtil.http(String.format(VALUE_FORMAT, TAG_HTTP, "HttpApiError：${it.message}, block=${block}"))
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

/**
 * 请求结果过滤，判断请求服务器请求结果是否成功，不成功则会抛出异常
 */
suspend fun <T> executeResponse(
    response: BaseResponse<T>,
    success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        when {
            response.isSucces() -> {
                success(response.getResponseData())
            }
            else -> {
                throw AppException(
                    response.getResponseCode(),
                    response.getResponseMsg(),
                    response.getResponseMsg()
                )
            }
        }
    }
}

