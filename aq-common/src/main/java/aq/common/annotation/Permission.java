package aq.common.annotation;

import aq.common.access.PermissionType;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    //是否验证登录
    boolean RequireLogin() default  true;

    //权限类型
    PermissionType PermissionType() default PermissionType.ACTION;

    //权限值
    String[]   value() default  {};

    //权限描述
    String[]  name() default {};

    //权限等级
    String[] level() default {};
}
