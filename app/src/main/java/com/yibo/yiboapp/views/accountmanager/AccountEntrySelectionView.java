package com.yibo.yiboapp.views.accountmanager;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;

public class AccountEntrySelectionView extends AccountEntryView {

    private TextView textTitle;
    private TextView textContent;
    private LinearLayout linearOther;
    private TextView textOtherTitle;
    private EditText editOtherContent;
    private View dividerOther;

    public AccountEntrySelectionView(Context context, EntryBean bean){
        super(context);
        this.entryBean = bean;
        buildView(bean);
    }

    @Override
    protected void buildView(EntryBean bean) {
        View view = View.inflate(getContext(), R.layout.view_account_entry_selection, this);
        textTitle = view.findViewById(R.id.text_title);
        textContent = view.findViewById(R.id.text_content);
        linearOther = view.findViewById(R.id.linear_other);
        textOtherTitle = view.findViewById(R.id.text_other_title);
        editOtherContent = view.findViewById(R.id.edit_other_content);
        dividerOther = view.findViewById(R.id.divider_other);

        if(bean != null){
            textTitle.setText(bean.getTitle());
            textContent.setText(bean.getHint());
            if(bean.isRequired()){
                view.findViewById(R.id.text_star).setVisibility(View.VISIBLE);
                view.findViewById(R.id.text_star_other).setVisibility(View.VISIBLE);
            }else {
                view.findViewById(R.id.text_star).setVisibility(View.GONE);
                view.findViewById(R.id.text_star_other).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public EntryBean getUpdatedEntryBean() {
        String content = textContent.getText().toString().trim();
        String otherContent = editOtherContent.getText().toString().trim();

        if(entryBean != null){
            if(linearOther.getVisibility() == View.VISIBLE){
                entryBean.setContent(otherContent);
            }else if(content.equals(entryBean.getHint())){
                entryBean.setContent("");
            }else {
                entryBean.setContent(content);
            }
        }

        return entryBean;
    }

    public void setSelectionClickListener(OnClickListener listener){
        textContent.setOnClickListener(listener);
    }

    public void setContent(String content){ textContent.setText(content); }

    public void showOtherEntry(String otherTitle, String otherHint){
        linearOther.setVisibility(View.VISIBLE);
        dividerOther.setVisibility(View.VISIBLE);
        textOtherTitle.setText(otherTitle);
        editOtherContent.setHint(otherHint);
    }

    public void hideOtherEntry(){
        linearOther.setVisibility(View.GONE);
        dividerOther.setVisibility(View.GONE);
    }
}
