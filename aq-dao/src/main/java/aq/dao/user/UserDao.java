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

    //回收方式查询
    List<Map<String,Object>> selectRecoveryList(Map<String,Object> map);

    //新增回收方式
    int insertRecovery(Map<String,Object> map);

    //编辑回收方式
    int updateRecovery(Map<String,Object> map);

    //新增默认回收方式
    int insertrecoveryListUser(Map<String,Object> map);

    //查询默认回收方式
    List<Map<String,Object>> selectRecoveryUserList(Map<String,Object> map);

    //更新默认回收方式
    int updaterecoveryListUser(Map<String,Object> map);

    //新增用户授权信息
    int insertUsers(Map<String,Object> map);

    //获取微信授权用户信息
    List<Map<String,Object>> selectUserInfos(Map<String,Object> map);


}
