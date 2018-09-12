package aq.common.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    //中文转Unicode
    public static String chinaToUnicode(String str){
        String result="";
        for (int i = 0; i < str.length(); i++){
            int chr1 = (char) str.charAt(i);
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)
                result+="\\u" + Integer.toHexString(chr1);
            }else{
                result+=str.charAt(i);
            }
        }
        return result;
    }

    //Unicode To 中文
    public static String unicodeToChina( String str){
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    // is Json String Or Not
    public static boolean isJsonStr(String str){
        try{
            if (null == str || "".equals(str.trim())) return false;
            new JsonParser().parse(str);
            return true;
        }
        catch (JsonParseException ex){
            return false;
        }
    }

    // is Empty Or Not
    public static boolean isEmpty(Object o) {
        if (null == o || "".equals(o.toString()) || "undefined".equals(o.toString())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断一组字符串是否有空值
     *
     * @param strs 需要判断的一组字符串
     * @return 判断结果，只要其中一个字符串为null或者为空，就返回true
     */
    public static boolean hasBlank(String... strs) {
        if (null == strs || 0 == strs.length) {
            return true;
        } else {
            //这种代码如果用java8就会很优雅了
            for (String str : strs) {
                if (isEmpty(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  获取指定长度的字符串
     * @param str 原始字符串
     * @param length 结果字符串的长度
     * @param cc 插入的字符串
     * @param flag true->高位补齐/false->低位补齐
     * @return 返回指定长度的字符串
     */
    public static String format(String str, int length, String cc, Boolean flag) {
        cc = isEmpty(cc)?" ":cc;
        for (int i=str.length(); i < length; i++) {
            if(flag){
                str = cc + str;
            }else{
                str = str + cc;
            }
        }
        return str;
    }

    public static String splitString(String src, int len) {
        int byteNum = 0;

        if (null == src) {
            System.out.println("The source String is null!");
            return "";
        }

        byteNum = src.length();
        byte bt[] = src.getBytes(); // 将String转换成byte字节数组

        if (len > byteNum) {
            len = byteNum;
        }

        // 判断是否出现了截半，截半的话字节对于的ASC码是小于0的值
        String subStrx = "";
        if (bt[len] < 0) {
            subStrx = new String(bt, 0, --len);
        } else {
            subStrx = new String(bt, 0, len);
        }
        return subStrx;
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return "";
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }

        return str.substring(start);
    }

    //String 转 JsonObject
    public static JsonObject toJsonObject(String str){
        return GsonHelper.getInstanceJsonparser().parse(str).getAsJsonObject();
    }

    public static JsonObject toJsonObject(String key, String str){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key,GsonHelper.getInstanceJsonparser().parse(str).getAsString());
        return jsonObject;
    }

}
