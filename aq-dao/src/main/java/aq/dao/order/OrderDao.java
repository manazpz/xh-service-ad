package aq.dao.order;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoOrder")
public interface OrderDao {

    //查询订单
    List<Map<String,Object>> selectorderList(Map<String,Object> map);

    //添加订单
    int insertOrder(Map<String,Object> map);

    //添加订单明细
    int insertOrderDetail(Map<String,Object> map);

    //查询订单
    List<Map<String,Object>> selectorderDetailList(Map<String,Object> map);

}
