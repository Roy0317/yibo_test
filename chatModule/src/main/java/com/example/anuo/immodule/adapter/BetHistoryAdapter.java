package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.BetHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :09/10/2019
 * Desc  :com.example.anuo.immodule.adapter
 */
public class BetHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<BetHistoryBean.SourceBean.MsgBean> mData;
    private List<Integer> mSelectedPos = new ArrayList<>();
    private List<BetHistoryBean.SourceBean.MsgBean> mSelectData = new ArrayList<>();

    public List<BetHistoryBean.SourceBean.MsgBean> getmSelectData() {
        return mSelectData;
    }

    public BetHistoryAdapter(Context context, List<BetHistoryBean.SourceBean.MsgBean> beanList) {
        this.context = context;
        this.mData = beanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BetHistoryHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_bet_history, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        final BetHistoryBean.SourceBean.MsgBean msgBean = mData.get(i);
        final BetHistoryHolder holder = (BetHistoryHolder) viewHolder;
        holder.cb_choose.setTag(new Integer(i));
        holder.tv_play_name.setText(msgBean.getPlayType() + "-" + msgBean.getPlayName());
        String qiHao = msgBean.getQiHao();
        if (qiHao.length() > 6) {
            qiHao = qiHao.substring(qiHao.length() - 6);
        }
        holder.tv_qihao.setText(qiHao + "期");
        holder.tv_content.setText(msgBean.getHaoMa());
        float amount = Float.parseFloat(msgBean.getBuyMoney());
        holder.tv_amount.setText((float) Math.round(amount * 100) / 100 + "元");
        String status = "";
        switch (msgBean.getStatus()) {
            //1:等待开奖  2:已中奖 3:未中奖 4:撤单 5:派奖回滚成功  6:和  7:回滚异常
            case 1:
                status = "等待开奖";
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.yellow_5));
                break;
            case 2:
                status = "已中奖";
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.red4));
                break;
            case 3:
                status = "未中奖";
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
                break;
//            case 4:
//                status = "已撤单";
//                break;
//            case 5:
//                status = "已派奖";
//                break;
//            case 6:
//                status = "和";
//                break;
//            case 7:
//                status = "派奖异常";
//                break;
        }
        holder.tv_status.setText(status);
        holder.cb_choose.setChecked(mSelectedPos.contains(i));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cb_choose.setChecked(!holder.cb_choose.isChecked());
            }
        });
        holder.cb_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!mSelectedPos.contains(holder.cb_choose.getTag())) {
                        mSelectedPos.add(i);
                        mSelectData.add(mData.get(i));
                    } else {
                        //防止多次触发 如果当前为选中 则直接返回 不需要执行其他操作
                        return;
                    }
                } else {
                    if (mSelectedPos.contains(holder.cb_choose.getTag())) {
                        //请注意这里需要使用Integer.valueOf 的方式，因为list中是Integer，是一个装箱的对象，这时候直接用pos会将其作为数组下标发生数组越界
                        mSelectedPos.remove(Integer.valueOf(i));
                        mSelectData.remove(mData.get(i));
                    } else {
                        //防止多次触发 如果当前为未选中则直接返回 不需要执行其他操作
                        return;
                    }
                }
                if (chooseListener != null) {
                    chooseListener.allChoose(checkAllChoose());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 全选
     */
    public void allChoose() {
        mSelectData.clear();
        mSelectedPos.clear();
        mSelectData.addAll(mData);
        for (int i = 0; i < mData.size(); i++) {
            mSelectedPos.add(new Integer(i));
        }
    }

    /**
     * 反选
     */
    public void allCancel() {
        mSelectData.clear();
        mSelectedPos.clear();
    }

    private boolean checkAllChoose() {
        return mSelectData.containsAll(mData);
    }

    private AllChooseListener chooseListener;

    public void setChooseListener(AllChooseListener chooseListener) {
        this.chooseListener = chooseListener;
    }

    public interface AllChooseListener {
        void allChoose(boolean allChoose);
    }

    class BetHistoryHolder extends RecyclerView.ViewHolder {
        TextView tv_play_name;
        TextView tv_qihao;
        TextView tv_content;
        TextView tv_amount;
        CheckBox cb_choose;
        TextView tv_status;

        public BetHistoryHolder(@NonNull View itemView) {
            super(itemView);
            tv_play_name = itemView.findViewById(R.id.tv_play_name);
            tv_qihao = itemView.findViewById(R.id.tv_qihao);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            cb_choose = itemView.findViewById(R.id.cb_choose);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
