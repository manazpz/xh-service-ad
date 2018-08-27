package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoGoods")
public interface GoodsDao {

    //查询商品
    List<Map<String,Object>> selectGoods(Map<String,Object> map);

    //查询品牌
    List<Map<String,Object>> selectBrand(Map<String,Object> map);


}
