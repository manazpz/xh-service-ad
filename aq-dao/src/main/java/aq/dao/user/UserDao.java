package aq.dao.user;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoUser")
public interface UserDao {

    //新增用户
    int insertUserInfo(Map<String,Object> map);

    //获取用户信息
    List<Map<String,Object>> selectUserInfo(Map<String,Object> map);

    //更新用户信息
    int updateUserInfo(Map<String,Object> map);

    //删除/恢复用户
    int deleteUser(Map<String,Object> map);

    //权限查询
    List<Map<String,Object>> selectPermissionList(Map<String,Object> map);

    //插入权限
    int insertPermission(Map<String,Object> map);

    //更新权限
    int updatePermission(Map<String,Object> map);

    //权限查询
    List<Map<String,Object>> selectUserPermission(Map<String,Object> map);

    //新增用户权限
    int insertUserPermission(Map<String,Object> map);

    //删除用户权限
    int deleteUserPermission(Map<String,Object> map);

}
