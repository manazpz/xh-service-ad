package aq.common.access;

import aq.common.util.DateTime;
import aq.common.util.StringUtil;

/**
 * Created by 杨大山 on 2017-02-17.
 */
public final class AccessUser  extends AbsAccessUser{

    //构造函数(有参)
    public AccessUser(String id,Ticket ticket,String userName,String displayName,PermissionCollection pc,AuthType authType,String from){
      this.userId = ticket.getUserId();
      if (StringUtil.isEmpty(id)){
            this.id = this.userId;
      }else {
          this.id = id;
      }
      this.accessTicket = ticket;
      this.name = userName;
      this.displayName = displayName;
      this.permissionCollection = pc;
      this.authenticationType = authType;
      this.isAuthenticated = true;
      this.loginTime = this.preAccessTime = DateTime.getDefaultLocalDateTime();
      this.from = from;
    }

    //Ticket
    private void setAccessTicket(Ticket ticket){
        this.accessTicket = ticket;
    }

    //ID
    private void setId(String id){
        this.id = id;
    }

    //用户ID
    private void setUserId(String userId){
        this.userId = userId;
    }

    //用户名
    private void setName(String name){
       this.name = name;
    }

    //用户昵称
    private void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    //权限
    private void setPermissionCollection(PermissionCollection pc){
        this.permissionCollection = pc;
    }

    //登录验证方式
    private void setAuthenticationType(AuthType type){
        this.authenticationType = type;
    }

    //是否已验证
    private void setIsAuthenticated(boolean bool){
        this.isAuthenticated = bool;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Ticket getAccessTicket() {
        return this.accessTicket;
    }

    @Override
    public PermissionCollection getPermissionCollection() {
        return this.permissionCollection;
    }

    @Override
    String getId() {
        return this.id;
    }

    @Override
    AuthType getAuthenticationType() {
        return this.authenticationType;
    }

    @Override
    String getName() {
        return this.name;
    }

    @Override
    boolean getIsAuthenticated() {
        return this.isAuthenticated;
    }

    //创建游客
    public static AccessUser createGuest(String id,String userId,String sessionId,String name,String displayName,PermissionCollection pc,String from){
        Ticket ticket = new Ticket("",sessionId,userId);
        AccessUser au = new AccessUser(id,ticket,name,displayName,pc,AuthType.TOURISTS,from);
        au.isAuthenticated = false;
        return  au;
    }
}
