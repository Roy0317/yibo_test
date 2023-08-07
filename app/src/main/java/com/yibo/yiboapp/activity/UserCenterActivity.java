package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.AccountResult;
import com.yibo.yiboapp.entify.AccountResultWraper;
import com.yibo.yiboapp.entify.MemInfoWraper;
import com.yibo.yiboapp.entify.Meminfo;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.VIPCenterBean;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class UserCenterActivity extends BaseActivity implements SessionResponse.Listener
        <CrazyResult<Object>> {

    public static final String TAG = UserCenterActivity.class.getSimpleName();
    public static final int ACCOUNT_RECORD_REQUEST = 0x01;
    public static final int ACCOUNT_REQUEST = 0x02;

    TextView accountTV;
    TextView levelTV;
    ImageView levelIcon;
    TextView scoreTV;

    XListView listView;
    EmptyListView empty;
    ListAdapter recordAdapter;
    List<String> listDatas;
    private TextView tvCurrentUserLevel;
    private TextView tvCurrentLevel;
    private TextView tvNextLevel;
    private ProgressBar pbProgressBar;
    private RelativeLayout rl_vip_level_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        initView();

        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listDatas = new ArrayList<>();
        recordAdapter = new ListAdapter(this, listDatas, R.layout.user_center_item);
        listView.setAdapter(recordAdapter);

        getAccountInfo(this);

        SysConfig sysConfig = UsualMethod.getConfigFromJson(this);
        if (sysConfig != null) {
            boolean isOn = sysConfig.getUsercenter_level_show_switch().equalsIgnoreCase("on");
            if (isOn) {
                levelIcon.setVisibility(View.VISIBLE);
                levelTV.setVisibility(View.VISIBLE);
                rl_vip_level_view.setVisibility(VISIBLE);
            } else {
                levelIcon.setVisibility(View.GONE);
                levelTV.setVisibility(View.GONE);
                rl_vip_level_view.setVisibility(GONE);
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.user_center_str));
        listView = (XListView) findViewById(R.id.xlistview);
        empty = (EmptyListView) findViewById(R.id.empty_list);
        empty.setListener(emptyListviewListener);

        accountTV = (TextView) findViewById(R.id.account_name);
        levelTV = (TextView) findViewById(R.id.account_level);
        levelIcon = (ImageView) findViewById(R.id.level_icon);
        scoreTV = (TextView) findViewById(R.id.account_score);
        //用户等级条目
        tvCurrentUserLevel = findViewById(R.id.tv_current_user_level);
        tvCurrentLevel = findViewById(R.id.tv_current_level);
        pbProgressBar = findViewById(R.id.progress_bar);
        tvNextLevel = findViewById(R.id.tv_next_level);
        rl_vip_level_view = findViewById(R.id.rl_vip_level_view);

    }

    public void getAccountInfo(Context context) {
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
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, accountRequest);
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            getRecords(true);
        }
    };

    private void getRecords(boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACCOUNT_URL);

        CrazyRequest<CrazyResult<AccountResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(ACCOUNT_RECORD_REQUEST)
                .headers(Urls.getHeader(this))
                .cachePeroid(60 * 1000)
                .shouldCache(true).placeholderText(getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<AccountResultWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    public class ListAdapter extends LAdapter<String> {

        Context context;

        public ListAdapter(Context mContext, List<String> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;

        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final String item) {
            TextView nameTV = holder.getView(R.id.name);
            nameTV.setText(item);
        }
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
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
        if (action == ACCOUNT_RECORD_REQUEST) {

            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.get_record_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.get_record_fail);
                return;
            }
            Object regResult = result.result;
            AccountResultWraper reg = (AccountResultWraper) regResult;

            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.get_record_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() == null) {
                return;
            }
            AccountResult accountResult = reg.getContent();
            SysConfig sysConfig = UsualMethod.getConfigFromJson(this);
            if (sysConfig != null) {
                boolean isOn = sysConfig.getUsercenter_level_show_switch().equalsIgnoreCase("on");
                if (isOn) {
                    refreshVipLevel(accountResult);
                }
            }

            listDatas.clear();
//            if (!Utils.isEmptyString(accountResult.getAccount())) {
            listDatas.add("帐号：" + (!Utils.isEmptyString(accountResult.getAccount()) ? accountResult.getAccount() : ""));
//            }

//            if (!Utils.isEmptyString(accountResult.getUserName())) {
            listDatas.add("真实姓名：" + (!Utils.isEmptyString(accountResult.getUserName()) ? accountResult.getUserName() : ""));
//            }

//            listDatas.add("姓别：" + accountResult.getSex());

            String phone = accountResult.getPhone();
            if (!Utils.isEmptyString(accountResult.getPhone())) {
                phone = Utils.hideChar(accountResult.getPhone(), 4);
            }
            listDatas.add("手机号码：" + (!Utils.isEmptyString(phone) ? phone : ""));

//            if (!Utils.isEmptyString(accountResult.getEmail())) {
            listDatas.add("邮箱：" + (!Utils.isEmptyString(accountResult.getEmail()) ? accountResult.getEmail() : ""));
//            }

//            if (!Utils.isEmptyString(accountResult.getQq())) {
            listDatas.add("QQ：" + (!Utils.isEmptyString(accountResult.getQq()) ? accountResult.getQq() : ""));
//            }

//            if (!Utils.isEmptyString(accountResult.getWechat())) {
            listDatas.add("微信：" + (!Utils.isEmptyString(accountResult.getWechat()) ? accountResult.getWechat() : ""));
//            }

//            if (!Utils.isEmptyString(accountResult.getCardNo())) {
            listDatas.add("卡号：" + (!Utils.isEmptyString(accountResult.getCardNo()) ? accountResult.getCardNo() : ""));
//            }

//            if (!Utils.isEmptyString(accountResult.getBankName())) {
            listDatas.add("开户行：" + (!Utils.isEmptyString(accountResult.getBankName()) ? accountResult.getBankName() : ""));
//            }

//            if (!Utils.isEmptyString(accountResult.getBankAddress())) {
            listDatas.add("开户行地址：" + (!Utils.isEmptyString(accountResult.getBankAddress()) ? accountResult.getBankAddress() : ""));
//            }
            recordAdapter.notifyDataSetChanged();
        } else if (action == ACCOUNT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                rl_vip_level_view.setVisibility(GONE);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            MemInfoWraper reg = (MemInfoWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新帐户名，余额等信息
            updateAccount(reg.getContent());
            getRecords(false);
        }
    }

    //更新帐户相关信息
    private void updateAccount(Meminfo meminfo) {
        if (meminfo == null) {
            return;
        }
        String accountName = !Utils.isEmptyString(meminfo.getAccount()) ? meminfo.getAccount() : "暂无名称";
        accountTV.setText(String.format("会员帐号：%s", accountName));

        String hide_member_level = UsualMethod.getConfigFromJson(this).getHide_member_level();

        if (!hide_member_level.equalsIgnoreCase("on")) {
            if (!Utils.isEmptyString(meminfo.getLevel_icon())) {
                UsualMethod.updateLocImageWithUrl(this, levelIcon, meminfo.getLevel_icon(), R.drawable.default_placeholder_picture);
            }
            levelTV.setText(String.format("会员等级：%s", meminfo.getLevel()));
        }


        if (UsualMethod.getConfigFromJson(this).getMny_score_show().equalsIgnoreCase("on")) {
            scoreTV.setVisibility(View.VISIBLE);
            scoreTV.setText(String.format("会员积分：%d", meminfo.getScore()));
        } else {
            scoreTV.setVisibility(View.GONE);
        }
    }


    public void refreshVipLevel(AccountResult vipCenterLevelBean) {
        if (vipCenterLevelBean != null && vipCenterLevelBean.getLevelInfo() != null) {
            //再存款多少可升级
            String newLevelDiffMoney = vipCenterLevelBean.getLevelInfo().getDiffMoney();
            //用户达到下一等级名称
            String newLevelName = "";
            //到大下级需存款
            String newLevelDepositMoney = "";
            //下级需打码
            String nextBetNum = "";
            if (vipCenterLevelBean.getLevelInfo().getNextLevel() != null) {

                newLevelName = vipCenterLevelBean.getLevelInfo().getNextLevel().getLevelName();

                newLevelDepositMoney = vipCenterLevelBean.getLevelInfo().getNextLevel().getDepositMoney();
                nextBetNum = vipCenterLevelBean.getLevelInfo().getNextLevel().getBetNum() + "";
            }
            //用户当前存款
            String accDepositTotal = vipCenterLevelBean.getLevelInfo().getAllMoney() + "";
            //用户当前等级名称
            String curLevelName = vipCenterLevelBean.getLevelInfo().getLevelName();
            //已打码
            String allBetNum = vipCenterLevelBean.getLevelInfo().getAllBetNum() + "";

            //再打码多少可升级
            String diffBetNum = vipCenterLevelBean.getLevelInfo().getDiffBetNum() + "";

            AccountResult.LevelInfoBean.NextLevelBean nextLevel = vipCenterLevelBean.getLevelInfo().getNextLevel();
            //此时是最高等级
            if (nextLevel == null) {
                tvCurrentUserLevel.setText("当前已为最高级");
                tvCurrentLevel.setText(curLevelName);
                tvNextLevel.setText("");
                pbProgressBar.setProgress(100);
            } else {
                double v = 0;
                switch (nextLevel.getLevelType()) {
                    case 1:
                        tvCurrentUserLevel.setText("再充值:" + newLevelDiffMoney + "元即可升级");
                        tvCurrentLevel.setText(curLevelName + "(已存款" + accDepositTotal + "元)");
                        tvNextLevel.setText(newLevelName + "(需存款" + newLevelDepositMoney + "元)");
                        if (!TextUtils.isEmpty(newLevelDepositMoney) && !TextUtils.isEmpty(accDepositTotal)) {
                            v = Double.valueOf(accDepositTotal) / Double.valueOf(newLevelDepositMoney) * 100;
                        }

                        break;
                    case 2:
                        tvCurrentUserLevel.setText("再打码:" + diffBetNum + "元即可升级");
                        tvCurrentLevel.setText(curLevelName + "(已打码" + allBetNum + "元)");
                        tvNextLevel.setText(newLevelName + "(需打码" + nextBetNum + "元)");
                        if (!TextUtils.isEmpty(nextBetNum) && !TextUtils.isEmpty(allBetNum)) {
                            v = Double.valueOf(allBetNum) / Double.valueOf(nextBetNum) * 100;
                        }
                        break;
                    case 3:
                        tvCurrentUserLevel.setText("再充值:" + newLevelDiffMoney + "元，打码" + diffBetNum + "元即可升级");
                        tvCurrentLevel.setText(curLevelName + "(已存款" + accDepositTotal + "元，打码" + allBetNum + "元)");
                        tvNextLevel.setText(newLevelName + "(需存款" + newLevelDepositMoney + "元,打码" + nextBetNum + "元)");
                        if (!TextUtils.isEmpty(nextBetNum) && !TextUtils.isEmpty(allBetNum) && !TextUtils.isEmpty(newLevelDepositMoney) && !TextUtils.isEmpty(accDepositTotal)) {
                            v = Double.valueOf(allBetNum + accDepositTotal) / Double.valueOf(nextBetNum + newLevelDepositMoney) * 100;
                        }
                        break;


                }
                long round = Math.round(v);
                int progress = Integer.parseInt(String.valueOf(round));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pbProgressBar.setProgress(progress, true);
                } else {
                    pbProgressBar.setProgress(progress);
                }

            }
        }
    }
}
