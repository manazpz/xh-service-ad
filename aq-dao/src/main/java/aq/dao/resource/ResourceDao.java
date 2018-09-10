package aq.dao.resource;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoResource")
public interface ResourceDao {

    //插入附件
    int insertResourcet(Map<String,Object> map);

    //更新附件
    int updateResource(Map<String,Object> map);

    //查询附件
    List<Map<String,Object>> selectResource(Map<String,Object> map);

    //删除附件
    int deleteResource(Map<String,Object> map);

}
