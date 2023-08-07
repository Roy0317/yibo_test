package com.yibo.yiboapp.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.TouzhuActivity;
import com.yibo.yiboapp.adapter.PlayRuleExpandAdapter;
import com.yibo.yiboapp.data.ChannelListener;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryAlgorithm;
import com.yibo.yiboapp.data.PeilvData;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.data.PeilvZhushuCalculator;
import com.yibo.yiboapp.data.PlayCodeConstants;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.CalcPeilvOrder;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.PeilvWebResult;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.PeilvBigDataAdapterKt;
import com.yibo.yiboapp.ui.PeilvBigDataHeaderAdapterKt;
import com.yibo.yiboapp.ui.PeilvTableContainer;
import com.yibo.yiboapp.ui.PeilvZhudangPopWindow;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.utils.ViewCache;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.CustomLoadingDialog;
import crazy_wrapper.Crazy.dialog.LoadingDialog;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;

import static com.yibo.yiboapp.fragment.PeilvSimpleFragment.MAX_MONEY_WEISHU;
import static com.yibo.yiboapp.R.id.item;


/**
 * Created by johnson on 2017/9/26.
 * 赔率版投注面板
 */

public class PeilvFragment extends Fragment implements ChannelListener {

    public static final String TAG = PeilvFragment.class.getSimpleName();
    XListView playListview;
    ExpandableListView playExpandListView;
    PlayRuleExpandAdapter ruleExpandAdapter;
    List<PlayItem> playRules;

    EmptyListView emptyListView;
    EmptyListView emptyListView1;
    PeilvAdapter playAdapter;
    List<PeilvData> listDatas = new ArrayList<PeilvData>();
    List<PeilvWebResult> webResults = new ArrayList<>();
    int pageIndexWhenLargePeilvs = 1;//很多赔率项时的分页页码
    int pageSize = 1000;

    long lhcServerTime = 0;

    PeilvBigDataHeaderAdapterKt peilvBigDataHeaderAdapterKt;
    PeilvBigDataAdapterKt peilvBigDataAdapterKt;
    RecyclerView peilvRecyclerView;
    TextView playRuleTV;
    LinearLayout topLayout;
    TextView toast;
    LinearLayout moneyInputLayout;
    XEditText moneyTV;
    Button okBtn;
    Button peilvBtn;

    LinearLayout pullBar;
    TextView pullBarTxt;

    //底部投注详情控件
    Button clearBtn;
    Button touzhuBtn;
    LinearLayout middleLayout;
    TextView zhushuTV;
    TextView totalMoneyTV;

    boolean isExpanded = true;

    String selectPlayCode = "";
    public String selectRuleCode = "";
    String selectPlayName = "";
    String selectSubPlayName = "";

    public void setCurrentQihao(String currentQihao) {
        this.currentQihao = currentQihao;
    }

    String currentQihao = "";
    String cpName = "";
    long cpDuration;

    double totalMoney;//总投注金额
    int calcOutZhushu;//计算出来的注数
    float calcPeilv;//计算出来的赔率

    int selectedListPos = -1;
    int selectedGridPos = -1;
//    PeilvAdapter peilvAdapter;

    //彩种相关信息
    String cpCode = "";//彩票代号
    String cpTypeCode = "";//彩票类型代号xlistview_without_animation
    String cpVersion = String.valueOf(Constant.lottery_identify_V1);//彩票版本号
    DecimalFormat decimalFormat = new DecimalFormat("0");
    DecimalFormat pldecimalFormat = new DecimalFormat("0.000");

    MyHandler myHandler;
    PeilvZhudangPopWindow popWindow;
    public static final int CALC_ZHUSHU_MONEY = 0x1;
    public static final int CLEAR_DATA = 0x2;
    public static final int POST_DATA = 0x3;
    public static final int UPDATE_LISTVIEW = 0x4;
    public static final int LOADING_DATA = 0x5;
    private static List<PeilvPlayData> selectNumList = Collections.synchronizedList(new ArrayList<PeilvPlayData>());

    String cpBianHao;//彩票编号
    PeilvListener peilvListener;

    int buttonAudioID;
    SoundPool buttonPool;

    PlayRuleExpandAdapter.RuleSelectCallback ruleSelectCallback;

    CustomLoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewCache.getInstance().setType(2);
        ViewCache.getInstance().push(getResources().getConfiguration(), getActivity());
        return inflater.inflate(R.layout.peilv_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initKeySound();
        myHandler = new MyHandler(this);
//        playAdapter = new PeilvListAdapter(getActivity(), listDatas, R.layout.peilv_gridview);
        playListview.setVerticalScrollBarEnabled(false);
        playListview.setVerticalFadingEdgeEnabled(false);
        playListview.setFastScrollEnabled(false);
        playAdapter = new PeilvAdapter(getActivity(), listDatas);
        playAdapter.setNormalTouzhuListener(new NormalTouzhuListener() {
            //普通投注情况下，在每个号码项中输入一个金额就通知更新一次底部注数和总金额
            @Override
            public void onNormalUpdate(int pos, int cellIndex, boolean withSound) {
                //若点击列表项的时候，快捷金额输入框中有金额，则将金额填入对应赔率PeilvPlayData
                if (pos >= 0 && pos < listDatas.size()) {
                    PeilvData datas = listDatas.get(pos);
                    List<PeilvPlayData> subDatas = datas.getSubData();
                    if (datas != null && !subDatas.isEmpty()) {
                        PeilvPlayData subData = subDatas.get(cellIndex);
                        String fastMoneyValue = moneyTV.getText().toString().trim();
                        if (withSound) {
                            //播放按键音
                            if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
                                if (buttonPool == null || buttonAudioID == 0) {
                                    initKeySound();
                                }
                                buttonPool.play(buttonAudioID, 1, 1, 0, 0, 1);
                            }
                        }
                        if (!Utils.isEmptyString(fastMoneyValue)) {
                            float money = Float.parseFloat(fastMoneyValue);
                            if (!subData.isCheckbox()) {
                                if (subData.isSelected() && subData.getMoney() == 0) {//里面赔率项若是选择状态下,且金额为空时设置快捷金额
                                    Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, pos, cellIndex);
                                    message.obj = money;
                                    message.sendToTarget();
                                }
                            }
                        } else {
                            if (subData.isCheckbox()) {
                                Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, pos, cellIndex);
                                message.sendToTarget();
                            }
                        }
                    }
                }
                //输入法点确定后，将选择的号码，金额发送到主界面更新底部栏显示出来
                TouzhuThreadPool.getInstance().addTask(new CalcZhushuAndMoney(PeilvFragment.this, listDatas));
            }
        });
        playListview.setAdapter(playAdapter);
        peilvBigDataHeaderAdapterKt = new PeilvBigDataHeaderAdapterKt();
        peilvBigDataAdapterKt = new PeilvBigDataAdapterKt(getActivity(),0);
        peilvBigDataAdapterKt.setNormalTouzhuListener(new com.yibo.yiboapp.interfaces.NormalTouzhuListener() {
            @Override
            public void onNormalUpdate(int pos, int cellIndex, boolean withSound) {
                //若点击列表项的时候，快捷金额输入框中有金额，则将金额填入对应赔率PeilvPlayData
                if (pos >= 0 && pos < listDatas.size()) {
                    PeilvData datas = listDatas.get(pos);
                    List<PeilvPlayData> subDatas = datas.getSubData();
                    if (datas != null && !subDatas.isEmpty()) {
                        PeilvPlayData subData = subDatas.get(cellIndex);
                        String fastMoneyValue = moneyTV.getText().toString().trim();
                        if (withSound) {
                            //播放按键音
                            if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
                                if (buttonPool == null || buttonAudioID == 0) {
                                    initKeySound();
                                }
                                buttonPool.play(buttonAudioID, 1, 1, 0, 0, 1);
                            }
                        }
                        if (!Utils.isEmptyString(fastMoneyValue)) {
                            float money = Float.parseFloat(fastMoneyValue);
                            if (!subData.isCheckbox()) {
                                if (subData.isSelected() && subData.getMoney() == 0) {//里面赔率项若是选择状态下,且金额为空时设置快捷金额
                                    Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, pos, cellIndex);
                                    message.obj = money;
                                    message.sendToTarget();
                                }
                            }
                        } else {
                            if (subData.isCheckbox()) {
                                Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, pos, cellIndex);
                                message.sendToTarget();
                            }
                        }
                    }
                }
                //输入法点确定后，将选择的号码，金额发送到主界面更新底部栏显示出来
                TouzhuThreadPool.getInstance().addTask(new CalcZhushuAndMoney(PeilvFragment.this, listDatas));
            }
        });

        ConcatAdapter concatAdapter = new ConcatAdapter(peilvBigDataHeaderAdapterKt,peilvBigDataAdapterKt);
        peilvRecyclerView.setAdapter(concatAdapter);

        playExpandListView.setGroupIndicator(null);
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
                List<SubPlayItem> rules = playRules.get(groupPosition).getRules();
                if (rules != null && !rules.isEmpty()) {
                    if (rules.size() == 1) {
                        ruleSelectCallback.onRuleCallback(playRules.get(groupPosition), rules.get(0));
                    }
                }
                return false;
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
                List<SubPlayItem> rules = playRules.get(groupPosition).getRules();
                if (rules != null && !rules.isEmpty()) {
                    ruleSelectCallback.onRuleCallback(playRules.get(groupPosition), rules.get(childPosition));
                }
                return false;
            }
        });
        playRules = new ArrayList<>();
        ruleExpandAdapter = new PlayRuleExpandAdapter(getActivity(), playRules);
        playExpandListView.setAdapter(ruleExpandAdapter);

        moneyTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onPlayTouzhu();
                return false;
            }
        });

        moneyTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                if (Utils.isEmptyString(s.toString())) {
//                    //当输入金额为空的时候，清除所有
//                    onPlayClean(true, false);
//                    return;
//                }
                if (Utils.isEmptyString(s.toString())) {
                    s = "0";
                }
                final float moneyValue = Float.parseFloat(s.toString());
                if (selectNumList.isEmpty()) {
                    return;
                }
                for (PeilvPlayData data : selectNumList) {
                    if (data.isSelected()) {
                        data.setMoney(moneyValue);
                    }
                }
                for (int i = 0; i < listDatas.size(); i++) {
                    PeilvData peilvData = listDatas.get(i);
                    for (int j = 0; j < peilvData.getSubData().size(); j++) {
                        PeilvPlayData data = peilvData.getSubData().get(j);
                        if (data.isSelected()) {
                            Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, i, j);
                            message.obj = moneyValue;
                            message.sendToTarget();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.LOG("aaa", "onclick ok btn");
                onPlayTouzhu();
//                String moneyValue = moneyTV.getText().toString().trim();
//                if (Utils.isEmptyString(moneyValue)) {
//                    showToast(R.string.input_peilv_money);
//                    moneyTV.setFocusable(true);
//                    moneyTV.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
//                            Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    return;
//                }
//                if (!Utils.isNumeric(moneyValue)) {
//                    showToast(R.string.input_money_must_be_zs);
//                    return;
//                }
//                //开始下注
//                actionTouzhu();
            }
        });

    }

    public void setRuleSelectCallback(PlayRuleExpandAdapter.RuleSelectCallback ruleSelectCallback) {
        this.ruleSelectCallback = ruleSelectCallback;
    }

    public void setCpBianHao(String cpBianHao) {
        this.cpBianHao = cpBianHao;
    }

    private void showToast(int showText) {
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String showText) {
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    //获取第几页的赔率数据

    private void getPagePeilvDatas() {
        updatePlayArea(webResults);
    }

    boolean isFengPan = false;

    public void setFengPan(boolean isFengPan) {
        this.isFengPan = isFengPan;
        updateListView();

        if (isFengPan) {
            if (isAdded()) {
                touzhuBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_press_unenable));
            }
        } else {
            if (isAdded()) {
                touzhuBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_selector));
            }
        }
    }

    /**
     * 列表下拉，上拉监听器
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
//            actionAcquireData(false);
        }

        public void onLoadMore() {
            getPagePeilvDatas();
        }
    }

    private void initView(final View view) {
        playListview = view.findViewById(R.id.listview);
        playListview.setPullLoadEnable(false);
        playListview.setPullRefreshEnable(false);
        playListview.setXListViewListener(new ListviewListener());
        playExpandListView = (ExpandableListView) view.findViewById(R.id.expand_list);
        playListview.setDivider(getResources().getDrawable(R.color.driver_line_color));
        playListview.setDividerHeight(1);
        playListview.setVisibility(View.VISIBLE);
        playListview.setFastScrollEnabled(true);

        emptyListView = view.findViewById(R.id.empty);
        emptyListView.setShowText(getActivity().getString(R.string.temp_not_support));
        emptyListView.setListener(new EmptyViewClickListener());

        emptyListView1 = view.findViewById(R.id.empty1);
        emptyListView1.setShowText("没有赔率数据,请联系客服");
        TextView textView = emptyListView1.findViewById(R.id.click_refresh);
        textView.setVisibility(View.GONE);

        topLayout = (LinearLayout) view.findViewById(R.id.layout);
        moneyInputLayout = (LinearLayout) view.findViewById(R.id.money_input_layout);
//        selectedPeilvNumber = (TextView) view.findViewById(R.id.select_peilv_number);
        toast = (TextView) view.findViewById(R.id.toast);
        moneyTV = (XEditText) view.findViewById(R.id.input_money);
        okBtn = (Button) view.findViewById(R.id.ok);
        peilvBtn = (Button) view.findViewById(R.id.peilv_view);
        peilvBtn.setEnabled(false);
        peilvRecyclerView = (RecyclerView) view.findViewById(R.id.peilvRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false);
        peilvRecyclerView.setLayoutManager(layoutManager);

        playRuleTV = (TextView) view.findViewById(R.id.play_rule);

        pullBar = (LinearLayout) view.findViewById(R.id.pull_bar);
        pullBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                View expandView = view.findViewById(R.id.play_rule_rexpand_layout);
                ImageView pullImg = (ImageView) view.findViewById(R.id.pull_img);
                expandView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                pullImg.setBackgroundResource(isExpanded ? R.drawable.pull_back_icon : R.drawable.pull_forward_icon);
                pullBarTxt.setText(isExpanded ? "点击隐藏玩法栏" : "点击显示玩法栏");
            }
        });
        pullBarTxt = (TextView) pullBar.findViewById(R.id.pull_bar_txt);
        middleLayout = (LinearLayout) view.findViewById(R.id.middle_layout);
        zhushuTV = (TextView) view.findViewById(R.id.zhushu_txt);
        totalMoneyTV = (TextView) view.findViewById(R.id.money_txt);
        middleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZhudangWindow();
            }
        });
        clearBtn = (Button) view.findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayClean(false);
            }
        });
        touzhuBtn = (Button) view.findViewById(R.id.confirm);
        touzhuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayTouzhu();
            }
        });
        moneyTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_MONEY_WEISHU) {
                    String toast = String.format("当前允许输入最大投注额长度为%d位", MAX_MONEY_WEISHU);
                    Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateBottom(int zhushu, double totalMoney) {
        if (zhushuTV == null || totalMoneyTV == null) {
            return;
        }
        zhushuTV.setText(String.format("%d注", zhushu));
        totalMoneyTV.setText(String.format("%.2f元", totalMoney));
    }

    public void updatePlayRules(List<PlayItem> items) {
        if (items != null) {
            playRules.clear();
            playRules.addAll(items);
        }

        ruleExpandAdapter.notifyDataSetChanged();
        for (int i = 0; i < ruleExpandAdapter.getGroupCount(); i++) {
            playExpandListView.expandGroup(i);
        }
    }

    /**
     * 没有某玩法对应球面板，或赔率数据加载失败时。点击重试事件
     */
    private final class EmptyViewClickListener implements EmptyListView.EmptyListviewListener {

        @Override
        public void onEmptyListviewClick() {
            //异步获取玩法对应的赔率信息
            if (peilvListener != null) {
                peilvListener.onPeilvAcquire(selectRuleCode, false);
            }
        }
    }

    public void setPeilvListener(PeilvListener peilvListener) {
        this.peilvListener = peilvListener;
    }

    public interface PeilvListener {
        void onPeilvAcquire(String playCode, boolean showDialog);

        /**
         * @param selectDatas 选择的项
         * @param money       多选状态时输入的金额
         */
        void onBetPost(List<PeilvPlayData> selectDatas, boolean isMulSelect, String money);
    }

    private void initKeySound() {
        if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
            //初始化SoundPool
            buttonPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
            buttonAudioID = buttonPool.load(getActivity(), R.raw.bet_select, 1);
        }
    }

    private boolean performItemFunc(final PeilvPlayData item, int listPos, int gridPos) {
        //更新数据并刷新界面
        TouzhuThreadPool.getInstance().addTask(new CalcListWhenSelectItem
                (TextUtils.isEmpty(moneyTV.getText().toString().trim()) ? "O" : moneyTV.getText().toString().trim()
                        , item, listPos, gridPos));
        return true;
    }

    private void updateView(int itemIndex, int gridPos, float moneyValue) {
        int visiblePosition = playListview.getFirstVisiblePosition();
        if (itemIndex - visiblePosition >= 0) {
            View view = null;
            int viewPos = 0;
            if (itemIndex == 0) {
                viewPos = itemIndex - visiblePosition + 1;
            } else {
                viewPos = itemIndex - visiblePosition;
            }
            view = playListview.getChildAt(viewPos);
            playAdapter.updateView(view, itemIndex, gridPos, moneyValue);
        }
    }

    private void onItemClick(final PeilvPlayData item, final int listPos, final int gridPos) {

        this.selectedListPos = listPos;
        this.selectedGridPos = gridPos;
        if (item == null) {
            return;
        }
        //播放按键音
        if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
            if (buttonPool == null || buttonAudioID == 0) {
                initKeySound();
            }
            buttonPool.play(buttonAudioID, 1, 1, 0, 0, 1);
        }
        performItemFunc(item, listPos, gridPos);

    }

    private boolean actionTouzhu() {
//        String moneyValue = moneyTV.getText().toString().trim();
//        if (Utils.isEmptyString(moneyValue)) {
//            showToast(R.string.input_peilv_money);
//            return false;
//        }
//        if (!Utils.isNumeric(moneyValue)) {
//            showToast(R.string.input_money_must_be_zs);
//            return false;
//        }
//        //开始下注
//        onPlayTouzhu();
        onPlayTouzhu();
        return true;
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

    //异步计算总注数和总金额
    private final class CalcZhushuAndMoney implements Runnable {

        List<PeilvData> datas;
        WeakReference<PeilvFragment> weak;
        List<PeilvPlayData> selectedDatas;

        CalcZhushuAndMoney(PeilvFragment context, List<PeilvData> datas) {
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
                sendMessage(0, 0, 0, isMulSelectMode(selectPlayCode), selectDatas);
                return;
            }
            PeilvPlayData pData = selectDatas.get(0);
            if (pData == null) {
                sendMessage(0, 0, 0, isMulSelectMode(selectPlayCode), selectDatas);
                return;
            }
            int totalZhushu = 0;
            float totalMoney = 0;
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
                PeilvWebResult webResult = UsualMethod.getPeilvData(getActivity(),cpBianHao, selectPlayCode, selectDatas.size(), pData);
                if (webResult == null) {
                    ToastUtils.showShort("所选号码请勿大于8位");
                    return;
                }
                Integer zhushu = PeilvZhushuCalculator.buyZhuShuValidate(numbers.toString(),
                        webResult.getMinSelected(), webResult.getPlayCode());
//                Utils.LOG(TAG, "zhushu ==== " + zhushu);
                totalZhushu += zhushu;
                String moneyValue = moneyTV.getText().toString().trim();
                if (!Utils.isEmptyString(moneyValue)) {
                    totalMoney = Float.parseFloat(moneyValue);
                }
                peilvWhenMultiSelect = webResult.getOdds();
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
        private void sendMessage(float totalMoney, float peilv, int totalZhushu, boolean isMultiSelect, List<PeilvPlayData> list) {
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


    public class PeilvAdapter extends BaseAdapter {

        Context context;
        List<PeilvData> mDatas;
        LayoutInflater mLayoutInflater;
        NormalTouzhuListener normalTouzhuListener;

        public PeilvAdapter(Context mContext, List<PeilvData> mDatas) {
            context = mContext;
            this.mDatas = mDatas;
            mLayoutInflater = LayoutInflater.from(mContext);
            ViewCache.getInstance().setType(2);
            ViewCache.getInstance().push(getResources().getConfiguration(), context);
        }

        public void setNormalTouzhuListener(NormalTouzhuListener normalTouzhuListener) {
            this.normalTouzhuListener = normalTouzhuListener;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            Log.e("getView方法走了", "-------");
            PeilvViewHolder peilvViewHolder;
            if (convertView == null) {
                peilvViewHolder = new PeilvViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.peilv_gridview, parent, false);
                peilvViewHolder.tagTV = (TextView) convertView.findViewById(R.id.tag);
                peilvViewHolder.tableView = (PeilvTableContainer) convertView.findViewById(R.id.tables);
                peilvViewHolder.tableView.setNormalTouzhuListener(normalTouzhuListener);
                convertView.setTag(peilvViewHolder);

            } else {
                peilvViewHolder = (PeilvViewHolder) convertView.getTag();
            }


            setDatas(peilvViewHolder, position);
            return convertView;
        }

        private void updateView(View view, int itemIndex, int gridPos, float moneyValue) {
            if (view == null) {
                return;
            }
            if (view.getTag() == null) {
                return;
            }
            PeilvViewHolder peilvViewHolder = (PeilvViewHolder) view.getTag();
            setDatas(peilvViewHolder, itemIndex, gridPos, moneyValue);
        }

        /**
         * 这个方法的作用？？？
         *
         * @param holder
         * @param position
         */
        private void setDatas(PeilvViewHolder holder, int position) {
            setDatas(holder, position, -1, -1);
        }

        @TargetApi(Build.VERSION_CODES.M)
        private void setDatas(PeilvViewHolder holder, final int position, int selectGridPos, final float moneyValue) {
            PeilvData item = mDatas.get(position);
            if (Utils.isEmptyString(item.getTagName())) {
                holder.tagTV.setVisibility(View.GONE);
            } else {
                holder.tagTV.setVisibility(View.VISIBLE);
                holder.tagTV.setText(item.getTagName());
            }

            PeilvTableContainer tableView = holder.tableView;
            tableView.setFengpan(isFengPan);
            tableView.setCpCode(cpCode);
            tableView.setTablePosition(position);


            //selectGridPos始终等于-1
            if (selectGridPos == -1) {
                tableView.fillTables(item.getSubData(), getActivity(), mDatas);
            } else {
                if (item.getSubData() != null) {
                    if (moneyValue != -1) {
                        PeilvPlayData playData = item.getSubData().get(selectGridPos);
                        if (playData != null) {
                            playData.setMoney(moneyValue);
                            item.getSubData().set(selectGridPos, playData);
                        }
                    }
                    tableView.updateTableItem(item.getSubData(), selectGridPos);
                }
            }


            tableView.setTableCellListener(new PeilvTableContainer.TableCellListener() {
                @Override
                public void onCellSelect(PeilvPlayData data, int cellIndex) {
                    if (isFengPan) {
                        ToastUtils.showShort(R.string.result_not_open_now);
                        return;
                    }
                    onItemClick(data, position, cellIndex);
                }
            });
        }


    }


    private static final class PeilvViewHolder {
        LinearLayout tagLayout;
        TextView tagTV;
        PeilvTableContainer tableView;
    }

    @Override
    public void onPlayRuleSelected(String cpVersion, String cpCode, String playCode,
                                   String subPlayCode, String playName, String subPlayName,
                                   float maxBounds, float maxRate, String currentQihao,
                                   String cpName, long cpDuration, int maxBetNum) {

        pageIndexWhenLargePeilvs = 1;
        moneyTV.setText("");//切换玩法时，先将金额输入框置空

        //更新玩法代码等变量
        this.cpVersion = cpVersion;
        this.cpCode = cpCode;
        this.selectPlayCode = playCode;
        this.selectRuleCode = subPlayCode;
        this.selectPlayName = playName;
        this.selectSubPlayName = subPlayName;
        this.currentQihao = currentQihao;
        this.cpName = cpName;
        this.cpDuration = cpDuration;

        playRuleTV.setText(subPlayName);
        selectNumList.clear();
        //如果是多选投注号码的情况下，切换顶部投注金额面板，去除正常的号码选择提示。
        if (isMulSelectMode(selectPlayCode)) {
            toast.setVisibility(View.GONE);
//            okBtn.setClickable(false);
        } else {
            peilvBtn.setClickable(false);
            toast.setVisibility(View.VISIBLE);
            peilvBtn.setVisibility(View.GONE);
            peilvBtn.setText("赔率:");
        }
        //异步获取玩法对应的赔率信息
        if (peilvListener != null) {
            peilvListener.onPeilvAcquire(subPlayCode, true);
        }
    }

    private void showProgressDialog(String showText) {
        if (loadingDialog == null && !getActivity().isFinishing()) {
            loadingDialog = new CustomLoadingDialog(getActivity(), false);
            loadingDialog.setOnDismissListener(new LoadingDialog.DismissListener() {
                @Override
                public void onDismiss() {
                    loadingDialog = null;
                }
            });
            loadingDialog.setContent(showText);
            CustomDialogManager dialogManager = (CustomDialogManager) loadingDialog;
            dialogManager.createDialog();
        }
    }

    private void dismissProgressDialog() {
        if (!getActivity().isFinishing()) {
            if (loadingDialog != null) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
            }
        }
    }

    //是否大量数据的玩法
    private boolean isBigDataPlayCode(String code) {
        if (code.equalsIgnoreCase(PlayCodeConstants.sanzidingwei) ||
                code.equalsIgnoreCase(PlayCodeConstants.erzidingwei) ||
                code.equalsIgnoreCase(PlayCodeConstants.pl3_erzizuhe) ||
                code.equalsIgnoreCase(PlayCodeConstants.pl3_sanzizuhe)) {
            return true;
        }
        return false;
    }


    public void updatePlayArea() {
        emptyListView1.setVisibility(View.VISIBLE);
        playListview.setVisibility(View.GONE);

    }

    /**
     * 获取到后台赔率数据后更新玩法号码面板，更新赔率及号码
     *
     * @param results
     */
    public void updatePlayArea(final List<PeilvWebResult> results) {

        emptyListView1.setVisibility(View.GONE);
        playListview.setVisibility(View.VISIBLE);

        webResults = results;
        if (isBigDataPlayCode(selectPlayCode)) {
            playListview.setPullLoadEnable(true);
        } else {
            playListview.setPullLoadEnable(false);
        }
        if (results == null || results.isEmpty()) {
            //加载赔率数据失败后，显示空占位VIEW
            listDatas.clear();
            playListview.setEmptyView(emptyListView);
            return;
        }
        if (Utils.isEmptyString(cpVersion) || Utils.isEmptyString(cpCode) ||
                Utils.isEmptyString(selectPlayCode) ||
                Utils.isEmptyString(selectRuleCode)) {
            return;
        }
        //若是号码量超大的玩法，且页码超过大于1时，视为追加数据
        boolean appendData = isBigDataPlayCode(selectPlayCode);
        List<PeilvData> peilvData = figurePlayDatas(cpVersion, cpCode, selectPlayCode,
                selectRuleCode, pageIndexWhenLargePeilvs, pageSize, appendData, webResults);
        if (peilvData == null) {
            listDatas.clear();
            updateListView();
            playListview.setVisibility(View.VISIBLE);
            return;
        }
        Utils.LOG(TAG, "dada = " + listDatas.size());
        refreshPaneAndClean(false);
        if (appendData) {
            pageIndexWhenLargePeilvs++;
            if (playListview.isPullLoading()) {
                playListview.stopLoadMore();
            }
        }

    }

    private void updateListView(){
        boolean appendData = isBigDataPlayCode(selectPlayCode);
        if (appendData) {
            playListview.setVisibility(View.GONE);
            peilvRecyclerView.setVisibility(View.VISIBLE);
            peilvBigDataAdapterKt.setList(listDatas.get(0).getSubData());
            peilvBigDataAdapterKt.setFengpan(isFengPan);
            peilvBigDataAdapterKt.setCpCode(cpCode);
            peilvBigDataAdapterKt.notifyDataSetChanged();
            peilvBigDataAdapterKt.setTableCellListener(new PeilvBigDataAdapterKt.TableCellListener() {
                @Override
                public void onCellSelect(@Nullable PeilvPlayData data, int cellIndex) {
                    if (isFengPan) {
                        ToastUtils.showShort(R.string.result_not_open_now);
                        return;
                    }
                    onItemClick(data, 0, cellIndex);
                }
            });

        } else {
            playListview.setVisibility(View.VISIBLE);
            peilvRecyclerView.setVisibility(View.GONE);
            Utils.LOG(TAG, "dada = " + listDatas.size());
            playAdapter.notifyDataSetChanged();

        }
    }

    private boolean isMulSelectMode(String playCode) {
        if (playCode.equals(PlayCodeConstants.zuxuan_san_peilv) ||
                playCode.equals(PlayCodeConstants.zuxuan_liu_peilv) ||
                playCode.equals(PlayCodeConstants.lianma_peilv_klsf) ||
                playCode.equals(PlayCodeConstants.lianma) ||
                playCode.equals(PlayCodeConstants.hexiao) ||
                playCode.equals(PlayCodeConstants.quanbuzhong) ||
                playCode.equals(PlayCodeConstants.weishulian) ||
                playCode.equals(PlayCodeConstants.syx5_renxuan) ||
                playCode.equals(PlayCodeConstants.lianxiao) ||
                playCode.equals(PlayCodeConstants.syx5_zuxuan) ||
                playCode.equals(PlayCodeConstants.syx5_zhixuan)
        ) {
            return true;
        }
        return false;
    }

    private long isXGLHCServerTime(String lotCode) {
        if (UsualMethod.isXGSixMark(lotCode)) {
            return this.lhcServerTime;
        }
        return 0;
    }


    public void setLhcServerTime(long lhcServerTime) {
        this.lhcServerTime = lhcServerTime;
    }

    /**
     * 计算出对应玩法下的号码及赔率面板区域显示数据
     *
     * @param cpVersion       彩票版本
     * @param cpCode          彩票类型代号
     * @param playCode        大玩法代号
     * @param subPlayCode     小玩法代号
     * @param pageIndex       页码
     * @param pageSize        每页大小
     * @param appendData      是否将计算出来的数据追加到原有数据后面
     * @param peilvWebResults 后台获取到的玩法对应的赔率数据
     */
    private List<PeilvData> figurePlayDatas(String cpVersion, String cpCode,
                                            String playCode, String subPlayCode,
                                            int pageIndex, int pageSize, boolean appendData,
                                            List<PeilvWebResult> peilvWebResults) {

        if (peilvWebResults == null) {
            return null;
        }
        List<PeilvData> peilvDatas = LotteryAlgorithm.figurePeilvOutPlayInfo(getActivity(),
                cpVersion, cpCode, playCode, subPlayCode, pageIndex, pageSize, peilvWebResults, appendData, isXGLHCServerTime(this.cpBianHao));
        if (peilvDatas == null) {
            return null;
        }
        if (!appendData) {
            listDatas.clear();
            //简约版本，去除赔率为空的项
            Iterator<PeilvData> iterator = peilvDatas.iterator();
            while (iterator.hasNext()) {
                PeilvData next = iterator.next();
                if (next.getSubData().isEmpty()) {
                    iterator.remove();
                }
            }
            listDatas.addAll(peilvDatas);
        } else {
            if (pageIndexWhenLargePeilvs > 1) {
                for (int i = 0; i < peilvDatas.size() && listDatas.size() == peilvDatas.size(); i++) {
                    PeilvData datas = peilvDatas.get(i);
                    if (datas != null) {
                        List<PeilvPlayData> subData = datas.getSubData();
                        if (subData != null) {
                            listDatas.get(i).getSubData().addAll(subData);
                        }
                    }
                }
            } else {
                listDatas.clear();
                listDatas.addAll(peilvDatas);
            }

        }
        return listDatas;
    }

    private void refreshPaneAndClean(boolean noClearView) {
        if (playAdapter != null) {
            updateListView();
            if (!noClearView) {
                ((TouzhuActivity) getActivity()).onClearView();
            }
        }
    }

    @Override
    public void onPlayClean(boolean clearAfterBetSuccess) {
        if (selectNumList.isEmpty()) {

            showToast(R.string.please_pick_numbers_first);

            return;
        }
        TouzhuThreadPool.getInstance().addTask(new PeilvBetRunnable(selectNumList, true, clearAfterBetSuccess));
    }


//    public void onPlayClean(boolean clearAfterBetSuccess, boolean show) {
//        if (selectNumList.isEmpty()) {
//            if (show) {
//                showToast(R.string.please_pick_numbers_first);
//            }
//            return;
//        }
//        TouzhuThreadPool.getInstance().addTask(new PeilvBetRunnable(selectNumList, true, clearAfterBetSuccess));
//    }


    @Override
    public void onPlayTouzhu() {
//        if (calcOutZhushu == 0 && !isMulSelectMode(selectPlayCode)) {
//            showToast(R.string.input_peilv_money);
//            return;
//        }

        if (isFengPan) {
            ToastUtils.showShort(R.string.result_not_open_now);
            return;
        }

        if (selectNumList.isEmpty()) {
            showToast(R.string.please_pick_numbers_first);
            return;
        }
        if (isMulSelectMode(this.selectPlayCode)) {//多选状态下一定要判断快捷金额输入框是否有金额
            if (Utils.isEmptyString(moneyTV.getText().toString().trim())) {
                showToast(R.string.input_peilv_money);
                moneyTV.setFocusable(true);
                moneyTV.requestFocus();
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
                showToast(R.string.input_peilv_money);
                moneyTV.setFocusable(true);
                moneyTV.requestFocus();
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

    private void showConfirmBetDialog() {

        final CustomConfirmDialog ccd = new CustomConfirmDialog(getActivity());
        ccd.setBtnNums(2);
        String content = "是否提交下注？";
        ccd.setContent(content);
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
//                TouzhuThreadPool.getInstance().addTask(new PeilvBetRunnable(selectNumList, false, false));
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private final class MyHandler extends Handler {

        private WeakReference<PeilvFragment> mReference;
        private PeilvFragment fragment;

        public MyHandler(PeilvFragment fragment) {
            mReference = new WeakReference<PeilvFragment>(fragment);
            if (mReference != null) {
                this.fragment = mReference.get();
            }
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
                        String moneyStr = moneyTV.getText().toString().trim();
                        if (!Utils.isEmptyString(moneyStr)) {
                            money = Float.parseFloat(moneyStr);
                        }
                        peilvBtn.setVisibility(View.VISIBLE);
                        peilvBtn.setText("赔率:" + peilv);
                        money = money * zhushu;
                    }
                    fragment.totalMoney = money;
                    fragment.calcPeilv = peilv;
                    fragment.calcOutZhushu = zhushu;
                    ((TouzhuActivity) fragment.getActivity()).onBottomUpdate(zhushu, money);
                    break;
                case CLEAR_DATA:
                    boolean clearAfterSuccess = (boolean) message.obj;
                    if (!clearAfterSuccess) {
                        showToast(R.string.clean_success);
                    }
                    selectNumList.clear();
                    fragment.actionRestore();

                    calcPeilv = 0;
                    totalMoney = 0;
                    calcOutZhushu = 0;
                    fragment.selectedListPos = -1;
                    fragment.selectedGridPos = -1;
                    ((TouzhuActivity) getActivity()).onBottomUpdate(0, 0);
                    break;
                case POST_DATA:
                    List<PeilvPlayData> selectedItems = (List<PeilvPlayData>) message.obj;
                    if (selectedItems != null && !selectedItems.isEmpty()) {
                        String moneyStr = "";
                        PeilvPlayData peilvData = selectedItems.get(0);
                        if (peilvData.isCheckbox()) {
                            moneyStr = fragment.moneyTV.getText().toString().trim();
                            if (Utils.isEmptyString(moneyStr)) {
                                showToast(R.string.please_tapin_bet_money);
                                return;
                            }
                        }
                        if (peilvListener != null) {
                            peilvListener.onBetPost(selectedItems, peilvData.isCheckbox(), moneyStr);
                        }
                    } else {
                        showToast(R.string.please_touzhu_first);
                    }
                    break;
                case UPDATE_LISTVIEW:
//                    refreshPaneAndClean(true);
                    int listPos = message.arg1;
                    int gridPos = message.arg2;
                    float moneyValue = -1;
                    if (message.obj != null) {
                        moneyValue = (float) message.obj;
                    } else {
                        moneyValue = -1;
                    }
                    if (listDatas.get(listPos).getSubData() != null) {
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
                    updateListView();
                    //输入法点确定后，将选择的号码，金额发送到主界面更新底部栏显示出来
                    TouzhuThreadPool.getInstance().addTask(new CalcZhushuAndMoney(fragment, listDatas));
                    break;
                case LOADING_DATA:
//                    fragment.dismissProgressDialog();
                    List<PeilvData> peilvs = (List<PeilvData>) message.obj;
                    if (peilvs == null) {
                        listDatas.clear();
                        updateListView();
                        playListview.setVisibility(View.VISIBLE);
                        return;
                    }
                    listDatas.addAll(peilvs);
                    refreshPaneAndClean(false);
                    break;
            }
        }
    }

    private void actionRestore() {
        //恢复列表初始状态
        refreshPaneAndClean(false);
        //恢复输入框到初始状态
        moneyTV.setText("");
        pageIndexWhenLargePeilvs = 1;
    }

    private final class CalcListWhenSelectItem implements Runnable {

        String fee;
        PeilvPlayData oldData;
        int listPos;
        int gridPos;

        public CalcListWhenSelectItem(String fee, PeilvPlayData data, int listPos, int gridPos) {
            this.fee = fee;
            this.oldData = data;
            this.listPos = listPos;
            this.gridPos = gridPos;
        }

        @Override
        public void run() {
            PeilvPlayData data = new PeilvPlayData();
            data.setCheckbox(oldData.isCheckbox());
            data.setNumber(oldData.getNumber());
            data.setPeilv(oldData.getPeilv());
//            data.setSelected(oldData.isSelected());
            data.setAllDatas(oldData.getAllDatas());
            data.setPeilvData(oldData.getPeilvData());
            data.setHelpNumber(oldData.getHelpNumber());
            data.setAppendTag(oldData.isAppendTag());
            data.setAppendTagToTail(oldData.isAppendTagToTail());
            data.setFilterSpecialSuffix(oldData.isFilterSpecialSuffix());
            data.setMoney(!Utils.isEmptyString(fee) &&
                    Utils.isNumeric(fee) ? Integer.parseInt(fee) : 0);
            if (!Utils.isEmptyString(fee)) {
                data.setMoney(Utils.isNumeric(fee) ? Integer.parseInt(fee) : 0);
            } else {
                data.setMoney(oldData.getMoney());
            }
            if (data.isSelected()) {
                data.setFocusDrawable(R.drawable.table_textview_bg_press);
            } else {
                data.setFocusDrawable(R.drawable.table_textview_bg);
                data.setMoney(0);
            }
            //}
            PeilvData peilvData = listDatas.get(listPos);
            List<PeilvPlayData> subData = peilvData.getSubData();
            if (gridPos >= subData.size()) {
                return;
            }
            subData.set(gridPos, data);
            Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, listPos, gridPos);
            message.sendToTarget();
        }
    }

    public interface NormalTouzhuListener {
        void onNormalUpdate(int tablePos, int cellIndex, boolean withSound);
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
        public void run() {
//            synchronized (PeilvFragment.this) {
            if (clear) {
                for (PeilvPlayData playData : datas) {
                    playData.setSelected(false);
                    playData.setMoney(0);
                    playData.setFocusDrawable(0);
                }
                Message message = myHandler.obtainMessage(CLEAR_DATA, clearAfterBetSuccess);
                message.sendToTarget();
                return;
            }

//                String moneyValue = moneyTV.getText().toString().trim();
//                for (PeilvPlayData playData : datas) {
//                    if (!playData.isCheckbox()) {
//                        if (!Utils.isEmptyString(moneyValue)) {
////                        playData.setMoney(Float.parseFloat(moneyValue));
//                        }
//                    }
//                }
            Message message = myHandler.obtainMessage(POST_DATA, datas);
            message.sendToTarget();
        }
    }
//    }

    public void showZhudangWindow() {
        if (selectNumList == null || selectNumList.isEmpty()) {
            showToast(R.string.havent_touzhu_anymore);
            return;
        }
        Utils.LOG(TAG, "the order num = " + selectNumList.size());
        boolean isMultiSelect = selectNumList.get(0).isCheckbox();
        String numberWhenMultiSelect = null;
        if (isMultiSelect) {
            List<String> numbers = new ArrayList<>();
            for (PeilvPlayData data : selectNumList) {
                numbers.add(data.getNumber());
            }
            Collections.sort(numbers);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numbers.size(); i++) {
                String num = numbers.get(i);
                sb.append(num);
                if (i != numbers.size() - 1) {
                    sb.append(",");
                }
            }
            Utils.LOG(TAG, "the sort number = " + sb.toString());
            numberWhenMultiSelect = sb.toString();
        }

        List<OrderDataInfo> orders = new ArrayList<>();
        for (PeilvPlayData data : selectNumList) {
            if (!isSelectedNumber(data)) {
                return;
            }
            OrderDataInfo info = new OrderDataInfo();
            if (isMultiSelect) {
                info.setRate(calcPeilv);
                info.setZhushu(calcOutZhushu);
                info.setMoney(totalMoney);
                info.setNumbers(numberWhenMultiSelect);
                info.setPlayData(data);
            } else {
                info.setRate(data.getPeilvData().getOdds());
                info.setZhushu(1);
                info.setMoney(data.getMoney());
                info.setNumbers(UsualMethod.getPeilvPostNumbers(data));
                info.setPlayData(data);
            }

            info.setSaveTime(System.currentTimeMillis());
            info.setMode(Constant.YUAN_MODE);
            info.setLotcode(cpCode);
            info.setPlayCode(selectPlayCode);
            info.setSubPlayCode(selectRuleCode);
            info.setPlayName(selectPlayName);
            info.setSubPlayName(selectSubPlayName);
            orders.add(info);
            if (isMultiSelect) {
                break;
            }
        }

        if (popWindow == null) {
            popWindow = new PeilvZhudangPopWindow(getActivity());
            popWindow.setAnimationStyle(R.style.adjust_window_anim);
            popWindow.setPeilvWindowListener(new PeilvZhudangPopWindow.PeilvWindowListener() {
                @Override
                public void onWindowDismiss(String json) {
                    if (Utils.isEmptyString(json)) {
                        return;
                    }
                }
            });
        }
        popWindow.setData(orders, currentQihao);
        if (!popWindow.isShowing()) {
            popWindow.showWindow(getActivity().findViewById(item));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listDatas.clear();
        decimalFormat = null;
        pldecimalFormat = null;
    }
}
