package aq.common.access;

import aq.common.util.DateTime;
import aq.common.util.UUIDUtil;

/**
 * Created by ywb on 2017-02-16.
 * 上下文基类
 */
public  abstract class ContextBase implements IContext {

    //在线用户
    protected AccessUserCollection accessUsers;

    //构造函数
    public ContextBase(){
        this.accessUsers = new AccessUserCollection();
    }

    //取当前登录用户
    public AccessUser user(){
        Ticket ticket = this.ticket();
        if (null == ticket){
            ticket = new Ticket();
        }
        if (this.accessUsers.containsKey(ticket.getSessionId())){
          return (AccessUser)this.accessUsers.get(ticket.getSessionId());
        }else {
            //游客
            String id = UUIDUtil.getUUID(); //UUID可能会重复 ？！不是最理想的方式
            AccessUser user = AccessUser.createGuest(id,id,id,"游客","风一样的男子",new PermissionCollection(),"APP");
            return user;
        }
    }

    //取当前会话Ticket
    public abstract Ticket ticket();

    private void setAccessUsers(AccessUserCollection ac){
        this.accessUsers = ac;
    }

    public AccessUserCollection getAccessUsers() {
        return this.accessUsers;
    }

    //登录
    public void login(AbsAccessUser user){
        if (this.accessUsers.containsKey(user.accessTicket.getSessionId())){
            this.accessUsers.remove(user.accessTicket.getSessionId());
        }
        this.accessUsers.put(user.accessTicket.getSessionId(),user);
    }

    //登出
    public void loginOut(String sessionId){
        this.accessUsers.remove(sessionId);
    }

    //清理过期和无效的用户
    public void removeExpiredUsers(){
       for (String sessionId :this.accessUsers.keySet()){
           if (this.accessUsers.get(sessionId).preAccessTime.isBefore(DateTime.getDefaultLocalDateTime().plusDays(1))){
               this.accessUsers.remove(sessionId);
           }
       }
    }
}

