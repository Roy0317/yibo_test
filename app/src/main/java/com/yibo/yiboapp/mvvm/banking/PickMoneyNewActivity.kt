package com.yibo.yiboapp.mvvm.banking

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.AccountDetailListActivity
import com.yibo.yiboapp.activity.PickMoneyUpdateBankActivity
import com.yibo.yiboapp.activity.UserCenterActivity
import com.yibo.yiboapp.data.Constant
import com.yibo.yiboapp.data.DrawDataResponse
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.ActivityPickMoneyNewBinding
import com.yibo.yiboapp.entify.Card
import com.yibo.yiboapp.entify.CardInfo
import com.yibo.yiboapp.entify.SysConfig
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.hideSoftInput
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min


class PickMoneyNewActivity : BaseActivityKotlin() {

    companion object{
        private const val  PICK_ACCOUNT_REQUEST = 1
        private const val  UPDATE_BANK_CARD = 2
    }

    private lateinit var binding: ActivityPickMoneyNewBinding
    private val sysConfig: SysConfig by lazy { UsualMethod.getConfigFromJson(this) }
    private var userName: String = ""
    private var accountBalance = 0.0
    private var drawDataResponse: DrawDataResponse? = null
    private var cardInfo: CardInfo? = null
    private var card: Card? = null
    private var tokenNumber: String = ""
    private var isPlayUser = false //是否是后台建的试玩账号，后台建的试玩账号就可以无视打码量直接提款
    private var needUpdCardNo = false
    private var jobCheckMoney: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickMoneyNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountBalance = intent.getDoubleExtra("balance", 0.0)
        isPlayUser = YiboPreference.instance(this).accountMode == Constant.ACCOUNT_PLATFORM_TEST_GUEST
        initView()
        fetchDrawDataNew()
    }

    private fun initView(){
        binding.title.textTitle.setText(R.string.pick_money_string)
        binding.title.textAction1.setText(R.string.pick_money_record)
        binding.title.textAction1.setOnClickListener { openRecordPage() }

        binding.textUserName.text = "用户名:${ManagerFactory.userManager.getLoginAccount()}"
        binding.textBalance.text = String.format("余额：%.2f元", accountBalance)

        if(!sysConfig.custom_tips.isNullOrEmpty()){
            binding.textWithdrawInfo.visibility = View.VISIBLE
            binding.textWithdrawInfo.text = sysConfig.custom_tips
        }else{
            binding.textWithdrawInfo.visibility = View.GONE
        }
        UsualMethod.LoadUserImage(this, binding.header)
        setBgImage()

        binding.title.imageBack.setOnClickListener {
            hideSoftInput()
            finish()
        }

        binding.imageMore.setOnClickListener {
            hideSoftInput()
            startActivity(Intent(this, UserCenterActivity::class.java))
        }

        binding.linearAccountType.setOnClickListener {
            val intent = BankingManager.openAccountManagerPage(this, true)
            startActivityForResult(intent, PICK_ACCOUNT_REQUEST)
        }

        binding.imageInfoFee.setOnClickListener {
            if(binding.textInfoFee.visibility == View.VISIBLE){
                binding.imageInfoFee.setImageResource(R.drawable.icon_info)
                binding.imageTriangle.visibility = View.GONE
                binding.textInfoFee.visibility = View.GONE
            }else{
                binding.imageInfoFee.setImageResource(R.drawable.icon_info_red)
                binding.imageTriangle.visibility = View.VISIBLE
                binding.textInfoFee.visibility = View.VISIBLE
                showWithdrawFeeStrategy()
            }
        }

        binding.linearWithdrawInfo.setOnClickListener {
            val visible = binding.constraintWithdrawInfo.visibility == View.VISIBLE
            binding.constraintWithdrawInfo.visibility = if(visible) View.GONE else View.VISIBLE
            binding.imageInfoArrow.setImageResource(if(visible) R.drawable.icon_arrow_right else R.drawable.icon_arrow_down)
        }

        binding.buttonConfirm.setOnClickListener {
            if (card == null) {
                showToast(R.string.choose_withdraw_account)
                return@setOnClickListener
            } else if (card!!.type == 1 && needUpdCardNo) {
                showUpdateBankDialog(userName)
                return@setOnClickListener
            }

            postPickMoney(cardInfo!!, card!!)
        }

        binding.editMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(seq: CharSequence, i: Int, i1: Int, i2: Int) {
                //如果第一个数字为0，第二个不为点，就不允许输入
                var s = seq
                if (s.toString().startsWith("0") && s.toString().trim().length > 1) {
                    if (s.toString().substring(1, 2) != ".") {
                        binding.editMoney.setText(s.subSequence(0, 1))
                        binding.editMoney.setSelection(1)
                        return
                    }
                }
                //如果第一为点，直接显示0.
                if (s.toString().startsWith(".")) {
                    binding.editMoney.setText("0.")
                    binding.editMoney.setSelection(2)
                    return
                }
                //限制输入小数位数(2位)
                if (s.toString().contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 2 + 1)
                        binding.editMoney.setText(s)
                        binding.editMoney.setSelection(s.length)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {
                binding.textFee.text = calculateFee(drawDataResponse?.content?.strategy, editable.toString())
                updateTokenAmountByRate()

                //输入数字一秒后发出请求确认金额符合后台限制
                lifecycleScope.launch{
                    if(jobCheckMoney?.isCompleted != true)
                        jobCheckMoney?.cancelAndJoin()
                    jobCheckMoney = launch {
                        delay(1000)
                        checkMoneyInput()
                    }
                }
            }
        })
    }

    private fun fetchDrawDataNew(){
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.fetchDrawDataNew() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.success){
                    drawDataResponse = response
                    updateInfo(response)
                    fetchWithdrawAccounts()
                }else{
                    showToast(getString(R.string.check_safety_fail) + ": ${response.msg}")
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.check_safety_fail) + ": ${result.message}")
            }
        }
    }

    private fun fetchWithdrawAccounts() {
        //取得多银行卡模式的账户资讯
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.fetchAccountsNew() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.success){
                    val infoList = response.cardInfoList
                    if(infoList.isEmpty()){
                        showAddAccountDialog()
                    }else{
                        showDefaultAccount(infoList)
                    }
                }else{
                    showToast(getString(R.string.fetch_account_fail) + ": ${response.msg}")
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.fetch_account_fail) + ": ${result.message}")
            }
        }
    }

    private fun setBgImage() {
        val bgUrl = sysConfig.member_center_bg_url
        if (!bgUrl.isNullOrEmpty()) {
            Glide.with(this).asBitmap()
                .load(bgUrl.trim())
                .into(object: SimpleTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.linearWithdrawBG.background = BitmapDrawable(resource)
                    }
                })
        }
    }

    private fun openRecordPage() {
        AccountDetailListActivity.createIntent(this, true)
    }

    private fun postPickMoney(cardInfo: CardInfo, card: Card) {
        val money: String = binding.editMoney.text.toString().trim()
        val pickPwd: String = binding.editPassword.text.toString().trim()

        if(money.isEmpty()){
            showToast("提款金额不可以为空")
            return
        }

        if(pickPwd.isEmpty()){
            showToast("提款密码不可以为空")
            return
        }

        val params = HashMap<String, String>()
        params["withdrawalType"] = cardInfo.drawRecordType.toString()
        params["money"] = money
        params["usdtRate"] = cardInfo.rate.toString()
        params["usdtNum"] = binding.textTokenNumber.text.toString()
        params["cardId"] = card.id.toString()
        params["repPwd"] = pickPwd

        Utils.LOG(TAG, "postPickMoney(), params = $params")
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.postPickMoneyNew(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    showToast("提交成功，请等待订单处理完成")
                    binding.editMoney.setText("")
                    binding.editPassword.setText("")
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

    private fun showWithdrawFeeStrategy(){
        val strategy = drawDataResponse?.content?.strategy
        if(strategy == null){
            binding.textInfoFee.text = ""
            return
        }

        val feeValue = String.format("%.2f", strategy.feeValue)
        val feeString = if(strategy.feeType == 1) "${feeValue}元" else "${feeValue}%"
        val message = "免费提款<font color=#C81011>${strategy.drawNum}</font>次后，每次提款收取<font color=#C81011>${feeString}</font>手续费"
        binding.textInfoFee.text = Html.fromHtml(message)
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

    private fun updateInfo(response: DrawDataResponse?){
        val content = response?.content ?: return

        if (!content.enablePick) {
            binding.buttonConfirm.setOnClickListener {
                showToast(content.drawFlag)
            }
        }
        userName = content.member.userName
        needUpdCardNo = content.needUpdCardNo
        if (needUpdCardNo) {
            showUpdateBankDialog(userName)
        }

        accountBalance = content.member.money
        binding.textBalance.text = String.format("余额：%.2f元", accountBalance)
        binding.textTransferTime.text = "${content.start}-${content.end}"
        val arrivalTime = content.arrivalTime
        val mins = if("0" == arrivalTime) "1-3" else arrivalTime
        binding.textTransferDueTime.text = String.format("提款%s分钟内到账。(如未到账，请联系在线客服)", mins)

        val freeTimes = if(content.strategy != null) max(content.strategy.drawNum - content.curWnum, 0) else 0
        binding.textWithdrawTimes.text = if(content.wnum == 0) "不限" else "${content.wnum - content.curWnum}次(含免费${freeTimes}次)"
        if (freeTimes == 0) {
            binding.textWithdrawTimes.text = "${content.wnum - content.curWnum}次"
        }

        val str = when {
            content.checkBetNum.toString().contains("E") -> BigDecimal(content.checkBetNum.toString()).toPlainString()
            content.checkBetNum == -1.0 -> "-1"
            else -> content.checkBetNum.toString()
        }
        binding.textCurrentBetMoney.text = content.member.betNum.toString()
        binding.textNeedBetMoney.text = if ((str == "-1")) "无限" else str
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

    private fun showDefaultAccount(infoList: List<CardInfo>) {
        cardInfo = infoList.firstOrNull { it.cardList.isNotEmpty() }
        card = if(cardInfo?.cardList?.isEmpty() != true) cardInfo!!.cardList[0] else null
        if (!needUpdCardNo && card != null) {
            showAccountInfo(cardInfo!!, card!!)
        }
    }

    /**
     * 顯示選定的帳戶信息
     */
    private fun showAccountInfo(cardInfo: CardInfo, card: Card) {
        binding.textAccountType.text = cardInfo.typeName
        binding.tagAccount.text = if(cardInfo.typeNumber == 1) "银行卡号" else "${cardInfo.typeName}地址"
        binding.textAccount.text = if(cardInfo.typeNumber == 1) "${card.cardNo}(${card.bankName})"
                                    else card.cardNo
        binding.linearBankAddress.visibility = if(cardInfo.typeNumber == 1) View.VISIBLE else View.GONE
        binding.textBankAddress.text = card.bankAddress.ifNullOrEmpty { "" }
        when(cardInfo.typeNumber){
            1 ->{
                binding.linearName.visibility = View.VISIBLE
                binding.tagName.text = "开户姓名"
                binding.textName.text = card.realName.ifNullOrEmpty { "" }
            }
            2 -> binding.linearName.visibility = View.GONE
            else ->{
                binding.linearName.visibility = View.VISIBLE
                binding.tagName.text = "${cardInfo.typeName}实名"
                binding.textName.text = card.realName.ifNullOrEmpty { "" }
            }
        }

        val stationMin = drawDataResponse?.content?.min?.toDouble() ?: 0.0
        val stationMax =
            if (drawDataResponse?.content?.max.isNullOrEmpty() || drawDataResponse?.content?.max == "0") {
                Double.MAX_VALUE
            } else {
                drawDataResponse?.content?.max?.toDouble()!!
            }
        val cardMax = if(cardInfo.maxDraw == 0.0) Double.MAX_VALUE else cardInfo.maxDraw
        val realMin = max(stationMin, cardInfo.minDraw)
        val realMax = min(stationMax, cardMax)
        if (realMin == 0.0 && realMax == Double.MAX_VALUE) {
            binding.textMoneyRange.text = "无限制"
        } else if (realMin == 0.0) {
            binding.textMoneyRange.text = "上限$realMax"
        } else if (realMax == Double.MAX_VALUE) {
            binding.textMoneyRange.text = "$realMin - 无限制"
        } else {
            binding.textMoneyRange.text = "$realMin - $realMax"
        }

        binding.textRate.text = cardInfo.rate.toString()
    }

    private fun calculateFee(strategy: DrawDataResponse.Content.Strategy?, moneyString: String): String{
        val freeTimes: Int
        val withdrawFee: Double
        val withdrawFeePercent: Double

        if (strategy != null) {
            val curNum = drawDataResponse?.content?.curWnum ?: 0
            freeTimes = max(strategy.drawNum - curNum, 0)
            if(strategy.feeType == 1){
                withdrawFee = strategy.feeValue
                withdrawFeePercent = -1.0
            }else{
                withdrawFee = 0.0
                withdrawFeePercent = strategy.feeValue
            }
        }else {
            withdrawFee = 0.0
            withdrawFeePercent = -1.0
            freeTimes = 0
        }

        if(freeTimes != 0)
            return "0"

        val tempFee = if (moneyString.isNotEmpty()) {
            if (withdrawFeePercent >= 0) {
                BigDecimal(moneyString)
                    .multiply(BigDecimal(withdrawFeePercent))
                    .divide(BigDecimal("100"))
                    .setScale(2, BigDecimal.ROUND_DOWN)
                    .toDouble()
            }else {
                withdrawFee
            }
        }else 0.0

        val lowerLimit = strategy?.lowerLimit ?: 0.0
        val upperLimit = if(strategy?.upperLimit == null || strategy.upperLimit == 0.0) Double.MAX_VALUE else strategy.upperLimit
        return min(max(tempFee, lowerLimit), upperLimit).toString()
    }

    /**
     * 依据提款类别的汇率计算数量
     */
    private fun updateTokenAmountByRate() {
        val rate = cardInfo?.rate ?: return
        val s = binding.editMoney.text.toString()

        if (s.isNotBlank()) {
            try {
                val money = s.toDouble()
                val number = floor(money/rate * 100) / 100
                val decimalFormat = DecimalFormat(".00")
                tokenNumber = decimalFormat.format(number)
                binding.textTokenNumber.text = tokenNumber
            } catch (e: Exception) {
                e.printStackTrace()
                tokenNumber = "0"
                binding.textTokenNumber.text = "0"
            }
        } else {
            tokenNumber = "0"
            binding.textTokenNumber.text = "0"
        }
    }

    private suspend fun checkMoneyInput(){
        val type = cardInfo?.drawRecordType ?: return
        val money = binding.editMoney.text.toString()
        val result = httpRequest { ManagerFactory.bankingManager.checkWithdrawMoneyInput(type.toString(), money) }
        if(result is HttpResult.Success){
            val response = result.data
            binding.textMoneyWarning.visibility = if(response.success) View.GONE else View.VISIBLE
            binding.textMoneyWarning.text = response.msg ?: ""
        }else if(result is HttpResult.Error){
            binding.textMoneyWarning.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK)
            return

        when (requestCode) {
            PICK_ACCOUNT_REQUEST -> {
                val json1 = data?.getStringExtra("cardInfo")
                val json2 = data?.getStringExtra("card")
                cardInfo = Gson().fromJson(json1, CardInfo::class.java)
                card = Gson().fromJson(json2, Card::class.java)
                if(cardInfo == null || card == null){
                    showToast("选择账号结果处理错误")
                    return
                }

                if (card!!.type == 1 && needUpdCardNo) {
                    showUpdateBankDialog(userName)
                } else {
                    showAccountInfo(cardInfo!!, card!!)
                    updateTokenAmountByRate()
                }
                updateInfo(drawDataResponse)
            }
            UPDATE_BANK_CARD -> needUpdCardNo = false
        }
    }
}