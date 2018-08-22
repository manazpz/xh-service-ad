package aq.common.datasource;
/*
 * 动态数据源切换
 * */
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class Routing extends AbstractRoutingDataSource {

    protected Object determineCurrentLookupKey() {
        return Multiple.getDataSourceKey();
    }
}
