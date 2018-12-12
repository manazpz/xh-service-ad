package aq.dao.report;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoCustomerR")
public interface CustomerRDao {

    //客户报表
    List<Map<String,Object>> selectCustomerReport(Map<String,Object> map);
}
