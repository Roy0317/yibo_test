package com.yibo.yiboapp.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.entify.SportBean;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by johnson on 2017/9/29.
 * 体育赛事每一项的表格视图
 */

public class SportTableContainer extends LinearLayout {

    Context mContext;
    int DEFAULT_ROW_HEIGHT = 40;
    int row_height = DEFAULT_ROW_HEIGHT;

    int txtSize = 12;
    int txtColorNormal = R.color.gray;
    int txtColorSelect = R.color.colorWhite;
    Drawable unSelectDrawable;//未选中状态下的背景
    Drawable selectDrawable;//选中状态下的背景

    public static final int FOOTBALL_PAGE = 0;
    public static final int BASKETBALL_PAGE = 1;


    public SportTableContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TableView);
        row_height = a.getDimensionPixelSize(R.styleable.TableView_row_height, DEFAULT_ROW_HEIGHT);

        txtColorNormal = a.getColor(R.styleable.TableView_textcolor_normal,
                mContext.getResources().getColor(R.color.gray));
        txtColorSelect = a.getColor(R.styleable.TableView_textcolor_select,
                mContext.getResources().getColor(R.color.colorWhite));
        txtSize = a.getDimensionPixelSize(R.styleable.TableView_text_size, 12);

        unSelectDrawable = a.getDrawable(R.styleable.TableView_unselect_drawable);
        selectDrawable = a.getDrawable(R.styleable.TableView_select_drawable);

        if (unSelectDrawable == null) {
            unSelectDrawable = mContext.getResources().getDrawable(R.drawable.light_gred_border_middle_segment);
        }
        if (selectDrawable == null) {
            selectDrawable = mContext.getResources().getDrawable(R.drawable.red_border_press);
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    //根据小玩法数组名称，填充表格头部玩法数据并返回头部视图
    private LinearLayout figureTableHeader(String[] subPlayNames) {
        if (subPlayNames == null) {
            return null;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.setLayoutDirection(HORIZONTAL);
        LinearLayout tableHeader = new LinearLayout(mContext);
        for (String name : subPlayNames) {
            LinearLayout tableItem = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.sport_table_header_item, null);
            TextView resultTV = (TextView) tableItem.findViewById(R.id.result);
            resultTV.setText(name);
            tableHeader.addView(tableItem,params);
        }
        return tableHeader;
    }

    /**
     * 根据体育球类型和大玩法获取小玩法名称
     * @param gameType 体育球类型  0-足球 1-篮球
     * @param playType 大玩法
     * @return
     */
    private String[] getSubPlayByPlaycode(int gameType,String playType) {
        if (gameType == FOOTBALL_PAGE) {
            if (playType.equals(Constant.FT_MN)||playType.equals(Constant.FT_MX)) {
                return new String[]{"场次", "独赢", "让球", "大小", "单双"};
            } else if (playType.equals(Constant.FT_TI)) {
                return new String[]{"主/客","1:0", "2:0", "2:1", "3:0", "3:1","3:2"};
            } else if (playType.equals(Constant.FT_BC)) {
                return new String[]{"0-1", "2-3", "4-6", "7或以上"};
            } else if (playType.equals(Constant.FT_HF)) {
                return new String[]{"主/主", "主/和", "主/客", "和/主","和/和","和/客","客/主","客/和","客/客",};
            }
        } else if (gameType == BASKETBALL_PAGE) {
            if (playType.equals(Constant.BK_MN)||playType.equals(Constant.BK_MX)) {
                return new String[]{"独赢", "让球", "大小球"};
            }
        }
        return null;
    }

    public static int tableColumnSize(int gameType,String playType) {
        if (gameType == FOOTBALL_PAGE) {
            if (playType.equals(Constant.FT_MN)||playType.equals(Constant.FT_MX)) {
                return 5;
            } else if (playType.equals(Constant.FT_TI)) {
                return 7;
            } else if (playType.equals(Constant.FT_BC)) {
                return 4;
            } else if (playType.equals(Constant.FT_HF)) {
                return 9;
            }
        } else if (gameType == BASKETBALL_PAGE) {
            if (playType.equals(Constant.BK_MN)||playType.equals(Constant.BK_MX)) {
                return 3;
            }
        }
        return 0;
    }

    /**
     * 根据寒事结果数据构造表格项
     * @param results 赛事结果
     * @param gameType 体育球类型  0-足球 1-篮球
     * @param playCode 大玩法
     */
    public int fillTables(Map<String,Object> results, int gameType, String playCode) {

        if (results == null || results.isEmpty()) {
            return 0;
        }

        //获取小玩法名称数组
        String[] subPlayCode = getSubPlayByPlaycode(gameType, playCode);
        if (subPlayCode == null) {
            return 0;
        }
        removeAllViews();
        TableLayout tabLayout = new TableLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        setOrientation(VERTICAL);

        //设置表格头部
        TableRow headRow = new TableRow(mContext);
        TableRow.LayoutParams rowHeadParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        rowHeadParams.weight = 1.0f;

        LinearLayout tableHeader = figureTableHeader(subPlayCode);
        headRow.addView(tableHeader,rowHeadParams);

        TableLayout.LayoutParams tp = new TableLayout.LayoutParams(TableLayout.LayoutParams.
                MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tp.weight = 1.0f;
        tabLayout.addView(headRow);
        //设置表格内容
        this.addView(tabLayout,tp);

        return subPlayCode.length;
    }

    /**
     *
     * @param item 赛事结果数据
     * @param ballType 球类
     * @param playType 玩法
     * @param columns 每行几列
     * @param gameCategory 球赛分类，滚球，今日赛事，早盘
     * @return
     */
    public static List<SportBean> fillSportResultData(Map<String,Object> item, int ballType, String playType,int columns,String gameCategory) {

        if (item == null) {
            return null;
        }
        if (ballType == FOOTBALL_PAGE) {
            if (playType.equals(Constant.FT_MN)) {
                return fillFootBallMX(item,columns,gameCategory);
            } else if (playType.equals(Constant.FT_TI)) {
                return fillFootBallTI(item,columns);
            } else if (playType.equals(Constant.FT_BC)) {
                return fillFootBallBC(item, columns);
            } else if (playType.equals(Constant.FT_HF)) {
                return fillFootBallHF(item, columns);
            } else if (playType.equals(Constant.FT_MX)) {
                return fillFootBallMX(item, columns,gameCategory);
            }
        } else if (ballType == BASKETBALL_PAGE) {
            if (playType.equals(Constant.BK_MN)) {
                return fillBasketBallMN(item, columns,gameCategory);
            } else if (playType.equals(Constant.BK_MX)) {
                return fillBasketBallMX(item, columns);
            }
        }
        return null;
    }

    //足球--波胆
    private static List<SportBean> fillFootBallTI(Map<String,Object> item,int columns) {

        List<SportBean> lists = new ArrayList<>();
        String gid = item.containsKey(Constant.gid)?(String)item.get(Constant.gid):"";
        String matchId = item.containsKey(Constant.matchId)?(String)item.get(Constant.matchId):"";
        String league = item.containsKey(Constant.league)?(String)item.get(Constant.league):"";
        String home = item.containsKey(Constant.home)?(String)item.get(Constant.home):"";
        String guest = item.containsKey(Constant.guest)?(String)item.get(Constant.guest):"";
        String scoreH = item.containsKey("scoreH") ? (String) item.get("scoreH") : "";
        String scoreC = item.containsKey("scoreC") ? (String) item.get("scoreC") : "";

        //第一行，主队
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setGameCategoryName("全场-波胆");
            itemBean.setLianSaiName(league);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);

            if (i == 0) {
                itemBean.setTxt("主队");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_H1C0")?(String)item.get("ior_H1C0"):"");
                itemBean.setPeilvKey("ior_H1C0");
                itemBean.setBetTeamName("1:0");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_H2C0")?(String)item.get("ior_H2C0"):"");
                itemBean.setPeilvKey("ior_H2C0");
                itemBean.setBetTeamName("2:0");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_H2C1")?(String)item.get("ior_H2C1"):"");
                itemBean.setPeilvKey("ior_H2C1");
                itemBean.setBetTeamName("2:1");
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_H3C0")?(String)item.get("ior_H3C0"):"");
                itemBean.setPeilvKey("ior_H3C0");
                itemBean.setBetTeamName("3:0");
            } else if (i == 5) {
                itemBean.setPeilv(item.containsKey("ior_H3C1")?(String)item.get("ior_H3C1"):"");
                itemBean.setPeilvKey("ior_H3C1");
                itemBean.setBetTeamName("3:1");
            } else if (i == 6) {
                itemBean.setPeilv(item.containsKey("ior_H3C2")?(String)item.get("ior_H3C2"):"");
                itemBean.setPeilvKey("ior_H3C2");
                itemBean.setBetTeamName("3:2");
            }
            lists.add(itemBean);
        }
        //第二行，客队
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setGameCategoryName("全场-波胆");
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setTxt("客队");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_H0C1")?(String)item.get("ior_H0C1"):"");
                itemBean.setPeilvKey("ior_H0C1");
                itemBean.setBetTeamName("0:1");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_H0C2")?(String)item.get("ior_H0C2"):"");
                itemBean.setPeilvKey("ior_H0C2");
                itemBean.setBetTeamName("0:2");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_H1C2")?(String)item.get("ior_H1C2"):"");
                itemBean.setPeilvKey("ior_H1C2");
                itemBean.setBetTeamName("1:2");
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_H0C3")?(String)item.get("ior_H0C3"):"");
                itemBean.setPeilvKey("ior_H0C3");
                itemBean.setBetTeamName("0:3");
            } else if (i == 5) {
                itemBean.setPeilv(item.containsKey("ior_H1C3")?(String)item.get("ior_H1C3"):"");
                itemBean.setPeilvKey("ior_H1C3");
                itemBean.setBetTeamName("1:3");
            } else if (i == 6) {
                itemBean.setPeilv(item.containsKey("ior_H2C3")?(String)item.get("ior_H2C3"):"");
                itemBean.setPeilvKey("ior_H2C3");
                itemBean.setBetTeamName("2:3");
            }
            lists.add(itemBean);
        }


        //第三行，"4:0","4:1","4:2","4:3" 这几个比分另起一行当做表格头部,因为一行不够显示十几个比分项
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setHeader(true);
            itemBean.setFakeItem(true);
            itemBean.setMid(matchId);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setTxt("主/客");
            } else if (i == 1) {
                itemBean.setTxt("4:0");
                itemBean.setBetTeamName("4:0");
            } else if (i == 2) {
                itemBean.setTxt("4:1");
                itemBean.setBetTeamName("4:1");
            } else if (i == 3) {
                itemBean.setTxt("4:2");
                itemBean.setBetTeamName("4:2");
            } else if (i == 4) {
                itemBean.setTxt("4:3");
                itemBean.setBetTeamName("4:3");
            }
            lists.add(itemBean);
        }

        //第四行，主队（"4:0","4:1","4:2","4:3"）的赔率
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setGameCategoryName("全场-波胆");
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setTxt("主队");
                itemBean.setFakeItem(true);
            }if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_H4C0")?(String)item.get("ior_H4C0"):"");
                itemBean.setPeilvKey("ior_H4C0");
                itemBean.setBetTeamName("4:0");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_H4C1")?(String)item.get("ior_H4C1"):"");
                itemBean.setPeilvKey("ior_H4C1");
                itemBean.setBetTeamName("4:1");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_H4C2")?(String)item.get("ior_H4C2"):"");
                itemBean.setPeilvKey("ior_H4C2");
                itemBean.setBetTeamName("4:2");
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_H4C3")?(String)item.get("ior_H4C3"):"");
                itemBean.setPeilvKey("ior_H4C3");
                itemBean.setBetTeamName("4:3");
            }
            lists.add(itemBean);
        }
        //第五行，客队（"0:4","1:4","2:4","3:4"）的赔率
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setGameCategoryName("全场-波胆");
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setTxt("客队");
                itemBean.setFakeItem(true);
            }if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_H0C4")?(String)item.get("ior_H0C4"):"");
                itemBean.setPeilvKey("ior_H0C4");
                itemBean.setBetTeamName("0:4");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_H1C4")?(String)item.get("ior_H1C4"):"");
                itemBean.setPeilvKey("ior_H1C4");
                itemBean.setBetTeamName("1:4");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_H2C4")?(String)item.get("ior_H2C4"):"");
                itemBean.setPeilvKey("ior_H2C4");
                itemBean.setBetTeamName("2:4");
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_H3C4")?(String)item.get("ior_H3C4"):"");
                itemBean.setPeilvKey("ior_H3C4");
                itemBean.setBetTeamName("3:4");
            }
            lists.add(itemBean);
        }

        //第六行，0：0，1：1，2：2，3：3，4：4,其它，这几个比分另起一行当做表格头部
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setHeader(true);
            itemBean.setFakeItem(true);
            itemBean.setMid(matchId);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setTxt("主/客");
            } else if (i == 1) {
                itemBean.setTxt("0:0");
                itemBean.setBetTeamName("0:0");
            }else if (i == 2) {
                itemBean.setTxt("1:1");
                itemBean.setBetTeamName("1:1");
            } else if (i == 3) {
                itemBean.setTxt("2:2");
                itemBean.setBetTeamName("2:2");
            } else if (i == 4) {
                itemBean.setTxt("3:3");
                itemBean.setBetTeamName("3:3");
            } else if (i == 5) {
                itemBean.setTxt("4:4");
                itemBean.setBetTeamName("4:4");
            } else if (i == 6) {
                itemBean.setTxt("其它");
            }
            lists.add(itemBean);
        }

        //第七行，平分赔率
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setGameCategoryName("全场-波胆");
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_H0C0")?(String)item.get("ior_H0C0"):"");
                itemBean.setPeilvKey("ior_H0C0");
                itemBean.setBetTeamName("0:0");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_H1C1")?(String)item.get("ior_H1C1"):"");
                itemBean.setPeilvKey("ior_H1C1");
                itemBean.setBetTeamName("1:1");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_H2C2")?(String)item.get("ior_H2C2"):"");
                itemBean.setPeilvKey("ior_H2C2");
                itemBean.setBetTeamName("2:2");
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_H3C3")?(String)item.get("ior_H3C3"):"");
                itemBean.setPeilvKey("ior_H3C3");
                itemBean.setBetTeamName("3:3");
            } else if (i == 5) {
                itemBean.setPeilv(item.containsKey("ior_H4C4")?(String)item.get("ior_H4C4"):"");
                itemBean.setPeilvKey("ior_H4C4");
                itemBean.setBetTeamName("4:4");
            } else if (i == 6) {
                itemBean.setPeilv(item.containsKey("ior_OVH")?(String)item.get("ior_OVH"):"");
                itemBean.setPeilvKey("ior_OVH");
            }
            lists.add(itemBean);
        }


        return lists;
    }

    /**
     * 足球--总入球
     * @param item 头部数据
     * @param columns 列数
     * @return
     */
    private static List<SportBean> fillFootBallBC(Map<String,Object> item,int columns) {

        List<SportBean> lists = new ArrayList<>();
        String gid = item.containsKey(Constant.gid)?(String)item.get(Constant.gid):"";
        String matchId = item.containsKey(Constant.matchId)?(String)item.get(Constant.matchId):"";
        String league = item.containsKey(Constant.league)?(String)item.get(Constant.league):"";
        String home = item.containsKey(Constant.home)?(String)item.get(Constant.home):"";
        String guest = item.containsKey(Constant.guest)?(String)item.get(Constant.guest):"";
        String scoreH = item.containsKey("scoreH") ? (String) item.get("scoreH") : "";
        String scoreC = item.containsKey("scoreC") ? (String) item.get("scoreC") : "";

        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setGameCategoryName("全场-波胆");
            itemBean.setLianSaiName(league);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setPeilv(item.containsKey("ior_T01")?(String)item.get("ior_T01"):"");
                itemBean.setPeilvKey("ior_T01");
                itemBean.setBetTeamName("0:1");
            } else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_T23")?(String)item.get("ior_T23"):"");
                itemBean.setPeilvKey("ior_T23");
                itemBean.setBetTeamName("2:3");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_T46")?(String)item.get("ior_T46"):"");
                itemBean.setPeilvKey("ior_T46");
                itemBean.setBetTeamName("4:6");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_OVER")?(String)item.get("ior_OVER"):"");
                itemBean.setPeilvKey("ior_OVER");
                itemBean.setBetTeamName("7或以上");
            }
            lists.add(itemBean);
        }
        return lists;
    }

    //足球--半场全场
    private static List<SportBean> fillFootBallHF(Map<String,Object> item,int columns) {

        List<SportBean> lists = new ArrayList<>();
        String gid = item.containsKey(Constant.gid)?(String)item.get(Constant.gid):"";
        String matchId = item.containsKey(Constant.matchId)?(String)item.get(Constant.matchId):"";
        String league = item.containsKey(Constant.league)?(String)item.get(Constant.league):"";
        String home = item.containsKey(Constant.home)?(String)item.get(Constant.home):"";
        String guest = item.containsKey(Constant.guest)?(String)item.get(Constant.guest):"";
        String scoreH = item.containsKey("scoreH") ? (String) item.get("scoreH") : "";
        String scoreC = item.containsKey("scoreC") ? (String) item.get("scoreC") : "";

        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setGameCategoryName("半场/全场");
            itemBean.setLianSaiName(league);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            if (i == 0) {
                itemBean.setPeilv(item.containsKey("ior_FHH")?(String)item.get("ior_FHH"):"");
                itemBean.setPeilvKey("ior_FHH");
                itemBean.setBetTeamName(home+"/"+home);
            } else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_FHN")?(String)item.get("ior_FHN"):"");
                itemBean.setPeilvKey("ior_FHN");
                itemBean.setBetTeamName(home+"/和局");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_FHC")?(String)item.get("ior_FHC"):"");
                itemBean.setPeilvKey("ior_FHC");
                itemBean.setBetTeamName(home+"/"+guest);
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_FNH")?(String)item.get("ior_FNH"):"");
                itemBean.setPeilvKey("ior_FNH");
                itemBean.setBetTeamName("和局/"+home);
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_FNN")?(String)item.get("ior_FNN"):"");
                itemBean.setPeilvKey("ior_FNN");
                itemBean.setBetTeamName("和局/和局");
            } else if (i == 5) {
                itemBean.setPeilv(item.containsKey("ior_FNC")?(String)item.get("ior_FNC"):"");
                itemBean.setPeilvKey("ior_FNC");
                itemBean.setBetTeamName("和局/"+guest);
            } else if (i == 6) {
                itemBean.setPeilv(item.containsKey("ior_FCH")?(String)item.get("ior_FCH"):"");
                itemBean.setPeilvKey("ior_FCH");
                itemBean.setBetTeamName(guest+"/"+home);
            } else if (i == 7) {
                itemBean.setPeilv(item.containsKey("ior_FCN")?(String)item.get("ior_FCN"):"");
                itemBean.setPeilvKey("ior_FCN");
                itemBean.setBetTeamName(guest+"/和局");
            } else if (i == 8) {
                itemBean.setPeilv(item.containsKey("ior_FCC")?(String)item.get("ior_FCC"):"");
                itemBean.setPeilvKey("ior_FCC");
                itemBean.setBetTeamName(guest+"/"+guest);
            }
            lists.add(itemBean);
        }
        return lists;
    }

    /**
     *足球--混合过关
     * @param item 赛事数据
     * @param columns 列数
     * @param categoryType 赛事类型  滚球，今日赛事，早盘
     * @return
     */
    private static List<SportBean> fillFootBallMX(Map<String,Object> item,int columns,String categoryType) {

        List<SportBean> lists = new ArrayList<>();
        String gid = item.containsKey(Constant.gid)?(String)item.get(Constant.gid):"";
        String matchId = item.containsKey(Constant.matchId)?(String)item.get(Constant.matchId):"";
        String league = item.containsKey(Constant.league)?(String)item.get(Constant.league):"";
        String home = item.containsKey(Constant.home)?(String)item.get(Constant.home):"";
        String guest = item.containsKey(Constant.guest)?(String)item.get(Constant.guest):"";
        String retimeset = item.containsKey("retimeset") ? (String) item.get("retimeset") : "";
        String scoreH = item.containsKey("scoreH") ? (String) item.get("scoreH") : "";
        String scoreC = item.containsKey("scoreC") ? (String) item.get("scoreC") : "";

        //第一行 全场赢
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setMid(matchId);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setBetTeamName(home);

            if (categoryType.equals(Constant.RB_TYPE)) {
                if (!Utils.isEmptyString(retimeset) && retimeset.contains("^")) {
                    String[] split = retimeset.split("^");
                    if (split != null && split.length == 2) {
                        itemBean.setHalfName(split[0].equals("1H")?"上半场":"下半场");
                        itemBean.setGameRealTime(split[1]);
                    }
                }else if (!Utils.isEmptyString(retimeset)){
                    itemBean.setGameRealTime("半场");
                }
                itemBean.setScores(scoreH + "-" + scoreC);
            }
            if (i == 0) {
                itemBean.setTxt("全场(主)");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_MH")?(String)item.get("ior_MH"):"");
                itemBean.setPeilvKey("ior_MH");
                itemBean.setGameCategoryName("全场-"+"独赢");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_RH")?(String)item.get("ior_RH"):"");
                itemBean.setTxt(item.containsKey("CON_RH")?(String)item.get("CON_RH"):"");
                itemBean.setPeilvKey("ior_RH");
                itemBean.setProject(item.containsKey("CON_RH")?(String)item.get("CON_RH"):"");
                itemBean.setGameCategoryName("全场-"+"让球");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_OUH")?(String)item.get("ior_OUH"):"");
                itemBean.setTxt(item.containsKey("CON_OUH")?"大"+item.get("CON_OUH"):"");
                itemBean.setPeilvKey("ior_OUH");
                itemBean.setProject(item.containsKey("CON_OUH")?(String) item.get("CON_OUH"):"");
                itemBean.setGameCategoryName("全场-"+"大小");
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_EOO")?(String)item.get("ior_EOO"):"");
                itemBean.setTxt("单");
                itemBean.setPeilvKey("ior_EOO");
                itemBean.setGameCategoryName("全场-"+"单双");
            }
            lists.add(itemBean);
        }
        //第二行 全场输
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setBetTeamName(guest);
            if (i == 0) {
                itemBean.setTxt("全场(客)");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_MC")?(String)item.get("ior_MC"):"");
                itemBean.setPeilvKey("ior_MC");
                itemBean.setGameCategoryName("全场-"+"独赢");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_RC")?(String)item.get("ior_RC"):"");
                itemBean.setTxt(item.containsKey("CON_RC")?(String)item.get("CON_RC"):"");
                itemBean.setPeilvKey("ior_RC");
                itemBean.setProject(item.containsKey("CON_RC")?(String)item.get("CON_RC"):"");
                itemBean.setGameCategoryName("全场-"+"让球");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_OUC")?(String)item.get("ior_OUC"):"");
                itemBean.setTxt(item.containsKey("CON_OUC")?"小"+(String)item.get("CON_OUC"):"");
                itemBean.setPeilvKey("ior_OUC");
                itemBean.setProject(item.containsKey("CON_OUC")?(String)item.get("CON_OUC"):"");
                itemBean.setGameCategoryName("全场-"+"大小");
            } else if (i == 4) {
                itemBean.setPeilv(item.containsKey("ior_EOE")?(String)item.get("ior_EOE"):"");
                itemBean.setTxt("双");
                itemBean.setPeilvKey("ior_EOE");
                itemBean.setGameCategoryName("全场-"+"单双");
            }
            lists.add(itemBean);
        }
        //第三行 全场和局
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setBetTeamName("和局");
            if (i == 0) {
                itemBean.setTxt("全场(和)");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_MN")?(String)item.get("ior_MN"):"");
                itemBean.setPeilvKey("ior_MN");
                itemBean.setGameCategoryName("全场-"+"独赢");
            }
            lists.add(itemBean);
        }
        //第四行 半场赢
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setBetTeamName(home);
            if (i == 0) {
                itemBean.setTxt("半场(主)");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_HMH")?(String)item.get("ior_HMH"):"");
                itemBean.setPeilvKey("ior_HMH");
                itemBean.setGameCategoryName("半场-"+"独赢");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_HRH")?(String)item.get("ior_HRH"):"");
                itemBean.setTxt(item.containsKey("CON_HRH")?(String)item.get("CON_HRH"):"");
                itemBean.setPeilvKey("ior_HRH");
                itemBean.setProject(item.containsKey("CON_HRH")?(String)item.get("CON_HRH"):"");
                itemBean.setGameCategoryName("半场-"+"让球");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_HOUH")?(String)item.get("ior_HOUH"):"");
                itemBean.setTxt(item.containsKey("CON_HOUH")?"大"+(String)item.get("CON_HOUH"):"");
                itemBean.setPeilvKey("ior_HOUH");
                itemBean.setProject(item.containsKey("CON_HOUH")?(String)item.get("CON_HOUH"):"");
                itemBean.setGameCategoryName("半场-"+"大小");
            }
            lists.add(itemBean);
        }
        //第五行 半场输
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setBetTeamName(guest);
            if (i == 0) {
                itemBean.setTxt("半场(客)");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_HMC")?(String)item.get("ior_HMC"):"");
                itemBean.setPeilvKey("ior_HMC");
                itemBean.setGameCategoryName("半场-"+"独赢");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_HRC")?(String)item.get("ior_HRC"):"");
                itemBean.setTxt(item.containsKey("CON_HRC")?(String)item.get("CON_HRC"):"");
                itemBean.setPeilvKey("ior_HRC");
                itemBean.setProject(item.containsKey("CON_HRC")?(String)item.get("CON_HRC"):"");
                itemBean.setGameCategoryName("半场-"+"让球");
            } else if (i == 3) {
                itemBean.setPeilv(item.containsKey("ior_HOUC")?(String)item.get("ior_HOUC"):"");
                itemBean.setTxt(item.containsKey("CON_HOUC")?"小"+(String)item.get("CON_HOUC"):"");
                itemBean.setPeilvKey("ior_HOUC");
                itemBean.setProject(item.containsKey("CON_HOUC")?(String)item.get("CON_HOUC"):"");
                itemBean.setGameCategoryName("半场-"+"大小");
            }
            lists.add(itemBean);
        }
        //第六行 半场和局
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setScoreH(scoreH);
            itemBean.setScoreC(scoreC);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setBetTeamName("和局");
            if (i == 0) {
                itemBean.setTxt("半场(和)");
                itemBean.setFakeItem(true);
            }else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_HMN")?(String)item.get("ior_HMN"):"");
                itemBean.setPeilvKey("ior_HMN");
                itemBean.setGameCategoryName("半场-"+"独赢");
            }
            lists.add(itemBean);
        }

        return lists;
    }

    //篮球-- 全部
    private static List<SportBean> fillBasketBallMN(Map<String,Object> item,int columns,String categoryType) {

        List<SportBean> lists = new ArrayList<>();
        String gid = item.containsKey(Constant.gid)?(String)item.get(Constant.gid):"";
        String matchId = item.containsKey(Constant.matchId)?(String)item.get(Constant.matchId):"";
        String league = item.containsKey(Constant.league)?(String)item.get(Constant.league):"";
        String home = item.containsKey(Constant.home)?(String)item.get(Constant.home):"";
        String guest = item.containsKey(Constant.guest)?(String)item.get(Constant.guest):"";
        String nowSession = item.containsKey("nowSession")?(String)item.get("nowSession"):"";
        String scoreH = item.containsKey("scoreH") ? (String) item.get("scoreH") : "";
        String scoreC = item.containsKey("scoreC") ? (String) item.get("scoreC") : "";

        //第一行
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setScoreC(scoreC);
            itemBean.setScoreH(scoreH);

            if (categoryType.equals(Constant.RB_TYPE)) {
                if (nowSession.equalsIgnoreCase("OT")) {
                    itemBean.setNowSession("加时");
                } else if (nowSession.equalsIgnoreCase("1Q")) {
                    itemBean.setNowSession("第一节");
                } else if (nowSession.equalsIgnoreCase("2Q")) {
                    itemBean.setNowSession("第二节");
                } else if (nowSession.equalsIgnoreCase("3Q")) {
                    itemBean.setNowSession("第三节");
                } else if (nowSession.equalsIgnoreCase("4Q")) {
                    itemBean.setNowSession("第四节");
                }
                itemBean.setScores(scoreH + "-" + scoreC);
            }
            if (i == 0) {
                itemBean.setPeilv(item.containsKey("ior_MH")?(String)item.get("ior_MH"):"");
                itemBean.setPeilvKey("ior_MH");
                itemBean.setGameCategoryName("全场-"+"独赢");
            } else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_RH")?(String)item.get("ior_RH"):"");
                itemBean.setTxt(item.containsKey("CON_RH")?(String)item.get("CON_RH"):"");
                itemBean.setPeilvKey("ior_RH");
                itemBean.setProject(item.containsKey("CON_RH")?(String)item.get("CON_RH"):"");
                itemBean.setGameCategoryName("全场-"+"让球");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_OUH")?(String)item.get("ior_OUH"):"");
                itemBean.setTxt("大"+(item.containsKey("CON_OUH")?(String)item.get("CON_OUH"):""));
                itemBean.setPeilvKey("ior_OUH");
                itemBean.setProject(item.containsKey("CON_OUH")?(String)item.get("CON_OUH"):"");
                itemBean.setGameCategoryName("全场-"+"大小");
            }
            lists.add(itemBean);
        }
        //第二行
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setScoreC(scoreC);
            itemBean.setScoreH(scoreH);
            if (i == 0) {
                itemBean.setPeilv(item.containsKey("ior_MC")?(String)item.get("ior_MC"):"");
                itemBean.setPeilvKey("ior_MC");
                itemBean.setGameCategoryName("全场-"+"独赢");
            } else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_RC")?(String)item.get("ior_RC"):"");
                itemBean.setTxt(item.containsKey("CON_RC")?(String)item.get("CON_RC"):"");
                itemBean.setPeilvKey("ior_RC");
                itemBean.setProject(item.containsKey("CON_RC")?(String)item.get("CON_RC"):"");
                itemBean.setGameCategoryName("全场-"+"让球");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_OUC")?(String)item.get("ior_OUC"):"");
                itemBean.setTxt("小"+(item.containsKey("CON_OUC")?(String)item.get("CON_OUC"):""));
                itemBean.setPeilvKey("ior_OUC");
                itemBean.setProject(item.containsKey("CON_OUC")?(String)item.get("CON_OUC"):"");
                itemBean.setGameCategoryName("全场-"+"大小");
            }
            lists.add(itemBean);
        }

        return lists;
    }

    //篮球-- 混合过关
    private static List<SportBean> fillBasketBallMX(Map<String,Object> item,int columns) {

        List<SportBean> lists = new ArrayList<>();
        String gid = item.containsKey(Constant.gid)?(String)item.get(Constant.gid):"";
        String matchId = item.containsKey(Constant.matchId)?(String)item.get(Constant.matchId):"";
        String league = item.containsKey(Constant.league)?(String)item.get(Constant.league):"";
        String home = item.containsKey(Constant.home)?(String)item.get(Constant.home):"";
        String guest = item.containsKey(Constant.guest)?(String)item.get(Constant.guest):"";

        //第一行
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setLianSaiName(league);
            itemBean.setTeamNames(home+" vs "+guest);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setScoreC(home);
            itemBean.setScoreH(guest);
            if (i == 0) {
                itemBean.setPeilv(item.containsKey("ior_MH")?(String)item.get("ior_MH"):"");
                itemBean.setPeilvKey("ior_MH");
                itemBean.setGameCategoryName("全场-"+"独赢");
            } else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_RH")?(String)item.get("ior_RH"):"");
                itemBean.setTxt(item.containsKey("CON_RH")?(String)item.get("CON_RH"):"");
                itemBean.setPeilvKey("ior_RH");
                itemBean.setProject(item.containsKey("CON_RH")?(String)item.get("CON_RH"):"");
                itemBean.setGameCategoryName("全场-"+"让球");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_OUH")?(String)item.get("ior_OUH"):"");
                itemBean.setTxt("大"+(item.containsKey("CON_OUH")?(String)item.get("CON_OUH"):""));
                itemBean.setPeilvKey("ior_OUH");
                itemBean.setProject(item.containsKey("CON_OUH")?(String)item.get("CON_OUH"):"");
                itemBean.setGameCategoryName("全场-"+"大小");
            }
            lists.add(itemBean);
        }
        //第二行
        for (int i=0;i<columns;i++) {
            SportBean itemBean = new SportBean();
            itemBean.setGid(gid);
            itemBean.setMid(matchId);
            itemBean.setHome(home);
            itemBean.setClient(guest);
            itemBean.setScoreC(home);
            itemBean.setScoreH(guest);
            if (i == 0) {
                itemBean.setPeilv(item.containsKey("ior_MC")?(String)item.get("ior_MC"):"");
                itemBean.setPeilvKey("ior_MC");
            } else if (i == 1) {
                itemBean.setPeilv(item.containsKey("ior_RC")?(String)item.get("ior_RC"):"");
                itemBean.setTxt(item.containsKey("CON_RC")?(String)item.get("CON_RC"):"");
                itemBean.setPeilvKey("ior_RC");
                itemBean.setProject(item.containsKey("CON_RC")?(String)item.get("CON_RC"):"");
            } else if (i == 2) {
                itemBean.setPeilv(item.containsKey("ior_OUC")?(String)item.get("ior_OUC"):"");
                itemBean.setTxt("小"+(item.containsKey("CON_OUC")?(String)item.get("CON_OUC"):""));
                itemBean.setPeilvKey("ior_OUC");
                itemBean.setProject(item.containsKey("CON_OUC")?(String)item.get("CON_OUC"):"");
            }
            lists.add(itemBean);
        }

        return lists;
    }


}
