package aq.common.access;

/**
 * Created by 杨大山 on 2017-02-16.
 *  定义标识登录对象的基本功能
 *  身份验证方式、是否已验证、身份名称
 *  仅限同一个包类的对象可以访问
 */
public abstract class AbsIdentity {

    //身份标识
    String id;

    //身份验证方式
    AuthType authenticationType;

    //是否已验证
    boolean isAuthenticated;

    //当前用户名称
    String name ;

    abstract String getId();
    abstract AuthType getAuthenticationType();
    abstract String getName();
    abstract boolean getIsAuthenticated();
}
