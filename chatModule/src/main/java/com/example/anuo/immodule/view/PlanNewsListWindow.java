package com.example.anuo.immodule.view;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.adapter.ChatPlanNewsLotteryPopAdapter;
import com.example.anuo.immodule.bean.ChatPlanNewsBean;
import com.example.anuo.immodule.eventbus.UpdateTitleEvent;
import com.example.anuo.immodule.jsonmodel.ChatPlanNewsJsonModel;
import com.example.anuo.immodule.presenter.ChatPlanNewsPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author soxin
 * 2019年12月05日16:43:31
 */
public class PlanNewsListWindow extends PopupWindow {

    private Context context;
    private List<ChatPlanNewsBean.SourceBean.LotteryListBean> dataBeanList;
    private ChatPlanNewsLotteryPopAdapter adapter;
    private ChatPlanNewsPresenter chatPlanNewsPresenter;

    public PlanNewsListWindow(Context context, List<ChatPlanNewsBean.SourceBean.LotteryListBean> dataBeanList,
                              ChatPlanNewsPresenter planNewsPresenter) {

        this.context = context;
        this.dataBeanList = dataBeanList;
        this.chatPlanNewsPresenter = planNewsPresenter;
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(null);
        initView();
    }

    private void initView() {

        View view = View.inflate(context, R.layout.popup_plan_news_window, null);
        RecyclerView recyclerView = view.findViewById(R.id.popup_plan_news_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        adapter = new ChatPlanNewsLotteryPopAdapter(R.layout.item_plan_new_lottery_pop, dataBeanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (!dataBeanList.get(position).isClick()) {
                    fetchData(dataBeanList.get(position));
                }
                for (ChatPlanNewsBean.SourceBean.LotteryListBean lotteryListBean : dataBeanList) {
                    if (lotteryListBean.isClick()) {
                        lotteryListBean.setClick(false);
                    }
                }
                dataBeanList.get(position).setClick(true);
                adapter.notifyItemChanged(position);
                EventBus.getDefault().post(new UpdateTitleEvent(dataBeanList.get(position).getLotteryName(), dataBeanList.get(position).getLotteryCode()));
                dismiss();
            }
        });

        setContentView(view);
    }

    //切换彩种
    private void fetchData(ChatPlanNewsBean.SourceBean.LotteryListBean item) {
        ChatPlanNewsJsonModel chatPlanNewsJsonModel = new ChatPlanNewsJsonModel();
        chatPlanNewsJsonModel.setOption("2");
        chatPlanNewsJsonModel.setLotteryCode(item.getLotteryCode());
        chatPlanNewsJsonModel.setPlayName(item.getLotteryName());
        chatPlanNewsPresenter.initData(chatPlanNewsJsonModel);
    }

    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

}
