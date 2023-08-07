package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yibo.yiboapp.R;

import java.util.ArrayList;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.response.SessionResponse;

/*
* 等待审核
* blues
* */
public class LookThroughActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    private ListView lvContent;
    private MyBaseAdapter adapter;
    private ArrayList<String> lvData = new ArrayList<>();

    private Button mBtn;

    public static void createIntent(Context context, ArrayList<String> list, String agentBtnTxt) {
        Intent intent = new Intent(context, LookThroughActivity.class);
        intent.putStringArrayListExtra("list", list);
        intent.putExtra("agentBtnTxt",agentBtnTxt);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_look_through);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.look_through));
        tvRightText.setVisibility(View.GONE);

        lvContent = findViewById(R.id.act_look_through_lv);
        mBtn = findViewById(R.id.act_look_through_btn);
        mBtn.setOnClickListener(this);
        mBtn.setText(this.getIntent().getStringExtra("agentBtnTxt"));

        ArrayList<String> arr = this.getIntent().getStringArrayListExtra("list");

        if (arr!= null && arr.size() > 0) {
            lvData.addAll(arr);
            adapter = new MyBaseAdapter(lvData);
            lvContent.setAdapter(adapter);
        } else {
            findViewById(R.id.act_look_through_layout).setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_look_through_btn:
                this.finish();
                break;
        }
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {

    }

    class MyBaseAdapter extends BaseAdapter {

        private ArrayList<String> arrays;

        public MyBaseAdapter(ArrayList<String> arrays) {
            this.arrays = arrays;
        }

        @Override
        public int getCount() {
            return arrays.size();
        }

        @Override
        public Object getItem(int position) {
            return arrays.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyBaseAdapter.Holder holder;
            if (convertView == null) {
                holder = new MyBaseAdapter.Holder();
                convertView = View.inflate(LookThroughActivity.this, R.layout.adapter_item_popwindow, null);
                holder.textView = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (MyBaseAdapter.Holder) convertView.getTag();
            }

            holder.textView.setText(arrays.get(position));

            return convertView;
        }

        class Holder {
            TextView textView;
        }
    }

}
