package aq.common.annotation;

import aq.common.datasource.Multiple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* 自定义注解的参数 用于标注Dao层方法访问的数据源
* */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DyncDataSource {

    //注解参数
    String dataSource() default Multiple.MASTER;
}
