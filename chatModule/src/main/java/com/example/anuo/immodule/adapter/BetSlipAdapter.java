package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.base.BetInfo;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.view.AlwaysMarqueeTextView;

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
 * Date  :2019-12-17
 * Desc  :com.example.anuo.immodule.adapter
 */
public class BetSlipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BetInfo> betInfos;
    private Context context;

    public BetSlipAdapter(List<BetInfo> betInfos, Context context) {
        this.betInfos = betInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BetSlipViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bet_slip_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final BetInfo betInfo = betInfos.get(i);
        CommonUtils.updateLocImage(context, ((BetSlipViewHolder) viewHolder).iv_lottery_type, betInfo.getLotCode(), betInfo.getLotIcon());
        ((BetSlipViewHolder) viewHolder).tv_lottery_name.setText(betInfo.getLottery_type());
        String lottery_qihao = betInfo.getLottery_qihao();
        if (lottery_qihao != null) {
            int length = lottery_qihao.length();
            if (length > 6) {
                lottery_qihao = lottery_qihao.substring(length - 6, length);
            }
        }
        ((BetSlipViewHolder) viewHolder).tv_lottery_qihao.setText("期号:" + lottery_qihao + "期");
        ((BetSlipViewHolder) viewHolder).tv_lottery_play.setText(betInfo.getLottery_play());
        ((BetSlipViewHolder) viewHolder).tv_lottery_content.setText(betInfo.getLottery_content());
        String lottery_amount = betInfo.getLottery_amount();
        if (TextUtils.isEmpty(lottery_amount)) {
            lottery_amount = "0";
        }
        float amount = Float.parseFloat(lottery_amount);
        ((BetSlipViewHolder) viewHolder).tv_lottery_amount.setText((float) Math.round(amount * 100) / 100 + "");
        ((BetSlipViewHolder) viewHolder).tv_follow_bet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFollowBetListener != null) {
                    onFollowBetListener.onSingleFollowBet(betInfo, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return betInfos == null ? 0 : betInfos.size();
    }

    class BetSlipViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_lottery_type;
        TextView tv_lottery_name;
        TextView tv_lottery_qihao;
        AlwaysMarqueeTextView tv_lottery_play;
        TextView tv_lottery_content;
        TextView tv_lottery_amount;
        TextView tv_follow_bet;


        public BetSlipViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_lottery_type = itemView.findViewById(R.id.iv_lottery_type);
            tv_lottery_name = itemView.findViewById(R.id.tv_lottery_name);
            tv_lottery_qihao = itemView.findViewById(R.id.tv_lottery_qihao);
            tv_lottery_play = itemView.findViewById(R.id.tv_lottery_play);
            tv_lottery_content = itemView.findViewById(R.id.tv_lottery_content);
            tv_lottery_amount = itemView.findViewById(R.id.tv_lottery_amount);
            tv_follow_bet = itemView.findViewById(R.id.tv_follow_bet);
        }
    }

    private OnSingleFollowBetListener onFollowBetListener;

    public void setOnFollowBetListener(OnSingleFollowBetListener onFollowBetListener) {
        this.onFollowBetListener = onFollowBetListener;
    }

    public interface OnSingleFollowBetListener {
        void onSingleFollowBet(BetInfo betInfo, int position);
    }
}
