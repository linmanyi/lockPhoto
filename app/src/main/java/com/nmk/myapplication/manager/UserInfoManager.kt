package com.nmk.myapplication.manager

import android.app.Activity
import com.nmk.myapplication.utils.common.CacheUtil
import java.util.*

class UserInfoManager {
    companion object {
        @JvmStatic
        fun getInstance() = UserInfoManager.HolderInstance.instance
    }

    object HolderInstance {
        var instance = UserInfoManager()
    }

    private val passwordKey = "passwordKey"

    /**
     * 密码
     */
    var password = ""

    fun updatePassword(password: String) {
        CacheUtil.putString(passwordKey,password)
        this.password = password
    }

    fun getPassword() {
        password = CacheUtil.getString(passwordKey)
    }
}