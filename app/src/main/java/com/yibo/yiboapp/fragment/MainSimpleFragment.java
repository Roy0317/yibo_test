package com.yibo.yiboapp.fragment;


import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.activity.MainActivity;
import com.yibo.yiboapp.activity.OtherPlayActivity;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.SettingUsualGameActivity;
import com.yibo.yiboapp.activity.SportListActivity;
import com.yibo.yiboapp.activity.CaipiaoActivity;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LotterysWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.ui.CircleImageView;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.LobbyHeaderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


/**
 * A simple {@link Fragment} subclass.
 * 彩票，体育，电子，真人主界面
 */
public class MainSimpleFragment extends BaseMainFragment implements View.OnClickListener, SessionResponse.Listener<CrazyResult<Object>> {

    LobbyHeaderView headerView;

    RelativeLayout caipiaoLayout;
    RelativeLayout sportLayout;
    RelativeLayout zhenrenLayout;
    RelativeLayout gameLayout;
    ScrollView scrollView;

    RelativeLayout usual_btn_layout;
    CircleImageView usualImg;
    TextView usualTxt;
    Button btnTouZhu;
    TextView usualSecTxt;

    boolean showRealPersonModule = true;//是否显示真人
    boolean showGameModule = true;//是否显示电子游戏
    TextView tv_online_count;
    public static final int LOTTERYS_REQUEST = 0x011;

    private LotterysWraper lotterysWraper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_module, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        headerView.syncHeaderWebDatas();
        headerView.bindDelegate(delegate);
        loadGameDataFromWeb();
    }

    public void bindDelegate(MainHeaderDelegate delegate) {
        this.delegate = delegate;
    }

    private void initView(View view) {

        btnTouZhu = (Button) view.findViewById(R.id.touzhu_btn);
        headerView = (LobbyHeaderView) view.findViewById(R.id.header);
        caipiaoLayout = (RelativeLayout) view.findViewById(R.id.caipiao_layout);
        sportLayout = (RelativeLayout) view.findViewById(R.id.sport_layout);
        zhenrenLayout = (RelativeLayout) view.findViewById(R.id.zhenren_layout);
        gameLayout = (RelativeLayout) view.findViewById(R.id.game_layout);
        scrollView = (ScrollView) view.findViewById(R.id.scroll);

        caipiaoLayout.setOnClickListener(this);
        sportLayout.setOnClickListener(this);
        zhenrenLayout.setOnClickListener(this);
        gameLayout.setOnClickListener(this);

        usual_btn_layout = (RelativeLayout) view.findViewById(R.id.usual_btn_layout);
        usual_btn_layout.setOnClickListener(this);
        usualImg = (CircleImageView) usual_btn_layout.findViewById(R.id.usual_btn);
        usualTxt = (TextView) usual_btn_layout.findViewById(R.id.txt);
        tv_online_count = (TextView)view.findViewById(R.id.tv_online_count);
        usualSecTxt = (TextView) usual_btn_layout.findViewById(R.id.sec_txt);


        SysConfig sc = UsualMethod.getConfigFromJson(getActivity());
        if (sc != null) {
            showRealPersonModule = !Utils.isEmptyString(sc.getOnoff_zhen_ren_yu_le()) && sc.getOnoff_zhen_ren_yu_le().equals("on");
            showGameModule = !Utils.isEmptyString(sc.getOnoff_dian_zi_you_yi()) && sc.getOnoff_dian_zi_you_yi().equals("on");
        }
    }

    private void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (getActivity() == null) {
            return;
        }
        if (getActivity().isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == LOTTERYS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null || !result.crazySuccess) {
                showToast("获取彩种信息失败");
                return;
            }
            LotterysWraper stw = (LotterysWraper) result.result;
            if (!stw.isSuccess()) {
                showToast(!Utils.isEmptyString(stw.getMsg()) ? stw.getMsg() :
                        "获取彩种信息失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (stw.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(getActivity());
                }
                if (stw.getCode() == 544){
                    UsualMethod.showVerifyActivity(getActivity());
                }
                return;
            }
            YiboPreference.instance(getActivity()).setToken(stw.getAccessToken());
            lotterysWraper = stw;
            CacheRepository.getInstance().saveLotteryData(getActivity(), stw.getContent());
        }
    }

    /**
     * 获取游戏数据，包括彩种，体育，真人，电子
     */
    private void loadGameDataFromWeb() {
        //获取彩种信息
        StringBuilder lotteryUrl = new StringBuilder();
        lotteryUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ALL_GAME_DATA_URL);
        CrazyRequest<CrazyResult<LotterysWraper>> lotteryRequest = new AbstractCrazyRequest.Builder().
                url(lotteryUrl.toString())
                .seqnumber(LOTTERYS_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(getActivity()))
                .shouldCache(false)
                .refreshAfterCacheHit(false)
                .placeholderText(getString(R.string.sync_caizhong_ing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotterysWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(getActivity(), lotteryRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_text:
                break;
            case R.id.caipiao_layout:
                CaipiaoActivity.createIntent(getActivity());
                break;
            case R.id.sport_layout:
                if (Utils.isTestPlay(this.getActivity())) {
                    return;
                }

                if (lotterysWraper == null || lotterysWraper.getContent() == null) {
                    showToast("没有体育数据");
                    return;
                }

                List<LotteryData> sports = new ArrayList<>();
                for (LotteryData data : lotterysWraper.getContent()) {
                    if (data.getModuleCode() == 0) {
                        sports.add(data);
                    }
                }

                if (sports.isEmpty()) {
                    showToast("没有体育数据");
                    return;
                }

                Type listType = new TypeToken<ArrayList<LotteryData>>() {
                }.getType();
                String json = new Gson().toJson(sports, listType);
                SportListActivity.createIntent(getActivity(), json);
                break;
            case R.id.zhenren_layout:
                if (Utils.isTestPlay(this.getActivity())) {
                    return;
                }
                if (!showRealPersonModule) {
                    showToast("请先在后台开启真人开关");
                    return;
                }
                OtherPlayActivity.createIntent(getActivity(), "真人", Constant.REAL_MODULE_CODE);
                break;
            case R.id.game_layout:
                if (Utils.isTestPlay(this.getActivity())) {
                    return;
                }
                if (!showGameModule) {
                    showToast("请先在后台开启游戏开关");
                    return;
                }
                OtherPlayActivity.createIntent(getActivity(), "电子", Constant.GAME_MODULE_CODE);
                break;
            case R.id.usual_btn_layout:
                if (!showGameModule) {
                    showToast("请先在后台开启游戏开关");
                    return;
                }
                clickUsualGame();
                break;
            case R.id.zxkf_layout:
                UsualMethod.viewService(getActivity());
                break;
        }
    }

    private void clickUsualGame() {
        SettingUsualGameActivity.createIntent(getActivity(), "");
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        ((MainActivity) getActivity()).hidenorshow(hidden);
    }

    @Override
    public void setOnlineCount(String count) {
        if (tv_online_count != null) {
            tv_online_count.setVisibility(View.VISIBLE);
            tv_online_count.setText("在线人数:" + count + "人");
        }
    }

    @Override
    public void refreshNewMainPageLoginBlock(boolean isLogin, String accountName, double balance) {}
}
