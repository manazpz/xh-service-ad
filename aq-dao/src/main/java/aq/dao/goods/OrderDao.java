package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoOrder")
public interface OrderDao {

    //查询商品
    List<Map<String,Object>> selectorderList(Map<String,Object> map);

}
