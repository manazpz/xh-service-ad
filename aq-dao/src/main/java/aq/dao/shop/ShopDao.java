package aq.dao.shop;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoShop")
public interface ShopDao {

    //添加店铺
    int insertShop(Map<String,Object> map);

    //查询店铺
    List<Map<String,Object>> selectShop(Map<String,Object> map);

    //更新店铺
    int updateShop(Map<String,Object> map);

    //删除店铺
    int deleteShop(Map<String,Object> map);

    //添加账单
    int insertSettlement(Map<String,Object> map);

    //查询账单
    List<Map<String,Object>> selectSettlement(Map<String,Object> map);

    //更新账单
    int updateSettlement(Map<String,Object> map);

}
