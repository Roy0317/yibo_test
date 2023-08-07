package com.yibo.yiboapp.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.LotteryData;

import java.util.ArrayList;

/**
 * Author: Ray
 * created on 2018/12/6
 * description : 简单版弹出PopupWindow的对话框中的布局
 */
public class SimplePopWindowFragment extends Fragment {

    private static final String TAG = "SimplePopWindowFragment";

    private ArrayList<LotteryData> list;


    public static SimplePopWindowFragment newInstance(ArrayList<LotteryData> list) {

        SimplePopWindowFragment simplePopWindowFragment = new SimplePopWindowFragment();
        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("list", list);
        simplePopWindowFragment.setArguments(bundle);
        return simplePopWindowFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
//            list = bundle.getParcelableArrayList("list");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_pop_window, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        GridView gridView = (GridView) view.findViewById(R.id.simple_pop_window_gridView);
//        SimplePopwindowAdapter adapter = new SimplePopwindowAdapter(getActivity(), list);
//        gridView.setAdapter(adapter);
    }


}
