package com.yibo.yiboapp.mvvm.password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.LoginActivity
import com.yibo.yiboapp.activity.LoginAndRegisterActivity
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.ActivityChangePasswordKotlinBinding
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import kotlinx.coroutines.launch

/**
 * 由ChangePwdActivity改写成kotlin
 */
class ChangePwdActivity : BaseActivityKotlin() {

    companion object{
        private const val LOGIN_PASS = "isLoginPassword"

        fun createIntent(context: Context, isLoginPassword: Boolean) {
            val intent = Intent(context, ChangePwdActivity::class.java)
            intent.putExtra(LOGIN_PASS, isLoginPassword)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityChangePasswordKotlinBinding
    private var isLoginPassword: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLoginPassword = intent?.getBooleanExtra(LOGIN_PASS, true) ?: true
        if(!isLoginPassword){
            checkAccount()
        }
        initView()
    }

    private fun initView(){
        binding.title.backText.setOnClickListener { onBackPressed() }
        binding.title.middleTitle.text = if(isLoginPassword) getString(R.string.pwd_modify) else getString(R.string.account_pwd_modify)
        binding.buttonConfirm.setOnClickListener {
            val old = binding.editOldPassword.text.toString()
            val new = binding.editNewPassword.text.toString()
            val con = binding.editConfirmPassword.text.toString()
            if(checkPasswordValidity(old, new, con)){
                if(isLoginPassword){
                    postModifyLoginPassword(old, new)
                }else{
                    postModifyWithdrawPassword(old, new)
                }
            }
        }
    }

    private fun checkAccount(){
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.checkAccountInfo() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    if(response.code == 121){
                        showToast(response.msg)
                        if(response.content != null){
                            val intent = Intent(this@ChangePwdActivity, SetBankPwdActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }else{
                    showToast(getString(R.string.check_safety_fail) + ": " + response.msg)
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast("获取提款密码失败，请稍后重试！ " + result.message)
                finish()
            }
        }
    }

    private fun checkPasswordValidity(oldPassword: String, newPassword: String, confirmPassword: String): Boolean{
        if(oldPassword.isEmpty()){
            showToast("请输入旧密码")
            return false
        }

        if(newPassword.isEmpty()){
            showToast("请输入新密码")
            return false
        }

        if(isLoginPassword){
            if(newPassword.length < 6 || newPassword.length > 16){
                showToast(getString(R.string.password_limit))
                return false
            }
        }else{
            if(newPassword.length < 6){
                showToast("提款密码至少6位")
                return false
            }
        }

        if(confirmPassword.isEmpty()){
            showToast("请再次输入新密码")
            return false
        }

        if(newPassword != confirmPassword){
            showToast("两次密码设置不一致")
            return false
        }

        return true
    }

    private fun postModifyWithdrawPassword(oldPassword: String, newPassword: String){
        val params = mapOf(
            "oldpassword" to oldPassword,
            "newpassword" to newPassword,
            "confirmpassword" to newPassword)

        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.postModifyWithdrawPassword(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    showToast(R.string.modify_cash_success)
                    finish()
                }else{
                    showToast(getString(R.string.modify_fail) + ": " + response.msg)
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.modify_fail) + ": " + result.message)
            }
        }
    }

    private fun postModifyLoginPassword(oldPassword: String, newPassword: String){
        val params = mapOf(
            "oldpassword" to oldPassword,
            "newpassword" to newPassword,
            "confirmpassword" to newPassword)

        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.bankingManager.postModifyLoginPassword(params) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    showToast(R.string.modify_success_locate)
                    val sc = UsualMethod.getConfigFromJson(this@ChangePwdActivity)
                    val username = YiboPreference.instance(this@ChangePwdActivity).username
                    if (sc.newmainpage_switch.isOn()) {
                        LoginAndRegisterActivity.createIntent(this@ChangePwdActivity, username, "", 0)
                    } else {
                        LoginActivity.createIntent(this@ChangePwdActivity, username, "")
                    }
                    finish()
                }else{
                    showToast(getString(R.string.modify_fail) + ": " + response.msg)
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                showToast(getString(R.string.modify_fail) + ": " + result.message)
            }
        }
    }
}