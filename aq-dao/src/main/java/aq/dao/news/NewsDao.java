package aq.dao.news;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoNews")
public interface NewsDao {

    //查询订单
    List<Map<String,Object>> selectorderLogList(Map<String,Object> map);

    //新增订单日志
    int insertOrderLog(Map<String,Object> map);

}
