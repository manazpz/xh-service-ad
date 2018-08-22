package aq.app.vue.boot;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by ywb on 2017-05-04.
 * 自定义类加载器 用于加载特定目录或文件到类加载器
 */
public class StdLoad extends URLClassLoader{

    /**
     * 使用自定义URL地址和父类加载器的构造方法
     *
     * @param urls 加载路径
     * @param parent 父类加载器
     */
    public StdLoad(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    /**
     * 使用url地址的构造方法
     *
     * @param urls 加载路径
     */
    public StdLoad(URL[] urls) {
        super(urls);
    }
}
