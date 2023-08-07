package com.yibo.yiboapp.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.utils.DateUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.BaseRecyclerAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.ActiveSearchData;
import com.yibo.yiboapp.entify.ActiveStationData;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;

import java.util.List;


public class ActiviteSearchDialog extends Dialog {

    private Context ctx;
    private View contentView;
    List<ActiveStationData.Aactivebean> list;
    List<ActiveSearchData> searchList;
    private RecyclerView mRecyclerView;
    private RecyclerView sRecyclerView;
    private TextView tv_title;
    private TextView confirm;
    private LinearLayout layout1, layout2;
    private ActiveStationData.Aactivebean selectBean;
    SerchAdapter adapter;
    SearchInfoAdapter infoAdapter;

    public ActiviteSearchDialog(Context context, List<ActiveStationData.Aactivebean> list) {
        super(context);
        this.ctx = context;
        this.list = list;
        selectBean = list.get(0);
        selectBean.setChecke(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = View.inflate(ctx, R.layout.dialog_active_search, null);
        setContentView(contentView);
        initViews();
        windowDeploy();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        sRecyclerView = findViewById(R.id.rec_search);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        adapter = new SerchAdapter(ctx, list);
        infoAdapter = new SearchInfoAdapter(R.layout.item_active_search, searchList);
        confirm = findViewById(R.id.confirm);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mRecyclerView.setAdapter(adapter);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        sRecyclerView.setAdapter(infoAdapter);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata(true);
            }
        });
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
        layoutParams.height = d.getHeight() / 5 * 3;
        layoutParams.width = d.getWidth() / 6 * 5;
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 搜索列表adapter
     */
    private class SerchAdapter extends BaseRecyclerAdapter<ActiveStationData.Aactivebean> {


        public SerchAdapter(Context ctx, List<ActiveStationData.Aactivebean> list) {
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
            ActiveStationData.Aactivebean item = mList.get(position);
            holder.rb_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectBean = mList.get(position);
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setChecke(false);
                    }
                    mList.get(position).setChecke(true);
                    notifyDataSetChanged();
                }
            });
            holder.rb_content.setChecked(item.isChecke());
            holder.rb_content.setText(item.getTitle());
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton rb_content;


            public ViewHolder(View itemView) {
                super(itemView);
                rb_content = itemView.findViewById(R.id.rb_content);

            }
        }


    }


    /**
     * 审核进度adapter
     */
    class SearchInfoAdapter extends BaseQuickAdapter<ActiveSearchData, BaseViewHolder> {
        public SearchInfoAdapter(int layoutResId, @Nullable List<ActiveSearchData> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ActiveSearchData item) {
            helper.setText(R.id.tv_user, item.getAccount());
            helper.setText(R.id.tv_xiangmu, item.getProductName());
            helper.setText(R.id.tv_time, DateUtil.getDateFormat(item.getCreateDatetime(), "yyyy-MM-dd"));
            switch (item.getStatus()) {
                case 1:
                    helper.setText(R.id.tv_state, "未处理");
                    break;
                case 2:
                    helper.setText(R.id.tv_state, "处理成功");
                    break;
                case 3:
                    helper.setText(R.id.tv_state, "处理失败");
                    break;
                case 4:
                    helper.setText(R.id.tv_state, "已取消");
                    break;
            }

            if(TextUtils.isEmpty(item.getRemark())){
                helper.getView(R.id.linearRemark).setVisibility(View.GONE);
            }else {
                helper.getView(R.id.linearRemark).setVisibility(View.VISIBLE);
                ((TextView)helper.getView(R.id.tv_remark)).setText(item.getRemark());
            }
        }
    }


    private void getdata(boolean showDialog) {
        ApiParams params = new ApiParams();
        params.put("activeId", selectBean.getId());
        HttpUtil.get(ctx, Urls.GET_ACTIVE_LOBBY_AWARDLIST, params, true, "正在获取信息", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    searchList = new Gson().fromJson(result.getContent(), new TypeToken<List<ActiveSearchData>>() {
                    }.getType());
                    infoAdapter.addData(searchList);
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                } else {

                }
            }
        });
    }
}
