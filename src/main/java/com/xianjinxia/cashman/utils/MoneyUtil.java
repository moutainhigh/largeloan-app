package com.xianjinxia.cashman.utils;

import java.math.BigDecimal;

/**
 * 计算器工具类
 * 提供准确精度的 加减乘除
 * 默认除法保留2位小数，四舍五入
 * @author mjh
 */
public class MoneyUtil {
	
	private static final int DEF_DIV_SCALE = 0;   // 默认除法运算精度
	private static final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";
	private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
	public static String add(Integer v1, Integer v2) {
		return add(Integer.toString(v1),Integer.toString(v2));
	}


	public static String add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}



	/**
	 * 提供精确的减法运算。
	 * @param v1       被减数
	 * @param v2      减数
	 * @return     两个参数的差
	 */
	public static String sub(Integer v1, Integer v2) {
		return sub(Integer.toString(v1),Integer.toString(v2));
	}

	public static String sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}



	/**
	 * 提供精确的乘法运算-参数为double
	 * @param v1        被乘数
	 * @param v2         乘数
	 * @return 两个参数的积
	 */
	public static String multiply(Integer v1, Integer v2) {
        return multiply(Integer.toString(v1), Integer.toString(v2));
	}

    /**
     * 提供精确的乘法运算-参数为double
     * @param v1        被乘数
     * @param v2         乘数
     * @return 两个参数的积
     */
    public static String multiply(String v1, String v2) {
        BigDecimal balance1 = new BigDecimal(v1);
        BigDecimal balance2 = new BigDecimal(v2);
        return balance1.multiply(balance2).toString();
    }


    /**
     * 除法运算，当发生除不尽的情况时，精确到 小数点以后 DEF_DIV_SCALE(默认0位） 位，以后的数字四舍五入。
     * @param v1          被除数
     * @param v2          除数
     * @return       两个参数的商
     */
	public static String div(Integer v1,Integer v2){
	    return div(Integer.toString(v1),Integer.toString(v2));
    }

    /**
     * 除法运算，当发生除不尽的情况时，精确到 小数点以后 DEF_DIV_SCALE(默认0位） 位，以后的数字四舍五入。
     * @param v1          被除数
     * @param v2          除数
     * @return       两个参数的商
     */
    public static String div(String v1, String v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }


    /**
     * 除法运算 当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * @param v1           被除数
     * @param v2            除数
     * @param scale   表示表示需要精确到小数点以后几位。
     * @return        两个参数的商
     */
    public static String div(Integer v1, Integer v2, int scale) {
        return div(Integer.toString(v1),Integer.toString(v2),scale);
    }
	
    /**
     * 除法运算 当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * @param v1           被除数
     * @param v2            除数
     * @param scale   表示表示需要精确到小数点以后几位。
     * @return        两个参数的商
     */
    public static String div(String v1, String v2, int scale) {
        return div(v1,v2,scale,BigDecimal.ROUND_HALF_UP);
    }

    public static String div(Integer v1,Integer v2,int scale,int roundMode){
        return div(Integer.toString(v1),Integer.toString(v2),scale,roundMode);
    }

	public static String div(String v1,String v2,int scale,int roundMode){
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度必须是正整数或者0");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, roundMode).toString();
	}



    /**
     * 小数位四舍五入处理。
     * @param v      需要四舍五入的数字
     * @param scale  小数点后保留几位
     * @return       四舍五入后的结果
     */
    public static String round(String v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"精度必须是正整数或者0");
		}
		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 把元转换为分--传入的元是Integer类型
	 * @param money
	 * @return
	 */
	public static Integer changeYuanToIntCent(Integer money ){
		return Integer.valueOf(multiply(money,100));
	}

	/**
	 * 把元转换为分--传入的元是Double类型
	 * @param money
	 * @return
	 */
	public static Integer changeYuanToIntCent(Double money ){
		BigDecimal amount = new BigDecimal(money.toString()).multiply(new BigDecimal(Double.toString(100)));
		return amount.intValue();
	}

	/**
	 * 把分转换为元
	 * @param money
	 * @return
	 */
	public static BigDecimal changeCentToYuan(Integer money ){
		return new BigDecimal(div(money,100,2));
	}

	/**
	 * 把分转换为元
	 * @param money
	 * @return
	 */
	public static Integer changeYuanToCent(BigDecimal money ){
		return Integer.valueOf(multiply(money.toString(),"100"));
	}

	/**
	 * 把分转换为元 --传入分类型为BigDecimal类型时
	 * @param money
	 * @return
	 */
	public static BigDecimal changeBigCentToYuan(BigDecimal money ){
		return new BigDecimal(div(money.toString(),"100",2));
	}

	/**
	 * 输入以元为单位的
	 * @param v
	 * @return
	 */
	public static String change(Integer v) {
		if (v==0){
			return "零元整";
		}
		String strValue = v + "";
		// i用来控制数
		int i = 0;
		// j用来控制单位
		int j = UNIT.length() - strValue.length();
		String rs = "";
		boolean isZero = false;
		for (; i < strValue.length(); i++, j++) {
			char ch = strValue.charAt(i);
			if (ch == '0') {
				isZero = true;
				if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
					rs = rs + UNIT.charAt(j);
					isZero = false;
				}
			} else {
				if (isZero) {
					rs = rs + "零";
					isZero = false;
				}
				rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
			}
		}
		if (!rs.endsWith("分")) {
			rs = rs + "整";
		}
		rs = rs.replaceAll("亿万", "亿");
		return rs;
	}
}
