package com.hray.library.util.http

import com.nmk.myapplication.work.network.http.BaseHttpApi
import okhttp3.Interceptor

/**
 *
 * @author Created by H-ray on 2022/12/22
 *
 * @desc : 网络请求框架基础封装
 *
 */
class NetworkApi : BaseHttpApi() {

    private var interceptorList = mutableListOf<Interceptor>()

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    fun setInterceptor(interceptor: Interceptor){
        if(!interceptorList.contains(interceptor)){
            interceptorList.add(interceptor)
        }
    }

    fun setInterceptor(list: List<Interceptor>){
        for(item in list){
            setInterceptor(item)
        }
    }

    override fun getInterceptorList(): List<Interceptor> {
        return interceptorList
    }

}