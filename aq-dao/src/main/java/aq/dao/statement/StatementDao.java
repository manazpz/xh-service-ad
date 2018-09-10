package aq.dao.statement;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoStatement")
public interface StatementDao {

    //新增声明
    int insertStatement(Map<String,Object> map);

    //更新声明
    int updateStatement(Map<String,Object> map);

    //获取声明列表
    List<Map<String,Object>> selectStatement(Map<String,Object> map);

    //删除声明列表
    int deleteStatement(Map<String,Object> map);
}
