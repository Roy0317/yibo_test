package com.yibo.yiboapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.Event.LoginEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.AccountDetailListActivity;
import com.yibo.yiboapp.activity.ActivePageActivity;
import com.yibo.yiboapp.activity.AppSettingActivity;
import com.yibo.yiboapp.activity.BigPanActivity;
import com.yibo.yiboapp.activity.CouponActivity;
import com.yibo.yiboapp.activity.DirectChargeActivity;
import com.yibo.yiboapp.activity.DonateActivity;
import com.yibo.yiboapp.activity.ExchangeScoreActivity;
import com.yibo.yiboapp.activity.JiajiangActivity;
import com.yibo.yiboapp.activity.JijinActivity;
import com.yibo.yiboapp.activity.MainActivity;
import com.yibo.yiboapp.activity.MemberListActivity;
import com.yibo.yiboapp.activity.MessageCenterActivity;
import com.yibo.yiboapp.activity.MiningActivity;
import com.yibo.yiboapp.activity.MyRecommendationActivity;
import com.yibo.yiboapp.activity.PointExchangeRecordActivity;
import com.yibo.yiboapp.activity.QuotaConvertActivity;
import com.yibo.yiboapp.activity.RechargeCouponActivity;
import com.yibo.yiboapp.activity.RecordsActivity;
import com.yibo.yiboapp.activity.RecordsActivityNew;
import com.yibo.yiboapp.activity.SuggestionFeedbackActivity;
import com.yibo.yiboapp.activity.TeamOverViewListActivity;
import com.yibo.yiboapp.activity.VerificationModifyActivity;
import com.yibo.yiboapp.activity.ZhangbianInfoActivity;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LoginOutWraper;
import com.yibo.yiboapp.entify.LoginUUIDBean;
import com.yibo.yiboapp.entify.MemberHeaderWraper;
import com.yibo.yiboapp.entify.PersonCenterItem;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.SysConfigWraper;
import com.yibo.yiboapp.entify.UnreadMsgCountWraper;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.mvvm.banking.DayBalanceActivity;
import com.yibo.yiboapp.mvvm.password.ChangePwdActivity;
import com.yibo.yiboapp.ui.ActiveStationActivity;
import com.yibo.yiboapp.ui.PersonCenterMenuHeader;
import com.yibo.yiboapp.utils.FaceUtils;
import com.yibo.yiboapp.utils.StartActivityUtils;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * Created by johnson on 2017/11/15.
 * <p>
 * 个人中心
 */

public class PersonCenterFragment extends Fragment implements SessionResponse.Listener<CrazyResult<Object>> {


    //侧边菜单头部
    PersonCenterMenuHeader header;
    List<List<PersonCenterItem>> datas;
    PersonAdapter personAdapter;
    ExpandableListView listview;
    public static final int lOGINOUT_REQUEST = 0x01;
    public static final int SYS_CONFIG_REQUEST = 0x08;
    public static final int UNREAD_MSG_REQUEST = 0x06;
    private static final int GET_HEADER = 0x55;
    String unReadCount = "";
    private TextView mTvMyRecommendation;
    private String isAllLevelFix = "off";
    FaceUtils faceUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_center, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        header.syncHeaderWebDatas(getActivity());
        header.updatePersonCenterMenuUI();
        UsualMethod.getUnreadMsg(getActivity(), UNREAD_MSG_REQUEST, this);
        ((MainActivity) getActivity()).header.updateCircleHeader();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        fillDatas();
        header = (PersonCenterMenuHeader) LayoutInflater.from(getActivity()).inflate(R.layout.menu_header_person_center, null);
        header.setActivity(getActivity());
        header.setFaceUploadListener(new PersonCenterMenuHeader.FaceUploadListener() {
            @Override
            public void onHeader() {
                actionPhoto();
            }
        });
        listview.addHeaderView(header);
        listview.setDivider(null);
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String title = "";
                PersonCenterItem data = datas.get(groupPosition).get(childPosition);
                if (data != null) {
                    title = data.getTxtName();
                }
                if (title.equals("彩票投注记录")) {
                    RecordsActivityNew.createIntent(getActivity(), "彩票投注记录", Constant.CAIPIAO_RECORD_STATUS, "");
                } else if (title.equals("六合投注记录")) {
                    RecordsActivity.createIntent(getActivity(), "六合投注记录", Constant.LHC_RECORD_STATUS, "LHC");
                }/* else if (title.equals("旧体育投注记录")) {
                    RecordsActivity.createIntent(getActivity(), "旧体育投注记录", Constant.OLD_SPORTS_RECORD_STATUS, "");
                }*/ else if (title.equals("体育投注记录")) {
                    RecordsActivity.createIntent(getActivity(), "体育投注记录", Constant.SPORTS_RECORD_STATUS, "");
                } else if (title.equals("沙巴体育投注记录")) {
                    RecordsActivity.createIntent(getActivity(), "沙巴体育投注记录", Constant.SBSPORTS_RECORD_STATUS, "");
                } else if (title.equals("真人投注记录")) {
                    RecordsActivity.createIntent(getActivity(), "真人投注记录", Constant.REAL_PERSON_RECORD_STATUS, "");
                } else if (title.equals("电子游戏记录")) {
                    RecordsActivityNew.createIntent(getActivity(), "电子游戏记录", Constant.ELECTRIC_GAME_RECORD_STATUS, "");
                } else if (title.equals("棋牌游戏记录")) {
                    RecordsActivityNew.createIntent(getActivity(), "棋牌游戏记录", Constant.CHESS_GAME_RECORD_STATUS, "");
                } else if (title.equals("第三方体育投注记录")) {
                    RecordsActivityNew.createIntent(getActivity(), "第三方体育投注记录", Constant.THIRLD_SPORT_RECORD, "");
                } else if (title.equals("捕鱼游戏记录")) {
                    RecordsActivityNew.createIntent(getActivity(), "捕鱼游戏记录", Constant.HUNTER_RECORD, "");
                } else if (title.equals("电竞游戏记录")) {
                    RecordsActivityNew.createIntent(getActivity(), "电竞游戏记录", Constant.ESPORT_RECORD, "");
                } else if (title.equals("每日输赢")){
                    startActivity(new Intent(getActivity(), DayBalanceActivity.class));
                } else if (title.equals("用户账变记录")) {
                    ZhangbianInfoActivity.createIntent(getActivity(), "用户账变记录", Constant.ACCOUNT_CHANGE_RECORD_STATUS, "");
                } else if (title.equals("账户明细记录")) {
                    AccountDetailListActivity.createIntent(getActivity());
                } else if (title.equals("登陆密码修改")) {
                    ChangePwdActivity.Companion.createIntent(getActivity(), true);
                } else if (title.equals("提款密码修改")) {
                    ChangePwdActivity.Companion.createIntent(getActivity(), false);
                } else if (title.equals("安全问题修改")) {
                    String uuidjson = YiboPreference.instance(getContext()).getLoginUuid();
                    String username = YiboPreference.instance(getContext()).getUsername();
                    long accountid = 0;
                    if (!TextUtils.isEmpty(uuidjson)) {
                        List<LoginUUIDBean> list = new Gson().fromJson(uuidjson, new TypeToken<List<LoginUUIDBean>>() {
                        }.getType());
                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getUsername().equals(username)) {
                                    accountid = list.get(i).getAccountId();
                                }
                            }
                        }
                    }
                    VerificationModifyActivity.createIntent(getActivity(), accountid);
                } else if (title.contains("我的站内信")) {
                    MessageCenterActivity.createIntent(getActivity());
                } else if (title.contains("每日加奖活动")) {
                    Intent intent = new Intent(getActivity(), JiajiangActivity.class);
                    intent.putExtra("title", "每日加奖活动");
                    CookieSyncManager.createInstance(getActivity());
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    cookieManager.setCookie(Urls.BASE_URL + Urls.MEIRIJIAJIANG, "SESSION=" + YiboPreference.instance(getActivity()).getToken());//cookies是在HttpClient中获得的cookie
                    CookieSyncManager.getInstance().sync();
                    intent.putExtra("url", Urls.BASE_URL + Urls.MEIRIJIAJIANG);
                    startActivity(intent);
                } else if (title.contains("周周转运活动")) {
                    Intent intent = new Intent(getActivity(), JiajiangActivity.class);
                    intent.putExtra("title", "周周转运活动");
                    CookieSyncManager.createInstance(getActivity());
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    cookieManager.setCookie(Urls.BASE_URL + Urls.ZHOUZHOUZHUANYUN, "SESSION=" + YiboPreference.instance(getActivity()).getToken());//cookies是在HttpClient中获得的cookie
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        cookieManager.flush();
                    } else {
                        CookieSyncManager.createInstance(getActivity());
                        CookieSyncManager.getInstance().sync();
                    }
                    intent.putExtra("url", Urls.BASE_URL + Urls.ZHOUZHOUZHUANYUN);
                    startActivity(intent);
                } else if (title.equals("充值卡中心")) {
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        showToast("功能暂未开放");
                        return true;
                    }
                    startActivity(new Intent(getActivity(), RechargeCouponActivity.class));
                } else if (title.equals("代金券中心")) {
                    startActivity(new Intent(getActivity(), CouponActivity.class));
                } else if (title.equals(getString(R.string.mining_active))) {
                    Intent intent = new Intent(getActivity(), MiningActivity.class);
                    startActivity(intent);
                } else if (title.equals("积分兑换")) {
                    ExchangeScoreActivity.createIntent(getActivity(), "", "");
                } else if (title.equals("积分兑换记录")) {
                    PointExchangeRecordActivity.createIntent(getActivity());
                } else if (title.equals("额度转换")) {
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        showToast("操作权限不足，请联系客服！");
                        return true;
                    }
                    QuotaConvertActivity.createIntent(getActivity());
                } else if (title.equals("优惠活动")) {
                    if (Utils.isEmptyString(UsualMethod.getConfigFromJson(getContext()).getWap_active_activity_link()))
                        ActivePageActivity.createIntent(getContext());
                    else
                        UsualMethod.viewLink(getContext(), UsualMethod.getConfigFromJson(getContext()).getWap_active_activity_link().trim());
                } else if (title.equals("幸运大转盘")) {
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        showToast("操作权限不足，请联系客服！");
                        return true;
                    }
                    BigPanActivity.createIntent(getActivity());
                } else if (title.equals(getString(R.string.red_packet_string))) {
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST
                            && UsualMethod.getConfigFromJson(getActivity()).getOn_off_guest_redperm().equals("off")) {
                        showToast("操作权限不足，请联系客服！");
                        return true;
                    }
                    StartActivityUtils.goRedPacket(getContext());
                }else if(title.equals("免提直充")){
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        showToast("操作权限不足，请联系客服！");
                        return true;
                    }
                    getActivity().startActivity(new Intent(getActivity(), DirectChargeActivity.class));
                }else if(title.equals("会员乐捐")){
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        showToast("操作权限不足，请联系客服！");
                        return true;
                    }
                    getActivity().startActivity(new Intent(getActivity(), DonateActivity.class));
                } else if (title.equals("用户列表")) {
                    startActivity(new Intent(getActivity(), MemberListActivity.class));
                } else if (title.equals("团队总览")) {
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        showToast("操作权限不足，请联系客服！");
                        return true;
                    }
                    startActivity(new Intent(getActivity(), TeamOverViewListActivity.class));
                } else if (title.equals("我的推荐")) {
                    if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        showToast("操作权限不足，请联系客服！");
                        return true;
                    }
                    startActivity(new Intent(getActivity(), MyRecommendationActivity.class));
                } else if (title.equals("设置")) {
                    AppSettingActivity.createIntent(getActivity());
                } else if (title.equals("活动大厅")) {
                    ActiveStationActivity.createIntent(getActivity());
                } else if (title.equals("余额生金")) {
                    startActivity(new Intent(getActivity(), JijinActivity.class));
                } else if (title.equals("联系我们")) {
                    SysConfig config = UsualMethod.getConfigFromJson(getActivity());
                    String version = config.getOnline_service_open_switch();
                    if (!version.isEmpty()) {
                        boolean success = UsualMethod.viewService(getActivity(), version);
                        if (!success) {
                            showToast("没有在线客服链接地址，无法打开");
                        }
                    }
                } else if (title.equals(getString(R.string.feedback_title))) {
                    SuggestionFeedbackActivity.createIntent(getActivity());
                } else if (title.equals("退出登录")) {
                    final CrazyRequest request = UsualMethod.startAsyncConfig(getActivity(), SYS_CONFIG_REQUEST);
                    RequestManager.getInstance().startRequest(getActivity(), request);
                    UsualMethod.actionLoginOut(getActivity(), lOGINOUT_REQUEST, true, PersonCenterFragment.this);
                } else if (title.equals("活动大厅")) {
                    ActiveStationActivity.createIntent(getContext());
                } else if (title.equals("账户管理")) {
                    Intent intent = BankingManager.Companion.openAccountManagerPage(getContext(), false);
                    startActivity(intent);
                }
                return true;
            }
        });
        personAdapter = new PersonAdapter();
        listview.setAdapter(personAdapter);
        for (int i = 0; i < datas.size(); i++) {
            listview.expandGroup(i);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (faceUtils != null) {
            faceUtils.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void actionPhoto() {
        if (faceUtils == null) {
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("userName", YiboPreference.instance(getActivity()).getUsername());
            uploadParams.put("stationId", YiboPreference.instance(getActivity()).getPwd());
            uploadParams.put("projectId", 1);
            uploadParams.put("pid", UsualMethod.getPackageName(getContext()));
            faceUtils = new FaceUtils(this, uploadParams);
            faceUtils.setOnFaceUploadListener(new FaceUtils.OnFaceUploadListener() {
                @Override
                public void onFaceUploadSuccess(String imgUrl) {
                    if (Utils.isEmptyString(imgUrl)) {
                        return;
                    }
                    UsualMethod.syncHeader(getContext(), GET_HEADER, false, PersonCenterFragment.this);
                    YiboPreference.instance(getContext()).setUserHeader(imgUrl);
                    header.updateCircleHeader();
                    ((MainActivity) getActivity()).header.updateCircleHeader();
                    showToast("设置头像成功");
                }

                @Override
                public void onFaceUploadFailed(String error) {
                    showToast("设置头像失败：" + error);
                }
            });
        }
        faceUtils.showSettingFaceDialog();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (activity.isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == lOGINOUT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.loginout_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.loginout_fail));
                return;
            }
            Object regResult = result.result;
            LoginOutWraper reg = (LoginOutWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.loginout_fail));
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            if (reg.isContent()) {
                UsualMethod.loginWhenSessionInvalid(getActivity());
            } else {
                showToast(getString(R.string.loginout_fail));
            }
        } else if (action == SYS_CONFIG_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.get_system_config_fail));
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.get_system_config_fail) : errorString);
                return;
            }
            SysConfigWraper stw = (SysConfigWraper) result.result;
            if (!stw.isSuccess()) {
                showToast(Utils.isEmptyString(stw.getMsg()) ?
                        getString(R.string.get_system_config_fail) : stw.getMsg());
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (stw.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(getActivity());
                }
                return;
            }
            YiboPreference.instance(getActivity()).setToken(stw.getAccessToken());
            //存储系统配置项
            if (stw.getContent() != null) {
                String configJson = new Gson().toJson(stw.getContent(), SysConfig.class);
                if (configJson.equals(YiboPreference.instance(getActivity()).getSysConfig())){
                    // 当前保存的系统配置已经是最新的了
                    return;
                }
                YiboPreference.instance(getActivity()).saveConfig(configJson);
                YiboPreference.instance(getActivity()).saveYjfMode(stw.getContent().getYjf());
                //保存系统版本号版本号
                YiboPreference.instance(getActivity()).saveGameVersion(stw.getContent().getVersion());
            }
        } else if (action == UNREAD_MSG_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            UnreadMsgCountWraper reg = (UnreadMsgCountWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            header.updatePersonCenterMenuUI();
            //更新侧边站内信未读消息数
            int count = reg.getContent();
            unReadCount = String.valueOf(count);
            this.datas.clear();
            fillDatas();
            personAdapter = new PersonAdapter();
            listview.setAdapter(personAdapter);
            for (int i = 0; i < datas.size(); i++) {
                listview.expandGroup(i);
            }

        } else if (action == GET_HEADER) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
//                String errorString = Urls.parseResponseResult(result.error);
//                showToast(Utils.isEmptyString(errorString) ? context.getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            MemberHeaderWraper reg = (MemberHeaderWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
//            UsualMethod.getConfigFromJson(getContext());
            YiboPreference.instance(getContext()).setUserHeader(reg.getContent());
            header.updateCircleHeader();
            //更新头像
        }
    }

    private void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        listview = (ExpandableListView) view.findViewById(R.id.list);
        listview.setGroupIndicator(null);

    }

    private void fillDatas() {
        SysConfig sc = UsualMethod.getConfigFromJson(getActivity());
        if (sc == null) {
            return;
        }
        //填充操作记录列表数据
        boolean isGuest = YiboPreference.instance(getActivity()).getAccountMode() ==
                Constant.ACCOUNT_PLATFORM_TEST_GUEST;
        List<PersonCenterItem> operates = new ArrayList<>();
        if (!Utils.isEmptyString(sc.getOnoff_lottery_game()) &&
                sc.getOnoff_lottery_game().equals("on")) {
            PersonCenterItem caipiaoItem = new PersonCenterItem();
            caipiaoItem.setIcon(R.drawable.caipiao_record_icon2_new);
            caipiaoItem.setTxtName("彩票投注记录");
            operates.add(caipiaoItem);
        }
        if (!Utils.isEmptyString(sc.getOnoff_liu_he_cai()) &&
                sc.getOnoff_liu_he_cai().equals("on")) {
            PersonCenterItem caipiaoItem = new PersonCenterItem();
            caipiaoItem.setIcon(R.drawable.lhc_record_icon2_new);
            caipiaoItem.setTxtName("六合投注记录");
            operates.add(caipiaoItem);
        }

        if (!isGuest || Utils.shiwanFromMobile(getActivity())) {

//            if (!Utils.isEmptyString(sc.getNew_onoff_sports_game()) &&
//                    sc.getNew_onoff_sports_game().equals("on")) {
//                PersonCenterItem newItem = new PersonCenterItem();
//                newItem.setIcon(R.drawable.sport_record_icon_new);
//                newItem.setTxtName("旧体育投注记录");
//                operates.add(newItem);
//            }

            if (!Utils.isEmptyString(sc.getOnoff_sports_game()) &&
                    sc.getOnoff_sports_game().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.sport_record_icon2_new);
                caipiaoItem.setTxtName("体育投注记录");
                operates.add(caipiaoItem);
            }


            if (!Utils.isEmptyString(sc.getOnoff_shaba_sports_game()) &&
                    sc.getOnoff_shaba_sports_game().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.sport_record_icon2_new);
                caipiaoItem.setTxtName("沙巴体育投注记录");
                operates.add(caipiaoItem);
            }


            if (!Utils.isEmptyString(sc.getOnoff_zhen_ren_yu_le()) &&
                    sc.getOnoff_zhen_ren_yu_le().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.realman_record_icon2_new);
                caipiaoItem.setTxtName("真人投注记录");
                operates.add(caipiaoItem);
            }
            if (!Utils.isEmptyString(sc.getOnoff_dian_zi_you_yi()) &&
                    sc.getOnoff_dian_zi_you_yi().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.egame_record_icon2_new);
                caipiaoItem.setTxtName("电子游戏记录");
                operates.add(caipiaoItem);
            }
            if (!Utils.isEmptyString(sc.getOnoff_chess()) &&
                    sc.getOnoff_chess().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.chess_game_record_icon_new);
                caipiaoItem.setTxtName("棋牌游戏记录");
                operates.add(caipiaoItem);
            }
            if (!Utils.isEmptyString(sc.getOnoff_avia_game()) &&
                    sc.getOnoff_avia_game().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
//                caipiaoItem.setIcon(R.drawable.game_record_icon_new);
                caipiaoItem.setIcon(R.drawable.chess_game_record_icon_new);
                caipiaoItem.setTxtName("电竞游戏记录");
                operates.add(caipiaoItem);
            }
            if (!Utils.isEmptyString(sc.getOnoff_dian_zi_you_yi()) &&
                    sc.getOnoff_dian_zi_you_yi().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.chess_game_record_icon_new);
                caipiaoItem.setTxtName("捕鱼游戏记录");
                operates.add(caipiaoItem);
            }
            if (!Utils.isEmptyString(sc.getOnoff_sports_game()) &&
                    sc.getOnoff_third_sport().equals("on")) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.chess_game_record_icon_new);
                caipiaoItem.setTxtName("第三方体育投注记录");
                operates.add(caipiaoItem);
            }

            PersonCenterItem balanceItem = new PersonCenterItem();
            balanceItem.setIcon(R.drawable.icon_day_balance);
            balanceItem.setTxtName("每日输赢");
            operates.add(balanceItem);

            if (!Utils.isEmptyString(sc.getOnoff_change_money()) &&
                    sc.getOnoff_change_money().equals("on") &&
                    (!Utils.isEmptyString(sc.getIosExamine()) ||
                            !sc.getIosExamine().equals("off"))) {
                PersonCenterItem caipiaoItem = new PersonCenterItem();
                caipiaoItem.setIcon(R.drawable.account_change_record_icon2_new);
                caipiaoItem.setTxtName("用户账变记录");
                operates.add(caipiaoItem);
            }
        }
        if ((!Utils.isEmptyString(sc.getIosExamine()) ||
                !sc.getIosExamine().equals("off"))) {
            //添加帐户明细记录
            PersonCenterItem caipiaoItem = new PersonCenterItem();
//            caipiaoItem.setIcon(R.drawable.account_detail_record_icon_new);
            caipiaoItem.setIcon(R.drawable.account_detail_reord_icon2_new);
            caipiaoItem.setTxtName("账户明细记录");
            operates.add(caipiaoItem);
        }
        this.datas.add(operates);

        //填充个人中心数据
        List<PersonCenterItem> persons = new ArrayList<>();
        if (!isGuest) {
            PersonCenterItem bankManager = new PersonCenterItem();
            bankManager.setIcon(R.drawable.icon_zhgl_new);
            bankManager.setTxtName(getString(R.string.zhgl));
            persons.add(bankManager);
        }

        if (UsualMethod.getConfigFromJson(getContext()) != null && "on".equals(UsualMethod.getConfigFromJson(getContext()).getOnoff_application_active())) {
            PersonCenterItem dating = new PersonCenterItem();
            dating.setIcon(R.drawable.mrjj_active_icon2_new);
            dating.setTxtName("活动大厅");
            persons.add(dating);
        }
        if ("on".equals(UsualMethod.getConfigFromJson(getContext()).getOnoff_money_income())) {
            PersonCenterItem loginJijin = new PersonCenterItem();
            loginJijin.setIcon(R.drawable.icon_yesj2_new);
            loginJijin.setTxtName(getString(R.string.yesj));
            persons.add(loginJijin);
        }
        PersonCenterItem loginPwd = new PersonCenterItem();
//        loginPwd.setIcon(R.drawable.pwd_modify_icon_new);
        loginPwd.setIcon(R.drawable.pwd_modify_icon2_new);
        loginPwd.setTxtName(getString(R.string.pwd_modify));
        persons.add(loginPwd);

        PersonCenterItem pickPwd = new PersonCenterItem();
//        pickPwd.setIcon(R.drawable.withdraw_record_icon_new);
        pickPwd.setIcon(R.drawable.withdraw_record_icon2_new);
        pickPwd.setTxtName(getString(R.string.account_pwd_modify));
        persons.add(pickPwd);


//        pickPwd.setIcon(R.drawable.withdraw_record_icon_new);
        if (!"off".equals(sc.getOn_off_mobile_certificate()) && !"".equals(sc.getOn_off_mobile_certificate())) {
            PersonCenterItem verPwd = new PersonCenterItem();
            verPwd.setIcon(R.drawable.withdraw_record_icon2_new);
            verPwd.setTxtName(getString(R.string.vervification_pwd_modify));
            persons.add(verPwd);
        }

        if ("on".equals(sc.getOne_bonus_onoff())) {
            PersonCenterItem Jiajiang = new PersonCenterItem();
//            Jiajiang.setIcon(R.drawable.score_exchange_icon_new);
            Jiajiang.setIcon(R.drawable.mrjj_active_icon_new);
            Jiajiang.setTxtName(getString(R.string.meirijiajiang));
            persons.add(Jiajiang);
        }
        if ("on".equals(sc.getWeek_deficit_onoff())) {
            PersonCenterItem Zhuanyun = new PersonCenterItem();
//            Zhuanyun.setIcon(R.drawable.realman_record_icon_new);
            Zhuanyun.setIcon(R.drawable.zzzy_icon_new);
            Zhuanyun.setTxtName("周周转运活动");
            persons.add(Zhuanyun);
        }

        if ("on".equals(sc.getRecharge_card_onoff())) {
            PersonCenterItem item = new PersonCenterItem();
            item.setIcon(R.drawable.recharge_card_icon_new);
            item.setTxtName("充值卡中心");
            persons.add(item);
        }

        if ("on".equals(sc.getCoupons_onoff())) {
            PersonCenterItem item = new PersonCenterItem();
            item.setIcon(R.drawable.coupon_icon_new);
            item.setTxtName("代金券中心");
            persons.add(item);
        }

        if ("on".equals(sc.getOnoff_mining())) {
            PersonCenterItem mining = new PersonCenterItem();
//            mining.setIcon(R.drawable.score_exchange_icon_new);
            mining.setIcon(R.drawable.wakuang_active_icon_new);
            mining.setTxtName(getString(R.string.mining_active));
            persons.add(mining);
        }


        PersonCenterItem messagePwd = new PersonCenterItem();
//        messagePwd.setIcon(R.drawable.message_center_new);
        messagePwd.setIcon(R.drawable.message_center2_new);
        messagePwd.setTxtName(getString(R.string.message_center_string_slide));
        messagePwd.setHelpName(unReadCount);
        persons.add(messagePwd);

        //是否显示积分兑换入口
        boolean visible = !Utils.isEmptyString(sc.getExchange_score()) && sc.getExchange_score().equals("on");
        if (visible) {
            PersonCenterItem jifen = new PersonCenterItem();
//            jifen.setIcon(R.drawable.score_exchange_icon_new);
            jifen.setIcon(R.drawable.score_exchange_icon2_new);
            jifen.setTxtName(getString(R.string.score_exchange_str));
            persons.add(jifen);

            PersonCenterItem jifenRecord = new PersonCenterItem();
//            jifenRecord.setIcon(R.drawable.score_exchange_icon_new);
            jifenRecord.setIcon(R.drawable.score_record_icon_new);
            jifenRecord.setTxtName("积分兑换记录");
            persons.add(jifenRecord);
        }

        if (UsualMethod.showConvertFee(getActivity())) {
            PersonCenterItem jifen = new PersonCenterItem();
//            jifen.setIcon(R.drawable.realman_record_icon_new);
            jifen.setIcon(R.drawable.eduzh_icon_new);
            jifen.setTxtName(getString(R.string.fee_convert_string));
            persons.add(jifen);
        }

        //优惠活动
        PersonCenterItem activeItem = new PersonCenterItem();
//        activeItem.setIcon(R.drawable.pwd_modify_icon);
        activeItem.setIcon(R.drawable.active_icon_new);
        activeItem.setTxtName(getString(R.string.active_string));
        persons.add(activeItem);

        //是否显示幸运大转盘项
        if (!Utils.isEmptyString(sc.getOnoff_turnlate()) && sc.getOnoff_turnlate().equals("on")) {
            PersonCenterItem jifen = new PersonCenterItem();
//            jifen.setIcon(R.drawable.realman_record_icon_new);
            jifen.setIcon(R.drawable.xingyun_dzp_icon_new);
            jifen.setTxtName(getString(R.string.big_pan_string));
            persons.add(jifen);
        }
        //是否显示抢红包
        if (!Utils.isEmptyString(sc.getOnoff_member_mobile_red_packet()) && sc.getOnoff_member_mobile_red_packet().equals("on")) {
            PersonCenterItem jifen = new PersonCenterItem();
//            jifen.setIcon(R.drawable.pwd_modify_icon_new);
            jifen.setIcon(R.drawable.grab_red_envelope_new);
            jifen.setTxtName(getString(R.string.red_packet_string));
            persons.add(jifen);
        }

        if("on".equals(sc.getOn_off_direct_charge())){
            PersonCenterItem item = new PersonCenterItem();
            item.setIcon(R.drawable.icon_direct_charge);
            item.setTxtName("免提直充");
            persons.add(item);
        }

        if("on".equals(sc.getOn_off_member_donate())){
            PersonCenterItem item = new PersonCenterItem();
            item.setIcon(R.drawable.icon_donate);
            item.setTxtName("会员乐捐");
            persons.add(item);
        }

        this.datas.add(persons);

        isAllLevelFix = sc.getOnoff_all_level_fixed();
        if (!TextUtils.isEmpty(isAllLevelFix) && isAllLevelFix.equals("on")) {
            //代理管理
            List<PersonCenterItem> agentManage = new ArrayList<>();

            //团队总览
            PersonCenterItem teamover = new PersonCenterItem();
//            teamover.setIcon(R.drawable.account_detail_record_icon_new);
            teamover.setIcon(R.drawable.team_all_data_icon_new);
            teamover.setTxtName("团队总览");
            agentManage.add(teamover);

            //用户列表
            PersonCenterItem userList = new PersonCenterItem();
//            userList.setIcon(R.drawable.account_detail_record_icon_new);
            userList.setIcon(R.drawable.user_list_icon_new);
            userList.setTxtName("用户列表");
            agentManage.add(userList);

            //我的推荐
            PersonCenterItem myRecommend = new PersonCenterItem();
//            myRecommend.setIcon(R.drawable.pwd_modify_icon_new);
            myRecommend.setIcon(R.drawable.my_recom_icon_new);
            myRecommend.setTxtName("我的推荐");
            agentManage.add(myRecommend);
            this.datas.add(agentManage);
        }


        //软件设置
        List<PersonCenterItem> settings = new ArrayList<>();
        PersonCenterItem settingPwd = new PersonCenterItem();
//        settingPwd.setIcon(R.drawable.pwd_modify_icon_new);
        settingPwd.setIcon(R.drawable.settings_icon2_new);
        settingPwd.setTxtName(getString(R.string.str_setting));
        settings.add(settingPwd);

        PersonCenterItem contactus = new PersonCenterItem();
//        contactus.setIcon(R.drawable.account_detail_record_icon_new);
        contactus.setIcon(R.drawable.contact_us_icon_new);
        contactus.setTxtName(getString(R.string.contast_us_str));
        settings.add(contactus);

        PersonCenterItem suggestion = new PersonCenterItem();
        suggestion.setIcon(R.drawable.icon_suggestion_new);
        suggestion.setTxtName(getString(R.string.feedback_title));
        settings.add(suggestion);

        PersonCenterItem exitLogin = new PersonCenterItem();
//        exitLogin.setIcon(R.drawable.pwd_modify_icon_new);
        exitLogin.setIcon(R.drawable.exit_login_icon_new);
        exitLogin.setTxtName(getString(R.string.exit_app));
        settings.add(exitLogin);
        this.datas.add(settings);
    }

    private final class PersonAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return datas.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return datas.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return datas.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder groupViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.person_center_group_item, parent, false);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }
//            String str = "";
//            if (groupPosition == 0) {
//                str = "操作记录";
//            } else if (groupPosition == 1) {
//                str = "个人中心";
//            } else if (groupPosition == 2) {
//                if (isAllLevelFix.equals("on")) {
//                    str = "代理管理";
//                } else {
//                    str = "软件设置";
//                }
//            } else if (groupPosition == 3) {
//                str = "软件设置";
//            }
//            groupViewHolder.tvTitle.setText(str);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.person_center_item, parent, false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.img = (ImageView) convertView.findViewById(R.id.icon);
                childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.txt);
                childViewHolder.helpTxt = (TextView) convertView.findViewById(R.id.help_num);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.tvTitle.setText(datas.get(groupPosition).get(childPosition).getTxtName());
            childViewHolder.img.setBackgroundResource(datas.get(groupPosition).get(childPosition).getIcon());
            String help = datas.get(groupPosition).get(childPosition).getHelpName();
            if (!Utils.isEmptyString(help)) {
                if (Utils.isNumeric(help) && Integer.parseInt(help) > 0) {
                    childViewHolder.helpTxt.setVisibility(View.VISIBLE);
                    childViewHolder.helpTxt.setText(help);
                }
            } else {
                childViewHolder.helpTxt.setVisibility(View.GONE);
            }
            if (isLastChild) {
                convertView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_new_main_page_menu_item_no_line));
            } else {
                convertView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_new_main_page_menu_item));
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView tvTitle;
        ImageView img;
        TextView helpTxt;
    }

    @Subscribe
    public void onEvent(LoginEvent event) {
        UsualMethod.syncHeader(getContext(), GET_HEADER, false, (MainActivity) getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}