package com.yibo.yiboapp.activity

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.yibo.yiboapp.Event.VerifyEvent
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.VerifyCheckData
import com.yibo.yiboapp.data.VerifyData
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.ActivityVerifyBinding
import com.yibo.yiboapp.mvvm.setNewOnClickedListener
import com.yibo.yiboapp.network.ApiParams
import com.yibo.yiboapp.network.HttpUtil
import com.yibo.yiboapp.network.NetworkResult
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity
import com.yibo.yiboapp.route.LDNetDiagnoUtils.LDNetUtil
import org.greenrobot.eventbus.EventBus

class VerifyActivity: BaseActivity() {

    private lateinit var binding: ActivityVerifyBinding
    private var countDownTimer: CountDownTimer? = null
    private var isWaiting = false
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListener()
        getVerifyImage()
    }

    fun onClickListener(){
        binding.imgReload.setOnClickListener { view: View? -> getVerifyImage() }
        binding.routeChecking.setOnClickListener { view: View? ->
            RouteCheckingActivity.createIntent(this)
            finish()
        }

        binding.btnActionVerify.setNewOnClickedListener {
            if (TextUtils.isEmpty(binding.edVerifyCode.text.toString())) {
                Toast.makeText(this, "验证码不得为空", Toast.LENGTH_SHORT).show()
                return@setNewOnClickedListener
            }
            if (!isWaiting) {
                actionVerify(binding.edVerifyCode.text.toString())
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun getVerifyImage() {
        binding.imgReload.visibility = View.GONE
        binding.routeChecking.visibility = View.GONE
        binding.tvTip.visibility = View.VISIBLE
        binding.tvTip.text = "验证中..."
        binding.tvTip.setTextColor(Color.parseColor("#000000"))
        HttpUtil.get(this, Urls.GET_VERIFY_IMAGE, null, false, "获取验证码..."
        ) { result: NetworkResult ->
            if (result.msg == "暂无数据，请退出重试") {
                val event = VerifyEvent()
                event.setIsSuccess(false)
                EventBus.getDefault().post(event)
                return@get
            }
            if (!TextUtils.isEmpty(result.content)) {
                binding.groupWait.visibility = View.INVISIBLE
                val verifyData =
                    Gson().fromJson(result.content,
                        VerifyData::class.java)
                if (!TextUtils.isEmpty(verifyData.code)) {
                    when (verifyData.code) {
                        "over_limit" -> {
                            //显示满载讯息
                            binding.tvTip.text = "伺服器玩家已达上限，请稍后重试"
                            autoRetryGetVerifyImage()
                        }
                        "wait" -> {
                            //显示排队讯息
                            binding.tvTip.text = "排队中...请耐心等候..."
                            binding.groupVerify.visibility = View.INVISIBLE
                            binding.groupWait.visibility = View.VISIBLE
                            binding.progressBar.progress = 0f
                            autoRetryGetVerifyImage()
                        }
                        "verify_code" -> {
                            binding.tvTip.text = "目前已启用限流功能，请输入下方验证码进入主页"
                            binding.groupVerify.visibility = View.VISIBLE
                            binding.groupWait.visibility = View.INVISIBLE
                            Glide.with(binding.imgVerifyCode).load(verifyData.src).into(binding.imgVerifyCode)
                        }
                        "clean_pass" -> {
                            YiboPreference.instance(this).verifyToken = verifyData.token
                            binding.tvTip.text = "验证中..."
                            EventBus.getDefault().post(VerifyEvent())
                            finish()
                        }
                        "logged_in", "token_valid" -> {
                            binding.tvTip.text = "验证中..."
                            EventBus.getDefault().post(VerifyEvent())
                            finish()
                        }
                    }
                }
            } else {
                if (LDNetUtil.isNetworkConnected(this)) {
                    Toast.makeText(this, "连线逾时，请切换更稳定的网路环境后重启动APP", Toast.LENGTH_SHORT).show()
                    binding.tvTip.text = "连线逾时，请切换更稳定的网路环境后重启动APP"
                } else {
                    Toast.makeText(this, "网路连线异常。", Toast.LENGTH_SHORT).show()
                    binding.tvTip.text = "网路连线异常。"
                }
                binding.tvTip.visibility = View.VISIBLE
                binding.tvTip.setTextColor(Color.parseColor("#FF0000"))
                binding.groupWait.visibility = View.INVISIBLE
                binding.groupVerify.visibility = View.INVISIBLE
                binding.imgReload.visibility = View.VISIBLE
                binding.routeChecking.visibility = View.VISIBLE
            }
        }
    }

    private fun autoRetryGetVerifyImage() {
        if (isWaiting) return
        countDownTimer = object : CountDownTimer(1000L * 15, 1000L) {
            override fun onTick(millis: Long) {
                isWaiting = true
                binding.tvSecond.text = (millis / 1000).toString()
                binding.progressBar.progress = (millis / 1000).toFloat()
            }

            override fun onFinish() {
                isWaiting = false
                getVerifyImage()
                cancel()
            }
        }.start()
    }

    private fun actionVerify(verifyCode: String) {
        val apiParams = ApiParams()
        apiParams["ccVerifyCode"] = verifyCode
        HttpUtil.postForm(this, Urls.POST_VERIFY_CODE, apiParams, false, "验证中..."
        ) { result: NetworkResult ->
            if (!TextUtils.isEmpty(result.content)) {
                val data =
                    Gson().fromJson(result.content,
                        VerifyCheckData::class.java)
                when (data.code) {
                    "verify_code_error" -> {
                        getVerifyImage()
                        Toast.makeText(this, "输入错误，请重新输入。", Toast.LENGTH_SHORT).show()
                    }
                    "valid" -> {
                        YiboPreference.instance(this).verifyToken = data.token
                        EventBus.getDefault().post(VerifyEvent())
                        finish()
                    }
                    "logged_in", "token_valid" -> {
                        EventBus.getDefault().post(VerifyEvent())
                        finish()
                    }
                }
            } else {
                if (LDNetUtil.isNetworkConnected(this)) {
                    Toast.makeText(this, "验证失败，请联络服务人员。", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "网路连线异常。", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}