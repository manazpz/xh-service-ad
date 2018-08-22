package aq.common.datasource;

public class Multiple {

    //数据源名称
    public static final String MASTER = "MASTER";
    public static final String SLAVE = "SLAVE";

    // 线程本地环境
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

    // 设置数据源
    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }
    // 获取数据源
    public static String  getDataSourceKey() {
        return dataSourceKey.get();
    }
    // 清除数据源
    public static void removeDataSource() {
       dataSourceKey.remove();
    }
}
