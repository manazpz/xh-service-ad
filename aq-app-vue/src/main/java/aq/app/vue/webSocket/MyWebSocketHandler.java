package aq.app.vue.webSocket;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class MyWebSocketHandler extends WebSocketHandler
{
    @Override
    public void configure(WebSocketServletFactory factory)
    {
        // 设置超时
        factory.getPolicy().setIdleTimeout(10000);

        // 注册
        factory.register(MyWebSocketListener.class);
    }
}
