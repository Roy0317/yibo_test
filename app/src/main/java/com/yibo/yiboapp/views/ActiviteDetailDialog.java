package com.yibo.yiboapp.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.BaseRecyclerAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.ActiveStationDetailData;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;

import java.util.List;


public class ActiviteDetailDialog extends Dialog {

    private Context ctx;
    private View contentView;
    List<ActiveStationDetailData.AwardsBean> list;
    int id;
    private RecyclerView mRecyclerView;
    private TextView tv_title;
    private TextView confirm;
    private String title;
    private ActiveStationDetailData.AwardsBean selectBean;
    DetailAdapter adapter;

    public ActiviteDetailDialog(Context context, int id, List<ActiveStationDetailData.AwardsBean> list, String title) {
        super(context);
        this.ctx = context;
        this.list = list;
        this.id = id;
        this.title = title;
        selectBean = list.get(0);
        selectBean.setIscheck(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = View.inflate(ctx, R.layout.dialog_active_detail, null);
        setContentView(contentView);
        initViews();
        setListener();
        windowDeploy();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        tv_title = findViewById(R.id.tv_title);
        adapter = new DetailAdapter(ctx, list);
        confirm = findViewById(R.id.confirm);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mRecyclerView.setAdapter(adapter);
        tv_title.setText(title);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata(true);
            }
        });
    }


    private void setListener() {
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//                selectBean = (ActiveStationDetailData.AwardsBean) adapter.getData().get(position);
//                for (int i = 0; i < adapter.getData().size(); i++) {
//                    ((ActiveStationDetailData.AwardsBean) adapter.getData().get(i)).setIscheck(false);
//                }
//                ((ActiveStationDetailData.AwardsBean) adapter.getData().get(position)).setIscheck(true);
//                adapter.notifyDataSetChanged();
//            }
//        });

    }


    public void setContent(String content) {

    }


    /**
     * 设置窗口显示
     */
    public void windowDeploy() {
        Window window = getWindow();
        //出现动画
//        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //设置显示位置
//        layoutParams.x = 0;
//        layoutParams.y = d.getHeight();
        //设置显示宽高
        layoutParams.height = d.getHeight() / 5 * 2;
        layoutParams.width = d.getWidth() / 6 * 5;
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

    }

    @Override
    public void show() {
        super.show();
    }

    private class DetailAdapter extends BaseRecyclerAdapter<ActiveStationDetailData.AwardsBean> {


        public DetailAdapter(Context ctx, List<ActiveStationDetailData.AwardsBean> list) {
            super(ctx, list);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_active_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder, position);
            ViewHolder holder = (ViewHolder) viewHolder;
            ActiveStationDetailData.AwardsBean item = mList.get(position);
            holder.rb_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectBean = mList.get(position);
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setIscheck(false);
                    }
                    mList.get(position).setIscheck(true);
                    notifyDataSetChanged();
                }
            });
            holder.rb_content.setChecked(item.isIscheck());
            holder.rb_content.setText(item.getAwardName());
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton rb_content;


            public ViewHolder(View itemView) {
                super(itemView);
                rb_content = itemView.findViewById(R.id.rb_content);

            }
        }


    }

    private void getdata(boolean showDialog) {
        ApiParams params = new ApiParams();
        params.put("activeId", selectBean.getActiveId());
        params.put("activeName", selectBean.getAwardIndex());
        HttpUtil.get(ctx, Urls.GET_ACTIVE_LOBBY_PLAY, params, true, "正在获取信息", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    ToastUtil.showToast(ctx, "申请成功");
                } else {
                    ToastUtil.showToast(ctx, result.getMsg());
                }
            }
        });
    }
}
