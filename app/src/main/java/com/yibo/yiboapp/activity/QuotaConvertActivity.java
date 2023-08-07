package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.GameWraper;
import com.yibo.yiboapp.entify.MemInfoWraper;
import com.yibo.yiboapp.entify.OtherPlay;
import com.yibo.yiboapp.entify.OtherPlayWrapper;
import com.yibo.yiboapp.entify.SBSportResultWrapper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.FeeConvertWindow;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 * 只针对真人，REAL_MODULE_CODE = 1
 */

public class QuotaConvertActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    public static final String TAG = QuotaConvertActivity.class.getSimpleName();

    private XListView listView;
    private EmptyListView empty;
    List<OtherPlay> datas;
    ListViewAdapter adapter;
    TextView tx_quota_remark;
    TextView tv_yue;
    public static final int REAL_DATAS_REQUEST = 0x01;
    public static final int REAL_LINK_REQUEST = 0x02;
    public static final int SYNC_BALANCE_REQUEST = 0x03;
    public static final int FEE_CONVERT_REQUEST = 0x04;
    public static final int REQUEST_REAL = 0x05;
    public static final int SBSPORT_REQUEST = 0x10;
    private static final int ACCOUNT_REQUEST = 0x22;

    public static final String REAL_CODE = "shaba";

    List<Float> balances = new ArrayList<>();
    FeeConvertWindow feeConvertWindow;
    boolean isSyncBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quota_convert);
        initView();
        datas = new ArrayList<>();
        adapter = new ListViewAdapter(this, datas, R.layout.quota_convert_item);
        tx_quota_remark = findViewById(R.id.tx_quota_remark);
        tv_yue = findViewById(R.id.tv_yue);
        listView.setAdapter(adapter);
        syncHeaderWebDatas(this);
        actionData(true);
        String config = UsualMethod.getConfigFromJson(this).getThird_auto_exchange();
        if ("off".equals(config)) {
            tx_quota_remark.setVisibility(View.GONE);
        } else {
            tx_quota_remark.setVisibility(View.VISIBLE);
        }
    }

    private void actionData(boolean showDialog) {
        if (isSyncBalance) {
            return;
        }
        //获取列表数据
        StringBuilder lunboUrl = new StringBuilder();
        lunboUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REAL_CONVERT_DATA_URL);
        CrazyRequest<CrazyResult<OtherPlayWrapper>> request = new AbstractCrazyRequest.Builder().
                url(lunboUrl.toString())
                .seqnumber(REAL_DATAS_REQUEST)
                .shouldCache(true)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.loading))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<OtherPlayWrapper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        if (!showDialog) {
            startProgress();
        }
        RequestManager.getInstance().startRequest(this, request);
    }

    //请求用户信息
    public void syncHeaderWebDatas(Context context) {
        StringBuilder accountUrls = new StringBuilder();
        accountUrls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MEMINFO_URL);
        CrazyRequest<CrazyResult<MemInfoWraper>> accountRequest = new AbstractCrazyRequest.Builder().
                url(accountUrls.toString())
                .seqnumber(ACCOUNT_REQUEST)
                .listener(this)
                .placeholderText("刷新中")
                .headers(Urls.getHeader(context))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MemInfoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, accountRequest);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.convert_fee_string));
        listView = (XListView) findViewById(R.id.xlistview);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        listView.setXListViewListener(new ListviewListener());
        listView.setDividerHeight(3);
        listView.setVisibility(View.VISIBLE);
        empty = (EmptyListView) findViewById(R.id.empty);
        listView.setEmptyView(empty);
        empty.setListener(emptyListviewListener);
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, QuotaConvertActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == REAL_DATAS_REQUEST) {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.acquire_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            OtherPlayWrapper reg = (OtherPlayWrapper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                datas.clear();
                datas.addAll(reg.getContent());
                adapter.notifyDataSetChanged();
            }
            //开始异步获取各游戏的余额
            balances.clear();
            getBalances(datas);
        } else if (action == REAL_LINK_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.jump_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.jump_fail);
                return;
            }
            Object regResult = result.result;
            GameWraper reg = (GameWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.jump_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                if (reg.getContent().isSuccess()) {
                    String agUrl = reg.getContent().getUrl();
                    Utils.LOG(TAG, "the jump game url = " + agUrl);
                    if (!Utils.isEmptyString(agUrl)) {
                        actionViewGame(agUrl);
                        return;
                    }
                    String html = reg.getContent().getHtml();
                    Utils.LOG(TAG, "the jump game html = " + html);
                    if (!Utils.isEmptyString(html)) {
                        actionViewGame(html);
                        return;
                    }
                    showToast(R.string.jump_url_empty);
                } else {
                    showToast(R.string.jump_url_empty);
                }
            }
        } else if (action == SYNC_BALANCE_REQUEST) {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            tvMiddleTitle.setText(getString(R.string.convert_fee_string));
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.sync_fee_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.sync_fee_fail);
                return;
            }
            Object regResult = result.result;
            String reg = (String) regResult;
            double value = 0;
            try {
                JSONObject json = new JSONObject(reg);
                if (!json.isNull("balance")) {
                    value = json.getDouble("balance");
                } else if (!json.isNull("money")) {
                    value = json.getDouble("money");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            balances.add((float) value);
            //将余额赋值到列表数据集中
            if (balances.size() == datas.size()) {
                isSyncBalance = false;
                for (int i = 0; i < datas.size(); i++) {
                    datas.get(i).setBalance(balances.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        } else if (action == FEE_CONVERT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.fee_convert_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.fee_convert_fail);
                return;
            }
            Object regResult = result.result;
            String reg = (String) regResult;
            try {
                JSONObject json = new JSONObject(reg);
                if (!json.isNull("success")) {
                    boolean success = json.getBoolean("success");
                    if (success) {
                        showToast(R.string.fee_convert_success);
                        //成功转换后，刷新余额
                        balances.clear();
                        getBalances(datas);
                    } else {
                        if (!json.isNull("msg")) {
                            String msg = json.getString("msg");
                            if (!Utils.isEmptyString(msg)) {
                                if (msg.contains("登录")) {
                                    UsualMethod.loginWhenSessionInvalid(this);
                                    return;
                                }
                                showToast(msg);
                            } else {
                                showToast(R.string.fee_convert_fail);
                            }
                        } else {
                            showToast(R.string.fee_convert_fail);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (action == REQUEST_REAL) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.jump_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.jump_fail);
                return;
            }
            Object regResult = result.result;
            String reg = (String) regResult;
            try {
                JSONObject json = new JSONObject(reg);
                if (!json.isNull("success")) {
                    boolean success = json.getBoolean("success");
                    if (success) {
//                        showToast(R.string.fee_convert_success);
                        //AG,MG,AB,OG,DS都返回跳转链接
                        //BBIN 返回的是一段html内容
                        String url = !json.isNull("url") ? json.getString("url") : "";
                        if (!Utils.isEmptyString(url)) {
                            if (nowGameName.contains("真人") || nowGameName.contains("电子") || nowGameName.contains("捕鱼")) {
                                SysConfig config = UsualMethod.getConfigFromJson(this);
                                if (config.getZrdz_jump_broswer().equals("on")) {
                                    actionViewGame(url);
                                } else {
                                    SportNewsWebActivity.createIntent(this, url, nowGameName);
                                }
                            } else {
                                actionViewGame(url);
                            }
                        } else {
                            String html = !json.isNull("html") ? json.getString("html") : "";
                            //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
                            actionViewGame(html);
                        }
                    } else {
                        if (!json.isNull("msg")) {
                            String msg = json.getString("msg");
                            showToast(msg);
                            if (msg.contains("超时") || msg.contains("其他")) {
                                UsualMethod.loginWhenSessionInvalid(this);
                            }
                        } else {
                            showToast(R.string.jump_fail);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (action == SBSPORT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.jump_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.jump_fail));
                return;
            }
            SBSportResultWrapper stw = (SBSportResultWrapper) result.result;
            if (!stw.isSuccess()) {
                showToast(!Utils.isEmptyString(stw.getMsg()) ? stw.getMsg() :
                        "跳转失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (stw.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(stw.getAccessToken());
            if (!Utils.isEmptyString(stw.getContent())) {
                UsualMethod.viewLink(this, stw.getContent());
            } else {
                showToast("没有链接，无法跳转");
            }
        } else if (action == ACCOUNT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? this.getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            MemInfoWraper reg = (MemInfoWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新帐户名，余额等信息
            if (!Utils.isEmptyString(reg.getContent().getBalance())) {
                String leftMoneyName = Double.valueOf(reg.getContent().getBalance()).doubleValue() + "";
                tv_yue.setText(leftMoneyName);
            }
        }
    }

    private CrazyRequest getBalances(String gameCode) {
        if (Utils.isEmptyString(gameCode)) {
            return null;
        }
        StringBuilder lunboUrl = new StringBuilder();
        if (gameCode.equalsIgnoreCase(REAL_CODE)) {
            lunboUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SBSPORT_BALANCE_URL);
        } else {
            lunboUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REAL_GAME_BALANCE_URL);
        }

        lunboUrl.append("?type=").append(gameCode);
        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(lunboUrl.toString())
                .seqnumber(SYNC_BALANCE_REQUEST)
                .syncDeliveryWithBrother(true)
                .shouldCache(false)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.loading))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        return request;
//        RequestManager.getInstance().startRequest(this,request);
//        startProgress();
    }

    private void getBalances(List<OtherPlay> datas) {
        if (datas == null) {
            return;
        }

        CrazyRequest[] requests = new CrazyRequest[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            requests[i] = getBalances(TextUtils.isEmpty(datas.get(i).getGameType()) ? datas.get(i).getPlayCode() : datas.get(i).getGameType());
        }
        tvMiddleTitle.setText(getString(R.string.sync_fee_ongoging));
        RequestManager.getInstance().startRequest(this, requests);
        startProgress();
        isSyncBalance = true;
    }

    private void actionConvert(String from, String to, String money, boolean fromSys, String url, String key) {

        StringBuilder lunboUrl = new StringBuilder();
        lunboUrl.append(Urls.BASE_URL).append(Urls.PORT).append(url);
        String myto = to;
        String myfrom = from;
        lunboUrl.append("?changeFrom=").append(myfrom);
        lunboUrl.append("&changeTo=").append(myto);
        lunboUrl.append("&" + key + "=").append(money);
        if (to.equalsIgnoreCase(REAL_CODE) || from.equalsIgnoreCase(REAL_CODE)) {
            SysConfig config = UsualMethod.getConfigFromJson(this);
            if (config != null) {
                String token = config.getSys_shaba_center_token();
                lunboUrl.append("&v=").append(token);
            }
        }
        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(lunboUrl.toString())
                .seqnumber(FEE_CONVERT_REQUEST)
                .syncDeliveryWithBrother(true)
                .shouldCache(false)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.convert_feeing))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 列表下拉，上拉监听器
     *
     * @author johnson
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
            actionData(false);
        }

        public void onLoadMore() {

        }
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            actionData(true);
        }
    };

    private String nowGameName = ""; //当前进入的选择的游戏名称

    private final class ListViewAdapter extends LAdapter<OtherPlay> {

        Context context;
        DecimalFormat decimalFormat;

        public ListViewAdapter(Context mContext, List<OtherPlay> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final OtherPlay item) {
            TextView name = holder.getView(R.id.name);
            TextView money = holder.getView(R.id.money);
            TextView convertBtn = holder.getView(R.id.convert);
            Button enterGame = holder.getView(R.id.enter);
            enterGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionForwardReal(context, item.getPlayCode(), item.getTitle(), item);
                }
            });
            name.setText(item.getTitle());
            money.setText(decimalFormat.format(item.getBalance()) + "元");
            RelativeLayout itemLayout = holder.getView(R.id.item);
            final String url = item.getFeeConvertUrl();
            String urlparams = url.substring(url.indexOf("?") + 1, url.length());
            final String feeUrl = url.replace(urlparams, "").replace("?", "");
            final String moneykey = urlparams.replace("=", "");
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showConvertList(context, TextUtils.isEmpty(item.getGameType()) ? item.getPlayCode() : item.getGameType(), item.getTitle(), feeUrl, moneykey);
                }
            });
            convertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showConvertList(context, TextUtils.isEmpty(item.getGameType()) ? item.getPlayCode() : item.getGameType(), item.getTitle(), feeUrl, moneykey);
                }
            });
        }


        //获取真人转发数据
        private void actionForwardReal(Context context, String gameCode, String gameName, OtherPlay data) {
            nowGameName = gameName;
            //获取列表数据
            StringBuilder url = new StringBuilder();
            url.append(Urls.BASE_URL).append(Urls.PORT);

            if (data.getIsListGame() == 1) {
                GameListActivity.createIntent(context, data.getTitle(), data.getPlayCode());
            } else if (data.getIsListGame() == 2) {
                SportActivity.createIntent(context, data.getTitle(), Constant.SPORT_MODULE_CODE + "");
            } else if (data.getIsListGame() == 0) {
                String result = UsualMethod.forwardGame(context, gameCode, REQUEST_REAL, QuotaConvertActivity.this, data.getForwardUrl());
                if (!Utils.isEmptyString(result)) {
                    showToast(result);
                }
            }
        }
    }

    private void requestsbUrl() {

        StringBuilder url = new StringBuilder();
        url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SBSPORT_JUMP_URL);
        CrazyRequest<CrazyResult<SBSportResultWrapper>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(SBSPORT_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.forward_jumping))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .convertFactory(GsonConverterFactory.create(new TypeToken<SBSportResultWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();

        RequestManager.getInstance().startRequest(this, request);
    }

    private void actionViewGame(String url) {
        if (Utils.isEmptyString(url)) {
            return;
        }
        showToast(R.string.enter_game_page);
        try {
            if (url.startsWith("HTTPS")) {
                url = url.replace("HTTPS", "https");
            } else if (url.startsWith("HTTP")) {
                url = url.replace("HTTP", "http");
            }
            if (url.startsWith("www")) {
                url = url.replace("www", "https://www");
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConvertList(final Context context, final String gameCode, final String gameName, final String url, final String key) {

        final String[] stringItems = getResources().getStringArray(R.array.convert_array);
        if (stringItems.length == 0) {
            return;
        }
        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
        dialog.title("转换");
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (position == 0) {
                    showWindow(gameCode, gameName, true, url, key);
                } else if (position == 1) {
                    showWindow(gameCode, gameName, false, url, key);
                }
            }
        });
    }

    public void showWindow(String gameCode, String gameName, boolean convertFromSysToPlatform, final String url, final String key) {

        if (feeConvertWindow == null) {
            feeConvertWindow = new FeeConvertWindow(this);
        }

        feeConvertWindow.setConvertListener(new FeeConvertWindow.ConvertListener() {
            @Override
            public void onConvert(String money, String gameCode, boolean fromSys) {
                if (Utils.isEmptyString(money)) {
                    showToast(R.string.input_peilv_money);
                    return;
                }
                if (!Utils.isNumeric(money)) {
                    showToast(R.string.input_money_must_be_zs);
                    return;
                }

                //开始发起转换
                if (fromSys) {
                    actionConvert("sys", gameCode, money, fromSys, url, key);
                } else {
                    actionConvert(gameCode, "sys", money, fromSys, url, key);
                }
            }
        });

        if (!feeConvertWindow.isShowing()) {
            feeConvertWindow.showWindow(findViewById(R.id.item), gameCode, gameName, convertFromSysToPlatform);
        }
    }
}
