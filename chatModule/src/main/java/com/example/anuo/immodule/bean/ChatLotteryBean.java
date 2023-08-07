package com.example.anuo.immodule.bean;

import android.print.PrinterId;

import java.util.List;

public class ChatLotteryBean {


    /**
     * msg : 操作成功。
     * code : R7028
     * success : true
     * source : {"success":true,"lotteryData":[{"sortNo":580,"code":"WFC","ago":10,"icon":"","name":"网易五分彩","className":"","lotVersion":1,"id":505,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":520,"code":"CQSSC","ago":70,"icon":"","name":"重庆时时彩","className":"","lotVersion":1,"id":500,"lotType":1,"groupCode":"ssc","stationId":2,"status":1},{"sortNo":510,"code":"LCQSSC","ago":70,"name":"老重庆时时彩","lotVersion":2,"id":1209,"lotType":1,"groupCode":"ssc","stationId":2,"status":2},{"sortNo":505,"code":"TJSSC","ago":70,"icon":"","name":"天津时时彩","className":"","lotVersion":1,"id":502,"lotType":1,"groupCode":"ssc","stationId":2,"status":2},{"sortNo":500,"code":"XJSSC","ago":70,"icon":"","name":"新疆时时彩","className":"","lotVersion":1,"id":501,"lotType":1,"groupCode":"ssc","stationId":2,"status":2},{"sortNo":485,"code":"FFC","ago":10,"icon":"","name":"分分彩","className":"","lotVersion":1,"id":503,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":480,"code":"LBJSC","ago":30,"name":"老北京赛车","lotVersion":1,"id":1196,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":480,"code":"EFC","ago":10,"name":"二分彩","lotVersion":1,"id":504,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":476,"code":"SFC","ago":10,"name":"三分彩","lotVersion":1,"id":1194,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":470,"code":"BJSC","ago":30,"name":"北京赛车","lotVersion":1,"id":506,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":465,"code":"XYFT","ago":60,"name":"幸运飞艇","lotVersion":1,"id":507,"lotType":3,"groupCode":"pk10","stationId":2,"status":1},{"sortNo":460,"code":"FKSC","ago":10,"name":"疯狂赛车","lotVersion":1,"id":508,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":455,"code":"SFSC","ago":10,"name":"幸运赛车","lotVersion":2,"id":544,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":454,"code":"FKFT","ago":10,"name":"疯狂飞艇","lotVersion":1,"id":1198,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":450,"code":"SH11X5","ago":90,"name":"上海11选5","lotVersion":1,"id":522,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":400,"code":"JX11X5","ago":90,"name":"江西11选5","lotVersion":2,"id":558,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":390,"code":"SD11X5","ago":90,"name":"山东11选5","lotVersion":2,"id":559,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":380,"code":"GD11X5","ago":90,"name":"广东11选5","lotVersion":1,"id":525,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":370,"code":"PL3","ago":600,"name":"排列三","lotVersion":1,"id":532,"lotType":8,"groupCode":"dpc","stationId":2,"status":2},{"sortNo":360,"code":"FC3D","ago":600,"name":"福彩3D","lotVersion":1,"id":533,"lotType":8,"groupCode":"dpc","stationId":2,"status":2},{"sortNo":350,"code":"LHC","ago":120,"name":"六合彩","lotVersion":1,"id":1267,"lotType":6,"groupCode":"six","stationId":2,"status":2},{"sortNo":340,"code":"SFLHC","ago":30,"name":"十分六合彩","lotVersion":1,"id":1268,"lotType":66,"groupCode":"six66","stationId":2,"status":1},{"sortNo":335,"code":"WFLHC","ago":10,"name":"五分六合彩","lotVersion":2,"id":1274,"lotType":66,"groupCode":"six66","stationId":2,"status":2},{"sortNo":334,"code":"SLHC","ago":5,"name":"三分六合彩","lotVersion":2,"id":1275,"lotType":66,"groupCode":"six66","stationId":2,"status":2},{"sortNo":333,"code":"FFLHC","ago":5,"name":"极速六合彩","lotVersion":1,"id":1271,"lotType":66,"groupCode":"six66","stationId":2,"status":2},{"sortNo":330,"code":"HNKLSF","ago":120,"name":"湖南快十","lotVersion":2,"id":570,"lotType":9,"groupCode":"klsf","stationId":2,"status":1},{"sortNo":320,"code":"GDKLSF","ago":120,"name":"广东快十","lotVersion":2,"id":571,"lotType":9,"groupCode":"klsf","stationId":2,"status":2},{"sortNo":310,"code":"CQXYNC","ago":120,"name":"幸运农场","lotVersion":2,"id":572,"lotType":9,"groupCode":"klsf","stationId":2,"status":2},{"sortNo":300,"code":"JSK3","ago":120,"name":"江苏快三","lotVersion":2,"id":545,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":290,"code":"AHK3","ago":120,"name":"安徽快三","lotVersion":2,"id":546,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":280,"code":"HUBK3","ago":120,"name":"湖北快三","lotVersion":1,"id":512,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":270,"code":"HEBK3","ago":120,"name":"河北快三","lotVersion":2,"id":548,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":260,"code":"GXK3","ago":120,"name":"广西快三","lotVersion":2,"id":549,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":250,"code":"SHHK3","ago":120,"name":"上海快三","lotVersion":2,"id":550,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":240,"code":"BJK3","ago":120,"name":"北京快三","lotVersion":1,"id":516,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":230,"code":"JXK3","ago":120,"name":"江西快三","lotVersion":1,"id":517,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":220,"code":"GSK3","ago":120,"name":"甘肃快三","lotVersion":1,"id":518,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":210,"code":"JLK3","ago":120,"name":"吉林快三","lotVersion":2,"id":554,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":205,"code":"TFK3","ago":10,"name":"十分快三","lotVersion":2,"id":1218,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":200,"code":"FFK3","ago":10,"name":"极速快三","lotVersion":1,"id":520,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":190,"code":"WFK3","ago":30,"name":"幸运快三","lotVersion":2,"id":556,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":180,"code":"PCEGG","ago":90,"icon":"","name":"PC蛋蛋","className":"","lotVersion":1,"id":529,"lotType":7,"groupCode":"pcdd","stationId":2,"status":2},{"sortNo":175,"code":"FF28","ago":10,"name":"极速28","lotVersion":2,"id":566,"lotType":7,"groupCode":"pcdd","stationId":2,"status":2},{"sortNo":170,"code":"JND28","ago":30,"name":"加拿大28","lotVersion":2,"id":565,"lotType":7,"groupCode":"pcdd","stationId":2,"status":2},{"sortNo":160,"code":"JS3D","ago":10,"name":"极速3D","lotVersion":2,"id":569,"lotType":8,"groupCode":"dpc","stationId":2,"status":2}]}
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
        /**
         * success : true
         * lotteryData : [{"sortNo":580,"code":"WFC","ago":10,"icon":"","name":"网易五分彩","className":"","lotVersion":1,"id":505,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":520,"code":"CQSSC","ago":70,"icon":"","name":"重庆时时彩","className":"","lotVersion":1,"id":500,"lotType":1,"groupCode":"ssc","stationId":2,"status":1},{"sortNo":510,"code":"LCQSSC","ago":70,"name":"老重庆时时彩","lotVersion":2,"id":1209,"lotType":1,"groupCode":"ssc","stationId":2,"status":2},{"sortNo":505,"code":"TJSSC","ago":70,"icon":"","name":"天津时时彩","className":"","lotVersion":1,"id":502,"lotType":1,"groupCode":"ssc","stationId":2,"status":2},{"sortNo":500,"code":"XJSSC","ago":70,"icon":"","name":"新疆时时彩","className":"","lotVersion":1,"id":501,"lotType":1,"groupCode":"ssc","stationId":2,"status":2},{"sortNo":485,"code":"FFC","ago":10,"icon":"","name":"分分彩","className":"","lotVersion":1,"id":503,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":480,"code":"LBJSC","ago":30,"name":"老北京赛车","lotVersion":1,"id":1196,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":480,"code":"EFC","ago":10,"name":"二分彩","lotVersion":1,"id":504,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":476,"code":"SFC","ago":10,"name":"三分彩","lotVersion":1,"id":1194,"lotType":2,"groupCode":"ffc","stationId":2,"status":2},{"sortNo":470,"code":"BJSC","ago":30,"name":"北京赛车","lotVersion":1,"id":506,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":465,"code":"XYFT","ago":60,"name":"幸运飞艇","lotVersion":1,"id":507,"lotType":3,"groupCode":"pk10","stationId":2,"status":1},{"sortNo":460,"code":"FKSC","ago":10,"name":"疯狂赛车","lotVersion":1,"id":508,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":455,"code":"SFSC","ago":10,"name":"幸运赛车","lotVersion":2,"id":544,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":454,"code":"FKFT","ago":10,"name":"疯狂飞艇","lotVersion":1,"id":1198,"lotType":3,"groupCode":"pk10","stationId":2,"status":2},{"sortNo":450,"code":"SH11X5","ago":90,"name":"上海11选5","lotVersion":1,"id":522,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":400,"code":"JX11X5","ago":90,"name":"江西11选5","lotVersion":2,"id":558,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":390,"code":"SD11X5","ago":90,"name":"山东11选5","lotVersion":2,"id":559,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":380,"code":"GD11X5","ago":90,"name":"广东11选5","lotVersion":1,"id":525,"lotType":5,"groupCode":"syxw","stationId":2,"status":2},{"sortNo":370,"code":"PL3","ago":600,"name":"排列三","lotVersion":1,"id":532,"lotType":8,"groupCode":"dpc","stationId":2,"status":2},{"sortNo":360,"code":"FC3D","ago":600,"name":"福彩3D","lotVersion":1,"id":533,"lotType":8,"groupCode":"dpc","stationId":2,"status":2},{"sortNo":350,"code":"LHC","ago":120,"name":"六合彩","lotVersion":1,"id":1267,"lotType":6,"groupCode":"six","stationId":2,"status":2},{"sortNo":340,"code":"SFLHC","ago":30,"name":"十分六合彩","lotVersion":1,"id":1268,"lotType":66,"groupCode":"six66","stationId":2,"status":1},{"sortNo":335,"code":"WFLHC","ago":10,"name":"五分六合彩","lotVersion":2,"id":1274,"lotType":66,"groupCode":"six66","stationId":2,"status":2},{"sortNo":334,"code":"SLHC","ago":5,"name":"三分六合彩","lotVersion":2,"id":1275,"lotType":66,"groupCode":"six66","stationId":2,"status":2},{"sortNo":333,"code":"FFLHC","ago":5,"name":"极速六合彩","lotVersion":1,"id":1271,"lotType":66,"groupCode":"six66","stationId":2,"status":2},{"sortNo":330,"code":"HNKLSF","ago":120,"name":"湖南快十","lotVersion":2,"id":570,"lotType":9,"groupCode":"klsf","stationId":2,"status":1},{"sortNo":320,"code":"GDKLSF","ago":120,"name":"广东快十","lotVersion":2,"id":571,"lotType":9,"groupCode":"klsf","stationId":2,"status":2},{"sortNo":310,"code":"CQXYNC","ago":120,"name":"幸运农场","lotVersion":2,"id":572,"lotType":9,"groupCode":"klsf","stationId":2,"status":2},{"sortNo":300,"code":"JSK3","ago":120,"name":"江苏快三","lotVersion":2,"id":545,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":290,"code":"AHK3","ago":120,"name":"安徽快三","lotVersion":2,"id":546,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":280,"code":"HUBK3","ago":120,"name":"湖北快三","lotVersion":1,"id":512,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":270,"code":"HEBK3","ago":120,"name":"河北快三","lotVersion":2,"id":548,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":260,"code":"GXK3","ago":120,"name":"广西快三","lotVersion":2,"id":549,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":250,"code":"SHHK3","ago":120,"name":"上海快三","lotVersion":2,"id":550,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":240,"code":"BJK3","ago":120,"name":"北京快三","lotVersion":1,"id":516,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":230,"code":"JXK3","ago":120,"name":"江西快三","lotVersion":1,"id":517,"lotType":4,"groupCode":"k3","stationId":2,"status":1},{"sortNo":220,"code":"GSK3","ago":120,"name":"甘肃快三","lotVersion":1,"id":518,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":210,"code":"JLK3","ago":120,"name":"吉林快三","lotVersion":2,"id":554,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":205,"code":"TFK3","ago":10,"name":"十分快三","lotVersion":2,"id":1218,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":200,"code":"FFK3","ago":10,"name":"极速快三","lotVersion":1,"id":520,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":190,"code":"WFK3","ago":30,"name":"幸运快三","lotVersion":2,"id":556,"lotType":4,"groupCode":"k3","stationId":2,"status":2},{"sortNo":180,"code":"PCEGG","ago":90,"icon":"","name":"PC蛋蛋","className":"","lotVersion":1,"id":529,"lotType":7,"groupCode":"pcdd","stationId":2,"status":2},{"sortNo":175,"code":"FF28","ago":10,"name":"极速28","lotVersion":2,"id":566,"lotType":7,"groupCode":"pcdd","stationId":2,"status":2},{"sortNo":170,"code":"JND28","ago":30,"name":"加拿大28","lotVersion":2,"id":565,"lotType":7,"groupCode":"pcdd","stationId":2,"status":2},{"sortNo":160,"code":"JS3D","ago":10,"name":"极速3D","lotVersion":2,"id":569,"lotType":8,"groupCode":"dpc","stationId":2,"status":2}]
         */

        private boolean success;
        private List<LotteryDataBean> lotteryData;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<LotteryDataBean> getLotteryData() {
            return lotteryData;
        }

        public void setLotteryData(List<LotteryDataBean> lotteryData) {
            this.lotteryData = lotteryData;
        }

        public static class LotteryDataBean {
            /**
             * sortNo : 580
             * code : WFC
             * ago : 10
             * icon :
             * name : 网易五分彩
             * className :
             * lotVersion : 1
             * id : 505
             * lotType : 2
             * groupCode : ffc
             * stationId : 2
             * status : 2
             */

            private int sortNo;
            private String code;
            private int ago;
            private String icon;
            private String name;
            private String className;
            private int lotVersion;
            private int id;
            private int lotType;
            private String groupCode;
            private int stationId;
            private int status;

            private String haoMa;
            private String qiHao;
            private long data;
            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getHaoMa() {
                return haoMa;
            }

            public void setHaoMa(String haoMa) {
                this.haoMa = haoMa;
            }

            public String getQiHao() {
                return qiHao;
            }

            public void setQiHao(String qiHao) {
                this.qiHao = qiHao;
            }

            public long getData() {
                return data;
            }

            public void setData(long data) {
                this.data = data;
            }

            public int getSortNo() {
                return sortNo;
            }

            public void setSortNo(int sortNo) {
                this.sortNo = sortNo;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getAgo() {
                return ago;
            }

            public void setAgo(int ago) {
                this.ago = ago;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public int getLotVersion() {
                return lotVersion;
            }

            public void setLotVersion(int lotVersion) {
                this.lotVersion = lotVersion;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLotType() {
                return lotType;
            }

            public void setLotType(int lotType) {
                this.lotType = lotType;
            }

            public String getGroupCode() {
                return groupCode;
            }

            public void setGroupCode(String groupCode) {
                this.groupCode = groupCode;
            }

            public int getStationId() {
                return stationId;
            }

            public void setStationId(int stationId) {
                this.stationId = stationId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
