package com.example.anuo.immodule.jsonmodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllLotteryInfoResponse {


    /**
     * success : true
     * accessToken : 5a95abc0-f9e6-4688-9539-1bb518904155
     * content : [{"duration":0,"sortNo":0,"ballonNums":0,"code":"ycp","czCode":"1000","moduleCode":3,"forwardAction":"/forwardTtLottery.do","name":"YG彩票","popFrame":false,"forwardUrl":"/forwardTtLottery.do","sys":false,"status":2},{"ballonNums":3,"code":"FFK3","czCode":"10","moduleCode":3,"pinLv":"1分钟","sys":true,"duration":10,"imgUrl":"","sortNo":2010,"name":"极速快三 ","popFrame":false,"lotVersion":2,"status":2},{"ballonNums":3,"code":"GSK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","sys":false,"duration":120,"imgUrl":"","sortNo":1999,"name":"甘肃快三","popFrame":false,"lotVersion":2,"status":2},{"ballonNums":3,"code":"MDK3","czCode":"10","moduleCode":3,"pinLv":"10分钟","sys":true,"duration":10,"imgUrl":"https://yt3.me/img/Y4MA/DQVwI0uq6.png","sortNo":1201,"name":"缅甸快三","popFrame":false,"lotVersion":2,"status":2},{"ballonNums":10,"code":"WFSC","czCode":"8","moduleCode":3,"pinLv":"5分钟","sys":true,"duration":30,"imgUrl":"","sortNo":567,"name":"五分赛车","popFrame":false,"lotVersion":2,"status":2},{"ballonNums":3,"code":"TWK3","czCode":"10","moduleCode":3,"pinLv":"5分钟","sys":true,"duration":10,"imgUrl":"","sortNo":560,"name":"台湾快三","popFrame":false,"lotVersion":2,"status":2},{"ballonNums":10,"code":"XXYFT","czCode":"8","moduleCode":3,"pinLv":"5分钟","sys":true,"duration":30,"imgUrl":"","sortNo":211,"name":"新幸运飞艇","popFrame":false,"lotVersion":2,"status":2},{"duration":30,"sortNo":210,"ballonNums":10,"code":"WFSC1","czCode":"8","moduleCode":3,"pinLv":"5分钟","name":"五分赛车1","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":209,"ballonNums":10,"code":"TFSC","czCode":"8","moduleCode":3,"pinLv":"10分钟","name":"十分赛车","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":209,"ballonNums":10,"code":"LBJSC","czCode":"8","moduleCode":3,"pinLv":"5分钟","name":"老北京赛车","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"ballonNums":10,"code":"XYFT","czCode":"8","moduleCode":3,"pinLv":"5分钟","sys":false,"duration":60,"imgUrl":"","sortNo":200,"name":"幸运飞艇","popFrame":false,"lotVersion":2,"status":2},{"ballonNums":10,"code":"AZFT","czCode":"8","moduleCode":3,"pinLv":"3分钟","sys":true,"duration":30,"imgUrl":"","sortNo":200,"name":"澳洲飞艇","popFrame":false,"lotVersion":2,"status":2},{"duration":10,"sortNo":199,"ballonNums":10,"code":"WFFT","czCode":"8","moduleCode":3,"pinLv":"5分钟","name":"五分飞艇","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":60,"sortNo":199,"ballonNums":10,"code":"XYFT2","czCode":"8","moduleCode":3,"pinLv":"5分钟","name":"168幸运飞艇","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"ballonNums":5,"code":"YNSSC","czCode":"9","moduleCode":3,"pinLv":"3分钟","sys":true,"duration":70,"imgUrl":"","sortNo":196,"name":"越南时时彩","popFrame":false,"lotVersion":2,"status":2},{"duration":70,"sortNo":195,"ballonNums":5,"code":"AZSSC","czCode":"9","moduleCode":3,"pinLv":"5分钟","name":"澳洲时时彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":194,"ballonNums":5,"code":"LCQSSC","czCode":"9","moduleCode":3,"pinLv":"5/10分钟","name":"老重庆时时彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":20,"sortNo":190,"ballonNums":10,"code":"JSSC168","czCode":"8","moduleCode":3,"pinLv":"75秒","name":"168极速赛车","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":190,"ballonNums":10,"code":"AZXYW168","czCode":"9","moduleCode":3,"pinLv":"5分钟","name":"168澳洲幸运5","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":190,"ballonNums":10,"code":"AZXYT168","czCode":"8","moduleCode":3,"pinLv":"5分钟","name":"168澳洲幸运10","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":10,"sortNo":188,"ballonNums":3,"code":"SF28","czCode":"11","moduleCode":3,"pinLv":"3分钟","name":"三分28","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":188,"ballonNums":3,"code":"WF28","czCode":"11","moduleCode":3,"pinLv":"5分钟","name":"五分28","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"ballonNums":10,"code":"AMLHC2","czCode":"6","moduleCode":3,"pinLv":"1天","sys":false,"duration":120,"imgUrl":"","sortNo":188,"name":"澳门六合彩2","popFrame":false,"lotVersion":2,"status":2},{"duration":10,"sortNo":188,"ballonNums":3,"code":"FF28","czCode":"11","moduleCode":3,"pinLv":"1分钟","name":"极速28","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":188,"ballonNums":3,"code":"TEQ28","czCode":"11","moduleCode":3,"pinLv":"3分钟","name":"土耳其28","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":180,"sortNo":188,"ballonNums":7,"code":"TTLHC","czCode":"66","moduleCode":3,"pinLv":"1天","name":"天天六合彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":188,"ballonNums":3,"code":"AZ28","czCode":"11","moduleCode":3,"pinLv":"3分钟","name":"澳洲28","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":184,"ballonNums":8,"code":"YNKL","czCode":"12","moduleCode":3,"pinLv":"5分钟","name":"越南快乐彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":184,"ballonNums":8,"code":"AZKL","czCode":"12","moduleCode":3,"pinLv":"3分钟","name":"澳洲快乐彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":170,"ballonNums":8,"code":"KL3F","czCode":"12","moduleCode":3,"pinLv":"3分钟","name":"快乐三分","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":170,"ballonNums":8,"code":"KL5F","czCode":"12","moduleCode":3,"pinLv":"5分钟","name":"快乐五分","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":170,"ballonNums":8,"code":"KLTF","czCode":"12","moduleCode":3,"pinLv":"10分钟","name":"快乐十分","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":120,"sortNo":165,"ballonNums":7,"code":"TWLHC","czCode":"6","moduleCode":3,"pinLv":"一 三 五","name":"台湾六合彩","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":165,"ballonNums":7,"code":"YNLHC","czCode":"66","moduleCode":3,"pinLv":"二 四 六","name":"越南六合彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":30,"sortNo":165,"ballonNums":7,"code":"WFLHC","czCode":"66","moduleCode":3,"pinLv":"5分钟","name":"五分六合彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":30,"sortNo":165,"ballonNums":7,"code":"SLHC","czCode":"66","moduleCode":3,"pinLv":"3分钟","name":"三分六合彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":15,"sortNo":165,"ballonNums":7,"code":"JSLHC","czCode":"66","moduleCode":3,"pinLv":"75秒","name":"极速六合彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":165,"ballonNums":5,"code":"TXFFC","czCode":"9","moduleCode":3,"pinLv":"1分钟","name":"腾讯分分彩","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":165,"ballonNums":7,"code":"DMLHC","czCode":"66","moduleCode":3,"pinLv":"一 三 五","name":"大马六合彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":150,"ballonNums":5,"code":"FFC","czCode":"9","moduleCode":3,"pinLv":"1分钟","name":"分分彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"ballonNums":5,"code":"SFC","czCode":"9","moduleCode":3,"pinLv":"3分钟","sys":true,"duration":10,"imgUrl":"","sortNo":150,"name":"三分时时彩","popFrame":false,"lotVersion":2,"status":2},{"duration":10,"sortNo":150,"ballonNums":5,"code":"XGFC","czCode":"9","moduleCode":3,"pinLv":"75秒","name":"香港极速彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":150,"ballonNums":5,"code":"TFC","czCode":"9","moduleCode":3,"pinLv":"10分钟","name":"十分彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":150,"ballonNums":5,"code":"AZWF","czCode":"9","moduleCode":3,"pinLv":"5分钟","name":"澳洲五分彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":150,"ballonNums":5,"code":"AMFC","czCode":"9","moduleCode":3,"pinLv":"75秒","name":"澳门极速彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":150,"ballonNums":5,"code":"AZXY","czCode":"9","moduleCode":3,"pinLv":"3分钟","name":"澳洲幸运彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"ballonNums":5,"code":"WFC","czCode":"9","moduleCode":3,"pinLv":"5分钟","sys":true,"duration":30,"imgUrl":"","sortNo":148,"name":"五分彩","popFrame":false,"lotVersion":2,"status":2},{"duration":120,"sortNo":147,"ballonNums":3,"code":"HBK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"湖北快三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":180,"sortNo":146,"ballonNums":3,"code":"ES1K3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"二十分快三(1)","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":180,"sortNo":146,"ballonNums":3,"code":"TFK3","czCode":"10","moduleCode":3,"pinLv":"10分钟","name":"十分快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":120,"sortNo":146,"ballonNums":3,"code":"AHK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"安徽快三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":144,"ballonNums":3,"code":"HEBK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"河北快三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":143,"ballonNums":3,"code":"GXK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"广西快三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":60,"sortNo":142,"ballonNums":3,"code":"TF3D","czCode":"15","moduleCode":3,"pinLv":"10分钟","name":"十分3D","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":15,"sortNo":142,"ballonNums":3,"code":"JS3D","czCode":"15","moduleCode":3,"pinLv":"1分半","name":"极速3D","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":600,"sortNo":142,"ballonNums":3,"code":"FC3D","czCode":"15","moduleCode":3,"pinLv":"24小时","name":"福彩3D","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":600,"sortNo":141,"ballonNums":3,"code":"PL3","czCode":"15","moduleCode":3,"pinLv":"24小时","name":"排列三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":600,"sortNo":141,"ballonNums":3,"code":"YNPL3","czCode":"15","moduleCode":3,"pinLv":"5分钟","name":"越南排列三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":140,"ballonNums":5,"code":"GD11X5","czCode":"14","moduleCode":3,"pinLv":"20分钟","name":"广东11选5","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":90,"sortNo":140,"ballonNums":5,"code":"AZ11X5","czCode":"14","moduleCode":3,"pinLv":"3分钟","name":"澳洲11选5","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":140,"ballonNums":5,"code":"YN11X5","czCode":"14","moduleCode":3,"pinLv":"5分钟","name":"越南11选5","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":139,"ballonNums":5,"code":"WF11X5","czCode":"14","moduleCode":3,"pinLv":"5分钟","name":"五分11选5","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":139,"ballonNums":5,"code":"SD11X5","czCode":"14","moduleCode":3,"pinLv":"20分钟","name":"山东11选5","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":10,"sortNo":139,"ballonNums":5,"code":"TF11X5","czCode":"14","moduleCode":3,"pinLv":"10分钟","name":"十分11选5","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":139,"ballonNums":5,"code":"SF11X5","czCode":"14","moduleCode":3,"pinLv":"3分钟","name":"三分11选5","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":138,"ballonNums":5,"code":"JX11X5","czCode":"14","moduleCode":3,"pinLv":"20分钟","name":"江西11选5","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":90,"sortNo":137,"ballonNums":5,"code":"SH11X5","czCode":"14","moduleCode":3,"pinLv":"20分钟","name":"上海11选5","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":10,"sortNo":103,"ballonNums":10,"code":"FKSC","czCode":"8","moduleCode":3,"pinLv":"75秒","name":"疯狂赛车","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":101,"ballonNums":10,"code":"SFSC","czCode":"8","moduleCode":3,"pinLv":"3分钟","name":"极速赛车","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":30,"sortNo":100,"ballonNums":10,"code":"BJSC","czCode":"8","moduleCode":3,"pinLv":"20分钟","name":"北京赛车","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"ballonNums":10,"code":"YLSM","czCode":"8","moduleCode":3,"pinLv":"3分钟","sys":true,"duration":30,"imgUrl":"","sortNo":100,"name":"英伦赛马","popFrame":false,"lotVersion":2,"status":2},{"duration":120,"sortNo":97,"ballonNums":8,"code":"CQXYNC","czCode":"12","moduleCode":3,"pinLv":"20分钟","name":"重庆幸运农场","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":70,"sortNo":95,"ballonNums":5,"code":"CQSSC","czCode":"9","moduleCode":3,"pinLv":"20分钟","name":"重庆时时彩","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":70,"sortNo":90,"ballonNums":3,"code":"JSSB3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"江苏骰宝(快3)","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":70,"sortNo":88,"ballonNums":3,"code":"PCEGG","czCode":"11","moduleCode":3,"pinLv":"5分钟","name":"PC蛋蛋","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":30,"sortNo":86,"ballonNums":3,"code":"JND28","czCode":"11","moduleCode":3,"pinLv":"3分钟半","name":"加拿大28","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":85,"ballonNums":7,"code":"LHC","czCode":"6","moduleCode":3,"pinLv":"二 四 六","name":"六合彩","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":60,"sortNo":85,"ballonNums":7,"code":"SFLHC","czCode":"66","moduleCode":3,"pinLv":"10分钟","name":"十分六合彩","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":90,"sortNo":84,"ballonNums":8,"code":"HNKLSF","czCode":"12","moduleCode":3,"pinLv":"20分钟","name":"湖南快乐十分","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":82,"ballonNums":5,"code":"TJSSC","czCode":"9","moduleCode":3,"pinLv":"20分钟","name":"天津时时彩","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":70,"sortNo":80,"ballonNums":5,"code":"XJSSC","czCode":"9","moduleCode":3,"pinLv":"20分钟","name":"新疆时时彩","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":90,"sortNo":70,"ballonNums":8,"code":"GDKLSF","czCode":"12","moduleCode":3,"pinLv":"20分钟","name":"广东快乐十分","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":120,"sortNo":66,"ballonNums":3,"code":"BJK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"北京快三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":10,"sortNo":66,"ballonNums":3,"code":"SFK3","czCode":"10","moduleCode":3,"pinLv":"3分钟","name":"三分快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":120,"sortNo":64,"ballonNums":3,"code":"JXK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"江西快三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":10,"sortNo":60,"ballonNums":3,"code":"RBK3","czCode":"10","moduleCode":3,"pinLv":"10分钟","name":"日本快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":60,"ballonNums":3,"code":"HCK3","czCode":"10","moduleCode":3,"pinLv":"10分钟","name":"汉城快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":60,"ballonNums":3,"code":"TGK3","czCode":"10","moduleCode":3,"pinLv":"10分钟","name":"泰国快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":10,"sortNo":60,"ballonNums":3,"code":"AZK3","czCode":"10","moduleCode":3,"pinLv":"3分钟","name":"澳洲快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"ballonNums":3,"code":"ESK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","sys":true,"duration":10,"imgUrl":"","sortNo":60,"name":"二十分快三","popFrame":false,"lotVersion":2,"status":2},{"duration":10,"sortNo":60,"ballonNums":3,"code":"YNK3","czCode":"10","moduleCode":3,"pinLv":"5分钟","name":"越南快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":30,"sortNo":55,"ballonNums":3,"code":"WFK3","czCode":"10","moduleCode":3,"pinLv":"5分钟","name":"幸运快三","popFrame":false,"lotVersion":2,"sys":true,"status":2},{"duration":120,"sortNo":50,"ballonNums":3,"code":"JLK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"吉林快三","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"duration":0,"sortNo":36,"ballonNums":7,"code":"AMLHC","czCode":"6","moduleCode":3,"pinLv":"二 四 六","name":"澳门六合彩","popFrame":false,"lotVersion":2,"sys":false,"status":2},{"ballonNums":7,"code":"QMLHC","czCode":"6","moduleCode":3,"pinLv":"1天","sys":false,"duration":120,"imgUrl":"","sortNo":35,"name":"快乐六合彩","popFrame":false,"lotVersion":2,"status":2},{"duration":120,"sortNo":1,"ballonNums":3,"code":"SHHK3","czCode":"10","moduleCode":3,"pinLv":"20分钟","name":"上海快三","popFrame":false,"lotVersion":2,"sys":false,"status":2}]
     */
    @SerializedName("success")
    private boolean success;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("content")
    private List<LotteryInfo> content;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setContent(List<LotteryInfo> content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<LotteryInfo> getContent() {
        return content;
    }

    public class LotteryInfo {
        /**
         * duration : 0
         * sortNo : 0
         * ballonNums : 0
         * code : ycp
         * czCode : 1000
         * moduleCode : 3
         * forwardAction : /forwardTtLottery.do
         * name : YG彩票
         * popFrame : false
         * forwardUrl : /forwardTtLottery.do
         * sys : false
         * status : 2
         */
        @SerializedName("duration")
        private int duration;
        @SerializedName("sortNo")
        private int sortNo;
        @SerializedName("ballonNums")
        private int ballonNums;
        @SerializedName("code")
        private String code;
        @SerializedName("czCode")
        private String czCode;
        @SerializedName("moduleCode")
        private int moduleCode;
        @SerializedName("forwardAction")
        private String forwardAction;
        @SerializedName("name")
        private String name;
        @SerializedName("popFrame")
        private boolean popFrame;
        @SerializedName("forwardUrl")
        private String forwardUrl;
        @SerializedName("sys")
        private boolean sys;
        @SerializedName("status")
        private int status;

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setSortNo(int sortNo) {
            this.sortNo = sortNo;
        }

        public void setBallonNums(int ballonNums) {
            this.ballonNums = ballonNums;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setCzCode(String czCode) {
            this.czCode = czCode;
        }

        public void setModuleCode(int moduleCode) {
            this.moduleCode = moduleCode;
        }

        public void setForwardAction(String forwardAction) {
            this.forwardAction = forwardAction;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPopFrame(boolean popFrame) {
            this.popFrame = popFrame;
        }

        public void setForwardUrl(String forwardUrl) {
            this.forwardUrl = forwardUrl;
        }

        public void setSys(boolean sys) {
            this.sys = sys;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getDuration() {
            return duration;
        }

        public int getSortNo() {
            return sortNo;
        }

        public int getBallonNums() {
            return ballonNums;
        }

        public String getCode() {
            return code;
        }

        public String getCzCode() {
            return czCode;
        }

        public int getModuleCode() {
            return moduleCode;
        }

        public String getForwardAction() {
            return forwardAction;
        }

        public String getName() {
            return name;
        }

        public boolean isPopFrame() {
            return popFrame;
        }

        public String getForwardUrl() {
            return forwardUrl;
        }

        public boolean isSys() {
            return sys;
        }

        public int getStatus() {
            return status;
        }
    }
}
