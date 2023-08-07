package crazy_wrapper.Crazy.response;

import crazy_wrapper.Crazy.CrazyException;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.request.CrazyRequest;

/**
 * Created by zhangy on 2016/8/3.
 * guard colud build-on response to delivery the result from Crazy dispater.
 * include normal success result,logic error result,and ecception error result.
 */
public class SessionResponse<T extends CrazyResult> {

    /** this is the success result from dispatcher**/
    public T result;
    public long costTime;
    /** this url is equal to the request url. we need to know each Response belong to which request**/
    public String url;
    public int action;
    public int pickType;
    public boolean isLastRequest;
    /** this is error when run dispatch, almost it is network exception,
     * but it can include errorResponseResult sometime which is handle by implement class
     * "NetworkCrazyError"**/
    //public CrazyResponse error;
    /** indicate which stage response belong**/
    public int tag = NORMAL_NETWORK_CRAZY_STAGE;

    public static final int FILE_CRAZY_STAGE = 2;
    public static final int NORMAL_NETWORK_CRAZY_STAGE = 1;
    public static final int TASK_NETWORK_CRAZY_STAGE = 3;
    public static final int DATA_VERIFICATE_CRAZY_STAGE = 4;
    public static final int NORMAL_NETWORK_CRAZY_STAGE_HAPPEN_INTERUPT = 5;

    /** Callback interface for delivering parsed responses. */
    public interface Listener<T extends CrazyResult>{
        public void onResponse(SessionResponse<T> response);
        //public void onErrorResponse(CrazyError crazyError,int stage,int pickType,String url,int action);
    }

    public interface StreamListener<T extends CrazyResult> extends Listener<T>{
        //@Override void onResponse(T results, int stage, int pickType, String url, int action);
        //@Override void onErrorResponse(CrazyError crazyError, int stage, int pickType, String url, int action);
        @Override void onResponse(SessionResponse<T> response);
        /** extends Listener<T> add stream status progress callback method</>**/
        void onStreamResponse(CrazyRequest<T> request, String path, long current, long total);
    }

    /**
     * we define an interface which has can handle super interface crazy result, in attachment, it
     * also can modify a specific request and result a new request url</>
     * @param <T>
     */
    public interface RequestChangeListener<T extends CrazyResult> extends Listener<T>{
        //@Override void onResponse(T results, int stage, int pickType, String url, int action);
        //@Override void onErrorResponse(CrazyError crazyError, int stage, int pickType, String url, int action);
        @Override void onResponse(SessionResponse<T> response);
        /**
         * @param request next request bring with request url
         * @param response last request perform response.
         * @return
         */
        CrazyRequest<?> requestChange(CrazyRequest<?> request, SessionResponse<T> response) throws CrazyException;
        /** do something before perform request**/
        CrazyRequest<?> beforeRequest(CrazyRequest<?> request) throws CrazyException;
        /** do something after perform request complete**/
        void afterRequest(CrazyRequest<?> request, SessionResponse<?> response) throws CrazyException;
    }

    /** return a successful response containning the parsed result**/
    public static <T extends CrazyResult> SessionResponse<T> success(T result){
        return new SessionResponse<T>(result);
    }
    public static <T extends CrazyResult> SessionResponse<T> success(T result, long costTime){
        return new SessionResponse<T>(result,costTime);
    }
    /** return a failed response containning parsed carzyerror result**/
    public static <T extends CrazyResult> SessionResponse<T> error(T errorResult){
        return new SessionResponse<T>(errorResult);
    }

    SessionResponse(T t){
        this(t,0);
    }

    SessionResponse(T t, long costTime){
        result = t;
        this.costTime = costTime;
    }

    public boolean isSuccess(){
        return result != null&&result.crazySuccess;
    }

}
