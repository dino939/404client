package com.denger.client.another.hooks.forge.even.addevents;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.objectweb.asm.Opcodes.*;

public class ASMEventHandlerHook implements IEventListener {
    private static final AtomicInteger IDs = new AtomicInteger();
    private static final String HANDLER_DESC = Type.getInternalName(IEventListener.class);
    private static final String HANDLER_FUNC_DESC = Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Event.class));
    private static final ASMEventHandlerHook.ASMClassLoader LOADER = new ASMEventHandlerHook.ASMClassLoader();
    private static final HashMap<Method, Class<?>> cache = new HashMap<>();

    private final IEventListener handler;
    private final SubscribeEvent subInfo;
    private String readable;
    private java.lang.reflect.Type filter = null;

    public ASMEventHandlerHook(Object target, Method method, boolean isGeneric) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (Modifier.isStatic(method.getModifiers()))
            handler = (IEventListener) createWrapper(method).newInstance();
        else
            handler = (IEventListener) createWrapper(method).getConstructor(Object.class).newInstance(target);
        subInfo = method.getAnnotation(SubscribeEvent.class);
        readable = "";

        if (isGeneric) {
            java.lang.reflect.Type type = method.getGenericParameterTypes()[0];
            if (type instanceof ParameterizedType) {
                filter = ((ParameterizedType) type).getActualTypeArguments()[0];
                if (filter instanceof ParameterizedType) // Unlikely that nested generics will ever be relevant for event filtering, so discard them
                {
                    filter = ((ParameterizedType) filter).getRawType();
                } else if (filter instanceof WildcardType) {
                    // If there's b.a wildcard filter of Object.class, then remove the filter.
                    final WildcardType wfilter = (WildcardType) filter;
                    if (wfilter.getUpperBounds().length == 1 && wfilter.getUpperBounds()[0] == Object.class && wfilter.getLowerBounds().length == 0) {
                        filter = null;
                    }
                }
            }
        }
    }
    Map<Class<? extends Event>,Event> eventMap = new HashMap<>();
    @SuppressWarnings("rawtypes")
    @Override
    public void invoke(Event event) {
        if (handler != null) {
            if (!event.isCancelable() || !event.isCanceled() || subInfo.receiveCanceled()) {
                if (filter == null || filter == ((IGenericEvent) event).getGenericType()) {
                    try {
                        handler.invoke(event);
                    } catch (Exception ignore) {
                    }

                }
            }
        }
    }

    public EventPriority getPriority() {
        return subInfo.priority();
    }

    public Class<?> createWrapper(Method callback) {
        if (cache.containsKey(callback)) {
            return cache.get(callback);
        }

        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        boolean isStatic = Modifier.isStatic(callback.getModifiers());
        String name = getUniqueName(callback);
        String desc = name.replace('.', '/');
        String instType = Type.getInternalName(callback.getDeclaringClass());
        String eventType = Type.getInternalName(callback.getParameterTypes()[0]);

        /*
        System.out.println("Name:     " + name);
        System.out.println("Desc:     " + desc);
        System.out.println("InstType: " + instType);
        System.out.println("Callback: " + callback.getName() + Type.getMethodDescriptor(callback));
        System.out.println("Event:    " + eventType);
        */

        cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER, desc, null, "java/lang/Object", new String[]{HANDLER_DESC});

        cw.visitSource(".dynamic", null);
        {
            if (!isStatic)
                cw.visitField(ACC_PUBLIC, "instance", "Ljava/lang/Object;", null, null).visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", isStatic ? "()V" : "(Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            if (!isStatic) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitFieldInsn(PUTFIELD, desc, "instance", "Ljava/lang/Object;");
            }
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "invoke", HANDLER_FUNC_DESC, null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            if (!isStatic) {
                mv.visitFieldInsn(GETFIELD, desc, "instance", "Ljava/lang/Object;");
                mv.visitTypeInsn(CHECKCAST, instType);
            }
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, eventType);
            mv.visitMethodInsn(isStatic ? INVOKESTATIC : INVOKEVIRTUAL, instType, callback.getName(), Type.getMethodDescriptor(callback), false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();
        Class<?> ret = LOADER.define(name, cw.toByteArray());
        cache.put(callback, ret);
        return ret;
    }

    private String getUniqueName(Method callback) {
        return String.format("%s_%d_%s_%s_%s", getClass().getName(), IDs.getAndIncrement(),
                callback.getDeclaringClass().getSimpleName(),
                callback.getName(),
                callback.getParameterTypes()[0].getSimpleName());
    }

    private static class ASMClassLoader extends ClassLoader {
        private ASMClassLoader() {
            super(null);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            return Class.forName(name, resolve, Thread.currentThread().getContextClassLoader());
        }

        Class<?> define(String name, byte[] data) {
            return defineClass(name, data, 0, data.length);
        }
    }

    public String toString() {
        return readable;
    }
}
