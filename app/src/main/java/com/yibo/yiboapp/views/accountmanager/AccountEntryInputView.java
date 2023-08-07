package com.yibo.yiboapp.views.accountmanager;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yibo.yiboapp.R;

public class AccountEntryInputView extends AccountEntryView {

    private TextView textTitle;
    private TextView textStar;
    private EditText editContent;

    public AccountEntryInputView(Context context, EntryBean bean){
        super(context);
        this.entryBean = bean;
        buildView(bean);
    }

    @Override
    protected void buildView(EntryBean bean) {
        View view = View.inflate(getContext(), R.layout.view_account_entry_input, this);
        textTitle = view.findViewById(R.id.text_title);
        textStar = view.findViewById(R.id.text_star);
        editContent = view.findViewById(R.id.edit_content);

        if(bean != null){
            textTitle.setText(bean.getTitle());
            textStar.setVisibility(bean.isRequired() ? View.VISIBLE : View.INVISIBLE);
            editContent.setHint(bean.getHint());

            if(bean.getInputType() == TYPE_PASSWORD){
                editContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            if(!TextUtils.isEmpty(bean.getContent())){
                editContent.setText(bean.getContent());
            }

            editContent.setEnabled(bean.isEditable());
        }
    }

    @Override
    public EntryBean getUpdatedEntryBean() {
        if(entryBean != null){
            String content = editContent.getText().toString().trim();
            entryBean.setContent(content);
        }

        return entryBean;
    }
}
