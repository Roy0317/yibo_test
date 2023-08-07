package com.yibo.yiboapp.fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.TouzhuOrderActivity;
import com.yibo.yiboapp.activity.TouzhuSimpleActivity;
import com.yibo.yiboapp.adapter.PlayPaneSimpleAdapter;
import com.yibo.yiboapp.adapter.PlayRuleSimpleExpandAdapter;
import com.yibo.yiboapp.data.ChannelListener;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.JieBaoZhuShuCalculator;
import com.yibo.yiboapp.data.LotteryAlgorithm;
import com.yibo.yiboapp.data.LotteryPlayLogic;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.BallonItem;
import com.yibo.yiboapp.entify.BallonRules;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.interfaces.PlayItemSelectListener;
import com.yibo.yiboapp.interfaces.RuleSelectCallback;
import com.yibo.yiboapp.ui.AdjustZhuDangPopWindow;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * 奖金投注面板fragment
 */
public class JiangjinSimpleFragment extends Fragment implements PlayItemSelectListener,
        SensorEventListener, ChannelListener {

    public static final String TAG = JiangjinSimpleFragment.class.getSimpleName();

    public static final int START_SHAKE = 0x1;
    public static final int AGAIN_SHAKE = 0x2;
    public static final int END_SHAKE = 0x3;
    public static final int HANDLE_TOUZHU = 0x5;
    public static final int BASIC_MONEY = 2;

    private ListView playListview;
    private PlayPaneSimpleAdapter playAdapter;
    private List<BallonRules> ballonDatas = new ArrayList<>();
    private int buttonAudioID;
    private SoundPool buttonPool;
    private TouzhuHandler ayncHandler;
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
    public int selectModeIndex = 0;//金额模式，元模式
    private int selectedBeishu = 1;//选择的倍数
    private float selectedMoney;//总投注金额,单位元
    private float selectedMinBounds = 0;//最小奖金
    private float bounds = 0;//奖金
    private float selectedMinRateBack;//最小返利率
    private String selectedNumbers;//选择好的投注号码
    private int calcOutZhushu;//计算出来的注数
    private BetListener betListener;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jiangjin_simple_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initKeySound();
        ayncHandler = new TouzhuHandler(this);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        int screenWidth = dm.widthPixels;
        List<BallonRules> datas = calcPaneMessageByPlayRule(cpVersion, cpCode, selectPlayCode, selectRuleCode);
        if (datas != null) {
            ballonDatas.clear();
            ballonDatas.addAll(datas);
        }
        //初始化并绑定投注数据到投注面板listview
        playAdapter = new PlayPaneSimpleAdapter(getActivity(), ballonDatas, R.layout.play_pane_item_simple);
        playAdapter.setScreenWidth(screenWidth);
        boolean isExpanded = true;
        playAdapter.setPlayBarStatus(isExpanded);
        playAdapter.setPlayItemSelectListener(this);
        playListview.setAdapter(playAdapter);
        playExpandListView.setGroupIndicator(null);
        isPlaySound = YiboPreference.instance(getActivity()).isButtonSoundAllow();
        playExpandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //点击group时，如果子项只有一项，则回调子项的玩法编码
                if (playRules == null) {
                    return false;
                }
                if (ruleSelectCallback == null) {
                    return false;
                }
                if (playRules.get(groupPosition) == null) {
                    return false;
                }

                if (mCurrentPosition == -1) {// 一个没有打开
                    playExpandListView.expandGroup(groupPosition, true);
                    mCurrentPosition = groupPosition;

                } else {// 至少有一个打开
                    // 判断一下是否点击自己
                    if (mCurrentPosition == groupPosition) {
                        playExpandListView.collapseGroup(mCurrentPosition);
                        mCurrentPosition = -1;
                        return true;
                    }
                    // 关闭上一个
                    playExpandListView.collapseGroup(mCurrentPosition);
                    playExpandListView.expandGroup(groupPosition, true);
                    // 更新position
                    mCurrentPosition = groupPosition;
                }

                if (playRules.get(groupPosition).getRules().size() == 1) {
                    setAllUnActivated();
                }
                PlayItem playItem = playRules.get(groupPosition);
                playItem.setActivated(true);
                playRules.set(groupPosition, playItem);
                ruleExpandAdapter.setData(playRules);


                List<SubPlayItem> rules = playRules.get(groupPosition).getRules();
                if (rules != null && !rules.isEmpty()) {
                    if (rules.size() == 1) {
                        ruleSelectCallback.onRuleCallback(playRules.get(groupPosition), rules.get(0),rules.get(0).getPalyId());
                    }
                }

                return true;
            }
        });
        playExpandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (playRules == null) {
                    return false;
                }
                if (ruleSelectCallback == null) {
                    return false;
                }
                if (playRules.get(groupPosition) == null) {
                    return false;
                }


                setAllUnActivated();
                SubPlayItem subPlayItem = playRules.get(groupPosition).getRules().get(childPosition);
                subPlayItem.setActivated(true);
                playRules.get(groupPosition).getRules().set(childPosition, subPlayItem);
                ruleExpandAdapter.setData(playRules);


                List<SubPlayItem> rules = playRules.get(groupPosition).getRules();
                if (rules != null && !rules.isEmpty()) {
                    ruleSelectCallback.onRuleCallback(playRules.get(groupPosition), rules.get(childPosition),rules.get(childPosition).getPalyId());
                }
                return false;
            }
        });

        playRules = new ArrayList<>();
        ruleExpandAdapter = new PlayRuleSimpleExpandAdapter(getActivity(), playRules);
        playExpandListView.setAdapter(ruleExpandAdapter);

    }


    public void setAllUnActivated() {
        for (PlayItem playRule : playRules) {
            playRule.setActivated(false);
            for (SubPlayItem subPlayItem : playRule.getRules()) {
                subPlayItem.setActivated(false);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ballonDatas.clear();
    }

    public void setCpBianHao(String cpBianHao) {
        this.cpBianHao = cpBianHao;
    }

    public void setRuleSelectCallback(RuleSelectCallback ruleSelectCallback) {
        this.ruleSelectCallback = ruleSelectCallback;
    }

    public void updateBottom(int zhushu, double totalMoney, float minRate) {

        minRate = adjustMinBounds(bounds,selectModeIndex);
        if (zhushuTV == null || moneyTV == null) {
            return;
        }
        zhushuTV.setText(String.format(Locale.CHINA, "共选中%d注", zhushu));
//        float rate = minRate - totalMoney;
        moneyTV.setText(String.format(Locale.CHINA, "共%.2f元，奖金%.3f元", totalMoney, minRate));
//        moneyTV.setText("共"+totalMoney+"元，奖金"+minRate+"元");
    }

    public void updatePlayRules(List<PlayItem> items) {
        if (items != null) {
            playRules.clear();
            playRules.addAll(items);
        }

        if (playRules.get(0).getRules().size() <= 1) {
            playRules.get(0).setActivated(true);
        } else {
            playRules.get(0).getRules().get(0).setActivated(true);
            playExpandListView.expandGroup(0, true);
            mCurrentPosition = 0;
        }

        ruleExpandAdapter.setData(playRules);
    }

    @Override
    public void onStart() {
        super.onStart();
        handleVibrateAndSensor();
        updateCartCount();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        //刷新投注面板
//        actionCleanSelectBllons();
        super.onPause();
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

    private void initKeySound() {
        if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
            //初始化SoundPool
            buttonPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
            buttonAudioID = buttonPool.load(getActivity(), R.raw.bet_select, 1);
        }
    }

    Button confirmBtn;

    @SuppressLint("ClickableViewAccessibility")
    private void initView(final View view) {
        playListview = (ListView) view.findViewById(R.id.xlistview);
        playListview.setDivider(getResources().getDrawable(R.color.driver_line_color));
        playListview.setDividerHeight(3);
        playExpandListView = (ExpandableListView) view.findViewById(R.id.expand_list);
        playRuleTV = (TextView) view.findViewById(R.id.play_rule);
//        tv_bets_count = (TextView) view.findViewById(R.id.tv_bets_count);
        modeBtn = (Button) view.findViewById(R.id.mode_btn);
        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseModeList();
            }
        });
        view.findViewById(R.id.ll_ib_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseModeList();
            }
        });

        view.findViewById(R.id.fl_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCount++;
                changeBetText();
            }
        });
        view.findViewById(R.id.fl_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCount <= 1) {
                    currentCount = 1;
                    return;
                } else {
                    currentCount--;
                }
                changeBetText();
            }
        });


        beishuInput = (XEditText) view.findViewById(R.id.beishu_input);
        beishuInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                beishuInput.setCursorVisible(true);
                return false;
            }
        });
        beishuInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String beishu = s.toString();
                if (beishu.startsWith("0")) {
                    Toast.makeText(getActivity(), "倍数不可以为0", Toast.LENGTH_SHORT).show();
                    beishuInput.setText("1");
                    return;
                }
                if (Utils.isEmptyString(beishu)) {
//                    showToast(R.string.input_beishu);
                    return;
                }
                if (!Utils.isNumeric(beishu)) {
                    showToast(R.string.input_correct_beishu_format);
                    return;
                }
                if (TextUtils.isEmpty(beishuInput.getText().toString())) {
                    currentCount = 0;
                } else {
                    currentCount = Integer.parseInt(beishuInput.getText().toString());
                }

                updateBottomWithBeishChange(Integer.parseInt(beishu));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_BEISHU_LENGTH) {
                    String toast = String.format("当前允许输入最大倍数长度为%d位", MAX_BEISHU_LENGTH);
                    Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
        clear_jixuan = (Button) view.findViewById(R.id.clear_jixuan);
        clear_jixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calcOutZhushu > 0) {
                    clearViewAfterBetSuccess();
                } else {
                    //开始随机投注
                    responseTouzhu(getActivity(), ballonDatas, cpVersion, cpCode, selectPlayCode,
                            selectRuleCode, true);
                }
            }
        });
        confirmBtn = (Button) view.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进购彩清单页,而不是直接下注
                onPlayTouzhu();
            }
        });
        zhushuTV = (TextView) view.findViewById(R.id.zhushu_txt);
        moneyTV = (TextView) view.findViewById(R.id.money_txt);
    }


    private boolean isFengPan = false;

    public void setFengPan(boolean fengPan) {
        isFengPan = fengPan;
        if (isFengPan) {
            if (isAdded()){
                confirmBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_press_unenable));
            }
        } else {
            if (isAdded()){
                confirmBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_selector));
            }
        }
    }

    private void changeBetText() {
        beishuInput.setText(String.valueOf(currentCount));
        updateBottomWithBeishChange(currentCount);
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

    /**
     * 调整奖金
     * @param
     */
    public float adjustMinBounds(float bounds,int selectModeIndex){
        switch (selectModeIndex){
            case 0:
                break;
            case 1:
                bounds = bounds/10;
                break;
            case 2:
                bounds = bounds/100;
                break;
        }
        return bounds;
    }



    private void updateBottomWithBeishChange(int beishu) {
        this.selectedBeishu = beishu;
        float money = adjustMoney(beishu, calcOutZhushu, selectModeIndex);
        this.selectedMoney = money;
//        selectedMinBounds = adjustMinBounds(bounds,selectModeIndex);
        updateBottom(calcOutZhushu, selectedMoney, selectedMinBounds);
    }

    /**
     * @param selectMode 以选择的金额模式 索引 ，0-元 1-角 2-分
     */
    private int switchMode(int selectMode) {
        String yjfMode = YiboPreference.instance(getActivity()).getYjfMode();
        if (Utils.isEmptyString(yjfMode)) {
            return selectModeIndex;
        }
        int yjfValue = Integer.parseInt(yjfMode);
        if (yjfValue == Constant.YUAN_MODE) {
            this.selectModeIndex = 0;
        } else if (yjfValue == Constant.JIAO_MODE) {
            if (selectMode == 0) {
                this.selectModeIndex = 0;
            } else if (selectMode == 1) {
                this.selectModeIndex = 1;
            }
        } else if (yjfValue == Constant.FEN_MODE) {
            if (selectMode == 0) {
                this.selectModeIndex = 0;
            } else if (selectMode == 1) {
                this.selectModeIndex = 1;
            } else if (selectMode == 2) {
                this.selectModeIndex = 2;
            }
        }
        return selectModeIndex;
    }

    private String[] getModeArrayWithConfig() {
        String savedMode = YiboPreference.instance(getActivity()).getYjfMode();
        if (Utils.isEmptyString(savedMode)) {
            return null;
        }
        String[] stringItems = new String[]{"元", "角", "分"};
        int mode = Integer.parseInt(savedMode);
        if (mode == Constant.YUAN_MODE) {
            stringItems = new String[]{"元"};
        } else if (mode == Constant.JIAO_MODE) {
            stringItems = new String[]{"元", "角"};
        } else if (mode == Constant.FEN_MODE) {
            stringItems = new String[]{"元", "角", "分"};
        }
        return stringItems;
    }

    private void showChooseModeList() {
        String[] stringItems = new String[]{"元", "角", "分"};
        String[] arr = getModeArrayWithConfig();
        if (arr != null) {
            stringItems = arr;
        }
        if (stringItems.length == 0) {
            return;
        }
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), stringItems, null);
        dialog.title("选择模式");
        dialog.isTitleShow(true).show();
        final String[] finalStringItems = stringItems;
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                selectModeIndex = switchMode(position);
                selectedMoney = adjustMoney(selectedBeishu, calcOutZhushu, selectModeIndex);
//                selectedMinBounds = adjustMinBounds(bounds,selectModeIndex) ;
                updateBottom(calcOutZhushu, selectedMoney, selectedMinBounds);
                modeBtn.setText(finalStringItems[position]);
            }
        });
    }

    private void handleVibrateAndSensor() {
        boolean virateAllow = YiboPreference.instance(getActivity()).isVirateAllow();
        if (!virateAllow) {
            return;
        }
        if (mVibrator == null) {
            mVibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        }
        if (mSensorManager == null) {
            mSensorManager = ((SensorManager) getActivity().getSystemService(SENSOR_SERVICE));
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onBallonClick(BallonItem ballonItem) {
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

    //局部刷新列表
//    private void updateBallView(int itemIndex,int ballPos) {
//        int visiblePosition = playListview.getFirstVisiblePosition();
//        if (itemIndex - visiblePosition >= 0) {
//            View view = playListview.getChildAt(itemIndex - visiblePosition);
//            playAdapter.updateView(view, itemIndex,ballPos);
//        }
//    }


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

    public void updatePlayNameView(String cpName) {
        playRuleTV.setText(cpName);
    }

    @Override
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
        bounds=minBounds;
        selectedMinRateBack = minRate;
        this.currentQihao = currentQihao;
        this.cpName = cpName;
        this.minRate = minBounds;
        this.cpDuration = cpDuration;
        this.maxBetNum = maxBetNum;

        clearZhushuBottonView();
        updateBottomClearBtn();

        Utils.LOG(TAG, "the cp duration = " + cpDuration);

        playRuleTV.setText(subPlayName);

        //更新投注球面板列表数据
        List<BallonRules> ballons = calcPaneMessageByPlayRule(cpVersion,
                cpCode, selectPlayCode, selectRuleCode);
        if (ballons != null) {
            ballonDatas.clear();
            ballonDatas.addAll(ballons);
        }
        refreshPaneAndClean();
    }

    @Override
    public void onPlayClean(boolean clearAfterBetSuccess) {
        if (ballonDatas.isEmpty()) {
            showToast(R.string.please_pick_numbers_first);
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
            showToast(R.string.please_pick_numbers_first);
            return;
        }
        actionCleanSelectBllons();
        refreshPaneAndClean();
    }

    public void clearZhushuBottonView() {
        calcOutZhushu = 0;
//        selectModeIndex = 0;
        selectedBeishu = 1;//选择的倍数
        selectedMoney = 0;//总投注金额
        selectedNumbers = "";//选择好的投注号码
        beishuInput.setText("1");
        currentCount = 1;
//        modeBtn.setText("元");
    }

    public void setBetListener(BetListener peilvListener) {
        this.betListener = peilvListener;
    }

    public interface BetListener {
        void onBetPost();
    }

    @Override
    public void onPlayTouzhu() {

        if (isFengPan) {
            ToastUtils.showShort("当前尚未开盘");
            return;
        }

        if (calcOutZhushu == 0) {
            showToast(R.string.please_select_correct_numbers);
            return;
        }
        //移动注单到购物车VIEW
        if (Utils.isEmptyString(selectedNumbers)) {
            showToast(R.string.havenot_calcout_number);
            return;
        }
        OrderDataInfo info = addZhuDang();
        String orderJson = new Gson().toJson(info, OrderDataInfo.class);
        TouzhuOrderActivity.createIntent(getActivity(), orderJson, cpVersion, cpName, cpDuration);
        //开始投注
//        if (betListener != null) {
//            betListener.onBetPost();
//        }
        actionCleanSelectBllons();
        refreshPaneAndClean();
        updateBottomClearBtn();
    }

    //准备投注的数据，保存在数据库中方便智能追号时使用
    public boolean prepareBetOrders() {
        if (calcOutZhushu == 0) {
            showToast(R.string.please_select_correct_numbers);
            return false;
        }
        //移动注单到购物车VIEW
        if (Utils.isEmptyString(selectedNumbers)) {
            showToast(R.string.havenot_calcout_number);
            return false;
        }
        addZhuDang();
        return true;
    }


    public void clearViewAfterBetSuccess() {
        actionCleanSelectBllons();
        refreshPaneAndClean();
        updateBottomClearBtn();
    }

    private void showToast(int showText) {
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    private void actionCleanSelectBllons() {
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 27 || Math.abs(y) > 27 || Math
                    .abs(z) > 27) && !isShake) {
                isShake = true;
                // TODO: 2016/10/19 实现摇动逻辑, 摇动后进行震动
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            //开始震动 发出提示音 展示动画效果
                            ayncHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            //再来一次震动提示
                            ayncHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(300);
                            ayncHandler.obtainMessage(END_SHAKE).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //线程异步handler
    private static class TouzhuHandler extends Handler {
        private WeakReference<JiangjinSimpleFragment> mReference;
        private JiangjinSimpleFragment mActivity;

        public TouzhuHandler(JiangjinSimpleFragment activity) {
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
                    ((TouzhuSimpleActivity) mActivity.getActivity()).onBottomUpdate(mActivity.calcOutZhushu,
                            mActivity.selectedMoney);
                    //有注数时则弹出注单浮框,让用户调整注单并投注
//                    if (zhushu > 0) {
//                        mActivity.showAdjustWindow(false);
//                    }
                    mActivity.updateBottomClearBtn();

                    //老版本当随机选的时候
//                    if (isRandom == 1) {
//                        mActivity.updateBottom(1, 2, mActivity.minRate);
//                    }
                    break;
            }
        }
    }

    public void updateBottomClearBtn() {
        if (calcOutZhushu > 0) {
            clear_jixuan.setText("清除");
        } else {
            clear_jixuan.setText("机选");
        }
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
        info.setLottype(cpCode);
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

    private void updateCartCount() {
        ((TouzhuSimpleActivity) (getActivity())).onCartUpdate();
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
                return;
            }
            int zhushu = JieBaoZhuShuCalculator.calc(Integer.parseInt(czCode), subCode, numbers);
            Utils.LOG(TAG, "the tou zhu zhu dang shu = " + zhushu);
            Message message = ayncHandler.obtainMessage(HANDLE_TOUZHU, zhushu, isRandom ? 1 : 0, numbers);
            ayncHandler.sendMessage(message);
        }
    }

    private void refreshPaneAndClean() {
        if (playAdapter != null) {
            playAdapter.notifyDataSetChanged();
            ((TouzhuSimpleActivity) getActivity()).onClearView();
            clearZhushuBottonView();
        }
    }


}
