package com.cristianwer.platform.network.interceptors

import okhttp3.logging.HttpLoggingInterceptor

object LoggingInterceptorFactory {
    fun create(isDebug: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}