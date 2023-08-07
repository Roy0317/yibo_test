@file:Suppress("DEPRECATION")

package com.yibo.yiboapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

object NetWorkTypeUtil {
    const val NETWORK_NONE = 0
    const val NETWORK_WIFI = 1
    const val NETWORK_2G = 2
    const val NETWORK_3G = 3
    const val NETWORK_4G = 4
    const val NETWORK_5G = 5
    const val NETWORK_MOBILE = 6
    fun getNetworkState(context: Context): Int {
        //获取系统网路服务
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return NETWORK_NONE
        //获取当前网路类型，如果为空，则为无网路
        val activeNetInfo = connectManager.activeNetworkInfo
        if (activeNetInfo == null || !activeNetInfo.isAvailable) {
            return NETWORK_NONE
        }
        //判断是否为wifi
        val wifiInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiInfo != null) {
            val state = wifiInfo.state
            if (state != null) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI
                }
            }
        }
        //判断当前连接的是哪种网路
        val networkInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (networkInfo != null) {
            val state = networkInfo.state
            val strSubTypeName = networkInfo.subtypeName
            if (state != null) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return when (activeNetInfo.subtype) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN, TelephonyManager.NETWORK_TYPE_GSM -> NETWORK_2G
                        TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_IWLAN -> NETWORK_3G
                        TelephonyManager.NETWORK_TYPE_LTE -> NETWORK_4G
                        TelephonyManager.NETWORK_TYPE_NR -> NETWORK_5G
                        TelephonyManager.NETWORK_TYPE_UNKNOWN -> NETWORK_MOBILE
                        else ->                             //中国移动、联通、电信  三种3G制式
                            if (strSubTypeName.equals("TD-SCDMA", true)
                                || strSubTypeName.equals("WCDMA", true)
                                || strSubTypeName.equals("CDMA2000", true)
                            ) {
                                NETWORK_3G
                            } else {
                                NETWORK_MOBILE
                            }
                    }
                }
            }
        }
        return NETWORK_NONE
    }
}
