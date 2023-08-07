package com.yibo.yiboapp.mvvm.banking

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.AccountDetailListActivity
import com.yibo.yiboapp.activity.PickMoneyUpdateBankActivity
import com.yibo.yiboapp.data.Constant
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.ActivityPickMoneyKotlinBinding
import com.yibo.yiboapp.entify.PickAccountMultiResponse
import com.yibo.yiboapp.entify.PickAccountMultiResponse.AccountBean
import com.yibo.yiboapp.entify.PickAccountResponse
import com.yibo.yiboapp.entify.PickMoneyData
import com.yibo.yiboapp.entify.SysConfig
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.*
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.floor

class PickMoneyActivity : BaseActivityKotlin() {

    companion object{
        private const val  PICK_ACCOUNT_REQUEST = 1
        private const val  UPDATE_BANK_CARD = 2
    }

    private lateinit var binding: ActivityPickMoneyKotlinBinding

    private val sysConfig: SysConfig by lazy { UsualMethod.getConfigFromJson(this) }
    private var userName: String = ""
    private var mockName: String = ""  //只显示部份的会员姓名
    private var pickMoneyData: PickMoneyData? = null
    private var pickAccount: AccountBean? = null
    private var accountBalance = 0.0
    private var isPlayUser = false //是否是后台建的试玩账号，后台建的试玩账号就可以无视打码量直接提款
    private var needUpdCardNo = false
    private var withdrawFee = -1f //手續費比例
    private var usdtRate: String = ""
    private var usdtNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickMoneyKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        isPlayUser = YiboPreference.instance(this).accountMode == Constant.ACCOUNT_PLATFORM_TEST_GUEST
        checkAccountFromWeb()
    }

    private fun initView(){
        binding.title.middleTitle.setText(R.string.pick_money_string)
        binding.title.rightText.visibility = View.VISIBLE
        binding.title.rightText.setText(R.string.pick_money_record)
        if(!sysConfig.custom_tips.isNullOrEmpty()){
            binding.customTips.visibility = View.VISIBLE
            binding.customTips.text = sysConfig.custom_tips
        }else{
            binding.customTips.visibility = View.GONE
        }
        UsualMethod.LoadUserImage(this, binding.header)
        setBgImage()

        binding.title.backText.setOnClickListener {
            hideSoftInput()
            finish()
        }
        binding.title.rightText.setOnClickListener { goRecords() }



        binding.linearAccount.setOnClickListener {
            val intent = BankingManager.openAccountManagerPage(this, true)
            startActivityForResult(intent, PICK_ACCOUNT_REQUEST)
        }

        binding.next.setOnClickListener {
            if (pickAccount == null) {
                showToast(R.string.choose_withdraw_account)
                return@setOnClickListener
            } else if (pickAccount!!.type == BankingManager.LOCAL_ACCOUNT_BANK && needUpdCardNo) {
                showUpdateBankDialog(userName)
                return@setOnClickListener
            }

            val pickMoneyValue: String = binding.inputMoney.text.toString().trim()
            val pickPwdValue: String = binding.inputPwd.text.toString().trim()
            if (sysConfig.member_multi_bank_card_permission.isOn()) {
                postPickMoneyMulti(pickAccount!!, pickMoneyValue, pickPwdValue)
            } else {
                postPickMoney(pickAccount!!, pickMoneyValue, pickPwdValue)
            }
        }

        binding.inputMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(seq: CharSequence, i: Int, i1: Int, i2: Int) {
                //如果第一个数字为0，第二个不为点，就不允许输入
                var s = seq
                if (s.toString().startsWith("0") && s.toString().trim().length > 1) {
                    if (s.toString().substring(1, 2) != ".") {
                        binding.inputMoney.setText(s.subSequence(0, 1))
                        binding.inputMoney.setSelection(1)
                        return
                    }
                }
                //如果第一为点，直接显示0.
                if (s.toString().startsWith(".")) {
                    binding.inputMoney.setText("0.")
                    binding.inputMoney.setSelection(2)
                    return
                }
                //限制输入小数位数(2位)
                if (s.toString().contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 2 + 1)
                        binding.inputMoney.setText(s)
                        binding.inputMoney.setSelection(s.length)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (!TextUtils.isEmpty(editable.toString()) && editable.toString().length < 10) {
                    if (withdrawFee >= 0) {
                        val money = editable.toString().toFloat()
                        val value = Utils.keepTwoDecimal(money * withdrawFee / 100)
                        binding.actPickMoneyServiceFee.text = value
                    }
                } else {
                    binding.actPickMoneyServiceFee.text = "0.00"
                }
                if (binding.linearUSDT.visibility == View.VISIBLE) {
                    updateUSDTNumber()
                }
            }
        })
    }

    //检查出款银行帐号等信息是否正确
    private fun checkAccountFromWeb() {
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.checkAccountInfo() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    userName = response.content.userName
                }else{
                    showToast(response.msg.ifNullOrEmpty { getString(R.string.check_safety_fail) })
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.check_safety_fail) + ": ${result.message}")
            }

            pickDataFromWeb()
        }
    }

    private fun pickDataFromWeb() {
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.pickMoneyData() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    pickMoneyData = response.content
                    updateValues(pickMoneyData)
                    fetchWithdrawAccounts()
                }else{
                    showToast(getString(R.string.sync_tikuan_info_fail) + ": ${response.msg}")
                    binding.content.visibility = View.GONE
                    binding.listviewEmpty.item.visibility = View.VISIBLE
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.sync_tikuan_info_fail) + ": ${result.message}")
                binding.content.visibility = View.GONE
                binding.listviewEmpty.item.visibility = View.VISIBLE
            }
        }
    }

    private fun fetchWithdrawAccounts() {
        if (sysConfig.member_multi_bank_card_permission.isOn()) {
            //取得多银行卡模式的账户资讯
            lifecycleScope.launch{
                val result = httpRequest { ManagerFactory.bankingManager.fetchMultiAccounts() }
                if(result is HttpResult.Success){
                    val response = result.data
                    val pickAccounts = adaptAccounts(response)
                    if(pickAccounts.isEmpty()){
                        showAddAccountDialog()
                    }else{
                        showDefaultBankAccount(pickAccounts)
                    }
                }else if(result is HttpResult.Error){
                    showToast(getString(R.string.fetch_account_fail) + ": ${result.message}")
                }
            }
        } else {
            //取得单银行卡模式的账户资讯
            lifecycleScope.launch{
                val result = httpRequest { ManagerFactory.bankingManager.fetchSingleAccounts() }
                if(result is HttpResult.Success){
                    val response = result.data
                    val pickAccounts = adaptAccounts(response)
                    if(pickAccounts.isEmpty()){
                        showAddAccountDialog()
                    }else{
                        showDefaultBankAccount(pickAccounts)
                    }
                }else if(result is HttpResult.Error){
                    showToast(getString(R.string.fetch_account_fail) + ": ${result.message}")
                }
            }
        }
    }

    private fun setBgImage() {
        //根据后台配置的头部背景图片地址设置背景
        val bgUrl = sysConfig.member_center_bg_url
        if (!bgUrl.isNullOrEmpty()) {
            updateHeaderBG(binding.llBackground, bgUrl)
        } else {
            binding.llBackground.background = resources.getDrawable(R.drawable.member_page_header_bg)
        }
    }


    private fun updateHeaderBG(bg: LinearLayout, imgUrl: String) {
        if (!imgUrl.isNullOrEmpty()) {
            Glide.with(this).asBitmap()
                .load(imgUrl.trim())
                .into(object: SimpleTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bg.background = BitmapDrawable(resource)
                    }
                })
        }
    }

    private fun goRecords() {
        AccountDetailListActivity.createIntent(this, true)
    }

    private fun postPickMoneyMulti(account: AccountBean, money: String, pickPwd: String) {
        val params = HashMap<String, String>()
        params["money"] = money
        params["repPwd"] = pickPwd
        params["withdrawalType"] = account.type.toString()
        when (account.type) {
            BankingManager.LOCAL_ACCOUNT_ALIPAY -> {}
            BankingManager.LOCAL_ACCOUNT_GOPAY, BankingManager.LOCAL_ACCOUNT_OKPAY,
            BankingManager.LOCAL_ACCOUNT_TOPAY, BankingManager.LOCAL_ACCOUNT_VPAY -> {
                params["cardId"] = account.id.toString()
            }
            BankingManager.LOCAL_ACCOUNT_USDT -> {
                params["cardId"] = account.id.toString()
                params["usdtAddress"] = account.cardNo
                params["usdtRate"] = usdtRate
                params["usdtNum"] = usdtNumber
            }
            BankingManager.LOCAL_ACCOUNT_BANK -> {
                params["cardId"] = account.id.toString()
                params["bankName"] = account.bankName
            }
            else -> {
                params["cardId"] = account.id.toString()
                params["bankName"] = account.bankName
            }
        }

        Utils.LOG(TAG, "postPickMoneyMulti(), params = $params")
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.postPickMoneyMulti(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    showToast("提交成功，请等待订单处理完成")
                    binding.inputMoney.setText("")
                    binding.inputPwd.setText("")
                    delay(1200)
                    finish()
                }else{
                    showToast(getString(R.string.post_fail) + ": ${response.msg}")
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.post_fail) + ": ${result.message}")
            }
        }
    }

    private fun postPickMoney(account: AccountBean, money: String, pickPwd: String) {
        val params = HashMap<String, String>()
        params["money"] = money
        params["repPwd"] = pickPwd
        params["type"] = account.type.toString()
        when (account.type) {
            BankingManager.LOCAL_ACCOUNT_ALIPAY,
            BankingManager.LOCAL_ACCOUNT_GOPAY, BankingManager.LOCAL_ACCOUNT_OKPAY,
            BankingManager.LOCAL_ACCOUNT_TOPAY, BankingManager.LOCAL_ACCOUNT_VPAY -> {}
            BankingManager.LOCAL_ACCOUNT_USDT -> {
                params["usdtAddress"] = account.cardNo
                params["usdtRate"] = usdtRate
                params["usdtNum"] = usdtNumber
            }
            BankingManager.LOCAL_ACCOUNT_BANK -> {
                params["bankName"] = account.bankName
            }
            else -> {
                params["bankName"] = account.bankName
            }
        }

        Utils.LOG(TAG, "postPickMoney(), params = $params")
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.postPickMoney(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    showToast("提交成功，请等待订单处理完成")
                    binding.inputMoney.setText("")
                    binding.inputPwd.setText("")
                    delay(1200)
                    finish()
                }else{
                    showToast(getString(R.string.post_fail) + ": ${response.msg}")
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.post_fail) + ": ${result.message}")
            }
        }
    }

    private fun showUpdateBankDialog(userName: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.update_bank_card)
            .setMessage(R.string.edit_bank_card_force_hint)
            .setPositiveButton("前往修改") { dialog1: DialogInterface?, which: Int ->
                val intent = PickMoneyUpdateBankActivity.createIntent(this, userName)
                startActivityForResult(intent, UPDATE_BANK_CARD)
            }
            .show()
    }

    private fun updateValues(data: PickMoneyData?) {
        data ?: return

        needUpdCardNo = data.isNeedUpdCardNo
        if (needUpdCardNo) {
            showUpdateBankDialog(userName)
        }
        mockName = data.userName
        binding.name.text = data.userName
        accountBalance = if (intent.getDoubleExtra("balance", 0.0) == 0.0) {
            data.accountBalance.toDouble()
        } else {
            intent.getDoubleExtra("balance", 0.0)
        }
        var channelMaxAmount = "無限制"
        var channelMinAmount = "無限制"
        if (data.drawLimitMap != null && pickAccount != null) {
            when (pickAccount!!.type) {
                BankingManager.LOCAL_ACCOUNT_ALIPAY -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_alipay
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_alipay
                }
                BankingManager.LOCAL_ACCOUNT_USDT -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_usdt
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_usdt
                }
                BankingManager.LOCAL_ACCOUNT_GOPAY -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_gopay
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_gopay
                }
                BankingManager.LOCAL_ACCOUNT_OKPAY -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_okpay
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_okpay
                }
                BankingManager.LOCAL_ACCOUNT_TOPAY -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_topay
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_topay
                }
                BankingManager.LOCAL_ACCOUNT_VPAY -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_vpay
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_vpay
                }
                BankingManager.LOCAL_ACCOUNT_BANK -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_bank
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_bank
                }
                else -> {
                    channelMinAmount = data.drawLimitMap.withdraw_min_money_bank
                    channelMaxAmount = data.drawLimitMap.withdraw_max_money_bank
                }
            }
        }
        binding.leftMoney.text = String.format("%.2f元", accountBalance)
        val tipContent = StringBuilder()
        tipContent.append("提示信息:\n\n")
            .append("每天的提款处理时间为：" + data.startTime).append("至")
            .append(data.endTime).append(";\n")

        val arrivalTime = data.arrivalTime
        tipContent.append("提款")
            .append(if(arrivalTime.isNullOrEmpty() || "0" == arrivalTime) "1-3" else arrivalTime)
            .append("分钟内到账。(如未到账，请联系在线客服)\n")

        tipContent.append("用户每日最小提款 ").append(data.minPickMoney).append(" 元，")
            .append("最大提款 ").append(data.maxPickMoney).append(" 元;\n")

        if (pickAccount != null) {
            tipContent.append("通道金额限制：$channelMinAmount 元 - $channelMaxAmount 元;\n")
        }

        tipContent.append("今日可提款 ").append(if(data.wnum == -1) "不限" else data.wnum)
            .append(" 次，已提款").append(data.curWnum).append("次\n")

        if (data.strategy != null) {
            binding.actPickMoneyServiceLayout.visibility = View.VISIBLE
            val num = data.strategy.drawNum - data.curWnum
            binding.actPickMoneyServiceFee.text = "0.00"
            if (num > 0) {
                binding.actPickMoneyFreeTimes.text = "$num"
            } else {
                binding.actPickMoneyFreeTimes.text = "0"
                if (data.strategy.feeType == 1) {
                    binding.actPickMoneyServiceFee.text = "${data.strategy.feeValue}"
                } else {
                    withdrawFee = data.strategy.feeValue
                }
            }

            tipContent.append("手续费说明 ：免费提款次数 ").append(data.strategy.drawNum)
                .append("次，超过次数后按每次提款收取")
                .append(if (data.strategy.feeType == 1) "${data.strategy.feeValue}元手续费" else "每次提款金额的")
                .append("${data.strategy.feeValue}%收取手续费")
        }

        tipContent.append("\n是否能提款：").append(if (data.isEnablePick) "是" else "否").append("\n")
        binding.tipInfo.text = tipContent.toString()


        val str = when {
            data.checkBetNum.toString().contains("E") -> BigDecimal(data.checkBetNum.toString()).toPlainString()
            data.checkBetNum == -1f -> "-1"
            else -> data.checkBetNum.toString()
        }
        val consumeContent = StringBuilder()
        consumeContent.append("消费比例:\n\n")
            .append("出款需达投注量：")
            .append(if ((str == "-1")) "无限" else str)
            .append(",有效投注金额${data.validBetMoney}元")

        val needMoney= BigDecimal(data.checkBetNum.toString()).subtract(BigDecimal(data.validBetMoney.toString()))
        needMoney.setScale(2, BigDecimal.ROUND_HALF_UP)
        if (needMoney.toDouble() >= 0) {
            consumeContent.append("\n还需投注：").append(needMoney.toPlainString())
        }
        consumeContent.append("\n")

        val isGuest = (YiboPreference.instance(this).accountMode == Constant.ACCOUNT_PLATFORM_TEST_GUEST)
        if (!isGuest) {
            binding.customRate.text = consumeContent.toString()
        } else {
            binding.customRate.visibility = View.GONE
        }

        if (isPlayUser) {
            binding.cantPickToast.visibility = View.GONE
            binding.next.isEnabled = true
        } else {
            //显示赋值是否可提款
            if (!data.isEnablePick) {
                binding.cantPickToast.visibility = View.VISIBLE
                if (data.curWnum == data.wnum) {
                    val toast = "今天已提款" + data.curWnum + "次，不能再提款"
                    binding.cantPickToast.text = toast
                    showToast(toast)
                } else {
                    binding.cantPickToast.text = data.drawFlag
                    showToast(data.drawFlag.ifNullOrEmpty { "提款条件未达到，无法提款" })
                }
                binding.next.isEnabled = false
            } else {
                binding.cantPickToast.visibility = View.GONE
                binding.next.isEnabled = true
            }
        }
    }

    private fun adaptAccounts(response: PickAccountResponse): List<AccountBean> {
        val results: MutableList<AccountBean> = ArrayList()
        if (response.content != null && response.content.pickAccounts != null) {
            for (account in response.content.pickAccounts) {
                if (!TextUtils.isEmpty(account.alipayAccount)) {
                    val accountBean = AccountBean()
                    accountBean.type = BankingManager.LOCAL_ACCOUNT_ALIPAY
                    accountBean.cardNo = account.alipayAccount
                    accountBean.userName = account.alipayName
                    results.add(accountBean)
                } else if (!TextUtils.isEmpty(account.gopayAccount)) {
                    val accountBean = AccountBean()
                    accountBean.type = BankingManager.LOCAL_ACCOUNT_GOPAY
                    accountBean.cardNo = account.gopayAccount
                    accountBean.userName = account.gopayName
                    results.add(accountBean)
                } else if (!TextUtils.isEmpty(account.okpayAccount)) {
                    val accountBean = AccountBean()
                    accountBean.type = BankingManager.LOCAL_ACCOUNT_OKPAY
                    accountBean.cardNo = account.okpayAccount
                    accountBean.userName = account.okpayName
                    results.add(accountBean)
                } else if (!TextUtils.isEmpty(account.topayaccount)) {
                    val accountBean = AccountBean()
                    accountBean.type = BankingManager.LOCAL_ACCOUNT_TOPAY
                    accountBean.cardNo = account.topayaccount
                    accountBean.userName = account.topayName
                    results.add(accountBean)
                } else if (!TextUtils.isEmpty(account.vpayaccount)) {
                    val accountBean = AccountBean()
                    accountBean.type = BankingManager.LOCAL_ACCOUNT_VPAY
                    accountBean.cardNo = account.vpayaccount
                    accountBean.userName = account.vpayName
                    results.add(accountBean)
                } else if (!TextUtils.isEmpty(account.usdtAddress)) {
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
                    accountBean.userName = account.userName
                    results.add(accountBean)
                }
            }
        }
        return results
    }

    /**
     * 开启多银行卡功能与否会决定使用哪个接口，要整理接口回传的data
     */
    private fun adaptAccounts(response: PickAccountMultiResponse): List<AccountBean> {
        val results: MutableList<AccountBean> = ArrayList()
        for (account in response.cardsList) {
            if (account.type == 6) {  //type 6  = vpay
                /** 对账户的type做校正，衔接app端的账户类型设定  */
                account.type = BankingManager.LOCAL_ACCOUNT_VPAY
            }
            if (account.type == 5) {
                account.type = BankingManager.LOCAL_ACCOUNT_TOPAY
            }
            if (account.type == 4) { //type 4  = okpay
                account.type = BankingManager.LOCAL_ACCOUNT_OKPAY
            }
            if (account.type == 3) { //type 3  = gopay
                account.type = BankingManager.LOCAL_ACCOUNT_GOPAY
            }
            if (account.type == 2) { //type 2 = usdt
                account.type = BankingManager.LOCAL_ACCOUNT_USDT
            }
            results.add(account)
        }

        if (response.alipayList != null) {
            for (account in response.alipayList) {
                account.type = BankingManager.LOCAL_ACCOUNT_ALIPAY
                results.add(account)
            }
        }

        /**  依据账户的种类进行排序  */
        results.sortWith(Comparator { o1: AccountBean, o2: AccountBean -> o1.type - o2.type })
        return results
    }

    private fun showAddAccountDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.choose_withdraw_account)
            .setMessage(R.string.add_withdraw_account)
            .setPositiveButton("前往设置") { dialog1: DialogInterface?, which: Int ->
                val intent = BankingManager.openAccountManagerPage(this, true)
                startActivityForResult(intent, PICK_ACCOUNT_REQUEST)
            }
            .show()
    }

    private fun showDefaultBankAccount(accounts: List<AccountBean>) {
        val bankAccount = accounts.firstOrNull { it.type == BankingManager.LOCAL_ACCOUNT_BANK }
        if (!needUpdCardNo && bankAccount != null) {
            bankAccount.realName = mockName
            pickAccount = bankAccount
            updateAccountInfo(bankAccount)
        }
    }

    private fun updateAccountInfo(account: AccountBean) {
        when (account.type) {
            BankingManager.LOCAL_ACCOUNT_ALIPAY -> {
                binding.textAccount.text = "支付宝"
                binding.linearAlipay.visibility = View.VISIBLE
                binding.linearBank.visibility = View.GONE
                binding.linearUSDT.visibility = View.GONE
                binding.textAlipay.text = "支付宝帐号：${account.cardNo}"
                binding.textAlipayName.text = "支付宝实名：" + account.realName.ifNullOrEmpty{ account.userName }
            }
            BankingManager.LOCAL_ACCOUNT_USDT -> {
                binding.textAccount.text = "USDT"
                binding.linearAlipay.visibility = View.GONE
                binding.linearBank.visibility = View.GONE
                binding.linearUSDT.visibility = View.VISIBLE
                binding.textUSDTAddress.text = "USDT地址：" + account.cardNo
                if (!TextUtils.isEmpty(sysConfig.usdt_out_rate)) {
                    usdtRate = sysConfig.usdt_out_rate
                    binding.textUSDTRate.text = "USDT汇率：$usdtRate"
                } else if (!TextUtils.isEmpty(sysConfig.usdt_rate)) {
                    usdtRate = sysConfig.usdt_rate
                    binding.textUSDTRate.text = "USDT汇率：$usdtRate"
                } else {
                    usdtRate = "0"
                    binding.textUSDTRate.text = "USDT汇率：请联系客服设置汇率"
                }
                updateUSDTNumber()
            }
            BankingManager.LOCAL_ACCOUNT_GOPAY -> {
                binding.textAccount.text = "GoPay"
                binding.linearAlipay.visibility = View.VISIBLE
                binding.linearBank.visibility = View.GONE
                binding.linearUSDT.visibility = View.GONE
                binding.textAlipay.text = "GoPay地址：" + account.cardNo
                binding.textAlipayName.text = "GoPay实名：" + account.realName.ifNullOrEmpty{ account.userName }
            }
            BankingManager.LOCAL_ACCOUNT_OKPAY -> {
                binding.textAccount.text = "OkPay"
                binding.linearAlipay.visibility = View.VISIBLE
                binding.linearBank.visibility = View.GONE
                binding.linearUSDT.visibility = View.GONE
                binding.textAlipay.text = "OkPay地址：" + account.cardNo
                binding.textAlipayName.text = "OkPay实名：" + account.realName.ifNullOrEmpty{ account.userName }
            }
            BankingManager.LOCAL_ACCOUNT_TOPAY -> {
                binding.textAccount.text = "ToPay"
                binding.linearAlipay.visibility = View.VISIBLE
                binding.linearBank.visibility = View.GONE
                binding.linearUSDT.visibility = View.GONE
                binding.textAlipay.text = "ToPay地址：" + account.cardNo
                binding.textAlipayName.text = "ToPay实名：" + account.realName.ifNullOrEmpty{ account.userName }
            }
            BankingManager.LOCAL_ACCOUNT_VPAY -> {
                binding.textAccount.text = "Vpay"
                binding.linearAlipay.visibility = View.VISIBLE
                binding.linearBank.visibility = View.GONE
                binding.linearUSDT.visibility = View.GONE
                binding.textAlipay.text = "VPay地址：" + account.cardNo
                binding.textAlipayName.text = "VPay实名：" + account.realName.ifNullOrEmpty{ account.userName }
            }
            BankingManager.LOCAL_ACCOUNT_BANK -> {
                binding.textAccount.text = "银行卡"
                binding.linearAlipay.visibility = View.GONE
                binding.linearBank.visibility = View.VISIBLE
                binding.linearUSDT.visibility = View.GONE
                binding.textBank.text = "银行卡：" + account.bankName + " - " + account.cardNo
                binding.textRealName.text = "真实姓名：" + account.realName
            }
            else -> {
                binding.textAccount.text = "银行卡"
                binding.linearAlipay.visibility = View.GONE
                binding.linearBank.visibility = View.VISIBLE
                binding.linearUSDT.visibility = View.GONE
                binding.textBank.text = "银行卡：" + account.bankName + " - " + account.cardNo
                binding.textRealName.text = "真实姓名：" + account.realName
            }
        }
    }

    private fun updateUSDTNumber() {
        if (usdtRate.isNullOrEmpty() || usdtRate == "0") {
            binding.textUSDTNumber.text = "USDT数量：0"
            return
        }

        val s = binding.inputMoney.text.toString()
        if (!TextUtils.isEmpty(s)) {
            try {
                val money = s.toDouble()
                val rate = usdtRate.toDouble()
                var number = money / rate
                number = floor(number * 100) / 100
                val decimalFormat = DecimalFormat(".00")
                usdtNumber = decimalFormat.format(number)
                binding.textUSDTNumber.text = String.format("USDT数量：%s", usdtNumber)
            } catch (e: Exception) {
                e.printStackTrace()
                binding.textUSDTNumber.text = "USDT数量：0"
            }
        } else {
            binding.textUSDTNumber.text = "USDT数量：0"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK)
            return

        when (requestCode) {
            PICK_ACCOUNT_REQUEST -> {
                val json = data?.getStringExtra("account")
                pickAccount = Gson().fromJson(json, AccountBean::class.java)
                if(pickAccount == null){
                    showToast("选择账号结果处理错误")
                    return
                }

                if (pickAccount!!.type == BankingManager.LOCAL_ACCOUNT_BANK && needUpdCardNo) {
                    showUpdateBankDialog(userName)
                } else {
                    updateAccountInfo(pickAccount!!)
                }
                updateValues(pickMoneyData)
            }
            UPDATE_BANK_CARD -> needUpdCardNo = false
        }
    }
}