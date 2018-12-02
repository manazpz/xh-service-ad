package aq.common.util;

import aq.common.constants.APPConstants;

import java.io.*;
import java.util.*;

/**
 * 数据库备份与还原
 */
public class DbOperate {

    /**
     * 备份数据库db
     *
     * @param root
     * @param pwd
     * @param dbName
     * @param backPath
     * @param backName
     */
    public static void dbBackUp(String root, String pwd, String dbName, String backPath, String backName) throws Exception {
        String pathSql = backPath + backName;
        File fileSql = new File(pathSql);
        //创建备份sql文件
        if (!fileSql.exists()) {
            fileSql.createNewFile();
        }
        //mysqldump -hlocalhost -uroot -p123456 db > /home/back.sql
        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump");
        sb.append(" -h"+ APPConstants.SPBILL_CREATE_IP);
        sb.append(" -u" + root);
        sb.append(" -p" + pwd);
        sb.append(" " + dbName + " >");
        sb.append(pathSql);
        System.out.println("cmd命令为：" + sb.toString());
        Runtime runtime = Runtime.getRuntime();
        System.out.println("开始备份：" + dbName);
        Process process = runtime.exec("cmd /c" + sb.toString());
        System.out.println("备份成功!");
    }

    /**
     * 恢复数据库
     *
     * @param root
     * @param pwd
     * @param dbName
     * @param filePath mysql -hlocalhost -uroot -p123456 db < /home/back.sql
     */
    public static void dbRestore(String root, String pwd, String dbName, String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append("mysql");
        sb.append(" -h" + APPConstants.SPBILL_CREATE_IP);
        sb.append(" -u" + root);
        sb.append(" -p" + pwd);
        sb.append(" " + dbName + " <");
        sb.append(filePath);
        System.out.println("cmd命令为：" + sb.toString());
        Runtime runtime = Runtime.getRuntime();
        System.out.println("开始还原数据");
        try {
            Process process = runtime.exec("cmd /c" + sb.toString());
            InputStream is = process.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is, "utf8"));
            String line = null;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
            }
            is.close();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("还原成功！");
    }

    public static List getFileName(String path) {
       List list = new ArrayList();
       list.clear();
       File f = new File(path);
       if (!f.exists()) {
           return list;
       }
       File fa[] = f.listFiles();
       for (int i = 0; i < fa.length; i++) {
          File fs = fa[i];
          list.add(fs.getName());
       }
       Collections.reverse(list);
       return list;
    }


}