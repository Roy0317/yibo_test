package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.IncomeListData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncommeListAdapter extends BaseRecyclerAdapter {
    public IncommeListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IncommeListAdapter.ViewHolder(mInflater.inflate(R.layout.item_jijinzhangdan, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder viewHolder = (ViewHolder) holder;
        IncomeListData.IncomDetial data = (IncomeListData.IncomDetial) getList().get(position);
        Long longdate;
        Date date;
        if (!TextUtils.isEmpty(data.getStatDate())) {
            try {
                longdate = UsualMethod.dateToStamp(data.getStatDate(), "yyyy-MM-dd");
                date = new Date();
                date.setTime(longdate);
                Calendar ca = Calendar.getInstance();
                ca.clear();
                ca.setTime(date);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                String content="余额生金-"+formatter.format(ca.getTime())+"-收益发放";
                ca.add(ca.DATE,1);
                int Week = ca.get(Calendar.DAY_OF_WEEK);
                int Day = ca.get(Calendar.DAY_OF_MONTH);
                int mounth = ca.get(Calendar.MONTH);
                viewHolder.tv_week.setText(weeken(Week));
                viewHolder.tv_date.setText(mouthAndDay(Day, mounth));


                viewHolder.tv_content.setText(content);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {

        }

        viewHolder.tv_incomme.setText("+" + data.getIncome());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_week;
        private TextView tv_date;
        private TextView tv_incomme;
        private TextView tv_content;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_week = itemView.findViewById(R.id.tv_week);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_incomme = itemView.findViewById(R.id.tv_incomme);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

    //根据值返回星期数
    String weeken(int Week) {
        String str = "";
        switch (Week) {
            case 1:
                str = "周日";
                break;
            case 2:
                str = "周一";
                break;

            case 3:
                str = "周二";
                break;

            case 4:
                str = "周三";
                break;

            case 5:
                str = "周四";
                break;

            case 6:
                str = "周五";
                break;

            case 7:
                str = "周六";
                break;
        }

        return str;
    }

    //传入日和月份 返回“03-11”格式的字符串
    String mouthAndDay(int day, int mounth) {
        String d;
        String m;
        mounth = mounth + 1;
        d = day + "";
        m = mounth + "";
        if (day < 10) {
            d = "0" + d;
        }
        if (mounth < 10) {
            m = "0" + m;
        }
        return m + "-" + d;
    }

}
