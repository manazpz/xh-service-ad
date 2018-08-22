package aq.common.access;

/**
 * Created by ywb on 2017-02-16.
 * 上下文工厂
 */
public class Factory {

    //获取当前上下文对象
    public static  IContext getContext(){
        return Context.current();
    }

}
