package aq.common.access;

/**
 * Created by ywb on 2017-02-27.
 * 令牌
 */
public class Ticket {

    //Token
    private String token;

    //用户ID
    private String userId;

    //Session ID
    private String sessionId;

    //构造函数（无参）
    public Ticket(){

    }

    //构造函数（有参）
    public Ticket(String token,String userId,String sessionId){
        this.token = token;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public String getToken(){
        return this.token;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getSessionId(){
        return this.sessionId;
    }

}
