package com.yibo.yiboapp.manager

import com.yibo.yiboapp.entify.ActiveBadgeWrapper
import com.yibo.yiboapp.entify.LunboWraper
import com.yibo.yiboapp.entify.NoticeNewBean
import com.yibo.yiboapp.entify.ServerTimeResponse
import com.yibo.yiboapp.network.RetrofitFactory
import retrofit2.Response
import retrofit2.http.Query

class GeneralManager {

    suspend fun fetchServerTimeAsync(): Response<ServerTimeResponse>{
        return RetrofitFactory.api().fetchServerTimeAsync()
    }

    /**
     * 类型
     * 1:关于我们,2:提款帮助,3:存款帮助,4:合作伙伴->联盟方案,5:合作伙伴->联盟协议,
     * 6:联系我们,7:常见问题,8:玩法介绍,9:彩票公告,10:视讯公告,11:体育公告,
     * 12:电子公告,13:最新公告
     */
    suspend fun fetchNotices(code: Int): Response<NoticeNewBean>{
        return RetrofitFactory.api().fetchNotices(code)
    }

    suspend fun fetchLunbos(code: Int): Response<LunboWraper>{
        return RetrofitFactory.api().fetchLunbos(code)
    }

    suspend fun fetchActivityBadges(): Response<ActiveBadgeWrapper>{
        return RetrofitFactory.api().fetchActivityBadges()
    }
}