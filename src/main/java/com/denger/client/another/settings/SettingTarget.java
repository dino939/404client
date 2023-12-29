package com.denger.client.another.settings;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface SettingTarget {
    String name() default "NotName";

}
