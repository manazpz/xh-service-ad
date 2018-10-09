package aq.dao.user;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoCustomer")
public interface CustomerDao {

    //获取客户信息
    List<Map<String,Object>> selectCustomerInfo(Map<String,Object> map);

}
