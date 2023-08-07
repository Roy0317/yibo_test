package com.yibo.yiboapp.data;

import androidx.annotation.ArrayRes;

import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 2017/9/16.
 * 各版本彩票号，球数，彩票玩法等等信息
 */

public class Lotterys {

    public static final String TAG = Lotterys.class.getSimpleName();

    public static LotteryConstants getLotterysByVersion(String versionNum) {
        if (versionNum.equals("1")) {
            return getLotterysInVersion1();
        } else if (versionNum.equals("2")) {
            return getLotterysInVersion2();
        } else if (versionNum.equals("2-1")) {
            return getLotterysInVersion2();
        } else if (versionNum.equals("3")) {
            return getLotterysInVersion3();
        } else if (versionNum.equals("4")) {
            return getLotterysInVersion4();
        }
        return null;
    }

    //分分彩玩法
    private static List<PlayItem> getFFCPlays() {

        List<PlayItem> plays = new ArrayList<PlayItem>();

        //大小单双玩法
        SubPlayItem dxds_zh = new SubPlayItem();
        dxds_zh.setName("总和");
        dxds_zh.setCode(PlayCodeConstants.dxds_zh);
        dxds_zh.setRandomCount(1);

        SubPlayItem dxds_q3 = new SubPlayItem();
        dxds_q3.setName("前三");
        dxds_q3.setCode(PlayCodeConstants.dxds_q3);
        dxds_q3.setRandomCount(3);

        SubPlayItem dxds_h3 = new SubPlayItem();
        dxds_h3.setName("后三");
        dxds_h3.setCode(PlayCodeConstants.dxds_h3);
        dxds_h3.setRandomCount(3);

        SubPlayItem dxds_h2 = new SubPlayItem();
        dxds_h2.setName("后二");
        dxds_h2.setCode(PlayCodeConstants.dxds_h2);
        dxds_h2.setRandomCount(2);

        SubPlayItem dxds_q2 = new SubPlayItem();
        dxds_q2.setName("前二");
        dxds_q2.setCode(PlayCodeConstants.dxds_q2);
        dxds_q2.setRandomCount(2);

        List<SubPlayItem> splays = new ArrayList<SubPlayItem>();
        splays.add(dxds_zh);
        splays.add(dxds_q3);
        splays.add(dxds_h3);
        splays.add(dxds_h2);
        splays.add(dxds_q2);

        PlayItem dxds = new PlayItem();
        dxds.setName("大小单双");
        dxds.setCode(PlayCodeConstants.dxds);
        dxds.setRules(splays);
        plays.add(dxds);

        //定位胆
        SubPlayItem dwd_sub = new SubPlayItem();
        dwd_sub.setName("定位胆");
        dwd_sub.setCode(PlayCodeConstants.dwd);
        dwd_sub.setRandomCount(1);

        List<SubPlayItem> subRule = new ArrayList<SubPlayItem>();
        subRule.add(dwd_sub);

        PlayItem dwd = new PlayItem();
        dwd.setName("定位胆");
        dwd.setCode(PlayCodeConstants.dwd);
        dwd.setRules(subRule);
        plays.add(dwd);

        //不定位胆
        SubPlayItem qsym_bdw = new SubPlayItem();
        qsym_bdw.setName("前三一码");
        qsym_bdw.setCode(PlayCodeConstants.bdw_q31m);
        qsym_bdw.setRandomCount(1);

        SubPlayItem zsym_bdw = new SubPlayItem();
        zsym_bdw.setName("中三一码");
        zsym_bdw.setCode(PlayCodeConstants.bdw_z31m);
        zsym_bdw.setRandomCount(1);

        SubPlayItem hsym_bdw = new SubPlayItem();
        hsym_bdw.setName("后三一码");
        hsym_bdw.setCode(PlayCodeConstants.bdw_h31m);
        hsym_bdw.setRandomCount(1);

        List<SubPlayItem> bdwd = new ArrayList<SubPlayItem>();
        bdwd.add(qsym_bdw);
        bdwd.add(zsym_bdw);
        bdwd.add(hsym_bdw);

        PlayItem bdwd2 = new PlayItem();
        bdwd2.setName("不定位胆");
        bdwd2.setCode(PlayCodeConstants.bdwd);
        bdwd2.setRules(bdwd);
        plays.add(bdwd2);

        //龙虎和
        SubPlayItem lhh = new SubPlayItem();
        lhh.setName("龙虎");
        lhh.setCode(PlayCodeConstants.longhudou);
        lhh.setRandomCount(1);
        SubPlayItem longhuhe = new SubPlayItem();
        longhuhe.setName("和");
        longhuhe.setRandomCount(1);
        longhuhe.setCode(PlayCodeConstants.longhuhe);

        List<SubPlayItem> lhhs = new ArrayList<SubPlayItem>();
        lhhs.add(lhh);
        lhhs.add(longhuhe);

        PlayItem longhuhePlay = new PlayItem();
        longhuhePlay.setName("龙虎和");
        longhuhePlay.setCode(PlayCodeConstants.longhh);
        longhuhePlay.setRules(lhhs);
        plays.add(longhuhePlay);

        //任选玩法
        SubPlayItem rszs = new SubPlayItem();
        rszs.setName("任三组三");
        rszs.setCode(PlayCodeConstants.rxwf_r3zux_zu3);
        rszs.setRandomCount(2);
        SubPlayItem rszl = new SubPlayItem();
        rszl.setName("任三组六");
        rszl.setCode(PlayCodeConstants.rxwf_r3zux_zu6);
        rszl.setRandomCount(3);
        SubPlayItem rsfs = new SubPlayItem();
        rsfs.setName("任三复式");
        rsfs.setCode(PlayCodeConstants.rxwf_r3zx_fs);
        rsfs.setRandomCount(3);
        SubPlayItem rsfs2 = new SubPlayItem();
        rsfs2.setName("任四复式");
        rsfs2.setCode(PlayCodeConstants.rxwf_r4zx_fs);
        rsfs2.setRandomCount(4);
        SubPlayItem refs = new SubPlayItem();
        refs.setName("任二复式");
        refs.setCode(PlayCodeConstants.rxwf_r2zx_fs);
        refs.setRandomCount(2);

        List<SubPlayItem> rswf = new ArrayList<SubPlayItem>();
        rswf.add(rszs);
        rswf.add(rszl);
        rswf.add(rsfs);
        rswf.add(rsfs2);
        rswf.add(refs);

        PlayItem rswfPlay = new PlayItem();
        rswfPlay.setName("任选玩法");
        rswfPlay.setCode(PlayCodeConstants.rxwf);
        rswfPlay.setRules(rswf);
        plays.add(rswfPlay);

        //二星直选
        SubPlayItem qehz = new SubPlayItem();
        qehz.setName("前二和值");
        qehz.setCode(PlayCodeConstants.q2zx_hz);
        qehz.setRandomCount(1);
        SubPlayItem qefs = new SubPlayItem();
        qefs.setName("前二复式");
        qefs.setRandomCount(2);
        qefs.setCode(PlayCodeConstants.q2zx_fs);
        SubPlayItem hehz = new SubPlayItem();
        hehz.setName("后二和值");
        hehz.setRandomCount(1);
        hehz.setCode(PlayCodeConstants.h2zx_hz);
        SubPlayItem hefs = new SubPlayItem();
        hefs.setName("后二复式");
        hefs.setRandomCount(2);
        hefs.setCode(PlayCodeConstants.h2zx_fs);

        List<SubPlayItem> exzx = new ArrayList<SubPlayItem>();
        exzx.add(qehz);
        exzx.add(qefs);
        exzx.add(hehz);
        exzx.add(hefs);


        PlayItem exzxPlay = new PlayItem();
        exzxPlay.setName("二星直选");
        exzxPlay.setCode(PlayCodeConstants.exzx);
        exzxPlay.setRules(exzx);
        plays.add(exzxPlay);

        //三星组选
        SubPlayItem qszus = new SubPlayItem();
        qszus.setName("前三组三");
        qszus.setRandomCount(2);
        qszus.setCode(PlayCodeConstants.q3zux_zu3);
        SubPlayItem qszuliu = new SubPlayItem();
        qszuliu.setName("前三组六");
        qszuliu.setRandomCount(3);
        qszuliu.setCode(PlayCodeConstants.q3zux_zu6);
        SubPlayItem zszus = new SubPlayItem();
        zszus.setName("中三组三");
        zszus.setRandomCount(2);
        zszus.setCode(PlayCodeConstants.z3zux_zu3);
        SubPlayItem zhszuliu = new SubPlayItem();
        zhszuliu.setName("中三组六");
        zhszuliu.setRandomCount(3);
        zhszuliu.setCode(PlayCodeConstants.z3zux_zu6);
        SubPlayItem hszus = new SubPlayItem();
        hszus.setName("后三组三");
        hszus.setRandomCount(2);
        hszus.setCode(PlayCodeConstants.h3zux_zu3);
        SubPlayItem hszuliu = new SubPlayItem();
        hszuliu.setName("后三组六");
        hszuliu.setRandomCount(3);
        hszuliu.setCode(PlayCodeConstants.h3zux_zu6);


        List<SubPlayItem> sxzhxuan = new ArrayList<SubPlayItem>();
        sxzhxuan.add(qszus);
        sxzhxuan.add(qszuliu);
        sxzhxuan.add(zszus);
        sxzhxuan.add(zhszuliu);
        sxzhxuan.add(hszus);
        sxzhxuan.add(hszuliu);

        PlayItem sxzuxuanPlay = new PlayItem();
        sxzuxuanPlay.setName("三星直选");
        sxzuxuanPlay.setCode(PlayCodeConstants.sxzx);
        sxzuxuanPlay.setRules(sxzhxuan);
        plays.add(sxzuxuanPlay);

        //三星玩法
        SubPlayItem qsfushi = new SubPlayItem();
        qsfushi.setName("前三复式");
        qsfushi.setRandomCount(3);
        qsfushi.setCode(PlayCodeConstants.q3zx_fs);
        SubPlayItem zhsfushi = new SubPlayItem();
        zhsfushi.setName("中三复式");
        zhsfushi.setRandomCount(3);
        zhsfushi.setCode(PlayCodeConstants.z3zx_fs);
        SubPlayItem hsfushi = new SubPlayItem();
        hsfushi.setName("后三复式");
        hsfushi.setRandomCount(3);
        hsfushi.setCode(PlayCodeConstants.h3zx_fs);

        List<SubPlayItem> sangxinwf = new ArrayList<SubPlayItem>();
        sangxinwf.add(qsfushi);
        sangxinwf.add(zhsfushi);
        sangxinwf.add(hsfushi);

        PlayItem sxwfPlay = new PlayItem();
        sxwfPlay.setName("三星玩法");
        sxwfPlay.setCode(PlayCodeConstants.sxwf_var);
        sxwfPlay.setRules(sangxinwf);
        plays.add(sxwfPlay);

        //四星玩法
        SubPlayItem qshifushi = new SubPlayItem();
        qshifushi.setName("前四复式");
        qshifushi.setRandomCount(4);
        qshifushi.setCode(PlayCodeConstants.q4zx_fs);
        SubPlayItem hshifushi = new SubPlayItem();
        hshifushi.setName("后四复式");
        hshifushi.setRandomCount(4);
        hshifushi.setCode(PlayCodeConstants.h4zx_fs);

        List<SubPlayItem> sixinwf = new ArrayList<SubPlayItem>();
        sixinwf.add(qshifushi);
        sixinwf.add(hshifushi);

        PlayItem sixingwfPlay = new PlayItem();
        sixingwfPlay.setName("四星玩法");
        sixingwfPlay.setCode(PlayCodeConstants.sixing_wf);
        sixingwfPlay.setRules(sixinwf);
        plays.add(sixingwfPlay);

        //五星玩法
        SubPlayItem wuxinfs = new SubPlayItem();
        wuxinfs.setName("五星复式");
        wuxinfs.setRandomCount(5);
        wuxinfs.setCode(PlayCodeConstants.wxzx_fs);

        List<SubPlayItem> wuxinwanfu = new ArrayList<SubPlayItem>();
        wuxinwanfu.add(wuxinfs);

        PlayItem wuxinwfPlay = new PlayItem();
        wuxinwfPlay.setName("五星玩法");
        wuxinwfPlay.setCode(PlayCodeConstants.wuxing_wf);
        wuxinwfPlay.setRules(wuxinwanfu);
        plays.add(wuxinwfPlay);

        //猜豹子
        SubPlayItem baozhi = new SubPlayItem();
        baozhi.setName("豹子");
        baozhi.setRandomCount(1);
        baozhi.setCode(PlayCodeConstants.baozi);

        SubPlayItem sunzhi = new SubPlayItem();
        sunzhi.setName("顺子");
        sunzhi.setRandomCount(1);
        sunzhi.setCode(PlayCodeConstants.shunzi);

        SubPlayItem duizhi = new SubPlayItem();
        duizhi.setName("对子");
        duizhi.setRandomCount(1);
        duizhi.setCode(PlayCodeConstants.duizi);

        SubPlayItem banshun = new SubPlayItem();
        banshun.setName("半顺");
        banshun.setRandomCount(1);
        banshun.setCode(PlayCodeConstants.banshun);

        SubPlayItem zhaliu = new SubPlayItem();
        zhaliu.setName("杂六");
        zhaliu.setRandomCount(1);
        zhaliu.setCode(PlayCodeConstants.zaliu);

        List<SubPlayItem> caibaozhi = new ArrayList<SubPlayItem>();
        caibaozhi.add(baozhi);
        caibaozhi.add(sunzhi);
        caibaozhi.add(duizhi);
        caibaozhi.add(banshun);
        caibaozhi.add(zhaliu);

        PlayItem caibaozhiPlay = new PlayItem();
        caibaozhiPlay.setName("猜豹子");
        caibaozhiPlay.setCode(PlayCodeConstants.caibaozi);
        caibaozhiPlay.setRules(caibaozhi);
        plays.add(caibaozhiPlay);

        Utils.LOG(TAG,"ssc plays size = "+plays.size());
        return plays;
    }

    //分分彩玩法-赔率版
    private static List<PlayItem> getPeilvFFCPlays() {
        List<PlayItem> plays = new ArrayList<PlayItem>();

        //整合玩法
        SubPlayItem dxds_zhenghe = new SubPlayItem();
        dxds_zhenghe.setName("整合");
        dxds_zhenghe.setCode(PlayCodeConstants.zhenghe);
        dxds_zhenghe.setRandomCount(1);

        List<SubPlayItem> splays = new ArrayList<SubPlayItem>();
        splays.add(dxds_zhenghe);

        PlayItem zhenghe = new PlayItem();
        zhenghe.setName("整合");
        zhenghe.setCode(PlayCodeConstants.zhenghe);
        zhenghe.setRules(splays);
        plays.add(zhenghe);

        //万位
        SubPlayItem dwd_sub = new SubPlayItem();
        dwd_sub.setName("万位");
        dwd_sub.setCode(PlayCodeConstants.wanwei);
        dwd_sub.setRandomCount(1);

        List<SubPlayItem> subRule = new ArrayList<SubPlayItem>();
        subRule.add(dwd_sub);

        PlayItem dwd = new PlayItem();
        dwd.setName("万位");
        dwd.setCode(PlayCodeConstants.wanwei);
        dwd.setRules(subRule);
        plays.add(dwd);

        //千位
        SubPlayItem qian_wei = new SubPlayItem();
        qian_wei.setName("千位");
        qian_wei.setCode(PlayCodeConstants.qianwei);
        qian_wei.setRandomCount(1);

        List<SubPlayItem> qianweiRule = new ArrayList<SubPlayItem>();
        qianweiRule.add(qian_wei);

        PlayItem qianweiItem = new PlayItem();
        qianweiItem.setName("千位");
        qianweiItem.setCode(PlayCodeConstants.qianwei);
        qianweiItem.setRules(qianweiRule);
        plays.add(qianweiItem);

        //百位
        SubPlayItem baiwei = new SubPlayItem();
        baiwei.setName("百位");
        baiwei.setCode(PlayCodeConstants.baiwei);
        baiwei.setRandomCount(1);

        List<SubPlayItem> baiweiRule = new ArrayList<SubPlayItem>();
        baiweiRule.add(baiwei);

        PlayItem baiweiItem = new PlayItem();
        baiweiItem.setName("百位");
        baiweiItem.setCode(PlayCodeConstants.baiwei);
        baiweiItem.setRules(baiweiRule);
        plays.add(baiweiItem);

        //十位
        SubPlayItem shiwei = new SubPlayItem();
        shiwei.setName("十位");
        shiwei.setCode(PlayCodeConstants.shiwei);
        shiwei.setRandomCount(1);

        List<SubPlayItem> shiweiRule = new ArrayList<SubPlayItem>();
        shiweiRule.add(shiwei);

        PlayItem shiweiItem = new PlayItem();
        shiweiItem.setName("十位");
        shiweiItem.setCode(PlayCodeConstants.shiwei);
        shiweiItem.setRules(shiweiRule);
        plays.add(shiweiItem);

        //个位
        SubPlayItem gewei = new SubPlayItem();
        gewei.setName("个位");
        gewei.setCode(PlayCodeConstants.gewei);
        gewei.setRandomCount(1);

        List<SubPlayItem> geweiRule = new ArrayList<SubPlayItem>();
        geweiRule.add(gewei);

        PlayItem geweiItem = new PlayItem();
        geweiItem.setName("个位");
        geweiItem.setCode(PlayCodeConstants.gewei);
        geweiItem.setRules(geweiRule);
        plays.add(geweiItem);

        //和尾数
        SubPlayItem heweishu = new SubPlayItem();
        heweishu.setName("和尾数");
        heweishu.setCode(PlayCodeConstants.heweishu);
        heweishu.setRandomCount(1);

        List<SubPlayItem> heweishuRule = new ArrayList<SubPlayItem>();
        heweishuRule.add(heweishu);

        PlayItem heweishuItem = new PlayItem();
        heweishuItem.setName("和尾数");
        heweishuItem.setCode(PlayCodeConstants.heweishu);
        heweishuItem.setRules(heweishuRule);
        plays.add(heweishuItem);

        //龙虎半
        SubPlayItem longhedou = new SubPlayItem();
        longhedou.setName("龙虎斗");
        longhedou.setCode(PlayCodeConstants.longhudou);
        longhedou.setRandomCount(1);

        List<SubPlayItem> lhdRule = new ArrayList<SubPlayItem>();
        lhdRule.add(longhedou);

        PlayItem lhdItem = new PlayItem();
        lhdItem.setName("龙虎斗");
        lhdItem.setCode(PlayCodeConstants.longhudou);
        lhdItem.setRules(lhdRule);
        plays.add(lhdItem);


        //棋牌
        SubPlayItem baijiale = new SubPlayItem();
        baijiale.setName("百家乐");
        baijiale.setCode(PlayCodeConstants.baijiale);
        baijiale.setRandomCount(1);

        SubPlayItem niuniu = new SubPlayItem();
        niuniu.setName("牛牛");
        niuniu.setCode(PlayCodeConstants.niuniu);
        niuniu.setRandomCount(1);

        SubPlayItem dezhoupuke = new SubPlayItem();
        dezhoupuke.setName("德州扑克");
        dezhoupuke.setCode(PlayCodeConstants.dezhoupuke);
        dezhoupuke.setRandomCount(1);

        SubPlayItem sanggong = new SubPlayItem();
        sanggong.setName("三公");
        sanggong.setCode(PlayCodeConstants.sangong);
        sanggong.setRandomCount(1);


        List<SubPlayItem> qipeiRule = new ArrayList<SubPlayItem>();
        qipeiRule.add(baijiale);
        qipeiRule.add(niuniu);
        qipeiRule.add(dezhoupuke);
        qipeiRule.add(sanggong);

        PlayItem qipeiItem = new PlayItem();
        qipeiItem.setName("棋牌");
        qipeiItem.setCode(PlayCodeConstants.qipai);
        qipeiItem.setRules(qipeiRule);
        plays.add(qipeiItem);

        //和数
        SubPlayItem qiansang = new SubPlayItem();
        qiansang.setName("前三");
        qiansang.setCode(PlayCodeConstants.qiansan);
        qiansang.setRandomCount(1);

        SubPlayItem zhongsang = new SubPlayItem();
        zhongsang.setName("中三");
        zhongsang.setCode(PlayCodeConstants.zhongsan);
        zhongsang.setRandomCount(1);

        SubPlayItem housang = new SubPlayItem();
        housang.setName("后三");
        housang.setCode(PlayCodeConstants.housan);
        housang.setRandomCount(1);


        List<SubPlayItem> heshuRule = new ArrayList<SubPlayItem>();
        heshuRule.add(qiansang);
        heshuRule.add(zhongsang);
        heshuRule.add(housang);

        PlayItem heshuItem = new PlayItem();
        heshuItem.setName("和数");
        heshuItem.setCode(PlayCodeConstants.heshu);
        heshuItem.setRules(heshuRule);
        plays.add(heshuItem);

        //一字
        SubPlayItem quanwu = new SubPlayItem();
        quanwu.setName("全五");
        quanwu.setCode(PlayCodeConstants.yizi_quanwu);
        quanwu.setRandomCount(1);

        SubPlayItem yiziqiansan = new SubPlayItem();
        yiziqiansan.setName("前三");
        yiziqiansan.setCode(PlayCodeConstants.yizi_qiansan);
        yiziqiansan.setRandomCount(1);

        SubPlayItem yizizhongsan = new SubPlayItem();
        yizizhongsan.setName("中三");
        yizizhongsan.setCode(PlayCodeConstants.yizi_zhongsan);
        yizizhongsan.setRandomCount(1);

        SubPlayItem yizihousan = new SubPlayItem();
        yizihousan.setName("后三");
        yizihousan.setCode(PlayCodeConstants.yizi_housan);
        yizihousan.setRandomCount(1);

        List<SubPlayItem> yiziRule = new ArrayList<SubPlayItem>();
        yiziRule.add(quanwu);
        yiziRule.add(yiziqiansan);
        yiziRule.add(yizizhongsan);
        yiziRule.add(yizihousan);


        PlayItem yiziItem = new PlayItem();
        yiziItem.setName("一字");
        yiziItem.setCode(PlayCodeConstants.yizi);
        yiziItem.setRules(yiziRule);
        plays.add(yiziItem);

        //二字
        SubPlayItem erziqiansan = new SubPlayItem();
        erziqiansan.setName("前三");
        erziqiansan.setCode(PlayCodeConstants.erzi_qiansan);
        erziqiansan.setRandomCount(1);

        SubPlayItem erzizhongsan = new SubPlayItem();
        erzizhongsan.setName("中三");
        erzizhongsan.setCode(PlayCodeConstants.erzi_zhongsan);
        erzizhongsan.setRandomCount(1);

        SubPlayItem erzihousan = new SubPlayItem();
        erzihousan.setName("后三");
        erzihousan.setCode(PlayCodeConstants.erzi_housan);
        erzihousan.setRandomCount(1);

        List<SubPlayItem> erziRule = new ArrayList<SubPlayItem>();
        erziRule.add(erziqiansan);
        erziRule.add(erzizhongsan);
        erziRule.add(erzihousan);


        PlayItem erziItem = new PlayItem();
        erziItem.setName("二字");
        erziItem.setCode(PlayCodeConstants.erzi);
        erziItem.setRules(erziRule);
        plays.add(erziItem);

        //三字
        SubPlayItem sanziqiansan = new SubPlayItem();
        sanziqiansan.setName("前三");
        sanziqiansan.setCode(PlayCodeConstants.sanzi_qiansan);
        sanziqiansan.setRandomCount(1);

        SubPlayItem sanzizhongsan = new SubPlayItem();
        sanzizhongsan.setName("中三");
        sanzizhongsan.setCode(PlayCodeConstants.sanzi_zhongsan);
        sanzizhongsan.setRandomCount(1);

        SubPlayItem sanzihousan = new SubPlayItem();
        sanzihousan.setName("后三");
        sanzihousan.setCode(PlayCodeConstants.sanzi_housan);
        sanzihousan.setRandomCount(1);

        List<SubPlayItem> sanziRule = new ArrayList<SubPlayItem>();
        sanziRule.add(sanziqiansan);
        sanziRule.add(sanzizhongsan);
        sanziRule.add(sanzihousan);


        PlayItem sanziItem = new PlayItem();
        sanziItem.setName("三字");
        sanziItem.setCode(PlayCodeConstants.sanzi);
        sanziItem.setRules(sanziRule);
        plays.add(sanziItem);


        //二字定位
        SubPlayItem wanqian = new SubPlayItem();
        wanqian.setName("万仟");
        wanqian.setCode(PlayCodeConstants.wanqian);
        wanqian.setRandomCount(1);

        SubPlayItem wanbai = new SubPlayItem();
        wanbai.setName("万佰");
        wanbai.setCode(PlayCodeConstants.wanbai);
        wanbai.setRandomCount(1);

        SubPlayItem wanshi = new SubPlayItem();
        wanshi.setName("万拾");
        wanshi.setCode(PlayCodeConstants.wanshi);
        wanshi.setRandomCount(1);

        SubPlayItem wange = new SubPlayItem();
        wange.setName("万个");
        wange.setCode(PlayCodeConstants.wange);
        wange.setRandomCount(1);

        SubPlayItem qianbai = new SubPlayItem();
        qianbai.setName("千佰");
        qianbai.setCode(PlayCodeConstants.qianbai);
        qianbai.setRandomCount(1);

        SubPlayItem qianshi = new SubPlayItem();
        qianshi.setName("仟拾");
        qianshi.setCode(PlayCodeConstants.qianshi);
        qianshi.setRandomCount(1);

        SubPlayItem qiange = new SubPlayItem();
        qiange.setName("仟个");
        qiange.setCode(PlayCodeConstants.qiange);
        qiange.setRandomCount(1);

        SubPlayItem baishi = new SubPlayItem();
        baishi.setName("佰拾");
        baishi.setCode(PlayCodeConstants.baishi);
        baishi.setRandomCount(1);

        SubPlayItem baige = new SubPlayItem();
        baige.setName("佰个");
        baige.setCode(PlayCodeConstants.baige);
        baige.setRandomCount(1);

        SubPlayItem shige = new SubPlayItem();
        shige.setName("拾个");
        shige.setCode(PlayCodeConstants.shige);
        shige.setRandomCount(1);

        List<SubPlayItem> erzidingweiRule = new ArrayList<SubPlayItem>();
        erzidingweiRule.add(wanqian);
        erzidingweiRule.add(wanbai);
        erzidingweiRule.add(wanshi);
        erzidingweiRule.add(wange);
        erzidingweiRule.add(qianbai);
        erzidingweiRule.add(qianshi);
        erzidingweiRule.add(qiange);
        erzidingweiRule.add(baishi);
        erzidingweiRule.add(baige);
        erzidingweiRule.add(shige);


        PlayItem erzidingweiItem = new PlayItem();
        erzidingweiItem.setName("二字定位");
        erzidingweiItem.setCode(PlayCodeConstants.erzidingwei);
        erzidingweiItem.setRules(erzidingweiRule);
        plays.add(erzidingweiItem);

        //三字定位
        SubPlayItem sanzidingwei_qiansan = new SubPlayItem();
        sanzidingwei_qiansan.setName("前三");
        sanzidingwei_qiansan.setCode(PlayCodeConstants.sanzidingwei_qiansan);
        sanzidingwei_qiansan.setRandomCount(1);

        SubPlayItem sanzidingwei_zhongsan = new SubPlayItem();
        sanzidingwei_zhongsan.setName("中三");
        sanzidingwei_zhongsan.setCode(PlayCodeConstants.sanzidingwei_zhongsan);
        sanzidingwei_zhongsan.setRandomCount(1);

        SubPlayItem sanzidingwei_housan = new SubPlayItem();
        sanzidingwei_housan.setName("后三");
        sanzidingwei_housan.setCode(PlayCodeConstants.sanzidingwei_housan);
        sanzidingwei_housan.setRandomCount(1);

        List<SubPlayItem> szdwRule = new ArrayList<SubPlayItem>();
        szdwRule.add(sanzidingwei_qiansan);
        szdwRule.add(sanzidingwei_zhongsan);
        szdwRule.add(sanzidingwei_housan);


        PlayItem szdwItem = new PlayItem();
        szdwItem.setName("三字定位");
        szdwItem.setCode(PlayCodeConstants.sanzidingwei);
        szdwItem.setRules(szdwRule);
        plays.add(szdwItem);

        //组选三
        SubPlayItem zxs_qiansan = new SubPlayItem();
        zxs_qiansan.setName("前三");
        zxs_qiansan.setCode(PlayCodeConstants.zuxuansan_qiansan);
        zxs_qiansan.setRandomCount(1);

        SubPlayItem zxs_zhongsan = new SubPlayItem();
        zxs_zhongsan.setName("中三");
        zxs_zhongsan.setCode(PlayCodeConstants.zuxuansan_zhongsan);
        zxs_zhongsan.setRandomCount(1);

        SubPlayItem zxs_housan = new SubPlayItem();
        zxs_housan.setName("后三");
        zxs_housan.setCode(PlayCodeConstants.zuxuansan_housan);
        zxs_housan.setRandomCount(1);

        List<SubPlayItem> zxRule = new ArrayList<SubPlayItem>();
        zxRule.add(zxs_qiansan);
        zxRule.add(zxs_zhongsan);
        zxRule.add(zxs_housan);


        PlayItem zxItem = new PlayItem();
        zxItem.setName("组选三");
        zxItem.setCode(PlayCodeConstants.zuxuan_san);
        zxItem.setRules(zxRule);
        plays.add(zxItem);

        //组选六
        SubPlayItem zxl_qiansan = new SubPlayItem();
        zxl_qiansan.setName("前三");
        zxl_qiansan.setCode(PlayCodeConstants.zuxuanliu_qiansan);
        zxl_qiansan.setRandomCount(1);

        SubPlayItem zxl_zhongsan = new SubPlayItem();
        zxl_zhongsan.setName("中三");
        zxl_zhongsan.setCode(PlayCodeConstants.zuxuanliu_zhongsan);
        zxl_zhongsan.setRandomCount(1);

        SubPlayItem zxl_housan = new SubPlayItem();
        zxl_housan.setName("后三");
        zxl_housan.setCode(PlayCodeConstants.zuxuanliu_housan);
        zxl_housan.setRandomCount(1);

        List<SubPlayItem> zxlRule = new ArrayList<SubPlayItem>();
        zxlRule.add(zxl_qiansan);
        zxlRule.add(zxl_zhongsan);
        zxlRule.add(zxl_housan);


        PlayItem zxlItem = new PlayItem();
        zxlItem.setName("组选六");
        zxlItem.setCode(PlayCodeConstants.zuxuan_liu);
        zxlItem.setRules(zxlRule);
        plays.add(zxlItem);

        //跨度
        SubPlayItem kuadu_qiansan = new SubPlayItem();
        kuadu_qiansan.setName("前三");
        kuadu_qiansan.setCode(PlayCodeConstants.kuadu_qiansan);
        kuadu_qiansan.setRandomCount(1);

        SubPlayItem kuadu_zhongsan = new SubPlayItem();
        kuadu_zhongsan.setName("中三");
        kuadu_zhongsan.setCode(PlayCodeConstants.kuadu_zhongsan);
        kuadu_zhongsan.setRandomCount(1);

        SubPlayItem kuadu_housan = new SubPlayItem();
        kuadu_housan.setName("后三");
        kuadu_housan.setCode(PlayCodeConstants.kuadu_housan);
        kuadu_housan.setRandomCount(1);

        List<SubPlayItem> kuaduRule = new ArrayList<SubPlayItem>();
        kuaduRule.add(kuadu_qiansan);
        kuaduRule.add(kuadu_zhongsan);
        kuaduRule.add(kuadu_housan);


        PlayItem kuaduItem = new PlayItem();
        kuaduItem.setName("跨度");
        kuaduItem.setCode(PlayCodeConstants.kuadu);
        kuaduItem.setRules(kuaduRule);
        plays.add(kuaduItem);

        Utils.LOG(TAG,"ssc plays size = "+plays.size());
        return plays;
    }

    //赛车玩法
    private static List<PlayItem> getSaiChePlays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();

        //定位胆
        SubPlayItem dwd_sub = new SubPlayItem();
        dwd_sub.setName("定位胆");
        dwd_sub.setCode(PlayCodeConstants.dwd);
        dwd_sub.setRandomCount(1);

        List<SubPlayItem> subRule = new ArrayList<SubPlayItem>();
        subRule.add(dwd_sub);

        PlayItem dwd = new PlayItem();
        dwd.setName("定位胆");
        dwd.setCode(PlayCodeConstants.dwd);
        dwd.setRules(subRule);
        playItems.add(dwd);

        //龙虎
        SubPlayItem guanlh = new SubPlayItem();
        guanlh.setName("冠军");
        guanlh.setCode(PlayCodeConstants.longhu_gunjun);
        guanlh.setRandomCount(1);

        SubPlayItem yalh = new SubPlayItem();
        yalh.setName("亚军");
        yalh.setCode(PlayCodeConstants.longhu_yajun);
        yalh.setRandomCount(1);

        SubPlayItem jilh = new SubPlayItem();
        jilh.setName("季军");
        jilh.setCode(PlayCodeConstants.longhu_jijun);
        jilh.setRandomCount(1);

        List<SubPlayItem> longhu_item = new ArrayList<SubPlayItem>();
        longhu_item.add(guanlh);
        longhu_item.add(yalh);
        longhu_item.add(jilh);

        PlayItem longhu = new PlayItem();
        longhu.setName("龙虎");
        longhu.setCode(PlayCodeConstants.longhh);
        longhu.setRules(longhu_item);
        playItems.add(longhu);

        //前一
        SubPlayItem qianyifs = new SubPlayItem();
        qianyifs.setName("前一复式");
        qianyifs.setCode(PlayCodeConstants.q1zx_fs);
        qianyifs.setRandomCount(1);

        List<SubPlayItem> q1list = new ArrayList<SubPlayItem>();
        q1list.add(qianyifs);

        PlayItem qiyiItem = new PlayItem();
        qiyiItem.setName("前一");
        qiyiItem.setCode(PlayCodeConstants.q1_str);
        qiyiItem.setRules(q1list);
        playItems.add(qiyiItem);

        //前二
        SubPlayItem qianerfs = new SubPlayItem();
        qianerfs.setName("前二复式");
        qianerfs.setCode(PlayCodeConstants.q2zx_fs);
        qianerfs.setRandomCount(2);

        List<SubPlayItem> q2list = new ArrayList<SubPlayItem>();
        q2list.add(qianerfs);

        PlayItem qierItem = new PlayItem();
        qierItem.setName("前二");
        qierItem.setCode(PlayCodeConstants.q2_str);
        qierItem.setRules(q2list);
        playItems.add(qierItem);

        //冠亚和
        SubPlayItem dxds = new SubPlayItem();
        dxds.setName("大小单双");
        dxds.setCode(PlayCodeConstants.dxds);
        dxds.setRandomCount(1);

        SubPlayItem yghz = new SubPlayItem();
        yghz.setName("冠亚和值");
        yghz.setCode(PlayCodeConstants.gyhz);
        yghz.setRandomCount(1);

        List<SubPlayItem> gyhlist = new ArrayList<SubPlayItem>();
        gyhlist.add(dxds);
        gyhlist.add(yghz);

        PlayItem gyhItem = new PlayItem();
        gyhItem.setName("冠亚和");
        gyhItem.setCode(PlayCodeConstants.gyh_str);
        gyhItem.setRules(gyhlist);
        playItems.add(gyhItem);

        //前三
        SubPlayItem qsfs = new SubPlayItem();
        qsfs.setName("前三复式");
        qsfs.setCode(PlayCodeConstants.q3zx_fs);
        qsfs.setRandomCount(3);


        List<SubPlayItem> qsfslist = new ArrayList<SubPlayItem>();
        qsfslist.add(qsfs);

        PlayItem qsfsItem = new PlayItem();
        qsfsItem.setName("前三");
        qsfsItem.setCode(PlayCodeConstants.qiansan);
        qsfsItem.setRules(qsfslist);
        playItems.add(qsfsItem);

        return playItems;
    }

    //赔率版-赛车玩法
    private static List<PlayItem> getPeilvSaiChePlays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();

        //冠亚和
        SubPlayItem gyh = new SubPlayItem();
        gyh.setName("冠.亚军");
        gyh.setCode(PlayCodeConstants.guan_yajun);
        gyh.setRandomCount(1);

        List<SubPlayItem> subRule = new ArrayList<SubPlayItem>();
        subRule.add(gyh);

        PlayItem dwd = new PlayItem();
        dwd.setName("冠.亚军");
        dwd.setCode(PlayCodeConstants.guan_yajun);
        dwd.setRules(subRule);
        playItems.add(dwd);

        //单号1-10
        SubPlayItem danhao110 = new SubPlayItem();
        danhao110.setName("单号1-10");
        danhao110.setCode(PlayCodeConstants.danhao1_10);
        danhao110.setRandomCount(1);

        List<SubPlayItem> dh110Rule = new ArrayList<SubPlayItem>();
        dh110Rule.add(danhao110);

        PlayItem dh110Item = new PlayItem();
        dh110Item.setName("单号1-10");
        dh110Item.setCode(PlayCodeConstants.danhao1_10);
        dh110Item.setRules(dh110Rule);
        playItems.add(dh110Item);

        //双面盘
        SubPlayItem smp = new SubPlayItem();
        smp.setName("双面盘");
        smp.setCode(PlayCodeConstants.shuangmianpan);
        smp.setRandomCount(1);

        List<SubPlayItem> smpRule = new ArrayList<SubPlayItem>();
        smpRule.add(smp);

        PlayItem smpItem = new PlayItem();
        smpItem.setName("双面盘");
        smpItem.setCode(PlayCodeConstants.shuangmianpan);
        smpItem.setRules(smpRule);
        playItems.add(smpItem);

        return playItems;
    }

    //福彩3D，排列3
    private static List<PlayItem> getFC3DPlays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();

        //直选
        SubPlayItem zxfs = new SubPlayItem();
        zxfs.setName("复式");
        zxfs.setCode(PlayCodeConstants.zhx_fs);
        zxfs.setRandomCount(3);

        List<SubPlayItem> subRule = new ArrayList<SubPlayItem>();
        subRule.add(zxfs);

        PlayItem dwd = new PlayItem();
        dwd.setName("直选");
        dwd.setCode(PlayCodeConstants.zhi_xuan_str);
        dwd.setRules(subRule);
        playItems.add(dwd);

        //组选
        SubPlayItem zuliu = new SubPlayItem();
        zuliu.setName("组六");
        zuliu.setCode(PlayCodeConstants.zux_z6);
        zuliu.setRandomCount(3);

        SubPlayItem zusang = new SubPlayItem();
        zusang.setName("组三");
        zusang.setCode(PlayCodeConstants.zux_z3);
        zusang.setRandomCount(2);

        List<SubPlayItem> longhu_item = new ArrayList<SubPlayItem>();
        longhu_item.add(zuliu);
        longhu_item.add(zusang);

        PlayItem zhuxuan = new PlayItem();
        zhuxuan.setName("组选");
        zhuxuan.setCode(PlayCodeConstants.zhuxuan_str);
        zhuxuan.setRules(longhu_item);
        playItems.add(zhuxuan);

        //不定位
        SubPlayItem ermabudingwei = new SubPlayItem();
        ermabudingwei.setName("二码不定位");
        ermabudingwei.setCode(PlayCodeConstants.bdw_2m);
        ermabudingwei.setRandomCount(2);

        SubPlayItem yimabudingwei = new SubPlayItem();
        yimabudingwei.setName("一码不定位");
        yimabudingwei.setCode(PlayCodeConstants.bdw_1m);
        yimabudingwei.setRandomCount(1);

        List<SubPlayItem> q1list = new ArrayList<SubPlayItem>();
        q1list.add(ermabudingwei);
        q1list.add(yimabudingwei);

        PlayItem qiyiItem = new PlayItem();
        qiyiItem.setName("不定位");
        qiyiItem.setCode(PlayCodeConstants.bdw);
        qiyiItem.setRules(q1list);
        playItems.add(qiyiItem);

        //二码
        SubPlayItem q2zx = new SubPlayItem();
        q2zx.setName("前二直选");
        q2zx.setCode(PlayCodeConstants.q2zx_fs);
        q2zx.setRandomCount(2);

        SubPlayItem q2zhux = new SubPlayItem();
        q2zhux.setName("前二组选");
        q2zhux.setCode(PlayCodeConstants.em_q2zux);
        q2zhux.setRandomCount(2);

        SubPlayItem h2zx = new SubPlayItem();
        h2zx.setName("后二直选");
        h2zx.setCode(PlayCodeConstants.h2zx_fs);
        h2zx.setRandomCount(2);

        SubPlayItem h2zhux = new SubPlayItem();
        h2zhux.setName("后二组选");
        h2zhux.setCode(PlayCodeConstants.em_h2zux);
        h2zhux.setRandomCount(2);

        List<SubPlayItem> q2list = new ArrayList<SubPlayItem>();
        q2list.add(q2zx);
        q2list.add(q2zhux);
        q2list.add(h2zx);
        q2list.add(h2zhux);

        PlayItem qierItem = new PlayItem();
        qierItem.setName("二码");
        qierItem.setCode(PlayCodeConstants.er_ma_str);
        qierItem.setRules(q2list);
        playItems.add(qierItem);

        //大小单双
        SubPlayItem dxds_q2 = new SubPlayItem();
        dxds_q2.setName("前二");
        dxds_q2.setCode(PlayCodeConstants.dxds_q2);
        dxds_q2.setRandomCount(2);

        SubPlayItem dxds_h2 = new SubPlayItem();
        dxds_h2.setName("后二");
        dxds_h2.setCode(PlayCodeConstants.dxds_h2);
        dxds_h2.setRandomCount(2);

        List<SubPlayItem> gyhlist = new ArrayList<SubPlayItem>();
        gyhlist.add(dxds_q2);
        gyhlist.add(dxds_h2);

        PlayItem gyhItem = new PlayItem();
        gyhItem.setName("大小单双");
        gyhItem.setCode(PlayCodeConstants.dxds);
        gyhItem.setRules(gyhlist);
        playItems.add(gyhItem);

        //定位胆
        SubPlayItem dwd_dwd = new SubPlayItem();
        dwd_dwd.setName("定位胆");
        dwd_dwd.setCode(PlayCodeConstants.dwd);
        dwd_dwd.setRandomCount(1);

        List<SubPlayItem> qsfslist = new ArrayList<SubPlayItem>();
        qsfslist.add(dwd_dwd);

        PlayItem qsfsItem = new PlayItem();
        qsfsItem.setName("定位胆");
        qsfsItem.setCode(PlayCodeConstants.dwd);
        qsfsItem.setRules(qsfslist);
        playItems.add(qsfsItem);

        return playItems;
    }

    //PC蛋蛋，加拿大28
    private static List<PlayItem> getPCEggPlays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();

        //定位胆
        SubPlayItem dwd_dwd = new SubPlayItem();
        dwd_dwd.setName("定位胆");
        dwd_dwd.setCode(PlayCodeConstants.dwd);
        dwd_dwd.setRandomCount(1);

        List<SubPlayItem> qsfslist = new ArrayList<SubPlayItem>();
        qsfslist.add(dwd_dwd);

        PlayItem qsfsItem = new PlayItem();
        qsfsItem.setName("定位胆");
        qsfsItem.setCode(PlayCodeConstants.dwd);
        qsfsItem.setRules(qsfslist);
        playItems.add(qsfsItem);

        //不定位
        SubPlayItem bdwd = new SubPlayItem();
        bdwd.setName("不定位胆");
        bdwd.setCode(PlayCodeConstants.bdw);
        bdwd.setRandomCount(1);

        List<SubPlayItem> q1list = new ArrayList<SubPlayItem>();
        q1list.add(bdwd);

        PlayItem qiyiItem = new PlayItem();
        qiyiItem.setName("不定胆");
        qiyiItem.setCode(PlayCodeConstants.bdw);
        qiyiItem.setRules(q1list);
        playItems.add(qiyiItem);

        //三星玩法
        SubPlayItem sxzx = new SubPlayItem();
        sxzx.setName("三星组选");
        sxzx.setCode(PlayCodeConstants.sxzx);
        sxzx.setRandomCount(3);

        SubPlayItem sxfs = new SubPlayItem();
        sxfs.setName("三星复式");
        sxfs.setCode(PlayCodeConstants.sxfs);
        sxfs.setRandomCount(3);

        List<SubPlayItem> subRule = new ArrayList<SubPlayItem>();
        subRule.add(sxzx);
        subRule.add(sxfs);

        PlayItem sxwf_item = new PlayItem();
        sxwf_item.setName("三星玩法");
        sxwf_item.setCode(PlayCodeConstants.sxwf_pcegg);
        sxwf_item.setRules(subRule);
        playItems.add(sxwf_item);

        //二星玩法
        SubPlayItem q2fs = new SubPlayItem();
        q2fs.setName("前二复式");
        q2fs.setCode(PlayCodeConstants.q2zx_fs);
        q2fs.setRandomCount(2);

        SubPlayItem q2zx = new SubPlayItem();
        q2zx.setName("前二组选");
        q2zx.setCode(PlayCodeConstants.q2zx);
        q2zx.setRandomCount(2);

        SubPlayItem h2fs = new SubPlayItem();
        h2fs.setName("后二复式");
        h2fs.setCode(PlayCodeConstants.h2zx_fs);
        h2fs.setRandomCount(2);

        SubPlayItem h2zx = new SubPlayItem();
        h2zx.setName("后二组选");
        h2zx.setCode(PlayCodeConstants.h2zx);
        h2zx.setRandomCount(2);

        List<SubPlayItem> exwfRule = new ArrayList<SubPlayItem>();
        exwfRule.add(q2fs);
        exwfRule.add(q2zx);
        exwfRule.add(h2fs);
        exwfRule.add(h2zx);

        PlayItem exwf_item = new PlayItem();
        exwf_item.setName("二星玩法");
        exwf_item.setCode(PlayCodeConstants.exwf_str);
        exwf_item.setRules(exwfRule);
        playItems.add(exwf_item);

        //和值
        SubPlayItem dxds = new SubPlayItem();
        dxds.setName("大小单双");
        dxds.setCode(PlayCodeConstants.dxds);
        dxds.setRandomCount(1);

        SubPlayItem hezi = new SubPlayItem();
        hezi.setName("和值");
        hezi.setCode(PlayCodeConstants.hz);
        hezi.setRandomCount(1);

        List<SubPlayItem> hzRule = new ArrayList<SubPlayItem>();
        hzRule.add(dxds);
        hzRule.add(hezi);

        PlayItem hz_item = new PlayItem();
        hz_item.setName("和值");
        hz_item.setCode(PlayCodeConstants.hz);
        hz_item.setRules(hzRule);
        playItems.add(hz_item);

        return playItems;
    }

    //11选5
    private static List<PlayItem> get11x5Plays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();

        //任选复式
        SubPlayItem r8z5 = new SubPlayItem();
        r8z5.setName("任八中五");
        r8z5.setCode(PlayCodeConstants.rxfs_rx8z5);
        r8z5.setRandomCount(8);

        SubPlayItem r7z5 = new SubPlayItem();
        r7z5.setName("任七中五");
        r7z5.setCode(PlayCodeConstants.rxfs_rx7z5);
        r7z5.setRandomCount(7);

        SubPlayItem r6z5 = new SubPlayItem();
        r6z5.setName("任六中五");
        r6z5.setCode(PlayCodeConstants.rxfs_rx6z5);
        r6z5.setRandomCount(6);

        SubPlayItem r4z4 = new SubPlayItem();
        r4z4.setName("任四中四");
        r4z4.setCode(PlayCodeConstants.rxfs_rx4z4);
        r4z4.setRandomCount(4);

        SubPlayItem r3z3 = new SubPlayItem();
        r3z3.setName("任三中三");
        r3z3.setCode(PlayCodeConstants.rxfs_rx3z3);
        r3z3.setRandomCount(3);

        SubPlayItem r2z2 = new SubPlayItem();
        r2z2.setName("任二中二");
        r2z2.setCode(PlayCodeConstants.rxfs_rx2z2);
        r2z2.setRandomCount(2);

        SubPlayItem r1z1 = new SubPlayItem();
        r1z1.setName("任一中一");
        r1z1.setCode(PlayCodeConstants.rxfs_rx1z1);
        r1z1.setRandomCount(1);

        List<SubPlayItem> subRule = new ArrayList<SubPlayItem>();
        subRule.add(r8z5);
        subRule.add(r7z5);
        subRule.add(r6z5);
        subRule.add(r4z4);
        subRule.add(r3z3);
        subRule.add(r2z2);
        subRule.add(r1z1);

        PlayItem dwd = new PlayItem();
        dwd.setName("任选复式");
        dwd.setCode(PlayCodeConstants.rxfs);
        dwd.setRules(subRule);
        playItems.add(dwd);

        //定位胆
        SubPlayItem dwd_dwd = new SubPlayItem();
        dwd_dwd.setName("定位胆");
        dwd_dwd.setCode(PlayCodeConstants.dwd);
        dwd_dwd.setRandomCount(1);

        List<SubPlayItem> qsfslist = new ArrayList<SubPlayItem>();
        qsfslist.add(dwd_dwd);

        PlayItem qsfsItem = new PlayItem();
        qsfsItem.setName("定位胆");
        qsfsItem.setCode(PlayCodeConstants.dwd);
        qsfsItem.setRules(qsfslist);
        playItems.add(qsfsItem);

        //二码
        SubPlayItem q2fs = new SubPlayItem();
        q2fs.setName("前二复式");
        q2fs.setCode(PlayCodeConstants.q2zx_fs);
        q2fs.setRandomCount(2);

        SubPlayItem q2zhux = new SubPlayItem();
        q2zhux.setName("前二组选");
        q2zhux.setCode(PlayCodeConstants.q2zx);
        q2zhux.setRandomCount(2);

        SubPlayItem h2fs = new SubPlayItem();
        h2fs.setName("后二复式");
        h2fs.setCode(PlayCodeConstants.h2zx_fs);
        h2fs.setRandomCount(2);

        SubPlayItem h2zhux = new SubPlayItem();
        h2zhux.setName("后二组选");
        h2zhux.setCode(PlayCodeConstants.h2zx);
        h2zhux.setRandomCount(2);

        List<SubPlayItem> q2list = new ArrayList<SubPlayItem>();
        q2list.add(q2fs);
        q2list.add(q2zhux);
        q2list.add(h2fs);
        q2list.add(h2zhux);

        PlayItem qierItem = new PlayItem();
        qierItem.setName("二码");
        qierItem.setCode(PlayCodeConstants.er_ma_str);
        qierItem.setRules(q2list);
        playItems.add(qierItem);

        //三码
        SubPlayItem qsfs_sangma = new SubPlayItem();
        qsfs_sangma.setName("前三复式");
        qsfs_sangma.setCode(PlayCodeConstants.q3zx_fs);
        qsfs_sangma.setRandomCount(3);

        SubPlayItem qszx = new SubPlayItem();
        qszx.setName("前三组选");
        qszx.setCode(PlayCodeConstants.q3zx);
        qszx.setRandomCount(3);

        SubPlayItem zsfs = new SubPlayItem();
        zsfs.setName("中三复式");
        zsfs.setCode(PlayCodeConstants.z3zx_fs);
        zsfs.setRandomCount(3);

        SubPlayItem zszhuxuan = new SubPlayItem();
        zszhuxuan.setName("中三组选");
        zszhuxuan.setCode(PlayCodeConstants.z3zx);
        zszhuxuan.setRandomCount(3);

        SubPlayItem h3fs = new SubPlayItem();
        h3fs.setName("后三复式");
        h3fs.setCode(PlayCodeConstants.h3zx_fs);
        h3fs.setRandomCount(3);

        SubPlayItem h3zx = new SubPlayItem();
        h3zx.setName("后三组选");
        h3zx.setCode(PlayCodeConstants.h3zx);
        h3zx.setRandomCount(3);


        List<SubPlayItem> gyhlist = new ArrayList<SubPlayItem>();
        gyhlist.add(qsfs_sangma);
        gyhlist.add(qszx);
        gyhlist.add(zsfs);
        gyhlist.add(zszhuxuan);
        gyhlist.add(h3fs);
        gyhlist.add(h3zx);

        PlayItem gyhItem = new PlayItem();
        gyhItem.setName("三码");
        gyhItem.setCode(PlayCodeConstants.shang_ma_str);
        gyhItem.setRules(gyhlist);
        playItems.add(gyhItem);

        return playItems;
    }

    //快三
    private static List<PlayItem> getKuai3Plays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();

        //和值

        SubPlayItem hezi = new SubPlayItem();
        hezi.setName("和值");
        hezi.setCode(PlayCodeConstants.hz);
        hezi.setRandomCount(1);

        SubPlayItem dxds = new SubPlayItem();
        dxds.setName("大小单双");
        dxds.setCode(PlayCodeConstants.dxds);
        dxds.setRandomCount(1);

        List<SubPlayItem> hzRule = new ArrayList<SubPlayItem>();
        hzRule.add(hezi);
        hzRule.add(dxds);

        PlayItem hz_item = new PlayItem();
        hz_item.setName("和值");
        hz_item.setCode(PlayCodeConstants.hz);
        hz_item.setRules(hzRule);
        playItems.add(hz_item);

        //三同号通选
        SubPlayItem sthtx = new SubPlayItem();
        sthtx.setName("三同号通选");
        sthtx.setCode(PlayCodeConstants.sthtx);
        sthtx.setRandomCount(1);

        List<SubPlayItem> qsfslist = new ArrayList<SubPlayItem>();
        qsfslist.add(sthtx);

        PlayItem qsfsItem = new PlayItem();
        qsfsItem.setName("三同号通选");
        qsfsItem.setCode(PlayCodeConstants.sthtx);
        qsfsItem.setRules(qsfslist);
        playItems.add(qsfsItem);

        //三同号单选
        SubPlayItem sthdx = new SubPlayItem();
        sthdx.setName("三同号单选");
        sthdx.setCode(PlayCodeConstants.sthdx);
        sthdx.setRandomCount(1);

        List<SubPlayItem> slhdxList = new ArrayList<SubPlayItem>();
        slhdxList.add(sthdx);

        PlayItem item = new PlayItem();
        item.setName("三同号单选");
        item.setCode(PlayCodeConstants.sthdx);
        item.setRules(slhdxList);
        playItems.add(item);

        //三不同号
        SubPlayItem sbth = new SubPlayItem();
        sbth.setName("三不同号");
        sbth.setCode(PlayCodeConstants.sbtx);
        sbth.setRandomCount(3);

        List<SubPlayItem> sbthList = new ArrayList<SubPlayItem>();
        sbthList.add(sbth);

        PlayItem sbthItem = new PlayItem();
        sbthItem.setName("三不同号");
        sbthItem.setCode(PlayCodeConstants.sbtx);
        sbthItem.setRules(sbthList);
        playItems.add(sbthItem);

        //三连号通选
        SubPlayItem slhtx = new SubPlayItem();
        slhtx.setName("三连号通选");
        slhtx.setCode(PlayCodeConstants.slhtx);
        slhtx.setRandomCount(1);

        List<SubPlayItem> slhtxList = new ArrayList<SubPlayItem>();
        slhtxList.add(slhtx);

        PlayItem slhtxItem = new PlayItem();
        slhtxItem.setName("三连号通选");
        slhtxItem.setCode(PlayCodeConstants.slhtx);
        slhtxItem.setRules(slhtxList);
        playItems.add(slhtxItem);

        //二同号复选
        SubPlayItem ethfx = new SubPlayItem();
        ethfx.setName("二同号复选");
        ethfx.setCode(PlayCodeConstants.ethfx);
        ethfx.setRandomCount(1);

        List<SubPlayItem> ethfxList = new ArrayList<SubPlayItem>();
        ethfxList.add(ethfx);

        PlayItem ethfxItem = new PlayItem();
        ethfxItem.setName("二同号复选");
        ethfxItem.setCode(PlayCodeConstants.ethfx);
        ethfxItem.setRules(ethfxList);
        playItems.add(ethfxItem);

        //二不同号
        SubPlayItem ebth = new SubPlayItem();
        ebth.setName("二不同号");
        ebth.setCode(PlayCodeConstants.ebth);
        ebth.setRandomCount(2);

        List<SubPlayItem> ebthList = new ArrayList<SubPlayItem>();
        ebthList.add(ebth);

        PlayItem ebthItem = new PlayItem();
        ebthItem.setName("二不同号");
        ebthItem.setCode(PlayCodeConstants.ebth);
        ebthItem.setRules(ebthList);
        playItems.add(ebthItem);

        return playItems;
    }

    //赔率版-快三
    private static List<PlayItem> getPeilvKuai3Plays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();
        //骰宝
        SubPlayItem shaibao = new SubPlayItem();
        shaibao.setName("大小骰宝");
        shaibao.setCode(PlayCodeConstants.daxiaoshaibao);
        shaibao.setRandomCount(1);

        List<SubPlayItem> shaibaoRule = new ArrayList<SubPlayItem>();
        shaibaoRule.add(shaibao);

        PlayItem hz_item = new PlayItem();
        hz_item.setName("骰宝");
        hz_item.setCode(PlayCodeConstants.daxiaoshaibao);
        hz_item.setRules(shaibaoRule);
        playItems.add(hz_item);

        return playItems;
    }

    //赔率版-pc eggegg,加拿大28
    private static List<PlayItem> getPeilvPC28Plays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();
        //幸运28
        SubPlayItem shaibao = new SubPlayItem();
        shaibao.setName("幸运28");
        shaibao.setCode(PlayCodeConstants.xingyun28);
        shaibao.setRandomCount(1);

        List<SubPlayItem> shaibaoRule = new ArrayList<SubPlayItem>();
        shaibaoRule.add(shaibao);

        PlayItem dxds = new PlayItem();
        dxds.setName("幸运28");
        dxds.setCode(PlayCodeConstants.xingyun28);
        dxds.setRules(shaibaoRule);

        playItems.add(dxds);

        return playItems;
    }

    //赔率版-快乐十分，重庆幸运农场
    private static List<PlayItem> getPeilvKuaile10FenPlays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();
        //单球1-8
        SubPlayItem shaibao = new SubPlayItem();
        shaibao.setName("单球1-8");
        shaibao.setCode(PlayCodeConstants.danqiu1_8);
        shaibao.setRandomCount(1);

        List<SubPlayItem> shaibaoRule = new ArrayList<SubPlayItem>();
        shaibaoRule.add(shaibao);

        PlayItem dxds = new PlayItem();
        dxds.setName("单球1-8");
        dxds.setCode(PlayCodeConstants.danqiu1_8);
        dxds.setRules(shaibaoRule);
        playItems.add(dxds);

        //第一球
        SubPlayItem dyq = new SubPlayItem();
        dyq.setName("第一球");
        dyq.setCode(PlayCodeConstants.diyiqiu);
        dyq.setRandomCount(1);

        List<SubPlayItem> dyqRule = new ArrayList<SubPlayItem>();
        dyqRule.add(dyq);

        PlayItem dyqItem = new PlayItem();
        dyqItem.setName("第一球");
        dyqItem.setCode(PlayCodeConstants.diyiqiu);
        dyqItem.setRules(dyqRule);
        playItems.add(dyqItem);

        //第二球
        SubPlayItem deq = new SubPlayItem();
        deq.setName("第二球");
        deq.setCode(PlayCodeConstants.dierqiu);
        deq.setRandomCount(1);

        List<SubPlayItem> deqRule = new ArrayList<SubPlayItem>();
        deqRule.add(deq);

        PlayItem deqItem = new PlayItem();
        deqItem.setName("第二球");
        deqItem.setCode(PlayCodeConstants.dierqiu);
        deqItem.setRules(deqRule);
        playItems.add(deqItem);

        //第三球
        SubPlayItem dsq = new SubPlayItem();
        dsq.setName("第三球");
        dsq.setCode(PlayCodeConstants.disanqiu);
        dsq.setRandomCount(1);

        List<SubPlayItem> dsqRule = new ArrayList<SubPlayItem>();
        dsqRule.add(dsq);

        PlayItem dsqItem = new PlayItem();
        dsqItem.setName("第三球");
        dsqItem.setCode(PlayCodeConstants.disanqiu);
        dsqItem.setRules(dsqRule);
        playItems.add(dsqItem);

        //第四球
        SubPlayItem dshiq = new SubPlayItem();
        dshiq.setName("第四球");
        dshiq.setCode(PlayCodeConstants.disiqiu);
        dshiq.setRandomCount(1);

        List<SubPlayItem> dshiqRule = new ArrayList<SubPlayItem>();
        dshiqRule.add(dshiq);

        PlayItem dshiqItem = new PlayItem();
        dshiqItem.setName("第四球");
        dshiqItem.setCode(PlayCodeConstants.disiqiu);
        dshiqItem.setRules(dshiqRule);
        playItems.add(dshiqItem);

        //第五球
        SubPlayItem dwuq = new SubPlayItem();
        dwuq.setName("第五球");
        dwuq.setCode(PlayCodeConstants.diwuqiu);
        dwuq.setRandomCount(1);

        List<SubPlayItem> dwuqRule = new ArrayList<SubPlayItem>();
        dwuqRule.add(dwuq);

        PlayItem dwuqItem = new PlayItem();
        dwuqItem.setName("第五球");
        dwuqItem.setCode(PlayCodeConstants.diwuqiu);
        dwuqItem.setRules(dwuqRule);
        playItems.add(dwuqItem);

        //第六球
        SubPlayItem dliuq = new SubPlayItem();
        dliuq.setName("第六球");
        dliuq.setCode(PlayCodeConstants.diliuqiu);
        dliuq.setRandomCount(1);

        List<SubPlayItem> dliuqRule = new ArrayList<SubPlayItem>();
        dliuqRule.add(dliuq);

        PlayItem dliuqItem = new PlayItem();
        dliuqItem.setName("第六球");
        dliuqItem.setCode(PlayCodeConstants.diliuqiu);
        dliuqItem.setRules(dliuqRule);
        playItems.add(dliuqItem);

        //第七球
        SubPlayItem dqiq = new SubPlayItem();
        dqiq.setName("第七球");
        dqiq.setCode(PlayCodeConstants.diqiqiu);
        dqiq.setRandomCount(1);

        List<SubPlayItem> dqiqRule = new ArrayList<SubPlayItem>();
        dqiqRule.add(dqiq);

        PlayItem dqiqItem = new PlayItem();
        dqiqItem.setName("第七球");
        dqiqItem.setCode(PlayCodeConstants.diqiqiu);
        dqiqItem.setRules(dqiqRule);
        playItems.add(dqiqItem);

        //第八球
        SubPlayItem dbaq = new SubPlayItem();
        dbaq.setName("第八球");
        dbaq.setCode(PlayCodeConstants.dibaqiu);
        dbaq.setRandomCount(1);

        List<SubPlayItem> dbaqRule = new ArrayList<SubPlayItem>();
        dbaqRule.add(dbaq);

        PlayItem dbaqItem = new PlayItem();
        dbaqItem.setName("第八球");
        dbaqItem.setCode(PlayCodeConstants.dibaqiu);
        dbaqItem.setRules(dbaqRule);
        playItems.add(dbaqItem);

        //连码
        SubPlayItem xerx = new SubPlayItem();
        xerx.setName("选二任选");
        xerx.setCode(PlayCodeConstants.xuanerrenxuan);
        xerx.setRandomCount(1);

        SubPlayItem xelz = new SubPlayItem();
        xelz.setName("选二连组");
        xelz.setCode(PlayCodeConstants.xuanerlianzu);
        xelz.setRandomCount(1);

        SubPlayItem xelzhi = new SubPlayItem();
        xelzhi.setName("选二连直");
        xelzhi.setCode(PlayCodeConstants.xuanerlianzhi);
        xelzhi.setRandomCount(1);

        SubPlayItem xsrx = new SubPlayItem();
        xsrx.setName("选三任选");
        xsrx.setCode(PlayCodeConstants.xuansanrenxuan);
        xsrx.setRandomCount(1);

        SubPlayItem xsqz = new SubPlayItem();
        xsqz.setName("选三前组");
        xsqz.setCode(PlayCodeConstants.xuansanqianzu);
        xsqz.setRandomCount(1);

        SubPlayItem xsqzhi = new SubPlayItem();
        xsqzhi.setName("选三前直");
        xsqzhi.setCode(PlayCodeConstants.xuansanqianzhi);
        xsqzhi.setRandomCount(1);

        SubPlayItem xsrxuan = new SubPlayItem();
        xsrxuan.setName("选四任选");
        xsrxuan.setCode(PlayCodeConstants.xuansirenxuan);
        xsrxuan.setRandomCount(1);

        SubPlayItem xwrxuan = new SubPlayItem();
        xwrxuan.setName("选五任选");
        xwrxuan.setCode(PlayCodeConstants.xuanwurenxuan);
        xwrxuan.setRandomCount(1);


        List<SubPlayItem> lianmaRule = new ArrayList<SubPlayItem>();
        lianmaRule.add(xerx);
        lianmaRule.add(xelz);
        lianmaRule.add(xelzhi);
        lianmaRule.add(xsrx);
        lianmaRule.add(xsqz);
        lianmaRule.add(xsqzhi);
        lianmaRule.add(xsrxuan);
        lianmaRule.add(xwrxuan);

        PlayItem lianmaItem = new PlayItem();
        lianmaItem.setName("连码");
        lianmaItem.setCode(PlayCodeConstants.lianma);
        lianmaItem.setRules(lianmaRule);
        playItems.add(lianmaItem);

        return playItems;
    }

    //赔率版--11x5
    private static List<PlayItem> getPeilv11x5Plays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();
        //双面盘
        SubPlayItem shaibao = new SubPlayItem();
        shaibao.setName("双面盘");
        shaibao.setCode(PlayCodeConstants.syx5_shuangmianpan);
        shaibao.setRandomCount(1);

        List<SubPlayItem> shaibaoRule = new ArrayList<SubPlayItem>();
        shaibaoRule.add(shaibao);

        PlayItem dxds = new PlayItem();
        dxds.setName("双面盘");
        dxds.setCode(PlayCodeConstants.syx5_shuangmianpan);
        dxds.setRules(shaibaoRule);
        playItems.add(dxds);

        //1-5球
        SubPlayItem dyq = new SubPlayItem();
        dyq.setName("1~5球");
        dyq.setCode(PlayCodeConstants.syx5_15qiu);
        dyq.setRandomCount(1);

        List<SubPlayItem> dyqRule = new ArrayList<SubPlayItem>();
        dyqRule.add(dyq);

        PlayItem dyqItem = new PlayItem();
        dyqItem.setName("1~5球");
        dyqItem.setCode(PlayCodeConstants.syx5_15qiu);
        dyqItem.setRules(dyqRule);
        playItems.add(dyqItem);

        return playItems;
    }

    //赔率版--福彩3D,排列3
    private static List<PlayItem> getPeilvfucai3dPlays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();
        //1-3球
        SubPlayItem shaibao = new SubPlayItem();
        shaibao.setName("1~3球");
        shaibao.setCode(PlayCodeConstants.pl3_13qiu);
        shaibao.setRandomCount(1);

        List<SubPlayItem> shaibaoRule = new ArrayList<SubPlayItem>();
        shaibaoRule.add(shaibao);

        PlayItem dxds = new PlayItem();
        dxds.setName("1~3球");
        dxds.setCode(PlayCodeConstants.pl3_13qiu);
        dxds.setRules(shaibaoRule);
        playItems.add(dxds);

        //整合
        SubPlayItem dyq = new SubPlayItem();
        dyq.setName("整合");
        dyq.setCode(PlayCodeConstants.pl3_zhenghe);
        dyq.setRandomCount(1);

        List<SubPlayItem> dyqRule = new ArrayList<SubPlayItem>();
        dyqRule.add(dyq);

        PlayItem dyqItem = new PlayItem();
        dyqItem.setName("整合");
        dyqItem.setCode(PlayCodeConstants.pl3_zhenghe);
        dyqItem.setRules(dyqRule);
        playItems.add(dyqItem);

        return playItems;
    }

    //赔率版--十分六合彩，六合彩
    private static List<PlayItem> getPeilvLiuHeCaiPlays() {

        List<PlayItem> playItems = new ArrayList<PlayItem>();
        //特码
        SubPlayItem temaA = new SubPlayItem();
        temaA.setName("特码A");
        temaA.setCode(PlayCodeConstants.tm_a);
        temaA.setRandomCount(1);

        SubPlayItem temaB = new SubPlayItem();
        temaB.setName("特码B");
        temaB.setCode(PlayCodeConstants.tm_b);
        temaB.setRandomCount(1);

        List<SubPlayItem> temaRule = new ArrayList<SubPlayItem>();
        temaRule.add(temaA);
        temaRule.add(temaB);

        PlayItem temaItem = new PlayItem();
        temaItem.setName("特码");
        temaItem.setCode(PlayCodeConstants.tema);
        temaItem.setRules(temaRule);
        playItems.add(temaItem);

        //正码
        SubPlayItem zhenmaA = new SubPlayItem();
        zhenmaA.setName("正码A");
        zhenmaA.setCode(PlayCodeConstants.zm_a);
        zhenmaA.setRandomCount(1);

        SubPlayItem zhenmaB = new SubPlayItem();
        zhenmaB.setName("正码B");
        zhenmaB.setCode(PlayCodeConstants.zm_b);
        zhenmaB.setRandomCount(1);

        List<SubPlayItem> zhenmaRule = new ArrayList<SubPlayItem>();
        zhenmaRule.add(zhenmaA);
        zhenmaRule.add(zhenmaB);

        PlayItem zhenmaItem = new PlayItem();
        zhenmaItem.setName("正码");
        zhenmaItem.setCode(PlayCodeConstants.zhenma);
        zhenmaItem.setRules(zhenmaRule);
        playItems.add(zhenmaItem);

        //正特码
        SubPlayItem zytm = new SubPlayItem();
        zytm.setName("正一特码");
        zytm.setCode(PlayCodeConstants.z1t);
        zytm.setRandomCount(1);

        SubPlayItem zetm = new SubPlayItem();
        zetm.setName("正二特码");
        zetm.setCode(PlayCodeConstants.z2t);
        zetm.setRandomCount(1);

        SubPlayItem zstm = new SubPlayItem();
        zstm.setName("正三特码");
        zstm.setCode(PlayCodeConstants.z3t);
        zstm.setRandomCount(1);

        SubPlayItem zsitm = new SubPlayItem();
        zsitm.setName("正四特码");
        zsitm.setCode(PlayCodeConstants.z4t);
        zsitm.setRandomCount(1);

        SubPlayItem zwutm = new SubPlayItem();
        zwutm.setName("正五特码");
        zwutm.setCode(PlayCodeConstants.z5t);
        zwutm.setRandomCount(1);

        SubPlayItem zliutm = new SubPlayItem();
        zliutm.setName("正六特码");
        zliutm.setCode(PlayCodeConstants.z6t);
        zliutm.setRandomCount(1);


        List<SubPlayItem> ztmRule = new ArrayList<SubPlayItem>();
        ztmRule.add(zytm);
        ztmRule.add(zetm);
        ztmRule.add(zstm);
        ztmRule.add(zsitm);
        ztmRule.add(zwutm);
        ztmRule.add(zliutm);

        PlayItem ztmItem = new PlayItem();
        ztmItem.setName("正特码");
        ztmItem.setCode(PlayCodeConstants.zhentema);
        ztmItem.setRules(ztmRule);
        playItems.add(ztmItem);


        //连码
        SubPlayItem szy = new SubPlayItem();
        szy.setName("三中二");
        szy.setCode(PlayCodeConstants.szezze);
        szy.setRandomCount(1);

        SubPlayItem erzhongquan = new SubPlayItem();
        erzhongquan.setName("二全中");
        erzhongquan.setCode(PlayCodeConstants.eqz);
        erzhongquan.setRandomCount(1);

        SubPlayItem eztzzt = new SubPlayItem();
        eztzzt.setName("二中特之中特");
        eztzzt.setCode(PlayCodeConstants.eztzzt);
        eztzzt.setRandomCount(1);

        SubPlayItem eztzze = new SubPlayItem();
        eztzze.setName("二中特之中二");
        eztzze.setCode(PlayCodeConstants.eztzze);
        eztzze.setRandomCount(1);

        SubPlayItem techuang = new SubPlayItem();
        techuang.setName("特串");
        techuang.setCode(PlayCodeConstants.tc);
        techuang.setRandomCount(1);

        SubPlayItem shizy = new SubPlayItem();
        shizy.setName("四中一");
        shizy.setCode(PlayCodeConstants.szy);
        shizy.setRandomCount(1);


        List<SubPlayItem> lianmaRule = new ArrayList<SubPlayItem>();
        lianmaRule.add(szy);
        lianmaRule.add(erzhongquan);
        lianmaRule.add(eztzzt);
        lianmaRule.add(eztzze);
        lianmaRule.add(techuang);
        lianmaRule.add(shizy);

        PlayItem lianmaItem = new PlayItem();
        lianmaItem.setName("连码");
        lianmaItem.setCode(PlayCodeConstants.lianma);
        lianmaItem.setRules(lianmaRule);
        playItems.add(lianmaItem);

        //特码半波
        SubPlayItem dyq = new SubPlayItem();
        dyq.setName("半波");
        dyq.setCode(PlayCodeConstants.bb);
        dyq.setRandomCount(1);

        List<SubPlayItem> dyqRule = new ArrayList<SubPlayItem>();
        dyqRule.add(dyq);

        PlayItem dyqItem = new PlayItem();
        dyqItem.setName("特码半波");
        dyqItem.setCode(PlayCodeConstants.bb);
        dyqItem.setRules(dyqRule);
        playItems.add(dyqItem);

        //一肖/尾数
        SubPlayItem ztxztw = new SubPlayItem();
        ztxztw.setName("正特肖/正特尾");
        ztxztw.setCode(PlayCodeConstants.ztxztw);
        ztxztw.setRandomCount(1);

        List<SubPlayItem> yxwsRule = new ArrayList<SubPlayItem>();
        yxwsRule.add(ztxztw);

        PlayItem yxwsItem = new PlayItem();
        yxwsItem.setName("一肖/尾数");
        yxwsItem.setCode(PlayCodeConstants.yixiao_weishu);
        yxwsItem.setRules(yxwsRule);
        playItems.add(yxwsItem);


        //特码生肖
        SubPlayItem tmsx = new SubPlayItem();
        tmsx.setName("特码生肖");
        tmsx.setCode(PlayCodeConstants.txsm);
        tmsx.setRandomCount(1);

        List<SubPlayItem> tmsxRule = new ArrayList<SubPlayItem>();
        tmsxRule.add(tmsx);

        PlayItem tmsxItem = new PlayItem();
        tmsxItem.setName("特码生肖");
        tmsxItem.setCode(PlayCodeConstants.txsm);
        tmsxItem.setRules(tmsxRule);
        playItems.add(tmsxItem);


        //合肖
        SubPlayItem erxiao = new SubPlayItem();
        erxiao.setName("二肖");
        erxiao.setCode(PlayCodeConstants.hx_ex);
        erxiao.setRandomCount(1);

        SubPlayItem shangxiao = new SubPlayItem();
        shangxiao.setName("三肖");
        shangxiao.setCode(PlayCodeConstants.hx_sanx);
        shangxiao.setRandomCount(1);

        SubPlayItem shixiao = new SubPlayItem();
        shixiao.setName("四肖");
        shixiao.setCode(PlayCodeConstants.hx_six);
        shixiao.setRandomCount(1);

        SubPlayItem wuxiao = new SubPlayItem();
        wuxiao.setName("五肖");
        wuxiao.setCode(PlayCodeConstants.hx_wux);
        wuxiao.setRandomCount(1);

        SubPlayItem liuxiao = new SubPlayItem();
        liuxiao.setName("六肖");
        liuxiao.setCode(PlayCodeConstants.hx_liux);
        liuxiao.setRandomCount(1);

        SubPlayItem qixiao = new SubPlayItem();
        qixiao.setName("七肖");
        qixiao.setCode(PlayCodeConstants.hx_qix);
        qixiao.setRandomCount(1);

        SubPlayItem baxiao = new SubPlayItem();
        baxiao.setName("八肖");
        baxiao.setCode(PlayCodeConstants.hx_bax);
        baxiao.setRandomCount(1);

        SubPlayItem jiuxiao = new SubPlayItem();
        jiuxiao.setName("九肖");
        jiuxiao.setCode(PlayCodeConstants.hx_jiux);
        jiuxiao.setRandomCount(1);

        SubPlayItem shiixiao = new SubPlayItem();
        shiixiao.setName("十肖");
        shiixiao.setCode(PlayCodeConstants.hx_shix);
        shiixiao.setRandomCount(1);

        SubPlayItem shiyixiao = new SubPlayItem();
        shiyixiao.setName("十一肖");
        shiyixiao.setCode(PlayCodeConstants.hx_syx);
        shiyixiao.setRandomCount(1);

        List<SubPlayItem> hxRule = new ArrayList<SubPlayItem>();
        hxRule.add(erxiao);
        hxRule.add(shangxiao);
        hxRule.add(shixiao);
        hxRule.add(wuxiao);
        hxRule.add(liuxiao);
        hxRule.add(qixiao);
        hxRule.add(baxiao);
        hxRule.add(jiuxiao);
        hxRule.add(shiixiao);
        hxRule.add(shiyixiao);

        PlayItem hxItem = new PlayItem();
        hxItem.setName("合肖");
        hxItem.setCode(PlayCodeConstants.hexiao);
        hxItem.setRules(hxRule);
        playItems.add(hxItem);


        //全不中
        SubPlayItem wbz = new SubPlayItem();
        wbz.setName("五不中");
        wbz.setCode(PlayCodeConstants.w_bz);
        wbz.setRandomCount(1);

        SubPlayItem lbz = new SubPlayItem();
        lbz.setName("六不中");
        lbz.setCode(PlayCodeConstants.liu_bz);
        lbz.setRandomCount(1);

        SubPlayItem qbz = new SubPlayItem();
        qbz.setName("七不中");
        qbz.setCode(PlayCodeConstants.qi_bz);
        qbz.setRandomCount(1);

        SubPlayItem bbz = new SubPlayItem();
        bbz.setName("八不中");
        bbz.setCode(PlayCodeConstants.ba_bz);
        bbz.setRandomCount(1);

        SubPlayItem jbz = new SubPlayItem();
        jbz.setName("九不中");
        jbz.setCode(PlayCodeConstants.jiu_bz);
        jbz.setRandomCount(1);

        SubPlayItem sbz = new SubPlayItem();
        sbz.setName("十不中");
        sbz.setCode(PlayCodeConstants.shi_bz);
        sbz.setRandomCount(1);

        SubPlayItem sybz = new SubPlayItem();
        sybz.setName("十一不中");
        sybz.setCode(PlayCodeConstants.shiy_bz);
        sybz.setRandomCount(1);

        SubPlayItem sebz = new SubPlayItem();
        sebz.setName("十二不中");
        sebz.setCode(PlayCodeConstants.shie_bz);
        sebz.setRandomCount(1);

        List<SubPlayItem> qbzRule = new ArrayList<SubPlayItem>();
        qbzRule.add(wbz);
        qbzRule.add(lbz);
        qbzRule.add(qbz);
        qbzRule.add(bbz);
        qbzRule.add(jbz);
        qbzRule.add(sbz);
        qbzRule.add(sybz);
        qbzRule.add(sebz);

        PlayItem qbzItem = new PlayItem();
        qbzItem.setName("全不中");
        qbzItem.setCode(PlayCodeConstants.quanbuzhong);
        qbzItem.setRules(qbzRule);
        playItems.add(qbzItem);

        //尾数连
        SubPlayItem ewlz = new SubPlayItem();
        ewlz.setName("二尾连中");
        ewlz.setCode(PlayCodeConstants.erw_lz);
        ewlz.setRandomCount(1);

        SubPlayItem swlz = new SubPlayItem();
        swlz.setName("三尾连中");
        swlz.setCode(PlayCodeConstants.sanw_lz);
        swlz.setRandomCount(1);

        SubPlayItem siwlz = new SubPlayItem();
        siwlz.setName("四尾连中");
        siwlz.setCode(PlayCodeConstants.siw_lz);
        siwlz.setRandomCount(1);

        SubPlayItem ewlbz = new SubPlayItem();
        ewlbz.setName("二尾连不中");
        ewlbz.setCode(PlayCodeConstants.erw_lbz);
        ewlbz.setRandomCount(1);

        SubPlayItem swlbz = new SubPlayItem();
        swlbz.setName("三尾连不中");
        swlbz.setCode(PlayCodeConstants.sanw_lbz);
        swlbz.setRandomCount(1);

        SubPlayItem siwlbz = new SubPlayItem();
        siwlbz.setName("四尾连不中");
        siwlbz.setCode(PlayCodeConstants.siw_lbz);
        siwlbz.setRandomCount(1);

        List<SubPlayItem> wslRule = new ArrayList<SubPlayItem>();
        wslRule.add(ewlz);
        wslRule.add(swlz);
        wslRule.add(siwlz);
        wslRule.add(ewlbz);
        wslRule.add(swlbz);
        wslRule.add(siwlbz);

        PlayItem wslItem = new PlayItem();
        wslItem.setName("尾数连");
        wslItem.setCode(PlayCodeConstants.weishulian);
        wslItem.setRules(wslRule);
        playItems.add(wslItem);


        //连肖
        SubPlayItem exlz = new SubPlayItem();
        exlz.setName("二肖连中");
        exlz.setCode(PlayCodeConstants.erx_lz);
        exlz.setRandomCount(1);

        SubPlayItem sxlz = new SubPlayItem();
        sxlz.setName("三肖连中");
        sxlz.setCode(PlayCodeConstants.sanx_lz);
        sxlz.setRandomCount(1);

        SubPlayItem sixlz = new SubPlayItem();
        sixlz.setName("四肖连中");
        sixlz.setCode(PlayCodeConstants.six_lz);
        sixlz.setRandomCount(1);

        SubPlayItem wxlz = new SubPlayItem();
        wxlz.setName("五肖连中");
        wxlz.setCode(PlayCodeConstants.wux_lz);
        wxlz.setRandomCount(1);

        SubPlayItem exlbz = new SubPlayItem();
        exlbz.setName("二肖连不中");
        exlbz.setCode(PlayCodeConstants.erx_lbz);
        exlbz.setRandomCount(1);

        SubPlayItem sxlbz = new SubPlayItem();
        sxlbz.setName("三肖连不中");
        sxlbz.setCode(PlayCodeConstants.sanx_lbz);
        sxlbz.setRandomCount(1);

        SubPlayItem sixlbz = new SubPlayItem();
        sixlbz.setName("四肖连不中");
        sixlbz.setCode(PlayCodeConstants.six_lbz);
        sixlbz.setRandomCount(1);

        List<SubPlayItem> lianxiaoRule = new ArrayList<SubPlayItem>();
        lianxiaoRule.add(exlz);
        lianxiaoRule.add(sxlz);
        lianxiaoRule.add(sixlz);
        lianxiaoRule.add(wxlz);
        lianxiaoRule.add(exlbz);
        lianxiaoRule.add(sxlbz);
        lianxiaoRule.add(sixlbz);

        PlayItem lianxiaoItem = new PlayItem();
        lianxiaoItem.setName("连肖");
        lianxiaoItem.setCode(PlayCodeConstants.lianxiao);
        lianxiaoItem.setRules(lianxiaoRule);
        playItems.add(lianxiaoItem);


        return playItems;
    }

    //第一版：奖金模式
    private static LotteryConstants getLotterysInVersion1() {

        List<LotteryData> data = new ArrayList<LotteryData>();

        List<PlayItem> sscPlays = getFFCPlays();
        //五分彩
        LotteryData wfc = new LotteryData();
        wfc.setName("五分彩");
        wfc.setBallonNums(5);
        wfc.setCzCode("1");
        wfc.setRules(sscPlays);
        //分分彩
        LotteryData ffc = new LotteryData();
        ffc.setName("分分彩");
        ffc.setBallonNums(5);
        ffc.setCzCode("1");
        ffc.setRules(sscPlays);
        //二分彩
        LotteryData efc = new LotteryData();
        efc.setName("二分彩");
        efc.setBallonNums(5);
        efc.setCzCode("1");
        efc.setRules(sscPlays);

        data.add(wfc);
        data.add(ffc);
        data.add(efc);


        //重庆时时彩
        LotteryData cyssc = new LotteryData();
        cyssc.setName("重庆时时彩");
        cyssc.setBallonNums(5);
        cyssc.setCzCode("2");
        cyssc.setRules(sscPlays);
        //新疆时时彩
        LotteryData xjssc = new LotteryData();
        xjssc.setName("新疆时时彩");
        xjssc.setBallonNums(5);
        xjssc.setCzCode("2");
        xjssc.setRules(sscPlays);
        //天津时时彩
        LotteryData tjssc = new LotteryData();
        tjssc.setName("天津时时彩");
        tjssc.setBallonNums(5);
        tjssc.setCzCode("2");
        tjssc.setRules(sscPlays);

        data.add(cyssc);
        data.add(xjssc);
        data.add(tjssc);

        List<PlayItem> saiChePlays = getSaiChePlays();
        //极速赛车
        LotteryData jssc = new LotteryData();
        jssc.setName("极速赛车");
        jssc.setBallonNums(10);
        jssc.setCzCode("3");
        jssc.setRules(saiChePlays);
        //北京赛车
        LotteryData bjsc = new LotteryData();
        bjsc.setName("北京赛车");
        bjsc.setBallonNums(10);
        bjsc.setCzCode("3");
        bjsc.setRules(saiChePlays);
        //幸运飞艇
        LotteryData xyft = new LotteryData();
        xyft.setName("幸运飞艇");
        xyft.setBallonNums(10);
        xyft.setCzCode("3");
        xyft.setRules(saiChePlays);

        data.add(jssc);
        data.add(bjsc);
        data.add(xyft);

        List<PlayItem> fc3DPlays = getFC3DPlays();
        //福彩3D
        LotteryData fcsd = new LotteryData();
        fcsd.setName("福彩3D");
        fcsd.setBallonNums(3);
        fcsd.setCzCode("4");
        fcsd.setRules(fc3DPlays);
        //排队三
        LotteryData pls = new LotteryData();
        pls.setName("排列三");
        pls.setBallonNums(3);
        pls.setCzCode("4");
        pls.setRules(fc3DPlays);

        data.add(fcsd);
        data.add(pls);


        List<PlayItem> x5Plays = get11x5Plays();

        //江西11选5
        LotteryData jx115 = new LotteryData();
        jx115.setName("江西11选5");
        jx115.setBallonNums(5);
        jx115.setCzCode("5");
        jx115.setRules(x5Plays);
        //广东11选5
        LotteryData gd115 = new LotteryData();
        gd115.setName("广东11选5");
        gd115.setBallonNums(5);
        gd115.setCzCode("5");
        gd115.setRules(x5Plays);
        //上海11选5
        LotteryData sh115 = new LotteryData();
        sh115.setName("上海11选5");
        sh115.setBallonNums(5);
        sh115.setCzCode("5");
        sh115.setRules(x5Plays);
        //山东11选5
        LotteryData sd115 = new LotteryData();
        sd115.setName("山东11选5");
        sd115.setBallonNums(5);
        sd115.setCzCode("5");
        sd115.setRules(x5Plays);

        data.add(jx115);
        data.add(gd115);
        data.add(sh115);
        data.add(sd115);

        //六合彩
        LotteryData lhc = new LotteryData();
        lhc.setName("六合彩");
        lhc.setBallonNums(7);
        lhc.setCzCode("6");
        lhc.setRules(null);

        data.add(lhc);

        List<PlayItem> pcEggPlays = getPCEggPlays();
        //加拿大28
        LotteryData jnd28 = new LotteryData();
        jnd28.setName("加拿大28");
        jnd28.setBallonNums(3);
        jnd28.setCzCode("7");
        jnd28.setRules(pcEggPlays);
        //PC蛋蛋
        LotteryData pcdd = new LotteryData();
        pcdd.setName("PC蛋蛋");
        pcdd.setBallonNums(3);
        pcdd.setCzCode("7");
        pcdd.setRules(pcEggPlays);

        data.add(jnd28);
        data.add(pcdd);

        //十分六合彩
        LotteryData sflhc = new LotteryData();
        sflhc.setName("十分六合彩");
        sflhc.setBallonNums(7);
        sflhc.setCzCode("66");
        sflhc.setRules(null);

        data.add(sflhc);

        List<PlayItem> kuai3Plays = getKuai3Plays();

        //安徽快三
        LotteryData ahk3 = new LotteryData();
        ahk3.setName("安徽快三");
        ahk3.setBallonNums(3);
        ahk3.setCzCode("100");
        ahk3.setRules(kuai3Plays);
        //湖北快三
        LotteryData hubeik3 = new LotteryData();
        hubeik3.setName("湖北快三");
        hubeik3.setBallonNums(3);
        hubeik3.setCzCode("100");
        hubeik3.setRules(kuai3Plays);
        //江苏快三
        LotteryData jsk3 = new LotteryData();
        jsk3.setName("江苏快三");
        jsk3.setBallonNums(3);
        jsk3.setCzCode("100");
        jsk3.setRules(kuai3Plays);
        //广西快三
        LotteryData gxk3 = new LotteryData();
        gxk3.setName("广西快三");
        gxk3.setBallonNums(3);
        gxk3.setCzCode("100");
        gxk3.setRules(kuai3Plays);
        //江西快三
        LotteryData jxk3 = new LotteryData();
        jxk3.setName("江西快三");
        jxk3.setBallonNums(3);
        jxk3.setCzCode("100");
        jxk3.setRules(kuai3Plays);
        //河北快三
        LotteryData hebeik3 = new LotteryData();
        hebeik3.setName("河北快三");
        hebeik3.setBallonNums(3);
        hebeik3.setCzCode("100");
        hebeik3.setRules(kuai3Plays);
        //北京快三
        LotteryData bjk3 = new LotteryData();
        bjk3.setName("北京快三");
        bjk3.setBallonNums(3);
        bjk3.setCzCode("100");
        bjk3.setRules(kuai3Plays);
        //幸运快三
        LotteryData xyk3 = new LotteryData();
        xyk3.setName("幸运快三");
        xyk3.setBallonNums(3);
        xyk3.setCzCode("100");
        xyk3.setRules(kuai3Plays);
        //极速快三
        LotteryData jishuk3 = new LotteryData();
        jishuk3.setName("极速快三");
        jishuk3.setBallonNums(3);
        jishuk3.setCzCode("100");
        jishuk3.setRules(kuai3Plays);
        //甘肃快三
        LotteryData gansuk3 = new LotteryData();
        gansuk3.setName("甘肃快三");
        gansuk3.setBallonNums(3);
        gansuk3.setCzCode("100");
        gansuk3.setRules(kuai3Plays);
        //上海快三
        LotteryData shk3 = new LotteryData();
        shk3.setName("上海快三");
        shk3.setBallonNums(3);
        shk3.setCzCode("100");
        shk3.setRules(kuai3Plays);

        data.add(ahk3);
        data.add(hubeik3);
        data.add(jsk3);
        data.add(gxk3);
        data.add(jxk3);
        data.add(hubeik3);
        data.add(bjk3);
        data.add(xyk3);
        data.add(jishuk3);
        data.add(gansuk3);
        data.add(shk3);

        LotteryConstants lcs = new LotteryConstants();
        lcs.setVersion("1");
        lcs.setLotterys(data);

        return lcs;
    }

    private static LotteryConstants getLotterysInVersion2() {

        List<LotteryData> data = new ArrayList<LotteryData>();

        List<PlayItem> peilvSaiChePlays = getPeilvSaiChePlays();

        //北京赛车
        LotteryData bjsc = new LotteryData();
        bjsc.setName("北京赛车");
        bjsc.setBallonNums(10);
        bjsc.setCzCode("8");
        bjsc.setRules(peilvSaiChePlays);
        //极速赛车
        LotteryData jssc = new LotteryData();
        jssc.setName("极速赛车");
        jssc.setBallonNums(10);
        jssc.setCzCode("8");
        jssc.setRules(peilvSaiChePlays);
        //幸运飞艇
        LotteryData xyft = new LotteryData();
        xyft.setName("幸运飞艇");
        xyft.setBallonNums(10);
        xyft.setCzCode("8");
        xyft.setRules(peilvSaiChePlays);

        data.add(jssc);
        data.add(bjsc);
        data.add(xyft);

        List<PlayItem> peilvFFCPlays = getPeilvFFCPlays();

        //五分彩
        LotteryData wfc = new LotteryData();
        wfc.setName("五分彩");
        wfc.setBallonNums(5);
        wfc.setCzCode("9");
        wfc.setRules(peilvFFCPlays);
        //天津时时彩
        LotteryData tjssc = new LotteryData();
        tjssc.setName("天津时时彩");
        tjssc.setBallonNums(5);
        tjssc.setCzCode("9");
        tjssc.setRules(peilvFFCPlays);
        //新疆时时彩
        LotteryData xjssc = new LotteryData();
        xjssc.setName("新疆时时彩");
        xjssc.setBallonNums(5);
        xjssc.setCzCode("9");
        xjssc.setRules(peilvFFCPlays);
        //重庆时时彩
        LotteryData cyssc = new LotteryData();
        cyssc.setName("重庆时时彩");
        cyssc.setBallonNums(5);
        cyssc.setCzCode("9");
        cyssc.setRules(peilvFFCPlays);
        //分分彩
        LotteryData ffc = new LotteryData();
        ffc.setName("分分彩");
        ffc.setBallonNums(5);
        ffc.setCzCode("9");
        ffc.setRules(peilvFFCPlays);
        //二分彩
        LotteryData efc = new LotteryData();
        efc.setName("二分彩");
        efc.setBallonNums(5);
        efc.setCzCode("9");
        efc.setRules(peilvFFCPlays);

        data.add(wfc);
        data.add(ffc);
        data.add(efc);
        data.add(cyssc);
        data.add(xjssc);
        data.add(tjssc);


        List<PlayItem> peilvKuai3Plays = getPeilvKuai3Plays();

        //河北快三
        LotteryData hebeik3 = new LotteryData();
        hebeik3.setName("河北快三");
        hebeik3.setBallonNums(3);
        hebeik3.setCzCode("10");
        hebeik3.setRules(peilvKuai3Plays);
        //甘肃快三
        LotteryData gansuk3 = new LotteryData();
        gansuk3.setName("甘肃快三");
        gansuk3.setBallonNums(3);
        gansuk3.setCzCode("10");
        gansuk3.setRules(peilvKuai3Plays);
        //极速快三
        LotteryData jishuk3 = new LotteryData();
        jishuk3.setName("极速快三");
        jishuk3.setBallonNums(3);
        jishuk3.setCzCode("10");
        jishuk3.setRules(peilvKuai3Plays);
        //幸运快三
        LotteryData xyk3 = new LotteryData();
        xyk3.setName("幸运快三");
        xyk3.setBallonNums(3);
        xyk3.setCzCode("10");
        xyk3.setRules(peilvKuai3Plays);
        //上海快三
        LotteryData shk3 = new LotteryData();
        shk3.setName("上海快三");
        shk3.setBallonNums(3);
        shk3.setCzCode("10");
        shk3.setRules(peilvKuai3Plays);
        //江西快三
        LotteryData jxk3 = new LotteryData();
        jxk3.setName("江西快三");
        jxk3.setBallonNums(3);
        jxk3.setCzCode("10");
        jxk3.setRules(peilvKuai3Plays);
        //江苏骰宝(快3)
        LotteryData jssbk3 = new LotteryData();
        jxk3.setName("江苏骰宝(快3)");
        jxk3.setBallonNums(3);
        jxk3.setCzCode("10");
        jxk3.setRules(peilvKuai3Plays);
        //北京快三
        LotteryData bjk3 = new LotteryData();
        bjk3.setName("北京快三");
        bjk3.setBallonNums(3);
        bjk3.setCzCode("10");
        bjk3.setRules(peilvKuai3Plays);
        //广西快三
        LotteryData gxk3 = new LotteryData();
        gxk3.setName("广西快三");
        gxk3.setBallonNums(3);
        gxk3.setCzCode("10");
        gxk3.setRules(peilvKuai3Plays);
        //安徽快三
        LotteryData ahk3 = new LotteryData();
        ahk3.setName("安徽快三");
        ahk3.setBallonNums(3);
        ahk3.setCzCode("10");
        ahk3.setRules(peilvKuai3Plays);
        //湖北快三
        LotteryData hubeik3 = new LotteryData();
        hubeik3.setName("湖北快三");
        hubeik3.setBallonNums(3);
        hubeik3.setCzCode("10");
        hubeik3.setRules(peilvKuai3Plays);


        data.add(ahk3);
        data.add(hubeik3);
        data.add(gxk3);
        data.add(jxk3);
        data.add(hubeik3);
        data.add(bjk3);
        data.add(xyk3);
        data.add(jishuk3);
        data.add(gansuk3);
        data.add(shk3);

        List<PlayItem> peilvPC28Plays = getPeilvPC28Plays();

        //加拿大28
        LotteryData jnd28 = new LotteryData();
        jnd28.setName("加拿大28");
        jnd28.setBallonNums(3);
        jnd28.setCzCode("11");
        jnd28.setRules(peilvPC28Plays);
        //PC蛋蛋
        LotteryData pcdd = new LotteryData();
        pcdd.setName("PC蛋蛋");
        pcdd.setBallonNums(3);
        pcdd.setCzCode("11");
        pcdd.setRules(peilvPC28Plays);

        data.add(jnd28);
        data.add(pcdd);

        List<PlayItem> peilvKuaile10FenPlays = getPeilvKuaile10FenPlays();

        //重庆幸运农场
        LotteryData cyxync = new LotteryData();
        cyxync.setName("重庆幸运农场");
        cyxync.setBallonNums(8);
        cyxync.setCzCode("12");
        cyxync.setRules(peilvKuaile10FenPlays);

        //湖南快乐十分
        LotteryData hnklsf = new LotteryData();
        hnklsf.setName("湖南快乐十分");
        hnklsf.setBallonNums(8);
        hnklsf.setCzCode("12");
        hnklsf.setRules(peilvKuaile10FenPlays);

        //广东快乐十分
        LotteryData gdklsf = new LotteryData();
        gdklsf.setName("广东快乐十分");
        gdklsf.setBallonNums(8);
        gdklsf.setCzCode("12");
        gdklsf.setRules(peilvKuaile10FenPlays);

        data.add(cyxync);
        data.add(hnklsf);
        data.add(gdklsf);

        List<PlayItem> peilv11x5Plays = getPeilv11x5Plays();

        //江西11选5
        LotteryData jx115 = new LotteryData();
        jx115.setName("江西11选5");
        jx115.setBallonNums(5);
        jx115.setCzCode("14");
        jx115.setRules(peilv11x5Plays);
        //广东11选5
        LotteryData gd115 = new LotteryData();
        gd115.setName("广东11选5");
        gd115.setBallonNums(5);
        gd115.setCzCode("14");
        gd115.setRules(peilv11x5Plays);
        //上海11选5
        LotteryData sh115 = new LotteryData();
        sh115.setName("上海11选5");
        sh115.setBallonNums(5);
        sh115.setCzCode("14");
        sh115.setRules(peilv11x5Plays);
        //山东11选5
        LotteryData sd115 = new LotteryData();
        sd115.setName("山东11选5");
        sd115.setBallonNums(5);
        sd115.setCzCode("14");
        sd115.setRules(peilv11x5Plays);

        data.add(jx115);
        data.add(gd115);
        data.add(sh115);
        data.add(sd115);

        List<PlayItem> peilvfucai3dPlays = getPeilvfucai3dPlays();

        //福彩3D
        LotteryData fcsd = new LotteryData();
        fcsd.setName("福彩3D");
        fcsd.setBallonNums(3);
        fcsd.setCzCode("15");
        fcsd.setRules(peilvfucai3dPlays);
        //排列三
        LotteryData pls = new LotteryData();
        pls.setName("排列三");
        pls.setBallonNums(3);
        pls.setCzCode("15");
        pls.setRules(peilvfucai3dPlays);

        data.add(fcsd);
        data.add(pls);


        List<PlayItem> peilvLiuHeCaiPlays = getPeilvLiuHeCaiPlays();

        //十分六合彩
        LotteryData sflhc = new LotteryData();
        sflhc.setName("十分六合彩");
        sflhc.setBallonNums(7);
        sflhc.setCzCode("66");
        sflhc.setRules(peilvLiuHeCaiPlays);

        data.add(sflhc);

        //六合彩
        LotteryData lhc = new LotteryData();
        lhc.setName("六合彩");
        lhc.setBallonNums(7);
        lhc.setCzCode("6");
        lhc.setRules(peilvLiuHeCaiPlays);

        data.add(lhc);

        LotteryConstants lcs = new LotteryConstants();
        lcs.setVersion("2");
        lcs.setLotterys(data);
        return lcs;
    }

    //第三版：奖金模式
    private static LotteryConstants getLotterysInVersion3() {

        List<LotteryData> data = new ArrayList<LotteryData>();

        List<PlayItem> sscPlays = getFFCPlays();
        //五分彩
        LotteryData wfc = new LotteryData();
        wfc.setName("五分彩");
        wfc.setBallonNums(5);
        wfc.setCzCode("51");
        wfc.setRules(sscPlays);
        //分分彩
        LotteryData ffc = new LotteryData();
        ffc.setName("分分彩");
        ffc.setBallonNums(5);
        ffc.setCzCode("51");
        ffc.setRules(sscPlays);
        //二分彩
        LotteryData efc = new LotteryData();
        efc.setName("二分彩");
        efc.setBallonNums(5);
        efc.setCzCode("51");
        efc.setRules(sscPlays);

        data.add(wfc);
        data.add(ffc);
        data.add(efc);


        //重庆时时彩
        LotteryData cyssc = new LotteryData();
        cyssc.setName("重庆时时彩");
        cyssc.setBallonNums(5);
        cyssc.setCzCode("52");
        cyssc.setRules(sscPlays);
        //新疆时时彩
        LotteryData xjssc = new LotteryData();
        xjssc.setName("新疆时时彩");
        xjssc.setBallonNums(5);
        xjssc.setCzCode("52");
        xjssc.setRules(sscPlays);
        //天津时时彩
        LotteryData tjssc = new LotteryData();
        tjssc.setName("天津时时彩");
        tjssc.setBallonNums(5);
        tjssc.setCzCode("52");
        tjssc.setRules(sscPlays);

        data.add(cyssc);
        data.add(xjssc);
        data.add(tjssc);

        List<PlayItem> saiChePlays = getSaiChePlays();
        //极速赛车
        LotteryData jssc = new LotteryData();
        jssc.setName("极速赛车");
        jssc.setBallonNums(10);
        jssc.setCzCode("53");
        jssc.setRules(saiChePlays);
        //北京赛车
        LotteryData bjsc = new LotteryData();
        bjsc.setName("北京赛车");
        bjsc.setBallonNums(10);
        bjsc.setCzCode("53");
        bjsc.setRules(saiChePlays);
        //幸运飞艇
        LotteryData xyft = new LotteryData();
        xyft.setName("幸运飞艇");
        xyft.setBallonNums(10);
        xyft.setCzCode("53");
        xyft.setRules(saiChePlays);

        data.add(jssc);
        data.add(bjsc);
        data.add(xyft);

        List<PlayItem> fc3DPlays = getFC3DPlays();
        //福彩3D
        LotteryData fcsd = new LotteryData();
        fcsd.setName("福彩3D");
        fcsd.setBallonNums(3);
        fcsd.setCzCode("54");
        fcsd.setRules(fc3DPlays);
        //排队三
        LotteryData pls = new LotteryData();
        pls.setName("排列三");
        pls.setBallonNums(3);
        pls.setCzCode("54");
        pls.setRules(fc3DPlays);

        data.add(fcsd);
        data.add(pls);


        List<PlayItem> x5Plays = get11x5Plays();

        //江西11选5
        LotteryData jx115 = new LotteryData();
        jx115.setName("江西11选5");
        jx115.setBallonNums(5);
        jx115.setCzCode("55");
        jx115.setRules(x5Plays);
        //广东11选5
        LotteryData gd115 = new LotteryData();
        gd115.setName("广东11选5");
        gd115.setBallonNums(5);
        gd115.setCzCode("55");
        gd115.setRules(x5Plays);
        //上海11选5
        LotteryData sh115 = new LotteryData();
        sh115.setName("上海11选5");
        sh115.setBallonNums(5);
        sh115.setCzCode("55");
        sh115.setRules(x5Plays);
        //山东11选5
        LotteryData sd115 = new LotteryData();
        sd115.setName("山东11选5");
        sd115.setBallonNums(5);
        sd115.setCzCode("55");
        sd115.setRules(x5Plays);

        data.add(jx115);
        data.add(gd115);
        data.add(sh115);
        data.add(sd115);

        //六合彩
        LotteryData lhc = new LotteryData();
        lhc.setName("六合彩");
        lhc.setBallonNums(7);
        lhc.setCzCode("6");
        lhc.setRules(null);

        data.add(lhc);

        List<PlayItem> pcEggPlays = getPCEggPlays();
        //加拿大28
        LotteryData jnd28 = new LotteryData();
        jnd28.setName("加拿大28");
        jnd28.setBallonNums(3);
        jnd28.setCzCode("57");
        jnd28.setRules(pcEggPlays);
        //PC蛋蛋
        LotteryData pcdd = new LotteryData();
        pcdd.setName("PC蛋蛋");
        pcdd.setBallonNums(3);
        pcdd.setCzCode("57");
        pcdd.setRules(pcEggPlays);

        data.add(jnd28);
        data.add(pcdd);

        //十分六合彩
        LotteryData sflhc = new LotteryData();
        sflhc.setName("十分六合彩");
        sflhc.setBallonNums(7);
        sflhc.setCzCode("66");
        sflhc.setRules(null);

        data.add(sflhc);

        List<PlayItem> kuai3Plays = getKuai3Plays();

        //安徽快三
        LotteryData ahk3 = new LotteryData();
        ahk3.setName("安徽快三");
        ahk3.setBallonNums(3);
        ahk3.setCzCode("58");
        ahk3.setRules(kuai3Plays);
        //湖北快三
        LotteryData hubeik3 = new LotteryData();
        hubeik3.setName("湖北快三");
        hubeik3.setBallonNums(3);
        hubeik3.setCzCode("58");
        hubeik3.setRules(kuai3Plays);
        //江苏快三
        LotteryData jsk3 = new LotteryData();
        jsk3.setName("江苏快三");
        jsk3.setBallonNums(3);
        jsk3.setCzCode("58");
        jsk3.setRules(kuai3Plays);
        //广西快三
        LotteryData gxk3 = new LotteryData();
        gxk3.setName("广西快三");
        gxk3.setBallonNums(3);
        gxk3.setCzCode("58");
        gxk3.setRules(kuai3Plays);
        //江西快三
        LotteryData jxk3 = new LotteryData();
        jxk3.setName("江西快三");
        jxk3.setBallonNums(3);
        jxk3.setCzCode("58");
        jxk3.setRules(kuai3Plays);
        //河北快三
        LotteryData hebeik3 = new LotteryData();
        hebeik3.setName("河北快三");
        hebeik3.setBallonNums(3);
        hebeik3.setCzCode("58");
        hebeik3.setRules(kuai3Plays);
        //北京快三
        LotteryData bjk3 = new LotteryData();
        bjk3.setName("北京快三");
        bjk3.setBallonNums(3);
        bjk3.setCzCode("58");
        bjk3.setRules(kuai3Plays);
        //幸运快三
        LotteryData xyk3 = new LotteryData();
        xyk3.setName("幸运快三");
        xyk3.setBallonNums(3);
        xyk3.setCzCode("58");
        xyk3.setRules(kuai3Plays);
        //极速快三
        LotteryData jishuk3 = new LotteryData();
        jishuk3.setName("极速快三");
        jishuk3.setBallonNums(3);
        jishuk3.setCzCode("58");
        jishuk3.setRules(kuai3Plays);
        //甘肃快三
        LotteryData gansuk3 = new LotteryData();
        gansuk3.setName("甘肃快三");
        gansuk3.setBallonNums(3);
        gansuk3.setCzCode("58");
        gansuk3.setRules(kuai3Plays);
        //上海快三
        LotteryData shk3 = new LotteryData();
        shk3.setName("上海快三");
        shk3.setBallonNums(3);
        shk3.setCzCode("58");
        shk3.setRules(kuai3Plays);

        data.add(ahk3);
        data.add(hubeik3);
        data.add(jsk3);
        data.add(gxk3);
        data.add(jxk3);
        data.add(hubeik3);
        data.add(bjk3);
        data.add(xyk3);
        data.add(jishuk3);
        data.add(gansuk3);
        data.add(shk3);

        LotteryConstants lcs = new LotteryConstants();
        lcs.setVersion("3");
        lcs.setLotterys(data);

        return lcs;
    }

    //第四版：赔率模式，对话聊天
    private static LotteryConstants getLotterysInVersion4() {

        List<LotteryData> data = new ArrayList<LotteryData>();

        //加拿大28
        LotteryData jnd28 = new LotteryData();
        jnd28.setName("加拿大28");
        jnd28.setBallonNums(3);
        jnd28.setCzCode("161");
        jnd28.setRules(null);
        //PC蛋蛋
        LotteryData pcdd = new LotteryData();
        pcdd.setName("PC蛋蛋");
        pcdd.setBallonNums(3);
        pcdd.setCzCode("161");
        pcdd.setRules(null);

        data.add(jnd28);
        data.add(pcdd);

        LotteryConstants lcs = new LotteryConstants();
        lcs.setVersion("4");
        lcs.setLotterys(data);
        return lcs;
    }


}
