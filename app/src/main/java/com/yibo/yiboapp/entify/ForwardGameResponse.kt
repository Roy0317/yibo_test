package com.yibo.yiboapp.entify

data class ForwardGameResponse(
    val success: Boolean,
    val url: String?,
    val content: String?,
    val html: String?,
    val msg: String?
)