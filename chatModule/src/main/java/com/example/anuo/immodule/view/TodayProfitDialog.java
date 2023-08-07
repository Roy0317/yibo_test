package com.example.anuo.immodule.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.TodayProfitResponse;

import java.util.List;

public class TodayProfitDialog extends Dialog {

    private List<TodayProfitResponse.Prize> prizeList;

    public TodayProfitDialog(Context context, List<TodayProfitResponse.Prize> list){
        super(context);
        this.prizeList = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_today_profit);
        windowDeploy();
        initViews();
    }

    /**
     * 设置窗口显示
     */
    public void windowDeploy() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //设置显示宽高
        layoutParams.height = d.getHeight() / 5 * 3;
        layoutParams.width = d.getWidth() / 6 * 5;
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    private void initViews(){
        findViewById(R.id.imageClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextView textEmpty = findViewById(R.id.textEmpty);

        if(prizeList == null || prizeList.isEmpty()){
            textEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            textEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            PrizeAdapter adapter = new PrizeAdapter(prizeList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    private static class PrizeAdapter extends RecyclerView.Adapter<PrizeViewHolder>{

        private List<TodayProfitResponse.Prize> prizeList;

        PrizeAdapter(List<TodayProfitResponse.Prize> list){
            this.prizeList = list;
        }

        @Override
        public int getItemCount() { return prizeList.size(); }

        @Override
        public PrizeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = View.inflate(viewGroup.getContext(), R.layout.item_today_profit, null);
            return new PrizeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TodayProfitDialog.PrizeViewHolder prizeViewHolder, int i) {
            prizeViewHolder.showPrizeInfo(i, prizeList.get(i));
        }
    }

    private static class PrizeViewHolder extends RecyclerView.ViewHolder{

        private TextView textRank;
        private TextView textAccount;
        private TextView textLottery;
        private TextView textMoney;


        PrizeViewHolder(View view){
            super(view);

            textRank = view.findViewById(R.id.textRank);
            textAccount = view.findViewById(R.id.textAccount);
            textLottery = view.findViewById(R.id.textLottery);
            textMoney = view.findViewById(R.id.textMoney);
        }

        public void showPrizeInfo(int position, TodayProfitResponse.Prize prize){
            switch (position){
                case 0:
                    textRank.setVisibility(View.VISIBLE);
                    textRank.setBackgroundResource(R.drawable.bg_prize_1st);
                    textRank.setText("1");
                    break;
                case 1:
                    textRank.setVisibility(View.VISIBLE);
                    textRank.setBackgroundResource(R.drawable.bg_prize_2nd);
                    textRank.setText("2");
                    break;
                case 2:
                    textRank.setVisibility(View.VISIBLE);
                    textRank.setBackgroundResource(R.drawable.bg_prize_3rd);
                    textRank.setText("3");
                    break;
                default:
                    textRank.setVisibility(View.INVISIBLE);
                    break;
            }

            textAccount.setText(prize.getUserName());
            textLottery.setText(prize.getPrizeProject());
            textMoney.setText(String.valueOf(prize.getPrizeMoney()));
        }
    }
}
