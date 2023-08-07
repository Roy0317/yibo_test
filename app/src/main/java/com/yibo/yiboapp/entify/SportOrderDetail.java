package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/3.
 */

public class SportOrderDetail {
    /**
     1全输 2输一半 3平 4赢一半 5全赢
     */
    public final static long RESULT_STATUS_LOST = 1;
    public final static long RESULT_STATUS_LOST_HALF = 2;
    public final static long RESULT_STATUS_DRAW = 3;
    public final static long RESULT_STATUS_WIN_HALF = 4;
    public final static long RESULT_STATUS_WIN = 5;


    long id;

    long sportType;
    /**
     1:滚球   2:今日  3:早盘
     */
    private Long gameTimeType;

    /**
     1:独赢 ＆ 让球 ＆ 大小 & 单 / 双
     2：波胆  上半场
     3：波胆  全场
     4：总入球数
     5：全场半场输赢
     6：混合过关      （参照枚举类DataType）
     */
    private Long dataType;

    /**
     1:全场独赢
     2:全场大小球
     3:全场让球盘
     4：全场得分单双
     5：全场波胆
     6：半场波胆
     7：总入球
     8：半场 全场 胜负关系
     9：主队全场分数大小
     10：客队全场分数大小
     */
    private Long betType;

    /**
     1：主胜
     2：主负
     3：平
     4：总得分单
     5：总得分双
     6：总得分大于
     7：总得分小于
     8：让球主队赢
     9：让球主队输
     10：波胆具体比分
     11:波胆其他比分
     12：总入球数 具体分数
     13：全场 半场胜负关系
     */
    private Long betItemType;

    private String project;

    /**
     滚球时使用，主队分数
     */
    private Long scoreH;

    /**
     滚球时使用，客队分数
     */
    private Long scoreC;

    /**
     主队
     */
    private String homeTeam;

    /**
     客队名称
     */
    private String guestTeam;

    /**
     联赛
     */
    private String league;

    /**
     H:亚洲盘  I:印尼盘 E:欧洲盘 M:马来西亚盘
     */
    private String plate;

    /**
     用于前端显示
     */
    private String remark;

    /**
     赔率
     */
    private float odds;

    /**
     1 待确认
     2：已确认
     3：已取消 (滚球系统自动取消)
     4: 手动取消
     */
    private Long bettingStatus;



    /**
     1：未结算 2：已结算
     */
    private Long balance;

    /**
     下注时间
     */
    private long bettingDate;

    /**
     投注结果
     */
    private float bettingResult;

    /**
     结算时间
     */
    private long accountDatetime;

    /**
     * 皇冠赛事ID
     */
    private Long gid;

    /**
     1全输 2输一半 3平 4赢一半 5全赢
     */
    private Long resultStatus;

    private Long stationId;

    private Long memberId;

    /**
     * 注单编码
     */
    private String bettingCode;

    /**
     * 投注金额
     */
    private float bettingMoney;

    /**
     * 注单状态说明，取消注单时写入取消原因
     */
    private String statusRemark;

    /**
     *  1:单注  2：混合过关 主单  3：混合过关子单
     */
    private Long mix;

    private String memberName;

    private String stationName;

    /**
     * 类别名称
     */
    private String typeNames;


    /**
     * 赛果，结算的时候回填  只用于页面显示使用
     */
    private String result;

    /**
     * 会员返水状态 （1，还未返水 2，已经返水,还未到账 3，返水已经回滚 ,4 反水已经到账 ）多级表示返点(1，还未返点 2，已经返点 3，返点已经回滚)
     */
    private Integer rollBackStatus;

    private float rollBackMoney;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getGameTimeType() {
        return gameTimeType;
    }

    public void setGameTimeType(Long gameTimeType) {
        this.gameTimeType = gameTimeType;
    }

    public Long getDataType() {
        return dataType;
    }

    public void setDataType(Long dataType) {
        this.dataType = dataType;
    }

    public Long getBetType() {
        return betType;
    }

    public void setBetType(Long betType) {
        this.betType = betType;
    }

    public Long getBetItemType() {
        return betItemType;
    }

    public void setBetItemType(Long betItemType) {
        this.betItemType = betItemType;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Long getScoreH() {
        return scoreH;
    }

    public void setScoreH(Long scoreH) {
        this.scoreH = scoreH;
    }

    public Long getScoreC() {
        return scoreC;
    }

    public void setScoreC(Long scoreC) {
        this.scoreC = scoreC;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getOdds() {
        return odds;
    }

    public void setOdds(float odds) {
        this.odds = odds;
    }

    public Long getBettingStatus() {
        return bettingStatus;
    }

    public void setBettingStatus(Long bettingStatus) {
        this.bettingStatus = bettingStatus;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public long getBettingDate() {
        return bettingDate;
    }

    public void setBettingDate(long bettingDate) {
        this.bettingDate = bettingDate;
    }

    public float getBettingResult() {
        return bettingResult;
    }

    public void setBettingResult(float bettingResult) {
        this.bettingResult = bettingResult;
    }

    public long getAccountDatetime() {
        return accountDatetime;
    }

    public void setAccountDatetime(long accountDatetime) {
        this.accountDatetime = accountDatetime;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Long resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getBettingCode() {
        return bettingCode;
    }

    public void setBettingCode(String bettingCode) {
        this.bettingCode = bettingCode;
    }

    public float getBettingMoney() {
        return bettingMoney;
    }

    public void setBettingMoney(float bettingMoney) {
        this.bettingMoney = bettingMoney;
    }

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    public Long getMix() {
        return mix;
    }

    public void setMix(Long mix) {
        this.mix = mix;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(String typeNames) {
        this.typeNames = typeNames;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getRollBackStatus() {
        return rollBackStatus;
    }

    public void setRollBackStatus(Integer rollBackStatus) {
        this.rollBackStatus = rollBackStatus;
    }

    public float getRollBackMoney() {
        return rollBackMoney;
    }

    public void setRollBackMoney(float rollBackMoney) {
        this.rollBackMoney = rollBackMoney;
    }

    public long getSportType() {
        return sportType;
    }

    public void setSportType(long sportType) {
        this.sportType = sportType;
    }
}
