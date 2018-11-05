package aq.app.vue.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

public class MyWebSocketListener implements WebSocketListener {
    private Session session;
    @Override
    public void onWebSocketConnect(Session session) {
        System.out.println("onWebSocketConnect->"+session.getRemoteAddress());
        this.session = session;
    }
    //发送String
    @Override
    public void onWebSocketText(String message) {
        System.out.println("onWebSocketText");
        if (session.isOpen()) {
            // echo the message back
            session.getRemote().sendString("echo msg->m"+message , null);
        }
    }
    //发送byte[]
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        System.out.println("onWebSocketBinary");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println("Error->" + cause.getMessage());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("onWebSocketClose");
        this.session = null;
    }

}