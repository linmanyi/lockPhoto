package com.nmk.myapplication.work.manager

import android.app.Activity
import com.nmk.myapplication.work.utils.common.CacheUtil
import java.util.*

class UserInfoManager {
    companion object {
        @JvmStatic
        fun getInstance() = HolderInstance.instance
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

    /**
     * 是否已设置密码
     */
    fun isSettingPassword(): Boolean {
        getPassword()
        return password.isNotEmpty()
    }

    /**
     * 输入密码是否正确
     */
    fun passwordIsRight(string: String): Boolean = password == string
}