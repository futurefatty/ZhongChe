package com.crcc.commonlib.utils;

import java.math.BigDecimal;

/**
 * author: Six
 * Created by on 2018/8/7
 */
public class BigDecimalUtil {


    private BigDecimalUtil() {

    }

    public static BigDecimal clearNoUseZeroForBigDecimal(BigDecimal num) {
        BigDecimal returnNum = null;
        String numStr = num.stripTrailingZeros().toPlainString();
        if (!numStr.contains(".")) {
            // 如果num 不含有小数点,使用stripTrailingZeros()处理时,变成了科学计数法
            returnNum = new BigDecimal(numStr);
        } else {
            if (num.compareTo(BigDecimal.ZERO) == 0) {
                returnNum = BigDecimal.ZERO;
            } else {
                returnNum = num.stripTrailingZeros();
            }
        }
        return returnNum;
    }

    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }


    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//四舍五入,保留2位小数
    }
}
