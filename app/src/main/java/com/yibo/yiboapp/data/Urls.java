package com.yibo.yiboapp.data;

import android.content.Context;
import android.util.Log;

import com.snail.antifake.jni.EmulatorCheckService;
import com.snail.antifake.jni.EmulatorDetectUtil;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.utils.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import crazy_wrapper.Crazy.Utils.RequestUtils;

/**
 * Created by johnson on 2017/9/25.
 */

public class Urls {


    public static String BASE_URL = BuildConfig.domain_url;
    public static Integer APP_ROUTE_TYPE = 1;
    public static String BASE_HOST_URL = BuildConfig.host_domain;


    public static final String FIX_NATIVE_BASE_URL_1 = "https://api.appdns13.com";
    public static final String FIX_NATIVE_BASE_URL_2 = "https://api.appdns14.com"; //备用
//    public static final String FIX_NATIVE_BASE_URL_3 = "https://dns.appdns1.com:59789";
//    public static final String FIX_NATIVE_BASE_URL_4 = "https://dns.appdns2.com:59789";
//    public static final String FIX_NATIVE_BASE_URL_5 = "https://dns.appdns3.com:59789";
//    public static final String FIX_NATIVE_BASE_URL_6 = "https://dns.appdns4.com:59789";

    public static final String GET_REAL_DOMAIN_URL = "/domain/getRealDomainUrl";//获取真正的域名,图片空间的接口
    public static final String GET_APP_ROUTE_URL = "/route/getRoute";//获取后台动态负载分配的一条线路
    public static final String GET_APP_ROUTE_URL_LIST = "/route/getAllRoutes";//获取线路列表
    public static final String GET_LAUNCHER_IMG_URL = "/launcher/getLauncher";//获取qi dong tu,图片空间的接口

//    public static final String LOGIN_URL = "/native/login.do";//登录
//    public static final String LOGIN_NEWV2_URL = "/native/loginNewV2.do";//登录
    public static final String LOGIN_NEWV2_URL = "/native/vipLogin.do";//登录
//    public static final String SYSTEM_TIME_URL = "/native/getServerTime.do";//获取系统时间
    public static final String REG_CONFIG_URL = "/native/regconfig.do";//注册配置信息
    public static final String REG_CONFIG_URV2L = "/native/regconfigV2.do";//注册配置信息
    public static final String REGISTER_URL = "/native/register.do";//会员注册
    public static final String REGISTER_VCODE_IMAGE_URL = "/native/regVerifycode.do";//获取register验证码图片
    public static final String LOGIN_VCODE_IMAGE_URL = "/native/loginCode.do";//获取login验证码图片
    public static final String NOTICE_URL = "/native/new_notice.do";//获取公告消息
    public static final String NOTICE_URL_V2 = "/native/new_notice_v2.do";//获取公告消息新版
    public static final String MEMINFO_URL = "/native/meminfo.do";//获取帐号相关信息
    public static final String REG_GUEST_URL = "/native/reg_guest.do";//试玩帐号注册
    public static final String SYS_CONFIG_URL = "/native/config.do";//系統配置
    public static final String ACTIVE_BADGE_URL = "/native/active_badges.do";//优惠活动角标数
    public static final String UNREAD_MSG_COUNT_URL = "/native/getMsgCount.do";//获取未读消息数
    public static final String ACQUIRE_GAMES_DATA = "/native/get_game_datas.do";//獲取游戲數據
    public static final String ACQUIRE_GAMES_DATAV2 = "/native/get_game_datas_v2.do";//獲取游戲數據v2
    public static final String LUNBO_URL = "/native/lunbo.do";//获取轮播图
    public static final String LOTTERYS_URL = "/native/getLoctterys.do";//获取彩种信息
    public static final String ALL_GAME_DATA_URL = "/native/all_games.do";//获取彩种等所有数据
    public static final String ALL_GAME_DATA_BACKUP = "/native/api/app/findAllGames.do";//获取彩种等所有数据并备份到local file
    public static final String GAME_VERSION_URL = "/native/gameVersion.do";//获取彩票版本
    public static final String GAME_PLAYS_URL = "/native/getGamePlays.do";//获取某彩种对应的玩法列表
    public static final String LOTTERY_LAST_RESULT_URL = "/native/lastResult.do";//获取彩票最后一期的开奖结果
    public static final String LOTTERY_LAST_RESULT_V2_URL = "/mobile/v3/lotteryDown.do";//获取彩票最后一期的开奖结果v2版
    public static final String LOTTERY_OFFICIAL_LAST_RESULT_URL = "/mobile/v3/last_data.do";//获取彩票最后一期的开奖结果（针对官方版在没有获取到开奖号码时定时获取用的）
    public static final String WIN_LOST_URL = "/native/win_lost.do";//今日输赢
    public static final String LOTTERY_COUNTDOWN_URL = "/native/getCountDown.do";//获取彩票最后一期的倒计时数
    public static final String LOTTERY_COUNTDOWNV2_URL = "/native/getCountDownV2.do";//获取彩票最后一期的倒计时数v2
    public static final String ALL_LOTTERYS_COUNTDOWN_URL = "/native/getLotterysCountDown.do";//获取所有彩票倒计时数
    public static final String LOTTERY_RECORD_URL = "/native/getBcLotteryRecords.do";//获取彩票投注记录

    public static final String LOTTERY_RECORD_DETAIL_URL = "/native/getOrderDetail.do";//获取彩票投注记录详情
    public static final String LOTTERY_RECORD_DETAILV2_URL = "/native/getOrderV2Detail.do";//获取彩票投注记录详情v2
    public static final String LOTTERY_CANCEL_ORDER_URL = "/native/cancelOrder.do";//撤销下注的订单
    public static final String ACCOUNT_CHANGE_RECORD_URL = "/native/accountChangeRecord.do";//帐变记录
    public static final String ACCOUNT_CHANGE_RECORD_URL_V2 = "/native/accountChangeRecordV2.do";//帐变记录
    public static final String MY_BIGPAN_RECORD_URL = "/native/turnlateRecord.do";//我的转盘中奖记录
    public static final String GET_QIHAO_URL = "/native/getQihao.do";//获取当前期号
    public static final String DO_BETS_URL = "/native/doBets.do";
    public static final String TOKEN_BETS_URL = "/native/bet_token.do";//下注验证令牌口令//下注
    public static final String ZUIHAO_QISHU_URL = "/native/getQiHaoForChase.do";//获取追号期数
    public static final String ACQUIRE_CURRENT_QIHAO_URL = "/native/getQihao.do";//获取期数
    public static final String OPEN_RESULTS_URL = "/native/open_results.do";//获取开奖公告
    public static final String OPEN_RESULT_DETAIL_URL = "/native/open_result_detail.do";//获取开奖公告详情
    public static final String OPEN_RESULT_DETAIL_URL_V2 = "/native/open_result_detail_v2.do";//获取开奖公告详情
    public static final String PICK_MONEY_DATA_URL = "/native/pick_money_data.do";//获取提款的配置及帐号数据
    public static final String PICK_MONEY_DATA_URL_V2 = "/native/pick_money_data_v2.do";//获取提款的配置及帐号数据
    public static final String PICK_MONEY_DATA_NEW_URL = "/native/pick_money_data_new.do";//获取提款的配置及帐号数据
    public static final String POST_PICK_MONEY_URL = "/native/post_pick_money.do";//提交提款请求
    public static final String POST_PICK_MONEY_NEW_URL = "/native/post_pick_money_new.do";//提交提款请求
    public static final String CHECK_PICK_MONEY_URL = "/native/check_account_security.do";//检查提款帐户请求
    public static final String POST_BANK_DATA_URL = "/native/post_bank_data.do";//提交银行信息设置
    public static final String COMPLETE_BANK_DATA_URL = "/center/banktrans/draw/cmitbkinfo.do";//完善银行卡信息
    public static final String UPDATE_BANK_DATA_URL = "/center/banktrans/draw/updbkinfo.do";//修改银行卡信息
    public static final String POST_SET_BANK_PWD_URL = "/native/set_receipt_pwd.do";//提交设置的提款密码
    public static final String GET_PAY_METHODS_URL = "/native/pay_methods.do";//获取充值方式
    public static final String SUBMIT_PAY_URL = "/native/submit_pay.do";//提交充值
    public static final String CHARGE_DRAW_RECORD_URL = "/native/money_records.do";//获取充值提款记录

    public static final String FETCH_MULTI_ACCOUNT_URL = "/center/member/meminfo/multiCardInfo.do";//取得多银行卡资料(get) (新接口)
    public static final String SET_PRIMARY_ACCOUNT_URL = "/center/member/meminfo/setPrimaryAccount.do";//设定主要多银行卡(post) (新接口)
    public static final String POST_NEW_USDT_ACCOUNT_URL = "/center/banktrans/draw/cmitusdtinfo.do";//新增USDT账户   (post) (异动逻辑)
    public static final String POST_NEW_BANK_ACCOUNT_URL = "/center/banktrans/draw/cmitbkinfo.do";//新增银行卡     (post) (异动逻辑)
    public static final String POST_NEW_ALIPAY_ACCOUNT_URL = "/center/banktrans/draw/cmitalipayinfo.do";//新增支付宝     (post) (异动逻辑)
    public static final String POST_NEW_GOPAY_ACCOUNT_URL = "/center/banktrans/draw/cmitgopayinfo.do"; //新增gopay
    public static final String POST_NEW_OKPAY_ACCOUNT_URL = "/center/banktrans/draw/cmitokpayinfo.do"; //新增okpay
    public static final String POST_NEW_TOPAY_ACCOUNT_URL = "/center/banktrans/draw/cmittopayinfo.do"; //新增TOpay
    public static final String POST_NEW_VPAY_ACCOUNT_URL = "/center/banktrans/draw/cmitvpayinfo.do"; //新增vpay

    public static final String POST_WITHDRAW_REQUEST_URL = "/center/banktrans/draw/drawcommit.do";//送出提款资料   (post) (异动逻辑)

    public static final String GET_PVODDS_URL = "/native/getOdds.do";//获取玩法对应的赔率
    public static final String get_server_bettime_for_lhc = "/native/get_server_bettime_for_lhc.do";
    public static final String DO_PEILVBETS_URL = "/native/dolotV2Bet.do";//赔率版下注
    public static final String DO_SIX_MARK_URL = "/native/doSixMarkBet.do";//六合彩下注
    public static final String DO_SIX_MARK_URLV2 = "/native/doSixMarkBetV2.do";//六合彩下注V2
    public static final String GET_OTHER_PLAY_DATA = "/native/getDatasBesidesLotterys.do";//获取，电子，真人，游戏数据
    public static final String FORWARD_REAL_URL = "/native/forwardReal.do";//转向真人
    public static final String SPORT_DATA_URL = "/native/getSportsData.do";//获取体育赛事数据
    public static final String SPORT_BETS_URL = "/native/sportBet.do";//体育下单投注
    public static final String SPORT_VALID_BETS_URL = "/native/valiteOdds.do";//校验下单投注
    public static final String MESSAGE_LIST_URL = "/native/message_list.do";//站内信记录
    public static final String SET_READ_URL = "/native/read.do";//设为已读
    public static final String DELETE_MESSAGE_URL = "/native/message_delete.do";//删除
    public static final String SET_ACTIVE_READ_URL = "/native/read_active.do";//设优惠活动为已读
    public static final String MODIFY_LOGIN_PASS = "/native/modify_pass.do";//修改登录密码
    public static final String MODIFY_CASH_PASS = "/native/modify_cashpass.do";//修改提款密码
    public static final String SPORT_RECORDS = "/native/getSportV2Records.do";//新体育投注记录
    public static final String OLD_SPORT_RECORDS = "/native/getSportRecords.do";//旧体育投注记录
    public static final String SBSPORT_RECORDS = "/native/sb_record.do";//沙巴体育投注记录
    public static final String SPORT_RECORDS_DETAIL = "/native/getSportRecordsDetail.do";//体育投注记录详情
    public static final String SPORT_NEWS = "/native/getSportNews.do";//体育新闻列表
    public static final String LOGIN_OUT_URL = "/native/logout.do";//登出
    public static final String REAL_CONVERT_DATA_URL = "/native/getRealGameData.do";//额度转换真人数据
    public static final String VALID_RED_PACKET_URL = "/native/getValidRedPacket.do";//获取可用红包信息
    public static final String GRAB_RED_PACKET_URL = "/native/grabPacket.do";//抢红包
    public static final String RED_PACKET_RECORD_URL = "/native/redPacketRecord.do";//已抢红包记录
    public static final String FAKE_PACKET_DATAS = "/native/fake_package_data.do";//红包假数据
    public static final String RED_PACKET_RULE_URL = "/native/nativeRedRule.do";//红包规则
    public static final String ACCOUNT_URL = "/native/accountInfo.do";//帐号信息
    public static final String SIGN_URL = "/native/sign.do";//签到
    public static final String SIGN_URL_RULE = "/native/notice.do?code=17";//签到规则
    public static final String EXCHANGE_CONFIG_URL = "/native/exchangeConfig.do";//积分兑换的配置列表
    public static final String EXCHANGE_URL = "/native/exchange.do";//积分兑换
    public static final String CHECK_UPDATE_URL = "/native/updateVersion.do";//在线检测升级接口
    public static final String ACQUIRE_ACTIVES_URL = "/native/active_infos_v2.do";
    public static final String ACQUIRE_ACTIVES_URL_V2 = "/native/active_infos_v2.do";//优惠活动
    public static final String ACQURIE_NOTICE_POP_URL = "/native/getPopNotices.do";//获取公告弹窗内容

    public static final String BIG_WHEEL_AWARD_RECORD_URL = "/native/turnRecord.do";//大转盘中奖记录
    public static final String BIG_WHEEL_DATA_URL = "/native/bigwheel.do";//大转盘活动数据
    public static final String BIG_WHEEL_ACTION_URL = "/native/turnlateAward.do";//大转盘抽奖动作
    public static final String ONLINE_PAY_ACTION_URL = "/native/pay.do";//POST表单提交
    public static final String PAY_TRANSLATE_URL = "/native/pay_safari.do";//pay
    public static final String BBIN_TRANSLATE_URL = "/native/bbin_redirect.do";//bbin 网页内容提交跳转
    public static final String PAY_TRANSLATE_SPECIAL_URL = "/native/pay_special_safari.do";//pay
    public static final String PAY_METHODS_URL = "/native/sync_pay_methods.do";//pay methods
    public static final String SBSPORT_JUMP_URL = "/native/enter_sbsport.do";


    //第三方接口，非原生定义
    public static final String LOTTERY_RULES_URL = "/mobile/v3/lottery_rules.do";//玩法说明
    public static final String REAL_GAME_BALANCE_URL = "/rc4m/getBalance.do";//获取真人或电子的余额，
    public static final String SBSPORT_BALANCE_URL = "/center/sbTrans/getBalance.do";//获取沙巴余额，
    public static final String FEE_CONVERT_URL = "/rc4m/thirdRealTransMoney.do";//真人额度转换接口
    public static final String SBFEE_CONVERT_URL = "/center/sbTrans/thirdTrans.do";//沙巴额度转换接口
    public static final String REAL_EBET_URL = "/forwardEbet.do";//EBET真人
    public static final String REAL_AG_URL = "/forwardAg.do";//AG真人娱乐接口
    public static final String REAL_TT_URL = "/forwardTtLottery.do";//易彩票跳轉地址
    public static final String REAL_MG_URL = "/forwardMg.do";//MG真人娱乐接口
    public static final String REAL_BBIN_URL = "/forwardBbin.do";//BBIN真人娱乐接口
    public static final String REAL_AB_URL = "/forwardAb.do";//AB真人娱乐接口
    public static final String REAL_OG_URL = "/forwardOg.do";//OG真人娱乐接口
    public static final String REAL_BBIN_URL2 = "/forwardBbin2.do";//BBIN真人娱乐接口
    public static final String REAL_DS_URL = "/forwardDs.do";//ds真人娱乐接口
    public static final String GAME_BBIN_URL2 = "/forwardBbin2.do";//BBIN电子娱乐接口
    public static final String GAME_PT_URL = "/forwardPt.do";//pt电子游戏接口
    public static final String GAME_MG_URL = "/forwardMg.do";//mg电子游戏接口
    public static final String GAME_BG_URL = "/forwardBg.do";//bg电子游戏接口
    public static final String GAME_DG_URL = "/forwardDg.do";//dg电子游戏接口
    public static final String GAME_RG_URL = "/forwardRg.do";//Rg电子游戏接口
    public static final String GAME_NT_URL = "/forwardNt.do";//Rg电子游戏接口
    public static final String GAME_NB_URL = "/forwardNbChess.do";//nb电子游戏接口
    public static final String GAME_QT_URL = "/forwardQt.do";//qt电子游戏接口
    public static final String GAME_KYQP_URL = "/forwardKYChess.do";//開元棋牌电子游戏接口
    public static final String GAME_CQ9_URL = "/forwardCq9.do";//cq9电子游戏接口
    public static final String GAME_YG_URL = "/forwardYgChess.do";//yg chess
    public static final String REAL_BET_RECORD_URL = "/mobile/betRecord/getLiveBetRecord.do";//真人投注记录
    public static final String GAME_BET_RECORD_URL = "/mobile/betRecord/getEgameBetRecord.do";//电子投注记录
    public static final String CHESS_BET_RECORD_URL = "/center/record/betrecord/chessRecord.do";//棋牌投注记录
    public static final String ONLINE_PAY_URL = "/onlinepay/pay.do";// 在线支付接口
    public static final String PAY_QRCODE_URL = "/onlinepay/utils/getWecahtQrcode.do";// 获取支付二维码接口
    public static final String PAY_QRCODE_FROM_WEB_URL = "/native/qrcodePay.do";// 获取支付二维码接口--在后台处理数据
    public static final String LOG_SYSTEM_CRASH_LOG_URL = "https://api.yunlog8.com/upload/upload_crash_log";
    public static final String GET_LONG_LOONG_URL = "/lotteryV3/doQueue.do";//长龙数据

    public static final String HEADER_URL = "https://im.zk6.me/header/headerSave";
    public static final String ACQUIRE_HEADER_URL = "https://im.zk6.me/header/getHeader";

    //Ray加的接口
    public static final String GET_MEMBER_LIST = "/native/userlist.do";//获取用户列表
    public static final String TEAM_OVERVIEW_LIST = "/native/team_overview.do";//获取团队总览真人
    public static final String MY_RECOMMEND_URL = "/native/my_recommand.do"; //我的推荐接口
    public static final String RECOMMEND_ADDUSER_URL = "/native/recommend_adduser.do"; //我的推荐新增会员
    public static final String LOTTERY_RECORD_URL_V2 = "/native/getBcLotteryRecordsV2.do";//获取彩票投注记录（改版）
    public static final String PUSH_DATAS_V2 = "/native/push_datas_v2.do";//获取后台推送记录
    public static final String CHAT_ROOM_URL = "/native/chatroom_url.do";//聊天室链接
    public static final String INCOME_MONEY_URL = "/native/income.do";//余额生金接口
    public static final String INCOME_QUESTION_URL = "/mobile/v3/userbill/issue.do";//余额生金答疑
    public static final String INCOME_ORDER_URL = "/native/incomeList.do";//余额生金账单
    public static final String BIG_PAN_RECORD_LIST = "/native/turnlateList.do";//大转盘中奖记录
    public static final String API_NATIVE_SIGN_RECORD = "/native/signList.do";//签到记录
    public static final String ZHOUSHITU = "/lottery/trendChart/index.do";//走势图
    public static final String MEIRIJIAJIANG = "/mobile/v3/bonusPage.do";//每日加奖
    public static final String ZHOUZHOUZHUANYUN = "/mobile/v3/deficitPage.do";//周周转运
    public static final String RECORD_JIAJIANG = "/native/nativeRecordBonus.do";//每日加奖记录
    public static final String RECORD_ZHUANYUN = "/native/nativeRecordDeficit.do";//周周转运记录
    public static final String BONUS_JIAJIANG = "/native/bonusPageData.do";//每日加奖数据接口
    public static final String LINGQU_JIAJIANG = "/native/nativeReceiveBonus.do";//每日加奖领取接口
    public static final String LINGQU_ZHUANYUN = "/native/nativeReceiveDeficit.do";//周周转运领取
    public static final String SERVER_TIME = "native/serverTime.do";//服务器时间
    public static final String MINING_URL = "/mobile/v3/miningActive/index.do";//挖矿

    public static final String GET_MID_AUTUMN_ACTIVE = "/native/getMinAutumnActive.do";//获取中秋活动
    public static final String GET_BOBING_ACTIVE = "/native/getBoBinActive.do";//获取中秋活动
    public static final String BOBIN_PLAY = "/native/boBinPlay.do";//抽奖动作
    public static final String GET_BOBIN_AWARDLIST = "/native/getBobinAwardList.do";//中奖记录
//    public static final String GET_NATIVE_FAKE_DATA_NEW = "/native/native_fake_data_new.do";//中奖记录


    public static final String GET_NATIVE_FAKE_DATA_NEW = "/native/bobin_fake_package_data.do";//   博饼假數據
    public static final String GENERAL_ACTIVITY_URL = "/native/generalActivity.do";//获取临时活动链接
    public static final String ONLINE_COUNT = "/native/online_count.do";//在线人数
    public static final String GET_PLAY_INFO = "/mobile/v3/getPlayInfo.do";//小玩法的奖金
    public static final String WAKUANGLIST = "/native/wakuangList.do";//获取挖矿活动列表
    public static final String GET_AWARD_LIST = "/native/getAwardList.do";//获取挖矿历史记录
    public static final String WAKUANG_PLAY = "/native/wakuangPlay.do";//开始探矿
    public static final String GET_MEMBER_SCORE_RECORD = "/native/memberScoreRecord.do";//获取积分记录

    public static final String GET_RECHARGE_CARD = "/native/nativeRecharge.do";//充值卡充值；参数“card”卡号，"password"密码
    public static final String GET_RECHARGE_RECORD = "/native/nativeRechargeCardData.do";//充值卡使用记录数据列表,参数“startTime”:string,"endTime":string
    public static final String GET_COUPON_RECHARGE = "/native/nativeDJQRecharge.do";//代金券使用；参数“cid”,券id "id"代金券使用者id
    public static final String DELETE_COUPON = "/native/nativeDeleteCs.do";//删除代金券 参数"id"
    public static final String GET_COUPON_RECORD = "/native/djcRecord.do";//获取代金券列表
    public static final String GET_ACTIVE_LOBBY_LIST = "/native/handleActive/activeLobbyList.do";//活动列表
    public static final String GET_ACTIVE_LOBBY_DETAIL = "/native/handleActive/detail.do";//；参数"activeId"//某个活动的详情
    public static final String GET_ACTIVE_LOBBY_PLAY = "/native/handleActive/play.do";//参数：活动id:\"activeId\",活动名称：\"activeName\"//申请活动";
    public static final String GET_ACTIVE_LOBBY_AWARDLIST = "/native/handleActive/getAwardList.do";//,参数，“activeId”//活动申请进度查询
    public static final String GET_THIRD_SPORT_RECORD = "/center/record/betrecord/thirdSportRecord.do";//第三方体育投注
    public static final String GET_HUNTER_RECORD = "/center/record/betrecord/hunterRecord.do";//捕鱼投注记录
    public static final String GET_ESPORT_RECORD = "/center/record/betrecord/esportRecord.do";//电竞投注
    public static final String IS_SIXMARK = "/native/isSixMark.do";//LHC列表
    public static final String DRAGON_BOAT_RACE = "/mobile/v3/dragonBoatRace/index.do";//龙舟活动
    public static final String GET_WINNING_DATA = "/native/getLotteryWinData.do";//中奖数据

    public static final String SEND_FEEDBACK = "/center/advice/saveAdvice.do";//傳送回饋訊息
    public static final String FEEDBACK_RECORDS = "/center/advice/adviceList.do";//取得回饋紀錄
    public static final String GET_CAPTCHA_ID = "/native/getCapthaId.do";//获取验证码ID
    public static final String GET_GOOGLE_ROBOT = "/native/googleRobotConfig.do";//获取谷歌人机验证数据
    public static final String GET_GOOGLE_ROBOT_CALLBACK = "/googleRobotCallback.do";//获取谷歌人机验证回调
    public static final String GET_VERIFY_SWITCH = "/native/getVerifySwitch.do";//获取行为验证开关相关值
    public static final String GET_QUESTION_LIST = "/native/getQuestionList.do";//获取问题列表
    public static final String SET_QUESTION_AND_ANSWER = "/native/setQuestionAndAnswer.do";//首次設定安全問題 (POST) 参数(accountId,  question, answer)
    public static final String GET_QUESTION_BY_MEMBER = "/native/getQuestionByMember.do";//获得安全问题(POST) 参数(accountId)
    public static final String CHECK_ANSWER = "/native/checkAnswer.do";//验证答案(POST) 参数 (accountId, answer)
    public static final String UPDATE_QUESTION_AND_ANSWER = "/native/updateQuestionAndAnswer.do";//验证答案(POST) 参数 (accountId, answer)

    public static final String DIRECT_CHARGE_CHECK = "/center/member/directCharge/checkMemberApply.do";
    public static final String DIRECT_CHARGE_APPLY = "/center/member/directCharge/apply.do";
    public static final String DIRECT_CHARGE_RECORDS = "/center/member/directCharge/list.do";

    public static final String MEMBER_DONATE_BALANCE = "/center/member/donate/getBalance.do"; //会员余额
    public static final String MEMBER_DONATE_APPLY = "/center/member/donate/apply.do"; //乐捐申请
    public static final String MEMBER_DONATE_RECORD = "/center/member/donate/list.do"; //乐捐申请记录

    public static final String NEED_UPDATE_PASSWORD = "/native/getNeedUpgradePassword.do"; // 检查会员是否需要提高密码等级

    //    public static final String CTRL_BASE = "https://ybappctrl.yb876.com"; //检测命令Base Url
    public static final String CTRL_BASE = "https://api.clyzltc.com"; //检测命令Base Url
    public static final String CTRL_CMDS = "/ctrl/ctrlCmds"; //获取检测命令接口
    public static final String CTRL_RESULT = "/ctrlResult/postCmdResult"; //提交检测结果

    public static final String GET_VERIFY_IMAGE = "/native/preDomainToken.do";//取得验证码图档api
    public static final String POST_VERIFY_CODE = "/native/domainLimitVerifycodeCheck.do";//验证码检查api

    public static final String PORT = "";
//    public static final String PORT = ":8082";
//    public static final String PORT = ":8080/game";

    public static Map<String, String> getHeader(Context context) {
        return getHeader(context, false);
    }

    public static Map<String, String> getHeader(Context context, boolean webviewHeader) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Charset", "utf-8");
        headers.put("Accept", "application/xml");
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept-Language", "zh-CN,en,*");
        if (!Utils.isEmptyString(BASE_HOST_URL)) {
            if (webviewHeader) {
                headers.put(RequestUtils.NATIVE_WEBVIEW_HOST, BASE_HOST_URL);
            } else {
                headers.put(RequestUtils.NATIVE_HOST, BASE_HOST_URL);
            }
        }
        headers.put(RequestUtils.ROUTE_TYPE, String.valueOf(APP_ROUTE_TYPE));
        headers.put(RequestUtils.NATIVE_DOMAIN, BASE_URL);
        headers.put(RequestUtils.NATIVE_FLAG, "1");
        headers.put("Cookie", "SESSION=" + YiboPreference.instance(context).getToken());
        headers.put("X-Requested-With", "XMLHttpRequest");//仿AJAX访问
        headers.put("User-Agent", "android/" + Utils.getVersionName(context) + "|" + YiboPreference.instance(context).getDeviceId());
        headers.put("app-code", "a" + BuildConfig.apk_code);
        headers.put("cc-token", YiboPreference.instance(context).getVerifyToken());
        headers.put("emulator", EmulatorDetectUtil.isEmulator(context) ? "1" : "0");
        try {
            headers.put("wtoken", Utils.getMD5(BuildConfig.apk_code + "," + YiboPreference.instance(context).getToken()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        headers.put("isSimulator", String.valueOf("android-"+EmulatorDetectUtil.isEmulator(context)));
        return headers;
    }


    public static String parseResponseResult(String result) {
        if (Utils.isEmptyString(result)) {
            return "";
        }
        if (result.equals("100")) {
            return "请求超时啦，请检查您的网络是否稳定";
        }
        return result;
    }

}
