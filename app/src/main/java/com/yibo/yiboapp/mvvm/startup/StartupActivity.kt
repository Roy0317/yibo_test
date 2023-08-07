package com.yibo.yiboapp.mvvm.startup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.example.anuo.immodule.utils.ChatSpUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yibo.yiboapp.BuildConfig
import com.yibo.yiboapp.Event.VerifyEvent
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.CheckUserMessageActivity
import com.yibo.yiboapp.activity.MainActivity
import com.yibo.yiboapp.activity.WlecomePictureActivity
import com.yibo.yiboapp.data.Constant
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.ActivityStartupBinding
import com.yibo.yiboapp.entify.RealDomainWraper
import com.yibo.yiboapp.entify.SysConfig
import com.yibo.yiboapp.manager.NetworkManager
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.RetrofitFactory
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity
import com.yibo.yiboapp.ui.LogingDialog
import com.yibo.yiboapp.utils.DeviceIdUtils
import com.yibo.yiboapp.utils.Utils
import com.yibo.yiboapp.views.RouteUrlChooseDialog
import com.yibo.yiboapp.views.gestureview.DefaultPatternCheckingActivity
import com.yibo.yiboapp.views.gestureview.DefaultPatternSettingActivity
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog
import crazy_wrapper.Crazy.dialog.CustomDialogManager
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StartupActivity: BaseActivityKotlin() {

    private val ANIM_TIME = 3000L
    private val REQUEST_SOME_PERMISSION = 0x1

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.VIBRATE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO)

    private lateinit var binding: ActivityStartupBinding
    private var logingDialog: LogingDialog? = null
    private var animTimer: CountDownTimer? = null
    private var routeUrlChooseDialog: RouteUrlChooseDialog? = null

    private var viewClickCount = 0
    private var isShowingDomainDialog = false
    private var real_fix_urls = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        EventBus.getDefault().register(this)
        binding = ActivityStartupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        YiboPreference.instance(this).deviceId = DeviceIdUtils.getDeviceId(this)
        init()
        initView()
        real_fix_urls = Urls.FIX_NATIVE_BASE_URL_1
        YiboPreference.instance(this).saveUrlForDomains(real_fix_urls)
        YiboPreference.instance(this).updatePasswordTimestamp = 0
    }

    override fun onStart() {
        super.onStart()
        val isShowCountDown = YiboPreference.instance(this).isShowCountDown
        if (isShowCountDown) {
            animTimer?.start()
            binding.buttonJump.visibility = View.VISIBLE
        } else {
            binding.buttonJump.visibility = View.GONE
            checkPermissionGranted()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        routeUrlChooseDialog?.dismiss()
        routeUrlChooseDialog = null
        logingDialog?.dismiss()
        logingDialog = null
        animTimer?.cancel()
        animTimer = null
    }

    private fun init(){
        animTimer = object : CountDownTimer(ANIM_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                val text = "${getString(R.string.jump)}($time)"
                binding.buttonJump.text = text
            }

            override fun onFinish() {
                binding.buttonJump.text = getString(R.string.jump)
                if (!isShowingDomainDialog) {
                    checkPermissionGranted()
                }
            }
        }
    }

    private fun initView(){
        logingDialog = LogingDialog(this, getString(R.string.loading))
        logingDialog!!.setCanceledOnTouchOutside(false)

        binding.textViewDomain.setOnClickListener {
            viewClickCount++
            if (viewClickCount >= 3) {
                isShowingDomainDialog = true
                showDomainDialog()
            }
        }
        binding.buttonJump.setOnClickListener {
            animTimer?.cancel()
            animTimer = null
            checkPermissionGranted()
        }
    }

    private fun checkPermissionGranted(){
        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            startAcquireDomain(real_fix_urls)
        }else{
            ActivityCompat.requestPermissions(this, permissions, REQUEST_SOME_PERMISSION)
        }
    }

    private fun showDomainDialog() {
        val ccd = CustomConfirmDialog(this)
        ccd.setBtnNums(2)
        ccd.setContent("应用域名:" + Urls.BASE_URL)
        ccd.setToastShow(false)
        ccd.setRightBtnClickListener {
            ccd.dismiss()
            checkPermissionGranted()
        }
        ccd.setLeftBtnClickListener {
            ccd.dismiss()
            checkPermissionGranted()
        }
        ccd.setCancelable(false)
        val dialogManager = ccd as CustomDialogManager
        dialogManager.createDialog()
    }

    /**
     * 开始获取真实域名
     */
    private fun startAcquireDomain(domain: String) {
        logingDialog?.showLoading(getString(R.string.sync_optimise_route))
        RetrofitFactory.api().fetchDomainList("$domain${Urls.GET_APP_ROUTE_URL_LIST}", BuildConfig.APPLICATION_ID, Constant.YB_SOURCE, UsualMethod.getReleaseAndSdkVersion())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { logingDialog?.dismiss() }
            .subscribe(object: SingleObserver<RealDomainWraper>{
                override fun onSubscribe(d: Disposable) { compositeDisposable.add(d) }
                override fun onSuccess(response: RealDomainWraper) {
                    if (!response.isSuccess) {
                        showToast("未能获取最优线路")
                        showNetErrorDialog(this@StartupActivity)
                    } else {
                        NetworkManager.saveDomainsJson(response)
                    }

                    //添加暗门，方便测试直接修改主域名
                    if (BuildConfig.DEBUG) {
                        if (ChatSpUtils.instance(this@StartupActivity).domainUrl.isNotEmpty()) {
                            Urls.BASE_URL = ChatSpUtils.instance(this@StartupActivity).domainUrl
                            Urls.BASE_HOST_URL = ""
                        }
                    }
                    fetchSysConfig()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    if (real_fix_urls == Urls.FIX_NATIVE_BASE_URL_2) {
                        showToast("无法取得最优线路!")
                        fetchSysConfig()
                    } else {
                        real_fix_urls = Urls.FIX_NATIVE_BASE_URL_2
                        YiboPreference.instance(this@StartupActivity).saveUrlForDomains(real_fix_urls)
                        startAcquireDomain(real_fix_urls)
                    }
                }
            })
    }

    private fun showNetErrorDialog(mContext: Context) {
        val ccd = CustomConfirmDialog(mContext)
        ccd.setBtnNums(2)
        ccd.setLeftBtnText("去路由检测")
        ccd.setRightBtnText("取消")
        ccd.setLeftBtnClickListener {
            RouteCheckingActivity.createIntent(mContext)
            ccd.dismiss()
        }
        ccd.setRightBtnClickListener { ccd.dismiss() }
        ccd.setTitle("温馨提示")
        ccd.setContent("当前网络环境较差，请检测路由状况")
        ccd.setHtmlContent(true)
        ccd.setBaseUrl(Urls.BASE_URL)
        ccd.createDialog(false)
        ccd.dialog.show()
    }

    private fun gotoMain() {
        if (isInGestureView()) {
            return
        }

        MainActivity.createIntent(this)
        finish()
    }

    private fun isInGestureView(): Boolean {
        return ActivityUtils.getTopActivity().localClassName == DefaultPatternCheckingActivity::class.java.canonicalName ||
                ActivityUtils.getTopActivity().localClassName == DefaultPatternSettingActivity::class.java.canonicalName ||
                ActivityUtils.getTopActivity().localClassName == CheckUserMessageActivity::class.java.canonicalName
    }

    private fun fetchSysConfig() {
        logingDialog?.showLoading(getString(R.string.sync_sys_configing))
        lifecycleScope.launch {
            val result = httpRequest { RetrofitFactory.api().fetchSysConfig() }
            if(result is HttpResult.Success){
                val wrapper = result.data
                if(!wrapper.isSuccess){
                    YiboPreference.instance(this@StartupActivity).setLoginState(false)
                    if (wrapper.code == 544){
                        UsualMethod.showVerifyActivity(this@StartupActivity)
                        return@launch
                    }
                    autoChangeRoute()
                }else{
                    YiboPreference.instance(this@StartupActivity).token = wrapper.getAccessToken()
                    //存储系统配置项
                    if (wrapper.content != null) {
                        val configJson = Gson().toJson(wrapper.content, SysConfig::class.java)
                        YiboPreference.instance(this@StartupActivity).saveConfig(configJson)
                        YiboPreference.instance(this@StartupActivity).saveYjfMode(wrapper.content.yjf)
                        //保存系统版本号版本号
                        YiboPreference.instance(this@StartupActivity).saveGameVersion(wrapper.content.version)
                        YiboPreference.instance(this@StartupActivity).saveNativeStyle(wrapper.content.native_style_code)
                    }
                    getBetIp()
                    goAfterLotteryGet()
                }
            }else if(result is HttpResult.Error){
                showToast(result.message)
                autoChangeRoute()
            }
        }
    }

    private fun autoChangeRoute() {
        var contentBeans = Gson().fromJson<MutableList<RealDomainWraper.ContentBean>>(
            YiboPreference.instance(this).routE_URLS,
            object : TypeToken<java.util.ArrayList<RealDomainWraper.ContentBean?>?>() {}.type)

        if (contentBeans.isNullOrEmpty()) {
            showToast("线路不稳，尝试自动切换其它可用线路")
            contentBeans = mutableListOf()
        }

        routeUrlChooseDialog = RouteUrlChooseDialog(this, contentBeans)
        routeUrlChooseDialog!!.setChooseListener(object: RouteUrlChooseDialog.OnRouteChooseListener{
            override fun onChoose(contentBean: RealDomainWraper.ContentBean, position: Int) {
                fetchSysConfig()
                routeUrlChooseDialog!!.stopAutoChooseRoute()
            }
            override fun onAutoRouteFailed() {
                showToast("自动切换线路失败，跳转大厅页面")
                gotoMain()
            }
        })
        routeUrlChooseDialog!!.autoChooseRoute()
    }

//    private fun changeRoute(message: String? = null){
//        val contentBeans = Gson().fromJson<List<RealDomainWraper.ContentBean>>(
//            YiboPreference.instance(this@StartupActivityKotlin).routE_URLS,
//            object: TypeToken<List<RealDomainWraper.ContentBean>>() {}.type)
//        if (contentBeans.isNullOrEmpty()) {
//            showToast(getString(R.string.no_route_urls))
//        }else{
//            val name = YiboPreference.instance(this).choosE_ROUTE_NAME
//            showToast(message ?: ("线路- $name -未能获取最新的系统配置"))
//            setRouteUrl(contentBeans[0])
//        }
//    }

    private fun getBetIp() {
//        lifecycleScope.launch(Dispatchers.IO){
//            val result = safeHttp { RetrofitFactory.api().findMyIP().await() }
//            if(result is HttpResult.Success){
//                val ip = result.data.ip2.ifNullOrEmpty { result.data.ip1 }
//                Utils.logAll(TAG, "getBetIp() = $ip")
//                ChatSpUtils.instance(this@StartupActivityKotlin).beT_IP = ip
//            }
//        }

        val d = RetrofitFactory.api().findMyIP()
            .subscribeOn(Schedulers.io())
            .subscribe { response ->
                val ip = response.ip2.ifNullOrEmpty { response.ip1 }
                Utils.logAll(TAG, "getBetIp() = $ip")
                ChatSpUtils.instance(this@StartupActivity).beT_IP = ip
            }
    }

    private fun goAfterLotteryGet() {
        if (!YiboPreference.instance(this).hasShowPicture) {
            val config = UsualMethod.getConfigFromJson(this)
            if (config != null) {
                if (config.native_welcome_page_switch.equals("on", ignoreCase = true)) {
                    WlecomePictureActivity.createIntent(this)
                    finish()
                } else {
                    gotoMain()
                }
            }
        } else {
            gotoMain()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_SOME_PERMISSION -> {
                if (grantResults.isNotEmpty()) {
                    val deniedPermissionList: MutableList<String> = ArrayList()
                    grantResults.forEachIndexed { index, result ->
                        if(result != PackageManager.PERMISSION_GRANTED)
                            deniedPermissionList.add(permissions[index])
                    }

                    if (deniedPermissionList.isEmpty()) {
                        YiboPreference.instance(this).packagE_DOMAIN = Urls.BASE_URL
                        YiboPreference.instance(this).packagE_HOST_URL = Urls.BASE_HOST_URL
                        startAcquireDomain(real_fix_urls)
                    } else {
                        for (s in deniedPermissionList) {
                            //勾选了对话框中”Don’t ask again”的选项, 返回false
                            if (!shouldShowRequestPermissionRationale(s)) {
                                showToast("请先给App授权！")
                                return
                            }
                        }
                        checkPermissionGranted()
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: VerifyEvent) {
        if (event.isSuccess){
            recreate()
        }else{
            showToast("连线逾时，请切换更稳定的网路环境后重启动APP")
        }
    }
}