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

    //更新用户授权信息
    int updateUserInfos(Map<String,Object> map);

    //新增用户银行信息
    int insertUsersBank(Map<String,Object> map);

    //获取用户银行信息
    List<Map<String,Object>> selectUserBank(Map<String,Object> map);

    //更新用户银行信息
    int updateUserBank(Map<String,Object> map);

    //新增用户证件信息
    int insertUsersPaper(Map<String,Object> map);

    //获取用户证件信息
    List<Map<String,Object>> selectUsersPaper(Map<String,Object> map);

    //更新用户证件信息
    int updateUsersPaper(Map<String,Object> map);

    //新增用户反馈
    int insertSuggestion(Map<String,Object> map);

    //查询用户反馈信息
    List<Map<String,Object>> selectSuggestion(Map<String,Object> map);

    //新增角色
    int insertRole(Map<String,Object> map);

    //更新角色
    int updateRole(Map<String,Object> map);

    //角色查询
    List<Map<String,Object>> selectRoleList(Map<String,Object> map);

    //用户角色查询
    List selectUserRoleList(Map<String,Object> map);

    //角色权限查询
    List<Map<String,Object>> selectRolePermission(Map<String,Object> map);

    //新增角色权限
    int insertRolePermission(Map<String,Object> map);

    //删除角色权限
    int deleteRolePermission(Map<String,Object> map);

    //删除角色权限
    int deleteUserRole(Map<String,Object> map);

    //用户角色查询
    List<Map<String,Object>> selectUserRole(Map<String,Object> map);

    //查询用户角色（特殊）
    List selectUserRoleFlag(Map<String,Object> map);

    //新增用户角色
    int insertUserRole(Map<String,Object> map);

}
