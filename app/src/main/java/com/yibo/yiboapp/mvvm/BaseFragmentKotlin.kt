package com.yibo.yiboapp.mvvm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.utils.Utils

open class BaseFragmentKotlin: Fragment() {

    @JvmField protected val TAG = javaClass.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Utils.logAll(TAG, "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun checkSessionValidity(code: Int){
        //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
        //所以此接口当code == 0时表示帐号被踢，或登录超时
        if (code == 0) {
            UsualMethod.loginWhenSessionInvalid(activity)
        }

        //限流session过期，重新进行限流检测
        if (code == 544) {
            UsualMethod.showVerifyActivity(activity)
        }
    }
}