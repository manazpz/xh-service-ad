package aq.dao.config;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;
/*
 * 配置表增删盖茶
 * */
@MapperScan("daoConfig")
public interface ConfigDao {

    //查询配置表
    List<Map<String,Object>> selectConfig(Map<String,Object> map);

    //新增配置表
    int insertConfig(Map<String,Object> map);

    //更新配置表
    int updateConfig(Map<String,Object> map);

    //删除配置表
    int deleteConfig(Map<String,Object> map);

    //查询第三方配置表
    List<Map<String,Object>> selectTppConfig(Map<String,Object> map);

    //编辑第三方配置表
    int updateTppConfig(Map<String,Object> map);

}
