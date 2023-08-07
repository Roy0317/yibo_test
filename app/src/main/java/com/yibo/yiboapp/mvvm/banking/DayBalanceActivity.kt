package com.yibo.yiboapp.mvvm.banking

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yibo.yiboapp.R
import com.yibo.yiboapp.databinding.ActivityDayBalanceBinding
import com.yibo.yiboapp.entify.DayBalanceResponse
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.mvvm.BaseActivityKotlin
import com.yibo.yiboapp.mvvm.hideLoading
import com.yibo.yiboapp.mvvm.showLoading
import com.yibo.yiboapp.mvvm.showToast
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.views.CustomDatePicker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * 		 1.startTime 开始时间 (没传预设今天)
 *       2.endTime 结束时间   (没传预设今天)
 *       3.winInfoName 分类名称
 *       4.betAmount 下注
 *       5.winAmount 中奖
 *       6.profitAndLossAmount 盈亏
 *
 *       开关没开dailyMoneyData就不会有该项。(没开启页面不会有该栏位)
 *       总下注、总中奖、总盈亏只会对有开启开关的进行计算。
 *       试玩账号dailyMoneyData只会有彩票跟总。
 *       试玩账号如果彩票没开将不会有dailyMoneyData。
 */
class DayBalanceActivity : BaseActivityKotlin() {

    private lateinit var binding: ActivityDayBalanceBinding
    private lateinit var customDatePicker1: CustomDatePicker
    private lateinit var customDatePicker2: CustomDatePicker
    private var adapter: DayBalanceAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initParams()
    }

    private fun initParams(){
        initCurrentTime()
        fetchDayBalanceData(1000L)
    }

    private fun initView(){
        binding.title.middleTitle.text = "每日输赢"
        binding.title.backText.setOnClickListener { onBackPressed() }
        binding.title.rightText.visibility = View.VISIBLE
        binding.title.rightText.text = "筛选"
        binding.title.rightText.setOnClickListener {
            val isVisible = binding.linearTime.visibility == View.VISIBLE
            binding.linearTime.visibility = if(isVisible) View.GONE else View.VISIBLE
        }
        binding.textStartTime.setOnClickListener {
            customDatePicker1.show(binding.textStartTime.text.toString())
        }
        binding.textEndTime.setOnClickListener {
            customDatePicker2.show(binding.textEndTime.text.toString())
        }
        binding.buttonConfirm.setOnClickListener {
            binding.linearTime.visibility = View.GONE
            fetchDayBalanceData()
        }
        initDatePicker()
    }

    private fun initDatePicker(){
        val defaultStartTime = "2010-01-01 00:00"
        val defaultEndTime = "2030-12-31 23:59"
        customDatePicker1 = CustomDatePicker(this,
            { time ->
                val date = time.split(" ")
                if(date.isNotEmpty())
                    binding.textStartTime.text = date[0]
            },
            defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm") //初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false) // 显示时和分
        customDatePicker1.setIsLoop(true) // 允许循环滚动

        customDatePicker2 = CustomDatePicker(this,
            { time ->
                val date = time.split(" ")
                if(date.isNotEmpty())
                    binding.textEndTime.text = date[0]
            },
            defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm") //初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false) // 显示时和分
        customDatePicker2.setIsLoop(true) // 允许循环滚动
    }

    private fun initCurrentTime(){
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val today = sdf.format(Date())
        binding.textStartTime.text = today
        binding.textEndTime.text = today
    }

    private fun fetchDayBalanceData(time: Long = 0){
        lifecycleScope.launch {
            if(time != 0L){
                delay(time)
            }
            showLoading()
            val start = binding.textStartTime.text.toString()
            val end = binding.textEndTime.text.toString()
            val param = mapOf<String, String>(
                "startTime" to start,
                "endTime" to end
            )
            val result = httpRequest { ManagerFactory.bankingManager.fetchDayBalanceData(param) }
            hideLoading()
            if(result is HttpResult.Success){
                val response = result.data
                if(response.success){
                    updateBalanceSheet(response.content.balanceList)
                }else{
                    showToast("获取资料失败:${response.msg}")
                    adapter?.clear()
                }
            }else if(result is HttpResult.Error){
                showToast("获取资料时发生错误:${result.message}")
                adapter?.clear()
            }
        }
    }

    private fun updateBalanceSheet(balanceList: List<DayBalanceResponse.Content.DayBalance>){
        if(adapter == null){
            adapter = DayBalanceAdapter(balanceList.toMutableList())
            binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.recyclerView.adapter = adapter
        }else{
            adapter?.update(balanceList)
        }
    }

    class DayBalanceAdapter(private val balanceList: MutableList<DayBalanceResponse.Content.DayBalance>):
        RecyclerView.Adapter<DayBalanceVH>(){


        fun update(data: List<DayBalanceResponse.Content.DayBalance>){
            balanceList.clear()
            balanceList.addAll(data)
            notifyDataSetChanged()
        }

        fun clear(){
            balanceList.clear()
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = balanceList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayBalanceVH {
            val view = View.inflate(parent.context, R.layout.item_day_balance, null)
            return DayBalanceVH(view)
        }

        override fun onBindViewHolder(holder: DayBalanceVH, position: Int) {
            holder.bindView(position, balanceList[position])
        }
    }

    class DayBalanceVH(val view: View): RecyclerView.ViewHolder(view){
        private val linearBG = view.findViewById<LinearLayout>(R.id.linear)
        private val textLotteryType = view.findViewById<TextView>(R.id.textLotteryType)
        private val textBetAmount = view.findViewById<TextView>(R.id.textBetAmount)
        private val textWinAmount = view.findViewById<TextView>(R.id.textWinAmount)
        private val textBalanceAmount = view.findViewById<TextView>(R.id.textBalanceAmount)

        fun bindView(position: Int, balance: DayBalanceResponse.Content.DayBalance){
            if(position%2 == 0){
                linearBG.setBackgroundColor(Color.parseColor("#F9F9F9"))
            }else{
                linearBG.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

            textLotteryType.text = balance.winInfoName
            textBetAmount.text = String.format("下注:%.2f元", balance.betAmount)
            textWinAmount.text = String.format("中奖:%.2f元", balance.winAmount)
            textBalanceAmount.text = String.format("盈亏:%.2f元", balance.balanceAmount)
        }
    }
}