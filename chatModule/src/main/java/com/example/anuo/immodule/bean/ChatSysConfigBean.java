package com.example.anuo.immodule.bean;

import com.example.anuo.immodule.utils.ChatSysConfig;

/*
 * 聊天室系统开关
 * */
public class ChatSysConfigBean {

    /**
     * msg : 操作成功。
     * code : R7031
     * success : true
     * source : {"file_url":"http://127.0.0.1:8091/add_file","name_station_online_service_link":"","name_red_bag_remark_info":"恭喜发财,大吉大利!","switch_front_bet_show":"1","name_vip_nick_name_update_num":"0","switch_lottery_result_default_type_show":"","switch_room_show":"1","switch_room_voice":"0","name_word_color_info":"","switch_check_in_show":"1","name_room_people_num":"0","switch_open_simple_chat_room_show":"0","switch_user_name_show":"1","switch_chat_word_size_show":"","switch_send_image_show":"1","switch_winning_list_show":"1","name_new_members_default_photo":"","switch_front_admin_ban_send":"1","switch_level_ico_show":"1","name_user_win_tips_per":"100","switch_level_show":"1","switch_room_people_admin_show":"0","name_backGround_color_info":"","switch_room_tips_show":"1","switch_lottery_result_show":"0","switch_red_bag_remark_show":"1","switch_msg_tips_show":"1","switch_yingkui_show":"1","switch_red_bag_send":"1","switch_red_info":"1","name_level_title_color_info":""}
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private ChatSysConfig source;
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

    public ChatSysConfig getSource() {
        return source;
    }

    public void setSource(ChatSysConfig source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
