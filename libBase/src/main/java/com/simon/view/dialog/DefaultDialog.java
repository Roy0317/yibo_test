package com.simon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simon.R;


/**
 * 常用对话框工具类
 */
public class DefaultDialog extends Dialog {

    private Context mContext;
    private Window window = null;
    private DialogSelectListener mListener;
    private TextView btnOk;
    private TextView btnCancle;
    private TextView description;
    private TextView dailogTitle;
    private LinearLayout ll_foot_dialog;
    private LinearLayout ll_content_dialog;

    private RelativeLayout root;

    private View bottom_sep;
    private View content_sep;

    private int grvatity = -1;

    private Object descriptionStr;
    private String btnOkStr;
    private String btnCancleStr;
    private String dialogTitleStr;

    private int btnOkTextColor = -1;
    private int btnCancleTextColor = -1;
    private int descriptionTextColor = -1;
    private int size = -1;
    private int backgroundColor = -1;

    private boolean hideCancle = false;
    private boolean isHideFoot = false;
    private boolean cancel = false;


    //内容区域布局
    private View contentView;

    private View view;

    public DefaultDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.mContext = context;
        view = View.inflate(mContext, R.layout.dialog_default, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(view);
        this.window = getWindow();
//        Display localDisplay = this.window.getWindowManager().getDefaultDisplay();
        LayoutParams localLayoutParams = getWindow().getAttributes();
        // localLayoutParams.verticalMargin = -0.1F;
        localLayoutParams.height = LayoutParams.WRAP_CONTENT;
        localLayoutParams.width = LayoutParams.WRAP_CONTENT;
        // localLayoutParams.width = (int) (0.9D * localDisplay.getWidth());
        this.window.setAttributes(localLayoutParams);
        // this.window.setWindowAnimations(R.style.dialogWindowAnim);
        setCanceledOnTouchOutside(cancel);
        setCancelable(cancel);
        initViews();
    }

    private void initViews() {
        btnOk =  findViewById(R.id.btn_ok);
        btnCancle =  findViewById(R.id.btn_cancle);
        description =  findViewById(R.id.content);
        dailogTitle =  findViewById(R.id.dailogTitle);
        bottom_sep = findViewById(R.id.bottom_sep);
        content_sep = findViewById(R.id.content_sep);
        ll_foot_dialog =  findViewById(R.id.ll_foot_dialog);
        ll_content_dialog =  findViewById(R.id.ll_content_dialog);
        root =  findViewById(R.id.root);


        if (backgroundColor != -1)
            root.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));

        if (mListener != null) {
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onChlidViewClick(v,true);
                    DefaultDialog.this.cancel();
                }
            });
            btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onChlidViewClick(v,false);
                    DefaultDialog.this.cancel();
                }
            });
        }

        if (grvatity != -1) {
            description.setGravity(grvatity);
            description.setPadding(30, 0, 30, 0);
        }

        if (descriptionStr != null) {
            if (descriptionStr instanceof Spanned) {
                description.setText((Spanned) descriptionStr);
            } else {
                description.setText(descriptionStr + "");
            }
            description.setMovementMethod(ScrollingMovementMethod.getInstance());
        }

        if (btnOkStr != null) {
            btnOk.setText(btnOkStr);

        }
        if (btnCancleStr != null) {
            btnCancle.setText(btnCancleStr);
        }

        if (dialogTitleStr != null) {
            dailogTitle.setVisibility(View.VISIBLE);
            dailogTitle.setText(dialogTitleStr);
        }

        if (hideCancle) {
            btnCancle.setVisibility(View.GONE);
            bottom_sep.setVisibility(View.GONE);
        }
        //是否隐藏底部按钮
        if (isHideFoot) {
            content_sep.setVisibility(View.GONE);
            ll_foot_dialog.setVisibility(View.GONE);
        }

        if (contentView != null) {
            description.setVisibility(View.GONE);
            ll_content_dialog.addView(contentView);
        }

        if (descriptionTextColor != -1) {
            description.setTextColor(descriptionTextColor);
        }

        if (size != -1) {
            description.setTextSize(16);
            description.setGravity(Gravity.LEFT);
        }


        if (btnCancleTextColor != -1) {
            btnCancle.setTextColor(btnCancleTextColor);
        }
        if (btnOkTextColor != -1) {
            btnOk.setTextColor(btnOkTextColor);
        }
    }

    public void setDialogListener(DialogSelectListener paramListener) {
        mListener = paramListener;
    }


    public void setDescription(Object str) {
        descriptionStr = str;
    }

    public void setBtnOk(String str) {
        btnOkStr = str;
    }

    public void setBtnCancle(String str) {
        btnCancleStr = str;
    }

    public void setDialogTitle(String title) {
        dialogTitleStr = title;
    }

    public void setBtnOkTextColor(int color) {
        btnOkTextColor = color;
    }

    public void setBtnCancleTextColor(int color) {
        btnCancleTextColor = color;
    }

    public void setDescriptionTextColor(int color) {
        descriptionTextColor = color;
    }


    public void setDescriptionTextSize(int size) {
        this.size = size;
    }


    public void setContentsView(View contentView) {
        this.contentView = contentView;
    }

    public View getRootView() {
        return view;
    }

    /**
     * 点击dialog之外区域是否dismiss
     *
     * @param cancel
     */
    public void setCancelOnTouchOutside(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * 隐藏底部按钮
     */
    public void hideFoot(boolean isHideFoot) {
        this.isHideFoot = isHideFoot;

    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    public TextView getBtnOk() {
        return btnOk;
    }

    public void setBtnOk(TextView btnOk) {
        this.btnOk = btnOk;
    }

    /**
     * @param gavity
     */
    public void setContentGravity(int gavity) {
        this.grvatity = gavity;
    }

    public void HideCancleBtn() {
        hideCancle = true;
    }
}
