package cn.mingc.mybatis.reflection.wrapper;

import cn.mingc.mybatis.reflection.MetaClass;
import cn.mingc.mybatis.reflection.invoker.Invoker;

public class BeanWrapper extends BaseWrapper {

    private MetaClass metaClass;

    private Object bean;

    public BeanWrapper(Object bean) {
        this.bean = bean;
        this.metaClass = MetaClass.forClass(bean.getClass());
    }

    @Override
    public Object get(String name) {
        try {
            if (metaClass.hasGetter(name)) {
                Invoker getter = metaClass.getGetInvoker(name);
                return getter.invoke(bean, null);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        throw new NullPointerException("属性没有提供getter: " + bean.getClass().getName() + "." + name);
    }

    @Override
    public void set(String name, Object value) {
        try {
            if (metaClass.hasSetter(name)) {
                Invoker setter = metaClass.getSetInvoker(name);
                setter.invoke(bean, new Object[] { value });
                return;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        throw new NullPointerException("属性没有提供setter: " + bean.getClass().getName() + "." + name);
    }
}
