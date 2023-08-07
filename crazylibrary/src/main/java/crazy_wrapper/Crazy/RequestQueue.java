package crazy_wrapper.Crazy;

import android.util.LruCache;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.network.LoadDataManager;
import crazy_wrapper.Crazy.request.CrazyRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangy on 2016/12/7.
 * this is an simple request queue manager to hold all the app requests pointers
 * you can cancel/cancelAll request,start compound request,stop request when exit app
 *
 */
public class RequestQueue {

    static RequestQueue mRequestQueue;
    Map<String,CrazyRequest<?>> mCurrentRequests = new HashMap<String, CrazyRequest<?>>();
    public int MAX_CACHE = 2*1024*1024;
    LruCache<String,CrazyRequest.Entry> cache;
    /**
     * Adds a Request to the dispatch queue.
     * @param request The request to service
     * @return The passed-in request
     */
    public <T extends CrazyResult> CrazyRequest<T> add(CrazyRequest<T> request) {
        // Tag the request as belonging to this queue and add it to the set of current requests.
        request.setRequestQueue(this);
        synchronized (mCurrentRequests) {
            mCurrentRequests.put(request.getTag(), request);
        }
        return request;
    }

    /**
     * start dispatch add-in request
     * if this request is combine requests. we combine all the sub-requests
     * of this request.
     * @param request
     */

    public void start(CrazyRequest<?> request){
        if (request == null){
            return;
        }
        List<CrazyRequest<?>> list = new ArrayList<CrazyRequest<?>>();
        if (!request.isGroupRequest()){
            list.add(request);
        }else{
            CrazyRequest[] requests = request.combineRequests();
            if (requests != null) {
                for (CrazyRequest crazy : requests) {
                    list.add(crazy);
                }
            }
        }
        CrazyRequest[] requestArray = new CrazyRequest[list.size()];
        for (int i = 0; i < list.size(); i++) {
            requestArray[i] = list.get(i);
        }
        LoadDataManager.getInstance().addTask(new BasicCrazyDispatcher(requestArray));
    }


    /**
     * stop queue,mean cancel all the requests
     */
    public void stop(){
        cancelQueue();
    }
//
//    public CrazyRequest<?> getRequest(String tag){
//        synchronized (mCurrentRequests) {
//            if (Utils.isEmptyString(tag)) {
//                return null;
//            }
//            return mCurrentRequests.get(tag);
//        }
//    }
    /**
     * A simple predicate or filter interface for Requests, for use by
     * {@link RequestQueue# cancelQueue(RequestFilter,false)}.
     */
    public interface RequestFilter {
        public boolean apply(CrazyRequest<?> request);
    }

    private void cancelQueue() {
        cancelQueue(null,true);
    }

    /**
     * Cancels requests in this queue for which the given filter applies.
     * @param filter The filtering function to use
     * @param all whether cancel all or not
     */
    private void cancelQueue(RequestFilter filter,boolean all) {
        synchronized (mCurrentRequests) {
            for (Map.Entry<String, CrazyRequest<?>> entry : mCurrentRequests.entrySet()) {
                CrazyRequest<?> request = entry.getValue();
                Utils.LOG("a","cancel request tag2 = "+request.getTag());
                if ((filter != null && filter.apply(request)) || all) {
                    Utils.LOG("a","cancel request tag status apply,tag = "+request.getTag());
                    if (!request.isGroupRequest()){
                        request.cancel();
                        continue;
                    }
                    CrazyRequest<?>[] requests = request.getCombineRequest();
                    if (request != null && requests.length > 0){
                        for (CrazyRequest r : requests){
                            r.cancel();
                        }
                    }
                }
            }
        }
    }

    /**
     * Cancels all requests in this queue with the given tag. Tag must be non-null
     * and equality is by identity.
     */
    public void cancel(final String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Cannot cancelAll with a null tag");
        }
        cancelQueue(new RequestFilter() {
            @Override public boolean apply(CrazyRequest<?> request) {
                return request.getTag().equals(tag);
            }
        }, false);
    }

    /**
     * cancel all the requests
     */
    public void cancelAll() {
        cancelQueue();
    }

    public <T extends CrazyResult> void finish(CrazyRequest<T> request) {
        // Remove from the set of requests currently being processed.
        synchronized (mCurrentRequests) {
            mCurrentRequests.remove(request);
        }
    }

    public void finishAll() {
        // Remove from the set of requests currently being processed.
        synchronized (mCurrentRequests) {
            mCurrentRequests.clear();
        }
    }

    public static RequestQueue getInstance() {
        if (mRequestQueue == null){
            mRequestQueue = new RequestQueue();
        }
        return mRequestQueue;
    }

    private RequestQueue(){
        initCache();
    }

    public LruCache getCache(){
        return cache;
    }

    private void initCache(){
        //MAX_CACHE = (int) (Runtime.getRuntime().maxMemory() / 1024);
        cache = new LruCache<String, CrazyRequest.Entry>(MAX_CACHE){
            @Override protected int sizeOf(String key, CrazyRequest.Entry value) {
                return value.resultString.getBytes().length;
            }
        };
    }

}
