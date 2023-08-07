package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BetToken;
import com.yibo.yiboapp.entify.CountDown;
import com.yibo.yiboapp.entify.LocCountDownWraper;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.ZuihaoListData;
import com.yibo.yiboapp.entify.ZuihaoQihaoWraper;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.PopupListMenu;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.ui.ZhudangDetailPopWindow;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * 智能追号
 *
 * @author johnson
 * 输入：追几期：该彩种今天剩余期数，初始倍数: 追号的第一期倍数
 */
public class BraveZuiHaoActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {


    SwipeMenuListView listView;
    TextView totalQishuTV;
    TextView totalMoneyTV;
    CheckBox stopZuihaoCheckBox;
    TextView jianQihao;
    TextView addQihao;
    XEditText zuihaoInput;
    Button touzhuBtn;

    TextView panBeiTV;
    TextView tongBeiTV;
    View panBeiBar;
    View tongBeiBar;
    LinearLayout meigeLayout;
    LinearLayout beixLayout;
    LinearLayout startBeiLayout;
    XEditText meigeInput;
    XEditText beixInput;
    XEditText startBeiInput;

    ZhuDangAdapter zhuDangAdapter;
    List<ZuihaoListData> zuihaoDatas;
    float totalBaseMoney;//单位元

    int initBeishu = 1;
    int initQishu = 10;

    CalcHandler calcHandler;
    float totalTouzhuFee;//总追号投注金额
    int maxQishu;//可追号最大期数
    List<Long> qihaos;

    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    ZhudangDetailPopWindow popWindow;

    public static final int ACQUIRE_VALID_QISHU = 0x01;
    public static final int TOKEN_BETS_REQUEST = 0x03;
    public static final int CURRENT_QIHAO_REQUEST = 0x04;
    public static final int REFRESH_QIHAO_WHEN_RECENT_QIHAO_INVALID_REQUEST = 0x05;

    public static final int UPDATE_LISTVIEW = 0x01;
    public static final int UPDATE_TOTALFEE = 0x02;
    String baseOrdersJson;//基本下注注单信息，用于追号时每期的基本投注信息
    String cpBianma;//彩票编码
    CountDownTimer endlineTouzhuTimer;//最新一期期号离投注结束的倒计时器

    int initMeigeBeishu = 1;
    int beishuMultiple = 2;
    int initStartBeishu = 1;

    public static final int FAN_BEI_MODE = 0;
    public static final int TONG_BEI_MODE = 1;
    int currentMode = FAN_BEI_MODE;//

    public static final int MEIGE_INPUT_TAG = 0;
    public static final int BEI_MULTIPLE_INPUT_TAG = 1;
    public static final int START_BEISHU_INPUT_TAG = 2;

    List<String> invalidQihaos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brave_zuihao);
        initView();

        calcHandler = new CalcHandler(this);
        cpBianma = getIntent().getStringExtra("cpBianma");
        baseOrdersJson = getIntent().getStringExtra("zhudang");
        totalBaseMoney = getTotalMoneyOfSelectedZhudang(baseOrdersJson);

        zuihaoDatas = new ArrayList<>();
        zhuDangAdapter = new ZhuDangAdapter(this, zuihaoDatas, R.layout.zuihao_listitem);
        listView.setAdapter(zhuDangAdapter);

        totalQishuTV = (TextView) findViewById(R.id.total_qishu);
        totalMoneyTV = (TextView) findViewById(R.id.total_money);
        stopZuihaoCheckBox = (CheckBox) findViewById(R.id.zuihao_checkbox);
        jianQihao = (TextView) findViewById(R.id.jian);
        addQihao = (TextView) findViewById(R.id.add);
        zuihaoInput = (XEditText) findViewById(R.id.qihao_input);

        View header = LayoutInflater.from(this).inflate(R.layout.brave_zuihao_header, null);

        meigeLayout = (LinearLayout) header.findViewById(R.id.peige_layout);
        startBeiLayout = (LinearLayout) header.findViewById(R.id.start_beishu_layout);
        beixLayout = (LinearLayout) header.findViewById(R.id.beix_layout);
        meigeInput = (XEditText) header.findViewById(R.id.meige_input);
        beixInput = (XEditText) header.findViewById(R.id.beix_input);
        startBeiInput = (XEditText) header.findViewById(R.id.start_bei_input);

        TopBeiWatcher meige = new TopBeiWatcher();
        meige.setTag(MEIGE_INPUT_TAG);
        TopBeiWatcher multiple = new TopBeiWatcher();
        multiple.setTag(BEI_MULTIPLE_INPUT_TAG);
        TopBeiWatcher startBeishu = new TopBeiWatcher();
        startBeishu.setTag(START_BEISHU_INPUT_TAG);

        meigeInput.addTextChangedListener(meige);
        beixInput.addTextChangedListener(multiple);
        startBeiInput.addTextChangedListener(startBeishu);
        jianQihao.setOnClickListener(this);
        addQihao.setOnClickListener(this);
        touzhuBtn.setOnClickListener(this);

        String moneyString = decimalFormat.format(0);
        totalMoneyTV.setText(String.format(getString(
                R.string.zuihao_total_money_format), moneyString));
        totalQishuTV.setText(String.format(getString(R.string.zuihao_total_qishu_format), initQishu));

        zuihaoInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String qishu = zuihaoInput.getText().toString().trim();
                if (Utils.isEmptyString(qishu)) {
                    showToast(R.string.please_input_qihao);
                    return false;
                }
                if (!Utils.isNumeric(qishu)) {
                    showToast(R.string.please_input_correct_qihao_format);
                    return false;
                }
                int qishuInt = Integer.parseInt(qishu);
                //判断输入的追号期数是否超过了可追的最大期数
                if (Integer.parseInt(qishu) > maxQishu) {
                    showToast(String.format(getString(R.string.sorry_when_qihao_limit), maxQishu));
                    qishuInt = maxQishu;
                    zuihaoInput.setText(String.valueOf(qishuInt));
                }
                TouzhuThreadPool.getInstance().addTask(new CalcZuihaoDatas(qihaos, qishuInt));
                return false;
            }
        });

        zuihaoInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String qishu = s.toString();


                if (Utils.isEmptyString(qishu)) {
                    showToast(R.string.please_input_qihao);
                    return;
                }

                if (qishu.startsWith("0")) {
                    zuihaoInput.setText("1");
                    qishu = "1";
                }
//                if (!Utils.isNumeric(qishu)) {
//                    showToast(R.string.please_input_correct_qihao_format);
//                    return ;
//                }
                int qishuInt = Integer.parseInt(qishu);
                //判断输入的追号期数是否超过了可追的最大期数
                if (Integer.parseInt(qishu) > maxQishu) {
                    showToast(String.format(getString(R.string.sorry_when_qihao_limit), maxQishu));
                    qishuInt = maxQishu;
                    zuihaoInput.setText(String.valueOf(qishuInt));
                }
                TouzhuThreadPool.getInstance().addTask(new CalcZuihaoDatas(qihaos, qishuInt));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listView.addHeaderView(header);
        //获取所有可追期号
        actionGetValidQishu();
    }

    //获取可用期数
    private void actionGetValidQishu() {
        StringBuilder qiHaoUrl = new StringBuilder();
        qiHaoUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ZUIHAO_QISHU_URL);
        qiHaoUrl.append("?lotCode=").append(cpBianma);
        CrazyRequest<CrazyResult<ZuihaoQihaoWraper>> request = new AbstractCrazyRequest.Builder()
                .url(qiHaoUrl.toString())
                .seqnumber(ACQUIRE_VALID_QISHU)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.acquire_valid_qishuing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ZuihaoQihaoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private void actionRefreshQishu() {
        StringBuilder qiHaoUrl = new StringBuilder();
        qiHaoUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ZUIHAO_QISHU_URL);
        qiHaoUrl.append("?lotCode=").append(cpBianma);
        CrazyRequest<CrazyResult<ZuihaoQihaoWraper>> request = new AbstractCrazyRequest.Builder()
                .url(qiHaoUrl.toString())
                .seqnumber(REFRESH_QIHAO_WHEN_RECENT_QIHAO_INVALID_REQUEST)
                .headers(Urls.getHeader(this))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ZuihaoQihaoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
        startProgress();
    }


    //根据投注清单中传递过来的清单列表计算每期基本下注金额
    private float getTotalMoneyOfSelectedZhudang(String zhuDangJson) {

        if (Utils.isEmptyString(zhuDangJson)) {
            return 0;
        }

        Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {
        }.getType();
        List<OrderDataInfo> list = new Gson().fromJson(zhuDangJson, listType);
        float totalMoney = 0;
        if (list != null) {
            for (OrderDataInfo info : list) {
                double money = info.getMoney();
                totalMoney += money;
            }
        }
        return totalMoney;
    }

//    private List<ZuihaoListData> buildZuihaoDatas(long initQihao,int initBeishu,int qiShu) {
//        List<ZuihaoListData> listData = new ArrayList<ZuihaoListData>();
//        totalTouzhuFee = 0;
//        for (int i=0;i<qiShu;i++) {
//            ZuihaoListData data = new ZuihaoListData();
//            data.setBeishu(initBeishu+i);
//            data.setTouZhuMoney(totalBaseMoney*(initBeishu+i));
//            data.setQihao(String.valueOf(initQihao+i));
//            listData.add(data);
//            totalTouzhuFee += data.getTouZhuMoney();
//        }
//        return listData;
//    }

    public static void createIntent(Context context, String zhuJsons, String cpBianma, String cpName,
                                    String playName, String playCode, String subPlayName,
                                    String subPlayCode, long duration) {
        Intent intent = new Intent(context, BraveZuiHaoActivity.class);
        intent.putExtra("zhudang", zhuJsons);
        intent.putExtra("cpBianma", cpBianma);
        intent.putExtra("cpName", cpName);
        intent.putExtra("playName", playName);
        intent.putExtra("playCode", playCode);
        intent.putExtra("subPlayName", subPlayName);
        intent.putExtra("subPlayCode", subPlayCode);
        intent.putExtra("duration", duration);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.intelligent_zuihao));
        tvRightText.setVisibility(View.VISIBLE);

        panBeiTV = (TextView) findViewById(R.id.fan_bei);
        tongBeiTV = (TextView) findViewById(R.id.same_bei);
        panBeiTV.setOnClickListener(this);
        tongBeiTV.setOnClickListener(this);
        panBeiBar = findViewById(R.id.left_bar);
        tongBeiBar = findViewById(R.id.right_bar);

        listView = (SwipeMenuListView) findViewById(R.id.cListview);
        touzhuBtn = (Button) findViewById(R.id.touzhu_btn);
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        actionSlideDetail(position);
                        break;
                    case 1:
                        actionSlideDelete(position);
                        zuihaoInput.setText(String.valueOf(zuihaoDatas.size()));
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    private final class TopBeiWatcher implements TextWatcher {

        int tag;

        public void setTag(int tag) {
            this.tag = tag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputStr = "";
            if (tag == MEIGE_INPUT_TAG) {
                inputStr = meigeInput.getText().toString().trim();
            } else if (tag == BEI_MULTIPLE_INPUT_TAG) {
                inputStr = beixInput.getText().toString().trim();
            } else if (tag == START_BEISHU_INPUT_TAG) {
                inputStr = startBeiInput.getText().toString().trim();
            }
            if (Utils.isEmptyString(inputStr)) {
//                showToast(R.string.please_input_number);
                return;
            }
            if (!Utils.isNumeric(inputStr)) {
                showToast(R.string.please_input_correct_qihao_format2);
                return;
            }
            int strWithIntegerFormat = Integer.parseInt(inputStr);
            if (tag == MEIGE_INPUT_TAG) {
                initMeigeBeishu = strWithIntegerFormat;
            } else if (tag == BEI_MULTIPLE_INPUT_TAG) {
                beishuMultiple = strWithIntegerFormat;
            } else if (tag == START_BEISHU_INPUT_TAG) {
                initStartBeishu = strWithIntegerFormat;
            }
            String currentQishu = zuihaoInput.getText().toString().trim();
            int qishu = 0;
            if (!Utils.isEmptyString(currentQishu)) {
                qishu = Integer.parseInt(currentQishu);
            }
            TouzhuThreadPool.getInstance().addTask(new CalcZuihaoDatas(qihaos, qishu));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void actionSlideDelete(int position) {
        if (zuihaoDatas != null && !zuihaoDatas.isEmpty()) {
            zuihaoDatas.remove(position);
            refreshList();
        }
    }

    private void actionSlideDetail(int position) {
        if (zuihaoDatas != null && !zuihaoDatas.isEmpty()) {
            ZuihaoListData zd = zuihaoDatas.get(position);
            String json = getIntent().getStringExtra("zhudang");
            showDetailWindow(json, zd.getBeishu(), zd.getQihao(), zd.getTouZhuMoney());
        }
    }

    private void showDetailWindow(String dangJsons, int beishu, String qihao, float totalMoney) {

        if (popWindow == null) {
            popWindow = new ZhudangDetailPopWindow(this);
        }
        popWindow.setData(dangJsons, beishu, qihao, totalMoney);
        if (!popWindow.isShowing()) {
            popWindow.showWindow(findViewById(R.id.item));
        }
    }

    private void refreshList() {

        if (zhuDangAdapter != null) {
            totalTouzhuFee = 0;
            zhuDangAdapter.notifyDataSetChanged();
            for (ZuihaoListData zd : zuihaoDatas) {
                totalTouzhuFee += zd.getTouZhuMoney();
            }
        }
        totalQishuTV.setText(String.format(getString(
                R.string.zuihao_total_qishu_format), zuihaoDatas.size()));
        String moneyString = decimalFormat.format(totalTouzhuFee);
        totalMoneyTV.setText(String.format(getString(
                R.string.zuihao_total_money_format), moneyString));

    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem detailItem = new SwipeMenuItem(BraveZuiHaoActivity.this);
            detailItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
            detailItem.setWidth(Utils.dip2px(BraveZuiHaoActivity.this, 90));
            detailItem.setTitle(R.string.detail_string);
            detailItem.setTitleSize(18);
            detailItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(detailItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setBackground(R.color.colorPrimary);
            deleteItem.setWidth(Utils.dip2px(BraveZuiHaoActivity.this, 90));
            deleteItem.setTitle(R.string.delete_string);
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(18);
//            deleteItem.setIcon(R.drawable.ic_delete);
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.jian:
                String jian = zuihaoInput.getText().toString().trim();
                if (Utils.isEmptyString(jian)) {
                    showToast(getString(R.string.please_input_qihao));
                    return;
                }
                if (!Utils.isNumeric(jian)) {
                    showToast(getString(R.string.please_input_correct_qihao_format));
                    return;
                }
                int bsInt = Integer.parseInt(jian);
                if (bsInt == 1) {//当前是第一期不需要再刷新页面
                    return;
                }
                bsInt--;
                zuihaoInput.setText(String.valueOf(bsInt));
                if (bsInt >= zuihaoDatas.size()) {
                    return;
                }
                zuihaoDatas.remove(zuihaoDatas.size() - 1);
                refreshList();
                break;
            case R.id.add:
                String add = zuihaoInput.getText().toString().trim();
                if (Utils.isEmptyString(add)) {
                    showToast(getString(R.string.please_input_qihao));
                    return;
                }
                if (!Utils.isNumeric(add)) {
                    showToast(getString(R.string.please_input_correct_qihao_format));
                    return;
                }
                int addInt = Integer.parseInt(add);
                addInt++;
                if (addInt > maxQishu) {
                    showToast(String.format(getString(R.string.sorry_when_qihao_limit), maxQishu));
                    return;
                }
                zuihaoInput.setText(String.valueOf(addInt));
                if (qihaos.get(addInt - 1) != null) {
                    long addQiHao = qihaos.get(addInt - 1);
                    ZuihaoListData addData = new ZuihaoListData();
                    int lastIndex = zuihaoDatas.size() - 1;
                    if (lastIndex + 1 < maxQishu) {
                        addData.setBeishu(calcBeishuWithMultipleMode(lastIndex + 1));
                        addData.setTouZhuMoney(totalBaseMoney * (addData.getBeishu()));
                        addData.setQihao(String.valueOf(addQiHao));
                        zuihaoDatas.add(addData);
                        refreshList();
                    }
                }
                break;
            case R.id.touzhu_btn:
                boolean hasAsk = YiboPreference.instance(this).hasTouzhuAsk();
                if (!hasAsk) {
                    showAskForTouzhuDialog();
                    return;
                }
                actionTouzhu();
                break;
            case R.id.right_text:
                showFuncMenu();
                break;
            case R.id.fan_bei:
                switchBeiMode(true);
                break;
            case R.id.same_bei:
                switchBeiMode(false);
                break;
            default:
                break;
        }
    }

    private void switchBeiMode(boolean isFanbeiMode) {
        if (isFanbeiMode) {
            beixLayout.setVisibility(View.VISIBLE);
            meigeInput.setEnabled(true);
            panBeiBar.setVisibility(View.VISIBLE);
            tongBeiBar.setVisibility(View.INVISIBLE);
            currentMode = FAN_BEI_MODE;
        } else {
            beixLayout.setVisibility(View.GONE);
            meigeInput.setEnabled(false);
            panBeiBar.setVisibility(View.INVISIBLE);
            tongBeiBar.setVisibility(View.VISIBLE);
            currentMode = TONG_BEI_MODE;
        }
//        initMeigeBeishu = 1;
//        beishuMultiple = 2;
//        initStartBeishu = 1;
        //切换翻倍模式时，刷新列表
        String currentQishu = zuihaoInput.getText().toString().trim();
        int qishu = 0;
        if (!Utils.isEmptyString(currentQishu)) {
            qishu = Integer.parseInt(currentQishu);
        }
        TouzhuThreadPool.getInstance().addTask(new CalcZuihaoDatas(qihaos, qishu));
    }


    /**
     * 显示右上角更多菜单
     */
    private void showFuncMenu() {

        String[] arrays = getResources().getStringArray(R.array.order_cart_funcs);
        PopupListMenu menu = new PopupListMenu(this, arrays);
        menu.setBackground(R.drawable.caipiao_item_bg);
        menu.setDimEffect(true);
        menu.setAnimation(true);
        menu.setOnItemClickListener(new PopupListMenu.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        RecordsActivity.createIntent(BraveZuiHaoActivity.this, "",
                                Constant.CAIPIAO_RECORD_STATUS,
                                cpBianma);
                        break;
                    case 1:
                        BetProfileActivity.createIntent(BraveZuiHaoActivity.this);
                        break;
                }
            }
        });
        menu.show(rightLayout, 0, 5);
    }

    private String wrapBets(String token) {
        if (zuihaoDatas.isEmpty()) {
            showToast(R.string.noorder_please_touzhu_first);
            return null;
        }
        //构造下注POST数据
        try {

            JSONObject content = new JSONObject();
            //准备下注订单信息
            if (!Utils.isEmptyString(baseOrdersJson)) {
                JSONArray betsArray = new JSONArray();
                Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {
                }.getType();
                List<OrderDataInfo> list = new Gson().fromJson(baseOrdersJson, listType);
                if (list != null) {
                    for (OrderDataInfo order : list) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(cpBianma).append("|");
                        sb.append(order.getSubPlayCode()).append("|");
                        sb.append(UsualMethod.convertPostMode(order.getMode())).append("|");
                        sb.append(order.getBeishu()).append("|");
                        sb.append(order.getNumbers());
                        betsArray.put(sb.toString());
                    }
                }
                content.put("bets", betsArray);
            }

            content.put("lotCode", cpBianma);
            content.put("qiHao", "");
            content.put("token", token);
            //准备追号数据
            if (zuihaoDatas != null && !zuihaoDatas.isEmpty()) {
                JSONArray traceArray = new JSONArray();
                traceArray.put(stopZuihaoCheckBox.isChecked() ? 1 : 0);
                List<ZuihaoListData> betDatas = new ArrayList<>();
                for (int i = 0; i < zuihaoDatas.size(); i++) {
                    ZuihaoListData data = zuihaoDatas.get(i);
                    if (data == null) {
                        continue;
                    }
                    boolean isInvalid = false;
                    //投注时过滤过期的期号
                    for (String invalid : invalidQihaos) {
                        if (data.getQihao().equals(invalid)) {
                            isInvalid = true;
                            break;
                        }
                    }
                    if (isInvalid) {
                        continue;
                    }
                    betDatas.add(data);
                }
                for (int i = 0; i < betDatas.size(); i++) {
                    ZuihaoListData data = betDatas.get(i);
                    if (data == null) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(data.getQihao()).append("|").append(i + 1);
                    traceArray.put(data.getQihao() + "|" + data.getBeishu());
                }
                content.put("tractData", traceArray);
            }

            String postJson = content.toString();
            Utils.LOG(TAG, "the post zuihao touzhu data = " + postJson);
            return postJson;
        } catch (Exception e) {
            e.printStackTrace();
            showToast("注单信息有误，请重新下注");
            return null;
        }
    }

    private void postBets(String data){
        ApiParams params = new ApiParams();
        params.put("data", data);
        HttpUtil.postForm(this, Urls.DO_BETS_URL, params, true, getString(R.string.bet_ongoing), new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if(result.isSuccess()){
                    YiboPreference.instance(BraveZuiHaoActivity.this).setToken(result.getAccessToken());
                    showToast(R.string.dobets_success);
                    //清除过期追号期数列表
                    invalidQihaos.clear();
                    //下注成功后，退出追号页面,清除所购的注单信息
                    Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {
                    }.getType();
                    List<OrderDataInfo> list = new Gson().fromJson(baseOrdersJson, listType);
                    for (OrderDataInfo info : list) {
                        DatabaseUtils.getInstance(BraveZuiHaoActivity.this).deleteOrder(info.getOrderno());
                    }
                    saveCurrentLotData();

                    boolean isSimpleStyle = UsualMethod.getConfigFromJson(BraveZuiHaoActivity.this).getBet_page_style().equalsIgnoreCase("v1");
                    if (isSimpleStyle) {
                        TouzhuSimpleActivity.createIntent(BraveZuiHaoActivity.this);
                    } else {
                        TouzhuActivity.createIntent(BraveZuiHaoActivity.this);
                    }
                    finish();
                }else {
                    showToast(TextUtils.isEmpty(result.getMsg()) ? "下注时出现错误，请重新下注" : result.getMsg());
                    if (result.getMsg().contains("登陆超时")) {
                        UsualMethod.loginWhenSessionInvalid(BraveZuiHaoActivity.this);
                    }
                }
            }
        });
    }

    private void actionTouzhu() {
        if (!invalidQihaos.isEmpty()) {
            String qihaos = "";
            for (String qihao : invalidQihaos) {
                qihaos += qihao;
                qihaos += ",";
            }
            if (!Utils.isEmptyString(qihaos)) {
                qihaos = qihaos.substring(0, qihaos.length() - 1);
            }
            toastTimeoutQihaoDialog(qihaos);
        } else {
            CrazyRequest tokenRequest = UsualMethod.buildBetsTokenRequest(this, TOKEN_BETS_REQUEST);
            RequestManager.getInstance().startRequest(this, tokenRequest);
        }
    }

    /**
     * 提交追号时的过期期数友情提示
     *
     * @param qihao
     */
    private void toastTimeoutQihaoDialog(String qihao) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(2);
        ccd.setContent(String.format("第%s期已经过期，下注时将不追这期，是否继续提交追号？", qihao));
        ccd.setToastShow(false);
        ccd.setToastBtnText("不再提示");
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
                CrazyRequest tokenRequest = UsualMethod.buildBetsTokenRequest(BraveZuiHaoActivity.this,
                        TOKEN_BETS_REQUEST);
                RequestManager.getInstance().startRequest(BraveZuiHaoActivity.this, tokenRequest);
            }
        });
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private void showAskForTouzhuDialog() {

        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(2);
        ccd.setContent(getString(R.string.touzhu_toast_string));
        ccd.setToastShow(true);
        ccd.setToastBtnText("不再提示");
        ccd.setOnToastBtnClick(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                boolean hasAsk = YiboPreference.instance(BraveZuiHaoActivity.this).hasTouzhuAsk();
                YiboPreference.instance(BraveZuiHaoActivity.this).setTouzhuAsk(!hasAsk);
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
                actionTouzhu();
            }
        });
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == ACQUIRE_VALID_QISHU) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_qishu_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_qishu_fail);
                return;
            }
            Object regResult = result.result;
            ZuihaoQihaoWraper reg = (ZuihaoQihaoWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.dobets_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            qihaos = reg.getContent();
            if (qihaos != null && !qihaos.isEmpty()) {
                maxQishu = qihaos.size();
                int firstQishu = maxQishu >= initQishu ? initQishu : maxQishu;
                //赋值总期数输入框为初始期数，即initQishu
                zuihaoInput.setText(String.valueOf(firstQishu));
                TouzhuThreadPool.getInstance().addTask(new CalcZuihaoDatas(qihaos, firstQishu));
            }
            //获取当前最近期号，及当前期号离投注结束还有多长时间
            UsualMethod.getCountDownByCpcode(this, cpBianma, CURRENT_QIHAO_REQUEST);
        } else if (action == REFRESH_QIHAO_WHEN_RECENT_QIHAO_INVALID_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
//                showToast(R.string.acquire_qishu_fail);
                return;
            }
            if (!result.crazySuccess) {
//                showToast(R.string.acquire_qishu_fail);
                return;
            }
            Object regResult = result.result;
            ZuihaoQihaoWraper reg = (ZuihaoQihaoWraper) regResult;
            if (!reg.isSuccess()) {
//                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():getString(R.string.acquire_qishu_fail));
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            qihaos = reg.getContent();
            if (qihaos != null && !qihaos.isEmpty() &&
                    zuihaoDatas != null && !zuihaoDatas.isEmpty()) {
                maxQishu = qihaos.size();
                int showQishu = zuihaoDatas.size();
                showQishu = maxQishu >= showQishu ? showQishu : maxQishu;
                TouzhuThreadPool.getInstance().addTask(new RefreshZuihaoDatas(qihaos, zuihaoDatas, showQishu));
                //获取当前最近期号，及当前期号离投注结束还有多长时间
                UsualMethod.getCountDownByCpcode(this, cpBianma, CURRENT_QIHAO_REQUEST);
            }
        } else if (action == TOKEN_BETS_REQUEST) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.dobets_token_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.dobets_token_fail);
                return;
            }
            Object regResult = result.result;
            BetToken reg = (BetToken) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.dobets_token_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //获取下注口令后开始下注
            String data = wrapBets(reg.getContent());
            if(!TextUtils.isEmpty(data))
                postBets(data);
        } else if (action == CURRENT_QIHAO_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            LocCountDownWraper reg = (LocCountDownWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新当前这期离结束投注的倒计时显示
            tvSecondTitle.setVisibility(View.VISIBLE);
            updateCurrenQihaoCountDown(reg.getContent());
        }

    }

    private void saveCurrentLotData() {
        SavedGameData data = new SavedGameData();
        data.setGameModuleCode(SavedGameData.LOT_GAME_MODULE);
        data.setAddTime(System.currentTimeMillis());
        data.setLotName(getIntent().getStringExtra("cpName"));
        data.setLotCode(cpBianma);
        data.setLotType(getIntent().getStringExtra("cpTypeCode"));
        data.setPlayName(getIntent().getStringExtra("playName"));
        data.setPlayCode(getIntent().getStringExtra("playCode"));
        data.setSubPlayName(getIntent().getStringExtra("subPlayName"));
        data.setDuration(getIntent().getLongExtra("duration", 0));
        data.setSubPlayCode(getIntent().getStringExtra("subPlayCode"));
        data.setCpVersion(getIntent().getStringExtra("cpVersion"));
        UsualMethod.localeGameData(this, data);
    }

    /**
     * 更新当前最近期数离投注结束倒计时时间
     *
     * @param countDown
     */
    private void updateCurrenQihaoCountDown(CountDown countDown) {
        if (countDown == null) {
            return;
        }
        //创建开奖周期倒计时器d
        long serverTime = countDown.getServerTime();
        long activeTime = countDown.getActiveTime();
        long duration = Math.abs(activeTime - serverTime);
        Utils.LOG(TAG, "current qihao open duration = " + countDown.getQiHao() + "," + duration);
        if (endlineTouzhuTimer == null) {
            createEndlineTouzhuTimer(duration, countDown.getQiHao());
        }
        //开始离投注结束时间倒计时
        endlineTouzhuTimer.start();
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
                long time = millisUntilFinished;
                tvSecondTitle.setText(String.format(getString(R.string.kaijian_deadline_format),
                        String.valueOf(currentQihao), Utils.int2Time(time)));
            }

            public void onFinish() {
                endlineTouzhuTimer = null;//置空当前期号倒计时器
                tvSecondTitle.setVisibility(View.GONE);
                invalidQihaos.add(currentQihao);
                //获取当前最近期号，及当前期号离投注结束还有多长时间
                UsualMethod.getCountDownByCpcode(BraveZuiHaoActivity.this, cpBianma, CURRENT_QIHAO_REQUEST);
                //有最近期号过期时刷新期号列表
//                actionRefreshQishu();
            }
        };
    }

    //去除最近失效的那一期追号列表项，并在列表尾追加一期新的期号
//    private void trimHeadAndAppendQihaoWhenRecentQihaoGone() {
//        if (zuihaoDatas != null && !zuihaoDatas.isEmpty()) {
//            int zuihaoSize = zuihaoDatas.size();
//            zuihaoDatas.remove(0);
//
//            zuihaoSize++;
//            if (zuihaoSize > maxQishu) {
//                showToast(String.format(getString(R.string.sorry_when_qihao_limit),maxQishu));
//                return;
//            }
//            if(qihaos.get(zuihaoSize - 1) != null){
//                long addQiHao = qihaos.get(zuihaoSize - 1);
//                ZuihaoListData data = new ZuihaoListData();
//                data.setBeishu(initBeishu + zuihaoSize - 1);
//                data.setTouZhuMoney(totalBaseMoney*(initBeishu + zuihaoSize - 1));
//                data.setQihao(String.valueOf(addQiHao));
//                zuihaoDatas.add(data);
//            }
//            refreshList();
//        }
//
//        if (qihaos != null && !qihaos.isEmpty()) {
//            maxQishu = qihaos.size();
//            int firstQishu = maxQishu >= initQishu ? initQishu : maxQishu;
//            //赋值总期数输入框为初始期数，即firstQishu
//            TouzhuThreadPool.getInstance().addTask(new CalcZuihaoDatas(qihaos,firstQishu));
//        }
//
//    }

    /**
     * 异步构造追号列表数据
     */
    private final class CalcZuihaoDatas implements Runnable {

        List<Long> qishus;
        int showQishu;

        CalcZuihaoDatas(List<Long> qishus, int showQishu) {
            this.qishus = qishus;
            this.showQishu = showQishu;
        }

        @Override
        public void run() {
            //根据后台返回的可用期数及当前要显示的期数，构造追号列表显示数据
            List<ZuihaoListData> list = new ArrayList<>();
            float currentTouzhuFee = 0;
            for (int i = 0; i < qishus.size(); i++) {
                if (i < showQishu) {
                    ZuihaoListData zuihaoListData = new ZuihaoListData();
                    int beishu = calcBeishuWithMultipleMode(i);
                    zuihaoListData.setBeishu(beishu);
                    zuihaoListData.setTouZhuMoney(totalBaseMoney * beishu);
                    zuihaoListData.setQihao(String.valueOf(qishus.get(i)));
                    list.add(zuihaoListData);
                    currentTouzhuFee += zuihaoListData.getTouZhuMoney();
                }
            }
            Message message = calcHandler.obtainMessage(UPDATE_LISTVIEW, list);
            calcHandler.sendMessage(message);
            Message fee = calcHandler.obtainMessage(UPDATE_TOTALFEE, currentTouzhuFee);
            calcHandler.sendMessage(fee);
        }
    }

    /**
     * 根据翻倍模式计数每期的倍数
     *
     * @param index
     * @return
     */
    private int calcBeishuWithMultipleMode(int index) {
        int beishu = 0;
        if (currentMode == FAN_BEI_MODE) {
            if (index < initMeigeBeishu) {
                beishu = initStartBeishu;
            } else {
                int w = index / initMeigeBeishu;
                double result = Math.pow(beishuMultiple, w);
                result = result * initStartBeishu;
                Utils.LOG(TAG, "result ==== " + result);
                beishu = (int) result;
            }
        } else {
            beishu = initStartBeishu;
        }
        Utils.LOG(TAG, "per,start,multi = " + initMeigeBeishu + "," + initStartBeishu + "," + beishuMultiple);
        Utils.LOG(TAG, "beishu = " + beishu);
        return beishu;
    }


    /**
     * 刷新追号列表数据
     */
    private final class RefreshZuihaoDatas implements Runnable {

        List<Long> qishus;
        int showQishu;
        List<ZuihaoListData> oldDatas = null;

        RefreshZuihaoDatas(List<Long> qishus, List<ZuihaoListData> oldData, int showQishu) {
            this.qishus = qishus;
            this.showQishu = showQishu;
            this.oldDatas = oldData;
        }

        @Override
        public void run() {
            //根据后台返回的可用期数及当前要显示的期数，构造追号列表显示数据
            List<ZuihaoListData> list = new ArrayList<>();
            float currentTouzhuFee = 0;
            if (oldDatas != null && !oldDatas.isEmpty()) {
                oldDatas.remove(0);
                int leftOldDataSize = oldDatas.size();
                for (int i = 0; i < qishus.size(); i++) {
                    if (i < leftOldDataSize) {
                        ZuihaoListData oldData = oldDatas.get(i);
                        list.add(oldData);
                        currentTouzhuFee += oldData.getTouZhuMoney();
                    } else if (i >= leftOldDataSize && i < showQishu) {
                        ZuihaoListData zuihaoListData = new ZuihaoListData();
                        //提取旧追号数据中最后一期倍数+1做为新追加的一期的倍数
                        zuihaoListData.setBeishu(oldDatas.get(oldDatas.size() - 1).getBeishu() + 1);
                        zuihaoListData.setTouZhuMoney(totalBaseMoney * (zuihaoListData.getBeishu()));
                        zuihaoListData.setQihao(String.valueOf(qishus.get(i)));
                        list.add(zuihaoListData);
                        currentTouzhuFee += zuihaoListData.getTouZhuMoney();
                    }
                }
            }
            Message message = calcHandler.obtainMessage(UPDATE_LISTVIEW, list);
            calcHandler.sendMessage(message);
            Message fee = calcHandler.obtainMessage(UPDATE_TOTALFEE, currentTouzhuFee);
            calcHandler.sendMessage(fee);
        }
    }

    //线程异步handler
    private static class CalcHandler extends android.os.Handler {
        private WeakReference<BraveZuiHaoActivity> mReference;
        private BraveZuiHaoActivity mActivity;

        public CalcHandler(BraveZuiHaoActivity activity) {
            mReference = new WeakReference<BraveZuiHaoActivity>(activity);
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
                case UPDATE_LISTVIEW:
                    List<ZuihaoListData> data = (List<ZuihaoListData>) msg.obj;
                    if (data != null) {
                        mActivity.zuihaoDatas.clear();
                        mActivity.zuihaoDatas.addAll(data);
                        mActivity.totalQishuTV.setText(String.format(mActivity.getString(
                                R.string.zuihao_total_qishu_format), data.size()));
                    }
                    mActivity.zhuDangAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_TOTALFEE:
                    //显示总金额
                    float currentTotalFee = (float) msg.obj;
                    String moneyString = mActivity.decimalFormat.format(currentTotalFee);
                    mActivity.totalMoneyTV.setText(String.format(mActivity.getString(
                            R.string.zuihao_total_money_format), moneyString));
                    break;
            }
        }
    }

    private final class ZhuDangAdapter extends LAdapter<ZuihaoListData> {

        public ZhuDangAdapter(Context mContext, List<ZuihaoListData> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final ZuihaoListData item) {

            final XEditText beishuInput = holder.getView(R.id.beishu_input);
            beishuInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                    String beishu = beishuInput.getText().toString().trim();
                    if (Utils.isEmptyString(beishu)) {
                        showToast(R.string.input_beishu);
                        return false;
                    }
                    if (!Utils.isNumeric(beishu)) {
                        showToast(R.string.please_input_correct_beishu_format);
                        return false;
                    }

                    ZuihaoListData data = new ZuihaoListData();
                    data.setBeishu(Integer.parseInt(beishu));
                    data.setTouZhuMoney(totalBaseMoney * Integer.parseInt(beishu));
                    data.setQihao(item.getQihao());
                    zuihaoDatas.set(position, data);
                    refreshList();
                    return false;
                }
            });

//            beishuInput.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    Utils.LOG(TAG,"kkkkkkkkkkkkkkkkkkkk s ="+s.toString());
//                    String beishu = s.toString();
//                    if (Utils.isEmptyString(beishu)) {
////                        showToast(R.string.input_beishu);
//                        return;
//                    }
//                    if (!Utils.isNumeric(beishu)) {
//                        showToast(R.string.please_input_correct_beishu_format);
//                        return;
//                    }
//                    ZuihaoListData data = new ZuihaoListData();
//                    data.setBeishu(Integer.parseInt(beishu));
//                    data.setTouZhuMoney(totalBaseMoney*Integer.parseInt(beishu));
//                    data.setQihao(item.getQihao());
//                    if (position < zuihaoDatas.size()) {
//                        zuihaoDatas.set(position, data);
//                        refreshList();
//                    }
//                    return;
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });

            TextView jian = holder.getView(R.id.jian);
            TextView add = holder.getView(R.id.add);
            TextView qiHao = holder.getView(R.id.qihao);
            TextView money = holder.getView(R.id.money);
            TextView totalMoney = holder.getView(R.id.total_touzhu_money);

            jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionJianAdd(beishuInput, item, position, false);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionJianAdd(beishuInput, item, position, true);
                }
            });

            beishuInput.setText(String.valueOf(item.getBeishu()));
            qiHao.setText(String.format(getString(R.string.qishu_format), item.getQihao()));
            String xiazhuFee = decimalFormat.format(item.getTouZhuMoney());
            money.setText(String.format(getString(R.string.money_zuihao_format), xiazhuFee));
            String leijiFee = decimalFormat.format(item.getLeijiMoney());
            totalMoney.setText(String.format(getString(R.string.zuihao_lieji_money_format), leijiFee));
            beishuInput.setText(String.valueOf(item.getBeishu()));

        }

        private void actionJianAdd(XEditText input, ZuihaoListData item, int position, boolean isAdd) {
            String beishu = input.getText().toString().trim();
            if (Utils.isEmptyString(beishu)) {
                showToast(getString(R.string.input_beishu));
                return;
            }
            if (!Utils.isNumeric(beishu)) {
                showToast(getString(R.string.input_correct_beishu_format));
                return;
            }
            int bsInt = Integer.parseInt(beishu);
            if (isAdd) {
                bsInt++;
            } else {
                if (bsInt == 1) {
                    return;
                }
                bsInt--;
                if (bsInt <= 0) {
                    bsInt = 1;
                }
            }
            input.setText(String.valueOf(bsInt));
            //更新列表数据
            ZuihaoListData data = new ZuihaoListData();
            data.setQihao(item.getQihao());
            data.setTouZhuMoney(totalBaseMoney * bsInt);
//            data.setLeijiMoney(item.getLeijiMoney());
            data.setBeishu(bsInt);
            zuihaoDatas.set(position, data);
            refreshList();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zuihaoDatas.clear();
        if (endlineTouzhuTimer != null) {
            endlineTouzhuTimer.cancel();
            endlineTouzhuTimer = null;
        }
    }
}
