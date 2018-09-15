package com.tistory.hornslied.evitaonline.commons.util.repeater;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Avis Network on 2017-11-15.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repeatable {
    
    Time interval() default @Time(ticks = 1);
}
