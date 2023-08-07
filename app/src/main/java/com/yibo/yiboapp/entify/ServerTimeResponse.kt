package com.yibo.yiboapp.entify


import com.google.gson.annotations.SerializedName

data class ServerTimeResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("content")
    val content: Content,
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("success")
    val success: Boolean){

    data class Content(
        @SerializedName("ip")
        val ip: String,
        @SerializedName("location")
        val location: String,
        @SerializedName("serverTime")
        val serverTime: Long
    )
}

