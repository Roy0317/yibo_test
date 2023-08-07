package com.yibo.yiboapp.entify;

import com.yibo.yiboapp.utils.Utils;

/**
 * Created by johnson on 2017/10/16.
 */

public class GoucaiResult {
    long serverTime;
    String qiHao;//最近一期的期号
    long activeTime;//激活截止时间
    int ago;//封盘离开奖时间差，秒
    String haoMa;//开奖号码
    String lastQihao;//上一次期号
    String lotCode;//彩long票编码
    String lotName;//彩票名字
    String codeType;//彩种代号
    public long countDownTime ;//倒计时
    public String time ;//倒计时时间毫米值
    public long agoDownTime;//时间到了ago之后 也要刷新一次
    String lotIcon;//彩种

    public String getLotIcon() {
        return lotIcon;
    }

    public void setLotIcon(String lotIcon) {
        this.lotIcon = lotIcon;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getQiHao() {
        if (!Utils.isEmptyString(qiHao)) {
            if (qiHao.length() <= 6) {
                return qiHao;
            } else {
                return qiHao.substring(qiHao.length() - 6);
            }
        } else {
            return "0";
        }

    }

    public void setQiHao(String qiHao) {
        this.qiHao = qiHao;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public int getAgo() {
        return ago;
    }

    public void setAgo(int ago) {
        this.ago = ago;
    }

    public String getHaoMa() {
        return haoMa;
    }

    public void setHaoMa(String haoMa) {
        this.haoMa = haoMa;
    }

    public String getLastQihao() {
        return lastQihao;
    }

    public void setLastQihao(String lastQihao) {
        this.lastQihao = lastQihao;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }
}
