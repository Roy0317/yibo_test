package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.JieBaoZhuShuCalculator;
import com.yibo.yiboapp.data.LotteryPlayLogic;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BetToken;
import com.yibo.yiboapp.entify.DoBetWraper;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.MyToast;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URLEncoder;
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

public class TouzhuOrderActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    XListView recordList;
    EmptyListView empty;
    DataAdapter recordAdapter;
    List<OrderDataInfo> listDatas;
    TextView zhushuMoneyTV;
    TextView againTV;
    TextView randomTV;
    Button zuihaoBtn;
    Button touzhBtn;

    String cpName = "";
    long duration;
    String cpVersion = "";
    String cpTypeCode = "";
    String playCode;
    String playName;
    String subPlayCode;
    String subPlayName = "";
    String cpBianma = "";

    FrontHandler frontHandler;
    public static final int TOKEN_BETS_REQUEST = 0x01;
    public static final int REFRESH_AFTER_DELETE = 0x01;
    public static final int UPDATE_LISTVIEW = 0x02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touzhu_order);
        initView();
        cpVersion = getIntent().getStringExtra("cpVersion");
        cpName = getIntent().getStringExtra("cpName");
        duration = getIntent().getLongExtra("duration", 0);
        String orderInfo = getIntent().getStringExtra("order");
        OrderDataInfo data = new Gson().fromJson(orderInfo, OrderDataInfo.class);
        if (data != null) {
            cpTypeCode = data.getLottype();
            playCode = data.getPlayCode();
            playName = data.getPlayName();
            subPlayCode = data.getSubPlayCode();
            subPlayName = data.getSubPlayName();
            cpBianma = data.getLotcode();
        }
        toggleZuihaoEnter();
        listDatas = DatabaseUtils.getInstance(this).getCartOrders(cpBianma);
        recordAdapter = new DataAdapter(this, listDatas, R.layout.goucai_list_item);
        recordList.setAdapter(recordAdapter);
        updateBottom();
    }

    public static void createIntent(Context context, String order, String cpVersion,
                                    String cpName, long duration) {
        Intent intent = new Intent(context, TouzhuOrderActivity.class);
        intent.putExtra("order", order);
        intent.putExtra("cpVersion", cpVersion);
        intent.putExtra("cpName", cpName);
        intent.putExtra("duration", duration);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("购彩列表");
        tvBackText.setVisibility(View.VISIBLE);
        recordList = (XListView) findViewById(R.id.xlistview);
        recordList.setPullLoadEnable(false);
        recordList.setPullRefreshEnable(false);
        zuihaoBtn = (Button) findViewById(R.id.zuihao_btn);
        touzhBtn = (Button) findViewById(R.id.confirm);
        zuihaoBtn.setOnClickListener(this);
        touzhBtn.setOnClickListener(this);
        empty = (EmptyListView) findViewById(R.id.empty);
        zhushuMoneyTV = (TextView) findViewById(R.id.zhushu_money_txt);
        againTV = (TextView) findViewById(R.id.again_touzhu);
        randomTV = (TextView) findViewById(R.id.random_touzhu);
        againTV.setOnClickListener(this);
        randomTV.setOnClickListener(this);

    }

    //追号开关是否打开
    private void toggleZuihaoEnter() {
        String sysConfig = YiboPreference.instance(this).getSysConfig();
        if (!Utils.isEmptyString(sysConfig)) {
            SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
            if (!Utils.isEmptyString(sc.getLottery_order_chase_switch()) && sc.getLottery_order_chase_switch().equals("on")) {
                zuihaoBtn.setVisibility(View.VISIBLE);
                return;
            }
        }
        zuihaoBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.again_touzhu) {
            finish();
        } else if (v.getId() == R.id.random_touzhu) {
            actionRandomTouzhu();
        } else if (v.getId() == R.id.zuihao_btn) {
            actionZuihao();
        } else if (v.getId() == R.id.confirm) {
            showConfirmBetDialog();
//            actionTouzhu();
        }
    }

    private void showConfirmBetDialog() {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(2);
        String content = "是否提交下注？";
        ccd.setContent(content);
        ccd.setTitle("温馨提示");
        ccd.setLeftBtnText("取消");
        ccd.setRightBtnText("确定");
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                actionTouzhu();
                ccd.dismiss();
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    //查看投注记录
    private void actionTouzhuRecord(String cpBianma) {
        RecordsActivityNew.createIntent(this, cpName,
                UsualMethod.isSixMark(this,cpBianma) ? Constant.LHC_RECORD_STATUS :
                        Constant.CAIPIAO_RECORD_STATUS, cpBianma);
    }

    private void showAfterBetDialog() {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(2);
        String content = "下注成功！";
        ccd.setContent(content);
        ccd.setTitle("温馨提示");
        ccd.setLeftBtnText("查看记录");
        ccd.setRightBtnText("继续下注");
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                actionTouzhuRecord(cpBianma);
                ccd.dismiss();
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                finish();
                ccd.dismiss();
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private void actionZuihao() {
        if (listDatas.isEmpty()) {
            showToast("没有追号注单，请先下注！");
            return;
        }
        List<OrderDataInfo> data = DatabaseUtils.getInstance(this).getCartOrders(cpBianma);
        if (data == null || data.isEmpty()) {
            showToast(R.string.please_touzhu_first);
            return;
        }
        Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {
        }.getType();
        String zhuJsons = new Gson().toJson(data, listType);
        BraveZuiHaoActivity.createIntent(this, zhuJsons, cpBianma, cpName, playName, playCode, subPlayName, subPlayCode, duration);
    }

    //线程异步handler
    private static class FrontHandler extends android.os.Handler {
        private WeakReference<TouzhuOrderActivity> mReference;
        private TouzhuOrderActivity mActivity;

        public FrontHandler(TouzhuOrderActivity activity) {
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
                case REFRESH_AFTER_DELETE:
                    mActivity.recordAdapter.notifyDataSetChanged();
                    mActivity.updateBottom();
                    break;
                case UPDATE_LISTVIEW:
                    List<OrderDataInfo> data = (List<OrderDataInfo>) msg.obj;
                    if (data != null && !data.isEmpty()) {
                        mActivity.listDatas.clear();
                        mActivity.listDatas.addAll(data);
                        mActivity.recordAdapter.notifyDataSetChanged();
                        mActivity.updateBottom();
                    } else {
                        //没有注单记录时，显示空白页面
                        mActivity.recordList.setEmptyView(mActivity.empty);
                    }
                    break;
            }
        }
    }

    //投注动作
    private void actionTouzhu() {
        CrazyRequest tokenRequest = UsualMethod.buildBetsTokenRequest(this, TOKEN_BETS_REQUEST);
        RequestManager.getInstance().startRequest(this, tokenRequest);
    }

    private void saveCurrentLotData() {
        SavedGameData data = new SavedGameData();
        data.setGameModuleCode(SavedGameData.LOT_GAME_MODULE);
        data.setAddTime(System.currentTimeMillis());
        data.setLotName(cpName);
        data.setLotCode(cpBianma);
        data.setLotType(cpTypeCode);
        data.setPlayName(playName);
        data.setPlayCode(playCode);
        data.setSubPlayName(subPlayName);
        data.setSubPlayCode(subPlayCode);
        data.setDuration(duration);
        data.setCpVersion(cpVersion);
        Utils.LOG(TAG, "the duration === " + duration);
        UsualMethod.localeGameData(this, data);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == TOKEN_BETS_REQUEST) {
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
        }
    }

    //提交投注
    private String wrapBets(String token) {
        if (listDatas == null || listDatas.isEmpty()) {
            showToast(R.string.noorder_please_touzhu_first);
            return null;
        }
        //构造下注POST数据
        try {
            JSONArray betsArray = new JSONArray();
            for (OrderDataInfo order : listDatas) {
                StringBuilder sb = new StringBuilder();
                sb.append(cpBianma).append("|");
                sb.append(order.getSubPlayCode()).append("|");
                sb.append(UsualMethod.convertPostMode(order.getMode())).append("|");
                sb.append(order.getBeishu()).append("|");
                sb.append(order.getNumbers());
                betsArray.put(sb.toString());
            }

            JSONObject content = new JSONObject();
            content.put("lotCode", cpBianma);
            content.put("qiHao", "");
            content.put("token", token);
            content.put("bets", betsArray);
            String postJson = content.toString();
//            return URLEncoder.encode(postJson, "utf-8");
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
                    YiboPreference.instance(TouzhuOrderActivity.this).setToken(result.getAccessToken());
                    showToast(R.string.dobets_success);
                    saveCurrentLotData();
                    handleClearAllMenus();
                    showAfterBetDialog();
                }else {
                    showToast(TextUtils.isEmpty(result.getMsg()) ? "下注时出现错误，请重新下注" : result.getMsg());
                    if (result.getMsg().contains("登陆超时")) {
                        UsualMethod.loginWhenSessionInvalid(TouzhuOrderActivity.this);
                    }
                }
            }
        });
    }

    private void doClear() {
        for (OrderDataInfo info : listDatas) {
            DatabaseUtils.getInstance(this).deleteOrder(info.getOrderno());
        }
        listDatas.clear();
        //没有注单记录时，显示空白页面
        recordList.setEmptyView(empty);
    }

    private void handleClearAllMenus() {
        if (listDatas == null) {
            return;
        }
        if (listDatas.size() >= 15) {
            TouzhuThreadPool.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    doClear();
                    frontHandler.sendEmptyMessage(REFRESH_AFTER_DELETE);
                }
            });
        } else {
            doClear();
            recordAdapter.notifyDataSetChanged();
            updateBottom();
        }
    }

    private void actionRandomTouzhu() {

//        LotteryConstants lotterys = Lotterys.getLotterysByVersion(cpVersion);
//        LotteryData selectedLotteryData = null;
//
//        String selectedPlayName = null;
//        String selectedPlayCode = null;
//        String selectedSubPlayName = null;
//        String selectedSubPlayCode = null;

        //随机选择当前彩种下的大小玩法
//        if (lotterys != null) {
//            List<LotteryData> loc = lotterys.getLotterys();
//            for (LotteryData data : loc) {
//                if (data.getCzCode().equals(cpTypeCode)) {
//                    selectedLotteryData = data;
//                    break;
//                }
//            }
//            if (selectedLotteryData != null) {
//                List<PlayItem> rules = selectedLotteryData.getRules();
//                if (rules != null && !rules.isEmpty()) {
//                    Random r = new Random();
//                    int index = r.nextInt(rules.size());
//                    Utils.LOG(TAG,"the random play index = "+index);
//                    if (index == rules.size()) {
//                        index = rules.size() - 1;
//                    }
//                    PlayItem item = rules.get(index);
//                    if (item != null) {
//                        selectedPlayName = item.getName();
//                        selectedPlayCode = item.getCode();
//                    }
//                    List<SubPlayItem> subRules = item.getRules();
//                    if (subRules != null && !subRules.isEmpty()) {
//                        int subIndex = r.nextInt(subRules.size());
//                        Utils.LOG(TAG,"the random sub play index = "+subIndex);
//                        if (subIndex == subRules.size()) {
//                            subIndex = subRules.size() - 1;
//                        }
//                        SubPlayItem subItem = subRules.get(subIndex);
//                        if (subItem != null) {
//                            selectedSubPlayName = subItem.getName();
//                            selectedSubPlayCode = subItem.getCode();
//                        }
//                    }
//                }
//            }
//        }

//        Utils.LOG(TAG,"random select play code = "+selectedPlayCode+" sub play code = "
//                +selectedSubPlayCode);
        //计算投注号码
        String touzhuNumbers = LotteryPlayLogic.randomTouzhu(cpVersion, cpTypeCode,
                playCode, subPlayCode);
        //计算注数
        int zhushu = JieBaoZhuShuCalculator.calc(Integer.parseInt(cpTypeCode),
                subPlayCode, touzhuNumbers);
        Utils.LOG(TAG, "the calc out zhushu ==== " + zhushu);

        //保存随机投注的注单信息到数据库
        OrderDataInfo info = new OrderDataInfo();
        info.setUser(YiboPreference.instance(this).getUsername());
        info.setPlayName(playName);
        info.setPlayCode(playCode);
        info.setSubPlayName(subPlayName);
        info.setSubPlayCode(subPlayCode);
        info.setBeishu(1);
        info.setZhushu(zhushu);
        info.setMoney(zhushu * 2);
        info.setNumbers(touzhuNumbers);
        info.setMode(Constant.YUAN_MODE);
        info.setCpCode(cpBianma);
        info.setLotcode(cpBianma);
        info.setLottype(cpTypeCode);
        info.setSaveTime(System.currentTimeMillis());
        DatabaseUtils.getInstance(this).saveCart(info);
        //将记录添加到列表
        listDatas.add(info);
        recordAdapter.notifyDataSetChanged();
        updateBottom();
    }

    private void updateBottom() {
        if (listDatas != null) {
            float totalMoney = 0;
            int totalZhushu = 0;
            for (OrderDataInfo info : listDatas) {
                double money = info.getMoney();
                totalMoney += money;
                totalZhushu += info.getZhushu();
            }
            zhushuMoneyTV.setText(String.format("%d注%.2f元", totalZhushu, totalMoney));
        }
    }

    public class DataAdapter extends LAdapter<OrderDataInfo> {

        Context context;

        public DataAdapter(Context mContext, List<OrderDataInfo> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        private String convertMode(int mode) {
            if (mode == Constant.YUAN_MODE) {
                return "元模式";
            } else if (mode == Constant.JIAO_MODE) {
                return "角模式";
            } else if (mode == Constant.FEN_MODE) {
                return "分模式";
            }
            return "元模式";
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final OrderDataInfo item) {

            TextView numberTV = holder.getView(R.id.numbers);
            TextView playRuleTV = holder.getView(R.id.play_rule);
            TextView modeTV = holder.getView(R.id.mode_txt);
            TextView zhushuMoneyTV = holder.getView(R.id.zhushu_money_txt);
            Button deleteBtn = holder.getView(R.id.delete);

            numberTV.setText(!Utils.isEmptyString(item.getNumbers()) ? item.getNumbers() : "暂无号码");
            playRuleTV.setText(item.getPlayName() + "-" + item.getSubPlayName());
            modeTV.setText(convertMode(item.getMode()));
            zhushuMoneyTV.setText(String.format("%d注%.2f元", item.getZhushu(), item.getMoney()));

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listDatas.remove(position);
                    DatabaseUtils.getInstance(context).deleteOrder(item.getOrderno());
                    recordAdapter.notifyDataSetChanged();
                    updateBottom();
                }
            });
        }
    }


}
