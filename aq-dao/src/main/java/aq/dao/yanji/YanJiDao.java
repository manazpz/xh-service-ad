package aq.dao.yanji;

import org.mybatis.spring.annotation.MapperScan;

import java.util.Map;

@MapperScan("daoYanJi")
public interface YanJiDao {

    //添加验机报告
    int insertYanJi(Map<String,Object> map);

}
