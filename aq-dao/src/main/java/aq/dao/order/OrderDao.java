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

    //修改订单
    int updateOrder(Map<String,Object> map);

    //更新订单明细
    int updateOrderDetail(Map<String,Object> map);

    //新增评论
    int insertRate(Map<String,Object> map);

    //查询评论
    List<Map<String,Object>> selectRate(Map<String,Object> map);

    //新增支付明细
    int insertBlance(Map<String,Object> map);

    //更新评论信息
    int updateRate(Map<String,Object> map);

    //查询物流信息
    List<Map<String,Object>> selectLogistical(Map<String,Object> map);

    //新增物流信息
    int insertLogistics(Map<String,Object> map);

    //新增退货申请信息
    int insertReturn(Map<String,Object> map);

    //查询退货申请信息
    List<Map<String,Object>> selectorderReturn(Map<String,Object> map);

    //更新退货申请信息
    int updataOrderReturn(Map<String,Object> map);

}
