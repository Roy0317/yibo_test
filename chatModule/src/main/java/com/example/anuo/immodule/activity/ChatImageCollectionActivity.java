package com.example.anuo.immodule.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.ChatCollectionImagesAdapter;
import com.example.anuo.immodule.bean.ChatCollectionImagesSelectBean;
import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.event.CommonEvent;
import com.example.anuo.immodule.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soxin
 * <p>
 * 2019年12月09日16:04:53
 */
public class ChatImageCollectionActivity extends ChatBaseActivity {

    private int selectCount = 0;

    public static void createIntent(Context context, ArrayList<ChatCollectionImagesSelectBean> collectImageArray) {
        Intent intent = new Intent(context, ChatImageCollectionActivity.class);
        intent.putExtra("collectionImageArray", collectImageArray);
        context.startActivity(intent);
    }

    AppCompatButton acb_delete;
    AppCompatButton acb_send;
    RecyclerView recyclerView;
    private ArrayList<ChatCollectionImagesSelectBean> collectImageArray;
    private ChatCollectionImagesAdapter adapter;


    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_collection_images;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        List<ChatCollectionImagesSelectBean> array = getIntent().getParcelableArrayListExtra("collectionImageArray");
        collectImageArray = new ArrayList<>();
        collectImageArray.addAll(array);
        adapter.addData(collectImageArray);

        adapter.setEmptyView(emptyTextView());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ChatCollectionImagesSelectBean item = collectImageArray.get(position);

                if (!item.isSelect() && selectCount >= 5) {
                    ToastUtils.showToast(ChatImageCollectionActivity.this, "最多只能选择5张");
                    return;
                }

                if (item.isSelect()) {
                    selectCount--;
                    item.setSelect(false);
                } else {
                    selectCount++;
                    item.setSelect(true);
                }

                adapter.notifyItemChanged(position);
            }
        });

    }

    @Override
    protected void initListener() {
        acb_delete.setOnClickListener(this);
        acb_send.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        super.initView();
        acb_delete = findViewById(R.id.acb_delete);
        acb_send = findViewById(R.id.acb_send);
        recyclerView = findViewById(R.id.recyclerView);
        tvMiddleTitle.setText("收藏图片");
//        tvRightText.setText("编辑");
//        tvRightText.setVisibility(View.VISIBLE);
        adapter = new ChatCollectionImagesAdapter(R.layout.item_dialog_collection_images, collectImageArray);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }


    private TextView emptyTextView() {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText("暂无收藏图片");
        textView.setTextSize(16);
        return textView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_text) {
            super.onClick(v);
            return;
        }

        int selectCount = 0;
        for (ChatCollectionImagesSelectBean chatCollectionImagesSelectBean : collectImageArray) {
            if (chatCollectionImagesSelectBean.isSelect()) {
                selectCount++;
            }
        }
        if (selectCount == 0) {
            ToastUtils.showToast(this, "请先选择图片");
            return;
        }

        String record = "";
        for (ChatCollectionImagesSelectBean chatCollectionImagesSelectBean : collectImageArray) {
            if (chatCollectionImagesSelectBean.isSelect()) {
                record = record + chatCollectionImagesSelectBean.getRecord() + ",";
            }
        }

        if (record.endsWith(",")) {
            record = record.substring(0, record.length() - 1);
        }

        if (v.getId() == R.id.acb_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("是否确认删除？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final String finalRecord = record;
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EventBus.getDefault().post(new CommonEvent(EventCons.DELETE_IMAGES, finalRecord));
                    finish();
                }
            });
            builder.create().show();


        } else if (v.getId() == R.id.acb_send) {

            EventBus.getDefault().post(new CommonEvent(EventCons.SEND_IMAGES, record));
            finish();
        }
    }
}
