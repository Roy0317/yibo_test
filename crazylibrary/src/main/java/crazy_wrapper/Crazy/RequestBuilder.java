package crazy_wrapper.Crazy;

import crazy_wrapper.Crazy.network.FileHandler;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import java.util.Map;

/**
 * this is a builder for create crazy request. it is abstract class
 * bacause we need sub-class to determine which Data Type Crazy request
 * result.. such as String,Map,Bitmap.......
 *
 * @author zhangy
 */
public abstract class RequestBuilder {

    public final CrazyRequest.RequestParams p;

    public RequestBuilder() {
        p = new CrazyRequest.RequestParams();
    }

    public RequestBuilder url(String url) {
        p.url = url;
        return this;
    }

    public RequestBuilder placeholderText(String text) {
        p.placeholdText = text;
        return this;
    }

    public RequestBuilder domain(String text) {
        p.domain = text;
        return this;
    }

    public RequestBuilder shouldCache(boolean cache) {
        p.mShouldCache = cache;
        return this;
    }

    public RequestBuilder refreshAfterCacheHit(boolean refresh) {
        p.refreshAfterCacheHit = refresh;
        return this;
    }

    public RequestBuilder cachePeroid(long period) {
        p.cachePeroid = period;
        return this;
    }

    public RequestBuilder priority(int priority) {
        p.priority = priority;
        return this;
    }

    public RequestBuilder crazyStategory(CrazyRequest.CrazyStategory<?> crazyStategory) {
        p.crazyStategory = crazyStategory;
        return this;
    }

    public RequestBuilder crazyFileStategory(CrazyRequest.CrazyStategory<?> crazyStategory) {
        p.crazyFileStategory = crazyStategory;
        return this;
    }

    public RequestBuilder syncDeliveryWithBrother(boolean syncDeliveryWithBrother) {
        p.syncDeliveryWithBrother = syncDeliveryWithBrother;
        return this;
    }

    public RequestBuilder headers(Map<String, String> headers) {
        p.headers = headers;
        return this;
    }

    public RequestBuilder body(String body) {
        p.body = body;
        return this;
    }

    public RequestBuilder execMethod(int executeMethod) {
        p.executeMethod = executeMethod;
        return this;
    }

    public RequestBuilder seqnumber(int seqnumber) {
        p.seqnumber = seqnumber;
        return this;
    }

    public RequestBuilder listener(SessionResponse.Listener<?> listener) {
        p.listener = listener;
        return this;
    }

    public RequestBuilder expandListener(SessionResponse.RequestChangeListener<?> listener) {
        p.expandListener = listener;
        return this;
    }

    public RequestBuilder streams(String streams) {
        p.streams = streams;
        return this;
    }

    public RequestBuilder deleteStreamAfterRequestSuccess(boolean delete) {
        p.deleteStreamAfterRequestSuccess = delete;
        return this;
    }

    public RequestBuilder deleteTempFile(boolean delete) {
        p.deleteTempFile = delete;
        return this;
    }

    public RequestBuilder loadMethod(int loadMethod) {
        p.loadMethod = loadMethod;
        return this;
    }

    public RequestBuilder protocol(int protocol) {
        p.protocol = protocol;
        return this;
    }

    public RequestBuilder fileHandlerListener(FileHandler.FileHandlerListener handlerListener) {
        p.fileHandlerListener = handlerListener;
        return this;
    }

    public RequestBuilder lastPushPos(long lastPushPos) {
        p.lastPushPos = lastPushPos;
        return this;
    }

    public RequestBuilder cascade(boolean isCascade) {
        p.isCascade = isCascade;
        return this;
    }

    public RequestBuilder path(String path) {
        p.path = path;
        return this;
    }

    public RequestBuilder tag(String tag) {
        p.mTag = tag;
        return this;
    }

    public RequestBuilder group(boolean group) {
        p.isGroupRequest = group;
        return this;
    }

    public RequestBuilder contentType(String contentType) {
        p.contentType = contentType;
        return this;
    }

    public RequestBuilder convertFactory(ResponseConverter.Factory factory) {
        p.convertFactory = factory;
        return this;
    }

    public abstract CrazyRequest create();


}
