package cn.mingc.mybatis.reflection;

import cn.mingc.mybatis.reflection.invoker.Invoker;
import cn.mingc.mybatis.reflection.invoker.MethodInvoker;
import cn.mingc.mybatis.reflection.property.PropertyNamer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Reflection {

    private static boolean classCacheEnable = true;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final ReflectPermission ACCESS_PERMISSION = new ReflectPermission("suppressAccessChecks");

    private static final Map<Class<?>, Reflection> REFLECTION_MAP = new ConcurrentHashMap<>();

    private Class<?> type;

    private String[] readablePropertyNames = EMPTY_STRING_ARRAY;

    private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;

    private Map<String, Invoker> getMethods = new HashMap<>();

    private Map<String, Invoker> setMethods = new HashMap<>();

    private Map<String, Class<?>> getTypes = new HashMap<>();

    private Map<String, Class<?>> setTypes = new HashMap<>();

    private Constructor<?> defaultConstructor;

    private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

    public Reflection(Class<?> clazz) {
        this.type = clazz;
        // 添加构造方法
        addDefaultConstructor(clazz);
        // 添加get方法
        addGetMethods(clazz);
        // 添加set方法
        addSetMethods(clazz);
        // 添加字段
        addFields(clazz);
        readablePropertyNames = getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
        writeablePropertyNames = setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
        for (String propertyName : readablePropertyNames) {
            caseInsensitivePropertyMap.put(propertyName.toUpperCase(Locale.ENGLISH), propertyName);
        }
        for (String propertyName : writeablePropertyNames) {
            caseInsensitivePropertyMap.put(propertyName.toUpperCase(Locale.ENGLISH), propertyName);
        }
    }

    public Reflection forClass(Class<?> clazz) {
        if (!classCacheEnable) {
            return new Reflection(clazz);
        }
        if (REFLECTION_MAP.containsKey(clazz)) {
            return REFLECTION_MAP.get(clazz);
        }
        synchronized (REFLECTION_MAP) {
            // computeIfAbsent获取key=clazz的value，如果不存在则执行k -> new Reflection(clazz)并将返回值存放的map中并返回这个值
            return REFLECTION_MAP.computeIfAbsent(clazz, k -> new Reflection(clazz));
//            if (REFLECTION_MAP.containsKey(clazz)) {
//                return REFLECTION_MAP.get(clazz);
//            }
//            Reflection reflection = new Reflection(clazz);
//            REFLECTION_MAP.put(clazz, reflection);
//            return reflection;
        }
    }

    private void addFields(Class<?> clazz) {
    }

    private void addGetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String methodName = method.getName();
            if (!PropertyNamer.isGetter(methodName) || method.getParameterCount() != 0) {
                continue;
            }
            String propertyName = PropertyNamer.method2Property(methodName);
            addMethodConflict(conflictingGetters, propertyName, method);
        }
        resolveGetterConflicts(conflictingGetters);
    }

    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        for (String propertyName : conflictingGetters.keySet()) {
            List<Method> getters = conflictingGetters.get(propertyName);
            Iterator<Method> getterIterator = getters.iterator();
            Method getter = getterIterator.next();
            Class<?> returnType = getter.getReturnType();
            while (getterIterator.hasNext()) {
                Method nextGetter = getterIterator.next();
                Class<?> nextReturnType = nextGetter.getReturnType();
                if (returnType.isAssignableFrom(nextReturnType)) {
                    getter = nextGetter;
                    returnType = nextReturnType;
                    continue;
                } else if (nextReturnType.isAssignableFrom(returnType)) {
                    continue;
                }
                throw new RuntimeException("无法从多个getter中确定一个用于" + propertyName);
            }
            addGetMethod(propertyName, getter);
        }
    }

    private void addGetMethod(String name, Method getter) {
        if (isValidPropertyName(name)) {
            getMethods.put(name, new MethodInvoker(getter));
            getTypes.put(name, getter.getReturnType());
        }
    }

    private void addSetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingSetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String methodName = method.getName();
            if (!PropertyNamer.isSetter(methodName) || method.getParameterCount() != 1) {
                continue;
            }
            String propertyName = PropertyNamer.method2Property(methodName);
            addMethodConflict(conflictingSetters, propertyName, method);
        }
    }

    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        // TODO
    }

    private void addSetMethod(String name, Method setter) {
        if (isValidPropertyName(name)) {
            setMethods.put(name, new MethodInvoker(setter));
            setTypes.put(name, setter.getParameterTypes()[0]);
        }
    }

    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        List<Method> methods = conflictingMethods.computeIfAbsent(name, k -> new ArrayList<>());
        methods.add(method);
    }

    private Method[] getClassMethods(Class<?> clazz) {
        Map<String, Method> uniqueMethods = new HashMap<>();
        Class<?> currClass = clazz;
        while (currClass != null) {
            addUniqueMethods(uniqueMethods, currClass.getDeclaredMethods());

            Class<?>[] interfaces = currClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }

            currClass = currClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();
        return methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method method : methods) {
            // 过滤桥接方法
            // 桥接方法可查看泛型擦除相关知识点
            if (method.isBridge()) {
                continue;
            }
            String signature = getSignature(method);
            if (uniqueMethods.containsKey(signature)) {
                continue;
            }
            if (canSetMethodAccessible()) {
                method.setAccessible(true);
            }
            if (method.isAccessible()) {
                uniqueMethods.put(signature, method);
            }
        }
    }

    private String getSignature(Method method) {
        StringBuilder signature = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            signature.append(returnType.getName()).append("#");
        }
        signature.append(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (i == 0) {
                signature.append(":");
            } else {
                signature.append(",");
            }
            signature.append(parameterType.getName());
        }
        return signature.toString();
    }

    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() != 0) {
                continue;
            }
            if (canSetMethodAccessible()) {
                constructor.setAccessible(true);
            }
            if (constructor.isAccessible()) {
                defaultConstructor = constructor;
            }
        }
    }

    public static boolean canSetMethodAccessible() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(ACCESS_PERMISSION);
            } catch (SecurityException e) {
                return false;
            }
        }
        return true;
    }

}
