package com.example.apodwallpaper.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor private constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithoutApiKey = chain.request()
        val url = requestWithoutApiKey.url()
            .newBuilder()
            .addQueryParameter(API_KEY_QUERY_NAME, API_KEY)
            //.addQueryParameter(DATE_QUERY_NAME, DATE)
            .build()
        val requestWithAttachedApiKey = requestWithoutApiKey.newBuilder()
            .url(url)
            .build()
        return chain.proceed(requestWithAttachedApiKey)
    }

    companion object {
        private const val API_KEY = "Lz20yu6rklgYMgl6SV04IL9PApqrLn59Izvfyanh"
        private const val API_KEY_QUERY_NAME = "api_key"
        private const val DATE_QUERY_NAME = "date"
        private const val DATE = "2020-01-01"


        fun create(): ApiKeyInterceptor {
            return ApiKeyInterceptor()
        }
    }
}