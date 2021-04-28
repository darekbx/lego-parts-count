package com.darekbx.legopartscount.repository

import okhttp3.Interceptor
import okhttp3.Response

abstract class HeaderInterceptor(
    private val name: String,
    private val valueProvider: () -> String
) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader(name, valueProvider())
            .build()

        return chain.proceed(newRequest)
    }
}
