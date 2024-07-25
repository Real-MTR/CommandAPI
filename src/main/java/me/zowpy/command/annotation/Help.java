package me.zowpy.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @project CommandAPI is a property of MTR
 * @date 7/26/2024
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Help {
    boolean playersOnly() default false;
    boolean consolesOnly() default false;
}