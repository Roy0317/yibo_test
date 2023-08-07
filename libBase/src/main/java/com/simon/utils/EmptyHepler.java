package com.simon.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.simon.R;
import com.simon.view.RecycleEmptyView;


public class EmptyHepler {

    /**
     * 给recyclerView加一个数据为空时EmptyView recyclerView外层最好用FrameLayout单独包着
     *
     * @param context
     * @param rv
     * @param resourceId
     * @param message
     * @param listener
     */
    public static void setNoDataEmptyView(Context context, RecycleEmptyView rv, int resourceId, String message, OnClickListener listener) {
        ViewGroup parentView = (ViewGroup) rv.getParent();

        removeItem(parentView, rv, R.id.id_recycler_empty);

        View inflate = getEmptyView(context, resourceId, message, listener);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        parentView.addView(inflate, params);
        rv.setEmptyView(inflate);
        rv.setTag(R.id.id_recycler_empty, inflate);
    }


    /**
     * @param view
     */
    public static void removeHalfRefreshView(View view) {
        ViewGroup parentView = (ViewGroup) view.getParent();
        if (parentView != null) {
            removeItem(parentView, view, R.id.id_half_refresh_view);
        }
    }

    /**
     * @param context
     * @param rv
     * @param listener
     */
    public static void setNoDataEmptyView(Context context, RecycleEmptyView rv, OnClickListener listener) {
        setNoDataEmptyView(context, rv, 0, null, listener);
    }


    /**
     * 给listView加一个数据为空时EmptyView listview外层最好用FrameLayout单独包着
     *
     * @param context
     * @param lv
     * @param resourceId
     * @param message
     * @param listener
     */
    public static void setNoDataEmptyView(Context context, AbsListView lv, int resourceId, String message, OnClickListener listener) {
        ViewGroup parentView = (ViewGroup) lv.getParent();

        removeItem(parentView, lv, R.id.id_listView_empty);

        View inflate = getEmptyView(context, resourceId, message, listener);
        parentView.addView(inflate);
        lv.setEmptyView(inflate);
        lv.setTag(R.id.id_listView_empty, inflate);
    }

    /**
     * @param context
     * @param lv
     */
    public static void removeAllItem(Context context, AbsListView lv) {
        ViewGroup parentView = (ViewGroup) lv.getParent();
        removeItem(parentView, lv, R.id.id_listView_empty);
        lv.setEmptyView(null);
    }

    /**
     * 删除上一个EmptyView
     *
     * @param parentView
     * @param lv
     * @param index
     */
    private static void removeItem(ViewGroup parentView, View lv, int index) {

        Object tag = lv.getTag(index);
        if (tag != null && tag instanceof View) {
            parentView.removeView((View) tag);
            lv.setTag(index, null);
        }
    }

    /**
     * 得到一个数据为空时的EmptyView
     *
     * @param context
     * @param resourceId
     * @param str
     * @param click
     * @return
     */
    public static View getEmptyView(Context context, int resourceId, String str, OnClickListener click) {

        View emptyView = View.inflate(context, R.layout.layout_empty, null);
        TextView txt_emtpy = emptyView.findViewById(R.id.txtEmtpy);
        if (str != null) {
            txt_emtpy.setText(str);
        }

        if (resourceId != 0) {
            ImageView image_empty = emptyView.findViewById(R.id.imageEmpty);
            image_empty.setImageResource(resourceId);
        }

        if (click != null) {
            emptyView.setOnClickListener(click);
        }

        return emptyView;

    }


    public interface OnAnimEnd {
        void end();
    }


    /***
     *
     * @param v
     */
    public static void setOnTouch(View v) {
//        v.setOnTouchListener((view, event) -> setTouch(view, event));
    }

    /**
     * @param view
     * @param event
     * @return
     */
    private static boolean setTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.setAlpha(0.6f);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                view.setAlpha(1.0f);
                break;
            default:
                break;
        }
        return false;
    }

}
