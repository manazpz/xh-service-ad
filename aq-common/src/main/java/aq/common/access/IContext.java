package aq.common.access;

/**
 * Created by ywb on 2017-02-16.
 *上下文对象
 * 属性：当前访问者、Ticket（令牌）
 */
public interface IContext {

    //当前访问用户
    AbsAccessUser user();

    //Ticket
    Ticket ticket();

    //登录
    void login(AbsAccessUser user);

    //登出
    void loginOut(String sessionId);

    //清理缓存中过期和无效的用户
    void removeExpiredUsers();

}
