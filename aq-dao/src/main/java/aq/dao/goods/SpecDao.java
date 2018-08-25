package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoSpec")
public interface SpecDao {

    //查询规格视图
    List<Map<String,Object>> selectGoodsSpec(Map<String,Object> map);

    //查询规格主数据
    List<Map<String,Object>> selectSpecH(Map<String,Object> map);

    //查询规格主数据明细
    List<Map<String,Object>> selectSpecP(Map<String,Object> map);

}
