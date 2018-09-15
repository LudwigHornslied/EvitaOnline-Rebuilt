package com.tistory.hornslied.evitaonline.commons.util.repeater;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Duration;

/**
 * Created by Avis Network on 2017-11-15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Time {
    
    long ticks() default 0;
    
    long milliseconds() default 0;
    
    long seconds() default 0;
    
    long minutes() default 0;
    
    long hours() default 0;
    
    long days() default 0;
    
    class convertTo {
        public static long milliseconds(Time span) {
            return span.milliseconds() + 50 * (
                    span.ticks() + 20 * (
                            span.seconds() + 60 * (
                                    span.minutes() + 60 * (
                                            span.hours() + 24 * span.days()
                                    )
                            )
                    )
            );
        }
        
        public static Duration duration(Time span) {
            return Duration.ofMillis(milliseconds(span));
        }
    }
    
}

