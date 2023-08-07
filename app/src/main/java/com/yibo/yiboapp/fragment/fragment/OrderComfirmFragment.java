package com.yibo.yiboapp.fragment.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.event.CommonEvent;
import com.example.anuo.immodule.fragment.base.ChatBaseFragment;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BetToken;
import com.yibo.yiboapp.entify.DoBetForChatWraper;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * Author:ray
 * Date  :16/08/2019
 * Desc  :com.yibo.yiboapp.ui.bet.fragment
 */
public class OrderComfirmFragment extends ChatBaseFragment implements SessionResponse.Listener<CrazyResult> {

    public static final int TOKEN_BETS_REQUEST = 0x01;
    public static final int DO_BETS_REQUEST = 0x02;

    XListView xListView;
    LinearLayout llSimpleBetDialogHeader;
    TextView tv_type;
    TextView tv_numbers;
    NestedScrollView nslScrollView;
    TextView tvMoney;
    LinearLayout llMultipleSelection;
    LinearLayout flContent;
    EditText inputMoney;
    ImageView ivFastBetMoney;
    LinearLayout llHeader;
    LinearLayout llPeilv;
    Button modeBtn;
    LinearLayout llIbMore;
    LinearLayout llMore;
    FrameLayout flMinus;
    EditText beishuInput;
    FrameLayout flPlus;
    TextView historyRecord;
    LinearLayout llGf;
    XListView xlistview;
    LinearLayout ll_layout;

    private BettingMainFragment parentFragment;
    private List<OrderDataInfo> listDatas;
    private Context context;

    //    private ChatBetListAdapter betListAdapter;
    private ArrayList<PeilvPlayData> selectNumList;
    private boolean isFengPan = false;
    private String selectPlayCode;
    private int currentCount = 1;//当前的倍数
    private String lotteryType;
    private boolean isOfficial = false;
    public static final int MAX_BEISHU_LENGTH = 7;
    public static final int BASIC_MONEY = 2;
    private String orderInfo;
    private String cpVersion;
    private String cpName;
    private long cpDuration;
    private int selectModeIndex;
    DataAdapter recordAdapter;

    String cpTypeCode = "";
    String playCode;
    String playName;
    String subPlayCode;
    String subPlayName = "";
    String cpBianma = "";

    List<OrderDataInfo> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_comfirm, container, false);
        context = getContext();
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        llHeader = view.findViewById(R.id.ll_simple_bet_dialog_header);
        xListView = view.findViewById(R.id.rcy_bet_list);
        llSimpleBetDialogHeader = view.findViewById(R.id.ll_simple_bet_dialog_header);
        tv_type = view.findViewById(R.id.tv_type);
        tv_numbers = view.findViewById(R.id.tv_numbers);
        nslScrollView = view.findViewById(R.id.nsl_scroll_view);
        tvMoney = view.findViewById(R.id.tv_money);
        llMultipleSelection = view.findViewById(R.id.ll_multiple_selection);
        flContent = view.findViewById(R.id.fl_content);
        inputMoney = view.findViewById(R.id.input_money);
        ivFastBetMoney = view.findViewById(R.id.iv_fast_bet_money);
        llPeilv = view.findViewById(R.id.ll_peilv);
        modeBtn = view.findViewById(R.id.mode_btn);
        llIbMore = view.findViewById(R.id.ll_ib_more);
        llMore = view.findViewById(R.id.ll_more);
        flMinus = view.findViewById(R.id.fl_minus);
        beishuInput = view.findViewById(R.id.beishu_input);
        flPlus = view.findViewById(R.id.fl_plus);
        historyRecord = view.findViewById(R.id.history_record);
        llGf = view.findViewById(R.id.ll_gf);
        xlistview = view.findViewById(R.id.xlistview);
        ll_layout = view.findViewById(R.id.ll_layout);

        Bundle arguments = getArguments();

        if (arguments != null) {
            orderInfo = arguments.getString("orderJson", "");
            cpVersion = arguments.getString("cpVersion", "");
            cpName = arguments.getString("cpName", "");
            cpDuration = arguments.getLong("cpDuration");
            selectModeIndex = arguments.getInt("selectModeIndex");
            isOfficial = arguments.getBoolean("isOfficial", false);
            isFengPan = arguments.getBoolean("isFengPan");
            selectNumList = arguments.getParcelableArrayList("orderData");
            selectPlayCode = arguments.getString("selectPlayCode");
            lotteryType = arguments.getString("tvLotteryType");
        }


        ivFastBetMoney.setOnClickListener(this);
        if (isOfficial) {
            xlistview.setPullLoadEnable(false);
            xlistview.setPullRefreshEnable(false);
            flContent.setVisibility(View.GONE);
            llGf.setVisibility(View.VISIBLE);
            llPeilv.setVisibility(View.GONE);
            xlistview.setVisibility(View.VISIBLE);
//            controlKeyboardLayout(ll_layout, inputMoney);
            OrderDataInfo data = new Gson().fromJson(orderInfo, OrderDataInfo.class);
            if (data != null) {
                cpTypeCode = data.getLottype();
                playCode = data.getPlayCode();
                playName = data.getPlayName();
                subPlayCode = data.getSubPlayCode();
                subPlayName = data.getSubPlayName();
                cpBianma = data.getLotcode();
            }
            listDatas = DatabaseUtils.getInstance(context).getCartOrders(cpBianma);
            recordAdapter = new DataAdapter(context, listDatas, R.layout.goucai_list_item_chat);
            xlistview.setAdapter(recordAdapter);

        } else {
            flContent.setVisibility(View.VISIBLE);
            llGf.setVisibility(View.GONE);
            xlistview.setVisibility(View.GONE);
            llPeilv.setVisibility(View.VISIBLE);
//            controlKeyboardLayout(ll_layout, beishuInput);
            showConfirmBetDialog(selectNumList, UsualMethod.isMulSelectMode(selectPlayCode), lotteryType);
        }


    }

    @Override
    protected void initListener() {
        super.initListener();
        parentFragment = (BettingMainFragment) getParentFragment();
        inputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String beishu = s.toString();
                if (beishu.startsWith("0")) {
                    Toast.makeText(getActivity(), "请正确输入投注金额", Toast.LENGTH_SHORT).show();
                    inputMoney.setText("1");
                    return;
                }
                if (Utils.isEmptyString(beishu)) {
//                    showToast(R.string.input_beishu);
                    return;
                }
                if (!Utils.isNumeric(beishu)) {
                    ToastUtils.showShort(R.string.input_peilv_money);
                }

                EventBus.getDefault().post(new CommonEvent(EventCons.CALCULATE_MONEY, inputMoney.getText().toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        modeBtn.setOnClickListener(v -> showChooseModeList());
        llIbMore.setOnClickListener(v -> showChooseModeList());

        flPlus.setOnClickListener(v -> {
            currentCount++;
            changeBetText();
        });
        flMinus.setOnClickListener(v -> {
            if (currentCount <= 1) {
                currentCount = 1;
                return;
            } else {
                currentCount--;
            }
            changeBetText();
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
                    ToastUtils.showShort(R.string.input_correct_beishu_format);
                    return;
                }
                if (TextUtils.isEmpty(beishuInput.getText().toString())) {
                    currentCount = 0;
                } else {
                    currentCount = Integer.parseInt(beishuInput.getText().toString());
                }

                parentFragment.updateBottomWithBeishChange(Integer.parseInt(beishu));

                updateList();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_BEISHU_LENGTH) {
                    String toast = String.format("当前允许输入最大倍数长度为%d位", MAX_BEISHU_LENGTH);
                    Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateList() {
        if (listDatas != null) {
            for (OrderDataInfo listData : listDatas) {
                listData.setBeishu((parentFragment.selectedBeishu));
                listData.setZhushu(parentFragment.calcOutZhushu);
                if (parentFragment.selectModeIndex == 0) {
                    listData.setMode(Constant.YUAN_MODE);
                } else if (parentFragment.selectModeIndex == 1) {
                    listData.setMode(Constant.JIAO_MODE);
                } else if (parentFragment.selectModeIndex == 2) {
                    listData.setMode(Constant.FEN_MODE);
                }
            }
        }

    }


    public void onGfPlayTouzhu() {
        CrazyRequest tokenRequest = buildBetsTokenRequest(context, TOKEN_BETS_REQUEST);
        RequestManager.getInstance().startRequest(context, tokenRequest);
    }


    public CrazyRequest buildBetsTokenRequest(Context context, int requestCode) {
        StringBuilder qiHaoUrl = new StringBuilder();
        qiHaoUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.TOKEN_BETS_URL);
        CrazyRequest<CrazyResult<BetToken>> request = new AbstractCrazyRequest.Builder().
                url(qiHaoUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .listener(this)
                .placeholderText(context.getString(R.string.vertify_bet_tokening))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<BetToken>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        return request;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_fast_bet_money:
                showFastAmountDialog();
                break;
        }
    }


    /**
     * 选择快捷金额
     */
    private void showFastAmountDialog() {
        final String[] finalSplit;
        if (UsualMethod.getConfigFromJson(getContext()) != null && !TextUtils.isEmpty(UsualMethod.getConfigFromJson(getContext()).getFast_bet_money())) {
            finalSplit = UsualMethod.getConfigFromJson(getContext()).getFast_bet_money().split(",");
        } else {
            finalSplit = Constant.DEFAULT_FASE_MONEY.split(",");
        }
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), finalSplit, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                try {
                    String money = finalSplit[position];
                    inputMoney.setText(money);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changeBetText() {
        beishuInput.setText(String.valueOf(currentCount));
        parentFragment.updateBottomWithBeishChange(currentCount);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult> response) {
        if (!isAdded() || response == null) {
            RequestManager.getInstance().afterRequest(response);
            return;
        }
        int action = response.action;
        if (action == TOKEN_BETS_REQUEST) {
            RequestManager.getInstance().afterRequest(response);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                ToastUtils.showShort(R.string.dobets_token_fail);
                return;
            }
            if (!result.crazySuccess) {
                ToastUtils.showShort(R.string.dobets_token_fail);
                return;
            }
            Object regResult = result.result;
            BetToken reg = (BetToken) regResult;
            if (!reg.isSuccess()) {
                ToastUtils.showShort(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.dobets_token_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(context);
                }
                return;
            }
            YiboPreference.instance(context).setToken(reg.getAccessToken());
            //获取下注口令后开始下注
            actionPostTouzhu(reg.getContent());
        } else if (action == DO_BETS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                RequestManager.getInstance().afterRequest(response);
                ToastUtils.showShort(R.string.dobets_fail);
                return;
            }
            if (!result.crazySuccess) {
                RequestManager.getInstance().afterRequest(response);
                ToastUtils.showShort(R.string.dobets_fail);
                return;
            }
            Object regResult = result.result;
            DoBetForChatWraper reg = (DoBetForChatWraper) regResult;
            if (!reg.isSuccess()) {
                RequestManager.getInstance().afterRequest(response);
                ToastUtils.showShort(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.dobets_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(context);
                }
                return;
            }
            YiboPreference.instance(context).setToken(reg.getAccessToken());
            parentFragment.saveCurrentLotData();
            Float totalAmount = parentFragment.selectedMoney;
            parentFragment.onBottomUpdate(0, 0);
            parentFragment.swtichFragment();
            RequestManager.getInstance().afterRequest(response);
            ToastUtils.showShort(R.string.dobets_success);
            if (TextUtils.isEmpty(ChatSpUtils.instance(context).getSendBetting()) || !ChatSpUtils.instance(context).getSendBetting().equals("1")) {
                // 禁止分享注单
//                EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK));
                new Handler().postDelayed(() -> EventBus.getDefault().post(new CommonEvent(EventCons.MOVE_TO_BACK)), 750);
            } else {
                parentFragment.showAfterBetDialog(reg.getContent(), list, "1", totalAmount);
            }
            DatabaseUtils.getInstance(getActivity()).deleteAllOrder();

        }
    }


    //提交投注
    public void actionPostTouzhu(String token) {


        if (listDatas == null || listDatas.isEmpty()) {
            ToastUtils.showShort(R.string.noorder_please_touzhu_first);
            return;
        }


        //用于分享注单的临时数据
        list.clear();
        for (OrderDataInfo data : listDatas) {
            OrderDataInfo orderDataInfo = new OrderDataInfo();
            orderDataInfo.setNumbers(data.getNumbers());
//            orderDataInfo.setMoney(data.getMoney());
            orderDataInfo.setMoney(parentFragment.selectedMoney);
            orderDataInfo.setPlayName(data.getSubPlayName());
            orderDataInfo.setMode(data.getMode());
            orderDataInfo.setZhushu(data.getZhushu());
            list.add(orderDataInfo);
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

            //构造下注crazy request
            StringBuilder qiHaoUrl = new StringBuilder();
            qiHaoUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.DO_BETS_URL);
            qiHaoUrl.append("?data=").append(URLEncoder.encode(postJson, "utf-8"));
            CrazyRequest<CrazyResult<DoBetForChatWraper>> request = new AbstractCrazyRequest.Builder().
                    url(qiHaoUrl.toString())
                    .seqnumber(DO_BETS_REQUEST)
                    .headers(Urls.getHeader(context))
                    .shouldCache(false)
                    .listener(this)
                    .placeholderText(getString(R.string.bet_ongoing))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<DoBetForChatWraper>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(context, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class DataAdapter extends LAdapter<OrderDataInfo> {

        Context context;

        public DataAdapter(Context mContext, List<OrderDataInfo> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }


        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final OrderDataInfo item) {

            TextView numberTV = holder.getView(R.id.numbers);
            TextView playRuleTV = holder.getView(R.id.play_rule);


            numberTV.setText(!Utils.isEmptyString(item.getNumbers()) ? item.getNumbers() : "暂无号码");
            playRuleTV.setText(item.getPlayName() + "-" + item.getSubPlayName());
        }
    }


    private void showConfirmBetDialog(final List<PeilvPlayData> selectDatas, final boolean isMulSelect, final String playRule) {


        //多选情况下隐藏listView
        if (isMulSelect) {
            llMultipleSelection.setVisibility(View.VISIBLE);
            llHeader.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        } else {
            //单选情况下显示listView
            llMultipleSelection.setVisibility(View.GONE);
            llHeader.setVisibility(View.GONE);
            xListView.setVisibility(View.VISIBLE);
        }


        if (isMulSelect) {
            //类型
            if (null == selectDatas.get(0).getItemName() || selectDatas.get(0).getItemName().length() == 0) {
                tv_type.setText(playRule);
            } else {
                tv_type.setText(String.format(Locale.CHINA, "%s-%s", playRule, selectDatas.get(0).getItemName()));
            }
            //号码
            StringBuilder balloonCount = new StringBuilder();
            for (int i = 0; i < selectDatas.size(); i++) {
                if (i == selectDatas.size() - 1) {
                    balloonCount.append(selectDatas.get(i).getNumber());
                } else {
                    balloonCount.append(selectDatas.get(i).getNumber()).append(",");
                }
            }

            tv_numbers.setText(balloonCount.toString());

            //钱数
            float tvMoney = selectDatas.get(0).getMoney();
//            tv_money.setText(String.valueOf(tvMoney));

        } else {
            xListView.setPullRefreshEnable(false);
            xListView.setPullLoadEnable(false);
            xListView.addHeaderView(View.inflate(context, R.layout.chat_simple_bet_dialog_header, null));
            xListView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return selectDatas == null ? 0 : selectDatas.size();
                }

                @Override
                public Object getItem(int position) {
                    return selectDatas.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    Holder holder;
                    if (convertView == null) {
                        holder = new Holder();
                        convertView = View.inflate(context, R.layout.chat_simple_bet_dialog_list_item, null);
                        holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                        holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);

                        convertView.setTag(holder);
                    } else {
                        holder = (Holder) convertView.getTag();
                    }
                    PeilvPlayData item = selectDatas.get(position);

                    if (null == item.getItemName() || 0 == item.getItemName().length()) {
                        holder.tvType.setText(playRule);
                    } else {
                        holder.tvType.setText(String.format(Locale.CHINA, "%s-%s", playRule, item.getItemName()));
                    }

                    holder.tvNumber.setText(item.getNumber());
//                    holder.tvMoney.setText(String.valueOf(item.getMoney()));
                    return convertView;
                }

                class Holder {

                    TextView tvType;
                    TextView tvNumber;
                    TextView tvMoney;
                }

            });
        }


    }

    public void showChooseModeList() {
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
                parentFragment.selectModeIndex = switchMode(position);
                parentFragment.selectedMoney = adjustMoney(parentFragment.selectedBeishu, parentFragment.calcOutZhushu, parentFragment.selectModeIndex);
                parentFragment.onBottomUpdate(parentFragment.calcOutZhushu, parentFragment.selectedMoney);
                modeBtn.setText(finalStringItems[position]);
                updateList();
            }
        });

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
     * @param selectMode 以选择的金额模式 索引 ，0-元 1-角 2-分
     */
    private int switchMode(int selectMode) {
        String yjfMode = YiboPreference.instance(getActivity()).getYjfMode();
        int selectModeIndex = parentFragment.selectModeIndex;
        if (Utils.isEmptyString(yjfMode)) {
            return selectModeIndex;
        }
        int yjfValue = Integer.parseInt(yjfMode);
        if (yjfValue == Constant.YUAN_MODE) {
            selectModeIndex = 0;
        } else if (yjfValue == Constant.JIAO_MODE) {
            if (selectMode == 0) {
                selectModeIndex = 0;
            } else if (selectMode == 1) {
                selectModeIndex = 1;
            }
        } else if (yjfValue == Constant.FEN_MODE) {
            if (selectMode == 0) {
                selectModeIndex = 0;
            } else if (selectMode == 1) {
                selectModeIndex = 1;
            } else if (selectMode == 2) {
                selectModeIndex = 2;
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

    /**
     * @param root         根布局View
     * @param scrollToView 需要移动的View
     */

    private void controlKeyboardLayout(final View root, final View scrollToView) {

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override

            public void onGlobalLayout() {

                Rect rect = new Rect();

                //获取root在窗体的可视区域

                root.getWindowVisibleDisplayFrame(rect);

                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)

                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;

                //若不可视区域高度大于100，则键盘显示

                if (rootInvisibleHeight > 100) {

                    int[] location = new int[2];

                    //获取scrollToView在窗体的坐标

                    scrollToView.getLocationInWindow(location);

                    int firstLocation = 0;

                    if (firstLocation < location[1]) {

                        firstLocation = location[1];
                    }

                    //计算root滚动高度，使scrollToView在可见区域

                    int srollHeight = (firstLocation + scrollToView.getHeight()) - rect.bottom;

                    root.scrollTo(0, srollHeight);

                } else {

                    //键盘隐藏

                    root.scrollTo(0, 0);

                }

            }

        });

    }


}
