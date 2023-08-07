package com.yibo.yiboapp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.data.PeilvData;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.interfaces.NormalTouzhuListener;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.utils.ViewCache;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by johnson on 2017/9/29.
 * 赔率版中每一项的表格视图
 */

public class PeilvSimpleTableContainer extends LinearLayout {

    private static final String TAG = "PeilvSimpleTableContain";


    public static final int CHAT_TYPE = 1; //聊天室
    public static final int NORMAL_TZ_TYPE = 0;//正常简约版投注页面

    private int containerType = NORMAL_TZ_TYPE; //默认为正常简约版投注页面
    private Context mContext;
    private int DEFAULT_ROW_HEIGHT = 40;
    private int row_height;
    private int tableCount;
    private int txtSize;
    private int txtColorNormal;
    private int txtColorSelect;
    private Drawable unSelectDrawable;//未选中状态下的背景
    private Drawable selectDrawable;//选中状态下的背景
    private boolean animEffect;//点击动画效果
    private String cpCode;//彩票类型代码
    private int position;
    private List<PeilvPlayData> gridViewDatas;
    private TableCellListener tableCellListener;
    private NormalTouzhuListener normalTouzhuListener;
    private boolean isFengpan;


    public PeilvSimpleTableContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        gridViewDatas = new ArrayList<>();
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TableView);
        row_height = a.getDimensionPixelSize(R.styleable.TableView_row_height, DEFAULT_ROW_HEIGHT);
        tableCount = a.getInteger(R.styleable.TableView_table_count, 2);

        txtColorNormal = a.getColor(R.styleable.TableView_textcolor_normal,
                mContext.getResources().getColor(R.color.gray));
        txtColorSelect = a.getColor(R.styleable.TableView_textcolor_select,
                mContext.getResources().getColor(R.color.colorWhite));
        txtSize = a.getDimensionPixelSize(R.styleable.TableView_text_size, 12);

        unSelectDrawable = a.getDrawable(R.styleable.TableView_unselect_drawable);
        selectDrawable = a.getDrawable(R.styleable.TableView_select_drawable);
        animEffect = a.getBoolean(R.styleable.TableView_click_anim_effect, false);

        if (unSelectDrawable == null) {
            unSelectDrawable = mContext.getResources().getDrawable(R.drawable.light_gred_border_middle_segment);
        }
        if (selectDrawable == null) {
            selectDrawable = mContext.getResources().getDrawable(R.drawable.red_border_press);
        }
        a.recycle();
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public void setNormalTouzhuListener(NormalTouzhuListener normalTouzhuListener) {
        this.normalTouzhuListener = normalTouzhuListener;
    }

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    //根据数据计算出表列数的倍数
    private int figureOutColumnCount(PeilvPlayData item) {

        int DEFAULT_COLUMNS = 2;
        if (item == null) {
            return DEFAULT_COLUMNS;
        }
        int count = 2;
        if (!Utils.isEmptyString(item.getHelpNumber())) {
            count++;
        }
        if (!Utils.isEmptyString(item.getNumber())) {
            count++;
        }
        if (!Utils.isEmptyString(item.getPeilv())) {
            count++;
        }
        if (count > 4) {
            return 1;
        }
        return DEFAULT_COLUMNS;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateTableItem(List<PeilvPlayData> datas, int selectPos) {
        PeilvPlayData selectData = datas.get(selectPos);
        if (selectData == null) {
            return;
        }
        if (datas != null) {
            gridViewDatas.clear();
            gridViewDatas.addAll(datas);
        }
        int count = figureOutColumnCount(selectData);
        TableLayout tableLayout = (TableLayout) this.getChildAt(0);
        Utils.LOG("tag", "the select table view tag = " + selectPos);
        int lineView = selectPos / count;
        lineView++;
        TableRow view = (TableRow) tableLayout.getChildAt(lineView);
        if (view != null) {
            View itemView;
            if (selectPos % count == 0) {
                itemView = view.getChildAt(0);
            } else {
                itemView = view.getChildAt(count - 1);
            }
            if (itemView != null) {
                final LinearLayout cell = (LinearLayout) itemView.findViewById(R.id.cell);
                TextView numberStringTV = itemView.findViewById(R.id.number_tv);
                TextView peilvTV = itemView.findViewById(R.id.peilv);
                //若是多选情况，则更新指定项的checkbox选中状态
                if (selectData.isCheckbox()) {
                    CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
                    checkBox.setChecked(selectData.isSelected());
                } else {
                    XEditText moneyTV = (XEditText) itemView.findViewById(R.id.money);
                    if (moneyTV != null) {
                        moneyTV.setText(String.valueOf(selectData.getMoney() > 0 ? selectData.getMoney() : ""));
                    }
                }
                switchCellBg(selectData.getFocusDrawable(), cell, numberStringTV, peilvTV, selectData.getColor());
            }
        }

    }

    /**
     * 将赔率数据列表中的每表格所在的位置传入
     *
     * @param position
     */
    public void setTablePosition(int position) {
        this.position = position;
    }

    TableLayout tabLayout;
    //    TableRow headRow;
    LayoutParams params =
            new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    TableRow.LayoutParams rowHeadParams =
            new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

    TableLayout.LayoutParams tp =
            new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);


    private List<PeilvPlayData> playDatas;
    private List<PeilvData> mDatas;

    public void fillTables(final List<PeilvPlayData> datas, final Activity activity, List<PeilvData> mDatas) {

        this.mDatas = mDatas;

        if (datas == null || datas.isEmpty()) {
            return;
        }

        removeAllViews();

        tabLayout = new TableLayout(mContext);

        tabLayout.setVerticalScrollBarEnabled(false);
        tabLayout.setVerticalFadingEdgeEnabled(false);
        params.weight = 1.0f;
        setOrientation(HORIZONTAL);
        int count = figureOutColumnCount(datas.get(0));
        rowHeadParams.weight = 1.0f;
        tp.weight = 1.0f;
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                (int) mContext.getResources().getDimension(R.dimen.peilv_item_table_height));
        //设置表格内容
        rowParams.weight = 1.0f;

        //填充表格
        fillContents(tabLayout, datas, count, rowParams);

        ViewParent parent = tabLayout.getParent();
        if (parent != null) {
            removeAllViews();
        }
        this.addView(tabLayout, tp);


    }

    LinearLayout row1;

    TableRow contentRow;

    LinearLayout row2;

    //columnCount 最大为2
    private void fillContents(TableLayout table, final List<PeilvPlayData> datas, int columnCount,
                              TableRow.LayoutParams rowHeadParams) {

        if (datas != null) {
            gridViewDatas.clear();
            gridViewDatas.addAll(datas);
        }

        for (int i = 0; i < gridViewDatas.size(); i++) {

            if (gridViewDatas.size() % 2 != 0 && i == gridViewDatas.size() - 1) {
                columnCount = 1;
            }

            int temp = i + 1;
            if (columnCount == 1) {
                row1 = getTableRow(i);
                contentRow = new TableRow(mContext);
                if (row1 != null) {
                    row1.setTag(i);
                    ViewParent parent = row1.getParent();
                    if (parent != null) {
                        contentRow.removeAllViews();
                    }
                    contentRow.addView(row1, rowHeadParams);
                }

                ViewParent parent = contentRow.getParent();
                if (parent != null) {
                    table.removeAllViews();
                }
                ViewParent parent1 = contentRow.getParent();
                if (parent1 != null) {
                    table.removeAllViews();
                }
                table.addView(contentRow);

            } else if (columnCount == 2) {
                row1 = getTableRow(i);

                if (temp < gridViewDatas.size()) {
                    row2 = getTableRow(temp);
                } else {
                    row2 = null;
                }
                if (i % columnCount == 0) {
                    contentRow = new TableRow(mContext);
                    if (row1 != null) {
                        ViewParent parent = row1.getParent();
                        if (parent != null) {
                            contentRow.removeAllViews();
                        }
                        contentRow.addView(row1, rowHeadParams);
                    }
                    if (row2 == null) {
                        row2 = new LinearLayout(mContext);
                    }

                    // 防止出现
                    // java.lang.IllegalStateException: The specified child already has a parent.
                    // You must call removeView() on the child's parent first.
                    // 这个问题
                    ViewParent parent = row2.getParent();
                    if (parent != null) {
                        contentRow.removeAllViews();
                    }

                    contentRow.addView(row2, rowHeadParams);

                    ViewParent parent1 = contentRow.getParent();
                    if (parent1 != null) {
                        table.removeAllViews();
                    }
                    table.addView(contentRow);

                }
            }

        }

    }

    /**
     * 更新cell的背景颜色，选中或未选中时
     * @param focusDrawable
     * @param cell
     * @param numberStringTV
     * @param peilvTV
     * @param color
     */
    private void switchCellBg(int focusDrawable, LinearLayout cell, TextView numberStringTV, TextView peilvTV, int color) {
        if (cell == null) {
            return;
        }
        for (int i = 0; i < cell.getChildCount(); i++) {
            View v = cell.getChildAt(i);
            if (focusDrawable > 0) {
                if (getContainerType() == CHAT_TYPE){
                    numberStringTV.setTextColor(color);
                    peilvTV.setTextColor(color);
                }
                v.setBackground(mContext.getResources().getDrawable(focusDrawable));
            } else {
                if (getContainerType() == CHAT_TYPE) {
                    numberStringTV.setTextColor(Color.WHITE);
                    peilvTV.setTextColor(Color.WHITE);
                    v.setBackground(mContext.getResources().getDrawable(R.drawable.chat_table_textview_bg));
                } else {
                    v.setBackground(mContext.getResources().getDrawable(R.drawable.table_textview_bg));
                }
            }
        }

    }

    LinearLayout tableRow;


    @TargetApi(Build.VERSION_CODES.M)
    private LinearLayout getTableRow(final int cellIndex) {

        tableRow = (LinearLayout) ViewCache.getInstance().popAndAquire(getResources().getConfiguration(), mContext);

        if (tableRow == null) {
//            Log.e("table==null", "重新加载");
            if (containerType == CHAT_TYPE) {
                tableRow = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.chat_table_layout_simple, null);
            } else if (containerType == NORMAL_TZ_TYPE) {
                tableRow = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.table_layout_simple, null);
            } else {
                tableRow = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.table_layout_simple, null);
            }
        }

        final LinearLayout cell = (LinearLayout) tableRow.findViewById(R.id.cell);
        LinearLayout categoryLayout = (LinearLayout) tableRow.findViewById(R.id.layout);
        TextView categoryTV = (TextView) tableRow.findViewById(R.id.category);
        TextView categoryBallonTV = (TextView) tableRow.findViewById(R.id.category_number_ballon);
        TextView numberTV = (TextView) tableRow.findViewById(R.id.number);
        TextView numberStringTV = (TextView) tableRow.findViewById(R.id.number_tv);
        TextView peilvTV = (TextView) tableRow.findViewById(R.id.peilv);
        LinearLayout ll_check = (LinearLayout) tableRow.findViewById(R.id.ll_check);
//        final XEditText moneyTV = (XEditText) tableRow.findViewById(R.id.money);
        CheckBox checkBox = (CheckBox) tableRow.findViewById(R.id.checkbox);


        final PeilvPlayData data = gridViewDatas.get(cellIndex);
        if (data == null) {
            return null;
        }

        switchCellBg(data.getFocusDrawable(), cell ,numberStringTV,peilvTV, data.getColor());
        if (!Utils.isEmptyString(data.getHelpNumber())) {
            if (Utils.isNumeric(data.getNumber())) {
                categoryBallonTV.setVisibility(View.VISIBLE);
                categoryTV.setVisibility(View.GONE);
                categoryBallonTV.setText(data.getNumber());
            } else {
                categoryBallonTV.setVisibility(View.GONE);
                categoryTV.setVisibility(View.VISIBLE);
                categoryTV.setText(data.getNumber());
            }
        }

        if (!Utils.isEmptyString(data.getNumber())) {
            String number = data.getNumber().trim();
            String helpNumber = null;
            if (!Utils.isEmptyString(data.getHelpNumber())) {
                helpNumber = data.getHelpNumber().trim();
            }

            //如果号码不是数字，或者辅助号码不为空，将显示区域占满cell
            String showTxt = !Utils.isEmptyString(helpNumber) ? helpNumber : number;
            if (!Utils.isEmptyString(showTxt)) {
                if (Utils.isNumeric(showTxt)) {
                    numberStringTV.setVisibility(View.GONE);
                    numberTV.setVisibility(View.VISIBLE);
                    String version = YiboPreference.instance(mContext).getGameVersion();
                    if (BuildConfig.DEBUG) {
//                        Log.e(TAG, "showTxt.length：" + showTxt.length());
                    }

                    if (showTxt.length() >= 3) {
                        numberTV.setTextSize(14);
                    } else {
                        numberTV.setTextSize(18);
                    }
                    UsualMethod.figureImgeByCpCode(cpCode, showTxt, version, numberTV, 0, getContext());
//                    numberTV.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    numberStringTV.setVisibility(View.VISIBLE);
                    numberTV.setVisibility(View.GONE);
                    numberStringTV.setText(showTxt);

                    //解决尾数连，号码显示不全，如果是多选情况下，并且字符串长度大于等于13
                    if (data.isCheckbox() && showTxt.length() >= 11) {
                        categoryTV.setTextSize(14);
                        numberStringTV.setTextSize(14);
                    } else {
                        categoryTV.setTextSize(15);
                        numberStringTV.setTextSize(15);
                    }

                }
            }
        } else {
            numberTV.setVisibility(View.GONE);
            numberStringTV.setVisibility(GONE);
        }

        if (!Utils.isEmptyString(data.getPeilv())) {
            if (!Utils.isEmptyString(data.getPeilv())) {
                peilvTV.setVisibility(View.VISIBLE);
                peilvTV.setText(data.getPeilv());
//                peilvTV.setText(Utils.doubleToString(Double.valueOf(data.getPeilv())));
            } else {
                peilvTV.setVisibility(View.GONE);
            }
        } else {
            peilvTV.setVisibility(View.GONE);
        }


        if (data.isCheckbox()) {
            checkBox.setVisibility(View.VISIBLE);
            ll_check.setVisibility(View.VISIBLE);
            checkBox.setChecked(data.isSelected());
            peilvTV.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.GONE);
            ll_check.setVisibility(View.GONE);
            peilvTV.setVisibility(View.VISIBLE);
        }


        cell.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tableCellListener != null) {
                    if (isFengpan) {
                        ToastUtils.showShort(R.string.result_not_open_now);
                        return;
                    }
                    List<PeilvData> mDatas = PeilvSimpleTableContainer.this.mDatas;
                    //给每一个子玩法的名称重新赋值2
                    for (PeilvData mData : mDatas) {
                        String tagName = mData.getTagName();
                        for (PeilvPlayData peilvPlayData : mData.getSubData()) {
                            peilvPlayData.setItemName(tagName);
                        }
                    }
                    PeilvPlayData playData = gridViewDatas.get(cellIndex);
                    boolean isZhiXuan = false;
                    //判断当前是不是十一选五的直选玩法
                    if (mDatas.size() > 0) {
                        PeilvData peilvData = mDatas.get(0);
                        if (peilvData.getSubData() != null && peilvData.getSubData().size() > 0) {
                            PeilvPlayData peilvPlayData = peilvData.getSubData().get(0);
                            if (peilvPlayData.getAllDatas() != null && peilvPlayData.getAllDatas().size() > 0) {
                                if (peilvPlayData.getAllDatas().get(0).getPlayCode().startsWith("zhixuan")) {
                                    isZhiXuan = true;
                                }
                            }
                        }
                    }
                    //直选玩法中每个玩法的不同种球类只能选择一个球
                    if (isZhiXuan) {
                        //当前选中的号码的类型
                        String itemName = playData.getItemName();
                        //选中的号码
                        String number = playData.getNumber();
                        for (PeilvData mData : mDatas) {
                            if (!mData.getTagName().equals(itemName)) {
                                for (PeilvPlayData peilvPlayData : mData.getSubData()) {
                                    if (peilvPlayData.getNumber().equals(number)) {
                                        if (peilvPlayData.isSelected()) {
                                            ToastUtils.showShort("不能同时选择相同的号码");
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (playData.isSelected()) {
                        playData.setMoney(0);
                        playData.setSelected(false);
                        if (getContainerType() == CHAT_TYPE) {
                            numberStringTV.setTextColor(Color.WHITE);
                            peilvTV.setTextColor(Color.WHITE);
                            playData.setColor(Color.WHITE);
                            playData.setFocusDrawable(R.drawable.chat_table_textview_bg);
                        } else {
                            playData.setFocusDrawable(R.drawable.table_textview_bg);
                        }
                    } else {
                        playData.setSelected(true);
                        if (getContainerType() == CHAT_TYPE) {
                            numberStringTV.setTextColor(Color.BLACK);
                            peilvTV.setTextColor(Color.BLACK);
                            playData.setColor(Color.BLACK);
                            playData.setFocusDrawable(R.drawable.chat_table_textview_bg_press_simple);
                        } else {
                            playData.setFocusDrawable(R.drawable.table_textview_bg_press_simple);
                        }
                    }
                    switchCellBg(playData.getFocusDrawable(), cell, numberStringTV, peilvTV ,playData.getColor());
                    gridViewDatas.set(cellIndex, playData);
                    //通知前台，更新底部面板中的注数及总金额
                    if (normalTouzhuListener != null) {
                        normalTouzhuListener.onNormalUpdate(position, cellIndex, true);
                    }
//                    tableCellListener.onCellSelect(        playData,cellIndex);
                }
            }
        });

        return tableRow;

    }

    public interface TableCellListener {
        void onCellSelect(PeilvPlayData data, int cellIndex);
    }

    public void setTableCellListener(TableCellListener tableCellListener) {
        this.tableCellListener = tableCellListener;
    }

    public void setFengpan(boolean fengpan) {
        isFengpan = fengpan;
    }
}
