package aq.common.aop;

import aq.common.annotation.DyncDataSource;
import aq.common.datasource.Multiple;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1) //控制package下类的加载顺序
public class DataSource {
    @Before("@annotation(aq.common.annotation.DyncDataSource)") //前置通知
    public void before(JoinPoint point){
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        DyncDataSource dataSourceAnnotation = className.getAnnotation(DyncDataSource.class);
        if (dataSourceAnnotation != null ) {
            //获得访问的方法名
            String methodName = point.getSignature().getName();
            //得到方法的参数的类型
            Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
            String dataSource = Multiple.MASTER;
            try {
                Method method = className.getMethod(methodName, argClass);
                if (method.isAnnotationPresent(DyncDataSource.class)) {
                    DyncDataSource annotation = method.getAnnotation(DyncDataSource.class);
                    dataSource = annotation.dataSource();
                    System.out.println("DataSource Aop ==== Current dataSource is  "+dataSource);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Multiple.setDataSourceKey(dataSource);
        }

    }

    @After("@annotation(aq.common.annotation.DyncDataSource)")   //后置通知
    public void after(JoinPoint point){
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        DyncDataSource dataSourceAnnotation = className.getAnnotation(DyncDataSource.class);
        if (dataSourceAnnotation != null ) {
            //获得访问的方法名
            String methodName = point.getSignature().getName();
            //得到方法的参数的类型
            Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
            String dataSource = Multiple.MASTER;
            try {
                Method method = className.getMethod(methodName, argClass);
                if (method.isAnnotationPresent(DyncDataSource.class)) {
                    DyncDataSource annotation = method.getAnnotation(DyncDataSource.class);
                    dataSource = annotation.dataSource();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(dataSource != null && !Multiple.MASTER.equals(dataSource)){
              Multiple.removeDataSource();
            }
        }
    }
}
