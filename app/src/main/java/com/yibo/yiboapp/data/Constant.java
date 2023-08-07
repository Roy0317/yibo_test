package com.yibo.yiboapp.data;

@SuppressWarnings({"unchecked", "hiding"})
public class Constant {


    public static final int PEILV_MODE = 0;
    public static final int MONEY_MODE = 1;

    public static final int BALLON_VIEW = 0;//小球
    public static final int LETTER_VIEW = 1;//全大小单双清
    public static final int WEISHU_VIEW = 2;//位数

    public static final int YUAN_MODE = 100;//元
    public static final int JIAO_MODE = 110;//元角
    public static final int FEN_MODE = 111;//元角分

    /**
     * 彩票版
     */
    public final static int VERSION_1 = 1;// 官方版本
    public final static int VERSION_2 = 2;// 信用版本

    public static final String TUCHU_LOTTERY = "v1";//突出彩票
    public static final String TUCHU_ZHENREN = "v2";//突出真人
    public static final String TUCHU_SPORT = "v3";//突出体育
    public static final String SELECT_TABS = "v4";//选项卡
    public static final String TUCHU_QIPAI = "v5";//突出棋牌

    /**
     * 彩票版本 1=第一版，2=第二版, 3=第三版,4=第四版,5=第五版，6=第六版
     */
    public static final int lottery_identify_V1 = 1;
    public static final int lottery_identify_V2 = 2;
    public static final int lottery_identify_V3 = 3;
    public static final int lottery_identify_V4 = 4;
    public static final int lottery_identify_V5 = 5;
    public static final int lottery_identify_V6 = 6;

    //主界面模版风格id
    public static final int OLD_CLASSIC_STYLE = 1;
    public static final int EASY_LIGHT_APP_STYLE1 = 2;
    public static final int E15_APP_STYLE2 = 3;
    public static final int NEW_APP_STYLE3 = 4;
    public static final int NEW_APP_STYLE4 = 5;
    public static final int NEW_APP_STYLE6 = 6;
    public static final int NEW_APP_STYLE7 = 7;
    public static final int NEW_APP_STYLE8 = 8;
    public static final int NEW_APP_STYLE9 = 9;
    public static final int NEW_APP_STYLE10 = 10;

    /**
     * 平台试玩账号
     */
    public static final int ACCOUNT_PLATFORM_TEST_GUEST = 6;
    /**
     * 会员平台账号
     */
    public static final int ACCOUNT_PLATFORM_MEMBER = 1;

    public static final int CAIPIAO_RECORD_STATUS = 0;
    public static final int LHC_RECORD_STATUS = 1;
    public static final int SPORTS_RECORD_STATUS = 2;
    public static final int REAL_PERSON_RECORD_STATUS = 3;
    public static final int ELECTRIC_GAME_RECORD_STATUS = 4;
    public static final int SBSPORTS_RECORD_STATUS = 5;
    public static final int MYBIGPAN_RECORD_STATUS = 6;
    public static final int OLD_SPORTS_RECORD_STATUS = 7;
    public static final int CHESS_GAME_RECORD_STATUS = 9;
    public static final int BIG_PAN_RECORD_STATUS = 10;
    public static final int THIRLD_SPORT_RECORD = 11;
    public static final int HUNTER_RECORD = 12;
    public static final int ESPORT_RECORD = 13;

    public static final int WAIT_KAIJIAN_STATUS = 1;
    public static final int ALREADY_WIN_STATUS = 2;
    public static final int NOT_WIN_STATUS = 3;
    public static final int CANCEL_ORDER_STATUS = 4;
    public static final int ROLLBACK_SUCCESS_STATUS = 5;
    public static final int ROLLBACK_FAIL_STATUS = 6;
    public static final int EXCEPTION_KAIJIAN_STATUS = 7;
    public static final int ACCOUNT_CHANGE_RECORD_STATUS = 8;
    public static final int ACCOUNT_CO_BUY_STATUS = 9; //合买失效
    public static final int ACCOUNT_PEACE_WIN_STATUS = 10; //和局中奖

    //充值或提款記錄狀態
    // 未处理
    public static long STATUS_UNTREATED = 1l;
    // 处理成功
    public static long STATUS_SUCCESS = 2l;
    // 处理失败
    public static long STATUS_FAILED = 3l;
    // 已取消
    public static long STATUS_CANCELED = 4l;
    // 已过期
    public static long STATUS_EXPIRED = 5l;
    public static long ROOL_BACK_RECHARGE = 6l;
    public static long ROOL_BACK = 7l;

    public static final String HIGHT_LIGHT_CAIPIAO = "v1";
    public static final String HIGHT_LIGHT_ZHENREN = "v2";
    public static final String HIGHT_LIGHT_SPORT = "v3";
    public static final String HIGHT_LIGHT_GAME = "v4";

    public static final String YCP_CODE = "ycp";
    public static final String TT_CODE = "tt";

    //体育-0，真人-1，电子-2代号
    public static final int SPORT_MODULE_CODE = 0;
    public static final int REAL_MODULE_CODE = 1;
    public static final int GAME_MODULE_CODE = 2;

    //体育数据头部KEY字段
    public static final String gid = "gid";
    public static final String home = "home";
    public static final String guest = "guest";
    public static final String homeCode = "homeCode";
    public static final String guestCode = "guestCode";
    public static final String league = "league";
    public static final String openTime = "openTime";
    public static final String live = "live";
    public static final String matchId = "matchId";
    public static final String ior_MH = "ior_MH";
    public static final String ior_MC = "ior_MC";
    public static final String ior_MN = "ior_MN";
    public static final String ior_RH = "ior_RH";
    public static final String CON_RH = "CON_RH";
    public static final String CON_RC = "CON_RC";
    public static final String ior_OUH = "ior_OUH";
    public static final String CON_OUH = "CON_OUH";
    public static final String ior_OUC = "ior_OUC";
    public static final String CON_OUC = "CON_OUC";
    public static final String ior_EOO = "ior_EOO";
    public static final String ior_EOE = "ior_EOE";
    public static final String ior_HMH = "ior_HMH";
    public static final String ior_HMC = "ior_HMC";
    public static final String ior_HMN = "ior_HMN";
    public static final String ior_HRH = "ior_HRH";
    public static final String CON_HRH = "CON_HRH";
    public static final String ior_HRC = "ior_HRC";
    public static final String CON_HRC = "CON_HRC";
    public static final String ior_HOUH = "ior_HOUH";
    public static final String CON_HOUH = "CON_HOUH";
    public static final String ior_HOUC = "ior_HOUC";
    public static final String CON_HOUC = "CON_HOUC";
    public static final String ior_RC = "ior_RC";
    public static final String scoreC = "scoreC";
    public static final String scoreH = "scoreH";


    public static final String FT_MN = "MN";
    public static final String FT_TI = "TI";
    public static final String FT_BC = "BC";
    public static final String FT_HF = "HF";
    public static final String FT_MX = "MX";
    public static final String BK_MN = "MN";
    public static final String BK_MX = "MX";

    public static final String RB_TYPE = "RB";
    public static final String TD_TYPE = "TD";
    public static final String FT_TYPE = "FT";

    //体育投注记录的记录类型
    //所有
    public static final int RECORD_TYPE_ALL = 1;
    //会员赢钱
    public static final int RECORD_TYPE_WIN = 2;
    //未开奖
    public static final int RECORD_TYPE_UNBALANCE = 3;
    //未成功
    public static final int RECORD_TYPE_REJECT = 4;

    public static final String PLATE_ASIA = "H";
    public static final String PLATE_INDIA = "I";
    public static final String PLATE_UER = "E";
    public static final String PLATE_MLXY = "M";

    public static final int ALL_BALL_CONSTANT = 0;
    public static final int FOOTBALL_CONSTANT = 1;
    public static final int BASCKETBALL_CONSTANT = 2;

    /**
     * 1全输 2输一半 3平 4赢一半 5全赢
     */
    public final static long BALANCE_ALL_LOST = 1;
    public final static long BALANCE_HALF_LOST = 2;
    public final static long BALANCE_DRAW = 3;
    public final static long BALANCE_HALF_WIN = 4;
    public final static long BALANCE_ALL_WIN = 5;

    //体育结算状态
    //未结算
    public final static long BALANCE_UNDO = 1;
    //系统结算
    public final static long BALANCE_DONE = 2;
    //结算失败
    public final static long BALANCE_ERROR = 3;
    //比赛腰斩
    public final static long BALANCE_CUT_GAME = 4;
    //租户 手动结算
    public final static long BALANCE_AGENT_HAND_DONE = 5;
    //比分网结算
    public final static long BALANCE_BFW_DONE = 6;

    public static final String[] scanpanWithRedirectFilterArr = new String[]{"sanyingpay"};

    public static final String[] scanpayFilterArr = new String[]{"xianfengjuhe", "lebaifu", "sanyingpay", "huiyinpay", "smartCloud", "goldpayment", "sptpay", "mibaozf", "lingdianpay",
            "szhoupay", "tongdapay", "kuaifu858pay", "huijupay", "zhihepay", "yoyupay", "jnspay", "paypal", "xifupay", "fiveonepay", "manbapay", "debaozhifu",
            "mifupay", "easypay168", "zhuogepay", "liyingpay", "quannengpay", "shangyinxinX", "yingyupay", "shenfubaopay", "mibeipay", "xunyinpay", "bldpay",
            "sevensevenpay", "saaspay", "tzpay", "fiveonebftpay", "malljls", "suhuibao", "cnpay8", "aikulipay", "bcfapi", "caifubao", "duolabao", "ifeepay",
            "aabill", "one2pay", "xiipay", "ytbao", "xiih5pay", "jinanfu", "tongbaopay", "yuanbaozhifu", "magopay", "duodebao", "zhifuhui", "xunhuibao",
            "xunfutong", "danbaopay", "easypay", "qifupay", "fulapay", "shangyinxin", "juhepay", "tonghuika", "kcpay", "baifupay", "shunfupay",
            "aimisenpay", "xinmapay", "woozf", "tianchuangpay", "juhepufa", "ludepay", "hebaopay", "zaixianbao", "dpayhk", "zeshengpay", "qingyifu",
            "changchengzhifu", "tianjinchuangxingou", "ak47pay", "ulinepay", "ufuzhifu", "juheminsheng", "yunmafuPay", "juherongzhifu", "quanyinpay", "heshengpay",
            "dingfengpay", "eshidai", "shuangchengpay", "ztbaopay", "gmstone", "wefupay", "xunjietong", "yuypay", "spdbweb", "dcpay", "shenhx", "cmbcpos", "mmpay",
            "foupang", "xjpay", "yizhibank", "xingfupay", "bifupay", "sf532", "caimao9", "atrustpay", "zdbbill", "jiamanpay", "lianlianspc", "ziyoupay",
            "bingshanpay", "juhebosspay", "huihepay", "jinyangpay", "shangxinpay", "congfu", "bitepay"}; // 扫码支付
    public static final String[] shunpayFilterArr = new String[]{"pay511", "jbfu", "huitianfu", "mibaozf", "yunweipay", "shangyizhifu", "kuaifu858pay", "chengfupay", "zhhky", "zhihepay",
            "yichigo", "zbpay", "xifupay", "kuaiyunpay", "qianfupay", "debaozhifu", "mifupay", "newepay", "zhuogepay", "shunxinfupay", "yingyupay", "jufuyun",
            "oubaopay", "easypay168", "mibeipay", "kaizepay", "ruyipay", "xunyinpay", "saaspay", "yafupay", "malljls", "allinepay", "payeze", "aikulipay",
            "oneeightsevenpay", "threetwopay", "ifeepay", "kuaidapay", "pay779", "haolianpay", "hlbpay", "one2pay", "scqydapay", "orangebank", "magopay",
            "yinbao", "xbeipay", "yompay", "kdpay", "jinanfu", "ekapay", "xunhuibao", "xunfutong", "r1pay", "caihongpay", "fuqpay", "renxinpay", "qifupay",
            "helibao", "shangyinxin", "qixunpay", "xinyingpay", "tongbaopay", "kcpay", "xinhuipay", "nowtopay", "nixiaoka", "tonghuika", "teapay", "zhishuapay",
            "duobaopay", "ankuaipay", "baifupay", "shunfupay", "changjiepay", "shunbaopay", "jinhaizhe", "xinmapay", "woozf", "tianchuangpay", "ludepay",
            "hebaopay", "zaixianbao", "yikapay", "antopay", "aimisenpay", "dpayhk", "zeshengpay", "qingyifu", "changchengzhifu", "tianjinchuangxingou", "yompay20", "xunbaoshangwu", "guoshengtong", "baifutong", "yunxunzhifu", "yuanbaozhifu", "ufuzhifu", "gaotongzhifu", "luobopay", "juheminsheng", "yunmafuPay", "chengbaopay", "heshengpay", "dingfengpay", "okpay", "eshidai", "shuangchengpay", "rujin8", "ztbaopay", "likanpay", "gmstone", "wefupay", "pay591", "xunjietong", "yuypay", "spdbweb", "baolepay", "dcpay", "shenhx", "cmbcpos", "shkpay", "mmpay", "foupang", "xjpay", "yizhibank",
            "xingfupay", "shengtongkeji", "xingheyitong", "ruiyinfu", "eboopay", "fangtepay", "jizhipay", "heyifu", "yiaipay", "dingjian", "ytbao", "sixbpay",
            "ninebaopay", "atrustpay", "yunxunpay", "rcpay", "yuebaopay", "duodebao", "zhifuhui", "juhebosspay", "huihepay", "jinyangpay", "wufupay", "congfu"};
    // weixinFilterArr、alipayFilterArr、scanpayFilterArr的合集，
    // 且payType不为3（单微信）和4（单支付宝）则跳转到本地收银台
    public static final String[] straightFilterArr = new String[]{"jiandanfu", "xianfengjuheh5", "xianfengjuhe", "lebaifu", "sanyingh5pay", "huiyinh5pay",
            "smartCloudh5", "lingdianpay", "shangyizhifuh5", "szhoupay", "szhouh5pay", "tongdah5pay", "paypalh5", "jnsh5pay", "yoyuh5pay", "fiveoneh5pay",
            "manbapay", "xjh5pay", "atrusth5pay", "mifuh5pay", "timesdatah5", "easypay168h5", "aimisenh5", "easypayh5", "sevensevenh5pay", "boeingh5",
            "yingyuh5pay", "zaixianbaowap", "juhebossh5", "mibeih5", "ruyih5pay", "yibaotongh5pay", "goldh5payment", "bldh5pay", "cnh5pay8", "saash5pay",
            "wh5zhifu", "zdbh5bill", "caimao9h5", "aikulipay", "bcfh5api", "shunfuh5pay", "caifubao", "duolabao", "ztbaoh5pay", "xinmah5pay", "magoh5pay",
            "aimisenpay", "juherongzhifu", "dcpay", "thirdpay", "xiipay", "cc8h5pay", "quanyinpay"};
    public static final String[] wappayFilterArr = new String[]{"xianfengjuheh5", "sevensevenh5pay", "juhebossh5", "mibeih5", "shenfubaoh5", "aikulih5pay",
            "shunfuh5pay", "payh5779", "yuebaoh5pay", "haolianh5pay", "ztbaoh5pay", "hlbh5pay", "gaotongh5pay", "scqydah5pay", "magoh5pay", "xingfupaywap",
            "zeshengpaywap", "duodebaoh5", "zhifuhuih5", "thirdpay", "xiih5pay", "ninebaoh5pay", "yizhibankh5", "atrusth5pay", "cc8h5pay", "dinh5pay",
            "jiamanpay", "ludepayh5", "ziyoupay", "bingshanpay", "jinyangpayh5", "wufupayh5", "ruyipayh5"}; // 不支持网银的版本


    public static final int AG_INT = 1;
    public static final int MG_INT = 3;
    public static final int QT_INT = 4;
    public static final int ALLBET_INT = 5;
    public static final int PT_INT = 6;
    public static final int CQ9_INT = 24;
    public static final int BG_INT = 20;
    public static final int PG_INT = 35;
    public static final int BBIN_INT = 2;
    public static final int OG_INT = 7;
    public static final int DS_INT = 8;
    public static final int DG_INT = 21;
    public static final int RG_INT = 22;
    public static final int NT_INT = 23;
    public static final int EBET_INT = 25;
    public static final int LB_INT = 43;

    public static final int NB_TYPE = 9; //NB棋牌
    public static final int KY_TYPE = 11; //开元棋牌
    public static final int LEG_TYPE = 26; //乐游棋牌
    public static final int YG_TYPE = 27; //YG棋牌
    public static final int YB_TYPE = 34; //YB棋牌
    public static final int BS_TYPE = 29; //百胜棋牌
    /**
     * 默认每页加载数量
     */
    public static final int PAGE_SIZE = 20;
    /**
     * 默认的快捷金额
     */
    public static final String DEFAULT_FASE_MONEY = "10,20,50,100,200,500,1000,2000,5000";

    /**
     * 默认的彩种分类
     */
    public static final String DEFAULT_LOTTERY_TYPE = "时时彩,快三,PC蛋蛋,快乐十分,赛车,十一选五,福彩三D,六合彩,十分六合彩";

    /**
     * 默认分类
     */
    public static final String SSC = "时时彩";
    public static final String KS = "快三";
    public static final String PCDD = "PC蛋蛋";
    public static final String KLSF = "快乐十分";
    public static final String SC = "赛车";
    public static final String SYXW = "十一选五";
    public static final String FCSD = "福彩三D";
    public static final String LHC = "六合彩";
    public static final String SFLHC = "十分六合彩";

    /**
     * 默认的首页弹窗时间间隔
     */
    public static final String DEFAULT_MAIN_POPUP_SPACE_TIME = "默认,1分钟,5分钟,10分钟,30分钟,1小时";
    public static  int FAKE_COUNT = 0;

    public static final int YB_SOURCE = 1;


}
