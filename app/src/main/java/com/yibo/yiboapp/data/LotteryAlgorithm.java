package com.yibo.yiboapp.data;

import android.app.Fragment;
import android.content.Context;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.BallonRules;
import com.yibo.yiboapp.entify.PeilvWebResult;
import com.yibo.yiboapp.utils.Utils;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.jar.JarEntry;

import static android.R.id.list;
import static android.R.id.title;

/**
 * 各彩种的投注算法
 *
 * @author johnson
 */
public class LotteryAlgorithm {

    public static final String TAG = LotteryAlgorithm.class.getSimpleName();
    public static final int WEISHU_LENGTH = 5;
    public static final String WEISHU_STR = "万,千,百,十,个";

    public static List<String> getWeishuRandomCount(
            String version,String cpCode,String pcode,String rcode){
        if (version.equals("1") || version.equals("3")) {
            if (rcode.equals(PlayCodeConstants.rxwf_r3zux_zu3) ||
                    rcode.equals(PlayCodeConstants.rxwf_r3zux_zu6)) {
                return Utils.randomNumbers(WEISHU_STR, false, 3, ",");
            }
        }
        return null;
    }

    //时时彩-组选三，六
    private static List<PeilvData> calcPeilvZuxuan(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();

        List<PeilvPlayData> sub = new ArrayList<PeilvPlayData>();
        for (int i=0;i<10;i++) {
            PeilvPlayData data = new PeilvPlayData();
            data.setNumber(String.valueOf(i));
            data.setCheckbox(true);
            data.setPeilvData(webResults.get(0));//设置这个是为了每一个号码项都可以拿到基本的投注数据，
            // 如最小勾选数，最大投注金额等
            data.setAllDatas(webResults);
            sub.add(data);
        }
        PeilvData zh = new PeilvData();
        zh.setAppendTag(false);
        zh.setSubData(sub);
        list.add(zh);

        return list;
    }

    //pc 蛋蛋 ，加拿大28
    private static List<PeilvData> calcPC28(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> hunhe = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> bosebaozi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> tema = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {

            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("大")||name.equals("小")||
                        name.equals("单")||name.equals("双")||
                        name.equals("大单")||name.equals("大双")||
                        name.equals("小单")||name.equals("小双")||
                        name.equals("极大")||name.equals("极小")){

                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    hunhe.add(data);

                }else if (name.equals("红波")||name.equals("绿波")||
                        name.equals("蓝波")||name.equals("豹子")){

                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    bosebaozi.add(data);

                }
            } else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                String name = result.getName();
                for (int i=0;i<28;i++) {
                    if (name.equals(String.valueOf(i))) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(name);
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        tema.add(data);
                    }
                }
            }
        }
        //混合
        PeilvData sbdxItem = new PeilvData();
        sbdxItem.setTagName("混合");
        sbdxItem.setSubData(hunhe);
        //波色，豹子
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("波色，豹子");
        wanweiItem.setSubData(bosebaozi);
        //特码
        PeilvData dianShuItem = new PeilvData();
        dianShuItem.setTagName("特码");
        dianShuItem.setSubData(tema);

        list.add(sbdxItem);
        list.add(wanweiItem);
        list.add(dianShuItem);

        return list;
    }

    //快乐十分--单球1-8
    private static List<PeilvData> calcKuaile10Dangqiu18(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> qiushus = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                for (int i=1;i<=20;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%02d",i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    qiushus.add(data);
                }
            }
        }
        String[] arrItem = new String[]{"一", "二", "三", "四", "五", "六", "七", "八"};
        for (String arrtxt : arrItem) {
            PeilvData item = new PeilvData();
            item.setTagName("第"+arrtxt+"球");
            item.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(qiushus);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                item.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }

    //快乐十分--1，2，3，4，5，6，7，8球
    private static List<PeilvData> calcKuaile10WhichBallon(String qiuIndex,List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> shuangmapan = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("大")||name.equals("小")||
                        name.equals("单")||name.equals("双")||
                        name.equals("尾大")||name.equals("尾小")||
                        name.equals("合单")||name.equals("合双")||
                        name.equals("东")||name.equals("西")||
                        name.equals("南")||name.equals("北")||
                        name.equals("中")||name.equals("发")||
                        name.equals("白")){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuangmapan.add(data);
                }
            }else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=1;i<=20;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%02d",i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }

        //第几球
        PeilvData sbdxItem = new PeilvData();
        sbdxItem.setTagName("第"+qiuIndex+"球");
        sbdxItem.setSubData(shuzhi);
        //双面盘
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("第"+qiuIndex+"球,双面盘");
        wanweiItem.setSubData(shuangmapan);

        list.add(sbdxItem);
        list.add(wanweiItem);

        return list;
    }

    //快乐十分--连码
    private static List<PeilvData> calcKuaile10Lianma(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuangmapan = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                for (int i=1;i<=20;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%02d",i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setCheckbox(true);
                    data.setPeilvData(webResults.get(0));//设置这个是为了每一个号码项都可以拿到基本的投注数据，
                    // 如最小勾选数，最大投注金额等
                    data.setAllDatas(webResults);
                    shuangmapan.add(data);
                }
            }
        }
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shuangmapan);

        list.add(wanweiItem);
        return list;
    }

    //赛车-冠亚军
    private static List<PeilvData> calcSaicheGyj(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuangmapan = new ArrayList<PeilvPlayData>();


        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(result.getName());
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                shuangmapan.add(data);
            }
        }
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("冠亚和");
        List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
        subList.addAll(shuangmapan);
        wanweiItem.setSubData(subList);
        list.add(wanweiItem);
        return list;

    }

    //赛车-单号1-10
    private static List<PeilvData> calcSaicheDanhao110(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        PeilvWebResult webResult = webResults.get(0);
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (int i=1;i<=10;i++) {
            PeilvPlayData data = new PeilvPlayData();
            data.setNumber(String.valueOf(i));
            data.setPeilv(String.valueOf(webResult.getOdds()));
            data.setPeilvData(webResult);
            shuzhi.add(data);
        }

        String[] array = new String[]{"冠军", "亚军", "季军"};
        for (String txt : array) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(txt);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<>();
            subList.addAll(shuzhi);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }

        String[] array2 = new String[]{"第四名", "第五名", "第六名","第七名", "第八名","第九名","第十名"};
        for (String txt : array2) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(txt);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(shuzhi);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }

        return list;
    }

    //赛车-双面盘
    private static List<PeilvData> calcSaicheSMP(List<PeilvWebResult> webResults) {


        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> hhhh = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxdslh = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxds = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();

                if (name.equals("和大") || name.equals("和小") ||
                        name.equals("和单") || name.equals("和双")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    hhhh.add(data);
                }

                if (name.equals("大") || name.equals("小") ||
                        name.equals("单") || name.equals("双")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dxds.add(data);
                    dxdslh.add(data);
                }

                if (name.equals("龙") || name.equals("虎")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dxdslh.add(data);
                }

            }
        }

        //冠,亚和
        PeilvData gyhItem = new PeilvData();
        gyhItem.setTagName("冠,亚和");
        gyhItem.setSubData(hhhh);
        list.add(gyhItem);

        String[] array = new String[]{"冠军", "亚军", "季军","第四名", "第五名"};
        for (String txt : array) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(txt);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(dxdslh);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }

        String[] array2 = new String[]{"第六名","第七名", "第八名","第九名","第十名"};
        for (String txt : array2) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(txt);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(dxds);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }

        return list;
    }

    //快乐十分--双面盘
    private static List<PeilvData> calcKuaile10smp(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> zhonghe = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> qiuDatas = new ArrayList<PeilvPlayData>();

        PeilvWebResult webResult = webResults.get(0);
        String arr[] = new String[]{"总大","总小","总单","总双","总尾大","总尾小"};
        for (String str : arr) {
            PeilvPlayData data = new PeilvPlayData();
            data.setNumber(str);
            data.setPeilv(String.valueOf(webResult.getOdds()));
            data.setPeilvData(webResult);
            zhonghe.add(data);
        }

        String arr2[] = new String[]{"大","小","单","双","尾大","尾小","合单","合双"};
        for (String str : arr2) {
            PeilvPlayData data = new PeilvPlayData();
            data.setNumber(str);
            data.setPeilv(String.valueOf(webResult.getOdds()));
            data.setPeilvData(webResult);
            qiuDatas.add(data);
        }

        //总和
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("总和");
        zhongheItem.setSubData(zhonghe);
        list.add(zhongheItem);

        //1~8球
        String[] arrQiu = new String[]{"一","二","三","四","五","六","七","八"};
        for (String str : arrQiu) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName("第"+str+"球");
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(qiuDatas);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }
        return list;
    }

    //十一选5--双面盘
    private static List<PeilvData> calc11x5smp(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> hhhhwwlh = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxds = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("和大") || name.equals("和小") ||
                        name.equals("和单") || name.equals("和双") ||
                        name.equals("尾大") || name.equals("尾小") ||
                        name.equals("龙") || name.equals("虎")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    hhhhwwlh.add(data);
                }

                if (name.equals("大") || name.equals("小") ||
                        name.equals("单") || name.equals("双")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dxds.add(data);
                }
            }
        }

        //总和
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("总和");
        zhongheItem.setSubData(hhhhwwlh);
        list.add(zhongheItem);

        //1~5球
        String[] balls = new String[]{"一","二","三","四","五"};
        for (String str : balls) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName("第"+str+"球");
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(dxds);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }
        return list;
    }

    //赔率11x5 直选
    private static List<PeilvData> calc11x5zhixuan(List<PeilvWebResult> webResults,String subcode) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> qiushus = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                for (int i=1;i<=11;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%02d",i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    data.setCheckbox(true);
                    data.setAllDatas(webResults);
                    qiushus.add(data);
                }
            }
        }

        String[] arrItem = null;
        if (subcode.equals(PlayCodeConstants.syx5_zhixuan_qianer)) {
            arrItem = new String[]{"一", "二"};
        }else if (subcode.equals(PlayCodeConstants.syx5_zhixuan_houer)) {
            arrItem = new String[]{"四", "五"};
        }else if (subcode.equals(PlayCodeConstants.syx5_zhixuan_qiansan)) {
            arrItem = new String[]{"一", "二","三"};
        }else if (subcode.equals(PlayCodeConstants.syx5_zhixuan_zhongsan)) {
            arrItem = new String[]{"二", "三","四"};
        }else if (subcode.equals(PlayCodeConstants.syx5_zhixuan_housan)) {
            arrItem = new String[]{"三", "四","五"};
        }

        if (arrItem == null) {
            return list;
        }
        for (String arrtxt : arrItem) {
            PeilvData item = new PeilvData();
            item.setTagName("第"+arrtxt+"球");
            item.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(qiushus);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                item.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }

    //赔率版 11x5 组选
    private static List<PeilvData> calc11x5zuxuan(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            for (int i=1;i<=11;i++) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(String.format("%02d",i));
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setCheckbox(true);
                data.setAllDatas(webResults);
                shuzhi.add(data);
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);

        return list;
    }

    //11x5 1-5球
    private static List<PeilvData> calc11x5Renxuan(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            for (int i=1;i<=11;i++) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(String.format("%02d",i));
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setCheckbox(true);
                data.setAllDatas(webResults);
                shuzhi.add(data);
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);

        return list;
    }

    //11x5 1-5球
    private static List<PeilvData> calc11x515(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=1;i<=11;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }

        //1~5球
        String[] balls = new String[]{"一","二","三","四","五"};
        for (String str : balls) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName("第"+str+"球");
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(shuzhi);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }
        return list;
    }

    //福彩3D,排列3
    private static List<PeilvData> calcPailie3(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxds = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                dxds.add(data);
            }else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<=9;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }

        //1~3球
        String[] balls = new String[]{"一","二","三"};
        for (String str : balls) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName("第"+str+"球");
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> myList = new ArrayList<PeilvPlayData>();
            myList.addAll(shuzhi);
            myList.addAll(dxds);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < myList.size(); i++) {
                    destList.add((PeilvPlayData) myList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }
        return list;
    }

    //排列三，住势盘
    private static List<PeilvData> calcpl3_zhushipan(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> qitashuzhi = new ArrayList<PeilvPlayData>();
        String[] numbers = new String[]{"佰", "拾", "个", "佰拾和尾数", "佰个和尾数", "拾个和尾数", "佰拾个和尾数"};
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                for (String item :numbers){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(item);
                    data.setAppendTag(true);
                    data.setAppendTagToTail(true);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    qitashuzhi.add(data);
                }
            }
        }

        String[] categorys = new String[]{"大","小","单","双","质","合"};
        for (String c : categorys) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(c);
            wanweiItem.setAppendTag(true);
            wanweiItem.setAppendTagToTail(true);
            List<PeilvPlayData> myList = new ArrayList<PeilvPlayData>();
            myList.addAll(qitashuzhi);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < myList.size(); i++) {
                    destList.add((PeilvPlayData) myList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }
        return list;
    }

    //排列三，一字组合
    private static List<PeilvData> calcpl3_yizizuhe(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> qitashuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> shuzhi = new ArrayList<>();

        String[] numbers = new String[]{"大","小","单","双","质","合"};
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                for (String item :numbers){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(item);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    qitashuzhi.add(data);
                }
            }else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i = 0;i<10; i++){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }

        String[] categorys = new String[]{"一字组合","佰","拾","个"};
        for (int j=0;j<categorys.length;j++) {
            String cc = categorys[j];
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(cc);
            if (j == 0) {
                wanweiItem.setAppendTag(false);
                List<PeilvPlayData> myList = new ArrayList<PeilvPlayData>();
                myList.addAll(shuzhi);
                List<PeilvPlayData> destList = new ArrayList<>();
                try {
                    for (int i = 0; i < myList.size(); i++) {
                        destList.add((PeilvPlayData) myList.get(i).clone());
                    }
                    wanweiItem.setSubData(destList);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }else{
                wanweiItem.setAppendTag(true);
                List<PeilvPlayData> myList = new ArrayList<PeilvPlayData>();
                myList.addAll(qitashuzhi);
                List<PeilvPlayData> destList = new ArrayList<>();
                try {
                    for (int i = 0; i < myList.size(); i++) {
                        destList.add((PeilvPlayData) myList.get(i).clone());
                    }
                    wanweiItem.setSubData(destList);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            list.add(wanweiItem);
        }
        return list;
    }

    //排列三，二字组合
    private static List<PeilvData> calcpl3_erzizuhe(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(result.getName());
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                shuzhi.add(data);
            }
        }
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);
        return list;
    }

    ////排列3 百十,佰个，拾个定位
    private static List<PeilvData> calcpl3_erzhidingwei(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<100;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%02d", i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    data.setAllDatas(webResults);
                    shuzhi.add(data);
                }
            }
        }
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);
        return list;
    }


    ////排列3 三字定位
    private static List<PeilvData> calcpl3_sanzhidingwei(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<1000;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%03d", i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    data.setAllDatas(webResults);
                    shuzhi.add(data);
                }
            }
        }
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);
        return list;
    }

    ////排列3 二字三字和数
    private static List<PeilvData> calcpl3_er_san_ziheshu(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<>();
        List<PeilvPlayData> qitashuzhi = new ArrayList<>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<10;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(result.getName());
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                qitashuzhi.add(data);
            }
        }
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setAppendTag(true);
        wanweiItem.setTagName("尾数");
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);

        PeilvData wanweiItem2 = new PeilvData();
        wanweiItem2.setAppendTag(false);
        wanweiItem2.setTagName("和数");
        wanweiItem2.setSubData(qitashuzhi);
        list.add(wanweiItem2);

        return list;
    }

    ////排列3 组选三，六
    private static List<PeilvData> calcpl3_zuxuan_san_liu(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<>();
        PeilvWebResult result = webResults.get(0);
        if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
            for (int i=0;i<10;i++) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(String.valueOf(i));
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setAllDatas(webResults);
                data.setCheckbox(true);
                shuzhi.add(data);
            }
        }
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);
        return list;
    }

    //排列3 整合
    private static List<PeilvData> calcPailie3zh(List<PeilvWebResult> webResults) {


        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> qitashuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> zzzzlhh = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> bsdbz = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("0") || name.equals("1") ||
                        name.equals("2") || name.equals("3") ||
                        name.equals("4") || name.equals("5") ||
                        name.equals("6") || name.equals("7") ||
                        name.equals("8") || name.equals("9")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    qitashuzhi.add(data);
                } else if (name.equals("总和大") || name.equals("总和小") ||
                        name.equals("总和单") || name.equals("总和双") ||
                        name.equals("龙") || name.equals("虎") ||
                        name.equals("和")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    zzzzlhh.add(data);
                } else if (name.equals("豹子") || name.equals("顺子") ||
                        name.equals("对子") || name.equals("半顺") ||
                        name.equals("杂六") ) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    bsdbz.add(data);
                }
            }else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<=9;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }

        //独胆
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("独胆");
        wanweiItem.setAppendTag(true);
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);
        //跨度
        PeilvData kuaduItem = new PeilvData();
        kuaduItem.setTagName("跨度");
        kuaduItem.setAppendTag(true);
        kuaduItem.setSubData(qitashuzhi);
        list.add(kuaduItem);
        //总和，龙虎
        PeilvData zhlhItem = new PeilvData();
        zhlhItem.setTagName("总和，龙虎");
        zhlhItem.setSubData(zzzzlhh);
        list.add(zhlhItem);
        //3连
        PeilvData slianItem = new PeilvData();
        slianItem.setTagName("3连");
        slianItem.setSubData(bsdbz);
        list.add(slianItem);

        return list;
    }

    //六合彩-特码
    private static List<PeilvData> calcLhcTema(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxdshhhhwwhll = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setAllDatas(webResults);
                dxdshhhhwwhll.add(data);
            }else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setAllDatas(webResults);
                shuzhi.add(data);
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);
        //第二项
        PeilvData kuaduItem = new PeilvData();
        kuaduItem.setTagName("");
        kuaduItem.setSubData(dxdshhhhwwhll);
        list.add(kuaduItem);

        return list;
    }

    //六合彩-正码
    private static List<PeilvData> calcLhcZhenma16(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> hhhh = new ArrayList<>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setAllDatas(webResults);
                hhhh.add(data);
            }
        }

        String[] array = new String[]{"正码一", "正码二", "正码三", "正码四", "正码五", "正码六"};
        for (String title : array) {
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(title);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> myList = new ArrayList<PeilvPlayData>();
            myList.addAll(hhhh);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < myList.size(); i++) {
                    destList.add((PeilvPlayData) myList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }
        return list;
    }


    //六合彩-正码
    private static List<PeilvData> calcLhcZhenma(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> hhhh = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setAllDatas(webResults);
                hhhh.add(data);
            }else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setAllDatas(webResults);
                shuzhi.add(data);
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);
        //第二项
        PeilvData kuaduItem = new PeilvData();
        kuaduItem.setTagName("");
        kuaduItem.setSubData(hhhh);
        list.add(kuaduItem);

        return list;
    }

    //六合彩-连码
    private static List<PeilvData> calcLhcLianma(List<PeilvWebResult> webResults) {


        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=1;i<=49;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%2d",i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setAllDatas(webResults);
                    data.setCheckbox(true);
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);

        return list;
    }

    //六合彩-特码半波
    private static List<PeilvData> calcLhcTemaBB(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                data.setAllDatas(webResults);
                shuzhi.add(data);
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shuzhi);
        list.add(wanweiItem);

        return list;
    }


    //六合彩-一肖尾数
    private static List<PeilvData> calcLhcYXWS(Context context,List<PeilvWebResult> webResults,long lhcServerTime) {


        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shengxiaoData = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> weishu0Data = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> weishu19Data = new ArrayList<PeilvPlayData>();

        Calendar CD = Calendar.getInstance();
        int year = CD.get(Calendar.YEAR) ;
//        String[] shengxiao = context.getResources().getStringArray(R.array.shengxiao_array);


//        String[] shengxiao = UsualMethod.getNumbersFromShengXiao(context);
        String[] shengxiao = null;
        if (lhcServerTime > 0) {
            shengxiao = UsualMethod.getNumbersFromShengXiaoBaseDate(context, lhcServerTime);
        }else {
            shengxiao = UsualMethod.getNumbersFromShengXiao(context);
        }

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHENGXIAO)) {
                if (result.getIsNowYear() != 1) {
                    for (String str : shengxiao) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(str.substring(0,1));
                        data.setHelpNumber(str.substring(2,str.length()));
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        shengxiaoData.add(data);
                    }
                }
            } else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_WEISHU)) {
                if (result.getIsNowYear() == 1) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber("0尾");
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    weishu0Data.add(data);
                }else{
                    for (int i=1;i<=9;i++) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(i+"尾");
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        weishu19Data.add(data);
                    }
                }
            }
        }

        //遍历赔率数据，将十二生肖中本命年的那个生肖赔率调整为本命年生肖的赔率
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHENGXIAO)) {
                if (result.getIsNowYear() == 1 && !shengxiaoData.isEmpty()) {
                    for (int i=0;i<shengxiaoData.size();i++) {
                        PeilvPlayData playData = shengxiaoData.get(i);

                        String sx = "";
                        if (lhcServerTime > 0) {
                            sx = UsualMethod.getShenxiaoFromDate(context, lhcServerTime);
                        }else{
                            sx = UsualMethod.getShengXiaoFromYear();
                        }
                        boolean nowYear = sx.equals(playData.getNumber());
                        if (nowYear) {
                            PeilvPlayData benmingniao = new PeilvPlayData();
                            benmingniao.setNumber(playData.getNumber());
                            benmingniao.setHelpNumber(playData.getHelpNumber());
                            benmingniao.setPeilv(String.valueOf(result.getOdds()));
                            benmingniao.setPeilvData(result);
                            shengxiaoData.set(i, benmingniao);
                        }
                    }
                    break;
                }
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shengxiaoData);
        list.add(wanweiItem);
        //第一项
        PeilvData weishuItem = new PeilvData();
        weishuItem.setTagName("");
        List<PeilvPlayData> weishuData = new ArrayList<PeilvPlayData>();
        weishuData.addAll(weishu0Data);
        weishuData.addAll(weishu19Data);
        weishuItem.setSubData(weishuData);
        list.add(weishuItem);

        return list;
    }

    //六合彩-一特码生肖
    private static List<PeilvData> calcLhcTemaShengXiao(Context context,List<PeilvWebResult> webResults,long lhcServerTime) {


        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shengxiaoData = new ArrayList<PeilvPlayData>();
        Calendar CD = Calendar.getInstance();
        int year = CD.get(Calendar.YEAR) ;
//        String[] shengxiao = context.getResources().getStringArray(R.array.shengxiao_array);
//        String[] shengxiao = UsualMethod.getNumbersFromShengXiao(context);

        String[] shengxiao = null;
        if (lhcServerTime > 0) {
            shengxiao = UsualMethod.getNumbersFromShengXiaoBaseDate(context, lhcServerTime);
        }else {
            shengxiao = UsualMethod.getNumbersFromShengXiao(context);
        }

        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHENGXIAO)) {
                if (result.getIsNowYear() != 1) {
                    for (String str : shengxiao) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(str.substring(0,1));
                        data.setHelpNumber(str.substring(2,str.length()));
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        data.setAllDatas(webResults);
                        shengxiaoData.add(data);
                    }
                }
            }
        }

        //遍历赔率数据，将十二生肖中本命年的那个生肖赔率调整为本命年生肖的赔率
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHENGXIAO)) {
                if (result.getIsNowYear() == 1 && !shengxiaoData.isEmpty()) {
                    for (int i=0;i<shengxiaoData.size();i++) {
                        PeilvPlayData playData = shengxiaoData.get(i);

                        String sx = "";
                        if (lhcServerTime > 0) {
                            sx = UsualMethod.getShenxiaoFromDate(context, lhcServerTime);
                        }else{
                            sx = UsualMethod.getShengXiaoFromYear();
                        }
                        boolean nowYear = sx.equals(playData.getNumber());
                        if (nowYear) {
                            PeilvPlayData benmingniao = new PeilvPlayData();
                            benmingniao.setNumber(playData.getNumber());
                            benmingniao.setHelpNumber(playData.getHelpNumber());
                            benmingniao.setPeilv(String.valueOf(result.getOdds()));
                            benmingniao.setPeilvData(result);
                            benmingniao.setAllDatas(webResults);
                            shengxiaoData.set(i, benmingniao);
                        }
                    }
                    break;
                }
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shengxiaoData);
        list.add(wanweiItem);

        return list;
    }

    //六合彩-合肖,连肖
    private static List<PeilvData> calcLhcHexiaoLianXiao(Context context,List<PeilvWebResult> webResults,long lhcServerTime) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shengxiaoData = new ArrayList<PeilvPlayData>();
        Calendar CD = Calendar.getInstance();
        int year = CD.get(Calendar.YEAR) ;
//        String[] shengxiao = context.getResources().getStringArray(R.array.shengxiao_array);
//        String[] shengxiao = UsualMethod.getNumbersFromShengXiao(context);

        String[] shengxiao = null;
        if (lhcServerTime > 0) {
            shengxiao = UsualMethod.getNumbersFromShengXiaoBaseDate(context, lhcServerTime);
        }else {
            shengxiao = UsualMethod.getNumbersFromShengXiao(context);
        }


        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHENGXIAO)) {
                if (result.getIsNowYear() != 1) {
                    for (String str : shengxiao) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(str.substring(0,1));
                        data.setHelpNumber(str.substring(2,str.length()));
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        data.setAllDatas(webResults);
                        data.setCheckbox(true);
                        shengxiaoData.add(data);
                    }
                }
            }
        }

        //遍历赔率数据，将十二生肖中本命年的那个生肖赔率调整为本命年生肖的赔率
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHENGXIAO)) {
                if (result.getIsNowYear() == 1 && !shengxiaoData.isEmpty()) {
                    for (int i=0;i<shengxiaoData.size();i++) {
                        PeilvPlayData playData = shengxiaoData.get(i);

                        String sx = "";
                        if (lhcServerTime > 0) {
                            sx = UsualMethod.getShenxiaoFromDate(context, lhcServerTime);
                        }else{
                            sx = UsualMethod.getShengXiaoFromYear();
                        }
                        boolean nowYear = sx.equals(playData.getNumber());
                        if (nowYear) {
                            PeilvPlayData benmingniao = new PeilvPlayData();
                            benmingniao.setNumber(playData.getNumber());
                            benmingniao.setHelpNumber(playData.getHelpNumber());
                            benmingniao.setPeilv(String.valueOf(result.getOdds()));
                            benmingniao.setPeilvData(result);
                            benmingniao.setCheckbox(true);
                            benmingniao.setAllDatas(webResults);
                            shengxiaoData.set(i, benmingniao);
                        }
                    }
                    break;
                }
            }
        }

        //第一项
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("");
        wanweiItem.setSubData(shengxiaoData);
        list.add(wanweiItem);

        return list;
    }

    //六合彩--尾数连
    private static List<PeilvData> calcLhcWsl(Context context,List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> weishu0Data = new ArrayList<>();
        List<PeilvPlayData> weishu19Data = new ArrayList<>();

        String[] weishu = context.getResources().getStringArray(R.array.weishu_array);
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_WEISHU)) {
                if (result.getIsNowYear() == 1) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber("0尾");
                    data.setHelpNumber(weishu[0]);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    data.setAllDatas(webResults);
                    data.setCheckbox(true);
                    weishu0Data.add(data);
                }else{
                    for (int i=1;i<=9;i++) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(i+"尾");
                        data.setHelpNumber(weishu[i]);
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        data.setAllDatas(webResults);
                        data.setCheckbox(true);
                        weishu19Data.add(data);
                    }
                }
            }
        }

        //第一项
        PeilvData weishuItem = new PeilvData();
        weishuItem.setTagName("");
        List<PeilvPlayData> weishuData = new ArrayList<>();
        weishuData.addAll(weishu0Data);
        weishuData.addAll(weishu19Data);
        weishuItem.setSubData(weishuData);
        list.add(weishuItem);

        return list;
    }

    //六合彩-全不中
    private static List<PeilvData> calcLhcQbz(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=1;i<=49;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.format("%02d",i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    data.setAllDatas(webResults);
                    data.setCheckbox(true);
                    shuzhi.add(data);
                }
            }
        }
        //数字项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("");
        zhongheItem.setSubData(shuzhi);

        list.add(zhongheItem);
        return list;
    }

    //快三--骰宝
    private static List<PeilvData> calcSaibao(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> sbdx = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> weisai = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> quansai = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dianshu = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> changpai = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> duanpai = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {

            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("大小骰宝")){
                    String arr = "1,2,3,4,5,6,大,小,单,双";
                    String[] arrs = arr.split(",");
                    for (String str : arrs) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(str);
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        sbdx.add(data);
                    }
                }else if (name.equals("围骰")){
                    String[] arr = new String[]{"1-1-1", "2-2-2", "3-3-3", "4-4-4", "5-5-5", "6-6-6"};
                    for (String str : arr) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(str);
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        weisai.add(data);
                    }
                }else if (name.equals("全骰")){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber("全骰");
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    quansai.add(data);
                }else if (name.equals("4点")||name.equals("5点")||
                        name.equals("6点")||name.equals("7点")||
                        name.equals("8点")||name.equals("9点")||
                        name.equals("10点")||name.equals("11点")||
                        name.equals("12点")||name.equals("13点")||
                        name.equals("14点")||name.equals("15点")||
                        name.equals("16点")||name.equals("17点")){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dianshu.add(data);
                }else if (name.equals("长牌")){
                    String[] arr = new String[]{"1-2","1-3","1-4","1-5","1-6","2-3","2-4","2-5","2-6",
                            "3-4","3-5","3-6","4-5","4-6","5-6"};
                    for (String str : arr) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(str);
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        changpai.add(data);
                    }
                }else if (name.equals("短牌")){
                    String[] arr = new String[]{"1-1","2-2","3-3","4-4","5-5","6-6"};
                    for (String str : arr) {
                        PeilvPlayData data = new PeilvPlayData();
                        data.setNumber(str);
                        data.setPeilv(String.valueOf(result.getOdds()));
                        data.setPeilvData(result);
                        duanpai.add(data);
                    }
                }
            }
        }
        //骰宝，大小
        PeilvData sbdxItem = new PeilvData();
        sbdxItem.setTagName("骰宝，大小");
        sbdxItem.setSubData(sbdx);
        //围骰，全骰
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName("围骰，全骰");
        List<PeilvPlayData> myList = new ArrayList<PeilvPlayData>();
        myList.addAll(weisai);
        myList.addAll(quansai);
        wanweiItem.setSubData(myList);
        //点数
        PeilvData dianShuItem = new PeilvData();
        dianShuItem.setTagName("点数");
        dianShuItem.setSubData(dianshu);
        //长牌
        PeilvData changpaiItem = new PeilvData();
        changpaiItem.setTagName("长牌");
        changpaiItem.setSubData(changpai);
        //短牌
        PeilvData duanpaiItem = new PeilvData();
        duanpaiItem.setTagName("短牌");
        duanpaiItem.setSubData(duanpai);

        list.add(sbdxItem);
        list.add(wanweiItem);
        list.add(dianShuItem);
        list.add(changpaiItem);
        list.add(duanpaiItem);

        return list;
    }

    ////时时彩-整合
    private static List<PeilvData> calcPeilvZhenhe(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> subZhongHe = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxdszh = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> qita = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {

            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("总和大")||name.equals("总和小")||
                        name.equals("总和单")||name.equals("总和双")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    subZhongHe.add(data);
                }else if (name.equals("大")||name.equals("小")||
                        name.equals("单")||name.equals("双")||
                        name.equals("质")||name.equals("合")){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dxdszh.add(data);
                }else if (name.equals("豹子")||name.equals("顺子")||
                        name.equals("对子")||name.equals("半顺")||
                        name.equals("杂六")){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    qita.add(data);
                }
            } else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<10;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }
        //总和项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("总和");
        zhongheItem.setAppendTag(false);
        zhongheItem.setSubData(subZhongHe);
        list.add(zhongheItem);


        String[] arr = new String[]{"万位","仟位","佰位","拾位","个位"};
        for (String str : arr){
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(str);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> wanweiList = new ArrayList<PeilvPlayData>();
            wanweiList.addAll(shuzhi);
            wanweiList.addAll(dxdszh);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < wanweiList.size(); i++) {
                    destList.add((PeilvPlayData) wanweiList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }

        String[] arr2 = new String[]{"前三", "中三", "后三"};
        for (String str : arr2){
            PeilvData wanweiItem = new PeilvData();
            wanweiItem.setTagName(str);
            wanweiItem.setAppendTag(true);
            List<PeilvPlayData> wanweiList = new ArrayList<PeilvPlayData>();
            wanweiList.addAll(qita);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < wanweiList.size(); i++) {
                    destList.add((PeilvPlayData) wanweiList.get(i).clone());
                }
                wanweiItem.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(wanweiItem);
        }


        return list;
    }

    //时时彩-和尾数
    private static List<PeilvData> calcPeilvHeweishu(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> dxdszh = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String arr[] = new String[]{"大","小","单","双","质","合"};
                for (String name : arr) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dxdszh.add(data);
                }
            }
        }

        String tags = "万仟,万佰,万拾,万个,仟佰,仟拾,仟个,佰拾,佰个,拾个,前三,中三,后三";
        List<String> tagList = Utils.splitString(tags,",");
        for (String tag : tagList) {
            PeilvData item = new PeilvData();
            item.setTagName(tag);
            item.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(dxdszh);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                item.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }


    //时时彩-龙虎斗
    private static List<PeilvData> calcPeilvlhd(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> lhd = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(result.getName());
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                lhd.add(data);
            }
        }
        String tags = "万仟,万佰,万拾,万个,仟佰,仟拾,仟个,佰拾,佰个,拾个";
        List<String> tagList = Utils.splitString(tags,",");
        for (String tag : tagList) {
            PeilvData item = new PeilvData();
            item.setTagName(tag);
            item.setAppendTag(true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(lhd);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                item.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }


    //时时彩-棋牌-百家乐
    private static List<PeilvData> calcPeilvBaijiale(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> zxhzx = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxdszh = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {

            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("庄")||name.equals("闲")||
                        name.equals("和局")||name.equals("庄对")||
                        name.equals("闲对")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    zxhzx.add(data);
                }else if (name.equals("大")||name.equals("小")||
                        name.equals("单")||name.equals("双")||
                        name.equals("质")||name.equals("合")){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dxdszh.add(data);
                }
            }
        }

        String tags = ",庄,闲";
        List<String> tagList = Utils.splitString(tags,",");
        for (String tag : tagList) {
            PeilvData item = new PeilvData();
            item.setTagName(tag);
            item.setAppendTag(Utils.isEmptyString(tag) ? false : true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(Utils.isEmptyString(tag) ? zxhzx : dxdszh);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                item.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(item);
        }

//        //第一项
//        PeilvData firstItem = new PeilvData();
//        firstItem.setTagName("");
//        firstItem.setAppendTag(false);
//        firstItem.setSubData(zxhzx);
//        //庄
//        PeilvData zdItem = new PeilvData();
//        zdItem.setTagName("庄");
//        zdItem.setAppendTag(true);
//        zdItem.setSubData(dxdszh);
//        //闲
//        PeilvData xdItem = new PeilvData();
//        xdItem.setTagName("闲");
//        xdItem.setAppendTag(true);
//        List<PeilvPlayData> items = new ArrayList<>();
//        items.addAll(dxdszh);
//        xdItem.setSubData(items);

//        list.add(firstItem);
//        list.add(zdItem);
//        list.add(xdItem);
        return list;
    }

    //时时彩-和数-前，中，后三
    private static List<PeilvData> calcPeilvheshu(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(result.getName());
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                shuzi.add(data);
            }
        }
        //数字项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("");
        zhongheItem.setAppendTag(false);
        zhongheItem.setSubData(shuzi);

        list.add(zhongheItem);

        return list;
    }

    //时时彩-跨度
    private static List<PeilvData> calcFFCKuadu(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(result.getName());
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                shuzhi.add(data);
            }
        }
        //数字项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("");
        zhongheItem.setSubData(shuzhi);

        list.add(zhongheItem);
        return list;
    }

    //时时彩-一字
    private static List<PeilvData> calcPeilvYiZhi(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<10;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }
        //数字项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("");
        zhongheItem.setAppendTag(false);
        zhongheItem.setSubData(shuzhi);

        list.add(zhongheItem);
        return list;
    }

    //时时彩-二字，三字
    private static List<PeilvData> calcPeilvErSangZhi(List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(result.getName());
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                shuzhi.add(data);
            }
        }
        //数字项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("");
        zhongheItem.setAppendTag(false);
        zhongheItem.setSubData(shuzhi);

        list.add(zhongheItem);

        return list;
    }

    //时时彩-二字，三字定位
    private static List<PeilvData> calcPeilvErSangZhiDingWei(List<PeilvWebResult> webResults,int max) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<max;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    if (max == 100) {
                        data.setNumber(String.format("%02d",i));
                    } else if (max == 1000) {
                        data.setNumber(String.format("%03d",i));
                    }
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }
        //数字项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("");
        zhongheItem.setAppendTag(false);
        zhongheItem.setSubData(shuzhi);

        list.add(zhongheItem);

        return list;
    }

    //时时彩-万千百十个位
    private static List<PeilvData> calcPeilvWeiItem(String tagName,String postTagName,List<PeilvWebResult> webResults) {

        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> zzzzlhhdxdszh = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> shuzhi = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {

            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("总和大")||name.equals("总和小")||
                        name.equals("总和单")||name.equals("总和双")||
                        name.equals("大")||name.equals("小")||
                        name.equals("单")||name.equals("双")||
                        name.equals("质")||name.equals("合")||
                        name.equals("龙")||name.equals("虎")||
                        name.equals("和")) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(result.getName());
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    if (name.equals("大")||name.equals("小")||
                            name.equals("单")||name.equals("双")||
                            name.equals("质")||name.equals("合")) {
                        data.setFilterSpecialSuffix(true);
                    }
                    zzzzlhhdxdszh.add(data);
                }
            } else if (result.getMarkType().equals(PeilvParser.MARK_TYPE_SHUZI)) {
                for (int i=0;i<10;i++) {
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(String.valueOf(i));
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    shuzhi.add(data);
                }
            }
        }
        //数字项
        PeilvData zhongheItem = new PeilvData();
        zhongheItem.setTagName("");
        zhongheItem.setAppendTag(false);
        zhongheItem.setSubData(shuzhi);
        //万位
        PeilvData wanweiItem = new PeilvData();
        wanweiItem.setTagName(tagName);
        wanweiItem.setPostTagName(postTagName);
        wanweiItem.setAppendTag(true);
        wanweiItem.setSubData(zzzzlhhdxdszh);

        list.add(zhongheItem);
        list.add(wanweiItem);

        return list;
    }

    //针对赔率版算出面板中显示的球列表
    public static List<PeilvData> figurePeilvOutPlayInfo(Context context,
            String cpVersion, String cpCode, String pcode,
            String rcode, int pageIndex, int pageSize) {
        return figurePeilvOutPlayInfo(context,cpVersion, cpCode, pcode, rcode, pageIndex, pageSize, null,false,0);
    }

    //针对赔率版算出面板中显示的球列表
    public static List<PeilvData> figurePeilvOutPlayInfo(Context context,
            String cpVersion, String cpCode,String pcode,
            String rcode,int pageIndex,int pageSize,
            List<PeilvWebResult> webResults,boolean appendData,long lhcServerTime) {

        List<PeilvData> datas = null;

        boolean isPeilv = cpVersion.equals(String.valueOf(Constant.lottery_identify_V2)) ||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V4))||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V5));
        if (isPeilv) {
            //时时彩
            if (cpCode.equals("9")) {
                if (pcode.equals(PlayCodeConstants.zhenghe)){
                    datas = calcPeilvZhenhe(webResults);
                } else if (pcode.equals(PlayCodeConstants.wanwei)) {
                    datas = calcPeilvWeiItem("万位","万仟",webResults);
                } else if (pcode.equals(PlayCodeConstants.qianwei)) {
                    datas = calcPeilvWeiItem("千位","仟佰",webResults);
                } else if (pcode.equals(PlayCodeConstants.baiwei)) {
                    datas = calcPeilvWeiItem("百位","佰拾",webResults);
                } else if (pcode.equals(PlayCodeConstants.shiwei)) {
                    datas = calcPeilvWeiItem("十位","拾个",webResults);
                } else if (pcode.equals(PlayCodeConstants.gewei)) {
                    datas = calcPeilvWeiItem("个位","个万",webResults);
                } else if (pcode.equals(PlayCodeConstants.heweishu)) {
                    datas = calcPeilvHeweishu(webResults);
                } else if (pcode.equals(PlayCodeConstants.longhh)) {
                    datas = calcPeilvlhd(webResults);
                } else if (pcode.equals(PlayCodeConstants.qipai)) {
                    if (rcode.equals(PlayCodeConstants.baijiale)) {
                        datas = calcPeilvBaijiale(webResults);
                    } else if (rcode.equals(PlayCodeConstants.niuniu)) {
                        datas = calcPeilvNiuniu(webResults);
                    } else if (rcode.equals(PlayCodeConstants.dezhoupuke)) {
                        datas = calcPeilvdzpk(webResults);
                    } else if (rcode.equals(PlayCodeConstants.sangong)) {
                        datas = calcPeilvSanggong(webResults);
                    }
                } else if (pcode.equals(PlayCodeConstants.heshu)){
                    datas = calcPeilvheshu(webResults);
                } else if (pcode.equals(PlayCodeConstants.yizi)) {
                    datas = calcPeilvYiZhi(webResults);
                } else if (pcode.equals(PlayCodeConstants.erzi)) {
                    datas = calcPeilvErSangZhi(webResults);
                } else if (pcode.equals(PlayCodeConstants.sanzi)) {
                    datas = calcPeilvErSangZhi(webResults);
                } else if (pcode.equals(PlayCodeConstants.erzidingwei)) {
                    datas = calcPeilvErSangZhiDingWei(webResults,100);
                } else if (pcode.equals(PlayCodeConstants.sanzidingwei)) {
                    datas = calcPeilvErSangZhiDingWei(webResults,1000);
                } else if (pcode.equals(PlayCodeConstants.zuxuan_san_peilv)) {
                    datas = calcPeilvZuxuan(webResults);
                } else if (pcode.equals(PlayCodeConstants.zuxuan_liu_peilv)) {
                    datas = calcPeilvZuxuan(webResults);
                } else if (pcode.equals(PlayCodeConstants.kuadu)) {
                    datas = calcFFCKuadu(webResults);
                }
            }
            //快三
            else if (cpCode.equals("10")){
                if (pcode.equals(PlayCodeConstants.daxiaoshaibao)){
                    datas = calcSaibao(webResults);
                }
            }
            //pc蛋蛋，加拿大28
            else if (cpCode.equals("11")) {
                datas = calcPC28(webResults);
            }
            //重庆幸运农场,湖南快乐十分,广东快乐十分
            else if (cpCode.equals("12")){
                if (pcode.equals(PlayCodeConstants.danqiu1_8)) {
                    datas = calcKuaile10Dangqiu18(webResults);
                }else if (pcode.equals(PlayCodeConstants.diyiqiu)){
                    datas = calcKuaile10WhichBallon("一",webResults);
                }else if (pcode.equals(PlayCodeConstants.dierqiu)){
                    datas = calcKuaile10WhichBallon("二",webResults);
                }else if (pcode.equals(PlayCodeConstants.disanqiu)){
                    datas = calcKuaile10WhichBallon("三",webResults);
                }else if (pcode.equals(PlayCodeConstants.disiqiu)){
                    datas = calcKuaile10WhichBallon("四",webResults);
                }else if (pcode.equals(PlayCodeConstants.diwuqiu)){
                    datas = calcKuaile10WhichBallon("五",webResults);
                }else if (pcode.equals(PlayCodeConstants.diliuqiu)){
                    datas = calcKuaile10WhichBallon("六",webResults);
                }else if (pcode.equals(PlayCodeConstants.diqiqiu)){
                    datas = calcKuaile10WhichBallon("七",webResults);
                }else if (pcode.equals(PlayCodeConstants.dibaqiu)){
                    datas = calcKuaile10WhichBallon("八",webResults);
                }else if (pcode.equals(PlayCodeConstants.lianma_peilv_klsf)){
                    datas = calcKuaile10Lianma(webResults);
                }else if (pcode.equals(PlayCodeConstants.shuangmianpan)){
                    datas = calcKuaile10smp(webResults);
                }
            }
            //极速赛车，北京赛车，幸运飞艇
            else if (cpCode.equals("8")) {
                if (pcode.equals(PlayCodeConstants.guan_yajun)) {
                    datas = calcSaicheGyj(webResults);
                } else if (pcode.equals(PlayCodeConstants.danhao1_10)) {
                    datas = calcSaicheDanhao110(webResults);
                } else if (pcode.equals(PlayCodeConstants.shuangmianpan)) {
                    datas = calcSaicheSMP(webResults);
                }
            }
            //11选5
            if (cpCode.equals("14")) {
                if (pcode.equals(PlayCodeConstants.syx5_shuangmianpan)) {
                    datas = calc11x5smp(webResults);
                } else if (pcode.equals(PlayCodeConstants.syx5_15qiu)) {
                    datas = calc11x515(webResults);
                } else if (pcode.equals(PlayCodeConstants.syx5_renxuan)) {
                    datas = calc11x5Renxuan(webResults);
                } else if (pcode.equals(PlayCodeConstants.syx5_zuxuan)) {
                    datas = calc11x5zuxuan(webResults);
                } else if (pcode.equals(PlayCodeConstants.syx5_zhixuan)) {
                    datas = calc11x5zhixuan(webResults,rcode);
                }
            }
            //福彩3D，排列三
            else if (cpCode.equals("15")) {
                if (pcode.equals(PlayCodeConstants.pl3_13qiu)) {
                    datas = calcPailie3(webResults);
                } else if (pcode.equals(PlayCodeConstants.pl3_zhenghe)) {
                    datas = calcPailie3zh(webResults);
                }else if (pcode.equalsIgnoreCase(PlayCodeConstants.pl3_zhushipan)) {
                    datas = calcpl3_zhushipan(webResults);
                } else if (pcode.equalsIgnoreCase(PlayCodeConstants.pl3_yizizuhe)) {
                    datas = calcpl3_yizizuhe(webResults);
                } else if (pcode.equalsIgnoreCase(PlayCodeConstants.pl3_erzizuhe)) {
                    datas = calcpl3_erzizuhe(webResults);
                } else if (pcode.equalsIgnoreCase(PlayCodeConstants.pl3_sanzizuhe)) {
                    datas = calcpl3_erzizuhe(webResults);
                } else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_baishidingwei)) {
                    datas = calcpl3_erzhidingwei(webResults);
                } else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_baigedingwei)) {
                    datas = calcpl3_erzhidingwei(webResults);
                } else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_shigedingwei)) {
                    datas = calcpl3_erzhidingwei(webResults);
                } else if (pcode.equalsIgnoreCase(PlayCodeConstants.pl3_sanzidingwei)) {
                    datas = calcpl3_sanzhidingwei(webResults);
                }else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_baishiheshu)) {
                    datas = calcpl3_er_san_ziheshu(webResults);
                }else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_baigeheshu)) {
                    datas = calcpl3_er_san_ziheshu(webResults);
                }else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_shigeheshu)) {
                    datas = calcpl3_er_san_ziheshu(webResults);
                }else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_sanziheshu)) {
                    datas = calcpl3_er_san_ziheshu(webResults);
                }else if (rcode.equalsIgnoreCase(PlayCodeConstants.pl3_zuxuansan) ||
                        rcode.equalsIgnoreCase(PlayCodeConstants.pl3_zuxuanliu)) {
                    datas = calcpl3_zuxuan_san_liu(webResults);
                }
            }
            //六合彩，十分六合彩
            else if (cpCode.equals("6") || cpCode.equals("66")) {
                if (pcode.equals(PlayCodeConstants.tema)) {
                    datas = calcLhcTema(webResults);
                }else if (pcode.equals(PlayCodeConstants.zhentema)) {
                    datas = calcLhcTema(webResults);
                }else if (pcode.equals(PlayCodeConstants.zhenma)) {
                    datas = calcLhcZhenma(webResults);
                } else if (pcode.equals(PlayCodeConstants.zhenma16)) {
                    datas = calcLhcZhenma16(webResults);
                } else if (pcode.equals(PlayCodeConstants.lianma)) {
                    datas = calcLhcLianma(webResults);
                } else if (pcode.equals(PlayCodeConstants.bb)) {
                    datas = calcLhcTemaBB(webResults);
                } else if (pcode.equals(PlayCodeConstants.yixiao_weishu)) {
                    datas = calcLhcYXWS(context,webResults,lhcServerTime);
                } else if (pcode.equals(PlayCodeConstants.txsm)) {
                    datas = calcLhcTemaShengXiao(context,webResults,lhcServerTime);
                } else if (pcode.equals(PlayCodeConstants.lianxiao)) {
                    datas = calcLhcHexiaoLianXiao(context,webResults,lhcServerTime);
                } else if (pcode.equals(PlayCodeConstants.hexiao)) {
                    datas = calcLhcHexiaoLianXiao(context,webResults,lhcServerTime);
                } else if (pcode.equals(PlayCodeConstants.quanbuzhong)) {
                    datas = calcLhcQbz(webResults);
                } else if (pcode.equals(PlayCodeConstants.weishulian)) {
                    datas = calcLhcWsl(context,webResults);
                }
            }

        }
        //分开获取数据，pageIndex 从1 开始
        if (datas != null && appendData){
            List<PeilvData> newDatas = new ArrayList<>();
            for (int i=0;i<datas.size();i++) {
                PeilvData d = datas.get(i);
                if (d == null) {
                    continue;
                }
                List<PeilvPlayData> subData = d.getSubData();
                if (subData != null) {
                    if (pageIndex < 1) {
                        throw new IllegalStateException("the page index must start from 1");
                    }
                    int totalSize = subData.size();
                    if (totalSize >= pageIndex*pageSize) {
                        subData = subData.subList((pageIndex-1)*pageSize, (pageIndex) * pageSize);
                    }else{
                        if (totalSize > (pageIndex - 1)*pageSize && totalSize < pageIndex*pageSize){
                            subData = subData.subList((pageIndex-1)*pageSize, (totalSize));
                        }else{
                            subData = new ArrayList<PeilvPlayData>();
                        }
                    }
                    d.setSubData(subData);
                }
                newDatas.add(d);
            }
            return newDatas;
        }
        return  datas;
    }

    //时时彩-棋牌-牛牛
    private static List<PeilvData> calcPeilvNiuniu(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> niu123456789wn = new ArrayList<PeilvPlayData>();
        List<PeilvPlayData> dxds = new ArrayList<PeilvPlayData>();

        for (PeilvWebResult result : webResults) {

            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                if (name.equals("大")||name.equals("小")||
                        name.equals("单")||name.equals("双")){
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    dxds.add(data);
                }else{
                    PeilvPlayData data = new PeilvPlayData();
                    data.setNumber(name);
                    data.setPeilv(String.valueOf(result.getOdds()));
                    data.setPeilvData(result);
                    niu123456789wn.add(data);
                }
            }
        }

        //第一项
        PeilvData firstItem = new PeilvData();
        firstItem.setTagName("");
        firstItem.setAppendTag(false);
        firstItem.setSubData(niu123456789wn);
        //第二项
        PeilvData secondItem = new PeilvData();
        secondItem.setTagName("");
        secondItem.setAppendTag(false);
        secondItem.setSubData(dxds);

        list.add(firstItem);
        list.add(secondItem);
        return list;
    }

    //时时彩-棋牌-德州扑克
    private static List<PeilvData> calcPeilvdzpk(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }
        List<PeilvPlayData> bshsslyzw = new ArrayList<PeilvPlayData>();
        for (PeilvWebResult result : webResults) {
            if (result.getMarkType().equals(PeilvParser.MARK_TYPE_QITA)) {
                String name = result.getName();
                PeilvPlayData data = new PeilvPlayData();
                data.setNumber(name);
                data.setPeilv(String.valueOf(result.getOdds()));
                data.setPeilvData(result);
                bshsslyzw.add(data);
            }
        }

        //第一项
        PeilvData firstItem = new PeilvData();
        firstItem.setTagName("");
        firstItem.setAppendTag(false);
        firstItem.setSubData(bshsslyzw);

        list.add(firstItem);
        return list;
    }

    //时时彩-棋牌-三公
    private static List<PeilvData> calcPeilvSanggong(List<PeilvWebResult> webResults) {
        List<PeilvData> list = new ArrayList<PeilvData>();
        if (webResults == null) {
            return list;
        }

        List<PeilvPlayData> zyh = new ArrayList<>();
        List<PeilvPlayData> dxdszh = new ArrayList<>();
        PeilvWebResult webResult = webResults.get(0);

        String arr1[] = new String[]{"左闲","右闲","和局"};
        for (String name : arr1) {
            PeilvPlayData data = new PeilvPlayData();
            data.setNumber(name);
            data.setPeilv(String.valueOf(webResult.getOdds()));
            data.setPeilvData(webResult);
            zyh.add(data);
        }

        String arr2[] = new String[]{"大","小","单","双","质","合"};
        for (String name : arr2) {
            PeilvPlayData data = new PeilvPlayData();
            data.setNumber(name);
            data.setPeilv(String.valueOf(webResult.getOdds()));
            data.setPeilvData(webResult);
            dxdszh.add(data);
        }

        String tags = ",左闲,右闲";
        List<String> tagList = Utils.splitString(tags,",");
        for (String tag : tagList) {
            PeilvData item = new PeilvData();
            item.setTagName(tag);
            item.setAppendTag(Utils.isEmptyString(tag) ? false : true);
            List<PeilvPlayData> subList = new ArrayList<PeilvPlayData>();
            subList.addAll(Utils.isEmptyString(tag) ? zyh : dxdszh);
            List<PeilvPlayData> destList = new ArrayList<>();
            try {
                for (int i = 0; i < subList.size(); i++) {
                    destList.add((PeilvPlayData) subList.get(i).clone());
                }
                item.setSubData(destList);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            list.add(item);
        }


//        //第一项
//        PeilvData zhongheItem = new PeilvData();
//        zhongheItem.setTagName("");
//        zhongheItem.setAppendTag(false);
//        zhongheItem.setSubData(zyh);
//        //左闲尾大
//        PeilvData wanweiItem = new PeilvData();
//        wanweiItem.setTagName("左闲");
//        wanweiItem.setAppendTag(true);
//        wanweiItem.setSubData(dxdszh);
//        //右闲尾大
//        PeilvData qianweiItem = new PeilvData();
//        qianweiItem.setTagName("右闲");
//        qianweiItem.setAppendTag(true);
//        List<PeilvPlayData> items = new ArrayList<>();
//        items.addAll(dxdszh);
//        qianweiItem.setSubData(items);

//        list.add(zhongheItem);
//        list.add(wanweiItem);
//        list.add(qianweiItem);
        return list;
    }


    public static List<BallonRules> figureOutPlayInfo(String cpVersion,String cpCode,
                                                      String pcode, String rcode) {

        if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V1))) {
            //时时彩
            if (cpCode.equals("1")||cpCode.equals("2")) {
                if (pcode.equals(PlayCodeConstants.dxds)) {
                    return calPlayDxds(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlayDwd(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.longhh)) {
                    return calPlayLonghuhe(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.rxwf)) {
                    return calPlayRxwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.exzx)) {
                    return calPlayExzx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sxzx) || pcode.equalsIgnoreCase(PlayCodeConstants.sxzux)) {
                    return calPlaySxzx(pcode, rcode);/////
                }else if (pcode.equals(PlayCodeConstants.sxwf_var)) {
                    return calPlaySxwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sixing_wf)) {
                    return calPlaySixingwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.wuxing_wf)) {
                    return calPlayWuxinwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.caibaozi)) {
                    return calPlayCaibaozhi(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.bdw) || pcode.equals(PlayCodeConstants.bdwd)) {
                    return calFFCPlayBdw(pcode, rcode);
                }
                //赛车
            } else if (cpCode.equals("3")) {
                if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlaySaicheDwd(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.longhh)||pcode.equalsIgnoreCase(PlayCodeConstants.lh)){
                    return calLonghuChampion(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.q1_str)){
                    return calQianyi(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.q2_str)){
                    return calQianer(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.gyh_str)){
                    return calGuanYaHe(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.qiansan)||pcode.equals(PlayCodeConstants.qiansan1)) {
                    return calQianShang(pcode, rcode);
                }
                // 福彩3D，排列3
            } else if (cpCode.equals("4")) {
                if (pcode.equals(PlayCodeConstants.zhi_xuan_str)) {
                    return calZxfs(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.zhuxuan_str)){
                    return calZhuxuan63(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.bdw_fx3d)){
                    return calBdw(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.er_ma_str)){
                    return calErma(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.dxds)) {
                    return calFucai3DPlayDxds(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calFucai3DPlayDwd(pcode, rcode);
                }
            } else if (cpCode.equals("5")) {
                if (pcode.equals(PlayCodeConstants.rxfs)) {
                    return calRxfs(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlac11x5Dwd(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.er_ma_str)) {
                    return cal11x5Erma(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.shang_ma_str)) {
                    return cal11x5Shangma(pcode, rcode);
                }
                //快三
            } else if (cpCode.equals("100")){
                if (pcode.equals(PlayCodeConstants.hz)) {
                    return calcK3Hezi(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sthtx)) {
                    return calcK3sthtx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sthdx)) {
                    return calcK3sthdx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sbtx)) {
                    return calcK3sbth(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.slhtx)) {
                    return calcK3slhtx(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.ethfx)) {
                    return calcK3Ethfx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.ethdx)) {
                    return calcK3Ethdx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.ebth)) {
                    return calcK3Ebth(pcode, rcode);
                }
                //PC蛋蛋，加拿大28
            } else if (cpCode.equals("7")) {
                if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlayPCDDDwd(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.bdw)) {
                    return calpcddBdw(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.exwf_str)) {
                    return calPlayPCDDExwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sxwf_var)) {
                    return calPlayPCDDSxwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.hz)) {
                    return calPlayPCDDhezi(pcode, rcode);
                }
                //六合彩
            } else if (cpCode.equals("6") || cpCode.equals("66")) {
                //跳到赔率版


            }
        } else if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V3))){
            //时时彩
            if (cpCode.equals("51")||cpCode.equals("52")) {
                if (pcode.equals(PlayCodeConstants.dxds)) {
                    return calPlayDxds(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlayDwd(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.longhh)) {
                    return calPlayLonghuhe(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.rxwf)) {
                    return calPlayRxwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.exzx)) {
                    return calPlayExzx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sxzx) || pcode.equalsIgnoreCase(PlayCodeConstants.sxzux)) {
                    return calPlaySxzx(pcode, rcode);/////
                }else if (pcode.equals(PlayCodeConstants.sxwf_var)) {
                    return calPlaySxwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sixing_wf)) {
                    return calPlaySixingwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.wuxing_wf)) {
                    return calPlayWuxinwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.caibaozi)) {
                    return calPlayCaibaozhi(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.bdw) || pcode.equals(PlayCodeConstants.bdwd)) {
                    return calFFCPlayBdw(pcode, rcode);
                }
                //赛车
            } else if (cpCode.equals("53")) {
                if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlaySaicheDwd(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.lh)){
                    return calLonghuChampion(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.q1_str)){
                    return calQianyi(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.q2_str)){
                    return calQianer(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.gyh)){
                    return calGuanYaHe(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.q3_str)) {
                    return calQianShang(pcode, rcode);
                }
                // 福彩3D，排列3
            } else if (cpCode.equals("54")) {
                if (pcode.equals(PlayCodeConstants.zhi_xuan_str)) {
                    return calZxfs(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.zhuxuan_str)){
                    return calZhuxuan63(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.bdw_fx3d)){
                    return calBdw(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.er_ma_str)){
                    return calErma(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.dxds)) {
                    return calFucai3DPlayDxds(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calFucai3DPlayDwd(pcode, rcode);
                }
                //11选5
            } else if (cpCode.equals("55")) {
                if (pcode.equals(PlayCodeConstants.rxfs)) {
                    return calRxfs(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlac11x5Dwd(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.bdw_11x5)) {
                    return cal11x5Bdw(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.er_ma_str)) {
                    return cal11x5Erma(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.shang_ma_str)) {
                    return cal11x5Shangma(pcode, rcode);
                }
                //快三
            } else if (cpCode.equals("58")){
                if (pcode.equals(PlayCodeConstants.hz)) {
                    return calcK3Hezi(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sthtx)) {
                    return calcK3sthtx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sthdx)) {
                    return calcK3sthdx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sbtx)) {
                    return calcK3sbth(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.slhtx)) {
                    return calcK3slhtx(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.ethfx)) {
                    return calcK3Ethfx(pcode, rcode);
                } else if (pcode.equals(PlayCodeConstants.ethdx)) {
                    return calcK3Ethdx(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.ebth)) {
                    return calcK3Ebth(pcode, rcode);
                }
                //PC蛋蛋，加拿大28
            } else if (cpCode.equals("57")) {
                if (pcode.equals(PlayCodeConstants.dwd)) {
                    return calPlayPCDDDwd(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.bdw)) {
                    return calpcddBdw(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.exwf_str)) {
                    return calPlayPCDDExwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.sxwf_pcegg)) {
                    return calPlayPCDDSxwf(pcode, rcode);
                }else if (pcode.equals(PlayCodeConstants.hz)) {
                    return calPlayPCDDhezi(pcode, rcode);
                }
                //六合彩
            } else if (cpCode.equals("6") || cpCode.equals("66")) {
                //跳到赔率版
            }
        }

        return null;
    }

    //福彩3D，排列3-大小单双
    private static List<BallonRules> calFucai3DPlayDxds(String pcode,String rcode) {

        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dxds_q2)) {

            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("百位");
            wanRules.setNums("大,小,单,双");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("十位");
            qianRules.setNums("大,小,单,双");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
        } else if (rcode.equals(PlayCodeConstants.dxds_h2)) {
            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("大,小,单,双");
            shiRules.setShowFuncView(false);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("大,小,单,双");
            geRules.setShowFuncView(false);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(shiRules);
            list.add(geRules);
        }
        return list;
    }

    private static List<BallonRules> calPlayDxds(String pcode,String rcode) {

        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dxds_zh)) {
            BallonRules ballonRules = new BallonRules();
            ballonRules.setRuleTxt("总和");
            ballonRules.setNums("大,小,单,双");
            ballonRules.setShowFuncView(false);
            ballonRules.setShowWeiShuView(false);
            ballonRules.setPlayCode(pcode);
            ballonRules.setRuleCode(rcode);

            list.add(ballonRules);
        } else if (rcode.equals(PlayCodeConstants.dxds_q3)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("大,小,单,双");
            wanRules.setShowFuncView(false);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("大,小,单,双");
            qianRules.setShowFuncView(false);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("大,小,单,双");
            baiRules.setShowFuncView(false);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);
        } else if (rcode.equals(PlayCodeConstants.dxds_q2)) {

            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("大,小,单,双");
            wanRules.setShowFuncView(false);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("大,小,单,双");
            qianRules.setShowFuncView(false);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
        } else if (rcode.equals(PlayCodeConstants.dxds_h3)) {

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("大,小,单,双");
            baiRules.setShowFuncView(false);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("大,小,单,双");
            shiRules.setShowFuncView(false);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("大,小,单,双");
            geRules.setShowFuncView(false);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);
        } else if (rcode.equals(PlayCodeConstants.dxds_h2)) {
            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("大,小,单,双");
            shiRules.setShowFuncView(false);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("大,小,单,双");
            geRules.setShowFuncView(false);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(shiRules);
            list.add(geRules);
        }
        return list;
    }

    // 组选-组六，组三
    private static List<BallonRules> calZhuxuan63(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.zux_z6)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组六");
            longRules.setNums("0,1,2,3,4,5,6,7,8,9");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }else if (rcode.equals(PlayCodeConstants.zux_z3)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组三");
            longRules.setNums("0,1,2,3,4,5,6,7,8,9");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    // 不定位胆-一码，二码不定位
    private static List<BallonRules> calBdw(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.bdw_2m) || rcode.equals(PlayCodeConstants.bdw_1m)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("不定位");
            longRules.setNums("0,1,2,3,4,5,6,7,8,9");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    // 11选5 不定位胆-前三，中三，后三
    private static List<BallonRules> cal11x5Bdw(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();

        BallonRules longRules = new BallonRules();
        if (rcode.equals(PlayCodeConstants.bdw_q3)) {
            longRules.setRuleTxt("前三");
        }else if (rcode.equals(PlayCodeConstants.bdw_z3)) {
            longRules.setRuleTxt("中三");
        }else if (rcode.equals(PlayCodeConstants.bdw_h3)) {
            longRules.setRuleTxt("后三");
        }
        longRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
        longRules.setShowFuncView(true);
        longRules.setShowWeiShuView(false);
        longRules.setPlayCode(pcode);
        longRules.setRuleCode(rcode);
        list.add(longRules);
        return list;
    }

    private static List<BallonRules> calpcddBdw(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.bdw_pcegg)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("不定位");
            longRules.setNums("0,1,2,3,4,5,6,7,8,9");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    private static List<BallonRules> cal11x5Erma(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q2zx_fs)) {

            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);
            list.add(wanRules);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);
            list.add(qianRules);

        } else if (rcode.equals(PlayCodeConstants.q2zx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组选");
            longRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        } else if (rcode.equals(PlayCodeConstants.h2zx_fs)) {

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);
            list.add(shiRules);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);
            list.add(geRules);

        } else if (rcode.equals(PlayCodeConstants.h2zx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组选");
            longRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    // 11选5-二码-前二直选，前二组选，后二直选，后二组选
    private static List<BallonRules> cal11x5Shangma(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q3zx_fs)) {

            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);
            list.add(wanRules);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);
            list.add(qianRules);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);
            list.add(baiRules);

        } else if (rcode.equals(PlayCodeConstants.q3zx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组选");
            longRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        } else if (rcode.equals(PlayCodeConstants.z3zx_fs)) {

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);
            list.add(qianRules);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);
            list.add(baiRules);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("十位");
            geRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);
            list.add(geRules);

        } else if (rcode.equals(PlayCodeConstants.z3zx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组选");
            longRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }else if (rcode.equals(PlayCodeConstants.h3zx_fs)) {

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);
            list.add(baiRules);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("十位");
            geRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);
            list.add(geRules);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);
            list.add(qianRules);

        }else if (rcode.equals(PlayCodeConstants.h3zx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组选");
            longRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    // 二码-前二直选，前二组选，后二直选，后二组选
    private static List<BallonRules> calErma(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q2zx_fs)) {

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);
            list.add(baiRules);

            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("十位");
            longRules.setNums("0,1,2,3,4,5,6,7,8,9");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);

        } else if (rcode.equals(PlayCodeConstants.em_q2zux)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组选");
            longRules.setNums("0,1,2,3,4,5,6,7,8,9");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        } else if (rcode.equals(PlayCodeConstants.h2zx_fs)) {

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);
            list.add(shiRules);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);
            list.add(geRules);

        } else if (rcode.equals(PlayCodeConstants.em_h2zux)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("组选");
            longRules.setNums("0,1,2,3,4,5,6,7,8,9");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //快三-三同号通选
    private static List<BallonRules> calcK3sthtx(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.sthtx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("三同号通选");
            longRules.setPostNums("三同号通选");
            longRules.setNums("通");
            longRules.setShowFuncView(false);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //快三-三同号单选
    private static List<BallonRules> calcK3sthdx(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.sthdx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("三同号单选");
            longRules.setNums("111,222,333,444,555,666");
            longRules.setShowFuncView(false);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //快三-三不同号
    private static List<BallonRules> calcK3sbth(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.sbtx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("三不同号");
            longRules.setNums("1,2,3,4,5,6");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //快三-三连号通选
    private static List<BallonRules> calcK3slhtx(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.slhtx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("三连号通选");
            longRules.setPostNums("三连号通选");
            longRules.setNums("通");
            longRules.setShowFuncView(false);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //快三-二连通单选
    private static List<BallonRules> calcK3Ethdx(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();

        BallonRules longRules = new BallonRules();
        longRules.setRuleTxt("同号");
        longRules.setNums("11,22,33,44,55,66");
        longRules.setShowFuncView(true);
        longRules.setShowWeiShuView(false);
        longRules.setPlayCode(pcode);
        longRules.setRuleCode(rcode);
        list.add(longRules);


        BallonRules longRules2 = new BallonRules();
        longRules2.setRuleTxt("不同号");
        longRules2.setNums("1,2,3,4,5,6");
        longRules2.setShowFuncView(true);
        longRules2.setShowWeiShuView(false);
        longRules2.setPlayCode(pcode);
        longRules2.setRuleCode(rcode);
        list.add(longRules2);

        return list;
    }

    //快三-二同号复选
    private static List<BallonRules> calcK3Ethfx(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.ethfx)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("二同号复选");
            longRules.setNums("11,22,33,44,55,66");
            longRules.setShowFuncView(false);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //快三-二不同号
    private static List<BallonRules> calcK3Ebth(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.ebth)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("二不同号");
            longRules.setNums("1,2,3,4,5,6");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //快三-和值
    private static List<BallonRules> calcK3Hezi(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dxds)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("大小单双");
            longRules.setNums("大,小,单,双");
            longRules.setShowFuncView(false);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        } else if (rcode.equals(PlayCodeConstants.hz)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("直选和值");
            longRules.setNums("3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //龙虎-冠亚季
    private static List<BallonRules> calLonghuChampion(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.longhu_gunjun) ||
                rcode.equals(PlayCodeConstants.longhu_yajun)||
                rcode.equals(PlayCodeConstants.longhu_jijun)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("龙虎");
            longRules.setNums("龙,虎");
            longRules.setShowFuncView(false);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //前一
    private static List<BallonRules> calQianyi(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q1zx_fs)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("冠");
            longRules.setNums("01,02,03,04,05,06,07,08,09,10");
            longRules.setShowFuncView(true);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }
        return list;
    }

    //前二
    private static List<BallonRules> calQianer(String pcode,String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q2zx_fs)) {

            BallonRules guangRules = new BallonRules();
            guangRules.setRuleTxt("冠");
            guangRules.setNums("01,02,03,04,05,06,07,08,09,10");
            guangRules.setShowFuncView(true);
            guangRules.setShowWeiShuView(false);
            guangRules.setPlayCode(pcode);
            guangRules.setRuleCode(rcode);
            list.add(guangRules);

            BallonRules yaRules = new BallonRules();
            yaRules.setRuleTxt("亚");
            yaRules.setNums("01,02,03,04,05,06,07,08,09,10");
            yaRules.setShowFuncView(true);
            yaRules.setShowWeiShuView(false);
            yaRules.setPlayCode(pcode);
            yaRules.setRuleCode(rcode);
            list.add(yaRules);

        }
        return list;
    }

    //冠亚和
    private static List<BallonRules> calGuanYaHe(String pcode,String rcode) {

        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dxds)) {
            BallonRules guangRules = new BallonRules();
            guangRules.setRuleTxt("大小单双");
            guangRules.setNums("大,小,单,双");
            guangRules.setShowFuncView(false);
            guangRules.setShowWeiShuView(false);
            guangRules.setPlayCode(pcode);
            guangRules.setRuleCode(rcode);
            list.add(guangRules);
        }else if (rcode.equals(PlayCodeConstants.gyhz)){
            BallonRules yaRules = new BallonRules();
            yaRules.setRuleTxt("冠亚和值");
            yaRules.setNums("3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19");
            yaRules.setShowFuncView(false);
            yaRules.setShowWeiShuView(false);
            yaRules.setPlayCode(pcode);
            yaRules.setRuleCode(rcode);
            list.add(yaRules);
        }
        return list;
    }

    //前三
    private static List<BallonRules> calQianShang(String pcode,String rcode) {

        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q3zx_fs)) {
            BallonRules guan = new BallonRules();
            guan.setRuleTxt("冠");
            guan.setNums("01,02,03,04,05,06,07,08,09,10");
            guan.setShowFuncView(true);
            guan.setShowWeiShuView(false);
            guan.setPlayCode(pcode);
            guan.setRuleCode(rcode);
            list.add(guan);

            BallonRules ya = new BallonRules();
            ya.setRuleTxt("亚");
            ya.setNums("01,02,03,04,05,06,07,08,09,10");
            ya.setShowFuncView(true);
            ya.setShowWeiShuView(false);
            ya.setPlayCode(pcode);
            ya.setRuleCode(rcode);
            list.add(ya);

            BallonRules ji = new BallonRules();
            ji.setRuleTxt("季");
            ji.setNums("01,02,03,04,05,06,07,08,09,10");
            ji.setShowFuncView(true);
            ji.setShowWeiShuView(false);
            ji.setPlayCode(pcode);
            ji.setRuleCode(rcode);
            list.add(ji);
        }
        return list;
    }

    //11选5-任选复式
    private static List<BallonRules> calRxfs(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();

        BallonRules baiRules = new BallonRules();
        if (rcode.equals(PlayCodeConstants.rxfs_rx8z5)) {
            baiRules.setRuleTxt("行八中五");
        }else if (rcode.equals(PlayCodeConstants.rxfs_rx7z5)){
            baiRules.setRuleTxt("任七中五");
        }else if (rcode.equals(PlayCodeConstants.rxfs_rx6z5)){
            baiRules.setRuleTxt("任六中五");
        }else if (rcode.equals(PlayCodeConstants.rxfs_rx4z4)){
            baiRules.setRuleTxt("任四中四");
        }else if (rcode.equals(PlayCodeConstants.rxfs_rx3z3)){
            baiRules.setRuleTxt("任三中三");
        }else if (rcode.equals(PlayCodeConstants.rxfs_rx2z2)){
            baiRules.setRuleTxt("任二中二");
        }else if (rcode.equals(PlayCodeConstants.rxfs_rx1z1)){
            baiRules.setRuleTxt("任一中一");
        }else if (rcode.equals(PlayCodeConstants.rxfs_rx5z5)){
            baiRules.setRuleTxt("任五中五");
        }
        baiRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
        baiRules.setShowFuncView(true);
        baiRules.setShowWeiShuView(false);
        baiRules.setPlayCode(pcode);
        baiRules.setRuleCode(rcode);
        list.add(baiRules);
        return list;
    }

    //福彩3D，排列3-大小单双
    private static List<BallonRules> calFucai3DPlayDwd(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dwd)) {

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);
            return list;
        }
        return null;
    }

    private static List<BallonRules> calPlac11x5Dwd(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dwd)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("01,02,03,04,05,06,07,08,09,10,11");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);
            return list;
        }
        return null;
    }

    private static List<BallonRules> calPlayDwd(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dwd)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);
            return list;
        }
        return null;
    }

    private static List<BallonRules> calPlaySaicheDwd(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dwd)) {
            BallonRules guanRules = new BallonRules();
            guanRules.setRuleTxt("冠");
            guanRules.setNums("01,02,03,04,05,06,07,08,09,10");
            guanRules.setShowFuncView(true);
            guanRules.setShowWeiShuView(false);
            guanRules.setPlayCode(pcode);
            guanRules.setRuleCode(rcode);

            BallonRules yaRules = new BallonRules();
            yaRules.setRuleTxt("亚");
            yaRules.setNums("01,02,03,04,05,06,07,08,09,10");
            yaRules.setShowFuncView(true);
            yaRules.setShowWeiShuView(false);
            yaRules.setPlayCode(pcode);
            yaRules.setRuleCode(rcode);

            BallonRules jiRules = new BallonRules();
            jiRules.setRuleTxt("季");
            jiRules.setNums("01,02,03,04,05,06,07,08,09,10");
            jiRules.setShowFuncView(true);
            jiRules.setShowWeiShuView(false);
            jiRules.setPlayCode(pcode);
            jiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("四");
            shiRules.setNums("01,02,03,04,05,06,07,08,09,10");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules wuRules = new BallonRules();
            wuRules.setRuleTxt("五");
            wuRules.setNums("01,02,03,04,05,06,07,08,09,10");
            wuRules.setShowFuncView(true);
            wuRules.setShowWeiShuView(false);
            wuRules.setPlayCode(pcode);
            wuRules.setRuleCode(rcode);

            BallonRules liuRules = new BallonRules();
            liuRules.setRuleTxt("六");
            liuRules.setNums("01,02,03,04,05,06,07,08,09,10");
            liuRules.setShowFuncView(true);
            liuRules.setShowWeiShuView(false);
            liuRules.setPlayCode(pcode);
            liuRules.setRuleCode(rcode);

            BallonRules qiRules = new BallonRules();
            qiRules.setRuleTxt("七");
            qiRules.setNums("01,02,03,04,05,06,07,08,09,10");
            qiRules.setShowFuncView(true);
            qiRules.setShowWeiShuView(false);
            qiRules.setPlayCode(pcode);
            qiRules.setRuleCode(rcode);

            BallonRules baRules = new BallonRules();
            baRules.setRuleTxt("八");
            baRules.setNums("01,02,03,04,05,06,07,08,09,10");
            baRules.setShowFuncView(true);
            baRules.setShowWeiShuView(false);
            baRules.setPlayCode(pcode);
            baRules.setRuleCode(rcode);

            BallonRules jiuRules = new BallonRules();
            jiuRules.setRuleTxt("九");
            jiuRules.setNums("01,02,03,04,05,06,07,08,09,10");
            jiuRules.setShowFuncView(true);
            jiuRules.setShowWeiShuView(false);
            jiuRules.setPlayCode(pcode);
            jiuRules.setRuleCode(rcode);

            BallonRules shiiRules = new BallonRules();
            shiiRules.setRuleTxt("十");
            shiiRules.setNums("01,02,03,04,05,06,07,08,09,10");
            shiiRules.setShowFuncView(true);
            shiiRules.setShowWeiShuView(false);
            shiiRules.setPlayCode(pcode);
            shiiRules.setRuleCode(rcode);

            list.add(guanRules);
            list.add(yaRules);
            list.add(jiRules);
            list.add(shiRules);
            list.add(wuRules);
            list.add(liuRules);
            list.add(qiRules);
            list.add(baRules);
            list.add(jiuRules);
            list.add(shiiRules);

            return list;
        }
        return null;
    }

    //pc蛋蛋-二星玩法
    private static List<BallonRules> calPlayPCDDExwf(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q2zx_fs)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("一位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("二位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            return list;
        }else if (rcode.equals(PlayCodeConstants.q2zx)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("前二组选");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);
            list.add(wanRules);
            return list;
        }else if (rcode.equals(PlayCodeConstants.h2zx_fs)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("二位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("三位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            return list;
        }else if (rcode.equals(PlayCodeConstants.h2zx)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("后二组选");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);
            list.add(wanRules);
            return list;
        }
        return null;
    }

    //pc蛋蛋-三星玩法
    private static List<BallonRules> calPlayPCDDSxwf(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.sxzx)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("三星组选");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            list.add(wanRules);
            return list;
        }else if (rcode.equals(PlayCodeConstants.sxfs)) {
            BallonRules yiRules = new BallonRules();
            yiRules.setRuleTxt("一位");
            yiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            yiRules.setShowFuncView(true);
            yiRules.setShowWeiShuView(false);
            yiRules.setPlayCode(pcode);
            yiRules.setRuleCode(rcode);
            list.add(yiRules);

            BallonRules erRules = new BallonRules();
            erRules.setRuleTxt("二位");
            erRules.setNums("0,1,2,3,4,5,6,7,8,9");
            erRules.setShowFuncView(true);
            erRules.setShowWeiShuView(false);
            erRules.setPlayCode(pcode);
            erRules.setRuleCode(rcode);
            list.add(erRules);

            BallonRules sangRules = new BallonRules();
            sangRules.setRuleTxt("三位");
            sangRules.setNums("0,1,2,3,4,5,6,7,8,9");
            sangRules.setShowFuncView(true);
            sangRules.setShowWeiShuView(false);
            sangRules.setPlayCode(pcode);
            sangRules.setRuleCode(rcode);
            list.add(sangRules);
            return list;
        }
        return null;
    }

    //pc蛋蛋，加拿大28-定位胆
    private static List<BallonRules> calPlayPCDDDwd(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dwd)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("一位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("二位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("三位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);
            return list;
        }
        return null;
    }

    //PC蛋蛋，加拿大28--和值
    private static List<BallonRules> calPlayPCDDhezi(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.dxds)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("大小单双");
            wanRules.setNums("大,小,单,双");
            wanRules.setShowFuncView(false);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);
            list.add(wanRules);
            return list;
        } else if (rcode.equals(PlayCodeConstants.hz)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("直选和值");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17," +
                    "18,19,20,21,22,23,24,25,26,27");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);
            list.add(wanRules);
            return list;
        }
        return null;
    }

    //直选-复式
    private static List<BallonRules> calZxfs(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.zhx_fs)) {

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);
            return list;
        }
        return null;
    }

    private static List<BallonRules> calPlayLonghuhe(String pcode, String rcode) {

        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.longhudou)) {
            BallonRules longRules = new BallonRules();
            longRules.setRuleTxt("龙虎");
            longRules.setNums("龙,虎");
            longRules.setShowFuncView(false);
            longRules.setShowWeiShuView(false);
            longRules.setPlayCode(pcode);
            longRules.setRuleCode(rcode);
            list.add(longRules);
        }else if (rcode.equals(PlayCodeConstants.longhuhe)) {
            BallonRules heRules = new BallonRules();
            heRules.setRuleTxt("和");
            heRules.setNums("和");
            heRules.setShowFuncView(false);
            heRules.setShowWeiShuView(false);
            heRules.setPlayCode(pcode);
            heRules.setRuleCode(rcode);
            list.add(heRules);
        }
        return list;
    }

    /**
     *
     * @param numStr 显示在投注面板上的号码
     * @param postNums 提交后台的号码
     * @return
     */
    public static List<BallListItemInfo> convertNumListToEntifyList(String numStr,String postNums) {
        List<String> numbers = Utils.splitString(numStr, ",");
        List<String> postNumbers = Utils.splitString(postNums, ",");
        List<BallListItemInfo> list = new ArrayList<BallListItemInfo>();
        for (int i=0;i<numbers.size();i++){
            BallListItemInfo info = new BallListItemInfo();
            info.setNum(numbers.get(i));
            if (!postNumbers.isEmpty() && postNumbers.get(i) != null ) {
                info.setPostNum(postNumbers.get(i));
            }
            info.setSelected(false);
            list.add(info);
        }
        return list;
    }

    public static List<BallListItemInfo> convertNumListToEntifyList(String numStr) {
        List<String> numbers = Utils.splitString(numStr, ",");
        List<BallListItemInfo> list = new ArrayList<BallListItemInfo>();
        for (String num : numbers) {
            BallListItemInfo info = new BallListItemInfo();
            info.setNum(num);
            info.setPostNum("");
            info.setSelected(false);
            list.add(info);
        }
        return list;
    }

    private static List<BallonRules> calPlayRxwf(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.rxwf_r3zux_zu3)) {
            BallonRules zhusangRules = new BallonRules();
            zhusangRules.setRuleTxt("组三");
            zhusangRules.setNums("0,1,2,3,4,5,6,7,8,9");
            zhusangRules.setShowFuncView(true);
            zhusangRules.setShowWeiShuView(true);
            zhusangRules.setPlayCode(pcode);
            zhusangRules.setRuleCode(rcode);
            zhusangRules.setWeishuInfo(convertNumListToEntifyList(WEISHU_STR));
            list.add(zhusangRules);
        }else if (rcode.equals(PlayCodeConstants.rxwf_r3zux_zu6)) {
            BallonRules zhuliuRules = new BallonRules();
            zhuliuRules.setRuleTxt("组六");
            zhuliuRules.setNums("0,1,2,3,4,5,6,7,8,9");
            zhuliuRules.setShowFuncView(true);
            zhuliuRules.setShowWeiShuView(true);
            zhuliuRules.setPlayCode(pcode);
            zhuliuRules.setRuleCode(rcode);
            zhuliuRules.setWeishuInfo(convertNumListToEntifyList(WEISHU_STR));
            list.add(zhuliuRules);
        }else if (rcode.equals(PlayCodeConstants.rxwf_r3zx_fs)
                || rcode.equals(PlayCodeConstants.rxwf_r4zx_fs)
                || rcode.equals(PlayCodeConstants.rxwf_r2zx_fs)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);
        }
        return list;
    }

    private static List<BallonRules> calPlayExzx(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q2zx_hz)) {
            BallonRules zhusangRules = new BallonRules();
            zhusangRules.setRuleTxt("和值");
            zhusangRules.setNums("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18");
            zhusangRules.setShowFuncView(true);
            zhusangRules.setShowWeiShuView(false);
            zhusangRules.setPlayCode(pcode);
            zhusangRules.setRuleCode(rcode);
            list.add(zhusangRules);
        }else if (rcode.equals(PlayCodeConstants.q2zx_fs)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
        }else if (rcode.equals(PlayCodeConstants.q2zx_ds)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
        }else if (rcode.equals(PlayCodeConstants.h2zx_hz)) {
            BallonRules hezhiRules = new BallonRules();
            hezhiRules.setRuleTxt("和值");
            hezhiRules.setNums("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18");
            hezhiRules.setShowFuncView(true);
            hezhiRules.setShowWeiShuView(false);
            hezhiRules.setPlayCode(pcode);
            hezhiRules.setRuleCode(rcode);
            list.add(hezhiRules);
        }else if (rcode.equals(PlayCodeConstants.h2zx_fs)) {
            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(shiRules);
            list.add(geRules);
        }else if (rcode.equals(PlayCodeConstants.h2zx_ds)) {
            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(shiRules);
            list.add(geRules);
        }
        return list;
    }

    private static List<BallonRules> calPlaySxzx(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        BallonRules zhuxuanRules = new BallonRules();
        if (rcode.equals(PlayCodeConstants.q3zux_zu3)||rcode.equals(PlayCodeConstants.z3zux_zu3)
                ||rcode.equals(PlayCodeConstants.h3zux_zu3)){
            zhuxuanRules.setRuleTxt("组三");
        } else if (rcode.equals(PlayCodeConstants.q3zux_zu6)||rcode.equals(PlayCodeConstants.z3zux_zu6)
                ||rcode.equals(PlayCodeConstants.h3zux_zu6)) {
            zhuxuanRules.setRuleTxt("组六");
        }
        zhuxuanRules.setNums("0,1,2,3,4,5,6,7,8,9");
        zhuxuanRules.setShowFuncView(true);
        zhuxuanRules.setShowWeiShuView(false);
        zhuxuanRules.setPlayCode(pcode);
        zhuxuanRules.setRuleCode(rcode);
        list.add(zhuxuanRules);
        return list;
    }

    private static List<BallonRules> calPlaySxwf(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q3zx_fs)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);

        }else if (rcode.equals(PlayCodeConstants.z3zx_fs)) {

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            list.add(qianRules);
            list.add(baiRules);
            list.add(shiRules);


        }else if (rcode.equals(PlayCodeConstants.h3zx_fs)) {

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);
        }
        return list;
    }

    private static List<BallonRules> calPlaySixingwf(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.q4zx_fs)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);
            list.add(shiRules);

        }else if (rcode.equals(PlayCodeConstants.h4zx_fs)) {

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(qianRules);
            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);

        }
        return list;
    }

    private static List<BallonRules> calPlayWuxinwf(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        if (rcode.equals(PlayCodeConstants.wxzx_fs)) {
            BallonRules wanRules = new BallonRules();
            wanRules.setRuleTxt("万位");
            wanRules.setNums("0,1,2,3,4,5,6,7,8,9");
            wanRules.setShowFuncView(true);
            wanRules.setShowWeiShuView(false);
            wanRules.setPlayCode(pcode);
            wanRules.setRuleCode(rcode);

            BallonRules qianRules = new BallonRules();
            qianRules.setRuleTxt("千位");
            qianRules.setNums("0,1,2,3,4,5,6,7,8,9");
            qianRules.setShowFuncView(true);
            qianRules.setShowWeiShuView(false);
            qianRules.setPlayCode(pcode);
            qianRules.setRuleCode(rcode);

            BallonRules baiRules = new BallonRules();
            baiRules.setRuleTxt("百位");
            baiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            baiRules.setShowFuncView(true);
            baiRules.setShowWeiShuView(false);
            baiRules.setPlayCode(pcode);
            baiRules.setRuleCode(rcode);

            BallonRules shiRules = new BallonRules();
            shiRules.setRuleTxt("十位");
            shiRules.setNums("0,1,2,3,4,5,6,7,8,9");
            shiRules.setShowFuncView(true);
            shiRules.setShowWeiShuView(false);
            shiRules.setPlayCode(pcode);
            shiRules.setRuleCode(rcode);

            BallonRules geRules = new BallonRules();
            geRules.setRuleTxt("个位");
            geRules.setNums("0,1,2,3,4,5,6,7,8,9");
            geRules.setShowFuncView(true);
            geRules.setShowWeiShuView(false);
            geRules.setPlayCode(pcode);
            geRules.setRuleCode(rcode);

            list.add(wanRules);
            list.add(qianRules);
            list.add(baiRules);
            list.add(shiRules);
            list.add(geRules);

        }
        return list;
    }

    private static List<BallonRules> calPlayCaibaozhi(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        BallonRules baozhiRules = new BallonRules();
        if (rcode.equals(PlayCodeConstants.baozi)) {
            baozhiRules.setRuleTxt("豹子");
        }else if (rcode.equals(PlayCodeConstants.shunzi)) {
            baozhiRules.setRuleTxt("顺子");
        }else if (rcode.equals(PlayCodeConstants.duizi)) {
            baozhiRules.setRuleTxt("对子");
        }else if (rcode.equals(PlayCodeConstants.banshun)) {
            baozhiRules.setRuleTxt("半顺");
        }else if (rcode.equals(PlayCodeConstants.zaliu)) {
            baozhiRules.setRuleTxt("杂六");
        }
        baozhiRules.setNums("前,中,后");
        baozhiRules.setPostNums("前三,中三,后三");
        baozhiRules.setShowFuncView(false);
        baozhiRules.setShowWeiShuView(false);
        baozhiRules.setPlayCode(pcode);
        baozhiRules.setRuleCode(rcode);
        list.add(baozhiRules);
        return list;
    }

    //分分彩-不定位胆
    private static List<BallonRules> calFFCPlayBdw(String pcode, String rcode) {
        List<BallonRules> list = new ArrayList<BallonRules>();
        BallonRules baozhiRules = new BallonRules();
        if (rcode.equals(PlayCodeConstants.bdw_q31m)) {
            baozhiRules.setRuleTxt("前三一码");
        }else if (rcode.equals(PlayCodeConstants.bdw_z31m)) {
            baozhiRules.setRuleTxt("中三一码");
        }else if (rcode.equals(PlayCodeConstants.bdw_h31m)) {
            baozhiRules.setRuleTxt("后三一码");
        }
        baozhiRules.setNums("0,1,2,3,4,5,6,7,8,9");
        baozhiRules.setShowFuncView(true);
        baozhiRules.setShowWeiShuView(false);
        baozhiRules.setPlayCode(pcode);
        baozhiRules.setRuleCode(rcode);
        list.add(baozhiRules);
        return list;
    }


}





