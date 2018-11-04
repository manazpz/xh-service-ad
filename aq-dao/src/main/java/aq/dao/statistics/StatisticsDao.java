package aq.dao.statistics;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;


@MapperScan("daoStatistics")
public interface StatisticsDao {

    //获取有效客户总数
    Map countCustomer();

    //获取有效商品总数
    Map countGoods();

    //获取已完结总数
    Map countEndOrder();

    //获取有效客户最新一条
    Map newestCustomer();

    //获取有效客户最新一条
    Map newestGoods();

    //获取完结订单最新一条
    Map newestEndOrder();

    //获取一周有效客户总数
    List<Map<String,Object>> countWeekCustomer(Map<String,Object> map);

    //获取一周上线商品
    List<Map<String,Object>> countWeekGoods(Map<String,Object> map);

    //获取有效客户列表
    List<Map<String,Object>> countWeekEndOrder(Map<String,Object> map);

    //获取订单3大类型
    List<Map<String,Object>> countOrderType(Map<String,Object> map);

    //获取订单半年销售
    List<Map<String,Object>> countEndOrderMoon(Map<String,Object> map);

    //获取订单半年销售
    List<Map<String,Object>> countEndOrderYear(Map<String,Object> map);

    //获取有效客户列表
    List<Map<String,Object>> selectCustomer(Map<String,Object> map);

    //获取标签商品
    List<Map<String,Object>> selectLableGoods(Map<String,Object> map);

    //昨日热销商品
    List<Map<String,Object>> selectZrSaleGoods(Map<String,Object> map);

    //订单类型统计
    Map selectOrderType(Map<String,Object> map);

    //昨日新增收入
    Map selectZrIncome(Map<String,Object> map);

    //昨日新增订单
    Map selectZrOrder(Map<String,Object> map);

}
