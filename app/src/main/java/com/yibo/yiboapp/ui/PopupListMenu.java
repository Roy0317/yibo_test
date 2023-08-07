package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.yibo.yiboapp.R;
import com.yibo.yiboapp.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class PopupListMenu {

    PopupWindow mPopupWindow;
    Context mContext;
    ListView mListView;
    PopupMenuAdapter mListAdapter;
    String[] mItems;
    OnItemClickListener mItemClickListener;
    boolean dimEffect;
    boolean showAnimation;
    int animStyle;
    int width;
    int height;

    public PopupListMenu(final Context mContext, String[] mItems) {
        // TODO Auto-generated constructor stub
        this.mItems = mItems;
        this.mContext = mContext;
        animStyle = R.style.list_anim_style;
        createPopupWindow(buildListView());
    }

    public PopupListMenu(final Context mContext, String[] mItems, int width, int height) {
        // TODO Auto-generated constructor stub
        this.mItems = mItems;
        this.mContext = mContext;
        animStyle = R.style.list_anim_style;
        this.width = width;
        this.height = height;
        createPopupWindow(buildListView(), width, height);
    }

    /**
     * whether to show dim effect or not
     *
     * @param dimEffect
     */
    public void setDimEffect(boolean dimEffect) {
        this.dimEffect = dimEffect;
        if (mPopupWindow == null)
            return;
        if (this.dimEffect) {
            mPopupWindow.setOnDismissListener(new FrameDismissListener());
            if (mContext instanceof Activity) {
                WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
                params.alpha = 0.5f;
                ((Activity) mContext).getWindow().setAttributes(params);
            }
        }
    }

    /**
     * whether to show aninmation or not
     *
     * @param show
     */
    public void setAnimation(boolean show) {
        this.showAnimation = show;
        if (mPopupWindow == null)
            return;
        if (this.showAnimation) {
            mPopupWindow.setAnimationStyle(animStyle);
        }
    }

    public void setAnimStyle(int style) {
        this.animStyle = animStyle;
        if (this.showAnimation) {
            mPopupWindow.setAnimationStyle(animStyle);
        }
    }

    /**
     * build the content listview for popupwindow
     *
     * @return
     */
    private ListView buildListView() {
        mListView = (ListView) LayoutInflater.from(mContext).inflate(R.layout.view_popup_menu, null).findViewById(R.id.listview);
//        mListView.setDivider(mContext.getResources().getColor(R.color.colorPrimary));
        mListView.setDivider(mContext.getResources().getDrawable(R.color.driver_line_color));
        mListView.setDividerHeight(3);
        mListAdapter = new PopupMenuAdapter(mContext, Arrays.asList(mItems), R.layout.popup_menu_item);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
                return true;
            }
        });
        return mListView;
    }


    public View getContentView() {
        return mListView;
    }

    public void createPopupWindow(ListView listView) {
        mPopupWindow = new PopupWindow(mListView, Utils.screenInfo(mContext).widthPixels / 2, LayoutParams.WRAP_CONTENT);
        //mPopupWindow.setBackgroundDrawable(new ColorDrawable(00000000));
        //mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
    }

    public void createPopupWindow(ListView listView, int width, int height) {
        mPopupWindow = new PopupWindow(mListView, width, height);
        //mPopupWindow.setBackgroundDrawable(new ColorDrawable(00000000));
        //mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
    }

    /**
     * pop dismiss listener
     */
    private final class FrameDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            if (!dimEffect) {
                return;
            }
            if (mContext instanceof Activity) {
                WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
                params.alpha = 1.0f;
                ((Activity) mContext).getWindow().setAttributes(params);
            }
        }
    }


    public void setBackground(int resId) {
        mListView.setBackgroundResource(resId);
    }


    public void setOnItemClickListener(OnItemClickListener mListener) {

        mItemClickListener = mListener;
    }

    public void show(View view, int xoff, int yoff) {

        if (view == null || mPopupWindow.isShowing())
            return;
        mPopupWindow.showAsDropDown(view, xoff, yoff);
    }

    public void showAtLocation(View view, int gravity, int xoff, int yoff) {
        mPopupWindow.showAtLocation(view, gravity, xoff, yoff);
    }

    class PopupMenuAdapter extends LAdapter<String> {

        public PopupMenuAdapter(Context mContext, List<String> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, String item) {

            TextView tvItem = holder.getView(R.id.tvItem);
            tvItem.setText(item);

            holder.getConvertView().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    if (mItemClickListener != null) {
                        mItemClickListener.OnItemClick(v, position);
                    }
                }
            });
        }

    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public interface OnItemClickListener {

        void OnItemClick(View view, int position);
    }
}
