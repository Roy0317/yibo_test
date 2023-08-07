package com.yibo.yiboapp.views

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anuo.immodule.utils.ChatSpUtils
import com.yibo.yiboapp.BuildConfig
import com.yibo.yiboapp.R
import com.yibo.yiboapp.adapter.BaseRecyclerAdapter
import com.yibo.yiboapp.data.Constant
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.entify.RealDomainWraper
import com.yibo.yiboapp.entify.ServerTimeResponse
import com.yibo.yiboapp.manager.NetworkManager
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.network.RetrofitFactory
import com.yibo.yiboapp.route.LDNetDiagnoUtils.LDNetUtil
import com.yibo.yiboapp.utils.NetWorkTypeUtil
import com.yibo.yiboapp.utils.Utils
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RouteUrlChooseDialog(context: Context, private val data: MutableList<RealDomainWraper.ContentBean>): Dialog(context) {

    private val CHECK_ROUTE_URL = 0x02
    private val COUNT_TIME = 3000000

    private var tv_ip: TextView? = null
    private var tv_connect: TextView? = null
    private var tv_connect_type: TextView? = null
    private var rcy_route_urls: RecyclerView? = null

    private val compositeDisposable = CompositeDisposable()
    @Volatile private var isAutoChoose = false
    private var routeUrlChooseAdapter: RouteUrlChooseAdapter? = null
    private var chooseListener: OnRouteChooseListener? = null
    private var animTimer: CountDownTimer? = null

    init {
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.dialog_route_url_choose, null)
        tv_ip = view.findViewById(R.id.tv_ip)
        tv_connect = view.findViewById(R.id.tv_connect)
        tv_connect_type = view.findViewById(R.id.tv_connect_type)
        rcy_route_urls = view.findViewById(R.id.rcy_route_urls)
        initRecyclerView()
        setContentView(view)

        val window = window!!
        window.setBackgroundDrawableResource(R.drawable.popup_dialog_bg)
        val layoutParams = window.attributes
        val d = window.windowManager.defaultDisplay
        layoutParams.width = d.width / 5 * 4
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = layoutParams
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    private fun initRecyclerView(){
        routeUrlChooseAdapter = RouteUrlChooseAdapter(context, data,
            object: OnRouteChooseListener {
                override fun onChoose(contentBean: RealDomainWraper.ContentBean, position: Int) {
                    NetworkManager.setRouteUrl(contentBean, position == 0)
                    contentBean.isChoosed = true
                    routeUrlChooseAdapter!!.notifyDataSetChanged()
                    if (chooseListener != null) {
                        RetrofitFactory.resetBaseUrl(Urls.BASE_URL)
                        chooseListener!!.onChoose(contentBean, position)
                    }
                    dismiss()
                }
                override fun onAutoRouteFailed() {}
            })
        rcy_route_urls!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rcy_route_urls!!.layoutManager = LinearLayoutManager(context)
        rcy_route_urls!!.adapter = routeUrlChooseAdapter
    }

    override fun show() {
        super.show()
//        Toast.makeText(context,"当前网路类型："+ NetWorkTypeUtil.getNetworkState(context),Toast.LENGTH_SHORT).show()
        tv_connect!!.text = if (LDNetUtil.isNetworkConnected(context)) "当前是否联网: 已联网" else "当前是否联网: 未联网"
        tv_connect_type!!.text = "网路类型：" + LDNetUtil.getNetWorkType(context)
        getIp()
        getRouteList()
        //每隔10秒刷新一下延迟
        if (animTimer == null) {
            animTimer = object : CountDownTimer(COUNT_TIME.toLong(), 10_000) {
                override fun onTick(millisUntilFinished: Long) { syncGetDelay(data) }
                override fun onFinish() {}
            }
        }
        animTimer!!.start()
    }

    override fun dismiss() {
        compositeDisposable.clear()
        animTimer?.cancel()
        animTimer = null
        super.dismiss()
    }

    fun setChooseListener(chooseListener: OnRouteChooseListener) {
        this.chooseListener = chooseListener
    }

    private fun getIp() {
        RetrofitFactory.api().findMyIP()
            .map { response -> response.ip2.ifNullOrEmpty { response.ip1 } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: SingleObserver<String>{
                override fun onSubscribe(d: Disposable) { compositeDisposable.add(d) }
                override fun onSuccess(ip: String) {
                    ChatSpUtils.instance(context).beT_IP = ip
                    if(isShowing){
                        if (TextUtils.isEmpty(ip)) {
                            tv_ip!!.visibility = View.GONE
                        } else {
                            tv_ip!!.visibility = View.VISIBLE
                            tv_ip!!.text = "当前IP:$ip"
                        }
                    }
                }
                override fun onError(e: Throwable) {}
            })
    }

    private fun getRouteList() {
        val domain = YiboPreference.instance(context).urlForDomains
        RetrofitFactory.api().fetchDomainList("$domain${Urls.GET_APP_ROUTE_URL_LIST}", BuildConfig.APPLICATION_ID, Constant.YB_SOURCE, UsualMethod.getReleaseAndSdkVersion())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: SingleObserver<RealDomainWraper>{
                override fun onSubscribe(d: Disposable) { compositeDisposable.add(d) }
                override fun onSuccess(response: RealDomainWraper) {
                    if (response.content.isNotEmpty()) {
                        NetworkManager.saveDomainsJson(response)
                        data.clear()
                        data.addAll(response.content)
                        routeUrlChooseAdapter!!.notifyDataSetChanged()
                    }
                    syncGetDelay(data)
                }
                override fun onError(e: Throwable) { syncGetDelay(data) }
            })
    }

    /**
     * 异步获取线路的延时
     */
    private fun syncGetDelay(data: List<RealDomainWraper.ContentBean>) {
        data.forEachIndexed { index, bean ->
            val baseUrl: String = bean.ip.ifNullOrEmpty{ bean.domain.ifNullOrEmpty { Urls.BASE_URL } }
            val timeMillis = System.currentTimeMillis()

            RetrofitFactory.api().fetchServerTime("${baseUrl}/native/getServerTime.do")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: SingleObserver<ServerTimeResponse>{
                    override fun onSubscribe(d: Disposable) { compositeDisposable.add(d) }
                    override fun onSuccess(response: ServerTimeResponse) {
                        if (response.success) {
                            val timeMillis1 = System.currentTimeMillis()
                            bean.delay = timeMillis1 - timeMillis
                            Utils.logAll("RouteUrlChoose", "route name = " + bean.name + ", autoChoose = " + isAutoChoose)
                            if (isAutoChoose) {
                                isAutoChoose = false
                                NetworkManager.setRouteUrl(bean, true)
                                routeUrlChooseAdapter!!.chooseListener!!.onChoose(bean, data.indexOf(bean))
                            }
                        } else {
                            bean.delay = 1000L
                        }
                        routeUrlChooseAdapter?.notifyDataSetChanged()
                    }
                    override fun onError(e: Throwable) {
                        bean.delay = 1000L
                        routeUrlChooseAdapter?.notifyDataSetChanged()
                    }
                })
        }

        // 增加一个10秒内回调线路选择失败的定时，
        compositeDisposable.add(Completable.timer(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { chooseListener?.onAutoRouteFailed() })
    }

    fun autoChooseRoute() {
        isAutoChoose = true
        getRouteList()
    }

    /**
     * 呼叫autoChooseRoute()者必须呼叫stopAutoChooseRoute()，不然10秒后会触发chooseListener?.onAutoRouteFailed()的回调
     */
    fun stopAutoChooseRoute(){
        compositeDisposable.clear()
    }

    private class RouteUrlChooseAdapter(context: Context, private val rows: List<RealDomainWraper.ContentBean>?,
                                        val chooseListener: OnRouteChooseListener?) :
        BaseRecyclerAdapter<RealDomainWraper.ContentBean?>(context, rows) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(mInflater.inflate(R.layout.item_route_url, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val viewHolder = holder as ViewHolder
            val contentBean = rows!![position]

            val delay = contentBean.delay
            if (delay <= 100) {
                viewHolder.iv_route_signal.setImageResource(R.drawable.signal5)
                viewHolder.tv_route_delay.setTextColor(ctx.resources.getColor(R.color.green))
            } else if (delay <= 300) {
                viewHolder.iv_route_signal.setImageResource(R.drawable.signal4)
                viewHolder.tv_route_delay.setTextColor(ctx.resources.getColor(R.color.green))
            } else if (delay <= 500) {
                viewHolder.iv_route_signal.setImageResource(R.drawable.signal3)
                viewHolder.tv_route_delay.setTextColor(ctx.resources.getColor(R.color.green))
            } else if (delay <= 800) {
                viewHolder.iv_route_signal.setImageResource(R.drawable.signal2)
                viewHolder.tv_route_delay.setTextColor(ctx.resources.getColor(R.color.red))
            } else {
                viewHolder.iv_route_signal.setImageResource(R.drawable.signal1)
                viewHolder.tv_route_delay.setTextColor(ctx.resources.getColor(R.color.red))
            }

            viewHolder.tv_route_name.text = contentBean.name.ifNullOrEmpty { "暂无名称" }
            viewHolder.tv_route_name.setOnClickListener {
                chooseListener?.onChoose(contentBean, position)
            }
            viewHolder.cb_route.setOnClickListener {
                chooseListener?.onChoose(contentBean, position)
            }
            viewHolder.tv_route_delay.text = if (contentBean.delay == 0L) "--ms" else "${contentBean.delay}ms"
            val chooseRouteID = YiboPreference.instance(ctx).choosE_ROUTE_ID
            viewHolder.cb_route.isChecked = chooseRouteID == contentBean.id
        }
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_route_signal: ImageView
        var tv_route_name: TextView
        var tv_route_delay: TextView
        var cb_route: RadioButton

        init {
            iv_route_signal = itemView.findViewById(R.id.iv_route_signal)
            tv_route_name = itemView.findViewById(R.id.tv_route_name)
            tv_route_delay = itemView.findViewById(R.id.tv_route_delay)
            cb_route = itemView.findViewById(R.id.rb_route_choose)
        }
    }

    interface OnRouteChooseListener {
        fun onChoose(contentBean: RealDomainWraper.ContentBean, position: Int)
        fun onAutoRouteFailed()
    }
}