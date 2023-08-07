package com.example.anuo.immodule.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.ChatWinningListAdapter;
import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.bean.ChatWinningListBean;
import com.example.anuo.immodule.interfaces.iview.IChatWinningListView;
import com.example.anuo.immodule.jsonmodel.ChatWinningListJsonModel;
import com.example.anuo.immodule.presenter.ChatWinningListPresenter;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatWinningListDialogActivity extends ChatBaseActivity implements IChatWinningListView {

    TextView tvClose;
    RecyclerView recyclerView;
    private ChatWinningListAdapter adapter;

    private ChatWinningListPresenter chatWinningListPresenter;
    private List<ChatWinningListBean.SourceBean.WinningListBean> winningList = new ArrayList<>();


    @Override
    protected void initView() {
        super.initView();
        tvClose = findViewById(R.id.tv_close);
        recyclerView = findViewById(R.id.recyclerView);

        initWindow();
        DividerItemDecoration dec = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatWinningListAdapter(R.layout.item_tick_rank_fragment, winningList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.dialog_tick_rank;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ChatWinningListJsonModel bean = new ChatWinningListJsonModel();
        bean.setPrizeType("2");
        chatWinningListPresenter.initData(bean);
    }

    @Override
    protected void initListener() {
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWindow() {
        Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        Window window = getWindow();
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
        windowLayoutParams.width = (int) (display.getWidth() * 0.9); // 宽度设置为屏幕的1.0
        windowLayoutParams.height = (int) (display.getHeight() * 0.7); // 高度设置为屏幕的0.6
        windowLayoutParams.dimAmount = 0.4f;
        windowLayoutParams.gravity = Gravity.CENTER;
    }

    @Override
    public void onGetWinningList(ChatWinningListBean bean) {

        winningList = bean.getSource().getWinningList();
        if (winningList.size() >= 10) {
            Collections.sort(winningList, new Comparator<ChatWinningListBean.SourceBean.WinningListBean>() {
                @Override
                public int compare(ChatWinningListBean.SourceBean.WinningListBean o1, ChatWinningListBean.SourceBean.WinningListBean o2) {
                    return Double.compare(o2.getPrizeMoney(), o1.getPrizeMoney());
                }
            });
            winningList.subList(0, 10);
        }
        chatWinningListPresenter.getPhotoList(ChatSpUtils.instance(this).getStationId(), ChatSpUtils.instance(this).getUserId());
    }

    @Override
    public void onGetPhotoList(ChatPersonPhotoListBean chatPersonPhotoListBean) {

        List<String> items = chatPersonPhotoListBean.getSource().getItems();

        for (int i = 0; i < winningList.size(); i++) {
            if (i < items.size()) {
                winningList.get(i).setImageUrl(items.get(i));
                if (i == 0) {
                    winningList.get(i).setSignalLogo(R.drawable.chatroom_champion);
                } else if (i == 1) {
                    winningList.get(i).setSignalLogo(R.drawable.chatroom_silver);
                } else if (i == 2) {
                    winningList.get(i).setSignalLogo(R.drawable.chatroom_copper);
                }
            }

        }
        adapter.addData(winningList);
    }

    @Override
    public void gotoActivity(Class aClass) {

    }

    @Override
    protected ChatBasePresenter initPresenter() {
        chatWinningListPresenter = new ChatWinningListPresenter(this, this);
        return chatWinningListPresenter;
    }
}
