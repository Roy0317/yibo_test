package com.example.anuo.immodule.lottery;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.Lunbar;
import com.example.anuo.immodule.utils.SysConfig;
import com.example.anuo.immodule.view.LotteryHistoryResultWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.Utils.Utils;

public class LotteryUtils {

    public final static int VERSION_2 = 2;// 信用版本

    private static final String[] redBo = new String[]{"1", "2", "7", "8", "12", "13", "18", "19", "23",
            "24", "29", "30", "34", "35", "40", "45", "46"};
    private static final String[] blueBo = new String[]{"3", "4", "9", "10", "14", "15", "20", "25", "26",
            "31", "36", "37", "41", "42", "47", "48"};
    private static final String[] greenBo = new String[]{"5", "6", "11", "16", "17", "21", "22", "27", "28", "32", "33",
            "38", "39", "43", "44", "49"};

    /**
     * 根据号码找红绿蓝颜色
     *
     * @param number
     * @return 1-红 2-绿 3-蓝
     */
    public static int findBoseFromNum(String number) {

        //如果返回的数组以0开头，例如"01"，"02" 这种，把0拿掉，只留下"1"，"2"
        if (number.startsWith("0")) {
            number = number.substring(1, 2);
        }
        if (Arrays.asList(redBo).contains(number)) {
            return 1;
        } else if (Arrays.asList(greenBo).contains(number)) {
            return 2;
        } else if (Arrays.asList(blueBo).contains(number)) {
            return 3;
        } else
            return 0;
    }

    /**
     * @param context
     * @param zero    数字小于10时，是否需要0 占位 1- 01
     * @return
     */
    public static String[] getNumbersFromShengXiao(Context context, boolean zero) {
        String
                shenxiao = getShengXiaoFromYear();
        String[] arr = context.getResources().getStringArray(R.array.shengxiao_str);
        String[] results = new String[arr.length];
        int bmnIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item.equals(shenxiao)) {
                bmnIndex = i;
                break;
            }
        }
        Utils.LOG("a", "bmnindex = " + bmnIndex);
        for (int i = 0; i < arr.length; i++) {
            int startNum = 0;
            if (i <= bmnIndex) {
                startNum = (bmnIndex - i) + 1;
            } else {
                startNum = (i - bmnIndex) - 1;
                startNum = 12 - startNum;
            }
            String numResult = arr[i] + "|" + getShenxiaoNumbers(startNum, zero);
            results[i] = numResult;

        }
        return results;
    }

    //计算某年所属生肖
    public static String getShengXiaoFromYear() {
        Lunbar lunbar = new Lunbar(Calendar.getInstance());
        String sx = lunbar.animalsYear();
        return sx;
    }

    public static String getShenxiaoNumbers(int startIndex, boolean zero) {
        int maxValue = 49;
        int tmp = startIndex;
        StringBuilder sb = new StringBuilder();
        while (tmp <= maxValue) {
            if (zero && tmp < 10) {
                sb.append("0" + tmp).append(",");
            } else
                sb.append(String.format("%d", tmp)).append(",");
            tmp += 12;
        }
        if (sb.length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static void renderKuai3BgForNumber(TextView tv, String number) {
        int n;
        try {
            n = Integer.parseInt(number);
        } catch (Exception e) {
            e.printStackTrace();
            n = 0;
        }
        if (n == 1) {
            tv.setBackgroundResource(R.drawable.kuai3_bg_one);
        } else if (n == 2) {
            tv.setBackgroundResource(R.drawable.kuai3_bg_two);
        } else if (n == 3) {
            tv.setBackgroundResource(R.drawable.kuai3_bg_three);
        } else if (n == 4) {
            tv.setBackgroundResource(R.drawable.kuai3_bg_four);
        } else if (n == 5) {
            tv.setBackgroundResource(R.drawable.kuai3_bg_five);
        } else if (n == 6) {
            tv.setBackgroundResource(R.drawable.kuai3_bg_six);
        } else {
//            tv.setBackgroundResource(R.drawable.kuai3_bg_empty_nottext);
        }
        if (n > 0 && n <= 6) {
            tv.setText("");
        } else {
            tv.setText(number);
        }
    }


    /**
     * 设置幸运农场，快十背景图片
     *
     * @param tv
     * @param num
     */
    public static void renderFarmBgForNumber(TextView tv, String num) {
        int n;
        try {
            n = Integer.parseInt(num);
        } catch (Exception e) {
            n = -1;
        }

        int resId = -1;
        switch (n) {
            case 1:
                resId = R.drawable.xync_01;
                break;
            case 2:
                resId = R.drawable.xync_02;
                break;
            case 3:
                resId = R.drawable.xync_03;
                break;
            case 4:
                resId = R.drawable.xync_04;
                break;
            case 5:
                resId = R.drawable.xync_05;
                break;
            case 6:
                resId = R.drawable.xync_06;
                break;
            case 7:
                resId = R.drawable.xync_07;
                break;
            case 8:
                resId = R.drawable.xync_08;
                break;
            case 9:
                resId = R.drawable.xync_09;
                break;
            case 10:
                resId = R.drawable.xync_10;
                break;
            case 11:
                resId = R.drawable.xync_11;
                break;
            case 12:
                resId = R.drawable.xync_12;
                break;
            case 13:
                resId = R.drawable.xync_13;
                break;
            case 14:
                resId = R.drawable.xync_14;
                break;
            case 15:
                resId = R.drawable.xync_15;
                break;
            case 16:
                resId = R.drawable.xync_16;
                break;
            case 17:
                resId = R.drawable.xync_17;
                break;
            case 18:
                resId = R.drawable.xync_18;
                break;
            case 19:
                resId = R.drawable.xync_19;
                break;
            case 20:
                resId = R.drawable.xync_20;
                break;
        }
        if (resId != -1)
            tv.setBackgroundResource(resId);
    }


    public static void renderBgForNumber(TextView tv, String number, Boolean isOptimise) {

        int color = findBoseFromNum(number);

        if (color == 1) {
            tv.setBackgroundResource(isOptimise ? R.drawable.bet_lhc_red : R.drawable.bet_lhc_red_old);
        } else if (color == 2) {
            tv.setBackgroundResource(isOptimise ? R.drawable.bet_lhc_green : R.drawable.bet_lhc_green_old);
        } else if (color == 3) {
            tv.setBackgroundResource(isOptimise ? R.drawable.bet_lhc_blue : R.drawable.bet_lhc_blue_old);
        } else {
            tv.setBackgroundResource(isOptimise ? R.drawable.bet_lhc_blue : R.drawable.bet_lhc_blue_old);
        }
//        Log.e(TAG, "color:" + color + ",number:" + number);
    }


    /**
     * 根据号码和指定的农历生肖年分，计算出号码对应的生肖
     *
     * @param number
     * @param date
     * @return
     */
    public static String getShenxiaoFromNumberAndDate(Context context, String number, long date) {
        String sx = "";
        if (date == 0) {
            sx = getShengXiaoFromYear();
        } else {
            sx = getShengXiaoFromDate(date);
        }
        String[] arr = context.getResources().getStringArray(R.array.shengxiao_str);
        int bmnIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item.equals(sx)) {
                bmnIndex = i;
                break;
            }
        }
        Utils.LOG("a", "bmnindex = " + bmnIndex);
        for (int i = 0; i < arr.length; i++) {
            int startNum = 0;
            if (i <= bmnIndex) {
                startNum = (bmnIndex - i) + 1;
            } else {
                startNum = (i - bmnIndex) - 1;
                startNum = 12 - startNum;
            }
            String numbers = getShenxiaoNumbers(startNum, true);
            String[] numArr = numbers.split(",");
            for (int j = 0; j < numArr.length; j++) {
                String n = numArr[j];
                if (n.equalsIgnoreCase(number) || n.equalsIgnoreCase("0" + number)) {
                    return arr[i];
                }
            }
        }
        return "";
    }

    /**
     * 根据指定日期获取农历生肖
     *
     * @param date
     * @return
     */
    public static String getShengXiaoFromDate(long date) {
        Calendar c = Calendar.getInstance();
        Date d = new Date(date);
        c.setTime(d);
        Lunbar lunbar = new Lunbar(c);
        String sx = lunbar.animalsYear();
        return sx;
    }

    public static boolean isSaiche(String cpBianHao) {
        if (Utils.isEmptyString(cpBianHao)) {
            return false;
        }
        if (cpBianHao.equalsIgnoreCase("BJSC") ||
                cpBianHao.equalsIgnoreCase("XYFT") ||
                cpBianHao.equalsIgnoreCase("FKSC") ||
                cpBianHao.equalsIgnoreCase("SFSC")) {
            return true;
        }
        return false;
    }


    /**
     * 是否六合彩彩种
     *
     * @param lotCode
     * @return
     */
    public static boolean isSixMark(String lotCode) {
        if (Utils.isEmptyString(lotCode)) {
            return false;
        }
        return lotCode.equalsIgnoreCase("LHC") ||
                lotCode.equalsIgnoreCase("SFLHC") ||
                lotCode.equalsIgnoreCase("WFLHC") ||
                lotCode.equalsIgnoreCase("FFLHC") ||
                lotCode.equalsIgnoreCase("SLHC");
    }



    public static boolean needCalcTotalDSDX(String lotCode) {
        String[] lotCodes = new String[]{"FFC", "BJSC", "CQSSC", "EFC", "WFC", "XYFT", "XJSSC",/*"JSSB3",*/"SFSC", /*老北京赛车*/ "LBJSC",
                /*老重庆时时彩*/
                "LCQSSC",
                "SFC",
                "TFC",
                "FF28"/*极速28*/,
                "AZWF"/*幸运五分彩*/,
                "XGFC",/*香港极速彩*/
                "AMFC",/**/
                "SF28",
                "WF28",
                "AZXYW168",/*澳洲幸运五168*/
                /*"HBK3","AHK3","GXK3","HEBK3","SHHK3",*/"JX11X5", "GD11X5", "SD11X5", "SH11X5", "PCEGG", "JND28", "FC3D", "PL3"/*,"GDKLSF",*/
                /*"HNKLSF","CQXYNC"*/};
        for (String code : lotCodes) {
            if (code.equals(lotCode)) {
                return true;
            }
        }
        return false;
    }

    public static int getMaxOpenNumFromLotCode(String lotCode, List<LotteryHistoryResultWindow.BallListItemInfo> numbers) {
        int totalNum = 0;
        if (isKuaileCai(lotCode)) {
            totalNum = 20 * numbers.size();//所有号码最高号码总和
        } else if (isSaiche2(lotCode)) {
            totalNum = 10 * 2;
        } else if (is11x5(lotCode)) {
            totalNum = 11 * numbers.size() + 1 * numbers.size();
        } else if (isSaiche2(lotCode)) {
            totalNum = 20;//冠亚和值
        } else if (is28(lotCode)) {
            totalNum = 27;
        } else {

            totalNum = 9 * numbers.size();//所有号码最高号码总和
        }
        return totalNum;
    }

    /**
     * 从彩票开奖历史纪录搬过来的版本
     * @param lotCode
     * @return
     */
    public static boolean isSaiche2(String lotCode) {
        if (lotCode.equals("SFSC") || lotCode.equals("XYFT") || lotCode.equals("BJSC") || lotCode.equals("LBJSC")) {
            return true;
        }
        return false;
    }

    public static boolean is11x5(String lotCode) {
        if (lotCode.contains("11X5")) {
            return true;
        }
        return false;
    }

    public static boolean isKuaileCai(String lotCode) {
        if (lotCode.equals("GDKLSF") || lotCode.equals("HNKLSF") || lotCode.equals("CQXYNC")) {
            return true;
        }
        return false;
    }

    public static boolean is28(String lotCode) {
        if (lotCode.equals("FF28") || lotCode.equals("SF28") || lotCode.equals("WF28")) {
            return true;
        }
        return false;
    }

    public static boolean isKuai3(String lotCode) {
        if (lotCode.equals("HBK3") || lotCode.equals("AHK3") || lotCode.equals("GXK3") ||
                lotCode.equals("HEBK3") || lotCode.equals("SHHK3")) {
            return true;
        }
        return false;
    }

    public static boolean isKuaiSan(String lotCode) {
        if (lotCode.endsWith("K3")) {
            return true;
        }
        return false;
    }

    public static List<LotteryHistoryResultWindow.BallListItemInfo> figureOutALLDXDS(
            Context context, List<LotteryHistoryResultWindow.BallListItemInfo> numbers, String lotCode) {

        if (numbers == null || numbers.isEmpty()) {
            return null;
        }

        int totalNum = getMaxOpenNumFromLotCode(lotCode, numbers);
        int total = 0;//当前开奖号码总和
        for (int i = 0; i < numbers.size(); i++) {
            LotteryHistoryResultWindow.BallListItemInfo info = numbers.get(i);
            if (!TextUtils.isEmpty(info.getNum()) && isNumeric(info.getNum())) {
                if (isSaiche2(lotCode)) {
                    if (i <= 1) {
                        total += Integer.parseInt(info.getNum());
                    }
                } else {
                    total += Integer.parseInt(info.getNum());
                }
            }
        }

        List<LotteryHistoryResultWindow.BallListItemInfo> appendList = new ArrayList<>();

        LotteryHistoryResultWindow.BallListItemInfo numInfo = new LotteryHistoryResultWindow.BallListItemInfo();
        numInfo.setNum(String.valueOf(total));
        appendList.add(numInfo);

        if (total >= 0) {
            //大或小
            if (is11x5(lotCode)) {
//                if (total >= totalNum / 2) {
                if (total > 30) {
                    LotteryHistoryResultWindow.BallListItemInfo bigInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                    bigInfo.setNum("大");
                    appendList.add(bigInfo);
                } else if (total == 30) {
                    LotteryHistoryResultWindow.BallListItemInfo bigInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                    bigInfo.setNum("和");
                    appendList.add(bigInfo);
                } else {
                    LotteryHistoryResultWindow.BallListItemInfo smallInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                    smallInfo.setNum("小");
                    appendList.add(smallInfo);
                }
            } else if (isSaiche2(lotCode)) {
                if (total > 11) {
                    LotteryHistoryResultWindow.BallListItemInfo bigInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                    bigInfo.setNum("大");
                    appendList.add(bigInfo);
                } else {
                    LotteryHistoryResultWindow.BallListItemInfo bigInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                    SysConfig config = CommonUtils.getConfigFromJson(context);
                    if ("on".equals(config.getPk10_guanyahe_11_heju())) {
                        bigInfo.setNum(total == 11 ? "和" : "小");
                    } else {
                        bigInfo.setNum("小");
                    }
                    appendList.add(bigInfo);
                }
            }
            else if (is28(lotCode)) {
                if (numbers.size() > 0) {
                    total = 0;
                    LotteryHistoryResultWindow.BallListItemInfo info = numbers.get(numbers.size() - 1);
                    if (!TextUtils.isEmpty(info.getNum()) && isNumeric(info.getNum())) {
                        total += Integer.parseInt(info.getNum());
                    }
                    if (total > totalNum / 2) {
                        LotteryHistoryResultWindow.BallListItemInfo bigInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                        bigInfo.setNum("大");
                        appendList.add(bigInfo);
                    } else {
                        LotteryHistoryResultWindow.BallListItemInfo smallInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                        smallInfo.setNum("小");
                        appendList.add(smallInfo);
                    }
                }
            } else {
                if (total > totalNum / 2) {
                    LotteryHistoryResultWindow.BallListItemInfo bigInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                    bigInfo.setNum("大");
                    appendList.add(bigInfo);
                } else {
                    LotteryHistoryResultWindow.BallListItemInfo smallInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                    smallInfo.setNum("小");
                    appendList.add(smallInfo);
                }
            }
            //单或双
            if (total % 2 == 0) {
                LotteryHistoryResultWindow.BallListItemInfo doubleInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                doubleInfo.setNum("双");
                appendList.add(doubleInfo);
            } else {
                LotteryHistoryResultWindow.BallListItemInfo singleInfo = new LotteryHistoryResultWindow.BallListItemInfo();
                singleInfo.setNum("单");
                appendList.add(singleInfo);
            }
        } else {
            //大或小
            LotteryHistoryResultWindow.BallListItemInfo smallInfo = new LotteryHistoryResultWindow.BallListItemInfo();
            smallInfo.setNum("无");
            appendList.add(smallInfo);
            //单或双
            LotteryHistoryResultWindow.BallListItemInfo dsInfo = new LotteryHistoryResultWindow.BallListItemInfo();
            dsInfo.setNum("无");
            appendList.add(dsInfo);
        }

        return appendList;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setListViewHeightBasedOnChildren(GridView gridView, int columnNum, int offset) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int count = listAdapter.getCount();
        if (count == 0) {
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = 0;
            gridView.setLayoutParams(params);
            return;
        }
        int hightCount = count / columnNum;
        if (hightCount == 0) {
            hightCount++;
        } else {
            if (count % columnNum > 0) {
                hightCount++;
            }
        }
        for (int i = 0, len = hightCount; i < len; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            // 4.4以下版本会报空指针异常（RelativeLayout布局）
            listItem.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + offset * hightCount;
        gridView.setLayoutParams(params);
    }
}
