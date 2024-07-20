package me.zowpy.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @project CommandAPI is a property of MTR
 * @date 7/20/2024
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TabCompleter {
    String parentCommand();
    boolean async() default true;
}
