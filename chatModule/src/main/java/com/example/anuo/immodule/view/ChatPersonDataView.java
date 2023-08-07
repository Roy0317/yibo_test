package com.example.anuo.immodule.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.anuo.immodule.R;



public class ChatPersonDataView extends LinearLayout {

    private TextView valueTxt;

    public Switch mSwitch;

    public ChatPersonDataView(Context context) {
        this(context, null);
    }

    public ChatPersonDataView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatPersonDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ChatPersonDataView, defStyleAttr, 0);
        String left = arr.getString(R.styleable.ChatPersonDataView_cpdView_left_txt);
        boolean showRight = arr.getBoolean(R.styleable.ChatPersonDataView_cpdView_show_right, false);
        String right = arr.getString(R.styleable.ChatPersonDataView_cpdView_right_txt);
        boolean showSwitch = arr.getBoolean(R.styleable.ChatPersonDataView_cpdView_switch, false);
        arr.recycle();
        LayoutInflater.from(context).inflate(R.layout.item_person_data, this);
        TextView leftTxt = findViewById(R.id.item_person_data_title);
        leftTxt.setText(left);
        if (showRight) {
            TextView rightTxt = findViewById(R.id.item_person_data_refresh);
            rightTxt.setText(right);
            rightTxt.setVisibility(View.VISIBLE);
            rightTxt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chatPersonDataViewBtn != null) {
                        chatPersonDataViewBtn.onClick();
                    }
                }
            });
        }

        valueTxt = findViewById(R.id.item_person_data_value);

        if (showSwitch) {
            mSwitch = findViewById(R.id.item_person_data_switch);
            mSwitch.setVisibility(View.VISIBLE);
            valueTxt.setVisibility(View.INVISIBLE);
        }



    }

    public void setText(String value) {
        valueTxt.setText(value);
    }

    public interface ChatPersonDataViewBtn{
        void onClick();
    }

    private ChatPersonDataViewBtn chatPersonDataViewBtn = null;

    public void setChatPersonDataViewBtn(ChatPersonDataViewBtn chatPersonDataViewBtn){
        this.chatPersonDataViewBtn = chatPersonDataViewBtn;
    }

    public void setCheck(boolean b) {
        if (mSwitch != null) {
            mSwitch.setChecked(b);
        }
    }


}
