package com.nmk.myapplication.work.utils.http.annotation

/**
 * @desc： Retrofit Annotation，禁用加签加密拦截器
 * @author： Created by H-ray on 2023/11/10.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DisableEncryptInterceptor