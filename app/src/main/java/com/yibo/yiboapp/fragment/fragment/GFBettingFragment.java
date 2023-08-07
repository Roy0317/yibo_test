package com.yibo.yiboapp.fragment.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.fragment.base.ChatBaseFragment;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.PlayPaneSimpleAdapter;
import com.yibo.yiboapp.adapter.PlayRuleSimpleExpandAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.JieBaoZhuShuCalculator;
import com.yibo.yiboapp.data.LotteryAlgorithm;
import com.yibo.yiboapp.data.LotteryPlayLogic;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.BallonItem;
import com.yibo.yiboapp.entify.BallonRules;

import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.fragment.JiangjinSimpleFragment;
import com.yibo.yiboapp.interfaces.PlayItemSelectListener;
import com.yibo.yiboapp.interfaces.RuleSelectCallback;
import com.yibo.yiboapp.ui.AdjustZhuDangPopWindow;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:ray
 * Date  :12/08/2019
 * Desc  :com.yibo.yiboapp.ui.bet.fragment
 */
public class GFBettingFragment extends ChatBaseFragment implements PlayItemSelectListener {


    private static final String TAG = "GFBettingFragment";


    public static final int START_SHAKE = 0x1;
    public static final int AGAIN_SHAKE = 0x2;
    public static final int END_SHAKE = 0x3;
    public static final int HANDLE_TOUZHU = 0x5;
    public static final int BASIC_MONEY = 2;

    private TouzhuHandler ayncHandler;

    private ListView playListview;
    public PlayPaneSimpleAdapter playAdapter;
    private List<BallonRules> ballonDatas = new ArrayList<>();
    private int buttonAudioID;
    private SoundPool buttonPool;
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;
    private boolean isShake = false;
    private AdjustZhuDangPopWindow adjustWindow;//点击底部设置按钮时的注单调整窗
    private String selectPlayCode = "";
    private String selectRuleCode = "";
    private String selectPlayName = "";
    private String selectSubPlayName = "";
    private String currentQihao = "";
    private int selectModeIndex = 0;//金额模式，元模式
    private int selectedBeishu = 1;//选择的倍数
    private float selectedMoney;//总投注金额,单位元
    private float selectedMinBounds = 0;//最小奖金
    private float selectedMinRateBack;//最小返利率
    private String selectedNumbers;//选择好的投注号码
    private int calcOutZhushu;//计算出来的注数
    private JiangjinSimpleFragment.BetListener betListener;
    private String cpCode = "";//彩票类型代号
    private String cpBianHao;//彩票编号
    private String cpVersion = String.valueOf(Constant.lottery_identify_V1);
    private int maxBetNum;
    private String cpName = "";
    private long cpDuration;
    private float minRate;
    private TextView playRuleTV;
    private ExpandableListView playExpandListView;
    private Button modeBtn;
    private XEditText beishuInput;
    //    private XEditText beishuInput;
    private Button clear_jixuan;
    private TextView zhushuTV;
    private TextView moneyTV;
    private PlayRuleSimpleExpandAdapter ruleExpandAdapter;
    private List<PlayItem> playRules;
    private RuleSelectCallback ruleSelectCallback;
    private int mCurrentPosition = -1;//开关标志
    private int currentCount = 1;//当前的倍数
    private TextView tv_bets_count;
    private boolean isPlaySound;

    public static final int MAX_BEISHU_LENGTH = 7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gf_betting, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        playListview = (ListView) view.findViewById(R.id.xlistview);
        playListview.setDivider(getResources().getDrawable(R.color.driver_line_color));
        playListview.setDividerHeight(3);

        ayncHandler = new TouzhuHandler(this);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        int screenWidth = dm.widthPixels;
        //初始化并绑定投注数据到投注面板listview
        playAdapter = new PlayPaneSimpleAdapter(getActivity(), ballonDatas, R.layout.play_pane_item_simple_chat);
        playAdapter.setScreenWidth(screenWidth);
        boolean isExpanded = true;
        playAdapter.setPlayBarStatus(isExpanded);
        playAdapter.setPlayItemSelectListener(this);
        playListview.setAdapter(playAdapter);

        playListview.setAdapter(playAdapter);
        isPlaySound = YiboPreference.instance(getActivity()).isButtonSoundAllow();

    }

    @Override
    protected void loadData() {
        super.loadData();

    }


    public void setCpBianHao(String cpBianHao) {
        this.cpBianHao = cpBianHao;
    }


    public void onPlayRuleSelected(String cpVersion, String cpCode,
                                   String playCode, String subPlayCode,
                                   String playName, String subPlayName,
                                   float minBounds, float minRate, String currentQihao,
                                   String cpName, long cpDuration, int maxBetNum) {
        this.cpVersion = cpVersion;
        this.cpCode = cpCode;
        this.selectPlayCode = playCode;
        this.selectRuleCode = subPlayCode;
        this.selectPlayName = playName;
        this.selectSubPlayName = subPlayName;
//        this.selectedMode = selectedMode;
        selectedMinBounds = minBounds;
        selectedMinRateBack = minRate;
        this.currentQihao = currentQihao;
        this.cpName = cpName;
        this.minRate = minBounds;
        this.cpDuration = cpDuration;
        this.maxBetNum = maxBetNum;

        clearZhushuBottonView();
//        updateBottomClearBtn();

        Utils.LOG(TAG, "the cp duration = " + cpDuration);


        //更新投注球面板列表数据
        List<BallonRules> ballons = calcPaneMessageByPlayRule(cpVersion,
                cpCode, selectPlayCode, selectRuleCode);
        if (ballons != null) {
            ballonDatas.clear();
            ballonDatas.addAll(ballons);
        }
        refreshPaneAndClean();
    }


    /**
     * 根据大小玩法来确定投注面板显示多少行，多少个投注号码，是否显示位数控件，是否显示选号功能便捷控件，
     * 显示的玩法标签文字 等等
     *
     * @param cpCode   彩票代号
     * @param playCode 大玩法code
     * @param ruleCode 小玩法code
     * @return
     */
    private List<BallonRules> calcPaneMessageByPlayRule(String cpVersion, String cpCode,
                                                        String playCode, String ruleCode) {
        if (Utils.isEmptyString(playCode) || Utils.isEmptyString(ruleCode)) {
            return null;
        }
        List<BallonRules> ballonRules = LotteryAlgorithm.figureOutPlayInfo(cpVersion,
                cpCode, playCode, ruleCode);
        if (ballonRules == null) {
            return null;
        }
        for (BallonRules br : ballonRules) {
            List<BallListItemInfo> ballListItemInfos = LotteryAlgorithm.convertNumListToEntifyList(
                    br.getNums(), br.getPostNums());
            br.setBallonsInfo(ballListItemInfos);
        }
        return ballonRules;
    }


    public void clearZhushuBottonView() {
        calcOutZhushu = 0;
        selectedBeishu = 1;//选择的倍数
        selectModeIndex = 0;
        selectedMoney = 0;//总投注金额
        selectedNumbers = "";//选择好的投注号码
        currentCount = 1;
    }



    private void refreshPaneAndClean() {
        if (playAdapter != null) {
            playAdapter.notifyDataSetChanged();
            ((BettingMainFragment) getParentFragment()).onClearView();
            clearZhushuBottonView();
        }
    }


    @Override
    public void onBallonClick(BallonItem item) {

    }

    private boolean isFengPan = false;

    public void setFengPan(boolean fengPan) {
        isFengPan = fengPan;
//        if (isFengPan) {
//            if (isAdded()) {
////                confirmBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_press_unenable));
//            }
//        } else {
//            if (isAdded()) {
////                confirmBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_selector));
//            }
//        }
    }

    @Override
    public void onViewClick(View view) {

        if (isFengPan) {
            ToastUtils.showShort(R.string.result_not_open_now);
            return;
        }
        //播放按键音
        if (isPlaySound) {
            if (buttonPool == null || buttonAudioID == 0) {
                initKeySound();
            }
            buttonPool.play(buttonAudioID, 1, 1, 0, 0, 1);
        }
        //选择球的时候清除本地已选择的金额，倍数，注数等
        clearZhushuBottonView();

        //开始计算投注号码串及注数
        responseTouzhu(getActivity(), ballonDatas, cpVersion, cpCode,
                selectPlayCode, selectRuleCode, false);
//        updateBallView(listPos,ballPos);
    }


    private void initKeySound() {
        if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
            //初始化SoundPool
            buttonPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
            buttonAudioID = buttonPool.load(getActivity(), R.raw.bet_select, 1);
        }
    }


    /**
     * 投注动作
     *
     * @param selectedDatas 非机选投注时，用户已经选择完投注球的球列表数据
     * @param cpVersion     彩票版本
     * @param czCode        彩种代号
     * @param playCode      大玩法
     * @param subCode       小玩法
     * @param isRandom      是否机选
     */
    private void responseTouzhu(Context context, List<BallonRules> selectedDatas, String cpVersion,
                                String czCode, String playCode,
                                String subCode, boolean isRandom) {
        TouzhuThreadPool.getInstance().addTask(new TouzhuThread(context,
                selectedDatas, cpVersion, czCode, playCode, subCode, isRandom));
    }


    //计算注数的线程
    private final class TouzhuThread implements Runnable {

        WeakReference<Context> wc;
        List<BallonRules> ballons;
        String cpVersion;
        String czCode;
        String playCode;
        String subCode;
        boolean isRandom;

        TouzhuThread(Context context, List<BallonRules> ballons, String cpVersion, String czCode,
                     String playCode, String subCode, boolean isRandom) {

            wc = new WeakReference<>(context);
            this.ballons = ballons;
            this.cpVersion = cpVersion;
            this.czCode = czCode;
            this.playCode = playCode;
            this.subCode = subCode;
            this.isRandom = isRandom;
        }

        @Override
        public void run() {

            if (wc.get() == null) {
                return;
            }
            if (isRandom) {
                List<BallonRules> ballonsAfterRandomSelect = LotteryPlayLogic.
                        selectRandomDatas(cpVersion, czCode, playCode, subCode);
                if (ballonsAfterRandomSelect != null && !ballonsAfterRandomSelect.isEmpty()) {
                    if (ballons != null) {
                        ballons.clear();
                        ballons.addAll(ballonsAfterRandomSelect);
                    }
                }
            }

            String numbers = LotteryPlayLogic.figureOutNumbersAfterUserSelected(ballons,
                    playCode, subCode);
            Utils.LOG(TAG, "the tou zhu post numbers = " + numbers);
            if (Utils.isEmptyString(numbers)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BettingMainFragment) getParentFragment()).onBottomUpdate(0, 0);
                    }
                });
             return;
            }
            int zhushu = JieBaoZhuShuCalculator.calc(Integer.parseInt(czCode), subCode, numbers);
            if (zhushu == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BettingMainFragment) getParentFragment()).onBottomUpdate(0, 0);
                    }
                });
                return;
            }
            Utils.LOG(TAG, "the tou zhu zhu dang shu = " + zhushu);
            Message message = ayncHandler.obtainMessage(HANDLE_TOUZHU, zhushu, isRandom ? 1 : 0, numbers);
            ayncHandler.sendMessage(message);
        }
    }


    //线程异步handler
    private static class TouzhuHandler extends Handler {
        private WeakReference<GFBettingFragment> mReference;
        private GFBettingFragment mActivity;

        public TouzhuHandler(GFBettingFragment activity) {
            mReference = new WeakReference<>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity == null) {
                return;
            }
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    mActivity.mVibrator.vibrate(300);
                    //发出提示音
//                    mActivity.mSoundPool.play(mActivity.mTouzhuAudioID, 1, 1, 0, 0, 1);
                    break;
                case AGAIN_SHAKE:
                    mActivity.mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
                    mActivity.isShake = false;
//                    mActivity.actionCleanSelectBllons();
//                    mActivity.playPane.setAdapter(mActivity.playPaneAdapter);
                    //开始随机投注
                    mActivity.responseTouzhu(mActivity.getActivity(), mActivity.ballonDatas,
                            mActivity.cpVersion, mActivity.cpCode, mActivity.selectPlayCode,
                            mActivity.selectRuleCode, true);
                    break;
                case HANDLE_TOUZHU:
                    int zhushu = msg.arg1;
                    String numbers = (String) msg.obj;
                    int isRandom = msg.arg2;
                    if (isRandom == 1) {
                        mActivity.refreshPaneAndClean();
                    }
                    Utils.LOG(TAG, "the select numbers = " + numbers);
                    mActivity.selectedNumbers = numbers;
                    Utils.LOG(TAG, "the calc out zhushu = " + zhushu);
                    mActivity.calcOutZhushu = zhushu;
                    float beilv = 1;
                    switch (mActivity.selectModeIndex) {
                        case 0:
                            beilv = 1;
                            break;
                        case 1:
                            beilv = 0.1f;
                            break;
                        case 2:
                            beilv = 0.01f;
                            break;
                    }
                    mActivity.selectedMoney = mActivity.calcOutZhushu * 2 * beilv;
                    BettingMainFragment parentFragment = (BettingMainFragment) mActivity.getParentFragment();
                    if (parentFragment != null) {
                        parentFragment.selectedMoney = mActivity.selectedMoney;
                        parentFragment.selectedBeishu = mActivity.selectedBeishu;
                        parentFragment.setSelectModeIndex(mActivity.selectModeIndex);
                        parentFragment.setSelectedNumbers(numbers);
                        parentFragment.onBottomUpdate(mActivity.calcOutZhushu,
                                mActivity.selectedMoney);
                    }
                    break;
            }
        }
    }






    public void actionCleanSelectBllons() {
        List<BallonRules> results = new ArrayList<BallonRules>();
        for (BallonRules info : ballonDatas) {
            if (info.isShowWeiShuView()) {
                List<BallListItemInfo> weishuInfo = info.getWeishuInfo();
                List<BallListItemInfo> data = new ArrayList<BallListItemInfo>();
                for (BallListItemInfo bi : weishuInfo) {
                    bi.setSelected(false);
                    data.add(bi);
                }
                weishuInfo.clear();
                weishuInfo.addAll(data);
                info.setWeishuInfo(weishuInfo);
            }
            List<BallListItemInfo> ballonsInfo = info.getBallonsInfo();
            List<BallListItemInfo> data = new ArrayList<BallListItemInfo>();
            for (BallListItemInfo ballon : ballonsInfo) {
                ballon.setSelected(false);
                data.add(ballon);
            }
            ballonsInfo.clear();
            ballonsInfo.addAll(data);
            info.setBallonsInfo(ballonsInfo);
            results.add(info);
        }
        ballonDatas.clear();
        ballonDatas.addAll(results);
    }


    public void onPlayClean(boolean clearAfterBetSuccess) {
        if (ballonDatas.isEmpty()) {
            ToastUtils.showShort(R.string.please_pick_numbers_first);
            return;
        }
        boolean needClear = false;
        for (BallonRules info : ballonDatas) {
            if (info.isShowWeiShuView()) {
                List<BallListItemInfo> weishuInfo = info.getWeishuInfo();
                for (BallListItemInfo bi : weishuInfo) {
                    if (bi.isSelected()) {
                        needClear = true;
                        break;
                    }
                }
            }
            List<BallListItemInfo> ballonsInfo = info.getBallonsInfo();
            for (BallListItemInfo ballon : ballonsInfo) {
                if (ballon.isSelected()) {
                    needClear = true;
                    break;
                }
            }
            if (needClear) {
                break;
            }
        }
        if (!needClear) {
            ToastUtils.showShort(R.string.please_pick_numbers_first);
            return;
        }
        actionCleanSelectBllons();
        refreshPaneAndClean();
    }

}