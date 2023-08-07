package com.example.anuo.immodule.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.BetHistoryAdapter;
import com.example.anuo.immodule.bean.BetHistoryBean;
import com.example.anuo.immodule.bean.BetSlipMsgBody;
import com.example.anuo.immodule.bean.ShareOrderBean;
import com.example.anuo.immodule.bean.base.BetInfo;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.event.CommonEvent;
import com.example.anuo.immodule.interfaces.iview.IChatBetHistoryView;
import com.example.anuo.immodule.jsonmodel.ChatBetHistoryModel;
import com.example.anuo.immodule.presenter.ChatBetHistoryPresenter;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :09/10/2019
 * Desc  :com.example.anuo.immodule.activity
 */
public class ChatBetHistoryActivity extends ChatBaseActivity implements IChatBetHistoryView, BetHistoryAdapter.AllChooseListener {
    RecyclerView rcy_bet_history;
    TextView tv_share_bet;
    TextView tv_all_choose;
    TextView tv_empty;
    private ChatBetHistoryPresenter presenter;
    private List<BetHistoryBean.SourceBean.MsgBean> beanList = new ArrayList<>();
    private BetHistoryAdapter adapter;
    private ChatBetHistoryModel chatBetHistoryModel;

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void onSetContentPre() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_chat_bet_history;
    }

    @Override
    protected void initView() {
        super.initView();
        rcy_bet_history = findViewById(R.id.rcy_bet_history);
        tv_share_bet = findViewById(R.id.tv_share_bet);
        tv_all_choose = findViewById(R.id.tv_all_choose);
        tv_empty = findViewById(R.id.tv_empty);
        Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        Window window = getWindow();
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
        windowLayoutParams.width = (int) (display.getWidth() * 1.0); // 宽度设置为屏幕的1.0
        windowLayoutParams.height = (int) (display.getHeight() * 0.5); // 高度设置为屏幕的0.7
        windowLayoutParams.dimAmount = 0.0f;
        windowLayoutParams.gravity = Gravity.BOTTOM;
        rcy_bet_history.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BetHistoryAdapter(this, beanList);
        rcy_bet_history.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        chatBetHistoryModel = new ChatBetHistoryModel();
        chatBetHistoryModel.setUserId(ChatSpUtils.instance(this).getUserId());
        chatBetHistoryModel.setStationId(ChatSpUtils.instance(this).getStationId());
        chatBetHistoryModel.setPrivate(false);
        chatBetHistoryModel.setDataType("1");
        chatBetHistoryModel.setSource(ConfigCons.SOURCE);
        chatBetHistoryModel.setCode(ConfigCons.GET_BET_HISTORY);
        presenter.initData(chatBetHistoryModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chatBetHistoryModel != null)
            presenter.initData(chatBetHistoryModel);
    }

    @Override
    protected ChatBasePresenter initPresenter() {
        presenter = new ChatBetHistoryPresenter(this, this);
        return presenter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void initListener() {
        tv_share_bet.setOnClickListener(this);
        tv_all_choose.setOnClickListener(this);
        adapter.setChooseListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_share_bet) {
            if (TextUtils.isEmpty(ChatSpUtils.instance(this).getSendBetting()) || !ChatSpUtils.instance(this).getSendBetting().equals("1")) {
                // 禁止分享注单
                showToast("暂时无法分享注单，请联系客服！");
                return;
            }
            List<ShareOrderBean> orders = new ArrayList<>();
            List<BetInfo> betInfos = new ArrayList<>();
            List<BetHistoryBean.SourceBean.MsgBean> msgBeans = adapter.getmSelectData();
            if (msgBeans.isEmpty()) {
                showToast("请选择想要分享的注单！");
                return;
            }
            for (BetHistoryBean.SourceBean.MsgBean msgBean : msgBeans) {
                ShareOrderBean orderBean = new ShareOrderBean();
                orderBean.setBetMoney(msgBean.getBuyMoney());
                orderBean.setOrderId(msgBean.getOrderId());
                orders.add(orderBean);
                BetInfo betInfo = new BetInfo();
                betInfo.setLottery_content(msgBean.getHaoMa());
                betInfo.setLottery_amount(msgBean.getBuyMoney());
                betInfo.setLottery_play(msgBean.getPlayName());
                betInfo.setLottery_qihao(msgBean.getQiHao());
                betInfo.setLottery_type(msgBean.getPlayType());
                betInfo.setLottery_zhushu((msgBean.getBuyZhuShu() == 0 ? 1 : msgBean.getBuyZhuShu()) + "");
                boolean isPeilv = CommonUtils.isSixMark(msgBean.getLotCode()) || CommonUtils.isPeilvVersionMethod(this);
                betInfo.setVersion(isPeilv ? "2" : "1");
                betInfo.setLotCode(msgBean.getLotCode());
                betInfo.setModel(convertModepost(msgBean.getModel()));
                betInfo.setLotIcon(msgBean.getIcon());
                betInfos.add(betInfo);
                if (betInfos.size() != 0 && betInfos.size() % 15 == 0) {
                    BetSlipMsgBody msgBody = new BetSlipMsgBody();
                    msgBody.setBetInfos(betInfos);
                    msgBody.setOrders(orders);
                    EventBus.getDefault().post(new CommonEvent(EventCons.SHARE_BET, msgBody));
                    betInfos = new ArrayList<>();
                    orders = new ArrayList<>();
                }
            }
            if (betInfos.isEmpty()) {
                finish();
                return;
            }
            BetSlipMsgBody msgBody = new BetSlipMsgBody();
            msgBody.setBetInfos(betInfos);
            msgBody.setOrders(orders);
            EventBus.getDefault().post(new CommonEvent(EventCons.SHARE_BET, msgBody));
            finish();
        } else if (i == R.id.tv_all_choose) {
            if (tv_all_choose.getText().toString().equals("全选")) {
                tv_all_choose.setText("取消");
                adapter.allChoose();
            } else {
                tv_all_choose.setText("全选");
                adapter.allCancel();
            }
            adapter.notifyDataSetChanged();
        }
    }


    public int convertModepost(int mode) {
        if (mode == 1) {
            return 100;
        } else if (mode == 10) {
            return 110;
        } else if (mode == 100) {
            return 111;
        }
        return 1;
    }

    @Override
    public void onGetBetHistory(BetHistoryBean.SourceBean source) {
        beanList.clear();
        beanList.addAll(source.getMsg());
        if (beanList.isEmpty()) {
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void allChoose(boolean allChoose) {
        if (allChoose) {
            tv_all_choose.setText("取消");
        } else {
            tv_all_choose.setText("全选");
        }
    }
}
