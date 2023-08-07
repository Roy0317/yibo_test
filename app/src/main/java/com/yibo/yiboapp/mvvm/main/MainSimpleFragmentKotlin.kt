package com.yibo.yiboapp.mvvm.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yibo.yiboapp.activity.*
import com.yibo.yiboapp.data.*
import com.yibo.yiboapp.databinding.MainModuleBinding
import com.yibo.yiboapp.entify.LotterysWraper
import com.yibo.yiboapp.fragment.BaseMainFragment
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.utils.Utils
import kotlinx.coroutines.launch

class MainSimpleFragmentKotlin: BaseMainFragment() {

    private lateinit var binding: MainModuleBinding
    private val sysConfig by lazy { UsualMethod.getConfigFromJson(activity) }
    private var lotterysWraper: LotterysWraper? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MainModuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        binding.header.syncHeaderWebDatas()
        binding.header.bindDelegate(delegate)
        fetchGameData()
    }

    private fun initView(){
        binding.caipiaoLayout.setOnClickListener { CaipiaoActivity.createIntent(activity) }

        binding.sportLayout.setOnClickListener {
            if (Utils.isTestPlay(activity)) {
                activity?.showToast("试玩帐号没有权限")
                return@setOnClickListener
            }

            if (lotterysWraper?.content == null) {
                activity?.showToast("没有体育数据")
                return@setOnClickListener
            }

            val sports = lotterysWraper?.content?.filter { it.moduleCode == 0 }
            if (sports.isNullOrEmpty()) {
                activity?.showToast("没有体育数据")
                return@setOnClickListener
            }

            val listType = object: TypeToken<List<LotteryData>>() {}.type
            val json = Gson().toJson(sports, listType)
            SportListActivity.createIntent(activity, json)
        }

        binding.zhenrenLayout.setOnClickListener {
            if (Utils.isTestPlay(this.activity)) {
                activity?.showToast("试玩帐号没有权限")
                return@setOnClickListener
            }

            if (!sysConfig.onoff_zhen_ren_yu_le.isOn()) {
                activity?.showToast("请先在后台开启真人开关")
                return@setOnClickListener
            }

            OtherPlayActivity.createIntent(activity, "真人", Constant.REAL_MODULE_CODE)
        }

        binding.gameLayout.setOnClickListener {
            if (Utils.isTestPlay(this.activity)) {
                activity?.showToast("试玩帐号没有权限")
                return@setOnClickListener
            }

            if (!sysConfig.onoff_dian_zi_you_yi.isOn()) {
                activity?.showToast("请先在后台开启游戏开关")
                return@setOnClickListener
            }
            OtherPlayActivity.createIntent(activity, "电子", Constant.GAME_MODULE_CODE)
        }

        binding.usualBtnLayout.setOnClickListener {
            if (!sysConfig.onoff_dian_zi_you_yi.isOn()) {
                activity?.showToast("请先在后台开启游戏开关")
                return@setOnClickListener
            }
            SettingUsualGameActivity.createIntent(activity, "")
        }
    }

    override fun setOnlineCount(count: String) {
        binding.tvOnlineCount.visibility = View.VISIBLE
        binding.tvOnlineCount.text = "在线人数:${count}人"
    }

    override fun refreshNewMainPageLoginBlock(isLogin: Boolean, accountName: String, balance: Double) {}

    private fun fetchGameData(){
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.gameManager.fetchAllGameData() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.isSuccess){
                    YiboPreference.instance(activity).token = response.accessToken
                    lotterysWraper = response
                    CacheRepository.getInstance().saveLotteryData(activity, response.content)
                }else{
                    activity?.showToast(response.msg.ifNullOrEmpty { "获取彩种信息失败" })
                    checkSessionValidity(response.code)
                }
            }else if(result is HttpResult.Error){
                activity?.showToast(result.message)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(activity is MainActivity)
            (activity as MainActivity).hidenorshow(hidden)
    }
}