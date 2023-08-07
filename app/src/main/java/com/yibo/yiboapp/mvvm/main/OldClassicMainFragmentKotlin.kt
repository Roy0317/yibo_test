package com.yibo.yiboapp.mvvm.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.anuo.immodule.utils.ScreenUtil
import com.gongwen.marqueen.MarqueeFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yibo.yiboapp.R
import com.yibo.yiboapp.activity.*
import com.yibo.yiboapp.adapter.GameAdapter
import com.yibo.yiboapp.adapter.GameAdapter.GameEventDelegate
import com.yibo.yiboapp.adapter.WinningDataMF
import com.yibo.yiboapp.data.*
import com.yibo.yiboapp.databinding.OldClassicMainViewBinding
import com.yibo.yiboapp.entify.UpdateAllGameEvent
import com.yibo.yiboapp.fragment.BaseMainFragment
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.*
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity
import com.yibo.yiboapp.utils.Utils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import java.util.*

class OldClassicMainFragmentKotlin: BaseMainFragment() {

    private lateinit var binding: OldClassicMainViewBinding
    private val mainTabs: MutableList<String> = ArrayList()
    private var currentTabName: String = ""  //当前选中的tab名称

    private var gameAdapter: GameAdapter? = null
    private val allData: MutableList<LotteryData> = ArrayList()
    private var currentGameData: MutableList<LotteryData> = ArrayList()

    private val winningData: MutableList<WinningDataWraper> = ArrayList()
    private var marqueeFactory: MarqueeFactory<LinearLayout, WinningDataWraper>? = null
    private var lastEventJson: String = ""
    private var nowGameName = "" //当前的游戏名称 点击真人和电子的时候才用这个参数
    private val disposable = CompositeDisposable()
    private val sysConfig by lazy { UsualMethod.getConfigFromJson(activity) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = OldClassicMainViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        binding.header.syncHeaderWebDatas()
        binding.header.bindDelegate(delegate)
        initGameAdapter()
        updateLotteryViewFromBackup()
        fetchGameData()
        getWinningData()

        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        if(!sysConfig.newmainpage_switch.isOn()){
            binding.header.syncUIWhenStart()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.marqueeView.startFlipping()
    }

    override fun onStop() {
        super.onStop()
        binding.marqueeView.stopFlipping()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MainActivity).hidenorshow(hidden)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun initView(){
        binding.emptyView.item.apply {
            clickRefresh?.text = "开始网络检测"
            emptyTxt?.visibility = View.GONE
            iv_empty_image?.visibility = View.GONE
        }
        binding.gridGames.isEnabled = false
        binding.scrollBar.attachScrollView(binding.tabScrollView)
        marqueeFactory = WinningDataMF(activity)
        binding.marqueeView.setMarqueeFactory(marqueeFactory)
        binding.marqueeView.startFlipping()
        initTabs()

        binding.emptyView.clickRefresh.setOnClickListener {
            startActivity(Intent(activity, RouteCheckingActivity::class.java))
            refreshLotteryMessage()
        }

        binding.refreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        binding.refreshLayout.setOnRefreshListener {
            if (currentTabName.isNotEmpty()) {
                updateTabWhenClick(currentTabName)
            } else if (mainTabs.isNotEmpty()) {
                updateTabWhenClick(mainTabs[0])
            }
            binding.header.syncHeaderWebDatas()
            refreshLotteryMessage()
            getWinningData()
        }
    }

    private fun initTabs(){
        binding.llWinningData.visibility = if(sysConfig.onoff_show_winning_data.isOn()) View.VISIBLE else View.GONE

        val mobileIndex = sysConfig.mobileIndex
        if (mobileIndex.equals(Constant.SELECT_TABS, true)) {
            binding.tabScrollView.visibility = View.VISIBLE
            binding.scrollBar.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
            if (!sysConfig.mainpage_module_indexs.isNullOrEmpty()) {
                val sorts = sysConfig.mainpage_module_indexs.split(",")
                if (sorts.isNotEmpty()) {
                    sorts.forEach { sort ->
                        //(1-彩票,3-真人,4-电子,5-体育,6-棋牌,7-红包游戏,8-电竞,9--捕鱼,10--热门
                        if (sort.isNotEmpty() && Utils.isInteger(sort)) {
                            when (sort) {
                                "1" -> mainTabs.add("彩票")
                                "3" -> mainTabs.add("真人")
                                "4" -> mainTabs.add("电子")
                                "5" -> mainTabs.add("体育")
                                "6" -> mainTabs.add("棋牌")
                                "8" -> mainTabs.add("电竞")
                                "9" -> mainTabs.add("捕鱼")
                            }
                        }
                    }
                }
            }
        } else {
            binding.tabScrollView.visibility = View.GONE
            binding.scrollBar.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
        }
        if (mainTabs.size == 1) {
            binding.tabScrollView.visibility = View.GONE
            binding.scrollBar.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
        }

        //根据主页选项卡排序动态添加选项卡view
        binding.tabLayout.removeAllViews()
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.width = ScreenUtil.getScreenWidth(activity) / 4
        params.weight = 1f
        //根据排序创建主页tab栏
        for (tab in mainTabs) {
            val layout = LayoutInflater.from(activity).inflate(R.layout.sub_tab_layout, null)
            val tabName = layout.findViewById<TextView>(R.id.m)
            layout.setOnClickListener {
                currentTabName = tab
                updateTabWhenClick(tab)
                updateSelectDataWithCurrentPage(tab)
            }
            tabName.text = tab
            binding.tabLayout.addView(layout, params)
        }
        //默认选中第一个选项卡
        if (mainTabs.isNotEmpty()) {
            updateTabWhenClick(mainTabs[0])
        }
    }

    private fun initGameAdapter(){
        gameAdapter = GameAdapter(activity, currentGameData, R.layout.caipiao_item)
        gameAdapter!!.setDelegate(object: GameEventDelegate{
            override fun onGameEvent(gameCode: String, gameModue: Int, gameName: String, data: LotteryData) {
                Utils.LOG(TAG, "onGameEvent，gameCode = $gameCode, gameModule = $gameModue, name = $gameName, data = $data")

                if (gameModue == LotteryData.CAIPIAO_MODULE) {
                    if (gameCode == Constant.YCP_CODE) {
                        forwardGame(data.forwardUrl.ifNullOrEmpty { data.forwardAction ?: "" })
                    } else {
                        onCaipiaoClicked(gameCode)
                    }
                } else {
                    if (Utils.isTestPlay(activity)) {
                        return
                    }
                    when (data.isListGame) {
                        1 -> GameListActivity.createIntent(context, data.name, data.czCode)
                        2 -> SportActivity.createIntent(context, data.name, Constant.SPORT_MODULE_CODE.toString() + "")
                        0,5 -> {
                            nowGameName = gameName
                            forwardGame(data.forwardUrl)
                        }
                    }
                }
            }
        })
        binding.gridGames.adapter = gameAdapter
    }

    private fun forwardGame(forwardUrl: String){
        val u = URL(Urls.BASE_URL + Urls.PORT + forwardUrl)
        val path = u.path
        val params = if(u.query.isNullOrEmpty()) mapOf() else
            u.query.split("&")
                .filter { it.contains("=") }
                .associate {
                    val a = it.split("=")
                    a[0] to a[1]
                }


        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.gameManager.forwardGame(path, params) }
            binding.refreshLayout.isRefreshing = false
            if(result is HttpResult.Success){
                val response = result.data
                if(response.success){
                    if (!response.html.isNullOrEmpty() && response.url.isNullOrEmpty()) {
                        //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
                        BBinActivity.createIntent(activity, response.html, "bbin")
                        return@launch
                    }

                    //AG,MG,AB,OG,DS都返回跳转链接
                    //BBIN 返回的是一段html内容
                    val url = response.url
                    val content = response.content
                    if (!url.isNullOrEmpty()) {
                        if (sysConfig.zrdz_jump_broswer.isOn()) {
                            //跳转到浏览器
                            UsualMethod.actionViewGame(activity, url)
                        } else {
                            //只用来限制新沙巴体育
                            if (nowGameName == "新沙巴体育" && sysConfig.onoff_new_shaba_jump_browsers.isOn()) {
                                UsualMethod.actionViewGame(activity, url)
                            }else{
                                SportNewsWebActivity.createIntent(activity, url, nowGameName)
                            }
                        }
                    } else if (!content.isNullOrEmpty()) {
                        if (sysConfig.zrdz_jump_broswer.isOn()) {
                            UsualMethod.actionViewGame(activity, content)
                        } else {
                            if (nowGameName == "新沙巴体育" && sysConfig.onoff_new_shaba_jump_browsers.isOn()) {
                                UsualMethod.actionViewGame(activity, content)
                            }else{
                                SportNewsWebActivity.createIntent(activity, content, nowGameName)
                            }
                        }
                    } else {
                        val html = response.html
                        //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
                        BBinActivity.createIntent(activity, html, "bbin")
                    }
                }else{
                    if (!response.msg.isNullOrEmpty()) {
                        activity?.showToast(response.msg)
                        if (response.msg.contains("超时") || response.msg.contains("其他")) {
                            UsualMethod.loginWhenSessionInvalid(activity)
                        }
                    } else {
                        activity?.showToast(R.string.jump_fail)
                    }
                }
            }else if(result is HttpResult.Error){
                activity?.showToast("${getString(R.string.jump_fail)}，${result.message}")
            }
        }
    }

    /**
     * 点击tab 时改变tab状态
     */
    private fun updateTabWhenClick(tabName: String) {
        for (i in 0 until binding.tabLayout.childCount) {
            val layout: View = binding.tabLayout.getChildAt(i)
            val textName = layout.findViewById<TextView>(R.id.m)
            if (textName.text == tabName) {
                currentTabName = tabName
                textName.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_txt_select))
                layout.background = ContextCompat.getDrawable(requireActivity(), R.drawable.bg_game_tab_selected)
            } else {
                textName.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_txt_normal))
                layout.background = null
            }
        }
    }

    /**
     * 有打开假数据再跟server取资料
     */
    private fun getWinningData() {
        if(!sysConfig.onoff_show_winning_data.isOn()){
            return
        }

        HttpUtil.get(activity, Urls.GET_WINNING_DATA, null, false) { result ->
            if (result.isSuccess) {
                var list = Gson().fromJson<List<WinningDataWraper>>(result.content,
                    object : TypeToken<List<WinningDataWraper>>() {}.type)

                //笔数太多了造成ANR
                if (list.size > 100) {
                    list = list.subList(0, 100)
                }
                winningData.clear()
                winningData.addAll(list)
                marqueeFactory!!.data = winningData
            }
        }
    }

    private fun fetchGameData(){
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.gameManager.fetchAllGameData() }
            binding.refreshLayout.isRefreshing = false
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    disposable.clear()
                    updateLotteryView(response.content)
                    if (response.content != null) {
                        val listType = object : TypeToken<List<LotteryData>>() {}.type
                        lastEventJson = Gson().toJson(response.content, listType)
                        CacheRepository.getInstance().saveLotteryData(activity, response.content)
                    }
                }else{
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                activity?.showToast("获取彩种信息时发生错误：${result.message}")
            }
        }
    }

    private fun updateLotteryViewFromBackup() {
        CacheRepository.getInstance().loadLotteryData(activity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<LotteryData>> {
                override fun onSubscribe(d: Disposable) { disposable.add(d) }
                override fun onSuccess(data: List<LotteryData>) {
                    Utils.LOG(TAG, "!data.isEmpty() ==> " + (data.isNotEmpty()))
                    if (data.isNotEmpty())
                        updateLotteryView(data)
                }
                override fun onError(e: Throwable) { e.printStackTrace() }
            })
    }

    private fun updateLotteryView(data: List<LotteryData>?) {
        allData.clear()
        if (data.isNullOrEmpty()) {
            return
        }

        currentGameData.clear()
        allData.addAll(data)
        val cp: MutableList<LotteryData> = ArrayList()
        val sport: MutableList<LotteryData> = ArrayList()
        val zhenren: MutableList<LotteryData> = ArrayList()
        val dianzhi: MutableList<LotteryData> = ArrayList()
        val chess: MutableList<LotteryData> = ArrayList()
        val esport: MutableList<LotteryData> = ArrayList()
        val buyu: MutableList<LotteryData> = ArrayList()

        for (d in data) {
            when (d.moduleCode) {
                LotteryData.CAIPIAO_MODULE -> { cp.add(d) }
                LotteryData.REALMAN_MODULE -> { zhenren.add(d) }
                LotteryData.DIANZI_MODULE -> { dianzhi.add(d) }
                LotteryData.SPORT_MODULE -> { sport.add(d) }
                LotteryData.CHESS_MODULE -> { chess.add(d) }
                LotteryData.ESPORT_MODULE -> { esport.add(d) }
                LotteryData.BUYU_MODULE -> { buyu.add(d) }
            }
        }

        val mobileIndex = sysConfig.mobileIndex
        if (mobileIndex.equals(Constant.TUCHU_LOTTERY)) {
            currentGameData.addAll(cp)
            currentGameData.addAll(sport)
            currentGameData.addAll(zhenren)
            currentGameData.addAll(dianzhi)
            currentGameData.addAll(chess)
            currentGameData.addAll(esport)
            currentGameData.addAll(buyu)
        } else if (mobileIndex.equals(Constant.TUCHU_ZHENREN)) {
            currentGameData.addAll(zhenren)
            currentGameData.addAll(dianzhi)
            currentGameData.addAll(cp)
            currentGameData.addAll(sport)
            currentGameData.addAll(chess)
            currentGameData.addAll(esport)
            currentGameData.addAll(buyu)
        } else if (mobileIndex.equals(Constant.TUCHU_SPORT)) {
            currentGameData.addAll(sport)
            currentGameData.addAll(cp)
            currentGameData.addAll(zhenren)
            currentGameData.addAll(dianzhi)
            currentGameData.addAll(chess)
            currentGameData.addAll(esport)
            currentGameData.addAll(buyu)
        } else if (mobileIndex.equals(Constant.TUCHU_QIPAI)) {
            currentGameData.addAll(chess)
            currentGameData.addAll(cp)
            currentGameData.addAll(sport)
            currentGameData.addAll(zhenren)
            currentGameData.addAll(dianzhi)
            currentGameData.addAll(esport)
            currentGameData.addAll(buyu)
        } else if (mobileIndex.equals(Constant.SELECT_TABS)) {
            if (mainTabs.isNotEmpty()) {
                when (currentTabName) {
                    "彩票" -> { currentGameData.addAll(cp) }
                    "真人" -> { currentGameData.addAll(zhenren) }
                    "棋牌" -> { currentGameData.addAll(chess) }
                    "电子" -> { currentGameData.addAll(dianzhi) }
                    "体育" -> { currentGameData.addAll(sport) }
                    "电竞" -> { currentGameData.addAll(esport) }
                    "捕鱼" -> { currentGameData.addAll(buyu) }
                }
            }
        } else {
            currentGameData.addAll(cp)
            currentGameData.addAll(sport)
            currentGameData.addAll(zhenren)
            currentGameData.addAll(dianzhi)
            currentGameData.addAll(chess)
            currentGameData.addAll(esport)
            currentGameData.addAll(buyu)
        }
        gameAdapter!!.notifyDataSetChanged()
        Utils.setListViewHeightBasedOnChildren(binding.gridGames, 3)
        lifecycleScope.launch {
            delay(300)
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun updateSelectDataWithCurrentPage(tabName: String) {
        if (allData.isEmpty()) {
            return
        }

        currentGameData.clear()
        for (d in allData) {
            if (tabName == "彩票") {
                if (d.moduleCode == LotteryData.CAIPIAO_MODULE) {
                    currentGameData.add(d)
                }
            } else if (tabName == "真人") {
                if (d.moduleCode == LotteryData.REALMAN_MODULE) {
                    currentGameData.add(d)
                }
            } else if (tabName == "棋牌") {
                if (d.moduleCode == LotteryData.CHESS_MODULE) {
                    currentGameData.add(d)
                }
            } else if (tabName == "电子") {
                if (d.moduleCode == LotteryData.DIANZI_MODULE) {
                    currentGameData.add(d)
                }
            } else if (tabName == "体育") {
                if (d.moduleCode == LotteryData.SPORT_MODULE) {
                    currentGameData.add(d)
                }
            } else if (tabName == "电竞") {
                if (d.moduleCode == LotteryData.ESPORT_MODULE) {
                    currentGameData.add(d)
                }
            } else if (tabName == "捕鱼") {
                if (d.moduleCode == LotteryData.BUYU_MODULE) {
                    currentGameData.add(d)
                }
            }
        }
        gameAdapter!!.notifyDataSetChanged()
        Utils.setListViewHeightBasedOnChildren(binding.gridGames, 3)
        lifecycleScope.launch {
            delay(300)
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun onCaipiaoClicked(gameCode: String) {
        CacheRepository.getInstance().loadLotteryPlayJson(activity, gameCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) { disposable.add(d) }

                override fun onSuccess(json: String) {
                    Utils.LOG(TAG, "find the backup of play rules")
                    openTouzhuPage(gameCode, json, true)
                }

                override fun onError(e: Throwable) {
                    Utils.LOG(TAG, "can't find the backup of play rules")
                    e.printStackTrace()
                    fetchLotteryPlays(gameCode)
                }
            })
    }

    private fun openTouzhuPage(gameCode: String, json: String, needRefresh: Boolean) {
        val isPeilvVersion = UsualMethod.isSixMark(context, gameCode) || UsualMethod.isPeilvVersionMethod(activity)
        val cpVersion = if (UsualMethod.isSixMark(context, gameCode)) Constant.lottery_identify_V2.toString()
                        else YiboPreference.instance(activity).gameVersion

        val isSimpleStyle = sysConfig.bet_page_style.equals("v1")
        if (isSimpleStyle) {
            TouzhuSimpleActivity.createIntent(activity, json, gameCode, needRefresh, isPeilvVersion, cpVersion)
        } else {
            TouzhuActivity.createIntent(activity, json, gameCode, needRefresh, isPeilvVersion, cpVersion)
        }
    }

    private fun fetchLotteryPlays(lotteryCode: String){
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.gameManager.fetchLotteryPlays(lotteryCode) }
            binding.refreshLayout.isRefreshing = false
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    if(response.content != null){
                        val json = Gson().toJson(response.content, LotteryData::class.java)
                        val code: String = response.content.code
                        CacheRepository.getInstance().saveLotteryPlayJson(activity, code, json)
                        openTouzhuPage(code, json, false)
                    }
                }else{
                    activity?.showToast("无法取得彩种玩法:${response.msg}")
                }
            }else if(result is HttpResult.Error){
                activity?.showToast("获取玩法失败：${result.message}")
            }
        }
    }

    private fun refreshLotteryMessage() {
        allData.clear()
        currentGameData.clear()
        fetchGameData()
    }

    override fun setOnlineCount(count: String?) {
        binding.tvOnlineCount.visibility = View.VISIBLE
        binding.tvOnlineCount.text = "在线人数:${count}人"
    }

    override fun refreshNewMainPageLoginBlock(isLogin: Boolean, accountName: String, balance: Double) {
        binding.header.refreshNewMainPageLoginBlock(isLogin, accountName, balance)
    }

    @Subscribe
    fun onEvent(event: UpdateAllGameEvent) {
        Utils.LOG(TAG, "onEvent(UpdateAllGameEvent event)")
        if (event.lotteryJson != null && event.lotteryJson != lastEventJson) {
            lastEventJson = event.lotteryJson
            val listType = object : TypeToken<ArrayList<LotteryData>>() {}.type
            val newData = Gson().fromJson<ArrayList<LotteryData>>(event.lotteryJson, listType)
            updateLotteryView(newData)
        }
    }
}