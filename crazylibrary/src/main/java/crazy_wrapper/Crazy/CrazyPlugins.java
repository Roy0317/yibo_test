package crazy_wrapper.Crazy;


import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import java.io.IOException;

/**
 * Created by zhangy on 2016/11/18.
 */
public interface CrazyPlugins {
    SessionResponse<?> performRequest(CrazyRequest crazyRequest) throws CrazyException,IOException;
}
