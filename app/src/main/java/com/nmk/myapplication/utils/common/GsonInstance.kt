package com.nmk.myapplication.utils.common

import com.google.gson.Gson

/**
 * @desc： Gson全局单例使用，节约内存开销
 * @author： Created by H-ray on 2023/7/21.
 */
object GsonInstance {

    private val gson by lazy { Gson() }

    @JvmStatic
    fun getInstance() = gson

}