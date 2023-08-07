package com.yibo.yiboapp.utils;

import android.content.Context;

import com.yibo.yiboapp.activity.RedPacketNewActivity;
import com.yibo.yiboapp.activity.RedPacketRainActivity;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.SysConfig;

/**
 * Activity 跳转页面
 *
 */
public class StartActivityUtils {

    public static void goRedPacket(Context context) {
        SysConfig sys = UsualMethod.getConfigFromJson(context);
        if (sys.getRob_redpacket_version().equals("v1")) {
            RedPacketRainActivity.createIntent(context);
        } else if (sys.getRob_redpacket_version().equals("v2")) {
            RedPacketNewActivity.createIntent(context);
        }
    }
}
