package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.MidAutumnActiveBean;

import java.util.List;

public class MidAutumnJiangpinAdapter extends BaseRecyclerAdapter<MidAutumnActiveBean.AwardListBean> {


    public MidAutumnJiangpinAdapter(Context ctx, List<MidAutumnActiveBean.AwardListBean> list) {
        super(ctx, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.autum_jiangpin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

        ViewHolder holder = (ViewHolder) viewHolder;
        MidAutumnActiveBean.AwardListBean item = mList.get(position);
        String type = "";
        if (TextUtils.isEmpty(item.getProductImg())) {
            switch (item.getAwardType()) {
                case 1:
                    type = "积分";
                    holder.img_jiangpin.setImageURI(Uri.parse("res://" + UsualMethod.getPackageName(ctx) + "/" + R.drawable.zhongqiujifen));
                    break;
                case 2:
                    type = "现金";
                    holder.img_jiangpin.setImageURI(Uri.parse("res://" + UsualMethod.getPackageName(ctx) + "/" + R.drawable.zhongqiu_xianjin));
                    break;
                case 3:
                    type = "礼物";
                    holder.img_jiangpin.setImageURI(Uri.parse("res://" + UsualMethod.getPackageName(ctx) + "/" + R.drawable.zhongqiu_liwu));
                    break;
                case 4:
                    type = "未中奖";
                    holder.img_jiangpin.setImageURI(Uri.parse("res://" + UsualMethod.getPackageName(ctx) + "/" + R.drawable.zhongqiujifen));
                    break;
                default:
                    holder.img_jiangpin.setImageURI(Uri.parse("res://" + UsualMethod.getPackageName(ctx) + "/" + R.drawable.zhongqiujifen));
            }


        } else {
            holder.img_jiangpin.setImageURI(Uri.parse(item.getProductImg()));
        }

        holder.tv_remark.setText(!TextUtils.isEmpty(type) ? type + item.getAwardValue() : "");
        holder.tv_jpname.setText(item.getAwardName());
        holder.tv_jpleixing.setText(item.getCodeStr());
        if (!TextUtils.isEmpty(item.getProductRemark()))
            holder.tv_remark.setText(item.getProductRemark());

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img_jiangpin;
        TextView tv_jpname;
        TextView tv_jpleixing;
        TextView tv_remark;

        public ViewHolder(View itemView) {
            super(itemView);
            img_jiangpin = itemView.findViewById(R.id.img_jiangpin);
            tv_jpname = itemView.findViewById(R.id.tv_jpname);
            tv_jpleixing = itemView.findViewById(R.id.tv_jpleixing);
            tv_remark = itemView.findViewById(R.id.tv_remark);
        }
    }


}
