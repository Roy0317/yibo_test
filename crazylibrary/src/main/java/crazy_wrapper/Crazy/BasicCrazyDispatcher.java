package crazy_wrapper.Crazy;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;

import crazy_wrapper.Crazy.Utils.RequestUtils;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static crazy_wrapper.Crazy.CrazyResponse.CACHE_REQUEST;
import static crazy_wrapper.Crazy.CrazyResponse.NORMAL_REQUEST;

/**
 * Created by zhangy on 2016/11/18.
 * thread dispatch crazy request ,support perform multi requests
 */
public class BasicCrazyDispatcher implements Runnable {

    public static final String TAG = BasicCrazyDispatcher.class.getSimpleName();
    CrazyRequest[] requests;
    CrazyDelivery crazyDelivery;//crazy delivery to send out response to ui.

    public BasicCrazyDispatcher(CrazyRequest... requests) {
        this.requests = requests;
        initDelivery();
    }

    @Override
    public void run() {
        long startNetworkTime = SystemClock.elapsedRealtime();
        int requestIndex = 0;
        CrazyRequest crazyRequest = null;
//        String currentName = Thread.currentThread().getName();
        try {
            if (this.requests == null) {
                throw new CrazyException("CrazyRequest is null,Maybe you havent put in request");
            }
            RequestManager.sortDeliveriedRequest(this.requests);
            if (requests == null) {
                throw new CrazyException("CrazyRequest is null,Maybe you havent put in request");
            }
            if (requests.length == 0) {
                throw new CrazyException("CrazyRequest size is empty,Maybe you havent put in request");
            }
            //this map is used to delivery all the responses out when each request has "SyncDeliveryWithBrother"
            //property
            Map<CrazyRequest<?>, SessionResponse<?>> map = new LinkedHashMap<CrazyRequest<?>, SessionResponse<?>>();
            /** crazy analysis all the requests to know whether all the requests has "cascade" property
             *  if we check out that all the requests is cascade. we will perform requests one by one according
             *  to the sort relative to request priority first.
             */
            while (requestIndex < requests.length) {

                crazyRequest = requests[requestIndex];
                /** we pause this request loop if this request need user interaction operation.
                 *  after user operation is performed,we continue this requests loop**/
                SessionResponse<?> response = process(crazyRequest);
                /**
                 *  when one request result is empty or error, and this request is not cascade with next request
                 * we continue next request.
                 *  Created by zhangy on 2017/2/13 17:00
                 **/
                if ((response == null || !response.isSuccess()) && !crazyRequest.isCascade()) {
                    requestIndex++;
                    continue;
                }
                /**
                 *  when this request is success,and this request result need delivery sync with
                 * brother requests.
                 *  Created by zhangy on 2017/2/13 17:04
                 **/
                if (crazyRequest.isSyncDeliveryWithBrother()) {
                    map.put(crazyRequest, response);
                }
                requestIndex++;
                /**
                 *  when this request is cascade with next request,and has expand listener
                 * we change request and replace old request.
                 *  Created by zhangy on 2017/2/13 17:06
                 **/
                SessionResponse.RequestChangeListener expandListener = crazyRequest.getExpandListener();
                if (crazyRequest.isCascade() && null != expandListener) {
                    if (crazyRequest.isCanceled()) {
                        throw new CrazyException("request cancel! location: perform request in "
                                + Thread.currentThread().getName() + "befor change request url ");
                    }
                    //if current request is cascade, we change next request url property
                    if (requestIndex >= requests.length) {
                        continue;
                    }
                    crazyRequest = requests[requestIndex];
                    /** update next request url after last request been performed success**/
                    crazyRequest = expandListener.requestChange(crazyRequest, response);
                    Utils.writeLogToFile(TAG, "the url after request change = " + crazyRequest.getUrl());
                    requests[requestIndex] = crazyRequest;
                }
            }
            Utils.writeLogToFile(TAG, "after all request been handle,the request index = " + requestIndex);
            /**
             *  deliver all the responses to ui when there has many
             * reqeusts waitting for delivery with brother requests.
             *  Created by zhangy on 2017/2/13 17:07
             **/
            doDeliverResponses(map);
        } catch (CrazyException error) {
            error.printStackTrace();
            Utils.writeExceptionToFile(TAG, error);
            /** deliver out error request to ui **/
            String errMsg = error instanceof CrazyTimeOutException ? ((CrazyTimeOutException) error).getErrorMsg() : error.getOriginExceptionMsg();
            String errorMsg = "网路波动，建议更换更稳定的网路环境";
//            if (crazyRequest != null) {
//                errorMsg = fillErrorMsg(crazyRequest, errMsg);
//            }
            if(checkIsExcludeApi(crazyRequest.getUrl())) return;
            if (!Utils.isEmptyString(errorMsg)) {
                deliverError(crazyRequest,error.getStatusCode(), errorMsg, error.getOriginExceptionMsg(), startNetworkTime);
            }
        } catch (Exception error) {
            error.printStackTrace();
            Utils.writeExceptionToFile(TAG, error);
            /** deliver out error request to ui **/
            String errorMsg = "网路波动，建议更换更稳定的网路环境";
//            if (crazyRequest != null) {
//                errorMsg = fillErrorMsg(crazyRequest, error.getMessage());
//            }
            if(checkIsExcludeApi(crazyRequest.getUrl())) return;
            if (!Utils.isEmptyString(errorMsg)) {
                deliverError(crazyRequest, "-1", errorMsg, error.getMessage(), startNetworkTime);
            }
        }
    }

    //周期性检测若失败不显示错误讯息,避免影响使用体验
    private boolean checkIsExcludeApi(String url) {
        List<String> excludeApiList = new ArrayList<>();
        excludeApiList.add("/ctrl/ctrlCmds");
        excludeApiList.add("/ctrlResult/postCmdResult");
        excludeApiList.add("/native/open_results.do");
        excludeApiList.add("/native/active_infos_v2.do");
        for (int i = 0; i < excludeApiList.size(); i++) {
            if (url.contains(excludeApiList.get(i))) return true;
        }
        return false;
    }

    private String fillErrorMsg(CrazyRequest crazyRequest,String exceptionMsg) {
        String ipAddr = Utils.getIp();
        String userAgent = "";
        //拿到请求头里的user-agent
        Map headers = crazyRequest.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            if (headers.containsKey("User-Agent")) {
                userAgent = (String) headers.get("User-Agent");
            }
        }
        //拿到请求链接并解析出域名，再加密
        String encryptDomain = "";
        String ipFromDns = "";
        String requestUrl = crazyRequest.getUrl();
        if (!Utils.isEmptyString(requestUrl)) {
//            Utils.LOG(TAG, "request url === " + requestUrl);
            String domain = Utils.getDomainFromUrl(requestUrl);
//            Utils.LOG(TAG, "request domain === " + domain);
            if (!Utils.isEmptyString(domain)) {
                encryptDomain = Utils.encrypt(domain);
                //DNS解析后的ip
                ipFromDns = Utils.getIPFromDns(domain);
            }
        }
//        String host = getRequestHost(crazyRequest);
        String host = "";
        String routeType = "";
        Map<String, Object> map = RequestUtils.getRequestHostFlag(crazyRequest);
        if (map.containsKey(RequestUtils.NATIVE_HOST)) {
            host = (String) map.get(RequestUtils.NATIVE_HOST);
        }
        if (map.containsKey(RequestUtils.ROUTE_TYPE)) {
            routeType = (String) map.get(RequestUtils.ROUTE_TYPE);
        }
        return "ip:" + ipAddr +
                ",版本号=" + userAgent +
                ",标识1 = " + encryptDomain +
                ",标识2 = " + (!TextUtils.isEmpty(ipFromDns) ? ipFromDns : "Can't get IP from dns") +
                ",线路方式 = 方式" + routeType +
                ",host = " + host +
                ",报错信息 = " + (!TextUtils.isEmpty(exceptionMsg) ? exceptionMsg : "");
    }


    /**
     * 处理单个请求执行
     * 1. whether this request is cascade or not.
     * 2. whether request again after get result from cache.
     * Created by zhangy on 2017/2/13 16:21
     **/
    private SessionResponse<?> process(CrazyRequest<?> request) throws CrazyException, IOException {
        if (request == null || crazyDelivery == null) {
            return null;
        }
        if (request.isCanceled()) {
            Utils.LOG(TAG, "request cancel! location: loop request perform in BasicCrazyDispatcher");
            return null;
        }
        try {
            CrazyPlugins crazyPlugins = new BasicCrazy();
            SessionResponse<?> response = wrapperPerform(crazyPlugins, request);
            // when request is sync delivery with brother,we dont delivery the response out,
            // else: we delivery out soon.
            if (!request.isSyncDeliveryWithBrother()) {
                crazyDelivery.postResponse(request, response);
            }
            if (response != null && response.isSuccess()) {
                /**
                 * after fetch result from cache.
                 *  when this request need to sync last result from network,
                 * we start request again to fetch result from network.
                 *  Created by zhangy on 2017/2/13 16:36
                 **/
                if (request.isRefreshAfterCacheHit() && response.pickType == CACHE_REQUEST) {
                    Utils.writeLogToFile(TAG, "sync reqeust again after hit from cache ");
                    request.setHitFromCache(true);
                    response = wrapperPerform(crazyPlugins, request);
                    //after finish refresh from web, send the response out to ui.
                    crazyDelivery.postResponse(request, response);
                }
            }
            return response;
        } catch (CrazyTimeOutException crazyException) {
            throw crazyException;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * crazy dispatcher work end with some error happen anywhere
     * we deliver out the error response also
     * created zhangy on 下午8:15 17/1/21
     **/
    private <T extends CrazyResult> void deliverError(CrazyRequest<T> request, String statusCode, String errorMsg, String originExceptionMsg, long startNetworkTime) {
        if (request != null && crazyDelivery != null) {
            request.setMarker(errorMsg);
            CrazyResult result = new CrazyResult();
            result.crazySuccess = false;
            result.error = errorMsg;
            result.statusCode = statusCode;
            result.originExceptionMsg = originExceptionMsg;
            CrazyResponse<T> crazyResponse = new CrazyResponse(request.getUrl(), request.getSeqnumber(),
                    result, SystemClock.elapsedRealtime() - startNetworkTime, NORMAL_REQUEST);
            SessionResponse response = request.parseCrazyResponse(crazyResponse);
            crazyDelivery.postResponse(request, response);
        }
    }

    /**
     * we wrapper request perform
     * we allow user to do something before request
     * we aloow user to do something after request
     *
     * @param crazy
     * @param crazyRequest
     */
    private SessionResponse<?> wrapperPerform(CrazyPlugins crazy, CrazyRequest crazyRequest) throws CrazyException, IOException {
        if (crazyRequest == null) {
            return null;
        }
        SessionResponse.RequestChangeListener listener = crazyRequest.getExpandListener();
        //do something before request perform
        if (listener != null) {
            crazyRequest = listener.beforeRequest(crazyRequest);
        }
        if (crazyRequest == null) {
            return null;
        }
        if (crazyRequest.isCanceled()) {
            Utils.LOG(TAG, "request cancel! location: perform request in wrapperPerform()");
            throw new CrazyException("request cancel! location: perform request in wrapperPerform()");
        }
        try {
            SessionResponse<?> response = crazy.performRequest(crazyRequest);
            //do something after request perform
            if (listener != null) {
                listener.afterRequest(crazyRequest, response);
            }
            return response;
        } catch (CrazyTimeOutException error) {
            throw error;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * create an excutor which perform deliver crazy response to UI
     * in sub thread.
     */
    private void initDelivery() {
        if (crazyDelivery == null) {
            crazyDelivery = new DefaultResponseDelivery(new Handler(Looper.getMainLooper()));
        }
    }

    /**
     * delivery out all the responses to ui,
     * this only will be called when requests are waitting for borther request to be deliver.
     *
     * @param map
     */
    private void doDeliverResponses(Map<CrazyRequest<?>, SessionResponse<?>> map) {

        if (crazyDelivery == null) {
            return;
        }
        if (map == null || map.isEmpty()) {
            return;
        }
        Set<Map.Entry<CrazyRequest<?>, SessionResponse<?>>> entries = map.entrySet();
        for (Map.Entry<CrazyRequest<?>, SessionResponse<?>> entry : entries) {
            if (entry == null) {
                continue;
            }
            crazyDelivery.postResponse(entry.getKey(), entry.getValue());
        }
        map.clear();
    }


}
