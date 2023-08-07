package com.yibo.yiboapp.fragment.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anuo.immodule.fragment.base.ChatBaseFragment;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.ChatPlayChooseAdapter;
import com.yibo.yiboapp.entify.PlayItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Author:Ray
 * Date  :13/08/2019
 * Desc  :com.yibo.yiboapp.ui.bet.fragment
 */
public class PlayChooseFragment extends ChatBaseFragment {

    private RecyclerView rcy_play;
    private List<PlayItem> listDatas = new ArrayList();
    private ChatPlayChooseAdapter playChooseAdapter;
    private ChatPlayChooseAdapter.OnSelectRuleListener ruleSelectCallback;

    public void setPositon(int positon) {
        this.positon = positon;
    }

    private int positon;


    public void setListDatas(List<PlayItem> listDatas) {
        this.listDatas = listDatas;
    }

    public void updatePlayRules(List<PlayItem> items) {
        if (items != null) {
            listDatas.clear();
            listDatas.addAll(items);
        }
        if (playChooseAdapter != null)
            playChooseAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pc_betting, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        rcy_play = view.findViewById(R.id.rcy_play_choose);
        playChooseAdapter = new ChatPlayChooseAdapter(act, new ArrayList<>());
        playChooseAdapter.setSelectRuleListener(ruleSelectCallback);
        playChooseAdapter.setPos(positon);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(act);
        rcy_play.setLayoutManager(linearLayoutManager);
        rcy_play.setAdapter(playChooseAdapter);
        handleData(listDatas,new ArrayList<>(),true);
    }

    public void setRuleSelectCallback(ChatPlayChooseAdapter.OnSelectRuleListener ruleSelectCallback) {
        this.ruleSelectCallback = ruleSelectCallback;
    }

    /**
     * 将数据根据界面显示(一行两个或者一行三个),添加到相对应的集合中，并刷新数据
     *
     * @param dataList
     * @param newDataList
     */
    @SuppressLint("CheckResult")
    private void handleData(final List<PlayItem> dataList, final List<List<PlayItem>> newDataList ,boolean refresh) {

        List<List<PlayItem>> list = new ArrayList<>();
//                int num = dataList.size() % 3 == 0 ? 3 : 2;
        List<PlayItem> tempList = null;

        for (int i = 0; i < dataList.size(); i++) {
            PlayItem data = dataList.get(i);
//            if (data.getRules() != null && data.getRules().size() > 1) {
////                Collections.sort(data.getRules());
//            }
            if (i % 3 == 0) {
                tempList = new ArrayList<>();
                list.add(tempList);
            }
            if (tempList != null)
                tempList.add(data);
        }

        Observable.just(list)
                .subscribe(lotteryDataList -> {
                    newDataList.clear();
                    newDataList.addAll(lotteryDataList);
                    if (refresh) {
                        playChooseAdapter.getList().clear();
                        playChooseAdapter.addAll(newDataList);
                        playChooseAdapter.resetExpend();
                        playChooseAdapter.notifyDataSetChanged();
                    }
                });
    }
}
