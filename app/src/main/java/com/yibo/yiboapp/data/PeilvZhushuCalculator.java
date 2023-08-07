package com.yibo.yiboapp.data;

/**
 * Created by johnson on 2017/9/28.
 * 赔率版注数计算
 */

public class PeilvZhushuCalculator {

    // 计算最大注数
    public static Integer buyZhuShuValidate(String haoMa, int minSelected, String playCode) {
        int selectedLen = haoMa.split(",").length;
        if (selectedLen == 0) {// 号码存储出错
            throw new IllegalStateException("下注信息错误。号码存储失败");
        }
        int a = 1, b = 1;
        switch(playCode){
            case PlayCodeConstants.xuanerlianzhi:
            case PlayCodeConstants.xuansanqianzhi:
                for(int j=selectedLen;j>selectedLen-minSelected;j--){
                    b = b*j;
                }
                if (b < 0) {
                    return 0;
                }
                return b;
            case PlayCodeConstants.syx5_zhixuan_houer:
            case PlayCodeConstants.syx5_zhixuan_qianer:
            case PlayCodeConstants.syx5_zhixuan_qiansan:
            case PlayCodeConstants.syx5_zhixuan_zhongsan:
            case PlayCodeConstants.syx5_zhixuan_housan:
            case PlayCodeConstants.zuxuansan_housan:
            case PlayCodeConstants.zuxuansan_zhongsan:
            case PlayCodeConstants.zuxuansan_qiansan:
            case PlayCodeConstants.zuxuanliu_housan:
            case PlayCodeConstants.zuxuanliu_zhongsan:
            case PlayCodeConstants.zuxuanliu_qiansan:
            case PlayCodeConstants.zuxuan_san_peilv:
            case PlayCodeConstants.zuxuan_liu_peilv:
                return 1;
            default:
                for (int j = selectedLen; j > minSelected; j--) {
                    b = b * j;
                }
                // 最大注数计算根据公式(C(6,3)=6*5*4/(3*2*1))
                for (int i = 1; i <= selectedLen-minSelected; i++) {
                    a = a * i;
                }
                if (a == 0) {
                    return 0;
                }
                if (b / a < 0) {
                    return 0;
                }
                return b/a;
        }

    }

}
