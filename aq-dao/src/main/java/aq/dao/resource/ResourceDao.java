package aq.dao.resource;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoResource")
public interface ResourceDao {

    //查询附件
    List<Map<String,Object>> selectResource(Map<String,Object> map);

}
