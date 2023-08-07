package com.example.anuo.immodule.bean;

import java.util.List;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :2020-04-28
 * Desc  :com.example.anuo.immodule.bean
 */
public class ChatLongDragonBean {

    /**
     * msg : 操作成功。
     * code : R70043
     * success : true
     * source : {"longDragon":[{"continueStage":3,"lotteryCode":"WFFT","lotteryIcon":"","lotteryName":"五分飞艇","lotteryNum":"20200428201","lotteryResult":"单","playName":"冠亚和值单双"},{"continueStage":3,"lotteryCode":"WFSC","lotteryIcon":"","lotteryName":"五分赛车","lotteryNum":"20200428201","lotteryResult":"虎","playName":"亚军龙虎"},{"continueStage":2,"lotteryCode":"WFFT","lotteryIcon":"","lotteryName":"五分飞艇","lotteryNum":"20200428201","lotteryResult":"7","playName":"冠亚和值"},{"continueStage":2,"lotteryCode":"WFFT","lotteryIcon":"","lotteryName":"五分飞艇","lotteryNum":"20200428201","lotteryResult":"小","playName":"冠亚和值大小"},{"continueStage":2,"lotteryCode":"WFFT","lotteryIcon":"","lotteryName":"五分飞艇","lotteryNum":"20200428201","lotteryResult":"小","playName":"冠军大小"},{"continueStage":2,"lotteryCode":"WFSC","lotteryIcon":"","lotteryName":"五分赛车","lotteryNum":"20200428201","lotteryResult":"虎","playName":"冠军龙虎"},{"continueStage":2,"lotteryCode":"WFC","lotteryName":"五分彩","lotteryNum":"20200428200","lotteryResult":"单","playName":"和值单双"},{"continueStage":2,"lotteryCode":"WFC","lotteryName":"五分彩","lotteryNum":"20200428200","lotteryResult":"小","playName":"和值大小"},{"continueStage":2,"lotteryCode":"FKSC","lotteryName":"疯狂赛车","lotteryNum":"202004280801","lotteryResult":"双","playName":"第6名单双"},{"continueStage":2,"lotteryCode":"FKSC","lotteryName":"疯狂赛车","lotteryNum":"202004280801","lotteryResult":"单","playName":"第4名单双"},{"continueStage":1,"lotteryCode":"WFFT","lotteryIcon":"","lotteryName":"五分飞艇","lotteryNum":"20200428201","lotteryResult":"龙","playName":"亚军龙虎"},{"continueStage":1,"lotteryCode":"WFFT","lotteryIcon":"","lotteryName":"五分飞艇","lotteryNum":"20200428201","lotteryResult":"大","playName":"第10名大小"},{"continueStage":1,"lotteryCode":"WFSC","lotteryIcon":"","lotteryName":"五分赛车","lotteryNum":"20200428201","lotteryResult":"单","playName":"冠亚和值单双"},{"continueStage":1,"lotteryCode":"WFC","lotteryName":"五分彩","lotteryNum":"20200428200","lotteryResult":"虎","playName":"龙虎斗"},{"continueStage":1,"lotteryCode":"FKFT","lotteryName":"极速飞艇","lotteryNum":"202004280801","lotteryResult":"03","playName":"猜冠军"},{"continueStage":1,"lotteryCode":"FKFT","lotteryName":"极速飞艇","lotteryNum":"202004280801","lotteryResult":"7","playName":"冠亚和值"},{"continueStage":1,"lotteryCode":"FKSC","lotteryName":"疯狂赛车","lotteryNum":"202004280801","lotteryResult":"单","playName":"第10名单双"},{"continueStage":1,"lotteryCode":"FKSC","lotteryName":"疯狂赛车","lotteryNum":"202004280801","lotteryResult":"双","playName":"第8名单双"}]}
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private SourceBean source;
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class SourceBean {
        private List<LongDragonBean> longDragon;

        public List<LongDragonBean> getLongDragon() {
            return longDragon;
        }

        public void setLongDragon(List<LongDragonBean> longDragon) {
            this.longDragon = longDragon;
        }

        public static class LongDragonBean {
            /**
             * continueStage : 3
             * lotteryCode : WFFT
             * lotteryIcon :
             * lotteryName : 五分飞艇
             * lotteryNum : 20200428201
             * lotteryResult : 单
             * playName : 冠亚和值单双
             */

            private int continueStage;
            private String lotteryCode;
            private String lotteryIcon;
            private String lotteryName;
            private String lotteryNum;
            private String lotteryResult;
            private String playName;

            public int getContinueStage() {
                return continueStage;
            }

            public void setContinueStage(int continueStage) {
                this.continueStage = continueStage;
            }

            public String getLotteryCode() {
                return lotteryCode;
            }

            public void setLotteryCode(String lotteryCode) {
                this.lotteryCode = lotteryCode;
            }

            public String getLotteryIcon() {
                return lotteryIcon;
            }

            public void setLotteryIcon(String lotteryIcon) {
                this.lotteryIcon = lotteryIcon;
            }

            public String getLotteryName() {
                return lotteryName;
            }

            public void setLotteryName(String lotteryName) {
                this.lotteryName = lotteryName;
            }

            public String getLotteryNum() {
                return lotteryNum;
            }

            public void setLotteryNum(String lotteryNum) {
                this.lotteryNum = lotteryNum;
            }

            public String getLotteryResult() {
                return lotteryResult;
            }

            public void setLotteryResult(String lotteryResult) {
                this.lotteryResult = lotteryResult;
            }

            public String getPlayName() {
                return playName;
            }

            public void setPlayName(String playName) {
                this.playName = playName;
            }
        }
    }
}
