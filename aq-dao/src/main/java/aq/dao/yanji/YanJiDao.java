package aq.dao.yanji;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoYanJi")
public interface YanJiDao {

    //添加验机报告
    int insertYanJi(Map<String,Object> map);

    //查询已验机报告
    List<Map<String,Object>> selectAmslerList(Map<String,Object> map);

    //更新验机报告
    int updateYanJi(Map<String,Object> map);

    //删除验机报告
    int deleteYanJi(Map<String,Object> map);

}
