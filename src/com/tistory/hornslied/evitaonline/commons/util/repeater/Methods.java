package com.tistory.hornslied.evitaonline.commons.util.repeater;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Created by Avis Network on 2017-11-15.
 */
public class Methods {
    
    public static Stream<Method> declaredMethodsInAncestors(Class<?> klass) {
        return Types.ancestors(klass)
                .stream()
                .flatMap(ancestor -> Stream.of(ancestor.getDeclaredMethods()))
                .distinct();
    }
}
