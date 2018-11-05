package aq.app.vue.start;

import aq.app.vue.webSocket.MyWebSocketHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class Main {

    private static Main instance;

    public static synchronized Main getInstance(){
        if (instance == null){
            instance = new Main();
        }
        return instance;
    }

    public void start(){
        Hook.getInstance().start(new Thread() {
            @Override
            public void run() {
                //内存清理、资源回收等
                try {

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }finally {
                    System.out.println("shutdown server");
                }
            }
        });
        Jetty.getInstance().setProjectName("aq-app-vue");
        Jetty.getInstance().start();
        System.out.print("Jetty start success!\n");
    }

    public void stop(){
        Hook.getInstance().stop();
    }

    private void init(String[] args) throws  Exception{
        String command = "start";
        if (args.length>1){
            command = args[args.length-1];
        }
        if ("start".equals(command.toLowerCase())){
            start();
        }else if ("stop".equals(command.toLowerCase())){
            stop();
        }
    }
    public static  void main(String[] args){
        try{
            Main.getInstance().init(args);
          //  (new Main()).init(args);
        }
        catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }
}
