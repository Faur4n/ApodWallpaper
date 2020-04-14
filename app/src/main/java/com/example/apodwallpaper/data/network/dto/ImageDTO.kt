package com.example.apodwallpaper.data.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageDTO (
    @SerializedName("title")
    val title : String,
    @SerializedName("url")
    val url : String,
    @SerializedName("explanation")
    val explanation : String,
    @SerializedName("date")
    val date : String,
    @SerializedName("media_type")
    val mediaType : String,
    @SerializedName("hdurl")
    val hdurl : String
) : Parcelable {
}