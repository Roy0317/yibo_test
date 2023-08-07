package com.yibo.yiboapp.manager

import android.content.Context
import com.yibo.yiboapp.activity.LoginActivity
import com.yibo.yiboapp.activity.LoginAndRegisterActivity
import com.yibo.yiboapp.application.YiboApplication
import com.yibo.yiboapp.data.Constant
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.entify.GoogleRobotResponse
import com.yibo.yiboapp.entify.MemInfoWraper
import com.yibo.yiboapp.entify.MemberHeaderWraper
import com.yibo.yiboapp.mvvm.isOn
import com.yibo.yiboapp.network.RetrofitFactory
import com.yibo.yiboapp.utils.Utils
import io.reactivex.Single
import retrofit2.Response
import java.security.NoSuchAlgorithmException

class UserManager {

    companion object{
        private const val USER_HEADER = "user_header"


        fun openLoginPage(context: Context,
                          account: String = YiboPreference.instance(context).username,
                          password: String = ""){

            val sysConfig = UsualMethod.getConfigFromJson(YiboApplication.getInstance())
            if (sysConfig.newmainpage_switch.isOn()) {
                LoginAndRegisterActivity.createIntent(context, account, password, 0)
            } else {
                LoginActivity.createIntent(context, account, password)
            }
        }
    }

    private val applicationContext by lazy { YiboApplication.getInstance() }
    private val sysConfig by lazy { UsualMethod.getConfigFromJson(YiboApplication.getInstance()) }


    fun isLogin(): Boolean{ return YiboPreference.instance(YiboApplication.getInstance()).isLogin }

    fun isTestPlayer(): Boolean{
        if (YiboPreference.instance(applicationContext).accountMode == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
            val username = YiboPreference.instance(applicationContext).username
            if (username.startsWith("guest")) {
                return true
            }
        }
        return false
    }

    fun allowRegisterSwitch(): Boolean {
        return sysConfig.onoff_register.isOn() && !sysConfig.onoff_mobile_app_reg.isOn()
    }

    fun getLoginAccount(): String{ return YiboPreference.instance(applicationContext).username }

    fun saveHeaderPhoto(photo: String){
        YiboPreference.instance(applicationContext).sp.edit()
            .putString(USER_HEADER, photo)
            .apply()
    }

    fun loadHeaderPhoto(): String{
        return YiboPreference.instance(applicationContext).sp
            .getString(USER_HEADER, "") ?: ""
    }



    fun fetchGoogleRobot(): Single<GoogleRobotResponse> {
        return RetrofitFactory.api().fetchGoogleRobot()
    }

    suspend fun fetchMemberInfo(): Response<MemInfoWraper>{
        return RetrofitFactory.api().fetchMemberInfo()
    }

    suspend fun syncHeaderPhoto(): Response<MemberHeaderWraper> {
        val context = YiboApplication.getInstance().applicationContext
        val userName = YiboPreference.instance(context).username
        val stationId = YiboPreference.instance(context).pwd
        val headerKey: String = try {
            //在外部项目上传时，暂时是将(站点代号+用户名+项目来源)的md5值当文件名
            Utils.getMD5(stationId + userName + "project-yibo" + UsualMethod.getPackageName(context))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }

        return RetrofitFactory.api().syncHeaderPhoto(Urls.ACQUIRE_HEADER_URL, headerKey)
    }
}