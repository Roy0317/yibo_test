package com.yibo.yiboapp.data;

import android.nfc.Tag;

import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JieBaoZhuShuCalculator {

	public static int calc(Integer lotType, String playCode, String haoMa) {
		switch (lotType) {
			case 1:// 系统彩
			case 2:// 时时彩
			case 51:
			case 52:
				return getSscZhushu(playCode, haoMa);
			case 3:// pk10
			case 53:
				return getBjscZhushu(playCode, haoMa);
			case 4:// 排列三
			case 54:
				return getPL3ZhuShu(playCode, haoMa);
			case 5:// 11选5
			case 55:
				return get11x5Zhushu(playCode, haoMa);
			case 7:// PC蛋蛋
			case 57:
				return getPceggZhuShu(playCode, haoMa);
			case 58:
			case 100:// 快三
				return getK3ZhuShu(playCode, haoMa);
			default:
				return 0;
		}
	}

	final static String reg09 = "^0?1?2?3?4?5?6?7?8?9?|-$";
	final static String reg09Comma = "^(0)?((,|\\b)1)?((,|\\b)2)?((,|\\b)3)?((,|\\b)4)?((,|\\b)5)?((,|\\b)6)?((,|\\b)7)?((,|\\b)8)?((,|\\b)9)?$";
	final static String reg10 = "^(01)?(02)?(03)?(04)?(05)?(06)?(07)?(08)?(09)?(10)?|-$";
	final static String reg11 = "^(01)?(02)?(03)?(04)?(05)?(06)?(07)?(08)?(09)?(10)?(11)?|-$";
	final static String reg11Comma = "^(01)?((,|\\b)02)?((,|\\b)03)?((,|\\b)04)?((,|\\b)05)?((,|\\b)06)?((,|\\b)07)?((,|\\b)08)?((,|\\b)09)?((,|\\b)10)?((,|\\b)11)?$";
	/** 二星和值 */
	final static int[] EXHZ = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
	/** 三星和值 */
	final static int[] SXHZ = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 };
	/** 快3和值 */
	final static int[] K3HZ = { 0, 0, 0, 1, 3, 6, 10, 15, 21, 25, 27, 27, 25, 21, 15, 10, 6, 3, 1 };
	/** 北京赛车冠亚和值 */
	final static int[] BJSCHZ = { 0, 0, 0, 2, 2, 4, 4, 6, 6, 8, 8, 10, 8, 8, 6, 6, 4, 4, 2, 2 };
	
	public static int erXingHeZhi(String haoMa) {
		int zs = 0;
		String[] split = haoMa.split(",");
		for (String h : split) {
			int s = Integer.parseInt(h);
			if (s < 0 || s > 18) {
				return 0;
			}
			zs += EXHZ[s];
		}
		return zs;
	}

	private static int getK3ZhuShu(String playCode, String haoMa) {
		switch (playCode) {
			case PlayCodeConstants.hz://和值
				int zs = 0;
				String[] split = haoMa.split(",");
				for (String h : split) {
					int s = Integer.parseInt(h);
					if (s < 3 || s > 18) {
						return 0;
					}
					zs += K3HZ[s];
				}
				return zs;
			case PlayCodeConstants.dxds://和值大小单双
				if (haoMa.matches("^大?小?单?双?$")) {
					return haoMa.length();
				}
				return 0;
			case PlayCodeConstants.sthtx://三同号通选
				if (haoMa.equals("三同号通选")) {
					return 1;
				}
				return 0;
			case PlayCodeConstants.sthdx://三同号单选
				String[] haoMas = haoMa.split(",");
				zs = 0;
				for (String hm : haoMas) {
					if (hm.matches("^([1-6])\\1\\1$")) {
						zs++;
					}else{
						return 0;
					}
				}
				return zs;
			case PlayCodeConstants.sbtx://三不同号
				haoMas = haoMa.split(",");
				Set<Integer> set=new HashSet<>();
				for(String hm:haoMas){
					int h=Integer.parseInt(hm);
					if(h<1||h>6){
						return 0;
					}
					set.add(h);
				}
				zs = set.size();
				if(zs<3){
					return 0;
				}
				return zs*(zs-1)*(zs-2)/6;
			case PlayCodeConstants.slhtx://三连号通选
				if (haoMa.equals("三连号通选")) {
					return 1;
				}
				return 0;
			case PlayCodeConstants.ethfx://二同号复选
				haoMas = haoMa.split(",");
				zs = 0;
				for (String hm : haoMas) {
					if (hm.matches("^([1-6])\\1$")) {
						zs++;
					}
				}
				return zs;
			case PlayCodeConstants.ethdx://二同号单选
				haoMas = haoMa.split(",");
				if(!haoMas[0].matches("^(([1-6])\\2){1,6}$")){
					return 0;
				}
				if(haoMas.length > 1 && !haoMas[1].matches("^1?2?3?4?5?6?$")){
					return 0;
				}
				if (haoMas.length > 1) {
					return haoMas[0].length()*haoMas[1].length()/2;
				}
				return 0;
			case PlayCodeConstants.ebth://二不同号
				haoMas = haoMa.split(",");
				zs = 0;
				for (String hm : haoMas) {
					if (hm.matches("^[1-6]$")) {
						zs++;
					}
				}
				if(zs<2){
					return 0;
				}
				return zs*(zs-1)/2;
		}
		return 0;
	}

	/**
	 * 将字符串每两位截取
	 * 
	 * @param str
	 * @return
	 */
	private static String[] splitWithTowChar(String str) {
		if (!Utils.isEmptyString(str)) {
			String[] arr = new String[str.length() / 2];
			for (int i = 0; i < str.length() / 2; i++) {
				arr[i] = str.substring(i * 2, i * 2 + 2);
			}
			return arr;
		}
		return null;
	}

	/**
	 * 时时彩投注注数计算
	 *
	 * @param playTypeCode
	 * @param haoma
	 * @return
	 */
	public static int getSscZhushu(String playCode, String haoMa) {
		switch (playCode) {
			case PlayCodeConstants.dwd:
				return dwd(haoMa, 5);
			case PlayCodeConstants.bdw_h31m:
			case PlayCodeConstants.bdw_q31m:
			case PlayCodeConstants.bdw_z31m:
				return bdwOne(haoMa);

			case PlayCodeConstants.h2zx_hz:
			case PlayCodeConstants.q2zx_hz:
				return erXingHeZhi(haoMa);
			case PlayCodeConstants.h3zux_zu3:
			case PlayCodeConstants.q3zux_zu3:
			case PlayCodeConstants.z3zux_zu3:
				return one09ZuHe(haoMa, 1);
			case PlayCodeConstants.h3zux_zu6:
			case PlayCodeConstants.q3zux_zu6:
			case PlayCodeConstants.z3zux_zu6:
				return one09ZuHe6(haoMa);
			//  复式
			case PlayCodeConstants.q2zx_fs:// 二复试
			case PlayCodeConstants.h2zx_fs:
				return one09By2Or3Nums(haoMa, 2);
			case PlayCodeConstants.q2zx_ds:// 二单试
			case PlayCodeConstants.h2zx_ds:
				return danShi(haoMa, "\\d\\d");
			case PlayCodeConstants.q3zx_fs:// 三复试
			case PlayCodeConstants.h3zx_fs:
			case PlayCodeConstants.z3zx_fs:
				return one09By2Or3Nums(haoMa, 3);
			case PlayCodeConstants.q3zx_ds:// 三单试
			case PlayCodeConstants.h3zx_ds:
			case PlayCodeConstants.z3zx_ds:
				return danShi(haoMa, "\\d{3}");
			case PlayCodeConstants.q4zx_fs:
			case PlayCodeConstants.h4zx_fs:
				return one09By2Or3Nums(haoMa, 4);
			case PlayCodeConstants.q4zx_ds:
			case PlayCodeConstants.h4zx_ds:
				return danShi(haoMa, "\\d{4}");
			case PlayCodeConstants.wxzx_fs:
				return one09By2Or3Nums(haoMa, 5);
			case PlayCodeConstants.wxzx_ds:
				return danShi(haoMa, "\\d{5}");

			case PlayCodeConstants.rxwf_r2zx_fs:
				return renXuanFuShi(haoMa, 2);
			case PlayCodeConstants.rxwf_r3zx_fs:
				return renXuanFuShi(haoMa, 3);
			case PlayCodeConstants.rxwf_r4zx_fs:
				return renXuanFuShi(haoMa, 4);
			case PlayCodeConstants.rxwf_r3zux_zu3:
				return renXuanRen3Zu(haoMa, 2);
			case PlayCodeConstants.rxwf_r3zux_zu6:
				return renXuanRen3Zu(haoMa, 3);
			case PlayCodeConstants.dxds_h2:
			case PlayCodeConstants.dxds_q2:
				return daXiaoDanShuang(haoMa, 2);
			case PlayCodeConstants.dxds_h3:
			case PlayCodeConstants.dxds_q3:
				return daXiaoDanShuang(haoMa, 3);
			case PlayCodeConstants.dxds_zh:
				return daXiaoDanShuang(haoMa, 1);
			case PlayCodeConstants.longhudou:
				if (!haoMa.matches("^龙?虎?$")) {
					return 0;
				}
				return haoMa.length();
			case PlayCodeConstants.longhuhe:
				if (!haoMa.equals("和")) {
					return 0;
				}
				return 1;
			case PlayCodeConstants.baozi:
			case PlayCodeConstants.shunzi:
			case PlayCodeConstants.duizi:
			case PlayCodeConstants.zaliu:
			case PlayCodeConstants.banshun:
				if (!haoMa.matches("^(前三)?(中三)?(后三)?$")) {
					return 0;
				}
				return haoMa.length() / 2;
			default:
				return 0;
		}
	}

	private static int dwd(String haoMa, int length) {
		int zs = 0;
		String[] hms = haoMa.split(",");
		if (hms.length != length) {
			return zs;
		}
		for (String hm : hms) {
			if (!hm.matches(reg09))// 验证投注号码
				return 0;
			if (!"-".equals(hm)) {
				zs += hm.length();
			}
		}
		return zs;
	}

	/**
	 * 单式
	 * 
	 * @param haoMa
	 * @return
	 */
	private static int danShi(String haoMa, String pattern) {
		String[] hms = haoMa.split(";");
		for (String hm : hms) {
			if (!hm.matches(pattern)) {
				return 0;
			}
		}
		return hms.length;
	}

	/**
	 * 任选组选，组三，组六
	 * 
	 * @param haoMa
	 * @param balls
	 * @return
	 */
	private static int renXuanRen3Zu(String haoMa, int balls) {
		String[] haoMas = haoMa.split("@");
		if (haoMas.length != 2 || !haoMas[0].matches("^(万)?((,|\\b)千)?((,|\\b)百)?((,|\\b)十)?(,个)?$") || !haoMas[1].matches(reg09Comma)) {
			return 0;
		}
		int weiZhiLength = haoMas[0].split(",").length;
		if (weiZhiLength < 3) {
			return 0;
		}
		String[] hms = haoMas[1].split(",");
		if (hms.length < balls)
			return 0;

		weiZhiLength = weiZhiLength * (weiZhiLength - 1) * (weiZhiLength - 2) / 6;
		int zhuShu = 1;
		switch (balls) {
			case 2:
				for (int i = hms.length, len = hms.length - 2; i > len; i--) {
					zhuShu *= i;
				}
				break;
			default:
				int l = 1;
				for (int a = 0; a < balls; a++)
					zhuShu *= hms.length - a;
				for (int a = balls; 0 < a; a--)
					l *= a;
				zhuShu /= l;
		}
		return zhuShu * weiZhiLength;
	}

	/**
	 * 任选复式
	 * 
	 * @param haoMa
	 * @param balls
	 *            任选球数
	 * @return
	 */
	private static int renXuanFuShi(String haoMa, int balls) {
		String[] hms = haoMa.split(",");
		if (hms.length < balls) {
			return 0;
		}
		List<Integer> list = new ArrayList<>();
		for (String hm : hms) {
			if (!hm.matches(reg09)) {
				return 0;
			}
			if (!"-".equals(hm)) {
				list.add(hm.length());
			}
		}
		if (list.size() < balls) {
			return 0;
		}
		List<List<Integer>> result = new ArrayList<>();
		paiLie(result, new ArrayList<Integer>(), list, new HashSet<Integer>(), balls, 0);
		int all = 0;
		for (List<Integer> l : result) {
			int a = 1;
			for (Integer i : l) {
				a *= i;
			}
			all += a;
		}
		return all;
	}

	/**
	 * 任选排列
	 * 
	 * @param s
	 * @param iL
	 * @param m
	 * @param start
	 */
	private static void paiLie(List<List<Integer>> result, List<Integer> temp, List<Integer> arr, Set<Integer> set, int m, int start) {
		if (m == 0) {
			result.add(temp);
			return;
		}
		Set<Integer> iL2;
		for (int i = start; i < arr.size(); i++) {
			iL2 = new HashSet<Integer>();
			iL2.addAll(set);
			if (!iL2.contains(i)) {
				List<Integer> t1 = new ArrayList<>();
				t1.addAll(temp);
				t1.add(arr.get(i));
				iL2.add(i);
				paiLie(result, t1, arr, iL2, m - 1, i);
			}
		}
	}

	/**
	 * 北京赛车投注注数计算
	 *
	 * @param playTypeCode
	 * @param haoma
	 * @return
	 */
	public static int getBjscZhushu(String playCode, String haoMa) {
		switch (playCode) {
			case PlayCodeConstants.gyhz:
				int zs = 0;
				String[] split = haoMa.split(",");
				for (String h : split) {
					int s = Integer.parseInt(h);
					if (s < 3 || s > 19) {
						return 0;
					}
					zs += BJSCHZ[s];
				}
				return zs;
			case PlayCodeConstants.dxds:// 大小单双
				if (haoMa.matches("^大?小?单?双?$")) {
					return haoMa.length();
				}
				return 0;
			case PlayCodeConstants.dwd:
				return towNumDWD(haoMa, 10, reg10);

			case PlayCodeConstants.q1zx_fs:
				if (!haoMa.matches(reg10)) {
					return 0;
				}
				return haoMa.length() / 2;
			case PlayCodeConstants.q2zx_fs:
				return towNum2XFuShi(haoMa, reg10);
			case PlayCodeConstants.q3zx_fs:
				return towNum3XFuShi(haoMa, reg10);
			case PlayCodeConstants.longhu_yajun:
			case PlayCodeConstants.longhu_gunjun:
			case PlayCodeConstants.longhu_jijun:
				if (!haoMa.matches("^龙?虎?$")) {
					return 0;
				}
				return haoMa.length();
			case PlayCodeConstants.q2zx_ds:
				return danShi(haoMa, "(?!(\\d\\d),\\1)(?:0\\d|10),(?:0\\d|10)");
			case PlayCodeConstants.q3zx_ds:
				return danShi(haoMa, "(?!(?:\\d\\d,)*?(\\d\\d),(?:\\d\\d,)*?\\1(?:\\d\\d)*?)(?:0\\d|10),(?:0\\d|10),(?:0\\d|10)");
			default:
				return 0;
		}
	}

	/**
	 * 11选5投注注数计算 s
	 * 
	 * @param playCode
	 * @param haoMa
	 * @return
	 */
	public static int get11x5Zhushu(String playCode, String haoMa) {
		switch (playCode) {
			case PlayCodeConstants.rxfs_rx1z1:
				return nXingZuXuan(1, haoMa);
			case PlayCodeConstants.rxfs_rx2z2:
				return nXingZuXuan(2, haoMa);
			case PlayCodeConstants.rxfs_rx3z3:
				return nXingZuXuan(3, haoMa);
			case PlayCodeConstants.rxfs_rx4z4:
				return nXingZuXuan(4, haoMa);
			case PlayCodeConstants.rxfs_rx5z5:
				return nXingZuXuan(5, haoMa);
			case PlayCodeConstants.rxfs_rx6z5:
				return nXingZuXuan(6, haoMa);
			case PlayCodeConstants.rxfs_rx7z5:
				return nXingZuXuan(7, haoMa);
			case PlayCodeConstants.rxfs_rx8z5:
				return nXingZuXuan(8, haoMa);

			case PlayCodeConstants.dwd:
				return towNumDWD(haoMa, 5, reg11);

			case PlayCodeConstants.bdw_q3:
			case PlayCodeConstants.bdw_h3:
			case PlayCodeConstants.bdw_z3:
				return buDingDan(haoMa);

			case PlayCodeConstants.q2zx_fs:
			case PlayCodeConstants.h2zx_fs:
				return towNum2XFuShi(haoMa, reg11);
			case PlayCodeConstants.h3zx_fs:
			case PlayCodeConstants.q3zx_fs:
			case PlayCodeConstants.z3zx_fs:
				return towNum3XFuShi(haoMa, reg11);
			case PlayCodeConstants.q2zx:
			case PlayCodeConstants.h2zx:
				return nXingZuXuan(2, haoMa);
			case PlayCodeConstants.h3zx:
			case PlayCodeConstants.q3zx:
			case PlayCodeConstants.z3zx:
				return nXingZuXuan(3, haoMa);
			case PlayCodeConstants.q2zx_ds:
			case PlayCodeConstants.h2zx_ds:
				return danShi(haoMa, "(?!(\\d\\d),\\1)(?:0\\d|1[0,1]),(?:0\\d|1[0,1])");
			case PlayCodeConstants.q3zx_ds:
			case PlayCodeConstants.z3zx_ds:
			case PlayCodeConstants.h3zx_ds:
				return danShi(haoMa, "(?!(?:\\d\\d,)*?(\\d\\d),(?:\\d\\d,)*?\\1(?:\\d\\d)*?)(?:0\\d|1[0,1]),(?:0\\d|1[0,1]),(?:0\\d|1[0,1])");

			default:
				return 0;
		}
	}

	/** 号码为两位的投注 二星复式算法 */
	public static int towNum2XFuShi(String haoMa, String reg) {
		String[] split = haoMa.split(",");
		if (split.length != 2)
			return 0;
		for (String hm : split) {
			if (!hm.matches(reg)) {
				return 0;
			}
		}
		String[] one = splitWithTowChar(split[0]);
		String[] two = splitWithTowChar(split[1]);
		int zs = one.length * two.length;
		zs -= arrayIntersection(one, two);
		return zs;
	}

	/**
	 * 获得2个数组中公共的部分的长度
	 * 
	 * @param one
	 * @param tow
	 * @return
	 */
	private static int arrayIntersection(String[] one, String[] two) {
		Set<String> set = new HashSet<>();
		for (String s : one) {
			set.add(s);
		}
		int i = 0;
		for (String s : two) {
			if (set.contains(s)) {
				i++;
			}
		}
		return i;
	}

	private static int arrayIntersection(String[] one, String[] two, String[] three) {
		Set<String> set1 = new HashSet<>();
		Set<String> set2 = new HashSet<>();
		for (String s : one) {
			set1.add(s);
		}
		for (String s : two) {
			set2.add(s);
		}
		int i = 0;
		for (String s : three) {
			if (set1.contains(s) && set2.contains(s)) {
				i++;
			}
		}
		return i;
	}

	/** 号码为两位的投注 三星复式算法 */
	public static int towNum3XFuShi(String haoMa, String reg) {
		String[] split = haoMa.split(",");
		if (split.length != 3)
			return 0;
		for (String hm : split) {
			if (!hm.matches(reg)) {
				return 0;
			}
		}
		String[] one = splitWithTowChar(split[0]);
		String[] two = splitWithTowChar(split[1]);
		String[] three = splitWithTowChar(split[2]);
		int zs = one.length * two.length * three.length;
		zs = zs - three.length * arrayIntersection(one, two);
		zs = zs - one.length * arrayIntersection(three, two);
		zs = zs - two.length * arrayIntersection(three, one);
		zs = zs + arrayIntersection(one, two, three) * 2;
		return zs;
	}

	/** 不定胆 */
	// 不定蛋特殊处理
	public static int buDingDan(String haoMa) {
		if (!haoMa.matches(reg11Comma))
			return 0;
		return haoMa.split(",").length;
	}

	/** 号码为两位的投注 定位胆 */
	public static int towNumDWD(String haoMa, int length, String reg) {
		int zs = 0;
		String[] hms = haoMa.split(",");
		if (hms.length != length)
			return 0;
		for (String hm : hms) {
			if (!hm.matches(reg))
				return 0;
			if (!"-".equals(hm)) {
				zs += hm.length() / 2;
			}
		}
		return zs;
	}

	/** N星组选 */
	public static int nXingZuXuan(int nBall, String haoMa) {
		if (haoMa.matches("(?:\\d\\d,)*?(\\d\\d),(?:\\d\\d,)*?\\1(?:\\d\\d)*?")) {
			return 0;
		}
		if (!haoMa.matches("(0[1-9]|10|11)(?:,(?:0[1-9]|10|11)){" + (nBall - 1) + ",}"))// 验证投注号码
			return 0;
		return comb(haoMa.split(",").length, nBall);
	}

	/**
	 * 计算组合个数
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	private static int comb(int n, int m) {
		if (n < m)
			return 0;
		int n1 = 1, n2 = 1;
		for (int i = n, j = 1; j <= m; n1 *= i--, n2 *= j++)
			;
		return n1 / n2;
	}

	/**
	 * 排列3 福彩3D 投注注数计算
	 * 
	 * @param playTypeCode
	 * @param haoma
	 * @return
	 */
	private static int getPL3ZhuShu(String playCode, String haoMa) {
		switch (playCode) {
			case PlayCodeConstants.bdw_1m:// 不定位胆
				return one09DanZhu(haoMa);
			case PlayCodeConstants.dwd:// 定位胆
				return dwd(haoMa, 3);
			case PlayCodeConstants.dxds_q2:
			case PlayCodeConstants.dxds_h2:
				return daXiaoDanShuang(haoMa, 2);
			case PlayCodeConstants.q2zx_fs:
			case PlayCodeConstants.h2zx_fs:
				return one09By2Or3Nums(haoMa, 2);
			case PlayCodeConstants.zhx_fs:
				return one09By2Or3Nums(haoMa, 3);
			case PlayCodeConstants.zhx_ds:
				return danShi(haoMa, "(\\d\\d\\d;)*\\d\\d\\d");
			case PlayCodeConstants.h2zx_ds:// 单式
			case PlayCodeConstants.q2zx_ds:
				return danShi(haoMa, "(\\d\\d;)*\\d\\d");
			case PlayCodeConstants.em_q2zux:
			case PlayCodeConstants.em_h2zux:
			case PlayCodeConstants.bdw_2m:
				return one09ZuHe(haoMa, 2);
			case PlayCodeConstants.zux_z3:
				return one09ZuHe(haoMa, 1);
			case PlayCodeConstants.zux_z6:
				return one09ZuHe6(haoMa);
			default:
				return 0;
		}
	}

	/**
	 * 前2，后2 大小单双
	 * 
	 * @param haoMa
	 * @return
	 */
	private static int daXiaoDanShuang(String haoMa, int geShu) {
		int zs = 0;
		String[] split = haoMa.split(",");
		if (split.length == geShu) {
			zs = 1;
			for (String hm : split) {
				if (!hm.matches("^大?小?单?双?$"))// 验证投注号码
					return 0;
				zs *= hm.length();
			}
		}
		return zs;
	}

	/**
	 * PC蛋蛋计算注数
	 * 
	 * @param playTypeCode
	 * @param haoma
	 * @return
	 */
	private static int getPceggZhuShu(String playCode, String haoMa) {
		if (Utils.isEmptyString(haoMa))
			return 0;
		switch (playCode) {
			case PlayCodeConstants.hz:// 和值
				int zs = 0;
				String[] split = haoMa.split(",");
				for (String h : split) {
					int s = Integer.parseInt(h);
					if (s < 0 || s > 27) {
						return 0;
					}
					zs += SXHZ[s];
				}
				return zs;
			case PlayCodeConstants.dxds:// 大小单双
				if (haoMa.matches("^大?小?单?双?$")) {
					return haoMa.length();
				}
				return 0;
			case PlayCodeConstants.q2zx_fs:// 前二复式
			case PlayCodeConstants.h2zx_fs:// 后二复式
				return one09By2Or3Nums(haoMa, 2);
			case PlayCodeConstants.sxfs:// 三星复式
				return one09By2Or3Nums(haoMa, 3);
			case PlayCodeConstants.q2zx:// 前二组选
			case PlayCodeConstants.h2zx:// 后二组选
				return one09ZuHe(haoMa, 2);

			case PlayCodeConstants.sxzx:// 三星组选
				return one09ZuHe6(haoMa);
			case PlayCodeConstants.bdw_pcegg:// 不定位胆
				return one09DanZhu(haoMa);
			case PlayCodeConstants.dwd:// 定位胆
				return dwd(haoMa, 3);
			case PlayCodeConstants.h2zx_ds:// 单式
			case PlayCodeConstants.q2zx_ds:
				return danShi(haoMa, "(\\d\\d;)*\\d\\d");
			case PlayCodeConstants.sxds:
				return danShi(haoMa, "(\\d\\d\\d;)*\\d\\d\\d");
			default:
				return 0;
		}
	}

	/**
	 * 2号码一注,或者3号码一注
	 * 
	 * @param haoma
	 * @return
	 */
	private static int one09By2Or3Nums(String haoMa, int balls) {
		int zs = 0;
		String[] split = haoMa.split(",");
		if (split.length == balls) {
			zs = 1;
			for (String hm : split) {
				if (!hm.matches(reg09))// 验证投注号码
					return 0;
				zs *= hm.length();
			}
		}
		return zs;
	}

	/**
	 * 组选
	 * 
	 * @param haoma
	 * @return
	 */
	private static int one09ZuHe(String haoMa, int step) {
		if (!haoMa.matches(reg09Comma))// 验证投注号码
			return 0;
		int zs = 0;
		String[] split = haoMa.split(",");
		if (split.length > 0)
			zs = split.length * (split.length - 1) / step;
		return zs;
	}

	/**
	 * 组选6
	 * 
	 * @param haoma
	 * @return
	 */
	private static int one09ZuHe6(String haoMa) {
		if (!haoMa.matches(reg09Comma))// 验证投注号码
			return 0;
		int zs = 0;
		String[] split = haoMa.split(",");
		if (split.length > 1) {
			zs = split.length * (split.length - 1) * (split.length - 2) / 6;
		}
		return zs;
	}

	/**
	 * 根据逗号分开后相加
	 * 
	 * @param haoma
	 * @return
	 */
	private static int one09DanZhu(String haoMa) {
		int zs = 0;
		for (String hm : haoMa.split(",")) {
			if (!hm.matches(reg09))// 验证投注号码
				return 0;
			if (!"-".equals(hm)) {
				zs += hm.length();
			}
		}
		return zs;
	}

	public static void main(String[] args) {
		// System.out.println("01,03,04,05,06,07,09".matches("(0[1-9]|10|11)(?:,(?:0[1-9]|10|11)){"+(4-1)+",}"));
		// System.out.println("02".matches("0[1-9]|10|11"));
		// System.out.println("03".matches("0[1-9]|10|11"));
		// System.out.println("04".matches("0[1-9]|10|11"));
		// System.out.println("05".matches("0[1-9]|10|11"));
		// System.out.println("06".matches("0[1-9]|10|11"));
		// System.out.println("07".matches("0[1-9]|10|11"));
		// System.out.println("08".matches("0[1-9]|10|11"));
		// System.out.println("09".matches("0[1-9]|10|11"));
		// System.out.println("10".matches("0[1-9]|10|11"));
		// System.out.println("11".matches("0[1-9]|10|11"));
		// System.out.println("千,百,十".matches("^(万)?((,|\\b)千)?((,|\\b)百)?((,|\\b)十)?(,个)?$"));
//		 System.out.println("1,2,3,4,5,6,11".matches(reg09Comma));
		// System.out.println("01,03,01".matches("(?:\\d\\d,)*?(\\d\\d),(?:\\d\\d,)*?\\1(?:\\d\\d)*?"));
		// System.out.println(nXingZuXuan(2, "01,01"));
//		System.out.println("11112244336644".matches("(([1-6])\\2){1,6}"));
	}

	private static int bdwOne(String haoMa) {
		if (!haoMa.matches(reg09Comma)) {
			return 0;
		}
		return haoMa.split(",").length;
	}

}
