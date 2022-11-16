package cn.mingc.mybatis.reflection.invoker;

import java.lang.reflect.Method;

public class MethodInvoker implements Invoker {

    private Method method;
    private Class<?> type;

    public MethodInvoker(Method method) {
        this(method, null);
    }

    public MethodInvoker(Method method, Class<?> type) {
        this.type = type;
        this.method = method;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws ReflectiveOperationException {
        return method.invoke(target, args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
