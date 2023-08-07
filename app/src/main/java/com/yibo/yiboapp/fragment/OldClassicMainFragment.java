package com.yibo.yiboapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anuo.immodule.utils.ScreenUtil;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.BBinActivity;
import com.yibo.yiboapp.activity.GameListActivity;
import com.yibo.yiboapp.activity.MainActivity;
import com.yibo.yiboapp.activity.SportActivity;
import com.yibo.yiboapp.activity.SportNewsWebActivity;
import com.yibo.yiboapp.activity.TouzhuActivity;
import com.yibo.yiboapp.activity.TouzhuSimpleActivity;
import com.yibo.yiboapp.adapter.GameAdapter;
import com.yibo.yiboapp.adapter.WinningDataMF;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.WinningDataWraper;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LocPlaysWraper;
import com.yibo.yiboapp.entify.LotterysWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.UpdateAllGameEvent;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.HorizontalScrollBar;
import com.yibo.yiboapp.views.LobbyHeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 经典版主题风格页面
 * Created by johnson on 2018/3/29.
 */

public class OldClassicMainFragment extends BaseMainFragment implements
        GameAdapter.GameEventDelegate, SessionResponse.Listener<CrazyResult<Object>>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int LOTTERYS_REQUEST = 0x011;
    public static final int PLAY_RULES_REQUEST = 0x07;
    public static final int REAL_REQUEST = 0x08;
    public static final int DIANZI_REQUEST = 0x09;

    ScrollView scrollView;
    LobbyHeaderView headerView;
    GridView gridGames;

    private HorizontalScrollView tabScrollView;
    private HorizontalScrollBar scrollBar;
    private LinearLayout tabLayout;
    private SwipeRefreshLayout refreshLayout;
    private TextView clickRefresh;
    private EmptyListView emptyListView;
    static TextView tv_online_count;
    private MarqueeView marqueeView;
    private LinearLayout winning_layout;


    List<String> main_tabs = new ArrayList<>();
    String currentTabName;//当前选中的tab名称
    GameAdapter gameAdapter;
    List<LotteryData> gameDatas = new ArrayList<>();
    List<LotteryData> allDatas = new ArrayList<>();
    private List<WinningDataWraper> winningData = new ArrayList<>();
    private MarqueeFactory<LinearLayout, WinningDataWraper> marqueeFactory;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String lastEventJson;
    private SysConfig sc = UsualMethod.getConfigFromJson(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.old_classic_main_view, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        headerView.syncHeaderWebDatas();
        headerView.bindDelegate(delegate);
        gameAdapter = new GameAdapter(getActivity(), gameDatas, R.layout.caipiao_item);
        gameAdapter.setDelegate(this);
        gridGames.setAdapter(gameAdapter);
        updateLotteryViewFromBackup();
        //加载游戏数据
        loadGameDataFromWeb();
        //获取中奖假数据
        getWinningData();

        EventBus.getDefault().register(this);
    }

    /**
     * 有打开假数据再跟server取资料
     */
    private void getWinningData() {
//        SysConfig sc = UsualMethod.getConfigFromJson(getActivity());
        if (sc == null || !"on".equals(sc.getOnoff_show_winning_data())) {
            return;
        }

        HttpUtil.get(getActivity(), Urls.GET_WINNING_DATA, null, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    List<WinningDataWraper> list = new Gson().fromJson(result.getContent(),
                            new TypeToken<List<WinningDataWraper>>() {
                            }.getType());

                    //笔数太多了造成ANR
                    if (list.size() > 100) {
                        list = list.subList(0, 100);
                    }
                    winningData.clear();
                    winningData.addAll(list);
                    marqueeFactory.setData(winningData);
                }
            }
        });
    }

    public void bindDelegate(MainHeaderDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onResume() {
        super.onResume();
//        SysConfig sc = UsualMethod.getConfigFromJson(getActivity());
        if (sc != null && sc.getNewmainpage_switch().equals("off")) {
            headerView.syncUIWhenStart();
        }
//        headerView.syncUIWhenStart();
    }

    @Override
    public void onStart() {
        super.onStart();
        marqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        ((MainActivity) getActivity()).hidenorshow(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        //设置刷新圆圈样式
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(this);

        emptyListView = (EmptyListView) view.findViewById(R.id.emptyView);
        //初始化emptyListView
        initEmptyListView();

        clickRefresh = (TextView) view.findViewById(R.id.click_refresh);
        tv_online_count = (TextView) view.findViewById(R.id.tv_online_count);
        clickRefresh.setOnClickListener(this);
        gridGames = (GridView) view.findViewById(R.id.gridGames);
        gridGames.setEnabled(false);

        tabScrollView = (HorizontalScrollView) view.findViewById(R.id.tabScrollView);
        scrollBar = (HorizontalScrollBar) view.findViewById(R.id.scrollBar);
        scrollBar.attachScrollView(tabScrollView);
        tabLayout = (LinearLayout) view.findViewById(R.id.tabLayout);
        headerView = (LobbyHeaderView) view.findViewById(R.id.header);
        scrollView = (ScrollView) view.findViewById(R.id.scroll);
        // 中奖数据部分
        winning_layout = (LinearLayout) view.findViewById(R.id.ll_winning_data);
        marqueeView = (MarqueeView) view.findViewById(R.id.marqueeView);
        marqueeFactory = new WinningDataMF(getActivity());
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.startFlipping();

        if (sc != null) {
            if (sc.getOnoff_show_winning_data().equals("on")) {
                winning_layout.setVisibility(View.VISIBLE);
            } else {
                winning_layout.setVisibility(View.GONE);
            }

            String mobileIndex = sc.getMobileIndex();
            if (mobileIndex.equalsIgnoreCase(Constant.SELECT_TABS)) {
                tabScrollView.setVisibility(View.VISIBLE);
                scrollBar.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                if (!Utils.isEmptyString(sc.getMainpage_module_indexs())) {
                    String[] sorts = sc.getMainpage_module_indexs().split(",");
                    if (sorts.length > 0) {
                        for (String sort : sorts) {
                            if (!TextUtils.isEmpty(sort) && Utils.isInteger(sort)) {
//                                (1-彩票,3-真人,4-电子,5-体育,6-棋牌,7-红包游戏,8-电竞,9--捕鱼,10--热门
                                if (sort.equals("1")) {
                                    main_tabs.add("彩票");
                                } else if (sort.equals("3")) {
//                                    if (showRealPersonModule) {
                                    main_tabs.add("真人");
//                                    }
                                } else if (sort.equals("6")) {
//                                    if (showChessModule) {
                                    main_tabs.add("棋牌");
//                                    }
                                } else if (sort.equals("4")) {
//                                    if (showGameModule) {
                                    main_tabs.add("电子");
//                                    }
                                } else if (sort.equals("5")) {
//                                    if (showSportModule) {
                                    main_tabs.add("体育");
//                                    }
                                } else if (sort.equals("8")) {
//                                    if (showAviaModule){
                                    main_tabs.add("电竞");
//                                    }
                                } else if (sort.equals("9")) {
                                    main_tabs.add("捕鱼");
                                }
                            }
                        }
                    }
                }
            } else {
                tabScrollView.setVisibility(View.GONE);
                scrollBar.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
            }
            if (main_tabs.size() == 1) {
                tabScrollView.setVisibility(View.GONE);
                scrollBar.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
            }

            //根据主页选项卡排序动态添加选项卡view
            tabLayout.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = ScreenUtil.getScreenWidth(requireContext()) / 4;
            params.weight = 1;
            //根据排序创建主页tab栏
            for (final String tab : main_tabs) {
                View layout = LayoutInflater.from(getActivity()).inflate(R.layout.sub_tab_layout, null);
                final TextView tabName = (TextView) layout.findViewById(R.id.m);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentTabName = tab;
                        updateTabWhenClick(tab);
                        updateSelectDataWithCurrentPage(tab);
                    }
                });

                if (tab.equals("彩票")) {
                    tabName.setText("彩票");
                } else if (tab.equals("真人")) {
                    tabName.setText("真人");
                } else if (tab.equals("棋牌")) {
                    tabName.setText("棋牌");
                } else if (tab.equals("电子")) {
                    tabName.setText("电子");
                } else if (tab.equals("体育")) {
                    tabName.setText("体育");
                } else if (tab.equals("电竞")) {
                    tabName.setText("电竞");
                } else if (tab.equals("捕鱼")) {
                    tabName.setText("捕鱼");
                }
                tabLayout.addView(layout, params);
            }
            //默认选中第一个选项卡
            if (!main_tabs.isEmpty()) {
                updateTabWhenClick(main_tabs.get(0));
            }
        }
    }

    private void initEmptyListView() {
        if (emptyListView.getClickRefresh() != null) {
            emptyListView.getClickRefresh().setText("开始网络检测");
        }
        if (emptyListView.getEmptyTxt() != null) {
            emptyListView.getEmptyTxt().setVisibility(View.GONE);
        }
        if (emptyListView.getIv_empty_image() != null) {
            emptyListView.getIv_empty_image().setVisibility(View.GONE);
        }
    }

    //点击tab 时改变tab状态
    private void updateTabWhenClick(String tabName) {
        if (tabLayout != null) {
            for (int i = 0; i < tabLayout.getChildCount(); i++) {
                View v = tabLayout.getChildAt(i);
                TextView name = (TextView) v.findViewById(R.id.m);
                View line = v.findViewById(R.id.line);
                if (name.getText().equals(tabName)) {
                    currentTabName = tabName;
                    name.setTextColor(getActivity().getResources().getColor(R.color.color_txt_select));
                    v.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_game_tab_selected));
//                    line.setVisibility(View.VISIBLE);
                } else {
                    name.setTextColor(getActivity().getResources().getColor(R.color.color_txt_normal));
                    v.setBackground(null);
//                    line.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_refresh:
                startActivity(new Intent(getActivity(), RouteCheckingActivity.class));
                refreshLotteryMessage();
                break;
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
                .refreshAfterCacheHit(true)
                .placeholderText(getString(R.string.sync_caizhong_ing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotterysWraper>() {
                }.getType()))
                .create();
        RequestManager.getInstance().startRequest(getActivity(), lotteryRequest);
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

        if (action == PLAY_RULES_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.request_fail));
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.request_fail) : errorString);
                return;
            }

            Object regResult = result.result;
            LocPlaysWraper reg = (LocPlaysWraper) regResult;
            if (!reg.isSuccess()) {
                showToast("无法取得彩种玩法");
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                String json = new Gson().toJson(reg.getContent(), LotteryData.class);
                String gameCode = reg.getContent().getCode();
                Utils.LOG(TAG, "after play rule, gameCode = " + gameCode);
                CacheRepository.getInstance().saveLotteryPlayJson(getActivity().getApplicationContext(), gameCode, json);
                openTouzhuPage(gameCode, json, false);
            }
        } else if (action == LOTTERYS_REQUEST) {

            CrazyResult<Object> result = response.result;
            refreshLayout.setRefreshing(false);
//            checkNetWorkIsAvai();
            if (result == null || !result.crazySuccess) {
                Utils.LOG(TAG, "LOTTERYS_REQUEST response: result == null || !result.crazySuccess");
                return;
            }

            LotterysWraper stw = (LotterysWraper) result.result;
            if (!stw.isSuccess()) {
                Utils.LOG(TAG, "LOTTERYS_REQUEST response: !stw.isSuccess()");
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

            disposable.clear();
            YiboPreference.instance(getActivity()).setToken(stw.getAccessToken());
            //更新彩种信息界面gridview
            updateLotterysView(stw.getContent());
            if (stw.getContent() != null) {
                Type listType = new TypeToken<ArrayList<LotteryData>>() {
                }.getType();
                lastEventJson = new Gson().toJson(stw.getContent(), listType);
                CacheRepository.getInstance().saveLotteryData(getActivity(), stw.getContent());
            }
        } else if (action == REAL_REQUEST || action == DIANZI_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.jump_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.jump_fail));
                return;
            }
            Object regResult = result.result;
            String reg = (String) regResult;
            if (reg.contains("html") && !reg.contains("url")) {
                //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
                BBinActivity.createIntent(getActivity(), reg, "bbin");
            } else {
                try {
                    JSONObject json = new JSONObject(reg);
                    if (!json.isNull("success")) {
                        boolean success = json.getBoolean("success");
                        if (success) {
//                        showToast(R.string.fee_convert_success);
                            //AG,MG,AB,OG,DS都返回跳转链接
                            //BBIN 返回的是一段html内容
                            String url = !json.isNull("url") ? json.getString("url") : "";
                            String content = !json.isNull("content") ? json.getString("content") : "";
                            if (!Utils.isEmptyString(url)) {
                                SysConfig config = UsualMethod.getConfigFromJson(getActivity());
                                if (config.getZrdz_jump_broswer().equals("on")) {
                                    //跳转到浏览器
                                    UsualMethod.actionViewGame(getActivity(), url);
                                } else {
                                    //只用来限制新沙巴体育
                                    if (nowGameName.equals("新沙巴体育") && config.getOnoff_new_shaba_jump_browsers().equals("on")) {
                                        UsualMethod.actionViewGame(getActivity(), url);
                                        return;
                                    }
                                    SportNewsWebActivity.createIntent(this.getActivity(), url, nowGameName);
                                }
                            } else if (!Utils.isEmptyString(content)) {
                                SysConfig config = UsualMethod.getConfigFromJson(getActivity());
                                if (config.getZrdz_jump_broswer().equals("on")) {
                                    UsualMethod.actionViewGame(getActivity(), content);
                                } else {
                                    if (nowGameName.equals("新沙巴体育") && config.getOnoff_new_shaba_jump_browsers().equals("on")) {
                                        UsualMethod.actionViewGame(getActivity(), content);
                                        return;
                                    }
                                    SportNewsWebActivity.createIntent(this.getActivity(), content, nowGameName);
                                }
                            } else {
                                String html = !json.isNull("html") ? json.getString("html") : "";
                                //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
                                BBinActivity.createIntent(getActivity(), html, "bbin");
                            }
                        } else {
                            if (!json.isNull("msg")) {
                                String msg = json.getString("msg");
                                showToast(msg);
                                if (msg.contains("超时") || msg.contains("其他")) {
                                    UsualMethod.loginWhenSessionInvalid(getActivity());
                                }
                            } else {
                                showToast(getString(R.string.jump_fail));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
                }
            }
        }
//        else if (action == SBSPORT_REQUEST) {
//            CrazyResult<Object> result = response.result;
//            if (result == null) {
//                showToast(getString(R.string.jump_fail));
//                return;
//            }
//            if (!result.crazySuccess) {
//                showToast(getString(R.string.jump_fail));
//                return;
//            }
//            SBSportResultWrapper stw = (SBSportResultWrapper) result.result;
//            if (!stw.isSuccess()) {
//                showToast(!Utils.isEmptyString(stw.getMsg()) ? stw.getMsg() :
//                        "跳转失败");
//                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
//                //所以此接口当code == 0时表示帐号被踢，或登录超时
//                if (stw == null || stw.getCode() == 0) {
//                    UsualMethod.loginWhenSessionInvalid(getActivity());
//                }
//                return;
//            }
//            YiboPreference.instance(getActivity()).setToken(stw.getAccessToken());
//            if (!Utils.isEmptyString(stw.getContent())) {
//                UsualMethod.viewLink(getActivity(), stw.getContent());
//            } else {
//                showToast("没有链接，无法跳转");
//            }
//        }
        else {
            //没有网路
            refreshLayout.setRefreshing(false);
        }
    }

    @Subscribe
    public void onEvent(UpdateAllGameEvent event) {
        Utils.LOG(TAG, "onEvent(UpdateAllGameEvent event)");
        if (event.getLotteryJson() != null && !event.getLotteryJson().equals(lastEventJson)) {
            lastEventJson = event.getLotteryJson();
            Type listType = new TypeToken<ArrayList<LotteryData>>() {
            }.getType();
            List<LotteryData> newData = new Gson().fromJson(event.getLotteryJson(), listType);
            updateLotterysView(newData);
        }
    }

    private void updateLotteryViewFromBackup() {
        CacheRepository.getInstance().loadLotteryData(getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<LotteryData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<LotteryData> data) {
                        Utils.LOG(TAG, "data != null && !data.isEmpty() ==> " + (data != null && !data.isEmpty()));
                        if (data != null && !data.isEmpty())
                            updateLotterysView(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void updateLotterysView(List<LotteryData> data) {
        this.allDatas.clear();
        if (data == null || data.isEmpty()) {
            return;
        }

        gameDatas.clear();
        allDatas.addAll(data);
        List<LotteryData> cp = new ArrayList<>();
        List<LotteryData> sport = new ArrayList<>();
        List<LotteryData> zhenren = new ArrayList<>();
        List<LotteryData> dianzhi = new ArrayList<>();
        List<LotteryData> chess = new ArrayList<>();
        List<LotteryData> esport = new ArrayList<>();
        List<LotteryData> buyu = new ArrayList<>();

        SysConfig config = UsualMethod.getConfigFromJson(getActivity());
        if (config != null) {
            for (LotteryData d : data) {
                if (d.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
                    cp.add(d);
                } else if (d.getModuleCode() == LotteryData.REALMAN_MODULE) {
                    zhenren.add(d);
                } else if (d.getModuleCode() == LotteryData.DIANZI_MODULE) {
                    dianzhi.add(d);
                } else if (d.getModuleCode() == LotteryData.SPORT_MODULE) {
                    sport.add(d);
                } else if (d.getModuleCode() == LotteryData.CHESS_MODULE) {
                    chess.add(d);
                } else if (d.getModuleCode() == LotteryData.ESPORT_MODULE) {
                    esport.add(d);
                } else if (d.getModuleCode() == LotteryData.BUYU_MODULE) {
                    buyu.add(d);
                }
            }
        }

        String mobileIndex = config.getMobileIndex();
        if (mobileIndex.equalsIgnoreCase(Constant.TUCHU_LOTTERY)) {
            this.gameDatas.addAll(cp);
            this.gameDatas.addAll(sport);
            this.gameDatas.addAll(zhenren);
            this.gameDatas.addAll(dianzhi);
            this.gameDatas.addAll(chess);
            this.gameDatas.addAll(esport);
            this.gameDatas.addAll(buyu);
        } else if (mobileIndex.equalsIgnoreCase(Constant.TUCHU_ZHENREN)) {
            this.gameDatas.addAll(zhenren);
            this.gameDatas.addAll(dianzhi);
            this.gameDatas.addAll(cp);
            this.gameDatas.addAll(sport);
            this.gameDatas.addAll(chess);
            this.gameDatas.addAll(esport);
            this.gameDatas.addAll(buyu);
        } else if (mobileIndex.equalsIgnoreCase(Constant.TUCHU_SPORT)) {
            this.gameDatas.addAll(sport);
            this.gameDatas.addAll(cp);
            this.gameDatas.addAll(zhenren);
            this.gameDatas.addAll(dianzhi);
            this.gameDatas.addAll(chess);
            this.gameDatas.addAll(esport);
            this.gameDatas.addAll(buyu);
        } else if (mobileIndex.equalsIgnoreCase(Constant.TUCHU_QIPAI)) {
            this.gameDatas.addAll(chess);
            this.gameDatas.addAll(cp);
            this.gameDatas.addAll(sport);
            this.gameDatas.addAll(zhenren);
            this.gameDatas.addAll(dianzhi);
            this.gameDatas.addAll(esport);
            this.gameDatas.addAll(buyu);
        } else if (mobileIndex.equalsIgnoreCase(Constant.SELECT_TABS)) {
            if (!main_tabs.isEmpty()) {
//                String firstTabName = main_tabs.get(0);
                String firstTabName = currentTabName;
                if (firstTabName.equals("彩票")) {
                    this.gameDatas.addAll(cp);
                } else if (firstTabName.equals("真人")) {
                    this.gameDatas.addAll(zhenren);
                } else if (firstTabName.equals("棋牌")) {
                    this.gameDatas.addAll(chess);
                } else if (firstTabName.equals("电子")) {
                    this.gameDatas.addAll(dianzhi);
                } else if (firstTabName.equals("体育")) {
                    this.gameDatas.addAll(sport);
                } else if (firstTabName.equals("电竞")) {
                    this.gameDatas.addAll(esport);
                } else if (firstTabName.equals("捕鱼")) {
                    this.gameDatas.addAll(buyu);
                }
            }
        } else {
            this.gameDatas.addAll(cp);
            this.gameDatas.addAll(sport);
            this.gameDatas.addAll(zhenren);
            this.gameDatas.addAll(dianzhi);
            this.gameDatas.addAll(chess);
            this.gameDatas.addAll(esport);
            this.gameDatas.addAll(buyu);
        }
        this.gameAdapter.notifyDataSetChanged();
        Utils.setListViewHeightBasedOnChildren(gridGames, 3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        }, 300);
    }

    private void updateSelectDataWithCurrentPage(String tabName) {
        if (allDatas == null || allDatas.isEmpty()) {
            return;
        }
        SysConfig config = UsualMethod.getConfigFromJson(getActivity());
        if (config != null) {
            this.gameDatas.clear();
            for (LotteryData d : allDatas) {
                if (tabName.equals("彩票")) {
                    if (d.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
                        this.gameDatas.add(d);
                    }
                } else if (tabName.equals("真人")) {
                    if (d.getModuleCode() == LotteryData.REALMAN_MODULE) {
                        this.gameDatas.add(d);
                    }
                } else if (tabName.equals("棋牌")) {
                    if (d.getModuleCode() == LotteryData.CHESS_MODULE) {
                        this.gameDatas.add(d);
                    }
                } else if (tabName.equals("电子")) {
                    if (d.getModuleCode() == LotteryData.DIANZI_MODULE) {
                        this.gameDatas.add(d);
                    }
                } else if (tabName.equals("体育")) {
                    if (d.getModuleCode() == LotteryData.SPORT_MODULE) {
                        this.gameDatas.add(d);
                    }
                } else if (tabName.equals("电竞")) {
                    if (d.getModuleCode() == LotteryData.ESPORT_MODULE) {
                        this.gameDatas.add(d);
                    }
                } else if (tabName.equals("捕鱼")) {
                    if (d.getModuleCode() == LotteryData.BUYU_MODULE) {
                        this.gameDatas.add(d);
                    }
                }
            }
        }
        this.gameAdapter.notifyDataSetChanged();
        Utils.setListViewHeightBasedOnChildren(gridGames, 3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        }, 300);
    }

    private String nowGameName = "";//当前的游戏名称 点击真人和电子的时候才用这个参数

    @Override
    public void onGameEvent(String gameCode, int gameModue, String gameName, LotteryData data) {
        Utils.LOG(TAG, "gameCode = " + gameCode + ", gameModule = " + gameModue + ", name = " + gameName + ", data = " + data);
        if (gameModue == LotteryData.CAIPIAO_MODULE) {
            if (gameCode.equalsIgnoreCase(Constant.YCP_CODE)) {
                String result = UsualMethod.forwardGame(getActivity(),
                        gameCode, DIANZI_REQUEST, this, TextUtils.isEmpty(data.getForwardUrl()) ? data.getForwardAction() : data.getForwardUrl());
                if (!Utils.isEmptyString(result)) {
                    showToast(result);
                }
            } else {
//                UsualMethod.syncLotteryPlaysByCode(getActivity(), gameCode, PLAY_RULES_REQUEST, this);
                onCaipiaoClicked(gameCode);
            }
        } else {
            if (Utils.isTestPlay(this.getActivity())) {
                return;
            }
            if (data.getIsListGame() == 1) {
                GameListActivity.createIntent(getContext(), data.getName(), data.getCzCode());
            } else if (data.getIsListGame() == 2) {
                SportActivity.createIntent(getContext(), data.getName(), Constant.SPORT_MODULE_CODE + "");
            } else if (data.getIsListGame() == 0 || data.getIsListGame() == 5) {
                nowGameName = gameName;
                String result = UsualMethod.forwardGame(getActivity(), gameCode, REAL_REQUEST, this, data.getForwardUrl());

                if (!Utils.isEmptyString(result)) {
                    showToast(result);
                }
            }
        }
    }

    private void onCaipiaoClicked(final String gameCode) {
        CacheRepository.getInstance().loadLotteryPlayJson(getActivity(), gameCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Utils.LOG(TAG, "find the backup of play rules");
                        openTouzhuPage(gameCode, json, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.LOG(TAG, "can't find the backup of play rules");
                        e.printStackTrace();
                        UsualMethod.syncLotteryPlaysByCode(getActivity(), gameCode, PLAY_RULES_REQUEST, OldClassicMainFragment.this);
                    }
                });
    }

    private void openTouzhuPage(String gameCode, String json, boolean needRefresh) {
        boolean isPeilvVersion = UsualMethod.isSixMark(getContext(), gameCode) || UsualMethod.isPeilvVersionMethod(getActivity());
        String cpVersion = UsualMethod.isSixMark(getContext(), gameCode) ?
                String.valueOf(Constant.lottery_identify_V2) : YiboPreference.instance(getActivity()).getGameVersion();
//
        boolean isSimpleStyle = UsualMethod.getConfigFromJson(getActivity()).getBet_page_style().equalsIgnoreCase("v1");
        if (isSimpleStyle) {
            TouzhuSimpleActivity.createIntent(getActivity(), json, gameCode, needRefresh, isPeilvVersion, cpVersion);
        } else {
            TouzhuActivity.createIntent(getActivity(), json, gameCode, needRefresh, isPeilvVersion, cpVersion);
        }
    }

    /**
     * 下拉刷新触发
     */
    @Override
    public void onRefresh() {

        //刷新回到第一个tab页面
        if (!Utils.isEmptyString(currentTabName)) {
            updateTabWhenClick(currentTabName);
        } else {
            if (!main_tabs.isEmpty()) {
                updateTabWhenClick(main_tabs.get(0));
            }
        }
        if (headerView != null) {
            headerView.syncHeaderWebDatas();
        }
        refreshLotteryMessage();
        //获取中奖假数据
        getWinningData();
    }

    private void refreshLotteryMessage() {
        allDatas.clear();
        gameDatas.clear();
        loadGameDataFromWeb();
//        checkNetWorkIsAvai();
    }

    private void checkNetWorkIsAvai() {
        if (!crazy_wrapper.Crazy.Utils.Utils.isNetworkAvailable(getActivity())) {
            refreshLayout.setRefreshing(false);
            emptyListView.setVisibility(View.VISIBLE);
            gridGames.setVisibility(View.GONE);
        } else {
            emptyListView.setVisibility(View.GONE);
            gridGames.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setOnlineCount(String count) {
        tv_online_count.setVisibility(View.VISIBLE);
        tv_online_count.setText("在线人数:" + count + "人");
    }

    public void refreshNewMainPageLoginBlock(boolean isLogin, String accountName, double balance) {
        headerView.refreshNewMainPageLoginBlock(isLogin, accountName, balance);
    }
}
