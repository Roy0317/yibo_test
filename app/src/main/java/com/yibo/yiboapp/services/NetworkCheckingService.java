package com.yibo.yiboapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.ActivesResultWraper;
import com.yibo.yiboapp.entify.CheckCtrlCmdsResult;
import com.yibo.yiboapp.entify.CheckPostCmdsResult;
import com.yibo.yiboapp.entify.ClientIpBean;
import com.yibo.yiboapp.entify.FindAllGamesResponse;
import com.yibo.yiboapp.entify.FindAllGamesResponseWrapper;
import com.yibo.yiboapp.entify.OpenResultWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.UpdateAllGameEvent;
import com.yibo.yiboapp.route.LDNetDiagnoService.LDNetDiagnoListener;
import com.yibo.yiboapp.route.LDNetDiagnoService.LDNetDiagnoService;
import com.yibo.yiboapp.utils.GetProtocol;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.simon.utils.RxJavaUtil.runOnThread;
import static com.simon.utils.RxJavaUtil.runOnUiThread;
import static java.util.stream.Collectors.toList;

/**
 * 1. 周期性地执行网络状况检测 startNetworkCheckingTimer()
 * 2. 周期性地备份大厅彩种信息 fetchAllGamesDataPeriodically()
 * 3. 周期性地备份开奖公告信息 fetchOpenResultPeriodically()
 * 4. 周期性地备份优惠活动信息 fetchAcquireActives()
 */
public class NetworkCheckingService extends Service implements SessionResponse.Listener<CrazyResult<Object>> {
    private final String TAG = NetworkCheckingService.class.getSimpleName();
    public static final int CTRL_CMDS = 0x99;
    public static final int CTRL_POST_CMDS = 0x100;
    public static final int DELEGATE_LOTTERYS_REQUEST = 0x200;
    public static final int LOTTERYS_REQUEST = 0x201;
    public static final int LOAD_OPEN_RESULTS_REQUEST = 0x01;
    public static final int ACQUIRE_ACTIVES_REQUEST = 0x02;

    public static final String ACTION = "action";
    public static final int ACTION_DEFAULT = 0x001;
    public static final int ACTION_MAIN_BACKUP = 0x101;

    private final int PING = 1;
    private final int TRACE_ROUTE = 2;
    private final int HTTP = 3;
    private String stationCode;

    private List<Integer> catchIdList = new ArrayList<>(); //暂存ID
    private List<Pair<Integer, Pair<TimerTask, Timer>>> timerList = new ArrayList<>(); //所有timer
    private CheckCtrlCmdsResult cmdsResult;
    private ClientIpBean clientIpBean;
    private final int default_interval = 180;  //预设间隔时间为60秒

    private LDNetDiagnoService _netDiagnoService;
    private String domainNameNew;
    private String encryptDomainName;
    private HttpURLConnection connection;
    private LDNetDiagnoListener listener;
    private GetProtocol getProtocol = new GetProtocol();


    private CompositeDisposable disposable = new CompositeDisposable();
    private boolean tryToCacheGameData = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(ACTION, ACTION_DEFAULT);
        Utils.LOG(TAG, "onStartCommand(), action = " + action);

        SysConfig config = UsualMethod.getConfigFromJson(this);
        if (action == ACTION_DEFAULT) {
            stationCode = config.getStationCode();
            startNetworkCheckingTimer();
        } else if (action == ACTION_MAIN_BACKUP) {
            tryToCacheGameData = "on".equals(config.getCache_data_local_switch());
            if (tryToCacheGameData) {
                disposable.clear();
//                fetchAllGamesDataPeriodically();
                fetchOpenResultPeriodically();
                fetchAcquireActives();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.LOG(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerList.size() > 0) {
//            Utils.LOG(TAG, "TimerList 清空前:" + timerList);
            for (int i = 0; i < timerList.size(); i++) {
                if (timerList.get(i).second.first != null) {
                    timerList.get(i).second.first.cancel();
//                    Utils.LOG(TAG, "TimerTask 服務停止" + i);
                }

                if (timerList.get(i).second.second != null) {
                    timerList.get(i).second.second.cancel();
//                    Utils.LOG(TAG, "Timer 服務停止" + i);
                }
            }
        }
        timerList.clear();
        disposable.clear();
//        Utils.LOG(TAG, "TimerList:" + timerList);
//        Utils.LOG(TAG, "TimerList 清空！");
        Utils.LOG(TAG, "Cmds 服務停止");
    }

    /**
     * 启动网路Cmds周期性检测
     */
    private void startNetworkCheckingTimer() {
        runOnThread(() -> {
            clientIpBean = new Gson().fromJson(Utils.getIp(), ClientIpBean.class);
            Timer cmdsTimer = new Timer();
            TimerTask cmdsTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (YiboPreference.instance(NetworkCheckingService.this).isStartNetworkService()) {
                        getCtrlCmds();
                    }
                }
            };
            cmdsTimer.schedule(cmdsTimerTask, 1000, 1000 * default_interval);
            timerList.add(new Pair<>(0, new Pair<>(cmdsTimerTask, cmdsTimer)));
            Utils.LOG(TAG, "Cmds 服務開始");
        });
    }


    /**
     * 取得后台检测对象清单
     */
    private void getCtrlCmds() {
        CrazyRequest<CrazyResult<CheckCtrlCmdsResult>> popRequest = null;
        StringBuilder popUrl = new StringBuilder();
        popUrl.append(Urls.CTRL_BASE).append(Urls.PORT).append(Urls.CTRL_CMDS);
        popUrl.append("?pid=" + BuildConfig.APPLICATION_ID);
        popRequest = new AbstractCrazyRequest.Builder().
                url(popUrl.toString())
                .seqnumber(CTRL_CMDS)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<CheckCtrlCmdsResult>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, popRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        int action = response.action;
        if (action == CTRL_CMDS) {
//            Utils.LOG(TAG, "Call 1st API: " + number++);
            CrazyResult<Object> result = response.result;
            if (result == null || !result.crazySuccess) {
                Utils.LOG(TAG, getString(R.string.acquire_fail));
            } else {
                cmdsResult = (CheckCtrlCmdsResult) result.result;
                //回传content内容不为空 开始网路监测服务
                //                    Utils.LOG(TAG, "Cmds content " + cmdsResult.getContent().size());

                if (cmdsResult.getContent() != null && cmdsResult.getContent().size() != 0) {
                    List<Integer> newIdList = new ArrayList<>();
                    for (int i = 0; i < cmdsResult.getContent().size(); i++) {
                        if (cmdsResult.getContent().get(i).getStationCode() != null){
                            //有站点名称，站点名称符合，ip符合
                            if(stationCode.equals(cmdsResult.getContent().get(i).getStationCode()) && cmdsResult.getContent().get(i).getClientIp() != null && clientIpBean!=null &&clientIpBean.getIp2().equals(cmdsResult.getContent().get(i).getClientIp())){
                                newIdList.add(cmdsResult.getContent().get(i).getId());
                                continue;
                            //有站点名称，站点名称符合
                            }else if(stationCode.equals(cmdsResult.getContent().get(i).getStationCode())){
                                newIdList.add(cmdsResult.getContent().get(i).getId());
                                continue;
                            }
                        }else{
                            //无站点名称，ip符合
                            if (cmdsResult.getContent().get(i).getClientIp() != null && clientIpBean!=null &&clientIpBean.getIp2().equals(cmdsResult.getContent().get(i).getClientIp())) {
                                newIdList.add(cmdsResult.getContent().get(i).getId());
                                continue;
                            //无站点名称，无ip
                            } else if (cmdsResult.getContent().get(i).getClientIp() == null) {
                                newIdList.add(cmdsResult.getContent().get(i).getId());
                            }
                        }
                    }
                    startNetworkCheck(newIdList);
                } else {
//                    Utils.LOG(TAG, "Cmds content 為空");
                }

            }
        } else if (action == CTRL_POST_CMDS) {
            CrazyResult<Object> result = response.result;
            if (result == null || !result.crazySuccess) {
                Utils.LOG(TAG, getString(R.string.acquire_fail));
            } else {
                CheckPostCmdsResult postCmdsResult = (CheckPostCmdsResult) result.result;
                if (postCmdsResult != null) {
                    Utils.LOG(TAG, "检测結果：" + postCmdsResult.getSuccess());
                }
            }
        } else if (action == LOTTERYS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result != null && result.crazySuccess) {
                FindAllGamesResponse encrypted = (FindAllGamesResponse) result.result;
                CacheRepository.getInstance().decryptAndSaveLotteryDataJson(getApplicationContext(), encrypted.getContent())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<String>() {
                            @Override
                            public void onSubscribe(Disposable d) { disposable.add(d); }

                            @Override
                            public void onSuccess(String json) {
                                EventBus.getDefault().post(new UpdateAllGameEvent(json));
                            }

                            @Override
                            public void onError(Throwable e) { e.printStackTrace(); }
                        });
            }
        } else if (action == DELEGATE_LOTTERYS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result != null && result.crazySuccess) {
                FindAllGamesResponseWrapper wrapper = (FindAllGamesResponseWrapper) result.result;
                FindAllGamesResponse encrypted = new Gson().fromJson(wrapper.getContent(), FindAllGamesResponse.class);
                if (encrypted != null) {
                    CacheRepository.getInstance().decryptAndSaveLotteryDataJson(getApplicationContext(), encrypted.getContent())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<String>() {
                                @Override
                                public void onSubscribe(Disposable d) { disposable.add(d); }

                                @Override
                                public void onSuccess(String json) {
                                    EventBus.getDefault().post(new UpdateAllGameEvent(json));
                                }

                                @Override
                                public void onError(Throwable e) { e.printStackTrace(); }
                            });
                }
            }
        } else if (action == LOAD_OPEN_RESULTS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result != null && result.crazySuccess) {
                OpenResultWraper wrapper = (OpenResultWraper) result.result;
                if (!wrapper.isSuccess()) {
                    Log.d(TAG, (!com.yibo.yiboapp.utils.Utils.isEmptyString(wrapper.getMsg()) ? wrapper.getMsg() :
                            getString(R.string.get_open_results_fail)));
                    return;
                }
                YiboPreference.instance(this).setToken(wrapper.getAccessToken());
                if (wrapper.getContent() != null)
                    CacheRepository.getInstance().saveOpenResultData(this, wrapper.getContent());
            }
        } else if (action == ACQUIRE_ACTIVES_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result != null && result.crazySuccess) {
                ActivesResultWraper wrapper = (ActivesResultWraper) result.result;
                if (!wrapper.isSuccess()) {
                    return;
                }

                if (wrapper.getContent() != null) {
                    CacheRepository.getInstance().saveAcquireActivesData(this, wrapper.getContent());
                }
            }
        }

    }

    /**
     * 检查 本地端 与 后台端 集合，做差集比对
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startNetworkCheck(List<Integer> newIdList) {
        // 差集 (newIdList - catchIdList)
        List<Integer> reduce1 = newIdList.stream().filter(item -> !catchIdList.contains(item)).collect(toList());
//        Utils.LOG(TAG, " ---差集 newIdList 與 catchIdList ---:" + reduce1);

        // 差集 (catchIdList - list2)
        List<Integer> reduce2 = catchIdList.stream().filter(item -> !newIdList.contains(item)).collect(toList());
//        Utils.LOG(TAG, " ---差集 catchIdList 與 newIdList ---:" + reduce2);

        //加入reduce1差集
        for (int i = 0; i < reduce1.size(); i++) {
            addTimerTask(reduce1.get(i));
        }

        //移除重复reduce2差集
        deleteRepeatReduce(reduce2);
    }

    public void addTimerTask(Integer reduceId) {
        CheckCtrlCmdsResult.CmdsBean cmdsBean = null;
        for (int i = 0; i < cmdsResult.getContent().size(); i++) {
            if (cmdsResult.getContent().get(i).getId().equals(reduceId)) {
                cmdsBean = cmdsResult.getContent().get(i);
            }
        }

        Timer timer = new Timer();
        CheckCtrlCmdsResult.CmdsBean finalCmdsBean = cmdsBean;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (Calendar.getInstance().getTimeInMillis() < finalCmdsBean.getDeadlineTime()) {
//                    Utils.LOG(TAG, "送出！" + finalCmdsBean.getId() + " Type:" + finalCmdsBean.getType());
                    listener = new LDNetDiagnoListener() {
                        @Override
                        public void OnNetDiagnoFinished(String log) {
                            if (YiboPreference.instance(NetworkCheckingService.this).isStartNetworkService()) {
                                //判断检测type类别
                                if (finalCmdsBean.getType() == HTTP) {
                                    new Thread(() -> {
                                        try {
                                            URL url = new URL(getProtocol.getProtocol(finalCmdsBean.getContent()));
                                            connection = (HttpURLConnection) url.openConnection();
                                            connection.setRequestMethod("GET");//設定訪問方式為“GET”
                                            connection.setConnectTimeout(8000);//設定連線伺服器超時時間為8秒
                                            connection.setReadTimeout(8000);//設定讀取伺服器資料超時時間為8秒
                                            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                                                //從伺服器獲取響應並把響應資料轉為字串列印
                                                InputStream in = connection.getInputStream();
                                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                                StringBuilder response = new StringBuilder();
                                                String line;
                                                while (null != (line = reader.readLine())) {
                                                    response.append(line);
                                                }
                                                if (YiboPreference.instance(NetworkCheckingService.this).isStartNetworkService()) {
                                                    postCheckingMessage(finalCmdsBean, clientIpBean, log + response.toString());
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            if (null != connection) {
                                                connection.disconnect();
                                            }
                                        }
                                    }).start();

                                } else {
                                    postCheckingMessage(finalCmdsBean, clientIpBean, log);
                                }

                            }
                        }

                        @Override
                        public void OnNetDiagnoUpdated(String log) {

                        }
                    };
//                    Utils.LOG(TAG, "TimerList : " + timerList.toString());
                    startChecking(finalCmdsBean, listener);
                } else {
//                    Utils.LOG(TAG, "时间到！ 移除timer" + finalCmdsBean.getId());

                    this.cancel();
                    timer.cancel();

                }
            }
        };
        if (finalCmdsBean.getInterval() != null) {
            if(finalCmdsBean.getInterval() == 0){
                timer.schedule(timerTask, 1000);
            }else {
                timer.schedule(timerTask, 1000, 1000 * finalCmdsBean.getInterval());
            }
        } else {
            timer.schedule(timerTask, 1000);
        }

        timerList.add(new Pair<>(reduceId, new Pair<>(timerTask, timer)));
        catchIdList.add(reduceId);
//        Utils.LOG(TAG, " ---加入reduce1差集---");
//        Utils.LOG(TAG, " ---暂存ID为---：" + catchIdList);
    }


    /**
     * 移除 本地端 与 后台端 重复交集
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteRepeatReduce(List<Integer> reduce2) {
        if (reduce2.size() == 0) {
//            Utils.LOG(TAG, " ---无重复交集---");
        } else {
//            Utils.LOG(TAG, " ---有重复交集---：" + reduce2.size());
            List<Integer> intersection = catchIdList.stream().filter(item -> reduce2.contains(item)).collect(toList());
//            Utils.LOG(TAG, " --- 重复交集为 ---：" + intersection);

            catchIdList.removeAll(intersection);
            for (int i = 0; i < timerList.size(); i++) {
                for (int j = 0; j < reduce2.size(); j++) {
                    if (timerList.get(i).first.equals(reduce2.get(j))) {
                        if (timerList.get(i).second.first != null) {
                            timerList.get(i).second.first.cancel();
//                            Utils.LOG(TAG, "ID " + reduce2.get(j) + "TimerTask 服務停止");
                        }

                        if (timerList.get(i).second.second != null) {
                            timerList.get(i).second.second.cancel();
//                            Utils.LOG(TAG, "ID " + reduce2.get(j) + "Timer 服務停止");
                        }
//                        Utils.LOG(TAG, " ---移除重复Timer---");
                        timerList.remove(i);
//                        Utils.LOG(TAG, " ---移除重复Timer为---：" + timerList);
                    }
                }
            }
//            Utils.LOG(TAG, " ---移除intersection交集---");
//            Utils.LOG(TAG, " ---移除重复交集暂存ID为---：" + catchIdList);
        }
    }

    /**
     * 根据cmdsBean的type执行 对应的路由检测 type 1 = PING  type 2 = TRACE_ROUTE  type 3 = HTTP
     */
    private void startChecking(CheckCtrlCmdsResult.CmdsBean cmdsBean, LDNetDiagnoListener listener) {
        runOnUiThread(() -> {
            String domainName = BuildConfig.domain_url;
            int count = 0;
            for (int i = 0; i < domainName.length(); i++) {
                if (":".equals(String.valueOf(domainName.charAt(i)))) {
                    count++;
                }
            }
            if (count == 1) {
                //没有端口
                domainNameNew = domainName.substring(domainName.lastIndexOf("/") + 1);
            } else {
                //有端口
                try {
                    domainNameNew = domainName.substring(domainName.lastIndexOf("/") + 1, domainName.lastIndexOf(":"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            encryptDomainName = crazy_wrapper.Crazy.Utils.Utils.encrypt(domainName);
            _netDiagnoService = new LDNetDiagnoService(this,
                    "testDemo", "网络诊断应用", "1.0.0", "huipang@corp.netease.com",
                    "deviceID(option)", cmdsBean.getContent(), "carriname", "ISOCountyCode",
                    "MobilCountryCode", "MobileNetCode", listener, encryptDomainName, cmdsBean.getType());
            // 设置是否使用JNIC 完成traceroute
            _netDiagnoService.setIfUseJNICTrace(true);
            _netDiagnoService.execute();
        });
    }


    /**
     * 提交网路检测
     */
    private void postCheckingMessage(CheckCtrlCmdsResult.CmdsBean bean, ClientIpBean ipBean, String log) {
        CrazyRequest<CrazyResult<CheckPostCmdsResult>> popRequest = null;
        StringBuilder popUrl = new StringBuilder();
        popUrl.append(Urls.CTRL_BASE).append(Urls.PORT).append(Urls.CTRL_RESULT);
        try {
            popUrl.append("?content=" + URLEncoder.encode(log, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            popUrl.append("?content=" + e.toString());
            e.printStackTrace();
        }
        popUrl.append("&stationCode=" + stationCode);
        popUrl.append("&username=" + YiboPreference.instance(this).getUsername());
        popUrl.append("&domain=" + BuildConfig.domain_url);
//        popUrl.append("&clientIp=" + ipBean.getIp2());
        popUrl.append("&taskNo=" + bean.getTaskNo());
        popRequest = new AbstractCrazyRequest.Builder().
                url(popUrl.toString())
                .seqnumber(CTRL_POST_CMDS)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<CheckPostCmdsResult>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();

        RequestManager.getInstance().startRequest(this, popRequest);
//        Utils.LOG(TAG, "提交：" + "StationCode:" + stationCode + " / IP:" + ipBean.getIp2());

    }

    private void fetchAllGamesDataPeriodically() {
//        Observable.interval(2, 10, TimeUnit.MINUTES)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) { disposable.add(d); }
//
//                    @Override
//                    public void onNext(Long aLong) { fetchDelegateAllGames(); }
//
//                    @Override
//                    public void onError(Throwable e) {}
//
//                    @Override
//                    public void onComplete() {}
//                });
        fetchDelegateAllGames();
    }

    private void fetchOpenResultPeriodically() {
        Observable.interval(2, 60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        fetchKaijianInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 请求取得彩种信息
     */
    private void fetchAllGames() {
        //获取彩种信息
        StringBuilder lotteryUrl = new StringBuilder();
        lotteryUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ALL_GAME_DATA_BACKUP);
        CrazyRequest<CrazyResult<FindAllGamesResponse>> lotteryRequest = new AbstractCrazyRequest.Builder().
                url(lotteryUrl.toString())
                .seqnumber(LOTTERYS_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(this))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<FindAllGamesResponse>() {}.getType()))
                .create();
        RequestManager.getInstance().startRequest(this, lotteryRequest);
    }

    /**
     * 透过代理取得彩种信息
     */
    private void fetchDelegateAllGames() {
        try {
            String data = CacheRepository.getInstance().getEncryptedDelegateData();
            //获取彩种信息
            String lotteryUrl = "https://api.ycyidc.com/receive/receiveData?data=" + data;
            CrazyRequest<CrazyResult<FindAllGamesResponseWrapper>> lotteryRequest = new AbstractCrazyRequest.Builder().
                    url(lotteryUrl)
                    .seqnumber(DELEGATE_LOTTERYS_REQUEST)
                    .listener(this)
                    .headers(Urls.getHeader(this))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<FindAllGamesResponseWrapper>() {}.getType()))
                    .create();
            RequestManager.getInstance().startRequest(this, lotteryRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得开奖公告信息
     */
    private void fetchKaijianInfo() {
        int start = 1;
        int pageSize = 20;
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.OPEN_RESULTS_URL);
        configUrl.append("?page=").append(start);
        configUrl.append("&rows=").append(pageSize);
        CrazyRequest<CrazyResult<OpenResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LOAD_OPEN_RESULTS_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .listener(this)
                .placeholderText(this.getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<OpenResultWraper>() {
                }.getType()))
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 取得优惠活动信息
     */
    private void fetchAcquireActives() {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACQUIRE_ACTIVES_URL_V2);
        CrazyRequest<CrazyResult<ActivesResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(ACQUIRE_ACTIVES_REQUEST)
                .headers(Urls.getHeader(this))
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .listener(this)
                .convertFactory(GsonConverterFactory.create(new TypeToken<ActivesResultWraper>() {
                }.getType()))
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }
}
