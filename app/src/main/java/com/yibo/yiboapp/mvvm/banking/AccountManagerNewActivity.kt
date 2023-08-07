package com.yibo.yiboapp.mvvm.banking

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.yibo.yiboapp.R
import com.yibo.yiboapp.adapter.AccountManagerNewAdapter
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.databinding.ActivityAccountManagerNewBinding
import com.yibo.yiboapp.entify.Card
import com.yibo.yiboapp.entify.NewAccountResponse
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.*
import com.yibo.yiboapp.mvvm.password.SetBankPwdActivity
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.views.SimpleSelectDialog
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class AccountManagerNewActivity : BaseActivityKotlin() {

    companion object{
        private const val ADD_ACCOUNT = 10
        private const val SET_BANK_PASSWORD = 11
        private const val COMPLETE_BANK = 12
    }


    private lateinit var binding: ActivityAccountManagerNewBinding
    private var toPickAccount = false
    private var accountAdapter: AccountManagerNewAdapter? = null

    private var realName: String = ""
    private var bankCard: String = ""
    private var isCheckAccountSuccessful = false
    private var hasSetAccountPassword = false // 是否设置过提款密码
    private var accountResponse: NewAccountResponse? = null
    private val accountItems: MutableList<SimpleSelectDialog.SimpleItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagerNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toPickAccount = intent.getBooleanExtra("toPickAccount", false)
        initView()
        checkAccountFromWeb()
    }

    private fun initView() {
        binding.imageBack.setOnClickListener { onBackPressed() }

        binding.imageNewAccount.setOnClickListener{
            if (isCheckAccountSuccessful) {
                showNewAccountDialog()
            } else {
                checkAccountFromWeb()
            }
        }

        binding.imageFilter.setOnClickListener { showAccountFilterDialog() }
    }

    //检查是否设置过出款帐户等信息
    private fun checkAccountFromWeb() {
        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.checkAccountInfo() }
            if(result is HttpResult.Success){
                val response = result.data
                if (!response.isSuccess) {
                    showToast(response.msg.ifNullOrEmpty { getString(R.string.check_safety_fail) })
                } else {
                    if (response.code == 121) {
                        //尚未设置提款密码
                        showToast(response.msg)
                        val intent = Intent(this@AccountManagerNewActivity, SetBankPwdActivity::class.java)
                        startActivityForResult(intent, SET_BANK_PASSWORD)
                    } else {
                        isCheckAccountSuccessful = true
                        realName = response.content.userName
                        bankCard = response.content.cardNo
                        fetchWithdrawAccounts()
                        hasSetAccountPassword = response.content?.repPwd?.isNotEmpty() == true
                    }
                }
            }else if(result is HttpResult.Error){
                showToast(result.message)
            }
        }
    }

    private fun fetchWithdrawAccounts(){
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.fetchAccountsNew() }
            if(result is HttpResult.Success){
                val response = result.data
                if(!response.success){
                    showToast(response.msg.ifNullOrEmpty { "无法取得账户信息" })
                }else{
                    setAccountItems(response)
                    accountResponse = setCardInfo(response)
                    val cards = filterCardsByType(accountResponse!!, accountItems)
                    updateRecyclerAccounts(cards)
                }
            }else if(result is HttpResult.Error){
                showToast(result.message)
            }
        }
    }

    private fun setAccountItems(response: NewAccountResponse){
        accountItems.clear()
        accountItems.add(SimpleSelectDialog.SimpleItem("全部", true))
        response.cardInfoList.forEach { accountItems.add(SimpleSelectDialog.SimpleItem(it.typeName)) }
    }

    /**
     * 将帐户种类图片塞到card里面方便adapter显示
     */
    private fun setCardInfo(response: NewAccountResponse): NewAccountResponse{
        return response.apply {
            cardInfoList.forEach { info ->
                info.cardList.forEach { card ->
                    card.bgColorCode = info.mobileImageColorCode
                    card.imageUrl = if(info.mobileImageUrl.contains("http")) info.mobileImageUrl
                                    else Urls.BASE_URL + info.mobileImageUrl
                    card.typeName = info.typeName
                }
            }
        }
    }

    private fun filterCardsByType(response: NewAccountResponse, items: List<SimpleSelectDialog.SimpleItem>): List<Card>{
        val selectedItemName = items.find { it.isChecked }!!.itemName
        return if(selectedItemName == "全部"){
            response.cardInfoList.flatMap { it.cardList }
        }else{
            response.cardInfoList.find { it.typeName == selectedItemName }!!.cardList
        }
    }

    private fun updateRecyclerAccounts(cards: List<Card>) {
        accountAdapter = AccountManagerNewAdapter(cards)
        if(toPickAccount){
            accountAdapter!!.setCallback(object: AccountManagerNewAdapter.PickAccountNewListener{
                override fun onPickAccount(card: Card) {
                    val cardInfo = accountResponse?.cardInfoList?.find { it.typeNumber == card.type }
                    if(cardInfo == null){
                        showToast("帐户信息出现错误，请联络客服")
                    }else{
                        val gson = Gson()
                        val json1 = gson.toJson(cardInfo)
                        val json2 = gson.toJson(card)
                        intent.putExtra("cardInfo", json1)
                        intent.putExtra("card", json2)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            })
        }
        binding.recyclerAccounts.layoutManager = LinearLayoutManager(this)
        binding.recyclerAccounts.adapter = accountAdapter
    }

    private fun showNewAccountDialog() {
        if(accountAdapter == null || accountResponse == null){
            showToast("未能取得账户信息，无法新增")
            return
        }

        val typeList: MutableList<SimpleSelectDialog.SimpleItem> = ArrayList()
        accountResponse!!.cardInfoList.forEach { typeList.add(SimpleSelectDialog.SimpleItem(it.typeName)) }
        SimpleSelectDialog(this, "新增帐户类型", typeList, object: SimpleSelectDialog.SimpleSelectListener{
            override fun onItemSelected(item: SimpleSelectDialog.SimpleItem) {
                val cardInfo = accountResponse!!.cardInfoList.find { it.typeName == item.itemName }!!
                if (cardInfo.cardList.size < cardInfo.cardCountLimit) {
                    val json = Gson().toJson(cardInfo)
                    val intent = AccountEditNewActivity.newIntent(this@AccountManagerNewActivity, json, realName)
                    startActivityForResult(intent, ADD_ACCOUNT, null)
                } else {
                    showToast("该类型帐户设置数量已达上限：${cardInfo.cardCountLimit}，无法绑定")
                }
            }
        }).show()
    }

    private fun showAccountFilterDialog(){
        if(accountResponse == null || accountItems.isEmpty()){
            showToast("未能取得账户信息，无法筛选")
            return
        }

        SimpleSelectDialog(this, "筛选帐户类型", accountItems, object: SimpleSelectDialog.SimpleSelectListener{
            override fun onItemSelected(item: SimpleSelectDialog.SimpleItem) {
                accountItems.forEach { it.isChecked = it.itemName == item.itemName }
                val cards = filterCardsByType(accountResponse!!, accountItems)
                updateRecyclerAccounts(cards)
            }
        }).show()
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