package com.tistory.hornslied.evitaonline.commons.util.repeater;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.function.Function;

/**
 * Created by Avis Network on 2017-11-15.
 *
 * A {@link Function} that can throw anything. Call {@link #applyThrows} directly
 * if you want to handle the exceptions, or call {@link #apply} to have them
 * wrapped in a {@link UncheckedExecutionException}.
 * <p>
 * TODO: Catches everything, not just {@link E}.. not ideal
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> extends Function<T, R> {
    
    R applyThrows(T t) throws E;
    
    @SuppressWarnings("deprecation")
	@Override
    default R apply(T t) {
        try {
            return applyThrows(t);
        } catch(Throwable throwable) {
            throw Throwables.propagate(throwable);
        }
    }
}
