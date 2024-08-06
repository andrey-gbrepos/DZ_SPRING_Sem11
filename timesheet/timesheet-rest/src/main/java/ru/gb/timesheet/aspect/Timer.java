package ru.gb.timesheet.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Timer {

    boolean enabled() default true;

//    org.slf4j.event.Level level() default Level.DEBUG;
}
