package com.yibo.yiboapp.fragment.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.bean.BetSlipMsgBody;
import com.example.anuo.immodule.bean.ChatBetBean;
import com.example.anuo.immodule.bean.ShareOrderBean;
import com.example.anuo.immodule.bean.base.BetInfo;
import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.event.CommonEvent;
import com.example.anuo.immodule.fragment.base.ChatBaseFragment;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.utils.WindowUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.ChatPlayChooseAdapter;
import com.yibo.yiboapp.adapter.NumbersAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.PeilvData;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.data.PeilvZhushuCalculator;
import com.yibo.yiboapp.data.PlayCodeConstants;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BoundsBean;
import com.yibo.yiboapp.entify.CalcPeilvOrder;
import com.yibo.yiboapp.entify.CountDown;
import com.yibo.yiboapp.entify.DoBetForChatWraper;
import com.yibo.yiboapp.entify.LhcServerTimeWrapper;
import com.yibo.yiboapp.entify.LocCountDownWraper;
import com.yibo.yiboapp.entify.LocPlaysWraper;
import com.yibo.yiboapp.entify.LotteryDownBean;
import com.yibo.yiboapp.entify.LotteryLast;
import com.yibo.yiboapp.entify.MemInfoWraper;
import com.yibo.yiboapp.entify.Meminfo;
import com.yibo.yiboapp.entify.OpenResultDetail;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.PeilvWebResult;
import com.yibo.yiboapp.entify.PeilvWebResultWraper;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.interfaces.PeilvListener;
import com.yibo.yiboapp.interfaces.RuleSelectCallback;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.FragmentUtil;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:Ray
 * Date  :2019年10月30日22:13:54
 * Desc  :聊天室投注页面 ------- 对应 TouzhuSimpleFragment
 */
public class BettingMainFragment extends ChatBaseFragment implements SessionResponse.Listener<CrazyResult<Object>>, ChatPlayChooseAdapter.OnSelectRuleListener {

    private static final String TAG = "BettingMainFragment";

    public static final int LAST_RESULT_REQUEST = 0x01;
    public static final int COUNT_DOWN_REQUEST = 0x02;
    public static final int ACQUIRE_PEILV_REQUEST = 0x03;
    public static final int POST_PEILV_BETS_REQUEST = 0x04;
    public static final int TOKEN_BETS_REQUEST = 0x05;

    public static final int ACCOUNT_REQUEST = 0x10;
    public static final int GET_HEADER = 0x11;
    public static final int PLAY_RULES_REQUEST = 0x07;
    public static final int SYNC_LHC_TIME = 0x100;

    private static final String onbetting = "正在下注";


    public static final int CALC_ZHUSHU_MONEY = 0x1;
    public static final int CLEAR_DATA = 0x2;
    public static final int POST_DATA = 0x3;
    public static final int UPDATE_LISTVIEW = 0x4;
    public static final int LOADING_DATA = 0x5;
    private TextView tv_lottery_type;
    private GridView gv_numbers;
    private TextView tv_empty;
    private TextView tv_qihao;
    private TextView tv_last_qihao;
    private TextView tv_dead_time;
    private TextView tv_balance;
    private LinearLayout ll_machine_choose;
    private FrameLayout fl_fast_or_cold;

    private FrameLayout fl_normal_or_out;
    private Button bt_clear;
    private TextView tv_bet_num;
    private Button bt_confirm;

    private LotteryData lotteryData;

    private boolean isFengPan = false;
    private PlayChooseFragment pcFragment;
    //玩法选择界面当前选中的玩法角标
    private int pos = 0;
    private List<PlayItem> playRules = new ArrayList<>();//彩票玩法
    private String cpTypeCode;//彩票类型代号
    private String cpName;//彩票名称
    private long cpDuration;//彩票开奖间隔时间
    private String cpBianHao;//彩票编码
    private int ago;//开奖时间与封盘时间差,单位秒
    private boolean Firstinto = true;
    //    private OrderComfirmFragment orderComfirmFragment;
    private boolean isPeilv;//是否是赔率版本
    private List<OrderDataInfo> list = new ArrayList<>();
    private String selectPlayCode = "01";
    private String selectRuleCode = "1";
    private String selectPlayName = "";
    private String selectSubPlayName = "";

    /**
     * 最多投注号码个数
     */
    private int maxBetNumber;
    /**
     * 最小奖金
     */
    private float minBounds;
    private float bounds;
    /**
     * 最大奖金
     */
    private float maxBounds;

    /**
     * 最小返水
     */
    private float minRakeback;
    private String winExample = "";
    private String detailDesc = "";
    private String playMethod = "";
    /**
     * 当前期号
     */
    private String currentQihao;
    /**
     * 彩票版本
     */
    private String cpVersion;

    /**
     * 是否赔率版
     */
    private boolean isPeilvVersion;
    /**
     * 是否在奖金版本中选中了六合彩，十分六合彩
     */
    private boolean lhcSelect = false;

    private SoundPool soundPool;

    int music;

    /**
     * 赔率版fragment
     */
    private XYBettingFragment xyFragment;

    /**
     * 奖金版fragment
     */
    private GFBettingFragment gfFragment;
    private Context context;

    private CountDownTimer endlineTouzhuTimer;
    private CountDownTimer lastResultAskTimer;//查询最后开奖结果的倒计时器
    String lastOpenResult = "";//上一期开奖结果
    private OrderComfirmFragment orderComfirmFragment;
    MyHandler myHandler;

    public static final int BASIC_MONEY = 2;
    private ChatSysConfig chatSysConfig;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_betting, container, false);
        this.context = getActivity();
        return view;
    }

    @Override
    protected boolean isAddToBackStack() {
        return true;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        chatSysConfig = ChatSpUtils.instance(context).getChatSysConfig();
        myHandler = new MyHandler(this);
        tv_lottery_type = view.findViewById(R.id.tv_lottery_type);
        gv_numbers = view.findViewById(R.id.numbers);
        tv_empty = view.findViewById(R.id.empty_open_numer);
        tv_qihao = view.findViewById(R.id.qihao);
        tv_last_qihao = view.findViewById(R.id.last_qihao);
        tv_dead_time = view.findViewById(R.id.deadTime);
        tv_balance = view.findViewById(R.id.accountBalance);
        ll_machine_choose = view.findViewById(R.id.llMachineChoose);
        fl_fast_or_cold = view.findViewById(R.id.flFastOrCold);
        fl_normal_or_out = view.findViewById(R.id.flNormalOrOut);

//        ll_history_record = view.findViewById(R.id.history_record);
//        ll_money_layout = view.findViewById(R.id.money_layout);
//        le_money_input = view.findViewById(R.id.money_input);
//        tv_fast_amount = view.findViewById(R.id.txtFastAmount);
//        ll_record_bar = view.findViewById(R.id.record_bar);
//        custom_seekbar = view.findViewById(R.id.custom_seekbar);
        bt_clear = view.findViewById(R.id.clear_jixuan);
        tv_bet_num = view.findViewById(R.id.touzhu_money_txt);
        bt_confirm = view.findViewById(R.id.confirm);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("BettingMainFragment", "onStart");
//        betPresenter.onStart();
    }

    @Override
    protected void loadData() {
        super.loadData();
        cpVersion = YiboPreference.instance(context).getGameVersion();
        updateLotteryConstants(lotteryData);
        //获取小玩法数据
        UsualMethod.syncLotteryPlaysByCode(context, cpBianHao, PLAY_RULES_REQUEST, this);
        //获取用户账户余额
        new Handler().postDelayed(() -> accountWeb(), 5000);
    }


    /**
     * 开始获取最近开奖结果
     *
     * @param bianHao 彩种编码
     */
    private void getKaiJianResult(String bianHao) {
        if (lastResultAskTimer != null) {
            lastResultAskTimer.cancel();
            lastResultAskTimer = null;
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_LAST_RESULT_V2_URL);
        configUrl.append("?lotCode=").append(bianHao);
        configUrl.append("&version=").append(YiboPreference.instance(context).getGameVersion());
        CrazyRequest<CrazyResult<LotteryDownBean>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LAST_RESULT_REQUEST)
                .headers(Urls.getHeader(context))
                .shouldCache(false)
                .listener(this)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotteryDownBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    @Override
    public void onSelect(PlayItem playItem, SubPlayItem lotteryPlay) {
        if (ruleSelectCallback != null) {
            ruleSelectCallback.onRuleCallback(playItem, lotteryPlay, lotteryPlay.getPalyId());
        }

    }


    private final class TouzhuPeilvListener implements PeilvListener {
        @Override
        public void onPeilvAcquire(String playCode, boolean showDialog) {
            getPeilvData(cpTypeCode, playCode, showDialog);
        }

        @Override
        public void onBetPost(List<PeilvPlayData> selectDatas, boolean isMulSelect, String money, int count, double totalMoney, String playRule, String winMoney) {

            realPeilvPostBets(selectDatas, isMulSelect, money);

        }
    }


    private void showToast(String content) {
        ToastUtils.showShort(content);
    }


    private boolean moneyLimit(float maxMoney, float minMoney, float money) {
        //先判断下注的总金额是否在最大和最小下注金额之前
//        if (!Utils.isEmptyString(money)) {
//            int moneyInt = Integer.parseInt(money);
        if (money < minMoney) {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showShort("投注金额不能小于" + minMoney + "元");
//            showToast(String.format(getString(R.string.touzhu_money_min_limit_format), minMoney));
            return false;
        }
        if (money > maxMoney) {
//            showToast(String.format(getString(R.string.touzhu_money_max_limit_format), maxMoney));
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showShort("投注金额不能大于" + maxMoney + "元");
            return false;
        }
//        }
        return true;
    }

    private ArrayList<PeilvPlayData> selectNumList;

    /**
     * 共计多少注 ， 还未选择金额的时候
     *
     * @param selectNumLists
     * @param zhushu
     */
    public void onBottomUpdate(List<PeilvPlayData> selectNumLists, int zhushu) {
        selectNumList = new ArrayList<>();
        if (selectNumLists != null) {
            selectNumList.addAll(selectNumLists);
        }

        if (isPeilv()) {
            if (xyFragment == null || tv_bet_num == null) {
                return;
            }
            tv_bet_num.setText(Html.fromHtml(getString(R.string.bet_simple_bottom_txt, zhushu + "", "0")));

        } else {
            if (xyFragment == null || tv_bet_num == null) {
                return;
            }
            tv_bet_num.setText(Html.fromHtml(getString(R.string.bet_simple_bottom_txt_jiangjin, zhushu + "", "0", "199")));
        }
    }


    public void onClearView() {
        clearZhushuBottonView();
    }

    private void clearZhushuBottonView() {
        if (orderComfirmFragment != null && gfFragment != null) {
            orderComfirmFragment.beishuInput.setText("1");
        }
        onBottomUpdate(0, 0);
    }


    public int calcOutZhushu = 0;
    private String selectedNumbers = "";

    public String getSelectedNumbers() {
        return selectedNumbers;
    }

    public void setSelectedNumbers(String selectedNumbers) {
        this.selectedNumbers = selectedNumbers;
    }

    public float selectedMoney;

    /**
     * 共计多少注 ， 还未选择金额的时候
     *
     * @param selectNumLists
     * @param zhushu
     */
    public void onBottomUpdate(int selectNumLists, double zhushu) {
        String s = String.format("%.2f", zhushu);   //1.13
        this.selectedMoney = Float.parseFloat(s);
        this.calcOutZhushu = selectNumLists;
        String finalZhuShu;
        //不用科学计数法表示
        if (String.valueOf(zhushu).contains("E")) {
            BigDecimal bigDecimal = BigDecimal.valueOf(zhushu);
            finalZhuShu = bigDecimal.toPlainString();
        } else {
            finalZhuShu = String.valueOf(selectedMoney);
        }

        if (isPeilv()) {
            if (xyFragment == null || tv_bet_num == null) {
                return;
            }
            tv_bet_num.setText(Html.fromHtml(getString(R.string.bet_simple_bottom_txt,
                    selectNumLists + "", finalZhuShu)));
        } else {
            if (gfFragment == null || tv_bet_num == null) {
                return;
            }

            minBounds = adjustMinBounds(bounds, selectModeIndex);
            tv_bet_num.setText(Html.fromHtml(getString(R.string.bet_simple_bottom_txt_jiangjin,
                    selectNumLists + "", finalZhuShu + "", String.format("%.3f", minBounds))));
        }
    }


    /**
     * 调整奖金
     *
     * @param
     */
    private float adjustMinBounds(float bounds, int selectModeIndex) {
        switch (selectModeIndex) {
            case 0:
                break;
            case 1:
                bounds = bounds / 10;
                break;
            case 2:
                bounds = bounds / 100;
                break;
        }
        return bounds;
    }

    public void updateBottom(int zhushu, double totalMoney) {
//        if (zhushuTV == null || moneyTV == null) {
//            return;
//        }
//        zhushuTV.setText(String.format(Locale.CHINA, "共选中%d注", zhushu));
////        float rate = minRate - totalMoney;
//        moneyTV.setText(String.format(Locale.CHINA, "共%.2f元，奖金%.2f元", totalMoney, minRate));


    }


    public void updatePlayRuleExpandListView() {
        // 初始化玩法fragment
        if (pcFragment == null) {
            pcFragment = new PlayChooseFragment();

            //点击侧边栏刷新赔率玩法数据
            pcFragment.setRuleSelectCallback(this);
            pcFragment.setPositon(pos);
        }
        FragmentUtil.init(this);
        FragmentUtil.addAndHide(pcFragment, R.id.child_fragment);

        pcFragment.updatePlayRules(playRules);
    }

    String playName;

    /**
     * 赔率版下单投注
     *
     * @param betDatas
     * @param money,若金额不为空，说明是多选状态
     */
    private void realPeilvPostBets(List<PeilvPlayData> betDatas, boolean mulSelect, String money) {
        if (betDatas == null || betDatas.isEmpty()) {
            return;
        }

        if (maxBetNumber != 0) {
            Map<String, Integer> map = new HashMap<>(20);
            for(PeilvPlayData playData: betDatas) {
                if (map.containsKey(playData.getItemName())) {
                    map.put(playData.getItemName(), map.get(playData.getItemName()) + 1);
                } else {
                    map.put(playData.getItemName(), 1);
                }

                if (map.get(playData.getItemName()) > maxBetNumber) {
                    showToast("投注失败,超过最大限制码数[" + maxBetNumber + "]码");
                    return;
                }
            }
        }


        //用于分享注单的临时数据
        list.clear();
        if (UsualMethod.isMulSelectMode(selectPlayCode)) {
            StringBuilder balloonCount = new StringBuilder();
            for (int i = 0; i < betDatas.size(); i++) {
                if (i == betDatas.size() - 1) {
                    balloonCount.append(betDatas.get(i).getNumber());
                } else {
                    balloonCount.append(betDatas.get(i).getNumber()).append(",");
                }
            }
            OrderDataInfo orderDataInfo = new OrderDataInfo();
            orderDataInfo.setNumbers(balloonCount.toString());
            orderDataInfo.setMoney(betDatas.get(0).getMoney() * betDatas.size());
            orderDataInfo.setZhushu(betDatas.size());
            orderDataInfo.setPlayName(TextUtils.isEmpty(betDatas.get(0).getItemName()) ? playName : playName + "--" + betDatas.get(0).getItemName());
            list.add(orderDataInfo);
        } else {
            for (PeilvPlayData data : betDatas) {
                OrderDataInfo orderDataInfo = new OrderDataInfo();
                orderDataInfo.setNumbers(data.getNumber());
                orderDataInfo.setMoney(data.getMoney());
                orderDataInfo.setPlayName(TextUtils.isEmpty(data.getItemName()) ? playName : playName + "--" + data.getItemName());
                list.add(orderDataInfo);
            }

        }


        //构造下注POST数据
        try {
            StringBuilder postUrl = new StringBuilder();
            String betIp = ChatSpUtils.instance(context).getBET_IP();
            if (UsualMethod.isSixMark(context,cpBianHao)) {
                JSONArray betsArray = null;
                if (mulSelect) {
                    PeilvPlayData pData = betDatas.get(0);
                    int minSelectCount = pData.getPeilvData() != null ? pData.getPeilvData().getMinSelected() : 0;
                    if (minSelectCount > betDatas.size()) {
                        if (isAdded()) {
                            showToast(String.format(getString(R.string.min_select_numbers_format), minSelectCount));
                        }
                        return;
                    }

                    //若是连码情况，则所选号码不能大于10个
                    if (this.selectPlayCode.equals(PlayCodeConstants.lianma) && betDatas.size() > 10) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 10));
                        return;
                    } else if (this.selectPlayCode.equals(PlayCodeConstants.lianxiao) && betDatas.size() > 6) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 6));
                        return;
                    } else if (this.selectPlayCode.equals(PlayCodeConstants.weishulian) && betDatas.size() > 6) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 6));
                        return;
                    }

                    if (selectPlayCode.equals(PlayCodeConstants.quanbuzhong)) {
                        switch (minSelectCount) {
                            case 3:
                            case 4:
                                if (betDatas.size() > 8) {
                                    showToast("选球数请勿大于8位!");
                                    return;
                                }
                                break;
                            case 5:
                            case 6:
                            case 7:
                                if (betDatas.size() > 10) {
                                    showToast(String.format("选球数请勿大于%d位!", 10));
                                    return;
                                }
                                break;
                            case 8:
                            case 9:
                                if (betDatas.size() > minSelectCount + 3) {
                                    showToast(String.format("选球数请勿大于%d位!", minSelectCount + 3));
                                    return;
                                }
                                break;
                            case 10:
                                if (betDatas.size() > 13) {
                                    showToast(String.format("选球数请勿大于%d位!", 13));
                                    return;
                                }
                                break;
                            case 11:
                            case 12:
                                if (betDatas.size() > minSelectCount + 2) {
                                    showToast(String.format("选球数请勿大于%d位!", minSelectCount + 2));
                                    return;
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (selectPlayCode.equalsIgnoreCase(PlayCodeConstants.lianma)) {
                        if (betDatas.size() > 10) {
                            showToast(String.format("号码数请勿大于%d位!", 10));
                            return;
                        }
                    }


                    List<String> numbers = new ArrayList<>();
                    for (PeilvPlayData data : betDatas) {
                        numbers.add(data.getNumber());
                    }
                    if (!selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str) && !selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str_zx)) {
                        Collections.sort(numbers);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < numbers.size(); i++) {
                        String num = numbers.get(i);
                        sb.append(num.trim());
                        if (i != numbers.size() - 1) {
                            sb.append(",");
                        }
                    }
//                    Utils.LOG(TAG,"the sort number = "+sb.toString());
                    //从多个赔率数据中根据用户选择的选项数，选择出选项数正确的赔率数据
                    betsArray = new JSONArray();
                    PeilvWebResult webResult = UsualMethod.getPeilvData(getContext(),cpBianHao, selectPlayCode, betDatas.size(), pData);
                    if (webResult != null) {
                        //先判断下注的总金额是否在最大和最小下注金额之
                        if (!Utils.isEmptyString(money)) {
                            float moneyFloat = Float.parseFloat(money);
                            boolean rightMoney = moneyLimit(webResult.getMaxBetAmmount(), webResult.getMinBetAmmount(), moneyFloat);
                            if (!rightMoney) {
                                return;
                            }
                        }
                        JSONObject item = new JSONObject();
                        item.put("haoma", sb.toString());
                        item.put("money", money);
                        item.put("markSixId", webResult.getId());
                        item.put("betIp", betIp);
                        betsArray.put(item);
                    }
                } else {
                    betsArray = new JSONArray();
                    for (PeilvPlayData data : betDatas) {
                        //先判断下注的总金额是否在最大和最小下注金额之前
                        if (data.getPeilvData() != null) {
                            PeilvWebResult peilvData = data.getPeilvData();
                            if (peilvData != null) {
                                boolean rightMoney = moneyLimit(peilvData.getMaxBetAmmount(), peilvData.getMinBetAmmount(), data.getMoney());
                                if (!rightMoney) {
                                    return;
                                }
                            }
                        }
                        JSONObject item = new JSONObject();
                        item.put("haoma", UsualMethod.getPeilvPostNumbers(data));
                        item.put("money", String.valueOf((int) data.getMoney()));
                        item.put("markSixId", String.valueOf(data.getPeilvData() != null ? data.getPeilvData().getId() : 0));
                        item.put("betIp", betIp);
                        betsArray.put(item);
                    }
                    Utils.LOG(TAG, "the betsarray == " + betsArray.toString());
                }

                JSONObject dataPost = new JSONObject();
                dataPost.put("pour", betsArray);
                dataPost.put("lotCode", cpBianHao);
                dataPost.put("playCode", selectRuleCode);
                dataPost.put("state", mulSelect ? 1 : 2);
                dataPost.put("qiHao", currentQihao);

                //构造下注crazy request
                postUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.DO_SIX_MARK_URL);
                postUrl.append("?data=").append(URLEncoder.encode(dataPost.toString(), "utf-8"));

            } else {

                String betJson = "";
                PeilvPlayData pData = betDatas.get(0);
                if (mulSelect) {
                    int minSelectCount = pData.getPeilvData() != null ? pData.getPeilvData().getMinSelected() : 0;
                    if (minSelectCount > betDatas.size()) {
                        showToast(String.format(getString(R.string.min_select_numbers_format), minSelectCount));
                        return;
                    }

                    //时时彩,分分彩系列---组选六不能大于八位
                    if (this.selectPlayCode.equals(PlayCodeConstants.zuxuan_liu_peilv) && betDatas.size() > 8) {
                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 8));
                        return;
                    }

                    //选4任选选5任选最多只能选6个号码
                    if (this.selectRuleCode.equals(PlayCodeConstants.xuansirenxuan) && betDatas.size() > 6) {

                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 6));
                        return;
                    }
                    if (this.selectRuleCode.equals(PlayCodeConstants.xuanwurenxuan) && betDatas.size() > 6) {
                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 6));
                        return;
                    }
                    //若是连码情况，则所选号码不能大于10个
                    if (this.cpTypeCode.equals("12")) {
                        if (this.selectPlayCode.equals(PlayCodeConstants.lianma_peilv_klsf) && betDatas.size() > 8) {
                            showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 8));
                            return;
                        }
                    } else if (selectPlayCode.equals(PlayCodeConstants.zuxuan_liu_peilv) && betDatas.size() > 8) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 8));
                        return;
                    }

                    if (this.selectPlayCode.equals(PlayCodeConstants.lianxiao) && betDatas.size() > 6) {
                        showToast(String.format("所选球数请勿大于%d", 6));
                        return;
                    }


                    List<String> numbers = new ArrayList<>();
                    JSONArray betsArray = new JSONArray();
                    for (PeilvPlayData data : betDatas) {
                        numbers.add(data.getNumber());
                    }

                    if (!selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str) && !selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str_zx)) {
                        Collections.sort(numbers);
                    } else {
                        //十一选五直选
                        int differentBallNum = 0;
                        for (int i = 0; i < betDatas.size(); i++) {
                            String firstName = betDatas.get(0).getItemName();
                            if (!betDatas.get(i).getItemName().equals(firstName)) {
                                differentBallNum++;
                            }
                        }

                        if (differentBallNum == 0) {
                            ToastUtils.showShort("下注拖胆号码错误");
                            return;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < numbers.size(); i++) {
                        String num = numbers.get(i);
                        sb.append(num.trim());
                        if (i != numbers.size() - 1) {
                            sb.append(",");
                        }
                    }
                    Utils.LOG(TAG, "the sort number = " + sb.toString());
                    String name = sb.toString();

                    JSONObject item = new JSONObject();
                    item.put("name", name);
                    item.put("money", money);
                    item.put("betIp", betIp);
                    //从多个赔率数据中根据用户选择的选项数，选择出选项数正确的赔率数据

                    PeilvWebResult webResult = UsualMethod.getPeilvData(getContext(),cpBianHao, selectPlayCode, betDatas.size(), pData);
                    if (webResult != null) {
                        //先判断下注的总金额是否在最大和最小下注金额之前
                        if (!Utils.isEmptyString(money)) {
                            float moneyFloat = Float.parseFloat(money);
                            boolean rightMoney = moneyLimit(webResult.getMaxBetAmmount(), webResult.getMinBetAmmount(), moneyFloat);
                            if (!rightMoney) {
                                return;
                            }
                        }
                        item.put("oddsId", String.valueOf(webResult.getId()));
                        item.put("odds", String.valueOf(webResult.getOdds()));
                        item.put("rate", String.valueOf(webResult.getRakeBack()));
                    }
                    betsArray.put(item);
                    betJson = betsArray.toString();
                } else {
                    JSONArray betsArray = new JSONArray();
                    for (PeilvPlayData data : betDatas) {

                        //先判断下注的总金额是否在最大和最小下注金额之前
                        if (data.getPeilvData() != null) {
                            PeilvWebResult peilvData = data.getPeilvData();
                            if (peilvData != null) {
                                boolean rightMoney = moneyLimit(peilvData.getMaxBetAmmount(), peilvData.getMinBetAmmount(), data.getMoney());
                                if (!rightMoney) {
                                    return;
                                }
                            }
                        }
                        JSONObject item = new JSONObject();
                        item.put("betIp", betIp);
                        item.put("name", UsualMethod.getPeilvPostNumbers(data));
                        item.put("money", String.valueOf((int) data.getMoney()));
                        item.put("oddsId", String.valueOf(data.getPeilvData() != null ? data.getPeilvData().getId() : 0));
                        item.put("rate", String.valueOf(data.getPeilvData() != null ? data.getPeilvData().getRakeBack() : 0));
                        betsArray.put(item);

                    }
                    betJson = betsArray.toString();
                }
                //构造下注crazy request

                postUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.DO_PEILVBETS_URL);
                postUrl.append("?data=").append(URLEncoder.encode(betJson, "utf-8"));
                postUrl.append("&lotCode=").append(cpBianHao);
                postUrl.append("&qiHao=").append(currentQihao);
                postUrl.append("&lotType=").append(cpTypeCode);
                postUrl.append("&playCode=").append(selectRuleCode);
                postUrl.append("&groupCode=").append(selectPlayCode);
                postUrl.append("&stId=").append(pData.getPeilvData().getStationId());


            }


            CrazyRequest<CrazyResult<DoBetForChatWraper>> request = new AbstractCrazyRequest.Builder().
                    url(postUrl.toString())
                    .seqnumber(POST_PEILV_BETS_REQUEST)
                    .headers(Urls.getHeader(context))
                    .listener(this)
                    .placeholderText(getString(R.string.bet_ongoing))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<DoBetForChatWraper>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(context, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据小玩法及彩种编码获取赔率信息
     *
     * @param lotCategoryType 彩种类型编码
     * @param playCode        玩法代号
     * @param showDialog
     */
    private void getPeilvData(String lotCategoryType, String playCode, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_PVODDS_URL);
        configUrl.append("?playCode=").append(playCode).append("&");
        configUrl.append("lotType=").append(lotCategoryType);

        CrazyRequest<CrazyResult<PeilvWebResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(ACQUIRE_PEILV_REQUEST)
                .headers(Urls.getHeader(context))
                .cachePeroid(30 * 1000)
                .listener(this)
                .shouldCache(true).placeholderText(getString(R.string.get_peilv_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<PeilvWebResultWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
//        if (!showDialog) {
//            startProgress();
//        }
    }


    private void updateLotteryConstants(LotteryData lotteryData) {
        if (lotteryData == null) {
            return;
        }
        playRules = lotteryData.getRules();
        cpTypeCode = lotteryData.getCzCode();
        cpBianHao = lotteryData.getCode();
        cpName = lotteryData.getName();
        cpDuration = lotteryData.getDuration();
        ago = lotteryData.getAgo() != null ? lotteryData.getAgo().intValue() : 0;
        LogUtils.e("ago:" + ago);

        Firstinto = true;
    }

    @Override
    protected void initListener() {
        super.initListener();
        ll_machine_choose.setOnClickListener(this);
        fl_fast_or_cold.setOnClickListener(this);
        fl_normal_or_out.setOnClickListener(this);
        tv_lottery_type.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);

    }

    private String moneyValue = "";

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CommonEvent event) {
        switch (event.getTag()) {
            case EventCons.CALCULATE_MONEY:
                moneyValue = (String) event.getData();
                if (selectNumList.isEmpty()) {
                    return;
                }
                for (PeilvPlayData data : selectNumList) {
                    if (data.isSelected()) {
                        data.setMoney(Float.parseFloat(moneyValue));
                    }
                }
                for (int i = 0; i < xyFragment.listDatas.size(); i++) {
                    PeilvData peilvData = xyFragment.listDatas.get(i);
                    for (int j = 0; j < peilvData.getSubData().size(); j++) {
                        PeilvPlayData data = peilvData.getSubData().get(j);
                        if (data.isSelected()) {
                            Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, i, j);
                            message.obj = Float.parseFloat(moneyValue);
                            message.sendToTarget();
                        }
                    }
                }
                break;
            case EventCons.XY_CONFIRM_BETTING:
//                AmountRebateBean rebateBean = (AmountRebateBean) event.getData();
//                mPresenter.actionPostTouzhu(rebateBean.getMoney(), rebateBean.getCurrentRateback());
                break;
            case EventCons.CLEAN_ORDER:
                onPlayClean(true);
                break;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_lottery_type:
                if (gfFragment == null && xyFragment == null) {
                    return;
                }
                FragmentUtil.init(this);
                if (bt_clear.getText().toString().equals("返回")) {
                    FragmentUtil.removeAndShow(orderComfirmFragment, pcFragment);
                    orderComfirmFragment = null;
                    bt_clear.setText("清除");
                    bt_confirm.setText("确定");
                    return;
                }
                if (!isPeilv()) {
                    if (gfFragment == null || pcFragment == null) return;
                    if (gfFragment.isHidden()) {
                        FragmentUtil.hideAndShow(pcFragment, gfFragment);
                    } else {
                        FragmentUtil.hideAndShow(gfFragment, pcFragment);
                    }
                } else {
                    if (xyFragment == null || pcFragment == null) return;
                    if (xyFragment.isHidden()) {
                        FragmentUtil.hideAndShow(pcFragment, xyFragment);
                    } else {
                        FragmentUtil.hideAndShow(xyFragment, pcFragment);
                    }
                }
                break;

            case R.id.confirm:
                confirmClick();
                break;
            case R.id.clear_jixuan:
                if (bt_clear.getText().toString().equals("返回")) {
                    FragmentUtil.init(this);
                    FragmentUtil.removeAndShow(orderComfirmFragment, isPeilv() ? xyFragment : gfFragment);
                    if (!isPeilv) {
                        DatabaseUtils.getInstance(getActivity()).deleteAllOrder();
                    }
                    orderComfirmFragment = null;
                    bt_clear.setText("清除");
                    bt_confirm.setText("确定");
                    return;
                }
                if (isFengPan) {
                    return;
                }
//                EventBus.getDefault().post(new CommonEvent(EventCons.CLEAN_ORDER));
                if (isPeilv()) {
                    onPlayClean(true);
                } else {
                    gfFragment.actionCleanSelectBllons();
                    refreshPaneAndClean();
                    onBottomUpdate(0, 0);
                }
                break;
        }

        super.onClick(v);
    }


    public void onPlayClean(boolean clearAfterBetSuccess) {
//        moneyTV.setText("");
        //隐藏键盘
        TouzhuThreadPool.getInstance().addTask(new PeilvBetRunnable(selectNumList, true, clearAfterBetSuccess));
    }

    //下注线程
    private final class PeilvBetRunnable implements Runnable {

        List<PeilvPlayData> datas;
        boolean clear;
        boolean clearAfterBetSuccess;


        PeilvBetRunnable(List<PeilvPlayData> datas, boolean clear, boolean clearAfterBetSuccess) {
            this.datas = datas;
            this.clear = clear;
            this.clearAfterBetSuccess = clearAfterBetSuccess;
        }

        @Override
        public synchronized void run() {
            if (clear) {
                if (datas == null) {
                    return;
                }
                synchronized (datas) {
                    for (PeilvPlayData playData : datas) {
                        playData.setSelected(false);
                        playData.setMoney(0);
                        playData.setFocusDrawable(0);
                    }
                    Message message = myHandler.obtainMessage(CLEAR_DATA, clearAfterBetSuccess);
                    message.sendToTarget();
                    return;
                }
            }
//            String moneyValue = moneyTV.getText().toString().trim();
//            for (PeilvPlayData playData : datas) {
//                if (!playData.isCheckbox()) {
//                    if (!Utils.isEmptyString(moneyValue)) {
////                        playData.setMoney(Float.parseFloat(moneyValue));
//                    }
//                }
//            }
            Message message = myHandler.obtainMessage(POST_DATA, datas);
            message.sendToTarget();
        }
    }


    private void confirmClick() {
        //进购彩清单页,而不是直接下注
        if (isFengPan) {
            ToastUtils.showShort("当前尚未开盘");
            return;
        }
        String confirmCon = bt_confirm.getText().toString();
        if (confirmCon.equals("确定")) {

            //当前在彩种选择fragment的时候要禁用点击事件
            if (pcFragment.isVisible()) {
                return;
            }
            if (!isPeilv()) {
                if (gfFragment != null) {
                    confirmGfChatTouzhu();
                }
            } else {
                if (xyFragment != null) {
                    confirmChatTouzhu();
                }
            }
        } else if (confirmCon.equals("投注")) {

            if (isPeilv()) {
                //如果是赔率版本的投注
                onPlayTouzhu();

            } else {
                //如果是奖金版本的下注
                if (orderComfirmFragment != null) {
                    orderComfirmFragment.onGfPlayTouzhu();
                }
            }
        }
    }


    private void confirmGfChatTouzhu() {

        onPlayJiangJinTouzhu();
    }


    public void onPlayJiangJinTouzhu() {

        if (isFengPan) {
            ToastUtils.showShort("当前尚未开盘");
            return;
        }

        if (calcOutZhushu == 0) {
            ToastUtils.showShort(R.string.please_select_correct_numbers);
            return;
        }
        //移动注单到购物车VIEW
        if (Utils.isEmptyString(selectedNumbers)) {
            ToastUtils.showShort(R.string.havenot_calcout_number);
            return;
        }
        OrderDataInfo info = addZhuDang();
        String orderJson = new Gson().toJson(info, OrderDataInfo.class);

        bt_clear.setText("返回");
        bt_confirm.setText("投注");
        FragmentUtil.init(this);
        orderComfirmFragment = new OrderComfirmFragment();
        Bundle bundle1 = new Bundle();

        //官方专用
        bundle1.putString("orderJson", orderJson);
        bundle1.putString("cpVersion", cpVersion);
        bundle1.putString("cpName", cpName);
        bundle1.putLong("cpDuration", cpDuration);
        bundle1.putInt("selectModeIndex", selectModeIndex);

        //通用
        bundle1.putString("selectPlayCode", selectPlayCode);
        bundle1.putBoolean("isOfficial", true);
        bundle1.putBoolean("isFengPan", isFengPan);
        bundle1.putString("tvLotteryType", tv_lottery_type.getText().toString());
        bundle1.putParcelableArrayList("orderData", selectNumList);
        orderComfirmFragment.setArguments(bundle1);
        FragmentUtil.addShowAndHide(orderComfirmFragment, gfFragment, R.id.child_fragment);

        gfFragment.actionCleanSelectBllons();
        refreshPaneAndClean();
    }

    public int selectModeIndex = 0;//金额模式，元模式


    public void setSelectModeIndex(int selectModeIndex) {
        this.selectModeIndex = selectModeIndex;
    }

    //将投注信息加入数据库表
    private OrderDataInfo addZhuDang() {
        DatabaseUtils instance = DatabaseUtils.getInstance(getActivity());
        if (!instance.isBetOrderExist(cpBianHao, selectPlayCode, selectRuleCode)) {
            DatabaseUtils.getInstance(getActivity()).deleteAllOrder();
        }
        OrderDataInfo info = new OrderDataInfo();
        info.setUser(YiboPreference.instance(getActivity()).getUsername());
        info.setPlayName(selectPlayName);
        info.setPlayCode(selectPlayCode);
        info.setSubPlayName(selectSubPlayName);
        info.setSubPlayCode(selectRuleCode);
        info.setBeishu(selectedBeishu);
        info.setZhushu(calcOutZhushu);
        info.setMoney(selectedMoney);
        info.setNumbers(selectedNumbers);
        info.setCpCode(cpBianHao);
        info.setLotcode(cpBianHao);
        info.setLottype(cpTypeCode);
        if (selectModeIndex == 0) {
            info.setMode(Constant.YUAN_MODE);
        } else if (selectModeIndex == 1) {
            info.setMode(Constant.JIAO_MODE);
        } else if (selectModeIndex == 2) {
            info.setMode(Constant.FEN_MODE);
        }
        info.setSaveTime(System.currentTimeMillis());
        DatabaseUtils.getInstance(getActivity()).saveCart(info);
        return info;
    }

    //切换到注单确认fragment
    private void confirmChatTouzhu() {
        if (selectNumList == null || selectNumList.size() == 0) {
            ToastUtils.showShort("请先选择号码在投注！");
            return;
        }
        bt_clear.setText("返回");
        bt_confirm.setText("投注");
        FragmentUtil.init(this);
        orderComfirmFragment = new OrderComfirmFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("selectPlayCode", selectPlayCode);
        bundle1.putBoolean("isOfficial", false);
        bundle1.putBoolean("isFengPan", isFengPan);
        bundle1.putString("tvLotteryType", tv_lottery_type.getText().toString());
        bundle1.putParcelableArrayList("orderData", selectNumList);
        orderComfirmFragment.setArguments(bundle1);
        FragmentUtil.addShowAndHide(orderComfirmFragment, xyFragment, R.id.child_fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WindowUtil.hideSoftInput(act);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    /**
     * 根据开奖结果显示号码view
     *
     * @param haoMaList
     */
    private void updateGridView(List<String> haoMaList, String cpTypeCode, String date) {
        if (haoMaList == null || haoMaList.isEmpty()) {
            return;
        }

        if (cpTypeCode.equals("6") || cpTypeCode.equals("66")) {
            gv_numbers.setNumColumns(haoMaList.size() + 1);
        } else {
            gv_numbers.setNumColumns(haoMaList.size());
        }

        NumbersAdapter numbersAdapter = new NumbersAdapter(context, haoMaList, R.layout.chat_touzhu_number_gridview_item, cpTypeCode, cpBianHao);
        numbersAdapter.setShowShenxiao(true);
        numbersAdapter.setDate(date);
        gv_numbers.setAdapter(numbersAdapter);
        Utils.setListViewHeightBasedOnChildren(gv_numbers, 10);
    }


    public boolean isPeilv() {
        return isPeilv;
    }

    public void setPeilv(boolean peilv) {
        isPeilv = peilv;
    }

    public void setLotteryData(LotteryData lotteryData) {
        this.lotteryData = lotteryData;
    }

    private final class MyHandler extends Handler {

        private WeakReference<BettingMainFragment> mReference;
        private BettingMainFragment fragment;

        public MyHandler(BettingMainFragment fragment) {
            mReference = new WeakReference<>(fragment);
            this.fragment = mReference.get();
        }


        public void handleMessage(Message message) {
            if (fragment == null) {
                return;
            }
            switch (message.what) {
                case CALC_ZHUSHU_MONEY:
                    CalcPeilvOrder peilvOrder = (CalcPeilvOrder) message.obj;
                    int zhushu = peilvOrder.getZhushu();
                    double money = peilvOrder.getTotalMoney();
                    float peilv = peilvOrder.getPeilvWhenMultiselect();
                    List<PeilvPlayData> datas = peilvOrder.getSelectDatas();
                    if (datas != null) {
                        selectNumList.clear();
                        selectNumList.addAll(datas);
                    }
                    //若是多选组合号码，则这里的总金额是输入框输入的金额，而不是注数*每注金额
                    if (peilvOrder.isMultiSelect()) {
                        String moneyStr = moneyValue;
                        if (!Utils.isEmptyString(moneyStr)) {
                            money = Double.parseDouble(moneyStr);
                        }
                        money = money * zhushu;
                    }
//                    fragment.totalMoney = money;
//                    fragment.calcPeilv = peilv;
//                    fragment.calcOutZhushu = zhushu;
                    onBottomUpdate(zhushu, money);
                    break;
                case CLEAR_DATA:
                    boolean clearAfterSuccess = (boolean) message.obj;
                    if (!clearAfterSuccess) {
                        ToastUtils.showShort(R.string.clean_success);
                    }
                    selectNumList.clear();
                    actionRestore();
//                    calcPeilv = 0;
//                    totalMoney = 0;
//                    calcOutZhushu = 0;

                    onBottomUpdate(null, 0);
                    break;
                case POST_DATA:
                    List<PeilvPlayData> selectedItems = (List<PeilvPlayData>) message.obj;
                    if (selectedItems != null && !selectedItems.isEmpty()) {
                        String moneyStr = "";
                        PeilvPlayData peilvData = selectedItems.get(0);
//                        if (peilvData.isCheckbox()) {
                        moneyStr = moneyValue;
                        if (Utils.isEmptyString(moneyStr)) {
                            ToastUtils.showShort(R.string.please_tapin_bet_money);
                            return;
                        }
//                        }
//                        if (peilvListener != null) {
//                            onBetPost(selectedItems, peilvData.isCheckbox(), moneyStr, calcOutZhushu, totalMoney, playRuleTV.getText().toString());
//                        }
                        realPeilvPostBets(selectedItems, UsualMethod.isMulSelectMode(selectPlayCode), moneyStr);
                    } else {
                        ToastUtils.showShort((R.string.please_touzhu_first));
                    }
                    break;
                case UPDATE_LISTVIEW:
//                    refreshPaneAndClean(true);

                    List<PeilvData> listDatas = xyFragment.listDatas;
                    XYBettingFragment.PeilvAdapter playAdapter = xyFragment.playAdapter;
                    int listPos = message.arg1;
                    int gridPos = message.arg2;
                    float moneyValue = -1;
                    if (message.obj != null) {
                        moneyValue = (float) message.obj;
                    } else {
                        moneyValue = -1;
                    }
                    if (xyFragment.listDatas.get(listPos).getSubData() != null) {
                        if (moneyValue != -1) {
                            PeilvPlayData playData = listDatas.get(listPos).getSubData().get(gridPos);
                            if (playData != null) {
                                playData.setMoney(moneyValue);
                                listDatas.get(listPos).getSubData().set(gridPos, playData);
                            }
                        }
                    }
//                        float moneyValue = (float) message.obj;
//                        updateView(listPos,gridPos,moneyValue);
//                    }else{
////                        updateView(listPos, gridPos, -1);
//                    }
                    playAdapter.notifyDataSetChanged();
                    //输入法点确定后，将选择的号码，金额发送到主界面更新底部栏显示出来
                    TouzhuThreadPool.getInstance().addTask(new CalcZhushuAndMoney(fragment, listDatas));
                    break;
//                case LOADING_DATA:
////                    fragment.dismissProgressDialog();
//                    List<PeilvData> peilvs = (List<PeilvData>) message.obj;
//                    if (peilvs == null) {
//                        listDatas.clear();
//                        playAdapter.notifyDataSetChanged();
//                        playListview.setVisibility(View.VISIBLE);
//                        return;
//                    }
//                    listDatas.addAll(peilvs);
//                    refreshPaneAndClean(false);
//                    break;
            }
        }
    }


    //异步计算总注数和总金额
    private final class CalcZhushuAndMoney implements Runnable {

        List<PeilvData> datas;
        WeakReference<BettingMainFragment> weak;
        List<PeilvPlayData> selectedDatas;

        CalcZhushuAndMoney(BettingMainFragment context, List<PeilvData> datas) {
            this.datas = datas;
            weak = new WeakReference<>(context);
            selectedDatas = new ArrayList<>();
        }

        @Override
        public void run() {

            if (weak == null) {
                return;
            }
            if (datas == null) {
                return;
            }

            for (PeilvData data : datas) {
                List<PeilvPlayData> subs = data.getSubData();
                for (PeilvPlayData ppd : subs) {
                    if (isSelectedNumber(ppd)) {
                        if (!ppd.isFilterSpecialSuffix()) {
                            ppd.setAppendTag(data.isAppendTag());
                            ppd.setItemName(!Utils.isEmptyString(data.getPostTagName()) && !Utils.isEmptyString(data.getPostTagName())
                                    ? data.getPostTagName() : data.getTagName());
                        } else {
                            ppd.setAppendTag(false);
                        }
                        selectedDatas.add(ppd);
                    }
                }
            }
            calcZhushuAndSendMessage(selectedDatas);
        }

        private void calcZhushuAndSendMessage(List<PeilvPlayData> selectDatas) {
            if (selectDatas.isEmpty()) {
                sendMessage(0, 0, 0, UsualMethod.isMulSelectMode(selectPlayCode), selectDatas);
                return;
            }
            PeilvPlayData pData = selectDatas.get(0);
            if (pData == null) {
                sendMessage(0, 0, 0, UsualMethod.isMulSelectMode(selectPlayCode), selectDatas);
                return;
            }
            int totalZhushu = 0;
            double totalMoney = 0;
            float peilvWhenMultiSelect = 0;
            boolean isMultiSelect = pData.isCheckbox();
            //若是多选情况下，则将多个号码拼接起来,以逗号分隔
            if (isMultiSelect) {
                int minSelectCount = pData.getPeilvData() != null ? pData.getPeilvData().getMinSelected() : 0;
                if (minSelectCount > selectDatas.size()) {
                    Utils.LOG("aa", "选择的号码小于" + pData.getPeilvData().getMinSelected() + ",不必计算注数");
                    sendMessage(0, 0, 0, isMultiSelect, selectDatas);
                    return;
                }

                StringBuilder numbers = new StringBuilder();
                for (int i = 0; i < selectDatas.size(); i++) {
                    PeilvPlayData playData = selectDatas.get(i);
                    numbers.append(playData.getNumber().trim());
                    if (i != selectDatas.size() - 1) {
                        numbers.append(",");
                    }
                }
                PeilvWebResult webResult = UsualMethod.getPeilvData(getContext(),cpBianHao, selectPlayCode, selectDatas.size(), pData);
                if (webResult == null) {
                    ToastUtils.showShort("所选号码请勿大于8位");
                    return;
                }
                Integer zhushu = PeilvZhushuCalculator.buyZhuShuValidate(numbers.toString(),
                        webResult.getMinSelected(), webResult.getPlayCode());
//                Utils.LOG(TAG, "zhushu ==== " + zhushu);
                totalZhushu += zhushu;
//                String moneyValue = moneyTV.getText().toString().trim();
//                if (!Utils.isEmptyString(moneyValue)) {
//                    totalMoney = Float.parseFloat(moneyValue);
//                }
                peilvWhenMultiSelect = webResult.getOdds();
                List<PeilvWebResult> list = new ArrayList<>();
                list.add(webResult);
                if (!UsualMethod.dontShowPeilvWhenTouzhu(selectPlayCode)) {
//                    updatePeilvTxt(list, totalZhushu, selectPlayCode);
                }
                totalMoney = 0;//多选时总金额是主线程金额输入框中的金额*注数，在主线程计算
            } else {
                for (PeilvPlayData data : selectDatas) {
                    Integer zhushu = PeilvZhushuCalculator.buyZhuShuValidate(data.getNumber(),
                            data.getPeilvData().getMinSelected(), data.getPeilvData().getPlayCode());
                    Utils.LOG(TAG, "zhushu ==== " + zhushu);
                    totalZhushu += zhushu;
                    totalMoney += data.getMoney();
                }
            }
            Utils.LOG("a", "the total money = " + totalMoney);
            sendMessage(totalMoney, peilvWhenMultiSelect, totalZhushu, isMultiSelect, selectDatas);
        }

        /**
         * 发送数据到主线程
         *
         * @param totalMoney    单注总金额
         * @param peilv
         * @param totalZhushu
         * @param isMultiSelect
         */
        private void sendMessage(double totalMoney, float peilv, int totalZhushu, boolean isMultiSelect, List<PeilvPlayData> list) {
            CalcPeilvOrder peilvOrder = new CalcPeilvOrder();
            peilvOrder.setTotalMoney(totalMoney);
            peilvOrder.setPeilvWhenMultiselect(peilv);
            peilvOrder.setZhushu(totalZhushu);
            peilvOrder.setMultiSelect(isMultiSelect);
            peilvOrder.setSelectDatas(list);

            Message message = myHandler.obtainMessage(CALC_ZHUSHU_MONEY);
            message.obj = peilvOrder;
            message.sendToTarget();

        }

    }


    /**
     * 判断是否是选中的号码
     *
     * @param data
     * @return
     */
    private boolean isSelectedNumber(PeilvPlayData data) {
        if (data == null) {
            return false;
        }
//        if (data.isCheckbox() && data.isSelected()) {
//            return true;
//        }
//        return data.getMoney() > 0;
        return data.isSelected();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {

        RequestManager.getInstance().afterRequest(response);

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing() || response == null) {
                return;
            }
        }

        int action = response.action;
        if (action == LAST_RESULT_REQUEST) {
            CrazyResult<Object> result = response.result;
//            //隐藏彩种
//            ViewUtils.setVisibility(gv_numbers, false);
            if (result == null) {
                //更新开奖结果
                tv_last_qihao.setText("第?????期");

                if (lastResultAskTimer != null) {
                    lastResultAskTimer.cancel();
                    lastResultAskTimer = null;
                }
                return;
            }
            if (!result.crazySuccess) {
                //更新开奖结果
                tv_last_qihao.setText("第?????期");
                if (lastResultAskTimer != null) {
                    lastResultAskTimer.cancel();
                    lastResultAskTimer = null;
                }
                return;
            }
            Object regResult = result.result;
            LotteryDownBean reg = (LotteryDownBean) regResult;
            Utils.LOG(TAG, "update resu reg == " + new Gson().toJson(reg, LotteryDownBean.class));
            if (!reg.isSuccess()) {
                //更新开奖结果
                tv_last_qihao.setText("第?????期");
                if (lastResultAskTimer != null) {
                    lastResultAskTimer.cancel();
                    lastResultAskTimer = null;
                }
                return;
            }
            //更新开奖结果
            updateLastResult(reg.getLast());

        } else if (action == ACCOUNT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            MemInfoWraper reg = (MemInfoWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            //更新帐户名，余额等信息
            updateAccount(reg.getContent());
        } else if (action == COUNT_DOWN_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                currentQihao = "???????";
                if (endlineTouzhuTimer != null) {
                    endlineTouzhuTimer.cancel();
                    endlineTouzhuTimer = null;
                }
                tv_dead_time.setText("停止下注");
                return;
            }
            if (!result.crazySuccess) {
                currentQihao = "???????";
                if (endlineTouzhuTimer != null) {
                    endlineTouzhuTimer.cancel();
                    endlineTouzhuTimer = null;
                }
                tv_dead_time.setText(("停止下注"));
                return;
            }
            Object regResult = result.result;
            LocCountDownWraper reg = (LocCountDownWraper) regResult;
            if (!reg.isSuccess()) {
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(context);
                }
                currentQihao = "???????";
                if (endlineTouzhuTimer != null) {
                    endlineTouzhuTimer.cancel();
                    endlineTouzhuTimer = null;
                }
                tv_dead_time.setText(("停止下注"));
                return;
            }


            CountDown content = reg.getContent();
            if (content == null ||
                    TextUtils.isEmpty(content.getQiHao()) ||
                    content.getServerTime() == 0 && content.getActiveTime() == 0) {
                currentQihao = "???????";
                if (endlineTouzhuTimer != null) {
                    endlineTouzhuTimer.cancel();
                    endlineTouzhuTimer = null;
                }
                tv_dead_time.setText(String.format(getString(R.string.kaijian_deadline_format),
                        String.valueOf(currentQihao), "停止下注"));
                return;
            }

            YiboPreference.instance(context).setToken(reg.getAccessToken());
            //更新当前这期离结束投注的倒计时显示
            updateCurrenQihaoCountDown(reg.getContent());
        } else if (action == PLAY_RULES_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
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
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            updateXYFragment(reg.getContent());

        } else if (action == SYNC_LHC_TIME) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
//                showToast(R.string.dobets_fail);
                return;
            }
            if (!result.crazySuccess) {
//                showToast(R.string.dobets_fail);
                return;
            }
            Object regResult = result.result;
            LhcServerTimeWrapper reg = (LhcServerTimeWrapper) regResult;
            if (!reg.isSuccess()) {
//                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():getString(R.string.dobets_fail));
//                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
//                //所以此接口当code == 0时表示帐号被踢，或登录超时
//                if (reg.getCode() == 0) {
//                    UsualMethod.loginWhenSessionInvalid(this);
//                }
                return;
            }
            YiboPreference.instance(context).setToken(reg.getAccessToken());
//            showToast(R.string.dobets_success);
            xyFragment.setLhcServerTime(reg.getContent());
        } else if (action == ACQUIRE_PEILV_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
//                showToast(R.string.acquire_peilv_fail);
                return;
            }
            if (!result.crazySuccess) {
//                showToast(R.string.acquire_peilv_fail);
                return;
            }
            Object regResult = result.result;
            PeilvWebResultWraper reg = (PeilvWebResultWraper) regResult;
            if (!reg.isSuccess()) {
                if (TextUtils.isEmpty(reg.getMsg()) || reg.getMsg().equals(getString(R.string.no_peilv_data))) {
                    showToast("没有赔率数据，请联系客服");
                    YiboPreference.instance(context).setToken(reg.getAccessToken());
                    xyFragment.updatePlayArea();
                    return;
                }

                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_peilv_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(context);
                }
                return;
            }
            YiboPreference.instance(context).setToken(reg.getAccessToken());
            //更新赔率面板号码区域赔率等数据
            xyFragment.updatePlayArea(reg.getContent());

        } else if (action == POST_PEILV_BETS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                ToastUtils.showShort(R.string.dobets_fail);
                return;
            }
            if (!result.crazySuccess) {
                ToastUtils.showShort(R.string.dobets_fail);
                return;
            }
            Object regResult = result.result;
            DoBetForChatWraper reg = (DoBetForChatWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.dobets_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(context);
                }
                return;
            }

            ChatBetBean chatBetBean = reg.getContent();
            YiboPreference.instance(context).setToken(reg.getAccessToken());
            ToastUtils.showShort(R.string.dobets_success);
            accountWeb();
            //下注成功后保存这次下注的彩种，玩法等相关数据到内存
            saveCurrentLotData();
            Float totalAmount = selectedMoney;
            //下注成功后清除选择号码的记录
            onPlayClean(true);
            swtichFragment();
            if (TextUtils.isEmpty(ChatSpUtils.instance(context).getSendBetting()) || !ChatSpUtils.instance(context).getSendBetting().equals("1")) {
                // 禁止分享注单
//                EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK));
                myHandler.postDelayed(() -> EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK)), 750);
            } else {
                showAfterBetDialog(chatBetBean, list, "2", totalAmount);
            }
        }
    }

    private void sendBetInfo(ChatBetBean chatBetBean, List<OrderDataInfo> list, String version) {
        //发送信用注单消息
        List<ShareOrderBean> orders = new ArrayList<>();
        List<BetInfo> betInfos = new ArrayList<>();
        String orderId = "";
        String[] strings;
        if (chatBetBean.getOrders().contains(",")) {
            strings = chatBetBean.getOrders().split(",");
        } else {
            strings = null;
            orderId = chatBetBean.getOrders();
        }
        try {
            for (int i = 0; i < list.size(); i++) {
                OrderDataInfo orderDataInfo = list.get(i);
                ShareOrderBean orderBean = new ShareOrderBean();
                orderBean.setBetMoney(orderDataInfo.getMoney() + "");
                orderBean.setOrderId(strings == null ? orderId : strings[i]);
                orders.add(orderBean);
                BetInfo betInfo = new BetInfo();
                betInfo.setLottery_content(orderDataInfo.getNumbers());
                betInfo.setLottery_amount(orderDataInfo.getMoney() + "");
                betInfo.setLottery_play(orderDataInfo.getPlayName());

                betInfo.setModel(orderDataInfo.getMode());
                betInfo.setLottery_qihao(chatBetBean.getQihao());
                betInfo.setLottery_type(cpName);
                betInfo.setLottery_zhushu((orderDataInfo.getZhushu() == 0 ? 1 : orderDataInfo.getZhushu()) + "");
                betInfo.setLotCode(lotteryData.getCode());
                betInfo.setLotIcon(lotteryData.getImgUrl());
                betInfo.setVersion(version);
                betInfos.add(betInfo);
                if (betInfos.size() % 15 == 0) {
                    BetSlipMsgBody msgBody = new BetSlipMsgBody();
                    msgBody.setBetInfos(betInfos);
                    msgBody.setOrders(orders);
                    EventBus.getDefault().post(new CommonEvent(EventCons.XY_BET_MSG, msgBody));
                    betInfos = new ArrayList<>();
                    orders = new ArrayList<>();
                }
            }
            if (betInfos.isEmpty()) {
                EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK));
                return;
            }
            BetSlipMsgBody msgBody = new BetSlipMsgBody();
            msgBody.setBetInfos(betInfos);
            msgBody.setOrders(orders);
            EventBus.getDefault().post(new CommonEvent(EventCons.XY_BET_MSG, msgBody));
            EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void swtichFragment() {
        FragmentUtil.init(this);
        FragmentUtil.removeAndShow(orderComfirmFragment, isPeilv() ? xyFragment : gfFragment);
        orderComfirmFragment = null;
        bt_clear.setText("清除");
        bt_confirm.setText("确定");
    }


    public void showAfterBetDialog(ChatBetBean chatBetBean, List<OrderDataInfo> list, String version, Float totalAmount) {
        String auto_share_bet_amount = chatSysConfig.getName_auto_share_bet_arrive_amount();
        int setAmount = 0;
        try {
            setAmount = Integer.parseInt(auto_share_bet_amount);
        } catch (Exception e) {
            LogUtils.e("auto_share_bet_amount", e.getMessage());
        }
        if (setAmount != 0 && totalAmount >= setAmount) {
            new Handler().postDelayed(() -> sendBetInfo(chatBetBean, list, version), 500);
            return;
        }
        final CustomConfirmDialog ccd = new CustomConfirmDialog(context);
        ccd.setBtnNums(2);
        String content = "下注成功,是否分享至聊天室？";
        ccd.setContent(content);
        ccd.setTitle("温馨提示");
        ccd.setLeftBtnText("取消");
        ccd.setRightBtnText("发送");
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK));
                ccd.dismiss();
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                //发送信用注单消息
                List<ShareOrderBean> orders = new ArrayList<>();
                List<BetInfo> betInfos = new ArrayList<>();
                String orderId = "";
                String[] strings;
                if (chatBetBean.getOrders().contains(",")) {
                    strings = chatBetBean.getOrders().split(",");
                } else {
                    strings = null;
                    orderId = chatBetBean.getOrders();
                }
                try {
                    for (int i = 0; i < list.size(); i++) {
                        OrderDataInfo orderDataInfo = list.get(i);
                        ShareOrderBean orderBean = new ShareOrderBean();
                        orderBean.setBetMoney(orderDataInfo.getMoney() + "");
                        orderBean.setOrderId(strings == null ? orderId : strings[i]);
                        orders.add(orderBean);
                        BetInfo betInfo = new BetInfo();
                        betInfo.setLottery_content(orderDataInfo.getNumbers());
                        betInfo.setLottery_amount(orderDataInfo.getMoney() + "");
                        betInfo.setLottery_play(orderDataInfo.getPlayName());

                        betInfo.setModel(orderDataInfo.getMode());
                        betInfo.setLottery_qihao(chatBetBean.getQihao());
                        betInfo.setLottery_type(cpName);
                        betInfo.setLottery_zhushu((orderDataInfo.getZhushu() == 0 ? 1 : orderDataInfo.getZhushu()) + "");
                        betInfo.setLotCode(lotteryData.getCode());
                        betInfo.setLotIcon(lotteryData.getImgUrl());
                        betInfo.setVersion(version);
                        betInfos.add(betInfo);
                        if (betInfos.size() % 15 == 0) {
                            BetSlipMsgBody msgBody = new BetSlipMsgBody();
                            msgBody.setBetInfos(betInfos);
                            msgBody.setOrders(orders);
                            EventBus.getDefault().post(new CommonEvent(EventCons.XY_BET_MSG, msgBody));
                            betInfos = new ArrayList<>();
                            orders = new ArrayList<>();
                        }
                    }
                    if (betInfos.isEmpty()) {
                        EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK));
                        ccd.dismiss();
                        return;
                    }
                    BetSlipMsgBody msgBody = new BetSlipMsgBody();
                    msgBody.setBetInfos(betInfos);
                    msgBody.setOrders(orders);
                    EventBus.getDefault().post(new CommonEvent(EventCons.XY_BET_MSG, msgBody));
                    EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK));
                    ccd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        ccd.createDialog();
        ccd.getDialog().show();
    }


    public void saveCurrentLotData() {
        SavedGameData data = new SavedGameData();
        data.setGameModuleCode(SavedGameData.LOT_GAME_MODULE);
        data.setAddTime(System.currentTimeMillis());
        data.setLotName(cpName);
        data.setLotCode(cpBianHao);
        data.setLotType(cpTypeCode);
        data.setPlayName(selectPlayName);
        data.setPlayCode(selectPlayCode);
        data.setSubPlayName(selectSubPlayName);
        data.setDuration(cpDuration);
        data.setSubPlayCode(selectRuleCode);
        data.setCpVersion(cpVersion);
        UsualMethod.localeGameData(context, data);
    }


    private void actionRestore() {
        //恢复列表初始状态
        refreshPaneAndClean();
//        恢复输入框到初始状态
//        moneyTV.setText("");
        xyFragment.pageIndexWhenLargePeilvs = 1;
    }

    private void refreshPaneAndClean() {
        if (isPeilv()) {
            if (xyFragment.playAdapter != null) {
                xyFragment.playAdapter.notifyDataSetChanged();
            }
        } else {
            if (gfFragment.playAdapter != null) {
                gfFragment.playAdapter.notifyDataSetChanged();
            }
        }

    }


    /**
     * @param content
     */
    private void updateXYFragment(LotteryData content) {
        if (content != null) {
            String lottery = new Gson().toJson(content, LotteryData.class);
            if (!Utils.isEmptyString(lottery)) {
                LotteryData lotteryData = new Gson().fromJson(lottery, LotteryData.class);
                updateLotteryConstants(lotteryData);
                //初始化大小玩法显示栏
                if (playRules == null) {
                    return;
                }
                if (playRules.isEmpty()) {
                    showToast("没有玩法，请检查平台是否开启玩法");
                    return;
                }
                PlayItem defaultPlayItem = playRules.get(0);
                if (defaultPlayItem != null) {
                    selectPlayCode = defaultPlayItem.getCode();
                    selectPlayName = defaultPlayItem.getName();
                    List<SubPlayItem> rules = defaultPlayItem.getRules();
                    if (rules != null && !rules.isEmpty()) {
                        ApiParams params = new ApiParams();
                        params.put("playId", rules.get(0).getPalyId());
                        HttpUtil.get(context, Urls.GET_PLAY_INFO, params, true, result -> {
                            if (result.isSuccess()) {
                                BoundsBean boundsBean = new Gson().fromJson(result.getContent(), BoundsBean.class);
                                minBounds = (float) boundsBean.getData().getBonusOdds();
                            } else {
                                minBounds = rules.get(0).getMinBonusOdds();
                            }
                            maxBetNumber = rules.get(0).getMaxBetNum();
                            selectRuleCode = rules.get(0).getCode();
                            selectSubPlayName = rules.get(0).getName();
                            bounds = minBounds;
                            maxBounds = rules.get(0).getMaxBounsOdds();
                            minRakeback = rules.get(0).getMinRakeback();
                            winExample = rules.get(0).getWinExample();
                            detailDesc = rules.get(0).getDetailDesc();
                            playMethod = rules.get(0).getPlayMethod();

                            soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
                            music = soundPool.load(getContext(), R.raw.scroolsound, 1);

                            //小玩法
                            tv_lottery_type.setText(selectSubPlayName);

                            //如果大玩法下中有一项小玩法，说明点击的是大玩法。需要将小玩法名称用大玩法代替
                            String palyNameShowInView = selectSubPlayName;
                            if (rules.size() == 1) {
                                palyNameShowInView = selectPlayName;
                                playName = selectPlayName;
                            } else {
                                playName = selectPlayName + "-" + selectSubPlayName;
                            }

                            //设置小玩法
                            tv_lottery_type.setText(palyNameShowInView);

                            if (isPeilv()) {
                                xyFragment = new XYBettingFragment();
                                xyFragment.setCpBianHao(cpBianHao);
                                xyFragment.setPeilvListener(new TouzhuPeilvListener());
                                FragmentUtil.init(this);
                                FragmentUtil.addFragment(xyFragment, false, R.id.child_fragment);
                                getKaiJianResult(cpBianHao);
                                UsualMethod.getCountDownByCpcode(context, cpBianHao, COUNT_DOWN_REQUEST, BettingMainFragment.this);

                                String cpVersion = UsualMethod.isSixMark(context,content.getCode()) ?
                                        String.valueOf(Constant.lottery_identify_V2) : YiboPreference.instance(getActivity()).getGameVersion();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        xyFragment.onPlayRuleSelected(cpVersion,
                                                cpTypeCode, selectPlayCode, selectRuleCode,
                                                selectPlayName, selectSubPlayName, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                                    }
                                }, 200);

                            } else {
                                gfFragment = new GFBettingFragment();
//                    gfFragment.setBetListener(new TouzhuJianjinListener());
                                gfFragment.setCpBianHao(cpBianHao);
                                FragmentUtil.init(this);
                                FragmentUtil.addFragment(gfFragment, false, R.id.child_fragment);
                                getKaiJianResult(cpBianHao);//获取开奖结果
                                UsualMethod.getCountDownByCpcode(context, cpBianHao, COUNT_DOWN_REQUEST, BettingMainFragment.this);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        gfFragment.onPlayRuleSelected(/*YiboPreference.instance(TouzhuActivity.this)
                                    .getGameVersion()*/cpVersion, cpTypeCode, selectPlayCode, selectRuleCode,
                                                selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                                    }
                                }, 200);
//
                            }
                            refreshPlayRuleExpandListView();
                            updatePlayRuleExpandListView();
                            if (isPeilv()) {
                                startSyncLhcServerTime();
                            }
                        });


                    }
                }


            }
        }

    }


    public void onPlayTouzhu() {
        if (isFengPan) {
            ToastUtils.showShort(R.string.result_not_open_now);
            return;
        }

        if (selectNumList.isEmpty()) {
            ToastUtils.showShort((R.string.please_pick_numbers_first));
            return;
        }
        if (UsualMethod.isMulSelectMode(this.selectPlayCode)) {//多选状态下一定要判断快捷金额输入框是否有金额
            if (Utils.isEmptyString(moneyValue)) {
                ToastUtils.showShort(R.string.input_peilv_money);
                orderComfirmFragment.inputMoney.setFocusable(true);
                orderComfirmFragment.inputMoney.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return;
            }
        } else {
            boolean allNotMoney = true;
            StringBuilder sb = new StringBuilder();
            for (PeilvPlayData data : selectNumList) {
                if (data.getMoney() == 0) {
                    sb.append("\"").append(!Utils.isEmptyString(data.getItemName()) ? data.getItemName() + "-" : "")
                            .append(data.getNumber()).append("\",");
                } else {
                    allNotMoney &= false;
                }
            }
            if (allNotMoney) {
                ToastUtils.showShort(R.string.input_peilv_money);
                orderComfirmFragment.inputMoney.setFocusable(true);
                orderComfirmFragment.inputMoney.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return;
            }
            if (sb.length() > 0) {
                sb = sb.deleteCharAt(sb.length() - 1);
                showToast(sb.toString() + "号码未输入金额,请输入后再投注");
                return;
            }
        }
//        showConfirmBetDialog();
        TouzhuThreadPool.getInstance().addTask(new PeilvBetRunnable(selectNumList, false, false));
    }


    //更新帐户相关信息
    private void updateAccount(Meminfo meminfo) {
        if (meminfo == null) {
            return;
        }

        tv_balance.setVisibility(View.VISIBLE);
        if (!Utils.isEmptyString(meminfo.getBalance())) {
            String leftMoneyName = String.format("%.2f元", Double.parseDouble(meminfo.getBalance()));
            tv_balance.setText("余额：" + leftMoneyName);
        }
    }

    RuleSelectCallback ruleSelectCallback;

    public void setRuleSelectCallback(RuleSelectCallback ruleSelectCallback) {
        this.ruleSelectCallback = ruleSelectCallback;
    }

    private void refreshPlayRuleExpandListView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRuleSelectCallback(new RuleSelectListener());
//                if (isPeilv()) {
////                    .updatePlayRules(playRules);
//                } else {
//                    setRuleSelectCallback(new RuleSelectListener());
////                    jiangjinSimpleFragment.updatePlayRules(playRules);
//                }
            }
        }, 200);
    }


    /**
     * 同步六合彩服务器当前时间
     */
    public void startSyncLhcServerTime() {
        if (!UsualMethod.isXGSixMark(this.cpBianHao)) {
            return;
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.get_server_bettime_for_lhc);
        configUrl.append("?lotCode=").append(cpBianHao);
        CrazyRequest<CrazyResult<LhcServerTimeWrapper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(SYNC_LHC_TIME)
                .headers(Urls.getHeader(context))
                .cachePeroid(30 * 1000)
                .listener(this)
                .shouldCache(false).placeholderText(getString(R.string.get_peilv_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LhcServerTimeWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }


    private final class RuleSelectListener implements RuleSelectCallback {
        @Override
        public void onRuleCallback(PlayItem playItem, SubPlayItem item, long playId) {
//            if (playItem == null || item == null) {
//                return;
//            }
            //若此玩法在后台配置中处于关闭状态，则直接返回
            if (!playItem.isOpenStatus()) {
                ToastUtils.showShort(R.string.play_disable_from_web);
                return;
            }


            ApiParams params = new ApiParams();
            params.put("playId", playId);

            HttpUtil.get(context, Urls.GET_PLAY_INFO, params, true, result -> {
                if (result.isSuccess()) {
                    BoundsBean boundsBean = new Gson().fromJson(result.getContent(), BoundsBean.class);
                    minBounds = (float) boundsBean.getData().getBonusOdds();
                } else {
                    minBounds = item.getMinBonusOdds();
                }

                selectPlayCode = playItem.getCode();
                selectPlayName = playItem.getName();

                maxBetNumber = item.getMaxBetNum();
                selectRuleCode = item.getCode();
                selectSubPlayName = item.getName();
                bounds = minBounds;
                maxBounds = item.getMaxBounsOdds();
                minRakeback = item.getMinRakeback();
                winExample = item.getWinExample();
                detailDesc = item.getDetailDesc();
                playMethod = item.getPlayMethod();
                //更新玩法栏内容及状态

                //如果大玩法下中有一项小玩法，说明点击的是大玩法。需要将小玩法名称用大玩法代替
                String palyNameShowInView = selectSubPlayName;
                if (playItem.getRules().size() == 1) {
                    palyNameShowInView = selectPlayName;
                    playName = selectPlayName;
                } else {
                    playName = selectPlayName + "-" + selectSubPlayName;
                }


                //设置小玩法
                tv_lottery_type.setText(palyNameShowInView);
                lhcSelect = UsualMethod.isSixMark(context,cpBianHao);

                //重新刷新投注面板
                if (isPeilv()) {
                    startSyncLhcServerTime();

                    xyFragment.onPlayRuleSelected(lhcSelect ? String.valueOf(Constant.lottery_identify_V2) :
                                    cpVersion, cpTypeCode, selectPlayCode, selectRuleCode,
                            selectPlayName, palyNameShowInView, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                    FragmentUtil.hideAndShow(pcFragment, xyFragment);
                } else {
                    gfFragment.onPlayRuleSelected(cpVersion, cpTypeCode, selectPlayCode, selectRuleCode,
                            selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);

                    FragmentUtil.hideAndShow(pcFragment, gfFragment);
                }
            });


        }
    }


    /**
     * 更新当前期数倒计时时间
     *
     * @param countDown
     */
    private void updateCurrenQihaoCountDown(CountDown countDown) {
        if (countDown == null) {
            return;
        }
        //创建开奖周期倒计时器
        long serverTime = countDown.getServerTime();
        long activeTime = countDown.getActiveTime();
        currentQihao = countDown.getQiHao();
        long duration = Math.abs(activeTime - serverTime);
        if (endlineTouzhuTimer != null) {
            endlineTouzhuTimer.cancel();
            endlineTouzhuTimer = null;
        }

        if (UsualMethod.getConfigFromJson(context).getNative_fenpang_bet_switch().equalsIgnoreCase("on")) {
            createEndlineTouzhuTimer(duration, countDown.getQiHao());
            //开始离投注结束时间倒计时
            endlineTouzhuTimer.start();
        } else {
            Utils.LOG(TAG, "current qihao open duration = " + countDown.getQiHao() + "," + duration);
            if (duration > ago * 1000) {
                duration = duration - ago * 1000;

                createEndlineTouzhuTimer(duration, countDown.getQiHao());
                //开始离投注结束时间倒计时
                endlineTouzhuTimer.start();
            } else {
                if (!UsualMethod.getConfigFromJson(context).getNative_fenpang_bet_switch().equalsIgnoreCase("on")) {
                    //当前时间差小于开封盘时间时；说明处于封盘中，直接倒计时剩下的封盘时间即可
                    setFengPaningOperation((int) (duration / 1000));
                } else {
                    createEndlineTouzhuTimer(duration, countDown.getQiHao());
                    //开始离投注结束时间倒计时
                    endlineTouzhuTimer.start();
                }
            }
        }
    }

    private Disposable disposable;

    /**
     * @param duration 剩余封盘时间段，单位秒
     */
    public void setFengPaningOperation(final int duration) {
        //如果封盘之后无法下注
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(duration + 1)
                .map(aLong -> duration - aLong)
                //封盘过程中
                .doOnSubscribe(disposable -> {
                    //执行过程中为封盘状态
                    //执行过程中下一期为封盘状态
                    tv_qihao.setTextColor(Color.RED);
                    tv_qihao.setText(String.format(getString(R.string.kaijian_deadline_format_open_time),
                            String.valueOf(Long.parseLong(currentQihao) + 1)));

                    setFengPan(true);
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        //截止时间
                        tv_dead_time.setText(Utils.int2Time(aLong * 1000));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //执行结束之后为不封盘状态
                        setFengPan(false);

                        tv_qihao.setTextColor(Color.WHITE);
                        //封盘时间倒计时结束后，先请求一次开奖结果
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getKaiJianResult(cpBianHao);
                            }
                        }, 2000);

                        UsualMethod.getCountDownByCpcode(context, cpBianHao, COUNT_DOWN_REQUEST, BettingMainFragment.this);
                    }
                });
    }


    private void setFengPan(boolean isFengPan) {
        this.isFengPan = isFengPan;
        setButtonBg();
    }


    private void setButtonBg() {
        if (!isAdded() || getActivity() == null || getContext() == null) {
            disposable.dispose();
            return;
        }
        if (isFengPan) {
            bt_confirm.setBackground(getResources().getDrawable(R.drawable.shape_red_bg3_gray));
            bt_confirm.setEnabled(false);
        } else {
            bt_confirm.setBackground(getResources().getDrawable(R.drawable.shape_red_bg3));
            bt_confirm.setEnabled(true);
        }

    }

    /**
     * 创建离结束投注倒计时
     *
     * @param duration     离当前期号投注结束的时长，毫秒级
     * @param currentQihao 当前期号
     */
    private void createEndlineTouzhuTimer(final long duration, final String currentQihao) {
        endlineTouzhuTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                //第%1$s期截止时间:
                if (getActivity() != null && !getActivity().isFinishing() && getContext() != null) {
                    tv_qihao.setText(String.format(context.getString(R.string.kaijian_deadline_format_time),
                            String.valueOf(currentQihao)));
                    //截止时间
                    tv_dead_time.setText(Utils.int2Time(millisUntilFinished));
                }

            }

            public void onFinish() {
                if (isAdded()) {
                    tv_dead_time.setText(getString(R.string.stop_touzhu));
                }
                endlineTouzhuTimer = null;//置空当前期号倒计时器
                UsualMethod.getCountDownByCpcode(context, cpBianHao, COUNT_DOWN_REQUEST, BettingMainFragment.this);
                //截止下注后再过一段开封盘时间ago，获取一次开奖结果
                Utils.LOG(TAG, "ago ===== " + ago);
                //截止下注时先获取一次开奖结果
                getKaiJianResult(cpBianHao);
            }
        };
    }


    private String compareQihao = "";

    /**
     * 更新开奖结果
     *
     * @param lotteryLast
     */
    private void updateLastResult(LotteryLast lotteryLast) {
        if (lotteryLast == null) {
            return;
        }
//        Utils.LOG(TAG, "update result == " + lotteryLast.getHaoMa());
        //先判断开奖结果是否与上一期的相同,如果相同则轮询获取
        boolean sameResultToLast = handleAcqureLastResult(lotteryLast.getHaoMa());
        if (Utils.isEmptyString(lotteryLast.getQiHao()) || Utils.isEmptyString(lotteryLast.getHaoMa())) {
            return;
        }
        if (!lotteryLast.getHaoMa().contains(",")) {
            return;
        }

        List<String> haomas1 = Utils.splitString(lotteryLast.getHaoMa(), ",");
        for (String hm : haomas1) {
            if (!Utils.isNumeric(hm) || hm.equalsIgnoreCase("?")) {
                tv_last_qihao.setText("开奖中...");
                return;
            }
        }
        //获取到正常的开奖号码后将定时获取开奖号码定时器取消
        if (!sameResultToLast) {
            Utils.LOG(TAG, "update resu cancel timer of ask result when get new haoma");
            if (lastResultAskTimer != null) {
                lastResultAskTimer.cancel();
                lastResultAskTimer = null;
            }
        }
        compareQihao = lotteryLast.getQiHao();
        String newQiHao = lotteryLast.getQiHao().substring(lotteryLast.getQiHao().length() - 4);
        String old = tv_last_qihao.getText().toString();
        if (isAdded()) {
            tv_last_qihao.setText(String.format(getString(R.string.qihao_string3), newQiHao));
        }
        if (!Utils.isEmptyString(lotteryLast.getHaoMa()) && lotteryLast.getHaoMa().contains(",")) {
            Utils.LOG(TAG, "ago THE open haoma === " + lotteryLast.getHaoMa() + ",bianhao = " + cpBianHao);
            String[] split = lotteryLast.getHaoMa().split(",");
            int first = 0;
            int second = 0;
            int third = 0;
            int total = 0;

            if (split.length >= 3) {
                first = Integer.parseInt(split[0]);
                second = Integer.parseInt(split[1]);
                third = Integer.parseInt(split[2]);
                total = first + second + third;
            }
            //快三增加大小单双
            if (cpTypeCode.equals("10") || cpTypeCode.equals("100") || cpTypeCode.equals("58")) {
                boolean isOff = UsualMethod.getConfigFromJson(context).getK3_baozi_daXiaoDanShuang().equals("off");

                if (isOff && first == second && first == third) {
                    //开关关闭 三个相同显示豹子
                    lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",豹,豹");
                } else {
                    if (total >= 11) {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",大");
                    } else {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",小");
                    }
                    if (total % 2 == 0) {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",双");
                    } else {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",单");
                    }
                }
                //添加总和
                lotteryLast.setHaoMa(lotteryLast.getHaoMa() + "," + total);

            }
            //PC蛋蛋增加总和数
            else if (cpTypeCode.equalsIgnoreCase("11") || cpTypeCode.equalsIgnoreCase("7") || cpTypeCode.equals("57")) {
                lotteryLast.setHaoMa(first + ",+," + second + ",+," + third + ",=," + total);
            }


            List<String> haomas = Utils.splitString(lotteryLast.getHaoMa(), ",");
            gv_numbers.setVisibility(View.VISIBLE);

            String qihao = "";
            if (isAdded()) {
                qihao = getString(R.string.qihao_string3);
            }

            if (!old.equals(String.format(qihao, newQiHao))) {
                Utils.LOG(TAG, "ago update result with animation----");

                updateNumberGridView(haomas, cpTypeCode);
            } else {
                if (Firstinto) {
                    updateNumberGridView(haomas, cpTypeCode);
                }

            }
        } else {
            gv_numbers.setVisibility(View.GONE);
        }
        //更新完开奖结果后，延迟5秒获取一下帐户余额
        new Handler().postDelayed(() -> accountWeb(), 5000);
    }


    /**
     * 根据开奖结果显示号码view
     *
     * @param haoMaList
     * @param lotType
     */
    private void updateNumberGridView(List<String> haoMaList, String lotType) {
        if (haoMaList == null || haoMaList.isEmpty()) {
            return;
        }

        if (cpTypeCode.equals("6") || cpTypeCode.equals("66")) {
            getOpenResultDetails(haoMaList, lotType);
        } else {
            updateGridView(haoMaList, lotType, null);
        }


    }


    private void getOpenResultDetails(final List<String> haoMaList, final String lotType) {
        ApiParams params = new ApiParams();
        params.put("lotCode", cpBianHao);
        params.put("page", 1);
        params.put("row", 5);
        HttpUtil.get(context, Urls.OPEN_RESULT_DETAIL_URL, params, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {

                    List<OpenResultDetail> kjList = new Gson().fromJson(result.getContent(), new TypeToken<List<OpenResultDetail>>() {
                    }.getType());

                    String kj = compareQihao;
                    String date;
                    if (kjList.get(0).getQiHao().equals(kj)) {
                        date = kjList.get(0).getDate();
                    } else if (kjList.get(1).getQiHao().equals(kj)) {
                        date = kjList.get(1).getDate();
                    } else if (kjList.get(2).getQiHao().equals(kj)) {
                        date = kjList.get(2).getDate();
                    } else if (kjList.get(3).getQiHao().equals(kj)) {
                        date = kjList.get(3).getDate();
                    } else if (kjList.get(4).getQiHao().equals(kj)) {
                        date = kjList.get(4).getDate();
                    } else {
                        date = "";
                    }
                    updateGridView(haoMaList, lotType, date);
                }
            }
        });
    }


    /**
     * 上一期的开奖号码
     *
     * @param currentHaoMa 返回当前开奖结果是否与上一期的相同
     */
    private boolean handleAcqureLastResult(String currentHaoMa) {

        if ((!Utils.isEmptyString(currentHaoMa) && currentHaoMa.equalsIgnoreCase(lastOpenResult)) ||
                currentHaoMa.contains("?")) {
            //开始定时请求第前期的开奖结果
            if (lastResultAskTimer != null) {
                lastResultAskTimer.cancel();
                lastResultAskTimer = null;
            }
            int ago_offset = 4;
            Utils.LOG(TAG, "ask open result timely..........");
            createLastResultTimer((ago_offset) * 1000);
            lastResultAskTimer.start();
            return true;
        }

        lastOpenResult = currentHaoMa;
        return false;
    }


    /**
     * 创建查询开奖结果倒计时
     *
     * @param duration
     */
    private void createLastResultTimer(final long duration) {
        lastResultAskTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Utils.LOG(TAG, "开始获取上一期开奖结果-----------");
                getKaiJianResult(cpBianHao);
                //当前期数投注时间到时，继续请求同步服务器上下一期号及离投注结束倒计时时间
//                showToast(R.string.sync_last_resulting);
                if (lastResultAskTimer != null) {
                    lastResultAskTimer.cancel();
                    lastResultAskTimer = null;
                }
                Utils.LOG(TAG, "ago aaaa");
                createLastResultTimer(duration);
                lastResultAskTimer.start();
            }
        };
    }

    public void accountWeb() {
        StringBuilder accountUrls = new StringBuilder();
        accountUrls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MEMINFO_URL);
        CrazyRequest<CrazyResult<MemInfoWraper>> accountRequest = new AbstractCrazyRequest.Builder().
                url(accountUrls.toString())
                .seqnumber(ACCOUNT_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(context))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MemInfoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, accountRequest);
    }


    public int selectedBeishu = 1;//选择的倍数

    public void updateBottomWithBeishChange(int beishu) {
        this.selectedBeishu = beishu;
        float money = adjustMoney(beishu, calcOutZhushu, selectModeIndex);
        this.selectedMoney = money;
        onBottomUpdate(calcOutZhushu, selectedMoney);
    }


    /**
     * 调整总金额
     *
     * @param beishu          倍数
     * @param zhushu          注数
     * @param selectModeIndex 模式索引
     */
    private float adjustMoney(int beishu, int zhushu, int selectModeIndex) {
        float money = BASIC_MONEY;
        money = money * beishu * zhushu;
        switch (selectModeIndex) {
            case 0:
                break;
            case 1:
                money = money / 10;
                break;
            case 2:
                money = money / 100;
                break;
        }
        return money;
    }
}