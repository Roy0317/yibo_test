package com.yibo.yiboapp.data


import com.google.gson.annotations.SerializedName

data class DrawDataResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("content")
    val content: Content,
    val msg: String? = null,
    @SerializedName("success")
    val success: Boolean
) {
    data class Content(
        @SerializedName("arrivalTime")
        val arrivalTime: String,
        @SerializedName("checkBetNum")
        val checkBetNum: Double,
        @SerializedName("consumeRate")
        val consumeRate: String,
        @SerializedName("curWnum")
        val curWnum: Int,
        @SerializedName("drawFlag")
        val drawFlag: String,
        @SerializedName("enablePick")
        val enablePick: Boolean,
        @SerializedName("end")
        val end: String,
        @SerializedName("max")
        val max: String,
        @SerializedName("member")
        val member: Member,
        @SerializedName("min")
        val min: String,
        @SerializedName("needUpdCardNo")
        val needUpdCardNo: Boolean,
        @SerializedName("start")
        val start: String,
        @SerializedName("strategy")
        val strategy: Strategy?,
        @SerializedName("wnum")
        val wnum: Int
    ) {
        data class Member(
            @SerializedName("account")
            val account: String,
            @SerializedName("accountStatus")
            val accountStatus: Int,
            @SerializedName("accountType")
            val accountType: Int,
            @SerializedName("alipayAccount")
            val alipayAccount: String,
            @SerializedName("alipayName")
            val alipayName: String,
            @SerializedName("bankAddress")
            val bankAddress: String,
            @SerializedName("bankName")
            val bankName: String,
            @SerializedName("betNum")
            val betNum: Double,
            @SerializedName("cardNo")
            val cardNo: String,
            @SerializedName("cardNoStatus")
            val cardNoStatus: Int,
            @SerializedName("city")
            val city: String,
            @SerializedName("createDatetime")
            val createDatetime: Long,
            @SerializedName("drawNeed")
            val drawNeed: Double,
            @SerializedName("email")
            val email: String,
            @SerializedName("gopayAddress")
            val gopayAddress: String,
            @SerializedName("gopayName")
            val gopayName: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("lastLoginDatetime")
            val lastLoginDatetime: Long,
            @SerializedName("lastLoginIp")
            val lastLoginIp: String,
            @SerializedName("levelGroup")
            val levelGroup: Int,
            @SerializedName("money")
            val money: Double,
            @SerializedName("moneyTypeId")
            val moneyTypeId: Int,
            @SerializedName("okpayAddress")
            val okpayAddress: String,
            @SerializedName("okpayName")
            val okpayName: String,
            @SerializedName("online")
            val online: Int,
            @SerializedName("parentNames")
            val parentNames: String,
            @SerializedName("phone")
            val phone: String,
            @SerializedName("province")
            val province: String,
            @SerializedName("qq")
            val qq: String,
            @SerializedName("registerIp")
            val registerIp: String,
            @SerializedName("registerOs")
            val registerOs: String,
            @SerializedName("registerUrl")
            val registerUrl: String,
            @SerializedName("score")
            val score: Double,
            @SerializedName("topayAddress")
            val topayAddress: String,
            @SerializedName("topayName")
            val topayName: String,
            @SerializedName("totalBetNum")
            val totalBetNum: Double,
            @SerializedName("usdtAddress")
            val usdtAddress: String,
            @SerializedName("usdtDraw")
            val usdtDraw: String,
            @SerializedName("userName")
            val userName: String,
            @SerializedName("vpayAddress")
            val vpayAddress: String,
            @SerializedName("vpayName")
            val vpayName: String,
            @SerializedName("wechat")
            val wechat: String
        )

        data class Strategy(
            @SerializedName("drawNum")
            val drawNum: Int,
            @SerializedName("feeType")
            val feeType: Int,
            @SerializedName("feeValue")
            val feeValue: Double,
            @SerializedName("id")
            val id: Int,
            @SerializedName("lowerLimit")
            val lowerLimit: Double,
            @SerializedName("remark")
            val remark: String,
            @SerializedName("stationId")
            val stationId: Int,
            @SerializedName("status")
            val status: Int,
            @SerializedName("upperLimit")
            val upperLimit: Double
        )
    }
}