package aq.app.vue.webSocket;

import aq.common.util.GsonHelper;
import net.sf.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@WebSocket(maxTextMessageSize = 128 * 1024, maxBinaryMessageSize = 128 * 1024)
public class AnnotatedEchoSocket {

    private Session session;

    private static ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<String, Session>();

    //当前发消息的人员编号
    private String nowUid = "";
    //当前接收消息的人员编号
    private String toUid = "";

    public static AnnotatedEchoSocket webSocketTest;

    @OnWebSocketConnect
    public void onText(Session session)throws  Exception{
        if(session.isOpen()){
            this.session = session;
            nowUid = session.getUpgradeRequest().getParameterMap().get("link").toString();
            webSocketSet.put(session.getUpgradeRequest().getParameterMap().get("link").toString(),session);
            System.out.print("用户："+nowUid+"已登入！");
        }
    }
    @OnWebSocketClose
    public void onWebSocketBinary(int i,String string) {
        if (!nowUid.equals("")) {
            webSocketSet.remove(nowUid);
            System.out.print("用户："+nowUid+"已退出！");
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) throws IOException {
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> to = new HashMap<>();
        Map<String,Object> mine = new HashMap<>();
        JSONObject json = JSONObject.fromObject(msg);
        to = GsonHelper.getInstance().fromJson(GsonHelper.getInstance().toJson(GsonHelper.getInstance().fromJson(json.toString(),Map.class).get("to")),Map.class);
        mine = GsonHelper.getInstance().fromJson(GsonHelper.getInstance().toJson(GsonHelper.getInstance().fromJson(json.toString(),Map.class).get("mine")),Map.class);
        String message = mine.get("content").toString();
        String name = mine.get("username").toString();
        String avatar = mine.get("avatar").toString();
        if(to != null){
            toUid = "["+to.get("id").toString()+"]";
            String fromid = "["+mine.get("id").toString()+"]";
            try {
                Session session = webSocketSet.get(toUid);
                res.put("id",toUid);
                res.put("content",message);
                res.put("username",name);
                res.put("avatar",avatar);
                res.put("type","");
                res.put("mine",false);
                res.put("fromid",fromid);
                res.put("timestamp",new Date().getTime());
                String sendmsg = GsonHelper.getInstance().toJson(res);
                if(session != null){
                    session.getRemote().sendString(sendmsg);
                    System.out.print("=============已发送==============");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println(toUid + "当前用户不在线");
        }
    }
    public void onWebSocketBinary( org.eclipse.jetty.websocket.api.Session session, int a, java.lang.String s){


    }

        /**
          * 获取当前时间
          *
          * @return
          */
    private String getNowTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }


}