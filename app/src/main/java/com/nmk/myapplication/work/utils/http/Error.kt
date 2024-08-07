package com.nmk.myapplication.work.utils.http

import com.nmk.myapplication.R
import com.nmk.myapplication.app.MyApplication

enum class Error(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, MyApplication.mContext.getString(R.string.error_message_try_again)),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, MyApplication.mContext.getString(R.string.error_message_parse_error)),
    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, MyApplication.mContext.getString(R.string.error_message_network_error)),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, MyApplication.mContext.getString(R.string.error_message_ssl_error)),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, MyApplication.mContext.getString(R.string.error_message_timeout_error));

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}