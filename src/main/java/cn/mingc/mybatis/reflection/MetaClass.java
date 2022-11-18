package cn.mingc.mybatis.reflection;

import cn.mingc.mybatis.reflection.invoker.Invoker;

public class MetaClass {

    private Reflection reflection;

    private MetaClass(Class<?> clazz) {
        this.reflection = Reflection.forClass(clazz);
    }

    public static MetaClass forClass(Class<?> clazz) {
        return new MetaClass(clazz);
    }

    public static boolean isClassCacheEnabled() {
        return Reflection.isClassCacheEnabled();
    }

    public static void setClassCacheEnabled(boolean classCacheEnabled) {
        Reflection.setClassCacheEnabled(classCacheEnabled);
    }

    public Invoker getGetInvoker(String name) {
        return reflection.getGetInvoker(name);
    }

    public Invoker getSetInvoker(String name) {
        return reflection.getSetInvoker(name);
    }

    public boolean hasGetter(String name) {
        return reflection.hasGetter(name);
    }

    public boolean hasSetter(String name) {
        return reflection.hasSetter(name);
    }

    private MetaClass metaClassForProperty(String name) {
        return null;
    }

}
