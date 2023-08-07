package crazy_wrapper.Crazy;


import crazy_wrapper.Crazy.request.CrazyRequest;

/**
 * Created by zhangy on 2016/11/13.
 * this factory want to generate crazy handle cell which will perform
 * crazy request
 *
 */
public interface CrazyFactory {

    Crazy create(CrazyRequest crazyRequest);

}
