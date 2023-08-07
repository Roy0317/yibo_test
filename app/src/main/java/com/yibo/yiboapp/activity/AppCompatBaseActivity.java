package com.yibo.yiboapp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.ListResultListener;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.ui.ProgressWheel;
import com.yibo.yiboapp.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


/**
 * base activity enterprise some base functions for each activity.
 *
 * @author johnson
 */
public class AppCompatBaseActivity extends BaseActivity implements OnClickListener {

    protected static final String TAG = "BaseActivity";

    protected RelativeLayout mLayout = null;
    protected TextView tvBackText;
    protected TextView tvMiddleTitle;
    protected TextView tvRightText;
    protected TextView tvSecondRightText;
    protected TextView tvSecondTitle;
    protected LinearLayout middle_layout;
    ProgressWheel progressWheel;
    protected RelativeLayout rightLayout;
    protected RelativeLayout rightIcon;
    protected TextView iconCount;
    protected ImageView titleIndictor;
    protected LinearLayout titleLayout;
    protected ImageView ivMoreMenu;

    DefaultListResultHandler defaultListResultHandler;//列表数据处理基础类


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Utils.useTransformBar(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void initView() {
        mLayout = (RelativeLayout) findViewById(R.id.title);
        if (mLayout != null) {
            tvBackText = (TextView) mLayout.findViewById(R.id.back_text);
            middle_layout = (LinearLayout) mLayout.findViewById(R.id.middle_layout);
            tvMiddleTitle = (TextView) middle_layout.findViewById(R.id.middle_title);
            tvRightText = (TextView) mLayout.findViewById(R.id.right_text);
            tvSecondRightText = (TextView) mLayout.findViewById(R.id.second_right_text);
            tvSecondTitle = (TextView) findViewById(R.id.second_title);
            progressWheel = (ProgressWheel) mLayout.findViewById(R.id.progress_wheel);
            ivMoreMenu = (ImageView) mLayout.findViewById(R.id.iv_more_menu);

            tvBackText.setOnClickListener(this);
            tvMiddleTitle.setOnClickListener(this);
            tvRightText.setOnClickListener(this);
            ivMoreMenu.setOnClickListener(this);
            tvSecondRightText.setOnClickListener(this);
            tvBackText.setVisibility(View.VISIBLE);
            tvRightText.setVisibility(View.GONE);

            rightLayout = (RelativeLayout) findViewById(R.id.right_layout);

            rightIcon = (RelativeLayout) findViewById(R.id.right_icon);
            iconCount = (TextView) rightIcon.findViewById(R.id.count);
            titleIndictor = (ImageView) findViewById(R.id.title_indictor);
            titleLayout = (LinearLayout) findViewById(R.id.clickable_title);
            rightIcon.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_text:
                hideKeyboard(this, v);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager == null) return;
            if (inputMethodManager.isActive())
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            System.out.println("e");
        }

    }


    protected void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int showText) {
        Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
    }

    protected void startProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.VISIBLE);
            progressWheel.spin();
        }
    }

    public void stopProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.GONE);
            if (progressWheel.isSpinning()) {
                progressWheel.stopSpinning();
            }
        }
    }

    /**
     * zhangy add 20161012 to optimize normal list crazt request and refresh UI code effect.
     * 提供列表crazy请求统一抽象封装处理单元，处理列表结果单元
     * 解决分页每页可应用crazy请求缓存提取及同步功能
     * 解决各模块需要根据请求页码判断是否同一页来防止列表数据异步加载刷新混乱问题，解耦业务强关联代码，减少重复代码
     * 解决需要根据数据获取来源来判断是否需要增加页码问题
     * 遗留问题：频繁上下拉列表后的多线程后台请求的流量，电量节省，前台响应慢的体验等问题。后续处理
     */
    public final class DefaultListResultHandler<T> implements ListResultListener<T> {

        Map<String, List<T>> map = null;

        public DefaultListResultHandler() {
            map = new HashMap<String, List<T>>();
        }

        @Override
        public List<T> sortList(List<T> originals, List<T> results, String url) {
            if (results == null || results.isEmpty()) {
                return originals;
            }
            if (map == null) {
                return originals;
            }
            map.put(url, results);
            originals.clear();//clear originals first for sake that we will loop all the results
            //and fill in it.
            /** sort each page results in sort map according to the url key. the only sort keyword
             *  can be compare is pageno column in url.
             */

            SortedMap<String, List<T>> n = new TreeMap<String, List<T>>(map);
            for (Map.Entry<String, List<T>> entry : n.entrySet()) {
                //zhang modify  20161103 to avoid last load results add to new result.
                //when switch list filter considence.
                //if (url.equals(entry.getKey())){
                originals.addAll(entry.getValue());
                //}
                //end modify
            }
            n.clear();
            return originals;
        }

        public void clear() {
            if (map != null) {
                map.clear();
            }
        }
    }

    //统一的列表结果处理接口，子类可用
    public <T> List<T> handleListResult(List<T> originals, List<T> result, String url, boolean clearFirst) {
        if (defaultListResultHandler == null) {
            defaultListResultHandler = new DefaultListResultHandler();
        }
        if (clearFirst) {
            defaultListResultHandler.clear();
        }
        return defaultListResultHandler.sortList(originals, result, url);
    }



    protected boolean handleCrazyResult(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
//        判断网站是否在维护中
        String cause = UsualMethod.isMaintenancing(response.result.error);
        if (!Utils.isEmptyString(cause)) {
            MintainceActivity.createIntent(this, cause);
            return false;
        }
        return true;
    }
}
