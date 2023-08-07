package com.yibo.yiboapp.mvvm.banking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.yibo.yiboapp.R
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.databinding.ActivityAccountEditNewBinding
import com.yibo.yiboapp.entify.CardInfo
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
import kotlinx.coroutines.launch

class AccountEditNewActivity : BaseActivityKotlin() {

    companion object{
        private const val CARD_INFO = "card_info"
        private const val REALNAME = "realName"

        fun newIntent(context: Context, json: String, realName: String): Intent {
            val intent = Intent(context, AccountEditNewActivity::class.java)
            intent.putExtra(CARD_INFO, json)
            intent.putExtra(REALNAME, realName)
            return intent
        }
    }



    private lateinit var binding: ActivityAccountEditNewBinding
    private val sysConfig: SysConfig by lazy { UsualMethod.getConfigFromJson(this) }
    private lateinit var cardInfo: CardInfo
    private val entryViews: MutableList<AccountEntryView> = ArrayList()
    private var chooseBankDialog: ChooseBankDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountEditNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initParams()
    }

    private fun initParams(){
        val json = intent.getStringExtra(CARD_INFO) ?: ""
        cardInfo = Gson().fromJson(json, CardInfo::class.java)
        val entryBeans = generateEntryBeans(cardInfo)
        generateEntryViews(entryBeans)

        binding.title.middleTitle.text = "添加${cardInfo.typeName}"
        binding.title.backText.setOnClickListener { onBackPressed() }
        binding.buttonConfirm.setOnClickListener {
            val params = wrapParams() ?: return@setOnClickListener
//            Utils.LOG(TAG, "params = $params")
            postAddAccount(params)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun generateEntryBeans(cardInfo: CardInfo): List<AccountEntryView.EntryBean> {
        val beans = ArrayList<AccountEntryView.EntryBean>()
        val name = intent.getStringExtra(REALNAME)
        val isNameEditable = sysConfig.draw_money_user_name_modify.isOn() || name.isNullOrEmpty()

        cardInfo.cardProperties.forEach { property ->
            if(property.propertyKey == "realName"){
                beans.add(AccountEntryView.EntryBean(
                    property.propertyKey,
                    property.propertyName,
                    "请输入${property.propertyName}",
                    name,
                    AccountEntryView.TYPE_INPUT,
                    true,
                    isNameEditable
                ))
            }else{
                beans.add(AccountEntryView.EntryBean(
                    property.propertyKey,
                    property.propertyName,
                    "请输入${property.propertyName}",
                    "",
                    if(property.propertyDom == "selectWithOther") AccountEntryView.TYPE_SELECTION else AccountEntryView.TYPE_INPUT,
                    !property.propertyRegex.isNullOrEmpty()
                ))

                if(property.propertyDoubleCheck){
                    beans.add(AccountEntryView.EntryBean(
                        "${property.propertyKey}__check",
                        "确认${property.propertyName}",
                        "请再次输入${property.propertyName}",
                        "",
                        if(property.propertyDom == "selectWithOther") AccountEntryView.TYPE_SELECTION else AccountEntryView.TYPE_INPUT,
                        !property.consumerEnum.isNullOrEmpty()
                    ))
                }
            }
        }

        beans.add(AccountEntryView.EntryBean(
            "receiptPwd",
            "提款密码",
            "请输入提款密码",
            "",
            AccountEntryView.TYPE_PASSWORD,
            true
        ))

        return beans
    }

    private fun generateEntryViews(entryBeans: List<AccountEntryView.EntryBean>) {
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
        loop@for (entryView in entryViews) {
            val bean = entryView.updatedEntryBean
            if (bean != null) {
                //AccountEntryHintView 会回传null
                val valueStr = bean.content
                if (bean.isRequired && valueStr.isNullOrEmpty()) {
                    showToast(bean.title + "不可为空")
                    return null
                }else if(valueStr.isNullOrEmpty()){
                    params[bean.tag] = ""
                    continue
                }

                if(bean.tag == "receiptPwd"){
                    params[bean.tag] = valueStr
                    continue
                }

                val property = cardInfo.cardProperties.find { it.propertyKey == bean.tag || "${it.propertyKey}__check" == bean.tag}!!
                if(!property.propertyRegex.isNullOrEmpty() && !bean.content.matches(Regex(property.propertyRegex))){
                    showToast("${property.propertyName}不符合输入规范${property.propertyRegex}")
                    return null
                }

                if(bean.tag.endsWith("__check") && bean.content != params[property.propertyKey]){
                    showToast("${property.propertyName}输入内容不一致")
                    return null
                }

                params[bean.tag] = valueStr
            }
        }

        params["typeNumber"] = cardInfo.typeNumber.toString()
        return params
    }

    private fun postAddAccount(params: Map<String, String>) {
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.postAddAccountNew(params) }
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
}