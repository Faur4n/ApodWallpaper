package com.example.apodwallpaper.data.network.endpoints

import com.example.apodwallpaper.data.network.DefaultResponse
import com.example.apodwallpaper.data.network.dto.ImageDTO
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageEndpoint {
    @GET("planetary/apod")
    fun image(): Single<Response<ImageDTO>>

    @GET("planetary/apod")
    fun imageByDate(@Query("date") date : String) : Single<Response<ImageDTO>>
}