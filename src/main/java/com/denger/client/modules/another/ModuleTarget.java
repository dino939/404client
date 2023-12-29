package com.denger.client.modules.another;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleTarget {

     String ModName() default "NullName";

     String description() default " ";

     Category category() default Category.MISC;
     int bind() default -1;
     boolean beta() default false;
     boolean enable() default false;
     int cooldown() default 0;
}