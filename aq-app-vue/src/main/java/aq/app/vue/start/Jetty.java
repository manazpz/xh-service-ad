package aq.app.vue.start;


import aq.app.vue.webSocket.WebSocketHandlerTest;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.util.Properties;

public class Jetty {

    public static final Properties props = new Properties();

    private static Jetty instance ;

    public String projectName;

    public Jetty(){
       try {
           props.load(this.getClass().getClassLoader().getResourceAsStream("server.properties"));
           System.out.print("加载server.properties配置文件...\n");
       }catch (IOException ex){
           throw new ExceptionInInitializerError("加载server.properties配置文件异常!!!");
       }
    }

    public void setProjectName(String name){
        if (instance==null) return;
        instance.projectName = name;
    }

    public static synchronized Jetty getInstance(){
        if (instance==null){
            instance = new Jetty();
        }
        return instance;
    }

    //加载Jetty容器
    public void start(){
        QueuedThreadPool threadPool = getThreadPool();
        Server server = new Server(threadPool);
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);
        ServerConnector http = getServerConnector(server);
        server.addConnector(http);


        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setResourceBase(getScheduleHome() + "/web");
        root.setDescriptor(getScheduleHome() + "/web/WEB-INF/web.xml");
        root.setParentLoaderPriority(true);

//        MyWebSocketHandler myWebSocketHandler = new MyWebSocketHandler();
        WebSocketHandlerTest myWebSocketHandler = new WebSocketHandlerTest();
        root.setHandler(myWebSocketHandler);

        server.setHandler(root);
        authorizeConfig(server);
        requestLogConfig(server);
        try {
            server.join();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private QueuedThreadPool getThreadPool() throws NumberFormatException{
        int min = Integer.parseInt(props.getProperty("server.threads.min"));
        int max = Integer.parseInt(props.getProperty("server.threads.max"));
        int idleTimeout = Integer.parseInt(props.getProperty("server.threads.idleTimeout"));
        int stopTimeout = Integer.parseInt(props.getProperty("server.threads.stopTimeout"));
        boolean detailedDump = Boolean.parseBoolean(props.getProperty("server.threads.enable.detailedDump"));

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("HTTP-SERVER-THREAD");
        threadPool.setMinThreads(min);
        threadPool.setMaxThreads(max);
        threadPool.setIdleTimeout(idleTimeout);
        threadPool.setStopTimeout(stopTimeout);
        threadPool.setDetailedDump(detailedDump);
        return threadPool;
    }

    //server
    private ServerConnector getServerConnector(Server server){
        int port = Integer.parseInt(props.getProperty("server.http.port"));
        int idleTimeout = Integer.parseInt(props.getProperty("server.http.idleTimeout"));

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);
        http_config.setRequestHeaderSize(8192);
        http_config.setResponseHeaderSize(8192);
        http_config.setSendServerVersion(true);
        http_config.setSendDateHeader(false);
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(port);
        http.setIdleTimeout(idleTimeout);
        return http;
    }

    //request log
    public void requestLogConfig(Server server) {
        Boolean enableRequestLog = false;
        String flag = System.getProperty("enableRequestLog");
        if (System.getProperty("enableRequestLog") == null) {
            enableRequestLog = Boolean.parseBoolean(props.getProperty("server.enable.requestLog"));
        } else {
            enableRequestLog = Boolean.parseBoolean(flag);
        }

        if (enableRequestLog) {
            NCSARequestLog requestLog = new NCSARequestLog(getScheduleHome() + "/logs/request_yyyy_mm_dd.log");
            requestLog.setAppend(true);
            requestLog.setExtended(false);
            requestLog.setLogTimeZone("GMT");
            requestLog.setLogLatency(true);
        }
    }

    //获取工程主目录
    private String getScheduleHome() {
        String scheduleHome = System.getProperty("SCHEDULE.HOME");

        if (scheduleHome == null || "".equals(scheduleHome)) {

            scheduleHome = System.getProperty("user.dir") + "/"+getInstance().projectName+"/src/main";

            System.setProperty("SCHEDULE.HOME", scheduleHome);
        }
        return scheduleHome;
    }

    //Jetty 认证
    private void authorizeConfig(Server server) {
        HashLoginService loginService = new HashLoginService("ZHONGCHUANG");
        String secretFile = System.getProperty("secretFile");
        secretFile = secretFile == null ? getScheduleHome() + "/resources/authorize.properties" : secretFile;
        loginService.setConfig(secretFile);
        server.addBean(loginService);
    }
}
