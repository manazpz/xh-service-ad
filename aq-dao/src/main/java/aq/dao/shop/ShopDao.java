package aq.dao.shop;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoShop")
public interface ShopDao {

    //查询店铺
    List<Map<String,Object>> selectShop(Map<String,Object> map);

}
