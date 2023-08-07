package com.yibo.yiboapp.mvvm.banking

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.AccountDetailListActivity
import com.yibo.yiboapp.activity.ConfirmPayActivity
import com.yibo.yiboapp.activity.UserCenterActivity
import com.yibo.yiboapp.data.*
import com.yibo.yiboapp.databinding.ActivityChargeSimpleBinding
import com.yibo.yiboapp.entify.BankPay
import com.yibo.yiboapp.entify.FastPay
import com.yibo.yiboapp.entify.OnlinePay
import com.yibo.yiboapp.entify.PayMethodResult
import com.yibo.yiboapp.entify.SubmitPayResultWraper
import com.yibo.yiboapp.entify.SysConfig
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.hideSoftInput
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.ui.PayMethodWindow
import com.yibo.yiboapp.ui.PayMethodWindow.PaySelectListener
import com.yibo.yiboapp.utils.Utils
import crazy_wrapper.Crazy.CrazyResult
import crazy_wrapper.Crazy.dialog.ActionSheetDialog
import crazy_wrapper.Crazy.response.SessionResponse
import crazy_wrapper.RequestManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.net.URLEncoder
import java.text.DecimalFormat

class ChargeSimpleActivity : BaseActivityKotlin() {

    private lateinit var binding: ActivityChargeSimpleBinding

    val SUBMIT_PAY = 0x02

    private val config: SysConfig by lazy { UsualMethod.getConfigFromJson(this) }
    private var minmoney = 0f
    private var popWindow: PayMethodWindow? = null
    private var isUSDT = false
    private var usdtRate = "0"
    private val decimalFormat = DecimalFormat("0.00")
    private var selectPayType = BankingManager.PAY_METHOD_ONLINE //已选择的支付方式
    private var selectPosition = 0//对应支付方式下的支付列表中已选择的位置
    private var payMethodResult: PayMethodResult? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargeSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        fetchChargeMethods()
    }

    private fun initView() {
        //赋值帐户名及余额
        val accountName = intent.getStringExtra("account")
        val moneyName = intent.getStringExtra("money")

        binding.title.textTitle.text = getString(R.string.charge_money)
        binding.title.textAction1.text = getString(R.string.charge_record)
        binding.title.textAction1.setOnClickListener { AccountDetailListActivity.createIntent(this) }
        binding.header.setOnClickListener{ UserCenterActivity.createIntent(this) }
        binding.confirm.setOnClickListener { onConfirmClicked() }
        binding.editUSDTNumber.addTextChangedListener(USDTInputWatcher())
        binding.usdtClean.setOnClickListener{ binding.editUSDTNumber.setText("") }
        binding.inputMoney.addTextChangedListener(InputContentChangeListener(binding.inputMoney))
        binding.summary.addTextChangedListener(InputContentChangeListener(binding.summary))
        binding.textCopyReceiver.setOnClickListener{ copyReceiver() }
        binding.copyBank.setOnClickListener{
            if (binding.bankAddress.text.toString().contains("开户行：")) {
                UsualMethod.copy(binding.bankAddress.text.toString().replace("开户行：", ""), this)
                showToast("复制成功")
            }
        }
        binding.qrcode.setOnLongClickListener {
            showChooseSaveModeList(binding.qrcode)
            false
        }
        binding.moneyClean.setOnClickListener{ binding.inputMoney.setText("") }
        binding.summarClean.setOnClickListener{ binding.summary.setText("") }
        binding.textUsername.text = accountName
        binding.textAccountBalance.text = String.format(getString(R.string.bottom_money_format), moneyName.ifNullOrEmpty { "0" })
        binding.changeMethod.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        binding.changeMethod.paint.isAntiAlias = true
        binding.changeMethod.setOnClickListener{
            if (payMethodResult != null) {
                showPayMethodWindow(payMethodResult!!, selectPayType, selectPosition)
            } else {
                showToast("暂无支付方式，无法更换")
            }
        }

        //点击主界面时，隐藏键盘
        binding.linearParent.setOnClickListener{ hideSoftInput() }
        UsualMethod.LoadUserImage(this, binding.header)
    }

    private fun fetchChargeMethods() {
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.fetchChargeMethods() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    //更新充值方式
                    payMethodResult = response.content
                    if (payMethodResult != null) {
                        var json = ""
                        val fast = payMethodResult!!.fast
                        if (!fast.isNullOrEmpty()) {
                            val fastPay = fast[selectPosition]
                            json = Gson().toJson(fastPay, FastPay::class.java)
                            selectPayType = BankingManager.PAY_METHOD_FAST
                        }
                        val bank = payMethodResult!!.bank
                        if (!bank.isNullOrEmpty()) {
                            val bankPay = bank[selectPosition]
                            json = Gson().toJson(bankPay, BankPay::class.java)
                            selectPayType = BankingManager.PAY_METHOD_BANK
                        }
                        val online = payMethodResult!!.online
                        if (!online.isNullOrEmpty()) {
                            val onlinePay = online[selectPosition]
                            json = Gson().toJson(onlinePay, OnlinePay::class.java)
                            selectPayType = BankingManager.PAY_METHOD_ONLINE
                        }
                        updatePayInfo(selectPayType, json)
                    }
                }else{
                    showToast(getString(R.string.acquire_pay_method_fail) + ":${response.msg}")
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.acquire_pay_method_fail) + ":${result.message}")
            }
        }
    }

    private fun showPayMethodWindow(result: PayMethodResult, type: Int, position: Int) {
        if (popWindow == null) {
            popWindow = PayMethodWindow(this)
        }

        popWindow?.setData(result, type, position)
        popWindow?.setPaySelectListener(PaySelectListener { type, payJson, position ->
            if (payJson.isNullOrEmpty()) {
                return@PaySelectListener
            }

            selectPayType = type
            selectPosition = position
            updatePayInfo(type, payJson)
        })

        if (popWindow?.isShowing != true) {
            popWindow?.showWindow(binding.linearParent)
        }
    }

    //更新界面上对应支付方式的支付信息
    private fun updatePayInfo(type: Int, json: String) {
        if (json.isEmpty()) {
            return
        }

        if (type == BankingManager.PAY_METHOD_ONLINE) {
            isUSDT = false
            showUSDTEntries(false)
            val onlinePay = Gson().fromJson(json, OnlinePay::class.java)
            binding.moneyTip.text = String.format(getString(R.string.min_charge_money_format), onlinePay.minFee)
            minmoney = onlinePay.minFee.toFloat()
            binding.summary.visibility = View.GONE
            PayMethodWindow.updateImage(this, binding.chargeMethodImg, onlinePay.icon, onlinePay.payType)
            binding.bankPayTip.visibility = View.GONE
            binding.linearReceiver.visibility = View.GONE
            binding.linearBank.visibility = View.GONE
            binding.qrcodeArea.visibility = View.GONE
        } else if (type == BankingManager.PAY_METHOD_FAST) {
            val fastPay = Gson().fromJson(json, FastPay::class.java)
            isUSDT = "USDT".equals(fastPay.payName, ignoreCase = true)
            showUSDTEntries(isUSDT)
            binding.moneyTip.text = String.format(getString(R.string.min_charge_money_format), fastPay.minFee)
            minmoney = fastPay.minFee.toFloat()
            binding.summary.visibility = View.VISIBLE
            binding.summary.hint = "${fastPay.frontLabel}(请输入正确的${fastPay.frontLabel}，否则无法到帐)"
            PayMethodWindow.updateImage(this, binding.chargeMethodImg, fastPay.icon, "")
            binding.bankPayTip.visibility = View.GONE
            binding.linearReceiver.visibility = View.VISIBLE
            binding.textReceiver.text = "收款人：${fastPay.receiveName.ifNullOrEmpty{"暂无姓名"}}(${fastPay.receiveAccount.ifNullOrEmpty{"暂无帐号"}})"
            binding.linearBank.visibility = View.GONE
            binding.qrcodeArea.visibility = View.VISIBLE
            if (!fastPay.qrCodeImg.isNullOrEmpty()) {
                val glideUrl = UsualMethod.getGlide(this, fastPay.qrCodeImg.trim { it <= ' ' })
                val options = RequestOptions().placeholder(R.drawable.default_placeholder_picture)
                    .error(R.drawable.default_placeholder_picture)
                Glide.with(this).load(glideUrl).apply(options).into(binding.qrcode)
            }
        } else if (type == BankingManager.PAY_METHOD_BANK) {
            isUSDT = false
            showUSDTEntries(false)
            val bankPay = Gson().fromJson(json, BankPay::class.java)
            binding.moneyTip.text = String.format(getString(R.string.min_charge_money_format), bankPay.minFee)
            minmoney = bankPay.minFee.toFloat()
            binding.summary.visibility = View.VISIBLE
            binding.summary.hint = "请输入存款人姓名"
            PayMethodWindow.updateImage(this, binding.chargeMethodImg, bankPay.icon, "")
            binding.bankPayTip.visibility = View.VISIBLE
            binding.linearReceiver.visibility = View.VISIBLE
            binding.textReceiver.text = "收款人：${bankPay.receiveName.ifNullOrEmpty{"暂无姓名"}}(${bankPay.bankCard.ifNullOrEmpty{"暂无帐号"}})"
            binding.linearBank.visibility = View.VISIBLE
            val bank = if (!bankPay.bankAddress.isNullOrEmpty())
                String.format("开户行：%s(%s)", bankPay.bankAddress, bankPay.payBankName) else "开户行：暂无开户行"
            binding.bankAddress.text = bank
            binding.qrcodeArea.visibility = View.GONE
        }
    }

    private inner class InputContentChangeListener constructor(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val name = charSequence.toString().trim { it <= ' ' }
            if (Utils.isEmptyString(name)) {
                if (editText.id == R.id.input_money) {
                    binding.moneyClean.visibility = View.GONE
                } else {
                    binding.summarClean.visibility = View.GONE
                }
            } else {
                if (editText.id == R.id.input_money) {
                    if (!isUSDT) {
                        binding.moneyClean.visibility = View.VISIBLE
                    }
                } else {
                    binding.summarClean.visibility = View.VISIBLE
                }
            }
        }
        override fun afterTextChanged(editable: Editable) {}
    }

    private inner class USDTInputWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val number = s.toString()
            if (TextUtils.isEmpty(number)) {
                binding.usdtClean.visibility = View.GONE
                binding.inputMoney.setText("0")
            } else {
                binding.usdtClean.visibility = View.VISIBLE
                val tempMoney: Double = usdtRate.toDouble() * number.trim { it <= ' ' }.toInt()
                binding.inputMoney.setText(decimalFormat.format(tempMoney))
            }
        }
        override fun afterTextChanged(s: Editable) {}
    }

    private fun showUSDTEntries(isUSDT: Boolean) {
        if (isUSDT) {
            decimalFormat.roundingMode = RoundingMode.HALF_UP
            binding.linearUSDT.visibility = View.VISIBLE
            binding.textMark.text = "USDT账号"
            if (config.usdt_rate.isNullOrEmpty()) {
                showToast("请联系客服设置汇率")
                usdtRate = ""
                binding.textUSDTRate.text = ""
            } else {
                usdtRate = config.usdt_rate
                binding.textUSDTRate.text = config.usdt_rate
            }
            binding.editUSDTNumber.requestFocus()
            binding.inputMoney.isEnabled = false
        } else {
            binding.linearUSDT.visibility = View.GONE
            binding.textMark.text = "转帐备注"
            binding.inputMoney.isEnabled = true
        }
    }

    //显示保存二维码选择框
    private fun showChooseSaveModeList(imageView: ImageView) {
        val stringItems = arrayOf("保存二维码", "识别二维码")
        val dialog = ActionSheetDialog(this, stringItems, null)
        dialog.title("选择方式")
        dialog.isTitleShow(true).show()
        dialog.setOnOperItemClickL { parent, view, position, id ->
            dialog.dismiss()
            saveQrCode(position == 1, imageView)
        }
    }

    /**
     * @param regAction 是否需要识别并跳转扫码
     */
    private fun saveQrCode(regAction: Boolean, imageView: ImageView) {
        if (payMethodResult == null || selectPayType != BankingManager.PAY_METHOD_FAST) {
            //只有扫码支付时才有将二维码存本地功能
            return
        }

        val fast = payMethodResult!!.fast ?: return
        val fastPay = fast[selectPosition] ?: return
        val url = fastPay.qrCodeImg
        if (Utils.isEmptyString(url)) {
            showToast("二维码地址为空，无法保存")
            return
        }

        Glide.with(this).asBitmap().load(url.trim { it <= ' ' })
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        if (regAction) {
                            showToast("正在识别...")
                            val saveSuccess = UsualMethod.saveBitmapToSD(BankingManager.QRCODE_NAME, bitmap)
                            if (saveSuccess) {
                                decoderQRFromPic(imageView.drawable)
                            }
                        } else {
                            showToast("正在保存二维码...")
                            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "qrcode", "save_qrcode")
                            showToast("二维码图片已保存,请使用微信,支付宝等进行扫码付款")
                        }
                    } catch (e: Exception) {
                        showToast("保存失败，请重试")
                        e.printStackTrace()
                    }
                }
            })
    }

    /**
     * 解析图片中的二维码
     */
    private fun decoderQRFromPic(drawable: Drawable) {
        lifecycleScope.launch(Dispatchers.IO){
            val qrcode = QRCodeDecoder.syncDecodeQRCode(Utils.drawableToBitmap(drawable))
            withContext(Dispatchers.Main){
                if (!qrcode.isNullOrEmpty()) {
                    if (qrcode.contains("wxp")) {
                        UsualMethod.toWeChatScan(this@ChargeSimpleActivity)
                    } else if (qrcode.contains("ALIPAY", true)) {
                        if (UsualMethod.hasInstalledAlipayClient(this@ChargeSimpleActivity))
                            UsualMethod.startAlipayClient(this@ChargeSimpleActivity, qrcode)
                        else {
                            showToast("没有检测到支付宝客户端")
                        }
                    } else {
                        showToast("没有检测到相应的扫码客户端，请打开软件从相册获取二维码手动扫描")
                    }
                } else {
                    showToast("没有检测到相应的扫码客户端，请打开软件从相册获取二维码手动扫描")
                }
            }
        }
    }

    //复制收款帐号
    private fun copyReceiver() {
        if (payMethodResult == null) {
            showToast("没有帐号可以复制")
            return
        }

        val account = when(selectPayType){
            BankingManager.PAY_METHOD_FAST ->{
                val fast = payMethodResult!!.fast
                if(fast.isNullOrEmpty()) "" else fast[selectPosition].receiveAccount.ifNullOrEmpty { "" }
            }
            BankingManager.PAY_METHOD_BANK ->{
                val bank = payMethodResult!!.bank
                if(bank.isNullOrEmpty() || bank[selectPosition].bankCard.isNullOrEmpty())
                    ""
                else {
                    val bankPay = bank[selectPosition]
                    String.format("%s(%s)", bankPay.receiveName, bankPay.bankCard)
                }
            }
            else -> ""
        }

        if(account.isEmpty()){
            showToast("没有帐号可以复制")
        }else{
            UsualMethod.copy(account, this)
            showToast("复制成功")
        }
    }

    private fun onConfirmClicked(){
        val time = YiboPreference.instance(this).confirmtime
        val configtime = config.deposit_interval_times
        if (!configtime.isNullOrEmpty()) {
            val configtimelong = configtime.toLong()
            if (System.currentTimeMillis() - time < configtimelong * 1000) {
                showToast("两次充值间隔不能小于${configtimelong}秒")
                return
            }
        }
        val account = binding.summary.text.toString().trim { it <= ' ' }
        var money = binding.inputMoney.text.toString().trim { it <= ' ' }
        val usdtNumber = binding.editUSDTNumber.text.toString()
        if (isUSDT) {
            if (usdtRate == "0") {
                showToast("请联系客服设置汇率")
                return
            } else if (usdtNumber.isEmpty()) {
                showToast("请输入USDT数量")
                return
            } else if (account.isEmpty()) {
                showToast("请输入USDT账号")
                return
            }
        }
        if (money.isEmpty()) {
            showToast("请输入充值金额")
            return
        }
        if (!Utils.isNumber(money)) {
            showToast("请输入正确格式的金额")
            return
        }
        if (money.contains(".") && money.length - 1 > money.indexOf(".") + 2) {
            showToast("金额不能超过小数第二位")
            return
        }
        val fm = money.toFloat()
        if (fm < minmoney) {
            showToast("最低充值金额" + minmoney + "元")
            return
        }
        if (!isUSDT) {
            money = ManagerFactory.bankingManager.addMoneyFloat(config.onoff_show_pay_quick_addmoney, money)
        }

        if (selectPayType == BankingManager.PAY_METHOD_FAST) {
            val fastPay = payMethodResult?.fast?.get(selectPosition) ?: return
            val params = if (isUSDT) {
                hashMapOf(
                    "payMethod" to selectPayType.toString(),
                    "rate" to usdtRate,
                    "ruantity" to usdtNumber,
                    "depositor" to account,
                    "money" to money,
                    "transactionNo" to account,
                    "payId" to fastPay.id.toString())
            } else {
                hashMapOf(
                    "payMethod" to selectPayType.toString(),
                    "money" to money,
                    "account" to account,
                    "payId" to fastPay.id.toString())
            }
            postChargeData(params)
        } else if (selectPayType == BankingManager.PAY_METHOD_BANK) {
            val bankPay = payMethodResult?.bank?.get(selectPosition) ?: return
            val params = hashMapOf(
                "payMethod" to selectPayType.toString(),
                "money" to money,
                "depositor" to account,
                "bankId" to bankPay.id.toString())
            postChargeData(params)
        } else if (selectPayType == BankingManager.PAY_METHOD_ONLINE) {
            //在线支付不提交后台订单
            //actionSubmitPay(selectPayType,selectPosition,money,"","");
            val onlinePay = payMethodResult!!.online[selectPosition]
            val payMethodName = onlinePay.payName
            val accountName = intent.getStringExtra("account")
            val chargeMoney = binding.inputMoney.text.toString().trim { it <= ' ' }
            val payJson = Gson().toJson(onlinePay, OnlinePay::class.java)
            ConfirmPayActivity.createIntent(
                this, "", accountName, chargeMoney, payMethodName, null,
                null, null, null, null, selectPayType, payJson)
            return
        }
    }

    private fun postChargeData(params: Map<String, String>){
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.postChargeData(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(!response.isSuccess){
                    showToast(response.msg.ifNullOrEmpty { getString(R.string.post_fail) })
                    checkSessionValidity(response.code)
                    return@launch
                }

                showToast("订单提交成功")
                val orderNo = response.content
                val accountName = intent.getStringExtra("account")
                val chargeMoney = binding.inputMoney.text.toString().trim { it <= ' ' }
                binding.inputMoney.setText("")
                binding.summary.setText("")
                binding.editUSDTNumber.setText("")
                if (selectPayType == BankingManager.PAY_METHOD_FAST) {
                    val fastPay = payMethodResult!!.fast[selectPosition]
                    val payMethodName = fastPay.payName
                    val dipositorAccount = binding.summary.text.toString().trim { it <= ' ' }
                    ConfirmPayActivity.createIntent(
                        this@ChargeSimpleActivity, orderNo, accountName, chargeMoney, payMethodName,
                        null, null, null, dipositorAccount, fastPay.qrCodeImg, selectPayType, ""
                    )
                }else if (selectPayType == BankingManager.PAY_METHOD_BANK) {
                    val bankPay = payMethodResult!!.bank[selectPosition]
                    val payMethodName = bankPay.payName
                    val receiveName = bankPay.receiveName
                    val receiveAccount = bankPay.bankCard
                    val dipositor = binding.summary.text.toString().trim { it <= ' ' }
                    ConfirmPayActivity.createIntent(
                        this@ChargeSimpleActivity,
                        orderNo, accountName, chargeMoney, payMethodName, receiveName, receiveAccount,
                        dipositor, null, null, selectPayType, "")
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.post_fail) + ":${result.message}")
            }
        }
    }
}