package crazy_wrapper;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.meiya.cunnar.crazy.crazylibrary.R;

import crazy_wrapper.Crazy.CrazyDialog;
import crazy_wrapper.Crazy.CrazyException;
import crazy_wrapper.Crazy.RequestQueue;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import java.util.*;


/**
 * Created by zhangy on 2016/9/6.
 * 处理正常的网络请求管理器
 */
public class RequestManager {


    public static final String TAG = RequestManager.class.getSimpleName();
    //    CrazyDialog.Proxy dialogProxy;//crazy dialog proxy object, all dialog oprate will be done with it.
    Map<String, CrazyDialog.Proxy> dialogProxyMap = new HashMap<>();
    static RequestManager manager;

    public static RequestManager getInstance() {
        if (manager == null) {
            manager = new RequestManager();
        }
        return manager;
    }

    private void showToast(Context context, int showText) {
        Toast.makeText(context, showText, Toast.LENGTH_LONG).show();
    }

    /**
     * start compound request
     *
     * @param context
     * @param multiRequest
     */
    public void startRequest(Context context, CrazyRequest<?> multiRequest) {
//        if (!Utils.isNetworkAvailable(context)) {
//            showToast(context, R.string.network_invalid);
//            return;
//        }
        if (multiRequest == null) {
            return;
        }
        // when there has not crazy listener attach to request. we try to attach again if context typeof SessionResponse.Listener<?>
        if (multiRequest.getListener() == null && context instanceof SessionResponse.Listener<?>) {
            multiRequest.setListener((SessionResponse.Listener<?>) context);
        }
        if (multiRequest.getListener() == null && context instanceof SessionResponse.RequestChangeListener) {
            multiRequest.setExpandListener((SessionResponse.RequestChangeListener<?>) context);
        }
        /**when this request has front dialog need to show.
         * call before rquest to prepare front ui crazy dialog show
         */
        if (multiRequest.getLoadMethod() != CrazyRequest.LOAD_METHOD.NONE.ordinal()
                && context instanceof Activity && !((Activity) context).isFinishing()) {
            showCrazyDialog(context, multiRequest);
        }
        CrazyRequest<?> crazyRequest = removeHostIfNeed(multiRequest);
        RequestQueue instance = RequestQueue.getInstance();
        instance.start(instance.add(crazyRequest));
    }

    /**
     * 直接传入SessionResponse.Listener的网络请求，可在非UI类中实现网络请求
     *
     * @param context
     * @param multiRequest
     * @param listener
     */
    public void startRequest(Context context, CrazyRequest<?> multiRequest, SessionResponse.Listener<?> listener) {
//        if (!Utils.isNetworkAvailable(context)) {
//            showToast(context, R.string.network_invalid);
//            return;
//        }
        if (multiRequest == null) {
            return;
        }
        // when there has not crazy listener attach to request. we try to attach again if context typeof SessionResponse.Listener<?>
        if (multiRequest.getListener() == null && listener != null) {
            multiRequest.setListener(listener);
        }
        /**when this request has front dialog need to show.
         * call before rquest to prepare front ui crazy dialog show
         */
        if (multiRequest.getLoadMethod() != CrazyRequest.LOAD_METHOD.NONE.ordinal()
                && context instanceof Activity && !((Activity) context).isFinishing()) {
            showCrazyDialog(context, multiRequest);
        }
        CrazyRequest<?> crazyRequest = removeHostIfNeed(multiRequest);
        RequestQueue instance = RequestQueue.getInstance();
        instance.start(instance.add(crazyRequest));
    }

    /**
     * 直接传入SessionResponse.RequestChangeListener的网络请求，可在非UI类中实现网络请求
     *
     * @param context
     * @param multiRequest
     * @param listener
     */
    public void startRequest(Context context, CrazyRequest<?> multiRequest, SessionResponse.RequestChangeListener listener) {
//        if (!Utils.isNetworkAvailable(context)) {
//            showToast(context, R.string.network_invalid);
//            return;
//        }
        if (multiRequest == null) {
            return;
        }

        if (multiRequest.getListener() == null && listener != null) {
            multiRequest.setExpandListener(listener);
        }
        /**when this request has front dialog need to show.
         * call before rquest to prepare front ui crazy dialog show
         */
        if (multiRequest.getLoadMethod() != CrazyRequest.LOAD_METHOD.NONE.ordinal()
                && context instanceof Activity && !((Activity) context).isFinishing()) {
            showCrazyDialog(context, multiRequest);
        }
        CrazyRequest<?> crazyRequest = removeHostIfNeed(multiRequest);
        RequestQueue instance = RequestQueue.getInstance();
        instance.start(instance.add(crazyRequest));
    }


    /**
     * handler multi request launch. we improve multi compound request
     *
     * @param context
     * @param requests sub-request list.
     */
    public void startRequest(Context context, CrazyRequest... requests) {
        if (requests == null || requests.length == 0) {
            return;
        }
        sortDeliveriedRequest(requests);
        List<CrazyRequest<?>> list = new ArrayList<>();
        for (int i = 0; i < requests.length; i++) {
            if (requests[i] == null) {
                continue;
            }
            // when there has not crazy listener attach to request. we try to attach again if context typeof SessionResponse.Listener<?>
            if (requests[i].getListener() == null && context instanceof SessionResponse.Listener<?>) {
                requests[i].setListener((SessionResponse.Listener<?>) context);
            }
            if (requests[i].getListener() == null && context instanceof SessionResponse.RequestChangeListener) {
                requests[i].setExpandListener((SessionResponse.RequestChangeListener<?>) context);
            }
            CrazyRequest<?> crazyRequest = removeHostIfNeed(requests[i]);
            list.add(crazyRequest);
        }
        //由于请求集进行了按优先级由高到级的排序,取第一个优先级最高的请求数据
        CrazyRequest<?> selectedRequest = requests[0];
        int seqnumber = selectedRequest.getSeqnumber();
        String tag = UUID.randomUUID().toString();
        String placeText = selectedRequest.getPlaceholdText();
        int loadMethod = selectedRequest.getLoadMethod();

        CrazyRequest multiRequests = new MultiWrapRequests(list);
        multiRequests.setSeqnumber(seqnumber);
        multiRequests.setTag(tag);
        multiRequests.setLoadMethod(loadMethod);
        multiRequests.setGroupRequest(requests.length >= 1 ? true : false);
        multiRequests.setPlaceholdText(placeText);

        startRequest(context, multiRequests);
    }

    //如果不是本系统的域名请求，则去掉定义在头部的'Host'字段
    private CrazyRequest<?> removeHostIfNeed(CrazyRequest<?> request) {
        Map<String, String> headers = request.getHeaders();
        if (headers != null && !headers.isEmpty() && headers.containsKey("Host") && !Utils.isEmptyString(headers.get("Host"))) {
            if (request.getUrl() != null && !request.getUrl().contains(headers.get("domain"))) {
                Map<String, String> newHeaders = new HashMap<>();
                for (String pKey : headers.keySet()) {
                    if (pKey != null && pKey.equalsIgnoreCase("Host")) {
                        continue;
                    }
                    newHeaders.put(pKey, headers.get(pKey));

                }
                request.setHeaders(newHeaders);
                return request;
            }
        }
        return request;
    }

    /**
     * show crazy dialog before request been performed,there has one thing need your focus.
     * we must pass in the request tag,so that we can cancel this request anytime. besides,
     * the tag is fetch from multirequest ,not single request when this request is a compound
     * request.
     *
     * @param context
     * @param request
     */
    private synchronized void showCrazyDialog(Context context, CrazyRequest request) {
        if (request == null || request.getLoadMethod() == CrazyRequest.LOAD_METHOD.NONE.ordinal()) {
            return;
        }
        try {
//            if (dialogProxy == null) {
//                dialogProxy = new CrazyDialog.DefaultDialogProxy(context, buildDialogEntry(context, request));
//            }
            CrazyDialog.Proxy dialogProxy = new CrazyDialog.DefaultDialogProxy(context, buildDialogEntry(context, request));
            dialogProxy.create();
            CrazyDialog.Proxy proxy = dialogProxyMap.get(request.getUrl());
            //已存在相同的网络请求，先dimiss之前的dialog再进行显示新的加载对话框
            if (proxy != null) {
                dismissCrazyDialog(request.getUrl());
            }
            dialogProxyMap.put(request.getUrl(), dialogProxy);
        } catch (CrazyException crazyException) {
            crazyException.printStackTrace();
        }
    }

    /**
     * prepare dialog pane content which is enterprised in Dialog.Entry
     * we provide tow buttons 'cancel','exec_background' when this request include stream request
     * . when you click
     * cancel we cancel this request default.
     *
     * @param context
     * @param request
     * @return
     */
    private CrazyDialog.Entry buildDialogEntry(Context context, final CrazyRequest request) {
        if (request == null) {
            return null;
        }
        CrazyDialog.Entry entry = new CrazyDialog.Entry();
        if (!Utils.isEmptyString(request.getStreams())) {
            entry.btnNums = 2;
            entry.btnTexts = new String[]{context.getString(R.string.background_exec),
                    context.getString(R.string.cancel)};
            OnBtnClickL leftClickL = new OnBtnClickL() {
                public void onBtnClick() {
                    dismissCrazyDialog(request.getUrl());
                }
            };
            OnBtnClickL rightClickL = new OnBtnClickL() {
                public void onBtnClick() {
                    dismissCrazyDialog(request.getUrl());
                    RequestQueue.getInstance().cancel(request.getTag());
                }
            };
            entry.clickLs = new OnBtnClickL[]{leftClickL, rightClickL};
        }
        entry.content = request.getPlaceholdText();
        entry.outsideCancel = false;
        entry.loadMethod = request.getLoadMethod();
        return entry;
    }

    public synchronized void dismissCrazyDialog(String key) {
        CrazyDialog.Proxy proxy = dialogProxyMap.get(key);
        if (proxy != null) {
            proxy.dismiss();
            dialogProxyMap.remove(key);
        }
    }

    /**
     * do some stuff after request perform compelete
     *
     * @param response
     */
    public synchronized void afterRequest(SessionResponse<?> response) {
        dismissCrazyDialog(response != null ? response.url : "");
        //todo do something with crazy response
    }

    /**
     * sort all the request. from high priority to low if this is union requests
     * 将一个请求处理中的各级请求按优先级从高到低排序
     *
     * @return 排序后的请求集
     */
    public static CrazyRequest[] sortDeliveriedRequest(CrazyRequest[] requests) {
        if (requests.length > 1) {
            Arrays.sort(requests, new Comparator<CrazyRequest>() {
                @Override
                public int compare(CrazyRequest t2, CrazyRequest t1) {
                    if (t2 == null || t1 == null) {
                        return 0;
                    }
                    return t1.getPriority() - t2.getPriority();
                }
            });
        }
        return requests;
    }

}
