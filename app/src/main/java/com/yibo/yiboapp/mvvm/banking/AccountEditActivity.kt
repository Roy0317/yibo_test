package com.yibo.yiboapp.mvvm.banking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.yibo.yiboapp.R
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.databinding.ActivityAccountEditKotlinBinding
import com.yibo.yiboapp.entify.PostBankWrapper
import com.yibo.yiboapp.entify.SysConfig
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.utils.Utils
import com.yibo.yiboapp.views.accountmanager.*
import com.yibo.yiboapp.views.accountmanager.AccountEntryView.EntryBean
import kotlinx.coroutines.launch

class AccountEditActivity : BaseActivityKotlin() {

    companion object{
        private const val ACCOUNT_TYPE = "account_type"
        private const val EDIT_TYPE = "edit_type"
        private const val PASSWORD = "password"
        private const val REALNAME = "realName"

        const val ACCOUNT_BANK = 1
        const val ACCOUNT_ALIPAY = 2
        const val ACCOUNT_USDT = 3
        const val ACCOUNT_GOPAY = 5
        const val ACCOUNT_OKPAY = 6
        const val ACCOUNT_TOPAY = 7
        const val ACCOUNT_VPAY = 8


        fun newIntent(context: Context, accountType: Int, toAddAccount: Boolean, needWithdrawPassword: Boolean, realName: String): Intent {
            val intent = Intent(context, AccountEditActivity::class.java)
            intent.putExtra(ACCOUNT_TYPE, accountType)
            intent.putExtra(EDIT_TYPE, toAddAccount)
            intent.putExtra(PASSWORD, needWithdrawPassword)
            intent.putExtra(REALNAME, realName)
            return intent
        }
    }


    private lateinit var binding: ActivityAccountEditKotlinBinding
    private val sysConfig: SysConfig by lazy { UsualMethod.getConfigFromJson(this) }
    private val entryViews: MutableList<AccountEntryView> = ArrayList()
    private var accountType = 0
    private var toAddAccount = false
    private var needPassword = false
    private var chooseBankDialog: ChooseBankDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountEditKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initParams()
    }

    private fun initParams(){
        accountType = intent.getIntExtra(ACCOUNT_TYPE, ACCOUNT_BANK)
        toAddAccount = intent.getBooleanExtra(EDIT_TYPE, true)
        needPassword = intent.getBooleanExtra(PASSWORD, false)
        val entryBeans = generateEntryBeans(accountType)
        generateEntryViews(entryBeans)

        binding.title.middleTitle.text = if (toAddAccount) "新增帐户" else "修改帐户"
        binding.title.backText.setOnClickListener { onBackPressed() }
        binding.buttonConfirm.setOnClickListener { onConfirmClicked() }
    }

    private fun onConfirmClicked(){
        val params: Map<String, String>? = wrapParams()
        if (params != null) {
            Utils.LOG(TAG, "params = $params")
            if (sysConfig.member_multi_bank_card_permission.isOn()) {
                postAddMultiAccount(params, accountType)
            } else {
                postAddAccount(params)
            }
        }
    }

    private fun generateEntryBeans(accountType: Int): List<EntryBean> {
        val beans = ArrayList<EntryBean>()
        when (accountType) {
            ACCOUNT_BANK -> {
                beans.add(EntryBean("bankName", "开户银行", "请输入开户银行", "", AccountEntryView.TYPE_SELECTION))
                //modify johnson 2020-11-28 修改持久人姓名开关开，或者注册时没有填写过真实姓名时；需要显示姓名栏
                //modify larry 2021-01-13 增加能不能修改姓名的属性
                val name = intent.getStringExtra(REALNAME)
                val isNameEditable = sysConfig.draw_money_user_name_modify.isOn() || name.isNullOrEmpty()
                beans.add(EntryBean("userName", "开户姓名", "请输入开户预留姓名", name, AccountEntryView.TYPE_INPUT, true, isNameEditable))
                beans.add(EntryBean("bankAddress", "银行地址", "请输入开户银行地址", "", AccountEntryView.TYPE_INPUT, true))
                beans.add(EntryBean("cardNo", "银行卡号", "银行卡号必须是16至19个数字组成", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("cardNo_Check", "确认卡号", "请再次输入银行卡号", "", AccountEntryView.TYPE_INPUT))
                if (needPassword) {
                    beans.add(EntryBean("repPwd", "提款密码", "请输入提款密码", "", AccountEntryView.TYPE_PASSWORD))
                }
            }
            ACCOUNT_USDT -> {
                beans.add(EntryBean("usdtAddress", "USDT地址", "请输入30-50个数字和字母组合", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("usdtAddress_Check", "确认地址", "请再次输入USDT地址", "", AccountEntryView.TYPE_INPUT))
                if (needPassword) {
                    beans.add(EntryBean("repPwd", "提款密码", "请输入提款密码", "", AccountEntryView.TYPE_PASSWORD))
                }
            }
            ACCOUNT_ALIPAY -> {
                beans.add(EntryBean("alipayName", "支付宝实名", "请输入支付宝实名", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("alipayAccount", "支付宝帐号", "请输入支付宝帐号", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("alipayAccount_Check", "确认帐号", "请再次输入支付宝帐号", "", AccountEntryView.TYPE_INPUT))
                if (needPassword) {
                    beans.add(EntryBean("repPwd", "提款密码", "请输入提款密码", "", AccountEntryView.TYPE_PASSWORD))
                }
            }
            ACCOUNT_GOPAY -> {
                beans.add(EntryBean("gopayName", "GoPay实名", "请输入GoPay实名", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("gopayAddress", "GoPay地址", "请输入16-18个数字和字母组合", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("gopayAddress_Check", "确认地址", "请再次输入GoPay地址", "", AccountEntryView.TYPE_INPUT))
                if (needPassword) {
                    beans.add(EntryBean("repPwd", "提款密码", "请输入提款密码", "", AccountEntryView.TYPE_PASSWORD))
                }
            }
            ACCOUNT_OKPAY -> {
                beans.add(EntryBean("okpayName", "OkPay实名", "请输入OkPay实名", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("okpayAddress", "OkPay地址", "请输入16个数字和字母组合", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("okpayAddress_Check", "确认地址", "请再次输入OkPay地址", "", AccountEntryView.TYPE_INPUT))
                if (needPassword) {
                    beans.add(EntryBean("repPwd", "提款密码", "请输入提款密码", "", AccountEntryView.TYPE_PASSWORD))
                }
            }
            ACCOUNT_TOPAY -> {
                beans.add(EntryBean("topayName", "ToPay实名", "请输入ToPay实名", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("topayAddress", "ToPay地址", "请输入ToPay地址", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("topayAddress_Check", "确认地址", "请再次输入ToPay地址", "", AccountEntryView.TYPE_INPUT))
                if (needPassword) {
                    beans.add(EntryBean("repPwd", "提款密码", "请输入提款密码", "", AccountEntryView.TYPE_PASSWORD))
                }
            }
            ACCOUNT_VPAY -> {
                beans.add(EntryBean("vpayName", "VPay实名", "请输入VPay实名", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("vpayAddress", "VPay地址", "请输入VPay地址", "", AccountEntryView.TYPE_INPUT))
                beans.add(EntryBean("vpayAddress_Check", "确认地址", "请再次输入VPay地址", "", AccountEntryView.TYPE_INPUT))
                if (needPassword) {
                    beans.add(EntryBean("repPwd", "提款密码", "请输入提款密码", "", AccountEntryView.TYPE_PASSWORD))
                }
            }
        }
        return beans
    }

    private fun generateEntryViews(entryBeans: List<EntryBean>) {
        var entryView: AccountEntryView?
        binding.linearEntry.removeAllViews()
        entryViews.clear()
        for (bean in entryBeans) {
            entryView = null
            when (bean.inputType) {
                AccountEntryView.TYPE_HINT -> entryView = AccountEntryHintView(this, bean)
                AccountEntryView.TYPE_INPUT, AccountEntryView.TYPE_PASSWORD -> entryView = AccountEntryInputView(this, bean)
                AccountEntryView.TYPE_SELECTION -> {
                    val view = AccountEntrySelectionView(this, bean)
                    view.setSelectionClickListener { v: View? -> showChooseBankDialog() }
                    entryView = view
                }
            }
            if (entryView != null) {
                binding.linearEntry.addView(entryView)
                entryViews.add(entryView)
            }
        }
    }

    private fun showChooseBankDialog() {
        val banksArray = resources.getStringArray(R.array.bank_card)
        val banks = listOf(*banksArray)
        if (chooseBankDialog == null) {
            chooseBankDialog = ChooseBankDialog(this, banks)
            chooseBankDialog!!.setChooseBankListener { position: Int, bankName: String? ->
                chooseBankDialog!!.dismiss()
                val entryView = entryViews.find { it.originalEntryBean.tag == "bankName" }
                if (entryView is AccountEntrySelectionView) {
                    chooseBankDialog!!.setChosenPosition(position)
                    entryView.setContent(bankName)
                    if (position == banks.size - 1) {
                        //選擇了其他銀行
                        entryView.showOtherEntry("银行名称", "请输入银行名称")
                    } else {
                        entryView.hideOtherEntry()
                    }
                }
            }
        }
        chooseBankDialog?.show()
    }

    private fun wrapParams(): Map<String, String>? {
        val params: MutableMap<String, String> = HashMap()
        var checkItem: EntryBean? = null
        for (entryView in entryViews) {
            val bean = entryView.updatedEntryBean
            if (bean != null && bean.isRequired) {
                //AccountEntryHintView 会回传null
                val valueStr = bean.content
                if (bean.isRequired && valueStr.isNullOrEmpty()) {
                    showToast(bean.title + "不可为空")
                    return null
                }
                params[bean.tag] = valueStr
                if (bean.tag.endsWith("_Check")) {
                    checkItem = bean
                }
            }
        }

        //检查确认项目的输入是否一致
        if (checkItem != null) {
            val tag = checkItem.tag.replace("_Check", "")
            val value = params[tag]
            val checkValue = params[checkItem.tag]
            if (value != null && value != checkValue) {
                Utils.LOG(TAG, "wrapParams(), $params")
                showToast(checkItem.title + "输入值不一致，请再次确认")
                return null
            }
        }

        params["type"] = accountType.toString()
        if(!params.containsKey("userName")){
            params["userName"] = intent.getStringExtra(REALNAME) ?: ""
        }
        return params
    }

    private fun postAddAccount(params: Map<String, String>) {
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.postAddAccount(params) }
            onGetAccountResponse(result)
        }
    }

    private fun postAddMultiAccount(params: Map<String, String>, type: Int) {
        val configUrl = StringBuilder()
        configUrl.append(Urls.BASE_URL).append(Urls.PORT)
        when (type) {
            BankingManager.LOCAL_ACCOUNT_ALIPAY -> configUrl.append(Urls.POST_NEW_ALIPAY_ACCOUNT_URL)
            BankingManager.LOCAL_ACCOUNT_USDT -> configUrl.append(Urls.POST_NEW_USDT_ACCOUNT_URL)
            BankingManager.LOCAL_ACCOUNT_GOPAY -> configUrl.append(Urls.POST_NEW_GOPAY_ACCOUNT_URL)
            BankingManager.LOCAL_ACCOUNT_OKPAY -> configUrl.append(Urls.POST_NEW_OKPAY_ACCOUNT_URL)
            BankingManager.LOCAL_ACCOUNT_TOPAY -> configUrl.append(Urls.POST_NEW_TOPAY_ACCOUNT_URL)
            BankingManager.LOCAL_ACCOUNT_VPAY -> configUrl.append(Urls.POST_NEW_VPAY_ACCOUNT_URL)
            BankingManager.LOCAL_ACCOUNT_BANK -> configUrl.append(Urls.POST_NEW_BANK_ACCOUNT_URL)
            else -> configUrl.append(Urls.POST_NEW_BANK_ACCOUNT_URL)
        }

        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.postAddMultiAccount(configUrl.toString(), params) }
            onGetAccountResponse(result)
        }
    }

    private fun onGetAccountResponse(result: HttpResult<PostBankWrapper>){
        if(result is HttpResult.Success){
            val response = result.data
            if(response.isSuccess){
                showToast(R.string.setting_account_data_success)
                setResult(RESULT_OK)
                finish()
            }else{
                showToast("新增帐户发生错误，${response.msg}")
            }
        }else if(result is HttpResult.Error){
            showToast("新增帐户发生错误! ${result.message}")
        }
    }
}