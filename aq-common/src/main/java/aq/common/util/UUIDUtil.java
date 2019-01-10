package aq.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class UUIDUtil {

    /**
     * 功能：去掉UUID中的“-”
     */
    public static String replaceUUID(String uuid) {
        uuid = uuid.replaceAll("\\-", "");
        return uuid;
    }
    /**
     * 功能：获取UUID并去除“-”
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("\\-", "");
    }

    /**
     * 功能：获取当前时间作为编号
     * @return
     */
    public static String getRandomReqNo() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date()).toString();// new Date()为获取当前系统时间，也可使用当前时间戳
    }
    /**
     * 获取随机的流水号(8位)
     * @return
     */
    public static String getRandomReqNumber() {
        int length=8;
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取随机的流水号(8位)
     * @return
     */
    public static String getRandomOrderId() {
        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = new Date();

        String str = simpleDateFormat.format(date);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + rannum;
    }

    /**
     * 获取随机的流水号(2位)
     * @return
     */
    public static String getRandomTwoNumber() {
        int length=2;
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
