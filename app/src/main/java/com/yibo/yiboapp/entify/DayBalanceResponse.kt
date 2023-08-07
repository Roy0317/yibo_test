package com.yibo.yiboapp.entify


import com.google.gson.annotations.SerializedName

data class DayBalanceResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("content")
    val content: Content,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("success")
    val success: Boolean) {
    data class Content(
        @SerializedName("dailyMoneyData")
        val balanceList: List<DayBalance>,
        @SerializedName("endTime")
        val endTime: String,
        @SerializedName("startTime")
        val startTime: String) {

        data class DayBalance(
            @SerializedName("betAmount")
            val betAmount: Double,
            @SerializedName("profitAndLossAmount")
            val balanceAmount: Double,
            @SerializedName("winAmount")
            val winAmount: Double,
            @SerializedName("winInfoName")
            val winInfoName: String
        )
    }
}