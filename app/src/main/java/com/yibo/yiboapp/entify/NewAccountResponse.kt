package com.yibo.yiboapp.entify


import com.google.gson.annotations.SerializedName

data class NewAccountResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("cardInfo")
    val cardInfoList: List<CardInfo>,
    @SerializedName("success")
    val success: Boolean,
    val msg: String?)

data class CardInfo(
    @SerializedName("cardCountLimit")
    val cardCountLimit: Int,
    @SerializedName("cardList")
    val cardList: List<Card>,
    @SerializedName("cardProperties")
    val cardProperties: List<CardProperty>,
    @SerializedName("drawRecordType")
    val drawRecordType: Int,
    @SerializedName("maxDraw")
    val maxDraw: Double,
    @SerializedName("minDraw")
    val minDraw: Double,
    @SerializedName("mobileImageColorCode")
    val mobileImageColorCode: String,
    @SerializedName("mobileImageUrl")
    val mobileImageUrl: String,
    @SerializedName("orderNumber")
    val orderNumber: Int,
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("tips")
    val tips: String,
    @SerializedName("typeName")
    val typeName: String,
    @SerializedName("typeNumber")
    val typeNumber: Int)

data class Card(
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("bankAddress")
    val bankAddress: String?,
    @SerializedName("bankName")
    val bankName: String?,
    @SerializedName("cardNo")
    val cardNo: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("createTime")
    val createTime: Long,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isPrimary")
    val isPrimary: Int,
    @SerializedName("province")
    val province: String,
    @SerializedName("realName")
    val realName: String?,
    @SerializedName("stationId")
    val stationId: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("userName")
    val userName: String,
    var imageUrl: String = "",
    var bgColorCode: String = "",
    var typeName: String = ""
)

/**
 * "consumerEnum":"REAL_NAME_SAME_AS_MEMBER",
 * "propertyDom":"text",
 * "propertyDoubleCheck":false,
 * "propertyKey":"realName",
 * "propertyName":"持卡人",
 * "propertyRegex":"^(?=\\s*\\S).*$"
 */
data class CardProperty(
    @SerializedName("consumerEnum")
    val consumerEnum: String?,   //非空时代表必填
    @SerializedName("propertyDom")
    val propertyDom: String,
    @SerializedName("propertyDoubleCheck")
    val propertyDoubleCheck: Boolean,
    @SerializedName("propertyKey")
    val propertyKey: String,
    @SerializedName("propertyName")
    val propertyName: String,
    @SerializedName("propertyRegex")
    val propertyRegex: String?
)