package com.yibo.yiboapp.mvvm.password

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yibo.yiboapp.R
import com.yibo.yiboapp.databinding.ActivitySetWithdrawPasswordBinding
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import kotlinx.coroutines.launch

/**
 * 由SetBankPwdActivity改写成KOTLIN
 */
class SetBankPwdActivity : BaseActivityKotlin() {

    private lateinit var binding: ActivitySetWithdrawPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetWithdrawPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        binding.title.middleTitle.text = "设置提款密码"
        binding.title.backText.setOnClickListener { onBackPressed() }
        binding.buttonConfirm.setOnClickListener {
            val password = binding.editNewPassword.text.toString()
            val passwordConfirm = binding.editConfirmPassword.text.toString()

            if(password.isEmpty()){
                showToast("请输入密码")
            }else if(passwordConfirm.isEmpty()){
                showToast("请再次确认密码")
            }else if(password != passwordConfirm){
                showToast("两次密码设置不一致")
            }else{
                postSetPassword(password)
            }
        }
    }

    private fun postSetPassword(password: String){
        val params = mapOf(
            "pwd" to password,
            "againPwd" to password)

        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.postSetWithdrawPassword(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    showToast(R.string.setting_bank_pwd_success)
                    setResult(RESULT_OK)
                    finish()
                }else{
                    showToast(getString(R.string.post_fail) + ": ${response.msg}")
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.post_fail) + ", ${result.message}")
            }
        }
    }
}