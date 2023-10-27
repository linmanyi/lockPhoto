package com.nmk.myapplication.work.network.http

import com.google.gson.GsonBuilder
import com.nmk.myapplication.work.network.http.data.HttpConstance
import me.hgj.jetpackmvvm.network.BaseNetworkApi
import me.hgj.jetpackmvvm.network.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Api基类
 * @author Created by H-ray on 2023/1/3.
 */
abstract class BaseHttpApi : BaseNetworkApi() {

    abstract fun getInterceptorList(): List<Interceptor>

    fun <T> getApi(serviceClass: Class<T>): T {
        return getApi(serviceClass, HttpConstance.BaseUrl)
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val interceptorList = getInterceptorList()
        builder.apply {
            for (item in interceptorList) {
                addInterceptor(item)
            }
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(),
                SSLSocketFactoryUtils.createTrustAllManager())
        }
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }
}