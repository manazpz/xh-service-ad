package aq.app.vue.spring;

import aq.app.vue.start.Jetty;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Spring 监听事件 ->spring 容器加载完成
 * 应用场景：开启quartz任务调度器
 * Created by ywb on 2018-05-13.
 */
@Component
public class listener implements ApplicationListener<ContextRefreshedEvent>{

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext ctx = contextRefreshedEvent.getApplicationContext();
        if (ctx.getParent() == null){
            System.out.print("spring load completed!\n");
            //这里可以调用startJob方法执行具体的quartz 任务
        }
    }
    //开启Job
    private void startJob(String jobClassName,ApplicationContext context){
        try{
            //通过反射创建Job对象实例 -> 注入Spring容器 -> 执行任务启动的方法
            Class<?> clazz = Class.forName(Jetty.props.get("app.module").toString().trim().concat(".job.").concat(jobClassName));
            Method method = clazz.getMethod("startJob");
            Object jobInstance = clazz.newInstance();
            context.getAutowireCapableBeanFactory().autowireBean(jobInstance);
            context.getAutowireCapableBeanFactory().autowireBeanProperties(jobInstance, AutowireCapableBeanFactory.AUTOWIRE_NO,false);
            method.invoke(jobInstance);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
