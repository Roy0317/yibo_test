package com.yibo.yiboapp.views.accountmanager;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.utils.Utils;

public class AccountEntryHintView extends AccountEntryView {

    public AccountEntryHintView(Context context, EntryBean bean){
        super(context);
        this.entryBean = bean;
        buildView(bean);
    }

    @Override
    protected void buildView(EntryBean entryBean) {
        TextView textView = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setText(entryBean.getHint());
        textView.setTextColor(Utils.getColor(getContext(), R.color.color_red_press));
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);
        addView(textView);
    }

    @Override
    public EntryBean getUpdatedEntryBean() {
        return null;
    }
}
