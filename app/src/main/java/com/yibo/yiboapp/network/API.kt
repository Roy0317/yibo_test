package com.yibo.yiboapp.network

import com.yibo.yiboapp.data.DrawDataResponse
import com.yibo.yiboapp.data.SimpleResponse
import com.yibo.yiboapp.entify.*
import com.yibo.yiboapp.fragment.SetBankPwdWraper
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface API {

    @GET("https://api.xbkqzi.com/domain/getServerIp")
    fun findMyIP(): Single<FindIPResponse>

    @GET
    fun fetchServerTime(@Url url: String): Single<ServerTimeResponse>

    @GET("/native/getServerTime.do")
    suspend fun fetchServerTimeAsync(): Response<ServerTimeResponse>

    @GET("/native/config.do")
    suspend fun fetchSysConfig(): Response<SysConfigWraper>

    @GET
    fun fetchDomainList(@Url url: String, @Query("pid") applicationID: String,
                        @Query("source") source: Int, @Query("version") version: String): Single<RealDomainWraper>

    @GET("/native/googleRobotConfig.do")
    fun fetchGoogleRobot(): Single<GoogleRobotResponse>

    //////////////////////////////////////  general  ///////////////////////////////////////////////

    /**
     * 1:关于我们,2:提款帮助,3:存款帮助,4:合作伙伴->联盟方案,5:合作伙伴->联盟协议,6:联系我们,7:常见问题 ,
     * 8:玩法介绍,9:彩票公告,10:视讯公告,11:体育公告,12:电子公告,13:最新公告
     */
    @GET("/native/new_notice_v2.do")
    suspend fun fetchNotices(@Query("code") code: Int): Response<NoticeNewBean>

    @GET("/native/lunbo.do")
    suspend fun fetchLunbos(@Query("code") code: Int): Response<LunboWraper>

    @GET("/native/active_badges.do")
    suspend fun fetchActivityBadges(): Response<ActiveBadgeWrapper>

    ////////////////////////////////////// member info //////////////////////////////////////////

    @GET("/native/meminfo.do")
    suspend fun fetchMemberInfo(): Response<MemInfoWraper>

    @FormUrlEncoded
    @POST
    suspend fun syncHeaderPhoto(@Url url: String, @Field("headerKey") headerKey: String): Response<MemberHeaderWraper>



    ////////////////////////////////////// account //////////////////////////////////////////
    @GET("/native/pay_methods.do")
    suspend fun fetchChargeMethods(): Response<PayMethodWraper>

    @FormUrlEncoded
    @POST("/native/submit_pay.do")
    suspend fun postChargeData(@FieldMap params: Map<String, String>): Response<SubmitPayResultWraper>

    @GET("/center/banktrans/draw/drawdataNew.do")
    suspend fun fetchDrawDataNew(): Response<DrawDataResponse>

    @FormUrlEncoded
    @POST("/center/banktrans/draw/withdrawMoneyCheck.do")
    suspend fun checkWithdrawMoneyInput(@FieldMap params: Map<String, String>): Response<SimpleResponse>

    @GET("/native/check_account_security.do")
    suspend fun checkAccountInfo(): Response<CheckPickAccountWrapper>

    @GET("/native/pick_money_data_v2.do")
    suspend fun pickMoneyData(): Response<PickMoneyDataWraper>

    @GET("/native/pick_money_data_new.do")
    suspend fun fetchSingleAccounts(): Response<PickAccountResponse>

    @GET("/center/member/meminfo/multiCardInfo.do")
    suspend fun fetchMultiAccounts(): Response<PickAccountMultiResponse>

    @GET("/center/member/meminfo/multiCardInfo.do")
    suspend fun fetchAccountsNew(): Response<NewAccountResponse>

    @FormUrlEncoded
    @POST("/native/post_pick_money_new.do")
    suspend fun postPickMoney(@FieldMap params: Map<String, String>): Response<PostPickMoneyWraper>

    @FormUrlEncoded
    @POST("/center/banktrans/draw/drawcommit.do")
    suspend fun postPickMoneyMulti(@FieldMap params: Map<String, String>): Response<PostPickMoneyWraper>

    @FormUrlEncoded
    @POST("/center/banktrans/draw/drawcommitnew.do")
    suspend fun postPickMoneyNew(@FieldMap params: Map<String, String>): Response<PostPickMoneyWraper>

    @FormUrlEncoded
    @POST("/native/set_receipt_pwd.do")
    suspend fun postSetWithdrawPassword(@FieldMap params: Map<String, String>): Response<SetBankPwdWraper>

    @FormUrlEncoded
    @POST("/native/modify_cashpass.do")
    suspend fun postModifyWithdrawPassword(@FieldMap params: Map<String, String>): Response<ModifyPwdWraper>

    @FormUrlEncoded
    @POST("/native/modify_pass.do")
    suspend fun postModifyLoginPassword(@FieldMap params: Map<String, String>): Response<ModifyPwdWraper>

    @FormUrlEncoded
    @POST("/center/banktrans/draw/cmitbkinfo.do")
    suspend fun postUpdateBankInfo(@FieldMap params: Map<String, String>): Response<UpdateBankResponse>

    @FormUrlEncoded
    @POST("/native/post_bank_data.do")
    suspend fun postAddAccount(@FieldMap params: Map<String, String>): Response<PostBankWrapper>

    @FormUrlEncoded
    @POST
    suspend fun postAddMultiAccount(@Url url: String, @FieldMap params: Map<String, String>): Response<PostBankWrapper>

    @FormUrlEncoded
    @POST("/center/record/winToday/getWinTodayJsonData.do")
    suspend fun fetchDayBalanceData(@FieldMap params: Map<String, String>): Response<DayBalanceResponse>

    @FormUrlEncoded
    @POST("/center/banktrans/draw/commitNewDrawCard.do")
    suspend fun postAddAccountNew(@FieldMap params: Map<String, String>): Response<PostBankWrapper>
    ////////////////////////////////////  game lottery  ////////////////////////////////

    @GET("/native/all_games.do")
    suspend fun fetchAllGameData(): Response<LotterysWraper>

    @GET("/native/getGamePlays.do")
    suspend fun fetchLotteryPlays(@Query("lotCode") lotCode: String): Response<LocPlaysWraper>

    @GET("/native/getOdds.do")
    suspend fun fetchPlayOdds(@Query("lotType") lotType: String, @Query("playCode") playCode: String): Response<PeilvWebResultWraper>

    @GET("/native/getOdds.do")
    fun getPvOddsUrl(@Query("playCode") playCode: String, @Query("lotType") lotType: String) : Single<PeilvWebResultWraper>

    @GET("{subPath}")
    suspend fun forwardGame(@Path(value = "subPath", encoded = true) path: String,
                            @QueryMap params: Map<String, String>): Response<ForwardGameResponse>
}