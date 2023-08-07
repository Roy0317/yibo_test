package com.simon.base;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simon.R;
import com.simon.eunmmodel.ELoadingType;
import com.simon.listener.DataErrorCallBack;
import com.simon.view.ActionView;
import com.simon.view.RootView;
import com.simon.view.loadmore.LoadMoreRecyclerAdapter;
import com.simon.widget.AppManager;
import com.simon.widget.RepeatedClickHandler;
import com.simon.widget.skinlibrary.base.SkinBaseActivity;


/**
 * Created by simon on 16/12/1.
 */

public abstract class BaseActivity extends SkinBaseActivity implements View.OnClickListener, BaseInterface {

    protected Activity act;
    /**
     * 内容容器
     */
    public RootView screentView;
    protected View contentView;

    protected ActionView errorview;
    /**
     * 数据为空时显示的占位视图
     */
//    protected ActionView emptyView;
    protected ActionView loadingView;
    protected View emptyView;
    /**
     * 默认加载类型
     */
    public ELoadingType loadingType = ELoadingType.TypeDialog;
    /**
     * 解决连点问题
     */
    private RepeatedClickHandler repeatedClickHandler;

    /**
     * 加载动画
     */
    protected ProgressDialog loadDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screentView = setView();
        if (screentView == null || contentView == null) {
            throw new IllegalStateException("content view and contentView must not be empty");
        }

        initViewBefore();
        setContentView(screentView);
        AppManager.getInstance().addActivity(this);
        repeatedClickHandler = new RepeatedClickHandler();
        act = this;
        initView(savedInstanceState);
        initListener();
        initData();
    }

    /**
     * 设置显示的视图
     */
    private RootView setView() {
        RootView view = new RootView(this);
        contentView = view.setContentView(View.inflate(this, setContentViewRes(), null));
        return view;
    }


    /**
     * 初始化view之前执行方法（根据当前项目添加方法）
     */
    protected abstract void initViewBefore();

    /**
     * 设置内容布局
     *
     * @return
     */
    protected abstract int setContentViewRes();

    /**
     * 初始化界面
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化事件监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();


    @Override
    public void onClick(View view) {
        if (repeatedClickHandler != null) {
            repeatedClickHandler.handleRepeatedClick(view);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        dismissProgress();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadDialog = null;
        AppManager.getInstance().remove(this);
    }


    /**
     * 添加fragment
     *
     * @param id
     * @param fragment
     */
    protected void addFragment(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(id, fragment, null);
        ft.commit();
    }


    /**
     * 设置第一次加载数据时显示的动画
     */
    private void setLoadingView() {
        screentView.removeView(errorview);
        screentView.removeView(loadingView);

        errorview = loadingView = null;

        contentView.setVisibility(View.INVISIBLE);

        loadingView = new ActionView(this);
        loadingView.addLoadingView();

        screentView.setContentView(loadingView);
    }


    public void dismissProgress() {
        dismissProgressLoadDialog();

        dissShowProgressInside();
        //恢复到默认值
        if (loadingType != ELoadingType.TypeDialog) {
            loadingType = ELoadingType.TypeDialog;
        }
    }


    /**
     *
     */
    private void dissShowProgressInside() {
        viewAnimation(contentView, errorview);
        viewAnimation(contentView, loadingView);
    }


    /**
     * @param tipTxt
     * @param pageNo
     * @param drawable 空占位图片
     * @param adapter
     */
    public void setEmptyView(String tipTxt, int pageNo, Drawable drawable, LoadMoreRecyclerAdapter adapter) {
        if (pageNo == 1 && adapter.getItemCountHF() == 0) {
            if (emptyView == null) {
                emptyView = View.inflate(this, R.layout.layout_empty, null);
            }
            if (!TextUtils.isEmpty(tipTxt)) {
                TextView txtView = emptyView.findViewById(R.id.txtEmtpy);
                txtView.setText(tipTxt);
            }

            if (drawable != null)
                ((ImageView) emptyView.findViewById(R.id.imageEmpty)).setImageDrawable(drawable);

            adapter.addFooter(emptyView);
        } else {
            if (emptyView != null) {
                adapter.removeFooter(emptyView);
                emptyView = null;
            }
        }
    }


    /**
     * 根据出错类型自动显示相应的提示界面
     *
     * @param listener
     */
    protected void errorHappen(int errorType, DataErrorCallBack listener) {
//        errorHappen(listener);
    }

    /**
     * 只有第一页出现才显示这个
     *
     * @param pageSize
     * @param errorType
     * @param listener
     */
    protected void errorHappen(int pageSize, int errorType, DataErrorCallBack listener) {
        if (pageSize == 1)
            errorHappen(errorType, listener);
    }

    /**
     * 错误发生了
     *
     * @param listener
     * @param view
     * @param id
     */
    protected void errorHappen(final DataErrorCallBack listener, View view, int... id) {
        screentView.removeView(errorview);
        errorview = null;

        contentView.setVisibility(View.INVISIBLE);

        errorview = new ActionView(this);
        errorview.addActionView(listener, view, id);

        if (loadingView == null)
            screentView.setContentView(errorview);
        else
            viewAnimation(errorview, loadingView);
    }

    /**
     * 是否显示对话框
     *
     * @param show
     */
    public void ifShowProgress(boolean show) {
        if (show)
            showProgress();
    }

    /**
     * @param diss
     */
    public void ifDissmissProgress(boolean diss) {
        if (diss)
            dismissProgress();
    }


    /**
     * 显示加载进度条
     */
    public void showLoadDialog(boolean canCancle) {
        try {
            if (!isFinishing()) {
                if (loadDialog == null) {
                    loadDialog = new ProgressDialog(this);
                    loadDialog.setCanceledOnTouchOutside(canCancle);
                    loadDialog.setCancelable(canCancle);
                }
                if (!loadDialog.isShowing()) {
                    loadDialog.show();
                }
            }
        } catch (Exception e) {
        }
    }


    /**
     * 停止显示加载进度条
     */
    public void dismissProgressLoadDialog() {
        try {
            if (!isFinishing()) {
                if (loadDialog != null && loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
            }
        } catch (Exception e) {
        }
    }


    public void showProgress() {
        showProgress(true);
    }


    public void showProgress(boolean canCancle) {
        if (loadingType == ELoadingType.TypeDialog) {
            showLoadDialog(canCancle);
        } else if (loadingType == ELoadingType.TypeInside) {
            setLoadingView();
        }
    }


    public void showProgress(ELoadingType type) {
        loadingType = type;
        showProgress();
    }


    protected void setTitleView(View titleView) {
        screentView.setTitleView(titleView);
    }


    /**
     * @param showView
     * @param dissMissView
     */
    private void viewAnimation(final View showView, final View dissMissView) {
        if (dissMissView == null) return;

        if (showView != null && showView.getVisibility() == View.VISIBLE &&
                dissMissView.getVisibility() != View.VISIBLE) {
            screentView.removeView(dissMissView);
            return;
        }

        if (showView == contentView && showView.getVisibility() != View.VISIBLE)
            showView.setVisibility(View.VISIBLE);

        if (contentView != null && showView != contentView)
            screentView.setContentView(showView);

        screentView.removeView(dissMissView);
        screentView.setContentView(dissMissView);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(ObjectAnimator.ofFloat(showView, "alpha", 0.0f, 1.0f), ObjectAnimator.ofFloat(dissMissView, "alpha", 1, 0.0f));
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                screentView.removeView(dissMissView);
                if (dissMissView == errorview) {
                    errorview = null;
                }
                if (dissMissView == loadingView) {
                    loadingView = null;
                }
            }
        });
        set.setDuration(300).start();
    }


    /**
     * 设置状态栏是否透明
     */
    public boolean initStatusMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return true;
        } else {
            return false;
        }
    }


    /**
     * 内容布局是否覆盖在topView上
     *
     * @param isCover
     */
    public void coverContentLayout(boolean isCover) {
        screentView.coverContentLayout(isCover);
    }


    /**
     * 显示Toast
     *
     * @param text 文本内容
     */
    public void MyToast(String text) {
        if (TextUtils.isEmpty(text))
            return;
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String text) {
        if (TextUtils.isEmpty(text))
            return;
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    /**
     * 显示Toast
     *
     * @param resId string资源id
     */
    protected void MyToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null) {
            Configuration config = res.getConfiguration();
            if (config != null && config.fontScale != 1.0f) {
                config.fontScale = 1.0f;
                res.updateConfiguration(config, res.getDisplayMetrics());
            }
        }
        return res;
    }
}