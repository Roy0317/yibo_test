package crazy_wrapper.Crazy;


/**
 * Created by zhangy on 2016/8/2.
 * crazy response after request.
 * the result include success and error response.
 */
public class CrazyResponse<T> {


    public static final int NORMAL_REQUEST = 0;
    public static final int CACHE_REQUEST = 1;
    /** json result from network**/
    public T result;
    public String url;
    public int action;// define unique request number.
    public int pickFrom = NORMAL_REQUEST;//where does to response pick from.
    public boolean isLastRequest;//whether or not the last request response when this is compound request
    /**
     * request roundtrip network time duration in millseconds
     */
    public final long networkTimsMs;

    public CrazyResponse(String url,T result,long networkTimsMs){
        this.result = result;
        this.networkTimsMs = networkTimsMs;
        this.url = url;
    }

    public CrazyResponse(String url,int action,T result,long networkTimsMs){
        this.result = result;
        this.networkTimsMs = networkTimsMs;
        this.url = url;
        this.action = action;
    }

    public CrazyResponse(String url,int action,T result,long networkTimsMs,int pickFrom){
        this.result = result;
        this.networkTimsMs = networkTimsMs;
        this.url = url;
        this.action = action;
        this.pickFrom = pickFrom;
    }

    public CrazyResponse(String url,int action,T result,long networkTimsMs,int pickFrom,boolean isLastRequest){
        this.result = result;
        this.networkTimsMs = networkTimsMs;
        this.url = url;
        this.action = action;
        this.pickFrom = pickFrom;
        this.isLastRequest = isLastRequest;
    }

}
