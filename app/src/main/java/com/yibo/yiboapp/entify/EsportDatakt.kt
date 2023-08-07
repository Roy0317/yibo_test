package com.yibo.yiboapp.entify

data class EsportDataKt(
    val aggsData: AggsData,
    val rows: List<EsportDataRow>,
    val total: Int
)

data class AggsData(
    val bettingMoneyCount: Double,
    val realBettingMoneyCount: Double,
    val winMoneyCount: Double
)

data class EsportDataRow(
    val accountId: Int,
    val bettingContent: String,
    val bettingMoney: Double,
    val bettingTime: Long,
    val buyTime: Long,
    val createDatetime: Long,
    val gameName: String,
    val id: Int,
    val ip: String,
    val matchName: String,
    val md5Str: String,
    val odds: String,
    val orderId: String,
    val parentIds: String,
    val platformType: String,
    val playName: String,
    val realBettingMoney: Double,
    val serverId: Int,
    val staId: Int,
    val stationId: Int,
    val status: String,
    val thirdMemberId: Int,
    val thirdUsername: String,
    val type: Int,
    val username: String,
    val winMoney: Double
)