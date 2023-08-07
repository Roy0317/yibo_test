package com.yibo.yiboapp.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.*
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.NavigationHeaderViewBinding
import com.yibo.yiboapp.entify.Meminfo
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.ui.SignInActivity
import com.yibo.yiboapp.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class NavigationHeaderView(context: Context, attrs: AttributeSet?, styleAttr: Int): FrameLayout(context, attrs, styleAttr) {

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    private val binding: NavigationHeaderViewBinding
    private val config by lazy { UsualMethod.getConfigFromJson(context) }
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var leftMoneyName: String = ""
    private var accountName: String = ""
    private var toShow = true
    private var rotate: RotateAnimation? = null
    private var balance: Double = 0.0

    init {
        binding = NavigationHeaderViewBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initListener()
        initParams()
    }

    private fun initListener(){
        binding.tvYinCang.setOnClickListener { changeJinE() }
        binding.charge.setOnClickListener {
            if (ManagerFactory.userManager.isTestPlayer()) {
                context.showToast("操作权限不足，请联系客服")
                return@setOnClickListener
            }

            val username = accountName.ifNullOrEmpty { YiboPreference.instance(context).username }
            val intentCharge = BankingManager.openChargePage(context, username, leftMoneyName)
            context.startActivity(intentCharge)
        }

        binding.tikuan.setOnClickListener {
            if (ManagerFactory.userManager.isTestPlayer()) {
                context.showToast("操作权限不足，请联系客服")
                return@setOnClickListener
            }
            val intent = BankingManager.openWithdrawPage(context, 0.0)
            context.startActivity(intent)
        }

        binding.convert.setOnClickListener {
            if (ManagerFactory.userManager.isTestPlayer()) {
                context.showToast("操作权限不足，请联系客服")
                return@setOnClickListener
            }
            QuotaConvertActivity.createIntent(context)
        }

        binding.sign.setOnClickListener { context.startActivity(Intent(context, SignInActivity::class.java)) }

        binding.llMoneyinfo.setOnClickListener {
            binding.tvRefresh.animation = rotate
            syncHeaderWebDatas(context)
            updateCircleHeader()
        }

        binding.headerBg.setOnClickListener{ UserCenterActivity.createIntent(context) }
    }

    private fun initParams(){
        //根据帐户类型是否显示充值提款按钮
        refreshAnimation()
        binding.sign.visibility = if (config.onoff_sign_in.isOn()) VISIBLE else GONE
        //根据后台配置的彩票logo图上更新头像图
        updateCircleHeader()
        //根据后台配置的头部背景图片地址设置背景
        val bgUrl = config.member_center_bg_url
        if (!bgUrl.isNullOrEmpty()) {
            Glide.with(context).asBitmap()
                .load(bgUrl.trim { it <= ' ' })
                .into(binding.headerBg)
        } else {
            binding.headerBg.setImageResource(R.drawable.member_page_header_bg)
        }

        syncHeaderWebDatas(context)
        syncHeaderPhoto()
    }

    fun updateCircleHeader() {
        UsualMethod.LoadUserImage(context, binding.header)
    }

    fun syncHeaderWebDatas(context: Context){
        scope.launch {
            val result = httpRequest { ManagerFactory.userManager.fetchMemberInfo() }
            if(result is HttpResult.Success){
                val response = result.data
                if(!response.isSuccess){
                    context.showToast("请求失败")
                }else{
                    if(response.content.isLogin){
                        updateAccount(response.content)
                    }
                }
            }else if(result is HttpResult.Error){
                context.showToast("请求失败: ${result.message}")
            }
        }
    }

    private fun syncHeaderPhoto(){
        scope.launch {
            val result = httpRequest { ManagerFactory.userManager.syncHeaderPhoto() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    ManagerFactory.userManager.saveHeaderPhoto(response.content)
                    updateCircleHeader()
                }
            }else if(result is HttpResult.Error){
                Utils.logAll("NavigationHeaderView", "syncHeaderPhoto error, ${result.message}")
            }
        }
    }

    //更新帐户相关信息
    private fun updateAccount(meminfo: Meminfo) {
        accountName = meminfo.account.ifNullOrEmpty { "暂无名称" }
        binding.name.text = accountName

        if (!meminfo.balance.isNullOrEmpty()) {
            balance = meminfo.balance.toDouble()
            //保留两位小数
            leftMoneyName = BigDecimal(meminfo.balance).setScale(2, RoundingMode.DOWN).toString()
            showYue(toShow)
        }
        ManagerFactory.bankingManager.saveUserBalance(context, leftMoneyName)
        //UI改版  侧边栏用户等级不显示  2021/10/8
//        if (sys.getShowuser_levelicon().equals("on")) {
//            updateLevelIcon(level, meminfo.getLevel_icon());
//            levelName.setText(meminfo.getLevel());
//        }
        if (context is MainActivity) {
            (context as MainActivity).refreshNewMainPageLoginBlock(ManagerFactory.userManager.isLogin(), accountName, balance)
        }
    }

    private fun refreshAnimation() {
        rotate = RotateAnimation(0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate!!.interpolator = LinearInterpolator()
        rotate!!.duration = 500 //设置动画持续周期
        rotate!!.repeatCount = 1 //设置重复次数
        rotate!!.fillAfter = true //动画执行完后是否停留在执行完的状态
        rotate!!.startOffset = 0 //执行前的等待时间
    }

    fun getBalance(): Double { return balance }

    fun getAccountName(): String { return accountName }

    //切换显示隐藏余额
    private fun changeJinE() {
        toShow = !toShow
        showYue(toShow)
    }

    private fun showYue(toShow: Boolean) {
        if (toShow) {
            binding.leftMoney.text = leftMoneyName
            binding.tvYinCang.setBackgroundResource(R.drawable.jijin_kejian)
        } else {
            binding.leftMoney.text = "***元"
            binding.tvYinCang.setBackgroundResource(R.drawable.jijin_bukejian)
        }
    }
}