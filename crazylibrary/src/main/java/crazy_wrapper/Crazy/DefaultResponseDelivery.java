package crazy_wrapper.Crazy;

import android.os.Handler;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import java.util.concurrent.Executor;

/**
 * Created by zhangy on 2016/8/4.
 * a default delivery implement class which will post response
 * to UI Thread
 */
public class DefaultResponseDelivery implements CrazyDelivery{

    /** Used for posting responses, typically to the main thread. */
    Executor mResponsePoster;
    public static final String TAG = DefaultResponseDelivery.class.getSimpleName();

    public DefaultResponseDelivery(final Handler handler){
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public DefaultResponseDelivery(Executor executor){
        mResponsePoster = executor;
    }

    @Override
    public void postResponse(CrazyRequest request, SessionResponse<?> response) {
        postResponse(request, response, null);
    }

    @Override
    public void postResponse(CrazyRequest request, SessionResponse<?> response, Runnable runnable) {
        mResponsePoster.execute(new ResponseRunnable(request,response,runnable));
    }

    /** this runnable will post the response to ui thread **/
    private class ResponseRunnable implements Runnable{

        CrazyRequest request;
        SessionResponse<?> response;
        Runnable mRunnable;

        ResponseRunnable(CrazyRequest request, SessionResponse<?> response, Runnable runnable){
            this.request = request;
            this.response = response;
            mRunnable = runnable;
        }

        @Override
        public void run() {

            if (request == null){
                return;
            }

            // If this request has canceled, finish it and don't deliver.
            if (request.isCanceled()){
                Utils.LOG(TAG,"request cancel! location: delivery response in "+DefaultResponseDelivery.class.getSimpleName());
                request.setMarker("request is canceled, stop delivered response");
                //return;
            }
            // Deliver a normal response or error, depending.
            request.setMarker("delivered done");
            request.deliveryResponse(response);
            if (response == null){
                request.setMarker("delivered un success beacuse response is null");
            }
            /**we will do after work if we bring in runnable after request done.**/
            if (mRunnable != null && !request.isCanceled()){
                Utils.writeLogToFile("Delivery"," DO work after delivery response");
                mRunnable.run();
            }
        }
    }

}
