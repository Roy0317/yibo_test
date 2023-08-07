package com.yibo.yiboapp.entify;

import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.data.Constant;

/**
 * Created by johnson on 2017/10/7.
 */

public class SysConfig {
    String onoff_mobile_guest_register;//注册时是否显示访客试玩入口开关
    //    String onoff_lottery_record;//彩票投注记录开关
//    String onoff_six_record;//六合投注记录开关
//    String onoff_real_record;//真人投注记录开关
//    String onoff_electronic_record;//电子投注记录开关
//    String onoff_sport_record;//体育投注记录开关
    String onoff_sign_in = "";//签到开关
    String app_qr_code_link_ios = "";//IOS版本app二维码地址
    String app_qr_code_link_android = "";//Android版本app二维码地址
    String app_download_info = "";//app下载提示语
    String lottery_order_cancle_switch = "";//彩票撤单开关
    String lottery_order_chase_switch = "";//彩票追号开关
    String onoff_change_money = "";//帐变记录开关
    String onoff_show_mode = "on";//彩票显示模式开关
    String stationCode = "";//站点编号

    String onoff_lottery_game = "";//彩票游戏开关
    String onoff_liu_he_cai = "";//六合彩开关
    String onoff_sports_game = "";//皇冠体育开关
    String new_onoff_sports_game = "";//新皇冠体育开关
    String onoff_zhen_ren_yu_le = "";//真人娱乐开关
    String onoff_dian_zi_you_yi = "";//电子游艺开关
    String onoff_chess = "";//棋牌游戏开关
    String iosExamine = "";//手机页面ios审核是否关闭
    String mobileIndex = "";////手机主页设置。v1－突出彩票，v2－突出真人,v3-突出体育
    String yjf = "";//元角分模式

    String onoff_mobile_verify_code = "off";//手机登录需要验证码。on－开，off－关

    String onoff_register = "on";//注册开关
    String onoff_mobile_app_reg;//注册app单独开关
    String onoff_member_mobile_red_packet = "";//手机抢红包开关
    //手机页面中显示提款按钮
    String onoff_mobile_drawing = "";
    //手机页面中显示存款按钮
    String onoff_mobile_recharge = "";
    //版本号
    String version;
    String customerServiceUrlLink;
    String online_customer_showphone;
    String live700_secret = "";

    String bankFlag = "";
    String fastFlag = "";
    String onlineFlag = "";
    String exchange_score = "";//积分兑换开关
    boolean isActive;//是否显示优惠活动
    String app_download_link_ios = "";
    String app_download_link_android = "";
    String onoff_turnlate = "";//大转盘开关
    String lottery_page_logo_url = "";//网站logo
    String native_style_code = "";//主页风格code

    String active_title_switch = "";//优惠活动标题显示或隐藏开关
    String charge_page_style = "classic";//充值页面风格切换
    String register_btn_pos_sort = "";//导航栏右上角注册选项位置
    String onoff_shaba_sports_game = "";//沙巴体育开关
    String switch_backto_computer = "";//是否显示返回电脑端按钮
    String member_center_bg_url = "";//个人中心头部背景图片地址
    String member_center_logo_url = "";//个人中心头部LOGO图片地址
    String sys_shaba_center_token = "";//沙巴体育授权口令
    String sys_real_center_token = "";//真人平台授权口令
    String switch_active_deadline_time = "";//优惠活动截止时间显示开关
    String basic_info_website_name;//网站名称
    String mny_score_show = "on";//积分显示开关
    String remark_field_switch = "on";//入款备注显示开关
    String logo_for_login = "";//登录页LOGO地址
    String show_active_badge = "on";//优惠活动角标显示开关
    String mobile_remember_loginpsd = "off";//手机登录是否默认记住密码

    String pay_tips_deposit_fast = "";//快速入款支付说明
    String pay_tips_deposit_general = "";//一般入款支付说明
    String pay_tips_deposit_third = "";//第三方入款支付说明

    String mobile_web_index_slide_images = "";
    String mobile_web_index_slide_url = "";

    String mobile_v3_bet_order_detail_total = "on";//手机新版投注详情页是否显示统计
    String native_welcome_page_switch = "on";//首次安装时是否展示欢迎页

    String foreign_game_hall_link = "";//外部游戏大厅链接地址
    String foreign_game_hall_link_switch = "";//外部游戏大厅链接开关

    String nbchess_showin_mainpage = "on";//棋牌是否显示在首页
    String onoff_nb_game = "off";//棋牌模块开关

    String fast_bet_switch = "on";//简约版投注是否显示快捷投注栏

    String onoff_all_level_fixed = "off";//是否显示代理管理

    String onlinepay_name_switch = "off";//在线充值时是否显示支付名
    String draw_money_user_name_modify = "off";//提款绑定银行卡真实姓名是否可修改
    String k3_baozi_daXiaoDanShuang = "off";//快三豹子算大小单双
    String onoff_tt_lottery_game = "";//易彩票开关
    String onoff_ky_game = "";//开元棋牌开关

    String bet_page_style = "v1";//v1－简约风格，v2－经典风格
    String switch_feeconvert_beside_pickmoney = "on";
    String webpay_group_sort = "1,2,3";//网站入款方式排序

    String open_lottery_website_url = "";//开奖网链
    // 接地址
    String open_lottery_website_switch = "off";//开奖网开关
    String confirm_dialog_before_bet = "on";//确认下注提示框开关
    String lunbo_interval_switch = "3";//轮播图时间

    String onoff_payment_show_info = "on";//支付二维码是否显示
    String switch_push_interval = "1";//默认一分钟获取一次推送
    String onoff_chat = "off"; // 聊天室开关
    String multi_list_dialog_switch = "off";//多功能列表式弹窗
    String promp_link_version = "v1";//推广链接展示方法 v1-原生模式,v2--网页模式
    String jump_style_when_click_cqk = "v1";//v1-个人中心 v2存款 v3提款-
    String mainpage_betrecord_click_goto_recordpage = "on";//主页投注记录点击是否直接进入投注记录
    String offline_charge_name = "";//充值页面线下充值自定义名称
    String online_charge_name = "";
    String fastcharge_charge_name = "";

    String native_mobile_agent_register_enter = "off";//代理注册

    String zrdz_jump_broswer = "on"; //真人，电子外部浏览器打开
    String onoff_new_shaba_jump_browsers = "on";//新沙巴体育是否浏览器打开
    String mainpage_version = "v1"; //主页版本。v1--经典版本 v2--额度转换版本 v3--余额生金版本 ,v4-app下载和优惠活动同时显示
    String rob_redpacket_version = "v2"; //抢红包优化版本，v1--经典版本 v2--优化版本
    String mobile_web_index_slide_url_name = "";//首页浮动链接名称
    String add_hot_text_appond_caizhong = "off";//彩种是否加热字
    String lunbo_speed_seconds = "";//加一下轮播速度配置
    String pk10_guanyahe_11_heju = "off";//pk10冠亚和开启11为和局
    String onoff_money_income = "off";//余额生金开关要同步一下
    String waitfor_openbet_after_bet_deadline = "off"; //封盘时时候可以投注下一期
    String pc_sign_logo = ""; //签到背景url配置
    String usercenter_level_show_switch = "on"; //会员中心是否显示等级信息
    String native_fenpang_bet_switch = "off"; //原生封盘时是否可以继续直接下注下一期
    String chat_icon_in_betpage_switch = "off"; //聊天室显示隐藏
    String web_notice_scroll_speed = "2"; //滚动条速度
    String wap_active_activity_link = ""; //优惠活动外部跳转链接
    String showuser_levelicon = "on"; //会员中心等级图标显示
    String fast_deposit_add_random = "off"; //快速入款充值金额是否需要补随机小数
    String fast_deposit_add_money_select = "0"; //快速入款充值金额是否需要补尾数 //0关闭; 1增加1~5随机数; 2增加1~5小数
    String online_charge_support_decimal_point = "off"; //在线充值时金额是否支持小数
    String goto_downpage_when_update_version = "V1";//下载的时候跳转外部浏览器还是在app内部更新
    String notice_list_dialog_expand_first_notice = "on";//首页公告弹窗列表是否默认展示第一条公告内容
    String onoff_show_pay_quick_addmoney = "off"; //快速存款是否随机增加金额 on:当用户输入整数金额的时候，随机增加金额 .00 - 0.99
    String show_lottery_trend = "on"; //要匹配是否显示走势图开关
    String show_agent_manager_mainpage = "off";//首页是否显示代理管理
    String one_bonus_onoff = "off";//每日加奖活动开关
    String week_deficit_onoff = "off";//周周转运
    String onoff_mid_autumn = "on"; //中秋
    String force_update_app = "off";//有新版本时是否强制会员更新
    String fixedAmoun = "";//快速入款金额
    String countdown_switch = "on";//是否需要显示倒计时开关
    String show_temp_activity_switch = "off";//是否需要临时活动开关
    String single_jump_pc_domain_url = "";//单独跳转PC域名
    String gesture_pwd_switch = "on"; //手势密码开关
    String switch_mainpage_online_count = "off"; //是否显示在线人数
    String onoff_show_remember_psd = "on";//登陆界面是否显示记住密码

    String online_count_fake = "";//追加在线假数据人数;加了一个配置
    String chat_foreign_link = "";//外部聊天室链接
    String third_auto_exchange = "off";//第三方免额度转换
    String switch_original_chat = "on";//原生聊天室开关，默认关闭显示网页版聊天室

    String native_chat_room = "on";//是否使用原生聊天室
    String fast_bet_money = "";//快捷金额
    String hide_member_level = "off";//是否要隐藏会员等级
    String switch_win_notice_inredpacket_page = "on";
    String online_handle_service_url = "";//人工入款在线客服地址
    String onoff_show_pay_custom;//人工入款开关
    String onoff_mining = "off";
    String on_off_recaptcha_verify = "off";//开启站点行为验证
    String on_off_recaptcha_verify_hide_code = "off";//开启行为验证后隐藏验证码
    String recaptcha_verify_type = "163huadong";//行为验证类型
    String onoff_application_active = "off";
    String onoff_avia_game = "off";//是否显示泛亚电竞
    String onoff_mobile_notlogged_to_loginview = "off"; //手机未登录是否跳转到登录页面开关
    String not_login_permission = "off";//未登录状态下是否只允许访问登录页面
    String dragon_boat_race_onoff = "off";//赛龙舟开关
    String add_bank_card_tips = "";//绑定银行卡信息
    String onoff_show_winning_data = "off";//是否显示中奖假数据
    String member_multi_bank_card_permission;//允许会员设定多银行卡(USDT)设定开关
    String on_off_alipay_withdrawal;//支付宝提款开关
    String on_off_usdt_withdrawal;//USDT提款开关
    String on_off_gopay_withdrawal;//GoPay提款开关
    String on_off_okpay_withdrawal;//OkPay提款开关
    String on_off_topay_withdrawal;//toPay提款开关
    String on_off_vpay_withdrawal;//toPay提款开关
    String usdt_rate = "";//USDT汇率
    String usdt_out_rate = ""; //usdt出款汇率
    String usdt_url = "";//USDT教程URL
    String usdt_show = "";//USDT入款支付说明
    String onoff_new_pay_mode = "";//开启新版支付模式
    String onoff_mobile_unread_msg_popups; // 手机未读消息提示弹窗
    String mobile_station_index_logo;//手机首页logo
    String sports_min_bet_money; //体育单注最低限额
    String sports_max_bet_money; //体育单注最高限额
    String native_vertify_captcha_switch = "off";//是否开启原生行为验证
    String on_off_mobile_certificate = "0";//开启多设备验证 为0则关闭
    String withdrawNotInteger = "off";//会员提款不能提10的倍数
    String on_off_guest_redperm = "off";//后台试玩账号抢红包开关
    String cache_data_local_switch = "on";//APP缓存数据到本地策略
    String app_withdraw_no_integer = "off";//APP提款是否允许小数
    String custom_tips = "";//提款页面自定义提示信息
    String newmainpage_switch = "off";//新版手机主页开关
    String mainpage_function_menu = "1,2,3,4";//主页功能菜单排序(1-存提款,2-投注记录,3-优惠活动,4-在线客服,5-额度转换,6-余额生金,7-APP下载)
    String login_reg_background_image_url = "";////登录注册页背景图地址
    String on_off_direct_charge = ""; //免提直充开关
    String direct_charge_gift_money_percent = "0";//免提直充奖金比例
    String googleRobotOnOff = "off";//谷歌人机验证
    String app_emulator_login = "off";//模拟器登陆
    String on_off_member_donate = "off";//会员乐捐开关
    String buy_lottery_change_sale_activity = "off";//on的话将大厅页面bottom bar的购彩大厅换成优惠活动
    String station_currency_type_permission = "off";//新提款类别管理，ON的话会回传新的提款通道格式JSON
    String activeCode = ""; //{1：周周转运活动}{2：赛龙舟活动}{3：中秋博饼活动}{4：挖矿活动}{5：每日加獎}

    public String getApp_emulator_login() {
        if(BuildConfig.DEBUG)
            return "on";
        else
            return app_emulator_login;
    }

    public void setApp_emulator_login(String app_emulator_login) {
        this.app_emulator_login = app_emulator_login;
    }

    public String getOn_off_member_donate() {
        return on_off_member_donate;
    }

    public void setOn_off_member_donate(String on_off_member_donate) {
        this.on_off_member_donate = on_off_member_donate;
    }

    public String getGoogleRobotOnOff() {
        return googleRobotOnOff;
    }

    public void setGoogleRobotOnOff(String googleRobotOnOff) {
        this.googleRobotOnOff = googleRobotOnOff;
    }

    public String getCustom_tips() {
        return custom_tips;
    }

    public void setCustom_tips(String custom_tips) {
        this.custom_tips = custom_tips;
    }

    public String getApp_withdraw_no_integer() {
        return app_withdraw_no_integer;
    }

    public void setApp_withdraw_no_integer(String app_withdraw_no_integer) {
        this.app_withdraw_no_integer = app_withdraw_no_integer;
    }

    public String getOn_off_mobile_certificate() {
        return on_off_mobile_certificate;
    }

    public void setOn_off_mobile_certificate(String on_off_mobile_certificate) {
        this.on_off_mobile_certificate = on_off_mobile_certificate;
    }

    public String getAdd_bank_card_tips() {
        return add_bank_card_tips;
    }

    public String getDragon_boat_race_onoff() {
        return dragon_boat_race_onoff;
    }

    public void setDragon_boat_race_onoff(String dragon_boat_race_onoff) {
        this.dragon_boat_race_onoff = dragon_boat_race_onoff;
    }

    public String getOnoff_mobile_verify_code() {
        return onoff_mobile_verify_code;
    }

    public void setOnoff_mobile_verify_code(String onoff_mobile_verify_code) {
        this.onoff_mobile_verify_code = onoff_mobile_verify_code;
    }

    public String getNot_login_permission() {
        return not_login_permission;
    }

    public String getOnoff_new_shaba_jump_browsers() {
        return onoff_new_shaba_jump_browsers;
    }

    //手机未登录是否跳转到登录页面开关
    public String getOnoff_mobile_notlogged_to_loginview() {
        return onoff_mobile_notlogged_to_loginview;
    }

    public String getOnoff_avia_game() {
        return onoff_avia_game;
    }

    public void setOnoff_avia_game(String onoff_avia_game) {
        this.onoff_avia_game = onoff_avia_game;
    }

    public String getOnoff_application_active() {
        return onoff_application_active;
    }

    public void setOnoff_application_active(String onoff_application_active) {
        this.onoff_application_active = onoff_application_active;
    }

    public void setSingle_jump_pc_domain_url(String single_jump_pc_domain_url) {
        this.single_jump_pc_domain_url = single_jump_pc_domain_url;
    }

    public String getOn_off_recaptcha_verify() {
        return on_off_recaptcha_verify;
    }

    public void setOn_off_recaptcha_verify(String on_off_recaptcha_verify) {
        this.on_off_recaptcha_verify = on_off_recaptcha_verify;
    }

    public String getOn_off_recaptcha_verify_hide_code() {
        return on_off_recaptcha_verify_hide_code;
    }

    public void setOn_off_recaptcha_verify_hide_code(String on_off_recaptcha_verify_hide_code) {
        this.on_off_recaptcha_verify_hide_code = on_off_recaptcha_verify_hide_code;
    }

    public String getRecaptcha_verify_type() {
        return recaptcha_verify_type;
    }

    public void setRecaptcha_verify_type(String recaptcha_verify_type) {
        this.recaptcha_verify_type = recaptcha_verify_type;
    }

    String onoff_adduser_front = "on";//是否允许前台添加会员

    String recharge_card_onoff = "off";//充值卡开关
    String coupons_onoff = "off"; //代金券开关
    String onoff_third_sport = "off";//第三方体育

    public String getOnoff_third_sport() {
        return onoff_third_sport;
    }

    public void setOnoff_third_sport(String onoff_third_sport) {
        this.onoff_third_sport = onoff_third_sport;
    }

    public String getRecharge_card_onoff() {
        return recharge_card_onoff;
    }

    public String getCoupons_onoff() {
        return coupons_onoff;
    }

    public String getOnoff_adduser_front() {
        return onoff_adduser_front;
    }

    public String getOnoff_mining() {
        return onoff_mining;
    }

    public String getOnoff_show_pay_custom() {
        return onoff_show_pay_custom;
    }

    public void setOnoff_show_pay_custom(String onoff_show_pay_custom) {
        this.onoff_show_pay_custom = onoff_show_pay_custom;
    }

    public String getOnline_handle_service_url() {
        return online_handle_service_url;
    }

    public void setOnline_handle_service_url(String online_handle_service_url) {
        this.online_handle_service_url = online_handle_service_url;
    }

    public String getSwitch_win_notice_inredpacket_page() {
        return switch_win_notice_inredpacket_page;
    }

    public void setSwitch_win_notice_inredpacket_page(String switch_win_notice_inredpacket_page) {
        this.switch_win_notice_inredpacket_page = switch_win_notice_inredpacket_page;
    }

    public String getHide_member_level() {
        return hide_member_level;
    }

    public String getFast_bet_money() {
        return fast_bet_money;
    }

    public void setFast_bet_money(String fast_bet_money) {
        this.fast_bet_money = fast_bet_money;
    }

    public void setCountdown_switch(String countdown_switch) {
        this.countdown_switch = countdown_switch;
    }

    public String getNative_chat_room() {
        return native_chat_room;
    }

    public void setNative_chat_room(String native_chat_room) {
        this.native_chat_room = native_chat_room;
    }

    public String getSwitch_original_chat() {
        return switch_original_chat;
    }


    public String getThird_auto_exchange() {
        return third_auto_exchange;
    }

    public void setThird_auto_exchange(String third_auto_exchange) {
        this.third_auto_exchange = third_auto_exchange;
    }


    public String getChat_foreign_link() {
        return chat_foreign_link;
    }

    public String getOnline_count_fake() {
        return online_count_fake;
    }

    public void setOnline_count_fake(String online_count_fake) {
        this.online_count_fake = online_count_fake;
    }

    public String getSwitch_mainpage_online_count() {
        return switch_mainpage_online_count;
    }

    public void setSwitch_mainpage_online_count(String switch_mainpage_online_count) {
        this.switch_mainpage_online_count = switch_mainpage_online_count;
    }

    public String getGesture_pwd_switch() {
        return gesture_pwd_switch;
    }

    public String getShow_temp_activity_switch() {
        return show_temp_activity_switch;
    }

    public String getSingle_jump_pc_domain_url() {
        return single_jump_pc_domain_url;
    }

    public void setShow_temp_activity_switch(String show_temp_activity_switch) {
        this.show_temp_activity_switch = show_temp_activity_switch;
    }

    public String getCountdown_switch() {
        return countdown_switch;
    }

    public String getFixedAmoun() {
        return fixedAmoun;
    }

    public void setFixedAmoun(String fixedAmoun) {
        this.fixedAmoun = fixedAmoun;
    }

    public String getForce_update_app() {
        return force_update_app;
    }

    public void setForce_update_app(String force_update_app) {
        this.force_update_app = force_update_app;
    }

    public String getOnoff_mid_autumn() {
        return onoff_mid_autumn;
    }

    public void setOnoff_mid_autumn(String onoff_mid_autumn) {
        this.onoff_mid_autumn = onoff_mid_autumn;
    }

    public void setGoto_downpage_when_update_version(String goto_downpage_when_update_version) {
        this.goto_downpage_when_update_version = goto_downpage_when_update_version;
    }

    public void setNotice_list_dialog_expand_first_notice(String notice_list_dialog_expand_first_notice) {
        this.notice_list_dialog_expand_first_notice = notice_list_dialog_expand_first_notice;
    }

    public String getOne_bonus_onoff() {
        return one_bonus_onoff;
    }

    public void setOne_bonus_onoff(String one_bonus_onoff) {
        this.one_bonus_onoff = one_bonus_onoff;
    }

    public String getWeek_deficit_onoff() {
        return week_deficit_onoff;
    }

    public void setWeek_deficit_onoff(String week_deficit_onoff) {
        this.week_deficit_onoff = week_deficit_onoff;
    }

    public String getMainpage_version() {
        return mainpage_version;
    }

    public void setMainpage_version(String mainpage_version) {
        this.mainpage_version = mainpage_version;
    }


    public String getShow_agent_manager_mainpage() {
        return show_agent_manager_mainpage;
    }

    public void setShow_agent_manager_mainpage(String show_agent_manager_mainpage) {
        this.show_agent_manager_mainpage = show_agent_manager_mainpage;
    }

    public String getShow_lottery_trend() {
        return show_lottery_trend;
    }

    public void setShow_lottery_trend(String show_lottery_trend) {
        this.show_lottery_trend = show_lottery_trend;
    }


    public String getShowuser_levelicon() {
        return showuser_levelicon;
    }

    public void setShowuser_levelicon(String showuser_levelicon) {
        this.showuser_levelicon = showuser_levelicon;
    }

    public String getWap_active_activity_link() {
        return wap_active_activity_link;
    }

    public void setWap_active_activity_link(String wap_active_activity_link) {
        this.wap_active_activity_link = wap_active_activity_link;
    }

    public String getFast_deposit_add_random() {
        return fast_deposit_add_random;
    }

    public void setFast_deposit_add_random(String fast_deposit_add_random) {
        this.fast_deposit_add_random = fast_deposit_add_random;
    }

    public String getFast_deposit_add_money_select() {
        return fast_deposit_add_money_select;
    }

    public void setFast_deposit_add_money_select(String fast_deposit_add_money_select) {
        this.fast_deposit_add_money_select = fast_deposit_add_money_select;
    }

    public String getNotice_list_dialog_expand_first_notice() {
        return notice_list_dialog_expand_first_notice;
    }

    public String getGoto_downpage_when_update_version() {
        return goto_downpage_when_update_version;
    }

    public String getOnline_charge_support_decimal_point() {
        return online_charge_support_decimal_point;
    }

    public void setOnline_charge_support_decimal_point(String online_charge_support_decimal_point) {
        this.online_charge_support_decimal_point = online_charge_support_decimal_point;
    }

    public String getNative_fenpang_bet_switch() {
        return native_fenpang_bet_switch;
    }

    public void setNative_fenpang_bet_switch(String native_fenpang_bet_switch) {
        this.native_fenpang_bet_switch = native_fenpang_bet_switch;
    }

    public String getWeb_notice_scroll_speed() {
        return web_notice_scroll_speed;
    }

    public void setWeb_notice_scroll_speed(String web_notice_scroll_speed) {
        this.web_notice_scroll_speed = web_notice_scroll_speed;
    }

    public String getChat_icon_in_betpage_switch() {
        return chat_icon_in_betpage_switch;
    }

    public void setChat_icon_in_betpage_switch(String chat_icon_in_betpage_switch) {
        this.chat_icon_in_betpage_switch = chat_icon_in_betpage_switch;
    }

    public String getPc_sign_logo() {
        return pc_sign_logo;
    }

    public void setPc_sign_logo(String pc_sign_logo) {
        this.pc_sign_logo = pc_sign_logo;
    }


    public String getWaitfor_openbet_after_bet_deadline() {
        return waitfor_openbet_after_bet_deadline;
    }

    public void setWaitfor_openbet_after_bet_deadline(String waitfor_openbet_after_bet_deadline) {
        this.waitfor_openbet_after_bet_deadline = waitfor_openbet_after_bet_deadline;
    }

    public String getPk10_guanyahe_11_heju() {
        return pk10_guanyahe_11_heju;
    }

    public void setPk10_guanyahe_11_heju(String pk10_guanyahe_11_heju) {
        this.pk10_guanyahe_11_heju = pk10_guanyahe_11_heju;
    }


    public String getOnoff_money_income() {
        return onoff_money_income;
    }

    public void setOnoff_money_income(String onoff_money_income) {
        this.onoff_money_income = onoff_money_income;
    }

    public String getLunbo_speed_seconds() {
        return lunbo_speed_seconds;
    }

    public void setLunbo_speed_seconds(String lunbo_speed_seconds) {
        this.lunbo_speed_seconds = lunbo_speed_seconds;
    }


    public String getAdd_hot_text_appond_caizhong() {
        return add_hot_text_appond_caizhong;
    }

    public void setAdd_hot_text_appond_caizhong(String add_hot_text_appond_caizhong) {
        this.add_hot_text_appond_caizhong = add_hot_text_appond_caizhong;
    }


    public String getRob_redpacket_version() {
        return rob_redpacket_version;
    }

    public void setRob_redpacket_version(String rob_redpacket_version) {
        this.rob_redpacket_version = rob_redpacket_version;
    }

    public String getZrdz_jump_broswer() {
        return zrdz_jump_broswer;
    }

    public void setZrdz_jump_broswer(String zrdz_jump_broswer) {
        this.zrdz_jump_broswer = zrdz_jump_broswer;
    }

    String welcome_page_startapp_text = "";
    String station_id;

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getOnline_charge_name() {
        return online_charge_name;
    }

    public void setOnline_charge_name(String online_charge_name) {
        this.online_charge_name = online_charge_name;
    }

    public String getFastcharge_charge_name() {
        return fastcharge_charge_name;
    }

    public void setFastcharge_charge_name(String fastcharge_charge_name) {
        this.fastcharge_charge_name = fastcharge_charge_name;
    }

    public String getWelcome_page_startapp_text() {
        return welcome_page_startapp_text;
    }

    public void setWelcome_page_startapp_text(String welcome_page_startapp_text) {
        this.welcome_page_startapp_text = welcome_page_startapp_text;
    }


    public String getMainpage_betrecord_click_goto_recordpage() {
        return mainpage_betrecord_click_goto_recordpage;
    }

    public void setMainpage_betrecord_click_goto_recordpage(String mainpage_betrecord_click_goto_recordpage) {
        this.mainpage_betrecord_click_goto_recordpage = mainpage_betrecord_click_goto_recordpage;
    }


    public void setOnoff_chat(String onoff_chat) {
        this.onoff_chat = onoff_chat;
    }

    public String getOnoff_chat() {
        return onoff_chat;
    }

    String offline_charge_toast_tip = "";//线下充值时二维码下的友好提示文字

    public String getOffline_charge_toast_tip() {
        return offline_charge_toast_tip;
    }

    public void setOffline_charge_toast_tip(String offline_charge_toast_tip) {
        this.offline_charge_toast_tip = offline_charge_toast_tip;
    }

    public String getPromp_link_version() {
        return promp_link_version;
    }

    public void setPromp_link_version(String promp_link_version) {
        this.promp_link_version = promp_link_version;
    }

    public String getNative_mobile_agent_register_enter() {
        return native_mobile_agent_register_enter;
    }

    public void setNative_mobile_agent_register_enter(String native_mobile_agent_register_enter) {
        this.native_mobile_agent_register_enter = native_mobile_agent_register_enter;
    }

    public String getMulti_list_dialog_switch() {
        return multi_list_dialog_switch;
    }

    public void setMulti_list_dialog_switch(String multi_list_dialog_switch) {
        this.multi_list_dialog_switch = multi_list_dialog_switch;
    }


    public void setSwitch_push_interval(String switch_push_interval) {
        this.switch_push_interval = switch_push_interval;
    }

    public String getOffline_charge_name() {
        return offline_charge_name;
    }

    public void setOffline_charge_name(String offline_charge_name) {
        this.offline_charge_name = offline_charge_name;
    }

    public String getMobile_web_index_slide_url_name() {
        return mobile_web_index_slide_url_name;
    }

    public void setMobile_web_index_slide_url_name(String mobile_web_index_slide_url_name) {
        this.mobile_web_index_slide_url_name = mobile_web_index_slide_url_name;
    }

    public String getJump_style_when_click_cqk() {
        return jump_style_when_click_cqk;
    }

    public void setJump_style_when_click_cqk(String jump_style_when_click_cqk) {
        this.jump_style_when_click_cqk = jump_style_when_click_cqk;
    }


    public String getSwitch_push_interval() {
        return switch_push_interval;
    }

    public String getDeposit_interval_times() {
        return deposit_interval_times;
    }

    public void setDeposit_interval_times(String deposit_interval_times) {
        this.deposit_interval_times = deposit_interval_times;
    }

    String deposit_interval_times = "0";//  支付间隔时间


    public String getOnoff_show_pay_quick_addmoney() {
        return onoff_show_pay_quick_addmoney;
    }

    public void setOnoff_show_pay_quick_addmoney(String onoff_show_pay_quick_addmoney) {
        this.onoff_show_pay_quick_addmoney = onoff_show_pay_quick_addmoney;
    }

    public void setLunbo_interval_switch(String lunbo_interval_switch) {
        this.lunbo_interval_switch = lunbo_interval_switch;
    }

    public String getOnoff_payment_show_info() {
        return onoff_payment_show_info;
    }

    public void setOnoff_payment_show_info(String onoff_payment_show_info) {
        this.onoff_payment_show_info = onoff_payment_show_info;
    }

    public String getMulti_broswer() {
        return multi_broswer;
    }

    public void setMulti_broswer(String multi_broswer) {
        this.multi_broswer = multi_broswer;
    }

    String multi_broswer = "on";//支持用户自己选择支付浏览器

    public String getOnline_service_open_switch() {
        return online_service_open_switch;
    }

    public void setOnline_service_open_switch(String online_service_open_switch) {
        this.online_service_open_switch = online_service_open_switch;
    }

    String online_service_open_switch = "v1";//在线客服打开方式 v1--浏览器打开 v2-应用内部打开

    public String getLunbo_interval_switch() {
        return lunbo_interval_switch;
    }

    public String getConfirm_dialog_before_bet() {
        return confirm_dialog_before_bet;
    }

    public void setConfirm_dialog_before_bet(String confirm_dialog_before_bet) {
        this.confirm_dialog_before_bet = confirm_dialog_before_bet;
    }

    public String getOpen_lottery_website_url() {
        return open_lottery_website_url;
    }

    public void setOpen_lottery_website_url(String open_lottery_website_url) {
        this.open_lottery_website_url = open_lottery_website_url;
    }

    public String getOpen_lottery_website_switch() {
        return open_lottery_website_switch;
    }

    public void setOpen_lottery_website_switch(String open_lottery_website_switch) {
        this.open_lottery_website_switch = open_lottery_website_switch;
    }

    public String getWebpay_group_sort() {
        return webpay_group_sort;
    }

    public void setWebpay_group_sort(String webpay_group_sort) {
        this.webpay_group_sort = webpay_group_sort;
    }

    public String getOnoff_chess() {
        return onoff_chess;
    }

    public void setOnoff_chess(String onoff_chess) {
        this.onoff_chess = onoff_chess;
    }

    public String getSwitch_feeconvert_beside_pickmoney() {
        return switch_feeconvert_beside_pickmoney;
    }

    public void setSwitch_feeconvert_beside_pickmoney(String switch_feeconvert_beside_pickmoney) {
        this.switch_feeconvert_beside_pickmoney = switch_feeconvert_beside_pickmoney;
    }

    public String getBet_page_style() {
        return bet_page_style;
    }

    public void setBet_page_style(String bet_page_style) {
        this.bet_page_style = bet_page_style;
    }

    public String getOnoff_tt_lottery_game() {
        return onoff_tt_lottery_game;
    }

    public void setOnoff_tt_lottery_game(String onoff_tt_lottery_game) {
        this.onoff_tt_lottery_game = onoff_tt_lottery_game;
    }

    public String getOnoff_ky_game() {
        return onoff_ky_game;
    }

    public void setOnoff_ky_game(String onoff_ky_game) {
        this.onoff_ky_game = onoff_ky_game;
    }

    String mobile_indexmeun_group_sort = "1,2,3,4,5";//1=彩票,2=真人,3=棋牌,4=电子,5=体育(逗号隔开)
    String mainpage_module_indexs = "1,3,4,5,6,7,8,9";//主页模块排序

    public String getMainpage_module_indexs() {
        return mainpage_module_indexs;
    }

    public String getMobile_indexmeun_group_sort() {
        return mobile_indexmeun_group_sort;
    }

    public void setMobile_indexmeun_group_sort(String mobile_indexmeun_group_sort) {
        this.mobile_indexmeun_group_sort = mobile_indexmeun_group_sort;
    }

    public String getK3_baozi_daXiaoDanShuang() {
        return k3_baozi_daXiaoDanShuang;
    }

    public void setK3_baozi_daXiaoDanShuang(String k3_baozi_daXiaoDanShuang) {
        this.k3_baozi_daXiaoDanShuang = k3_baozi_daXiaoDanShuang;
    }

    public String getOnoff_all_level_fixed() {
        return onoff_all_level_fixed;
    }

    public String getDraw_money_user_name_modify() {
        return draw_money_user_name_modify;
    }

    public void setOnoff_all_level_fixed(String onoff_all_level_fixed) {
        this.onoff_all_level_fixed = onoff_all_level_fixed;
    }


    public void setDraw_money_user_name_modify(String draw_money_user_name_modify) {
        this.draw_money_user_name_modify = draw_money_user_name_modify;
    }

    public String getOnlinepay_name_switch() {
        return onlinepay_name_switch;
    }

    public void setOnlinepay_name_switch(String onlinepay_name_switch) {
        this.onlinepay_name_switch = onlinepay_name_switch;
    }


    public String getFast_bet_switch() {
        return fast_bet_switch;
    }

    public void setFast_bet_switch(String fast_bet_switch) {
        this.fast_bet_switch = fast_bet_switch;
    }

    public String getNbchess_showin_mainpage() {
        return nbchess_showin_mainpage;
    }

    public void setNbchess_showin_mainpage(String nbchess_showin_mainpage) {
        this.nbchess_showin_mainpage = nbchess_showin_mainpage;
    }

    public String getOnoff_nb_game() {
        return onoff_nb_game;
    }

    public void setOnoff_nb_game(String onoff_nb_game) {
        this.onoff_nb_game = onoff_nb_game;
    }

    public String getForeign_game_hall_link() {
        return foreign_game_hall_link;
    }

    public void setForeign_game_hall_link(String foreign_game_hall_link) {
        this.foreign_game_hall_link = foreign_game_hall_link;
    }

    public String getForeign_game_hall_link_switch() {
        return foreign_game_hall_link_switch;
    }

    public void setForeign_game_hall_link_switch(String foreign_game_hall_link_switch) {
        this.foreign_game_hall_link_switch = foreign_game_hall_link_switch;
    }

    public String getNew_onoff_sports_game() {
        return new_onoff_sports_game;
    }

    public void setNew_onoff_sports_game(String new_onoff_sports_game) {
        this.new_onoff_sports_game = new_onoff_sports_game;
    }

    public String getNative_welcome_page_switch() {
        return native_welcome_page_switch;
    }

    public void setNative_welcome_page_switch(String native_welcome_page_switch) {
        this.native_welcome_page_switch = native_welcome_page_switch;
    }

    public String getMobile_v3_bet_order_detail_total() {
        return mobile_v3_bet_order_detail_total;
    }

    public void setMobile_v3_bet_order_detail_total(String mobile_v3_bet_order_detail_total) {
        this.mobile_v3_bet_order_detail_total = mobile_v3_bet_order_detail_total;
    }

    public String getMobile_web_index_slide_images() {
        return mobile_web_index_slide_images;
    }

    public void setMobile_web_index_slide_images(String mobile_web_index_slide_images) {
        this.mobile_web_index_slide_images = mobile_web_index_slide_images;
    }

    public String getMobile_web_index_slide_url() {
        return mobile_web_index_slide_url;
    }

    public void setMobile_web_index_slide_url(String mobile_web_index_slide_url) {
        this.mobile_web_index_slide_url = mobile_web_index_slide_url;
    }

    public String getPay_tips_deposit_fast() {
        return pay_tips_deposit_fast;
    }

    public void setPay_tips_deposit_fast(String pay_tips_deposit_fast) {
        this.pay_tips_deposit_fast = pay_tips_deposit_fast;
    }

    public String getPay_tips_deposit_general() {
        return pay_tips_deposit_general;
    }

    public void setPay_tips_deposit_general(String pay_tips_deposit_general) {
        this.pay_tips_deposit_general = pay_tips_deposit_general;
    }

    public String getPay_tips_deposit_third() {
        return pay_tips_deposit_third;
    }

    public void setPay_tips_deposit_third(String pay_tips_deposit_third) {
        this.pay_tips_deposit_third = pay_tips_deposit_third;
    }

    public String getShow_active_badge() {
        return show_active_badge;
    }

    public void setShow_active_badge(String show_active_badge) {
        this.show_active_badge = show_active_badge;
    }

    public String getLogo_for_login() {
        return logo_for_login;
    }

    public void setLogo_for_login(String logo_for_login) {
        this.logo_for_login = logo_for_login;
    }

    public String getRemark_field_switch() {
        return remark_field_switch;
    }

    public void setRemark_field_switch(String remark_field_switch) {
        this.remark_field_switch = remark_field_switch;
    }

    public String getMny_score_show() {
        return mny_score_show;
    }

    public void setMny_score_show(String mny_score_show) {
        this.mny_score_show = mny_score_show;
    }

    public String getBasic_info_website_name() {
        return basic_info_website_name;
    }

    public void setBasic_info_website_name(String basic_info_website_name) {
        this.basic_info_website_name = basic_info_website_name;
    }

    public String getSwitch_active_deadline_time() {
        return switch_active_deadline_time;
    }

    public void setSwitch_active_deadline_time(String switch_active_deadline_time) {
        this.switch_active_deadline_time = switch_active_deadline_time;
    }

    public String getSys_shaba_center_token() {
        return sys_shaba_center_token;
    }

    public void setSys_shaba_center_token(String sys_shaba_center_token) {
        this.sys_shaba_center_token = sys_shaba_center_token;
    }

    public String getSys_real_center_token() {
        return sys_real_center_token;
    }

    public void setSys_real_center_token(String sys_real_center_token) {
        this.sys_real_center_token = sys_real_center_token;
    }

    public String getMember_center_bg_url() {
        return member_center_bg_url;
    }

    public void setMember_center_bg_url(String member_center_bg_url) {
        this.member_center_bg_url = member_center_bg_url;
    }

    public String getMember_center_logo_url() {
        return member_center_logo_url;
    }

    public void setMember_center_logo_url(String member_center_logo_url) {
        this.member_center_logo_url = member_center_logo_url;
    }

    public String getSwitch_backto_computer() {
        return switch_backto_computer;
    }

    public void setSwitch_backto_computer(String switch_backto_computer) {
        this.switch_backto_computer = switch_backto_computer;
    }

    public String getOnoff_shaba_sports_game() {
        return onoff_shaba_sports_game;
    }

    public void setOnoff_shaba_sports_game(String onoff_shaba_sports_game) {
        this.onoff_shaba_sports_game = onoff_shaba_sports_game;
    }

    public String getActive_title_switch() {
        return active_title_switch;
    }

    public void setActive_title_switch(String active_title_switch) {
        this.active_title_switch = active_title_switch;
    }

    public String getCharge_page_style() {
        return charge_page_style;
    }

    public void setCharge_page_style(String charge_page_style) {
        this.charge_page_style = charge_page_style;
    }

    public String getRegister_btn_pos_sort() {
        return register_btn_pos_sort;
    }

    public void setRegister_btn_pos_sort(String register_btn_pos_sort) {
        this.register_btn_pos_sort = register_btn_pos_sort;
    }

    public String getNative_style_code() {
//        return String.valueOf(Constant.EASY_LIGHT_APP_STYLE1);
        return native_style_code;
    }

    public void setNative_style_code(String native_style_code) {
        this.native_style_code = native_style_code;
    }

    public String getLottery_page_logo_url() {
        return lottery_page_logo_url;
    }

    public void setLottery_page_logo_url(String lottery_page_logo_url) {
        this.lottery_page_logo_url = lottery_page_logo_url;
    }

    public String getOnoff_turnlate() {
        return onoff_turnlate;
    }

    public void setOnoff_turnlate(String onoff_turnlate) {
        this.onoff_turnlate = onoff_turnlate;
    }

    public String getApp_download_link_ios() {
        return app_download_link_ios;
    }

    public void setApp_download_link_ios(String app_download_link_ios) {
        this.app_download_link_ios = app_download_link_ios;
    }

    public String getApp_download_link_android() {
        return app_download_link_android;
    }

    public void setApp_download_link_android(String app_download_link_android) {
        this.app_download_link_android = app_download_link_android;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getExchange_score() {
        return exchange_score;
    }

    public void setExchange_score(String exchange_score) {
        this.exchange_score = exchange_score;
    }

    public String getBankFlag() {
        return bankFlag;
    }

    public void setBankFlag(String bankFlag) {
        this.bankFlag = bankFlag;
    }

    public String getFastFlag() {
        return fastFlag;
    }

    public void setFastFlag(String fastFlag) {
        this.fastFlag = fastFlag;
    }

    public String getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public String getOnline_customer_showphone() {
        return online_customer_showphone;
    }

    public void setOnline_customer_showphone(String online_customer_showphone) {
        this.online_customer_showphone = online_customer_showphone;
    }

    public String getOnoff_member_mobile_red_packet() {
        return onoff_member_mobile_red_packet;
    }

    public void setOnoff_member_mobile_red_packet(String onoff_member_mobile_red_packet) {
        this.onoff_member_mobile_red_packet = onoff_member_mobile_red_packet;
    }

    public String getCustomerServiceUrlLink() {
        return customerServiceUrlLink;
    }

    public void setCustomerServiceUrlLink(String customerServiceUrlLink) {
        this.customerServiceUrlLink = customerServiceUrlLink;
    }

    public String getLive700_secret() {
        return live700_secret;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOnoff_mobile_drawing() {
        return onoff_mobile_drawing;
    }

    public void setOnoff_mobile_drawing(String onoff_mobile_drawing) {
        this.onoff_mobile_drawing = onoff_mobile_drawing;
    }

    public String getOnoff_mobile_recharge() {
        return onoff_mobile_recharge;
    }

    public void setOnoff_mobile_recharge(String onoff_mobile_recharge) {
        this.onoff_mobile_recharge = onoff_mobile_recharge;
    }

    public String getYjf() {
        return yjf;
    }

    public void setYjf(String yjf) {
        this.yjf = yjf;
    }

    public String getOnoff_lottery_game() {
        return onoff_lottery_game;
    }

    public void setOnoff_lottery_game(String onoff_lottery_game) {
        this.onoff_lottery_game = onoff_lottery_game;
    }

    public String getOnoff_liu_he_cai() {
        return onoff_liu_he_cai;
    }

    public void setOnoff_liu_he_cai(String onoff_liu_he_cai) {
        this.onoff_liu_he_cai = onoff_liu_he_cai;
    }

    public String getOnoff_sports_game() {
        return onoff_sports_game;
    }

    public void setOnoff_sports_game(String onoff_sports_game) {
        this.onoff_sports_game = onoff_sports_game;
    }

    public String getOnoff_zhen_ren_yu_le() {
        return onoff_zhen_ren_yu_le;
    }

    public void setOnoff_zhen_ren_yu_le(String onoff_zhen_ren_yu_le) {
        this.onoff_zhen_ren_yu_le = onoff_zhen_ren_yu_le;
    }

    public String getOnoff_dian_zi_you_yi() {
        return onoff_dian_zi_you_yi;
    }

    public void setOnoff_dian_zi_you_yi(String onoff_dian_zi_you_yi) {
        this.onoff_dian_zi_you_yi = onoff_dian_zi_you_yi;
    }

    public String getIosExamine() {
        return iosExamine;
    }

    public void setIosExamine(String iosExamine) {
        this.iosExamine = iosExamine;
    }

    public String getMobileIndex() {
        return mobileIndex;
    }

    public void setMobileIndex(String mobileIndex) {
        this.mobileIndex = mobileIndex;
    }

    public String getOnoff_change_money() {
        return onoff_change_money;
    }

    public void setOnoff_change_money(String onoff_change_money) {
        this.onoff_change_money = onoff_change_money;
    }

    public String getOnoff_mobile_guest_register() {
        return onoff_mobile_guest_register;
    }

    public void setOnoff_mobile_guest_register(String onoff_mobile_guest_register) {
        this.onoff_mobile_guest_register = onoff_mobile_guest_register;
    }

    public String getOnoff_sign_in() {
        return onoff_sign_in;
    }

    public void setOnoff_sign_in(String onoff_sign_in) {
        this.onoff_sign_in = onoff_sign_in;
    }

    public String getApp_qr_code_link_ios() {
        return app_qr_code_link_ios;
    }

    public void setApp_qr_code_link_ios(String app_qr_code_link_ios) {
        this.app_qr_code_link_ios = app_qr_code_link_ios;
    }

    public String getApp_qr_code_link_android() {
        return app_qr_code_link_android;
    }

    public void setApp_qr_code_link_android(String app_qr_code_link_android) {
        this.app_qr_code_link_android = app_qr_code_link_android;
    }

    public String getLottery_order_cancle_switch() {
        return lottery_order_cancle_switch;
    }

    public void setLottery_order_cancle_switch(String lottery_order_cancle_switch) {
        this.lottery_order_cancle_switch = lottery_order_cancle_switch;
    }

    public String getLottery_order_chase_switch() {
        return lottery_order_chase_switch;
    }

    public void setLottery_order_chase_switch(String lottery_order_chase_switch) {
        this.lottery_order_chase_switch = lottery_order_chase_switch;
    }

    public String getOnoff_register() {
        return onoff_register;
    }

    public void setOnoff_register(String onoff_register) {
        this.onoff_register = onoff_register;
    }

    public String getOnoff_mobile_app_reg() {
        return onoff_mobile_app_reg;
    }

    public void setOnoff_mobile_app_reg(String onoff_mobile_app_reg) {
        this.onoff_mobile_app_reg = onoff_mobile_app_reg;
    }

    public String getUsercenter_level_show_switch() {
        return usercenter_level_show_switch;
    }

    public void setUsercenter_level_show_switch(String usercenter_level_show_switch) {
        this.usercenter_level_show_switch = usercenter_level_show_switch;
    }

    public String getOnoff_show_mode() {
        return onoff_show_mode;
    }

    public void setOnoff_show_mode(String onoff_show_mode) {
        this.onoff_show_mode = onoff_show_mode;
    }

    public String getMobile_remember_loginpsd() {
        return mobile_remember_loginpsd;
    }

    public void setMobile_remember_loginpsd(String mobile_remember_loginpsd) {
        this.mobile_remember_loginpsd = mobile_remember_loginpsd;
    }

    public String getOnoff_show_remember_psd() {
        return onoff_show_remember_psd;
    }

    public void setOnoff_show_remember_psd(String onoff_show_remember_psd) {
        this.onoff_show_remember_psd = onoff_show_remember_psd;
    }

    public String getOnoff_show_winning_data() {
        return onoff_show_winning_data;
    }

    public void setOnoff_show_winning_data(String onoff_show_winning_data) {
        this.onoff_show_winning_data = onoff_show_winning_data;
    }

    public String getMember_multi_bank_card_permission() {
//        return "off";
        return member_multi_bank_card_permission;
    }

    public String getOn_off_alipay_withdrawal() {
        return on_off_alipay_withdrawal;
    }

    public void setOn_off_alipay_withdrawal(String on_off_alipay_withdrawal) {
        this.on_off_alipay_withdrawal = on_off_alipay_withdrawal;
    }

    public String getOn_off_usdt_withdrawal() {
        return on_off_usdt_withdrawal;
    }

    public void setOn_off_usdt_withdrawal(String on_off_usdt_withdrawal) {
        this.on_off_usdt_withdrawal = on_off_usdt_withdrawal;
    }

    public String getOn_off_gopay_withdrawal() {
        return on_off_gopay_withdrawal;
    }

    public void setOn_off_gopay_withdrawal(String on_off_gopay_withdrawal) {
        this.on_off_gopay_withdrawal = on_off_gopay_withdrawal;
    }

    public String getOn_off_okpay_withdrawal() {
        return on_off_okpay_withdrawal;
    }

    public void setOn_off_okpay_withdrawal(String on_off_okpay_withdrawal) {
        this.on_off_okpay_withdrawal = on_off_okpay_withdrawal;
    }

    public String getOn_off_topay_withdrawal() {
        return on_off_topay_withdrawal;
    }

    public void setOn_off_topay_withdrawal(String on_off_topay_withdrawal) {
        this.on_off_topay_withdrawal = on_off_topay_withdrawal;
    }

    public String getOn_off_vpay_withdrawal() {
        return on_off_vpay_withdrawal;
    }

    public void setOn_off_vpay_withdrawal(String on_off_vpay_withdrawal) {
        this.on_off_vpay_withdrawal = on_off_vpay_withdrawal;
    }

    public String getUsdt_rate() {
        return usdt_rate;
    }

    public void setUsdt_rate(String usdt_rate) {
        this.usdt_rate = usdt_rate;
    }

    public String getUsdt_out_rate() {
        return usdt_out_rate;
    }

    public void setUsdt_out_rate(String usdt_out_rate) {
        this.usdt_out_rate = usdt_out_rate;
    }

    public String getUsdt_url() {
        return usdt_url;
    }

    public void setUsdt_url(String usdt_url) {
        this.usdt_url = usdt_url;
    }

    public String getUsdt_show() {
        return usdt_show;
    }

    public void setUsdt_show(String usdt_show) {
        this.usdt_show = usdt_show;
    }

    public String getOnoff_new_pay_mode() {
        return onoff_new_pay_mode;
    }

    public void setOnoff_new_pay_mode(String onoff_new_pay_mode) {
        this.onoff_new_pay_mode = onoff_new_pay_mode;
    }

    public String getOnoff_mobile_unread_msg_popups() {
        return onoff_mobile_unread_msg_popups;
    }

    public void setOnoff_mobile_unread_msg_popups(String onoff_mobile_unread_msg_popups) {
        this.onoff_mobile_unread_msg_popups = onoff_mobile_unread_msg_popups;
    }

    public String getMobile_station_index_logo() {
        return mobile_station_index_logo;
    }

    public void setMobile_station_index_logo(String mobile_station_index_logo) {
        this.mobile_station_index_logo = mobile_station_index_logo;
    }

    public String getApp_download_info() {
        return app_download_info;
    }

    public void setApp_download_info(String app_download_info) {
        this.app_download_info = app_download_info;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getSports_min_bet_money() {
        return sports_min_bet_money;
    }

    public void setSports_min_bet_money(String sports_min_bet_money) {
        this.sports_min_bet_money = sports_min_bet_money;
    }

    public String getSports_max_bet_money() {
        return sports_max_bet_money;
    }

    public void setSports_max_bet_money(String sports_max_bet_money) {
        this.sports_max_bet_money = sports_max_bet_money;
    }

    public String getNative_vertify_captcha_switch() {
        return native_vertify_captcha_switch;
    }

    public void setNative_vertify_captcha_switch(String native_vertify_captcha_switch) {
        this.native_vertify_captcha_switch = native_vertify_captcha_switch;
    }

    public String getWithdrawNotInteger() {
        return withdrawNotInteger;
    }

    public void setWithdrawNotInteger(String withdrawNotInteger) {
        this.withdrawNotInteger = withdrawNotInteger;
    }

    public String getOn_off_guest_redperm() {
        return on_off_guest_redperm;
    }

    public void setOn_off_guest_redperm(String on_off_guest_redperm) {
        this.on_off_guest_redperm = on_off_guest_redperm;
    }

    public String getCache_data_local_switch() {
        return cache_data_local_switch;
    }

    public void setCache_data_local_switch(String cache_data_local_switch) {
        this.cache_data_local_switch = cache_data_local_switch;
    }

    public String getNewmainpage_switch() {
        return newmainpage_switch;
    }

    public String getMainpage_function_menu() {
        return mainpage_function_menu;
    }

    public String getLogin_reg_background_image_url() {
        return login_reg_background_image_url;
    }

    public void setLogin_reg_background_image_url(String login_reg_background_image_url) {
        this.login_reg_background_image_url = login_reg_background_image_url;
    }

    public String getOn_off_direct_charge() {
        return on_off_direct_charge;
    }

    public String getDirect_charge_gift_money_percent() {
        return direct_charge_gift_money_percent;
    }

    public String getBuy_lottery_change_sale_activity() {
        return buy_lottery_change_sale_activity;
    }

    public void setBuy_lottery_change_sale_activity(String buy_lottery_change_sale_activity) {
        this.buy_lottery_change_sale_activity = buy_lottery_change_sale_activity;
    }

    public String getStation_currency_type_permission() {
        return station_currency_type_permission;
    }

    public void setStation_currency_type_permission(String station_currency_type_permission) {
        this.station_currency_type_permission = station_currency_type_permission;
    }
}
