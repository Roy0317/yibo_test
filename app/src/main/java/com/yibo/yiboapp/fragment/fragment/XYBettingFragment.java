package com.yibo.yiboapp.fragment.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.fragment.base.ChatBaseFragment;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryAlgorithm;
import com.yibo.yiboapp.data.PeilvData;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.data.PeilvZhushuCalculator;
import com.yibo.yiboapp.data.PlayCodeConstants;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.CalcPeilvOrder;
import com.yibo.yiboapp.entify.PeilvWebResult;
import com.yibo.yiboapp.interfaces.NormalTouzhuListener;
import com.yibo.yiboapp.interfaces.PeilvListener;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.PeilvSimpleTableContainer;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.ThreadUtils;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.utils.ViewCache;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * Author:Ray
 * Date  :2019年11月01日14:45:48¬
 * Desc  :com.yibo.yiboapp.ui.bet.fragment ----- 对应 PeilvSimpleFragment
 */
public class XYBettingFragment extends ChatBaseFragment {


    private static final String TAG = "XYBettingFragment";

    public static final int CALC_ZHUSHU_MONEY = 0x1;
    public static final int CLEAR_DATA = 0x2;
    public static final int POST_DATA = 0x3;
    public static final int UPDATE_LISTVIEW = 0x4;
    public static final int LOADING_DATA = 0x5;

    XListView playListview;
    EmptyListView emptyListView;
    EmptyListView emptyListView1;
    List<PeilvWebResult> webResults = new ArrayList<>();
    String selectPlayCode = "";
    String selectRuleCode = "";
    String selectPlayName = "";
    String selectSubPlayName = "";
    String currentQihao = "";
    String cpName = "";
    List<PeilvData> listDatas = new ArrayList<>();

    PeilvAdapter playAdapter;


    //彩种相关信息
    String cpCode = "";//彩票代号
    String cpTypeCode = "";//彩票类型代号xlistview_without_animation
    String cpVersion = String.valueOf(Constant.lottery_identify_V1);//彩票版本号
    DecimalFormat decimalFormat = new DecimalFormat("0");
    DecimalFormat pldecimalFormat = new DecimalFormat("0.000");

    int pageIndexWhenLargePeilvs = 1;//很多赔率项时的分页页码
    int pageSize = 10;

    String cpBianHao;//彩票编号
    long lhcServerTime = 0;

    private boolean isFengPan = false;

    int selectedListPos = -1;
    int selectedGridPos = -1;

    int calcOutZhushu;//计算出来的注数
    float calcPeilv;//计算出来的赔率

    int buttonAudioID;
    SoundPool buttonPool;
    MyHandler myHandler;
    private static List<PeilvPlayData> selectNumList = Collections.synchronizedList(new ArrayList<PeilvPlayData>());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewCache.getInstance().setType(1);
        ViewCache.getInstance().push(getResources().getConfiguration(), getActivity());
        View view = inflater.inflate(R.layout.fragment_xy_betting, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        myHandler = new MyHandler(this);
        playListview = view.findViewById(R.id.listview);
        playListview.setPullLoadEnable(false);
        playListview.setPullRefreshEnable(false);
        //下拉刷新上拉加载的操作
        playListview.setXListViewListener(new ListviewListener());
        playListview.setDivider(getResources().getDrawable(R.color.driver_line_color));
        playListview.setDividerHeight(1);
        playListview.setVisibility(View.VISIBLE);
        playListview.setFastScrollEnabled(true);

        emptyListView = view.findViewById(R.id.empty);
        emptyListView.setShowText(getActivity().getString(R.string.temp_not_support));
//        emptyListView.setListener(new PeilvSimpleFragment.EmptyViewClickListener());

        emptyListView1 = view.findViewById(R.id.empty1);
        emptyListView1.setShowText("没有赔率数据,请联系客服");
        TextView textView = emptyListView1.findViewById(R.id.click_refresh);
        textView.setVisibility(View.GONE);

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }

        ((BettingMainFragment) getParentFragment()).onBottomUpdate(null, 0);

        playListview.setVerticalScrollBarEnabled(false);
        playListview.setVerticalFadingEdgeEnabled(false);
        playListview.setFastScrollEnabled(false);
        playAdapter = new PeilvAdapter(getActivity(), listDatas);
        //普通投注情况下，在每个号码项中输入一个金额就通知更新一次底部注数和总金额
        playAdapter.setNormalTouzhuListener((pos, cellIndex, withSound) -> {
            if (isFengPan) {
                ToastUtils.showShort(R.string.result_not_open_now);
                return;
            }
            //若点击列表项的时候，快捷金额输入框中有金额，则将金额填入对应赔率PeilvPlayData
            if (pos >= 0 && pos < listDatas.size()) {
                PeilvData datas = listDatas.get(pos);
                List<PeilvPlayData> subDatas = datas.getSubData();
                if (datas != null && !subDatas.isEmpty()) {
                    PeilvPlayData subData = subDatas.get(cellIndex);

                    if (withSound) {
                        //播放按键音
                        if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
                            if (buttonPool == null || buttonAudioID == 0) {
                                initKeySound();
                            }
                            buttonPool.play(buttonAudioID, 1, 1, 0, 0, 1);
                        }
                    }
                }
            }
            //输入法点确定后，将选择的号码，金额发送到主界面更新底部栏显示出来
            TouzhuThreadPool.getInstance().addTask(new CalcZhushuAndMoney(XYBettingFragment.this, listDatas));
        });
        playListview.setAdapter(playAdapter);
    }


    @Override
    protected void loadData() {
        super.loadData();

    }

    @Override
    protected void initListener() {
        super.initListener();

    }


    /**
     * 列表下拉，上拉监听器
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
//            actionAcquireData(false);
        }

        public void onLoadMore() {
            getPagePeilvDatas();
        }
    }

    //获取第几页的赔率数据
    private void getPagePeilvDatas() {
        updatePlayArea(webResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public void updatePlayArea() {
        emptyListView1.setVisibility(View.VISIBLE);
        playListview.setVisibility(View.GONE);
    }


    //异步计算总注数和总金额
    private final class CalcZhushuAndMoney implements Runnable {

        List<PeilvData> datas;
        WeakReference<XYBettingFragment> weak;
        List<PeilvPlayData> selectedDatas;

        CalcZhushuAndMoney(XYBettingFragment context, List<PeilvData> datas) {
            this.datas = datas;
            weak = new WeakReference<>(context);
            selectedDatas = new ArrayList<>();
        }

        @Override
        public void run() {

            if (weak == null) {
                return;
            }
            if (datas == null) {
                return;
            }

            for (PeilvData data : datas) {
                List<PeilvPlayData> subs = data.getSubData();
                for (PeilvPlayData ppd : subs) {
                    if (isSelectedNumber(ppd)) {
                        if (!ppd.isFilterSpecialSuffix()) {
                            ppd.setAppendTag(data.isAppendTag());
                            ppd.setItemName(!Utils.isEmptyString(data.getPostTagName()) && !Utils.isEmptyString(data.getPostTagName())
                                    ? data.getPostTagName() : data.getTagName());
                        } else {
                            ppd.setAppendTag(false);
                        }
                        selectedDatas.add(ppd);
                    }
                }
            }
            calcZhushuAndSendMessage(selectedDatas);
        }

        private void calcZhushuAndSendMessage(List<PeilvPlayData> selectDatas) {
            if (selectDatas.isEmpty()) {
                sendMessage(0, 0, 0, isMulSelectMode(selectPlayCode), selectDatas);
                return;
            }
            PeilvPlayData pData = selectDatas.get(0);
            if (pData == null) {
                sendMessage(0, 0, 0, isMulSelectMode(selectPlayCode), selectDatas);
                return;
            }
            int totalZhushu = 0;
            double totalMoney = 0;
            float peilvWhenMultiSelect = 0;
            boolean isMultiSelect = pData.isCheckbox();
            //若是多选情况下，则将多个号码拼接起来,以逗号分隔
            if (isMultiSelect) {
                int minSelectCount = pData.getPeilvData() != null ? pData.getPeilvData().getMinSelected() : 0;
                if (minSelectCount > selectDatas.size()) {
                    Utils.LOG("aa", "选择的号码小于" + pData.getPeilvData().getMinSelected() + ",不必计算注数");
                    sendMessage(0, 0, 0, isMultiSelect, selectDatas);
                    return;
                }

                StringBuilder numbers = new StringBuilder();
                for (int i = 0; i < selectDatas.size(); i++) {
                    PeilvPlayData playData = selectDatas.get(i);
                    numbers.append(playData.getNumber().trim());
                    if (i != selectDatas.size() - 1) {
                        numbers.append(",");
                    }
                }
                PeilvWebResult webResult = UsualMethod.getPeilvData(getContext(),cpBianHao, selectPlayCode, selectDatas.size(), pData);
                if (webResult == null) {
                    ToastUtils.showShort("所选号码请勿大于8位");
                    return;
                }
                Integer zhushu = PeilvZhushuCalculator.buyZhuShuValidate(numbers.toString(),
                        webResult.getMinSelected(), webResult.getPlayCode());
//                Utils.LOG(TAG, "zhushu ==== " + zhushu);
                totalZhushu += zhushu;
//                String moneyValue = moneyTV.getText().toString().trim();
//                if (!Utils.isEmptyString(moneyValue)) {
//                    totalMoney = Float.parseFloat(moneyValue);
//                }
                peilvWhenMultiSelect = webResult.getOdds();
                List<PeilvWebResult> list = new ArrayList<>();
                list.add(webResult);
                if (!UsualMethod.dontShowPeilvWhenTouzhu(selectPlayCode)) {
                    updatePeilvTxt(list, totalZhushu, selectPlayCode);
                }
                totalMoney = 0;//多选时总金额是主线程金额输入框中的金额*注数，在主线程计算
            } else {
                for (PeilvPlayData data : selectDatas) {
                    Integer zhushu = PeilvZhushuCalculator.buyZhuShuValidate(data.getNumber(),
                            data.getPeilvData().getMinSelected(), data.getPeilvData().getPlayCode());
                    Utils.LOG(TAG, "zhushu ==== " + zhushu);
                    totalZhushu += zhushu;
                    totalMoney += data.getMoney();
                }
            }
            Utils.LOG("a", "the total money = " + totalMoney);
            sendMessage(totalMoney, peilvWhenMultiSelect, totalZhushu, isMultiSelect, selectDatas);
        }

        /**
         * 发送数据到主线程
         *
         * @param totalMoney    单注总金额
         * @param peilv
         * @param totalZhushu
         * @param isMultiSelect
         */
        private void sendMessage(double totalMoney, float peilv, int totalZhushu, boolean isMultiSelect, List<PeilvPlayData> list) {
            CalcPeilvOrder peilvOrder = new CalcPeilvOrder();
            peilvOrder.setTotalMoney(totalMoney);
            peilvOrder.setPeilvWhenMultiselect(peilv);
            peilvOrder.setZhushu(totalZhushu);
            peilvOrder.setMultiSelect(isMultiSelect);
            peilvOrder.setSelectDatas(list);

            Message message = myHandler.obtainMessage(CALC_ZHUSHU_MONEY);
            message.obj = peilvOrder;
            message.sendToTarget();

        }

    }




    public void onPlayRuleSelected(String cpVersion, String cpCode, String playCode,
                                   String subPlayCode, String playName, String subPlayName,
                                   float maxBounds, float maxRate, String currentQihao,
                                   String cpName, long cpDuration, int maxBetNum) {

        pageIndexWhenLargePeilvs = 1;
//        moneyTV.setText("");//切换玩法时，先将金额输入框置空

        //更新玩法代码等变量
        this.cpVersion = cpVersion;
        this.cpCode = cpCode;
        this.selectPlayCode = playCode;
        this.selectRuleCode = subPlayCode;
        this.selectPlayName = playName;
        this.selectSubPlayName = subPlayName;
        this.currentQihao = currentQihao;
        this.cpName = cpName;


//        playRuleTV.setText(subPlayName);
        selectNumList.clear();
        //如果是多选投注号码的情况下，切换顶部投注金额面板，去除正常的号码选择提示。
        if (isMulSelectMode(selectPlayCode)) {
//                        okBtn.setClickable(false);
        } else {
//            peilvBtn.setClickable(false);
//            peilvBtn.setVisibility(View.GONE);
        }
        //异步获取玩法对应的赔率信息
        if (peilvListener != null) {
            peilvListener.onPeilvAcquire(subPlayCode, true);
        }
    }


    /**
     * 获取到后台赔率数据后更新玩法号码面板，更新赔率及号码
     *
     * @param results
     */
    public void updatePlayArea(final List<PeilvWebResult> results) {
        emptyListView1.setVisibility(View.GONE);
        playListview.setVisibility(View.VISIBLE);
        webResults = results;
        if (isBigDataPlayCode(selectPlayCode)) {
            playListview.setPullLoadEnable(true);
        } else {
            playListview.setPullLoadEnable(false);
        }
        if (results == null || results.isEmpty()) {
            //加载赔率数据失败后，显示空占位VIEW
            listDatas.clear();
            playListview.setEmptyView(emptyListView);
            return;
        }
        if (Utils.isEmptyString(cpVersion) || Utils.isEmptyString(cpCode) ||
                Utils.isEmptyString(selectPlayCode) ||
                Utils.isEmptyString(selectRuleCode)) {
            return;
        }

        //获取到赔率时初始化赔率显示项
        updatePeilvTxt(results, 0, selectPlayCode);

        //若是号码量超大的玩法，且页码超过大于1时，视为追加数据
        boolean appendData = isBigDataPlayCode(selectPlayCode);
        List<PeilvData> peilvData = figurePlayDatas(cpVersion, cpCode, selectPlayCode,
                selectRuleCode, pageIndexWhenLargePeilvs, pageSize, appendData, webResults);
        if (peilvData == null) {
            listDatas.clear();
            playAdapter.notifyDataSetChanged();
            playListview.setVisibility(View.VISIBLE);
            return;
        }
        Utils.LOG(TAG, "dada = " + listDatas.size());
        refreshPaneAndClean(false);
        if (appendData) {
            pageIndexWhenLargePeilvs++;
            if (playListview.isPullLoading()) {
                playListview.stopLoadMore();
            }
        }

    }


    private void updatePeilvTxt(final List<PeilvWebResult> presults, final int zhushu, String playCode) {
        if (presults.isEmpty()) {
//            peilvBtn.setVisibility(View.GONE);
            return;
        }
        if (isMulSelectMode(playCode)) {
            if (UsualMethod.isSixMark(getContext(),cpBianHao)) {
                if (!presults.isEmpty()) {
                    //取出最大和最小赔率,从DA到XIAO
                    Collections.sort(presults, new Comparator<PeilvWebResult>() {
                        @Override
                        public int compare(PeilvWebResult oldData, PeilvWebResult newData) {
                            return Float.compare(oldData.getOdds(), newData.getOdds());
                        }
                    });
                    String smallPeilv = String.valueOf(presults.get(0).getOdds());
                    String maxPeilv = String.valueOf(presults.get(presults.size() - 1).getOdds());
                    final String peilvs = String.format("赔率: %s/%s", maxPeilv, smallPeilv);
//                    peilvBtn.setVisibility(View.VISIBLE);
                    ThreadUtils.doOnUIThread(new ThreadUtils.UITask() {
                        @Override
                        public void doOnUI() {
//                            peilvBtn.setText(peilvs);
                        }
                    });
                } else {
                    if (zhushu > 0) {
                        final String peilv = String.format("赔率: %.3f", presults.get(0).getOdds());
                        ThreadUtils.doOnUIThread(new ThreadUtils.UITask() {
                            @Override
                            public void doOnUI() {
//                                peilvBtn.setText(peilv);
                            }
                        });
                    } else {
//                        peilvBtn.setVisibility(View.GONE);
                    }
                }
            } else {
//                peilvBtn.setVisibility(View.VISIBLE);
                ThreadUtils.doOnUIThread(new ThreadUtils.UITask() {
                    @Override
                    public void doOnUI() {
                        if (zhushu > 0) {
//                            peilvBtn.setText(String.format("赔率:%.3f", presults.get(0).getOdds()));
                        } else {
//                            peilvBtn.setText("请选择号码");
                        }
                    }
                });

            }
        }


    }


    private boolean isMulSelectMode(String playCode) {
        if (playCode.equals(PlayCodeConstants.zuxuan_san_peilv) ||
                playCode.equals(PlayCodeConstants.zuxuan_liu_peilv) ||
                playCode.equals(PlayCodeConstants.lianma_peilv_klsf) ||
                playCode.equals(PlayCodeConstants.lianma) ||
                playCode.equals(PlayCodeConstants.hexiao) ||
                playCode.equals(PlayCodeConstants.quanbuzhong) ||
                playCode.equals(PlayCodeConstants.weishulian) ||
                playCode.equals(PlayCodeConstants.syx5_renxuan) ||
                playCode.equals(PlayCodeConstants.lianxiao) ||
                playCode.equals(PlayCodeConstants.syx5_zuxuan) ||
                playCode.equals(PlayCodeConstants.syx5_zhixuan)
        ) {
            return true;
        }
        return false;
    }


    public void setLhcServerTime(long lhcServerTime) {
        this.lhcServerTime = lhcServerTime;
    }


    PeilvListener peilvListener;

    public void setPeilvListener(PeilvListener peilvListener) {
        this.peilvListener = peilvListener;
    }


    private void refreshPaneAndClean(boolean noClearView) {
        if (playAdapter != null) {
            playAdapter.notifyDataSetChanged();
            if (!noClearView) {
                if (getActivity() != null) ;
//                ((TouzhuSimpleActivity) getActivity()).onClearView();
            }
        }
    }

    //是否大量数据的玩法
    private boolean isBigDataPlayCode(String code) {
        if (code.equalsIgnoreCase(PlayCodeConstants.sanzidingwei) ||
                code.equalsIgnoreCase(PlayCodeConstants.erzidingwei) ||
                code.equalsIgnoreCase(PlayCodeConstants.pl3_erzizuhe) ||
                code.equalsIgnoreCase(PlayCodeConstants.pl3_sanzizuhe)) {
            return true;
        }
        return false;
    }


    /**
     * 计算出对应玩法下的号码及赔率面板区域显示数据
     *
     * @param cpVersion       彩票版本
     * @param cpCode          彩票类型代号
     * @param playCode        大玩法代号
     * @param subPlayCode     小玩法代号
     * @param pageIndex       页码
     * @param pageSize        每页大小
     * @param appendData      是否将计算出来的数据追加到原有数据后面
     * @param peilvWebResults 后台获取到的玩法对应的赔率数据
     */
    private List<PeilvData> figurePlayDatas(String cpVersion, String cpCode,
                                            String playCode, String subPlayCode,
                                            int pageIndex, int pageSize, boolean appendData,
                                            List<PeilvWebResult> peilvWebResults) {

        if (peilvWebResults == null) {
            return null;
        }
        List<PeilvData> peilvDatas = LotteryAlgorithm.figurePeilvOutPlayInfo(getActivity(),
                cpVersion, cpCode, playCode, subPlayCode, pageIndex, pageSize, peilvWebResults, appendData, isXGLHCServerTime(this.cpBianHao));
        if (peilvDatas == null) {
            return null;
        }
        if (!appendData) {
            listDatas.clear();

            //简约版本，去除赔率为空的项
            Iterator<PeilvData> iterator = peilvDatas.iterator();
            while (iterator.hasNext()) {
                PeilvData next = iterator.next();
                if (next.getSubData().isEmpty()) {
                    iterator.remove();
                }
            }

            listDatas.addAll(peilvDatas);
        } else {
            if (pageIndexWhenLargePeilvs > 1) {
                for (int i = 0; i < peilvDatas.size() && listDatas.size() == peilvDatas.size(); i++) {
                    PeilvData datas = peilvDatas.get(i);
                    if (datas != null) {
                        List<PeilvPlayData> subData = datas.getSubData();
                        if (subData != null) {
                            listDatas.get(i).getSubData().addAll(subData);
                        }
                    }
                }
            } else {
                listDatas.clear();
                listDatas.addAll(peilvDatas);
            }

        }
        return listDatas;
    }


    public class PeilvAdapter extends BaseAdapter {

        Context context;
        List<PeilvData> mDatas;
        LayoutInflater mLayoutInflater;
        NormalTouzhuListener normalTouzhuListener;

        public PeilvAdapter(Context mContext, List<PeilvData> mDatas) {
            context = mContext;
            this.mDatas = mDatas;
            mLayoutInflater = LayoutInflater.from(mContext);
            ViewCache.getInstance().setType(1);
            ViewCache.getInstance().push(getResources().getConfiguration(), context);

        }

        public void setNormalTouzhuListener(NormalTouzhuListener normalTouzhuListener) {
            this.normalTouzhuListener = normalTouzhuListener;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            Log.e("getView方法走了", "-------");
            PeilvViewHolder peilvViewHolder;
            if (convertView == null) {
                peilvViewHolder = new PeilvViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.chat_peilv_simple_gridview, parent, false);
                peilvViewHolder.tagTV = (TextView) convertView.findViewById(R.id.tag);
                peilvViewHolder.tableView = (PeilvSimpleTableContainer) convertView.findViewById(R.id.tables);
                peilvViewHolder.tableView.setNormalTouzhuListener(normalTouzhuListener);
                peilvViewHolder.tableView.setContainerType(PeilvSimpleTableContainer.CHAT_TYPE);//设置为聊天室类型
                convertView.setTag(peilvViewHolder);

            } else {
                peilvViewHolder = (PeilvViewHolder) convertView.getTag();
            }


            setDatas(peilvViewHolder, position);
            return convertView;
        }


        /**
         * @param holder
         * @param position
         */
        private void setDatas(PeilvViewHolder holder, int position) {
            setDatas(holder, position, -1, -1);
        }

        @TargetApi(Build.VERSION_CODES.M)
        private void setDatas(PeilvViewHolder holder, final int position, int selectGridPos, final float moneyValue) {
            PeilvData item = mDatas.get(position);
            if (Utils.isEmptyString(item.getTagName())) {
                holder.tagTV.setVisibility(View.GONE);
            } else {
                holder.tagTV.setVisibility(View.VISIBLE);
                holder.tagTV.setText(item.getTagName());
            }

            PeilvSimpleTableContainer tableView = holder.tableView;
            tableView.setFengpan(isFengPan);
            tableView.setCpCode(cpCode);
            tableView.setTablePosition(position);


            //selectGridPos始终等于-1

            tableView.fillTables(item.getSubData(), getActivity(), mDatas);

            tableView.setTableCellListener(new PeilvSimpleTableContainer.TableCellListener() {
                @Override
                public void onCellSelect(PeilvPlayData data, int cellIndex) {
                    onItemClick(data, position, cellIndex);
                }
            });
        }

    }

    private void onItemClick(final PeilvPlayData item, final int listPos, final int gridPos) {

        this.selectedListPos = listPos;
        this.selectedGridPos = gridPos;
        if (item == null) {
            return;
        }
        //播放按键音
        if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
            if (buttonPool == null || buttonAudioID == 0) {
                initKeySound();
            }
            buttonPool.play(buttonAudioID, 1, 1, 0, 0, 1);
        }
        performItemFunc(item, listPos, gridPos);

    }


    private boolean performItemFunc(final PeilvPlayData item, int listPos, int gridPos) {
        //更新数据并刷新界面
        TouzhuThreadPool.getInstance().addTask(new CalcListWhenSelectItem
                ("0", item, listPos, gridPos));
        return true;
    }

    private long isXGLHCServerTime(String lotCode) {
        if (UsualMethod.isXGSixMark(lotCode)) {
            return this.lhcServerTime;
        }
        return 0;
    }


    private final class CalcListWhenSelectItem implements Runnable {

        String fee;
        PeilvPlayData oldData;
        int listPos;
        int gridPos;

        public CalcListWhenSelectItem(String fee, PeilvPlayData data, int listPos, int gridPos) {
            this.fee = fee;
            this.oldData = data;
            this.listPos = listPos;
            this.gridPos = gridPos;
        }

        @Override
        public void run() {
            PeilvPlayData data = new PeilvPlayData();
            data.setCheckbox(oldData.isCheckbox());
            data.setNumber(oldData.getNumber());
            data.setPeilv(oldData.getPeilv());
//            data.setSelected(oldData.isSelected());
            data.setAllDatas(oldData.getAllDatas());
            data.setPeilvData(oldData.getPeilvData());
            data.setHelpNumber(oldData.getHelpNumber());
            data.setAppendTag(oldData.isAppendTag());
            data.setAppendTagToTail(oldData.isAppendTagToTail());
            data.setFilterSpecialSuffix(oldData.isFilterSpecialSuffix());
            data.setMoney(!Utils.isEmptyString(fee) &&
                    Utils.isNumeric(fee) ? Integer.parseInt(fee) : 0);
            if (!Utils.isEmptyString(fee)) {
                data.setMoney(Utils.isNumeric(fee) ? Integer.parseInt(fee) : 0);
            } else {
                data.setMoney(oldData.getMoney());
            }
            if (data.isSelected()) {
                data.setFocusDrawable(R.drawable.table_textview_bg_press_simple);
            } else {
                data.setFocusDrawable(R.drawable.table_textview_bg);
                data.setMoney(0);
            }
            //}
            PeilvData peilvData = listDatas.get(listPos);
            List<PeilvPlayData> subData = peilvData.getSubData();
            if (gridPos >= subData.size()) {
                return;
            }
            subData.set(gridPos, data);
            Message message = myHandler.obtainMessage(UPDATE_LISTVIEW, listPos, gridPos);
            message.sendToTarget();
        }
    }


    private static final class PeilvViewHolder {
        LinearLayout tagLayout;
        TextView tagTV;
        PeilvSimpleTableContainer tableView;
    }


    public void setCpBianHao(String cpBianHao) {
        this.cpBianHao = cpBianHao;
    }


    public void setFengPan(boolean fengPan) {
        isFengPan = fengPan;
        playAdapter.notifyDataSetChanged();

        if (isFengPan) {
            if (isAdded()) {
//                touzhuBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_press_unenable));
            }
        } else {
            if (isAdded()) {
//                touzhuBtn.setBackground(getResources().getDrawable(R.drawable.red_corner_btn_bg_selector));
            }
        }
    }






    private void initKeySound() {
        if (YiboPreference.instance(getActivity()).isButtonSoundAllow()) {
            //初始化SoundPool
            buttonPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
            buttonAudioID = buttonPool.load(getActivity(), R.raw.bet_select, 1);
        }
    }



    private final class MyHandler extends Handler {

        private WeakReference<XYBettingFragment> mReference;
        private XYBettingFragment fragment;

        public MyHandler(XYBettingFragment fragment) {
            mReference = new WeakReference<>(fragment);
            if (mReference != null) {
                this.fragment = mReference.get();
            }
        }


        public void handleMessage(Message message) {
            if (fragment == null) {
                return;
            }
            switch (message.what) {
                case CALC_ZHUSHU_MONEY:
                    CalcPeilvOrder peilvOrder = (CalcPeilvOrder) message.obj;
                    int zhushu = peilvOrder.getZhushu();
                    double money = peilvOrder.getTotalMoney();
                    float peilv = peilvOrder.getPeilvWhenMultiselect();
                    List<PeilvPlayData> datas = peilvOrder.getSelectDatas();
                    if (datas != null) {
                        selectNumList.clear();
                        selectNumList.addAll(datas);
                    }
                    //若是多选组合号码，则这里的总金额是输入框输入的金额，而不是注数*每注金额
                    fragment.calcPeilv = peilv;
                    fragment.calcOutZhushu = zhushu;

                    ((BettingMainFragment) fragment.getParentFragment()).onBottomUpdate(selectNumList,zhushu);
                    break;
                case UPDATE_LISTVIEW:
//                    refreshPaneAndClean(true);
                    int listPos = message.arg1;
                    int gridPos = message.arg2;
                    float moneyValue = -1;
                    if (message.obj != null) {
                        moneyValue = (float) message.obj;
                    } else {
                        moneyValue = -1;
                    }
                    if (listDatas.get(listPos).getSubData() != null) {
                        if (moneyValue != -1) {
                            PeilvPlayData playData = listDatas.get(listPos).getSubData().get(gridPos);
                            if (playData != null) {
                                playData.setMoney(moneyValue);
                                listDatas.get(listPos).getSubData().set(gridPos, playData);
                            }
                        }
                    }
                    playAdapter.notifyDataSetChanged();
                    //输入法点确定后，将选择的号码，金额发送到主界面更新底部栏显示出来
                    TouzhuThreadPool.getInstance().addTask(new CalcZhushuAndMoney(fragment, listDatas));
                    break;
            }
        }
    }


    /**
     * 判断是否是选中的号码
     *
     * @param data
     * @return
     */
    private boolean isSelectedNumber(PeilvPlayData data) {
        if (data == null) {
            return false;
        }
        return data.isSelected();
    }
}
