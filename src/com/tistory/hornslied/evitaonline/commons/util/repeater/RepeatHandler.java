package com.tistory.hornslied.evitaonline.commons.util.repeater;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

import com.google.common.cache.LoadingCache;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Avis Network on 2017-06-09.
 */
public class RepeatHandler {
    
    public static RepeatHandler Instance;
    
    private final JavaPlugin plugin;
    private final Map<Object, Set<BukkitTask>> tasksByInstance = new IdentityHashMap<>();
    
    public RepeatHandler(JavaPlugin plugin) {
        Instance = this;
        this.plugin = plugin;
    }
    
    private static class RepeatableMethod {
        final MethodHandle handle;
        
        @SuppressWarnings("unused")
		final Duration delay;
        final Duration interval;
        
        RepeatableMethod(MethodHandle handle, Duration delay, Duration interval) {
            this.handle = handle;
            this.delay = delay;
            this.interval = interval;
        }
    }
    
    public void unregisterRepeatables(final Object object) {
        final Set<BukkitTask> set = tasksByInstance.remove(object);
        if(set != null) {
            set.forEach(BukkitTask::cancel);
        }
    }
    
    public void registerRepeatables(final Object object) {
        tasksByInstance.computeIfAbsent(object, o -> REPEATABLE_METHODS_BY_CLASS
            .getUnchecked(object.getClass())
            .stream()
            .map(repeatable -> {
                MethodHandle handle = repeatable.handle.bindTo(object);
                return Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handle.invokeExact();
                        } catch(Throwable throwable) {
                            // ignored
                        }
                    }
                }, 0, repeatable.interval.toMillis() / 50L);
            }).collect(Collectors.toImmutableSet()));
    }
    
    private static final LoadingCache<Class<?>, ImmutableSet<RepeatableMethod>> REPEATABLE_METHODS_BY_CLASS = CacheUtils.newCache(cls -> {
        final ImmutableSet.Builder<RepeatableMethod> methods = ImmutableSet.builder();
        
        Methods.declaredMethodsInAncestors(cls).forEach(method -> {
            final Repeatable annotation = method.getAnnotation(Repeatable.class);
            if(annotation != null) {
                method.setAccessible(true);
                
                final Class<?>[] parameters = method.getParameterTypes();
                if(!(parameters.length == 0 || parameters.length == 1)) {
                    throw new IllegalArgumentException(method.getName() + " does not have compatible parameter types (" + Joiner.on(",").join(parameters) + ")");
                }
                
                try {
                    methods.add(new RepeatableMethod(
                            MethodHandles.publicLookup().unreflect(method),
                            Duration.ZERO, Time.convertTo.duration(annotation.interval())
                    ));
                } catch(IllegalAccessException e) {
                    throw new IllegalStateException("Failed to get handle for repeatable method " + method, e);
                }
            }
        });
        return methods.build();
    });
    
}
