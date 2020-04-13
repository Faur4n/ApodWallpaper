package com.example.apodwallpaper.data.network

import com.example.apodwallpaper.data.network.endpoints.ImageEndpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestAPI{


    private const val BASE_URL = "https://api.nasa.gov/"
    private const val TIMEOUT_IN_SECONDS = 2

    private var imageEndpoint: ImageEndpoint

    init {
        val client: OkHttpClient = buildClient()
        val retrofit : Retrofit = buildRetrofit(client)

        imageEndpoint = retrofit.create(ImageEndpoint::class.java)
    }

    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(ApiKeyInterceptor.create())
            .build()
    }

    fun getImage(): ImageEndpoint {
        return imageEndpoint
    }




}