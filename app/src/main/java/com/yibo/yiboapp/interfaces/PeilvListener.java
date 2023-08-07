package com.yibo.yiboapp.interfaces;

import com.yibo.yiboapp.data.PeilvPlayData;

import java.util.List;

/**
 * @author: soxin
 * @version: 1
 * @project: trunk
 * @package: com.yibo.yiboapp.interfaces
 * @description:
 * @date: 2019-10-31
 * @time: 15:08
 */
public interface PeilvListener {
    void onPeilvAcquire(String playCode, boolean showDialog);

    /**
     * @param selectDatas 选择的项
     * @param money       多选状态时输入的金额
     */
    void onBetPost(List<PeilvPlayData> selectDatas, boolean isMulSelect, String money, int count, double totalMoney, String playRule, String winMoney);
}
