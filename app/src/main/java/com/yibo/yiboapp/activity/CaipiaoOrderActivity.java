package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yibo.yiboapp.data.JieBaoZhuShuCalculator;
import com.yibo.yiboapp.data.LotteryConstants;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.LotteryPlayLogic;
import com.yibo.yiboapp.data.Lotterys;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.data.ZhudangAdapter;
import com.yibo.yiboapp.entify.BetToken;
import com.yibo.yiboapp.entify.DoBetWraper;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.QihaoWraper;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.BallonView;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.PopupListMenu;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 * 彩标投注后的投注清单列表
 * 投注数列表，再选一注，机选一注，追号功能
 */
public class CaipiaoOrderActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>>{

    public static final String TAG = CaipiaoOrderActivity.class.getSimpleName();
    SwipeMenuListView cListview;
    ZhudangAdapter orderAdapter;
    List<OrderDataInfo> cartOrders;

    LinearLayout layout_order_info;
    TextView againTouzhu;
    TextView randomTouzhu;
    TextView zuihao;
    TextView totalMoney;

    EmptyListView empty;
    RelativeLayout bottomLayout;
    TextView order_time;
    TextView order_qihao;

    String cpTypeCode;//当前已选择的彩种类型编码
    String cpBianHao;//已选择的彩票编码
    String cpVersion;
    int ago;//封盘时间

    public static final int REFRESH_AFTER_DELETE = 0x01;
    public static final int UPDATE_LISTVIEW = 0x02;
    FrontHandler frontHandler;
    public static final int TOKEN_BETS_REQUEST = 0x02;
    public static final int GET_QIHAO_REQUEST = 0x03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caipiao_order);
        initView();
        cpVersion = getIntent().getStringExtra("cpVersion");
        if (Utils.isEmptyString(cpVersion)) {
            cpVersion = YiboPreference.instance(this).getGameVersion();
        }
        toggleZuihaoEnter();
        empty.setShowText(getString(R.string.cart_empty));
        empty.setButtonText(getString(R.string.bug_first));
        empty.setListener(new EmptyButtonListener());

        frontHandler = new FrontHandler(this);
        cpTypeCode = getIntent().getStringExtra("cpTypeCode");
        cpBianHao = getIntent().getStringExtra("cpBianHao");

        order_time.setText(Utils.formatTime(System.currentTimeMillis()));
        cartOrders = new ArrayList<>();
        orderAdapter = new ZhudangAdapter(this, cartOrders, R.layout.order_list_item);
        cListview.setAdapter(orderAdapter);
        TouzhuThreadPool.getInstance().addTask(new QueryOrders(this));
    }


    private final class EmptyButtonListener implements EmptyListView.EmptyListviewListener {
        @Override
        public void onEmptyListviewClick() {
            finish();
        }
    }

    //提交投注
    private String wrapBets(String token) {
        if (cartOrders.isEmpty()) {
            showToast(R.string.noorder_please_touzhu_first);
            return null;
        }
        //构造下注POST数据
        try {
            JSONArray betsArray = new JSONArray();
            for (OrderDataInfo order : cartOrders) {
                StringBuilder sb = new StringBuilder();
                sb.append(cpBianHao).append("|");
                sb.append(order.getSubPlayCode()).append("|");
                sb.append(UsualMethod.convertPostMode(order.getMode())).append("|");
                sb.append(order.getBeishu()).append("|");
                sb.append(order.getNumbers());
                betsArray.put(sb.toString());
            }

            JSONObject content = new JSONObject();
            content.put("lotCode", cpBianHao);
            content.put("qiHao", "");
            content.put("token", token);
            content.put("bets", betsArray);
            String postJson = content.toString();
            return URLEncoder.encode(postJson, "utf-8");
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
                    YiboPreference.instance(CaipiaoOrderActivity.this).setToken(result.getAccessToken());
                    showToast(R.string.dobets_success);
                    handleClearAllMenus();
                }else {
                    showToast(TextUtils.isEmpty(result.getMsg()) ? "下注时出现错误，请重新下注" : result.getMsg());
                    if (result.getMsg().contains("登陆超时")) {
                        UsualMethod.loginWhenSessionInvalid(CaipiaoOrderActivity.this);
                    }
                }
            }
        });
    }

    private void actionGetQihao(String lotCode) {
        //构造下注crazy request
        StringBuilder qiHaoUrl = new StringBuilder();
        qiHaoUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACQUIRE_CURRENT_QIHAO_URL);
        qiHaoUrl.append("?lotCode=").append(lotCode);
        CrazyRequest<CrazyResult<QihaoWraper>> request = new AbstractCrazyRequest.Builder().
                url(qiHaoUrl.toString())
                .seqnumber(GET_QIHAO_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<QihaoWraper>(){}.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this,request);
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
            Object regResult =  result.result;
            BetToken reg = (BetToken) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():getString(R.string.dobets_token_fail));
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
        }else if (action == GET_QIHAO_REQUEST) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult =  result.result;
            QihaoWraper reg = (QihaoWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新投注期号VIEW
            if (reg.getContent() != null) {
                order_qihao.setText(getString(R.string.touzhu_current_qihao)+reg.getContent());
            }

        }
    }

    /**
     * 加载订单信息
     */
    private final class QueryOrders implements Runnable{

        WeakReference<Context> weakReference;
        QueryOrders(Context context) {
            weakReference = new WeakReference<Context>(context);
        }
        @Override
        public void run() {
            if (weakReference.get() == null) {
                return;
            }
            List<OrderDataInfo> data = DatabaseUtils.getInstance(weakReference.get()).getCartOrders();
            Message msg = frontHandler.obtainMessage(UPDATE_LISTVIEW, data);
            frontHandler.sendMessage(msg);
        }
    }


    private void updateOrderMoney() {
        if (cartOrders != null) {
            float total = 0;
            for (OrderDataInfo info : cartOrders) {
                double money = info.getMoney();
                total += money;
            }
            DecimalFormat decimalFormat =new DecimalFormat("0.00");
            String moneyString = decimalFormat.format(total);
            totalMoney.setText(String.format(getString(R.string.order_money_list_format),moneyString));
        }
    }

    @Override
    protected void initView() {
        super.initView();
        String cpName = getIntent().getStringExtra("cpName");
        if (!Utils.isEmptyString(cpName)) {
            tvMiddleTitle.setText(getString(R.string.caipiao_cart_string)+"("+cpName+")");
        }else{
            tvMiddleTitle.setText(getString(R.string.caipiao_cart_string));
        }
        tvRightText.setVisibility(View.VISIBLE);
        cListview = (SwipeMenuListView) findViewById(R.id.cListview);

        layout_order_info = (LinearLayout) findViewById(R.id.layout_order_info);
        order_time = (TextView) findViewById(R.id.order_time);
        order_qihao = (TextView) findViewById(R.id.current_qihao);

        againTouzhu = (TextView) findViewById(R.id.again_touzhu);
        randomTouzhu = (TextView) findViewById(R.id.random_touzhu);
        zuihao = (TextView) findViewById(R.id.intelligent);
        againTouzhu.setOnClickListener(this);
        randomTouzhu.setOnClickListener(this);
        zuihao.setOnClickListener(this);

        empty = (EmptyListView) findViewById(R.id.empty);
        totalMoney = (TextView) findViewById(R.id.order_money);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottom_func);
        BallonView clearBtn = (BallonView) findViewById(R.id.clear_btn);
        BallonView touzhuBtn = (BallonView) findViewById(R.id.touzhu_btn);
        clearBtn.setOnClickListener(this);
        touzhuBtn.setOnClickListener(this);

        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.order_footer, null);
        cListview.addFooterView(view);
        cListview.setMenuCreator(creator);
        cListview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        String orderno = cartOrders.get(position).getOrderno();
                        DatabaseUtils.getInstance(CaipiaoOrderActivity.this).
                                deleteOrder(orderno);
                        cartOrders.remove(position);
                        orderAdapter.notifyDataSetChanged();
                        updateOrderMoney();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    //追号开关是否打开
    private void toggleZuihaoEnter() {
        String sysConfig = YiboPreference.instance(this).getSysConfig();
        if (!Utils.isEmptyString(sysConfig)) {
            SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
            if (!Utils.isEmptyString(sc.getLottery_order_chase_switch())&&sc.getLottery_order_chase_switch().equals("on")) {
                zuihao.setVisibility(View.VISIBLE);
                return;
            }
        }
        zuihao.setVisibility(View.GONE);
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
            @Override public void OnItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        RecordsActivity.createIntent(CaipiaoOrderActivity.this,"",Constant.CAIPIAO_RECORD_STATUS,
                                cpBianHao);
                        break;
                    case 1:
                        BetProfileActivity.createIntent(CaipiaoOrderActivity.this);
                        break;
                }
            }
        });
        menu.show(rightLayout, 0, 5);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_text) {
            showFuncMenu();
        } else if (v.getId() == R.id.again_touzhu) {
            actionAgainTouzhu();
        } else if (v.getId() == R.id.random_touzhu) {
            actionRandomTouzhu(cpVersion);
            List<OrderDataInfo> datas = DatabaseUtils.getInstance(this).getCartOrders();
            if (datas != null && !datas.isEmpty()) {
                cartOrders.clear();
                cartOrders.addAll(datas);
                orderAdapter.notifyDataSetChanged();
                updateOrderMoney();
                layout_order_info.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.intelligent) {
            actionZuihao();
        } else if (v.getId() == R.id.clear_btn) {
            handleClearAllMenus();
        } else if (v.getId() == R.id.touzhu_btn) {
            actionTouzhu();
        }
    }

    //投注动作
    private void actionTouzhu() {
        CrazyRequest tokenRequest = UsualMethod.buildBetsTokenRequest(this, TOKEN_BETS_REQUEST);
        RequestManager.getInstance().startRequest(this,tokenRequest);
    }

    //线程异步handler
    private static class FrontHandler extends android.os.Handler {
        private WeakReference<CaipiaoOrderActivity> mReference;
        private CaipiaoOrderActivity mActivity;

        public FrontHandler(CaipiaoOrderActivity activity) {
            mReference = new WeakReference<CaipiaoOrderActivity>(activity);
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
                    mActivity.orderAdapter.notifyDataSetChanged();
                    mActivity.updateOrderMoney();
                    break;
                case UPDATE_LISTVIEW:
                    List<OrderDataInfo> data = (List<OrderDataInfo>) msg.obj;
                    if (data != null && !data.isEmpty()) {
                        mActivity.cartOrders.clear();
                        mActivity.cartOrders.addAll(data);
                        mActivity.orderAdapter.notifyDataSetChanged();
                        mActivity.updateOrderMoney();
                        //获取一下当前期号
                        mActivity.actionGetQihao(mActivity.cpBianHao);
                        mActivity.layout_order_info.setVisibility(View.VISIBLE);
                        mActivity.bottomLayout.setVisibility(View.VISIBLE);
                    }else{
                        //没有注单记录时，显示空白页面
                        mActivity.cListview.setEmptyView(mActivity.empty);
                        mActivity.layout_order_info.setVisibility(View.GONE);
                        mActivity.bottomLayout.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    private void handleClearAllMenus() {
        if (cartOrders == null) {
            return;
        }
        if (cartOrders.size() >= 15) {
            TouzhuThreadPool.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    doClear();
                    frontHandler.sendEmptyMessage(REFRESH_AFTER_DELETE);
                }
            });
        }else{
            doClear();
            orderAdapter.notifyDataSetChanged();
            updateOrderMoney();
        }
    }

    private void doClear() {
        for (OrderDataInfo info : cartOrders) {
            DatabaseUtils.getInstance(CaipiaoOrderActivity.this).deleteOrder(info.getOrderno());
        }
        cartOrders.clear();

        //没有注单记录时，显示空白页面
        cListview.setEmptyView(empty);
        layout_order_info.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);

    }

    private void actionAgainTouzhu() {
        finish();
    }

    private void actionRandomTouzhu(String cpVersion) {

        LotteryConstants lotterys = Lotterys.getLotterysByVersion(cpVersion);
        LotteryData selectedLotteryData = null;

        String selectedPlayName = null;
        String selectedPlayCode = null;
        String selectedSubPlayName = null;
        String selectedSubPlayCode = null;


        //随机选择当前彩种下的大小玩法
        if (lotterys != null) {
            List<LotteryData> loc = lotterys.getLotterys();
            for (LotteryData data : loc) {
                if (data.getCzCode().equals(cpTypeCode)) {
                    selectedLotteryData = data;
                    break;
                }
            }
            if (selectedLotteryData != null) {
                List<PlayItem> rules = selectedLotteryData.getRules();
                if (rules != null && !rules.isEmpty()) {
                    Random r = new Random();
                    int index = r.nextInt(rules.size());
                    Utils.LOG(TAG,"the random play index = "+index);
                    if (index == rules.size()) {
                        index = rules.size() - 1;
                    }
                    PlayItem item = rules.get(index);
                    if (item != null) {
                        selectedPlayName = item.getName();
                        selectedPlayCode = item.getCode();
                    }
                    List<SubPlayItem> subRules = item.getRules();
                    if (subRules != null && !subRules.isEmpty()) {
                        int subIndex = r.nextInt(subRules.size());
                        Utils.LOG(TAG,"the random sub play index = "+subIndex);
                        if (subIndex == subRules.size()) {
                            subIndex = subRules.size() - 1;
                        }
                        SubPlayItem subItem = subRules.get(subIndex);
                        if (subItem != null) {
                            selectedSubPlayName = subItem.getName();
                            selectedSubPlayCode = subItem.getCode();
                        }
                    }
                }
            }
        }

        Utils.LOG(TAG,"random select play code = "+selectedPlayCode+" sub play code = "
                +selectedSubPlayCode);
        //计算投注号码
        String touzhuNumbers = LotteryPlayLogic.randomTouzhu(cpVersion, cpTypeCode,
                selectedPlayCode, selectedSubPlayCode);
        //计算注数
        int zhushu = JieBaoZhuShuCalculator.calc(Integer.parseInt(cpTypeCode),
                selectedSubPlayCode, touzhuNumbers);
        Utils.LOG(TAG,"the calc out zhushu ==== "+zhushu);

        //保存随机投注的注单信息到数据库
        OrderDataInfo info = new OrderDataInfo();
        info.setUser(YiboPreference.instance(this).getUsername());
        info.setPlayName(selectedPlayName);
        info.setPlayCode(selectedPlayCode);
        info.setSubPlayName(selectedSubPlayName);
        info.setSubPlayCode(selectedSubPlayCode);
        info.setBeishu(1);
        info.setZhushu(zhushu);
        info.setMoney(zhushu*2);
        info.setNumbers(touzhuNumbers);
        info.setMode(Constant.YUAN_MODE);
        info.setSaveTime(System.currentTimeMillis());
        DatabaseUtils.getInstance(this).saveCart(info);
    }

    private void actionZuihao() {

        if (cartOrders == null || cartOrders.isEmpty()) {
            showToast(R.string.please_touzhu_first);
            return;
        }
        Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {}.getType();
        String zhuJsons = new Gson().toJson(cartOrders, listType);
//        BraveZuiHaoActivity.createIntent(this,zhuJsons,cpBianHao);

    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(Utils.dip2px(CaipiaoOrderActivity.this,90));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, CaipiaoOrderActivity.class);
        context.startActivity(intent);
    }

    public static void createIntent(Context context,String cpTypeCode,String cpBianHao,
                                    String cpName,String cpVersion) {
        Intent intent = new Intent(context, CaipiaoOrderActivity.class);
        intent.putExtra("cpTypeCode", cpTypeCode);
        intent.putExtra("cpBianHao", cpBianHao);
        intent.putExtra("cpName", cpName);
        intent.putExtra("cpVersion", cpVersion);
        context.startActivity(intent);
    }

}
