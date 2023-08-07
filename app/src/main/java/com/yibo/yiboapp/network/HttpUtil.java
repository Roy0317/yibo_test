package com.yibo.yiboapp.network;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.activity.MintainceActivity;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.RequestEventTrack;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HttpUtil {


    /**
     * @param ctx
     * @param url
     * @param params
     * @param showDialog
     * @param callBack
     */
    public static void get(final Context ctx, String url, ApiParams params, boolean showDialog, HttpCallBack callBack) {
        get(ctx, url, params, showDialog, ctx.getString(R.string.loading), callBack);
    }

    /**
     * @param ctx
     * @param url
     * @param params
     * @param showDialog
     * @param period     缓存时间
     * @param callBack
     */
    public static void get(final Context ctx, String url, ApiParams params, boolean showDialog, long period, HttpCallBack callBack) {

        boolean shouldCache = period > 0;
        startRequest(
                ctx,
                url,
                params,
                ctx.getString(R.string.loading),
                false,
                shouldCache,
                period,
                CrazyRequest.Priority.HIGH.ordinal(),
                CrazyRequest.ExecuteMethod.GET.ordinal(),
                CrazyRequest.Protocol.HTTP.ordinal(),
                showDialog,
                callBack);
    }


    /**
     * @param ctx
     * @param url
     * @param params
     * @param showDialog
     * @param period               缓存时间
     * @param refreshAfterCacheHit 从缓存中获取到请求数据后，是否需要再次同步网络数据
     * @param callBack
     */
    public static void get(final Context ctx, String url, ApiParams params, boolean showDialog, String loadTips, long period, boolean refreshAfterCacheHit, HttpCallBack callBack) {

        boolean shouldCache = period > 0;
        startRequest(
                ctx,
                url,
                params,
                loadTips,
                refreshAfterCacheHit,
                shouldCache,
                period,
                CrazyRequest.Priority.HIGH.ordinal(),
                CrazyRequest.ExecuteMethod.GET.ordinal(),
                CrazyRequest.Protocol.HTTP.ordinal(),
                showDialog,
                callBack);
    }


    public static void get(final Context ctx, String url, ApiParams params, boolean showDialog, String loadTips, long period, HttpCallBack callBack) {

        get(ctx, url, params, showDialog, loadTips, period, false, callBack);
    }


    /**
     * @param ctx
     * @param url
     * @param params
     * @param showDialog
     * @param loadTips
     * @param callBack
     */
    public static void get(final Context ctx, String url, ApiParams params, boolean showDialog, String loadTips, HttpCallBack callBack) {
        startRequest(
                ctx,
                url,
                params,
                loadTips,
                false,
                false,
                0,
                CrazyRequest.Priority.HIGH.ordinal(),
                CrazyRequest.ExecuteMethod.GET.ordinal(),
                CrazyRequest.Protocol.HTTP.ordinal(),
                showDialog,
                callBack);
    }


    /**
     * @param ctx
     * @param url
     * @param params
     * @param showDialog
     * @param callBack
     */
    public static void postForm(final Context ctx, String url, ApiParams params, boolean showDialog, HttpCallBack callBack) {
        postForm(ctx, url, params, showDialog, ctx.getString(R.string.loading), callBack);
    }


    /**
     * @param ctx
     * @param url
     * @param params
     * @param showDialog
     * @param loadTips
     * @param callBack
     */
    public static void postForm(final Context ctx, String url, ApiParams params, boolean showDialog, String loadTips, HttpCallBack callBack) {
        startRequest(
                ctx,
                url,
                params,
                loadTips,
                false,
                false,
                0,
                CrazyRequest.Priority.HIGH.ordinal(),
                CrazyRequest.ExecuteMethod.FORM.ordinal(),
                CrazyRequest.Protocol.HTTP.ordinal(),
                showDialog,
                callBack);
    }


    /**
     * @param ctx
     * @param url                  请求地址
     * @param params
     * @param loadTips             请求提示
     * @param refreshAfterCacheHit 从缓存中获取到请求数据后，是否需要再次同步网络数据
     * @param shouldCache          是否需要缓存
     * @param period               缓存时间
     * @param priority             请求优先级
     * @param execMethod           网络请求方式
     * @param protocol             请求的协议类型(自定义)
     * @param showDialog           是否显示加载对话框
     * @param callBack             请求回调
     */
    public static void startRequest(final Context ctx,
                                    String url,
                                    ApiParams params,
                                    String loadTips,
                                    boolean refreshAfterCacheHit,
                                    boolean shouldCache,
                                    long period,
                                    int priority,
                                    int execMethod,
                                    int protocol,
                                    boolean showDialog,
                                    HttpCallBack callBack) {

        if (!Utils.isNetworkAvailable(ctx) && callBack != null) {
            callBack.receive(new NetworkResult(ctx.getString(R.string.network_invalid)));
            return;
        }

        if (params == null)
            params = new ApiParams();

        String configUrl = Urls.BASE_URL + Urls.PORT + url + params.getParams(!url.endsWith("&") && !url.endsWith("?"));
        boolean convertBean = true;
        if (configUrl.endsWith("?")){
            configUrl = configUrl.substring(0, configUrl.length()-1);
        }

        Map<String, String> header = Urls.getHeader(ctx);
        /*追踪请求接口事件*/
        final String eventUid = UUID.randomUUID().toString();
        final String finalConfigUrl = configUrl;

        Observable.create((ObservableOnSubscribe<RequestEventTrack>) emitter -> {
                    for (Map.Entry<String, String> event : RequestEventTrack.getNeedTrackEventMap().entrySet()) {
                        if (finalConfigUrl.contains(event.getKey())) {
                            RequestEventTrack eventTrack = new RequestEventTrack();
                            eventTrack.setUid(eventUid);
                            eventTrack.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault()).format(System.currentTimeMillis()));
                            eventTrack.setEvent(event.getValue());
                            eventTrack.setUrl(finalConfigUrl);
                            eventTrack.setHeaders(header);
                            DatabaseUtils.getInstance(ctx).saveEventTrack(eventTrack);
                            break;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl)
//                .seqnumber(RECORD_REQUEST)
                .headers(header)
                .refreshAfterCacheHit(refreshAfterCacheHit)  //
                .shouldCache(shouldCache)  //get请求是否需要缓存
                .cachePeroid(period)  //缓存时间
                .placeholderText(loadTips)  //加载提示
                .priority(priority)
                .execMethod(execMethod)
                .protocol(protocol)
                .convertFactory(null)
                .listener(new MyListener(ctx, callBack, eventUid))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(ctx, request);
    }


    public static void startRequest(final Context ctx,
                                    String url,
                                    ApiParams params,
                                    String loadTips,
                                    boolean refreshAfterCacheHit,
                                    boolean shouldCache,
                                    long period,
                                    int priority,
                                    int execMethod,
                                    int protocol,
                                    boolean showDialog,
                                    HttpCallBack callBack, String baseUrl, boolean globalUrl, Map<String ,String> header) {

        if (!Utils.isNetworkAvailable(ctx) && callBack != null) {
            callBack.receive(new NetworkResult(ctx.getString(R.string.network_invalid)));
            return;
        }

        if (params == null)
            params = new ApiParams();

        String configUrl = "";
        if (globalUrl) {
            configUrl = url + params.getParams(!url.endsWith("&") && !url.endsWith("?"));
        } else {
            configUrl = (!Utils.isEmptyString(baseUrl) ? baseUrl : Urls.BASE_URL) + Urls.PORT + url + params.getParams(!url.endsWith("&") && !url.endsWith("?"));
        }
        if (configUrl.endsWith("?")){
            configUrl = configUrl.substring(0,configUrl.length()-1);
        }
//        if (params != null && params.containsKey("convert_bean") && !(Boolean) params.get("convert_bean")) {
//            convertBean = false;
//        }
//        ResponseConverter.Factory factory = GsonConverterFactory.create(new TypeToken<String>() {
//        }.getType());

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl)
//                .seqnumber(RECORD_REQUEST)
                .headers(header)
                .refreshAfterCacheHit(refreshAfterCacheHit)  //
                .shouldCache(shouldCache)  //get请求是否需要缓存
                .cachePeroid(period)  //缓存时间
                .placeholderText(loadTips)  //加载提示
                .priority(priority)
                .execMethod(execMethod)
                .protocol(protocol)
                .convertFactory(null)
                .listener(new MyListener(ctx, callBack))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();

        RequestManager.getInstance().startRequest(ctx, request);
    }



    /**
     * 统一处理请求结果
     */
    private static class MyListener implements SessionResponse.Listener<CrazyResult<String>> {

        private Context ctx;
        private HttpCallBack callBack;
        private String eventUid;

        public MyListener(Context ctx, HttpCallBack callBack) {
            this.callBack = callBack;
            this.ctx = ctx;
        }

        public MyListener(Context ctx, HttpCallBack callBack, String eventUid) {
            this.callBack = callBack;
            this.ctx = ctx;
            this.eventUid = eventUid;
        }

        @Override
        public void onResponse(SessionResponse<CrazyResult<String>> response) {
            try {
                RequestManager.getInstance().afterRequest(response);

                 if (ctx instanceof BaseActivity) {
                    ((BaseActivity) ctx).stopProgress();
                }
                if (ctx instanceof Activity) {
                    if (((Activity) ctx).isFinishing() || response == null) {
                        return;
                    }
                }

                CrazyResult<String> result = response.result;
                if (result == null || !result.crazySuccess) {
                    callBack.receive(new NetworkResult(ctx.getString(R.string.network_error)));
                    return;
                }

                //判断网站是否在维护中
                String cause = UsualMethod.isMaintenancing(result.result);
                if (!Utils.isEmptyString(cause)) {
                    MintainceActivity.createIntent(ctx, cause);
                    return;
                }

                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();
                NetworkResult networkResult = gson.fromJson(result.result, NetworkResult.class);

                networkResult.setUrl(response.url);
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (!TextUtils.isEmpty(result.result) && networkResult.getMsg().contains("登录")
                        && !networkResult.isSuccess() && networkResult.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(ctx);
                }

                JSONObject rootObject = new JSONObject(result.result);
                String content;
                if (rootObject.has("content"))
                    content = rootObject.getString("content");
                else
                    content = result.result;

                networkResult.setContent(content);
                callBack.receive(networkResult);
            } catch (Exception e) {
                e.printStackTrace();
                NetworkResult errResult = new NetworkResult(ctx.getString(R.string.result_error));
                if (response != null && response.result != null)
                    errResult.setContent(response.result.result);
                callBack.receive(errResult);
            } finally {
                Observable.create((ObservableOnSubscribe<RequestEventTrack>) emitter -> {
                            boolean isRecorded = false;
                            for (Map.Entry<String, String> event : RequestEventTrack.getNeedTrackEventMap().entrySet()) {
                                if (response.url.contains(event.getKey())) {
                                    DatabaseUtils.getInstance(ctx).updateEventTrack(eventUid, response.result);
                                    isRecorded = true;
                                }
                            }
                            /*其他事件失败时也顺便记录*/
                            if (!isRecorded && (!TextUtils.isEmpty(response.result.originExceptionMsg) || !response.result.statusCode.equals("200") || !response.result.result.contains("\"success\":true"))) {
                                RequestEventTrack eventTrack = new RequestEventTrack();
                                eventTrack.setEvent("其他事件");
                                eventTrack.setUid(eventUid);
                                eventTrack.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault()).format(System.currentTimeMillis()));
                                eventTrack.setUrl(response.url);
                                eventTrack.setHeaders(Urls.getHeader(ctx));
                                eventTrack.setStatusCode(response.result.statusCode);
                                eventTrack.setResponse(TextUtils.isEmpty(response.result.originExceptionMsg) ? response.result.result : response.result.originExceptionMsg);
                                DatabaseUtils.getInstance(ctx).saveEventTrack(eventTrack);
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        }
    }
}
