package aq.app.vue.boot;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by ywb on 2017-05-04.
 * 引导启动对象 初始化系统目录以及类加载器
 */
public final class Boot {

    //引导启动对象
    private static Boot instance;


    private String projectName;

    public static synchronized Boot getInstance(){
        if (instance==null){
            instance = new Boot();
        }
        return instance;
    }

    private void setProjectName(String name){
        getInstance().projectName = name;
    }

    //定义类加载器
    private ClassLoader classLoader = null;

    //定义后台对象
    private Object objInstance = null;

    //私有构造方法
    private Boot(){}
    /**
     * 初始化系统,设置系统目录,创建系统类加载器以及实例化启动类
     *
     * @throws Exception 初始化异常
     */
    public void init() throws Exception {

        initSysConstruct();         // 初始化系统结构
        initSysClassLoaders();      // 初始化类加载器

        // 设置当前主线程类加载器
        Thread.currentThread().setContextClassLoader(classLoader);
        System.out.print("设置类加载器\n");
        Class startupClass = classLoader.loadClass("aq.app.vue.start.Main");
        System.out.print("加载实例化启动类\n");
       // Object startupInstance = startupClass.newInstance();
        Method method = startupClass.getMethod("getInstance",(Class [] )null);
        System.out.print("实例化启动类\n");
        objInstance = method.invoke(startupClass,(Class [] )null);
       //objInstance = startupInstance;
    }

    private void initSysConstruct() throws Exception {
        setScheduleHome();
        setScheduleConf();
        setScheduleLibs();
        setScheduleTemp();
    }

    private void setScheduleHome() throws Exception {
        String scheduleHome = System.getProperty("SCHEDULE.HOME");

        if (scheduleHome != null && !"".equals(scheduleHome))
            return;
        File bootstrapJar = new File(System.getProperty("user.dir"), "run.jar");
        if (bootstrapJar.exists())
            System.setProperty("SCHEDULE.HOME", (new File(System.getProperty("user.dir"), "..")).getCanonicalPath());
        else
            System.setProperty("SCHEDULE.HOME", System.getProperty("user.dir"));

    }

    private void setScheduleConf() {
        String scheduleConf = System.getProperty("SCHEDULE.CONF");
        if (scheduleConf != null && !"".equals(scheduleConf))
            return;
        System.setProperty("SCHEDULE.CONF", System.getProperty("SCHEDULE.HOME") + "/conf");
    }

    private void setScheduleLibs() {
        String scheduleLibs = System.getProperty("SCHEDULE.LIBS");
        if (scheduleLibs != null && !"".equals(scheduleLibs))
            return;
        System.setProperty("SCHEDULE.LIBS", System.getProperty("SCHEDULE.HOME") + "/libs");
    }

    private void setScheduleTemp() {
        String scheduleTemp = System.getProperty("SCHEDULE.TEMP");
        if (scheduleTemp == null || "".equals(scheduleTemp)) {
            scheduleTemp = System.getProperty("SCHEDULE.HOME") + "/temp";
        }
        System.setProperty("java.io.tmpdir", scheduleTemp);
    }

    private void initSysClassLoaders() {
        try {
            Set<URL> set = new LinkedHashSet<URL>();
            String scheduleConf = System.getProperty("SCHEDULE.CONF");
            String scheduleLibs = System.getProperty("SCHEDULE.LIBS");

            File conf = new File(scheduleConf);
            if (!conf.exists())
                throw new ExceptionInInitializerError("system can not be found [conf] directory");
            conf = conf.getCanonicalFile();
            set.add(conf.toURI().toURL());

            File libs = new File(scheduleLibs);
            if (!libs.exists())
                throw new ExceptionInInitializerError("system can not be found [libs] directory");
            libs = libs.getCanonicalFile();
            for (String fileName : libs.list()) {
                if (!fileName.endsWith(".jar")) continue;
                File lib = new File(libs, fileName);
                lib = lib.getCanonicalFile();
                set.add(lib.toURI().toURL());
            }

            URL[] array = set.toArray(new URL[set.size()]);
            classLoader = new StdLoad(array);
            if( classLoader == null ) {
                classLoader = this.getClass().getClassLoader();
            }
        } catch (Exception t) {
            throw new ExceptionInInitializerError("initial system classloader failure");
        }
    }

    /**
     * 启动后台程序
     *
     * @throws Exception 反射异常
     */
    public void start() throws Exception {
        if (objInstance == null) init();
        Method method = objInstance.getClass().getMethod("start", (Class [] )null);
        method.invoke(objInstance, (Object [])null);
    }

    /**
     * 停止后台程序
     *
     * @throws Exception 反射异常
     */
    public void stop() throws Exception {
        Method method = objInstance.getClass().getMethod("stop", (Class [] ) null);
        method.invoke(objInstance, (Object [] ) null);

    }

    /**
     * Main 方法入口
     *
     * @param args 命令行参数被处理
     */
    public static void main(String[] args) {

        try {
            //初始化 加载目录结构、类等
            Boot.getInstance().setProjectName("aq-app-");
            Boot.getInstance().init();
            String command = "start";
            if (args.length > 0) {
                command = args[args.length - 1];
            }

            if ("start".equals(command)) {
                Boot.getInstance().start();
            } else if ("stop".equals(command)) {
                Boot.getInstance().stop();
            } else {
                System.err.println("the command [" + command + "] can not be found.");
            }
        } catch (Throwable th) {
            th.printStackTrace();
            System.exit(1);
        }
    }
}
