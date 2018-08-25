package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoSearch")
public interface SearchDao {

    //搜索型号
    List<Map<String,Object>> selectSearchSpec(Map<String,Object> map);

}
