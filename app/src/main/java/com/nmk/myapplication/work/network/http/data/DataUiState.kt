package com.nmk.myapplication.work.network.http.data

/**
作者：Hayden
时间：2022/12/13 17:28
描述：接口返回单个实体或实体为null的数据统一回调
 */
data class DataUiState<T>(
    /**请求是否成功**/
    var isSuccess: Boolean = true,
    /**操作的对象**/
    var data: T? = null,
    /**请求失败的错误信息**/
    var errorMsg: String = "",
    /** 失败的code **/
    var errorCode: Int = 0
)