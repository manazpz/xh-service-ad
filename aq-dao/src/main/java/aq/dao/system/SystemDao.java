package aq.dao.system;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;
/*
* 系统用户、角色、权限、登录校验等相关
* */
@MapperScan("daoSystem")
public interface SystemDao {

    //获取令牌
    List<Map<String,Object>> selectSysToken(Map<String,Object> map);

    //删除令牌
    int deleteSytToken(Map<String,Object> map);

    //插入令牌
    int insertSytToken(Map<String,Object> map);

    //更新令牌
    int updateSytToken(Map<String,Object> map);

    //登录
    List<Map<String,Object>> selectSysLogin(Map<String,Object> map);

    //获取用户密码
    List<Map<String,Object>> selectPassword(Map<String,Object> map);

    //更新用户信息
    int updateUser(Map<String,Object> map);

    //查询用户基本信息
    List<Map<String,Object>> selectUserInfo(Map<String,Object> map);

    //查询用户拥有的权限
    List<Map<String,Object>> selectSysPermissionUser(Map<String,Object> map);

    //权限查询
    List<Map<String,Object>> selectSysPermissionInfo(Map<String,Object> map);

    //插入附件
    int insertAttachment(Map<String,Object> map);

    //开关操作
    int updateSwitch(Map<String,Object> map);

    //查询开关
    List<Map<String,Object>> selectSwitch(Map<String,Object> map);

    //新增地址信息
    int insertAddress(Map<String,Object> map);

    //查询地址信息
    List<Map<String,Object>> selectAddress(Map<String,Object> map);

    //修改地址信息
    int updateAdress(Map<String,Object> map);

    //删除地址信息
    int deleteAdress(Map<String,Object> map);

    //获取客服信息
    List<Map<String,Object>> selectCustomService(Map<String,Object> map);

    //获取客服信息
    List<Map<String,Object>> selectSms(Map<String,Object> map);

}
