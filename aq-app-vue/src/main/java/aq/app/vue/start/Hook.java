package aq.app.vue.start;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ywb on 2017-05-04.
 * 钩子线程
 */
public class Hook implements Runnable {

    //钩子线程实例
    private static Hook instance;

    public Hook(){

    }

    //安全（锁）单例
    public static synchronized Hook getInstance(){
      if (instance==null){ instance = new Hook();}
      return instance;
    }

    public void start(Thread shutDownHookThread){
       synchronized (this){
           System.out.println("应用程序启动中...");
           if (shutDownHookThread!=null){
               Runtime.getRuntime().addShutdownHook(shutDownHookThread);
           }
           new Thread(getInstance()).start();
       }
    }

    public void stop(){
        synchronized (this){
            try {
                    System.out.print("开始关闭应用程序...\n");
                    Socket socket = new Socket("localhost",55555);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("stop\n".getBytes());
                    outputStream.close();
                    System.out.print("应用程序已停止\n");

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void run() {
        synchronized (this){
            ServerSocket server = null;
            Socket conn = null;
            try {
                Thread.currentThread().setName("Thread-Hook-Server");
                server = new ServerSocket(55555);
                while (true){
                    //监听入站请求 一直阻塞 直至监听到请求
                    conn = server.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String command = bufferedReader.readLine();
                    if ("stop".equalsIgnoreCase(command)||"bye".equalsIgnoreCase(command)||"quit".equalsIgnoreCase(command)){
                        break;
                    }
                    conn.close();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                try {
                    if (conn!=null) conn.close();
                    if (server!=null) server.close();
                    System.exit(0);
                }catch (Exception ex){
                    ex.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }
}
