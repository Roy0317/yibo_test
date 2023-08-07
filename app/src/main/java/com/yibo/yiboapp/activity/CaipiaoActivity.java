package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.GameAdapter;
import com.yibo.yiboapp.adapter.NumbersAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LocPlaysWraper;
import com.yibo.yiboapp.entify.LotterysWraper;
import com.yibo.yiboapp.entify.OpenResultDetail;
import com.yibo.yiboapp.entify.OpenResultDetailWraper;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.UpdateAllGameEvent;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author johnson
 * 彩票模块主界面
 */

public class CaipiaoActivity extends BaseActivity implements GameAdapter.GameEventDelegate,
        SessionResponse.Listener<CrazyResult<Object>> {


    public static final String TAG = CaipiaoActivity.class.getSimpleName();
    LinearLayout fastLayout;
    Button randomTouzhuView;
    Button touzhuBtn;
    TextView fastCpName;
    GridView haomaGridView;
    TextView open_time;

    GridView caipiaos;
    List<LotteryData> cpData = new ArrayList<>();
    GameAdapter caipiaoListAdapter;
    public static final int PLAY_RULES_REQUEST = 0x01;
    public static final int LOTTERYS_REQUEST = 0x02;
    public static final int OPEN_RESULT_REQUEST = 0x04;

    String randomCpName;//随机下注的彩种名
    String randomCpCode;//随机下注的采种号
    String randomCpTypeCode;//随机下注的彩票类型代号
    long duration;//封盘时间离开奖时间差

    private String lastEventJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chipin_mall);
        initView();
        caipiaoListAdapter = new GameAdapter(this, cpData, R.layout.caipiao_item);
        caipiaoListAdapter.setDelegate(this);
        caipiaos.setAdapter(caipiaoListAdapter);
        caipiaos.setVerticalScrollBarEnabled(false);
        updateLotteryViewFromBackup();
        startAsyncLottery();
        //每次从大厅进来都会请求一次彩种资料，所以先不要注册事件更新
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void startAsyncLottery() {
        //获取彩种信息
        StringBuilder lotteryUrl = new StringBuilder();
        lotteryUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERYS_URL);
        CrazyRequest<CrazyResult<LotterysWraper>> lotteryRequest = new AbstractCrazyRequest.Builder().
                url(lotteryUrl.toString())
                .seqnumber(LOTTERYS_REQUEST)
                .headers(Urls.getHeader(this))
//                .shouldCache(true)
//                .refreshAfterCacheHit(true)
                .placeholderText(getString(R.string.sync_caizhong_ing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotterysWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, lotteryRequest);
    }

    @Override
    public void onGameEvent(String gameCode, int gameModue, String gameName, LotteryData data) {
        onCaipiaoClicked(gameCode);
    }

    @Subscribe
    public void onEvent(UpdateAllGameEvent event){
        if (event.getLotteryJson() != null && !event.getLotteryJson().equals(lastEventJson)){
            lastEventJson = event.getLotteryJson();
            Type listType = new TypeToken<ArrayList<LotteryData>>() {}.getType();
            List<LotteryData> newData = new Gson().fromJson(event.getLotteryJson(), listType);
            updateLotterysView(newData);
            figureRandomLottery(newData);
        }
    }

    private void updateLotteryViewFromBackup(){
        CacheRepository.getInstance().loadLotteryData(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<LotteryData>>() {
                    @Override
                    public void onSubscribe(Disposable d) { compositeDisposable.add(d); }

                    @Override
                    public void onSuccess(List<LotteryData> data) {
                        if(data != null && !data.isEmpty()){
                            updateLotterysView(data);
                            figureRandomLottery(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) { e.printStackTrace(); }
                });
    }

    private void updateLotterysView(List<LotteryData> data) {
        if (data == null && data.isEmpty()) {
            return;
        }
        //将所有彩种按彩种分类归类
        LotteryData lotteryItem;
        LinkedHashMap<String, List<LotteryData>> resultMap = new LinkedHashMap<>();
        for (int i = 0; i < data.size(); i++) {
            lotteryItem = data.get(i);
            if (resultMap.containsKey(lotteryItem.getCzCode())) {
                resultMap.get(lotteryItem.getCzCode()).add(lotteryItem);
            } else {
                LinkedList<LotteryData> list = new LinkedList<>();
                list.addLast(lotteryItem);
                resultMap.put(lotteryItem.getCzCode(), list);
            }
        }

        if (data != null) {
            cpData.clear();
//            cpTypeNames.addAll(resultMap.keySet());
            for (List<LotteryData> item : resultMap.values()) {
                cpData.addAll(item);
            }
            caipiaoListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.touzhu_btn:
//                if (!YiboPreference.instance(this).isLogin()) {
//                    showToast(R.string.havent_login);
//                    return;
//                }
                if (Utils.isEmptyString(randomCpCode)) {
                    showToast("没有彩种，无法投注");
                    return;
                }
                onCaipiaoClicked(randomCpCode);
                break;
            case R.id.random_touzhu:
//                Utils.shakeView(this,randomTouzhuView);
//                UsualMethod.fillBallons(this,ballon_wrapper,Utils.randomNumbers("0,1,2,3,4,5,6,7,8,9",true, 5, ","));
                break;
        }
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, CaipiaoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.caipiao_string));
        caipiaos = (GridView) findViewById(R.id.caipiao_mall);
        //快速投注view
        haomaGridView = (GridView) findViewById(R.id.ballon_wrapper);
        fastLayout = (LinearLayout) findViewById(R.id.func_layout);
        fastLayout.setVisibility(View.VISIBLE);
        randomTouzhuView = (Button) findViewById(R.id.random_touzhu);
        randomTouzhuView.setVisibility(View.GONE);
        //快捷投注开关
        touzhuBtn = (Button) findViewById(R.id.touzhu_btn);
        fastCpName = (TextView) findViewById(R.id.cpname);
        open_time = (TextView) findViewById(R.id.open_time);
        randomTouzhuView.setOnClickListener(this);
        touzhuBtn.setOnClickListener(this);
        //控制控件是否显示的开关
        setViewOrGone();
    }

    private void setViewOrGone() {
        //快捷投注开关是否显示和隐藏
        SysConfig config = UsualMethod.getConfigFromJson(this);
        String touZhuBtnSwitch = config.getFast_bet_switch();
        if ("on".equals(touZhuBtnSwitch)) {
            touzhuBtn.setVisibility(View.VISIBLE);
        } else {
            touzhuBtn.setVisibility(View.GONE);
        }
    }

    //根据彩票编码获取开奖结果
    private void getOpenResultDetail(String lotteryCode) {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.OPEN_RESULT_DETAIL_URL);
        urls.append("?lotCode=").append(lotteryCode);
        CrazyRequest<CrazyResult<OpenResultDetailWraper>> playsRequest = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(OPEN_RESULT_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(true)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<OpenResultDetailWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, playsRequest);
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == PLAY_RULES_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null || !result.crazySuccess) {
                showToast("获取玩法失败");
                return;
            }

            Object regResult = result.result;
            LocPlaysWraper reg = (LocPlaysWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : "获取玩法失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                String json = new Gson().toJson(reg.getContent(), LotteryData.class);
                String gameCode = reg.getContent().getCode();
                Utils.LOG(TAG, "after play rule, gameCode = " + gameCode);
                CacheRepository.getInstance().saveLotteryPlayJson(getApplicationContext(), gameCode, json);
                openTouzhuPage(gameCode, json, false);
            }
        } else if (action == LOTTERYS_REQUEST) {
            CrazyResult<Object> result = response.result;
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
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }

            compositeDisposable.clear();
            YiboPreference.instance(this).setToken(stw.getAccessToken());
            //更新彩种信息界面gridview
            updateLotterysView(stw.getContent());
            figureRandomLottery(stw.getContent());
            if (stw.getContent() != null) {
                Type listType = new TypeToken<ArrayList<LotteryData>>() {}.getType();
                lastEventJson = new Gson().toJson(stw.getContent(), listType);
                CacheRepository.getInstance().saveLotteryData(this, stw.getContent());
            }
        } else if (action == OPEN_RESULT_REQUEST) {
            tvSecondTitle.setVisibility(View.GONE);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("获取开奖结果失败");
                return;
            }
            if (!result.crazySuccess) {
                showToast("获取开奖结果失败");
                return;
            }

            Object regResult = result.result;
            OpenResultDetailWraper reg = (OpenResultDetailWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                //获取到随机彩种的开奖信息后，更新快捷下注面板
                updateFastView(reg.getContent());
            }
        }
    }


    private void updateFastView(List<OpenResultDetail> details) {
        if (details == null || details.isEmpty()) {
            return;
        }
        OpenResultDetail openResultDetail = details.get(0);
        if (openResultDetail == null) {
            return;
        }
        fastCpName.setText("[" + randomCpName + "] 最新开奖:");
        List<String> haoMaList = openResultDetail.getHaoMaList();
        updateNumberGridView(haoMaList, randomCpTypeCode);
        open_time.setText("每 " + Utils.int2Time(duration * 1000) + " 开奖");
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
//        if (Utils.isEmptyString(lotType)) {
//            return;
//        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        int screenWidth = dm.widthPixels;
        int column = (int) ((screenWidth - Utils.dip2px(this, 10)) /
                Utils.dip2px(this, 30));
        Utils.LOG(TAG, "the figure out column == " + column);
        haomaGridView.setNumColumns(column);
        haomaGridView.setAdapter(new NumbersAdapter(this, haoMaList,
                R.layout.number_gridview_item, lotType, randomCpCode));
        Utils.setListViewHeightBasedOnChildren(haomaGridView, column);
    }


    private void randomLottery(List<LotteryData> lotterys) {
        if (lotterys == null || lotterys.isEmpty()) {
            return;
        }
        Random r = new Random();
        int index = Math.abs(r.nextInt(lotterys.size()));
        if (index == lotterys.size()) {
            index--;
        }
        LotteryData randomLot = lotterys.get(index);
        if (randomLot != null) {
            randomCpName = randomLot.getName();
            randomCpCode = randomLot.getCode();
            duration = randomLot.getDuration();
            randomCpTypeCode = randomLot.getCzCode();
            getOpenResultDetail(randomLot.getCode());
        }
    }

    /**
     * 从数据库取出并分析用户最常玩的彩种用于快捷下注
     *
     * @return
     */
    private void figureRandomLottery(List<LotteryData> lotterys) {
        if (lotterys == null)
            return;

        List<SavedGameData> lotGames = DatabaseUtils.getInstance(this).getUsualCaipiaoGames(this);
        if (lotGames != null && !lotGames.isEmpty()) {
            List<String> lotCodes = new ArrayList<>();
            for (SavedGameData data : lotGames) {
                lotCodes.add(data.getLotCode());
            }
            Set uniqueSet = new HashSet(lotCodes);
            int count = 0;
            String highFrequencyLotCode = "";
            for (Object code : uniqueSet) {
                int frequency = Collections.frequency(lotCodes, code);
                if (frequency > count) {
                    count = frequency;
                    highFrequencyLotCode = (String) code;
                }
            }
            Utils.LOG(TAG, "the high frequency lot code = " + highFrequencyLotCode);
            if (Utils.isEmptyString(highFrequencyLotCode)) {
                randomLottery(lotterys);
                return;
            }
            for (SavedGameData data : lotGames) {
                if (data.getLotCode().equals(highFrequencyLotCode)) {
                    randomCpName = data.getLotName();
                    randomCpCode = data.getLotCode();
                    randomCpTypeCode = data.getLotType();
                    duration = data.getDuration();
                    break;
                }
            }
            if (!Utils.isEmptyString(randomCpCode)) {
                getOpenResultDetail(randomCpCode);
            } else {
                randomLottery(lotterys);
            }
        } else {
            randomLottery(lotterys);
        }
    }

    private void onCaipiaoClicked(final String gameCode){
        CacheRepository.getInstance().loadLotteryPlayJson(this, gameCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) { compositeDisposable.add(d); }

                    @Override
                    public void onSuccess(String json) {
                        Utils.LOG(TAG, "find the backup of play rules");
                        openTouzhuPage(gameCode, json, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.LOG(TAG, "can't find the backup of play rules");
                        e.printStackTrace();
                        UsualMethod.syncLotteryPlaysByCode(CaipiaoActivity.this, gameCode, PLAY_RULES_REQUEST, CaipiaoActivity.this);
                    }
                });
    }

    private void openTouzhuPage(String gameCode, String json, boolean needRefresh){
        boolean isPeilvVersion = UsualMethod.isSixMark(this, gameCode) || UsualMethod.isPeilvVersionMethod(this);
        String cpVersion = UsualMethod.isSixMark(this, gameCode) ?
                String.valueOf(Constant.lottery_identify_V2) : YiboPreference.instance(this).getGameVersion();
//
        boolean isSimpleStyle = UsualMethod.getConfigFromJson(this).getBet_page_style().equalsIgnoreCase("v1");
        if (isSimpleStyle) {
            TouzhuSimpleActivity.createIntent(this, json, gameCode, needRefresh, isPeilvVersion, cpVersion);
        } else {
            TouzhuActivity.createIntent(this, json, gameCode, needRefresh, isPeilvVersion, cpVersion);
        }
    }
}
