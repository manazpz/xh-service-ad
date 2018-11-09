package aq.app.vue.webSocket;

import aq.app.vue.webSocket.MyAdvancedEchoCreator;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebSocketHandlerTest extends WebSocketHandler {
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(10L * 60L * 1000L);
        webSocketServletFactory.getPolicy().setAsyncWriteTimeout(10L * 1000L);
        /* 设置自定义的WebSocket组合 */
        webSocketServletFactory.setCreator(new MyAdvancedEchoCreator());

    }
}
