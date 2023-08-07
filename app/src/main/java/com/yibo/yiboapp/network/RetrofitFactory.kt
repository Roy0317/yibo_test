package com.yibo.yiboapp.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.snail.antifake.jni.EmulatorDetectUtil
import com.yibo.yiboapp.BuildConfig
import com.yibo.yiboapp.application.YiboApplication
import com.yibo.yiboapp.data.DatabaseUtils
import com.yibo.yiboapp.data.RequestEventTrack
import com.yibo.yiboapp.data.Urls
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.utils.Utils
import crazy_wrapper.Crazy.Utils.RequestUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object RetrofitFactory {

    private var mApi: API

    init {
        mApi = newApiInstance(BuildConfig.domain_url)
    }

    fun resetBaseUrl(url: String){
        mApi = newApiInstance(url)
    }

    @Synchronized
    private fun newApiInstance(url: String): API{
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                                else HttpLoggingInterceptor.Level.NONE)

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(SaveResponseInterceptor())
            .addInterceptor(interceptor)
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url + Urls.PORT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        return retrofit.create(API::class.java)
    }

    fun api(): API { return mApi }
}



class SaveResponseInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response
        var code = 0
        var body = ""
        try{
            response = chain.proceed(request)
            body = response.body?.string() ?: ""
            code = response.code
        }catch (e: Exception){
            body = e.toString()
            throw e
        }finally {
            val eventTrack = RequestEventTrack()
            eventTrack.uid = UUID.randomUUID().toString()
            eventTrack.timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z",
                Locale.getDefault()).format(System.currentTimeMillis())
            eventTrack.url = request.url.toString()
            eventTrack.headers = request.headers.toMap()
            eventTrack.statusCode = code.toString()
            eventTrack.response = body
            DatabaseUtils.getInstance(YiboApplication.getInstance()).saveEventTrack(eventTrack)
        }

        return response.newBuilder()
            .body(body.toResponseBody(response.body?.contentType()))
            .build();
    }
}


class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val context = YiboApplication.getInstance()
        val newRequest = chain.request().newBuilder()
            .addHeader("Accept-Charset", "utf-8")
            .addHeader("Accept", "application/xml")
            .addHeader("Accept", "application/json")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Accept-Language", "zh-CN,en,*")
            .addHeader(RequestUtils.ROUTE_TYPE, Urls.APP_ROUTE_TYPE.toString())
            .addHeader(RequestUtils.NATIVE_DOMAIN, Urls.BASE_URL)
            .addHeader(RequestUtils.NATIVE_FLAG, "1")
            .addHeader("Cookie", "SESSION=" + YiboPreference.instance(context).token)
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("User-Agent", "android/" + Utils.getVersionName(context) + "|" +YiboPreference.instance(context).deviceId)
            .addHeader("app-code",  "a" + BuildConfig.apk_code)
            .addHeader("cc-token", YiboPreference.instance(context).verifyToken)
            .addHeader("emulator", if (EmulatorDetectUtil.isEmulator(context)) "1" else "0")
            .apply {
                if (Urls.BASE_HOST_URL.isNotEmpty()) {
                    addHeader(RequestUtils.NATIVE_HOST, Urls.BASE_HOST_URL)
                }

                try {
                    addHeader("wtoken", Utils.getMD5(BuildConfig.apk_code + "," + YiboPreference.instance(context).token))
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                }
            }
            .build()

        return chain.proceed(newRequest)
    }
}

suspend fun <T: Any> httpRequest(api: suspend() -> retrofit2.Response<T>): HttpResult<T>{
    return try{
        val response = api()
        if(response.isSuccessful){
            val body = response.body()!!
            val json = JSONObject(Gson().toJson(body))
            if(json.has("accessToken")){
                YiboPreference.instance(YiboApplication.getInstance()).token = json.getString("accessToken")
            }
            HttpResult.Success(body)
        }else{
            HttpResult.Error(response.raw().toString())
        }
    }catch (e: Exception){
        e.printStackTrace()
        if(e is SocketTimeoutException){
            HttpResult.Error("连线逾时：${e.message}")
        }else{
            HttpResult.Error("网路连线错误：${e.message}")
        }
    }
}

sealed class HttpResult<out T: Any>{
    class Success<out T: Any> (val data: T): HttpResult<T>()
    class Error(val message: String): HttpResult<Nothing>()
}