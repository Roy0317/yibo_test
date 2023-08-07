package com.yibo.yiboapp.mvvm.banking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.lifecycleScope
import com.yibo.yiboapp.R
import com.yibo.yiboapp.databinding.ActivityUpdateBankAccountKotlinBinding
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpdateBankAccountActivity : BaseActivityKotlin() {

    companion object{
        private const val USER_NAME = "user_name"
        private const val BANK_CARD = "bank_card"

        fun createIntent(context: Context, userName: String, bankCard: String): Intent {
            val intent = Intent(context, UpdateBankAccountActivity::class.java)
            intent.putExtra(USER_NAME, userName)
            intent.putExtra(BANK_CARD, bankCard)
            return intent
        }
    }

    private lateinit var binding: ActivityUpdateBankAccountKotlinBinding
    private val bankNames by lazy { resources.getStringArray(R.array.bank_card) }
    private var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBankAccountKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initParams()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(1000L)
            isInit = true
        }
    }

    private fun initParams(){
        binding.title.middleTitle.text = "完善出款银行信息"
        val userName=  intent.getStringExtra(USER_NAME)
        binding.editUserName.setText(userName)
        binding.editUserName.isEnabled = userName.isNullOrEmpty()
        val bankCard = intent.getStringExtra(BANK_CARD)
        binding.editBankCard.setText(bankCard)
        binding.spinnerBank.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(bankNames[position] == "其他"){
                    binding.editBankName.setText("")
                    binding.editBankName.requestFocus()
                }else{
                    binding.editBankName.setText(bankNames[position])
                    if(isInit){
                        binding.editProvince.requestFocus()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.title.backText.setOnClickListener { onBackPressed() }
        binding.buttonNext.setOnClickListener {
            if (checkAllInput()) {
                val userName: String = binding.editUserName.text.toString()
                val bankName: String = binding.editBankName.text.toString()
                val province: String = binding.editProvince.text.toString()
                val city: String = binding.editCity.text.toString()
                val bankAddress: String = binding.editNetSpot.text.toString()
                val cardNo: String = binding.editBankCard.text.toString()
                val repPwd: String = binding.editPassword.text.toString()
                updateBankCard(userName, bankName, province, city, bankAddress, cardNo, repPwd)
            }
        }
    }

    private fun checkAllInput(): Boolean {
        if (binding.editBankName.text.toString().isBlank()) {
            showToast("所属银行为必填栏位")
            return false
        } else if (binding.editBankCardCheck.text.toString().isBlank()) {
            showToast("确认银行卡号为必填栏位")
            return false
        } else if (binding.editBankCard.text.toString() != binding.editBankCardCheck.text.toString()) {
            showToast("请再次确认银行卡号")
            return false
        } else if (binding.editPassword.text.toString().isBlank()) {
            showToast("提款密码为必填栏位")
            return false
        }
        return true
    }

    private fun updateBankCard(userName: String, bankName: String, province: String, city: String,
                               bankAddress: String, cardNo: String, repPwd: String) {
        val params = HashMap<String, String>()
        params["userName"] = userName
        params["bankName"] = bankName
        params["province"] = province
        params["city"] = city
        params["bankAddress"] = bankAddress
        params["cardNo"] = cardNo
        params["repPwd"] = repPwd

        lifecycleScope.launch{
            val result = httpRequest { ManagerFactory.bankingManager.postUpdateBankInfo(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    showToast(R.string.update_bank_card_success)
                    delay(1000L)
                    setResult(RESULT_OK)
                    finish()
                }else{
                    showToast(getString(R.string.update_bank_card_failure) + ", ${response.msg}")
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.update_bank_card_failure) + ", ${result.message}")
            }
        }
    }
}