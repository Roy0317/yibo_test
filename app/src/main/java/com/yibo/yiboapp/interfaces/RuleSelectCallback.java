package com.yibo.yiboapp.interfaces;

import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;

public interface RuleSelectCallback {
        void onRuleCallback(PlayItem playItem, SubPlayItem item , long playId);
    }