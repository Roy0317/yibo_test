package com.yibo.yiboapp.views

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.donkingliang.banner.CustomBanner.OnPageClickListener
import com.donkingliang.banner.CustomBanner.ViewCreator
import com.example.anuo.immodule.utils.CommonUtils
import com.example.anuo.immodule.utils.ScreenUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.*
import com.yibo.yiboapp.data.Constant
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.MainHeaderViewBinding
import com.yibo.yiboapp.entify.LunboResult
import com.yibo.yiboapp.entify.NoticeNewBean
import com.yibo.yiboapp.entify.SysConfig
import com.yibo.yiboapp.fragment.BaseMainFragment.MainHeaderDelegate
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.manager.BankingManager.Companion.openChargePage
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.manager.UserManager
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.utils.StartActivityUtils
import com.yibo.yiboapp.utils.Utils
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.math.RoundingMode

class LobbyHeaderView(context: Context, attrs: AttributeSet?): LinearLayout(context, attrs) {

    companion object{
        const val OPEN_DRAWER = 1
    }


    constructor(context: Context): this(context, null)

    private val YYHD = 1 //优惠活动
    private val YESJ = 2 //余额生金
    private val BIG_PAN = 3 //大转盘
    private val RED_PACKET = 4 //抢红包
    private val MRJJ = 5 //每日加奖
    private val ZZZY = 6 //周周运转


    private val binding: MainHeaderViewBinding = MainHeaderViewBinding.inflate(LayoutInflater.from(getContext()))
    private val bannerData: MutableList<LunboResult> = ArrayList()
    private val notices: MutableList<NoticeNewBean.ContentBean> = ArrayList()
    private val config: SysConfig by lazy { UsualMethod.getConfigFromJson(getContext()) }
    private var delegate: MainHeaderDelegate? = null
    private var isActive = false
    private var version: String = ""
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val userManager by lazy { ManagerFactory.userManager }

    init {
        binding.root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(binding.root)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        initBannerView()
        initNewMainPageViews()
        setWidth(binding.cqkLayout)
        setWidth(binding.tzjlLayout)
        setWidth(binding.downloadActivityLayout)
        setWidth(binding.zxkfLayout)

        binding.cqkLayout.setOnClickListener {
            if(!userManager.isLogin()){
                context.showToast("请先登录")
                UserManager.openLoginPage(context)
                return@setOnClickListener
            }

            if ("v1" == config.jump_style_when_click_cqk) {
                delegate?.onDelegate(OPEN_DRAWER)
            }
            if ("v2" == config.jump_style_when_click_cqk) {
                if (Utils.shiwanFromMobile(context)) {
                    context.showToast("操作权限不足，请联系客服！")
                    return@setOnClickListener
                }

                val username = YiboPreference.instance(context).username
                val balance = ManagerFactory.bankingManager.loadUserBalance(context)
                val intentCharge = openChargePage(context, username, balance)
                context.startActivity(intentCharge)
            }
            if ("v3" == config.jump_style_when_click_cqk) {
                if (Utils.shiwanFromMobile(context)) {
                    context.showToast("操作权限不足，请联系客服！")
                    return@setOnClickListener
                }

                val intent = BankingManager.openWithdrawPage(context, 0.0)
                context.startActivity(intent)
            }
        }
        binding.tzjlLayout.setOnClickListener {
            if (config.mainpage_betrecord_click_goto_recordpage.isOn()) {
                RecordsActivityNew.createIntent(context, "彩票投注记录", Constant.CAIPIAO_RECORD_STATUS, "")
            } else {
                delegate?.onDelegate(OPEN_DRAWER)
            }
        }
        binding.downloadActivityLayout.setOnClickListener {
            when (version) {
                "v1" -> {
                    if (config.isActive) {
                        if (config.wap_active_activity_link.isNullOrEmpty())
                            ActivePageActivity.createIntent(context)
                        else UsualMethod.viewLink(context, config.wap_active_activity_link.trim { it <= ' ' })
                    } else {
                        //未登录状态下点击下载链接跳转到登录页面
                        if (userManager.isLogin()) {
                            AppDownloadActivity.createIntent(context)
                        }else{
                            context.showToast("请先登录")
                            UserManager.openLoginPage(context)
                        }
                    }
                }
                "v2" -> checkLoginStatus { QuotaConvertActivity.createIntent(context) }
                "v3" -> checkLoginStatus { context.startActivity(Intent(context, JijinActivity::class.java)) }
                "v4" -> {
                    if (config.wap_active_activity_link.isNullOrEmpty())
                        ActivePageActivity.createIntent(context)
                    else UsualMethod.viewLink(context, config.wap_active_activity_link.trim { it <= ' ' })
                }
            }
        }
        binding.zxkfLayout.setOnClickListener {
            val version = config.online_service_open_switch
            if (version.isNotEmpty()) {
                val success = UsualMethod.viewService(context, version)
                if (!success) {
                    context.showToast("没有在线客服链接地址，无法打开")
                }
            }
        }
        binding.noticeLayout.setOnClickListener {
            if (notices.isEmpty()) {
                return@setOnClickListener
            }

            val listType = object : TypeToken<ArrayList<NoticeNewBean.ContentBean>>() {}.type
            val json = Gson().toJson(notices, listType)
            NoticesPageActivity.createIntent(context, json)
        }
        binding.rlAppLoad.setOnClickListener {
            //未登录状态下点击下载链接跳转到登录页面
            if(userManager.isLogin()){
                AppDownloadActivity.createIntent(context)
            }else{
                context.showToast("请先登录")
                UserManager.openLoginPage(context)
            }
        }

        if (!config.newmainpage_switch.isOn()) {
            checkNewMainPageSwitch()
            syncUIWhenStart()
        } else {
            binding.testLayout.removeAllViews()
            if (!config.mainpage_function_menu.isNullOrEmpty()) {
                val mainPageList = config.mainpage_function_menu.replace(" ", "")
                    .split(",").toTypedArray()

                //检查后台备置需大于4笔，并检查不得为中文。
                if (mainPageList.size >= 4 && mainPageList.all { Utils.isNumeric(it) }) {
                    val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f)
                    layoutParams.width = if (mainPageList.size > 4) {
                        binding.imgMore.visibility = VISIBLE
                        (ScreenUtil.getScreenWidth(context) - resources.getDimensionPixelSize(R.dimen.icon_13dp_size)) / 4
                    } else {
                        binding.imgMore.visibility = GONE
                        ScreenUtil.getScreenWidth(context) / 4
                    }
                    for (i in mainPageList.indices) {
                        binding.testLayout.addView(createMainPageItem(mainPageList[i], layoutParams))
                    }
                } else {
                    binding.imgMore.visibility = GONE
                    val defaultMainPageList = "1,2,3,4".split(",").toTypedArray()
                    val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f)
                    layoutParams.width = ScreenUtil.getScreenWidth(context) / 4
                    for (i in defaultMainPageList.indices) {
                        binding.testLayout.addView(createMainPageItem(defaultMainPageList[i], layoutParams))
                    }
                    context.showToast("解析异常，请检查配置格式。")
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scope.cancel()
    }

    private fun initBannerView(){
        //设置轮播图的滚动速度
        val time = config.lunbo_speed_seconds
        //根据服务端返回数据设置轮播速度
        val duration = if (!time.isNullOrEmpty() && Utils.isDigit2(time)) time.toInt() * 1000 else 1000
        binding.banner.scrollDuration = duration
        binding.banner.setIndicator(
            ContextCompat.getDrawable(context, R.drawable.shape_point_select),
            ContextCompat.getDrawable(context, R.drawable.shape_point_unselect)
        )
        binding.banner.setOnPageClickListener(OnPageClickListener<LunboResult> { position, result ->
            if(!result.titleUrl.isNullOrEmpty()){
                UsualMethod.viewLink(context, result.titleUrl)
                return@OnPageClickListener
            }

            when (result.jumpType) {
                YYHD -> ActivePageActivity.createIntent(context)
                YESJ -> context.startActivity(Intent(context, JijinActivity::class.java))
                BIG_PAN -> checkLoginStatus { BigPanActivity.createIntent(context) }
                RED_PACKET -> {
                    if (!userManager.isLogin()) {
                        UserManager.openLoginPage(context)
                    } else if (userManager.isTestPlayer() && !config.on_off_guest_redperm.isOn()) {
                        context.showToast("操作权限不足，请联系客服！")
                    } else {
                        StartActivityUtils.goRedPacket(context)
                    }
                }
                MRJJ -> {
                    checkLoginStatus {
                        val intent = Intent(context, JiajiangActivity::class.java)
                        intent.putExtra("title", "每日加奖活动")
                        CookieSyncManager.createInstance(context)
                        val cookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setCookie(Urls.BASE_URL + Urls.MEIRIJIAJIANG,
                            "SESSION=" + YiboPreference.instance(context).token) //cookies是在HttpClient中获得的cookie
                        CookieSyncManager.getInstance().sync()
                        intent.putExtra("url", Urls.BASE_URL + Urls.MEIRIJIAJIANG)
                        context.startActivity(intent)
                    }
                }
                ZZZY -> {
                    checkLoginStatus {
                        val intent = Intent(context, JiajiangActivity::class.java)
                        intent.putExtra("title", "周周转运活动")
                        CookieSyncManager.createInstance(context)
                        val cookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setCookie(Urls.BASE_URL + Urls.ZHOUZHOUZHUANYUN,
                            "SESSION=" + YiboPreference.instance(context).token) //cookies是在HttpClient中获得的cookie
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            cookieManager.flush()
                        } else {
                            CookieSyncManager.createInstance(context)
                            CookieSyncManager.getInstance().sync()
                        }
                        intent.putExtra("url", Urls.BASE_URL + Urls.ZHOUZHOUZHUANYUN)
                        context.startActivity(intent)
                    }
                }
            }
        })
    }

    private fun initNewMainPageViews(){
        val divider = findViewById<TextView>(R.id.new_main_page_divider)
        if (config.newmainpage_switch == "on") {
            val dividerString = StringBuilder()
            for (i in 0..14) {
                dividerString.append("..........")
            }
            divider.text = dividerString.toString()
            divider.visibility = VISIBLE
            binding.newMainPageRegister.setOnClickListener { v: View? ->
                LoginAndRegisterActivity.createIntent(context, YiboPreference.instance(context).username,
                    YiboPreference.instance(context).pwd, 1)
            }
            binding.newMainPageLogin.setOnClickListener { v: View? ->
                LoginAndRegisterActivity.createIntent(context, YiboPreference.instance(context).username,
                    YiboPreference.instance(context).pwd, 0)
            }
            refreshNewMainPageLoginBlock(
                YiboPreference.instance(context).isLogin,
                (context as MainActivity).header.getAccountName() ?: "",
                (context as MainActivity).header.getBalance())
        } else {
            binding.newMainPageLoginLayout.visibility = GONE
            divider.visibility = GONE
        }
    }

    fun refreshNewMainPageLoginBlock(isLogin: Boolean, accountName: String, balance: Double){
        if(config.newmainpage_switch.isOn()){
            binding.newMainPageLoginLayout.visibility = VISIBLE
            binding.newMainPageDivider.visibility = VISIBLE
            if (!isLogin) {
                binding.newMainPageRegister.visibility = if (userManager.allowRegisterSwitch()) VISIBLE else GONE
                binding.newMainPageLogin.visibility = VISIBLE
                binding.newMainPageName.visibility = GONE
                binding.newMainPageBalance.text = "登录后显示余额"
            } else {
                binding.newMainPageRegister.visibility = GONE
                binding.newMainPageLogin.visibility = GONE
                binding.newMainPageName.visibility = VISIBLE
                binding.newMainPageName.text = accountName
                val bg = BigDecimal(balance).setScale(2, RoundingMode.DOWN)
                binding.newMainPageBalance.text = "可用余额：$bg 元"
            }
        }else{
            binding.newMainPageLoginLayout.visibility = GONE
            binding.newMainPageDivider.visibility = GONE
        }
    }

    /**
     * 检查登入状态及是否为试玩帐号
     */
    private fun checkLoginStatus(action: () -> Unit) {
        if (!userManager.isLogin()) {
            context.showToast("请先登录")
            UserManager.openLoginPage(context)
        }else if (userManager.isTestPlayer()) {
            context.showToast("操作权限不足，请联系客服！")
        }else {
            action()
        }
    }

    private fun actionLoginOrRegisterActivity(userName: String, password: String, type: Int) {
        if (config.newmainpage_switch.isOn()) {
            LoginAndRegisterActivity.createIntent(context, userName, password, type)
        } else {
            if (type == 0) {
                LoginActivity.createIntent(context, userName, password)
            } else if (type == 1) {
                RegisterActivity.createIntent(context)
            }
        }
    }

    //主页功能菜单排序(1-存提款,2-投注记录,3-优惠活动,4-在线客服,5-额度转换,6-余额生金,7-APP下载)
    private fun createMainPageItem(menuNum: String, layoutParams: LayoutParams): TextView {
        val mainPageItem = TextView(context)
        var drawable: Drawable? = null
        var menuName = ""
        val itemSize = resources.getDimensionPixelSize(R.dimen.icon_25dp_size)
        val itemSizeSmall = resources.getDimensionPixelSize(R.dimen.icon_20dp_size)
        when (menuNum) {
            "1" -> {
                menuName = " 存提款 "
                drawable = context.getDrawable(R.drawable.app_money_icon)
                drawable?.setBounds(0, 0, itemSize, itemSize)
                mainPageItem.setOnClickListener { v: View? ->
                    if (delegate != null) {
                        delegate!!.onDelegate(OPEN_DRAWER)
                    }
                }
            }
            "2" -> {
                menuName = "投注记录"
                drawable = context.getDrawable(R.drawable.app_record_icon)
                drawable?.setBounds(0, 0, itemSize, itemSize)
                mainPageItem.setOnClickListener { v: View? ->
                    if (config.mainpage_betrecord_click_goto_recordpage.isOn()) {
                        RecordsActivityNew.createIntent(context, "彩票投注记录", Constant.CAIPIAO_RECORD_STATUS, "")
                    } else if (delegate != null) {
                        delegate!!.onDelegate(OPEN_DRAWER)
                    }
                }
            }
            "3" -> {
                menuName = "优惠活动"
                drawable = context.getDrawable(R.drawable.app_active_icon)
                drawable?.setBounds(0, 0, itemSize, itemSize)
                mainPageItem.setOnClickListener { v: View? ->
                    if (config.wap_active_activity_link.isNullOrEmpty())
                        ActivePageActivity.createIntent(context)
                    else
                        UsualMethod.viewLink(context, UsualMethod.getConfigFromJson(context).wap_active_activity_link.trim { it <= ' ' })
                }
            }
            "4" -> {
                menuName = "在线客服"
                drawable = context.getDrawable(R.drawable.app_service_icon)
                drawable?.setBounds(0, 0, itemSize, itemSize)
                mainPageItem.setOnClickListener { v: View? ->
                    val config = UsualMethod.getConfigFromJson(context)
                    val version = config.online_service_open_switch
                    if (!version.isEmpty()) {
                        val success = UsualMethod.viewService(context, version)
                        if (!success) {
                            context.showToast("没有在线客服链接地址，无法打开")
                        }
                    }
                }
            }
            "5" -> {
                menuName = "额度转换"
                drawable = context.getDrawable(R.drawable.fee_convert_menu_new)
                drawable?.setBounds(0, 0, itemSize, itemSize)
                mainPageItem.setOnClickListener { v: View? ->
                    checkLoginStatus { QuotaConvertActivity.createIntent(context) }
                }
            }
            "6" -> {
                menuName = "余额生金"
                drawable = context.getDrawable(R.drawable.icon_with_jijin)
                drawable?.setBounds(0, 0, itemSize, itemSize)
                mainPageItem.setOnClickListener { v: View? ->
                    checkLoginStatus { context.startActivity(Intent(context, JijinActivity::class.java)) }
                    if (UsualMethod.checkIsLogin(context)) {
                        context.startActivity(Intent(context, JijinActivity::class.java))
                    } else if (Utils.shiwanFromMobile(context)) {
                        Toast.makeText(context, "操作权限不足，请联系客服！", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
            }
            "7" -> {
                menuName = "APP下载"
                drawable = context.getDrawable(R.drawable.app_download_icon)
                drawable?.setBounds(0, 0, itemSizeSmall, itemSizeSmall)
                mainPageItem.setPadding(10, 0, 0, 0)
                mainPageItem.setOnClickListener { v: View? ->
                    if (UsualMethod.checkIsLogin(context)) {
                        AppDownloadActivity.createIntent(context)
                    }
                }
            }
            "8" -> {
                menuName = "购彩大厅"
                drawable = context.getDrawable(R.drawable.icon_goucai_center)
                drawable?.setBounds(0, 0, itemSizeSmall, itemSizeSmall)
                mainPageItem.setPadding(10, 0, 0, 0)
                mainPageItem.setOnClickListener { v: View? ->
                    CaigouMallActivity.createIntent(context)
                }
            }
        }
        mainPageItem.layoutParams = layoutParams
        mainPageItem.text = menuName
        mainPageItem.setCompoundDrawables(drawable, null, null, null)
        return mainPageItem
    }

    //根据后台开关控制UI显示，"off"直接在 版本对应的textView上加入drawableTop
    private fun syncUICheckNewMainSwitch(drawable: Drawable?) {
        val drawableSize = resources.getDimensionPixelSize(R.dimen.icon_35dp_size)
        drawable?.setBounds(0, 0, drawableSize, drawableSize)

        if (!config.newmainpage_switch.isOn()) {
            binding.activeTv.setCompoundDrawables(null, drawable, null, null)
            binding.imgActive.visibility = GONE
        } else {
            binding.imgActive.setImageDrawable(drawable)
        }
    }

    //根据后台开关控制UI显示，"off"直接在textView上加入drawableTop
    private fun checkNewMainPageSwitch() {
        val drawableSize = resources.getDimensionPixelSize(R.dimen.icon_35dp_size)
        val layoutParams = binding.rlAppLoad.layoutParams as LayoutParams
        layoutParams.width =
            (ScreenUtil.getScreenWidth(context) - resources.getDimensionPixelSize(R.dimen.icon_13dp_size)) / 4
        binding.rlAppLoad.layoutParams = layoutParams

        if (!config.newmainpage_switch.isOn()) {
            val drawable = resources.getDrawable(R.drawable.app_money_icon)
            drawable.setBounds(0, 0, drawableSize, drawableSize)
            binding.cqkTv.setCompoundDrawables(null, drawable, null, null)
            binding.imgMoney.visibility = GONE
            val drawable2 = resources.getDrawable(R.drawable.app_record_icon)
            drawable2.setBounds(0, 0, drawableSize, drawableSize)
            binding.tzjlTv.setCompoundDrawables(null, drawable2, null, null)
            binding.imgRecord.visibility = GONE
            val drawable3 = resources.getDrawable(R.drawable.app_service_icon)
            drawable3.setBounds(0, 0, drawableSize, drawableSize)
            binding.zxkfTv.setCompoundDrawables(null, drawable3, null, null)
            binding.imgService.visibility = GONE
            val drawable4 = resources.getDrawable(R.drawable.app_download_icon)
            drawable4.setBounds(0, 0, drawableSize, drawableSize)
            binding.appLoadTv.setCompoundDrawables(null, drawable4, null, null)
            binding.imgAppLoad.visibility = GONE
        }
    }

    private fun setWidth(view: View) {
        val layoutParams = view.layoutParams as LayoutParams
        layoutParams.width = (ScreenUtil.getScreenWidth(context) - resources.getDimensionPixelSize(R.dimen.icon_13dp_size)) / 4
        view.layoutParams = layoutParams
    }

    /**
     * 更新公告文字，需要以网页形式显示
     */
    private fun updateNotices(notices: List<NoticeNewBean.ContentBean>?) {
        if (notices.isNullOrEmpty()) {
            binding.noticeLayout.visibility = GONE
            return
        }

        this.notices.addAll(notices)
        val notice = notices.joinToString { it.content }
        binding.noticeLayout.visibility = VISIBLE
        try {
            val html = "<html><head></head><body>$notice</body></html>"
            binding.noticeTip.text = Html.fromHtml(html, null, null)
            binding.noticeTip.isSelected = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateActivityBadges(count: Int){
        if(config.isActive && config.show_active_badge.isOn()){
            if(count == 0){
                binding.badges.visibility = GONE
            }else{
                binding.badges.visibility = VISIBLE
                binding.badges.text = count.toString()
            }
        }else{
            binding.badges.visibility = GONE
        }
    }

    private fun updateLundo(results: List<LunboResult>?) {
        if (results.isNullOrEmpty()) {
            binding.banner.visibility = GONE
            return
        } else {
            binding.banner.visibility = VISIBLE
        }

        bannerData.clear()
        bannerData.addAll(results)
        binding.banner.setPages(object : ViewCreator<LunboResult> {
            override fun createView(context: Context, position: Int): View {
                //这里返回的是轮播图的项的布局 支持任何的布局
                //position 轮播图的第几个项
                val imageView = ImageView(getContext())
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                return imageView
            }

            override fun updateUI(context: Context, view: View, position: Int, data: LunboResult) {
                Glide.with(getContext())
                    .load(data.titleImg.trim { it <= ' ' })
                    .into((view as ImageView))
            }
        }, bannerData)

        if (bannerData.size > 1) {
            //轮播时间设置为后台可配置时间
            val time = config.lunbo_interval_switch
            val period = if(CommonUtils.isNumeric(time)) time.toInt() else 3
            binding.banner.startTurning(period * 1000L)
        }
    }

    fun syncUIWhenStart() {
        version = config.mainpage_version
        if ("v2" == version) {
            binding.activeTv.text = "额度转换"
            val drawable = context.getDrawable(R.drawable.fee_convert_menu_new)
            syncUICheckNewMainSwitch(drawable)
        }
        if ("v3" == version) {
            binding.activeTv.text = "余额生金"
            val drawable = context.getDrawable(R.drawable.icon_with_jijin)
            syncUICheckNewMainSwitch(drawable)
        }
        if ("v1" == version) {
            if (config.isActive) {
                binding.activeTv.text = "优惠活动"
                val drawable = context.getDrawable(R.drawable.app_active_icon)
                syncUICheckNewMainSwitch(drawable)
            } else {
                binding.activeTv.text = "APP下载"
                val drawable = context.getDrawable(R.drawable.app_download_icon)
                syncUICheckNewMainSwitch(drawable)
            }
        }
        if ("v4" == version) {
            binding.activeTv.text = "优惠活动"
            val drawable = context.getDrawable(R.drawable.app_active_icon)
            syncUICheckNewMainSwitch(drawable)
            binding.rlAppLoad.visibility = VISIBLE
            val layoutParams = binding.rlAppLoad.layoutParams as LayoutParams
            layoutParams.width = ScreenUtil.getScreenWidth(context) / 4
            binding.rlAppLoad.layoutParams = layoutParams
            binding.imgMore.visibility = VISIBLE
        } else {
            binding.rlAppLoad.visibility = GONE
        }
    }

    fun syncHeaderWebDatas(){
        fetchNotices()
        //优惠活动角标功能暂时关闭
        //fetchActivityBadges()
        fetchLunbos()
    }

    private fun fetchNotices(){
        scope.launch {
            val result = httpRequest { ManagerFactory.generalManager.fetchNotices(13) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    updateNotices(response.content)
                }else{
                    context.showToast("获取最新公告失败")
                }
            }else if(result is HttpResult.Error){
                context.showToast("获取最新公告失败：${result.message}")
            }
        }
    }

    /**
     * 获取有多少未读的优惠活动事件角标
     */
    private fun fetchActivityBadges(){
        scope.launch {
            val result = httpRequest { ManagerFactory.generalManager.fetchActivityBadges() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    updateActivityBadges(response.content)
                }
            }
        }
    }

    private fun fetchLunbos(){
        scope.launch {
            val result = httpRequest { ManagerFactory.generalManager.fetchLunbos(5) }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    updateLundo(response.content)
                }else{
                    context.showToast("获取轮播图失败")
                }
            }else if(result is HttpResult.Error){
                context.showToast("获取轮播图失败：${result.message}")
            }
        }
    }

    fun bindDelegate(delegate: MainHeaderDelegate?) { this.delegate = delegate }


}