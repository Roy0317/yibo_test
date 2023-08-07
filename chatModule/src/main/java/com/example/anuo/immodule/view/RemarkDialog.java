package com.example.anuo.immodule.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.anuo.immodule.R;

public class RemarkDialog extends Dialog {

    private EditText editRemark;
    private RemarkListener remarkListener;


    public RemarkDialog(Context context, RemarkListener l){
        super(context);
        this.remarkListener = l;

        View view = View.inflate(context, R.layout.dialog_remark, null);
        editRemark = view.findViewById(R.id.editRemark);
        setContentView(view);

        view.findViewById(R.id.textConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                remarkListener.onGetRemark(editRemark.getText().toString());
                dismiss();
            }
        });

        view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                dismiss();
            }
        });

        setCancelable(true);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失
    }

    private void hideSoftInput(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public interface RemarkListener{
        void onGetRemark(String remark);
    }
}
