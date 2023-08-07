package com.example.anuo.immodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.Switch;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.PersonPhotoAdapter;
import com.example.anuo.immodule.bean.ChatPersonDataBean;
import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatPersonDataView;
import com.example.anuo.immodule.jsonmodel.ModifyPersonDataModel;
import com.example.anuo.immodule.presenter.ChatPersonDataPresenter;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.GlideUtils;
import com.example.anuo.immodule.utils.PhotoUtil;
import com.example.anuo.immodule.view.ChatPersonDataView;
import com.example.anuo.immodule.view.CircleImageView;
import com.example.anuo.immodule.view.LastInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.Utils.Utils;

/*
 * 聊天室个人资料页面
 * */
public class PersonDataActivity extends ChatBaseActivity implements IChatPersonDataView {

    private static final String TAG = PersonDataActivity.class.getSimpleName();

    View mView;
    CircleImageView photoImg;
    LastInputEditText userName;
    ChatPersonDataView allWinMoney; //总盈利
    ChatPersonDataView todayWinMoney; //今日赢利
    ChatPersonDataView betMoney; //总投注
    ChatPersonDataView rechargeAll; //总充值
    ChatPersonDataView rechargeToday; //今日充值
    ChatPersonDataView dmlTxt; //打码量
    ChatPersonDataView noticeSendVioce; //消息发送提示音
    ChatPersonDataView noticeRecieveVioce; //消息接收提示音
    ChatPersonDataView noticeRoom; //进房提示
    ChatPersonDataView noticeMsg; //消息通知


    private ChatPersonDataPresenter presenter;
    private PopupWindow photoPopView; //点击换头像 弹窗
    private PhotoUtil photoUtil;
    private ChatSysConfig chatSysConfig;
    private ModifyPersonDataModel modifyPersonDataModel = null;

    private RecyclerView photoRc; //点击选择头像的列表
    private PersonPhotoAdapter photoAdapter;
    private List<String> photoData = new ArrayList<>();

    private int nowPos = -1; //当前用户选择的头像位置
    private boolean isRefresh = false; //是否在投注刷新数据

    /*
     * 用来判断用户是否修改
     * */
    private String photoUrl; //头像地址
    private String nickName; //昵称
    private String roomId;
    private Bitmap newPhotoBitmap;
    private String newPhotoType;

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_person_data;
    }


    @Override
    protected ChatBasePresenter initPresenter() {
        presenter = new ChatPersonDataPresenter(this, this);
        return presenter;
    }

    @Override
    protected void initView() {
        super.initView();

        mView = findViewById(R.id.act_person_data_layout);
        photoImg = findViewById(R.id.act_person_data_photo);
        userName = findViewById(R.id.act_person_data_user_name);
        allWinMoney = findViewById(R.id.act_person_data_all_win);
        todayWinMoney = findViewById(R.id.act_person_data_today_win);
        betMoney = findViewById(R.id.act_person_data_bet);
        rechargeAll = findViewById(R.id.act_person_data_recharge_all);
        rechargeToday = findViewById(R.id.act_person_data_recharge_today);
        dmlTxt = findViewById(R.id.act_person_data_dml);
        noticeSendVioce = findViewById(R.id.act_person_notice_send_voice);
        noticeRecieveVioce = findViewById(R.id.act_person_notice_recieve_voice);
        noticeRoom = findViewById(R.id.act_person_notice_room);
        noticeMsg = findViewById(R.id.act_person_notice_msg);

        tvMiddleTitle.setText("个人设置");
        roomId = this.getIntent().getStringExtra("roomId");
        photoUrl = this.getIntent().getStringExtra("photo");
        nickName = this.getIntent().getStringExtra("nickName");
        Utils.logd(TAG, "photoUrl = " + photoUrl);
        GlideUtils.loadHeaderPic(PersonDataActivity.this, photoUrl, photoImg);
        if (!TextUtils.isEmpty(nickName)) {
            userName.setText(nickName);
        } else {
            userName.setHint("暂无昵称");
        }
        chatSysConfig = ChatSpUtils.instance(this).getChatSysConfig();
        initPop();
        if (chatSysConfig.getSwitch_user_name_show().equals("0")) {
            findViewById(R.id.act_person_data_modify_name).setVisibility(View.GONE);
        }

        if (chatSysConfig.getSwitch_yingkui_show().equals("0")) {
            todayWinMoney.setVisibility(View.GONE);
        }

        if (chatSysConfig.getSwitch_bet_num_show().equals("0")) {
            dmlTxt.setVisibility(View.GONE);
        }

        if (noticeSendVioce.mSwitch != null) {
            if (TextUtils.isEmpty(ChatSpUtils.instance(this).getNoticeSendVoice())) {
                if (chatSysConfig.getSwitch_room_voice().equals("1")) {
                    noticeSendVioce.setCheck(true);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeSendVoice("1");
                } else {
                    noticeSendVioce.setCheck(false);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeSendVoice("0");
                }
            } else {
                if (ChatSpUtils.instance(this).getNoticeSendVoice().equals("1")) {
                    noticeSendVioce.setCheck(true);
                } else {
                    noticeSendVioce.setCheck(false);
                }
            }
            setCheckListener(noticeSendVioce.mSwitch, 0);
        }

        if (noticeRecieveVioce.mSwitch != null) {
            if (TextUtils.isEmpty(ChatSpUtils.instance(this).getNoticeRecieveVoice())) {
                if (chatSysConfig.getSwitch_msg_receive_notify().equals("1")) {
                    noticeRecieveVioce.setCheck(true);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeRecieveVoice("1");
                } else {
                    noticeRecieveVioce.setCheck(false);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeRecieveVoice("0");
                }

            } else {
                if (ChatSpUtils.instance(this).getNoticeRecieveVoice().equals("1")) {
                    noticeRecieveVioce.setCheck(true);
                } else {
                    noticeRecieveVioce.setCheck(false);
                }
            }
            setCheckListener(noticeRecieveVioce.mSwitch, 1);
        }

        if (noticeRoom.mSwitch != null) {
            if (TextUtils.isEmpty(ChatSpUtils.instance(this).getNoticeRoom())) {
                if (chatSysConfig.getSwitch_room_tips_show().equals("1")) {
                    noticeRoom.setCheck(true);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeRoom("1");
                } else {
                    noticeRoom.setCheck(false);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeRoom("0");
                }
            } else {
                if (ChatSpUtils.instance(this).getNoticeRoom().equals("1")) {
                    noticeRoom.setCheck(true);
                } else {
                    noticeRoom.setCheck(false);
                }
            }
            setCheckListener(noticeRoom.mSwitch, 2);
        }

        if (noticeMsg.mSwitch != null) {
            if (TextUtils.isEmpty(ChatSpUtils.instance(this).getNoticeMessage())) {
                if (chatSysConfig.getSwitch_new_msg_notification().equals("1")) {
                    noticeMsg.setCheck(true);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeMessage("1");
                } else {
                    noticeMsg.setCheck(false);
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeMessage("0");
                }
            } else {
                if (ChatSpUtils.instance(this).getNoticeMessage().equals("1")) {
                    noticeMsg.setCheck(true);
                } else {
                    noticeMsg.setCheck(false);
                }
            }
            setCheckListener(noticeMsg.mSwitch, 3);
        }

    }

    private void setCheckListener(Switch s, final int pos) {
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pos == 0) {
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeSendVoice(isChecked ? "1" : "0");
                } else if (pos == 1) {
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeRecieveVoice(isChecked ? "1" : "0");
                } else if (pos == 2) {
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeRoom(isChecked ? "1" : "0");
                } else if (pos == 3) {
                    ChatSpUtils.instance(PersonDataActivity.this).setNoticeMessage(isChecked ? "1" : "0");
                }
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        presenter.getPhotoList(ChatSpUtils.instance(this).getStationId(), ChatSpUtils.instance(this).getUserId());
//        presenter.getPersonData(ChatSpUtils.instance(this).getUserId(), roomId, false);
    }

    @Override
    protected void initListener() {
        photoImg.setOnClickListener(this);
        findViewById(R.id.act_person_data_modify_name).setOnClickListener(this);
        findViewById(R.id.act_person_data_commit).setOnClickListener(this);
        todayWinMoney.setChatPersonDataViewBtn(new ChatPersonDataView.ChatPersonDataViewBtn() {
            @Override
            public void onClick() {
                presenter.getPersonData(ChatSpUtils.instance(PersonDataActivity.this).getUserId(), roomId, true);
                isRefresh = true;
            }
        });

        betMoney.setChatPersonDataViewBtn(new ChatPersonDataView.ChatPersonDataViewBtn() {
            @Override
            public void onClick() { }
        });
    }

    private void initPop() {
        View popView = this.getLayoutInflater().inflate(R.layout.popup_person_photo, null);
        photoPopView = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        recordPopView.setOutsideTouchable(false);
//        recordPopView.setFocusable(false);
        setPopWindowDismiss(photoPopView);
        photoRc = popView.findViewById(R.id.popup_person_photo_rc);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        photoRc.setLayoutManager(layoutManager);
        photoAdapter = new PersonPhotoAdapter(this, photoData);
        photoRc.setAdapter(photoAdapter);
        photoAdapter.setPhotoItemClick(new PersonPhotoAdapter.PhotoItemClick() {
            @Override
            public void onClick(int position) {
                photoPopView.dismiss();
                if(position < photoData.size()){
                    GlideUtils.loadHeaderPic(PersonDataActivity.this, photoData.get(position), photoImg);
                    nowPos = position;
                    newPhotoBitmap = null;
                }else {
                    if (chatSysConfig.getSwitch_local_ava().equals("0")) {
                        showToast("无法自定义头像，请联系客服!");
                    }else {
                        if(photoUtil == null){
                            photoUtil = new PhotoUtil();
                        }
                        photoUtil.pickPhoto(PersonDataActivity.this);
                    }
                }
            }
        });
    }

    public void setPopWindowDismiss(PopupWindow pop) {
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeBgColor(false);
            }
        });
    }

    //弹窗的时候变背景颜色
    public void changeBgColor(boolean pop) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = pop ? 0.3f : 1f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    public String encodeImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT)
                .replace("data:image/jpeg;base64,", "")
                .replace("\n", "")
                .replace("\r", "");
    }

    private boolean isNameChanged() {
        String name = userName.getText().toString().trim();
        return !name.equals(nickName);
    }

    private boolean isPhotoChanged(){
        return (nowPos >= 0 && !photoData.get(nowPos).equals(photoUrl)) || newPhotoBitmap != null;
    }

    private double getBet(double win, double rebate, double bet) {
        double mon = win + rebate - bet;
        return mon;
    }

    private void requestToModifyPersonData(String photoUrl, String userName){
        modifyPersonDataModel = new ModifyPersonDataModel(ConfigCons.MODIFY_PERSON_DATA,
                userName, photoUrl, ChatSpUtils.instance(this).getStationId(),
                ChatSpUtils.instance(this).getUserId());

        presenter.modifyPersonData(modifyPersonDataModel);
    }

    @Override
    public void getPhotoList(ChatPersonPhotoListBean chatPersonPhotoListBean) {
        photoData.addAll(chatPersonPhotoListBean.getSource().getItems());
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPhotoUploaded(boolean isSuccess, String photoUrl) {
        Utils.logd(TAG, "onPhotoUploaded(), isSuccess = " + isSuccess + ", photoUrl = " + photoUrl);
        if(isSuccess){
            String name = userName.getText().toString().trim();
            requestToModifyPersonData(photoUrl, name);
        }else {
            showToast("上传头像操作发生错误");
        }
    }

    @Override
    public void getPersonData(ChatPersonDataBean chatPersonDataBean) {
        if (!isRefresh) {
            if (!TextUtils.isEmpty(chatPersonDataBean.getSource().getNickName())) {
                nickName = chatPersonDataBean.getSource().getNickName();
                userName.setText(chatPersonDataBean.getSource().getNickName());
            } else {
                userName.setHint("暂无昵称");
            }
            photoUrl = chatPersonDataBean.getSource().getAvatar();
            GlideUtils.loadHeaderPic(PersonDataActivity.this, photoUrl, photoImg);
        }

        if (chatPersonDataBean.getSource().getWinLost() != null) {
            ChatPersonDataBean.SourceBean.WinLostBean bean = chatPersonDataBean.getSource().getWinLost();
            allWinMoney.setText(CommonUtils.doubleToString(bean.getAllWinAmount()));
            todayWinMoney.setText(CommonUtils.doubleToString(bean.getYingkuiAmount()));
            betMoney.setText(CommonUtils.doubleToString(bean.getAllBetAmount()));
            rechargeAll.setText(CommonUtils.doubleToString(bean.getSumDepost()));
            rechargeToday.setText(CommonUtils.doubleToString(bean.getSumDepostToday()));
            dmlTxt.setText(CommonUtils.doubleToString(bean.getBetNum()));
//            double win = bean.getChessWinAmount() + bean.getEgameWinAmount() + bean.getLotteryWinAmount() + bean.getRealWinAmount() + bean.getSportsWinAmount() + bean.getSbSportsWinAmount();
//            allWinMoney.setText(CommonUtils.doubleToString(win));
//            double resMon = getBet(bean.getChessWinAmount(), bean.getChessRebateAmount(), bean.getChessBetAmount()) +
//                    getBet(bean.getEgameWinAmount(), bean.getEgameRebateAmount(), bean.getEgameBetAmount()) +
//                    getBet(bean.getLotteryWinAmount(), bean.getLotteryRebateAmount(), bean.getLotteryBetAmount()) +
//                    getBet(bean.getRealWinAmount(), bean.getRealRebateAmount(), bean.getRealBetAmount()) +
//                    getBet(bean.getSportsWinAmount(), bean.getSportsRebateAmount(), bean.getSportsBetAmount()) +
//                    getBet(bean.getSbSportsWinAmount(), bean.getSbSportsRebateAmount(), bean.getSbSportsBetAmount());
//            todayWinMoney.setText(CommonUtils.doubleToString(resMon));
//            double bet = bean.getChessBetAmount() + bean.getEgameBetAmount() + bean.getLotteryBetAmount() + bean.getRealBetAmount() + bean.getSportsBetAmount() + bean.getSbSportsBetAmount();
//            betMoney.setText(CommonUtils.doubleToString(bet));
        } else {
            showToast(presenter.getMsg());
        }
    }

    @Override
    public void ModifyPersonData(boolean isSuc) {
        if (isSuc) {
            showToast("修改成功");
            Intent intent = new Intent();
            if (modifyPersonDataModel != null) {
                intent.putExtra("model", modifyPersonDataModel);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.act_person_data_photo) {
            photoPopView.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
            changeBgColor(true);
        } else if (i == R.id.act_person_data_modify_name) {
            userName.setEnabled(true);
            userName.setFocusable(true);
            userName.setFocusableInTouchMode(true);
            userName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(userName, InputMethodManager.SHOW_IMPLICIT);
        } else if (i == R.id.act_person_data_commit) {
            if(isPhotoChanged()){
                if(newPhotoBitmap != null){//自定义头像
                    Utils.logd(TAG, "photo is custom changed");
                    String photo = encodeImage(newPhotoBitmap);
                    presenter.uploadAvatarPhoto(photo, newPhotoType);
                }else {//從photoList挑选图像的
                    Utils.logd(TAG, "photo is changed from list");
                    String name = userName.getText().toString().trim();
                    String url = photoData.get(nowPos);
                    requestToModifyPersonData(url, name);
                }
            }else if(isNameChanged()){
                String name = userName.getText().toString().trim();
                requestToModifyPersonData(photoUrl, name);
            }else {
                onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(photoUtil != null && resultCode == Activity.RESULT_OK){
            if(requestCode == PhotoUtil.CAMERA_REQUEST_CODE || requestCode == PhotoUtil.IMAGE_REQUEST_CODE){
                Uri uri = photoUtil.getPhotoUriOnActivityResult(requestCode, resultCode, data);
                if(uri == null){
                    showToast("取得头像失败");
                }else {
                    photoUtil.startPhotoZoom(this, uri);
                }
            }else if(requestCode == PhotoUtil.RESULT_REQUEST_CODE){
                Bitmap bitmap = photoUtil.getCroppedPhotoBitmap(this);
                if(bitmap == null){
                    showToast("取得头像失败");
                }else {
                    nowPos = -1;
                    newPhotoBitmap = bitmap;
                    newPhotoType = photoUtil.getCroppedImageType();
                    photoImg.setImageBitmap(bitmap);
                }
            }
        }
    }
}
