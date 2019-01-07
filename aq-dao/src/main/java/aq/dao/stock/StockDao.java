package aq.dao.stock;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoStock")
public interface StockDao {

    //插入库存
    int insertStock(Map<String,Object> map);

    //更新库存
    int updateStock(Map<String,Object> map);

    //删除库存
    int deleteStock(Map<String,Object> map);

    //删除出库
    int deleteStockOut(Map<String,Object> map);

    //查询库存
    List<Map<String,Object>> selectStock(Map<String,Object> map);
}
