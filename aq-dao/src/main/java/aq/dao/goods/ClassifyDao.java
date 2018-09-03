package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoClassify")
public interface ClassifyDao {

    //查询分类
    List<Map<String,Object>> selectClassify(Map<String,Object> map);

    //一级分类
    List<Map<String,Object>> selectGoodsClassify123(Map<String,Object> map);

    //删除规格
    int deleteClassify(Map<String,Object> map);


}
