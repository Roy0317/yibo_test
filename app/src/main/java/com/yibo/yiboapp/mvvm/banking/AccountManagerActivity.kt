package com.yibo.yiboapp.mvvm.banking

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yibo.yiboapp.R
import com.yibo.yiboapp.adapter.AccountManagerAdapter
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.databinding.ActivityAccountManagerBinding
import com.yibo.yiboapp.entify.PickAccountMultiResponse
import com.yibo.yiboapp.entify.PickAccountMultiResponse.AccountBean
import com.yibo.yiboapp.entify.PickAccountResponse
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.*
import com.yibo.yiboapp.mvvm.password.SetBankPwdActivity
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.views.accountmanager.SelectListDialog
import kotlinx.coroutines.launch
import java.util.*

class AccountManagerActivity: BaseActivityKotlin(){

    companion object{
        private const val ADD_ACCOUNT = 10
        private const val SET_BANK_PASSWORD = 11
        private const val COMPLETE_BANK = 12
    }

    private lateinit var binding: ActivityAccountManagerBinding
    private val sysConfig = UsualMethod.getConfigFromJson(this)
    private var toPickAccount = false
    private var accountAdapter: AccountManagerAdapter? = null

    private var realName: String = ""
    private var bankCard: String = ""
    private var isCheckAccountSuccessful = false
    private var hasSetAccountPassword = false // 是否设置过提款密码

    private var accountMultiResponse: PickAccountMultiResponse? = null
    private var isBankCardSet = false //是否已经设置过银行卡

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toPickAccount = intent.getBooleanExtra("toPickAccount", false)
        initView()
        checkAccountFromWeb()
    }

    private fun initView() {
        binding.title.middleTitle.setText(R.string.zhgl)
        binding.title.rightText.visibility = View.VISIBLE
        binding.title.rightText.setText(R.string.add_account)
        binding.title.rightText.setOnClickListener{
            if (isCheckAccountSuccessful) {
                showAccountTypeDialog()
            } else {
                checkAccountFromWeb()
            }
        }
        binding.title.backText.setOnClickListener { onBackPressed() }
    }

    //检查是否设置过出款帐户等信息
    private fun checkAccountFromWeb() {
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.checkAccountInfo() }
            if(result is HttpResult.Success){
                val response = result.data
                if (!response.isSuccess) {
                    showToast(response.msg.ifNullOrEmpty { getString(R.string.check_safety_fail) })
                    checkSessionValidity(response.code)
                } else {
                    if (response.code == 121) {
                        //尚未设置提款密码
                        showToast(response.msg)
                        val intent = Intent(this@AccountManagerActivity, SetBankPwdActivity::class.java)
                        startActivityForResult(intent, SET_BANK_PASSWORD)
                    } else {
                        isCheckAccountSuccessful = true
                        realName = response.content.userName
                        bankCard = response.content.cardNo.ifNullOrEmpty { response.content.bankAddress }.ifNullOrEmpty { "" }
                        fetchWithdrawAccounts()
                        hasSetAccountPassword = response.content?.repPwd?.isNotEmpty() == true
                    }
                }
            }else if(result is HttpResult.Error){
                showToast(result.message)
            }
        }
    }

    private fun fetchWithdrawAccounts() {
        if (sysConfig.member_multi_bank_card_permission.isOn()) {
            //取得多银行卡模式的账户资讯
            lifecycleScope.launch{
                val result = httpRequest { ManagerFactory.bankingManager.fetchMultiAccounts() }
                if(result is HttpResult.Success){
                    accountMultiResponse = result.data
                    val accounts = adaptAccounts(accountMultiResponse)
                    updateRecyclerAccounts(accounts)
                    //多银行卡会有写入table不同步的问题，所以不进行完善银行卡功能。有问题让用户找客服修改
                    //if(needCompleteBankCard(accounts)){
                    //    showCompleteBankDialog();
                    //}
                }else if(result is HttpResult.Error){
                    showToast(result.message)
                }
            }
        }else{
            lifecycleScope.launch{
                val result = httpRequest { ManagerFactory.bankingManager.fetchSingleAccounts() }
                if(result is HttpResult.Success){
                    val response = result.data
                    if (!response.isSuccess) {
                        showToast(response.msg.ifNullOrEmpty { getString(R.string.fetch_account_fail) })
                    } else {
                        val accounts = adaptAccounts(response)
                        if (needCompleteBankCard(accounts)) {
                            showCompleteBankDialog()
                        } else {
                            updateRecyclerAccounts(accounts)
                        }
                    }
                }else if(result is HttpResult.Error){
                    showToast(result.message)
                }
            }
        }
    }

    /**
     * 开启多银行卡功能与否会决定使用哪个接口，要整理接口回传的data
     */
    private fun adaptAccounts(response: PickAccountResponse): List<AccountBean> {
        val results: MutableList<AccountBean> = ArrayList()
        response.content?.pickAccounts?.forEach { account ->
            if (!account.alipayAccount.isNullOrEmpty() && sysConfig.on_off_alipay_withdrawal.isOn()) {
                val accountBean = AccountBean()
                accountBean.type = BankingManager.LOCAL_ACCOUNT_ALIPAY
                accountBean.cardNo = account.alipayAccount
                accountBean.userName = account.alipayName
                results.add(accountBean)
            } else if (!account.usdtAddress.isNullOrEmpty() && sysConfig.on_off_usdt_withdrawal.isOn()) {
                val accountBean = AccountBean()
                accountBean.type = BankingManager.LOCAL_ACCOUNT_USDT
                accountBean.cardNo = account.usdtAddress
                accountBean.userName = account.userName
                results.add(accountBean)
            } else if (!TextUtils.isEmpty(account.cardNo)) {
                val accountBean = AccountBean()
                accountBean.type = BankingManager.LOCAL_ACCOUNT_BANK
                accountBean.bankName = account.bankName
                accountBean.bankAddress = account.bankAddress
                accountBean.cardNo = account.cardNo
                accountBean.realName = account.userName
                results.add(accountBean)
            } else if (!account.gopayAccount.isNullOrEmpty() && sysConfig.on_off_gopay_withdrawal.isOn()) {
                val accountBean = AccountBean()
                accountBean.type = BankingManager.LOCAL_ACCOUNT_GOPAY
                accountBean.cardNo = account.gopayAccount
                accountBean.userName = account.gopayName
                results.add(accountBean)
            } else if (!account.okpayAccount.isNullOrEmpty() && sysConfig.on_off_okpay_withdrawal.isOn()) {
                val accountBean = AccountBean()
                accountBean.type = BankingManager.LOCAL_ACCOUNT_OKPAY
                accountBean.cardNo = account.okpayAccount
                accountBean.userName = account.okpayName
                results.add(accountBean)
            } else if (!account.topayaccount.isNullOrEmpty() && sysConfig.on_off_topay_withdrawal.isOn()) {
                val accountBean = AccountBean()
                accountBean.type = BankingManager.LOCAL_ACCOUNT_TOPAY
                accountBean.cardNo = account.topayaccount
                accountBean.userName = account.topayName
                results.add(accountBean)
            } else if (!account.vpayaccount.isNullOrEmpty() && sysConfig.on_off_vpay_withdrawal.isOn()) {
                val accountBean = AccountBean()
                accountBean.type = BankingManager.LOCAL_ACCOUNT_VPAY
                accountBean.cardNo = account.vpayaccount
                accountBean.userName = account.vpayName
                results.add(accountBean)
            }
        }
        return results
    }

    /**
     * 开启多银行卡功能与否会决定使用哪个接口，要整理接口回传的data
     */
    private fun adaptAccounts(response: PickAccountMultiResponse?): List<AccountBean> {
        val results: MutableList<AccountBean> = ArrayList()
        response?.cardsList?.forEach{ account ->
            /** 对账户的type做校正，衔接app端的账户类型设定  */
            if (account.type == 6 && sysConfig.on_off_vpay_withdrawal.isOn()) {
                account.type = BankingManager.LOCAL_ACCOUNT_VPAY
                results.add(account)
            } else if (account.type == 5 && sysConfig.on_off_topay_withdrawal.isOn()) {
                account.type = BankingManager.LOCAL_ACCOUNT_TOPAY
                results.add(account)
            } else if (account.type == 4 && sysConfig.on_off_okpay_withdrawal.isOn()) {
                account.type = BankingManager.LOCAL_ACCOUNT_OKPAY
                results.add(account)
            } else if (account.type == 3 && sysConfig.on_off_gopay_withdrawal.isOn()) {
                account.type = BankingManager.LOCAL_ACCOUNT_GOPAY
                results.add(account)
            } else if (account.type == 2 && sysConfig.on_off_usdt_withdrawal.isOn()) {
                account.type = BankingManager.LOCAL_ACCOUNT_USDT
                results.add(account)
            } else {
                results.add(account)
            }
        }

        if (sysConfig.on_off_alipay_withdrawal.isOn()) {
            response?.alipayList?.forEach { account ->
                account.type = BankingManager.LOCAL_ACCOUNT_ALIPAY
                results.add(account)
            }
        }

        /** 依据账户的种类进行排序 */
        results.sortBy { it.type }
        return results
    }

    private fun needCompleteBankCard(pickAccounts: List<AccountBean>): Boolean {
        return pickAccounts.any { it.type == BankingManager.LOCAL_ACCOUNT_BANK && it.bankName.isEmpty() }
    }

    private fun showCompleteBankDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.complete_bank_card)
            .setMessage(R.string.complete_bank_card_tip)
            .setPositiveButton("前往设置") { _: DialogInterface?, _: Int ->
                val intent = UpdateBankAccountActivity.createIntent(this, realName, bankCard)
                startActivityForResult(intent, COMPLETE_BANK)
            }
            .show()
    }

    private fun updateRecyclerAccounts(pickAccounts: List<AccountBean>) {
        if (pickAccounts.isEmpty()) {
            showToast("尚未设置提款帐户")
        }

        if (accountAdapter == null) {
            binding.rcyAccountManager.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            accountAdapter = AccountManagerAdapter(this)
            accountAdapter!!.list = pickAccounts
            if (toPickAccount) {
                accountAdapter!!.setAccountListener { account: AccountBean ->
                    if (account.type == BankingManager.LOCAL_ACCOUNT_ALIPAY && !sysConfig.on_off_alipay_withdrawal.isOn()) {
                        showToast("暂不支持支付宝提款，请联系客服！")
                        return@setAccountListener
                    }
                    if (account.type == BankingManager.LOCAL_ACCOUNT_USDT && !sysConfig.on_off_usdt_withdrawal.isOn()) {
                        showToast("暂不支持USDT提款，请联系客服！")
                        return@setAccountListener
                    }
                    if (account.type == BankingManager.LOCAL_ACCOUNT_GOPAY && !sysConfig.on_off_gopay_withdrawal.isOn()) {
                        showToast("暂不支持GoPay提款，请联系客服！")
                        return@setAccountListener
                    }
                    if (account.type == BankingManager.LOCAL_ACCOUNT_OKPAY && !sysConfig.on_off_okpay_withdrawal.isOn()) {
                        showToast("暂不支持OkPay提款，请联系客服！")
                        return@setAccountListener
                    }
                    if (account.type == BankingManager.LOCAL_ACCOUNT_TOPAY && !sysConfig.on_off_topay_withdrawal.isOn()) {
                        showToast("暂不支持ToPay提款，请联系客服！")
                        return@setAccountListener
                    }
                    if (account.type == BankingManager.LOCAL_ACCOUNT_VPAY && !sysConfig.on_off_vpay_withdrawal.isOn()) {
                        showToast("暂不支持VPay提款，请联系客服！")
                        return@setAccountListener
                    }
                    val json = Gson().toJson(account)
                    val intent = intent
                    intent.putExtra("account", json)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            binding.rcyAccountManager.adapter = accountAdapter
        } else {
            accountAdapter!!.list = pickAccounts
        }
    }

    private fun showAccountTypeDialog() {
        // 如果没有设置银行卡账户，那么只让先设置银行卡账号
        isBankCardSet = accountAdapter!!.list.any { it.type == BankingManager.LOCAL_ACCOUNT_BANK }

        val typeList: MutableList<String> = ArrayList()
        typeList.add("银行卡")
        if (isBankCardSet) {
            if (sysConfig.on_off_usdt_withdrawal.isOn()) {
                typeList.add("USDT")
            }
            if (sysConfig.on_off_alipay_withdrawal.isOn()) {
                typeList.add("支付宝")
            }
            if (sysConfig.on_off_gopay_withdrawal.isOn()) {
                typeList.add("GoPay")
            }
            if (sysConfig.on_off_okpay_withdrawal.isOn()) {
                typeList.add("OkPay")
            }
            if (sysConfig.on_off_topay_withdrawal.isOn()) {
                typeList.add("ToPay")
            }
            if (sysConfig.on_off_vpay_withdrawal.isOn()) {
                typeList.add("VPay")
            }
        }

        val dialog = SelectListDialog(this, "新增帐户类型", typeList) { position: Int ->
            val accountType: Int = when (typeList[position]) {
                "USDT" -> BankingManager.LOCAL_ACCOUNT_USDT
                "支付宝" -> BankingManager.LOCAL_ACCOUNT_ALIPAY
                "GoPay" -> BankingManager.LOCAL_ACCOUNT_GOPAY
                "OkPay" -> BankingManager.LOCAL_ACCOUNT_OKPAY
                "ToPay" -> BankingManager.LOCAL_ACCOUNT_TOPAY
                "VPay" -> BankingManager.LOCAL_ACCOUNT_VPAY
                "银行卡" -> BankingManager.LOCAL_ACCOUNT_BANK
                else -> BankingManager.LOCAL_ACCOUNT_BANK
            }
            if (sysConfig.member_multi_bank_card_permission.isOn()) {
                //新增多银行卡账户必须填写提款密码
                if (checkAccountLimit(accountType, accountMultiResponse)) {
                    val intent =
                        AccountEditActivity.newIntent(this, accountType, true, true, realName)
                    startActivityForResult(intent, ADD_ACCOUNT)
                }
            } else {
                if (accountAdapter!!.list.any { it.type == accountType }) {
                    showToast("已设置过相同类型帐户，无须重复绑定")
                } else {
                    val intent =
                        AccountEditActivity.newIntent(this, accountType, true, true, realName)
                    startActivityForResult(intent, ADD_ACCOUNT)
                }
            }
        }
        dialog.show()
    }

    /**
     * 欲新增多银行卡时，须检查银行与usdt账户是否已达设置上限
     */
    private fun checkAccountLimit(accountType: Int, response: PickAccountMultiResponse?): Boolean {
        if (response == null) {
            showToast(R.string.fetch_account_fail)
            return false
        }

        when (accountType) {
            BankingManager.LOCAL_ACCOUNT_BANK -> {
                val accountCount = getAccountCountByType(accountType, response.cardsList)
                val bankLimit = response.bankLimit
                if (bankLimit.isNotEmpty() && accountCount >= bankLimit.toInt()) {
                    showToast("银行账户设置已达上限：$bankLimit")
                    return false
                }
            }
            BankingManager.LOCAL_ACCOUNT_ALIPAY -> if ((response.alipayList?.size ?: 0) >= 1) {
                showToast("支付宝账户设置已达上限：1")
                return false
            }
            BankingManager.LOCAL_ACCOUNT_USDT -> {
                val accountCount2 = getAccountCountByType(accountType, response.cardsList)
                val usdtLimit = response.usdtLimit
                if (usdtLimit.isNotEmpty() && accountCount2 >= usdtLimit.toInt()) {
                    showToast("USDT账户设置已达上限：$usdtLimit")
                    return false
                }
            }
            BankingManager.LOCAL_ACCOUNT_GOPAY -> {
                val accountCountGopay = getAccountCountByType(accountType, response.cardsList)
                val gopayLimit = response.gopayLimit
                if (gopayLimit.isNotEmpty() && accountCountGopay >= gopayLimit.toInt()) {
                    showToast("GoPay账户设置已达上限：$gopayLimit")
                    return false
                }
            }
            BankingManager.LOCAL_ACCOUNT_OKPAY -> {
                val accountCountOkpay = getAccountCountByType(accountType, response.cardsList)
                val okpayLimit = response.okpayLimit
                if (!TextUtils.isEmpty(okpayLimit) && accountCountOkpay >= okpayLimit.toInt()) {
                    showToast("OkPay账户设置已达上限：$okpayLimit")
                    return false
                }
            }
            BankingManager.LOCAL_ACCOUNT_TOPAY -> {
                val accountCountTopay = getAccountCountByType(accountType, response.cardsList)
                val topayLimit = response.topayLimit
                if (topayLimit.isNotEmpty() && accountCountTopay >= topayLimit.toInt()) {
                    showToast("ToPay账户设置已达上限：$topayLimit")
                    return false
                }
            }
            BankingManager.LOCAL_ACCOUNT_VPAY -> {
                val accountCountVpay = getAccountCountByType(accountType, response.cardsList)
                val vpayLimit = response.vpayLimit
                if (vpayLimit.isNotEmpty() && accountCountVpay >= vpayLimit.toInt()) {
                    showToast("VPay帐户设置已达上限$vpayLimit")
                    return false
                }
            }
        }
        return true
    }

    private fun getAccountCountByType(accountType: Int, accountBeans: List<AccountBean>): Int {
        // bean的type已经在 adaptAccounts 时校正为app端使用的定义了
        return accountBeans.count { it.type == accountType }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_ACCOUNT) {
                if (!hasSetAccountPassword) {
                    checkAccountFromWeb()
                } else {
                    fetchWithdrawAccounts()
                }
            } else if (requestCode == SET_BANK_PASSWORD) {
                checkAccountFromWeb()
            } else if (requestCode == COMPLETE_BANK) {
                fetchWithdrawAccounts()
            }
        }
    }
}