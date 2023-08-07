package com.example.anuo.immodule.utils;

import java.util.List;

/*
 * 聊天室系统配置
 * */
public class ChatSysConfig {

    /**
     * file_url : http://127.0.0.1:8091/add_file
     * name_station_online_service_link :
     * name_red_bag_remark_info : 恭喜发财,大吉大利!
     * switch_front_bet_show : 1
     * name_vip_nick_name_update_num : 0
     * switch_lottery_result_default_type_show :
     * switch_room_show : 1
     * switch_room_voice : 0
     * name_word_color_info :
     * switch_check_in_show : 1
     * name_room_people_num : 0
     * switch_open_simple_chat_room_show : 0
     * switch_user_name_show : 1
     * switch_chat_word_size_show :
     * switch_send_image_show : 1
     * switch_winning_list_show : 1
     * name_new_members_default_photo :
     * switch_front_admin_ban_send : 1
     * switch_level_ico_show : 1
     * name_user_win_tips_per : 100
     * switch_level_show : 1
     * switch_room_people_admin_show : 0
     * name_backGround_color_info :
     * switch_room_tips_show : 1
     * switch_lottery_result_show : 0
     * switch_red_bag_remark_show : 1
     * switch_msg_tips_show : 1
     * switch_yingkui_show : 1
     * switch_red_bag_send : 1
     * switch_red_info : 1
     * name_level_title_color_info :
     */

    private String file_url;
    private String name_station_online_service_link; //站在在线客服链接
    private String name_red_bag_remark_info = "恭喜发财,大吉大利"; //红包默认备注文字
    private String switch_front_bet_show = "1"; //前台投注开关
    private String name_vip_nick_name_update_num = "0"; //会员修改昵称次数
    private String switch_lottery_result_default_type_show; //开奖结果默认彩种
    private String switch_room_show = "1"; //房间列表默认开关
    private String switch_room_voice = "0"; //消息发送提示音
    private String name_word_color_info; //聊天字体颜色选择
    private String switch_check_in_show = "1"; //显示签到开关
    private String name_room_people_num = "0"; //追加房间在线人数
    private String switch_open_simple_chat_room_show = "0"; //简版聊天室开关
    private String switch_user_name_show = "1"; //自定义用户名开关
    private String switch_chat_word_size_show; //聊天室字体大小
    private String switch_send_image_show = "1"; //发送图片开关
    private String switch_winning_list_show = "1"; //中奖榜单开关
    private String name_new_members_default_photo; //新会员默认头像
    private String switch_front_admin_ban_send = "1"; //启用前台管理员发送违禁词
    private String switch_level_ico_show = "1"; //等级图标显示开关
    private String name_user_win_tips_per = "100"; //投注命中率 用户的胜率高于该胜率显示
    private String switch_level_show = "1"; //等级显示开关
    private String switch_room_people_admin_show = "0"; //在线人数只有管理员可见
    private String switch_lottery_result_show = "1"; //开奖结果显示开关
    private String switch_red_bag_remark_show = "1"; //红包是否显示备注文字开关
    private String switch_msg_receive_notify = "0"; //消息接收提示音开关
    private String switch_yingkui_show = "1"; //盈亏显示开关
    private String switch_red_bag_send = "1"; //发送红包开关
    private String switch_red_info = "1"; //红包领取详情开关
    private String switch_new_msg_notification = "0";//聊天室后台消息通知开关
    private String switch_bet_num_show = "1";//个人打码量显示开关
    private String switch_sum_recharge_show = "1";//会员总充值显示开关
    private String switch_today_recharge_show = "1";//会员今日充值显示开关
    private String switch_room_tips_show = "1"; //进房提示开关
    private String switch_winning_banner_show = "1";//中奖弹幕开关
    /**
     * name_cut_dragon_continue_stage : 0
     * switch_agent_enter_public_room : 1
     * switch_long_dragon_show : 0
     * switch_ban_speak_del_msg : 1
     * switch_red_bag_IP_once : 1
     * name_bullet_screen_show_time_info : 10
     * name_backGround_color_info : [{"color":"c732ac","levelName":"后台管理"},{"color":"32c73c","levelName":"渣渣会员"},{"color":"32b8c7","levelName":"砖石会员"},{"color":"9832c7","levelName":"王者会员"},{"color":"c7c732","levelName":"天王会员"},{"color":"c75232","levelName":"帝尊会员"}]
     * switch_plan_list_show : 1
     * switch_plan_user_show : 1
     * switch_currency_unit_show : yuan:1,jiao:0,fen:0
     * name_level_title_color_info : [{"color":"c732ac","levelName":"后台管理"},{"color":"32c73c","levelName":"渣渣会员"},{"color":"32b8c7","levelName":"砖石会员"},{"color":"9832c7","levelName":"王者会员"},{"color":"c7c732","levelName":"天王会员"},{"color":"c75232","levelName":"帝尊会员"}]
     */

    private String name_cut_dragon_continue_stage;
    private String switch_agent_enter_public_room;
    private String switch_long_dragon_show;//长龙开关
    private String switch_ban_speak_del_msg;//禁言同时是否删除消息
    private String switch_red_bag_IP_once;
    private String name_bullet_screen_show_time_info;
    private String switch_plan_list_show;//彩票计划开关
    private String switch_plan_user_show;//导师计划开关
    private String switch_currency_unit_show;
    private List<NameBackGroundColorInfoBean> native_name_backGround_color_info;//聊天背景颜色选择
    private List<NameLevelTitleColorInfoBean> native_name_level_title_color_info;//等级文字颜色选择
    private String name_win_lottery_animation_interval;//开奖结果间隔
    /**
     * switch_history_perpage_count : 15:15
     * switch_enter_room_toast_animation : 0
     */

    private String switch_history_perpage_count;//聊天记录拉取历史记录数目
    private String switch_enter_room_toast_animation;
    private String name_auto_share_bet_arrive_amount = "0";//达到投注金额自动分享注单(元）
    private String switch_local_ava = "0";//本地自定义头像修改开关 0:关闭  1:开启
    private String switch_today_earnings_list_info = "";//今日盈利榜单开关
    private String switch_fuli_logo_config = "0"; // ("福利图标是否显示", "select", "0", "系统配置", 1075, "0:否;1:是"),
    private String name_every_speaking_word_limit = "100"; //会员每次发言限制多少字数
    private String switch_msg_tips_show = "1"; //消息提示音

    public String getSwitch_fuli_logo_config() {
        return switch_fuli_logo_config;
    }

    public String getSwitch_today_earnings_list_info() {
        return switch_today_earnings_list_info;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getName_station_online_service_link() {
        return name_station_online_service_link;
    }

    public void setName_station_online_service_link(String name_station_online_service_link) {
        this.name_station_online_service_link = name_station_online_service_link;
    }

    public String getName_red_bag_remark_info() {
        return name_red_bag_remark_info;
    }

    public void setName_red_bag_remark_info(String name_red_bag_remark_info) {
        this.name_red_bag_remark_info = name_red_bag_remark_info;
    }

    public String getSwitch_front_bet_show() {
        return switch_front_bet_show;
    }

    public void setSwitch_front_bet_show(String switch_front_bet_show) {
        this.switch_front_bet_show = switch_front_bet_show;
    }

    public String getName_vip_nick_name_update_num() {
        return name_vip_nick_name_update_num;
    }

    public void setName_vip_nick_name_update_num(String name_vip_nick_name_update_num) {
        this.name_vip_nick_name_update_num = name_vip_nick_name_update_num;
    }

    public String getSwitch_lottery_result_default_type_show() {
        return switch_lottery_result_default_type_show;
    }

    public void setSwitch_lottery_result_default_type_show(String switch_lottery_result_default_type_show) {
        this.switch_lottery_result_default_type_show = switch_lottery_result_default_type_show;
    }

    public String getSwitch_room_show() {
        return switch_room_show;
    }

    public void setSwitch_room_show(String switch_room_show) {
        this.switch_room_show = switch_room_show;
    }

    public String getSwitch_room_voice() {
        return switch_room_voice;
    }

    public void setSwitch_room_voice(String switch_room_voice) {
        this.switch_room_voice = switch_room_voice;
    }

    public String getName_word_color_info() {
        return name_word_color_info;
    }

    public void setName_word_color_info(String name_word_color_info) {
        this.name_word_color_info = name_word_color_info;
    }

    public String getSwitch_check_in_show() {
        return switch_check_in_show;
    }

    public void setSwitch_check_in_show(String switch_check_in_show) {
        this.switch_check_in_show = switch_check_in_show;
    }

    public String getName_room_people_num() {
        return name_room_people_num;
    }

    public void setName_room_people_num(String name_room_people_num) {
        this.name_room_people_num = name_room_people_num;
    }

    public String getSwitch_open_simple_chat_room_show() {
        return switch_open_simple_chat_room_show;
    }

    public void setSwitch_open_simple_chat_room_show(String switch_open_simple_chat_room_show) {
        this.switch_open_simple_chat_room_show = switch_open_simple_chat_room_show;
    }

    public String getSwitch_user_name_show() {
        return switch_user_name_show;
    }

    public void setSwitch_user_name_show(String switch_user_name_show) {
        this.switch_user_name_show = switch_user_name_show;
    }

    public String getSwitch_chat_word_size_show() {
        return switch_chat_word_size_show;
    }

    public void setSwitch_chat_word_size_show(String switch_chat_word_size_show) {
        this.switch_chat_word_size_show = switch_chat_word_size_show;
    }

    public String getSwitch_send_image_show() {
        return switch_send_image_show;
    }

    public void setSwitch_send_image_show(String switch_send_image_show) {
        this.switch_send_image_show = switch_send_image_show;
    }

    public String getSwitch_winning_list_show() {
        return switch_winning_list_show;
    }

    public void setSwitch_winning_list_show(String switch_winning_list_show) {
        this.switch_winning_list_show = switch_winning_list_show;
    }

    public String getName_new_members_default_photo() {
        return name_new_members_default_photo;
    }

    public void setName_new_members_default_photo(String name_new_members_default_photo) {
        this.name_new_members_default_photo = name_new_members_default_photo;
    }

    public String getSwitch_front_admin_ban_send() {
        return switch_front_admin_ban_send;
    }

    public void setSwitch_front_admin_ban_send(String switch_front_admin_ban_send) {
        this.switch_front_admin_ban_send = switch_front_admin_ban_send;
    }

    public String getSwitch_level_ico_show() {
        return switch_level_ico_show;
    }

    public void setSwitch_level_ico_show(String switch_level_ico_show) {
        this.switch_level_ico_show = switch_level_ico_show;
    }

    public String getName_user_win_tips_per() {
        return name_user_win_tips_per;
    }

    public void setName_user_win_tips_per(String name_user_win_tips_per) {
        this.name_user_win_tips_per = name_user_win_tips_per;
    }

    public String getSwitch_level_show() {
        return switch_level_show;
    }

    public void setSwitch_level_show(String switch_level_show) {
        this.switch_level_show = switch_level_show;
    }

    public String getSwitch_room_people_admin_show() {
        return switch_room_people_admin_show;
    }

    public void setSwitch_room_people_admin_show(String switch_room_people_admin_show) {
        this.switch_room_people_admin_show = switch_room_people_admin_show;
    }

    public String getName_win_lottery_animation_interval() {
        return name_win_lottery_animation_interval;
    }

    public void setName_win_lottery_animation_interval(String name_win_lottery_animation_interval) {
        this.name_win_lottery_animation_interval = name_win_lottery_animation_interval;
    }

    //    public String getName_backGround_color_info() {
//        return name_backGround_color_info;
//    }
//
//    public void setName_backGround_color_info(String name_backGround_color_info) {
//        this.name_backGround_color_info = name_backGround_color_info;
//    }

    public String getSwitch_room_tips_show() {
        return switch_room_tips_show;
    }

    public void setSwitch_room_tips_show(String switch_room_tips_show) {
        this.switch_room_tips_show = switch_room_tips_show;
    }

    public String getSwitch_lottery_result_show() {
        return switch_lottery_result_show;
    }

    public void setSwitch_lottery_result_show(String switch_lottery_result_show) {
        this.switch_lottery_result_show = switch_lottery_result_show;
    }

    public String getSwitch_red_bag_remark_show() {
        return switch_red_bag_remark_show;
    }

    public void setSwitch_red_bag_remark_show(String switch_red_bag_remark_show) {
        this.switch_red_bag_remark_show = switch_red_bag_remark_show;
    }

    public String getSwitch_msg_receive_notify() {
        return switch_msg_receive_notify;
    }

    public void setSwitch_msg_receive_notify(String switch_msg_receive_notify) {
        this.switch_msg_receive_notify = switch_msg_receive_notify;
    }

    public String getSwitch_yingkui_show() {
        return switch_yingkui_show;
    }

    public void setSwitch_yingkui_show(String switch_yingkui_show) {
        this.switch_yingkui_show = switch_yingkui_show;
    }

    public String getSwitch_red_bag_send() {
        return switch_red_bag_send;
    }

    public void setSwitch_red_bag_send(String switch_red_bag_send) {
        this.switch_red_bag_send = switch_red_bag_send;
    }

    public String getSwitch_red_info() {
        return switch_red_info;
    }

    public void setSwitch_red_info(String switch_red_info) {
        this.switch_red_info = switch_red_info;
    }
//
//    public String getName_level_title_color_info() {
//        return name_level_title_color_info;
//    }
//
//    public void setName_level_title_color_info(String name_level_title_color_info) {
//        this.name_level_title_color_info = name_level_title_color_info;
//    }

    public String getSwitch_new_msg_notification() {
        return switch_new_msg_notification;
    }

    public void setSwitch_new_msg_notification(String switch_new_msg_notification) {
        this.switch_new_msg_notification = switch_new_msg_notification;
    }

    public String getSwitch_bet_num_show() {
        return switch_bet_num_show;
    }

    public void setSwitch_bet_num_show(String switch_bet_num_show) {
        this.switch_bet_num_show = switch_bet_num_show;
    }

    public String getName_cut_dragon_continue_stage() {
        return name_cut_dragon_continue_stage;
    }

    public void setName_cut_dragon_continue_stage(String name_cut_dragon_continue_stage) {
        this.name_cut_dragon_continue_stage = name_cut_dragon_continue_stage;
    }

    public String getSwitch_agent_enter_public_room() {
        return switch_agent_enter_public_room;
    }

    public void setSwitch_agent_enter_public_room(String switch_agent_enter_public_room) {
        this.switch_agent_enter_public_room = switch_agent_enter_public_room;
    }

    public String getSwitch_long_dragon_show() {
        return switch_long_dragon_show;
    }

    public void setSwitch_long_dragon_show(String switch_long_dragon_show) {
        this.switch_long_dragon_show = switch_long_dragon_show;
    }

    public String getSwitch_ban_speak_del_msg() {
        return switch_ban_speak_del_msg;
    }

    public void setSwitch_ban_speak_del_msg(String switch_ban_speak_del_msg) {
        this.switch_ban_speak_del_msg = switch_ban_speak_del_msg;
    }

    public String getSwitch_red_bag_IP_once() {
        return switch_red_bag_IP_once;
    }

    public void setSwitch_red_bag_IP_once(String switch_red_bag_IP_once) {
        this.switch_red_bag_IP_once = switch_red_bag_IP_once;
    }

    public String getName_bullet_screen_show_time_info() {
        return name_bullet_screen_show_time_info;
    }

    public void setName_bullet_screen_show_time_info(String name_bullet_screen_show_time_info) {
        this.name_bullet_screen_show_time_info = name_bullet_screen_show_time_info;
    }

    public String getSwitch_plan_list_show() {
        return switch_plan_list_show;
    }

    public void setSwitch_plan_list_show(String switch_plan_list_show) {
        this.switch_plan_list_show = switch_plan_list_show;
    }

    public String getSwitch_plan_user_show() {
        return switch_plan_user_show;
    }

    public void setSwitch_plan_user_show(String switch_plan_user_show) {
        this.switch_plan_user_show = switch_plan_user_show;
    }

    public String getSwitch_currency_unit_show() {
        return switch_currency_unit_show;
    }

    public void setSwitch_currency_unit_show(String switch_currency_unit_show) {
        this.switch_currency_unit_show = switch_currency_unit_show;
    }

    public List<NameBackGroundColorInfoBean> getNative_name_backGround_color_info() {
        return native_name_backGround_color_info;
    }

    public void setNative_name_backGround_color_info(List<NameBackGroundColorInfoBean> native_name_backGround_color_info) {
        this.native_name_backGround_color_info = native_name_backGround_color_info;
    }

    public List<NameLevelTitleColorInfoBean> getNative_name_level_title_color_info() {
        return native_name_level_title_color_info;
    }

    public void setNative_name_level_title_color_info(List<NameLevelTitleColorInfoBean> native_name_level_title_color_info) {
        this.native_name_level_title_color_info = native_name_level_title_color_info;
    }

    public String getSwitch_history_perpage_count() {
        return switch_history_perpage_count;
    }

    public void setSwitch_history_perpage_count(String switch_history_perpage_count) {
        this.switch_history_perpage_count = switch_history_perpage_count;
    }

    public String getSwitch_enter_room_toast_animation() {
        return switch_enter_room_toast_animation;
    }

    public void setSwitch_enter_room_toast_animation(String switch_enter_room_toast_animation) {
        this.switch_enter_room_toast_animation = switch_enter_room_toast_animation;
    }

    public String getSwitch_winning_banner_show() {
        return switch_winning_banner_show;
    }

    public void setSwitch_winning_banner_show(String switch_winning_banner_show) {
        this.switch_winning_banner_show = switch_winning_banner_show;
    }

    public String getSwitch_sum_recharge_show() {
        return switch_sum_recharge_show;
    }

    public void setSwitch_sum_recharge_show(String switch_sum_recharge_show) {
        this.switch_sum_recharge_show = switch_sum_recharge_show;
    }

    public String getSwitch_today_recharge_show() {
        return switch_today_recharge_show;
    }

    public void setSwitch_today_recharge_show(String switch_today_recharge_show) {
        this.switch_today_recharge_show = switch_today_recharge_show;
    }

    public static class NameBackGroundColorInfoBean {
        /**
         * color : c732ac
         * levelName : 后台管理
         */

        private String color;
        private String levelName;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }
    }

    public static class NameLevelTitleColorInfoBean {
        /**
         * color : c732ac
         * levelName : 后台管理
         */

        private String color;
        private String levelName;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }
    }

    public String getName_auto_share_bet_arrive_amount() {
        return name_auto_share_bet_arrive_amount;
    }

    public void setName_auto_share_bet_arrive_amount(String name_auto_share_bet_arrive_amount) {
        this.name_auto_share_bet_arrive_amount = name_auto_share_bet_arrive_amount;
    }

    public String getSwitch_local_ava() {
        return switch_local_ava;
    }

    public void setSwitch_local_ava(String switch_local_ava) {
        this.switch_local_ava = switch_local_ava;
    }

    public String getName_every_speaking_word_limit() {
        return name_every_speaking_word_limit;
    }

    public void setName_every_speaking_word_limit(String name_every_speaking_word_limit) {
        this.name_every_speaking_word_limit = name_every_speaking_word_limit;
    }
}
