package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.SportExpandListAdapter;
import com.yibo.yiboapp.data.SportMainListAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.MoneyRecordResult;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.SportBean;
import com.yibo.yiboapp.entify.SportBet;
import com.yibo.yiboapp.entify.SportBetData;
import com.yibo.yiboapp.entify.SportBetResultWraper;
import com.yibo.yiboapp.entify.SportData;
import com.yibo.yiboapp.entify.SportDataWraper;
import com.yibo.yiboapp.entify.SportGameCount;
import com.yibo.yiboapp.entify.ValidBetContent;
import com.yibo.yiboapp.entify.ValidBetWraper;
import com.yibo.yiboapp.ui.BallonView;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.PopupListMenu;
import com.yibo.yiboapp.ui.ProgressWheel;
import com.yibo.yiboapp.ui.SportBetOrderWindow;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.PagerSlidingTabStrip;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

import static com.yibo.yiboapp.ui.SportTableContainer.BASKETBALL_PAGE;
import static com.yibo.yiboapp.ui.SportTableContainer.FOOTBALL_PAGE;

/**
 * @author johnson
 * 体育投注页
 */
public class SportActivity extends FragmentActivity implements
        View.OnClickListener, SessionResponse.Listener<CrazyResult<Object>>, SportExpandListAdapter.SportResultItemClick {

    public static final String TAG = SportActivity.class.getSimpleName();
    PagerSlidingTabStrip tabbar = null;
    ViewPager pager;

    protected RelativeLayout mLayout = null;
    protected TextView tvBackText;
    protected TextView tvMiddleTitle;
    protected TextView tvRightText;
    protected TextView tvSecondTitle;
    protected LinearLayout middle_layout;
    static ProgressWheel progressWheel;
    LinearLayout segmentLayout;
    TextView leftSegment;
    TextView rightSegment;
    protected RelativeLayout rightLayout;

    TextView zhushuView;
    TextView gameCountView;
    BallonView refreshBtn;
    BallonView betBtn;

    Button page_btn;


    ListFragment gunQiuFragment;
    ListFragment saiShiFragment;
    ListFragment zaoPanFragment;

    List<List<Map>> gunQiuDatas;
    List<List<Map>> saiShiDatas;
    List<List<Map>> zaoPanDatas;

    int pageIndex = 1;
    int pageCount = 0;
    public static final int GET_RECORD = 0x01;
    public static final int POST_SPORT_BET = 0x02;
    public static final int POST_VALID_SPORT_BET = 0x03;
    String ftCategory = "MN";
    String bkCategory = "MN";
    String ftCategoryValue = "全部";
    String bkCategoryValue = "全部";

    int currentBallPage = FOOTBALL_PAGE;//当前segment页码

    private final static String[] BALL_TYPES = new String[]{"FT", "BK"};
    private final static String[] GAME_TYPES = new String[]{"RB", "TD", "FT"};
    String[] ftPlayCodes = new String[]{"MN", "TI", "BC", "HF", "MX"};
    String[] bkPlayCodes = new String[]{"MN", "MX"};
    String[] fb_categorys = new String[]{"全部", "波胆", "总入球", "半场/全场", "混合过关", "下注记录", "体育规则"};
    String[] bb_categorys = new String[]{"全部", "混合过关", "下注记录", "体育规则"};
    String[] bet_records = new String[]{"下注记录"};

    String[] tabNames = null;
    PagerAdapter contentAdapter;
    List<List<Map>> results = new ArrayList<>();
    public static final int CALC_SPORT_DATA = 0x01;
    MyHandler myHandler;
    List<SportBean> selectSports = null;
    String inputMoney;
    boolean appcetBestPeilv;
    int selectedBallType;
    String selectedCategoryType;
    String selectedPlayType;
    SportBetOrderWindow betWindow;
    CountDownTimer counterTimer;
    public static final long REFRESH_DURATION = 180 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.LOG(TAG, "onCreate()");
        Utils.useTransformBar(this);
        setContentView(R.layout.activity_sport);
        initView();

        gunQiuDatas = new ArrayList<>();
        saiShiDatas = new ArrayList<>();
        zaoPanDatas = new ArrayList<>();


        Type listType = new TypeToken<ArrayList<List<Map>>>() {
        }.getType();
        String gunQiuDatasJson = new Gson().toJson(gunQiuDatas, listType);
        String saiShiDatasJson = new Gson().toJson(saiShiDatas, listType);
        String zaoPanDatasJson = new Gson().toJson(zaoPanDatas, listType);

        gunQiuFragment = ListFragment.getInstance(gunQiuDatasJson, 0);
        saiShiFragment = ListFragment.getInstance(saiShiDatasJson, 1);
        zaoPanFragment = ListFragment.getInstance(zaoPanDatasJson, 1);


        myHandler = new MyHandler(this);
        selectSports = new ArrayList<>();

        tabNames = getResources().getStringArray(R.array.sport_tabs);
        contentAdapter = new PagerAdapter(this, getSupportFragmentManager(), tabNames);
        pager.setAdapter(contentAdapter);
        tabbar.setViewPager(pager);
        pager.setCurrentItem(pageIndex);
        tabbar.setTextColor(R.color.dark_grey);
        tabbar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Utils.LOG("aa", "the select position = " + position);
                if (position == 0) {
                    tvRightText.setText("下注记录");
                } else {
                    tvRightText.setText("全部 ");
                }
//                tvRightText.setVisibility(position == 0 ? View.GONE:View.VISIBLE);
                //滑动选择球赛分类时先清除之前选择过的赔率项
                actionClear();
                getRecords(SportActivity.this, pageIndex, currentBallPage, pager.getCurrentItem(), getCurrentCategory(), true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        switchSegment(FOOTBALL_PAGE);
    }

    private String getCurrentCategory() {
        return currentBallPage == FOOTBALL_PAGE ? ftCategory : bkCategory;
    }


    protected void initView() {
        mLayout = (RelativeLayout) findViewById(R.id.title);
        if (mLayout != null) {
            tvBackText = (TextView) mLayout.findViewById(R.id.back_text);
            middle_layout = (LinearLayout) mLayout.findViewById(R.id.middle_layout);
            tvMiddleTitle = (TextView) middle_layout.findViewById(R.id.middle_title);
            tvMiddleTitle.setText(getIntent().getStringExtra("gameName"));
            tvRightText = (TextView) mLayout.findViewById(R.id.right_text);
            tvSecondTitle = (TextView) findViewById(R.id.second_title);
            tvSecondTitle.setVisibility(View.GONE);
            progressWheel = (ProgressWheel) mLayout.findViewById(R.id.progress_wheel);
            segmentLayout = (LinearLayout) mLayout.findViewById(R.id.segment);
            leftSegment = (TextView) segmentLayout.findViewById(R.id.left);
            rightSegment = (TextView) segmentLayout.findViewById(R.id.right);
            leftSegment.setOnClickListener(this);
            rightSegment.setOnClickListener(this);

            tvBackText.setOnClickListener(this);
            tvMiddleTitle.setOnClickListener(this);
            tvRightText.setOnClickListener(this);
            tvBackText.setVisibility(View.VISIBLE);
            tvRightText.setVisibility(View.VISIBLE);
            Drawable rightDrawable = getResources().getDrawable(R.drawable.arrow_down);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tvRightText.setCompoundDrawables(null, null, rightDrawable, null);
            tvRightText.setCompoundDrawablePadding(10);
            rightLayout = (RelativeLayout) findViewById(R.id.right_layout);

            tvMiddleTitle.setVisibility(View.GONE);
            segmentLayout.setVisibility(View.VISIBLE);
        }
        tabbar = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

        zhushuView = (TextView) findViewById(R.id.order_num);
        gameCountView = (TextView) findViewById(R.id.game_count);
        refreshBtn = (BallonView) findViewById(R.id.refresh_btn);
        betBtn = (BallonView) findViewById(R.id.touzhu_btn);

        page_btn = (Button) findViewById(R.id.page_btn);
        page_btn.setOnClickListener(this);

        zhushuView.setOnClickListener(this);
        gameCountView.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        betBtn.setOnClickListener(this);
        findViewById(R.id.setting).setOnClickListener(this);

    }

    private void createRefreshTimer(final long duration) {
        counterTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished;
                tvSecondTitle.setText(String.format(getString(R.string.left_refresh_game_counter_format),
                        String.valueOf(time / 1000)));
            }

            public void onFinish() {
                tvSecondTitle.setVisibility(View.GONE);
                startProgress();
                getRecords(SportActivity.this, pageIndex, currentBallPage, pager.getCurrentItem(), getCurrentCategory(), false);
            }
        };
    }

    /**
     * 获取体育数据
     *
     * @param context
     * @param pageIndex    当前页码
     * @param segmentPage  足球或篮球分栏页索引
     * @param currentPage  当前页索引，0-滚球页 1-赛事 2-早盘
     * @param categoryType 玩法分类
     * @param showDialog   是否显示加载框
     */
    private static void getRecords(Context context, int pageIndex, int segmentPage,
                                   int currentPage, String categoryType, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SPORT_DATA_URL);
        configUrl.append("?gameType=").append(BALL_TYPES[segmentPage]).append("_")
                .append(getDateStrByPageIndex(currentPage)).append("_")
                .append(categoryType);

        configUrl.append("&pageNo=" + pageIndex);
        configUrl.append("&sortType=1");
        CrazyRequest<CrazyResult<SportDataWraper>> request = new AbstractCrazyRequest.Builder()
                .url(configUrl.toString())
                .seqnumber(GET_RECORD)
                .headers(Urls.getHeader(context))
                .placeholderText(context.getString(R.string.get_game_ongoing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<SportDataWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    private static void getRecords(Context context, int currentPage, String categoryType, boolean showDialog) {
        getRecords(context, 1, FOOTBALL_PAGE, currentPage, categoryType, showDialog);
    }

    private static String getDateStrByPageIndex(int page) {
        return GAME_TYPES[page];
    }

    private void switchSegment(int page) {
        //选择篮球或足球时先清除选择的赔率项
        actionClear();
        if (page == FOOTBALL_PAGE) {
            leftSegment.setBackgroundResource(R.drawable.top_segment_left_press);
            leftSegment.setTextColor(getResources().getColor(R.color.colorPrimary));

            rightSegment.setBackgroundResource(R.drawable.top_segment_right_normal);
            rightSegment.setTextColor(getResources().getColor(R.color.colorWhite));

            currentBallPage = FOOTBALL_PAGE;
            pageIndex = 1;
            if (pager.getCurrentItem() == 0) {
                tvRightText.setText("下注记录");
            } else {
                tvRightText.setText(ftCategoryValue);
            }
            getRecords(this, pageIndex, currentBallPage, pager.getCurrentItem(), getCurrentCategory(), true);

        } else {
            leftSegment.setBackgroundResource(R.drawable.top_segment_left_normal);
            leftSegment.setTextColor(getResources().getColor(R.color.colorWhite));

            rightSegment.setBackgroundResource(R.drawable.top_segment_right_press);
            rightSegment.setTextColor(getResources().getColor(R.color.colorPrimary));

            currentBallPage = BASKETBALL_PAGE;
            pageIndex = 1;
            if (pager.getCurrentItem() == 0) {
                tvRightText.setText("下注记录");
            } else {
                tvRightText.setText(bkCategoryValue);
            }
            getRecords(this, pageIndex, currentBallPage, pager.getCurrentItem(), getCurrentCategory(), true);
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        String[] tabNames = null;
        Context context;

        public PagerAdapter(Context context, FragmentManager fm, String[] tabNames) {
            super(fm);
            this.context = context;
            this.tabNames = tabNames;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return gunQiuFragment;
            } else if (position == 1) {
                return saiShiFragment;
            } else if (position == 2) {
                return zaoPanFragment;
            }
            return null;
        }

    }


    private static final class EmptyListener implements EmptyListView.EmptyListviewListener {

        Context context;
        int position;
        String category;

        EmptyListener(Context context, int postion, String category) {
            this.context = context;
            this.position = postion;
            this.category = category;
        }

        @Override
        public void onEmptyListviewClick() {
            getRecords(context, position, category, true);
        }
    }

    /**
     * 充值fragment
     */
    public static class ListFragment extends Fragment {

        List<List<Map>> datas;
        SportMainListAdapter gameAdapter;
        ListView listView;
        int position;
        String categoryType;
        EmptyListView empty;

        public static ListFragment getInstance(String datas, int position) {

            ListFragment instance = new ListFragment();
            Bundle args = new Bundle();
            args.putString("datas", datas);
            args.putInt("position", position);
            instance.setArguments(args);
            return instance;
        }

        public ListFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Type listType = new TypeToken<ArrayList<MoneyRecordResult>>() {
            }.getType();
            datas = new Gson().fromJson(getArguments().getString("datas"), listType);
            position = getArguments().getInt("position");
            gameAdapter = new SportMainListAdapter(getActivity(), datas, R.layout.sport_list_item);
            gameAdapter.setUpdateSubListListener(new SportMainListAdapter.UpdateSubListListener() {
                @Override
                public void onUpdate(int position, SportExpandListAdapter adapter) {
                    updateView(position, adapter);
                    stopProgress();
                }

                @Override
                public void beforeUpdate(int position) {
//                    showToast("正在获取...");
                    startProgress();

                }

                @Override
                public void afterUpdate(int position) {
                    stopProgress();
                }
            });
            gameAdapter.setResultItemListener((SportExpandListAdapter.SportResultItemClick) getActivity());
        }

        public void stopLoading() {
            empty.setShowText(getString(R.string.temp_no_saishi_data));
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
        }

        public void updateData(List<List<Map>> data, int ballType, String gameCategory, String playType) {
            stopLoading();
            if (data != null) {
                this.datas.clear();
                this.datas.addAll(data);
                gameAdapter.updateType(ballType, gameCategory, playType);
                gameAdapter.notifyDataSetChanged();
            }
        }

        private void updateView(int itemIndex, SportExpandListAdapter adapter) {
            int visiblePosition = listView.getFirstVisiblePosition();
            if (itemIndex - visiblePosition >= 0) {
                View view = listView.getChildAt(itemIndex - visiblePosition);
                if (view != null && adapter != null) {
                    XListView subListView = (XListView) view.findViewById(R.id.xlistview);
                    subListView.setAdapter(adapter);
                    List<Map> maps = datas.get(itemIndex);
                    boolean isExpand = maps.get(0).containsKey("expand") && (Boolean) maps.get(0).get("expand");
                    maps.get(0).put("expand", !isExpand);
                    gameAdapter.updateView(view, itemIndex, !isExpand);
                }
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.sport_main_list, container, false);
            listView = (ListView) view.findViewById(R.id.xlistview);
            empty = (EmptyListView) view.findViewById(R.id.empty);
            empty.setListener(new EmptyListener(getActivity(), position, categoryType));
            listView.setDivider(null);
            listView.setAdapter(gameAdapter);
            return view;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_text:
                finish();
                break;
            case R.id.left:
                switchSegment(FOOTBALL_PAGE);
                break;
            case R.id.right:
                switchSegment(BASKETBALL_PAGE);
                break;
            case R.id.right_text:
                onRightClick();
                break;
            case R.id.refresh_btn:
                startProgress();
                pageIndex = 1;
                getRecords(SportActivity.this, pageIndex, currentBallPage, pager.getCurrentItem(), getCurrentCategory(), false);
                break;
            case R.id.order_num:
            case R.id.game_count:
            case R.id.setting:
                actionViewOrder(selectSports, true);
                break;
            case R.id.page_btn:
                showChoosePageList();
                break;
            case R.id.touzhu_btn:
                if (selectSports.isEmpty()) {
                    showToast(R.string.please_touzhu_first);
                    return;
                }
                if (Utils.isEmptyString(inputMoney)) {
                    showToast(R.string.input_peilv_money);
                    return;
                }
                if (!Utils.isNumeric(inputMoney)) {
                    showToast(R.string.input_money_must_be_zs);
                    return;
                }
                SportBetData sportBetData = formBetOrders(selectedBallType, selectedCategoryType,
                        selectedPlayType, selectSports, appcetBestPeilv, inputMoney, 0, 0);
                actionValidBets(sportBetData);
                break;
        }
    }

    private String[] getPages() {
        if (pageCount == 0) {
            return null;
        }
        String[] pages = new String[pageCount];
        for (int i = 0; i < pageCount; i++) {
            pages[i] = String.valueOf(i + 1);
        }
        return pages;
    }

    /**
     * 切换页码的弹框
     */
    private void showChoosePageList() {
        String[] stringItems = getPages();
        if (stringItems == null) {
            return;
        }
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("选择页码");
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                pageIndex = position + 1;
                getRecords(SportActivity.this, pageIndex,
                        currentBallPage, pager.getCurrentItem(), getCurrentCategory(), true);
            }
        });
    }

    private void actionClear() {
        selectSports.clear();
        selectedBallType = 0;
        selectedCategoryType = "";
        selectedPlayType = "";
        inputMoney = "";
        appcetBestPeilv = false;
        updateBottom(0, 0);
    }

    private void actionClearWhenSelectPage() {
        selectedBallType = 0;
        selectedCategoryType = "";
        inputMoney = "";
        appcetBestPeilv = false;
        //串关时不清除旧已选记录，因为混合过关支持多个
        if (!selectedPlayType.equals(Constant.BK_MX) && !selectedPlayType.equals(Constant.FT_MX)) {
            selectSports.clear();
            updateBottom(0, 0);
            selectedPlayType = "";
        }

    }

    private void actionViewOrder(List<SportBean> sportBeans, boolean shoudong) {
        showGameOrderWindow(sportBeans, shoudong);
    }

    private void actionBets(SportBetData sportBetData) {
        if (sportBetData == null) {
            return;
        }
        String postJson = new Gson().toJson(sportBetData, SportBetData.class);
        postSportBets(postJson);
    }

    private void actionValidBets(SportBetData sportBetData) {
        if (sportBetData == null) {
            return;
        }
        String postJson = new Gson().toJson(sportBetData, SportBetData.class);
        postValidBets(postJson);
    }

    private void postValidBets(String postJson) {
        try {
            StringBuilder url = new StringBuilder();
            url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SPORT_VALID_BETS_URL);
            url.append("?data=").append(URLEncoder.encode(postJson, "utf-8"));
            CrazyRequest<CrazyResult<ValidBetWraper>> request = new AbstractCrazyRequest.Builder()
                    .url(url.toString())
                    .seqnumber(POST_VALID_SPORT_BET)
                    .headers(Urls.getHeader(this))
                    .placeholderText(getString(R.string.post_valid_bet))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<ValidBetWraper>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actionRecords() {
//        SysConfig config = UsualMethod.getConfigFromJson(this);
//        if (config.getNew_onoff_sports_game().equalsIgnoreCase("on")) {
//            RecordsActivity.createIntent(this, "新体育投注记录", Constant.SPORTS_RECORD_STATUS,
//                    currentBallPage, true);
//        } else {
//            RecordsActivity.createIntent(this, "旧体育投注记录", Constant.OLD_SPORTS_RECORD_STATUS,
//                    currentBallPage, true);
//        }
        RecordsActivity.createIntent(this, "体育投注记录", Constant.SPORTS_RECORD_STATUS,
                currentBallPage, true);
    }

    private void onRightClick() {

        if (pager.getCurrentItem() == 0) {
            actionRecords();
        } else {
            final String[] arrays = currentBallPage == FOOTBALL_PAGE ? fb_categorys : bb_categorys;
            PopupListMenu menu = new PopupListMenu(this, arrays);
            menu.setBackground(R.drawable.caipiao_item_bg);
            menu.setDimEffect(true);
            menu.setAnimation(true);
            menu.setOnItemClickListener(new PopupListMenu.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    switch (position) {
                        default:
                            if (position == arrays.length - 1) {
                                String url = Urls.BASE_URL + Urls.PORT + getString(R.string.sports_rule);
                                ActiveDetailActivity.createIntent(SportActivity.this, "", "体育规则", url);
                            } else if (position == arrays.length - 2) {
                                actionRecords();
                            } else {
                                String category = arrays[position];
                                if (Utils.isEmptyString(category)) {
                                    return;
                                }
                                if (currentBallPage == 0) {
                                    ftCategory = ftPlayCodes[position];
                                    ftCategoryValue = category;
                                } else if (currentBallPage == 1) {
                                    bkCategory = bkPlayCodes[position];
                                    bkCategoryValue = category;
                                }
                                tvRightText.setText(category);
                                //筛选比赛玩法时，先清除之前选择过的赔率项
                                actionClear();
                                onCategoryClick(getCurrentCategory());
                            }
                            break;
                    }
                }
            });
            menu.show(rightLayout, 0, 5);
        }
    }

    /**
     * 右上角玩法过滤下拉菜单项点击事件
     *
     * @param categoryType
     */
    private void onCategoryClick(String categoryType) {
        pageIndex = 1;
        getRecords(this, pageIndex, currentBallPage, pager.getCurrentItem(), categoryType, true);
    }

    protected void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int showText) {
        Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == GET_RECORD) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_fail);
                return;
            }
            Object regResult = result.result;
            SportDataWraper reg = (SportDataWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            Utils.LOG(TAG, "sport data = " + reg.getContent());
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //开启异步线程，更新体育数据
            new CalcSportDataThread(reg.getContent()).start();
        } else if (action == POST_SPORT_BET) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.dobets_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.dobets_fail);
                return;
            }
            Object regResult = result.result;
            SportBetResultWraper reg = (SportBetResultWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.dobets_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (!YiboPreference.instance(SportActivity.this).getSportBetShow()) {
                showSportOrderDialog();
            } else {
                showToast("下注成功");
            }
//            saveCurrentLotData();
            actionClear();
        } else if (action == POST_VALID_SPORT_BET) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.validate_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.validate_fail);
                return;
            }
            Object regResult = result.result;
            ValidBetWraper reg = (ValidBetWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.dobets_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            ValidBetContent content = reg.getContent();
            if (content != null) {
                if (content.getOdds() != null && !content.getOdds().isEmpty()) {
                    if (content.getOdds().size() == this.selectSports.size()) {
                        for (int i = 0; i < content.getOdds().size(); i++) {
                            SportBean bean = this.selectSports.get(i);
                            if (bean != null) {
                                bean.setGid(String.valueOf(content.getOdds().get(i).getGid()));
                                bean.setPeilv(String.valueOf(content.getOdds().get(i).getOdds()));
                            }
                        }
                    }
                    SportBetData sportBetData = formBetOrders(selectedBallType, selectedCategoryType,
                            selectedPlayType, selectSports, appcetBestPeilv,
                            inputMoney, 0, 0);
                    actionBets(sportBetData);
                }
            }
        }
    }

    private void showSportOrderDialog() {

        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        String content = "下注成功，是否查看注单？";
        ccd.setContent(content);
        ccd.setToastShow(true);
        ccd.setOnToastBtnClick(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                YiboPreference.instance(SportActivity.this).setSportBetShow(true);
            }
        });
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
                actionRecords();
            }
        });
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private void saveCurrentLotData() {
        SavedGameData data = new SavedGameData();
        data.setGameModuleCode(SavedGameData.SPORT_GAME_MODULE);
        data.setAddTime(System.currentTimeMillis());
        data.setBallType(BALL_TYPES[currentBallPage]);
        data.setGameType(GAME_TYPES[pageIndex]);
        data.setFtPlayCode(ftCategory);
        data.setBkPlayCode(bkCategory);
        data.setLotName((currentBallPage == FOOTBALL_PAGE ? "足球" : "篮球") + "-" + (currentBallPage == FOOTBALL_PAGE ?
                ftCategoryValue : bkCategoryValue));
        UsualMethod.localeGameData(this, data);
    }

    private class CalcSportDataThread extends Thread {

        String sportJson;

        CalcSportDataThread(String sportJson) {
            this.sportJson = sportJson;
        }

        @Override
        public void run() {
            super.run();

            if (Utils.isEmptyString(sportJson)) {
                myHandler.sendEmptyMessage(CALC_SPORT_DATA);
                return;
            }
            if (Utils.isEmptyString(sportJson)) {
                return;
            }
            sportJson = sportJson.replaceAll("</font>", "");
            SportData sportData = null;
            try {
                sportData = new Gson().fromJson(sportJson, SportData.class);
                if (sportData == null) {
                    myHandler.sendEmptyMessage(CALC_SPORT_DATA);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                myHandler.sendEmptyMessage(CALC_SPORT_DATA);
                return;
            }
            if (sportData == null) {
                myHandler.sendEmptyMessage(CALC_SPORT_DATA);
                return;
            }

            pageCount = sportData.getPageCount();
            Map<String, Object> results = new HashMap<>();
            //更新滚球，今日赛事，早盘的 足球和篮球赛数
            SportGameCount gameCount = sportData.getGameCount();
            results.put("counts", gameCount);

            //更新赛事列表
            JsonArray games = sportData.getGames();
            JsonArray headers = sportData.getHeaders();
            if (games == null || games.size() == 0) {
                Message msg = myHandler.obtainMessage(CALC_SPORT_DATA, results);
                myHandler.sendMessage(msg);
                return;
            }
            if (headers == null || headers.size() == 0) {
                Message msg = myHandler.obtainMessage(CALC_SPORT_DATA, results);
                myHandler.sendMessage(msg);
                return;
            }
            LinkedList<Map<String, Object>> datas = new LinkedList<>();
            try {
                for (int i = 0; i < games.size(); i++) {
                    JsonArray item = (JsonArray) games.get(i);
                    if (item == null) {
                        continue;
                    }
                    if (item.size() != headers.size()) {
                        continue;
                    }
                    //将每项赛事数据依据headers数据项一一对应装入Map
                    int itemLength = item.size();
                    int headerLength = headers.size();
                    Map<String, Object> map = new HashMap<>();
                    for (int j = 0; j < itemLength && itemLength == headerLength; j++) {
                        if (item.get(j).getAsString().contains("<font")) {
                            int s = item.get(j).getAsString().indexOf("<font");
                            int e = item.get(j).getAsString().indexOf(">", s);
                            String temp = item.get(j).getAsString().substring(0, s) + item.get(j).getAsString().substring(e + 1);
                            map.put(headers.get(j).getAsString(), temp);
                        } else {
                            map.put(headers.get(j).getAsString(), item.get(j).getAsString());
                        }
                    }
                    datas.addLast(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = myHandler.obtainMessage(CALC_SPORT_DATA, results);
                myHandler.sendMessage(msg);
            }

            //将所有数据按联赛名称分类
            Map dataItem;
            LinkedHashMap<String, List<Map>> resultMap = new LinkedHashMap<>(); // 最终要的结果
            for (int i = 0; i < datas.size(); i++) {
                dataItem = datas.get(i);
                if (resultMap.containsKey(dataItem.get("league"))) {
                    resultMap.get(dataItem.get("league")).add(dataItem);
                } else {
                    LinkedList<Map> list = new LinkedList<>();
                    list.addLast(dataItem);
                    resultMap.put((String) dataItem.get("league"), list);
                }
            }

            //更新滚球，今日赛事，早盘的 足球和篮球赛数
            results.put("games", resultMap);
            Message msg = myHandler.obtainMessage(CALC_SPORT_DATA, results);
            myHandler.sendMessage(msg);
        }
    }

    public static void startProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.VISIBLE);
            progressWheel.spin();
        }
    }

    public static void stopProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.GONE);
            if (progressWheel.isSpinning()) {
                progressWheel.stopSpinning();
            }
        }
    }


    private final class MyHandler extends Handler {

        private WeakReference<SportActivity> mReference;
        private SportActivity fragment;

        public MyHandler(SportActivity fragment) {
            mReference = new WeakReference<>(fragment);
            if (mReference != null) {
                this.fragment = mReference.get();
            }
        }

        public void handleMessage(Message message) {
            if (fragment == null) {
                return;
            }
            switch (message.what) {
                case CALC_SPORT_DATA:

                    page_btn.setText(pageIndex + "/" + pageCount);
                    //获取新数据时先清除之前选择过的赔率项
//                    actionClear();
                    actionClearWhenSelectPage();
                    Map<String, Object> gameResults = (Map<String, Object>) message.obj;
//                    if (gameResults == null || gameResults.isEmpty()) {
//                        return;
//                    }
                    //更新tabs各赛事场数
                    if (gameResults != null && gameResults.containsKey("counts")) {
                        SportGameCount gameCount = (SportGameCount) gameResults.get("counts");
                        if (gameCount != null) {
                            if (currentBallPage == FOOTBALL_PAGE) {
                                tabNames[0] = "滚球(" + gameCount.getRB_FT() + ")";
                                tabNames[1] = "今日赛事(" + gameCount.getTD_FT() + ")";
                                tabNames[2] = "早盘(" + gameCount.getFT_FT() + ")";
                            } else if (currentBallPage == BASKETBALL_PAGE) {
                                tabNames[0] = "滚球(" + gameCount.getRB_BK() + ")";
                                tabNames[1] = "今日赛事(" + gameCount.getTD_BK() + ")";
                                tabNames[2] = "早盘(" + gameCount.getFT_BK() + ")";
                            }
                            //更新tab数据并刷新tabbar
                            contentAdapter.notifyDataSetChanged();
                            tabbar.setViewPager(pager);
                        }
                    }
                    //清除数据，并刷新列表
                    results.clear();
                    if (gameResults != null && gameResults.containsKey("games")) {
                        Map<String, List<Map>> gameDatas = (Map<String, List<Map>>) gameResults.get("games");
                        if (gameDatas != null) {
                            results.addAll((Collection<? extends List<Map>>) gameDatas.values());
                            tvSecondTitle.setVisibility(View.VISIBLE);
                        } else {
                            tvSecondTitle.setVisibility(View.GONE);
                        }
                    } else {
                        tvSecondTitle.setVisibility(View.GONE);
                    }
                    int scrollPageIndex = pager.getCurrentItem();
                    if (scrollPageIndex == 0) {
                        gunQiuFragment.updateData(results, currentBallPage, GAME_TYPES[scrollPageIndex],
                                getCurrentCategory());
                    } else if (scrollPageIndex == 1) {
                        saiShiFragment.updateData(results, currentBallPage, GAME_TYPES[scrollPageIndex],
                                getCurrentCategory());
                    } else if (scrollPageIndex == 2) {
                        zaoPanFragment.updateData(results, currentBallPage, GAME_TYPES[scrollPageIndex],
                                getCurrentCategory());
                    }
                    //获取完数据后，开始刷新赛事计时器
                    if (gameResults != null && !results.isEmpty()) {
                        if (counterTimer == null) {
                            createRefreshTimer(REFRESH_DURATION);
                            counterTimer.start();
                        } else {
                            counterTimer.cancel();
                            counterTimer.start();
                        }
                    }
                    break;
            }
        }
    }

    public static void createIntent(Context context, String gameName, String code) {
        Intent intent = new Intent(context, SportActivity.class);
        intent.putExtra("gameName", gameName);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    @Override
    public void onSportItemClick(int ballType, String categoryType, String playType, SportBean sportBean) {
        if (sportBean == null) {
            return;
        }
        Utils.LOG(TAG, "gameid = " + sportBean.getGid() + ",peilv = " + sportBean.getPeilv());
        if (Utils.isEmptyString(sportBean.getPeilv())) {
//            showToast(R.string.peilv_data_empty);
            return;
        }
        //由于足球或篮球的混合过关可以选择不同球赛的赔率下注，所以会有多个赔率数据项的情况
        //其它情况下只有一个比赛赔率下注
        if (playType.equals(Constant.BK_MX) || playType.equals(Constant.FT_MX)) {
            boolean hasFoundSameGame = false;
            for (int i = 0; i < selectSports.size(); i++) {
                SportBean sb = selectSports.get(i);
                if (playType.equals(Constant.BK_MX) || playType.equals(Constant.FT_MX)) {
                    if (sb.getGid().equals(sportBean.getGid())) {
                        selectSports.set(i, sportBean);
                        hasFoundSameGame = true;
                        break;
                    }
                }
            }
            if (!hasFoundSameGame) {
                selectSports.add(sportBean);
            }
        } else {
            selectSports.clear();
            selectSports.add(sportBean);
        }

        updateBottom(1, selectSports.size());
        this.selectedBallType = ballType;
        this.selectedCategoryType = categoryType;
        this.selectedPlayType = playType;

        //点击选择赔率后，直接弹出投注窗
        actionViewOrder(selectSports, false);
//        Utils.LOG(TAG,"sport list size = "+selectSports.size());

    }

    private void updateBottom(int orderCount, int sportSize) {
        zhushuView.setText(orderCount + "单");
        gameCountView.setText(sportSize + "场");
    }

    /**
     * 封装下注json
     *
     * @param ballType       球类型
     * @param categoryType   分类，早盘，今日赛事，滚球
     * @param playType       玩法
     * @param selectSports   已选择的赔率项信息集
     * @param acceptBestOdds 是否接受最佳赔率
     * @param moneyValue     下注金额
     * @param confirmGid     真正校验过的赛事ID
     * @param confirmOdd     真正校验过的赛事赔率
     */
    private SportBetData formBetOrders(int ballType, String categoryType, String playType,
                                       List<SportBean> selectSports, boolean acceptBestOdds,
                                       String moneyValue, long confirmGid, float confirmOdd) {
        if (selectSports == null || selectSports.isEmpty()) {
            return null;
        }
        SportBetData sportBetData = new SportBetData();
        sportBetData.setPlate("H");
        sportBetData.setGameType(BALL_TYPES[ballType] + "_" + categoryType + "_" + playType);
        sportBetData.setMoney(!Utils.isEmptyString(moneyValue) ? Integer.parseInt(moneyValue) : 0);
        sportBetData.setAcceptBestOdds(acceptBestOdds);
        List<SportBet> items = new ArrayList<>();
        for (SportBean sportBean : selectSports) {
            SportBet sportBet = new SportBet();
            //足球滚球时将主客队比分传到后台
            if (categoryType.equalsIgnoreCase("RB")) {
                sportBet.setScoreH(sportBean.getScoreH());
                sportBet.setScoreC(sportBean.getScoreC());
            }
            if (confirmGid > 0) {
                sportBet.setGid(confirmGid);
            } else {
                sportBet.setGid(!Utils.isEmptyString(sportBean.getGid()) ? Long.parseLong(sportBean.getGid()) : 0);
            }
            if (confirmOdd > 0) {
                sportBet.setOdds(confirmOdd);
            } else {
                sportBet.setOdds(!Utils.isEmptyString(sportBean.getPeilv()) ? Float.parseFloat(sportBean.getPeilv()) : 0);
            }
            sportBet.setProject(!Utils.isEmptyString(sportBean.getProject()) ? sportBean.getProject() : "");
            sportBet.setType(!Utils.isEmptyString(sportBean.getPeilvKey()) ? sportBean.getPeilvKey() : "");
//            if (ballType == BASKETBALL_PAGE) {
            sportBet.setMid(!Utils.isEmptyString(sportBean.getMid()) ? Long.parseLong(sportBean.getMid()) : 0);
//            }else {
//                sportBet.setMid(0);
//            }
            items.add(sportBet);
        }
        sportBetData.setItems(items);
//        postJson = new Gson().toJson(sportBetData, SportBetData.class);
        return sportBetData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectSports.clear();
        gunQiuDatas.clear();
        saiShiDatas.clear();
        zaoPanDatas.clear();
        progressWheel = null;
    }

    private void postSportBets(String postJson) {
        Utils.LOG(TAG, "sport bet json = " + postJson);
        try {
            StringBuilder url = new StringBuilder();
            url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SPORT_BETS_URL);
            url.append("?data=").append(URLEncoder.encode(postJson, "utf-8"));
            CrazyRequest<CrazyResult<SportBetResultWraper>> request = new AbstractCrazyRequest.Builder()
                    .url(url.toString())
                    .seqnumber(POST_SPORT_BET)
                    .headers(Urls.getHeader(this))
                    .placeholderText(getString(R.string.post_pick_moneying))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<SportBetResultWraper>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showGameOrderWindow(final List<SportBean> sportBeans, boolean shoudong) {

        if (sportBeans == null || sportBeans.isEmpty()) {
            showToast(R.string.please_touzhu_first);
            return;
        }
        if (betWindow == null) {
            betWindow = new SportBetOrderWindow(this);
            betWindow.setSportResultListener(new SportBetOrderWindow.SportBetListener() {
                @Override
                public void onSportResult(List<SportBean> data, String moneyValue, boolean accept) {
                    if (data == null) {
                        return;
                    }
                    if (selectSports != null) {
                        selectSports.clear();
                        selectSports.addAll(data);
                    }
                    updateBottom(selectSports.size() == 0 ? 0 : 1, selectSports.size());
                    if (selectSports.isEmpty()) {
                        showToast(R.string.please_touzhu_first);
                        return;
                    }
                    appcetBestPeilv = accept;
                    inputMoney = moneyValue;
                    if (Utils.isEmptyString(moneyValue)) {
                        showToast(R.string.input_peilv_money);
                        return;
                    }
                    if (!Utils.isNumeric(moneyValue)) {
                        showToast(R.string.input_money_must_be_zs);
                        return;
                    }
                    betWindow.getAccountBalances();
                    SportBetData sportBetData = formBetOrders(selectedBallType, selectedCategoryType,
                            selectedPlayType, selectSports, accept, moneyValue, 0, 0);
                    //下注前先校验一下赔率
                    actionValidBets(sportBetData);

                }

                @Override
                public void onSportCancel(List<SportBean> sportBetData, String moneyValue, boolean accept) {
                    if (selectSports != null) {
                        selectSports.clear();
                        selectSports.addAll(sportBetData);
                    }
                    appcetBestPeilv = accept;
                    inputMoney = moneyValue;
                    updateBottom(selectSports.size() == 0 ? 0 : 1, selectSports.size());
                }
            });
        }

        betWindow.getAccountBalances();
        betWindow.setData(sportBeans, getCurrentCategory().equalsIgnoreCase("MX"));
        if (!betWindow.isShowing()) {
            betWindow.showWindow(findViewById(R.id.item), shoudong);
        }
    }

    public static class MVerticalBannerView {
    }
}
