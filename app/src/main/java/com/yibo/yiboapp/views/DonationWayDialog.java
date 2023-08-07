package com.yibo.yiboapp.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

import com.yibo.yiboapp.R;

public class DonationWayDialog extends Dialog {
    public static final int DONATE_MONEY = 11;
    public static final int DONATE_PERCENT = 12;

    private RadioButton radioMoney;
    private RadioButton radioPercent;
    private DonationWayListener wayListener;

    public DonationWayDialog(Context context, DonationWayListener l){
        super(context);
        this.wayListener = l;

        View view = View.inflate(context, R.layout.dialog_donation_way, null);
        radioMoney = (RadioButton) view.findViewById(R.id.radioMoney);
        radioPercent = (RadioButton) view.findViewById(R.id.radioPercent);
        setContentView(view);

        view.findViewById(R.id.linearMoney).setOnClickListener(v -> {
            if(wayListener != null){
                wayListener.onChooseDonationWay(DONATE_MONEY);
                dismiss();
            }
        });

        view.findViewById(R.id.linearPercent).setOnClickListener(v -> {
            if(wayListener != null){
                wayListener.onChooseDonationWay(DONATE_PERCENT);
                dismiss();
            }
        });

        setCancelable(true);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失
    }

    public void setCurrentWay(int currentWay){
        switch (currentWay){
            case DONATE_MONEY:
                radioMoney.setChecked(true);
                break;
            case DONATE_PERCENT:
                radioPercent.setChecked(true);
                break;
        }
    }

    public interface DonationWayListener{
        void onChooseDonationWay(int way);
    }
}
