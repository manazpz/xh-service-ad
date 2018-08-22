package aq.common.access;

import java.time.LocalDateTime;

public abstract class AbsAccessUser extends AbsIdentity {

    //用户ID
    String userId;

    //昵称
    String displayName;

    //用户携带的令牌
    Ticket accessTicket;

    //登录时间
    LocalDateTime loginTime;

    //上次访问时间
    LocalDateTime preAccessTime;

    //用户来源 （登录入口）
    String from;

    //权限
    PermissionCollection permissionCollection ;

    public abstract String getUserId();
    public abstract String getDisplayName();
    public abstract Ticket getAccessTicket();
    public abstract PermissionCollection getPermissionCollection();

}
