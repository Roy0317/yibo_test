package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.ChatMainActivity;
import com.example.anuo.immodule.bean.ChatLotteryBean;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;

import java.util.List;


public class ChatLotteryResultAdapter extends PagerAdapter {
    //上下文
    private Context mContext;
    //数据
    private List<ChatLotteryBean.SourceBean.LotteryDataBean> mData;


    public ChatLotteryResultAdapter(Context context, List<ChatLotteryBean.SourceBean.LotteryDataBean> list) {
        mContext = context;
        mData = list;
    }

    @Override
    public int getCount() {
        return mData.size() *10;
    }

    /**
     * 1.将当前视图添加到container中
     * 2.返回当前View
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = View.inflate(mContext, R.layout.item_lottery_result, null);
        TextView name = view.findViewById(R.id.item_lottery_result_name);
        ImageView img = view.findViewById(R.id.item_lottery_result_img);
        TextView time = view.findViewById(R.id.item_lottery_result_time);
        GridView gridView = view.findViewById(R.id.item_lottery_result_grid);

        ChatLotteryBean.SourceBean.LotteryDataBean bean = mData.get(position  % mData.size());
        name.setText(bean.getName());
        showImg(img, bean.getCode(), bean.getIcon());
        if (!TextUtils.isEmpty(bean.getQiHao())) {
            time.setText("第" + bean.getQiHao() + "期");
        }

        if (!TextUtils.isEmpty(bean.getHaoMa())) {
            List<String> haomas = CommonUtils.splitString(bean.getHaoMa(), ",");
            updateNumberGridView(haomas,gridView, bean.getCode(), bean.getData(), bean.getType() + "");
        } else {
            LotteryResultNullAdapter lotteryResultNullAdapter = new LotteryResultNullAdapter(mContext,5);
            gridView.setNumColumns(5);
            gridView.setAdapter(lotteryResultNullAdapter);
        }

        view.setTag(position  % mData.size());
        container.addView(view);
        return view;
    }


    @Override
    public int getItemPosition(Object object) {
        View view = (View)object;
        int currentPage = ((ChatMainActivity)mContext).getLotteryPos(); // Get current page index
        if(currentPage == (Integer)view.getTag()){
            return POSITION_NONE;
        }else{
            return POSITION_UNCHANGED;
        }
    }



    /**
     * 从当前container中删除指定位置（position）的View
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View) object);
    }

    /**
     * 确定页视图是否与特定键对象关联
     *
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



    private void showImg(ImageView img, String code , String url) {
        if (TextUtils.isEmpty(url)) {
            url =  ChatSpUtils.instance(mContext).getMainAppBaseUrl() + "/native/resources/images/" + code + ".png";
        }

        Glide.with(mContext)
                .load(url)
                .into(img);
        //彩种的图地址是根据彩种编码号为姓名构成的

    }

    /**
     * 根据开奖结果显示号码view
     *
     * @param haoMaList
     */
    private void updateNumberGridView(List<String> haoMaList, GridView numbersView, String code, long date, String lotType) {
        if (haoMaList == null || haoMaList.isEmpty()) {
            return;
        }

//        int layoutResId;
//        if (LotteryUtils.isSaiche(code)) { //赛车
//            layoutResId = R.layout.chat_touzhu_number_gridview_item_tittle;
//        } else if (LotteryUtils.isSixMark(code)) { //六合彩
//            layoutResId = R.layout.chat_result_number_gridview_item_lhc;
//            //这个是为了添加一个"+"来占用布局的
//            haoMaList.add(haoMaList.size() - 1, "");
//        } else {
//            layoutResId = R.layout.chat_touzhu_number_gridview_item;
//        }

//        gridView.setNumColumns(haoMaList.size());
//        gridView.setAdapter(new ChatNumbersAdapter(mContext, haoMaList,
//                true ? String.valueOf(ChatSpUtils.instance(mContext).getLotteryVersion_1()) : String.valueOf(ChatSpUtils.instance(mContext).getLotteryVersion_2()),
//                layoutResId, lotType, true, date));



        numbersView.setNumColumns(10);
        numbersView.setAdapter(new ChatBallAdapter(mContext, haoMaList,
                R.layout.chat_number_gridview_item, lotType, code));
        CommonUtils.setListViewHeightBasedOnChildren(numbersView, 10, 10);

    }


}
