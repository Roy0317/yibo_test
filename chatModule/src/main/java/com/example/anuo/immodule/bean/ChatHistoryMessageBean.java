package com.example.anuo.immodule.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ChatHistoryMessageBean implements Parcelable {

    /**
     * msg : 操作成功。
     * code : R7029
     * success : true
     * source : [{"date":"2019-10-04","msgType":2,"nickName":"","privatePermission":false,"msgId":"19106a7ab52cd2134b4fb9961d36b34c35e9","levelName":"王者会员","avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","url":"191029ec87d36d0c441aa193765b960b5e2e","sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","userType":1,"time":"2019-10-04 21:23:55","account":"yunji123","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"1910d48708c360844db2be2bc11fd40f8a42","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:24:19","account":"yunji123","url":"19109a2bd5151ba04a44941fcf0b5802f224","levelIcon":""},{"msgType":1,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"context":"😅","msgId":"1910e7d60e45509b41db8eb4b02922f09df9","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:26:17","account":"yunji123","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"1910d511e974010a40479392b1639f6e74f3","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:26:33","account":"yunji123","url":"1910f2358ff91b4f4a4087c34b47a81005e4","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"19108cb3edacb41c42b7b5e10af3ed071974","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:28:56","account":"yunji123","url":"19104b8c247a3a53427cac72cdbaae1d539b","levelIcon":""},{"msgType":1,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"context":"😃","msgId":"1910da91eaaac70d4501a2049c09a86069ef","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:29:03","account":"yunji123","levelIcon":""},{"msgType":1,"sender":"e6c50a37e02845a99c4e3c8267285e04","nickName":"","privatePermission":false,"context":"😄","msgId":"1910c8d797e30a2a47fbaa7d6067e9a2d076","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdKHwZy.jpg","time":"2019-10-04 21:29:11","account":"yun123","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"19102ad9dc72f0af423cbfa9b17a83223fe5","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:29:27","account":"yunji123","url":"1910eb3485a7db8d474292a4b91f2a8cd627","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"1910f09c478eba36405db94b2268f915af0e","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:30:10","account":"yunji123","url":"1910fb3d361cd8804b4aab673c55cbd1221a","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"191040438bf70dc84db39e8c3ad816c9ec88","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 21:32:52","account":"yunji123","url":"1910b3eb49364eef402c835ebc6af522a1ba","levelIcon":""},{"msgType":3,"nickName":"","privatePermission":true,"count":"0","msgId":"191084addab772bf46edb0de010ae86e8041","levelName":"王者会员","remark":"恭喜发财，大吉大利！","avatar":"0","money":"3","sender":"99814d93b34d42d9a1aea819a8c75b3e","userType":4,"time":"2019-10-04 21:46:48","payId":"180d2817880e4c7f8c72ba2b38812510","account":"test1","levelIcon":""},{"msgType":1,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"context":"😆","msgId":"19100e36aeee91ed48639104fb8bcf29da69","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 22:32:10","account":"yunji123","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"1910bf1fb92d7f4646878eb053506d92cdc2","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 22:43:16","account":"yunji123","url":"19109df3df9b52984d47931f9abb6dfebbae","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"19100cc1746fca344997ba324c40ad53b9cd","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 22:44:24","account":"yunji123","url":"19109607356068aa48dc847e46453d472675","levelIcon":""},{"msgType":2,"sender":"1a5abbd4b2b844dfac4a30e3ac3a40e1","nickName":"","privatePermission":false,"msgId":"19102c98fbebd4c34ae8a1270b76298748f2","levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","time":"2019-10-04 22:49:38","account":"yunji123","url":"1910931a63f3e3a14c7494be92deb474a252","levelIcon":""},{"date":"2019-10-05","msgType":5,"sender":"99814d93b34d42d9a1aea819a8c75b3e","context":"{\"betInfo\":{\"lottery_amount\":\"2.0\",\"lottery_content\":\"08\",\"lottery_play\":\"猜冠军\",\"lottery_qihao\":\"771792\",\"lottery_type\":\"老北京赛车\",\"lottery_zhushu\":\"1\"},\"code\":\"R7008\",\"msgUUID\":\"aab78eeb-45ed-4f80-b119-b631fa89a962\",\"orders\":[{\"betMoney\":\"2.0\",\"orderId\":\"C19100500030\"}],\"roomId\":\"77214bf37cba4b8695a0f379218a6e95\",\"source\":\"app\",\"stationId\":\"yunt0Chat_2\",\"userId\":\"99814d93b34d42d9a1aea819a8c75b3e\"}","msgId":"107041c740468d487f840b1c1dc08d48a3","levelName":"后台管理员","remark":"","userType":2,"time":"2019-10-05 22:03:45","account":"99814d93b34d42d9a1aea819a8c75b3e"}]
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private String status;
    private List<SourceBean> source;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SourceBean> getSource() {
        return source;
    }

    public void setSource(List<SourceBean> source) {
        this.source = source;
    }

    public static class SourceBean implements Parcelable{
        /**
         * date : 2019-10-04
         * msgType : 2
         * nickName :
         * privatePermission : false
         * msgId : 19106a7ab52cd2134b4fb9961d36b34c35e9
         * levelName : 王者会员
         * avatar : https://yj8.me/img/h3eM/iozdFlgtp.jpg
         * url : 191029ec87d36d0c441aa193765b960b5e2e
         * sender : 1a5abbd4b2b844dfac4a30e3ac3a40e1
         * userType : 1
         * time : 2019-10-04 21:23:55
         * account : yunji123
         * levelIcon :
         * context : 😅
         * count : 0
         * remark : 恭喜发财，大吉大利！
         * money : 3
         * payId : 180d2817880e4c7f8c72ba2b38812510
         *
         */

        private String date;
        private int msgType;
        private String nativeNickName;
        private boolean privatePermission;
        private String msgId;
        private String levelName;
        private String avatar;
        private String url;
        private String sender;
        // 1:普通用户  2:后台管理员  3:机器人 4:前台管理员
        private int userType;

        private String time;
        private String account;
        private String levelIcon;
        private String context;
        private String count;
        private String remark;
        private String money;
        private String payId;
        private String nativeAccount;
        private String nativeAvatar;
        private String nativeContent;
        private String text;
        private String lotteryName;
        private int isPlanUser;
        private ChatPersonDataBean.SourceBean.WinLostBean winOrLost;
        private int stopTalkType;


        public int getStopTalkType() {
            return stopTalkType;
        }

        public void setStopTalkType(int stopTalkType) {
            this.stopTalkType = stopTalkType;
        }

        public ChatPersonDataBean.SourceBean.WinLostBean getWinOrLost() {
            return winOrLost;
        }

        public void setWinOrLost(ChatPersonDataBean.SourceBean.WinLostBean winOrLost) {
            this.winOrLost = winOrLost;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getNickName() {
            return nativeNickName;
        }

        public void setNickName(String nickName) {
            this.nativeNickName = nickName;
        }

        public boolean isPrivatePermission() {
            return privatePermission;
        }

        public void setPrivatePermission(boolean privatePermission) {
            this.privatePermission = privatePermission;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAccount() {
            return account;
        }

        public String getNativeNickName() {
            return nativeNickName;
        }

        public void setNativeNickName(String nativeNickName) {
            this.nativeNickName = nativeNickName;
        }

        public int getIsPlanUser() {
            return isPlanUser;
        }

        public void setIsPlanUser(int isPlanUser) {
            this.isPlanUser = isPlanUser;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getLevelIcon() {
            return levelIcon;
        }

        public void setLevelIcon(String levelIcon) {
            this.levelIcon = levelIcon;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String getNativeAccount() {
            return nativeAccount;
        }

        public void setNativeAccount(String nativeAccount) {
            this.nativeAccount = nativeAccount;
        }

        public String getNativeAvatar() {
            return nativeAvatar;
        }

        public void setNativeAvatar(String nativeAvatar) {
            this.nativeAvatar = nativeAvatar;
        }

        public String getNativeContent() {
            return nativeContent;
        }

        public void setNativeContent(String nativeContent) {
            this.nativeContent = nativeContent;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLotteryName() {
            return lotteryName;
        }

        public void setLotteryName(String lotteryName) {
            this.lotteryName = lotteryName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeInt(this.msgType);
            dest.writeString(this.nativeNickName);
            dest.writeByte(this.privatePermission ? (byte) 1 : (byte) 0);
            dest.writeString(this.msgId);
            dest.writeString(this.levelName);
            dest.writeString(this.avatar);
            dest.writeString(this.url);
            dest.writeString(this.sender);
            dest.writeInt(this.userType);
            dest.writeString(this.time);
            dest.writeString(this.account);
            dest.writeString(this.levelIcon);
            dest.writeString(this.context);
            dest.writeString(this.count);
            dest.writeString(this.remark);
            dest.writeString(this.money);
            dest.writeString(this.payId);
            dest.writeString(this.nativeAccount);
            dest.writeString(this.nativeAvatar);
            dest.writeString(this.nativeContent);
            dest.writeString(this.text);
            dest.writeString(this.lotteryName);
            dest.writeInt(this.isPlanUser);
            dest.writeParcelable(this.winOrLost, flags);
        }

        public SourceBean() {
        }

        protected SourceBean(Parcel in) {
            this.date = in.readString();
            this.msgType = in.readInt();
            this.nativeNickName = in.readString();
            this.privatePermission = in.readByte() != 0;
            this.msgId = in.readString();
            this.levelName = in.readString();
            this.avatar = in.readString();
            this.url = in.readString();
            this.sender = in.readString();
            this.userType = in.readInt();
            this.time = in.readString();
            this.account = in.readString();
            this.levelIcon = in.readString();
            this.context = in.readString();
            this.count = in.readString();
            this.remark = in.readString();
            this.money = in.readString();
            this.payId = in.readString();
            this.nativeAccount = in.readString();
            this.nativeAvatar = in.readString();
            this.nativeContent = in.readString();
            this.text = in.readString();
            this.lotteryName = in.readString();
            this.isPlanUser = in.readInt();
            this.winOrLost = in.readParcelable(ChatPersonDataBean.SourceBean.WinLostBean.class.getClassLoader());
        }

        public static final Creator<SourceBean> CREATOR = new Creator<SourceBean>() {
            @Override
            public SourceBean createFromParcel(Parcel source) {
                return new SourceBean(source);
            }

            @Override
            public SourceBean[] newArray(int size) {
                return new SourceBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg);
        dest.writeString(this.code);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.status);
        dest.writeTypedList(this.source);
    }

    public ChatHistoryMessageBean() {
    }

    protected ChatHistoryMessageBean(Parcel in) {
        this.msg = in.readString();
        this.code = in.readString();
        this.success = in.readByte() != 0;
        this.status = in.readString();
        this.source = in.createTypedArrayList(SourceBean.CREATOR);
    }

    public static final Creator<ChatHistoryMessageBean> CREATOR = new Creator<ChatHistoryMessageBean>() {
        @Override
        public ChatHistoryMessageBean createFromParcel(Parcel source) {
            return new ChatHistoryMessageBean(source);
        }

        @Override
        public ChatHistoryMessageBean[] newArray(int size) {
            return new ChatHistoryMessageBean[size];
        }
    };
}
