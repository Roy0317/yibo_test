package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.LayoutRes;
import com.google.android.material.tabs.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.TouzhuSimpleActivity;
import com.yibo.yiboapp.adapter.SimplePopwindowAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.SimplePageBean;
import com.yibo.yiboapp.interfaces.CurrentLotteryDataChangeListener;
import com.yibo.yiboapp.interfaces.SimpleClearListener;
import com.yibo.yiboapp.interfaces.SixMarkPeilvListener;
import com.yibo.yiboapp.utils.WindowUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ray
 * created on 2018/12/6
 * description :简单的彩种下拉选择框(PopupWindow：不能嵌套Fragment+ViewPager会造成找不到view的问题)
 */
public class SimpleCaizhongWindow extends PopupWindow implements PopupWindow.OnDismissListener {


    private List<LotteryData> datas;//数据

    private Context context;

    private Activity activity;

    @LayoutRes
    private int simpleLayout = -1;

    private int currentPos = 0;

    private int lastPos = -1;

    private LotteryData currentLotteryData;

    private SixMarkPeilvListener sixMarkPeilvListener;

    private SimpleClearListener simpleClearListener;

    private CurrentLotteryDataChangeListener currentLotteryDataChangeListener;

    private RelativeLayout anchorView;


    public SimpleCaizhongWindow(ViewGroup viewGroup, Context context, Activity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.anchorView = (RelativeLayout) viewGroup;
        this.setBackgroundDrawable(null);//解决四周带阴影的问题
        this.setOutsideTouchable(true);//点击外部消失
        this.setFocusable(true);//必须写,不写后果自负，解决点击重复弹出
        this.setWidth(viewGroup.getWidth());
        this.setOnDismissListener(this);
    }

    public void setCurrentLotteryData(LotteryData currentLotteryData) {
        this.currentLotteryData = currentLotteryData;
    }

    public void initView(final Context context) {


        final List<SimplePageBean> simplePageList = new ArrayList<>();

        if (simpleLayout == -1) {
            simpleLayout = R.layout.popup_window_title_down;
        }

        View view = View.inflate(context, simpleLayout, null);
        setContentView(view);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_click_fold_up);

        String[] lotteryType = Constant.DEFAULT_LOTTERY_TYPE.split(",");

        //点击收起
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    SimpleCaizhongWindow.this.dismiss();
                }
            }
        });


        Type listType = new TypeToken<ArrayList<LotteryData>>() {
        }.getType();
        List<LotteryData> datas = new Gson().fromJson(YiboPreference.instance(context).getLotterys(), listType);


        ArrayList<LotteryData> newData = new ArrayList<>();

        for (LotteryData data : datas) {
            if (data.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
                newData.add(data);
            }
        }

        //时时彩、赛车、十一选五、快三、六合彩、福彩3D、pc蛋蛋、快乐十分、十分六合彩
        ArrayList<LotteryData> arrayList1 = new ArrayList<>();
        ArrayList<LotteryData> arrayList2 = new ArrayList<>();
        ArrayList<LotteryData> arrayList3 = new ArrayList<>();
        ArrayList<LotteryData> arrayList4 = new ArrayList<>();
        ArrayList<LotteryData> arrayList5 = new ArrayList<>();
        ArrayList<LotteryData> arrayList6 = new ArrayList<>();
        ArrayList<LotteryData> arrayList7 = new ArrayList<>();
        ArrayList<LotteryData> arrayList8 = new ArrayList<>();
//        ArrayList<LotteryData> arrayList9 = new ArrayList<>();

        for (LotteryData lotteryData : newData) {
            switch (lotteryData.getCzCode()) {
                case "9":
                case "1":
                case "2":
                case "51":
                case "52":
                    //时时彩
                    arrayList1.add(lotteryData);
                    break;
                case "10":
                case "100":
                case "58":
                    //快三
                    arrayList4.add(lotteryData);
                    break;
                case "11":
                case "7":
                case "57":
                    //PC蛋蛋
                    arrayList7.add(lotteryData);
                    break;
                case "12":
                    //快乐十分
                    arrayList8.add(lotteryData);
                    break;
                case "8":
                case "3":
                case "53":
                    //赛车
                    arrayList2.add(lotteryData);
                    break;
                case "14":
                case "5":
                case "55":
                    //十一选五
                    arrayList3.add(lotteryData);
                    break;
                case "15":
                case "4":
                case "54":
                    //福彩三D
                    arrayList6.add(lotteryData);
                    break;
                case "6":
                case "66":
                    //十分六合彩
                    //六合彩
                    arrayList5.add(lotteryData);
                    break;
            }
        }


        if (arrayList1.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.SSC, arrayList1));
        }
        if (arrayList2.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.SC, arrayList2));
        }
        if (arrayList3.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.SYXW, arrayList3));
        }
        if (arrayList4.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.KS, arrayList4));
        }
        if (arrayList5.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.LHC, arrayList5));
        }
        if (arrayList6.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.FCSD, arrayList6));
        }
        if (arrayList7.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.PCDD, arrayList7));
        }
        if (arrayList8.size() > 0) {
            simplePageList.add(new SimplePageBean(Constant.KLSF, arrayList8));
        }
//        if (arrayList9.size() > 0) {
//            simplePageList.add(new SimplePageBean(Constant.SFLHC, arrayList9));
//        }


        boolean isAdded = false;
        boolean isLoop = true;

        for (int i = 0; i < simplePageList.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(simplePageList.get(i).getPageName());

            if (isLoop) {
                for (LotteryData lotteryData : simplePageList.get(i).getLotteryData()) {
                    if (lotteryData.getName().equals(currentLotteryData.getName())) {
                        lotteryData.setSelect(true);
                        tabLayout.addTab(tab, true);
                        isAdded = true;
                        isLoop = false;
                        currentPos = i;
                        break;
                    }
                }
            }

            if (!isAdded) {
                tabLayout.addTab(tab);
            }
            isAdded = false;
        }


        GridView gridView = (GridView) view.findViewById(R.id.simple_pop_window_gridView);
        final SimplePopwindowAdapter adapter = new SimplePopwindowAdapter(context, simplePageList.get(currentPos).getLotteryData());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<LotteryData> lotteryData = simplePageList.get(currentPos).getLotteryData();
                if (lastPos != currentPos) {
                    //全部重置
                    for (SimplePageBean simplePageBean : simplePageList) {
                        for (LotteryData lotteryData1 : simplePageBean.getLotteryData()) {
                            lotteryData1.setSelect(false);
                        }
                    }
                    lotteryData.get(position).setSelect(true);
                    adapter.setList(lotteryData);
                } else {
                    //只重置当前页面
                    for (LotteryData lotteryDatum : lotteryData) {
                        lotteryDatum.setSelect(false);
                    }
                    lotteryData.get(position).setSelect(true);
                    adapter.setList(lotteryData);
                }

                LotteryData data = simplePageList.get(currentPos).getLotteryData().get(position);

                if (currentLotteryDataChangeListener != null) {
                    currentLotteryDataChangeListener.onCurrentLotteryChangeListener(data);
                }
                if (data == null) {
                    return;
                }

                if (UsualMethod.isSixMark(context,data.getCode())) {
                    if (sixMarkPeilvListener != null) {
                        sixMarkPeilvListener.onLhcSelectAndisPeilvVersionChangeListener(true, true);
                    }
                } else {
                    if (sixMarkPeilvListener != null) {
                        sixMarkPeilvListener.onLhcSelectAndisPeilvVersionChangeListener(false,
                                UsualMethod.isPeilvVersionMethod(context));
                    }
                }

                if (simpleClearListener != null) {
                    simpleClearListener.onClearAndUpdateListener();
                }

                //重新选择彩种后，需要重新获取彩种对应的玩法
                ((TouzhuSimpleActivity) activity).refreshLotteryPlayRules(data.getCode(), true);
                dismiss();
            }
        });

        //时时彩、赛车、十一选五、快三、六合彩(包含十分六合彩)、福彩3D、pc蛋蛋、快乐十分
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                lastPos = currentPos;
                currentPos = tab.getPosition();
                adapter.setList(simplePageList.get(currentPos).getLotteryData());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        recomputeTlOffset1(currentPos, tabLayout, lotteryType);


    }


    public void setDatas(List<LotteryData> datas) {
        this.datas = datas;
    }

    public List<LotteryData> getDatas() {
        return datas;
    }


    @Override
    public void onDismiss() {
        WindowUtils.setWindowAttributes((Activity) context, 1.0f);
    }


    /**
     * 重新计算需要滚动的距离
     *
     * @param index 选择的tab的下标
     */
    private void recomputeTlOffset1(int index, final TabLayout tabLayout, String[] lotteryType) {
//        if (tabLayout.getTabAt(index) != null) tabLayout.getTabAt(index).select();
        final int width = (int) (getTabLayoutOffsetWidth(index, lotteryType) * context.getResources().getDisplayMetrics().density);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.smoothScrollTo(width, 0);
            }
        });
    }


    /**
     * 根据字符个数计算偏移量
     *
     * @param index 选中tab的下标
     * @return 需要移动的长度
     */
    private int getTabLayoutOffsetWidth(int index, String[] channelNameList) {
        String str = "";
        for (int i = 0; i < index; i++) {
            //channelNameList是一个List<String>的对象
            //取出直到index的tab的文字，计算长度
            str += channelNameList[i];
        }
        return str.length() * 14 + index * 12;
    }

    public void setSimpleClearListener(SimpleClearListener simpleClearListener) {
        this.simpleClearListener = simpleClearListener;
    }

    public void setSixMarkPeilvListener(SixMarkPeilvListener sixMarkPeilvListener) {
        this.sixMarkPeilvListener = sixMarkPeilvListener;
    }

    public void setCurrentLotteryDataChangeListener(CurrentLotteryDataChangeListener currentLotteryDataChangeListener) {
        this.currentLotteryDataChangeListener = currentLotteryDataChangeListener;
    }
}
