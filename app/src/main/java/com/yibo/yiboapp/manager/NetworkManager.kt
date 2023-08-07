package com.yibo.yiboapp.manager

import com.google.gson.Gson
import com.yibo.yiboapp.BuildConfig
import com.yibo.yiboapp.application.YiboApplication
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.entify.RealDomainWraper
import com.yibo.yiboapp.utils.Utils

object NetworkManager {

    private val TAG = this::class.java.simpleName


    fun saveDomainsJson(response: RealDomainWraper){
        val context = YiboApplication.getInstance()

        if (response.content.isNotEmpty()) {
            // 将打包域名添加到线路列表里
            val bean = RealDomainWraper.ContentBean()
            bean.domain = YiboPreference.instance(context).packagE_DOMAIN
            bean.ip = YiboPreference.instance(context).packagE_HOST_URL
            bean.routeType = 0
            bean.name = "初始线路"
            bean.id = 0
            response.content.add(0, bean)

            val routeUrlsJsonStr = Gson().toJson(response.content)
            Utils.logAll(TAG, "all routes = $routeUrlsJsonStr")
            YiboPreference.instance(context).routE_URLS = routeUrlsJsonStr

            // 如果本地没有保存选择的线路或选择的是自动线路，就选中打包线路
            if (YiboPreference.instance(context).choosE_ROUTE.isEmpty() || YiboPreference.instance(context).isAutoRoute) {
                setRouteUrl(bean, YiboPreference.instance(context).isAutoRoute)
            } else {
                Urls.BASE_URL = YiboPreference.instance(context).choosE_ROUTE
                Urls.BASE_HOST_URL = YiboPreference.instance(context).choosE_HOST_URL
                Urls.APP_ROUTE_TYPE = YiboPreference.instance(context).choosE_ROUTE_TYPE
            }
        }
    }

    fun setRouteUrl(bean: RealDomainWraper.ContentBean, isAutoChoose: Boolean = false) {
        val context = YiboApplication.getInstance()
        Utils.logAll(TAG, "ip = ${bean.ip}, domain = ${bean.domain}")

        //如果获取的线路中IP和HOST域名都存在,则更新请求基础域名和请求域名对应的host域名
        if (bean.ip.isNotEmpty() && bean.domain.isNotEmpty()) {
            Urls.BASE_URL = bean.ip
            Urls.APP_ROUTE_TYPE = 3
            Urls.BASE_HOST_URL = bean.domain
        } else if (!Utils.isEmptyString(bean.ip)) {
            Urls.BASE_URL = bean.ip
            Urls.APP_ROUTE_TYPE = 2
            Urls.BASE_HOST_URL = ""
        } else {
            if (bean.domain.isNotEmpty()) {
                Urls.BASE_URL = bean.domain
                Urls.APP_ROUTE_TYPE = 2
            }else{
                Urls.BASE_URL = BuildConfig.domain_url
                Urls.APP_ROUTE_TYPE = 1
            }
            Urls.BASE_HOST_URL = ""
        }
        YiboPreference.instance(context).choosE_ROUTE = Urls.BASE_URL
        YiboPreference.instance(context).choosE_HOST_URL = Urls.BASE_HOST_URL
        YiboPreference.instance(context).choosE_ROUTE_TYPE = Urls.APP_ROUTE_TYPE
        YiboPreference.instance(context).choosE_ROUTE_NAME = bean.name
        YiboPreference.instance(context).choosE_ROUTE_ID = bean.id
        YiboPreference.instance(context).setAUTO_ROUTE(isAutoChoose)
    }
}