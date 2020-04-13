package com.example.apodwallpaper.data.network.dto

import com.google.gson.annotations.SerializedName

data class ImageDTO(
    @SerializedName("title")
    val title : String,
    @SerializedName("url")
    val url : String,
    @SerializedName("explanation")
    val explanation : String,
    @SerializedName("date")
    val date : String
) {
}