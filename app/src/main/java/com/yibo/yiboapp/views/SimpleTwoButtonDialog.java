package com.yibo.yiboapp.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yibo.yiboapp.R;

public class SimpleTwoButtonDialog extends Dialog {

    private SimpleTwoButtonDialogListener listener;

    public SimpleTwoButtonDialog(Context context, String title, String message, String left, String right){
        super(context);

        View view = View.inflate(context, R.layout.dialog_simple_two_button, null);
        TextView textTitle = (TextView) view.findViewById(R.id.textTitle);
        textTitle.setText(title);
        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(message);
        TextView textLeft = (TextView) view.findViewById(R.id.textLeft);
        textLeft.setText(left);
        TextView textRight = (TextView) view.findViewById(R.id.textRight);
        textRight.setText(right);

        textLeft.setOnClickListener(v -> {
            if(listener != null) listener.onButtonClicked(true);
        });

        textRight.setOnClickListener(v -> {
            if(listener != null) listener.onButtonClicked(false);
        });

        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void setListener(SimpleTwoButtonDialogListener l){ this.listener = l; }

    public interface SimpleTwoButtonDialogListener{
        void onButtonClicked(boolean isLeft);
    }
}
