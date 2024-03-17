//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\WorkSpace\Minecraft-Deobfuscator\1.16.5 stable mappings"!

//Decompiled by Procyon!

package com.denger.client.another.hooks.forge.even.addevents;

import com.denger.client.Execute;
import com.denger.client.Main;
import com.denger.client.utils.ReflectFileld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.ListenerList;
import net.minecraftforge.eventbus.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventManagerForge {

    Main instance;
    public EventManagerForge(Execute instance){
        register(instance);
    }
    private int busID = (int) new ReflectFileld(MinecraftForge.EVENT_BUS, EventBus.class, 5).getValue();


    public void register(final Object target) {
        if (getListener().containsKey(target)) {
            return;
        }
        registerObject(target);
    }

    private void typesFor(final Class<?> clz, final Set<Class<?>> visited) {
        if (clz.getSuperclass() == null) return;
        typesFor(clz.getSuperclass(), visited);
        Arrays.stream(clz.getInterfaces()).forEach(i -> typesFor(i, visited));
        visited.add(clz);
    }

    private void registerObject(final Object obj) {
        final HashSet<Class<?>> classes = new HashSet<>();
        typesFor(obj.getClass(), classes);
        Arrays.stream(obj.getClass().getMethods()).
                filter(m -> !Modifier.isStatic(m.getModifiers())).
                forEach(m -> classes.stream().
                        map(c -> getDeclMethod(c, m)).
                        filter(rm -> rm.isPresent() && rm.get().isAnnotationPresent(SubscribeEvent.class)).
                        findFirst().
                        ifPresent(rm -> registerListener(obj, m, rm.get())));
    }

    private Optional<Method> getDeclMethod(final Class<?> clz, final Method in) {
        try {
            return Optional.of(clz.getDeclaredMethod(in.getName(), in.getParameterTypes()));
        } catch (NoSuchMethodException nse) {
            return Optional.empty();
        }

    }

    public void unregister(Object object) {
        List<IEventListener> list =getListener().remove(object);
        if (list == null)
            return;
        for (IEventListener listener : list) {
            ListenerList.unregisterAll(busID, listener);
        }
    }


    private void registerListener(final Object target, final Method method, final Method real) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException(
                    "Method " + method + " has @SubscribeEvent annotation. " +
                            "It has " + parameterTypes.length + " arguments, " +
                            "but event handler methods require b.a single argument only."
            );
        }

        Class<?> eventType = parameterTypes[0];

        if (!Event.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(
                    "Method " + method + " has @SubscribeEvent annotation, " +
                            "but takes an argument that is not an Event subtype : " + eventType);
        }
        register(eventType, target, real);
    }

    private boolean isMethodBad(final Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(SubscribeEvent.class);
    }

    private boolean isMethodBad(final Method method, final Class<? extends Event> eventClass) {
        return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
    }

    private void register(Class<?> eventType, Object target, Method method) {
        try {
            final ASMEventHandlerHook asm = new ASMEventHandlerHook(target, method, IGenericEvent.class.isAssignableFrom(eventType));
            addToListeners(target, eventType, asm, asm.getPriority());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException ignored) {
        }
    }
    private void addToListeners(final Object target, final Class<?> eventType, final IEventListener listener, final EventPriority priority) {
        ListenerList listenerList = EventListenerHelper.getListenerList(eventType);
        listenerList.register(busID, priority, listener);
        List<IEventListener> others = getListener().computeIfAbsent(target, k -> Collections.synchronizedList(new ArrayList<>()));
        others.add(listener);
    }
    public ConcurrentHashMap<Object, List<IEventListener>> getListener(){
        return (ConcurrentHashMap<Object, List<IEventListener>>) new ReflectFileld(MinecraftForge.EVENT_BUS, EventBus.class, 4).getValue();
    }
}
