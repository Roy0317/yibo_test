package com.yibo.yiboapp.data


import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("success")
    val success: Boolean
)