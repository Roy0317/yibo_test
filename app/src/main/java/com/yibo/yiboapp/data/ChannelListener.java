package com.yibo.yiboapp.data;

/**
 * Created by johnson on 2017/9/26.
 */

public interface ChannelListener {
    void onPlayRuleSelected(String cpVersion, String cpCode,String playCode,
                            String subPlayCode,String playName,String subPlayName,
                            float maxBounds,float maxRate,String qihao,String cpName,long duration,int maxBetNum);
    void onPlayClean(boolean clearAfterSuccess);
    void onPlayTouzhu();
}
