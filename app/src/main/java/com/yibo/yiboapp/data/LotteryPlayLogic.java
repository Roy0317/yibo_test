package com.yibo.yiboapp.data;

import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.BallonRules;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.utils.Utils;

import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.yibo.yiboapp.R.id.ballon;

/**
 * Created by johnson on 2017/9/19.
 * 彩票机选，选择的号码生成，注数计算等逻辑
 */

public class LotteryPlayLogic {

    public static final String TAG = LotteryPlayLogic.class.getSimpleName();

    //根据彩票版本，彩票彩种，大小玩法代号构建界面投注面板显示的球数据
    public static List<BallonRules> buildPaneDataByCode(
            String cpVersion, String czCode,String playCode, String subPlayCode) {
        return LotteryAlgorithm.figureOutPlayInfo(cpVersion, czCode, playCode, subPlayCode);
    }

    /**
     * 根据用户投注好的球数据来计算投注的号码，号码是按规定的格式生成
     * @param listBeenSelected 用户投注选择好的球数据
     * @param playCode 大玩法代号
     * @param subPlayCode 小玩法代号
     * @return
     */
    public static String figureOutNumbersAfterUserSelected(
            List<BallonRules> listBeenSelected,String playCode, String subPlayCode){

        if (Utils.isEmptyString(subPlayCode)) {
            throw new IllegalStateException("play code have not specific value");
        }

        if (listBeenSelected == null || listBeenSelected.isEmpty()) {
            return "";
        }
        //line 投注池行数，若只有一行，则在拼接投注号码时，加逗号
        int line = listBeenSelected.size();
        //某些玩法不需要用逗号分隔的
        boolean noNeedComma = playCode.equals(PlayCodeConstants.dxds)||
                playCode.equals(PlayCodeConstants.caibaozi)||
                playCode.equals(PlayCodeConstants.longhh)||
                playCode.equals(PlayCodeConstants.q1_str)||
                playCode.equals(PlayCodeConstants.q2_str)||
                playCode.equals(PlayCodeConstants.qiansan)||
                (playCode.equals(PlayCodeConstants.hz)&&subPlayCode.equals(PlayCodeConstants.dxds))||
                (subPlayCode.equals(PlayCodeConstants.longhu_gunjun)||subPlayCode.equals(PlayCodeConstants.longhu_jijun)
                ||subPlayCode.equals(PlayCodeConstants.longhu_yajun))||
                (playCode.equals(PlayCodeConstants.gyh)&&subPlayCode.equals(PlayCodeConstants.dxds));
        //是否需要我使用横杆占位空号码
        boolean needHengan = playCode.equals(PlayCodeConstants.dwd)
                ||playCode.equals(PlayCodeConstants.rxwf);
        //是否需要使用@符号来对有带位数选择的玩法进行投注号码和位数分隔
        boolean needAtFlag = subPlayCode.equals(PlayCodeConstants.rxwf_r3zux_zu3) ||
                subPlayCode.equals(PlayCodeConstants.rxwf_r3zux_zu6);
        //是否使用备选投注号码
        boolean needSelectSecondHaomao = playCode.equals(PlayCodeConstants.caibaozi)||
                subPlayCode.equals(PlayCodeConstants.sthtx)||
                subPlayCode.equals(PlayCodeConstants.slhtx);

        StringBuilder numstr = new StringBuilder();
        for (BallonRules info : listBeenSelected) {
            StringBuilder num = new StringBuilder();
            if (info.isShowWeiShuView()) {
                List<BallListItemInfo> weishuInfo = info.getWeishuInfo();
                for (BallListItemInfo bi : weishuInfo) {
                    if (bi.isSelected()) {
                        num.append(bi.getNum()).append(",");
                    }
                }
            }
            if (needAtFlag && num.toString().trim().endsWith(",")) {
                num = num.deleteCharAt(num.length()-1);
                num.append("@");
            }
            List<BallListItemInfo> ballonsInfo = info.getBallonsInfo();
            for (BallListItemInfo ballon : ballonsInfo) {
                if (ballon.isSelected()) {
                    num.append(needSelectSecondHaomao?ballon.getPostNum():ballon.getNum());
                    //处理不同玩法下是否拼接逗号
                    if (noNeedComma) {
                        continue;
                    }
                    if (line == 1) {
                        num.append(",");
                    }
                }
            }
            if (num.length() > 0 && num.toString().endsWith(",")){
                num = num.deleteCharAt(num.length()-1);
            }
            if (Utils.isEmptyString(num.toString().trim()) && needHengan) {
                numstr.append("-").append(",");
            }else{
                if (!Utils.isEmptyString(num.toString().trim())) {//若没有内容，则不追加","
                    numstr.append(num.toString().trim()).append(",");
                }
            }
        }
        if (numstr.toString().endsWith(",")){
            numstr = numstr.deleteCharAt(numstr.length()-1);
        }
        if (Utils.isEmptyString(numstr.toString().trim())) {
            return "";
        }
        Utils.LOG(TAG,"the figure out touzhu haomao = "+numstr);
        return numstr.toString().trim();
    }

    /**
     * 根据投注号码串和小玩法来计算投注数
     * @param czCode
     * @param subPlayCode
     * @param touzhuNumbers
     * @return
     */
    public static int figureOutZhushuByNumberAndCode(
            String czCode,String subPlayCode,String touzhuNumbers) {
        if (!Utils.isNumeric(czCode)) {
            Utils.LOG(TAG,"cai zhong code is not numeric");
            return 0;
        }
        int zhushu = JieBaoZhuShuCalculator.calc(
                Integer.parseInt(czCode), subPlayCode, touzhuNumbers);
        Utils.LOG(TAG,"the calc out zhushu ==== "+zhushu);
        return zhushu;
    }

    private static List<Integer> randomLines(int lines,int numCount,boolean allowRepeat) {

        int maxNum = lines;
        int i;
        int count = 0;
        List<Integer> numbers = new ArrayList<Integer>();
        Random r = new Random();
        while (count < numCount) {
            i = Math.abs(r.nextInt(maxNum));
            if (!allowRepeat) {
                if (i == maxNum) {
                    i--;
                }
                if (numbers.contains(i)) {
                    continue;
                }
            }
            numbers.add(i);
            count++;
        }
        return numbers;
    }

    private static void selectBallonByRandomNumbers(
            List<String> randomNums,BallonRules ballon,String version,String cpCode,
            String pcode,String rcode) {
        //随机选择球并重新将选择好的球信息更新到ballon中
        if (randomNums != null && !randomNums.isEmpty()) {
            List<BallListItemInfo> replaces = new ArrayList<BallListItemInfo>();
            List<BallListItemInfo> binfos = ballon.getBallonsInfo();
            for (BallListItemInfo info : binfos) {
                for (String number : randomNums) {
                    if (info.getNum().equals(number)) {
                        info.setSelected(true);
                        info.setClickOn(true);
                        break;
                    }
                }
                replaces.add(info);
            }
            binfos.clear();
            ballon.setBallonsInfo(replaces);
        }

        //是否需要随机选择定位位数球
        if (ballon.isShowWeiShuView()) {
            //随机选择定位球，并重新将选择好的球信息更新到ballon中
            List<BallListItemInfo> weishuData = new ArrayList<BallListItemInfo>();
            List<BallListItemInfo> weishuList = ballon.getWeishuInfo();
            List<String> weishu = LotteryAlgorithm.getWeishuRandomCount(version,cpCode, pcode, rcode);
            for (BallListItemInfo info : weishuList) {
                for (String number : weishu) {
                    if (info.getNum().equals(number)) {
                        info.setSelected(true);
                        break;
                    }
                }
                weishuData.add(info);
            }
            weishuList.clear();
            weishuList.addAll(weishuData);
            ballon.setWeishuInfo(weishuList);
        }
    }

    /**
     * 随机选择的投注球
     * @param version
     * @param cpCode
     * @param pcode
     * @param rcode
     * @return
     */
    public static List<BallonRules> selectRandomDatas(String version,String cpCode,String pcode,String rcode) {
        List<BallonRules> datas = buildPaneDataByCode(version, cpCode, pcode, rcode);
        if (datas == null) {
            return null;
        }
        for (BallonRules br : datas) {
            List<BallListItemInfo> ballListItemInfos = LotteryAlgorithm.convertNumListToEntifyList(
                    br.getNums(),br.getPostNums());
            br.setBallonsInfo(ballListItemInfos);
        }
        //随机选择的号码数
        int randomBallonCount = 0;
        LotteryConstants constants = Lotterys.getLotterysByVersion(version);
        List<LotteryData> lotterys = constants.getLotterys();
        for (LotteryData ld : lotterys) {
            if (ld.getCzCode().equals(cpCode)) {
                List<PlayItem> rules = ld.getRules();
                for (PlayItem pitem : rules) {
                    if (pitem.getCode().equals(pcode)) {
                        List<SubPlayItem> subRules = pitem.getRules();
                        for (SubPlayItem sitem : subRules) {
                            if (sitem.getCode().equals(rcode)) {
                                randomBallonCount = sitem.getRandomCount();
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }

        Utils.LOG(TAG,"the random count "+randomBallonCount+" in pcode = "+pcode+" rcode = "+rcode);
        int randomBallonPerLine = randomBallonCount;
        int lines = datas.size();
        List<Integer> randomLineIndexs = null;//随机选择时的随机行数索引

        //确定每行随机选择的号码数
        if (lines == randomBallonCount) {
            randomBallonPerLine = 1;
        } else if (lines == 1) {
            randomBallonPerLine = randomBallonCount;
        } else if (lines > randomBallonCount) {
            randomLineIndexs = randomLines(lines, randomBallonCount, false);
            randomBallonPerLine = 1;
        }
        //遍历投注球列表
        for (int i=0;i<datas.size();i++) {
            BallonRules ballon = datas.get(i);
            String nums = ballon.getNums();
            if (randomLineIndexs != null && !randomLineIndexs.isEmpty()) {
                if (randomLineIndexs.contains(i)){
                    List<String> randomNums = Utils.randomNumbers(nums,false,
                            randomBallonPerLine,",");
                    selectBallonByRandomNumbers(randomNums,ballon,version,cpCode,pcode,rcode);
                }
            }else{
                List<String> randomNums = Utils.randomNumbers(nums,false, randomBallonPerLine,",");
                selectBallonByRandomNumbers(randomNums,ballon,version,cpCode,pcode,rcode);
            }
        }
        Utils.LOG(TAG,"the data after random select = "+datas.size());
        return datas;
    }

    /**
     * 随机投注
     * @param version
     * @param cpCode
     * @param pcode
     * @param rcode
     * return 返回投注号码
     */
    public static String randomTouzhu(String version,String cpCode,String pcode,String rcode) {
        List<BallonRules> selectedBallons = selectRandomDatas(version, cpCode, pcode, rcode);
        if (selectedBallons != null && !selectedBallons.isEmpty()) {
            String touzhuNumer = figureOutNumbersAfterUserSelected(selectedBallons, pcode, rcode);
            Utils.LOG(TAG,"the random tou zhu post num = "+touzhuNumer);
            return touzhuNumer;
        }
        return "";
    }

}
