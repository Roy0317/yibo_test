package crazy_wrapper.Crazy.request;

import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.RequestBuilder;
import crazy_wrapper.Crazy.response.SessionResponse;

/**
 * Created by zhangy on 2017/6/12.
 * an default subclass of crazy request handle all requests
 */

public class AbstractCrazyRequest<T extends CrazyResult> extends CrazyRequest<T>{

    private Class<T> clazz;

    /**
     * 若父类没有设置请求策略，则使用默认定义的请求策略
     * @return
     */
    @Override public CrazyStategory getCrazyStategory() {
        CrazyStategory<?> cr = super.getCrazyStategory();
        if (cr == null){
            cr = new DefaultCrazyCacheStrategy();
        }
        return cr;
    }

    AbstractCrazyRequest(){
        //clazz = (Class<T>) ((ParameterizedType) getClass()
        //    .getGenericSuperclass()).getActualTypeArguments()[0];
    }


    @Override public SessionResponse<T> parseCrazyResponse(CrazyResponse<T> crazyResponse) {
        if (crazyResponse == null){
            return null;
        }
        SessionResponse<T> sessionResponse = null;
        T result = crazyResponse.result;

        if (result == null) {
            return sessionResponse;
        }

        if (getCrazyStategory() != null){
            if (getCrazyStategory().parseRule(result)){
                sessionResponse = SessionResponse.success(result, crazyResponse.networkTimsMs);
            }else{
                sessionResponse = SessionResponse.error(result);
            }
            sessionResponse.isLastRequest = crazyResponse.isLastRequest;
            sessionResponse.url = crazyResponse.url;
            sessionResponse.action = crazyResponse.action;
            sessionResponse.pickType = crazyResponse.pickFrom;
        }
        return sessionResponse;
    }

    @Override public void deliveryResponse(SessionResponse<T> response) {
        SessionResponse.Listener listener = getListener();
        if (listener != null){
            listener.onResponse(response);
        }
        SessionResponse.RequestChangeListener expandListener = getExpandListener();
        if (expandListener != null){
            expandListener.onResponse(response);
        }
    }

    /**
     * we implement a default cache strategy which define that: if the json have field
     * named "success" and the value is true. we let the cache go strough. to be honest,
     * this is according to cunnar guard request json result format.
     */
    public static final class DefaultCrazyCacheStrategy<T extends CrazyResult> implements CrazyStategory<T>{

        @Override public boolean cacheRule(T result) {

            if (result != null && result.crazySuccess) {
                return true;
            }
            return false;
        }

        @Override public boolean parseRule(T result) {
            if (result != null && result.crazySuccess) {
                return true;
            }
            return false;
        }

        @Override public String modifyUrl(T result, CrazyRequest crazyRequest) {
            return crazyRequest.getUrl();
        }
    }

    /**
     * 字符串请求构造器
     */
    public static final class Builder extends RequestBuilder {
        public Builder(){super();}
        @Override public CrazyRequest<?> create() {
            final CrazyRequest<?> crazyRequest = new AbstractCrazyRequest();
            p.apply(crazyRequest);
            return crazyRequest;
        }
    }

}
