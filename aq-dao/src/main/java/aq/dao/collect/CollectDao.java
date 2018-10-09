package aq.dao.collect;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoCollect")
public interface CollectDao {

    //添加收藏
    int insertCollect(Map<String,Object> map);

    //取消收藏
    int deleteCollect(Map<String,Object> map);

    //查询收藏
    List<Map<String,Object>> selectCollect(Map<String,Object> map);

}
