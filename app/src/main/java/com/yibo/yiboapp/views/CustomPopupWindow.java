package com.yibo.yiboapp.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.ZhangbianInfoActivity;
import com.yibo.yiboapp.data.UsualMethod;

/**
 * 统一封装下拉菜单
 */
public class CustomPopupWindow extends PopupWindow {

    private Context context;
    private TextView tvType;
    private String type="";

    public CustomPopupWindow(Context context, TextView textView, String[] arrays) {
        super(context);
        initView(context, textView, arrays);
    }

    public CustomPopupWindow(Context context, AttributeSet attrs, Context context1, TextView tvType, String type) {
        super(context, attrs);
        this.context = context1;
        this.tvType = tvType;
        this.type = type;
    }


    private void initView(Context context, TextView tvType, String[] arrays) {
        this.context = context;
        this.tvType = tvType;
        initPopupWindowContent( arrays, tvType);
    }


    /**
     * 初始化popupWindow内容
     *
     * @param arrays
     * @param tv
     */
    private void initPopupWindowContent( final String[] arrays, TextView tv) {

        setWidth(tv.getWidth());
        setOutsideTouchable(true);
        setFocusable(true);//必须写,不写后果自负
        ListView listView = new ListView(context);
        listView.setDividerHeight(2);
        listView.setBackgroundColor(Color.WHITE);
        final MyBaseAdapter adapter = new MyBaseAdapter(arrays);
        listView.setAdapter(adapter);
        adapter.setSelectPosition(0);

        //获取listView的每一个item的高度
        View listItem = listView.getAdapter().getView(0, null, listView);
        listItem.measure(0, 0);
        int listItemHeight = listItem.getMeasuredHeight();
//        showToast("listItemHeight：" + listItemHeight + ",width:" + tv.getWidth() + ",arrays的长度:" + arrays.length);
        //判断listView的总高度如果大于tv.getWidth(),则设置高度为tv.getWidth()
        if (arrays.length * listItemHeight >= tv.getWidth()) {
            setHeight(tv.getWidth());
        }

        //设置内容
        setContentView(listView);
        setOnDismissListener(() -> tvType.setTextColor(context.getResources().getColor(R.color.system_default_color)));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = arrays[position];
            tvType.setText(item);
            adapter.setSelectPosition(position);
            dismiss();
            type = UsualMethod.getAccountByCode(item);
        });

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class MyBaseAdapter extends BaseAdapter {

        private String[] arrays;

        public MyBaseAdapter(String[] arrays) {
            this.arrays = arrays;
        }

        private int selectPosition = 0;

        public void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrays.length;
        }

        @Override
        public Object getItem(int position) {
            return arrays[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyBaseAdapter.Holder holder;
            if (convertView == null) {
                holder = new MyBaseAdapter.Holder();
                convertView = View.inflate(context, R.layout.adapter_item_popwindow, null);
                holder.textView = convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (MyBaseAdapter.Holder) convertView.getTag();
            }
            String newText = arrays[position];
            if (newText.contains(",")) {
                newText = newText.split(",")[0];
            }

            holder.textView.setText(newText);

            if (selectPosition == position) {
                holder.textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.textView.setBackgroundResource(R.color.check_bg);
            } else {
                holder.textView.setTextColor(context.getResources().getColor(R.color.grey));
                holder.textView.setBackgroundResource(R.color.white);
            }

            return convertView;
        }

        class Holder {
            TextView textView;
        }

    }
}
