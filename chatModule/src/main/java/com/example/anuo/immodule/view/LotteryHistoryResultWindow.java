package com.example.anuo.immodule.view;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.adapter.LAdapter;
import com.example.anuo.immodule.base.LViewHolder;
import com.example.anuo.immodule.jsonmodel.LotteryHistoryResultResponse;
import com.example.anuo.immodule.jsonmodel.LotteryHistoryResultResponse.*;
import com.example.anuo.immodule.lottery.LotteryUtils;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.SysConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import crazy_wrapper.Crazy.Utils.Utils;

public class LotteryHistoryResultWindow extends PopupWindow{
    private static String TAG = LotteryHistoryResultWindow.class.getSimpleName();

    private Context mContext;
    private RecyclerView recyclerView;

    public LotteryHistoryResultWindow(Context context){
        mContext = context;
        View content = View.inflate(context, R.layout.window_lottry_history_result, null);
        this.setContentView(content);
        DisplayMetrics dm = CommonUtils.screenInfo(mContext);
        this.setWidth(dm.widthPixels*8/9);
        this.setHeight(dm.heightPixels/2);
        this.setOutsideTouchable(false);
        this.setFocusable(true);

        recyclerView = (RecyclerView) content.findViewById(R.id.recyclerView);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() { changeWindowAlpha(1f); }
        });
    }

    public void showHistoryResults(LotteryHistoryResultResponse response, String cpBianma, String cpType){
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        HistoryResultAdapter adapter = new HistoryResultAdapter(cpBianma, cpType, response.getContent());
        recyclerView.setAdapter(adapter);
    }

    public void showWindow(View anchor, int offsetY){
        changeWindowAlpha(0.7f);
        showAtLocation(anchor, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, offsetY);
    }

    private void changeWindowAlpha(float alpha){
        if (!(mContext instanceof Activity)){
            throw new IllegalStateException("attach window is not in activity");
        }

        final Activity activity = (Activity) mContext;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = alpha;
        activity.getWindow().setAttributes(params);
    }

    private static class HistoryResultAdapter extends RecyclerView.Adapter<HistoryResultAdapter.VH>{

        private String cpBianma;
        private String cpType;
        private List<OpenResultDetail> results;

        public HistoryResultAdapter(String cpBianma, String cpType, List<OpenResultDetail> results) {
            this.cpBianma = cpBianma;
            this.cpType = cpType;
            this.results = results;
        }

        @Override
        public int getItemCount() { return results.size(); }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            Utils.logd(TAG, "onCreateViewHolder");
            View view = View.inflate(viewGroup.getContext(), R.layout.item_lottery_history_result, null);
            return new VH(view, cpBianma, cpType);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Utils.logd(TAG, "onBindViewHolder");
            holder.bindData(results.get(position));
        }

        private static class VH extends RecyclerView.ViewHolder{

            private TextView qihao;
            private GridView numbersView;
            private TextView emptyView;
            private TextView tvLoading;
            private LinearLayout secondLayout;
            private TextView zhonghe;
            private TextView bigsmall;
            private TextView singledouble;
            private TextView textTime;

            private String cpBianma;
            private String cpType;

            public VH(@NonNull View view, String cpBianma, String cpType) {
                super(view);
                this.cpBianma = cpBianma;
                this.cpType = cpType;

                qihao = view.findViewById(R.id.qihao);
                numbersView = view.findViewById(R.id.numbers);
                emptyView = view.findViewById(R.id.empty_numbers);
                tvLoading = view.findViewById(R.id.tv_loading);
                secondLayout = view.findViewById(R.id.layout2);
                zhonghe = (TextView) secondLayout.findViewById(R.id.zhonghe);
                bigsmall = (TextView) secondLayout.findViewById(R.id.bigsmall);
                singledouble = (TextView) secondLayout.findViewById(R.id.singledouble);
                textTime = view.findViewById(R.id.kaijian_time);
            }

            public void bindData(OpenResultDetail item){
                qihao.setText(String.format("第%s期", item.getQiHao().length() <= 6 ? item.getQiHao() :
                        item.getQiHao().substring(item.getQiHao().length() - 6)));
                List<String> haoMa = item.getHaoMaList();
                String haoma = "";
                if (haoMa != null && !haoMa.isEmpty()) {
                    for (String hm : haoMa) {
                        haoma += hm + ",";
                    }
                    if (haoma.endsWith(",")) {
                        haoma = haoma.substring(0, haoma.length() - 1);
                    }
                    List<BallListItemInfo> numbers = convertNumbers(haoma);
                    if (LotteryUtils.needCalcTotalDSDX(cpBianma)) {
                        List<BallListItemInfo> appendInfos = LotteryUtils.figureOutALLDXDS(itemView.getContext(), numbers, cpBianma);
                        secondLayout.setVisibility(View.VISIBLE);
                        if (cpBianma.equalsIgnoreCase("PCEGG") || cpBianma.equalsIgnoreCase("JND28") ||
                                cpBianma.equalsIgnoreCase("FC3D") || cpBianma.equalsIgnoreCase("PL3")) {
                            String n = haoMa.get(haoMa.size() - 1);
                            if (TextUtils.isEmpty(n) || !LotteryUtils.isNumeric(n)) {
                                zhonghe.setText(String.format("总和:%s", "无"));
                                bigsmall.setText(String.format("大小:%s", "无"));
                                singledouble.setText(String.format("单双:%s", "无"));
                            } else {
                                zhonghe.setText(String.format("总和:%s", n));
                                int total = /*Utils.getMaxOpenNumFromLotCode(cpBianma, numbers);*/27;
                                bigsmall.setText(String.format("大小:%s", Integer.parseInt(n) <= total / 2 ? "小" : "大"));
                                singledouble.setText(String.format("单双:%s", Integer.parseInt(n) % 2 == 0 ? "双" : "单"));
                            }
                        } else {
                            if (appendInfos != null && appendInfos.size() == 3) {
                                if (cpBianma.equals("FF28")||cpBianma.equals("WF28")||cpBianma.equals("SF28")){
                                    //极速28
                                    zhonghe.setVisibility(View.GONE);
                                    bigsmall.setText(String.format("大小:%s", appendInfos.get(1).getNum()));
                                    try {
                                        singledouble.setText(String.format("单双:%s", Integer.parseInt(numbers.get(6).getNum()) %2==0?"双":"单" ));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    zhonghe.setVisibility(View.VISIBLE);
                                    zhonghe.setText(String.format("总和:%s", appendInfos.get(0).getNum()));
                                    bigsmall.setText(String.format("大小:%s", appendInfos.get(1).getNum()));
                                    singledouble.setText(String.format("单双:%s", appendInfos.get(2).getNum()));
                                }
                            }
                        }
                    } else {
                        secondLayout.setVisibility(View.GONE);
                    }


                    if (LotteryUtils.isKuaiSan(cpBianma)) {
                        SysConfig config = CommonUtils.getConfigFromJson(itemView.getContext());
                        boolean isOff = "off".equals(config.getK3_baozi_daXiaoDanShuang());
                        //如果开关关闭 就显示豹子
                        if (isOff && numbers != null && numbers.size() >= 5) {
                            if (numbers.get(0).getNum().equals(numbers.get(1).getNum()) &&
                                    numbers.get(0).getNum().equals(numbers.get(2).getNum())) {
                                numbers.get(3).setNum("豹");
                                numbers.get(4).setNum("豹");
                            }
                        }
                    }

                    String numbersStr = "";
                    for (BallListItemInfo hm : numbers) {
                        numbersStr += hm.getNum() + ",";
                    }
                    if (numbersStr.endsWith(",")) {
                        numbersStr = numbersStr.substring(0, numbersStr.length() - 1);
                    }

                    if (!TextUtils.isEmpty(numbersStr)) {
                        if (numbersStr.contains(",")) {
                            emptyView.setVisibility(View.GONE);
                            numbersView.setVisibility(View.VISIBLE);
                            long date = 0;
                            if (!TextUtils.isEmpty(item.getDate())) {
                                date = LotteryUtils.date2TimeStamp(item.getDate(), "yyyy-MM-dd");
                            }

                            List<String> data = new ArrayList<>(Arrays.asList(numbersStr.split(",")));
                            NumbersAdapter adapter = new NumbersAdapter(itemView.getContext(),
                                    data, R.layout.item_lottery_history_result_detail,
                                    String.valueOf(cpType), cpBianma, date);
                            adapter.setShowShenxiao(true);

                            numbersView.setAdapter(adapter);
                            LotteryUtils.setListViewHeightBasedOnChildren(numbersView, 10, 0);
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                            numbersView.setVisibility(View.GONE);
                            emptyView.setText(numbersStr);
                        }
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        numbersView.setVisibility(View.GONE);
                        emptyView.setText("等待开奖");
                    }
                }

                String date = !TextUtils.isEmpty(item.getDate()) ? item.getDate() : "";
                String time = !TextUtils.isEmpty(item.getTime()) ? item.getTime() : "";
                textTime.setText(String.format("%s %s", date, time));
            }

            private List<BallListItemInfo> convertNumbers(String numbers) {
                if (TextUtils.isEmpty(numbers)) {
                    return null;
                }

                String[] nums = numbers.split(",");
                List<BallListItemInfo> ballons = new ArrayList<BallListItemInfo>();
                for (String number : nums) {
                    BallListItemInfo info = new BallListItemInfo();
                    info.setNum(number);
                    ballons.add(info);
                }
                return ballons;
            }
        }
    }

    public static class BallListItemInfo {

        String num;
        String postNum;
        boolean isSelected;
        boolean clickOn;//是否点击了

        public boolean isClickOn() {
            return clickOn;
        }

        public void setClickOn(boolean clickOn) {
            this.clickOn = clickOn;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPostNum() {
            return postNum;
        }

        public void setPostNum(String postNum) {
            this.postNum = postNum;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    private static class NumbersAdapter extends LAdapter<String> {
        Context context;
        String codeType;
        String cpBianma;
        List<String> mDatas;
        String cpVersion;
        long resultTime;//开奖时间
        String dateTime;
        boolean showShenxiao;

        public NumbersAdapter(Context mContext, List<String> mDatas, int layoutId, String codeType, String cpBianma) {
            this(mContext, mDatas, layoutId, codeType, cpBianma, 0);
        }

        public NumbersAdapter(Context mContext, List<String> mDatas, int layoutId, String codeType, String cpBianma, long time) {
            super(mContext, mDatas, layoutId);

            context = mContext;
            this.codeType = codeType;
            this.mDatas = mDatas;
            this.cpBianma = cpBianma;
            this.resultTime = time;
            cpVersion = ChatSpUtils.instance(context).getGameVersion();
            if (codeType.equals("6") || codeType.equals("66")) {
                if (mDatas != null && mDatas.size() != 0) {
                    String str = mDatas.get(mDatas.size() - 1);
                    mDatas.add(mDatas.size()-1, "=");
                }
            }
        }

        public void setShowShenxiao(boolean showShenxiao) {
            this.showShenxiao = showShenxiao;
        }

        public void setDate(String dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final String item) {
            TextView ball = holder.getView(R.id.ball);
            TextView summary = holder.getView(R.id.summary);
            if (item.equals("=")) {
                ball.setText(item);
                ball.setBackground(null);
                return;
            }

            CommonUtils.figureImgeByCpCode(codeType, item, cpVersion, ball, position);
            if (LotteryUtils.isSixMark(cpBianma) && showShenxiao) {
                if (summary != null) {
                    summary.setVisibility(View.VISIBLE);

                    long newDate = 0;
                    if (!TextUtils.isEmpty(dateTime)) {
                        newDate = CommonUtils.date2TimeStamp(dateTime, CommonUtils.DATE_FORMAT);
                    }
                    String s;
                    if (resultTime != 0) {
                        s = CommonUtils.getNumbersFromShengXiaoName(context, item, resultTime);
                    } else {
                        s = CommonUtils.getNumbersFromShengXiaoName(context, item, newDate);
                    }
                    summary.setText(!TextUtils.isEmpty(s) ? s : "无");
                }
            } else {
                if (summary != null) {
                    summary.setVisibility(View.GONE);
                }
            }
        }
    }
}
