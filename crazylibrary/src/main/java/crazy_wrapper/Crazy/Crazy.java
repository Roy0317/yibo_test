package crazy_wrapper.Crazy;

import crazy_wrapper.Crazy.request.CrazyRequest;

/**
 * Created by zhangy on 2016/8/2.
 * base perform crazy request .
 */
public interface Crazy {

    void performRequest(CrazyDelivery delivery, CrazyRequest... crazyRequests)
        throws CrazyException;

}
