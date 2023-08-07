package crazy_wrapper.Crazy.request;

import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.ResponseConverter;
import crazy_wrapper.Crazy.RequestQueue;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.network.FileHandler;
import crazy_wrapper.Crazy.response.SessionResponse;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by zhangy on 2016/8/2.
 * this is the basic request for all app request. it can
 * be put into queue, can be executed according to priority.
 * can be canceled,can be cached by self-define network cache
 * strategy
 *
 * always, we just need to create a crazy request with crazy builder which
 * you can set many property into request.
 */
public abstract class CrazyRequest<T extends CrazyResult> {

    public static final String TAG = CrazyRequest.class.getSimpleName();
    int seqnumber;//请求序列号
    public String marker;
    private boolean isCanceled;

    /**zhangy add 20160906 for optimize local app request core logic**/
    private RequestQueue requestQueue;
    private String url;//请求地址
    private String placeholdText;
    boolean hitFromCache;//是否在内存缓存中找到请求结果了
    boolean mShouldCache;//请求是否需要被缓存
    boolean refreshAfterCacheHit;//从缓存中获取到请求数据后，是否需要再次同步网络数据
    long cachePeroid = 30*60*1000;//缓存多长时间
    private int priority = Priority.NORMAL.ordinal();//请求的优先级,或者说主次地位
    CrazyStategory crazyStategory;//请求的缓存解析等策略，用户可实现自己的策略
    CrazyStategory crazyFileStategory;//文件请求的缓存解析等策略，用户可实现自己的策略
    boolean syncDeliveryWithBrother;//请求结果是否与关联的兄弟请求同步返回
    Map<String,String> headers;//请求附加的自定义请求头
    String body;//请求实体string
    private int executeMethod = ExecuteMethod.GET.ordinal();//请求方法
    private SessionResponse.Listener<?> listener;//请求状态监听器
    private SessionResponse.RequestChangeListener<?> expandListener;//带请求状态变化行为的监听器
    private String streams;//请求附件的文件流路径集
    boolean deleteStreamAfterRequestSuccess;//在有文件流需要传输时，请求成功后是否要将附加记录清除
    boolean deleteTempFile;//在有文件流需要传输时，请求成功后是否要将附加文件清除
    int loadMethod = LOAD_METHOD.NONE.ordinal();//请求加载方式加载信息显示
    int protocol;//请求的协议类型(自定义)
    String contentType;//请求数据头content-type
    FileHandler.FileHandlerListener fileHandlerListener;//文件上传或下载请求时的进度状态监听
    long lastPushPos;//文件上传时上次上传的位置
    String path;//请求附件的文件流路径
    boolean isCascade;//是否级联请求
    String mTag;
    boolean isGroupRequest;//是否组合请求
    boolean lastRequest;//是否所有请求中最后一个请求
    String attachCommand;//附加的请求数据或命令
    ResponseConverter.Factory convertFactory;
    Type returnType;


    public enum ExecuteMethod{
        GET,FORM,BODY
    }
    public enum Protocol{
        HTTP,HTTPS,UPLOAD,DOWNLOAD,BITMAP,MULTI,STORAGE
    }
    public enum Priority {
        LOW,
        NORMAL,
        HIGH
    }
    public enum LOAD_METHOD{
        NONE,
        DIALOG,//确认框
        TOAST,//提示toast
        PROGRESS,//进度框
        LOADING//加载框
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void cancel(){
        isCanceled = true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRequestQueue(RequestQueue requestQueue){
        this.requestQueue = requestQueue;
    }

    public boolean isHitFromCache() {
        return hitFromCache;
    }

    public void setHitFromCache(boolean hitFromCache) {
        this.hitFromCache = hitFromCache;
    }

    public boolean isRefreshAfterCacheHit() {
        return refreshAfterCacheHit;
    }

    public void setRefreshAfterCacheHit(boolean refreshAfterCacheHit) {
        this.refreshAfterCacheHit = refreshAfterCacheHit;
    }

    public long getCachePeroid() {
        return cachePeroid;
    }

    public void setCachePeroid(long cachePeroid) {
        this.cachePeroid = cachePeroid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean ismShouldCache() {
        return mShouldCache;
    }

    public void setmShouldCache(boolean mShouldCache) {
        this.mShouldCache = mShouldCache;
    }

    public boolean isSyncDeliveryWithBrother() {
        return syncDeliveryWithBrother;
    }

    public void setSyncDeliveryWithBrother(boolean syncDeliveryWithBrother) {
        this.syncDeliveryWithBrother = syncDeliveryWithBrother;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getExecuteMethod() {
        return executeMethod;
    }

    public void setExecuteMethod(int executeMethod) {
        this.executeMethod = executeMethod;
    }

    public CrazyStategory getCrazyStategory() {
        return crazyStategory;
    }

    public void setCrazyStategory(CrazyStategory crazyStategory) {
        this.crazyStategory = crazyStategory;
    }

    public CrazyStategory getCrazyFileStategory() {
        return crazyFileStategory;
    }

    public void setCrazyFileStategory(CrazyStategory crazyFileStategory) {
        this.crazyFileStategory = crazyFileStategory;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public SessionResponse.Listener<?> getListener() {
        return listener;
    }

    public void setListener(SessionResponse.Listener<?> listener) {
        this.listener = listener;
    }

    public SessionResponse.RequestChangeListener<?> getExpandListener() {
        return expandListener;
    }

    public void setExpandListener(SessionResponse.RequestChangeListener<?> expandListener) {
        this.expandListener = expandListener;
    }

    public String getStreams() {
        return streams;
    }

    public void setStreams(String streams) {
        this.streams = streams;
    }

    public boolean isDeleteRecordAfterRequestSuccess() {
        return deleteStreamAfterRequestSuccess;
    }

    public void setDeleteStreamAfterRequestSuccess(boolean deleteStreamAfterRequestSuccess) {
        this.deleteStreamAfterRequestSuccess = deleteStreamAfterRequestSuccess;
    }

    public boolean isDeleteTempFile() {
        return deleteTempFile;
    }

    public void setDeleteTempFile(boolean deleteTempFile) {
        this.deleteTempFile = deleteTempFile;
    }

    public int getLoadMethod() {
        return loadMethod;
    }

    public void setLoadMethod(int loadMethod) {
        this.loadMethod = loadMethod;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public FileHandler.FileHandlerListener getFileHandlerListener() {
        return fileHandlerListener;
    }

    public void setFileHandlerListener(FileHandler.FileHandlerListener fileHandlerListener) {
        this.fileHandlerListener = fileHandlerListener;
    }

    public long getLastPushPos() {
        return lastPushPos;
    }

    public void setLastPushPos(long lastPushPos) {
        this.lastPushPos = lastPushPos;
    }

    public boolean isCascade() {
        return isCascade;
    }

    public void setIsCascade(boolean isCascade) {
        this.isCascade = isCascade;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Set a tag on this request. Can be used to cancel all requests with this
     * tag
     * @return This Request object to allow for chaining.
     */
    public CrazyRequest<?> setTag(String tag) {
        mTag = tag;
        return this;
    }

    /**
     * Returns this request's tag.
     */
    public String getTag() {
        return mTag;
    }

    public boolean isGroupRequest() {
        return isGroupRequest;
    }

    public void setGroupRequest(boolean isGroupRequest) {
        this.isGroupRequest = isGroupRequest;
    }

    public boolean isLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(boolean lastRequest) {
        this.lastRequest = lastRequest;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAttachCommand() {
        return attachCommand;
    }

    public void setAttachCommand(String attachCommand) {
        this.attachCommand = attachCommand;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public ResponseConverter.Factory getConvertFactory() {
        return convertFactory;
    }

    public void setConvertFactory(ResponseConverter.Factory convertFactory) {
        this.convertFactory = convertFactory;
    }

    public void setMarker(String marker) {
        this.marker = marker;
        Utils.writeLogToFile(TAG,"Crazy Request Marker Info = "+marker);
    }

    public void setSeqnumber(int seqnumber) {
        this.seqnumber = seqnumber;
    }

    public int getSeqnumber() {
        return seqnumber;
    }

    public void setPlaceholdText(String loadingText) {
        this.placeholdText = loadingText;
    }

    public String getPlaceholdText() {
        return placeholdText;
    }

    public abstract SessionResponse<T> parseCrazyResponse(CrazyResponse<T> crazyResponse);

    /**
    * delivery success result
    * Created by zhangy on 2017/2/13 15:37
    **/
    abstract public void deliveryResponse(SessionResponse<T> response);
    /**
     * the request entry which will be cached in memory or disk space
     */
    public static final class Entry{

        public String resultString;//the json data from network
        public long cacheTime;//the time to add entry to cache.
        public long cacheDuration;//cache period,expired will remove this cache entry.

        public boolean isExpired(){
            if (System.currentTimeMillis() - cacheTime > cacheDuration){
                return true;
            }
            return false;
        }

    }


    public interface CrazyStategory<T>{
        /** let customer user to define the role that under what situation the entry could be cached.**/
        boolean cacheRule(T result);
        /** let customer user to define the role how the request result could be parsed success or not.**/
        boolean parseRule(T result);
        /** let customer to change request url if need **/
        String modifyUrl(T result, CrazyRequest crazyRequest);
    }

    public static class RequestParams{

        /**zhangy add 20160906 for optimize local app request core logic**/
        public String url;//请求地址
        public String placeholdText;//发起请求时的UI前台加载文字
        public String domain;//请求主域名
        public boolean mShouldCache;//是否需要被缓存
        public boolean refreshAfterCacheHit;//从缓存中获取到请求数据后，是否需要再次同步网络数据
        public long cachePeroid = 30*60*1000;//缓存多长时间,单位毫秒
        public int priority;
        public CrazyStategory crazyStategory;//请求的缓存解析等策略，用户可实现自己的策略
        public CrazyStategory crazyFileStategory;//文件请求的缓存解析等策略，用户可实现自己的策略
        public boolean syncDeliveryWithBrother;//请求结果是否与关联的兄弟请求同步返回
        public Map<String,String> headers;//发起请求时附带的请求头数据
        public String body;//请求实体string,post 请求时以字符流的形式在请求实体中传递的数据
        public int executeMethod = ExecuteMethod.GET.ordinal();//请求方式
        public int seqnumber;//请求唯一序列码，用于唯一标识请求
        public SessionResponse.Listener<?> listener;//请求结果的监听器
        public SessionResponse.RequestChangeListener<?> expandListener;//带请求状态变化行为的扩展监听器
        public String streams;////请求附件的文件流路径集
        public boolean deleteStreamAfterRequestSuccess;//在有文件流需要传输时，请求成功后是否要将附加记录清除
        public boolean deleteTempFile;//在有文件流需要传输时，请求成功后是否要将附加文件清除
        public int loadMethod = LOAD_METHOD.NONE.ordinal();//请求预加载提示信息方式
        public int protocol = Protocol.HTTP.ordinal();//请求的协议类型(自定义)
        public FileHandler.FileHandlerListener fileHandlerListener;//文件上传或下载请求时的进度状态监听
        public long lastPushPos;//文件上传时上次上传的位置
        public boolean isCascade;//是否级联请求
        public String path;//请求附件的文件流路径
        public String mTag;
        public boolean isGroupRequest;//是否组合请求
        public String contentType;//请求数据头content-type
        public String attachCommand;
        public ResponseConverter.Factory convertFactory;

        public RequestParams() {
            executeMethod = ExecuteMethod.GET.ordinal();//请求方式
            priority = Priority.NORMAL.ordinal();
        }

        public void apply(CrazyRequest crazyRequest) {
            crazyRequest.setUrl(url);
            crazyRequest.setPlaceholdText(placeholdText);
            crazyRequest.setRefreshAfterCacheHit(refreshAfterCacheHit);
            crazyRequest.setCachePeroid(cachePeroid);
            crazyRequest.setPriority(priority);
            crazyRequest.setCrazyStategory(crazyStategory);
            crazyRequest.setCrazyFileStategory(crazyFileStategory);
            crazyRequest.setSyncDeliveryWithBrother(syncDeliveryWithBrother);
            crazyRequest.setHeaders(headers);
            crazyRequest.setBody(body);
            crazyRequest.setExecuteMethod(executeMethod);
            crazyRequest.setmShouldCache(mShouldCache);
            crazyRequest.setSeqnumber(seqnumber);
            crazyRequest.setListener(listener);
            crazyRequest.setStreams(streams);
            crazyRequest.setDeleteStreamAfterRequestSuccess(deleteStreamAfterRequestSuccess);
            crazyRequest.setDeleteTempFile(deleteTempFile);
            crazyRequest.setLoadMethod(loadMethod);
            crazyRequest.setProtocol(protocol);
            crazyRequest.setFileHandlerListener(fileHandlerListener);
            crazyRequest.setLastPushPos(lastPushPos);
            crazyRequest.setIsCascade(isCascade);
            crazyRequest.setExpandListener(expandListener);
            crazyRequest.setPath(path);
            crazyRequest.setTag(mTag);
            crazyRequest.setGroupRequest(isGroupRequest);
            crazyRequest.setContentType(contentType);
            crazyRequest.setAttachCommand(attachCommand);
            crazyRequest.setConvertFactory(convertFactory);

        }
    }


    public CrazyRequest<?>[] combineRequests(){
        return null;
    }

    public CrazyRequest<?>[] getCombineRequest(){
        return null;
    }


}
