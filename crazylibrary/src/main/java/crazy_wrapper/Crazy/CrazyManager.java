package crazy_wrapper.Crazy;

import crazy_wrapper.Crazy.network.LoadDataManager;

/**
 * Created by zhangy on 2017/5/3.
 */

public class CrazyManager {

    public static void cancel(){
        RequestQueue.getInstance().cancelAll();
        LoadDataManager.getInstance().release();
    }

    public static void init() {
        //// TODO: 2017/5/3 init something before crazy framework work.
    }

    

}
