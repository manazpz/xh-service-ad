package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoSpec")
public interface SpecDao {

    //查询规格表
    List<Map<String,Object>> selectSpec(Map<String,Object> map);

}
