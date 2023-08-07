package com.yibo.yiboapp.manager

import android.content.Context
import android.content.Intent
import com.yibo.yiboapp.activity.ChargeMoneyActivity
import com.yibo.yiboapp.activity.NewChargeMoneyActivity
import com.yibo.yiboapp.data.DrawDataResponse
import com.yibo.yiboapp.data.SimpleResponse
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.entify.CheckPickAccountWrapper
import com.yibo.yiboapp.entify.DayBalanceResponse
import com.yibo.yiboapp.entify.ModifyPwdWraper
import com.yibo.yiboapp.entify.NewAccountResponse
import com.yibo.yiboapp.entify.PayMethodWraper
import com.yibo.yiboapp.entify.PickAccountMultiResponse
import com.yibo.yiboapp.entify.PickAccountResponse
import com.yibo.yiboapp.entify.PickMoneyDataWraper
import com.yibo.yiboapp.entify.PostBankWrapper
import com.yibo.yiboapp.entify.PostPickMoneyWraper
import com.yibo.yiboapp.entify.SubmitPayResultWraper
import com.yibo.yiboapp.entify.UpdateBankResponse
import com.yibo.yiboapp.fragment.SetBankPwdWraper
import com.yibo.yiboapp.mvvm.banking.AccountManagerActivity
import com.yibo.yiboapp.mvvm.banking.AccountManagerNewActivity
import com.yibo.yiboapp.mvvm.banking.PickMoneyActivity
import com.yibo.yiboapp.mvvm.banking.PickMoneyNewActivity
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.network.RetrofitFactory
import retrofit2.Response
import retrofit2.http.FieldMap

class BankingManager {

    companion object{
        const val PAY_METHOD_ONLINE = 0
        const val PAY_METHOD_FAST = 1
        const val PAY_METHOD_BANK = 2
        const val QRCODE_NAME = "yb_qrcode.jpg"

        const val LOCAL_ACCOUNT_BANK = 1
        const val LOCAL_ACCOUNT_ALIPAY = 2
        const val LOCAL_ACCOUNT_USDT = 3
        const val LOCAL_ACCOUNT_GOPAY = 5
        const val LOCAL_ACCOUNT_OKPAY = 6
        const val LOCAL_ACCOUNT_TOPAY = 7
        const val LOCAL_ACCOUNT_VPAY = 8

        private const val USER_BALANCE = "user_yu_e"

        fun openChargePage(context: Context, accountName: String, balanceString: String): Intent{
            val config = UsualMethod.getConfigFromJson(context)
            val intent = if (config.charge_page_style == "classic") {
                Intent(context, NewChargeMoneyActivity::class.java)
            }else{
                Intent(context, ChargeMoneyActivity::class.java)
            }
            intent.putExtra("account", accountName)
            intent.putExtra("money", balanceString)
            return intent
        }

        fun openWithdrawPage(context: Context, balance: Double = 0.0): Intent {
            val config = UsualMethod.getConfigFromJson(context)
            val intent = if(config.station_currency_type_permission.isOn()){
                Intent(context, PickMoneyNewActivity::class.java)
            }else{
                Intent(context, PickMoneyActivity::class.java)
            }
            intent.putExtra("balance", balance)
            return intent
        }

        fun openAccountManagerPage(context: Context, toPickAccount: Boolean): Intent{
            val config = UsualMethod.getConfigFromJson(context)
            val intent = if(config.station_currency_type_permission.isOn()){
                Intent(context, AccountManagerNewActivity::class.java)
            }else{
                Intent(context, AccountManagerActivity::class.java)
            }
            intent.putExtra("toPickAccount", toPickAccount)
            return intent
        }
    }


    fun saveUserBalance(context: Context, balance: String){
        YiboPreference.instance(context).sp.edit()
            .putString(USER_BALANCE, balance)
            .apply()
    }

    fun loadUserBalance(context: Context): String {
        return YiboPreference.instance(context).sp
            .getString(USER_BALANCE, "") ?: ""
    }

    /**
     * 设置充值金额小数点后的金额（随机生成） needFloat:on 需要设置  off:不需要
     */
    fun addMoneyFloat(needFloat: String, money: String): String {
        return if (needFloat.isOn()) {
            val arr = money.split("\\.")
            if (arr.size > 1) {
                money
            } else {
                //用户没有设置小数点
                val temp = (Math.random() * 100).toInt() //随机生成100以内的随机数
                if (temp < 10) {
                    "$money.0$temp"
                } else {
                    "$money.$temp"
                }
            }
        } else {
            money
        }
    }















    suspend fun fetchChargeMethods(): Response<PayMethodWraper>{
        return RetrofitFactory.api().fetchChargeMethods()
    }

    suspend fun postChargeData(params: Map<String, String>): Response<SubmitPayResultWraper>{
        return RetrofitFactory.api().postChargeData(params)
    }

    suspend fun fetchDrawDataNew(): Response<DrawDataResponse>{
        return RetrofitFactory.api().fetchDrawDataNew()
    }

    suspend fun checkWithdrawMoneyInput(type: String, money: String): Response<SimpleResponse>{
        val params = hashMapOf("withdrawalType" to type, "money" to money)
        return RetrofitFactory.api().checkWithdrawMoneyInput(params)
    }

    suspend fun checkAccountInfo(): Response<CheckPickAccountWrapper> {
        return RetrofitFactory.api().checkAccountInfo()
    }

    suspend fun pickMoneyData(): Response<PickMoneyDataWraper>{
        return RetrofitFactory.api().pickMoneyData()
    }

    suspend fun fetchSingleAccounts(): Response<PickAccountResponse>{
        return RetrofitFactory.api().fetchSingleAccounts()
    }

    suspend fun fetchMultiAccounts(): Response<PickAccountMultiResponse>{
        return RetrofitFactory.api().fetchMultiAccounts()
    }

    suspend fun fetchAccountsNew(): Response<NewAccountResponse>{
        return RetrofitFactory.api().fetchAccountsNew()
    }

    suspend fun postPickMoney(params: Map<String, String>): Response<PostPickMoneyWraper>{
        return RetrofitFactory.api().postPickMoney(params)
    }

    suspend fun postPickMoneyMulti(params: Map<String, String>): Response<PostPickMoneyWraper>{
        return RetrofitFactory.api().postPickMoneyMulti(params)
    }

    suspend fun postPickMoneyNew(params: Map<String, String>): Response<PostPickMoneyWraper>{
        return RetrofitFactory.api().postPickMoneyNew(params)
    }

    suspend fun postSetWithdrawPassword(params: Map<String, String>): Response<SetBankPwdWraper>{
        return RetrofitFactory.api().postSetWithdrawPassword(params)
    }

    suspend fun postModifyWithdrawPassword(params: Map<String, String>): Response<ModifyPwdWraper>{
        return RetrofitFactory.api().postModifyWithdrawPassword(params)
    }

    suspend fun postModifyLoginPassword(params: Map<String, String>): Response<ModifyPwdWraper>{
        return RetrofitFactory.api().postModifyLoginPassword(params)
    }

    suspend fun postUpdateBankInfo(params: Map<String, String>): Response<UpdateBankResponse>{
        return RetrofitFactory.api().postUpdateBankInfo(params)
    }

    suspend fun postAddAccount(params: Map<String, String>): Response<PostBankWrapper>{
        return RetrofitFactory.api().postAddAccount(params)
    }

    suspend fun postAddMultiAccount(url: String, params: Map<String, String>): Response<PostBankWrapper>{
        return RetrofitFactory.api().postAddMultiAccount(url, params)
    }

    suspend fun fetchDayBalanceData(params: Map<String, String>): Response<DayBalanceResponse>{
        return RetrofitFactory.api().fetchDayBalanceData(params)
    }

    /**
     * 新版新增提款帐户api
     */
    suspend fun postAddAccountNew(params: Map<String, String>): Response<PostBankWrapper>{
        return RetrofitFactory.api().postAddAccountNew(params)
    }
}