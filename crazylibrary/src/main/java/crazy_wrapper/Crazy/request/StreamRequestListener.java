package crazy_wrapper.Crazy.request;

import android.content.Context;
import crazy_wrapper.Crazy.CrazyException;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.network.FileHandler;
import crazy_wrapper.Crazy.response.SessionResponse;

/**
 * Created by zhangy on 2016/11/18.
 *
 * this crazy listener class will do below stuff:
 * it used to perform compound request
 * it can let us to do something before/after a sub request
 * it can let us to handle stream progress when a stream request is performing, but this compound request need has a request with stream.
 *
 */
public abstract class StreamRequestListener implements SessionResponse.RequestChangeListener<CrazyResult<String>>,FileHandler.FileHandlerListener{

    public static final String TAG = StreamRequestListener.class.getSimpleName();
    Context context;
    SessionResponse.StreamListener uiListener;

    public StreamRequestListener(Context context,SessionResponse.StreamListener streamListener){
        this.context = context;
        if (context instanceof SessionResponse.StreamListener){
            uiListener = (SessionResponse.StreamListener) context;
        }else{
            this.uiListener = streamListener;
        }
    }

    public StreamRequestListener(Context context){
        this.context = context;
        if (context instanceof SessionResponse.StreamListener){
            uiListener = (SessionResponse.StreamListener) context;
        }
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<String>> response) {
        if (uiListener != null) {
            uiListener.onResponse(response);
        }
    }

    @Override public CrazyRequest<?> beforeRequest(CrazyRequest<?> request) throws CrazyException {
        if (request == null){
            return request;
        }
        // TODO: 2017/3/17 before a request be performed, we can do something pre-stuff
        return request;
    }

    @Override public void afterRequest(CrazyRequest<?> request,SessionResponse<?> response) throws CrazyException {
        if (request == null || response == null){
            return;
        }
        // TODO: 2017/3/17 after a request finish compelete, we may need to do some ui operation. 
    }

    @Override public void onHandleStatus(CrazyRequest<?> request,long current,long total,boolean notError) {
        if (request == null || uiListener == null) {
            return;
        }
        if (Utils.isEmptyString(request.getPath())){
            return;
        }
        // TODO: 2017/3/17 this function is designed to do user logic (request has stream progress need to be handled)
        // todo when each request of compound request has stream progress
    }


    @Override public long sizeOf(String path) {
        // TODO: 2017/3/17 we pass in the file size when request is download resource from network
        // todo so that we can listener the file stream progress
        return 0;
    }


    @Override public CrazyRequest<? extends CrazyResult> requestChange(CrazyRequest<?> request,SessionResponse<CrazyResult<String>> response) throws CrazyException {
        if (response == null){
            return request;
        }
        if (!response.isSuccess()){
            return request;
        }
        // TODO: 2017/3/17 we can change something when a crazy request is performed. such as below: we can change request url before next
        // TODO: 2017/3/17  request be performed, always this function is used in compound request!
        return request;
    }

}
