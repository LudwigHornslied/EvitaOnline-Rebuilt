package com.tistory.hornslied.evitaonline.commons.util.repeater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Avis Network on 2017-11-15.
 */
public class Types {
    
    /**
     * Get all ancestor types of the given type, both classes and interfaces,
     * in breadth-first order. The superclass of each class is traversed before
     * its interfaces.
     *
     * @param allowDuplicates If true, duplicate interfaces will appear in the result wherever
     *                        they occur in the ancestry graph. If false, all but the
     *                        first occurance of each interface will be omitted from the result.
     *                        This makes the operation somewhat more expensive, so duplicates
     *                        should be allowed if possible.
     */
    @SuppressWarnings("unchecked")
	public static <T> Collection<Class<? super T>> ancestors(Class<T> type, boolean allowDuplicates) {
        List<Class<? super T>> list = new ArrayList<>();
        list.add(type);
        for(int i = 0; i < list.size(); i++) {
            final Class<?> t = list.get(i);
            if(t.getSuperclass() != null) list.add((Class<? super T>) t.getSuperclass());
            final Class<? super T>[] interfaces = (Class<? super T>[]) t.getInterfaces();
            if(allowDuplicates) {
                Collections.addAll(list, interfaces);
            } else {
                for(Class<? super T> iface : interfaces) {
                    if(!list.contains(iface)) list.add(iface);
                }
            }
        }
        return list;
    }
    
    /**
     * Equivalent to {@link #ancestors(Class, boolean)} with duplicate results allowed.
     */
    public static <T> Collection<Class<? super T>> ancestors(Class<T> type) {
        return ancestors(type, true);
    }
}
