package com.poorni.momatugallerychallenge.model

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    val photoData: List<Photo>
)

data class Photo(
    @SerializedName("download_url") val image: String,
    @SerializedName("author") val author: String
)