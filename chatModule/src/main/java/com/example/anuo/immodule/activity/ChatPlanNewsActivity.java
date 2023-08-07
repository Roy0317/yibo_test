package com.example.anuo.immodule.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.ChatPlanNewsAdapter;
import com.example.anuo.immodule.bean.ChatPlanNewsBean;
import com.example.anuo.immodule.eventbus.UpdateTitleEvent;
import com.example.anuo.immodule.interfaces.iview.IChatPlanNewsView;
import com.example.anuo.immodule.jsonmodel.ChatPlanNewsJsonModel;
import com.example.anuo.immodule.presenter.ChatPlanNewsPresenter;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ToastUtils;
import com.example.anuo.immodule.view.PlanNewsListWindow;
import com.simon.utils.DisplayUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soxin
 * 2019年12月04日19:22:36
 */
public class ChatPlanNewsActivity extends ChatBaseActivity implements IChatPlanNewsView {
    RecyclerView recyclerView;
    private String currentLotteryCode;
    private TextView middle_title;
    private ImageView ivIndicator;
    private LinearLayout middleTileLinear;
    private ChatPlanNewsPresenter planNewsPresenter;
    private ChatPlanNewsAdapter adapter;
    private TextView rightText;
    private List<String> playList = new ArrayList<>();
    private List<ChatPlanNewsBean.SourceBean.LotteryListBean> lotteryListBeans = new ArrayList<>();
    private List<ChatPlanNewsBean.SourceBean.ResultListBean> resultListBeans = new ArrayList<>();
    private PlanNewsListWindow planNewsListWindow;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_chat_plan_news;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //初始化
        ChatPlanNewsJsonModel chatPlanNewsJsonModel = new ChatPlanNewsJsonModel();
        chatPlanNewsJsonModel.setOption("1");
        planNewsPresenter.initData(chatPlanNewsJsonModel); //回调方法--->onGetPlanNews()

    }

    @Override
    protected ChatBasePresenter initPresenter() {
        planNewsPresenter = new ChatPlanNewsPresenter(this, this);
        return planNewsPresenter;
    }

    @Override
    protected void initListener() {
        rightText.setOnClickListener(this);
        ivIndicator.setOnClickListener(this);
        middle_title.setOnClickListener(this);

    }


    @Override
    public void onGetPlanNews(ChatPlanNewsBean sourceBean) {
        if (sourceBean.isSuccess()) {


            ChatPlanNewsBean.SourceBean msg = sourceBean.getSource();

            if (msg.getResultList() != null && msg.getResultList().size() > 0) {
                //渲染计划结果列表数据
                resultListBeans.clear();
                resultListBeans.addAll(msg.getResultList());
            }
            if (msg.getPlayList() != null && msg.getPlayList().size() != 0) {
                //小玩法数据
                playList.clear();
                playList.addAll(msg.getPlayList());
                rightText.setText(playList.get(0));
            }

            if (!TextUtils.isEmpty(msg.getLotteryCode())) {
                //当前的彩种code编码
                currentLotteryCode = msg.getLotteryCode();
            }

            if (msg.getLotteryList() != null && msg.getLotteryList().size() != 0) {
                //彩种数据
                lotteryListBeans.clear();
                lotteryListBeans.addAll(msg.getLotteryList());
            }
            if (!TextUtils.isEmpty(msg.getLotteryName())) {
                //顶部标题设置默认彩种
                middle_title.setText(msg.getLotteryName());
            }
            //顶部标题栏目
            middleTileLinear.setVisibility(View.VISIBLE);
            middleTileLinear.setBackgroundResource(R.drawable.bg_chat_plan_new);
            ivIndicator.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, "数据刷新失败请重试");
        }

    }

    @Override
    protected void initView() {
        super.initView();
        recyclerView = findViewById(R.id.fragment_plan_news_recyclerView);
        middle_title = findViewById(R.id.middle_title);
        middleTileLinear = findViewById(R.id.clickable_title);
        ivIndicator = findViewById(R.id.title_indictor);
        middleTileLinear.setVisibility(View.INVISIBLE);
        rightText = findViewById(R.id.right_text);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("切换计划");
        Drawable drawable = getResources().getDrawable(R.drawable.down);
        rightText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        rightText.setCompoundDrawablePadding(DisplayUtil.dip2px(this, 5));
        adapter = new ChatPlanNewsAdapter(R.layout.item_chat_plan_news, resultListBeans);
        DividerItemDecoration dec = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_text) {
            final ListPopupWindow mListPop = new ListPopupWindow(this);
            mListPop.setAdapter(new ArrayAdapter<>(this, R.layout.item_chat_plan_news_popwindow, playList));
            mListPop.setAnchorView(rightText);
            mListPop.setVerticalOffset(DisplayUtil.dip2px(this, 5));
            mListPop.setModal(true);
            mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ChatPlanNewsJsonModel chatPlanNewsJsonModel = new ChatPlanNewsJsonModel();
                    chatPlanNewsJsonModel.setOption("3");
                    chatPlanNewsJsonModel.setLotteryCode(currentLotteryCode);
                    chatPlanNewsJsonModel.setPlayName(playList.get(position));
                    planNewsPresenter.initData(chatPlanNewsJsonModel);
                    rightText.setText(playList.get(position));
                    mListPop.dismiss();
                }
            });
            mListPop.show();
        } else if (v.getId() == R.id.middle_title || v.getId() == R.id.title_indictor) {
            if (planNewsListWindow == null) {
                if (lotteryListBeans != null && lotteryListBeans.size() > 0) {
                    lotteryListBeans.get(0).setClick(true);
                }
                planNewsListWindow = new PlanNewsListWindow(this, lotteryListBeans, planNewsPresenter);
            }
            planNewsListWindow.showAsDropDown(findViewById(R.id.title));
            planNewsListWindow.notifyDataChanged();
        }
        super.onClick(v);
    }


    @Subscribe
    public void onEvent(UpdateTitleEvent updateTitleEvent ) {
        currentLotteryCode = updateTitleEvent.getCode();
        middle_title.setText(updateTitleEvent.getTitle());
    }
}
