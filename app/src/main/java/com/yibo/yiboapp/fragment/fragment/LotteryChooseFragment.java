package com.yibo.yiboapp.fragment.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anuo.immodule.fragment.base.ChatBaseFragment;
import com.example.anuo.immodule.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.ChatLotteryChooseAdapter;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.YiboPreference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:ray
 * Date  :31/07/2019
 * Desc  :com.yibo.yiboapp.ui.bet.fragment
 */
public class LotteryChooseFragment extends ChatBaseFragment {

    //原始数据
    private List<LotteryData> gameDatas = new ArrayList<>();
    //显示数据
    private List<LotteryData> listDatas = new ArrayList<>();
    private ChatLotteryChooseAdapter lotteryAdapter;
    private boolean isPeilvVersion;
    private int cpVersion;
    private boolean lhcSelect;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_lottery, container, false);
        return view;
    }

    @Override
    protected boolean isAddToBackStack() {
        return true;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        RecyclerView rcyBfcLotteryChoose = view.findViewById(R.id.rcy_bfc_lottery_choose);
        lotteryAdapter = new ChatLotteryChooseAdapter(act, listDatas);
        GridLayoutManager layoutManager = new GridLayoutManager(act, 2, LinearLayoutManager.HORIZONTAL, false);
        rcyBfcLotteryChoose.setLayoutManager(layoutManager);
        rcyBfcLotteryChoose.setAdapter(lotteryAdapter);
    }

    @Override
    protected void loadData() {
        super.loadData();
        getLotterys();
    }

    public void getLotterys() {
        List<LotteryData> beanList = new Gson().fromJson(YiboPreference.instance(act).getLotterys(),
                new TypeToken<List<LotteryData>>() {
                }.getType());

        if (!beanList.isEmpty()) {
            gameDatas.clear();
            gameDatas.addAll(beanList);
            listDatas.clear();
            for (LotteryData d : gameDatas) {
                if (d.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
                    listDatas.add(d);
                }
            }
            lotteryAdapter.notifyDataSetChanged();

        } else {
            ToastUtils.showToast(act, "获取彩种信息失败");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


}
