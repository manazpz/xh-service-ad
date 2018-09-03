package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoSpec")
public interface SpecDao {

    //查询规格表
    List<Map<String,Object>> selectSpec(Map<String,Object> map);

    //添加规格
    int insertSpec(Map<String,Object> map);

    //更新规格
    int updateSpec(Map<String,Object> map);

    //删除规格
    int deleteSpec(Map<String,Object> map);


}
