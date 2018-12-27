package com.vcyber.baselibrary.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    public String permissionName() default "";
    public int id() default  0;
    public String[] permissionNames() default {};
}
