package crazy_wrapper.Crazy;


import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

/**
 * Created by zhangy on 2016/8/4.
 * delivery for delivery request and response out .
 * typically is main thread. means UI thread.
 */
public interface CrazyDelivery {

    /** post the response and request ,this response include error result**/
    void postResponse(CrazyRequest request, SessionResponse<?> response);
    /** post the response and request ,then exectute runnable after post **/
    void postResponse(CrazyRequest request, SessionResponse<?> response, Runnable runnable);


}
