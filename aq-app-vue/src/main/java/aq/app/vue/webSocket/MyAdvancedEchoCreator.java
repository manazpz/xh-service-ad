package aq.app.vue.webSocket;

import aq.app.vue.webSocket.AnnotatedEchoSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;

public class MyAdvancedEchoCreator  implements org.eclipse.jetty.websocket.servlet.WebSocketCreator{


    AnnotatedEchoSocket annotatedEchoSocket;

    public MyAdvancedEchoCreator() {
        annotatedEchoSocket = new AnnotatedEchoSocket();

    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        for (String sub : req.getSubProtocols()) {
            /**
             *   官方的Demo，这里可以根据相应的参数做判断，使用什么样的websocket
             */
        }

        // 没有有效的请求，忽略它
        return annotatedEchoSocket;

    }
}