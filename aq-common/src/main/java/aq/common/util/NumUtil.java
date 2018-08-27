package aq.common.util;

public class NumUtil {

    //比较double
    public static double compareDouble(double d1,double min,double max) {
        return d1 < min ? min : d1 > max ? max : d1;
    }

}
