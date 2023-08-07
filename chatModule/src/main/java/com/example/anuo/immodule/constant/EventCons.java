package com.example.anuo.immodule.constant;

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
 * Date  :29/07/2019
 * Desc  :事件标记常量类
 */
public class EventCons {
    //事件标记
    public static final String DIALOG_DISMISS = "dialog_dismiss";
    //显示投注页标记
    public static final String SHOW_ACTIVITY = "show_activity";
    //投注页是否已经实例
    public static final String ACTIVITY_RUNNING = "activity_running";
    //销毁投注页
    public static final String FINISH_ACTIVITY = "finish_activity";
    //彩种选择事件
    public static final String CHOOSE_LOTTERY = "choose_lottery";
    //彩票玩法选中事件
    public static final String LOTTERY_RULE_SELECTED = "lottery_rule_selected";
    //聊天室投注界面头部显示
    public static final String SHOW_TITLE = "show_title";
    //更新投注总数和金额，信用版
    public static final String REFRESH_TOTAL_ORDER = "refresh_total_order";
    //清空投注单
    public static final String CLEAN_ORDER = "clean_order";
    //官方下注总数
    public static final String GF_TOTAL_ORDER = "gf_total_order";
    //官方投注事件，待确认
    public static final String GF_BETTING = "gf_betting";
    //信用投注事件，待确认
    public static final String XY_BETTING = "xy_betting";
    //官方投注事件，已确认
    public static final String GF_CONFIRM_BETTING = "gf_confirm_betting";
    //信用投注事件，已确认
    public static final String XY_CONFIRM_BETTING = "xy_confirm_betting";
    //官方进入确认页
    public static final String GF_INTO_CONFIRM = "gf_into_confirm";
    //信用进入确认页
    public static final String XY_INTO_CONFIRM = "xy_into_confirm";
    //倍数和模式改变事件
    public static final String BEISHU_MODE_EXCHANGE = "beishu_mode_exchange";
    //注数和金额显示事件
    public static final String ORDER_AMOUNT = "order_amount";
    //赔率结果List<PeilvWebResult>
    public static final String PEI_LV_LIST = "pei_lv_list";
    //计算信用自定义金额
    public static final String CALCULATE_MONEY = "calculate_money";
    //官方注单消息
    public static final String GF_BET_MSG = "gf_bet_msg";
    //信用注单消息
    public static final String XY_BET_MSG = "xy_bet_msg";
    //历史注单列表分享注单消息
    public static final String SHARE_BET = "share_bet";
    //投注成功返回投注页
    public static final String BACK_BET_FRAGMENT = "back_bet_fragment";
    //投注成功后刷新余额
    public static final String REFRESH_AMOUNT = "refresh_amount";
    //初始化信用玩法的反水条
    public static final String INIT_XY_SEEK_BAR = "init_xy_seek_bar";
    //初始化官方玩法的反水条
    public static final String INIT_GF_SEEK_BAR = "init_gf_seek_bar";
    //投注完成后收起投注页
    public static final String MOVE_TO_BACK = "move_to_back";
    //删除图片
    public static final String DELETE_IMAGES = "delete_images";
    //图片收藏发送图片
    public static final String SEND_IMAGES = "send_images";
    //未读私聊消息
    public static final String UNREAD_MESSAGE = "un_read_message";

}
