package com.yibo.yiboapp.manager

import android.content.Context
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.entify.*
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.network.RetrofitFactory
import com.yibo.yiboapp.utils.Utils
import retrofit2.Response

class GameManager {







    suspend fun fetchAllGameData(): Response<LotterysWraper>{
        return RetrofitFactory.api().fetchAllGameData()
    }

    suspend fun fetchLotteryPlays(lotteryCode: String): Response<LocPlaysWraper>{
        return RetrofitFactory.api().fetchLotteryPlays(lotteryCode)
    }

    suspend fun fetchPlayOdds(lotType: String, playCode: String): Response<PeilvWebResultWraper>{
        return RetrofitFactory.api().fetchPlayOdds(lotType, playCode)
    }

    suspend fun forwardGame(url: String, params: Map<String, String>): Response<ForwardGameResponse>{
        return RetrofitFactory.api().forwardGame(url, params)
    }





    /**
     * 依据彩种大类类别判断是否需要计算总和大小单双
     */
    fun needCalculateTotalDSDXByCpType(cpType: String): Boolean{
        return when(cpType){
            //时时彩
            "9", "1", "2", "51", "52",
            //快三
//            "10", "100", "58",
            //PC蛋蛋
            "11", "7", "57",
            //赛车
            "8", "3", "53",
            //十一选五
            "14", "5", "55" -> true
            else -> false
        }
    }

    fun isSaiche(cpType: String): Boolean {
        return cpType in arrayOf("8", "3", "53")
    }

    fun is11x5(cpType: String): Boolean {
        return cpType in arrayOf("14", "4", "55")
    }

    fun isKuaileCai(lotCode: String): Boolean {
        return lotCode in arrayOf("GDKLSF", "HNKLSF", "CQXYNC")
    }

    fun is28(cpType: String): Boolean {
        return cpType in arrayOf("11", "7", "57")
    }

    fun isKuaiSan(cpType: String): Boolean {
        return cpType in arrayOf("10", "100", "58", "160")
    }

    fun getMaxOpenNumFromLotCode(cpType: String, lotCode: String, numbers: List<BallListItemInfo>): Int {
        return if (isKuaileCai(lotCode)) {
            20 * numbers.size //所有号码最高号码总和
        } else if (isSaiche(cpType)) {
            20
        } else if (is11x5(cpType)) {
            11 * numbers.size + 1 * numbers.size
        } else if (is28(cpType)) {
            27
        } else if (isKuaiSan(cpType)) {
            20
        } else {
            9 * numbers.size //所有号码最高号码总和
        }
    }

    fun figureOutALLDXDS(context: Context?, numbers: List<BallListItemInfo>?,
                         cpType: String, lotCode: String): List<BallListItemInfo>? {
        if (numbers.isNullOrEmpty()) {
            return null
        }

        val totalNum = getMaxOpenNumFromLotCode(cpType, lotCode, numbers)
        var total = 0 //当前开奖号码总和
        for (i in numbers.indices) {
            if ((isKuaiSan(cpType) || is28(cpType)) && i == numbers.size - 1) {
                break
            }
            val info = numbers[i]
            if (!info.num.isNullOrEmpty() && Utils.isNumeric(info.num)) {
                if (isSaiche(cpType)) {
                    if (i <= 1) total += info.num.toInt()
                } else {
                    total += info.num.toInt()
                }
            }
        }
        val appendList: MutableList<BallListItemInfo> = ArrayList()
        val numInfo = BallListItemInfo()
        numInfo.num = total.toString()
        appendList.add(numInfo)

        if (total >= 0) {
            //大或小
            if (is11x5(cpType)) {
                val info = BallListItemInfo()
                info.num = when{
                    total > 30 -> "大"
                    total == 30 -> "和"
                    else -> "小"
                }
                appendList.add(info)
            } else if (isSaiche(cpType)) {
                val bigInfo = BallListItemInfo()
                if (total > 11) {
                    bigInfo.num = "大"
                } else if (UsualMethod.getConfigFromJson(context).pk10_guanyahe_11_heju.isOn()) {
                    bigInfo.num = if (total == 11) "和" else "小"
                } else {
                    bigInfo.num = "小"
                }
                appendList.add(bigInfo)
            } else {
                val info = BallListItemInfo()
                info.num = if (total > totalNum / 2) "大" else "小"
                appendList.add(info)
            }

            //单或双
            val doubleInfo = BallListItemInfo()
            doubleInfo.num = if (total % 2 == 0) "双" else "单"
            appendList.add(doubleInfo)
        } else {
            //大或小
            val smallInfo = BallListItemInfo()
            smallInfo.num = "无"
            appendList.add(smallInfo)
            //单或双
            val dsInfo = BallListItemInfo()
            dsInfo.num = "无"
            appendList.add(dsInfo)
        }
        return appendList
    }
}